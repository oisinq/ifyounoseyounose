package org.ifyounoseyounose.backend;

import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.util.HashMap;

public class ReportBuilderTest {

    @Before
    public void setUp() throws Exception {
    }

    @Test
    public void detectSmells() {
        ReportBuilder r = new ReportBuilder();

        File directory = new File("./src/test/java/smellycodedirectory/");

        HashMap<String, Integer> smellDetectorObjects = new HashMap<>();
        smellDetectorObjects.put("ArrowHeaded", 2);

        CompleteReport re = r.generateReport(smellDetectorObjects, directory);
        System.err.println(re);
        assert(true);
    }
}