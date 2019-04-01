package org.ifyounoseyounose;

import com.google.common.eventbus.Subscribe;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import com.github.javaparser.StaticJavaParser;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.visitor.VoidVisitor;
import org.ifyounoseyounose.GUI.Controller;
import org.ifyounoseyounose.GUI.EventBusFactory;
import org.ifyounoseyounose.GUI.SetupController;
import org.ifyounoseyounose.javaparsertest.MethodNameCollector;
import org.ifyounoseyounose.javaparsertest.MethodNamePrinter;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


public class Main extends Application {
    private static final String FILE_PATH = "src/main/java/org.ifyounoseyounose/javaparsertest/ReversePolishNotation.java";
    FXMLLoader mainLoader=null;
    Parent main=null;
    Controller mainApplicationController=null;
    Scene mainScene=null;
    @Override
    public void start(Stage primaryStage) throws IOException {


        primaryStage.setTitle("Code Smeller");
        final FXMLLoader setupLoader = new FXMLLoader(getClass().getResource("SetupScreen.fxml"));
        final Parent setup =  setupLoader.load();
        final SetupController setupController = setupLoader.getController();
        File Directorytosmell=null;
        Scene setupScene=new Scene(setup, 800,600);

        mainLoader = new FXMLLoader(getClass().getResource("CodeSmeller.fxml"));
        main =  mainLoader.load();
        mainApplicationController = mainLoader.getController();
        mainScene=new Scene(main, 800,600);

        primaryStage.setScene(setupScene);


        setupController.setSecondScene(mainScene);
        mainApplicationController.setFirstScene(setupScene);
        //mainApplicationController.setInputDirectory(setupController.getInputDirectory());
        //mainApplicationController.displayTreeView(setupController.getInputDirectory());
        //setupController.getInputDirectory();
        //setupScene.property

        primaryStage.show();

    }

    public static void main(String[] args) throws Exception  {
        EventBusFactory.getEventBus().register(new Main());//TODO TEST IF I NEED THIS
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