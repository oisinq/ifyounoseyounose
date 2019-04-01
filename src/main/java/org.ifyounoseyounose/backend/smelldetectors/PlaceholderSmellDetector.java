package org.ifyounoseyounose.backend.smelldetectors;

import com.github.javaparser.ast.CompilationUnit;
import org.ifyounoseyounose.backend.JavaParserSmellDetector;
import org.ifyounoseyounose.backend.SmellDetector;
import org.ifyounoseyounose.backend.SmellReport;

import java.util.List;

public class PlaceholderSmellDetector extends SmellDetector implements JavaParserSmellDetector {
    @Override
    public SmellReport detectSmell(List<CompilationUnit> compilationUnits) {

        return null;
    }
}
