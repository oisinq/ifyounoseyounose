package org.ifyounoseyounose.backend.smelldetectors;

import com.github.javaparser.Range;
import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.body.VariableDeclarator;
import com.github.javaparser.ast.expr.*;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;

import java.util.List;

/**
 * TooManyLiteralsCollector - JavaParser collector for visiting the individual nodes on the Abstract Syntax Tree
 */
public class TooManyLiteralsCollector extends VoidVisitorAdapter<List<Integer>> {

    @Override
    public void visit(IntegerLiteralExpr md, List<Integer> collector) {
        super.visit(md, collector);
        addLineNumbers(md, collector);
    }

    @Override
    public void visit(DoubleLiteralExpr md, List<Integer> collector) {
        super.visit(md, collector);
        addLineNumbers(md, collector);
    }

    @Override
    public void visit(CharLiteralExpr md, List<Integer> collector) {
        super.visit(md, collector);
        addLineNumbers(md, collector);
    }

    // If we visit a declaration, we don't want to count any declaration as literals, so we ignore its child nodes
    @Override
    public void visit(VariableDeclarationExpr dec, List<Integer> collector) {

    }

    @Override
    public void visit(VariableDeclarator dec, List<Integer> collector) {

    }

    @Override
    public void visit(AssignExpr dec, List<Integer> collector) {

    }

    private void addLineNumbers(Node node, List<Integer> collector) {
        if (node.getRange().isPresent()) {
            Range r = node.getRange().get();
            for (int lineNumber = r.begin.line; lineNumber <= r.end.line; lineNumber++) {
                collector.add(lineNumber);
            }
        }
    }
}
