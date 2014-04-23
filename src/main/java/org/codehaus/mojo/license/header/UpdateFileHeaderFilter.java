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

/**
 * Implementation of {@link FileHeaderFilter} to update an incoming header.
 *
 * @author tchemit <chemit@codelutin.com>
 * @plexus.component role="org.codehaus.mojo.license.header.FileHeaderFilter" role-hint="update-file-header"
 * @since 1.0
 */
public class UpdateFileHeaderFilter
    extends FileHeaderFilter
{

    /**
     * Flag sets to {@code true} if description can be updated.
     */
    protected boolean updateDescription;

    /**
     * Flag set to {@code true} if license can be updated.
     */
    protected boolean updateLicense;

    /**
     * Flag sets to {@code true} if copyright can be updated.
     */
    protected boolean updateCopyright;

    public UpdateFileHeaderFilter()
    {
    }

    @Override
    protected FileHeader getNewHeader( FileHeader oldHeader )
    {

        FileHeader result = new FileHeader();

        FileHeader newHeader = getFileHeader();

        FileHeaderTransformer transformer = getTransformer();

        boolean modified = false;

        // by default, reuse the old header
        result.setDescription( oldHeader.getDescription() );
        result.setCopyrightFirstYear( oldHeader.getCopyrightFirstYear() );
        result.setCopyrightLastYear( oldHeader.getCopyrightLastYear() );
        result.setCopyrightHolder( oldHeader.getCopyrightHolder() );
        result.setLicense( oldHeader.getLicense() );

        if ( isUpdateDescription() && !transformer.isDescriptionEquals( oldHeader, newHeader ) )
        {

            // can update description and it has changed

            if ( log.isDebugEnabled() )
            {
                log.debug( "description has changed from [" + oldHeader.getDescription() + "] to [" +
                               newHeader.getDescription() + "]" );
            }

            // description has changed, mark header to be updated
            modified = true;

            // use the new description
            result.setDescription( newHeader.getDescription() );
        }

        if ( isUpdateCopyright() && !transformer.isCopyrightEquals( oldHeader, newHeader ) )
        {

            // can update copyright and it has changed

            if ( log.isDebugEnabled() )
            {
                log.debug(
                    "copyright has changed from [" + oldHeader.getCopyright() + "] to [" + newHeader.getCopyright() +
                        "]" );
            }

            // description has changed, mark header to be updated
            modified = true;

            // use the new copyright
            result.setCopyrightFirstYear( newHeader.getCopyrightFirstYear() );
            result.setCopyrightLastYear( newHeader.getCopyrightLastYear() );
            result.setCopyrightHolder( newHeader.getCopyrightHolder() );
        }

        if ( isUpdateLicense() && !transformer.isLicenseEquals( oldHeader, newHeader ) )
        {

            // can update license and it has changed

            if ( log.isDebugEnabled() )
            {
                log.debug(
                    "license has changed from [" + oldHeader.getLicense() + "] to [" + newHeader.getLicense() + "]" );
            }

            // description has changed, mark header to be updated
            modified = true;

            // use the new license
            result.setLicense( newHeader.getLicense() );
        }

        if ( !modified )
        {

            // nothing has to be updated, so return a {@code null} result
            result = null;
        }

        return result;
    }

    public boolean isUpdateCopyright()
    {
        return updateCopyright;
    }

    public void setUpdateCopyright( boolean updateCopyright )
    {
        this.updateCopyright = updateCopyright;
    }

    public boolean isUpdateDescription()
    {
        return updateDescription;
    }

    public void setUpdateDescription( boolean updateDescription )
    {
        this.updateDescription = updateDescription;
    }

    public boolean isUpdateLicense()
    {
        return updateLicense;
    }

    public void setUpdateLicense( boolean updateLicense )
    {
        this.updateLicense = updateLicense;
    }
}
