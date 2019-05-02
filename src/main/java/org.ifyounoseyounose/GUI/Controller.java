package org.ifyounoseyounose.GUI;

import com.google.common.eventbus.Subscribe;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.PieChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.*;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import org.fxmisc.flowless.VirtualizedScrollPane;
import org.fxmisc.richtext.GenericStyledArea;
import org.fxmisc.richtext.StyledTextArea;
import org.fxmisc.richtext.TextExt;
import org.fxmisc.richtext.model.*;
import org.ifyounoseyounose.backend.CompleteReport;
import org.ifyounoseyounose.backend.FileReport;
import org.reactfx.SuspendableNo;
import org.reactfx.util.Either;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.function.BiConsumer;
import java.util.function.Function;

public class Controller {

    private static Boolean JavaToggle;
    private final TextOps<String, TextStyle> styledTextOps = SegmentOps.styledTextOps();
    private final LinkedImageOps<TextStyle> linkedImageOps = new LinkedImageOps<>();
    private final GenericStyledArea<ParStyle, Either<String, LinkedImage>, TextStyle> area =
            new GenericStyledArea<>(
                    ParStyle.EMPTY,                                                 // default paragraph style
                    (paragraph, style) -> paragraph.setStyle(style.toCss()),        // paragraph style setter

                    TextStyle.EMPTY.updateFontSize(12).updateFontFamily("Serif").updateTextColor(Color.BLACK),  // default segment style
                    styledTextOps._or(linkedImageOps, (s1, s2) -> Optional.empty()),                            // segment operations
                    seg -> createNode(seg, (text, style) -> text.setStyle(style.toCss())));                     // Node creator and segment style setter
    private final SuspendableNo updatingToolbar = new SuspendableNo();
    @FXML
    BarChart projectBarChart, fileBarChart;
    @FXML
    PieChart projectPieChart, filePieChart;
    @FXML
    private TextArea projectStats, fileStats;
    @FXML
    private TreeView<String> treeView;
    @FXML
    private Tab code;
    private String InputDirectory = null;//
    private CompleteReport completeReport;
    private FileReport fileReport;
    private HashMap<String, Color> colorTrackers = new HashMap<>();
    @FXML
    private ColorPicker ArrowHeadedColour, BloatedClassColour, BloatedMethodColour, BloatedParameterColour,
            DataOnlyColour, DataHidingColour, DeadCodeColour, DuplicateCodeColour, MessageChainingColour,
            PrimitiveObsessionColour, SwitchStatementColour, SpeculativeGeneralityColour, TemporaryFieldsColour, TooManyLiteralsColour;
    private HashMap<String, ColorPicker> colorPickers = new HashMap<>();
    @FXML
    private ListView<Map.Entry<String, String>> projectSmellListbyLine, projectSmellListbySmell, fileSmellList;

    {
        area.setWrapText(true);
        area.setStyleCodecs(
                ParStyle.CODEC,
                Codec.styledSegmentCodec(Codec.eitherCodec(Codec.STRING_CODEC, LinkedImage.codec()), TextStyle.CODEC));
    }

