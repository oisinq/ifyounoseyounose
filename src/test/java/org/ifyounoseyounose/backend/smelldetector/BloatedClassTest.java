package org.ifyounoseyounose.backend.smelldetector;

import org.ifyounoseyounose.backend.SmellDetectorManager;
import org.ifyounoseyounose.backend.smelldetectors.BloatedClassSmellDetector;

import org.ifyounoseyounose.backend.smelldetectors.SmellDetector;
import org.junit.Test;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class BloatedClassTest {

    @Test
    public void detectSmells() {
        SmellDetectorManager s = new SmellDetectorManager();

        List<File> l = new ArrayList<>();
        l.add(new File("./src/test/java/smellycodedirectory/SmellySwitchStatements.java"));
        l.add(new File("./src/test/java/smellycodedirectory/SmellyBloatedParamCode.java"));


        
        List<SmellDetector> smellDetectors = new ArrayList<>();
        smellDetectors.add(new BloatedClassSmellDetector());

        s.detectSmells(smellDetectors, l);
        assert(true);
    }
}

