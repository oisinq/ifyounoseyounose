package org.ifyounoseyounose.backend.smelldetectors;

import com.github.javaparser.Range;
import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;

import java.util.List;

public class SpeculativeGeneralityMethodCollector extends VoidVisitorAdapter<List<Integer>> {
    @Override
    public void visit(MethodDeclaration md, List<Integer> collector) {// Collects method declarations and returns their lines
        super.visit(md, collector);

        String s = md.getBody().toString().substring(10, md.getBody().toString().length() - 2);//Gets the simple string version of method
        if ((s.trim().length() == 0) || s.contains("TO DO") || s.contains("TODO") || s.contains("todo") || s.contains("to do")) {//Checks if the method is empty of contains to do
            addLineNumbers(md, collector);//Collects the line numbers
        }
    }

    void addLineNumbers(Node node, List<Integer> collector) {
        if (node.getRange().isPresent()) {
            Range r = node.getRange().get();
            collector.add(r.begin.line);
        }
    }
}
