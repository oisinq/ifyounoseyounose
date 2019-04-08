package org.ifyounoseyounose;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.ifyounoseyounose.GUI.Controller;
import org.ifyounoseyounose.GUI.SetupController;
import java.io.IOException;

public class Gui extends Application {
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
        Scene setupScene=new Scene(setup, 800,600);

        mainLoader = new FXMLLoader(getClass().getResource("CodeSmeller.fxml"));
        main =  mainLoader.load();
        mainApplicationController = mainLoader.getController();
        mainScene=new Scene(main, 800,600);

        primaryStage.setScene(setupScene);


        setupController.setSecondScene(mainScene);
        mainApplicationController.setFirstScene(setupScene);

        primaryStage.show();
    }
}
