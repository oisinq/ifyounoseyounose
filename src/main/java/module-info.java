/// This file is a requirement for Java 11 to import exactly which parts of our libraries that we want to use
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
