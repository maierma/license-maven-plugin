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

/**
 * Contract of a file header.
 * <p/>
 * A header has three sections like in this example :
 * <p/>
 * <pre>
 * Description
 * %--
 * Copyright (C) firstYear[ - lastYear] holder
 * %--
 * License
 * </pre>
 *
 * @author tchemit <chemit@codelutin.com>
 * @since 1.0
 */
public class FileHeader
{

    /**
     * Copyright to string format
     */
    protected static final String COPYRIGHT_TO_STRING_FORMAT = "Copyright (C) %1$s %2$s";

    /**
     * Description of the project or module to add in header
     */
    protected String description;

    /**
     * Copyright holder
     */
    protected String copyrightHolder;

    /**
     * Copyright first year of application
     */
    protected Integer copyrightFirstYear;

    /**
     * Copyright last year of application (can be nullif copyright is
     * only on one year).
     */
    protected Integer copyrightLastYear;

    /**
     * License used in the header.
     */
    protected String license;

    /**
     * @return the project name, or nay other common informations for all
     *         files of a project (or module)
     */
    public String getDescription()
    {
        return description;
    }

    /**
     * @return the copyright holder
     */
    public String getCopyrightHolder()
    {
        return copyrightHolder;
    }

    /**
     * @return the first year of the copyright
     */
    public Integer getCopyrightFirstYear()
    {
        return copyrightFirstYear;
    }

    /**
     * @return the last year of the copyright (if copyright affects only one
     *         year, can be equals to the {@link #getCopyrightFirstYear()}).
     */
    public Integer getCopyrightLastYear()
    {
        return copyrightLastYear;
    }

    /**
     * Produces a string representation of the copyright.
     * <p/>
     * If copyright acts on one year :
     * <pre>
     * Copyright (C) 2010 Holder
     * </pre>
     * <p/>
     * If copyright acts on more than one year :
     * <pre>
     * Copyright (C) 2010 - 2012 Holder
     * </pre>
     *
     * @return the String representation of the copyright
     */
    public String getCopyright()
    {
        String copyright;
        if ( getCopyrightLastYear() == null )
        {

            // copyright on one year
            copyright = String.format( COPYRIGHT_TO_STRING_FORMAT, getCopyrightFirstYear(), getCopyrightHolder() );
        }
        else
        {

            // copyright on more than one year
            copyright =
                String.format( COPYRIGHT_TO_STRING_FORMAT, getCopyrightFirstYear() + " - " + getCopyrightLastYear(),
                               getCopyrightHolder() );
        }
        return copyright;
    }

    /**
     * @return the license content (this is not the fully license content,
     *         but just a per file license resume)
     */
    public String getLicense()
    {
        return license;
    }

    public void setDescription( String description )
    {
        this.description = description;
    }

    public void setCopyrightHolder( String copyrightHolder )
    {
        this.copyrightHolder = copyrightHolder;
    }

    public void setCopyrightFirstYear( Integer copyrightFirstYear )
    {
        this.copyrightFirstYear = copyrightFirstYear;
    }

    public void setCopyrightLastYear( Integer copyrightLastYear )
    {
        this.copyrightLastYear = copyrightLastYear;
    }

    public void setLicense( String license )
    {
        this.license = license;
    }
}
