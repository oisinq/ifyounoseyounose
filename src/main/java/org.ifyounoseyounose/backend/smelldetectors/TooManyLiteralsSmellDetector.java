package org.ifyounoseyounose.backend.smelldetectors;

import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.visitor.VoidVisitor;
import org.ifyounoseyounose.backend.SmellReport;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * TooManyLiteralsSmellDetector - Returns every line in which a literal int, double, float or char is referenced, apart from variable assignments
 */
public class TooManyLiteralsSmellDetector extends LimitableSmellDetector implements JavaParserSmellDetector {

    public TooManyLiteralsSmellDetector(int limit) {
        super(limit);
    }

    public TooManyLiteralsSmellDetector() {
        super(3);
    }

    @Override
    public SmellReport detectSmell(HashMap<CompilationUnit, File> compilationUnits) {
        SmellReport smellReport = new SmellReport();
        VoidVisitor<List<Integer>> visitor = new TooManyLiteralsCollector();

        // We check for literals in each individual CompliationUnit and record the line numbers of instances of literals being used
        for (CompilationUnit compilationUnit : compilationUnits.keySet()) {
            List<Integer> collector = new ArrayList<>();
            visitor.visit(compilationUnit, collector);
            if (collector.size() > limit) {
                smellReport.addToReport(compilationUnits.get(compilationUnit),collector);
            }
        }

        return smellReport;
    }
}

