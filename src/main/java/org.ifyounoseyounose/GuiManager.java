package org.ifyounoseyounose;

import com.google.common.eventbus.Subscribe;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import org.ifyounoseyounose.GUI.Controller;
import org.ifyounoseyounose.GUI.EventBusFactory;
import org.ifyounoseyounose.GUI.SetupController;
import org.ifyounoseyounose.backend.CompleteReport;
import org.ifyounoseyounose.backend.ReportBuilder;

import java.io.IOException;
import java.util.HashMap;
import java.util.Set;


public class GuiManager extends Application {
    @Override
    public void start(Stage primaryStage) throws IOException {

        primaryStage.setTitle("IfYouNoseYouNose");

        //load in the setup screen
        final FXMLLoader setupLoader = new FXMLLoader(getClass().getResource("SetupScreen.fxml"));
        final Parent setup = setupLoader.load();
        final SetupController setupController = setupLoader.getController();
        Scene setupScene = new Scene(setup, 1000, 600);

        //load in the main controller
        FXMLLoader mainLoader = new FXMLLoader(getClass().getResource("CodeSmeller.fxml"));
        Parent main = mainLoader.load();
        Controller mainApplicationController = mainLoader.getController();
        Scene mainScene = new Scene(main, 1000, 600);

        //set the stage
        primaryStage.setScene(setupScene);

        //let the setup screen know where it can go once its ready
        setupController.setSecondScene(mainScene);

        //load in logo
        primaryStage.getIcons().add(new Image(GuiManager.class.getResourceAsStream("Logo.jpg")));
        //display the scene
        primaryStage.show();
        //event handler for when the window is closed
        primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent event) {
                System.exit(0);
            }
        });


        ReportBuilder reportBuilder = new ReportBuilder();
        //this will get the result from the setup screen whenever it is set.
        EventBusFactory.getEventBus().register(new Object() {
            @Subscribe
            public void setInputDirectory(EventBusFactory e) {
                //this gets the report from report builder using the smells passed by the event bus
                CompleteReport completeReport = reportBuilder.generateReport(e.getSmells(), e.getFile());
                //hand the report to main application
                mainApplicationController.setCompleteReport(completeReport);

                //this is a window listener for the new scene similar to the one above
                mainScene.getWindow().setOnCloseRequest(new EventHandler<WindowEvent>() {
                    @Override
                    public void handle(WindowEvent event) {
                        System.exit(0);
                    }
                });
                //hashmap with code smell as key, limit as value sure
            }
        });




    }
}
