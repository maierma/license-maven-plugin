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

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;
import org.codehaus.mojo.license.MojoHelper;
import org.codehaus.plexus.util.IOUtil;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URL;

/**
 * The model of a license.
 *
 * @author tchemit <chemit@codelutin.com>
 * @since 1.0
 */
public class License
{

    public static final String LICENSE_HEADER_FILE = "header.txt";

    public static final String LICENSE_CONTENT_FILE = "license.txt";

    /**
     * base url of license (directory where to find license files)
     */
    protected URL baseURL;

    /**
     * the name of the licenses (ex lgpl-3.0)
     */
    protected String name;

    /**
     * the description of the license
     */
    protected String description;

    /**
     * url of the license's content
     */
    protected URL licenseURL;

    /**
     * url of the license header's content
     */
    protected URL headerURL;

    public License()
    {
    }

    public String getName()
    {
        return name;
    }

    public URL getLicenseURL()
    {
        if ( licenseURL == null )
        {
            licenseURL = MojoHelper.getUrl( getBaseURL(), LICENSE_CONTENT_FILE );
        }
        return licenseURL;
    }

    public URL getHeaderURL()
    {
        if ( headerURL == null )
        {
            headerURL = MojoHelper.getUrl( getBaseURL(), LICENSE_HEADER_FILE );
        }
        return headerURL;
    }

    public String getDescription()
    {
        return description;
    }

    public URL getBaseURL()
    {
        return baseURL;
    }

    public String getLicenseContent( String encoding )
        throws IOException
    {
        if ( baseURL == null )
        {
            throw new IllegalStateException( "no baseURL defined, can not obtain license content in " + this );
        }

        Reader r = new BufferedReader( new InputStreamReader( getLicenseURL().openStream(), encoding ) );
        try
        {
            return IOUtil.toString( r );
        }
        finally
        {
            r.close();
        }
    }

    public String getHeaderContent( String encoding )
        throws IOException
    {
        if ( baseURL == null )
        {
            throw new IllegalStateException( "no baseURL defined, can not obtain header content in " + this );
        }
        Reader r = new BufferedReader( new InputStreamReader( getHeaderURL().openStream(), encoding ) );
        try
        {
            return IOUtil.toString( r );
        }
        finally
        {
            r.close();
        }
    }

    public void setName( String name )
    {
        this.name = name;
    }

    public void setDescription( String description )
    {
        this.description = description;
    }

    public void setBaseURL( URL baseURL )
    {
        this.baseURL = baseURL;
    }

    public void setLicenseURL( URL licenseURL )
    {
        this.licenseURL = licenseURL;
    }

    public void setHeaderURL( URL headerURL )
    {
        this.headerURL = headerURL;
    }

    @Override
    public String toString()
    {
        ToStringBuilder builder = new ToStringBuilder( this, ToStringStyle.MULTI_LINE_STYLE );
        builder.append( "name", name );
        builder.append( "description", description );
        builder.append( "licenseURL", licenseURL );
        builder.append( "headerURL", headerURL );
        return builder.toString();
    }
}
