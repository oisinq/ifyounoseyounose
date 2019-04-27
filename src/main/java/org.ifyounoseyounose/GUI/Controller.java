package org.ifyounoseyounose.GUI;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import com.google.common.eventbus.Subscribe;

import java.io.File;

import javafx.scene.control.TextArea;

import java.io.IOException;
import java.lang.reflect.Array;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.Function;

import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import org.fxmisc.flowless.VirtualizedScrollPane;
import org.fxmisc.richtext.GenericStyledArea;
import org.fxmisc.richtext.StyledTextArea;
import org.fxmisc.richtext.TextExt;
import org.fxmisc.richtext.model.Codec;
import org.fxmisc.richtext.model.Paragraph;
import org.fxmisc.richtext.model.SegmentOps;
import org.fxmisc.richtext.model.StyledSegment;
import org.fxmisc.richtext.model.TextOps;
import org.ifyounoseyounose.backend.FileReport;
import org.ifyounoseyounose.backend.CompleteReport;
import org.reactfx.SuspendableNo;
import org.reactfx.util.Either;

public class Controller {

    @FXML private TextArea txtView;
    @FXML private TreeView<String> treeView;
    @FXML private Tab code;
    @FXML private MenuItem backToSetup;
    public String InputDirectory = null;//
    private Scene firstScene;
    private CompleteReport completeReport;
    private FileReport fileReport;
    private static Boolean JavaToggle;
    private HashMap<String, Color> colourPicker = new HashMap<>();
    @FXML private ColorPicker ArrowHeadedColour,BloatedClassColour,BloatedMethodColour,BloatedParameterColour,
            DataOnlyColour,DataHidingColour,DeadCodeColour,DuplicateCodeColour,MessageChainingColour,
            PrimitiveObsessionColour,SwitchStatementColour,ToomanyLiteralsColour;
    private HashMap<String,ColorPicker> colorPickers = new HashMap<>();

    // the initialize method is automatically invoked by the FXMLLoader - it's magic
    public void initialize() {
        EventBusFactory.getEventBus().register(new Object() {
            @Subscribe
            public void setInputDirectory(EventBusFactory e) {
                InputDirectory = e.getFileLocation().replace("\\", "/");
                JavaToggle = e.getDisplayJava();
                displayTreeView(InputDirectory);
            }
        });

        initializeColourPicker();
        initializeColorPickers();
        setColourButtons();
        code.setContent(displayCodeTab());

        treeView.getSelectionModel().selectedItemProperty().addListener((v, oldValue, newValue) -> {
            try {
                //area.clear();
                String classString = Files.readString(Path.of(getPathFromTreeView(v.getValue())));
                area.replaceText(classString);
                area.clearStyle(0,area.getLength());
                fileReport = completeReport.getAllDetectedSmells(new File(getPathFromTreeView(v.getValue())));
                setClassColours();
            } catch (IOException e) {
                //e.printStackTrace();
            }
        });
        //backToSetup.setOnAction(this::openFirstScene);//TODO this lets you go back , but doesn't clear everything
    }

    private void setColourButtons(){
        Set<String> s = colorPickers.keySet();
        for (String a: s){
            //colourPicker.get(a);
            colorPickers.get(a).setValue(colourPicker.get(a));
        }
    }

    private void initializeColourPicker() {
        colourPicker.put("ArrowHeaded", Color.rgb(83,255,189));
        colourPicker.put("BloatedClass", Color.rgb(178, 207, 255));
        colourPicker.put("BloatedMethod", Color.rgb(117, 169, 255));
        colourPicker.put("BloatedParameter", Color.rgb(141, 158, 186));
        colourPicker.put("DataOnly", Color.rgb(208, 244, 137));
        colourPicker.put("DataHiding", Color.rgb(242, 190, 87));
        colourPicker.put("DeadCode", Color.rgb(255, 180, 140));
        colourPicker.put("DuplicateCode", Color.rgb(249, 187, 184));
        colourPicker.put("MessageChaining", Color.rgb(185, 158, 193));
        colourPicker.put("PrimitiveObsession", Color.rgb(65, 178, 219));
        //colourPicker.put("SpeculativeGenerality", Color.rgb(83,255,189));
        colourPicker.put("SwitchStatement", Color.rgb(127, 193, 127));
        //colourPicker.put("TemporaryFields", Color.rgb(237, 107, 64));
        colourPicker.put("TooManyLiterals", Color.rgb(167, 229, 87));
        //colourPicker.put("DataHiding", Color.rgb(221, 205, 28));

    }

    private void initializeColorPickers(){
        colorPickers.put("ArrowHeaded",ArrowHeadedColour);
        colorPickers.put("BloatedClass", BloatedClassColour);
        colorPickers.put("BloatedMethod",BloatedMethodColour);
        colorPickers.put("BloatedParameter",BloatedParameterColour);
        colorPickers.put("DataOnly",DataOnlyColour);
        colorPickers.put("DeadCode",DeadCodeColour);
        colorPickers.put("DuplicateCode",DuplicateCodeColour);
        colorPickers.put("MessageChaining",MessageChainingColour);
        colorPickers.put("PrimitiveObsession",PrimitiveObsessionColour);
        //colorPickers.put("SpeculativeGenerality",SpeculativeGeneralityColour),
        colorPickers.put("SwitchStatement",SwitchStatementColour);
        colorPickers.put("TooManyLiterals",ToomanyLiteralsColour);
        colorPickers.put("DataHiding",DataHidingColour);
    }


