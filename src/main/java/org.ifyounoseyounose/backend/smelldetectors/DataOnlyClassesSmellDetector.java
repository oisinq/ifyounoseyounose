package org.ifyounoseyounose.backend.smelldetectors;

import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.visitor.VoidVisitor;
import org.ifyounoseyounose.backend.SmellReport;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class DataOnlyClassesSmellDetector implements JavaParserSmellDetector, SmellDetector {

    @Override
    public SmellReport detectSmell(HashMap<CompilationUnit, File> compilationUnits) {
        SmellReport smellReport = new SmellReport();
        VoidVisitor<List<Integer>> visitor = new DataOnlyClassesCollector();

        for (CompilationUnit compilationUnit : compilationUnits.keySet()) {

                List<Integer> collector = new ArrayList<>();
                visitor.visit(compilationUnit, collector);
                smellReport.addToReport(compilationUnits.get(compilationUnit), collector);

        }

        return smellReport; //returns the lines of the methods although the whole class is problematic
    }


    public String getSmellName() {
        return "DataOnlyClasses";
    }
}
