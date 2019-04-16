package org.ifyounoseyounose.backend.smelldetectors;

public class LimitableSmellDetector implements SmellDetector {
    protected int limit;

    LimitableSmellDetector(int limit) {
        setLimit(limit);
    }

    LimitableSmellDetector() {
        limit = 0;
    }

    public void setLimit(int limit) {
        this.limit = limit;
    }
}
