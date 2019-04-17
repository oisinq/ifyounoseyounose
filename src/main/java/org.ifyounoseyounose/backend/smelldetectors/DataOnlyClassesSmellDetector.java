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

        int count = 0; //keeps count of the number methods that are checked
        for (CompilationUnit compilationUnit : compilationUnits.keySet()) {
            List<Integer> collector = new ArrayList<>();

            if (count > 0 && collector.isEmpty()) {
                smellReport.addToReport(compilationUnits.get(compilationUnit), collector);
            }
                
                collector.add(0);
                visitor.visit(compilationUnit, collector);
                smellReport.addToReport(compilationUnits.get(compilationUnit), collector);
                count++;

        }

        return smellReport; //returns the lines of the methods although the whole class is problematic
    }


    public String getSmellName() {
        return "DataOnlyClasses";
    }
}
