package org.ifyounoseyounose.backend.smelldetectors;

import com.github.javaparser.Range;
import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.comments.Comment;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;

import java.util.List;


public class BloatedMethodCollector extends VoidVisitorAdapter<List<Integer>> {
    private int limit = 20;

    @Override
    public void visit(MethodDeclaration md, List<Integer> collector) {
        super.visit(md, collector);
        int commentNumber = 0;

        if (md.getRange().isPresent()) {
            int linesCount = md.getRange().get().end.line - md.getRange().get().begin.line;
            if (linesCount >= limit) {
                //gets the first line of the method
                List<Comment> comments = md.getAllContainedComments();
                List<Comment> orphaned = md.getOrphanComments();

                commentNumber += orphaned.size() + comments.size();

                if (linesCount - commentNumber >= limit) {
                    addLineNumbers(md, collector);
                }
            }
        }
    }

    private void addLineNumbers(Node node, List<Integer> collector) {
        if (node.getRange().isPresent()) {
            Range r = node.getRange().get();
            for (int lineNumber = r.begin.line; lineNumber <= r.end.line; lineNumber++) {
                collector.add(lineNumber);
            }
        }
    }

    public void setLimit(int limit) {
        this.limit = limit;
    }
}
