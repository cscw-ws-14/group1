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

@ComponentDescription("Maps the values from IN, to a certain group number.")
@OutPorts({ @OutPort(value = "OUT") })
@InPorts({ @InPort(value = "IN") })
public class Mapper extends Component {

	static final String copyright = " ";

	private OutputPort outport;

	private InputPort inportIN;

	private int value;

	@Override
	protected void execute() throws InterruptedException {

		Packet ip;

		ip = inportIN.receive();
		value = Integer.parseInt((String) ip.getContent());
		drop(ip);

		Packet out;

		if (value < 500) {
			out = create("level:0");
		} else if (value < 750) {
			out = create("level:1");
		} else if (value < 1000) {
			out = create("level:2");
		} else if (value < 1150) {
			out = create("level:3");
		} else if (value < 1300) {
			out = create("level:4");
		} else if (value < 1500) {
			out = create("level:5");
		} else if (value < 1700) {
			out = create("level:6");
		} else if (value < 1900) {
			out = create("level:7");
		} else if (value < 2200) {
			out = create("level:8");
		} else {
			out = create("level:9");
		}
		outport.send(out);
	}

	@Override
	protected void openPorts() {

		inportIN = openInput("IN");

		outport = openOutput("OUT");

	}

}
