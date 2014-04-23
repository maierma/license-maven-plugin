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
package org.codehaus.mojo.license.header;

import org.codehaus.mojo.license.header.transformer.FileHeaderTransformer;
import org.nuiton.processor.Processor;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

/**
 * File header processor.
 *
 * @author tchemit <chemit@codelutin.com>
 * @plexus.component role="org.nuiton.processor.Processor" role-hint="file-header"
 * @since 1.0
 */
public class FileHeaderProcessor
    extends Processor
{

    /**
     * processor configuration
     */
    protected FileHeaderProcessorConfiguration configuration;

    /**
     * internal file header filter
     */
    protected FileHeaderFilter filter;

    public FileHeaderProcessor()
    {
    }

    public FileHeaderProcessorConfiguration getConfiguration()
    {
        return configuration;
    }

    public FileHeaderFilter getFilter()
    {
        return filter;
    }

    /**
     * @return {@code true} if processed file was touched (says the header was
     *         fully found), {@code false} otherwise
     * @see FileHeaderFilter#isTouched()
     */
    public boolean isTouched()
    {
        return getFilter() != null && getFilter().isTouched();
    }

    /**
     * @return {@code true} if processed file was modified (says the header was
     *         fully found and content changed), {@code false} otherwise
     * @see FileHeaderFilter#isModified()
     */
    public boolean isModified()
    {
        return getFilter() != null && getFilter().isModified();
    }

    /**
     * @return {@code true} if header of header was detected
     * @see FileHeaderFilter#isDetectHeader()
     */
    public boolean isDetectHeader()
    {
        return getFilter() != null && getFilter().isDetectHeader();
    }

    public void process( File filein, File fileout )
        throws IOException, IllegalStateException
    {

        checkInit();
        reset();

        FileReader input = new FileReader( filein );
        try
        {
            FileWriter output = new FileWriter( fileout );
            try
            {
                process( input, output );
            }
            finally
            {
                output.close();
            }
        }
        finally
        {
            input.close();
        }
    }

    public void populateFilter()
    {
        FileHeader fileHeader = getConfiguration().getFileHeader();
        boolean change = false;

        FileHeaderFilter filter = getFilter();

        if ( !fileHeader.equals( filter.getFileHeader() ) )
        {

            // change file header

            filter.setFileHeader( fileHeader );
            change = true;
        }
        FileHeaderTransformer transformer = getConfiguration().getTransformer();
        if ( !transformer.equals( filter.getTransformer() ) )
        {

            // change file transformer

            filter.setTransformer( transformer );
            change = true;
        }
        if ( change )
        {

            // something has changed, must reset content cache
            filter.resetContent();
        }
    }

    public void setConfiguration( FileHeaderProcessorConfiguration configuration )
    {
        this.configuration = configuration;
    }

    public void setFilter( FileHeaderFilter filter )
    {
        this.filter = filter;
        setInputFilter( filter );
    }

    public void reset()
    {
        if ( filter != null )
        {
            filter.reset();
        }
    }

    protected FileHeader getFileHeader()
    {
        return getConfiguration().getFileHeader();
    }

    protected FileHeaderTransformer getTransformer()
    {
        return getConfiguration().getTransformer();
    }

    protected void checkInit()
        throws IllegalStateException
    {
        if ( getConfiguration() == null )
        {
            throw new IllegalStateException( "no configuration set." );
        }
        if ( getFileHeader() == null )
        {
            throw new IllegalStateException( "no file header set." );
        }
        if ( getTransformer() == null )
        {
            throw new IllegalStateException( "no file header transformer set." );
        }
        if ( getFilter() == null )
        {
            throw new IllegalStateException( "no file header filter set." );
        }
    }
}
