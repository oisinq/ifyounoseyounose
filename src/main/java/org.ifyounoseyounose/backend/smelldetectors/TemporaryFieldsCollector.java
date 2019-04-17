package org.ifyounoseyounose.backend.smelldetectors;
import com.github.javaparser.ast.Node;

import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;

import java.util.List;

public class TemporaryFieldsCollector extends VoidVisitorAdapter<List<MethodDeclaration>> {

    @Override
    public void visit(MethodDeclaration md, List<MethodDeclaration> collector) {
        super.visit(md, collector);
        List<Node> temp=md.getChildNodes();
        for(Node child: temp){
            System.out.println(child);
        }
        collector.add(md);
    }
}
