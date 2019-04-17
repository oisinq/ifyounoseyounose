package org.ifyounoseyounose.backend.smelldetectors;
import com.github.javaparser.Range;
import com.github.javaparser.ast.Node;

import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.body.VariableDeclarator;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;

import java.util.List;
import java.util.Optional;

public class TemporaryFieldsCollector extends VoidVisitorAdapter<List<Integer>> {

    public void addLineNumbers(Node node, List<Integer> collector) {
        Optional<Range> m = node.getRange();
        Range r = m.get();
       collector.add( r.begin.line);
    }
}
