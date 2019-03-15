import com.github.javaparser.JavaParser;
import com.github.javaparser.StaticJavaParser;
import com.github.javaparser.ast.CompilationUnit;

public class ModifyingVisitorStarter {
    private static final String FILE_PATH = "src/main/java/ReversePolishNotation.java";

    public static void main(String[] args) {
        CompilationUnit cu = StaticJavaParser.parse(FILE_PATH);
    }
}
