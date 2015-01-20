package IndoorAirQuality;

import java.util.StringTokenizer;

import com.jpmorrsn.fbp.engine.*;

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

/**
 * By: 
 * 	Shekoufeh
 * 
 *
 */

@ComponentDescription("Subscribes to a message to the mosquitto server.")

@InPorts({
    @InPort(value = "CLIENTID", description = "client id of the client, can put random string here", type = String.class, optional = true),
    @InPort(value = "TOPIC", description = "topic", type = String.class)
})
@OutPorts({ @OutPort(value = "MESSAGE") })
public class MosquittoSubscribe extends Component implements MqttCallback{

    private InputPort clientId;

    private InputPort inportTopic;
    private OutputPort outPort;

//    private OutputPort outport;

    // some default values, will be overridden later
    private String _topic        = "heartbeat";
//    private String _content      = "beat";
    private String _clientId     = "client_id_";

    // some values that is not necessary to change
//    private int qos             = 2;
    private String broker       = "tcp://localhost:1883";
    
    private int stayAliveTime = 10000;
    @Override
    protected void execute() throws Exception {

        //
        _clientId = (String) fillInput(clientId, _clientId);
        
    
        	Packet pT = inportTopic.receive();
        	
        	_topic = (String) pT.getContent();
            drop(pT);
            
            try {
                MemoryPersistence persistence = new MemoryPersistence();

                // connect to mqtt server, make sure that the server is on
                System.out.println("Connecting to broker: " + broker);
                MqttClient sampleClient = new MqttClient(broker, _clientId, persistence);
                MqttConnectOptions connOpts = new MqttConnectOptions();
                connOpts.setCleanSession(true);
                sampleClient.connect(connOpts);
                sampleClient.setCallback(this);
                sampleClient.subscribe(_topic);
                stayAlive();
                sampleClient.disconnect();
                sampleClient.close();

                
            } catch(MqttException me) {
                System.out.println("reason "+me.getReasonCode());
                System.out.println("msg "+me.getMessage());
                System.out.println("loc "+me.getLocalizedMessage());
                System.out.println("cause "+me.getCause());
                System.out.println("excerpt "+me);
                me.printStackTrace();
            }
        
    }
    
    private void stayAlive() throws InterruptedException{
    	while(true){
    		sleep(stayAliveTime);
    	}
    }

    @Override
    protected void openPorts() {
        clientId = openInput("CLIENTID");
        inportTopic =  openInput("TOPIC");
        outPort = openOutput("MESSAGE");
    }

    public Object fillInput(InputPort inPort, Object defaultValue){
        Packet rp = inPort.receive();
        if (rp == null) {
            return defaultValue;
        }

        Object ret = rp.getContent();
        drop(rp);

        return ret;
    }

	public void connectionLost(Throwable arg0) {
		// TODO Auto-generated method stub
		
	}

	public void deliveryComplete(IMqttDeliveryToken arg0) {
		// TODO Auto-generated method stub
		
	}

	public void messageArrived(String topic, MqttMessage message) throws Exception {
		// TODO Auto-generated method stub
//		System.out.println(message);
		String msgStr = message.toString();
		Packet msgPacket =  create(msgStr);
		outPort.send(msgPacket);
	
	}

}
