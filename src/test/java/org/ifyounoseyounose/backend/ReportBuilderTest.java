package org.ifyounoseyounose.backend;

import org.ifyounoseyounose.backend.smelldetectors.PlaceholderSmellDetector;
import org.ifyounoseyounose.backend.smelldetectors.SmellDetector;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class ReportBuilderTest {

    @Before
    public void setUp() throws Exception {
    }

    @Test
    public void detectSmells() {
        ReportBuilder r = new ReportBuilder();

        File directory = new File("./src/test/java/smellycodedirectory/");

        List<SmellDetector> smellDetectors = new ArrayList<>();
        smellDetectors.add(new PlaceholderSmellDetector());

        r.generateReport(smellDetectors, directory);
        assert(true);
    }
}