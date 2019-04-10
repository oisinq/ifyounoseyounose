package org.ifyounoseyounose.backend.smelldetector;

import org.ifyounoseyounose.backend.SmellDetectorManager;
import org.ifyounoseyounose.backend.smelldetectors.MessageChainingSmellDetector;
import org.ifyounoseyounose.backend.smelldetectors.SmellDetector;
import org.ifyounoseyounose.backend.smelldetectors.SpeculativeGeneralitySmellDetector;
import org.junit.Test;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class SpeculativeGeneralityTest {
    @Test
    public void detectSmells() {
        SmellDetectorManager s = new SmellDetectorManager();

        List<File> l = new ArrayList<>();
        l.add(new File("./src/test/java/smellycodedirectory/Yeet.java"));
        l.add(new File("./src/test/java/smellycodedirectory/Yeet2.java"));
        l.add(new File("./src/test/java/smellycodedirectory/SpeculativeGenerality.java"));

        List<SmellDetector> smellDetectors = new ArrayList<>();
        smellDetectors.add(new SpeculativeGeneralitySmellDetector());

        s.detectSmells(smellDetectors, l);
        assert(true);
    }
}
