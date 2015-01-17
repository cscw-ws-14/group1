package com.smartoffice.movement;

import com.jpmorrsn.fbp.engine.Network;

public class MovementControl extends Network{

	@Override
	protected void define() throws Exception {
		
		component("Receiver", MosquittoSubscribe.class);
		component("Movement Calculate", Classify.class);
		component("Publisher",WebsocketComponent.class);
		component("Parser",JsonParser.class);
		
		initialize("/le/Motion/Pir", component("Receiver"), port("TOPIC"));
		initialize("bv",component("Parser"),port("KEY"));
		
		connect(component("Receiver"), port("MESSAGE"), component("Parser"), port("JSON"));
		connect(component("Parser"), port("VALUE[0]"), component("Movement Calculate"), port("DATA"));
		connect(component("Movement Calculate"), port("MESSAGE"),component("Publisher"), port("MESSAGE"));
		
	}
	
	public static void main(String[] argv) throws Exception  { 
		 new MovementControl().go(); 
		} 

}
