package org.ifyounoseyounose.backend;

import org.ifyounoseyounose.backend.smelldetectors.MessageChainingSmellDetector;
import org.ifyounoseyounose.backend.smelldetectors.PlaceholderSmellDetector;
import org.ifyounoseyounose.backend.smelldetectors.SmellDetector;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ReportBuilderTest {

    @Before
    public void setUp() throws Exception {
    }

    @Test
    public void detectSmells() {
        ReportBuilder r = new ReportBuilder();

        File directory = new File("./src/test/java/smellycodedirectory/");

        HashMap<String, Integer> smellDetectorObjects = new HashMap<>();
        smellDetectorObjects.put("MessageChaining", 0);

        r.generateReport(smellDetectorObjects, directory);
        assert(true);
    }
}