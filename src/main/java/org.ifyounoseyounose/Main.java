package org.ifyounoseyounose;

import com.google.common.eventbus.Subscribe;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.ifyounoseyounose.GUI.Controller;
import org.ifyounoseyounose.GUI.EventBusFactory;
import org.ifyounoseyounose.GUI.SetupController;

import java.io.File;
import java.io.IOException;

public class Main extends Application {
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

        launch(args);
    }

}