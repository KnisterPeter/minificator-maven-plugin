package com.sinnerschrader.maven.minificator;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.io.Writer;
import java.net.URL;

import org.apache.commons.io.FileUtils;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.mozilla.javascript.Context;
import org.mozilla.javascript.Function;
import org.mozilla.javascript.Scriptable;
import org.mozilla.javascript.tools.shell.Global;

import com.yahoo.platform.yui.compressor.CssCompressor;

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
        getLog().info("Wrote css to " + lesscssOutputFile);
      } finally {
        writer.close();
      }

    } catch (IOException e) {
      throw new MojoExecutionException("Failed to compile less scripts", e);
    } finally {
      Context.exit();
    }
  }
}
