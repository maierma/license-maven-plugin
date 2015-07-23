[![Build Status](https://travis-ci.org/netceteragroup/license-maven-plugin.svg?branch=master)](https://travis-ci.org/netceteragroup/license-maven-plugin)

The original version of this fork is hosted on Kuali.org [https://svn.kuali.org/repos/foundation/tags/license-maven-plugin-1.6.4/]

Changes introduced based on the public 1.6.4 version
====================================================
* added classpath search for license resolver in LicenseStore.java, lines 51-57 and 180-194
* fixing NPE in AddThirdPartyMojo.java, lines 299-303
* added licenseResolvers property to AbstractLicenseNameMojo.java, lines 45-55, 118-123, 165-170, 191-195
* added fix in AbstractAddThirdPartyMojo.java to support ${license.missingFile} as well as ${license.artifactLicenseMapping} at same time, lines 322-329, 290-304
* added new property skipProjectLicense in UpdateProjectLicenseMojo.java to control if LICENSE.txt should be created in root of package
* added new property skipProjectThirdParty in AbstractAddThirdPartyMojo.java + AddThirdPartyMojo.java to control if THIRD-PARTY.txt should be created in root of package.
* changed property thirdPartyFilename to thirdPartyFile in AbstractAddThirdPartyMojo.java to have same behavior and settings as for creation of LICENSE.txt 
