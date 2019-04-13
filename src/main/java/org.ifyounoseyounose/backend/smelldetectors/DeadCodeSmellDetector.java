package org.ifyounoseyounose.backend.smelldetectors;

import com.github.javaparser.StaticJavaParser;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.expr.MethodCallExpr;
import com.github.javaparser.ast.visitor.VoidVisitor;

import com.github.javaparser.resolution.types.ResolvedType;
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
        VoidVisitor<List<MethodDeclaration>> visitor1 = new DeadCodeMethodCollector();//Retrieves method declarations
        HashMap<File, HashMap<String, MethodDeclaration>> methodHash = new HashMap<>();

        for(CompilationUnit comp: compilationUnits.keySet()){
            List<MethodDeclaration> methodList = new ArrayList<>(); //see if you already have a hashmap for current key

            visitor1.visit(comp, methodList);
            HashMap<String, MethodDeclaration> temp=new HashMap<>(); //see if you already have a hashmap for current key

            for(MethodDeclaration m: methodList) {
                System.out.println(m.getName());
                temp.put(m.getSignature().toString(), m);
            }
            methodHash.put(compilationUnits.get(comp), temp);
        }


        ReflectionTypeSolver[] ha = new ReflectionTypeSolver[1];
        ha[0] = new ReflectionTypeSolver();
        CombinedTypeSolver combinedTypeSolver = new CombinedTypeSolver(ha);
        // Configure JavaParser to use type resolution
        JavaSymbolSolver symbolSolver = new JavaSymbolSolver(combinedTypeSolver);
        StaticJavaParser.getConfiguration().setSymbolResolver(symbolSolver);
        // Find all the calculations with two sides:

        for(CompilationUnit comp: compilationUnits.keySet())
            try {
                CompilationUnit comp1 = StaticJavaParser.parse(compilationUnits.get(comp));
                comp1.findAll(MethodCallExpr.class).forEach(be -> {
                    System.out.println(be.resolve());
                    for (File search : compilationUnits.values()){
                        if(!be.getName().toString().equals("println")&&!be.getName().toString().equals("print")) {
                            ResolvedType resolvedType = be.calculateResolvedType();
                            if (methodHash.get(search).containsKey(be.resolve().getSignature())) {

                                methodHash.get(search).remove(be.resolve().getSignature(), methodHash.get(search).get(be.resolve().getSignature()));
                            }
                        }
                }});


            } catch (FileNotFoundException e) {
                System.out.println("No file found");
            }
       for(File e: methodHash.keySet())
       {
        List<Integer> lines= new ArrayList<>();
        for (MethodDeclaration m: methodHash.get(e).values()) {
            if(!"main".equals(m.getName().asString())) {
                System.out.println(m.getName());
                ((DeadCodeMethodCollector) visitor1).addLineNumbers(m, lines);
            }
        }
         smells.addToReport(e, lines);
       }
        return smells;
    }
}