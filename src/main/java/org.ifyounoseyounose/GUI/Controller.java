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
import java.util.List;
import java.util.Optional;
import java.util.function.BiConsumer;
import java.util.function.Function;
import javafx.scene.control.IndexRange;
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
import org.fxmisc.richtext.model.StyleSpans;
import org.fxmisc.richtext.model.StyledSegment;
import org.fxmisc.richtext.model.TextOps;
import org.reactfx.SuspendableNo;
import org.reactfx.util.Either;

import static org.fxmisc.richtext.model.TwoDimensional.Bias.Backward;
import static org.fxmisc.richtext.model.TwoDimensional.Bias.Forward;


public class Controller {

    @FXML private TextArea txtView ;
    @FXML private TreeView<String> treeView;
    @FXML private Tab code;
    public String InputDirectory=null;//
    private Scene firstScene;

    // the initialize method is automatically invoked by the FXMLLoader - it's magic
    public void initialize() {
        EventBusFactory.getEventBus().register(new Controller());//TODO TEST IF I NEED THIS
        EventBusFactory.getEventBus().register(new Object() {
            @Subscribe
            public void setInputDirectory(EventBusFactory e){
                String temp= e.getLocation().replace("\\", "/");
                InputDirectory=temp;
                displayTreeView(temp);
            }
        });
        code.setContent(displayCodeTab());
        //setCodeAreaText("Click on a file you wish to see the text for");
    }

    public void setFirstScene(Scene scene) {
        firstScene = scene;
    }

    public void openFirstScene(ActionEvent actionEvent) {
        Stage primaryStage = (Stage)((Node)actionEvent.getSource()).getScene().getWindow();
        primaryStage.setScene(firstScene);
    }

    public static void createTree(File file, TreeItem<String> parent) {
        if (file.isDirectory()) {
            TreeItem<String> treeItem = new TreeItem<>(file.getName());
            parent.getChildren().add(treeItem);
            for (File f : file.listFiles()) {
                createTree(f, treeItem);
            }
        } else {
            parent.getChildren().add(new TreeItem<>(file.getName()));
        }
    }

    public void displayTreeView(String inputDirectoryLocation) {
        TreeItem<String> rootItem = new TreeItem<>(inputDirectoryLocation);


        File Input = new File(inputDirectoryLocation);
        File fileList[] = Input.listFiles();

        // create tree
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


    public Node displayCodeTab(){

            area.setEditable(false);
            area.beingUpdatedProperty().addListener((o, old, beingUpdated) -> {
                if(!beingUpdated) {
                    Color backgroundColor;

                    IndexRange selection = area.getSelection();
                    if(selection.getLength() != 0) {
                        StyleSpans<TextStyle> styles = area.getStyleSpans(selection);
                        Color[] backgrounds = styles.styleStream().map(s -> s.backgroundColor.orElse(null)).distinct().toArray(i -> new Color[i]);
                        backgroundColor = backgrounds.length == 1 ? backgrounds[0] : null;
                    } else {
                        int p = area.getCurrentParagraph();
                        int col = area.getCaretColumn();
                        TextStyle style = area.getStyleAtPosition(p, col);
                        backgroundColor = style.backgroundColor.orElse(null);
                    }

                    int startPar = area.offsetToPosition(selection.getStart(), Forward).getMajor();
                    int endPar = area.offsetToPosition(selection.getEnd(), Backward).getMajor();
                    List<Paragraph<ParStyle, Either<String, LinkedImage>, TextStyle>> pars = area.getParagraphs().subList(startPar, endPar + 1);

                    @SuppressWarnings("unchecked")
                    Optional<Color>[] paragraphBackgrounds = pars.stream().map(p -> p.getParagraphStyle().backgroundColor).distinct().toArray(Optional[]::new);
                    Optional<Color> paragraphBackground = paragraphBackgrounds.length == 1 ? paragraphBackgrounds[0] : Optional.empty();

                }
            });

            VirtualizedScrollPane<GenericStyledArea<ParStyle, Either<String, LinkedImage>, TextStyle>> vsPane = new VirtualizedScrollPane<>(area);
            VBox vbox = new VBox();
            VBox.setVgrow(vsPane, Priority.ALWAYS);
            vbox.getChildren().addAll(vsPane);

            Node node=vbox;
            return node;
    }

        private Node createNode(StyledSegment<Either<String, LinkedImage>, TextStyle> seg,
                                BiConsumer<? super TextExt, TextStyle> applyStyle) {
            return seg.getSegment().unify(
                    text -> StyledTextArea.createStyledTextNode(text, seg.getStyle(), applyStyle),
                    LinkedImage::createNode
            );
        }

        private void updateParagraphStyleInSelection(ParStyle mixin,int a,int b) {
            c(style -> style.updateWith(mixin),a,b);
        }


        private void updateParagraphBackground(Color color,int a,int b) {
            if(!updatingToolbar.get()) {
                updateParagraphStyleInSelection(ParStyle.backgroundColor(color),a, b);
            }
        }

        public void setCodeAreaText(String a,Color b){
        area.replaceText(a);
        updateParagraphBackground(Color.web("#56cbf9",0.8),0,4);
        updateParagraphBackground(Color.web("#56cbf9",0.2),5,8);
        }

    private void c(Function<ParStyle, ParStyle> updater,int start,int end) {
        int startPar = start;
        int endPar = end;
        System.err.println(startPar + " - " + endPar);
        System.out.println("hi");
        for(int i = startPar; i <= endPar; ++i) {
            Paragraph<ParStyle, Either<String, LinkedImage>, TextStyle> paragraph = area.getParagraph(i);
            area.setParagraphStyle(i, updater.apply(paragraph.getParagraphStyle()));
        }
    }
}