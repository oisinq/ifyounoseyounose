package org.ifyounoseyounose.backend.smelldetectors;

import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;

import java.util.List;


public class DataOnlyClassesClassCollector extends VoidVisitorAdapter<List<Integer>> {
    public void visit(ClassOrInterfaceDeclaration cd, List<Integer> collector) {
        super.visit(cd, collector);
        int numberMethods = cd.getMethods().size();
        int numberConstruct = cd.getConstructors().size();
        collector.add(numberMethods + numberConstruct);
    }
}