/*
 * By:
 * Shekoufeh Gorgi Zadeh
 * 
 * 10.12.2014
 * 
 */
package IndoorAirQuality;

import com.jpmorrsn.fbp.engine.Component;
import com.jpmorrsn.fbp.engine.ComponentDescription;
import com.jpmorrsn.fbp.engine.InPort;
import com.jpmorrsn.fbp.engine.InPorts;
import com.jpmorrsn.fbp.engine.InputPort;
import com.jpmorrsn.fbp.engine.OutPort;
import com.jpmorrsn.fbp.engine.OutPorts;
import com.jpmorrsn.fbp.engine.OutputPort;
import com.jpmorrsn.fbp.engine.Packet;

@ComponentDescription("Simulates Speaker.")
@OutPorts({ @OutPort(value = "OUT", optional = true) })
@InPorts({ @InPort(value = "IN")})
public class SpeakerActuator extends Component {

	static final String copyright = " ";

	private OutputPort outport;

	private InputPort inportIN;


	@Override
	protected void execute() throws InterruptedException {

		Packet ip;		
		
		ip = inportIN.receive();
		System.out.println("================Beep");
		drop(ip);
	}

	@Override
	protected void openPorts() {

		inportIN = openInput("IN");
		
		outport = openOutput("OUT");

	}

}
