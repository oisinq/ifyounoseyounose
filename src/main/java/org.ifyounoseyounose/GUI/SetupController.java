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
import org.ifyounoseyounose.backend.smelldetectors.SpeculativeGeneralityMethodCollector;
import org.ifyounoseyounose.backend.smelldetectors.TemporaryFieldsSmellDetector;

import java.io.File;
import java.util.HashMap;
import java.util.Set;

public class SetupController {

    @FXML private Button browse,settingsDisplay,smellTime;
    @FXML private ScrollPane scrollPane;
    @FXML private CheckBox JavaToggle,ToggleButtons,ArrowHeaded,BloatedClass,BloatedMethod,BloatedParameter,DataOnly,DataHiding,DeadCode,DuplicateCode,MessageChaining,PrimitiveObsession,SpeculativeGenerality,SwitchStatement,TemporaryFields,TooManyLiterals;
    @FXML private Slider ArrowHeadedSlider,BloatedClassSlider,BloatedMethodSlider,BloatedParameterSlider,DuplicateCodeSlider,MessageChainingSlider,PrimitiveObsessionSlider,SwitchStatementSlider,TemporaryFieldsSlider,TooManyLiteralsSlider;
    @FXML private TextField HiddenText,displayDirectory,ArrowHeadedText,BloatedClassText,BloatedMethodText,BloatedParameterText,DeadCodeText,DuplicateCodeText,MessageChainingText,PrimitiveObsessionText,SwitchStatementText,TemporaryFieldsText,TooManyLiteralsText;
    Set<CheckBox> checkboxes;
    @FXML private VBox vbox;
    @FXML private AnchorPane ap;
    HashMap<CheckBox,TextField> connectidkidc;

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
        connectidkidc=new HashMap<>(){{
                put(ArrowHeaded, ArrowHeadedText);
                put(BloatedClass,BloatedClassText);
                put(BloatedMethod,BloatedMethodText);
                put(BloatedParameter,BloatedParameterText);
                put(DataOnly,HiddenText);
                put(DataHiding,HiddenText);
                put(DeadCode,HiddenText);
                put(DuplicateCode,DuplicateCodeText);
                put(MessageChaining,MessageChainingText);
                put(PrimitiveObsession,PrimitiveObsessionText);
                put(SpeculativeGenerality,HiddenText);
                put(SwitchStatement,SwitchStatementText);
                put(TemporaryFields,TemporaryFieldsText);
                put(TooManyLiterals,TooManyLiteralsText);
            }};
        
        checkboxes=connectidkidc.keySet();

        //set all to be true by default
        ToggleButtons.selectedProperty().setValue(true);
        toggleButtons();

        ToggleButtons.selectedProperty().addListener((ChangeListener) (arg0, arg1, arg2) -> {
            toggleButtons();
        });


        /*this has all the button listeners pretty much*/
        setSettingsDisplay();//call once on intialise to set settings buttons as hidden

        ArrowHeadedSlider.valueProperty().addListener((ChangeListener) (arg0, arg1, arg2) -> {
            ArrowHeadedText.setText(String.valueOf(((int) ArrowHeadedSlider.getValue())));
        });
        BloatedClassSlider.valueProperty().addListener((ChangeListener) (arg0, arg1, arg2) -> {
            BloatedClassText.setText(String.valueOf(((int) BloatedClassSlider.getValue())));
        });
        BloatedMethodSlider.valueProperty().addListener((ChangeListener) (arg0, arg1, arg2) -> {
            BloatedMethodText.setText(String.valueOf(((int) BloatedMethodSlider.getValue())));
        });
        BloatedParameterSlider.valueProperty().addListener((ChangeListener) (arg0, arg1, arg2) -> {
            BloatedParameterText.setText(String.valueOf(((int) BloatedParameterSlider.getValue())));
        });
        DuplicateCodeSlider.valueProperty().addListener((ChangeListener) (arg0, arg1, arg2) -> {
            DuplicateCodeText.setText(String.valueOf(((int) DuplicateCodeSlider.getValue())));
        });
        MessageChainingSlider.valueProperty().addListener((ChangeListener) (arg0, arg1, arg2) -> {
            MessageChainingText.setText(String.valueOf(((int) MessageChainingSlider.getValue())));
        });
        SwitchStatementSlider.valueProperty().addListener((ChangeListener) (arg0, arg1, arg2) -> {
            SwitchStatementText.setText(String.valueOf(((int) SwitchStatementSlider.getValue())));
        });
        TooManyLiteralsSlider.valueProperty().addListener((ChangeListener) (arg0, arg1, arg2) -> {
            TooManyLiteralsText.setText(String.valueOf(((int) TooManyLiteralsSlider.getValue())));
        });
        PrimitiveObsessionSlider.valueProperty().addListener((ChangeListener) (arg0, arg1, arg2) -> {
            PrimitiveObsessionText.setText(String.valueOf(((int)PrimitiveObsessionSlider.getValue())));
        });
        TemporaryFieldsSlider.valueProperty().addListener((ChangeListener) (arg0, arg1, arg2) -> {
            TemporaryFieldsText.setText(String.valueOf(((int) TemporaryFieldsSlider.getValue())));
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
                HashMap<String,Integer> smells=getSmellsToTest();
                if(selectedDirectoryString!=null&&smells!=null) {
                    eventBus.post(new EventBusFactory(smells,displayDirectory.getText(), selectedDirectory,JavaToggle.selectedProperty().get()));
                    Stage stage = (Stage) ((Node) event.getTarget()).getScene().getWindow();
                    stage.setScene(secondScene);
                }
            }
        });
    }

    public HashMap<String,Integer> getSmellsToTest(){
        HashMap<String,Integer> toReturn=new HashMap<>();
        for(CheckBox a: checkboxes){
            if(a.isSelected()){
                toReturn.put(a.getId(),Integer.parseInt(connectidkidc.get(a).getText()));
                System.out.println(a.getId());
            }
        }
        return toReturn;
    }

    public void toggleButtons(){
        for(CheckBox a: checkboxes){
            a.setSelected(ToggleButtons.isSelected());
        }
        DataHiding.selectedProperty().setValue(false);//we always want this to be false
    }
}