package org.ifyounoseyounose.backend.smelldetectors;

import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.visitor.VoidVisitor;
import org.ifyounoseyounose.backend.SmellReport;

import java.io.File;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
public class DataOnlyClassesSmellDetector extends SmellDetector implements JavaParserSmellDetector {

    @Override
    public SmellReport detectSmell(HashMap<CompilationUnit, File> compilationUnits) {
        SmellReport smellReport = new SmellReport();
        VoidVisitor<List<Integer>> visitor = new DataOnlyClassesCollector();

        for (CompilationUnit compilationUnit : compilationUnits.keySet()) {

            List<Integer> collector = new ArrayList<>();


            visitor.visit(compilationUnit, collector);
            System.err.println(compilationUnits.get(compilationUnit) + " - " + collector.size());
            smellReport.addToReport(compilationUnits.get(compilationUnit),collector);
        }

        return smellReport;
    }

}
