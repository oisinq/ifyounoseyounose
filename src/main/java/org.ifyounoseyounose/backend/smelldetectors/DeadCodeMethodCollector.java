package org.ifyounoseyounose.backend.smelldetectors;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;
import com.github.javaparser.ast.body.CallableDeclaration.Signature;
import java.util.List;


public class DeadCodeMethodCollector extends VoidVisitorAdapter<List<Signature>> {

    @Override
    public void visit(MethodDeclaration md, List<Signature> collector) {
        super.visit(md, collector);
        collector.add(md.getSignature());
    }
}