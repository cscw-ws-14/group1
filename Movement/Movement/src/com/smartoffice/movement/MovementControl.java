package com.smartoffice.movement;

import com.jpmorrsn.fbp.engine.Network;
import com.smartoffice.movement.component.*;

public class MovementControl extends Network{

	@Override
	protected void define() throws Exception {
		
		component("Receiver", MosquittoSubscribe.class);
		component("Movement Calculate", ClassifyComponent.class);
		component("Publisher",MessageBoxComponent.class);
		component("Parser",JsonParser.class);
		component("Kinect App",KinectComponent.class);
		
		initialize("/le/Motion/Pir", component("Receiver"), port("TOPIC"));
		initialize("bv",component("Parser"),port("KEY"));
		
		connect(component("Receiver"), port("MESSAGE"), component("Parser"), port("JSON"));
		connect(component("Parser"), port("VALUE[0]"), component("Movement Calculate"), port("DATA"));
		connect(component("Movement Calculate"), port("MESSAGE"),component("Publisher"), port("MESSAGE"));
		connect(component("Publisher"),port("YES"),component("Kinect App"),port("MESSAGE"));
		
	}
	
	public static void main(String[] argv) throws Exception  { 
		 new MovementControl().go(); 
		} 

}
