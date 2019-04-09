package org.ifyounoseyounose.backend.smelldetectors;
import com.github.javaparser.Range;
import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;

import java.util.List;
import java.util.Optional;


public class DeadCodeMethodCollector extends VoidVisitorAdapter<List<MethodDeclaration>> {

    @Override
    public void visit(MethodDeclaration md, List<MethodDeclaration> collector) {
        super.visit(md, collector);
        collector.add(md);
    }

}