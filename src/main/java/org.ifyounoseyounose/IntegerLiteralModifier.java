package  org.ifyounoseyounose;
import com.github.javaparser.ast.body.FieldDeclaration;
import com.github.javaparser.ast.visitor.ModifierVisitor;

public class IntegerLiteralModifier extends ModifierVisitor<Void> {


    @Override
    public FieldDeclaration visit(FieldDeclaration fd, Void arg) {
        super.visit(fd, arg);
        return fd;
    }
}
