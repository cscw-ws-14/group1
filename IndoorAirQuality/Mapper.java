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

@ComponentDescription("Maps the values from IN, to a certain group number.")
@OutPorts({ @OutPort(value = "OUT")})
@InPorts({ @InPort(value = "IN") })
public class Mapper extends Component {

	static final String copyright = " ";

	private OutputPort outport;

	private InputPort inportIN;

	private int value;

	@Override
	protected void execute() throws InterruptedException {

		Packet ip;
		String info;
		ip = inportIN.receive();
		info = (String) ip.getContent();
		value = extractValue(info);
		drop(ip);

		Packet out;

		if (value < 500) {
			out = create(info+",\"level\":0");
		} else if (value < 750) {
			out = create(info+",\"level\":1");
		} else if (value < 1000) {
			out = create(info+",\"level\":2");
		} else if (value < 1150) {
			out = create(info+",\"level\":3");
		} else if (value < 1300) {
			out = create(info+",\"level\":4");
		} else if (value < 1500) {
			out = create(info+",\"level\":5");
		} else if (value < 1700) {
			out = create(info+",\"level\":6");
		} else if (value < 1900) {
			out = create(info+",\"level\":7");
		} else if (value < 2200) {
			out = create(info+",\"level\":8");
		} else {
			out = create(info+",\"level\":9");
		}
		outport.send(out);
		
	}

	private int extractValue(String info) {
		StringTokenizer infoTokenizer = new StringTokenizer(info,",");
		StringTokenizer elementTokenizer;
		while(infoTokenizer.hasMoreTokens()){
			elementTokenizer = new StringTokenizer(infoTokenizer.nextToken(),":");
			if(elementTokenizer.nextToken().equals("\"VOC\""))
				return Integer.parseInt(elementTokenizer.nextToken());
		}
		return 0;
	}

	@Override
	protected void openPorts() {

		inportIN = openInput("IN");

		outport = openOutput("OUT");
	}

}
