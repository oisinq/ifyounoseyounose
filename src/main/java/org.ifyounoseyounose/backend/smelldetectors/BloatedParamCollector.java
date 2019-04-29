package org.ifyounoseyounose.backend.smelldetectors;


import com.github.javaparser.ast.NodeList;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.body.Parameter;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;

import java.util.List;

public class BloatedParamCollector extends VoidVisitorAdapter<List<Integer>> {
    private int limit = 20;


    @Override
    public void visit(MethodDeclaration md, List<Integer> collector) {
        super.visit(md, collector);
        NodeList<Parameter> param = md.getParameters();

        if (param.size() >= limit) {
            //giving the line of the method declaration
            if (md.getRange().isPresent()) {
                collector.add(md.getRange().get().begin.line);
            }
        }
    }

    public void setLimit(int limit) {
        this.limit = limit;
    }
}
