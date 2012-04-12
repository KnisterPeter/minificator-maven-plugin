package com.sinnerschrader.minificator.ant;

import java.io.File;

import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Task;

import com.sinnerschrader.minificator.ExecutionException;
import com.sinnerschrader.minificator.Less;

/**
 * @author marwol
 */
public class LessTask extends Task {

  private Less less;

  /**
   * @see org.apache.tools.ant.Task#init()
   */
  @Override
  public void init() throws BuildException {
    super.init();
    less = new Less(new AntLogger(this));
  }

  /**
   * @param lessJs
   *          the lessJs to set
   */
  public final void setLessJs(File lessJs) {
    less.setLessJs(lessJs);
  }

  /**
   * @param input
   *          the input to set
   */
  public final void setInput(File input) {
    less.setLesscssInputFile(input);
  }

  /**
   * @param output
   *          the output to set
   */
  public final void setOutput(File output) {
    less.setLesscssOutputFile(output);
  }

  /**
   * @see org.apache.tools.ant.Task#execute()
   */
  @Override
  public void execute() throws BuildException {
    try {
      less.run();
    } catch (ExecutionException e) {
      throw new BuildException(e);
    }
  }

}
