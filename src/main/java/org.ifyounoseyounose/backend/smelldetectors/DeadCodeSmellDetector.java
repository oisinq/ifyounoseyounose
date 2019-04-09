package org.ifyounoseyounose.backend.smelldetectors;

import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.visitor.VoidVisitor;
import org.ifyounoseyounose.backend.SmellReport;

import java.io.File;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class DeadCodeSmellDetector extends SmellDetector implements JavaParserSmellDetector  {
    public SmellReport detectSmell(HashMap<CompilationUnit, File> compilationUnits){
        SmellReport smells = new SmellReport();
            VoidVisitor<List<MethodDeclaration>> visitor1 = new DeadCodeMethodCollector();
            VoidVisitor<List<Integer>> visitor2 = new DeadCodeObjectCollector();
            HashMap<File, List<MethodDeclaration>> methodHash = new HashMap<>();
            for(CompilationUnit comp: compilationUnits.keySet()){
                List<MethodDeclaration> methodList = methodHash.get(compilationUnits.get(comp)); //see if you already have a hashmap for current key

                if (methodList == null) { // If not, create one and put it in the map
                    methodList = new ArrayList();
                    methodHash.put(compilationUnits.get(comp),methodList);
                }
            visitor1.visit(comp, methodHash.get(compilationUnits.get(comp)));
            System.out.println(methodHash.get(compilationUnits.get(comp)).get(1).getDeclarationAsString());
            }
        return null;

    }

}
