import com.github.javaparser.StaticJavaParser;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.visitor.VoidVisitor;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class Main {
    private static final String FILE_PATH = "src/main/java/ReversePolishNotation.java";

    public static void main(String[] args) throws Exception {
        System.out.println(System.getProperty("user.dir"));
        //CompilationUnit
        CompilationUnit cu = StaticJavaParser.parse(new File(FILE_PATH));

        VoidVisitor<?> methodNameVisitor = new MethodNamePrinter();
        methodNameVisitor.visit(cu, null);

        List<String> methodNames = new ArrayList<>();
        VoidVisitor<List<String>> methodNameCollector = new MethodNameCollector();
        methodNameCollector.visit(cu, methodNames);
        methodNames.forEach(n -> System.out.println("Method Name Collected: " + n));
    }
}