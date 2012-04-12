package com.sinnerschrader.minificator;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.LineIterator;
import org.codehaus.jackson.map.ObjectMapper;

import com.google.javascript.jscomp.CompilationLevel;
import com.google.javascript.jscomp.Compiler;
import com.google.javascript.jscomp.CompilerOptions;
import com.google.javascript.jscomp.JSSourceFile;
import com.google.javascript.jscomp.Result;

/**
 * @author marwol
 */
public class Closure {

  private Logger logger;

  private boolean isJson;

  private File baseDir;

  @Deprecated
  private String closureBasePath;

  private String[] closureBasePaths;

  @Deprecated
  private String closureSourceFile;

  private String[] closureSourceFiles;

  private File closureTargetFile;

  private String closurePreScript;

  private String closurePostScript;

  private File closurePreScriptFile;

  private File closurePostScriptFile;

  private CompilationLevel closureOptimization = getCompilationLevel("SIMPLE_OPTIMIZATIONS");

  /**
   * @param logger
   */
  public Closure(Logger logger) {
    this.logger = logger;
  }

  /**
   * @return the logger
   */
  public final Logger getLogger() {
    return this.logger;
  }

  /**
   * @param logger
   *          the logger to set
   */
  public final void setLogger(Logger logger) {
    this.logger = logger;
  }

  /**
   * @return the isJson
   */
  public final boolean isJson() {
    return this.isJson;
  }

  /**
   * @param isJson
   *          the isJson to set
   */
  public final void setJson(boolean isJson) {
    this.isJson = isJson;
  }

  /**
   * @return the baseDir
   */
  public final File getBaseDir() {
    return this.baseDir;
  }

  /**
   * @param baseDir
   *          the baseDir to set
   */
  public final void setBaseDir(File baseDir) {
    this.baseDir = baseDir;
  }

  /**
   * @return the closureTargetFile
   */
  public final File getClosureTargetFile() {
    return this.closureTargetFile;
  }

  /**
   * @param closureTargetFile
   *          the closureTargetFile to set
   */
  public final void setClosureTargetFile(File closureTargetFile) {
    this.closureTargetFile = closureTargetFile;
  }

  /**
   * @return the closureOptimization
   */
  public final CompilationLevel getClosureOptimization() {
    return this.closureOptimization;
  }

  /**
   * @param closureOptimization
   *          the closureOptimization to set
   */
  public final void setClosureOptimization(CompilationLevel closureOptimization) {
    this.closureOptimization = closureOptimization;
  }

  /**
   * @return the closureBasePath
   */
  public final String getClosureBasePath() {
    return this.closureBasePath;
  }

  /**
   * @return the closureSourceFile
   */
  public final String getClosureSourceFile() {
    return this.closureSourceFile;
  }

  /**
   * @return the closurePreScript
   */
  public final String getClosurePreScript() {
    return this.closurePreScript;
  }

  /**
   * @return the closurePostScript
   */
  public final String getClosurePostScript() {
    return this.closurePostScript;
  }

  /**
   * @return the closurePreScriptFile
   */
  public final File getClosurePreScriptFile() {
    return this.closurePreScriptFile;
  }

  /**
   * @return the closurePostScriptFile
   */
  public final File getClosurePostScriptFile() {
    return this.closurePostScriptFile;
  }

  /**
   * @param closurePreScript
   *          the closurePreScript to set
   */
  public final void setClosurePreScript(String closurePreScript) {
    this.closurePreScript = closurePreScript;
  }

  /**
   * @param closurePostScript
   *          the closurePostScript to set
   */
  public final void setClosurePostScript(String closurePostScript) {
    this.closurePostScript = closurePostScript;
  }

  /**
   * @param closurePreScriptFile
   */
  public void setClosurePreScriptFile(File closurePreScriptFile) {
    this.closurePreScriptFile = closurePreScriptFile;
  }

