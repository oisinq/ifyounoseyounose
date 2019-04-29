package org.ifyounoseyounose.backend.smelldetectors;

import com.github.javaparser.Range;
import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;

import java.util.List;

public class TemporaryFieldsCollector extends VoidVisitorAdapter<List<Integer>> {

    void addLineNumbers(Node node, List<Integer> collector) {
        if (node.getRange().isPresent()) {
            Range r = node.getRange().get();
            collector.add(r.begin.line);
        }
    }
}
