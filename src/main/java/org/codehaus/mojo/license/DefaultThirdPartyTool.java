/**
 * Copyright 2010-2013 The Kuali Foundation
 *
 * Licensed under the Educational Community License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.opensource.org/licenses/ecl2.php
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.codehaus.mojo.license;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.SortedMap;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.maven.artifact.Artifact;
import org.apache.maven.artifact.factory.ArtifactFactory;
import org.apache.maven.artifact.repository.ArtifactRepository;
import org.apache.maven.artifact.resolver.ArtifactNotFoundException;
import org.apache.maven.artifact.resolver.ArtifactResolutionException;
import org.apache.maven.artifact.resolver.ArtifactResolver;
import org.apache.maven.model.License;
import org.apache.maven.project.MavenProject;
import org.apache.maven.project.MavenProjectBuilder;
import org.apache.maven.project.MavenProjectHelper;
import org.codehaus.mojo.license.model.LicenseMap;
import org.codehaus.plexus.logging.AbstractLogEnabled;
import org.codehaus.plexus.logging.Logger;

/**
 * Default implementation of the third party tool.
 *
 * @author <a href="mailto:tchemit@codelutin.com">Tony Chemit</a>
 * @version $Id: DefaultThirdPartyTool.java 14410 2011-08-10 20:54:51Z tchemit $
 * @plexus.component role="org.codehaus.mojo.license.ThirdPartyTool" role-hint="default"
 */
public class DefaultThirdPartyTool extends AbstractLogEnabled implements ThirdPartyTool {

    public static final String DESCRIPTOR_CLASSIFIER = "third-party";

    public static final String DESCRIPTOR_TYPE = "properties";

    // ----------------------------------------------------------------------
    // Components
    // ----------------------------------------------------------------------

    /**
     * The component that is used to resolve additional artifacts required.
     *
     * @plexus.requirement
     */
    private ArtifactResolver artifactResolver;

    /**
     * The component used for creating artifact instances.
     *
     * @plexus.requirement
     */
    private ArtifactFactory artifactFactory;

    /**
     * Project builder.
     *
     * @plexus.requirement
     */
    private MavenProjectBuilder mavenProjectBuilder;

    /**
     * Maven ProjectHelper.
     *
     * @plexus.requirement
     */
    private MavenProjectHelper projectHelper;

    /**
     * Maven project comparator.
     */
    private final Comparator<MavenProject> projectComparator = MojoHelper.newMavenProjectComparator();

