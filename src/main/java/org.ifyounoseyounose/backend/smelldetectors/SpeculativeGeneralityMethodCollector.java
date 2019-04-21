package org.ifyounoseyounose.backend.smelldetectors;

import com.github.javaparser.Range;
import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.comments.Comment;
import com.github.javaparser.ast.expr.BinaryExpr;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;

import java.util.List;
import java.util.Optional;

public class SpeculativeGeneralityMethodCollector extends VoidVisitorAdapter<List<Integer>> {
    @Override
    public void visit(MethodDeclaration md, List<Integer> collector) {// Collects method declarations and returns their lines
        super.visit(md, collector);

        String s = md.getBody().toString().substring(10, md.getBody().toString().length() - 2);//Gets the simple string version of method
        if ((s.trim().length() == 0) || s.contains("TO DO") || s.contains("TODO") || s.contains("todo") || s.contains("to do")) {//Checks if the method is empty of contains to do
            addLineNumbers(md, collector);//Collects the line numbers
        }
    }

    private void addLineNumbers(Node node, List<Integer> collector) {//Gets line number of the method declaration
        Optional<Range> m = node.getRange();
        Range r = m.get();

        collector.add(r.begin.line);

    }
}