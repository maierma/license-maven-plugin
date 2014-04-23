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

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.net.URL;

/**
 * Tests {@link LicenseRepository}.
 *
 * @author tchemit <chemit@codelutin.com>
 * @since 1.0
 */
public class LicenseRepositoryTest
{

    protected LicenseRepository repository;

    @Before
    public void setUp()
    {
        repository = null;
    }

    @Test
    public void testJarRepository()
        throws IOException
    {

        repository = new LicenseRepository();
        URL baseURL = getClass().getResource( LicenseStore.JAR_LICENSE_REPOSITORY );
        repository.setBaseURL( baseURL );
        repository.load();

        License[] licenses = repository.getLicenses();
        Assert.assertNotNull( licenses );
        Assert.assertEquals( LicenseStoreTest.DEFAULT_LICENSES.size(), licenses.length );

        for ( String licenseName : LicenseStoreTest.DEFAULT_LICENSES )
        {
            License license = repository.getLicense( licenseName );
            Assert.assertNotNull( license );
        }

        for ( String licenseName : repository.getLicenseNames() )
        {
            Assert.assertTrue( LicenseStoreTest.DEFAULT_LICENSES.contains( licenseName ) );
        }
    }
}
