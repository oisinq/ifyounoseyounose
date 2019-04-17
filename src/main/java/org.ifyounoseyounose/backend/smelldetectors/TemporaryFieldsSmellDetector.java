package org.ifyounoseyounose.backend.smelldetectors;

import com.github.javaparser.JavaParser;
import com.github.javaparser.StaticJavaParser;
import com.github.javaparser.ast.CompilationUnit;

import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.body.BodyDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.body.TypeDeclaration;
import com.github.javaparser.ast.body.VariableDeclarator;
import com.github.javaparser.ast.expr.AnnotationExpr;
import com.github.javaparser.ast.expr.BinaryExpr;
import com.github.javaparser.ast.expr.MethodCallExpr;
import com.github.javaparser.ast.nodeTypes.NodeWithVariables;
import com.github.javaparser.ast.visitor.VoidVisitor;

import org.ifyounoseyounose.backend.SmellReport;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class TemporaryFieldsSmellDetector extends LimitableSmellDetector implements JavaParserSmellDetector, SmellDetector  {

    public SmellReport detectSmell(HashMap<CompilationUnit, File> compilationUnits) {
        SmellReport smellReport = new SmellReport();
        VoidVisitor<List<Integer>> visitor = new TemporaryFieldsCollector();

        for (CompilationUnit compilationUnit : compilationUnits.keySet()) {
            try {
                boolean occured = false;//Checks if an instance variable has already occurred in the method
                CompilationUnit cu = StaticJavaParser.parse(compilationUnits.get(compilationUnit));
                HashMap<VariableDeclarator, Integer> variables = new HashMap<>();// Stores instance variables and there amount of occurrences
                for (TypeDeclaration<?> typeDec : cu.getTypes()) {// Finds the instance variables and stores them in a hashmap
                    for (BodyDeclaration<?> member : typeDec.getMembers()) {
                        member.toFieldDeclaration().ifPresent(field -> {
                            for (VariableDeclarator variable : field.getVariables()) {
                                variables.put(variable, 0);
                            }
                        });
                    }
                }
                for(MethodDeclaration md:cu.findAll(MethodDeclaration.class)){// Checks each method
                    occured=false;
                    for(BinaryExpr be: md.findAll(BinaryExpr.class)){// Checks all binary expressions for the instance variable
                        for (VariableDeclarator v : variables.keySet()) {
                            List<Node> children = be.getChildNodes();
                            for (Node chi : children) {
                                if (chi.toString().equals(v.getNameAsString())&&!occured) {// If the variable is found iterate but only if its the first cccurence in the method
                                    occured=true;
                                    variables.put(v, variables.get(v)+1);
                                }
                            }
                        }
                    }
                    for(MethodCallExpr me: md.findAll(MethodCallExpr.class)){// Checks all method expressions for the variable
                        for (VariableDeclarator v : variables.keySet()) {
                            List<Node> children = me.getChildNodes();
                            for (Node chi : children) {
                                if (chi.toString().equals(v.getNameAsString())&&!occured) {
                                    occured=true;
                                    variables.put(v, variables.get(v)+1);
                                }
                            }

                        }
                    }
                }
                for(VariableDeclarator v: variables.keySet()){
                    List<Integer> lines = new ArrayList<>();
                    if(variables.get(v)<limit){// If less than user set limit add declaration of the variable to the smell report
                        ((TemporaryFieldsCollector) visitor).addLineNumbers(v,lines);
                        smellReport.addToReport(compilationUnits.get(compilationUnit), lines);
                    }
                }
            }
            catch(IOException e){

            }
        }

        return smellReport;
    }
    public String getSmellName() {
        return "TemporaryFields";
    }
}


