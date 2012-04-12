package com.sinnerschrader.minificator.maven;

import java.io.File;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;

import com.sinnerschrader.minificator.CssEmbed;
import com.sinnerschrader.minificator.ExecutionException;

/**
 * @author marwol
 * @goal cssembed
 * @phase process-resources
 */
public class CssEmbedMojo extends AbstractMojo {

  /**
   * @parameter
   */
  private File cssembedInputFile;

  /**
   * @parameter
   */
  private File cssembedOutputFile;

  /**
   * @parameter
   */
  private String cssembedRoot;

  @Override
  public void execute() throws MojoExecutionException, MojoFailureException {
    try {
      CssEmbed cssEmbed = new CssEmbed();
      cssEmbed.setCssembedInputFile(cssembedInputFile);
      cssEmbed.setCssembedOutputFile(cssembedOutputFile);
      cssEmbed.setCssembedRoot(cssembedRoot);
      cssEmbed.run();
    } catch (ExecutionException e) {
      throw new MojoExecutionException("Failed to execute css-embed mojo", e);
    }
  }

}
