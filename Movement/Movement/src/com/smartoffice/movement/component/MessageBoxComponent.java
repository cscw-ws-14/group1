package com.smartoffice.movement.component;

import com.jpmorrsn.fbp.engine.*;
import javax.swing.JOptionPane;

/**
 * need WebsocketServer.java (the wrapper of the websocket server)
 *
 * need java_websocket.jar -> use it as library
 */

@ComponentDescription("pop up a confirm box in an AWT window")

@InPorts({
        @InPort(value = "MESSAGE", description = "message ", type = String.class)
})

@OutPorts({
        @OutPort(value = "YES", optional = true),
        @OutPort(value = "NO", optional = true)
})
public class MessageBoxComponent extends com.jpmorrsn.fbp.engine.Component{
    private InputPort _messagePort;
    private OutputPort outportYes;
    private OutputPort outportNo;
    static int flag = 0;

    @Override
    protected void execute() throws Exception {
        Packet packet = _messagePort.receive();

        if(packet != null) {
            String message = (String) packet.getContent();
            drop(packet);
            if(flag == 0)
            {
            int result = JOptionPane.showConfirmDialog(null, message, "Alert", JOptionPane.YES_NO_OPTION, JOptionPane.PLAIN_MESSAGE);
            if (result == JOptionPane.YES_OPTION)
            {
                Packet out = create(true);
                flag = 1;
                outportYes.send(out);
            }
            else
            {
                //Packet out = create(false);
               // outportNo.send(out);
            }
            }

            System.out.println("message box pop up: " + message);
        }
    }

    @Override
    protected void openPorts() {
        _messagePort =  openInput("MESSAGE");

        outportYes = openOutput("YES");
        outportNo = openOutput("NO");
    }
}
