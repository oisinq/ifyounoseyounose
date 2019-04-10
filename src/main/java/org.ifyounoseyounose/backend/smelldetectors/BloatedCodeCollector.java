package org.ifyounoseyounose.backend.smelldetectors;
import java.util.List;

import com.github.javaparser.Range;
import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;

import java.util.Optional;

public class BloatedCodeCollector extends VoidVisitorAdapter<List<Integer>> {

    @Override
    public void visit(MethodDeclaration md, List<Integer> collector){
        super.visit(md, collector);
        int limit = 20;

        if(md.getRange().get().end.line - md.getRange().get().begin.line >= limit){
            addLineNumbers(md, collector);
        }

    }

    private void addLineNumbers(Node node, List<Integer> collector) {
        Optional<Range> m = node.getRange();
        Range r = m.get();
        for (int lineNumber = r.begin.line; lineNumber <= r.end.line; lineNumber++) {
            collector.add(lineNumber);
        }
    }

}
