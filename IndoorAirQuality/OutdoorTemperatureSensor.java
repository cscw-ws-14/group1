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
@OutPorts({ @OutPort(value = "TEMPERATURE") })
@InPorts({ @InPort(value = "LOWERBOUND", optional = true, type = Integer.class),
		   @InPort(value = "UPPERBOUND", optional = true , type = Integer.class),
		   @InPort(value = "ACTIVATE")})
public class OutdoorTemperatureSensor extends Component {

	static final String copyright = " ";

	private OutputPort outport;

	private InputPort inportLowerBound;
	private InputPort inportUpperBound, inportActivate;
	
	private int lowerBound;
	private int upperBound;

	@Override
	protected void execute() throws InterruptedException {

		Packet ip;

		ip = inportLowerBound.receive();
		lowerBound =  Integer.parseInt((String)ip.getContent());
		drop(ip);
	
		ip = inportUpperBound.receive();
		upperBound = Integer.parseInt((String)ip.getContent());
		drop(ip);
		
		Packet out;
		
		Random random = new Random();
		int temp;
		int range = upperBound - lowerBound + 1;
		
		if((ip = inportActivate.receive())!=null){
			temp = random.nextInt(range) + lowerBound;
			out = create(temp+"");
			outport.send(out);
			drop(ip);
		}
	}


	@Override
	protected void openPorts() {

		inportLowerBound = openInput("LOWERBOUND");
		inportUpperBound = openInput("UPPERBOUND");
		inportActivate = openInput("ACTIVATE");

		outport = openOutput("TEMPERATURE");

	}

}