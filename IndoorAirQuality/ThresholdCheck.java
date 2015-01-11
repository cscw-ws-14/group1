/*
 * By:
 * Shekoufeh Gorgi Zadeh
 * 
 * 10.12.2014
 * 
 */
package IndoorAirQuality;

import java.util.ArrayList;
import java.util.Collections;

import com.jpmorrsn.fbp.engine.Component;
import com.jpmorrsn.fbp.engine.ComponentDescription;
import com.jpmorrsn.fbp.engine.InPort;
import com.jpmorrsn.fbp.engine.InPorts;
import com.jpmorrsn.fbp.engine.InputPort;
import com.jpmorrsn.fbp.engine.OutPort;
import com.jpmorrsn.fbp.engine.OutPorts;
import com.jpmorrsn.fbp.engine.OutputPort;
import com.jpmorrsn.fbp.engine.Packet;

@ComponentDescription("Checks if the value from IN, exceeds given THRESHOLD.")
@OutPorts({ @OutPort(value = "OUT") })
@InPorts({ @InPort(value = "IN"), @InPort(value = "THRESHOLD", type = Integer.class)})
public class ThresholdCheck extends Component {

	static final String copyright = " ";

	private OutputPort outport;

	private InputPort inportIN, inportThreshold;

	private int threshold, value;
	
	private boolean firstTime = true;
	
	private boolean alreadyOverThreshold = false;

	@Override
	protected void execute() throws InterruptedException {

		Packet ip;		
		if(firstTime){
			
			ip = inportThreshold.receive();
			threshold = (Integer)ip.getContent();
			drop(ip);
			
			firstTime = false;
		}
		
		ip = inportIN.receive();
		value = Integer.parseInt((String)ip.getContent());
		drop(ip);
		
		if(value >= threshold && !alreadyOverThreshold) {
			Packet out;
			
			out = create("beep:true");
			outport.send(out);
			
			alreadyOverThreshold = true;
		}
		if(value < threshold && alreadyOverThreshold) {
			Packet out;
			
			out = create("beep:false");
			outport.send(out);
			
			alreadyOverThreshold = false;
		}
	}

	@Override
	protected void openPorts() {

		inportIN = openInput("IN");
		inportThreshold = openInput("THRESHOLD");
		
		outport = openOutput("OUT");

	}

}
