package org.ifyounoseyounose.GUI;

import com.google.common.eventbus.EventBus;
import javafx.beans.value.ChangeListener;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class SetupController {

    @FXML private Button browse,settingsDisplay,smellTime;
    @FXML private ScrollPane scrollPane;
    @FXML private CheckBox ArrowHeaded,BloatedClass,BloatedMethod,BloatedParameter,DataOnly,DataHiding,DeadCode,DuplicateCode,MessageChaining,PrimitiveObsession,SwitchStatement,TooManyLiterals;
    @FXML private Slider ArrowHeadedSlider,BloatedClassSlider;
    @FXML private TextField displayDirectory,ArrowHeadedText;
    List<CheckBox> checkboxes;
    @FXML private VBox vbox;
    @FXML private AnchorPane ap;

    File selectedDirectory=null;
    private Scene secondScene;
    String selectedDirectoryString=null;
    EventBus eventBus = EventBusFactory.getEventBus();//gotta explain this!!!!

    private boolean showSettings=false;

    public void setSettingsDisplay(){//TODO::once smells are set this would be better with an array
        scrollPane.setVisible(showSettings);
    }

    public void setSecondScene(Scene scene){
        secondScene = scene;
    }

    public void initialize() {
        checkboxes=new ArrayList<CheckBox>(){
            {
                add(ArrowHeaded); add(BloatedClass); add(BloatedMethod); add(BloatedParameter); add(DataOnly); add(DataHiding); add(DeadCode); add(DuplicateCode); add(MessageChaining); add(PrimitiveObsession); add(SwitchStatement); add(TooManyLiterals);
            }
        };
        //set all to be true by default
        for(CheckBox a: checkboxes){
            a.setSelected(true);
        }
        /*this has all the button listeners pretty much*/
        setSettingsDisplay();//call once on intialise to set settings buttons as hidden

        ArrowHeadedSlider.valueProperty().addListener((ChangeListener) (arg0, arg1, arg2) -> {
            ArrowHeadedText.setText(String.valueOf(((int) ArrowHeadedSlider.getValue())));
        });

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
                //List smells=getSmellsToTest();//TODO come back and fix
                HashMap<String,Integer> smells=new HashMap<>();
                smells.put(ArrowHeaded.getId(),(int)ArrowHeadedSlider.getValue());
                if(selectedDirectoryString!=null&&smells!=null) {

                    //smells.add
                    eventBus.post(new EventBusFactory(smells,displayDirectory.getText(), selectedDirectory));
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