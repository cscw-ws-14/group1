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
		//component("Publisher", WebsocketComponent.class);
		component("TimeJSON", Jsonify.class);
		component("TimeWS",WebsocketComponent.class);
		
		initialize("/cscw-bplus-04/Motion/PIR", component("Receiver"), port("TOPIC"));
		initialize("bv",component("Parser"),port("KEY"));
		initialize("\"jointName\"",component("JSONConvert"),port("KEY[0]"));
		initialize("\"x\"",component("JSONConvert"),port("KEY[1]"));
		initialize("\"y\"",component("JSONConvert"),port("KEY[2]"));
		initialize("\"z\"",component("JSONConvert"),port("KEY[3]"));
		initialize("\"type\"",component("TimeJSON"), port("KEY[0]"));
		initialize("\"TimeIdle\"",component("TimeJSON"), port("KEY[1]"));
		initialize("\"idle\"",component("TimeJSON"), port("VALUE[0]"));
		//initialize("8118",component("Publisher"), port("PORT"));
		initialize("7220",component("TimeWS"), port("PORT"));
		
		connect(component("Receiver"), port("MESSAGE"), component("Parser"), port("JSON"));
		connect(component("Parser"), port("VALUE[0]"), component("Movement Calculate"), port("DATA"));
		connect(component("Movement Calculate"), port("MESSAGE"),component("Message"), port("MESSAGE"));
		connect(component("Message"),port("YES"),component("Kinect App"),port("MESSAGE"));
		connect(component("Kinect App"),port("VALUE[0]"),component("JSONConvert"),port("VALUE[0]"));
		connect(component("Kinect App"),port("VALUE[1]"),component("JSONConvert"),port("VALUE[1]"));
		connect(component("Kinect App"),port("VALUE[2]"),component("JSONConvert"),port("VALUE[2]"));
		connect(component("Kinect App"),port("VALUE[3]"),component("JSONConvert"),port("VALUE[3]"));
		connect(component("JSONConvert"),port("MESSAGE"),component("TimeWS"),port("MESSAGE"));
		connect(component("Movement Calculate"),port("TIME"),component("TimeJSON"),port("VALUE[1]"));
		connect(component("TimeJSON"),port("MESSAGE"),component("TimeWS"),port("MESSAGE"));
		
	}
	
	public static void main(String[] argv) throws Exception  { 
		 new MovementControl().go(); 
		} 

}
