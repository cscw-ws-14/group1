package com.smartoffice.movement.component;

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

    private InputPort _messagePort;
    private WebsocketServer _ws =  WebsocketServer.getInstance(8118);

    @Override
    protected void execute() throws Exception {
        Packet packet = _messagePort.receive();
        if(packet != null) {
            String message = (String) packet.getContent();
            drop(packet);

            _ws.sendToAll(message);
            System.out.println("sending: " + message);
        }
    }

    @Override
    protected void openPorts() {
        _messagePort =  openInput("MESSAGE");
    }

}
