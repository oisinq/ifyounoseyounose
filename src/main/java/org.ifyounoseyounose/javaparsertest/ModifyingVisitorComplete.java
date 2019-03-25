package org.ifyounoseyounose.javaparsertest;
import com.github.javaparser.StaticJavaParser;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.FieldDeclaration;
import com.github.javaparser.ast.expr.IntegerLiteralExpr;
import com.github.javaparser.ast.visitor.ModifierVisitor;

import java.io.File;
import java.util.regex.Pattern;

public class ModifyingVisitorComplete {

    private static class IntegerLiteralModifier extends ModifierVisitor<Void> {

        private static final Pattern LOOK_AHEAD_THREE = Pattern.compile("(\\d)(?=(\\d{3})+$)");

        @Override
        public FieldDeclaration visit(FieldDeclaration fd, Void arg) {
            super.visit(fd, arg);
            fd.getVariables().forEach(v ->
                v.getInitializer().ifPresent(i -> {
                    if (i instanceof IntegerLiteralExpr) {
                        v.setInitializer(formatWithUnderscores(((IntegerLiteralExpr) i).getValue()));
                    }
                })
            );
            return fd;
        }


        static String formatWithUnderscores(String value) {
            String withoutUnderscores = value.replace("_", "");
            return LOOK_AHEAD_THREE.matcher(withoutUnderscores).replaceAll("$1_");
        }
    }

    public static void main(String[] args) throws Exception {
        CompilationUnit cu = StaticJavaParser.parse(new File("src/main/java/ReversePolishNotation.java"));
        System.out.println(cu.getAllContainedComments());

        ModifierVisitor<?>numericLiteralVisitor=new IntegerLiteralModifier();
        numericLiteralVisitor.visit(cu, null);

       // System.out.println(cu.toString());
    }
}
