package org.ifyounoseyounose.backend.smelldetectors;

import com.github.javaparser.Range;
import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;
import java.util.List;


public class DeadCodeMethodCollector extends VoidVisitorAdapter<List<MethodDeclaration>> {

    @Override
    public void visit(MethodDeclaration md, List<MethodDeclaration> collector) {//Gets all method declarations
        super.visit(md, collector);
        collector.add(md);
    }

    void addLineNumbers(Node node, List<Integer> collector) {
        if (node.getRange().isPresent()) {
            Range r = node.getRange().get();
            for (int lineNumber = r.begin.line; lineNumber <= r.end.line; lineNumber++) {
                collector.add(lineNumber);
            }
        }
    }
}