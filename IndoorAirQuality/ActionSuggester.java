/*
 * By:
 * Shekoufeh Gorgi Zadeh
 * 
 * 10.12.2014
 * 
 */
package IndoorAirQuality;

import java.util.ArrayList;
import java.util.Random;
import java.util.RandomAccess;

import com.jpmorrsn.fbp.engine.Component;
import com.jpmorrsn.fbp.engine.ComponentDescription;
import com.jpmorrsn.fbp.engine.InPort;
import com.jpmorrsn.fbp.engine.InPorts;
import com.jpmorrsn.fbp.engine.InputPort;
import com.jpmorrsn.fbp.engine.OutPort;
import com.jpmorrsn.fbp.engine.OutPorts;
import com.jpmorrsn.fbp.engine.OutputPort;
import com.jpmorrsn.fbp.engine.Packet;

@ComponentDescription("Simulates air sensor. Sends out VOC")
@OutPorts({ @OutPort(value = "DOORSTATE", optional = true),
			@OutPort(value = "WINDOWSTATE", optional = true) })
@InPorts({ @InPort(value = "VOC"),
		   @InPort(value = "TEMPERATURE", optional = true),
		   @InPort(value = "THRESHOLD", optional = true)})
public class ActionSuggester extends Component {

	static final String copyright = " ";

	private OutputPort outportDoor, outportWindow;

	private InputPort inportVoc, inportTemp, inportThreshold;
	
	
	private int threshold, voc;
	private float temperator;
	private boolean door, window;
	
	private boolean firstTime = true;

	@Override
	protected void execute() throws InterruptedException {

		Packet ip;

		if(firstTime)
		{
			ip = inportThreshold.receive();
			threshold = Integer.parseInt((String)ip.getContent());
			drop(ip);
			firstTime = false;
		}
		
		ip = inportTemp.receive();
		if(ip!=null){
			temperator =  Float.parseFloat((String)ip.getContent());
			drop(ip);
		}
		ip = inportVoc.receive();
		if(ip!=null){
			voc = Integer.parseInt((String)ip.getContent());
			drop(ip);
		}
		Packet out;
		int diff;
		if(voc >= threshold){
			diff = voc - threshold;
			if(diff > 1000){
				
				out = create("window:true");
				outportDoor.send(out);
				
				out = create("door:true");
				outportWindow.send(out);
				
			} else if(temperator < 10){
				
				out = create("door:true");
				outportDoor.send(out);
				
			} else {
				out = create("window:true");
				outportWindow.send(out);
			}
		}
		else {
			out = create("door:false");
			outportDoor.send(out);
			
			out = create("window:false");
			outportWindow.send(out);
		}
		
	
	}


	@Override
	protected void openPorts() {

		inportTemp = openInput("TEMPERATURE");
		inportThreshold = openInput("THRESHOLD");
		inportVoc = openInput("VOC");

		outportDoor = openOutput("DOORSTATE");
		outportWindow = openOutput("WINDOWSTATE");

	}

}