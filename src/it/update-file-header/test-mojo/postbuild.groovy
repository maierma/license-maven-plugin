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
def assertExistsDirectory(file)
{
  if ( !file.exists() || !file.isDirectory() )
  {
    println(file.getAbsolutePath() + " file is missing or is not a directory.")
    assert false
  }
  assert true
}

def assertExistsFile(file)
{
  if ( !file.exists() || file.isDirectory() )
  {
    println(file.getAbsolutePath() + " file is missing or a directory.")
    assert false
  }
  assert true
}

def assertContains(file, content, expected)
{
  if ( !content.contains(expected) )
  {
    println(expected + " was not found in file [" + file + "]\n :" + content)
    return false
  }
  return true
}

def assertNotContains(file, content, expected)
{
  if ( content.contains(expected) )
  {
    println(expected + " should not be found in file [" + file + "]\n :" + content)
    return false
  }
  return true
}

//
//TEST Java files
//

file = new File(basedir, 'src/main/java/org/codehaus/mojo/license/MyBean.java');
assertExistsFile(file);

content = file.text;
assert assertContains(file, content, 'Copyright (C) 2012 License Test');

file = new File(basedir, 'src/main/java/org/codehaus/mojo/license/MyBean2.java');
assertExistsFile(file);

content = file.text;
assert assertContains(file, content, 'Copyright (C) 2010 Tony');
assert assertContains(file, content, 'do NOT update!');
assert assertNotContains(file, content, 'Fake to be removed!');

file = new File(basedir, 'src/main/java/org/codehaus/mojo/license/MyBean3.java');
assertExistsFile(file);

content = file.text;
assert assertContains(file, content, ' * %%Ignore-License');
assert assertContains(file, content, ' * yet another license');
assert assertContains(file, content, ' * Copyright (C) 2000 Codelutin Do not update!');

file = new File(basedir, 'src/main/java/org/codehaus/mojo/license/MyBean.java2');
assertExistsFile(file);

content = file.text;
assert assertContains(file, content, 'Copyright (C) 2012 License Test');

file = new File(basedir, 'src/main/java/org/codehaus/mojo/license/MyBean2.java2');
assertExistsFile(file);

content = file.text;
assert assertContains(file, content, 'Copyright (C) 2010 Tony');
assert assertContains(file, content, 'do NOT update!');
assert assertNotContains(file, content, 'Fake to be removed!');

file = new File(basedir, 'src/main/java/org/codehaus/mojo/license/MyBean3.java2');
assertExistsFile(file);

content = file.text;
assert assertContains(file, content, ' * %%Ignore-License');
assert assertContains(file, content, ' * yet another license');
assert assertContains(file, content, ' * Copyright (C) 2000 Codelutin Do not update!');

//
// Test apt files
//

file = new File(basedir, 'src/files/apt/test.apt');
assertExistsFile(file);

content = file.text;
assert assertContains(file, content, 'Copyright (C)');
assert assertContains(file, content, '~~ #%L');
assert assertContains(file, content, '~~ #L%');
assert assertContains(file, content, '$Id');
assert assertNotContains(file, content, '~~ ~~');

file = new File(basedir, 'src/files/apt/test2.apt');
assertExistsFile(file);

content = file.text;
assert assertContains(file, content, 'Copyright (C)');
assert assertContains(file, content, '~~ #%L');
assert assertContains(file, content, '~~ #L%');
assert assertContains(file, content, '$Id');
assert assertNotContains(file, content, '~~ ~~');

file = new File(basedir, 'src/files/apt/test.apt2');
assertExistsFile(file);

content = file.text;
assert assertContains(file, content, 'Copyright (C)');
assert assertContains(file, content, '~~ #%L');
assert assertContains(file, content, '~~ #L%');
assert assertContains(file, content, '$Id');
assert assertNotContains(file, content, '~~ ~~');

file = new File(basedir, 'src/files/apt/test2.apt2');
assertExistsFile(file);

content = file.text;
assert assertContains(file, content, 'Copyright (C)');
assert assertContains(file, content, '~~ #%L');
assert assertContains(file, content, '~~ #L%');
assert assertContains(file, content, '$Id');
assert assertNotContains(file, content, '~~ ~~');

//
// Test rst files
//

file = new File(basedir, 'src/files/rst/test.rst');
assertExistsFile(file);

