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

import org.codehaus.mojo.license.model.License;

import java.io.File;

/**
 * Updates (or creates) the main project license file according to the given
 * license defines as {@link #licenseName}.
 * <p/>
 * Can also generate a bundled license file (to avoid collision names in
 * class-path). This file is by default generated in
 * {@code META-INF class-path directory}.
 *
 * @author tchemit <chemit@codelutin.com>
 * @goal update-project-license
 * @phase generate-resources
 * @requiresProject true
 * @since 1.0
 */
public class UpdateProjectLicenseMojo
    extends AbstractLicenseNameMojo
{

    /**
     * Project license file to synchronize with main license defined in
     * descriptor file.
     *
     * @parameter expression="${license.licenceFile}" default-value="${basedir}/LICENSE.txt"
     * @required
     * @since 1.0
     */
    protected File licenseFile;

    /**
     * The directory where to generate license resources.
     * <p/>
     * <b>Note:</b> This option is not available for {@code pom} module types.
     *
     * @parameter expression="${license.outputDirectory}"  default-value="target/generated-sources/license"
     * @since 1.0
     */
    protected File outputDirectory;
    
    /**
     * A flag to stop creating license file in project root of resource directory
     * <p/>
     * This is usefull to skip license information in root of package if you use
     * license information generated for bundle
     * <p/>
     * If sets to {@code true}, will not copy license file from
     * {@link #outputDirectory} to root of classes directory
     * 
     *  @parameter expression="${license.skipProjectLicense}" default-value="false"
     *  @since 1.6-nca-7
     * 
     */
    protected boolean skipProjectLicense;

    /**
     * A flag to copy the main license file in a bundled place.
     * <p/>
     * This is usefull for final application to have a none confusing location
     * to seek for the application license.
     * <p/>
     * If Sets to {@code true}, will copy the license file to the
     * {@link #bundleLicensePath} to {@link #outputDirectory}.
     * <p/>
     * <b>Note:</b> This option is not available for {@code pom} module types.
     *
     * @parameter expression="${license.generateBundle}"  default-value="false"
     * @since 1.0
     */
    protected boolean generateBundle;

    /**
     * The path of the bundled license file to produce when
     * {@link #generateBundle} is on.
     * <p/>
     * <b>Note:</b> This option is not available for {@code pom} module types.
     *
     * @parameter expression="${license.bundleLicensePath}"  default-value="META-INF/${project.artifactId}-LICENSE.txt"
     * @since 1.0
     */
    protected String bundleLicensePath;

    /**
     * A flag to force to generate project license file even if it is up-to-date.
     *
     * @parameter expression="${license.force}"  default-value="false"
     * @since 1.0.0
     */
    protected boolean force;

    /**
     * A flag to skip the goal.
     *
     * @parameter expression="${license.skipUpdateProjectLicense}" default-value="false"
     * @since 1.0
     */
    protected boolean skipUpdateProjectLicense;

    /**
     * Flag to known if generate is needed.
     */
    private boolean doGenerate;

    @Override
    protected void init()
        throws Exception
    {

        if ( isSkip() )
        {
            return;
        }

        super.init();

        // must generate if file does not exist or pom never thant license file
        File licenseFile = getLicenseFile();
        if ( licenseFile != null )
        {
            File pomFile = getProject().getFile();

            setDoGenerate( isForce() || !licenseFile.exists() || licenseFile.lastModified() <= pomFile.lastModified() );
        }
    }

    @Override
    protected void doAction()
        throws Exception
    {

        License license = getLicense();

        File target = getLicenseFile();

        if ( isDoGenerate() )
        {

            getLog().info( "Will create or update license file [" + license.getName() + "] to " + target );
            if ( isVerbose() )
            {
                getLog().info( "detail of license :\n" + license );
            }

            if ( target.exists() && isKeepBackup() )
            {
                if ( isVerbose() )
                {
                    getLog().info( "backup " + target );
                }
                // copy it to backup file
                FileUtil.backupFile( target );
            }
        }

        // obtain license content
        String licenseContent = license.getLicenseContent( getEncoding() );

        if ( isDoGenerate() )
        {

            // writes it root main license file
            FileUtil.writeString( target, licenseContent, getEncoding() );
        }

        if ( hasClassPath() )
        {
            File resourceTarget = new File( getOutputDirectory(), target.getName() );
            if (!skipProjectLicense()) 
            {
                // copy the license file to the resources directory
                FileUtil.copyFile( target, resourceTarget );
    
                addResourceDir( getOutputDirectory(), "**/" + resourceTarget.getName() );
            }
            if ( isGenerateBundle() )
            {

                // creates the bundled license file
                File bundleTarget = FileUtil.getFile( getOutputDirectory(), getBundleLicensePath() );
                FileUtil.copyFile( target, bundleTarget );

                if ( !resourceTarget.getName().equals( bundleTarget.getName() ) | !resourceTarget.exists() )
                {

                    addResourceDir( getOutputDirectory(), "**/" + bundleTarget.getName() );
                }
            }


        }
    }
    
    public boolean skipProjectLicense()
    {
        return skipProjectLicense;
    }

    public File getLicenseFile()
    {
        return licenseFile;
    }

    public boolean isGenerateBundle()
    {
        return generateBundle;
    }

    public File getOutputDirectory()
    {
        return outputDirectory;
    }

    public String getBundleLicensePath()
    {
        return bundleLicensePath;
    }

    public boolean isDoGenerate()
    {
        return doGenerate;
    }

    public boolean isForce()
    {
        return force;
    }

    @Override
    public boolean isSkip()
    {
        return skipUpdateProjectLicense;
    }

    public void setLicenseFile( File licenseFile )
    {
        this.licenseFile = licenseFile;
    }

    public void setGenerateBundle( boolean generateBundle )
    {
        this.generateBundle = generateBundle;
    }

    public void setOutputDirectory( File outputDirectory )
    {
        this.outputDirectory = outputDirectory;
    }

    public void setBundleLicensePath( String bundleLicensePath )
    {
        this.bundleLicensePath = bundleLicensePath;
    }

    public void setDoGenerate( boolean doGenerate )
    {
        this.doGenerate = doGenerate;
    }

    @Override
    public void setSkip( boolean skipUpdateProjectLicense )
    {
        this.skipUpdateProjectLicense = skipUpdateProjectLicense;
    }

    public void setForce( boolean force )
    {
        this.force = force;
    }
}
