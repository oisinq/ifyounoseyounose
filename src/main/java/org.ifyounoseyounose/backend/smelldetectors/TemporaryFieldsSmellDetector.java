package org.ifyounoseyounose.backend.smelldetectors;

import com.github.javaparser.JavaParser;
import com.github.javaparser.StaticJavaParser;
import com.github.javaparser.ast.CompilationUnit;

import com.github.javaparser.ast.body.BodyDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.body.TypeDeclaration;
import com.github.javaparser.ast.body.VariableDeclarator;
import com.github.javaparser.ast.visitor.VoidVisitor;

import org.ifyounoseyounose.backend.SmellReport;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class TemporaryFieldsSmellDetector implements JavaParserSmellDetector, SmellDetector {

    public SmellReport detectSmell(HashMap<CompilationUnit, File> compilationUnits) {
        SmellReport smellReport = new SmellReport();
        VoidVisitor<List<MethodDeclaration>> visitor = new TemporaryFieldsCollector();
        for (CompilationUnit compilationUnit : compilationUnits.keySet()) {
            try {
                CompilationUnit cu = StaticJavaParser.parse(compilationUnits.get(compilationUnit));
                HashMap<VariableDeclarator, Integer> variables = new HashMap<>();
                for (TypeDeclaration<?> typeDec : cu.getTypes()) {
                    for (BodyDeclaration<?> member : typeDec.getMembers()) {
                        member.toFieldDeclaration().ifPresent(field -> {
                            for (VariableDeclarator variable : field.getVariables()) {
                                variables.put(variable, 0);
                            }
                        });
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


