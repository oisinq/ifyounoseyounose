package org.ifyounoseyounose.backend.smelldetectors;

import com.github.javaparser.ast.CompilationUnit;
import org.ifyounoseyounose.backend.SmellReport;

import java.io.File;
import java.util.HashMap;

public class DeadCodeSmellDetector implements JavaParserSmellDetector {
    public DeadCodeSmellDetector() {
        super();
    }
    public SmellReport detectSmell(HashMap<CompilationUnit, File> compilationUnits){
        SmellReport smells = new SmellReport();

        return null;
    }

}
