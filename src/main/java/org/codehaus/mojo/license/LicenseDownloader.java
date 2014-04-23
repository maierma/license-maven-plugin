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

import java.io.*;
import java.net.URL;
import java.net.URLConnection;

/**
 * Utilities for downloading remote license files.
 *
 * @author pgier
 * @since 1.0
 */
public class LicenseDownloader
{

    /**
     * Defines the connection timeout in milliseconds when attempting to download license files.
     */
    public static final int DEFAULT_CONNECTION_TIMEOUT = 5000;

    public static void downloadLicense( String licenseUrlString, File outputFile )
        throws IOException
    {
        InputStream licenseInputStream = null;
        FileOutputStream fos = null;

        try
        {
            URL licenseUrl = new URL( licenseUrlString );
            URLConnection connection = licenseUrl.openConnection();
            connection.setConnectTimeout( DEFAULT_CONNECTION_TIMEOUT );
            connection.setReadTimeout( DEFAULT_CONNECTION_TIMEOUT );
            licenseInputStream = connection.getInputStream();
            fos = new FileOutputStream( outputFile );
            copyStream( licenseInputStream, fos );
            licenseInputStream.close();
            fos.close();
        }
        finally
        {
            FileUtil.tryClose( licenseInputStream );
            FileUtil.tryClose( fos );
        }

    }

    /**
     * Copy data from one stream to another.
     *
     * @param inStream
     * @param outStream
     * @throws IOException
     */
    private static void copyStream( InputStream inStream, OutputStream outStream )
        throws IOException
    {
        byte[] buf = new byte[1024];
        int len;
        while ( ( len = inStream.read( buf ) ) > 0 )
        {
            outStream.write( buf, 0, len );
        }
    }

}