content = file.text;
assert assertContains(file, content, 'Copyright (C)');
assert assertContains(file, content, '.. * #%L');
assert assertContains(file, content, '.. * #L%');
assert assertContains(file, content, '$Id');
assert assertNotContains(file, content, '.. * .. *');

file = new File(basedir, 'src/files/rst/test2.rst');
assertExistsFile(file);

content = file.text;
assert assertContains(file, content, 'Copyright (C)');
assert assertContains(file, content, '.. * #%L');
assert assertContains(file, content, '.. * #L%');
assert assertContains(file, content, '$Id');
assert assertNotContains(file, content, '.. * .. *');

file = new File(basedir, 'src/files/rst/test.rst2');
assertExistsFile(file);

content = file.text;
assert assertContains(file, content, 'Copyright (C)');
assert assertContains(file, content, '.. * #%L');
assert assertContains(file, content, '.. * #L%');
assert assertContains(file, content, '$Id');
assert assertNotContains(file, content, '.. * .. *');

file = new File(basedir, 'src/files/rst/test2.rst2');
assertExistsFile(file);

content = file.text;
assert assertContains(file, content, 'Copyright (C)');
assert assertContains(file, content, '.. * #%L');
assert assertContains(file, content, '.. * #L%');
assert assertContains(file, content, '$Id');
assert assertNotContains(file, content, '.. * .. *');

//
// Test xml files
//

file = new File(basedir, 'src/files/xml/test.xml');
assertExistsFile(file);

content = file.text;
assert content.startsWith("<?xml version='1.0' encoding='UTF-8'?>");
assert assertContains(file, content, 'Copyright (C)');
assert assertContains(file, content, '#%L');
assert assertContains(file, content, '#L%');
assert assertContains(file, content, '$Id');

file = new File(basedir, 'src/files/xml/test2.xml');
assertExistsFile(file);

content = file.text;
assert content.startsWith("<?xml version='1.0' encoding='UTF-8'?>");
assert assertContains(file, content, 'Copyright (C)');
assert assertContains(file, content, '#%L');
assert assertContains(file, content, '#L%');
assert assertContains(file, content, '$Id');

file = new File(basedir, 'src/files/xml/test.xml2');
assertExistsFile(file);

content = file.text;
assert content.startsWith("<?xml version='1.0' encoding='UTF-8'?>");
assert assertContains(file, content, 'Copyright (C)');
assert assertContains(file, content, '#%L');
assert assertContains(file, content, '#L%');
assert assertContains(file, content, '$Id');

file = new File(basedir, 'src/files/xml/test2.xml2');
assertExistsFile(file);

content = file.text;
assert content.startsWith("<?xml version='1.0' encoding='UTF-8'?>");
assert assertContains(file, content, 'Copyright (C)');
assert assertContains(file, content, '#%L');
assert assertContains(file, content, '#L%');
assert assertContains(file, content, '$Id');

//
// Test jsp files
//

file = new File(basedir, 'src/files/jsp/test.jsp');
assertExistsFile(file);

content = file.text;
assert content.startsWith("<%--");
assert assertContains(file, content, 'Copyright (C)');
assert assertContains(file, content, '#%L');
assert assertContains(file, content, '#L%');
assert assertContains(file, content, '$Id');

file = new File(basedir, 'src/files/jsp/test2.jsp');
assertExistsFile(file);

content = file.text;
assert content.startsWith("<%--");
assert assertContains(file, content, 'Copyright (C)');
assert assertContains(file, content, '#%L');
assert assertContains(file, content, '#L%');
assert assertContains(file, content, '$Id');

file = new File(basedir, 'src/files/jsp/test.jsp2');
assertExistsFile(file);

content = file.text;
assert content.startsWith("<%--");
assert assertContains(file, content, 'Copyright (C)');
assert assertContains(file, content, '#%L');
assert assertContains(file, content, '#L%');
assert assertContains(file, content, '$Id');

file = new File(basedir, 'src/files/jsp/test2.jsp2');
assertExistsFile(file);

content = file.text;
assert content.startsWith("<%--");
assert assertContains(file, content, 'Copyright (C)');
assert assertContains(file, content, '#%L');
assert assertContains(file, content, '#L%');
assert assertContains(file, content, '$Id');

//
// Test properties files
//

file = new File(basedir, 'src/files/properties/test.properties');
assertExistsFile(file);

