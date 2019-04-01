package org.ifyounoseyounose.GUI;

import com.google.common.eventbus.Subscribe;
import com.google.common.eventbus.AsyncEventBus;
import com.google.common.eventbus.EventBus;
import javafx.event.Event;

import java.util.concurrent.Executors;

public class EventBusFactory {
    String location;
    private static EventBus eventBus = new AsyncEventBus(Executors.newCachedThreadPool());

    EventBusFactory(String location){
        this.location=location;
    }

    public static EventBus getEventBus() {
        return eventBus;
    }

    public String getResult(){
        return location;
    }


}
