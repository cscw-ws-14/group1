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
@OutPorts({ @OutPort(value = "VOC", type = Integer.class) })
@InPorts({ @InPort(value = "LOWERBOUND", optional = true, type = Integer.class),
		   @InPort(value = "UPPERBOUND", optional = true , type = Integer.class)})
public class AirSensor extends Component {

	static final String copyright = " ";

	private OutputPort outport;

	private InputPort inportLowerBound;
	private InputPort inportUpperBound;
	
	private int lowerBound;
	private int upperBound;

	@Override
	protected void execute() throws InterruptedException {

		Packet ip;

		ip = inportLowerBound.receive();
		lowerBound =  (Integer)ip.getContent();
		drop(ip);
	
		ip = inportUpperBound.receive();
		upperBound = (Integer)ip.getContent();
		drop(ip);
		
		Packet out;
		
		Random random = new Random();
		int voc;
		int range = upperBound - lowerBound + 1;
		for(int i = 0 ; i < 1000; ++i)
		{
			sleep(100);
			voc = random.nextInt(range) + lowerBound;
			out = create(voc);
			outport.send(out);
		}
	}


	@Override
	protected void openPorts() {

		inportLowerBound = openInput("LOWERBOUND");
		inportUpperBound = openInput("UPPERBOUND");

		outport = openOutput("VOC");

	}

}