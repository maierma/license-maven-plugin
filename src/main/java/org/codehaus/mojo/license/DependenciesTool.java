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

import org.apache.maven.artifact.repository.ArtifactRepository;
import org.apache.maven.project.MavenProject;

import java.util.List;
import java.util.SortedMap;

/**
 * A tool to deal with dependencies of a project.
 *
 * @author tchemit <chemit@codelutin.com>
 * @since 1.0
 */
public interface DependenciesTool
{

    /**
     * For a given {@code project}, obtain the universe of his dependencies after applying transitivity and
     * filtering rules given in the {@code configuration} object.
     * <p/>
     * Result is given in a map where keys are unique artifact id
     *
     * @param project            the project to scann
     * @param configuration      the configuration
     * @param localRepository    local repository used to resolv dependencies
     * @param remoteRepositories remote repositories used to resolv dependencies
     * @param cache              a optional cache where to keep resolved dependencies
     * @return the map of resolved dependencies indexed by their unique id.
     * @see MavenProjectDependenciesConfigurator
     */
    SortedMap<String, MavenProject> loadProjectDependencies( MavenProject project,
                                                             MavenProjectDependenciesConfigurator configuration,
                                                             ArtifactRepository localRepository,
                                                             List<ArtifactRepository> remoteRepositories,
                                                             SortedMap<String, MavenProject> cache );
}
