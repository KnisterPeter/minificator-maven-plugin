package com.sinnerschrader.maven.minificator;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.Reader;
import java.io.Writer;

import net.nczonline.web.cssembed.CSSURLEmbedder;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;

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
      Reader in = new FileReader(cssembedInputFile);
      CSSURLEmbedder embedder = new CSSURLEmbedder(in, CSSURLEmbedder.DATAURI_OPTION, false);
      in.close();

      if (cssembedRoot == null) {
        cssembedRoot = cssembedInputFile.getParentFile().getCanonicalPath();
      }
      if (!cssembedRoot.endsWith(File.separator)) {
        cssembedRoot += File.separator;
      }

      Writer out = new FileWriter(cssembedOutputFile);
      embedder.embedImages(out, cssembedRoot);
      out.close();
    } catch (Exception e) {
      throw new MojoExecutionException("Failed to run cssembed", e);
    }
  }

}
