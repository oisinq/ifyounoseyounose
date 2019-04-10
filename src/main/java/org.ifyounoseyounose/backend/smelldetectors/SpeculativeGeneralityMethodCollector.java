package org.ifyounoseyounose.backend.smelldetectors;

import com.github.javaparser.Range;
import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;

import java.util.List;
import java.util.Optional;

public class SpeculativeGeneralityMethodCollector extends VoidVisitorAdapter<List<Integer>> {
    @Override
    public void visit(MethodDeclaration md, List<Integer> collector) {
        super.visit(md, collector);
        String s = md.getBody().toString().substring(10, md.getBody().toString().length()-2);
        if(s.trim().length() ==0) {
            addLineNumbers(md, collector);
        }
    }
    private void addLineNumbers(Node node, List<Integer> collector) {
        Optional<Range> m = node.getRange();
        Range r = m.get();
        for (int lineNumber = r.begin.line; lineNumber <= r.end.line; lineNumber++) {
            System.out.println(lineNumber);
            collector.add(lineNumber);
        }
    }
}
