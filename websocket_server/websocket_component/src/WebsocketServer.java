import org.java_websocket.WebSocket;
import org.java_websocket.handshake.ClientHandshake;
import org.java_websocket.server.WebSocketServer;

import java.net.InetSocketAddress;
import java.net.UnknownHostException;
import java.util.Collection;

/**
 * webscocket server class,
 * it is a singleton class,
 *
 * initiate or get the instance using getInstance([port number])
 * limitation: there is no such a thing as "sub/channel/subfolder" using this library
 *
 * this class will open a port and keep listening into that port until the program get killed
 * this class can not receive message
 */

public class WebsocketServer extends WebSocketServer {

    private static WebsocketServer instance = null;
    public static int port = 8887;

    public static WebsocketServer getInstance(int port){
        WebsocketServer.port = port;
        return WebsocketServer.getInstance();
    }

    protected static WebsocketServer getInstance() {
        if(instance == null) {
            try {
                instance = new WebsocketServer(WebsocketServer.port);
                instance.start();

            } catch (UnknownHostException e) {
                e.printStackTrace();
            }
        }

        return instance;
    }

    protected WebsocketServer(int port) throws UnknownHostException {
        super( new InetSocketAddress( port ) );
    }

    @Override
    public void onOpen(WebSocket webSocket, ClientHandshake clientHandshake) {
        System.out.println("new connection: " + clientHandshake.getResourceDescriptor());
        System.out.println( webSocket.getRemoteSocketAddress().getAddress().getHostAddress() + " connected!" );
    }

    @Override
    public void onClose(WebSocket webSocket, int i, String s, boolean b) {
        System.out.println( webSocket + " disconnected!" );
    }

    @Override
    public void onMessage(WebSocket webSocket, String s) {
        // suppose not to receive any message. because it is a publishing server
    }

    @Override
    public void onError(WebSocket webSocket, Exception ex) {
        ex.printStackTrace();
        if( webSocket != null ) {
            // some errors like port binding failed may not be assignable to a specific web socket
        }
    }

    /**
     * Sends <var>text</var> to all currently connected WebSocket clients.
     *
     * @param text
     *            The String to send across the network.
     * @throws InterruptedException
     *             When socket related I/O errors occur.
     */
    public void sendToAll( String text ) {
        Collection<WebSocket> con = connections();
        synchronized ( con ) {
            for( WebSocket c : con ) {
                c.send( text );
            }
        }
    }
}
