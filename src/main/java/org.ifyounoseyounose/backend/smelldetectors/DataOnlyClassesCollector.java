package org.ifyounoseyounose.backend.smelldetectors;

import com.github.javaparser.Range;
import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.body.MethodDeclaration;

import com.github.javaparser.ast.expr.VariableDeclarationExpr;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;

import java.util.List;
import java.util.Optional;

public class DataOnlyClassesCollector extends VoidVisitorAdapter<List<Integer>> {


    @Override
    public void visit(MethodDeclaration md, List<Integer> collector) {
        super.visit(md, collector);

        String body = md.getBody().toString(); //
        body = body.replaceAll(md.getDeclarationAsString(), "");

        if (md.getName().toString().contains("get") || md.getName().toString().contains("set")) {
            addLineNumbers(md,collector);
        } else if (md.getRange().get().end.line - md.getRange().get().begin.line <= 2
                && (body.contains("return") || body.contains("this"))) {
          addLineNumbers(md,collector);
        }
        else{
            //this is not a DataOnlyClass so we can stop looking
            //empty the List
            collector.clear();
        }

    }

    @Override
    public void visit(VariableDeclarationExpr vd, List<Integer> collector) {
        super.visit(vd, collector);

    }


    private void addLineNumbers(Node node, List<Integer> collector) {
        Optional<Range> m = node.getRange();
        Range r = m.get();
        for (int lineNumber = r.begin.line; lineNumber <= r.end.line; lineNumber++) {
            collector.add(lineNumber);
        }
    }

}
