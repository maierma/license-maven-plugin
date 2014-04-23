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
package org.codehaus.mojo.license.model;

import org.apache.maven.artifact.Artifact;
import org.apache.maven.model.License;

import java.util.ArrayList;
import java.util.List;

/**
 * Contains the license information for a single project/dependency
 *
 * @author pgier
 * @since 1.0
 */
public class ProjectLicenseInfo
{
    private String groupId;

    private String artifactId;

    private String version;

    private List<License> licenses = new ArrayList<License>();

    private String licenseResolutionResult;

    public String getLicenseResolutionResult()
    {
        return licenseResolutionResult;
    }

    public void setLicenseResolutionResult( String licenseResolutionResult )
    {
        this.licenseResolutionResult = licenseResolutionResult;
    }

    /**
     * Default constructor
     */
    public ProjectLicenseInfo()
    {

    }

    public ProjectLicenseInfo( String groupId, String artifactId, String version )
    {
        this.groupId = groupId;
        this.artifactId = artifactId;
        this.version = version;
    }

    public String getGroupId()
    {
        return groupId;
    }

    public void setGroupId( String groupId )
    {
        this.groupId = groupId;
    }

    public String getArtifactId()
    {
        return artifactId;
    }

    public void setArtifactId( String artifactId )
    {
        this.artifactId = artifactId;
    }

    public String getVersion()
    {
        return version;
    }

    public void setVersion( String version )
    {
        this.version = version;
    }

    public List<License> getLicenses()
    {
        return licenses;
    }

    public void setLicenses( List<License> licenses )
    {
        this.licenses = licenses;
    }

    public void addLicense( License license )
    {
        licenses.add( license );
    }

    public String toString()
    {
        return getId();
    }

    /**
     * The unique ID for the project
     *
     * @return String containing "groupId:artifactId"
     */
    public String getId()
    {
        return groupId + ":" + artifactId;
    }

    /**
     * Compare this artifact to another ProjectLicenseInfo, or compare to an instance
     * of org.apache.maven.artifact.Artifact
     */
    public boolean equals( Object compareTo )
    {
        if ( compareTo instanceof ProjectLicenseInfo )
        {
            ProjectLicenseInfo compare = (ProjectLicenseInfo) compareTo;
            if ( groupId.equals( compare.getGroupId() ) && artifactId.equals( compare.getArtifactId() ) )
            {
                return true;
            }
        }
        if ( compareTo instanceof Artifact )
        {
            Artifact compare = (Artifact) compareTo;
            if ( groupId.equals( compare.getGroupId() ) && artifactId.equals( compare.getArtifactId() ) )
            {
                return true;
            }
        }
        return false;
    }

    public int hashCode()
    {
        return getId().hashCode();
    }

}
