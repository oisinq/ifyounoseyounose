package org.ifyounoseyounose.backend.smelldetectors;
import org.ifyounoseyounose.backend.SmellReport;

import java.util.List;

/**
 * ReflectionSmellDetector - interface extended by any SmellDetector that uses the Reflection API to detect smelsls
 */
public interface ReflectionSmellDetector {
    SmellReport detectSmell(List<Class> classes);
}
