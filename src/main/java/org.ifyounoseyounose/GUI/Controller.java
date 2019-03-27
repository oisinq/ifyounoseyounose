package org.ifyounoseyounose.GUI;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.File;


public class Controller {
    // the FXML annotation tells the loader to inject this variable before invoking initialize.
    @FXML private TreeView<String> treeView;
    public String InputDirectory=null;
    private Scene firstScene;

    public void setInputDirectory(String set){
        InputDirectory=set;
    }
    // the initialize method is automatically invoked by the FXMLLoader - it's magic
    public void initialize() {
        //displayTreeView(InputDirectory);
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
}
