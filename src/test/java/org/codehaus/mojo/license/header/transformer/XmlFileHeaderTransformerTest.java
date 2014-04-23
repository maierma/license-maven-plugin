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
package org.codehaus.mojo.license.header.transformer;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * Tests the {@link XmlFileHeaderTransformer}.
 *
 * @author tchemit <chemit@codelutin.com>
 * @since 1.0
 */
public class XmlFileHeaderTransformerTest
{

    protected XmlFileHeaderTransformer transformer;

    private static final String CONTENT = "content";

    private static final String HEADER = "header";

    @Before
    public void setUp()
    {
        transformer = new XmlFileHeaderTransformer();
    }

    @After
    public void tearDown()
    {
        transformer = null;
    }

    @Test
    public void testAddHeaderWithNoProlog()
    {
        String header = HEADER;
        String content = CONTENT;
        String result = transformer.addHeader( header, content );
        Assert.assertEquals( header + content, result );
    }

    @Test
    public void testAddHeaderWithProlog()
    {
        String header = HEADER;
        String prolog = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>";
        String content = prolog + CONTENT;
        String result = transformer.addHeader( header, content );
        Assert.assertEquals( prolog + '\n' + header + CONTENT, result );

        header = HEADER;
        content = "  " + prolog + CONTENT;
        result = transformer.addHeader( header, content );
        Assert.assertEquals( "  " + prolog + '\n' + header + CONTENT, result );
    }
}
