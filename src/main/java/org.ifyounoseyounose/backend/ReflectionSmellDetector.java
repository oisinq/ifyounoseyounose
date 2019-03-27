package org.ifyounoseyounose.backend;

import java.io.File;

public interface ReflectionSmellDetector {
    // This might not be a Reflection class that it's importing, so there may be compatibility issues, so watch out.
    SmellReport detectSmell(Class sourceCode);

    SmellReport detectSmell(File sourceCode);// In case its not a Reflection class
}
