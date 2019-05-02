package org.ifyounoseyounose.backend.smelldetectors;

import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.visitor.VoidVisitor;
import org.ifyounoseyounose.backend.SmellReport;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * PrimitiveObsessionSmellDetector - detects if a class uses too many primitives
 */
public class PrimitiveObsessionSmellDetector extends LimitableSmellDetector implements JavaParserSmellDetector, SmellDetector {

    public PrimitiveObsessionSmellDetector() {
        super(15);
    }

    @Override
    public SmellReport detectSmell(HashMap<CompilationUnit, File> compilationUnits) {
        SmellReport smellReport = new SmellReport();
        VoidVisitor<List<Integer>> visitor = new PrimitiveObsessionCollector();

        for (CompilationUnit compilationUnit : compilationUnits.keySet()) {
            List<Integer> collector = new ArrayList<>();
            visitor.visit(compilationUnit, collector);
            if (collector.size() <= limit) {
                collector.clear();
                smellReport.addToReport(compilationUnits.get(compilationUnit), collector);
            } else {
                smellReport.addToReport(compilationUnits.get(compilationUnit), collector);
            }

        }

        return smellReport;
    }

    public String getSmellName() {
        return "PrimitiveObsession";
    }
}
