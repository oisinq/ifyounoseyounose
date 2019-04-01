package org.ifyounoseyounose;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import com.github.javaparser.StaticJavaParser;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.visitor.VoidVisitor;
import org.ifyounoseyounose.GUI.Controller;
import org.ifyounoseyounose.javaparsertest.MethodNameCollector;
import org.ifyounoseyounose.javaparsertest.MethodNamePrinter;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.tools.JavaCompiler;

public class Main extends Application {
    private static final String FILE_PATH = "src/main/java/org.ifyounoseyounose/javaparsertest/ReversePolishNotation.java";

    @Override
    public void start(Stage primaryStage) throws IOException {
        primaryStage.setTitle("Code Smeller");
        final FXMLLoader loader = new FXMLLoader(getClass().getResource("CodeSmeller.fxml"));
        final Parent root = (Parent) loader.load();
        final Controller controller = loader.<Controller>getController();


        primaryStage.setScene(new Scene(root, 800,600));
        primaryStage.show();
    }

    public static void main(String[] args) throws Exception  {
        System.out.println(System.getProperty("user.dir"));

        JavaCompiler c;

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