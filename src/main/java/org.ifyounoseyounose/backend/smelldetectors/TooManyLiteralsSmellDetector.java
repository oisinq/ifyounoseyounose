package org.ifyounoseyounose.backend.smelldetectors;

import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.visitor.VoidVisitor;
import org.ifyounoseyounose.backend.JavaParserSmellDetector;
import org.ifyounoseyounose.backend.SmellDetector;
import org.ifyounoseyounose.backend.SmellReport;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * TooManyLiteralsSmellDetector -
 */
public class TooManyLiteralsSmellDetector extends SmellDetector implements JavaParserSmellDetector {

    @Override
    public SmellReport detectSmell(HashMap<CompilationUnit, File> compilationUnits) {
        SmellReport smellReport = new SmellReport();
        VoidVisitor<List<Integer>> visitor = new TooManyLiteralsCollector();

        for (CompilationUnit compilationUnit : compilationUnits.keySet()) {
            List<Integer> collector = new ArrayList<>();
            visitor.visit(compilationUnit, collector);
            System.err.println(compilationUnits.get(compilationUnit) + " - " + collector.size());
            smellReport.addToReport(compilationUnits.get(compilationUnit),collector);
        }

        return smellReport;
    }
}

