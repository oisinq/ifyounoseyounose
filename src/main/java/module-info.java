module org.ifyounoseyounose {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.compiler;
    requires javafx.web;
    requires richtextfx;
    requires reactfx;
    requires flowless;
    requires com.github.javaparser.core;
    requires com.github.javaparser.symbolsolver.core;
    requires guava;
    opens org.ifyounoseyounose to javafx.fxml;
    exports org.ifyounoseyounose;
}
