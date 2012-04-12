package com.sinnerschrader.minificator.ant;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Task;

import com.sinnerschrader.minificator.Closure;
import com.sinnerschrader.minificator.ExecutionException;

/**
 * @author marwol
 */
public class ClosureTask extends Task {

  private Closure closure;

  /**
   * @see org.apache.tools.ant.Task#init()
   */
  @Override
  public void init() throws BuildException {
    super.init();
    closure = new Closure(new AntLogger(this));
    closure.setBaseDir(super.getProject().getBaseDir());
  }

  /**
   * @param isJson
   *          the isJson to set
   */
  public final void setJson(boolean isJson) {
    closure.setJson(isJson);
  }

  /**
   * @param closureTargetFile
   *          the closureTargetFile to set
   */
  public final void setTarget(File closureTargetFile) {
    closure.setClosureTargetFile(closureTargetFile);
  }

  /**
   * @param optimization
   *          the closureOptimization to set
   */
  public final void setClosureOptimization(String optimization) {
    closure.setClosureOptimization(optimization);
  }

  /**
   * @param closurePreScript
   *          the closurePreScript to set
   */
  public final void setPreScript(String closurePreScript) {
    closure.setClosurePreScript(closurePreScript);
  }

  /**
   * @param closurePostScript
   *          the closurePostScript to set
   */
  public final void setPostScript(String closurePostScript) {
    closure.setClosurePostScript(closurePostScript);
  }

  /**
   * @param closurePreScriptFile
   */
  public void setPreScriptFile(File closurePreScriptFile) {
    closure.setClosurePreScriptFile(closurePreScriptFile);
  }

  /**
   * @param closurePostScriptFile
   */
  public void setPostScriptFile(File closurePostScriptFile) {
    closure.setClosurePostScriptFile(closurePostScriptFile);
  }

  /**
   * @param basePath
   *          the closureBasePath to set
   */
  public void setBasePath(String basePath) {
    closure.setClosureBasePath(basePath);
  }

  private final List<NestedString> basePaths = new ArrayList<ClosureTask.NestedString>();

  public NestedString createBasePath() {
    NestedString ns = new NestedString();
    basePaths.add(ns);
    return ns;
  }

  private String[] getBasePaths() {
    List<String> basePaths = new ArrayList<String>();
    for (NestedString ns : this.basePaths) {
      basePaths.add(ns.text);
    }
    return basePaths.toArray(new String[basePaths.size()]);
  }

  /**
   * @param sourceFile
   *          the closureSourceFile to set
   */
  public void setSourceFile(String sourceFile) {
    closure.setClosureSourceFile(sourceFile);
  }

  private final List<NestedString> sourceFiles = new ArrayList<ClosureTask.NestedString>();

  public NestedString createSourceFile() {
    NestedString ns = new NestedString();
    sourceFiles.add(ns);
    return ns;
  }

  private String[] getSourceFiles() {
    List<String> sourceFiles = new ArrayList<String>();
    for (NestedString ns : this.sourceFiles) {
      sourceFiles.add(ns.text);
    }
    return sourceFiles.toArray(new String[sourceFiles.size()]);
  }

  /**
   * @see org.apache.tools.ant.Task#execute()
   */
  @Override
  public void execute() throws BuildException {
    try {
      if (!basePaths.isEmpty())
        closure.setClosureBasePaths(getBasePaths());
      if (!sourceFiles.isEmpty())
        closure.setClosureSourceFiles(getSourceFiles());

      closure.run();
    } catch (ExecutionException e) {
      throw new BuildException(e);
    }
  }

  public static class NestedString {

    private String text;

    public void addText(String text) {
      this.text = text;
    }

  }

}
