package org.ifyounoseyounose.backend;
import com.github.javaparser.ast.CompilationUnit;

import java.io.File;
import java.util.HashMap;

public interface JavaParserSmellDetector {
    SmellReport detectSmell(HashMap<CompilationUnit, File> compilationUnits);
}
