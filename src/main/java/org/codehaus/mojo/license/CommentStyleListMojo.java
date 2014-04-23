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

import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.codehaus.mojo.license.header.transformer.FileHeaderTransformer;

import java.util.*;

/**
 * Displays all the available comment style to box file headers.
 *
 * @author themit <chemit@codelutin.com>
 * @requiresProject false
 * @requiresDirectInvocation
 * @goal comment-style-list
 * @since 1.0
 */
public class CommentStyleListMojo
    extends AbstractLicenseMojo
{

    /**
     * A flag to display also the content of each license.
     *
     * @parameter expression="${detail}"
     * @since 1.0
     */
    private boolean detail;

    /**
     * All available header transformers.
     *
     * @component role="org.codehaus.mojo.license.header.transformer.FileHeaderTransformer"
     * @since 1.0
     */
    private Map<String, FileHeaderTransformer> transformers;

    @Override
    protected void init()
        throws Exception
    {
        //nothing to do
    }

    @Override
    public void doAction()
        throws MojoExecutionException, MojoFailureException
    {

        StringBuilder buffer = new StringBuilder();
        if ( isVerbose() )
        {
            buffer.append( "\n\n-------------------------------------------------------------------------------\n" );
            buffer.append( "                           license-maven-plugin\n" );
            buffer.append( "-------------------------------------------------------------------------------\n\n" );
        }
        List<String> names = new ArrayList<String>( transformers.keySet() );
        Collections.sort( names );

        int maxLength = 0;
        int maxDLength = 0;
        for ( String name : names )
        {
            if ( name.length() > maxLength )
            {
                maxLength = name.length();
            }
            FileHeaderTransformer transformer = transformers.get( name );
            if ( transformer.getDescription().length() > maxDLength )
            {
                maxDLength = transformer.getDescription().length();
            }
        }

        String pattern = "  - %1$-" + maxLength + "s : %2$-" + maxDLength + "s, extensions : %3$s\n";

        buffer.append( "List of available comment styles:\n\n" );
        for ( String transformerName : names )
        {
            FileHeaderTransformer transformer = transformers.get( transformerName );
            buffer.append( String.format( pattern, transformerName, transformer.getDescription(),
                                          Arrays.toString( transformer.getDefaultAcceptedExtensions() ) ) );
            if ( detail )
            {
                buffer.append( "\n   example : \n" );
                buffer.append( transformer.boxComment( "header", true ) );
                buffer.append( '\n' );
            }
        }

        getLog().info( buffer.toString() );
    }

    public boolean isDetail()
    {
        return detail;
    }

    public void setDetail( boolean detail )
    {
        this.detail = detail;
    }

    public Map<String, FileHeaderTransformer> getTransformers()
    {
        return transformers;
    }

    public void setTransformers( Map<String, FileHeaderTransformer> transformers )
    {
        this.transformers = transformers;
    }
}
