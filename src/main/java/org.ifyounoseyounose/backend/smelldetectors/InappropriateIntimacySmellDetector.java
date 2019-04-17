package org.ifyounoseyounose.backend.smelldetectors;
import com.github.javaparser.StaticJavaParser;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.expr.MethodCallExpr;
import com.github.javaparser.ast.visitor.VoidVisitor;

import com.github.javaparser.symbolsolver.JavaSymbolSolver;
import com.github.javaparser.symbolsolver.resolution.typesolvers.CombinedTypeSolver;
import com.github.javaparser.symbolsolver.resolution.typesolvers.ReflectionTypeSolver;
import org.ifyounoseyounose.backend.SmellReport;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class InappropriateIntimacySmellDetector extends LimitableSmellDetector implements JavaParserSmellDetector {
    public SmellReport detectSmell(HashMap<CompilationUnit, File> compilationUnits){
        SmellReport smells = new SmellReport();
        VoidVisitor<List<Integer>> visitor1 = new inappropriateIntimacyDeclarationCollector();//Retrieves method declarations
        ReflectionTypeSolver[] ha = new ReflectionTypeSolver[1];
        ha[0] = new ReflectionTypeSolver();
        CombinedTypeSolver combinedTypeSolver = new CombinedTypeSolver(ha);
        // Configure JavaParser to use type resolution
        JavaSymbolSolver symbolSolver = new JavaSymbolSolver(combinedTypeSolver);
        StaticJavaParser.getConfiguration().setSymbolResolver(symbolSolver);
        int count=0;
        for(CompilationUnit comp: compilationUnits.keySet()){
            List<MethodCallExpr> temp = new ArrayList();
            try {
                CompilationUnit comp1 = StaticJavaParser.parse(compilationUnits.get(comp));
                temp=comp1.findAll(MethodCallExpr.class);
                for(MethodCallExpr exp: temp)
                {
                    if(!exp.getClass().equals(compilationUnits.get(comp))) {
                        count++;
                    }
                    if(count>limit){
                        List<Integer> lines = new ArrayList<>();
                        visitor1.visit(comp, lines);
                        smells.addToReport(compilationUnits.get(comp), lines);
                    }
                }

            }
            catch(FileNotFoundException e){
                System.out.println("");
            }
        }

        return smells;
    }
}
