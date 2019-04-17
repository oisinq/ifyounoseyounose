package org.ifyounoseyounose.backend.smelldetectors;

import com.github.javaparser.Range;
import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;

import java.util.List;
import java.util.Optional;

public class InappropriateIntimacyDeclarationCollector extends VoidVisitorAdapter<List<Integer>> {
    void addLineNumbers(Node node, List<Integer> collector) {//Gets the line of the given declaration
        Optional<Range> m = node.getRange();
        Range r = m.get();

        collector.add(r.begin.line);

    }
}
