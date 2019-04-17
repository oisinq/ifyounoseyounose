package org.ifyounoseyounose.backend.smelldetectors;
import com.github.javaparser.Range;
import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;

import com.github.javaparser.ast.visitor.VoidVisitorAdapter;

import java.util.List;
import java.util.Optional;


public class inappropriateIntimacyDeclarationCollector extends VoidVisitorAdapter<List<Integer>> {

    @Override
    public void visit(ClassOrInterfaceDeclaration cd, List<Integer> collector) {//Gets all method declarations
        super.visit(cd, collector);
        addLineNumbers(cd, collector);
    }
    void addLineNumbers(Node node, List<Integer> collector) {//Gets the line of the method declaration
        Optional<Range> m = node.getRange();
        Range r = m.get();

        collector.add(r.begin.line);

    }
}