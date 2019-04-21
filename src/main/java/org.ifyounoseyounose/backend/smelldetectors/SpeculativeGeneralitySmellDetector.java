package org.ifyounoseyounose.backend.smelldetectors;

import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.visitor.VoidVisitor;
import org.ifyounoseyounose.backend.SmellReport;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class SpeculativeGeneralitySmellDetector extends SmellDetector implements JavaParserSmellDetector{

    public SmellReport detectSmell(HashMap<CompilationUnit, File> compilationUnits){

        SmellReport smells = new SmellReport();
        VoidVisitor<List<Integer>> visitor1 = new SpeculativeGeneralityMethodCollector();//Visits method bodies
        for(CompilationUnit comp: compilationUnits.keySet()){
            List<Integer> lineNumbers = new ArrayList();
            visitor1.visit(comp, lineNumbers);//Stores the line numbers of methods
            smells.addToReport(compilationUnits.get(comp), lineNumbers);// Adds to report
        }

        return smells;
        }

    public String getSmellName() {
        return "SpeculativeGenerality";
    }
    }

