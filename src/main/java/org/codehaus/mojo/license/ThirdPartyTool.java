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
import java.util.Collection;
import java.util.List;
import java.util.SortedMap;
import java.util.SortedSet;

import org.apache.maven.artifact.repository.ArtifactRepository;
import org.apache.maven.model.License;
import org.apache.maven.project.MavenProject;
import org.codehaus.mojo.license.model.LicenseMap;

/**
 * A tool to load third party files missing files.
 * <p/>
 * We should put here all the logic code written in some mojo and licenseMap...
 *
 * @author tchemit <chemit@codelutin.com>
 * @since 1.0
 */
public interface ThirdPartyTool {

    /**
     * Plexus Role
     */
    String ROLE = ThirdPartyTool.class.getName();

    /**
     * @param encoding
     *            encoding used to read or write properties files
     * @param projects
     *            all projects where to read third parties descriptors
     * @param unsafeProjects
     *            all unsafe projects
     * @param licenseMap
     *            license map where to store new licenses
     * @param localRepository
     *            local repository
     * @param remoteRepositories
     *            remote repositories
     * @return the map of loaded missing from the remote missing third party files
     * @throws ThirdPartyToolException
     *             if any
     * @throws IOException
     *             if any
     */
    SortedProperties loadThirdPartyDescriptorsForUnsafeMapping(String encoding, Collection<MavenProject> projects,
            SortedSet<MavenProject> unsafeProjects, LicenseMap licenseMap, ArtifactRepository localRepository,
            List<ArtifactRepository> remoteRepositories) throws ThirdPartyToolException, IOException;

    /**
     * For the given {@code project}, attach the given {@code file} as a third-party file.
     * <p/>
     * The file will be attached as with a classifier {@code third-parties} and a type {@code properties}.
     *
     * @param project
     *            the project on which to attch the third-party file
     * @param file
     *            the third-party file to attach.
     */
    void attachThirdPartyDescriptor(MavenProject project, File file);

    /**
     * Obtain the third party file from the repository.
     * <p/>
     * Will first search in the local repository, then into the remote repositories and will resolv it.
     *
     * @param project
     *            the project
     * @param localRepository
     *            the local repository
     * @param repositories
     *            the remote repositories
     * @return the locale file resolved into the local repository
     * @throws ThirdPartyToolException
     *             if any
     */
    File resolvThirdPartyDescriptor(MavenProject project, ArtifactRepository localRepository,
            List<ArtifactRepository> repositories) throws ThirdPartyToolException;

    /**
     * From the given {@code licenseMap}, obtain all the projects with no license.
     *
     * @param licenseMap
     *            the license map to query
     * @param doLog
     *            a flag to add debug logs
     * @return the set of projects with no license
     */
    SortedSet<MavenProject> getProjectsWithNoLicense(LicenseMap licenseMap, boolean doLog);

    SortedProperties loadUnsafeMapping(LicenseMap licenseMap, SortedMap<String, MavenProject> artifactCache,
            String encoding, File missingFile) throws IOException;

    /**
     * Add a license (name and url are {@code licenseName}) to the given {@code licenseMap} for the given
     * {@code project}.
     *
     * @param licenseMap
     *            the license map where to add the license
     * @param project
     *            the project
     * @param licenseName
     *            the name of the license
     */
    void addLicense(LicenseMap licenseMap, MavenProject project, String licenseName);

    /**
     * Add a given {@code license} to the given {@code licenseMap} for the given {@code project}.
     *
     * @param licenseMap
     *            the license map where to add the license
     * @param project
     *            the project
     * @param license
     *            the license to add
     */
    void addLicense(LicenseMap licenseMap, MavenProject project, License license);

    /**
     * Add a given {@code licenses} to the given {@code licenseMap} for the given {@code project}.
     *
     * @param licenseMap
     *            the license map where to add the licenses
     * @param project
     *            the project
     * @param licenses
     *            the licenses to add
     */
    void addLicense(LicenseMap licenseMap, MavenProject project, List<?> licenses);

    /**
     * For a given {@code licenseMap}, merge all {@code licenses}.
     * <p/>
     * The first value of the {@code licenses} is the license to keep and all other values will be merged into the first
     * one.
     *
     * @param licenseMap
     *            the license map to merge
     * @param licenses
     *            all the licenses to merge (the first license will be the unique one to kkep)
     */
    void mergeLicenses(LicenseMap licenseMap, String... licenses);
}
