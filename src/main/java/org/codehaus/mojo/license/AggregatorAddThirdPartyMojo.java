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
import java.util.List;
import java.util.SortedMap;
import java.util.SortedSet;

import org.apache.commons.collections.CollectionUtils;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugin.logging.Log;
import org.apache.maven.project.MavenProject;
import org.apache.maven.project.ProjectBuildingException;
import org.codehaus.mojo.license.model.LicenseMap;

/**
 * This aggregator goal (will be executed only once and only on pom projects) executed the {@code add-third-party} on
 * all his modules (in a parellel build cycle) then aggreates all the third-party files in final one in the pom project.
 *
 * @author tchemit <chemit@codelutin.com>
 * @goal aggregate-add-third-party
 * @phase generate-resources
 * @requiresProject true
 * @aggregator
 * @execute goal="add-third-party"
 * @since 1.0
 */
public class AggregatorAddThirdPartyMojo extends AbstractAddThirdPartyMojo {

    /**
     * The projects in the reactor.
     *
     * @parameter expression="${reactorProjects}"
     * @readonly
     * @required
     * @since 1.0
     */
    protected List<?> reactorProjects;

    @Override
    protected boolean checkPackaging() {
        return acceptPackaging("pom");
    }

    @Override
    protected boolean checkSkip() {
        if (!isDoGenerate() && !isDoGenerateBundle()) {

            getLog().info("All files are up to date, skip goal execution.");
            return false;
        }
        return super.checkSkip();
    }

    @Override
    protected SortedMap<String, MavenProject> loadDependencies() {
        // use the cache filled by modules in reactor
        return getArtifactCache();
    }

    @Override
    protected SortedProperties createUnsafeMapping() throws ProjectBuildingException, IOException {

        String path = getMissingFile().getAbsolutePath().substring(
                getProject().getBasedir().getAbsolutePath().length() + 1);

        if (isVerbose()) {
            getLog().info("Use missing file path : " + path);
        }

        SortedProperties unsafeMappings = new SortedProperties(getEncoding());

        LicenseMap licenseMap = getLicenseMap();

        for (Object o : reactorProjects) {
            MavenProject p = (MavenProject) o;

            File file = new File(p.getBasedir(), path);

            if (file.exists()) {

                SortedProperties tmp = getThirdPartyTool().loadUnsafeMapping(licenseMap, getArtifactCache(),
                        getEncoding(), file);
                unsafeMappings.putAll(tmp);
            }

            SortedSet<MavenProject> unsafes = getThirdPartyTool().getProjectsWithNoLicense(licenseMap, isVerbose());
            if (CollectionUtils.isEmpty(unsafes)) {

                // no more unsafe dependencies, can break
                break;
            }
        }
        return unsafeMappings;
    }

    @Override
    protected void doAction() throws Exception {
        Log log = getLog();

        if (exists(getArtifactLicenseMapping())) {
            File propertiesFile = copyToFileSystem(getArtifactLicenseMapping());
            setMissingFile(propertiesFile);
        }

        if (isVerbose()) {
            log.info("After executing on " + reactorProjects.size() + " project(s)");
        }
        SortedMap<String, MavenProject> artifacts = getArtifactCache();

        LicenseMap licenseMap = getLicenseMap();

        getLog().info(artifacts.size() + " detected artifact(s).");
        if (isVerbose()) {
            for (String id : artifacts.keySet()) {
                getLog().info(" - " + id);
            }
        }
        getLog().info(licenseMap.size() + " detected license(s).");
        if (isVerbose()) {
            for (String id : licenseMap.keySet()) {
                getLog().info(" - " + id);
            }
        }
        boolean unsafe = checkUnsafeDependencies();

        writeThirdPartyFile();

        if (unsafe && isFailIfWarning()) {
            throw new MojoFailureException("There is some dependencies with no license, please review the modules.");
        }
    }

}
