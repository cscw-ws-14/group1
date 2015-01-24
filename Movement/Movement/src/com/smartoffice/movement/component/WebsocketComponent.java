package com.smartoffice.movement.component;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;

import com.jpmorrsn.fbp.engine.*;
import com.smartoffice.movement.library.WebsocketServer;

/**
 * need WebsocketServer.java (the wrapper of the websocket server)
 *
 * need java_websocket.jar -> use it as library
 */

@ComponentDescription("publish a message to the websocket server")

@InPorts({
        @InPort(value = "MESSAGE", description = "topic and message", type = String.class)
})
public class WebsocketComponent extends Component {
	BufferedWriter bWrite = null;
	static String data =  new String();
	static int count = 0;

    private InputPort _messagePort;
    private WebsocketServer _ws =  WebsocketServer.getInstance(8118);

    @Override
    protected void execute() throws Exception {
        Packet packet = _messagePort.receive();
        if(packet != null) {
            String message = (String) packet.getContent();
            drop(packet);
            _ws.sendToAll(message);
            count++;
            data.concat(message + "\n");
            System.out.println("sending: " + message);
            if(count == 10)
            {
            	 File logFile = new File("Test");

                 // This will output the full path where the file will be written to...
                 System.out.println(logFile.getCanonicalPath());

                 bWrite = new BufferedWriter(new FileWriter(logFile));
                 bWrite.write("Hello world!");
            }
        }
    }

    @Override
    protected void openPorts() {
        _messagePort =  openInput("MESSAGE");
    }

}
