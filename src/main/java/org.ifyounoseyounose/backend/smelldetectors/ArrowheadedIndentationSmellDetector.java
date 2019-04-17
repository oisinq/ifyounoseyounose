package org.ifyounoseyounose.backend.smelldetectors;

import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.visitor.VoidVisitor;
import org.ifyounoseyounose.backend.SmellReport;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ArrowheadedIndentationSmellDetector extends LimitableSmellDetector implements JavaParserSmellDetector, SmellDetector {

    public ArrowheadedIndentationSmellDetector(int limit) {
        super(limit);
    }

    public ArrowheadedIndentationSmellDetector() {
        super(4);
    }

    @Override
    public SmellReport detectSmell(HashMap<CompilationUnit, File> compilationUnits) {
        SmellReport smellReport = new SmellReport();
        ArrowheadedIndentationCollector visitor = new ArrowheadedIndentationCollector();
         visitor.setLimit(limit);

        // We check for literals in each individual CompliationUnit and record the line numbers of instances of literals being used
        for (CompilationUnit compilationUnit : compilationUnits.keySet()) {
            List<Integer> collector = new ArrayList<>();
            visitor.visit(compilationUnit, collector);
            smellReport.addToReport(compilationUnits.get(compilationUnit),collector);
        }

        return smellReport;
    }

    public String getSmellName() {
        return "ArrowheadedIndentation";
    }
}
