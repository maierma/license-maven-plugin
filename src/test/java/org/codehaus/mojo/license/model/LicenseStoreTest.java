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
import java.util.Arrays;
import java.util.List;

/**
 * Tests {@link LicenseStore}.
 *
 * @author tchemit <chemit@codelutin.com>
 * @since 1.0
 */
public class LicenseStoreTest
{

    public static final List<String> DEFAULT_LICENSES =
        Arrays.asList( "agpl_v3", "apache_v2", "cddl_v1", "fdl_v1_3", "gpl_v1", "gpl_v2", "gpl_v3", "lgpl_v2_1",
                       "lgpl_v3", "mit" );

    public static final List<String> NEW_LICENSES = Arrays.asList( "license1", "license2" );

    protected LicenseStore store;

    @Before
    public void setUp()
    {
        store = null;
    }

    @Test
    public void testJarRepository()
        throws IOException
    {

        store = new LicenseStore();
        store.init();

        List<LicenseRepository> repositories = store.getRepositories();
        Assert.assertNotNull( repositories );
        Assert.assertEquals( 1, repositories.size() );
        LicenseRepository repository = repositories.get( 0 );

        License[] licenses1 = repository.getLicenses();
        License[] licenses = store.getLicenses();
        Assert.assertNotNull( licenses );
        Assert.assertNotNull( licenses1 );
        Assert.assertEquals( DEFAULT_LICENSES.size(), licenses.length );
        Assert.assertEquals( DEFAULT_LICENSES.size(), licenses1.length );

        for ( String licenseName : DEFAULT_LICENSES )
        {
            License license = repository.getLicense( licenseName );
            License license1 = store.getLicense( licenseName );
            Assert.assertNotNull( license );
            Assert.assertNotNull( license1 );
            Assert.assertEquals( license, license1 );
        }

        for ( String licenseName : store.getLicenseNames() )
        {
            Assert.assertTrue( DEFAULT_LICENSES.contains( licenseName ) );
        }
    }

    @Test
    public void testOtherJarRepository()
        throws IOException
    {

        URL baseURL = getClass().getResource( "/newRepository" );
        LicenseRepository jarRepository = new LicenseRepository();
        jarRepository.setBaseURL( baseURL );

        store = new LicenseStore();
        store.addRepository( jarRepository );
        store.init();
        List<LicenseRepository> repositories = store.getRepositories();
        Assert.assertNotNull( repositories );
        Assert.assertEquals( 1, repositories.size() );
        LicenseRepository repository = repositories.get( 0 );

        License[] licenses1 = repository.getLicenses();
        License[] licenses = store.getLicenses();
        Assert.assertNotNull( licenses );
        Assert.assertNotNull( licenses1 );
        Assert.assertEquals( licenses1.length, 2 );
        Assert.assertEquals( licenses1.length, licenses.length );

        for ( String licenseName : NEW_LICENSES )
        {
            License license = repository.getLicense( licenseName );
            License license1 = store.getLicense( licenseName );
            Assert.assertNotNull( license );
            Assert.assertNotNull( license1 );
            Assert.assertEquals( license, license1 );
        }

        for ( String licenseName : store.getLicenseNames() )
        {
            Assert.assertTrue( NEW_LICENSES.contains( licenseName ) );
        }
    }
}
