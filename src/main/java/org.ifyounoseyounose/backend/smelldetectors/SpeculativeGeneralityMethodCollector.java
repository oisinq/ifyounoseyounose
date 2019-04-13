package org.ifyounoseyounose.backend.smelldetectors;

import com.github.javaparser.Range;
import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.comments.Comment;
import com.github.javaparser.ast.expr.BinaryExpr;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;

import java.util.List;
import java.util.Optional;

public class SpeculativeGeneralityMethodCollector extends VoidVisitorAdapter<List<Integer>> {
    @Override
    public void visit(MethodDeclaration md, List<Integer> collector) {
        super.visit(md, collector);
        //System.out.println(md);
        MethodDeclaration m1=md;
        String methodBody="";
        for (Comment child : m1.getAllContainedComments()) {
            System.out.println(m1);
            System.out.println(m1.remove(child));
            System.out.println(m1);
            methodBody = m1.getBody().toString();
            if(methodBody.substring(10, md.getBody().toString().length() - 2).trim().length()==0)
            {
                addLineNumbers(md, collector);
            }
            //System.out.println("Removed child"+child+"Method"+methodBody);
        }
            String s = md.getBody().toString().substring(10, md.getBody().toString().length() - 2);
            if ((s.trim().length() == 0) || s.contains("TO DO") || s.contains("TODO") || s.contains("todo") || s.contains("to do")) {
                addLineNumbers(md, collector);
            }
        }

    private void addLineNumbers(Node node, List<Integer> collector) {//Gets line number of input
        Optional<Range> m = node.getRange();
        Range r = m.get();

            collector.add(r.begin.line);

    }
}
