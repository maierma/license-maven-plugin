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
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.SortedMap;
import java.util.SortedSet;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.apache.maven.artifact.repository.ArtifactRepository;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugin.logging.Log;
import org.apache.maven.project.MavenProject;
import org.apache.maven.project.ProjectBuildingException;
import org.codehaus.mojo.license.model.LicenseMap;

/**
 * Goal to generate the third-party license file.
 * <p/>
 * This file contains a list of the dependencies and their licenses. Each dependency and it's license is displayed on a
 * single line in the format <br/>
 *
 * <pre>
 *   (&lt;license-name&gt;) &lt;project-name&gt; &lt;groupId&gt;:&lt;artifactId&gt;:&lt;version&gt; - &lt;project-url&gt;
 * </pre>
 *
 * It will also copy it in the class-path (says add the generated directory as a resource of the build).
 *
 * @author tchemit <chemit@codelutin.com>
 * @goal add-third-party
 * @phase generate-resources
 * @requiresDependencyResolution test
 * @requiresProject true
 * @since 1.0
 */
public class AddThirdPartyMojo extends AbstractAddThirdPartyMojo implements MavenProjectDependenciesConfigurator {

    /**
     * Local Repository.
     *
     * @parameter expression="${localRepository}"
     * @required
     * @readonly
     * @since 1.0.0
     */
    protected ArtifactRepository localRepository;

    /**
     * Remote repositories used for the project.
     *
     * @parameter expression="${project.remoteArtifactRepositories}"
     * @required
     * @readonly
     * @since 1.0.0
     */
    protected List remoteRepositories;

    /**
     * Deploy the third party missing file in maven repository.
     *
     * @parameter expression="${license.deployMissingFile}" default-value="true"
     * @since 1.0
     */
    protected boolean deployMissingFile;

    /**
     * Load from repositories third party missing files.
     *
     * @parameter expression="${license.useRepositoryMissingFiles}" default-value="true"
     * @since 1.0
     */
    protected boolean useRepositoryMissingFiles;

    /**
     * dependencies tool.
     *
     * @component
     * @readonly
     * @since 1.0
     */
    private DependenciesTool dependenciesTool;

    /**
     * Controls if THIRD-PARTY.properties gets created or not
     *
     * @parameter expression="${license.doGenerateMissing}" default-value="false"
     */
    private boolean doGenerateMissing;

    @Override
    protected boolean checkPackaging() {
        return rejectPackaging("pom");
    }

    @Override
    protected SortedMap<String, MavenProject> loadDependencies() {

        return dependenciesTool.loadProjectDependencies(getProject(), this, localRepository, remoteRepositories,
                getArtifactCache());
    }

    @Override
    protected SortedProperties createUnsafeMapping() throws ProjectBuildingException, IOException,
            ThirdPartyToolException {

        SortedProperties unsafeMappings = getThirdPartyTool().loadUnsafeMapping(getLicenseMap(), getArtifactCache(),
                getEncoding(), getMissingFile());

        SortedSet<MavenProject> unsafeDependencies = getUnsafeDependencies();

        if (CollectionUtils.isNotEmpty(unsafeDependencies)) {

            // there is some unresolved license

            if (isUseRepositoryMissingFiles()) {

                // try to load missing third party files from dependencies

                Collection<MavenProject> projects = new ArrayList<MavenProject>(getProjectDependencies().values());
                projects.remove(getProject());
                projects.removeAll(unsafeDependencies);

                SortedProperties resolvedUnsafeMapping = new SortedProperties("UTF-8");
                // The next few lines attempt to download groupid--artifactid--third-party.properties for every
                // dependency in the tree

                // getThirdPartyTool().loadThirdPartyDescriptorsForUnsafeMapping(
                // getEncoding(), projects, unsafeDependencies, getLicenseMap(), localRepository,
                // remoteRepositories);

                // push back resolved unsafe mappings
                unsafeMappings.putAll(resolvedUnsafeMapping);

            }
        }
        if (isVerbose()) {
            getLog().info("found " + unsafeMappings.size() + " unsafe mappings");
        }

        // compute if missing file should be (re)-generate
        boolean generateMissingfile = doGenerateMissing
                && computeDoGenerateMissingFile(unsafeMappings, unsafeDependencies);

        setDoGenerateMissing(generateMissingfile);

        if (generateMissingfile && isVerbose()) {
            StringBuilder sb = new StringBuilder();
            sb.append("Will use from missing file ");
            sb.append(unsafeMappings.size());
            sb.append(" dependencies :");
            for (Map.Entry<Object, Object> entry : unsafeMappings.entrySet()) {
                String id = (String) entry.getKey();
                String license = (String) entry.getValue();
                sb.append("\n - ").append(id).append(" - ").append(license);
            }
            getLog().info(sb.toString());
        } else {
            if (isUseMissingFile() && !unsafeMappings.isEmpty()) {
                getLog().debug("Missing file " + getMissingFile() + " is up-to-date.");
            }
        }
        return unsafeMappings;
    }

