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
        VoidVisitor<List<Integer>> visitor1 = new InappropriateIntimacyDeclarationCollector();//Retrieves declarations
        ReflectionTypeSolver[] ha = new ReflectionTypeSolver[1];
        ha[0] = new ReflectionTypeSolver();
        CombinedTypeSolver combinedTypeSolver = new CombinedTypeSolver(ha);
        // Configure JavaParser to use type resolution
        JavaSymbolSolver symbolSolver = new JavaSymbolSolver(combinedTypeSolver);
        StaticJavaParser.getConfiguration().setSymbolResolver(symbolSolver);
        int count=0;//Used to compare against limit
        for(CompilationUnit comp: compilationUnits.keySet()){
                List<MethodCallExpr> temp = new ArrayList();//Holds the method calls
                List<Integer> lines= new ArrayList();// Stores the line numbers
            try {
                CompilationUnit comp1 = StaticJavaParser.parse(compilationUnits.get(comp));
                temp=comp1.findAll(MethodCallExpr.class);//Stores the method call expr
                for(MethodCallExpr exp: temp)// Iterates through all the method call expr in the class
                {
                    if(!exp.getClass().equals(compilationUnits.get(comp))) {//If a method call is not from this class track it
                        count++;
                    }
                    if(count>limit){//If the amount of times a method from another method is called exceeds limit add the line number

                        ((InappropriateIntimacyDeclarationCollector) visitor1).addLineNumbers(exp, lines);
                    }
                }
                smells.addToReport(compilationUnits.get(comp), lines);
            }
            catch(FileNotFoundException e){
                System.out.println("");
            }
        }

        return smells;
    }
}
