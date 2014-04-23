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

/**
 * An exception occurring during the execution of this tool.
 *
 * @author <a href="mailto:tchemit@codelutin.com">tony chemit</a>
 * @version $Id: ThirdPartyToolException.java 14409 2011-08-10 15:30:41Z tchemit $
 */
public class ThirdPartyToolException
    extends Exception
{
    /**
     * Construct a new <code>ThirdPartyToolException</code> exception wrapping an underlying <code>Exception</code>
     * and providing a <code>message</code>.
     *
     * @param message could be null
     * @param cause   could be null
     */
    public ThirdPartyToolException( String message, Exception cause )
    {
        super( message, cause );
    }

    /**
     * Construct a new <code>ThirdPartyToolException</code> exception wrapping an underlying <code>Throwable</code>
     * and providing a <code>message</code>.
     *
     * @param message could be null
     * @param cause   could be null
     */
    public ThirdPartyToolException( String message, Throwable cause )
    {
        super( message, cause );
    }

    /**
     * Construct a new <code>ThirdPartyToolException</code> exception providing a <code>message</code>.
     *
     * @param message could be null
     */
    public ThirdPartyToolException( String message )
    {
        super( message );
    }
}
