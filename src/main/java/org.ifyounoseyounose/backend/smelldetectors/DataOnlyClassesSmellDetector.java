package org.ifyounoseyounose.backend.smelldetectors;

import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.visitor.VoidVisitor;
import org.ifyounoseyounose.backend.SmellReport;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.LinkedHashSet;

public class DataOnlyClassesSmellDetector extends LimitableSmellDetector implements JavaParserSmellDetector, SmellDetector {

    @Override
    public SmellReport detectSmell(HashMap<CompilationUnit, File> compilationUnits) {
        SmellReport smellReport = new SmellReport();
        VoidVisitor<List<Integer>> visitorData = new DataOnlyClassesCollector();
        VoidVisitor<List<Integer>> visitorClass = new DataOnlyClassesClassCollector();

        //get number of methods

        for (CompilationUnit compilationUnit : compilationUnits.keySet()) {

            List<Integer> collector = new ArrayList<>();

            visitorClass.visit(compilationUnit, collector);
            limit = collector.get(0);
            collector.clear();
            visitorData.visit(compilationUnit, collector);
            LinkedHashSet<Integer> hashSet = new LinkedHashSet<>(collector);
            ArrayList<Integer> listWithoutDuplicates = new ArrayList<>(hashSet);
            if (listWithoutDuplicates.size() == limit) {
                listWithoutDuplicates.clear();
                listWithoutDuplicates.add(0);
            } else {
                listWithoutDuplicates.clear();
            }

            smellReport.addToReport(compilationUnits.get(compilationUnit), listWithoutDuplicates);
        }

        return smellReport; //returns the lines of the methods although the whole class is problematic
    }


    public String getSmellName() {
        return "DataOnly";
    }
}