    /**
     * @param unsafeMappings
     *            the unsafe mapping coming from the missing file
     * @param unsafeDependencies
     *            the unsafe dependencies from the project
     * @return {@code true} if missing ifle should be (re-)generated, {@code false} otherwise
     * @throws IOException
     *             if any IO problem
     * @since 1.0
     */
    protected boolean computeDoGenerateMissingFile(SortedProperties unsafeMappings,
            SortedSet<MavenProject> unsafeDependencies) throws IOException {

        if (!isUseMissingFile()) {

            // never use the missing file
            return false;
        }

        if (isForce()) {

            // the mapping for missing file is not empty, regenerate it
            return !CollectionUtils.isEmpty(unsafeMappings.keySet());
        }

        if (!CollectionUtils.isEmpty(unsafeDependencies)) {

            // there is some unsafe dependencies from the project, must
            // regenerate missing file
            return true;
        }

        File missingFile = getMissingFile();

        if (!missingFile.exists()) {

            // the missing file does not exists, this happens when
            // using remote missing file from dependencies
            return true;
        }

        // check if the missing file has changed
        SortedProperties oldUnsafeMappings = new SortedProperties(getEncoding());
        oldUnsafeMappings.load(missingFile);
        return !unsafeMappings.equals(oldUnsafeMappings);
    }

    @Override
    protected boolean checkSkip() {
        if (!isDoGenerate() && !isDoGenerateBundle() && !isDoGenerateMissing()) {

            getLog().info("All files are up to date, skip goal execution.");
            return false;
        }
        return true;
    }

    @Override
    protected void doAction() throws Exception {
        boolean unsafe = checkUnsafeDependencies();

        writeThirdPartyFile();

        if (isDoGenerateMissing()) {
            writeMissingFile();
        }

        if (unsafe && isFailIfWarning()) {
            throw new MojoFailureException("There is at least one dependency with no license information");
        }

        if (!unsafe && isUseMissingFile() && MapUtils.isEmpty(getUnsafeMappings()) && getMissingFile().exists()) {

            // there is no missing dependencies, but still a missing file, delete it
            getLog().debug("There is no dependency to put in missing file, delete it at " + getMissingFile());
            FileUtil.deleteFile(getMissingFile());
        }

        if (!unsafe && isDeployMissingFile() && MapUtils.isNotEmpty(getUnsafeMappings())) {

            // can deploy missing file
            File file = getMissingFile();

            getLog().debug("Will deploy third party file from " + file);
            getThirdPartyTool().attachThirdPartyDescriptor(getProject(), file);
        }

        addResourceDir(getOutputDirectory(), "**/*.txt");
    }

    protected void writeMissingFile() throws IOException {

        Log log = getLog();
        LicenseMap licenseMap = getLicenseMap();
        File file = getMissingFile();

        FileUtil.createDirectoryIfNecessary(file.getParentFile());
        log.info("Regenerate missing license file " + file);

        FileOutputStream writer = new FileOutputStream(file);
        try {
            StringBuilder sb = new StringBuilder(" Generated by " + getClass().getName());
            List<String> licenses = new ArrayList<String>(licenseMap.keySet());
            licenses.remove(LicenseMap.getUnknownLicenseMessage());
            if (!licenses.isEmpty()) {
                sb.append("\n-------------------------------------------------------------------------------");
                sb.append("\n Already used licenses in project :");
                for (String license : licenses) {
                    sb.append("\n - ").append(license);
                }
            }
            sb.append("\n-------------------------------------------------------------------------------");
            sb.append("\n Please fill the missing licenses for dependencies :\n\n");
            getUnsafeMappings().store(writer, sb.toString());
        } finally {
            writer.close();
        }
    }

    public boolean isDoGenerateMissing() {
        return doGenerateMissing;
    }

    public void setDoGenerateMissing(boolean doGenerateMissing) {
        this.doGenerateMissing = doGenerateMissing;
    }

    public ArtifactRepository getLocalRepository() {
        return localRepository;
    }

    public List getRemoteRepositories() {
        return remoteRepositories;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isIncludeTransitiveDependencies() {
        return includeTransitiveDependencies;
    }

    // /**
    // * {@inheritDoc}
    // */
    // public List<String> getExcludedScopes()
    // {
    // return Arrays.asList( Artifact.SCOPE_SYSTEM );
    // }

    public boolean isDeployMissingFile() {
        return deployMissingFile;
    }

    public boolean isUseRepositoryMissingFiles() {
        return useRepositoryMissingFiles;
    }

}
