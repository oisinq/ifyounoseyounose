package org.ifyounoseyounose.backend.smelldetectors;
import org.ifyounoseyounose.backend.SmellReport;

import java.io.File;
import java.util.HashMap;
/**
 * ReflectionSmellDetector - interface extended by any SmellDetector that uses the Reflection API to detect smelsls
 */
public interface ReflectionSmellDetector {
    SmellReport detectSmell(HashMap<Class, File> classes);
}
