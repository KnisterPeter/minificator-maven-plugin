package com.sinnerschrader.minificator;

/**
 * @author marwol
 */
public class ExecutionException extends Exception {

  private static final long serialVersionUID = -4411184832690323224L;

  /**
   * @param message
   * @param cause
   */
  public ExecutionException(String message, Throwable cause) {
    super(message, cause);
  }

  /**
   * @param message
   */
  public ExecutionException(String message) {
    super(message);
  }

}
