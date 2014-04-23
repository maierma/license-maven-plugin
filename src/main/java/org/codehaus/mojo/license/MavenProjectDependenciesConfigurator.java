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

import java.util.List;

/**
 * Contract to configure which dependencies will be loaded by the dependency tool via the method
 * {@link DependenciesTool#loadProjectDependencies(org.apache.maven.project.MavenProject, MavenProjectDependenciesConfigurator, org.apache.maven.artifact.repository.ArtifactRepository, java.util.List, java.util.SortedMap)}
 *
 * @author tchemit <chemit@codelutin.com>
 * @see DependenciesTool
 * @since 1.0
 */
public interface MavenProjectDependenciesConfigurator
{

    /**
     * @return {@code true} if should include transitive dependencies, {@code false} to include only direct
     *         dependencies.
     */
    boolean isIncludeTransitiveDependencies();

    /**
     * @return list of scopes to include while loading dependencies, if {@code null} is setted, then include all scopes.
     */
    List<String> getIncludedScopes();

    /**
     * @return list of scopes to exclude while loading dependencies, if {@code null} is setted, then include all scopes.
     */
    List<String> getExcludedScopes();

    /**
     * @return a pattern to include dependencies by thier {@code artificatId}, if {@code null} is setted then include
     *         all artifacts.
     */
    String getIncludedArtifacts();

    /**
     * @return a pattern to include dependencies by their {@code groupId}, if {@code null} is setted then include
     *         all artifacts.
     */
    String getIncludedGroups();

    /**
     * @return a pattern to exclude dependencies by their {@code artifactId}, if {@code null} is setted the no exclude is
     *         done on artifactId.
     */
    String getExcludedGroups();

    /**
     * @return a pattern to exclude dependencies by their {@code groupId}, if {@code null} is setted then no exclude
     *         is done on groupId.
     */
    String getExcludedArtifacts();

    /**
     * @return {@code true} if verbose mode is on, {@code false} otherwise.
     */
    boolean isVerbose();
}
