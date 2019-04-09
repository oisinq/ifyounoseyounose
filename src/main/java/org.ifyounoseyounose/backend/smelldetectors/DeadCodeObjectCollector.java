package org.ifyounoseyounose.backend.smelldetectors;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.expr.VariableDeclarationExpr;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;

import java.util.List;


public class DeadCodeObjectCollector extends VoidVisitorAdapter<List<VariableDeclarationExpr>> {

    @Override
    public void visit(VariableDeclarationExpr md, List<VariableDeclarationExpr> collector) {
        super.visit(md, collector);
        collector.add(md);
    }


}