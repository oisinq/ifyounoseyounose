package org.ifyounoseyounose.backend.smelldetector;

import org.ifyounoseyounose.backend.SmellDetectorManager;
import org.ifyounoseyounose.backend.smelldetectors.DuplicateCodeSmellDetector;
import org.ifyounoseyounose.backend.smelldetectors.SmellDetector;
import org.ifyounoseyounose.backend.smelldetectors.TooManyLiteralsSmellDetector;
import org.junit.Test;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class DuplicateCodeTest {

    @Test
    public void detectSmells() {
        SmellDetectorManager s = new SmellDetectorManager();

        List<File> l = new ArrayList<>();
        l.add(new File("./src/test/java/smellycodedirectory/DuplicatedCode.java"));

        List<SmellDetector> smellDetectors = new ArrayList<>();
        smellDetectors.add(new DuplicateCodeSmellDetector());

        s.detectSmells(smellDetectors, l);
        assert(true);
    }
}