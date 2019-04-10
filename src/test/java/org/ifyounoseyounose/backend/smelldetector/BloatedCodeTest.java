package org.ifyounoseyounose.backend.smelldetector;

import org.ifyounoseyounose.backend.SmellDetectorManager;
import org.ifyounoseyounose.backend.smelldetectors.BloatedCodeSmellDetector;
import org.ifyounoseyounose.backend.smelldetectors.SmellDetector;
import org.junit.Test;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class BloatedCodeTest {
    @Test
    public void detectSmells() {
        SmellDetectorManager s = new SmellDetectorManager();

        List<File> l = new ArrayList<>();
        l.add(new File("./src/test/java/smellycodedirectory/SmellySwitchStatements.java"));
        l.add(new File("./src/test/java/smellycodedirectory/SmellyBloatedCode.java"));


        List<SmellDetector> smellDetectors = new ArrayList<>();
        smellDetectors.add(new BloatedCodeSmellDetector());

        s.detectSmells(smellDetectors, l);
        assert(true);
    }
}
