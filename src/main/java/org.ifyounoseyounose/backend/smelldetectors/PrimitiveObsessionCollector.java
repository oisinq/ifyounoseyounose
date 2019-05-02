package org.ifyounoseyounose.backend.smelldetectors;

import com.github.javaparser.Range;
import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.type.PrimitiveType;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;

import java.util.List;

/**
 * PrimitiveObsessionCollector - visits each PrimitiveType node and records its line number
 */
public class PrimitiveObsessionCollector extends VoidVisitorAdapter<List<Integer>> {

    @Override
    public void visit(PrimitiveType md, List<Integer> collector) {
        super.visit(md, collector);
        addLineNumbers(md, collector);
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
