package com.sinnerschrader.minificator.ant;

import java.io.File;

import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Task;

import com.sinnerschrader.minificator.CssEmbed;
import com.sinnerschrader.minificator.ExecutionException;

/**
 * @author marwol
 */
public class CssEmbedTask extends Task {

  private CssEmbed cssEmbed;

  /**
   * @param input
   *          the input to set
   */
  public final void setInput(File input) {
    cssEmbed.setCssembedInputFile(input);
  }

  /**
   * @param output
   *          the output to set
   */
  public final void setOutput(File output) {
    cssEmbed.setCssembedOutputFile(output);
  }

  /**
   * @param root
   *          the root to set
   */
  public final void setRoot(String root) {
    cssEmbed.setCssembedRoot(root);
  }

  /**
   * @see org.apache.tools.ant.Task#init()
   */
  @Override
  public void init() throws BuildException {
    super.init();
    cssEmbed = new CssEmbed();
  }

  /**
   * @see org.apache.tools.ant.Task#execute()
   */
  @Override
  public void execute() throws BuildException {
    try {
      cssEmbed.run();
    } catch (ExecutionException e) {
      throw new BuildException(e);
    }
  }

}
