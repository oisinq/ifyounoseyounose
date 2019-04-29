package org.ifyounoseyounose;

import com.google.common.eventbus.Subscribe;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
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
        primaryStage.setTitle("Code Smeller");
        final FXMLLoader setupLoader = new FXMLLoader(getClass().getResource("SetupScreen.fxml"));
        final Parent setup =  setupLoader.load();
        final SetupController setupController = setupLoader.getController();
        Scene setupScene=new Scene(setup, 1000,600);

        FXMLLoader mainLoader = new FXMLLoader(getClass().getResource("CodeSmeller.fxml"));
        Parent main =  mainLoader.load();
        Controller mainApplicationController = mainLoader.getController();
        Scene mainScene=new Scene(main, 1000,600);

        primaryStage.setScene(setupScene);

        setupController.setSecondScene(mainScene);
        mainApplicationController.setFirstScene(setupScene);

        ReportBuilder reportBuilder=new ReportBuilder();

        EventBusFactory.getEventBus().register(new Object() {
            @Subscribe
            public void setInputDirectory(EventBusFactory e){
                CompleteReport completeReport =reportBuilder.generateReport(e.getSmells(),e.getFile());//generate report wants a hashmap (List<SmellDetector> smells, File directory
                HashMap<String,Integer> test=e.getSmells();
                Set<String> s=test.keySet();
                System.out.println(s.toString());
                mainApplicationController.setCompleteReport(completeReport);
                //hashmap with code smell as key, limit as value sure
            }
        });

        //String classString=Files.readString(Path.of("D:/Code/ifyounoseyounose/src/main/java/org.ifyounoseyounose/Main.java"));
        //Color color = Color.web("#56cbf9");
        //mainApplicationController.setCodeAreaText(classString,color,5);

        primaryStage.show();
        primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent event) {
                Platform.exit();
            }
        });
    }
}
