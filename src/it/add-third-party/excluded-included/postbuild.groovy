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
file = new File(basedir, 'target/generated-sources/license/thirdWithoutScope.txt');
assert file.exists();
content = file.text;
assert !content.contains('the project has no dependencies.');
assert content.contains('commons-logging:commons-logging:1.1.1');
assert content.contains('org.nuiton:nuiton-utils:1.4');
assert content.contains('org.nuiton.i18n:nuiton-i18n:1.2.2');
assert content.contains('org.nuiton:maven-helper-plugin');
assert !content.contains('junit:junit:4.8.2');

file = new File(basedir, 'target/generated-sources/license/thirdWithScope.txt');
assert file.exists();
content = file.text;
assert !content.contains('the project has no dependencies.');
assert !content.contains('commons-logging:commons-logging:1.1.1');
assert !content.contains('org.nuiton:nuiton-utils:1.4');
assert !content.contains('org.nuiton.i18n:nuiton-i18n:1.2.2');
assert !content.contains('org.nuiton:maven-helper-plugin');
assert content.contains('junit:junit:4.8.2');

file = new File(basedir, 'target/generated-sources/license/thirdWithoutGroup.txt');
assert file.exists();
content = file.text;
assert !content.contains('the project has no dependencies.');
assert content.contains('commons-logging:commons-logging:1.1.1');
assert !content.contains('org.nuiton:nuiton-utils:1.4');
assert !content.contains('org.nuiton.i18n:nuiton-i18n:1.2.2');
assert content.contains('junit:junit:4.8.2');


file = new File(basedir, 'target/generated-sources/license/thirdWithoutArtifact.txt');
assert file.exists();
content = file.text;
assert !content.contains('the project has no dependencies.');
assert content.contains('commons-logging:commons-logging:1.1.1');
assert content.contains('org.nuiton:nuiton-utils:1.4');
assert !content.contains('org.nuiton.i18n:nuiton-i18n:1.2.2');
assert content.contains('junit:junit:4.8.2');


file = new File(basedir, 'target/generated-sources/license/thirdWithGroupWithoutArtifact.txt');
assert file.exists();
content = file.text;
assert !content.contains('the project has no dependencies.');
assert !content.contains('commons-logging:commons-logging:1.1.1');
assert !content.contains('org.nuiton:nuiton-utils:1.4');
assert !content.contains('org.nuiton.i18n:nuiton-i18n:1.2.2');
assert content.contains('org.nuiton:maven-helper-plugin');


file = new File(basedir, 'target/generated-sources/license/thirdWithoutGroupWithArtifact.txt');
assert file.exists();
content = file.text;
assert content.contains('the project has no dependencies.');
assert !content.contains('commons-logging:commons-logging:1.1.1');
assert !content.contains('org.nuiton:nuiton-utils:1.4');
assert !content.contains('org.nuiton.i18n:nuiton-i18n:1.2.2');
assert !content.contains('org.nuiton:maven-helper-plugin');


file = new File(basedir, 'target/generated-sources/license/thirdWithGroupWithArtifact.txt');
assert file.exists();
content = file.text;
assert !content.contains('the project has no dependencies.');
assert !content.contains('commons-logging:commons-logging:1.1.1');
assert content.contains('org.nuiton:nuiton-utils:1.4');
assert content.contains('org.nuiton.i18n:nuiton-i18n:1.2.2');
assert content.contains('org.nuiton:maven-helper-plugin');


file = new File(basedir, 'target/generated-sources/license/thirdWithoutGroupWithoutArtifact.txt');
assert file.exists();
content = file.text;
assert !content.contains('the project has no dependencies.');
assert content.contains('commons-logging:commons-logging:1.1.1');
assert !content.contains('org.nuiton:nuiton-utils:1.4');
assert !content.contains('org.nuiton.i18n:nuiton-i18n:1.2.2');
assert !content.contains('org.nuiton:maven-helper-plugin');
assert content.contains('junit:junit:4.8.2');

return true;
