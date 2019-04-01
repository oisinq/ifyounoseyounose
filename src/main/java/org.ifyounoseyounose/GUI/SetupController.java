package org.ifyounoseyounose.GUI;

import com.google.common.eventbus.EventBus;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;


public class SetupController {

    @FXML private Button browse;
    @FXML private Button settingsDisplay;
    @FXML private Button smellTime;
    @FXML private CheckBox smell1,smell2,smell3,smell4;//TODO::rename with actual smell
    @FXML private TextField displayDirectory;
    //Stage stage = (Stage) browse.getScene().getWindow();
    File selectedDirectory=null;
    private Scene secondScene;
    String selectedDirectoryString=null;
    EventBus eventBus = EventBusFactory.getEventBus();//gotta explain this!!!!

    private boolean showSettings=false;


    public void setSettingsDisplay(){//TODO::once smells are set this would be better with an array
        smell1.setVisible(showSettings);
        smell2.setVisible(showSettings);
        smell3.setVisible(showSettings);
        smell4.setVisible(showSettings);

    }

    public void setSecondScene(Scene scene){
        secondScene = scene;
    }

    public void initialize() {

        /*this has all the button listeners pretty much*/
        setSettingsDisplay();//call once on intialise to set settings buttons as hidden
        settingsDisplay.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                showSettings=!showSettings;
                setSettingsDisplay();
            }
        });

        browse.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                DirectoryChooser chooser = new DirectoryChooser();

                chooser.setTitle("Choose the Directory of files you wish to smell");
                selectedDirectory = chooser.showDialog(new Stage());
                selectedDirectoryString=selectedDirectory.getAbsolutePath();

                displayDirectory.setText(selectedDirectoryString);
            }
        });

        smellTime.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                eventBus.post(new EventBusFactory(selectedDirectoryString));
                Stage stage=(Stage) ((Node)event.getTarget()).getScene().getWindow();
                stage.setScene(secondScene);
            }
        });



    }

}