    /**
     *This keeps track of what colour is linked to each code smell
     */
    private void setcolorTrackers() {
        ArrowHeadedColour.setOnAction(t -> {
            colorTrackers.replace("ArrowHeaded", ArrowHeadedColour.getValue());
            setClassColours();
        });
        BloatedClassColour.setOnAction(t -> {
            colorTrackers.replace("BloatedClass", BloatedClassColour.getValue());
            setClassColours();
        });
        BloatedMethodColour.setOnAction(t -> {
            colorTrackers.replace("BloatedMethod", BloatedMethodColour.getValue());
            setClassColours();
        });
        BloatedParameterColour.setOnAction(t -> {
            colorTrackers.replace("BloatedParameter", BloatedParameterColour.getValue());
            setClassColours();
        });
        DataOnlyColour.setOnAction(t -> {
            colorTrackers.replace("DataOnly", DataOnlyColour.getValue());
            setClassColours();
        });
        DataHidingColour.setOnAction(t -> {
            colorTrackers.replace("DataHiding", DataHidingColour.getValue());
            setClassColours();
        });
        DeadCodeColour.setOnAction(t -> {
            colorTrackers.replace("DeadCode", DeadCodeColour.getValue());
            setClassColours();
        });
        DuplicateCodeColour.setOnAction(t -> {
            colorTrackers.replace("DuplicateCode", DuplicateCodeColour.getValue());
            setClassColours();
        });
        MessageChainingColour.setOnAction(t -> {
            colorTrackers.replace("MessageChaining", MessageChainingColour.getValue());
            setClassColours();
        });
        PrimitiveObsessionColour.setOnAction(t -> {
            colorTrackers.replace("PrimitiveObsession", PrimitiveObsessionColour.getValue());
            setClassColours();
        });
        SpeculativeGeneralityColour.setOnAction(t -> {
            colorTrackers.replace("SpeculativeGenerality", SpeculativeGeneralityColour.getValue());
            setClassColours();
        });
        SwitchStatementColour.setOnAction(t -> {
            colorTrackers.replace("SwitchStatement", SwitchStatementColour.getValue());
            setClassColours();
        });
        TemporaryFieldsColour.setOnAction(t -> {
            colorTrackers.replace("TemporaryFields", TemporaryFieldsColour.getValue());
            setClassColours();
        });
        TooManyLiteralsColour.setOnAction(t -> {
            colorTrackers.replace("TooManyLiterals", TooManyLiteralsColour.getValue());
            setClassColours();
        });
    }

    /**
     *This is invoked by the FXML loader before anything else is
     */
    public void initialize() {
        //the event bus gets info sent from setup controller
        EventBusFactory.getEventBus().register(new Object() {
            @Subscribe
            public void setInputDirectory(EventBusFactory e) {
                InputDirectory = e.getFileLocation().replace("\\", "/");
                JavaToggle = e.getDisplayJava();
                displayTreeView(InputDirectory);
            }
        });

        final boolean[] projectStatsRan = {false};//this boolean makes sure the project stats tab only runs once
        initializecolorTrackers();//makes sure hashmaps are set up
        initializecolorPickers();
        setColourButtons();//listeners for the colour buttons
        code.setContent(displayCodeTab());
        area.replaceText("Select a java file to check its smells");

        setcolorTrackers();

        treeView.getSelectionModel().selectedItemProperty().addListener((v, oldValue, newValue) -> {
            try {
                String classString = Files.readString(Path.of(getPathFromTreeView(v.getValue())));
                area.replaceText(classString);
                clearStats();//clears stats when files swapped
                fileReport = completeReport.getAllDetectedSmells(new File(getPathFromTreeView(v.getValue())));
                if (!projectStatsRan[0]) {
                    projectStatsBuilder();//only runs once, sets project stats
                    projectStatsRan[0] = true;
                }
                if (fileReport != null) {
                    setClassColours();//update highlighting
                    fileStatsBuilder();//update stats page
                } else {
                    fileStats.setText("All Clear!");//display if no smells detected
                }
            } catch (IOException e) {
                System.err.println("Cannot get path " + getPathFromTreeView(v.getValue()));
            }
        });
    }

    /**
     *This clears some report fields as needed
     */
    private void clearStats() {
        area.clearStyle(0, area.getLength());
        fileBarChart.getData().clear();
        filePieChart.getData().clear();
        fileSmellList.getItems().clear();
    }

