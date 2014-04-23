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

import org.apache.maven.model.License;
import org.codehaus.mojo.license.model.ProjectLicenseInfo;
import org.junit.Assert;
import org.junit.Test;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactoryConfigurationError;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * @since 1.0
 */
public class LicenseSummaryTest
{

    /**
     * Test reading the license summary xml file into ProjectLicenseInfo objects
     */
    @Test
    public void testReadLicenseSummary()
        throws IOException, SAXException, ParserConfigurationException
    {
        File licenseSummaryFile = new File( "src/test/resources/license-summary-test.xml" );
        Assert.assertTrue( licenseSummaryFile.exists() );
        FileInputStream fis = new FileInputStream( licenseSummaryFile );
        List<ProjectLicenseInfo> list = LicenseSummaryReader.parseLicenseSummary( fis );
        fis.close();
        ProjectLicenseInfo dep = list.get( 0 );
        Assert.assertEquals( "org.codehaus.mojo", dep.getGroupId() );
        Assert.assertEquals( "junk", dep.getArtifactId() );
        Assert.assertEquals( "1.1", dep.getVersion() );

    }

    /**
     * Test writing license information to a license.xml file and then read this file
     * back in to make sure it's ok.
     */
    @Test
    public void testWriteReadLicenseSummary()
        throws IOException, SAXException, ParserConfigurationException, TransformerFactoryConfigurationError,
        TransformerException
    {
        List<ProjectLicenseInfo> licSummary = new ArrayList<ProjectLicenseInfo>();
        ProjectLicenseInfo dep1 = new ProjectLicenseInfo( "org.test", "test1", "1.0" );
        ProjectLicenseInfo dep2 = new ProjectLicenseInfo( "org.test", "test2", "2.0" );

        License lic = new License();
        lic.setName( "lgpl" );
        lic.setUrl( "http://www.gnu.org/licenses/lgpl-3.0.txt" );
        lic.setComments( "lgpl version 3.0" );
        dep1.addLicense( lic );
        dep2.addLicense( lic );

        licSummary.add( dep1 );
        licSummary.add( dep2 );

        File licenseSummaryFile = File.createTempFile( "licSummary", "tmp" );
        // File licenseSummaryFile = new File( "src/test/resources/license-summary-test-2.xml" );
        LicenseSummaryWriter.writeLicenseSummary( licSummary, licenseSummaryFile );

        Assert.assertTrue( licenseSummaryFile.exists() );
        FileInputStream fis = new FileInputStream( licenseSummaryFile );
        List<ProjectLicenseInfo> list = LicenseSummaryReader.parseLicenseSummary( fis );
        fis.close();
        ProjectLicenseInfo dep = list.get( 0 );
        Assert.assertEquals( "org.test", dep.getGroupId() );
        Assert.assertEquals( "test1", dep.getArtifactId() );
        Assert.assertEquals( "1.0", dep.getVersion() );

    }
}
