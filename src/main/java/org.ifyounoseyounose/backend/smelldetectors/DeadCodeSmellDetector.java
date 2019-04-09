package org.ifyounoseyounose.backend.smelldetectors;

import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.visitor.VoidVisitor;
import org.ifyounoseyounose.backend.SmellReport;

import java.io.File;

import java.util.HashMap;
import java.util.List;

public class DeadCodeSmellDetector implements JavaParserSmellDetector {
    public DeadCodeSmellDetector() {
        super();
    }
    public SmellReport detectSmell(HashMap<CompilationUnit, File> compilationUnits){
        SmellReport smells = new SmellReport();
            VoidVisitor<List<MethodDeclaration>> visitor1 = new DeadCodeMethodCollector();
            HashMap<File, List<MethodDeclaration>> methodList = new HashMap<>();
            for(CompilationUnit comp: compilationUnits.keySet()){
            visitor1.visit(comp, methodList.get(compilationUnits.get(comp)));
            System.out.println(methodList.get(compilationUnits.get(comp)).toString());
            }
        return null;

    }

}
