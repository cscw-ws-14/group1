package IndoorAirQuality;

import java.util.ArrayList;
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
    @InPort(value = "CLIENTID", description = "client id", type = String.class, optional = true),
    @InPort(value = "TOPIC", description = "topic", type = String.class),
    @InPort(value="FILTER", arrayPort = true, optional = true),
    @InPort(value="EXTRAINFO", optional = true)
})
@OutPorts({ @OutPort(value = "MESSAGE") })
public class MosquittoSubscribe extends Component implements MqttCallback{

    private InputPort clientId;
    private InputPort[] inportFilter;
    
    private InputPort inportTopic, inportExtra;
    private OutputPort outPort;



    // some default values, will be overridden later
    private String _topic        = "heartbeat";
    private String _clientId     = "client_id_";

    // some values that is not necessary to change
    private String broker       = "tcp://localhost:1883";
    
    private int stayAliveTime = 10000;
    private int filterSize = 0;
    private ArrayList<String> filter = new ArrayList<String>();
    private String extraInfoTopic = null;
    @Override
    protected void execute() throws Exception {

        //
        _clientId = (String) fillInput(clientId, _clientId);
        
    
        	Packet pT = inportTopic.receive();
        	
        	_topic = (String) pT.getContent();
            drop(pT);
            
            filterSize = inportFilter.length;
            
            
            Packet p;
            for(int i = 0 ; i<filterSize;++i){
            	p = inportFilter[i].receive();
            	filter.add((String)p.getContent());
            	drop(p);
            }
            
            p = inportExtra.receive();
            if(p!=null){
            	extraInfoTopic = (String)p.getContent();
            	drop(p);
            }
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
        inportFilter = openInputArray("FILTER");
        inportExtra = openInput("EXTRAINFO");
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

		if(relatedTopic(topic)){
			
			String msgStr = message.toString();
			if(extraInfoTopic!=null){
				String extraInfo = parseTopic(topic);
				if(extraInfo!=null)
					msgStr = msgStr + ":\"extraInfo\":"+extraInfo;
			}
			
			Packet msgPacket = create(msgStr);
			outPort.send(msgPacket);
		}
	
	}

	private boolean relatedTopic(String topic) {
		// TODO Auto-generated method stub
			
		String subTopic;
		for(int i = 0 ; i < filter.size();++i ){
			subTopic = filter.get(i);
			if(!subTopicExistsInTopic(subTopic, topic))
				return false;
		}
		return true;
	}

	private boolean subTopicExistsInTopic(String subTopic, String topic) {
		// TODO Auto-generated method stub
		StringTokenizer topicTokenizer = new StringTokenizer(topic,"/");

		String topicToken;
		while(topicTokenizer.hasMoreTokens()){
			topicToken = topicTokenizer.nextToken();
			if(subTopic.equals(topicToken))
				return true;
		}
		return false;
	}

	private String parseTopic(String topic) {
		// TODO Auto-generated method stub
	
		StringTokenizer tokenizeReceivedTopic = new StringTokenizer(topic,"/");
		String subtopic;
		while(tokenizeReceivedTopic.hasMoreTokens()){
			subtopic = tokenizeReceivedTopic.nextToken();
			if(subtopic.contains(extraInfoTopic))
				return subtopic;
		}
		return null;
	}

}
