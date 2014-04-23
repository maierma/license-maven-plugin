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
file = new File(basedir, 'target/generated-sources/license/THIRD-PARTY.txt');
assert file.exists();
content = file.text;
assert !content.contains('the project has no dependencies.');
assert content.contains('(The Apache Software License, Version 2.0) Commons Logging (commons-logging:commons-logging:1.1.1 - http://commons.apache.org/logging)');

file = new File(basedir, 'target/generated-sources/license/META-INF/add-third-party-no-encoding-THIRD-PARTY.txt');
assert file.exists();
content = file.text;
assert !content.contains('the project has no dependencies.');
assert content.contains('(The Apache Software License, Version 2.0) Commons Logging (commons-logging:commons-logging:1.1.1 - http://commons.apache.org/logging)');


file = new File(basedir, 'build.log');
assert file.exists();
content = file.text;
assert content.contains('File encoding has not been set, using platform encoding');

return true;