content = file.text;
assert assertContains(file, content, 'Copyright (C)');
assert assertContains(file, content, '# #%L');
assert assertContains(file, content, '# #L%');
assert assertNotContains(file, content, '# # \n');
assert assertContains(file, content, '$Id');

file = new File(basedir, 'src/files/properties/test2.properties');
assertExistsFile(file);

content = file.text;
assert assertContains(file, content, 'Copyright (C)');
assert assertContains(file, content, '# #%L');
assert assertContains(file, content, '# #L%');
assert assertContains(file, content, '# #%L');
assert assertNotContains(file, content, '# # \n');
assert assertContains(file, content, '$Id');


file = new File(basedir, 'src/files/properties/test.properties2');
assertExistsFile(file);

content = file.text;
assert assertContains(file, content, 'Copyright (C)');
assert assertContains(file, content, '# #%L');
assert assertContains(file, content, '# #L%');
assert assertNotContains(file, content, '# # \n');
assert assertContains(file, content, '$Id');


file = new File(basedir, 'src/files/properties/test2.properties2');
assertExistsFile(file);

content = file.text;
assert assertContains(file, content, 'Copyright (C)');
assert assertContains(file, content, '# #%L');
assert assertContains(file, content, '# #L%');
assert assertContains(file, content, '# #%L');
assert assertNotContains(file, content, '# # \n');
assert assertContains(file, content, '$Id');

//
// Test sh files
//

file = new File(basedir, 'src/files/properties/test.sh');
assertExistsFile(file);

content = file.text;
assert content.startsWith('#!/bin/sh');
assert assertContains(file, content, 'Copyright (C)');
assert assertContains(file, content, '# #%L');
assert assertContains(file, content, '# #L%');
assert assertNotContains(file, content, '# # \n');
assert assertContains(file, content, '$Id');

file = new File(basedir, 'src/files/properties/test2.sh');
assertExistsFile(file);

content = file.text;
assert content.startsWith('#!/bin/sh');
assert assertContains(file, content, 'Copyright (C)');
assert assertContains(file, content, '# #%L');
assert assertContains(file, content, '# #L%');
assert assertNotContains(file, content, '# # \n');
assert assertContains(file, content, '$Id');

file = new File(basedir, 'src/files/properties/test.sh2');
assertExistsFile(file);

content = file.text;
assert content.startsWith('#!/bin/sh');
assert assertContains(file, content, 'Copyright (C)');
assert assertContains(file, content, '# #%L');
assert assertContains(file, content, '# #L%');
assert assertNotContains(file, content, '# # \n');
assert assertContains(file, content, '$Id');

file = new File(basedir, 'src/files/properties/test2.sh2');
assertExistsFile(file);

content = file.text;
assert content.startsWith('#!/bin/sh');
assert assertContains(file, content, 'Copyright (C)');
assert assertContains(file, content, '# #%L');
assert assertContains(file, content, '# #L%');
assert assertNotContains(file, content, '# # \n');
assert assertContains(file, content, '$Id');

//
// Test ftl files
//

file = new File(basedir, 'src/files/ftl/test.ftl');
assertExistsFile(file);

content = file.text;
assert assertContains(file, content, 'Copyright (C)');
assert assertContains(file, content, '<#--');
assert assertContains(file, content, ' #%L');
assert assertContains(file, content, ' #L%');
assert assertContains(file, content, '$Id');
assert assertContains(file, content, '-->');

file = new File(basedir, 'src/files/ftl/test2.ftl');
assertExistsFile(file);

content = file.text;
assert assertContains(file, content, 'Copyright (C)');
assert assertContains(file, content, '<#--');
assert assertContains(file, content, ' #%L');
assert assertContains(file, content, ' #L%');
assert assertContains(file, content, '$Id');
assert assertContains(file, content, '-->');

file = new File(basedir, 'src/files/ftl/test.ftl2');
assertExistsFile(file);

content = file.text;
assert assertContains(file, content, 'Copyright (C)');
assert assertContains(file, content, '<#--');
assert assertContains(file, content, ' #%L');
assert assertContains(file, content, ' #L%');
assert assertContains(file, content, '$Id');
assert assertContains(file, content, '-->');

file = new File(basedir, 'src/files/ftl/test2.ftl2');
assertExistsFile(file);

