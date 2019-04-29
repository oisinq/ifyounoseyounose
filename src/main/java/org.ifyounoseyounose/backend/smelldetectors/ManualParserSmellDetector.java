package org.ifyounoseyounose.backend.smelldetectors;

import org.ifyounoseyounose.backend.SmellReport;

import java.io.File;
import java.util.List;

/**
 * ManualParserSmellDetector - interface extended by any SmellDetector that uses manual techniques like regular expressions
 * to detect code smells
 */
public interface ManualParserSmellDetector {
    SmellReport detectSmell(List<File> files);
}