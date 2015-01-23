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
import java.util.StringTokenizer;

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
@OutPorts({ @OutPort(value = "OUT")  })
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
		String info = (String)ip.getContent();
		value = getValue(info);
		String room = getRoom(info);
//		System.out.println("Room"+room);
		drop(ip);
		
		if(value >= threshold && !alreadyOverThreshold) {
			Packet out;
			
			out = create("\"beep\":true,\"VOC\":"+value+",\"room\":\""+room+"\"");
			outport.send(out);
			
			alreadyOverThreshold = true;
		}
		else{
			Packet out;

			out = create("\"beep\":false,\"VOC\":"+value+",\"room\":\""+room+"\"");
			outport.send(out);
		}
		if(value < threshold && alreadyOverThreshold) {
			
			
			alreadyOverThreshold = false;
		}
	}

	private String getRoom(String info) {
		// TODO Auto-generated method stub
		StringTokenizer tokenizer = new StringTokenizer(info, ":");
		String token = "";
		while(tokenizer.hasMoreTokens()){
			token = tokenizer.nextToken();
			if(token.equals("extraInfo")){
				return tokenizer.nextToken();
			}
		}
		
		return token;
	}

	private int getValue(String info) {
		// TODO Auto-generated method stub
		StringTokenizer tokenizer = new StringTokenizer(info, ":");
		return Integer.parseInt(tokenizer.nextToken());
	}

	@Override
	protected void openPorts() {

		inportIN = openInput("IN");
		inportThreshold = openInput("THRESHOLD");
		
		outport = openOutput("OUT");
		
	}

}
