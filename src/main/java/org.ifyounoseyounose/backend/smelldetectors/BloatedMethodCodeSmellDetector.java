package org.ifyounoseyounose.backend.smelldetectors;

import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.visitor.VoidVisitor;
import org.ifyounoseyounose.backend.SmellReport;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * BloatedMethodCodeSmellDetector - Detects if the length of a method is greater than the "limit" or not
 */
public class BloatedMethodCodeSmellDetector extends LimitableSmellDetector implements JavaParserSmellDetector, SmellDetector {

    public BloatedMethodCodeSmellDetector() {
        super(20);
    }

    @Override
    public SmellReport detectSmell(HashMap<CompilationUnit, File> compilationUnits) {
        SmellReport smellReport = new SmellReport();
        BloatedMethodCollector visitor = new BloatedMethodCollector();
        visitor.setLimit(limit);

        for (CompilationUnit compilationUnit : compilationUnits.keySet()) {
            List<Integer> collector = new ArrayList<>();
            visitor.visit(compilationUnit, collector);
            smellReport.addToReport(compilationUnits.get(compilationUnit), collector);
        }

        return smellReport;
    }

    public String getSmellName() {
        return "BloatedMethod";
    }
}
