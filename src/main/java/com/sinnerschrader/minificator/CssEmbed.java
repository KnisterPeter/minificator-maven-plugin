package com.sinnerschrader.minificator;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.Reader;
import java.io.Writer;

import net.nczonline.web.cssembed.CSSURLEmbedder;

/**
 * @author marwol
 */
public class CssEmbed {

  private File cssembedInputFile;

  private File cssembedOutputFile;

  private String cssembedRoot;

  /**
   * @return the cssembedInputFile
   */
  public final File getCssembedInputFile() {
    return this.cssembedInputFile;
  }

  /**
   * @param cssembedInputFile
   *          the cssembedInputFile to set
   */
  public final void setCssembedInputFile(File cssembedInputFile) {
    this.cssembedInputFile = cssembedInputFile;
  }

  /**
   * @return the cssembedOutputFile
   */
  public final File getCssembedOutputFile() {
    return this.cssembedOutputFile;
  }

  /**
   * @param cssembedOutputFile
   *          the cssembedOutputFile to set
   */
  public final void setCssembedOutputFile(File cssembedOutputFile) {
    this.cssembedOutputFile = cssembedOutputFile;
  }

  /**
   * @return the cssembedRoot
   */
  public final String getCssembedRoot() {
    return this.cssembedRoot;
  }

  /**
   * @param cssembedRoot
   *          the cssembedRoot to set
   */
  public final void setCssembedRoot(String cssembedRoot) {
    this.cssembedRoot = cssembedRoot;
  }

  /**
   * @throws ExecutionException
   */
  public void run() throws ExecutionException {
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
      throw new ExecutionException("Failed to run cssembed", e);
    }
  }

}
