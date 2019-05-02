package org.ifyounoseyounose.backend.smelldetectors;

import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.visitor.VoidVisitor;
import org.ifyounoseyounose.backend.SmellReport;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * SwitchStatementSmellDetector - SmelLDetector that detects any switch statements with more cases than "limit", and highlights those lines
 */
public class SwitchStatementSmellDetector extends LimitableSmellDetector implements JavaParserSmellDetector, SmellDetector {

    public SwitchStatementSmellDetector() {
        super(3);
    }

    @Override
    public SmellReport detectSmell(HashMap<CompilationUnit, File> compilationUnits) {
        SmellReport smellReport = new SmellReport();
        VoidVisitor<List<Integer>> visitor = new SwitchStatementCollector();

        // We check for literals in each individual CompliationUnit and record the line numbers of instances of literals being used
        for (CompilationUnit compilationUnit : compilationUnits.keySet()) {
            List<Integer> collector = new ArrayList<>();
            visitor.visit(compilationUnit, collector);
            smellReport.addToReport(compilationUnits.get(compilationUnit), collector);
        }

        return smellReport;
    }

    public String getSmellName() {
        return "SwitchStatement";
    }
}

