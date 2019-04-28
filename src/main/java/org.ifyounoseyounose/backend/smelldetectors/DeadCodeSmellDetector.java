package org.ifyounoseyounose.backend.smelldetectors;

import com.github.javaparser.StaticJavaParser;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.expr.MethodCallExpr;
import com.github.javaparser.symbolsolver.JavaSymbolSolver;
import com.github.javaparser.symbolsolver.resolution.typesolvers.CombinedTypeSolver;
import com.github.javaparser.symbolsolver.resolution.typesolvers.ReflectionTypeSolver;
import org.ifyounoseyounose.backend.SmellReport;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;

public class DeadCodeSmellDetector implements JavaParserSmellDetector, SmellDetector  {

    public SmellReport detectSmell(HashMap<CompilationUnit, File> compilationUnits){
        SmellReport smells = new SmellReport();
        DeadCodeMethodCollector visitor1 = new DeadCodeMethodCollector();//Retrieves method declarations
        HashMap<File, HashMap<String, MethodDeclaration>> methodHash = new HashMap<>();// Stores method declarations and their string variations

        for(CompilationUnit comp: compilationUnits.keySet()){
           addMethods(visitor1 , comp, methodHash, compilationUnits.get(comp));
        }
        setSymbolSolver();

        for(CompilationUnit comp: compilationUnits.keySet()) {
            compareMethods(comp, compilationUnits.values(), methodHash);
        }

       for(File e: methodHash.keySet()) {
        List<Integer> lines= new ArrayList<>();//Temporary list
        for (MethodDeclaration m: methodHash.get(e).values()) {//Iterate through method declarations that havent been used
            if(!"main".equals(m.getName().asString())&&!"initialize".equals(m.getName().asString())) {//Ignore main
                 visitor1.addLineNumbers(m, lines);// Add there line number to that classes list
            }
        }
         smells.addToReport(e, lines);
       }
        return smells;
    }
    private void addMethods( DeadCodeMethodCollector visitor1 , CompilationUnit comp, HashMap<File, HashMap<String, MethodDeclaration>> methodHash, File file){
        List<MethodDeclaration> methodList = new ArrayList<>(); //Temporary storage of method declarations

        visitor1.visit(comp, methodList);
        HashMap<String, MethodDeclaration> temp=new HashMap<>(); //Temporary storage of string and method declaration

        for(MethodDeclaration m: methodList) {//Adds every element from the temp methods list to the hashmap
            temp.put(m.getName().asString(), m);
        }
        methodHash.put(file, temp);//Places in the overall hashmap
    }
    private void compareMethods(CompilationUnit comp, Collection<File> files, HashMap<File, HashMap<String, MethodDeclaration>> methodHash){
        comp.findAll(MethodCallExpr.class).forEach(be -> {
            for (File search : files) {//Check against our stored methods
                String methodName = be.getNameAsString();
                if (methodHash.get(search).containsKey(methodName)) {//If used method is in the list remove it
                    MethodDeclaration declareCheck=  methodHash.get(search).get(methodName);
                    methodHash.get(search).remove(methodName,declareCheck);
                }
            }
        });
    }
    private void setSymbolSolver(){
        ReflectionTypeSolver[] ha = new ReflectionTypeSolver[1];
        ha[0] = new ReflectionTypeSolver();
        CombinedTypeSolver combinedTypeSolver = new CombinedTypeSolver(ha);// Configure JavaParser to use type resolution
        JavaSymbolSolver symbolSolver = new JavaSymbolSolver(combinedTypeSolver);
        StaticJavaParser.getConfiguration().setSymbolResolver(symbolSolver);
    }

    public String getSmellName() {
        return "DeadCode";
    }
}