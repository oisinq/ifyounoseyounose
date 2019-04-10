package org.ifyounoseyounose.backend.smelldetectors;

import com.github.javaparser.StaticJavaParser;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.expr.BinaryExpr;
import com.github.javaparser.ast.expr.VariableDeclarationExpr;
import com.github.javaparser.ast.visitor.VoidVisitor;
import com.github.javaparser.resolution.types.ResolvedType;
import com.github.javaparser.symbolsolver.JavaSymbolSolver;
import com.github.javaparser.symbolsolver.resolution.typesolvers.CombinedTypeSolver;
import com.github.javaparser.symbolsolver.resolution.typesolvers.ReflectionTypeSolver;
import org.ifyounoseyounose.backend.SmellReport;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class DeadCodeSmellDetector extends SmellDetector implements JavaParserSmellDetector  {
    public SmellReport detectSmell(HashMap<CompilationUnit, File> compilationUnits){
        SmellReport smells = new SmellReport();
            VoidVisitor<List<MethodDeclaration>> visitor1 = new DeadCodeMethodCollector();
            VoidVisitor<List<VariableDeclarationExpr>> visitor2 = new DeadCodeObjectCollector();
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
        //for(CompilationUnit comp: compilationUnits.keySet()) {
        CompilationUnit cu = StaticJavaParser.parse("class X { int x() { return 1 + 1.0 - 5; } }");
            ReflectionTypeSolver[] ha = new ReflectionTypeSolver[1];
            ha[0] = new ReflectionTypeSolver();
            CombinedTypeSolver combinedTypeSolver = new CombinedTypeSolver(ha);
            JavaSymbolSolver symbolSolver = new JavaSymbolSolver(combinedTypeSolver);
            StaticJavaParser.getConfiguration().setSymbolResolver(symbolSolver);
            cu.findAll(BinaryExpr.class).forEach(be -> {
                // Find out what type it has:
                ResolvedType resolvedType = be.calculateResolvedType();

                // Show that it's "double" in every case:
                System.out.println(be.toString() + " is a: " + resolvedType);
            });
            //visitor2.visit(comp, temp);

        //}
        return null;

    }

}
