package org.ifyounoseyounose.backend.smelldetectors;

import com.github.javaparser.Range;
import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.stmt.SwitchStmt;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;

import java.util.List;

/**
 * SwitchStatementCollector - JavaParser collector that visits all SwitchStmt Nodes in the Abstract Syntax Tree
 */
public class SwitchStatementCollector extends VoidVisitorAdapter<List<Integer>> {
    int limit = 0;

    @Override
    public void visit(SwitchStmt md, List<Integer> collector) {
        super.visit(md, collector);

        // This checks if the number of cases is over the limit. If it is, we add the lines to the list.
        // We subtract one because getChildNodes() also includes the condition of the switch statement
        if ((md.getChildNodes().size() - 1) > limit) {
            addLineNumbers(md, collector);
        }
    }

    public void setLimit(int limit) {
        this.limit = limit;
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