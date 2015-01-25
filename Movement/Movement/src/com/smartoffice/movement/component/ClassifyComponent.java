package com.smartoffice.movement.component;

import java.util.Calendar;

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
	@OutPort(value = "MESSAGE", description = "sends a message to the node handling Kinect", type = String.class, optional = true),
	@OutPort(value = "TIME", description = "sends time idle", type = String.class, optional = true)
})

public class ClassifyComponent extends Component {
	
	private InputPort inPort;
	private OutputPort outPort,timePort;
	
	private static int trueCount = 0;
	private static int falseCount = 0;
	private static int totalCount = 0;
	private static long minIdle;
	private static long startTime;
	private static long endTime;

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
			reset();
		}
		else
		{
			falseCount++;
			totalCount++;
			endTime = Calendar.getInstance().getTimeInMillis();
			minIdle = (endTime-startTime)/1000;
			Packet op = create(String.valueOf(minIdle));
			timePort.send(op);
		}
		if(totalCount == 10)
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
					Packet finishpacket = create("Do you want to start an exercise?");
					outPort.send(finishpacket);
				}
			}
		}
		

	}
	
	private void reset()
	{
		startTime = Calendar.getInstance().getTimeInMillis();
	}
	
	@Override
	protected void openPorts() {
		inPort = openInput("DATA");
		outPort = openOutput("MESSAGE");
		timePort = openOutput("TIME");

	}

}