    //this gets the filepath of a object from its treeview location
    public String getPathFromTreeView(TreeItem<String> v) {
        StringBuilder pathBuilder = new StringBuilder();
        for (TreeItem<String> item = v;
             item != null; item = item.getParent()) {
            pathBuilder.insert(0, item.getValue());
            pathBuilder.insert(0, "/");
        }
        return pathBuilder.toString().substring(1);
    }

    public void setCompleteReport(CompleteReport report) {
        completeReport = report;
    }

    public void setFirstScene(Scene scene) {
        firstScene = scene;
    }

    public void openFirstScene(ActionEvent actionEvent) {
        Stage primaryStage = (Stage) treeView.getScene().getWindow();
        primaryStage.setScene(firstScene);
    }

    public static void createTree(File file, TreeItem<String> parent) {
        if (file.isDirectory()) {
            TreeItem<String> treeItem = new TreeItem<>(file.getName());
            parent.getChildren().add(treeItem);
            for (File f : file.listFiles()) {
                createTree(f, treeItem);
            }
        } else if (!JavaToggle || file.getName().endsWith(".java")) {
            parent.getChildren().add(new TreeItem<>(file.getName()));
        }
    }

    public void displayTreeView(String inputDirectoryLocation) {
        TreeItem<String> rootItem = new TreeItem<>(inputDirectoryLocation);
        File Input = new File(inputDirectoryLocation);
        File fileList[] = Input.listFiles();
        for (File file : fileList) {
            createTree(file, rootItem);
        }
        treeView.setRoot(rootItem);
    }

    private final TextOps<String, TextStyle> styledTextOps = SegmentOps.styledTextOps();
    private final LinkedImageOps<TextStyle> linkedImageOps = new LinkedImageOps<>();
    private final GenericStyledArea<ParStyle, Either<String, LinkedImage>, TextStyle> area =
            new GenericStyledArea<>(
                    ParStyle.EMPTY,                                                 // default paragraph style
                    (paragraph, style) -> paragraph.setStyle(style.toCss()),        // paragraph style setter

                    TextStyle.EMPTY.updateFontSize(12).updateFontFamily("Serif").updateTextColor(Color.BLACK),  // default segment style
                    styledTextOps._or(linkedImageOps, (s1, s2) -> Optional.empty()),                            // segment operations
                    seg -> createNode(seg, (text, style) -> text.setStyle(style.toCss())));                     // Node creator and segment style setter
    {
        area.setWrapText(true);
        area.setStyleCodecs(
                ParStyle.CODEC,
                Codec.styledSegmentCodec(Codec.eitherCodec(Codec.STRING_CODEC, LinkedImage.codec()), TextStyle.CODEC));
    }

    private final SuspendableNo updatingToolbar = new SuspendableNo();


    public Node displayCodeTab() {
        area.setEditable(false);

        VirtualizedScrollPane<GenericStyledArea<ParStyle, Either<String, LinkedImage>, TextStyle>> vsPane = new VirtualizedScrollPane<>(area);
        VBox vbox = new VBox();
        VBox.setVgrow(vsPane, Priority.ALWAYS);
        vbox.getChildren().addAll(vsPane);

        Node node = vbox;
        return node;
    }

    private Node createNode(StyledSegment<Either<String, LinkedImage>, TextStyle> seg,
                            BiConsumer<? super TextExt, TextStyle> applyStyle) {
        return seg.getSegment().unify(
                text -> StyledTextArea.createStyledTextNode(text, seg.getStyle(), applyStyle),
                LinkedImage::createNode
        );
    }

    private void updateParagraphStyleInSelection(ParStyle mixin, int line) {
        setLineStyle(style -> style.updateWith(mixin), line);
    }

    private void updateParagraphBackground(Color color, int line) {
        if (!updatingToolbar.get()) {
            updateParagraphStyleInSelection(ParStyle.backgroundColor(color), line);
        }
    }

    public void setLineColour(Color color, int line) {//TODO Rename as set line smell
        if (line==-1){
            System.out.println("Class smell time!");
            //updateParagraphBackground(color, line);//this is to set line 0
            for (int i = 0; i < area.getText().split("\n").length; i++) {
                updateParagraphBackground(color, i);
            }
        } else {
            updateParagraphBackground(color, line);
        }
    }

    public void resetAllLines() {
        System.out.println("Resetting background to white");
        for (int i = 0; i < area.getText().split("\n").length; i++) {
            updateParagraphBackground(Color.WHITE, i);
        }
    }

    public void setClassColours() {
        HashMap<String, List<Integer>> fileReportHashMap = fileReport.getSmellDetections();
        Set<String> smellDetectors = fileReportHashMap.keySet();

        resetAllLines();
        System.out.println("File: " + fileReport.getFile().getName());

        for (String s : smellDetectors) {
            List<Integer> smellyLines = fileReportHashMap.get(s);
            System.out.println("SmellyLines for " + s + ": " + smellyLines.toString());
            for (int i : smellyLines) {
                setLineColour(colourPicker.get(s), i - 1);
            }
        }
    }


    private void setLineStyle(Function<ParStyle, ParStyle> updater, int line) {
        Paragraph<ParStyle, Either<String, LinkedImage>, TextStyle> paragraph = area.getParagraph(line);
        area.setParagraphStyle(line, updater.apply(paragraph.getParagraphStyle()));
    }
}