package org.ifyounoseyounose.backend.smelldetectors;
import java.util.List;


import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;



public class BloatedMethodCollector extends VoidVisitorAdapter<List<Integer>> {

    @Override
    public void visit(MethodDeclaration md, List<Integer> collector){
        super.visit(md, collector);
        int limit = collector.get(0);

        if(md.getRange().get().end.line - md.getRange().get().begin.line >= limit){
            //gets the first line of the method
            collector.add(md.getRange().get().begin.line);
        }
    }

}
