package com.sinnerschrader.minificator.maven;

import org.apache.maven.plugin.AbstractMojo;

import com.sinnerschrader.minificator.Logger;

/**
 * @author marwol
 */
class MavenLogger implements Logger {

  private final AbstractMojo mojo;

  /**
   * @param mojo
   */
  public MavenLogger(AbstractMojo mojo) {
    this.mojo = mojo;
  }

  /**
   * @see com.sinnerschrader.minificator.Logger#info(java.lang.String)
   */
  @Override
  public void info(String message) {
    mojo.getLog().info(message);
  }

}
