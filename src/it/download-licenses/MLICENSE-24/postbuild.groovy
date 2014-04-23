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
file = new File(basedir, 'target/generated-resources/licenses-excludedScope.xml');
assert file.exists();
content = file.text;
assert !content.contains('the project has no dependencies.');
assert content.contains('commons-logging');
assert content.contains('nuiton-utils');
assert content.contains('nuiton-i18n');
assert content.contains('maven-helper-plugin');
assert !content.contains('junit');

file = new File(basedir, 'target/generated-resources/licenses-includedScope.xml');
assert file.exists();
content = file.text;
assert !content.contains('commons-logging');
assert !content.contains('nuiton-utils');
assert !content.contains('nuiton-i18n');
assert !content.contains('maven-helper-plugin');
assert content.contains('junit');

file = new File(basedir, 'target/generated-resources/licenses-includedScope2.xml');
assert file.exists();
content = file.text;
assert content.contains('commons-logging');
assert !content.contains('nuiton-utils');
assert !content.contains('nuiton-i18n');
assert !content.contains('maven-helper-plugin');
assert content.contains('junit');

return true;
