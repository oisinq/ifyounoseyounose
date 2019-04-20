package org.ifyounoseyounose.backend.smelldetectors;

import com.github.javaparser.Range;
import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.body.ConstructorDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.comments.*;


import com.github.javaparser.ast.visitor.VoidVisitorAdapter;

import java.util.List;
import java.util.Optional;

public class DataOnlyClassesCollector extends VoidVisitorAdapter<List<Integer>> {


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

         if (mdString.contains("this." ) && md.getType().isVoidType()){
             addLineNumbers(md, collector);
         } else if (mdString.contains("return") && !md.getType().isVoidType()) {
             addLineNumbers(md, collector);
         }

      else if ((md.getName().toString().contains("get") && !md.getType().isVoidType()) ||
                    (md.getName().toString().contains("set") && md.getType().isVoidType())) {
                 addLineNumbers(md, collector);
            }
         else {
            collector.clear(); //list is emptied if it is not a data only method
        }

    }

    //need to check constructors also
    @Override
    public void visit(ConstructorDeclaration cd, List<Integer> collector) {
        String mdString =cd.getBody().toString();
        //mdString only contains the body of the method with removed blank lines & comments removed
        for (Comment child : cd.getAllContainedComments()) {
            cd.remove(child);
        }
        for (Comment child : cd.getOrphanComments()) {
            cd.remove(child);
        }

        mdString = cd.getBody().toString();

        if(mdString.contains("this." )){
            addLineNumbers(cd, collector);
        }
        else {
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
