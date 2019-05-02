package org.ifyounoseyounose.backend.smelldetectors;

import com.github.javaparser.Range;
import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.stmt.ForStmt;
import com.github.javaparser.ast.stmt.IfStmt;
import com.github.javaparser.ast.stmt.WhileStmt;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;

import java.util.List;

/**
 * ArrowheadedIndentationCollector - JavaParser collector that visits IfStmt, ForStmt and WhileStmt nodes, and checks indentation levels
 */
public class ArrowheadedIndentationCollector extends VoidVisitorAdapter<List<Integer>> {
    // This records the current depth, increasing as it enters if, while and for statements
    private int depth = 0;
    private int limit;

    void setLimit(int limit) {
        this.limit = limit;
    }

    @Override
    public void visit(IfStmt md, List<Integer> collector) {
        depth++; // When we're at an if statement, we increase the depth
        if (depth >= limit) { // If we hit the limit, then we records the line numbers of the statement
            addLineNumbers(md, collector);
        } else { // Otherwise, we recursively continue to search down the abstract syntax tree
            super.visit(md, collector);
        }
        depth--;
    }

    @Override
    public void visit(ForStmt md, List<Integer> collector) {
        depth++; // When we're at an if statement, we increase the depth
        if (depth >= limit) { // If we hit the limit, then we records the line numbers of the statement
            addLineNumbers(md, collector);
        } else { // Otherwise, we recursively continue to search down the abstract syntax tree
            super.visit(md, collector);
        }
        depth--;
    }

    @Override
    public void visit(WhileStmt md, List<Integer> collector) {
        depth++; // When we're at an if statement, we increase the depth
        if (depth >= limit) { // If we hit the limit, then we records the line numbers of the statement
            addLineNumbers(md, collector);
        } else { // Otherwise, we recursively continue to search down the abstract syntax tree
            super.visit(md, collector);
        }
        depth--;
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
