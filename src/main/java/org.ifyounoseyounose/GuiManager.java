package org.ifyounoseyounose;

import com.google.common.eventbus.Subscribe;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import org.ifyounoseyounose.GUI.Controller;
import org.ifyounoseyounose.GUI.EventBusFactory;
import org.ifyounoseyounose.GUI.SetupController;
import org.ifyounoseyounose.backend.ReportBuilder;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class GuiManager extends Application {
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
        Scene setupScene=new Scene(setup, 1000,600);

        mainLoader = new FXMLLoader(getClass().getResource("CodeSmeller.fxml"));
        main =  mainLoader.load();
        mainApplicationController = mainLoader.getController();
        mainScene=new Scene(main, 1000,600);

        primaryStage.setScene(setupScene);

        setupController.setSecondScene(mainScene);
        mainApplicationController.setFirstScene(setupScene);

        ReportBuilder reportBuilder=new ReportBuilder();

        EventBusFactory.getEventBus().register(new Object() {
            @Subscribe
            public void setInputDirectory(EventBusFactory e){
                //HashMap<String,int> pass = ;
                reportBuilder.generateReport(e.getSmells(),e.getFile());//generate report wants a hashmap (List<SmellDetector> smells, File directory
                //hashmap with code smell as key, limit as value sure
            }
        });

        //String classString=Files.readString(Path.of("D:/Code/ifyounoseyounose/src/main/java/org.ifyounoseyounose/Main.java"));
        //Color color = Color.web("#56cbf9");
        //mainApplicationController.setCodeAreaText(classString,color,5);

        primaryStage.show();
    }
}
