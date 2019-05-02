package org.ifyounoseyounose.backend.smelldetectors;

import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.visitor.VoidVisitor;
import org.ifyounoseyounose.backend.SmellReport;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * BloatedParamSmellDetector - Detects if a method has more parameters than the "limit" or not
 */
public class BloatedParamSmellDetector extends LimitableSmellDetector implements JavaParserSmellDetector, SmellDetector {

    public BloatedParamSmellDetector() {
        super(20);
    }

    @Override
    public SmellReport detectSmell(HashMap<CompilationUnit, File> compilationUnits) {
        SmellReport smellReport = new SmellReport();
        BloatedParamCollector visitor = new BloatedParamCollector();
        visitor.setLimit(limit);

        // Runs the visitor for each compilationunit in the project
        for (CompilationUnit compilationUnit : compilationUnits.keySet()) {
            List<Integer> collector = new ArrayList<>();
            visitor.visit(compilationUnit, collector);
            smellReport.addToReport(compilationUnits.get(compilationUnit), collector);
        }

        return smellReport;
    }

    public String getSmellName() {
        return "BloatedParameter";
    }
}