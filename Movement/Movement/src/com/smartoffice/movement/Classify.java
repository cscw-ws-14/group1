package com.smartoffice.movement;

import com.jpmorrsn.fbp.engine.Component;
import com.jpmorrsn.fbp.engine.InPort;
import com.jpmorrsn.fbp.engine.InPorts;
import com.jpmorrsn.fbp.engine.InputPort;
import com.jpmorrsn.fbp.engine.OutPort;
import com.jpmorrsn.fbp.engine.OutPorts;
import com.jpmorrsn.fbp.engine.OutputPort;
import com.jpmorrsn.fbp.engine.Packet;

@InPorts({
	@InPort(value = "DATA", description = "gets the current value from the MQTT Receiver and maintains")
})
@OutPorts({
	@OutPort(value = "MESSAGE", description = "sends a message to the node handling Kinect", type = String.class, optional = true)
})

public class Classify extends Component {
	
	private InputPort inPort;
	private OutputPort outPort;
	
	private static int trueCount = 0;
	private static int falseCount = 0;
	private static int totalCount = 0;

	@Override
	protected void execute() throws Exception {
		
		Packet ip;

		ip = inPort.receive();
		String name = (String) ip.getContent();
		drop(ip);
		if(name.equals("true"))
		{
			trueCount++;
			totalCount++;
		}
		else
		{
			falseCount++;
			totalCount++;
		}
		if(totalCount == 50)
		{
			if(trueCount>falseCount)
			{
				trueCount = 0;
				falseCount = 0;
				totalCount = 0;
			}
			else
			{
				trueCount = 0;
				falseCount = 0;
				totalCount = 0;
				if(outPort.isConnected())
				{
					Packet finishpacket = create("Send request");
					outPort.send(finishpacket);
				}
			}
		}
		

	}
	
	@Override
	protected void openPorts() {
		inPort = openInput("DATA");
		outPort = openOutput("MESSAGE");

	}

}