    /**
     *This populates the file stats tab
     */
    private void fileStatsBuilder() {
        //this files out the text area at the bottom of the tab
        fileStats.setText("There are " + fileReport.getSmellyLinesCount()
                + " Smelly lines in this file\n\n------File Report--------\n\n" + fileReport.toString());

        //this gets all the smells and their number of occurances
        List<Map.Entry<String, Integer>> listOfSmellsByCount = fileReport.getListOfSmellsByCount();
        ObservableList<PieChart.Data> pieChartData = FXCollections.observableArrayList();//create the pi chart
        XYChart.Series dataSeries = new XYChart.Series();//this data series gets used by the bar chart

        for (Map.Entry<String, Integer> s : listOfSmellsByCount) {
            AbstractMap.SimpleEntry<String, String> entryWithStringValue = new AbstractMap.SimpleEntry<>(s.getKey(), s.getValue().toString());
            if (entryWithStringValue.getKey().equals("BloatedClass") || entryWithStringValue.getKey().equals("DataOnly")) {
                entryWithStringValue.setValue("Full class smell");
            }
            fileSmellList.getItems().add(entryWithStringValue);
            if (!entryWithStringValue.getKey().equals("BloatedClass") && !entryWithStringValue.getKey().equals("DataOnly")) {
                dataSeries.getData().add(new XYChart.Data(s.getKey(), s.getValue()));
                pieChartData.add(new PieChart.Data(s.getKey(), s.getValue()));
            }
        }
        fileBarChart.getData().add(dataSeries);
        fileBarChart.setLegendVisible(false);
        filePieChart.setData(pieChartData);
    }

    /**
     *this populates the project stats tab
     */
    private void projectStatsBuilder() {
        //this files out the text area at the bottom of the tab
        projectStats.setText("There are " + completeReport.getNumberOfSmellyLines()
                + " smelly lines across your project\n\n------Complete Project Report--------\n\n" + completeReport.toString());

        //this gets all the smells and their number of occurances
        List<Map.Entry<String, Integer>> filesByLineCount = completeReport.getListOfFilesByLineCount();
        ObservableList<PieChart.Data> pieChartData = FXCollections.observableArrayList();
        XYChart.Series dataSeries = new XYChart.Series();

        //the counter is used to only show the top 10 smelliest files
        int counter = 0;
        for (Map.Entry<String, Integer> s : filesByLineCount) {
            AbstractMap.SimpleEntry<String, String> entryWithStringValue = new AbstractMap.SimpleEntry<>(s.getKey(), s.getValue().toString());
            projectSmellListbyLine.getItems().add(entryWithStringValue);
            if (counter < 10) {
                counter++;
                dataSeries.getData().add(new XYChart.Data(s.getKey().substring(0, 12), s.getValue()));
            }
        }
        counter = 0;//reset counter for the next run
        for (Map.Entry<String, Integer> s : completeReport.getListOfFilesBySmellCount()) {
            AbstractMap.SimpleEntry<String, String> entryWithStringValue = new AbstractMap.SimpleEntry<>(s.getKey(), s.getValue().toString());
            projectSmellListbySmell.getItems().add(entryWithStringValue);
            if (counter < 10) {
                counter++;
                pieChartData.add(new PieChart.Data(s.getKey(), s.getValue()));
            }
        }
        projectBarChart.getData().add(dataSeries);
        projectBarChart.setLegendVisible(false);
        projectPieChart.setData(pieChartData);
    }

    private void setColourButtons() {
        Set<String> s = colorPickers.keySet();
        for (String a : s) {
            colorPickers.get(a).setValue(colorTrackers.get(a));
        }
    }

    /**
     *sets the initial colours for each button
     */
    private void initializecolorTrackers() {
        colorTrackers.put("ArrowHeaded", Color.rgb(83, 255, 189));
        colorTrackers.put("BloatedClass", Color.rgb(178, 207, 255));
        colorTrackers.put("BloatedMethod", Color.rgb(117, 169, 255));
        colorTrackers.put("BloatedParameter", Color.rgb(141, 158, 186));
        colorTrackers.put("DataOnly", Color.rgb(208, 244, 137));
        colorTrackers.put("DataHiding", Color.rgb(242, 190, 87));
        colorTrackers.put("DeadCode", Color.rgb(255, 180, 140));
        colorTrackers.put("DuplicateCode", Color.rgb(249, 187, 184));
        colorTrackers.put("MessageChaining", Color.rgb(185, 158, 193));
        colorTrackers.put("PrimitiveObsession", Color.rgb(65, 178, 219));
        colorTrackers.put("SpeculativeGenerality", Color.rgb(206, 177, 103));
        colorTrackers.put("SwitchStatement", Color.rgb(127, 193, 127));
        colorTrackers.put("TemporaryFields", Color.rgb(237, 107, 64));
        colorTrackers.put("TooManyLiterals", Color.rgb(167, 229, 87));
    }

