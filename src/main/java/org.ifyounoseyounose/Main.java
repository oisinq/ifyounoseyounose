package org.ifyounoseyounose;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import com.github.javaparser.StaticJavaParser;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.visitor.VoidVisitor;
import org.ifyounoseyounose.GUI.Controller;
import org.ifyounoseyounose.GUI.SetupController;
import org.ifyounoseyounose.javaparsertest.MethodNameCollector;
import org.ifyounoseyounose.javaparsertest.MethodNamePrinter;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
public class Main extends Application {
    private static final String FILE_PATH = "src/main/java/org.ifyounoseyounose/javaparsertest/ReversePolishNotation.java";

    @Override
    public void start(Stage primaryStage) throws IOException {
        primaryStage.setTitle("Code Smeller");
        final FXMLLoader setupLoader = new FXMLLoader(getClass().getResource("SetupScreen.fxml"));
        final Parent setup =  setupLoader.load();
        final SetupController setupController = setupLoader.<SetupController>getController();

        Scene setupScene=new Scene(setup, 800,600);

        final FXMLLoader mainLoader = new FXMLLoader(getClass().getResource("CodeSmeller.fxml"));
        final Parent main =  mainLoader.load();
        final Controller mainApplicationController = mainLoader.<Controller>getController();
        Scene mainScene=new Scene(main, 800,600);

        primaryStage.setScene(setupScene);


        // injecting second scene into the controller of the first scene
        //FirstController firstPaneController = (FirstController) firstPaneLoader.getController();
        //firstPaneController.setSecondScene(secondScene);

        // injecting first scene into the controller of the second scene
        //SecondController secondPaneController = (SecondController) secondPageLoader.getController();
        //secondPaneController.setFirstScene(firstScene);
        setupController.setSecondScene(mainScene);
        mainApplicationController.setFirstScene(setupScene);

        primaryStage.show();
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