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
file = new File(basedir, 'src/main/java/org/codehaus/mojo/license/plugin/test/MyBean.java');
assert file.exists();

content = file.text;
assert content.contains('Copyright (C) 2112 License Test');

file = new File(basedir, 'src/main/java/org/codehaus/mojo/license/plugin/test/MyBean2.java');
assert file.exists();

content = file.text;
assert !content.contains('Copyright (C) 2010 Tony Update me');
assert content.contains('Copyright (C) 2112 License Test');
assert content.contains('do NOT update!');
assert !content.contains('Fake to be removed!');

file = new File(basedir, 'src/main/java/org/codehaus/mojo/license/plugin/test/MyBean3.java');
assert file.exists();

content = file.text;
assert content.contains('%%Ignore-License');
assert content.contains('Copyright (C) 2000 Codelutin Do not update!');

return true;
