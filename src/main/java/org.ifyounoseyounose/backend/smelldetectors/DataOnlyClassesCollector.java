package org.ifyounoseyounose.backend.smelldetectors;


import com.github.javaparser.Range;
import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.body.ConstructorDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.comments.Comment;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;

import java.util.List;


public class DataOnlyClassesCollector extends VoidVisitorAdapter<List<Integer>> {

    boolean fullClass = false;

    @Override
    public void visit(MethodDeclaration md, List<Integer> collector) {
        super.visit(md, collector);

        //converting md to a string removes any whitespace

        String mdString;
        //mdString only contains the body of the method with removed blank lines & comments removed
        for (Comment child : md.getAllContainedComments()) {
            md.remove(child);
        }
        for (Comment child : md.getOrphanComments()) {
            md.remove(child);
        }

        mdString = md.getBody().toString();


        if ((md.getName().toString().contains("get") && !md.getType().isVoidType()) ||
                (md.getName().toString().contains("set") && md.getType().isVoidType())) {
            collector.add(md.getRange().get().begin.line);
        }

        if (mdString.contains("this.") && md.getType().isVoidType()) {
            collector.add(md.getRange().get().begin.line);
        }

    }


    //need to check constructors also
    @Override
    public void visit(ConstructorDeclaration cd, List<Integer> collector) {
        String mdString;

        //mdString only contains the body of the method with removed blank lines & comments removed
        for (Comment child : cd.getAllContainedComments()) {
            cd.remove(child);
        }
        for (Comment child : cd.getOrphanComments()) {
            cd.remove(child);
        }

        mdString = cd.getBody().toString();

        if (mdString.contains("this.")) {
            collector.add(cd.getRange().get().begin.line);
        }


    }

    void addLineNumbers(Node node, List<Integer> collector) {
        if (node.getRange().isPresent()) {
            Range r = node.getRange().get();
            collector.add(r.begin.line);
        }
    }

}