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
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;


public class SetupController {

    @FXML private Button browse;
    @FXML private Button settingsDisplay;
    @FXML private Button smellTime;
    @FXML private CheckBox DuplicateCode,MessageChaining,PrimitiveObsession,SwitchStatement,TooManyLiterals;
    List<CheckBox> checkboxes = new ArrayList<CheckBox>();

    @FXML private TextField displayDirectory;
    @FXML private VBox vbox;
    @FXML private AnchorPane ap;

    File selectedDirectory=null;
    private Scene secondScene;
    String selectedDirectoryString=null;
    EventBus eventBus = EventBusFactory.getEventBus();//gotta explain this!!!!

    private boolean showSettings=false;


    public void setSettingsDisplay(){//TODO::once smells are set this would be better with an array
        DuplicateCode.setVisible(showSettings);
        MessageChaining.setVisible(showSettings);
        PrimitiveObsession.setVisible(showSettings);
        SwitchStatement.setVisible(showSettings);
        TooManyLiterals.setVisible(showSettings);
    }

    public void setSecondScene(Scene scene){
        secondScene = scene;
    }

    public void initialize() {
        //set all to be true by default
        DuplicateCode.setSelected(true);
        MessageChaining.setSelected(true);
        PrimitiveObsession.setSelected(true);
        SwitchStatement.setSelected(true);
        TooManyLiterals.setSelected(true);

        //add them to the list for checking
        checkboxes.add(DuplicateCode);
        checkboxes.add(MessageChaining);
        checkboxes.add(PrimitiveObsession);
        checkboxes.add(SwitchStatement);
        checkboxes.add(TooManyLiterals);
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
                List smells=getSmellsToTest();
                if(selectedDirectoryString!=null&&smells!=null) {
                    eventBus.post(new EventBusFactory(selectedDirectoryString, selectedDirectory));
                    Stage stage = (Stage) ((Node) event.getTarget()).getScene().getWindow();
                    stage.setScene(secondScene);
                }
            }
        });
    }


    public List getSmellsToTest(){
        List toReturn=new ArrayList();
        for(CheckBox a: checkboxes){
            if(a.isSelected()){
                System.out.println(a.getId());
                toReturn.add(a.getId());
            }
        }

        return toReturn;
    }
}