content = file.text;
assert assertContains(file, content, 'Copyright (C)');
assert assertContains(file, content, '<#--');
assert assertContains(file, content, ' #%L');
assert assertContains(file, content, ' #L%');
assert assertContains(file, content, '$Id');
assert assertContains(file, content, '-->');

//
// Test sql files
//
file = new File(basedir, 'src/files/sql/test.sql');
assertExistsFile(file);

content = file.text;
assert assertContains(file, content, 'Copyright (C)');
assert assertContains(file, content, '-- #%L');
assert assertContains(file, content, '-- #L%');
assert assertContains(file, content, '$Id');
assert assertContains(file, content, '---');

file = new File(basedir, 'src/files/sql/test2.sql');
assertExistsFile(file);

content = file.text;
assert assertContains(file, content, 'Copyright (C)');
assert assertContains(file, content, '-- #%L');
assert assertContains(file, content, '-- #L%');
assert assertContains(file, content, '$Id');
assert assertContains(file, content, '---');

file = new File(basedir, 'src/files/sql/test.sql2');
assertExistsFile(file);

content = file.text;
assert assertContains(file, content, 'Copyright (C)');
assert assertContains(file, content, '-- #%L');
assert assertContains(file, content, '-- #L%');
assert assertContains(file, content, '$Id');
assert assertContains(file, content, '---');

file = new File(basedir, 'src/files/sql/test2.sql2');
assertExistsFile(file);

content = file.text;
assert assertContains(file, content, 'Copyright (C)');
assert assertContains(file, content, '-- #%L');
assert assertContains(file, content, '-- #L%');
assert assertContains(file, content, '$Id');
assert assertContains(file, content, '---');

// Test on the child1 module

file = new File(basedir, 'child1/src/main/java/org/codehaus/mojo2/license/MyBean.java');
assertExistsFile(file);

content = file.text;
assert assertContains(file, content, 'Copyright (C) 2012 License Test');

file = new File(basedir, 'child1/src/main/java/org/codehaus/mojo2/license/MyBean2.java');
assertExistsFile(file);

content = file.text;
assert assertContains(file, content, 'Copyright (C) 2010 Tony');
assert assertContains(file, content, 'do NOT update!');
assert assertNotContains(file, content, 'Fake to be removed!');

file = new File(basedir, 'child1/src/main/java/org/codehaus/mojo2/license/MyBean3.java');
assertExistsFile(file);

content = file.text;
assert assertContains(file, content, ' * %%Ignore-License');
assert assertContains(file, content, ' * yet another license');
assert assertContains(file, content, ' * Copyright (C) 2000 Codelutin Do not update!');

//
// Test apt files
//

file = new File(basedir, 'child1/src/files/apt/test.apt');
assertExistsFile(file);

content = file.text;
assert assertContains(file, content, 'Copyright (C)');
assert assertContains(file, content, '~~ #%L');
assert assertContains(file, content, '~~ #L%');
assert assertContains(file, content, '$Id');
assert assertNotContains(file, content, '~~ ~~');

file = new File(basedir, 'child1/src/files/apt/test2.apt');
assertExistsFile(file);

content = file.text;
assert assertContains(file, content, 'Copyright (C)');
assert assertContains(file, content, '~~ #%L');
assert assertContains(file, content, '~~ #L%');
assert assertContains(file, content, '$Id');
assert assertNotContains(file, content, '~~ ~~');

//
// Test rst files
//

file = new File(basedir, 'child1/src/files/rst/test.rst');
assertExistsFile(file);

content = file.text;
assert assertContains(file, content, 'Copyright (C)');
assert assertContains(file, content, '.. * #%L');
assert assertContains(file, content, '.. * #L%');
assert assertContains(file, content, '$Id');
assert assertNotContains(file, content, '.. * .. *');

file = new File(basedir, 'child1/src/files/rst/test2.rst');
assertExistsFile(file);

content = file.text;
assert assertContains(file, content, 'Copyright (C)');
assert assertContains(file, content, '.. * #%L');
assert assertContains(file, content, '.. * #L%');
assert assertContains(file, content, '$Id');
assert assertNotContains(file, content, '.. * .. *');

//
// Test xml files
//

file = new File(basedir, 'child1/src/files/xml/test.xml');
assertExistsFile(file);

content = file.text;
assert content.startsWith("<?xml version='1.0' encoding='UTF-8'?>");
assert assertContains(file, content, 'Copyright (C)');
assert assertContains(file, content, '#%L');
assert assertContains(file, content, '#L%');
assert assertContains(file, content, '$Id');

