package org.ifyounoseyounose;

import javafx.application.Application;

public class Main {
    public static void main(String[] args) {
        //EventBusFactory.getEventBus().register(new Main());//TODO TEST IF I NEED THIS
        System.out.println(System.getProperty("user.dir"));
        Application.launch(GuiManager.class, args);
    }
}