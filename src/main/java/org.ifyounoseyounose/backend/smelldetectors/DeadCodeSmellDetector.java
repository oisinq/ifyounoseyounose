package org.ifyounoseyounose.backend.smelldetectors;

import com.github.javaparser.StaticJavaParser;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.CallableDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.expr.BinaryExpr;
import com.github.javaparser.ast.expr.MethodCallExpr;
import com.github.javaparser.ast.expr.MethodReferenceExpr;
import com.github.javaparser.ast.expr.VariableDeclarationExpr;
import com.github.javaparser.ast.visitor.VoidVisitor;
import com.github.javaparser.ast.body.CallableDeclaration.Signature;
import com.github.javaparser.symbolsolver.JavaSymbolSolver;
import com.github.javaparser.symbolsolver.resolution.typesolvers.CombinedTypeSolver;
import com.github.javaparser.symbolsolver.resolution.typesolvers.ReflectionTypeSolver;
import org.ifyounoseyounose.backend.SmellReport;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class DeadCodeSmellDetector extends SmellDetector implements JavaParserSmellDetector  {
    public SmellReport detectSmell(HashMap<CompilationUnit, File> compilationUnits){
        SmellReport smells = new SmellReport();
        VoidVisitor<List<Signature>> visitor1 = new DeadCodeMethodCollector();
        HashMap<File, List<Signature>> methodHash = new HashMap<>();
        for(CompilationUnit comp: compilationUnits.keySet()){
            List<Signature> methodList = methodHash.get(compilationUnits.get(comp)); //see if you already have a hashmap for current key

            if (methodList == null) { // If not, create one and put it in the map
                methodList = new ArrayList();
                methodHash.put(compilationUnits.get(comp),methodList);
            }
            visitor1.visit(comp, methodHash.get(compilationUnits.get(comp)));
        }
        for(List<Signature> a: methodHash.values())
        {
            for(Signature b: a)
            {
               System.out.println(b.toString());
            }
        }

        ReflectionTypeSolver[] ha = new ReflectionTypeSolver[1];
        ha[0] = new ReflectionTypeSolver();
        CombinedTypeSolver combinedTypeSolver = new CombinedTypeSolver(ha);
        // Configure JavaParser to use type resolution
        JavaSymbolSolver symbolSolver = new JavaSymbolSolver(combinedTypeSolver);
        StaticJavaParser.getConfiguration().setSymbolResolver(symbolSolver);
        // Find all the calculations with two sides:

        for(CompilationUnit comp: compilationUnits.keySet()) {

            try {
                CompilationUnit comp1 = StaticJavaParser.parse(compilationUnits.get(comp));
                comp.findAll(MethodCallExpr.class).forEach(be -> {
                    if(methodHash.get(compilationUnits.get(be.getClass())).contains(be.resolve().getSignature()))
                    {
                        methodHash.get(compilationUnits.get(be.getClass())).remove(be.resolve().getSignature());
                    }
                });
            }
            catch(FileNotFoundException e)
            {
                System.out.println("No file found");
            }

        }
        for(List<Signature> e: methodHash.values())
        {
            smells.addToReport(e.getClass(), e.);
        }
        return smells;
    }
}