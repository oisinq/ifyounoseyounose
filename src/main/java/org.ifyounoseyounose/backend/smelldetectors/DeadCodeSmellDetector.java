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

public class DeadCodeSmellDetector implements JavaParserSmellDetector, SmellDetector  {

    public SmellReport detectSmell(HashMap<CompilationUnit, File> compilationUnits){
        SmellReport smells = new SmellReport();
        VoidVisitor<List<MethodDeclaration>> visitor1 = new DeadCodeMethodCollector();//Retrieves method declarations
        HashMap<File, HashMap<String, MethodDeclaration>> methodHash = new HashMap<>();// Stores method declarations and their string variations

        for(CompilationUnit comp: compilationUnits.keySet()){
            List<MethodDeclaration> methodList = new ArrayList<>(); //Temporary storage of method declarations

            visitor1.visit(comp, methodList);
            HashMap<String, MethodDeclaration> temp=new HashMap<>(); //Temporary storage of string and method declaration

            for(MethodDeclaration m: methodList) {//Adds every element from the temp methods list to the hashmap
                temp.put(m.getSignature().toString(), m);
            }
            methodHash.put(compilationUnits.get(comp), temp);//Places in the overall hashmap
        }


        ReflectionTypeSolver[] ha = new ReflectionTypeSolver[1];
        ha[0] = new ReflectionTypeSolver();
        CombinedTypeSolver combinedTypeSolver = new CombinedTypeSolver(ha);
        // Configure JavaParser to use type resolution
        JavaSymbolSolver symbolSolver = new JavaSymbolSolver(combinedTypeSolver);
        StaticJavaParser.getConfiguration().setSymbolResolver(symbolSolver);

        for(CompilationUnit comp: compilationUnits.keySet()) {
            try {
                System.out.println(comp);
                CompilationUnit comp1 = StaticJavaParser.parse(compilationUnits.get(comp));
                comp1.findAll(MethodCallExpr.class).forEach(be -> {
                    for (File search : compilationUnits.values()) {//Check against our stored methods
                        if (!be.getName().toString().equals("println") && !be.getName().toString().equals("print")) {//Ignore prints
                            if (methodHash.get(search).containsKey(be.resolve().getSignature())) {//If used method is in the list remove it

                                methodHash.get(search).remove(be.resolve().getSignature(), methodHash.get(search).get(be.resolve().getSignature()));
                            }
                        }
                    }
                });


            } catch (FileNotFoundException e) {
                System.out.println("No file found");
            }
        }
       for(File e: methodHash.keySet())
       {
        List<Integer> lines= new ArrayList<>();//Temporary list
        for (MethodDeclaration m: methodHash.get(e).values()) {//Iterate through method declarations that havent been used
            if(!"main".equals(m.getName().asString())) {//Ignore main
                 ((DeadCodeMethodCollector) visitor1).addLineNumbers(m, lines);// Add there line number to that classes list
            }
        }
         smells.addToReport(e, lines);
       }
        return smells;
    }


    public String getSmellName() {
        return "DeadCode";
    }
}