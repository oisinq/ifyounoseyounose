package org.ifyounoseyounose.backend;
import com.github.javaparser.ast.CompilationUnit;
import java.util.List;

import java.util.List;

public interface JavaParserSmellDetector {
    SmellReport detectSmell(List<CompilationUnit> compilationUnits);
}
