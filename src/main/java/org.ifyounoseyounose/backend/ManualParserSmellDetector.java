package org.ifyounoseyounose.backend;
import java.io.File;
import java.util.List;

public interface ManualParserSmellDetector {
    SmellReport detectSmell(List<File> files);
}