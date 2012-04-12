package com.sinnerschrader.minificator;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.io.Writer;
import java.net.URL;

import org.apache.commons.io.FileUtils;
import org.mozilla.javascript.Context;
import org.mozilla.javascript.Function;
import org.mozilla.javascript.Scriptable;
import org.mozilla.javascript.tools.shell.Global;

import com.yahoo.platform.yui.compressor.CssCompressor;

/**
 * @author marwol
 */
public class Less {

  private final Logger logger;

  private File lessJs;

  private File lesscssInputFile;

  private File lesscssOutputFile;

  /**
   * @param logger
   */
  public Less(Logger logger) {
    this.logger = logger;
  }

  /**
   * @return the lessJs
   */
  public final File getLessJs() {
    return this.lessJs;
  }

  /**
   * @param lessJs
   *          the lessJs to set
   */
  public final void setLessJs(File lessJs) {
    this.lessJs = lessJs;
  }

  /**
   * @return the lesscssInputFile
   */
  public final File getLesscssInputFile() {
    return this.lesscssInputFile;
  }

  /**
   * @param lesscssInputFile
   *          the lesscssInputFile to set
   */
  public final void setLesscssInputFile(File lesscssInputFile) {
    this.lesscssInputFile = lesscssInputFile;
  }

  /**
   * @return the lesscssOutputFile
   */
  public final File getLesscssOutputFile() {
    return this.lesscssOutputFile;
  }

  /**
   * @param lesscssOutputFile
   *          the lesscssOutputFile to set
   */
  public final void setLesscssOutputFile(File lesscssOutputFile) {
    this.lesscssOutputFile = lesscssOutputFile;
  }

  /**
   * @throws ExecutionException
   */
  public void run() throws ExecutionException {
    Context context = Context.enter();
    try {
      Global global = new Global();
      global.init(context);
      Scriptable scope = context.initStandardObjects(global);

      URL embed = getClass().getResource("/embed.js");
      URL parser = getClass().getResource("/less-1.0.38.js");

      context.evaluateReader(scope, new InputStreamReader(embed.openStream()), embed.getFile(), 1, null);
      if (lessJs == null) {
        context.evaluateReader(scope, new InputStreamReader(parser.openStream()), parser.getFile(), 1, null);
      } else {
        context.evaluateReader(scope, new FileReader(lessJs), lessJs.getName(), 1, null);
      }

      FileUtils.forceMkdir(lesscssOutputFile.getParentFile());
      Writer writer = new FileWriter(lesscssOutputFile);
      try {
        String css = (String) Context.call(null, (Function) scope.get("compileFile", scope), scope, scope, new Object[] { "file:"
            + lesscssInputFile.getAbsolutePath() });
        new CssCompressor(new StringReader(css)).compress(writer, -1);
        logger.info("Wrote css to " + lesscssOutputFile);
      } finally {
        writer.close();
      }

    } catch (IOException e) {
      throw new ExecutionException("Failed to compile less scripts", e);
    } finally {
      Context.exit();
    }
  }

}
