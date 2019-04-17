package org.ifyounoseyounose.backend.smelldetectors;

/*Todo:
/*should this implement SmellDetector? It did originally, but if I added a method to SmellDetector (like
get name, I'll need to implement it here too, which doesn't make sense*/
public class LimitableSmellDetector {
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
