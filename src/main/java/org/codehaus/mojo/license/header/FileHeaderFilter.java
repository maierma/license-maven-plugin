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

import org.apache.maven.plugin.logging.Log;
import org.codehaus.mojo.license.header.transformer.FileHeaderTransformer;
import org.nuiton.processor.filters.DefaultFilter;

/**
 * File Header filter.
 *
 * @author tchemit <chemit@codelutin.com>
 * @since 1.0
 */
public abstract class FileHeaderFilter
    extends DefaultFilter
{

    /**
     * flag set to {@code true} when a header was detected (says detects both
     * start and end process tags).
     */
    protected boolean touched;

    /**
     * flag set to {@code true} when a header was detected and was modified.
     */
    protected boolean modified;

    /**
     * flag set to {@code true} as soon as start process tag was detected.
     */
    protected boolean detectHeader;

    /**
     * incoming default file header model
     */
    protected FileHeader fileHeader;

    /**
     * header transformer
     */
    protected FileHeaderTransformer transformer;

    /**
     * cached header content
     */
    protected String headerContent;

    /**
     * cached full header content (with process tag + comment box)
     */
    protected String processTagHeaderContent;

    /**
     * cached full header content (with process tag + comment box)
     */
    protected String fullHeaderContent;

    /**
     * maven logger
     */
    protected Log log;

    /**
     * Obtains the new header to use according to the old one.
     * <p/>
     * <b>Note:</b> If the new header should not be updated, then the result is {@code null}.
     *
     * @param oldHeader the old header found in file.
     * @return {@code null} if header is still the same, otherwise the new header to apply
     * @since 1.0
     */
    protected abstract FileHeader getNewHeader( FileHeader oldHeader );

    public FileHeaderFilter()
    {
    }

    public Log getLog()
    {
        return log;
    }

    public void setLog( Log log )
    {
        this.log = log;
    }

    @Override
    protected String performInFilter( String ch )
    {
        if ( log.isDebugEnabled() )
        {
            log.debug( "performInFilter - original header =\n" + ch );
        }
        if ( isTouched() )
        {
            // Can NOT authorize two header in a same source
            throw new IllegalStateException( "Can only have one file header start tag : " + getHeader() );
        }
        if ( getMatchIndexFor( ch, getHeader() ) == NOT_FOUND )
        {

            // the header was detected, mark file to be touched
            touched = true;

            // obtain old header model
            FileHeaderTransformer headerTransformer = getTransformer();
            String tmp = headerTransformer.unboxComent( ch );
            FileHeader oldHeader = headerTransformer.toFileHeader( tmp );

            // obtain the new header (according to what to update)
            FileHeader newFileHeader = getNewHeader( oldHeader );

            FileHeader header;

            if ( newFileHeader == null )
            {

                // keep the old header
                header = oldHeader;

            }
            else
            {

                // mark that the header was updated
                modified = true;

                header = newFileHeader;
            }

            return transformer.toHeaderContent( header );
        }
        // Means we detects the process start tag but not the end one.
        // coming then here from the flush filter method... So changes nothing
        // just return the text as it comes.
        return ch;
    }

    @Override
    protected String performOutFilter( String ch )
    {
        if ( log.isDebugEnabled() )
        {
            log.debug( ch );
        }
        return ch;
    }

    @Override
    protected String getHeader()
    {
        return getTransformer().getProcessStartTag();
    }

    @Override
    protected String getFooter()
    {
        return getTransformer().getProcessEndTag();
    }

    @Override
    protected void changeState( State newState )
    {
        super.changeState( newState );
        if ( newState == State.SEARCH_FOOTER )
        {
            // on a decouvert un header
            detectHeader = true;
        }
    }

    public String getHeaderContent()
    {
        if ( headerContent == null )
        {
            headerContent = getTransformer().toString( getFileHeader() );
        }
        return headerContent;
    }

    public String getProcessTagHeaderContent()
    {
        if ( processTagHeaderContent == null )
        {

            // box with process tag
            processTagHeaderContent = getTransformer().boxProcessTag( getHeaderContent() );

        }
        return processTagHeaderContent;
    }

    public String getFullHeaderContent()
    {
        if ( fullHeaderContent == null )
        {

            // box with comment
            fullHeaderContent = getTransformer().boxComment( getProcessTagHeaderContent(), true );
        }
        return fullHeaderContent;
    }

    public boolean isTouched()
    {
        return touched;
    }

    public boolean isModified()
    {
        return modified;
    }

    public boolean isDetectHeader()
    {
        return detectHeader;
    }

    public FileHeader getFileHeader()
    {
        return fileHeader;
    }

    public FileHeaderTransformer getTransformer()
    {
        return transformer;
    }

    public void setFileHeader( FileHeader fileHeader )
    {
        this.fileHeader = fileHeader;
    }

    public void setTransformer( FileHeaderTransformer transformer )
    {
        this.transformer = transformer;
    }

    public void reset()
    {
        touched = false;
        modified = false;
        detectHeader = false;
        state = State.SEARCH_HEADER;
    }

    public void resetContent()
    {
        headerContent = null;
        processTagHeaderContent = null;
        fullHeaderContent = null;
    }
}
