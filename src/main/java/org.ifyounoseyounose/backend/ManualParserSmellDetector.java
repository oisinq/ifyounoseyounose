package org.ifyounoseyounose.backend;

import java.io.File;

public interface ManualParserSmellDetector {
    SmellReport detectSmell(File sourceCode);
}