    /**
     *links the colour pickers to the names of the identifier strings
     */
    private void initializecolorPickers() {
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
        colorPickers.put("SpeculativeGenerality", SpeculativeGeneralityColour);
        colorPickers.put("SwitchStatement", SwitchStatementColour);
        colorPickers.put("TemporaryFields", TemporaryFieldsColour);
        colorPickers.put("TooManyLiterals", TooManyLiteralsColour);
    }

    /**
     *this gets the filepath of a object from its treeview location
     */
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

    /**
     *this creates the treeview of the file directory in the gui
     */
    private static void createTree(File file, TreeItem<String> parent) {
        if (file.isDirectory()) {
            TreeItem<String> treeItem = new TreeItem<>(file.getName());
            parent.getChildren().add(treeItem);
            File[] fileList = file.listFiles();
            if (fileList != null) {
                Arrays.sort(fileList);
                for (File f : fileList) {
                    createTree(f, treeItem);
                }
            }
        } else if (!JavaToggle || file.getName().endsWith(".java")) {
            //if the user set to only show java files then this else if will do that
            parent.getChildren().add(new TreeItem<>(file.getName()));
        }
    }
    /**
     *this gets a passes the values to create tree view
     */
    private void displayTreeView(String inputDirectoryLocation) {
        TreeItem<String> rootItem = new TreeItem<>(inputDirectoryLocation);
        File Input = new File(inputDirectoryLocation);
        File[] fileList = Input.listFiles();
        if (fileList != null) {
            Arrays.sort(fileList);
            for (File file : fileList) {
                createTree(file, rootItem);
            }
        }
        treeView.setRoot(rootItem);
    }

    private Node displayCodeTab() {
        area.setEditable(false);
        VirtualizedScrollPane<GenericStyledArea<ParStyle, Either<String, LinkedImage>, TextStyle>> vsPane = new VirtualizedScrollPane<>(area);
        VBox vbox = new VBox();
        VBox.setVgrow(vsPane, Priority.ALWAYS);
        vbox.getChildren().addAll(vsPane);
        return vbox;
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

    private void setLineColour(Color color, int line) {
        if (line == -1) {
            for (int i = 0; i < area.getText().split("\n").length; i++) {
                updateParagraphBackground(color, i);
            }
        } else {
            updateParagraphBackground(color, line);
        }
    }
    /**
     *Resets the background colour of all lines to white
     */
    private void resetAllLines() {
        for (int i = 0; i < area.getText().split("\n").length; i++) {
            updateParagraphBackground(Color.WHITE, i);
        }
    }

    /**
     *This gets the lines that smells and calls set smell colours for the respective smells
     */
    private void setClassColours() {
        HashMap<String, List<Integer>> fileReportHashMap = fileReport.getSmellDetections();
        resetAllLines();
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
            for (int i : smellyLines) {
                setLineColour(colorTrackers.get(smellName), i - 1);
            }
        }
    }
    /**
     *This takes a line and a style and sets the line to that style
     */
    private void setLineStyle(Function<ParStyle, ParStyle> updater, int line) {
        Paragraph<ParStyle, Either<String, LinkedImage>, TextStyle> paragraph = area.getParagraph(line);
        area.setParagraphStyle(line, updater.apply(paragraph.getParagraphStyle()));
    }
}