  /**
   * @param closurePostScriptFile
   */
  public void setClosurePostScriptFile(File closurePostScriptFile) {
    this.closurePostScriptFile = closurePostScriptFile;
  }

  /**
   * @param closureBasePath
   *          the closureBasePath to set
   */
  public final void setClosureBasePath(String closureBasePath) {
    closureBasePaths = new String[] { closureBasePath };
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

  private static CompilationLevel getCompilationLevel(String level) {
    return CompilationLevel.valueOf(level);
  }

  /**
   * @param optimization
   *          The closure optimization level
   */
  public final void setClosureOptimization(String optimization) {
    final CompilationLevel cl = getCompilationLevel(optimization);
    if (cl != null) {
      this.closureOptimization = cl;
    }
  }

  /**
   * @throws ExecutionException
   */
  public void run() throws ExecutionException {
    final Compiler compiler = new Compiler();
    final CompilerOptions options = new CompilerOptions();
    closureOptimization.setOptionsForCompilationLevel(options);

    final List<JSSourceFile> externs = Collections.emptyList();
    final List<JSSourceFile> sources = new ArrayList<JSSourceFile>();
    if (closurePreScript != null) {
      getLogger().info("Include PreScript:");
      sources.add(JSSourceFile.fromCode("closurePreScript.js", closurePreScript));
    }
    getFiles(sources);
    if (closurePostScript != null) {
      getLogger().info("Include PostScript:");
      sources.add(JSSourceFile.fromCode("closurePostScript.js", closurePostScript));
    }

    getLogger().info("Got " + sources.size() + " source files");
    final Result result = compiler.compile(externs, sources, options);
    if (!result.success) {
      throw new ExecutionException("Failed to compile scripts");
    }

    getLogger().info("Writing compiled js to " + closureTargetFile);
    try {
      FileUtils.forceMkdir(closureTargetFile.getParentFile());
      final FileWriter writer = new FileWriter(closureTargetFile);
      try {
        String preSource = closurePreScriptFile != null ? FileUtils.readFileToString(closurePreScriptFile) : "";
        String postSource = closurePostScriptFile != null ? FileUtils.readFileToString(closurePostScriptFile) : "";

        StringBuilder sb = new StringBuilder();
        sb.append(preSource).append(compiler.toSource()).append(postSource);
        writer.write(sb.toString());
      } finally {
        writer.close();
      }
    } catch (IOException e) {
      throw new ExecutionException("Failed to write target file: " + closureTargetFile, e);
    }

  }

  private List<JSSourceFile> getFiles(List<JSSourceFile> files) throws ExecutionException {
    if (closureSourceFiles != null && closureSourceFiles.length > 0)
      for (final String fileName : closureSourceFiles) {
        for (String path : closureBasePaths) {
          final File sourceFile = new File(new File(baseDir, path), fileName);
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

  private List<JSSourceFile> getFiles(File closureSourceFile) throws ExecutionException {
    try {
      final List<JSSourceFile> sources = new ArrayList<JSSourceFile>();

      boolean inResourceBlock = false;
      final LineIterator it = FileUtils.lineIterator(closureSourceFile);
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
            getLogger().info("Source File: " + sourceFile.getAbsolutePath());
            sources.add(JSSourceFile.fromFile(sourceFile));
          }
        }
      }

      return sources;
    } catch (IOException e) {
      throw new ExecutionException("Failed to read source files: " + closureSourceFile, e);
    }
  }

  private List<JSSourceFile> getFilesFromJson(final File sourceFile) throws ExecutionException {
    try {
      getLogger().info("sourceFile JSON: " + sourceFile);
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
        getLogger().info("Source File: " + file.getAbsolutePath());
        sources.add(JSSourceFile.fromFile(file));
      }
      return sources;
    } catch (IOException e) {
      throw new ExecutionException("Failed to read source files: " + sourceFile, e);
    }
  }

}
