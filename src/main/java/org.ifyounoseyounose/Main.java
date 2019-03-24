package  org.ifyounoseyounose;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import com.github.javaparser.StaticJavaParser;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.visitor.VoidVisitor;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
public class Main extends Application {
    private static final String FILE_PATH = "src/main/java/org.ifyounoseyounose/ReversePolishNotation.java";

    @Override
    public void start(Stage primaryStage) throws IOException {
        primaryStage.setTitle("Code Smeller 3000");
        Parent root = FXMLLoader.load(getClass().getResource("CodeSmeller.fxml"));
        primaryStage.setScene(new Scene(root, 800,600));
        primaryStage.show();
    }
    private static Parent loadFXML(String fxml) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource(fxml + ".fxml"));
        return fxmlLoader.load();
    }

    public static void main(String[] args) throws Exception  {
        System.out.println(System.getProperty("user.dir"));
        //CompilationUnit
        CompilationUnit cu = StaticJavaParser.parse(new File(FILE_PATH));

        VoidVisitor<?> methodNameVisitor = new MethodNamePrinter();
        methodNameVisitor.visit(cu, null);

        List<String> methodNames = new ArrayList<>();
        VoidVisitor<List<String>> methodNameCollector = new MethodNameCollector();
        methodNameCollector.visit(cu, methodNames);
        methodNames.forEach(n -> System.out.println("Method Name Collected: " + n));
        launch(args);
    }

}