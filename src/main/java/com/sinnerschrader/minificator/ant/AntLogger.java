package com.sinnerschrader.minificator.ant;

import org.apache.tools.ant.Task;

import com.sinnerschrader.minificator.Logger;

/**
 * @author marwol
 */
public class AntLogger implements Logger {

  private final Task task;

  /**
   * @param task
   */
  public AntLogger(Task task) {
    this.task = task;
  }

  /**
   * @see com.sinnerschrader.minificator.Logger#info(java.lang.String)
   */
  @Override
  public void info(String message) {
    task.log(message);
  }

}
