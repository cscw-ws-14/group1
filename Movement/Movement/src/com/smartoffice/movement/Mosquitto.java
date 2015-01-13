package com.smartoffice.movement;

import com.jpmorrsn.fbp.engine.*;

import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

/**
 * go to
 * https://github.com/r4ccoon/javaFBPMosquittoComponent
 * to get the PAHO mosquittos jar files (java libs that are needed)
 *
 * to install mosquitto, on osx-> brew install mosquitto
 * on windows and linux , http://mosquitto.org/download/ and follow the instructions
 */

@ComponentDescription("Recieve message from MQTT broker and pass to Kinect")

@OutPorts({
	@OutPort(value = "MESSAGE", description = "client id of the client, can put random string here", type = String.class, optional = true),
})

public class Mosquitto extends Component{

	private OutputPort message;

	//    private OutputPort outport;

	// some default values, will be overridden later
	private String _topic        = "/movegame";
	private String _clientId = "client";

	// some values that is not necessary to change
	private String broker       = "tcp://localhost:1883";

	@Override
	protected void execute() throws Exception {


		try {
			MemoryPersistence persistence = new MemoryPersistence();

			// connect to mqtt server, make sure that the server is on
			System.out.println("Connecting to broker: " + broker);
			MqttClient sampleClient = new MqttClient(broker, _clientId, persistence);
			MqttConnectOptions connOpts = new MqttConnectOptions();
			connOpts.setCleanSession(true);
			sampleClient.connect(connOpts);

			// set up topic and publishing the message
			System.out.println("Connected");
			sampleClient.subscribe(_topic);

			// disconnect from the server
			sampleClient.disconnect();
			System.out.println("Disconnected");

		} catch(MqttException me) {
			System.out.println("reason "+me.getReasonCode());
			System.out.println("msg "+me.getMessage());
			System.out.println("loc "+me.getLocalizedMessage());
			System.out.println("cause "+me.getCause());
			System.out.println("excerpt "+me);
			me.printStackTrace();
		}

	}

	@Override
	protected void openPorts() {
		message = openOutput("MESSAGE");

	}

	// public Packet checkInput(InputPort inPort) throws Exception {

	// }

	/* public Object fillInput(InputPort inPort, Object defaultValue){
        Packet rp = inPort.receive();
        if (rp == null) {
            return defaultValue;
        }

        Object ret = rp.getContent();
        drop(rp);

        return ret;
    }*/
}
