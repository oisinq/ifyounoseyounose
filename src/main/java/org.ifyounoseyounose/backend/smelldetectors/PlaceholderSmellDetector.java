package org.ifyounoseyounose.backend.smelldetectors;

import com.github.javaparser.ast.CompilationUnit;
import org.ifyounoseyounose.backend.JavaParserSmellDetector;
import org.ifyounoseyounose.backend.SmellDetector;
import org.ifyounoseyounose.backend.SmellReport;

import java.io.File;
import java.util.HashMap;
import java.util.List;

public class PlaceholderSmellDetector extends SmellDetector implements JavaParserSmellDetector {
    @Override
    public SmellReport detectSmell(HashMap<CompilationUnit, File> compilationUnits) {

        return null;
    }
}
