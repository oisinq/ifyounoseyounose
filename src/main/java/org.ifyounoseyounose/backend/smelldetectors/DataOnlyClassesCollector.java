package org.ifyounoseyounose.backend.smelldetectors;

import com.github.javaparser.Range;
import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.body.MethodDeclaration;

import com.github.javaparser.ast.visitor.VoidVisitorAdapter;

import java.util.List;
import java.util.Optional;

public class DataOnlyClassesCollector extends VoidVisitorAdapter<List<Integer>> {


    @Override
    public void visit(MethodDeclaration md, List<Integer> collector) {
        super.visit(md, collector);

        String body = md.getBody().toString(); //getting body of the method as a string
        //check if get or set is in the method names
        if (md.getRange().get().end.line - md.getRange().get().begin.line <= 2) {
            body = body.replaceAll(md.getDeclarationAsString(), "");
            if (body.contains("return") || body.contains("this")) {
                addLineNumbers(md, collector);
            } else if ((md.getName().toString().contains("get") && body.contains("return")) ||
                    (md.getName().toString().contains("set") && !body.contains("return"))) {
                addLineNumbers(md, collector);
            }
        } else {
            collector.clear(); //list is emptied if it is not a data only method
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
