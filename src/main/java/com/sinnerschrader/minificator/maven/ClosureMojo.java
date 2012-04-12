package com.sinnerschrader.minificator.maven;

import java.io.File;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;

import com.sinnerschrader.minificator.Closure;
import com.sinnerschrader.minificator.ExecutionException;

/**
 * @author marwol
 * @goal closure
 * @phase process-resources
 */
public class ClosureMojo extends AbstractMojo {

  private final Closure closure;

  /**
   * @param isJson
   *          the isJson to set
   * 
   * @parameter
   */
  public final void setJson(boolean isJson) {
    closure.setJson(isJson);
  }

  /**
   * @param baseDir
   *          the baseDir to set
   * 
   * @parameter expression="${basedir}"
   */
  public final void setBaseDir(File baseDir) {
    closure.setBaseDir(baseDir);
  }

  /**
   * @param closureTargetFile
   *          the closureTargetFile to set
   * 
   * @parameter
   */
  public final void setClosureTargetFile(File closureTargetFile) {
    closure.setClosureTargetFile(closureTargetFile);
  }

  /**
   * @param closurePreScript
   *          the closurePreScript to set
   * 
   * @parameter
   */
  public final void setClosurePreScript(String closurePreScript) {
    closure.setClosurePreScript(closurePreScript);
  }

  /**
   * @param closurePostScript
   *          the closurePostScript to set
   * 
   * @parameter
   */
  public final void setClosurePostScript(String closurePostScript) {
    closure.setClosurePostScript(closurePostScript);
  }

  /**
   * @param closurePreScriptFile
   * 
   * @parameter
   */
  public void setClosurePreScriptFile(File closurePreScriptFile) {
    closure.setClosurePreScriptFile(closurePreScriptFile);
  }

  /**
   * @param closurePostScriptFile
   * 
   * @parameter
   */
  public void setClosurePostScriptFile(File closurePostScriptFile) {
    closure.setClosurePostScriptFile(closurePostScriptFile);
  }

  /**
   * @param closureBasePath
   *          the closureBasePath to set
   * 
   * @parameter
   * @deprecated Use {{@link #closureBasePaths} instead
   */
  @Deprecated
  public final void setClosureBasePath(String closureBasePath) {
    closure.setClosureBasePath(closureBasePath);
  }

  /**
   * @param closureBasePaths
   *          the closureBasePaths to set
   * 
   * @parameter
   */
  public final void setClosureBasePaths(String[] closureBasePaths) {
    closure.setClosureBasePaths(closureBasePaths);
  }

  /**
   * @param closureSourceFile
   *          the closureSourceFile to set
   * 
   * @parameter
   * @deprecated Use {{@link #closureSourceFiles} instead
   */
  @Deprecated
  public final void setClosureSourceFile(String closureSourceFile) {
    closure.setClosureSourceFile(closureSourceFile);
  }

  /**
   * @param closureSourceFiles
   *          the closureSourceFiles to set
   * 
   * @parameter
   */
  public final void setClosureSourceFiles(String[] closureSourceFiles) {
    closure.setClosureSourceFiles(closureSourceFiles);
  }

  /**
   * 
   */
  public ClosureMojo() {
    closure = new Closure(new MavenLogger(this));
  }

  /**
   * @param optimization
   *          The closure optimization level
   * 
   * @parameter expression=
   *            "${SIMPLE_OPTIMIZATIONS,ADVANCED_OPTIMIZATIONS,WHITESPACE_ONLY}"
   */
  public final void setClosureOptimization(String optimization) {
    closure.setClosureOptimization(optimization);
  }

  @Override
  public void execute() throws MojoExecutionException, MojoFailureException {
    try {
      closure.run();
    } catch (ExecutionException e) {
      throw new MojoExecutionException("Failed to execute closure mojo", e);
    }
  }

}
