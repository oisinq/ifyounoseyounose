package org.ifyounoseyounose.backend.smelldetectors;

import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.visitor.VoidVisitor;
import org.ifyounoseyounose.backend.SmellReport;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class BloatedMethodCodeSmellDetector extends LimitableSmellDetector implements JavaParserSmellDetector, SmellDetector {

    public BloatedMethodCodeSmellDetector(int limit) {
        super(limit);
    }

    public BloatedMethodCodeSmellDetector() {
        super(20);
    }

    @Override
    public SmellReport detectSmell(HashMap<CompilationUnit, File> compilationUnits) {
        SmellReport smellReport = new SmellReport();
        VoidVisitor<List<Integer>> visitor = new BloatedMethodCollector();

        for (CompilationUnit compilationUnit : compilationUnits.keySet()) {
            List<Integer> collector = new ArrayList<>();
            collector.add(limit);
            visitor.visit(compilationUnit, collector);
            collector.remove(0);
            smellReport.addToReport(compilationUnits.get(compilationUnit),collector);
        }

        return smellReport;
    }
}
