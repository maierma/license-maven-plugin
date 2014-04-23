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
package org.codehaus.mojo.license.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedMap;
import java.util.SortedSet;
import java.util.TreeMap;
import java.util.TreeSet;

import org.apache.maven.project.MavenProject;
import org.codehaus.mojo.license.MojoHelper;

/**
 * Map of artifacts (stub in mavenproject) group by their license.
 *
 * @author tchemit <chemit@codelutin.com>
 * @since 1.0
 */
public class LicenseMap extends TreeMap<String, SortedSet<MavenProject>> {

    private static final long serialVersionUID = 864199843545688069L;

    public static final String unknownLicenseMessage = "Unknown license";

    private final Comparator<MavenProject> projectComparator;

    public LicenseMap() {
        projectComparator = MojoHelper.newMavenProjectComparator();
    }

    public SortedSet<MavenProject> put(String key, MavenProject value) {
        // handle multiple values as a set to avoid duplicates
        SortedSet<MavenProject> valueList = get(key);
        if (valueList == null) {
            valueList = new TreeSet<MavenProject>(projectComparator);
        }
        valueList.add(value);
        return put(key, valueList);
    }

    public SortedMap<MavenProject, String[]> toDependencyMap() {
        SortedMap<MavenProject, Set<String>> tmp = new TreeMap<MavenProject, Set<String>>(projectComparator);

        for (Map.Entry<String, SortedSet<MavenProject>> entry : entrySet()) {
            String license = entry.getKey();
            SortedSet<MavenProject> set = entry.getValue();
            for (MavenProject p : set) {
                Set<String> list = tmp.get(p);
                if (list == null) {
                    list = new HashSet<String>();
                    tmp.put(p, list);
                }
                list.add(license);
            }
        }

        SortedMap<MavenProject, String[]> result = new TreeMap<MavenProject, String[]>(projectComparator);
        for (Map.Entry<MavenProject, Set<String>> entry : tmp.entrySet()) {
            List<String> value = new ArrayList<String>(entry.getValue());
            Collections.sort(value);
            result.put(entry.getKey(), value.toArray(new String[value.size()]));
        }
        tmp.clear();
        return result;
    }

    public static String getUnknownLicenseMessage() {
        return unknownLicenseMessage;
    }

}
