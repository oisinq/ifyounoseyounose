module org.ifyounoseyounose {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.compiler;
    requires com.github.javaparser.core;
    requires  guava;
    requires javassist;

    opens org.ifyounoseyounose to javafx.fxml;
    exports org.ifyounoseyounose;
}