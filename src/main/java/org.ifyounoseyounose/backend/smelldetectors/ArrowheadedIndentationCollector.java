package org.ifyounoseyounose.backend.smelldetectors;

import com.github.javaparser.Range;
import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.stmt.ForStmt;
import com.github.javaparser.ast.stmt.IfStmt;
import com.github.javaparser.ast.stmt.WhileStmt;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;

import java.util.List;
import java.util.Optional;

public class ArrowheadedIndentationCollector extends VoidVisitorAdapter<List<Integer>> {
    private int depth = 0;
    private int limit;

    void setLimit(int limit) {
        this.limit = limit;
    }

    @Override
    public void visit(IfStmt md, List<Integer> collector) {
        depth++;
        if (depth >= limit) {
            addLineNumbers(md, collector);
        } else {
            super.visit(md, collector);
        }
        depth--;
    }

    @Override
    public void visit(ForStmt md, List<Integer> collector) {
        depth++;
        if (depth >= limit) {
            addLineNumbers(md, collector);
        } else {
            super.visit(md, collector);
        }
        depth--;
    }

    @Override
    public void visit(WhileStmt md, List<Integer> collector) {
        depth++;
        if (depth >= limit) {
            addLineNumbers(md, collector);
        } else {
            super.visit(md, collector);
        }
        depth--;
    }

    private void addLineNumbers(Node node, List<Integer> collector) {
        Optional<Range> m = node.getRange();
        Range r = m.get();
        for (int lineNumber = r.begin.line; lineNumber <= r.end.line; lineNumber++) {
            collector.add(lineNumber);
        }
    }
}
