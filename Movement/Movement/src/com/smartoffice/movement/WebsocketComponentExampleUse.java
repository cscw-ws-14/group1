package com.smartoffice.movement;

import com.jpmorrsn.fbp.engine.*;

public class WebsocketComponentExampleUse extends Network{
    protected void define() {
        component("WS", WebsocketComponent.class);

        initialize("test bro", component("WS"), port("MESSAGE"));
    }

    public static void main(String[] argv) throws Exception  {
        new WebsocketComponentExampleUse().go();
    }
}