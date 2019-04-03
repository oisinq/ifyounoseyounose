package org.ifyounoseyounose.backend.smelldetectors;
import com.github.javaparser.ast.CompilationUnit;
import org.ifyounoseyounose.backend.SmellReport;

import java.io.File;
import java.util.HashMap;

/**
 * JavaParserSmellDetector - interface extended by any SmellDetector that uses the JavaParser library
 */
public interface JavaParserSmellDetector {
    SmellReport detectSmell(HashMap<CompilationUnit, File> compilationUnits);
}
