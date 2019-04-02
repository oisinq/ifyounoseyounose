package org.ifyounoseyounose.backend.smelldetectors;

import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.visitor.VoidVisitor;
import org.ifyounoseyounose.backend.JavaParserSmellDetector;
import org.ifyounoseyounose.backend.SmellDetector;
import org.ifyounoseyounose.backend.SmellReport;

import java.util.ArrayList;
import java.util.List;

/**
 * TooManyLiteralsSmellDetector -
 */
public class TooManyLiteralsSmellDetector extends SmellDetector implements JavaParserSmellDetector {

    @Override
    public SmellReport detectSmell(List<CompilationUnit> compilationUnits) {
        SmellReport smellReport = new SmellReport();
        VoidVisitor<List<Integer>> visitor = new TooManyLiteralsCollector();

        for (CompilationUnit compilationUnit : compilationUnits) {
            List<Integer> collector = new ArrayList<>();
            Class c = compilationUnit.getClass();
            visitor.visit(compilationUnit, collector);
            smellReport.addToReport(c, collector);
        }

        return smellReport;
    }
}

