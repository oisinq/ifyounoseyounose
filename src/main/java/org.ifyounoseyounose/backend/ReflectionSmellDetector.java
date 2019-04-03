package org.ifyounoseyounose.backend;
import java.util.List;

import java.util.List;

public interface ReflectionSmellDetector {
    // This might not be a Reflection class that it's importing, so there may be compatibility issues, so watch out.
    SmellReport detectSmell(List<Class> classes);
}
