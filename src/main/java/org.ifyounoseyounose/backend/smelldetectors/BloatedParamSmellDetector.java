package org.ifyounoseyounose.backend.smelldetectors;

import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.visitor.VoidVisitor;
import org.ifyounoseyounose.backend.SmellReport;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class BloatedParamSmellDetector extends LimitableSmellDetector implements JavaParserSmellDetector, SmellDetector {

    public BloatedParamSmellDetector(int limit) {
        super(limit);
    }

    public BloatedParamSmellDetector() {
        super(20);
    }

    @Override
    public SmellReport detectSmell(HashMap<CompilationUnit, File> compilationUnits) {
        SmellReport smellReport = new SmellReport();
        VoidVisitor<List<Integer>> visitor = new BloatedParamCollector();

        for (CompilationUnit compilationUnit : compilationUnits.keySet()) {
            List<Integer> collector = new ArrayList<>();
            collector.add(limit);
            visitor.visit(compilationUnit, collector);
            collector.remove(0);
            System.err.println(compilationUnits.get(compilationUnit) + " - " + collector.size());
            smellReport.addToReport(compilationUnits.get(compilationUnit),collector);
        }

        return smellReport;
    }

    public String getSmellName() {
        return "BloatedParam";
    }
}
