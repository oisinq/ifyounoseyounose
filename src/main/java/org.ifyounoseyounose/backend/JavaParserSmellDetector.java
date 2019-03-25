package org.ifyounoseyounose.backend;

import com.github.javaparser.ast.CompilationUnit;

public interface JavaParserSmellDetector {
    SmellReport detectSmell(CompilationUnit sourceCode);
}
