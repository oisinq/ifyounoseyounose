package org.ifyounoseyounose.GUI;

import com.google.common.eventbus.AsyncEventBus;
import com.google.common.eventbus.EventBus;
import java.io.File;
import java.util.HashMap;
import java.util.concurrent.Executors;

public class EventBusFactory {
    String fileLocation;
    File file;
    HashMap<String,Integer> smells;

    private static EventBus eventBus = new AsyncEventBus(Executors.newCachedThreadPool());

    EventBusFactory(HashMap<String,Integer> smell,String fileLocation,File file){
        this.smells=smell;
        this.fileLocation=fileLocation;
        this.file=file;
    }

    public static EventBus getEventBus() {
        return eventBus;
    }

    public String getFileLocation(){
        return fileLocation;
    }

    public File getFile(){
        return file;
    }

    public HashMap<String, Integer> getSmells() {
        return smells;
    }
}
