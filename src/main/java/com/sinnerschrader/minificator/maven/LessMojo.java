package com.sinnerschrader.minificator.maven;

import java.io.File;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;

import com.sinnerschrader.minificator.ExecutionException;
import com.sinnerschrader.minificator.Less;

/**
 * @author marwol
 * @goal lesscss
 * @phase process-resources
 */
public class LessMojo extends AbstractMojo {

  /**
   * @parameter
   */
  private File lessJs;

  /**
   * @parameter
   */
  private File lesscssInputFile;

  /**
   * @parameter
   */
  private File lesscssOutputFile;

  @Override
  public void execute() throws MojoExecutionException, MojoFailureException {
    try {
      Less less = new Less(new MavenLogger(this));
      less.setLesscssInputFile(lesscssInputFile);
      less.setLesscssOutputFile(lesscssOutputFile);
      less.setLessJs(lessJs);
      less.run();
    } catch (ExecutionException e) {
      throw new MojoExecutionException("Failed to execute less mojo", e);
    }
  }

}