file = new File(basedir, 'child1/src/files/xml/test2.xml');
assertExistsFile(file);

content = file.text;
assert content.startsWith("<?xml version='1.0' encoding='UTF-8'?>");
assert assertContains(file, content, 'Copyright (C)');
assert assertContains(file, content, '#%L');
assert assertContains(file, content, '#L%');
assert assertContains(file, content, '$Id');

//
// Test jsp files
//

file = new File(basedir, 'child1/src/files/jsp/test.jsp');
assertExistsFile(file);

content = file.text;
assert content.startsWith("<%--");
assert assertContains(file, content, 'Copyright (C)');
assert assertContains(file, content, '#%L');
assert assertContains(file, content, '#L%');
assert assertContains(file, content, '$Id');

file = new File(basedir, 'child1/src/files/jsp/test2.jsp');
assertExistsFile(file);

content = file.text;
assert content.startsWith("<%--");
assert assertContains(file, content, 'Copyright (C)');
assert assertContains(file, content, '#%L');
assert assertContains(file, content, '#L%');
assert assertContains(file, content, '$Id');

//
// Test properties files
//

file = new File(basedir, 'child1/src/files/properties/test.properties');
assertExistsFile(file);

content = file.text;
assert assertContains(file, content, 'Copyright (C)');
assert assertContains(file, content, '# #%L');
assert assertContains(file, content, '# #L%');
assert assertNotContains(file, content, '# # \n');
assert assertContains(file, content, '$Id');

file = new File(basedir, 'child1/src/files/properties/test2.properties');
assertExistsFile(file);

content = file.text;
assert assertContains(file, content, 'Copyright (C)');
assert assertContains(file, content, '# #%L');
assert assertContains(file, content, '# #L%');
assert assertContains(file, content, '# #%L');
assert assertNotContains(file, content, '# # \n');
assert assertContains(file, content, '$Id');

//
// Test sh files
//

file = new File(basedir, 'child1/src/files/properties/test.sh');
assertExistsFile(file);

content = file.text;
assert content.startsWith('#!/bin/sh');
assert assertContains(file, content, 'Copyright (C)');
assert assertContains(file, content, '# #%L');
assert assertContains(file, content, '# #L%');
assert assertNotContains(file, content, '# # \n');
assert assertContains(file, content, '$Id');

file = new File(basedir, 'child1/src/files/properties/test2.sh');
assertExistsFile(file);

content = file.text;
assert content.startsWith('#!/bin/sh');
assert assertContains(file, content, 'Copyright (C)');
assert assertContains(file, content, '# #%L');
assert assertContains(file, content, '# #L%');
assert assertNotContains(file, content, '# # \n');
assert assertContains(file, content, '$Id');

//
// Test ftl files
//

file = new File(basedir, 'child1/src/files/ftl/test.ftl');
assertExistsFile(file);

content = file.text;
assert assertContains(file, content, 'Copyright (C)');
assert assertContains(file, content, '<#--');
assert assertContains(file, content, ' #%L');
assert assertContains(file, content, ' #L%');
assert assertContains(file, content, '$Id');
assert assertContains(file, content, '-->');

file = new File(basedir, 'child1/src/files/ftl/test2.ftl');
assertExistsFile(file);

content = file.text;
assert assertContains(file, content, 'Copyright (C)');
assert assertContains(file, content, '<#--');
assert assertContains(file, content, ' #%L');
assert assertContains(file, content, ' #L%');
assert assertContains(file, content, '$Id');
assert assertContains(file, content, '-->');

//
// Test sql files
//
file = new File(basedir, 'child1/src/files/sql/test.sql');
assertExistsFile(file);

content = file.text;
assert assertContains(file, content, 'Copyright (C)');
assert assertContains(file, content, '-- #%L');
assert assertContains(file, content, '-- #L%');
assert assertContains(file, content, '$Id');
assert assertContains(file, content, '---');

file = new File(basedir, 'child1/src/files/sql/test2.sql');
assertExistsFile(file);

content = file.text;
assert assertContains(file, content, 'Copyright (C)');
assert assertContains(file, content, '-- #%L');
assert assertContains(file, content, '-- #L%');
assert assertContains(file, content, '$Id');
assert assertContains(file, content, '---');

return true;
