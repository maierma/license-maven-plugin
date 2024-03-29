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

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.codehaus.mojo.license.MojoHelper;

import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;
import java.util.*;
import java.util.Map.Entry;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author tchemit <chemit@codelutin.com>
 * @since 1.0
 */
public class LicenseRepository
    implements Iterable<License>
{

    /**
     * Logger
     */
    private static final Log log = LogFactory.getLog( LicenseRepository.class );

    public static final String REPOSITORY_DEFINITION_FILE = "licenses.properties";

    public static final Pattern LICENSE_DESCRIPTION_PATTERN =
        Pattern.compile( "(.*)\\s*~~\\s*license\\s*:\\s*(.*)\\s*~~\\s*header\\s*:\\s*(.*)\\s*" );

    /**
     * the base url of the licenses repository
     */
    protected URL baseURL;

    /**
     * licenses of this repository
     */
    protected List<License> licenses;

    /**
     * flag to known if repository was init (pass to {@code true} when invoking
     * the method {@link #load()}).
     */
    protected boolean init;

    public LicenseRepository()
    {
    }

    public URL getBaseURL()
    {
        return baseURL;
    }

    public void setBaseURL( URL baseURL )
    {
        checkNotInit( "setBaseURL" );
        this.baseURL = baseURL;
    }

    public void load()
        throws IOException
    {
        checkNotInit( "load" );
        try
        {
            if ( baseURL == null || StringUtils.isEmpty( baseURL.toString() ) )
            {
                throw new IllegalStateException( "no baseURL defined in " + this );
            }

            URL definitionURL = MojoHelper.getUrl( getBaseURL(), REPOSITORY_DEFINITION_FILE );
            if ( licenses != null )
            {
                licenses.clear();
            }
            else
            {
                licenses = new ArrayList<License>();
            }

            if ( !checkExists( definitionURL ) )
            {
                throw new IllegalArgumentException(
                    "no licenses.properties found with url [" + definitionURL + "] for resolver " + this );
            }
            Properties p = new Properties();
            p.load( definitionURL.openStream() );

            for ( Entry<Object, Object> entry : p.entrySet() )
            {
                String licenseName = (String) entry.getKey();
                licenseName = licenseName.trim().toLowerCase();
                URL licenseBaseURL = MojoHelper.getUrl( baseURL, licenseName );

                License license = new License();
                license.setName( licenseName );
                license.setBaseURL( licenseBaseURL );

                String licenseDescription = (String) entry.getValue();
                Matcher matcher = LICENSE_DESCRIPTION_PATTERN.matcher( licenseDescription );
                String licenseFile;
                String headerFile;

                if ( matcher.matches() )
                {
                    licenseDescription = matcher.group( 1 );
                    licenseFile = matcher.group( 2 );
                    headerFile = matcher.group( 3 );
                }
                else
                {
                    licenseFile = License.LICENSE_CONTENT_FILE;
                    headerFile = License.LICENSE_HEADER_FILE;
                }

                URL licenseURL = MojoHelper.getUrl( licenseBaseURL, licenseFile );
                if ( !checkExists( licenseURL ) )
                {
                    throw new IllegalArgumentException(
                        "Could not find license (" + license + ") content file at [" + licenseURL + "] for resolver " +
                            this );
                }
                license.setLicenseURL( licenseURL );

                URL headerURL = MojoHelper.getUrl( licenseBaseURL, headerFile );
                if ( !checkExists( headerURL ) )
                {
                    throw new IllegalArgumentException(
                        "Could not find license (" + license + ") header file at [" + headerURL + "] for resolver " +
                            this );
                }
                license.setHeaderURL( headerURL );

                license.setDescription( licenseDescription );

                if ( log.isInfoEnabled() )
                {
                    log.info( "register " + license.getDescription() );
                }
                if ( log.isDebugEnabled() )
                {
                    log.debug( license );
                }
                licenses.add( license );
            }
            licenses = Collections.unmodifiableList( licenses );
        }
        finally
        {
            // mark repository as available
            init = true;
        }
    }

    public String[] getLicenseNames()
    {
        checkInit( "getLicenseNames" );
        List<String> result = new ArrayList<String>( licenses.size() );
        for ( License license : this )
        {
            result.add( license.getName() );
        }
        return result.toArray( new String[result.size()] );
    }

    public License[] getLicenses()
    {
        checkInit( "getLicenses" );
        return licenses.toArray( new License[licenses.size()] );
    }

    public License getLicense( String licenseName )
    {
        checkInit( "getLicense" );
        if ( StringUtils.isEmpty( licenseName ) )
        {
            throw new IllegalArgumentException( "licenceName can not be null, nor empty" );
        }

        License license = null;
        for ( License l : this )
        {
            if ( licenseName.equals( l.getName() ) )
            {
                // got it
                license = l;
                break;
            }
        }
        return license;
    }

    public Iterator<License> iterator()
    {
        checkInit( "iterator" );
        return licenses.iterator();
    }

    protected boolean checkExists( URL url )
        throws IOException
    {
        URLConnection openConnection = url.openConnection();
        return openConnection.getContentLength() > 0;
    }

    protected void checkInit( String operation )
        throws IllegalStateException
    {
        if ( !init )
        {
            throw new IllegalStateException(
                "repository " + this + " was not init, operation [" + operation + "] not possible." );
        }
    }

    protected void checkNotInit( String operation )
        throws IllegalStateException
    {
        if ( init )
        {
            throw new IllegalStateException(
                "repository " + this + "was init, operation [" + operation + "+] not possible." );
        }
    }

}
