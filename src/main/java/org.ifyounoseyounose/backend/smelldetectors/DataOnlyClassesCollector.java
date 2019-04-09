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
    public void visit(MethodDeclaration md, List<Integer> collector){
        super.visit(md, collector);
       // System.out.println(md.getDeclarationAsString());
        // two options, first line in the method is return something
        // or this.something = something

            String returnStatement = md.getBody().toString();
            returnStatement = returnStatement.replaceAll(md.getDeclarationAsString(), "");
            //returnStatement = returnStatement.replaceAll("", "");
           // System.out.println(returnStatement);
           // System.out.println(md.getBegin().get().line);
        if(md.getName().toString().contains("get") || md.getName().toString().contains("set")){
            addLineNumbers(md,collector);
        }
            else if(md.getRange().get().end.line-md.getRange().get().begin.line<=2
                                  && (returnStatement.contains("return") || returnStatement.contains("this") )) {
                addLineNumbers(md,collector);
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
