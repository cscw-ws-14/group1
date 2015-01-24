package com.smartoffice.movement;

import com.jpmorrsn.fbp.engine.Network;
import com.smartoffice.movement.component.*;

public class MovementControl extends Network{

	@Override
	protected void define() throws Exception {
		
		component("Receiver", MosquittoSubscribe.class);
		component("Movement Calculate", ClassifyComponent.class);
		component("Message",MessageBoxComponent.class);
		component("Parser",JsonParser.class);
		component("Kinect App",KinectComponent.class);
		component("JSONConvert", Jsonify.class);
		component("Publisher", WebsocketComponent.class);
		
		initialize("/le/Motion/Pir", component("Receiver"), port("TOPIC"));
		initialize("bv",component("Parser"),port("KEY"));
		initialize("jointName;x;y;z",component("JSONConvert"),port("KEY"));
		
		connect(component("Receiver"), port("MESSAGE"), component("Parser"), port("JSON"));
		connect(component("Parser"), port("VALUE[0]"), component("Movement Calculate"), port("DATA"));
		connect(component("Movement Calculate"), port("MESSAGE"),component("Message"), port("MESSAGE"));
		connect(component("Message"),port("YES"),component("Kinect App"),port("MESSAGE"));
		connect(component("Kinect App"),port("VALUE"),component("JSONConvert"),port("VALUE"));
		connect(component("JSONConvert"),port("MESSAGE"),component("Publisher"),port("MESSAGE"));
		
	}
	
	public static void main(String[] argv) throws Exception  { 
		 new MovementControl().go(); 
		} 

}
