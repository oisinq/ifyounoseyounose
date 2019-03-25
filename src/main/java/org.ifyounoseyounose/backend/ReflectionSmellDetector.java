package org.ifyounoseyounose.backend;

public interface ReflectionSmellDetector {
    // This might not be a Reflection class that it's importing, so there may be compatibility issues, so watch out.
    SmellReport detectSmell(Class sourceCode);
}
