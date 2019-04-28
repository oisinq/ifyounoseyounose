package org.ifyounoseyounose.GUI;

import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.chart.BarChart;
import javafx.scene.control.*;
import javafx.stage.Stage;
import com.google.common.eventbus.Subscribe;

import java.io.File;

import javafx.scene.control.TextArea;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
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

    @FXML
    private TextArea txtView,fileStats;
    @FXML
    private TreeView<String> treeView;
    @FXML
    private Tab code;
    @FXML
    private MenuItem backToSetup;
    public String InputDirectory = null;//
    private Scene firstScene;
    private CompleteReport completeReport;
    private FileReport fileReport;
    private static Boolean JavaToggle;
    private HashMap<String, Color> colourTracker = new HashMap<>();
    @FXML
    private ColorPicker ArrowHeadedColour, BloatedClassColour, BloatedMethodColour, BloatedParameterColour,
            DataOnlyColour, DataHidingColour, DeadCodeColour, DuplicateCodeColour, MessageChainingColour,
            PrimitiveObsessionColour, SwitchStatementColour,SpeculativeGeneralityColour,TemporaryFieldsColour, TooManyLiteralsColour;
    private HashMap<String, ColorPicker> colorPickers = new HashMap<>();
    @FXML private ListView<Map.Entry<String,Integer>> SmellList;
    @FXML BarChart fileBarChart;

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

        initializecolourTracker();
        initializeColorPickers();
        setColourButtons();
        code.setContent(displayCodeTab());
        area.replaceText("Select a java file to check its smells");

        ArrowHeadedColour.setOnAction(t -> {
            colourTracker.replace("ArrowHeaded", ArrowHeadedColour.getValue());
            setClassColours();
        });
        BloatedClassColour.setOnAction(t -> {
            colourTracker.replace("BloatedClass", BloatedClassColour.getValue());
            setClassColours();
        });
        BloatedMethodColour.setOnAction(t -> {
            colourTracker.replace("BloatedMethod", BloatedMethodColour.getValue());
            setClassColours();
        });
        BloatedParameterColour.setOnAction(t -> {
            colourTracker.replace("BloatedParameter", BloatedParameterColour.getValue());
            setClassColours();
        });
        DataOnlyColour.setOnAction(t -> {
            colourTracker.replace("DataOnly", DataOnlyColour.getValue());
            setClassColours();
        });
        DataHidingColour.setOnAction(t -> {
            colourTracker.replace("DataHiding", DataHidingColour.getValue());
            setClassColours();
        });
        DeadCodeColour.setOnAction(t -> {
            colourTracker.replace("DeadCode", DeadCodeColour.getValue());
            setClassColours();
        });
        DuplicateCodeColour.setOnAction(t -> {
            colourTracker.replace("DuplicateCode", DuplicateCodeColour.getValue());
            setClassColours();
        });
        MessageChainingColour.setOnAction(t -> {
            colourTracker.replace("MessageChaining", MessageChainingColour.getValue());
            setClassColours();
        });
        PrimitiveObsessionColour.setOnAction(t -> {
            colourTracker.replace("PrimitiveObsession", PrimitiveObsessionColour.getValue());
            setClassColours();
        });
        SpeculativeGeneralityColour.setOnAction(t -> {
            colourTracker.replace("SpeculativeGenerality",  SpeculativeGeneralityColour.getValue());
            setClassColours();
        });
        SwitchStatementColour.setOnAction(t -> {
            colourTracker.replace("SwitchStatement", SwitchStatementColour.getValue());
            setClassColours();
        });
        TemporaryFieldsColour.setOnAction(t -> {
            colourTracker.replace("TemporaryFields", TemporaryFieldsColour.getValue());
            setClassColours();
        });
        TooManyLiteralsColour.setOnAction(t -> {
            colourTracker.replace("TooManyLiterals", TooManyLiteralsColour.getValue());
            setClassColours();
        });

        treeView.getSelectionModel().selectedItemProperty().addListener((v, oldValue, newValue) -> {
            try {
                String classString = Files.readString(Path.of(getPathFromTreeView(v.getValue())));
                area.replaceText(classString);
                area.clearStyle(0, area.getLength());
                SmellList.getItems().clear();
                fileReport = completeReport.getAllDetectedSmells(new File(getPathFromTreeView(v.getValue())));
                if (fileReport!=null){
                    setClassColours();
                    fileReportBuilder();
                }else{
                    fileStats.setText("All Clear!");
                }
            } catch (IOException e) {
                //e.printStackTrace();
            }
        });
        //backToSetup.setOnAction(this::openFirstScene);//TODO this lets you go back , but doesn't clear everything
    }

    private void fileReportBuilder(){
        int a=fileReport.getSmellyLinesCount();
        fileStats.setText("There are " +a+ " Smelly lines in this file");
        //fileReport.geta()
        List<Map.Entry<String, Integer>> b=fileReport.getListOfSmellsByCount();
        SmellList.getItems().addAll();
        for (Map.Entry<String,Integer> s: b) {
            SmellList.getItems().add(s);
        }
    }

    private void setColourButtons() {
        Set<String> s = colorPickers.keySet();
        for (String a : s) {

            colorPickers.get(a).setValue(colourTracker.get(a));
        }
    }

    private void initializecolourTracker() {
        colourTracker.put("ArrowHeaded", Color.rgb(83, 255, 189));
        colourTracker.put("BloatedClass", Color.rgb(178, 207, 255));
        colourTracker.put("BloatedMethod", Color.rgb(117, 169, 255));
        colourTracker.put("BloatedParameter", Color.rgb(141, 158, 186));
        colourTracker.put("DataOnly", Color.rgb(208, 244, 137));
        colourTracker.put("DataHiding", Color.rgb(242, 190, 87));
        colourTracker.put("DeadCode", Color.rgb(255, 180, 140));
        colourTracker.put("DuplicateCode", Color.rgb(249, 187, 184));
        colourTracker.put("MessageChaining", Color.rgb(185, 158, 193));
        colourTracker.put("PrimitiveObsession", Color.rgb(65, 178, 219));
        colourTracker.put("SpeculativeGenerality", Color.rgb(83,255,189));
        colourTracker.put("SwitchStatement", Color.rgb(127, 193, 127));
        colourTracker.put("TemporaryFields", Color.rgb(237, 107, 64));
        colourTracker.put("TooManyLiterals", Color.rgb(167, 229, 87));
    }

    private void initializeColorPickers() {
        colorPickers.put("ArrowHeaded", ArrowHeadedColour);
        colorPickers.put("BloatedClass", BloatedClassColour);
        colorPickers.put("BloatedMethod", BloatedMethodColour);
        colorPickers.put("BloatedParameter", BloatedParameterColour);
        colorPickers.put("DataHiding", DataHidingColour);
        colorPickers.put("DataOnly", DataOnlyColour);
        colorPickers.put("DeadCode", DeadCodeColour);
        colorPickers.put("DuplicateCode", DuplicateCodeColour);
        colorPickers.put("MessageChaining", MessageChainingColour);
        colorPickers.put("PrimitiveObsession", PrimitiveObsessionColour);
        colorPickers.put("SpeculativeGenerality",SpeculativeGeneralityColour);
        colorPickers.put("SwitchStatement", SwitchStatementColour);
        colorPickers.put("TemporaryFields", TemporaryFieldsColour);
        colorPickers.put("TooManyLiterals", TooManyLiteralsColour);
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
            File fileList[] = file.listFiles();
            Arrays.sort(fileList);
            for (File f : fileList) {
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
        Arrays.sort(fileList);
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
        resetAllLines();
        System.out.println("File: " + fileReport.getFile().getName());

        setSmellColours("BloatedClass", fileReportHashMap);
        setSmellColours("DataOnly", fileReportHashMap);
        setSmellColours("BloatedMethod", fileReportHashMap);
        setSmellColours("SpeculativeGenerality", fileReportHashMap);
        setSmellColours("DeadCode", fileReportHashMap);
        setSmellColours("ArrowHeaded", fileReportHashMap);
        setSmellColours("SwitchStatement", fileReportHashMap);
        setSmellColours("DuplicateCode", fileReportHashMap);
        setSmellColours("TemporaryFields", fileReportHashMap);
        setSmellColours("TooManyLiterals", fileReportHashMap);
        setSmellColours("MessageChaining", fileReportHashMap);
        setSmellColours("DataHiding", fileReportHashMap);
        setSmellColours("BloatedParameter", fileReportHashMap);
        setSmellColours("PrimitiveObsession", fileReportHashMap);
    }

    private void setSmellColours(String smellName, HashMap<String, List<Integer>> fileReportHashMap) {
        if (fileReportHashMap.containsKey(smellName)) {
            List<Integer> smellyLines = fileReportHashMap.get(smellName);
            System.out.println("SmellyLines for " + smellName + ": " + smellyLines.toString());
            for (int i : smellyLines) {
                setLineColour(colourTracker.get(smellName), i - 1);
            }
        }
    }

    private void setLineStyle(Function<ParStyle, ParStyle> updater, int line) {
        Paragraph<ParStyle, Either<String, LinkedImage>, TextStyle> paragraph = area.getParagraph(line);
        area.setParagraphStyle(line, updater.apply(paragraph.getParagraphStyle()));
    }
}