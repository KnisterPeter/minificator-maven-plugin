package com.sinnerschrader.maven.minificator;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.LineIterator;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.codehaus.jackson.map.ObjectMapper;

import com.google.javascript.jscomp.CompilationLevel;
import com.google.javascript.jscomp.Compiler;
import com.google.javascript.jscomp.CompilerOptions;
import com.google.javascript.jscomp.JSSourceFile;
import com.google.javascript.jscomp.Result;

/**
 * @author marwol
 * @goal closure
 * @phase process-resources
 */
public class ClosureMojo extends AbstractMojo {

  /**
   * @parameter
   */
  private boolean isJson;

  /**
   * @parameter expression="${basedir}"
   */
  private File baseDir;

  /**
   * @parameter
   * @deprecated Use {{@link #closureBasePaths} instead
   */
  @Deprecated
  private String closureBasePath;

  /**
   * @parameter
   */
  private String[] closureBasePaths;

  /**
   * @parameter
   * @deprecated Use {{@link #closureSourceFiles} instead
   */
  @Deprecated
  private String closureSourceFile;

  /**
   * @parameter
   */
  private String[] closureSourceFiles;

  /**
   * @parameter
   */
  private File closureTargetFile;

  /**
   * @parameter
   */
  private String closurePreScript;

  /**
   * @param closurePreScript
   *          the closurePreScript to set
   */
  public final void setClosurePreScript(String closurePreScript) {
    getLog().info("Set PreScript:"+closurePreScript);
    this.closurePreScript = closurePreScript;
  }
  
  /**
   * @parameter
   */
  private String closurePostScript;

  /**
   * @param closurePostScript
   *          the closurePostScript to set
   */
  public final void setClosurePostScript(String closurePostScript) {
    getLog().info("Set PostScript:"+closurePostScript);
    this.closurePostScript = closurePostScript;
  }


  /**
   * @param closureBasePath
   *          the closureBasePath to set
   */
  public final void setClosureBasePath(String closureBasePath) {
    this.closureBasePaths = new String[] { closureBasePath };
  }

  /**
   * @param closureBasePaths
   *          the closureBasePaths to set
   */
  public final void setClosureBasePaths(String[] closureBasePaths) {
    this.closureBasePaths = closureBasePaths;
  }

  /**
   * @param closureSourceFile
   *          the closureSourceFile to set
   */
  public final void setClosureSourceFile(String closureSourceFile) {
    this.closureSourceFiles = new String[] { closureSourceFile };
  }

  /**
   * @param closureSourceFiles
   *          the closureSourceFiles to set
   */
  public final void setClosureSourceFiles(String[] closureSourceFiles) {
    this.closureSourceFiles = closureSourceFiles;
  }

  @Override
  public void execute() throws MojoExecutionException, MojoFailureException {
    Compiler compiler = new Compiler();
    CompilerOptions options = new CompilerOptions();
    CompilationLevel.SIMPLE_OPTIMIZATIONS.setOptionsForCompilationLevel(options);

    List<JSSourceFile> externs = Collections.emptyList();
    List<JSSourceFile> sources = new ArrayList<JSSourceFile>();
    if (closurePreScript != null) {
      getLog().info("Include PreScript:");
    	sources.add(JSSourceFile.fromCode("closurePreScript.js", closurePreScript));
    }
    getFiles(sources);
    if (closurePostScript != null) {
      getLog().info("Include PostScript:");
    	sources.add(JSSourceFile.fromCode("closurePostScript.js", closurePostScript));
    }

    getLog().info("Got " + sources.size() + " source files");
    Result result = compiler.compile(externs, sources, options);
    if (!result.success) {
      throw new MojoExecutionException("Failed to compile scripts");
    }

    getLog().info("Writing compiled js to " + closureTargetFile);
    try {
      FileUtils.forceMkdir(closureTargetFile.getParentFile());
      FileWriter writer = new FileWriter(closureTargetFile);
      try {
        writer.write(compiler.toSource());
      } finally {
        writer.close();
      }
    } catch (IOException e) {
      throw new MojoExecutionException("Failed to write target file: " + closureTargetFile, e);
    }
  }

  private List<JSSourceFile> getFiles(List<JSSourceFile> files) throws MojoExecutionException {
    if (closureSourceFiles != null && closureSourceFiles.length > 0)
      for (final String fileName : closureSourceFiles) {
        for (String path : closureBasePaths) {
          File sourceFile = new File(new File(baseDir, path), fileName);
          if (!sourceFile.exists())
            continue;
          if (isJson) {
            files.addAll(getFilesFromJson(sourceFile));
          } else
            files.addAll(getFiles(sourceFile));
        }
      }
    return files;
  }

  private List<JSSourceFile> getFiles(File closureSourceFile) throws MojoExecutionException {
    try {
      List<JSSourceFile> sources = new ArrayList<JSSourceFile>();

      boolean inResourceBlock = false;
      LineIterator it = FileUtils.lineIterator(closureSourceFile);
      while (it.hasNext()) {
        String line = it.next().trim();
        if ("// --START-RESOURCES--".equals(line)) {
          inResourceBlock = true;
        } else if ("// --END-RESOURCES--".equals(line)) {
          inResourceBlock = false;
        } else if (inResourceBlock) {
          String file = line.replaceAll("[\"',]", "");
          File sourceFile = null;
          for (String path : closureBasePaths) {
            File base = new File(this.baseDir, path);
            if (new File(base, file).exists()) {
              sourceFile = new File(base, file);
              break;
            }
          }
          if (sourceFile == null) {
            throw new FileNotFoundException("Failed to locate '" + file + "' in any base path");
          }
          if (FileUtils.readFileToString(sourceFile).contains("// --START-RESOURCES--")) {
            sources.addAll(getFiles(sourceFile));
          } else {
            getLog().info("Source File: " + sourceFile.getAbsolutePath());
            sources.add(JSSourceFile.fromFile(sourceFile));
          }
        }
      }

      return sources;
    } catch (IOException e) {
      throw new MojoExecutionException("Failed to read source files: " + closureSourceFile, e);
    }
  }

  private List<JSSourceFile> getFilesFromJson(final File sourceFile) throws MojoExecutionException {
    try {
      getLog().info("sourceFile JSON: " + sourceFile);
      final List<JSSourceFile> sources = new ArrayList<JSSourceFile>();
      for (final String fileName : new ObjectMapper().readValue(sourceFile, String[].class)) {
        File file = null;
        for (String path : closureBasePaths) {
          File base = new File(this.baseDir, path);
          if (new File(base, fileName).exists()) {
            file = new File(base, fileName);
            break;
          }
        }
        if (file == null) {
          throw new FileNotFoundException("Failed to locate '" + fileName + "' in any base path");
        }
        getLog().info("Source File: " + file.getAbsolutePath());
        sources.add(JSSourceFile.fromFile(file));
      }
      return sources;
    } catch (IOException e) {
      throw new MojoExecutionException("Failed to read source files: " + sourceFile, e);
    }
  }

}
