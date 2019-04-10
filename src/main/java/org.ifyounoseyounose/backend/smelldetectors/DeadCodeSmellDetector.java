package org.ifyounoseyounose.backend.smelldetectors;

import com.github.javaparser.StaticJavaParser;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.MethodDeclaration;

import com.github.javaparser.ast.expr.VariableDeclarationExpr;

import com.github.javaparser.symbolsolver.JavaSymbolSolver;
import com.github.javaparser.symbolsolver.resolution.typesolvers.CombinedTypeSolver;
import com.github.javaparser.symbolsolver.resolution.typesolvers.ReflectionTypeSolver;
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
            VoidVisitor<List<VariableDeclarationExpr >> visitor2 = new DeadCodeObjectCollector();
            HashMap<File, List<MethodDeclaration>> methodHash = new HashMap<>();
            for(CompilationUnit comp: compilationUnits.keySet()){
                List<MethodDeclaration> methodList = methodHash.get(compilationUnits.get(comp)); //see if you already have a hashmap for current key

                if (methodList == null) { // If not, create one and put it in the map
                    methodList = new ArrayList();
                    methodHash.put(compilationUnits.get(comp),methodList);
                }
            visitor1.visit(comp, methodHash.get(compilationUnits.get(comp)));
            }
            List<VariableDeclarationExpr> temp = new ArrayList<>();
        for(CompilationUnit comp: compilationUnits.keySet()) {
            CombinedTypeSolver combinedTypeSolver = new CombinedTypeSolver();
            combinedTypeSolver.add(new ReflectionTypeSolver());
            JavaSymbolSolver symbolSolver = new JavaSymbolSolver(combinedTypeSolver);
            StaticJavaParser.getConfiguration().setSymbolResolver(symbolSolver);

            visitor2.visit(comp, temp);

        }
        return null;

    }

}
