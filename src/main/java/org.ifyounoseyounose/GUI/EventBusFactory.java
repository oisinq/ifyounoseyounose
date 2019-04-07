package org.ifyounoseyounose.GUI;

import com.google.common.eventbus.AsyncEventBus;
import com.google.common.eventbus.EventBus;
//import org.ifyounoseyounose.backend.SmellDetector;

import java.io.File;
import java.util.List;
import java.util.concurrent.Executors;

public class EventBusFactory {
    String location;
    File file;
//    List<SmellDetector> smells;
    private static EventBus eventBus = new AsyncEventBus(Executors.newCachedThreadPool());

    EventBusFactory(String location,File file){
        this.location=location;
        this.file=file;
    }

    public static EventBus getEventBus() {
        return eventBus;
    }

    public String getLocation(){
        return location;
    }

    public File getFile(){
        return file;
    }
//
//    public List<SmellDetector> getSelectedSmells(){
//        return smells;
//    }


}