    /**
     * {@inheritDoc}
     */
    @Override
    public void attachThirdPartyDescriptor(MavenProject project, File file) {

        projectHelper.attachArtifact(project, DESCRIPTOR_TYPE, DESCRIPTOR_CLASSIFIER, file);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public SortedSet<MavenProject> getProjectsWithNoLicense(LicenseMap licenseMap, boolean doLog) {

        Logger log = getLogger();

        // get unsafe dependencies (says with no license)
        SortedSet<MavenProject> unsafeDependencies = licenseMap.get(LicenseMap.getUnknownLicenseMessage());

        if (doLog) {
            if (CollectionUtils.isEmpty(unsafeDependencies)) {
                log.debug("There is no dependency with no license from poms.");
            } else {
                log.debug("There is " + unsafeDependencies.size() + " dependencies with no license from poms : ");
                for (MavenProject dep : unsafeDependencies) {

                    // no license found for the dependency
                    log.debug(" - " + MojoHelper.getArtifactId(dep.getArtifact()));
                }
            }
        }

        return unsafeDependencies;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public SortedProperties loadThirdPartyDescriptorsForUnsafeMapping(String encoding,
            Collection<MavenProject> projects, SortedSet<MavenProject> unsafeDependencies, LicenseMap licenseMap,
            ArtifactRepository localRepository, List<ArtifactRepository> remoteRepositories)
            throws ThirdPartyToolException, IOException {

        SortedProperties result = new SortedProperties(encoding);
        Map<String, MavenProject> unsafeProjects = new HashMap<String, MavenProject>();
        for (MavenProject unsafeDependency : unsafeDependencies) {
            String id = MojoHelper.getArtifactId(unsafeDependency.getArtifact());
            unsafeProjects.put(id, unsafeDependency);
        }

        for (MavenProject mavenProject : projects) {

            if (CollectionUtils.isEmpty(unsafeDependencies)) {

                // no more unsafe dependencies to find
                break;
            }

            File thirdPartyDescriptor = resolvThirdPartyDescriptor(mavenProject, localRepository, remoteRepositories);

            if (thirdPartyDescriptor != null && thirdPartyDescriptor.exists() && thirdPartyDescriptor.length() > 0) {

                if (getLogger().isInfoEnabled()) {
                    getLogger().info("Detects third party descriptor " + thirdPartyDescriptor);
                }

                // there is a third party file detected form the given dependency
                SortedProperties unsafeMappings = new SortedProperties(encoding);

                if (thirdPartyDescriptor.exists()) {

                    getLogger().debug("Load missing file " + thirdPartyDescriptor);

                    // load the missing file
                    unsafeMappings.load(thirdPartyDescriptor);
                }

                for (String id : unsafeProjects.keySet()) {

                    if (unsafeMappings.containsKey(id)) {

                        String license = (String) unsafeMappings.get(id);
                        if (StringUtils.isEmpty(license)) {

                            // empty license means not fill, skip it
                            continue;
                        }

                        // found a resolved unsafe dependency in the missing third party file
                        MavenProject resolvedProject = unsafeProjects.get(id);
                        unsafeDependencies.remove(resolvedProject);

                        // push back to
                        result.put(id, license.trim());

                        addLicense(licenseMap, resolvedProject, license);
                    }
                }
            }
        }
        return result;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public File resolvThirdPartyDescriptor(MavenProject project, ArtifactRepository localRepository,
            List<ArtifactRepository> repositories) throws ThirdPartyToolException {
        if (project == null) {
            throw new IllegalArgumentException("The parameter 'project' can not be null");
        }
        if (localRepository == null) {
            throw new IllegalArgumentException("The parameter 'localRepository' can not be null");
        }
        if (repositories == null) {
            throw new IllegalArgumentException("The parameter 'remoteArtifactRepositories' can not be null");
        }

        try {
            return resolveThirdPartyDescriptor(project, localRepository, repositories);
        } catch (ArtifactNotFoundException e) {
            getLogger().debug("ArtifactNotFoundException: Unable to locate third party descriptor: " + e);
            return null;
        } catch (ArtifactResolutionException e) {
            throw new ThirdPartyToolException("ArtifactResolutionException: Unable to locate third party descriptor: "
                    + e.getMessage(), e);
        } catch (IOException e) {
            throw new ThirdPartyToolException(
                    "IOException: Unable to locate third party descriptor: " + e.getMessage(), e);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void addLicense(LicenseMap licenseMap, MavenProject project, String licenseName) {
        License license = new License();
        license.setName(licenseName.trim());
        license.setUrl(licenseName.trim());
        addLicense(licenseMap, project, license);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void addLicense(LicenseMap licenseMap, MavenProject project, License license) {
        addLicense(licenseMap, project, Arrays.asList(license));
    }

    /**
     * This does some cleanup, and basic safety checks on the license objects passed in<br>
     * 1 - Ignore system scoped dependencies<br>
     * 2 - If there are no licenses declared, use "Unknown License"<br>
     * 3 - If there is no name declared, but there is a url, use the url as a key<br>
     * 4 - If there is no name or url, use "Unknown License" as a key<br>
     */
    @Override
    public void addLicense(LicenseMap licenseMap, MavenProject project, List<?> licenses) {

        if (Artifact.SCOPE_SYSTEM.equals(project.getArtifact().getScope())) {
            // Do not deal with system scope dependencies
            return;
        }

        if (CollectionUtils.isEmpty(licenses)) {
            // No natively declared license was expressed in the pom for this dependency
            // Really funky use of "put"
            // This adds the dependency to the list of dependencies with an unknown license
            licenseMap.put(LicenseMap.getUnknownLicenseMessage(), project);
            return;
        }

        for (Object o : licenses) {
            String id = MojoHelper.getArtifactId(project.getArtifact());
            if (o == null) {
                getLogger().warn("could not acquire a license for " + id);
                continue;
            }
            License license = (License) o;
            String licenseKey = license.getName();

            // tchemit 2010-08-29 Ano #816 Check if the License object is well formed
            if (StringUtils.isEmpty(license.getName())) {
                getLogger().debug("No license name defined for " + id);
                licenseKey = license.getUrl();
            }

            if (StringUtils.isEmpty(licenseKey)) {
                getLogger().debug("No license url defined for " + id);
                licenseKey = LicenseMap.getUnknownLicenseMessage();
            }

            // Really funky use of "put"
            // This adds the dependency to the list of dependencies with this license
            licenseMap.put(licenseKey, project);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void mergeLicenses(LicenseMap licenseMap, String... licenses) {
        if (licenses.length == 0) {
            return;
        }

        String mainLicense = licenses[0].trim();
        SortedSet<MavenProject> mainSet = licenseMap.get(mainLicense);
        if (mainSet == null) {
            getLogger().debug("No license [" + mainLicense + "] found, will create it.");
            mainSet = new TreeSet<MavenProject>(projectComparator);
            licenseMap.put(mainLicense, mainSet);
        }
        int size = licenses.length;
        for (int i = 1; i < size; i++) {
            String license = licenses[i].trim();
            SortedSet<MavenProject> set = licenseMap.get(license);
            if (set == null) {
                getLogger().debug("No license [" + license + "] found, skip this merge.");
                continue;
            }
            getLogger().debug("Merge license [" + license + "] (" + set.size() + " depedencies).");
            mainSet.addAll(set);
            set.clear();
            licenseMap.remove(license);
        }
    }

    protected Properties getProperties(String encoding, File file) throws IOException {
        if (!file.exists()) {
            return new Properties();
        }
        // Load our properties file that maps Maven GAV's to a license
        SortedProperties properties = new SortedProperties(encoding);
        getLogger().debug("Loading " + file);
        properties.load(file);
        return properties;
    }

    protected void handleStuff(Properties customMappings, SortedMap<String, MavenProject> artifactCache) {
        // Store any custom mappings that are not used by this project
        List<String> unusedDependencies = new ArrayList<String>();

        // If the custom mapping file contains GAV entries with type+classifier, remove type+classifier
        // A simple GAV is enough to figure out appropriate licensing
        Map<String, String> migrateKeys = migrateCustomMappingKeys(customMappings.stringPropertyNames());
        for (String id : migrateKeys.keySet()) {
            String migratedId = migrateKeys.get(id);

            MavenProject project = artifactCache.get(migratedId);
            if (project == null) {
                // Now we are sure this is an unused dependency
                // Add this GAV as one that we don't care about for this project
                unusedDependencies.add(id);
            } else {
                if (!id.equals(migratedId)) {

                    // migrates id to migratedId
                    getLogger().info("Migrates [" + id + "] to [" + migratedId + "] in the custom mapping file.");
                    Object value = customMappings.get(id);
                    customMappings.remove(id);
                    customMappings.put(migratedId, value);
                }
            }
        }

        if (!unusedDependencies.isEmpty()) {
            // there are some unused dependencies in the custom mappings file, remove them
            for (String id : unusedDependencies) {
                getLogger().debug("dependency [" + id + "] does not exist in this project");
                // Remove it from the custom mappings file since we don't care about it for this project
                customMappings.remove(id);
            }
        }

    }

    protected void handleUnsafeDependencies(Set<MavenProject> deps, Properties mappings, LicenseMap licenseMap) {
        Iterator<MavenProject> itr = deps.iterator();
        while (itr.hasNext()) {
            MavenProject dep = itr.next();
            String license = getLicense(dep, mappings);
            if (!StringUtils.isBlank(license)) {
                addLicense(licenseMap, dep, license);
                itr.remove();
            } else {
                getLogger().debug(MojoHelper.getArtifactName(dep) + " " + license);
            }
        }
    }

    protected String getLicense(MavenProject d, Properties mappings) {
        String groupId = d.getGroupId();
        String artifactId = d.getArtifactId();
        String version = d.getVersion();

        // Exact match
        String id1 = groupId + "--" + artifactId + "--" + version;
        // Match on groupId + artifactId
        String id2 = groupId + "--" + artifactId;
        // Match on groupId
        String id3 = groupId;

        String value1 = mappings.getProperty(id1);
        String value2 = mappings.getProperty(id2);
        String value3 = mappings.getProperty(id3);

        // Return the license, starting with the most specific, progressing to the least specific
        if (!StringUtils.isBlank(value1)) {
            return value1;
        } else if (!StringUtils.isBlank(value2)) {
            return value2;
        } else if (!StringUtils.isBlank(value3)) {
            return value3;
        } else {
            return null;
        }
    }

    /**
     *
     */
    @Override
    public SortedProperties loadUnsafeMapping(LicenseMap licenseMap, SortedMap<String, MavenProject> artifactCache,
            String encoding, File missingFile) throws IOException {

        // This is the list of dependencies with no declared license info in their pom's
        SortedSet<MavenProject> unsafeDependencies = licenseMap.get(LicenseMap.getUnknownLicenseMessage());

        // Load our properties file that maps Maven GAV's to a license
        Properties customMappings = getProperties(encoding, missingFile);

        // Update licenseMap with info from customMappings
        handleUnsafeDependencies(unsafeDependencies, customMappings, licenseMap);

        // Return customMappings
        return new SortedProperties(customMappings);
    }

    // ----------------------------------------------------------------------
    // Private methods
    // ----------------------------------------------------------------------

    /**
     * @param project
     *            not null
     * @param localRepository
     *            not null
     * @param repositories
     *            not null
     * @return the resolved site descriptor
     * @throws IOException
     *             if any
     * @throws ArtifactResolutionException
     *             if any
     * @throws ArtifactNotFoundException
     *             if any
     */
    private File resolveThirdPartyDescriptor(MavenProject project, ArtifactRepository localRepository,
            List<ArtifactRepository> repositories) throws IOException, ArtifactResolutionException,
            ArtifactNotFoundException {
        File result;

        // TODO: this is a bit crude - proper type, or proper handling as metadata rather than an artifact in 2.1?
        Artifact artifact = artifactFactory.createArtifactWithClassifier(project.getGroupId(), project.getArtifactId(),
                project.getVersion(), DESCRIPTOR_TYPE, DESCRIPTOR_CLASSIFIER);
        try {
            artifactResolver.resolve(artifact, repositories, localRepository);

            result = artifact.getFile();

            // we use zero length files to avoid re-resolution (see below)
            if (result.length() == 0) {
                getLogger().debug("Skipped third party descriptor");
            }
        } catch (ArtifactNotFoundException e) {
            getLogger().debug("Unable to locate third party files descriptor : " + e);

            // we can afford to write an empty descriptor here as we don't expect it to turn up later in the remote
            // repository, because the parent was already released (and snapshots are updated automatically if changed)
            result = new File(localRepository.getBasedir(), localRepository.pathOf(artifact));

            FileUtil.createNewFile(result);
        }

        return result;
    }

    private final Pattern GAV_PLUS_TYPE_PATTERN = Pattern.compile("(.+)--(.+)--(.+)--(.+)");

    private final Pattern GAV_PLUS_TYPE_AND_CLASSIFIER_PATTERN = Pattern.compile("(.+)--(.+)--(.+)--(.+)--(.+)");

    private Map<String, String> migrateCustomMappingKeys(Set<String> customMappingKeys) {
        Map<String, String> migrateKeys = new HashMap<String, String>();
        for (String id : customMappingKeys) {
            Matcher matcher;
            String newId = id;
            matcher = GAV_PLUS_TYPE_AND_CLASSIFIER_PATTERN.matcher(id);
            if (matcher.matches()) {
                newId = matcher.group(1) + "--" + matcher.group(2) + "--" + matcher.group(3);
            } else {
                matcher = GAV_PLUS_TYPE_PATTERN.matcher(id);
                if (matcher.matches()) {
                    newId = matcher.group(1) + "--" + matcher.group(2) + "--" + matcher.group(3);
                }
            }
            migrateKeys.put(id, newId);
        }
        return migrateKeys;
    }
}
