package org.ifyounoseyounose.backend;

import org.ifyounoseyounose.backend.smelldetectors.SmellDetector;
import org.ifyounoseyounose.backend.smelldetectors.ViolationOfDataHidingSmellDetector;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class SmellDetectorManagerTest {

    @Before
    public void setUp() throws Exception {
    }

    @Test
    public void detectSmells() {
        SmellDetectorManager s = new SmellDetectorManager();

        List<File> l = new ArrayList<>();
        l.add(new File("./src/test/java/smellycodedirectory/Yeet.java"));
        l.add(new File("./src/test/java/smellycodedirectory/Yeet2.java"));
        l.add(new File("./src/test/java/smellycodedirectory/seq/Yeet3.java"));
        l.add(new File("./src/test/java/smellycodedirectory/SmellyLiterals.java"));

        List<SmellDetector> smellDetectors = new ArrayList<>();
        smellDetectors.add(new ViolationOfDataHidingSmellDetector());

        s.detectSmells(smellDetectors, l);
        assert(true);
    }
}