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

import java.io.File;
import java.util.Arrays;

import org.apache.commons.lang.StringUtils;
import org.apache.maven.execution.MavenSession;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.project.MavenProject;
import org.codehaus.plexus.util.ReaderFactory;

/**
 * Abstract license mojo.
 *
 * @author tchemit <chemit@codelutin.com>
 * @since 1.0
 */
public abstract class AbstractLicenseMojo extends AbstractMojo {

    /**
     * Current maven session. (used to launch certain mojo once by build).
     *
     * @parameter expression="${session}"
     * @required
     * @readonly
     * @since 1.0
     */
    private MavenSession session;

    /**
     * The reacted project.
     *
     * @parameter default-value="${project}"
     * @required
     * @since 1.0
     */
    private MavenProject project;

    /**
     * Flag to activate verbose mode.
     * <p/>
     * <b>Note:</b> Verbose mode is always on if you starts a debug maven instance (says via {@code -X}).
     *
     * @parameter expression="${license.verbose}" default-value="${maven.verbose}"
     * @since 1.0
     */
    private boolean verbose;

    /**
     * Encoding used to read and writes files.
     * <p/>
     * <b>Note:</b> If nothing is filled here, we will use the system property {@code file.encoding}.
     *
     * @parameter expression="${license.encoding}" default-value="${project.build.sourceEncoding}"
     * @since 1.0
     */
    private String encoding;

    public final String getEncoding() {
        return encoding;
    }

    public final void setEncoding(String encoding) {
        this.encoding = encoding;
    }

    /**
     * Method to initialize the mojo before doing any concrete actions.
     * <p/>
     * <b>Note:</b> The method is invoked before the {@link #doAction()} method.
     *
     * @throws Exception
     *             if any
     */
    protected abstract void init() throws Exception;

    /**
     * Do plugin action.
     * <p/>
     * The method {@link #execute()} invoke this method only and only if :
     * <ul>
     * <li>{@link #checkPackaging()} returns {@code true}.</li>
     * <li>method {@link #init()} returns {@code true}.</li>
     * </ul>
     *
     * @throws Exception
     *             if any
     */
    protected abstract void doAction() throws Exception;

    @Override
    public final void execute() throws MojoExecutionException, MojoFailureException {
        try {
            if (getLog().isDebugEnabled()) {

                // always be verbose in debug mode
                setVerbose(true);
            }

            // check if project packaging is compatible with the mojo

            boolean canContinue = checkPackaging();
            if (!canContinue) {
                getLog().debug("Skip for packaging '" + getProject().getPackaging() + "'");
                return;
            }

            // init the mojo

            try {

                checkEncoding();

                init();

            } catch (MojoFailureException e) {
                throw e;
            } catch (MojoExecutionException e) {
                throw e;
            } catch (Exception e) {
                throw new MojoExecutionException("could not init goal " + getClass().getSimpleName() + " for reason : "
                        + e.getMessage(), e);
            }

            // check if mojo can be skipped

            canContinue = checkSkip();
            if (!canContinue) {
                if (isVerbose()) {
                    getLog().info("Goal will not be executed.");
                }
                return;
            }

            // can really execute the mojo

            try {

                doAction();

            } catch (MojoFailureException e) {
                throw e;
            } catch (MojoExecutionException e) {
                throw e;
            } catch (Exception e) {
                throw new MojoExecutionException("could not execute goal " + getClass().getSimpleName()
                        + " for reason : " + e.getMessage(), e);
            }
        } finally {
            afterExecute();
        }
    }

    /**
     * A call back to execute after the {@link #execute()} is done
     */
    protected void afterExecute() {
        // by default do nothing
    }

    /**
     * Check if the project packaging is acceptable for the mojo.
     * <p/>
     * By default, accept all packaging types.
     * <p/>
     * <b>Note:</b> This method is the first instruction to be executed in the {@link #execute()}.
     * <p/>
     * <b>Tip:</b> There is two method to simplify the packaging check :
     * <p/>
     * {@link #acceptPackaging(String...)}
     * <p/>
     * and
     * <p/>
     * {@link #rejectPackaging(String...)}
     *
     * @return {@code true} if can execute the goal for the packaging of the project, {@code false} otherwise.
     */
    protected boolean checkPackaging() {
        // by default, accept every type of packaging
        return true;
    }

    /**
     * Checks if the mojo execution should be skipped.
     *
     * @return {@code false} if the mojo should not be executed.
     */
    protected boolean checkSkip() {
        // by default, never skip goal
        return true;
    }

    /**
     * Accept the project's packaging between some given.
     *
     * @param packages
     *            the accepted packaging
     * @return {@code true} if the project's packaging is one of the given ones.
     */
    protected boolean acceptPackaging(String... packages) {
        String projectPackaging = getProject().getPackaging();

        for (String p : packages) {
            if (p.equals(projectPackaging)) {
                // accept packaging
                return true;
            }
        }
        // reject packaging
        return false;
    }

    /**
     * Accept the project's packaging if not in given one.
     *
     * @param packages
     *            the rejecting packagings
     * @return {@code true} if the project's packaging is not in the given ones.
     */
    protected boolean rejectPackaging(String... packages) {
        String projectPackaging = getProject().getPackaging();

        for (String p : packages) {
            if (p.equals(projectPackaging)) {
                // reject this packaging
                return false;
            }
        }
        // accept packaging
        return true;
    }

    /**
     * Method to be invoked in init phase to check sanity of {@link #getEncoding()}.
     * <p/>
     * If no encoding was filled, then use the default for system (via {@code file.encoding} environement property).
     */
    protected void checkEncoding() {

        if (isVerbose()) {
            getLog().info("Will check encoding : " + getEncoding());
        }
        if (StringUtils.isEmpty(getEncoding())) {
            getLog().warn(
                    "File encoding has not been set, using platform encoding " + ReaderFactory.FILE_ENCODING
                            + ", i.e. build is platform dependent!");
            setEncoding(ReaderFactory.FILE_ENCODING);
        }
    }

    public final MavenProject getProject() {
        return project;
    }

    public final void setProject(MavenProject project) {
        this.project = project;
    }

    public final boolean isVerbose() {
        return verbose;
    }

    public final void setVerbose(boolean verbose) {
        this.verbose = verbose;
    }

    public final MavenSession getSession() {
        return session;
    }

    public final void setSession(MavenSession session) {
        this.session = session;
    }

    public final long getBuildTimestamp() {
        return session.getStartTime().getTime();
    }

    /**
     * Add a new resource location to the maven project (in not already present).
     *
     * @param dir
     *            the new resource location to add
     * @param includes
     *            files to include
     */
    protected void addResourceDir(File dir, String... includes) {
        boolean added = MojoHelper.addResourceDir(dir, getProject(), includes);
        if (added && isVerbose()) {
            getLog().info("add resource " + dir + " with includes " + Arrays.toString(includes));
        }
    }

    /**
     * @return {@code true} if project is not a pom, {@code false} otherwise.
     */
    protected boolean hasClassPath() {
        return rejectPackaging("pom");
    }

}
