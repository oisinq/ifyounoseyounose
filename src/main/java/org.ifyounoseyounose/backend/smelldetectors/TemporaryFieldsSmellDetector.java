package org.ifyounoseyounose.backend.smelldetectors;

import com.github.javaparser.StaticJavaParser;
import com.github.javaparser.ast.CompilationUnit;

import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.body.BodyDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.body.TypeDeclaration;
import com.github.javaparser.ast.body.VariableDeclarator;
import com.github.javaparser.ast.expr.AssignExpr;
import com.github.javaparser.ast.expr.BinaryExpr;
import com.github.javaparser.ast.expr.MethodCallExpr;

import org.ifyounoseyounose.backend.SmellReport;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class TemporaryFieldsSmellDetector extends LimitableSmellDetector implements JavaParserSmellDetector, SmellDetector  {

    public SmellReport detectSmell(HashMap<CompilationUnit, File> compilationUnits) {
        SmellReport smellReport = new SmellReport();
        TemporaryFieldsCollector visitor = new TemporaryFieldsCollector();

        for (CompilationUnit compilationUnit : compilationUnits.keySet()) {

                boolean occurred;//Checks if an instance variable has already occurred in the method
                HashMap<VariableDeclarator, Integer> variables = new HashMap<>();// Stores instance variables and there amount of occurrences
                for (TypeDeclaration<?> typeDec : compilationUnit.getTypes()) {// Finds the instance variables and stores them in a hashmap
                    for (BodyDeclaration<?> member : typeDec.getMembers()) {
                        member.toFieldDeclaration().ifPresent(field -> {
                            for (VariableDeclarator variable : field.getVariables()) {
                                variables.put(variable, 0);
                            }
                        });
                    }
                }
                for(MethodDeclaration md:compilationUnit.findAll(MethodDeclaration.class)){// Checks each method
                    occurred=false;
                    for(BinaryExpr be: md.findAll(BinaryExpr.class)){// Checks all binary expressions against the instance variable
                        for (VariableDeclarator v : variables.keySet()) {
                            List<Node> children = be.getChildNodes();
                            for (Node chi : children) {
                                if (chi.toString().equals(v.getNameAsString())&&!occurred) {// If the variable is found iterate but only if its the first cccurence in the method
                                    occurred=true;
                                    variables.put(v, variables.get(v)+1);
                                }
                            }
                        }
                    }
                    for(MethodCallExpr me: md.findAll(MethodCallExpr.class)){// Checks all method expressions against the variable
                        for (VariableDeclarator v : variables.keySet()) {
                            List<Node> children = me.getChildNodes();
                            for (Node chi : children) {
                                if (chi.toString().equals(v.getNameAsString())&&!occurred) {
                                    occurred=true;
                                    variables.put(v, variables.get(v)+1);
                                }
                            }

                        }
                    }
                    for(AssignExpr me: md.findAll(AssignExpr.class)){// Checks all assignment against the variable
                        for (VariableDeclarator v : variables.keySet()) {
                            List<Node> children = me.getChildNodes();
                            for (Node chi : children) {
                                if (chi.toString().equals(v.getNameAsString())&&!occurred) {
                                    occurred=true;
                                    variables.put(v, variables.get(v)+1);
                                }
                            }

                        }
                    }
                }
                for(VariableDeclarator v: variables.keySet()){
                    List<Integer> lines = new ArrayList<>();
                    if(variables.get(v)<limit){// If less than user set limit add declaration of the variable to the smell report
                        visitor.addLineNumbers(v,lines);
                        smellReport.addToReport(compilationUnits.get(compilationUnit), lines);
                    }
                }

        }

        return smellReport;
    }
    public String getSmellName() {
        return "TemporaryFields";
    }
}


