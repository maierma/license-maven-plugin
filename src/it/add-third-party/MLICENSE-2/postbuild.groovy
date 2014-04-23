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
file = new File(basedir, 'child1/src/license/THIRD-PARTY.properties');
assert file.exists();
content = file.text;
assert content.contains('org.springframework.ws--spring-oxm--1.5.8=');
assert content.contains('org.jboss.seam--jboss-seam--2.2.0.GA=');
assert content.contains('net.sf.docbook--docbook-xsl--1.76.1=');

file = new File(basedir, 'child2/src/license/THIRD-PARTY.properties');
assert file.exists();
content = file.text;
assert content.contains('org.springframework.ws--spring-oxm--1.5.8=The Apache Software License, Version 2.0');
assert content.contains('org.jboss.seam--jboss-seam--2.2.0.GA=The Apache Software License, Version 2.0');
assert content.contains('net.sf.docbook--docbook-xsl--1.76.1=Docbook license (MIT-like)');

return true;