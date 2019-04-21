package org.ifyounoseyounose;
import javafx.application.Application;
import org.ifyounoseyounose.GUI.EventBusFactory;
public class Main  {
    public static void main(String[] args) throws Exception  {
        EventBusFactory.getEventBus().register(new Main());//TODO TEST IF I NEED THIS
        System.out.println(System.getProperty("user.dir"));
        Application.launch(GuiManager.class, args);
    }
}