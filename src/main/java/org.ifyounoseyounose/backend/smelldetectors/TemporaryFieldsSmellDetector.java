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
                boolean occured = false;
                CompilationUnit cu = StaticJavaParser.parse(compilationUnits.get(compilationUnit));
                HashMap<VariableDeclarator, Integer> variables = new HashMap<>();
                for (TypeDeclaration<?> typeDec : cu.getTypes()) {
                    for (BodyDeclaration<?> member : typeDec.getMembers()) {
                        //System.out.println(member.toEnumDeclaration());
                        member.toFieldDeclaration().ifPresent(field -> {
                            for (VariableDeclarator variable : field.getVariables()) {
                                variables.put(variable, 0);
                            }
                        });
                    }
                }
                for(MethodDeclaration md:cu.findAll(MethodDeclaration.class)){
                    occured=false;
                    for(BinaryExpr be: md.findAll(BinaryExpr.class)){
                        for (VariableDeclarator v : variables.keySet()) {
                            List<Node> children = be.getChildNodes();
                            for (Node chi : children) {
                                if (chi.toString().equals(v.getNameAsString())&&!occured) {
                                    occured=true;
                                    variables.put(v, variables.get(v)+1);
                                }
                            }
                        }
                    }
                    for(MethodCallExpr me: md.findAll(MethodCallExpr.class)){
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
                    if(variables.get(v)<limit){
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


