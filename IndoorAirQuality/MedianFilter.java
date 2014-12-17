/*
 * By:
 * Shekoufeh Gorgi Zadeh
 * 
 * 10.12.2014
 * 
 */
package IndoorAirQuality;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Random;
import java.util.RandomAccess;
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

@ComponentDescription("Finds the median from the input of size of SIZE. (Gets SIZE-times values from IN, and"
		+ "then finds Median).")
@OutPorts({ @OutPort(value = "OUT") })
@InPorts({ @InPort(value = "IN"), @InPort(value = "SIZE")})
public class MedianFilter extends Component {

	static final String copyright = " ";

	private OutputPort outport;

	private InputPort inportIN, inportSize;
	
	private ArrayList<Integer> values = new ArrayList<Integer>();
	
	private int counter = 0;
	private int size;
	
	private boolean firstTime = true;

	@Override
	protected void execute() throws InterruptedException {

		Packet ip;		
		if(firstTime){
			
			ip = inportSize.receive();
			size = Integer.parseInt((String)ip.getContent());
			drop(ip);
			
			firstTime = false;
		}
		
		ip = inportIN.receive();
		values.add(Integer.parseInt((String)ip.getContent()));
		drop(ip);
	
		counter++;
		
		if(counter == size){
			Packet out;
			
			int median = findMedian();
			out = create(median+"");
			outport.send(out);
			
			counter = 0;
			values.clear();
		}
	}


	private int findMedian() {
		Collections.sort(values);
		
		int middle = size/2;
		if(size%2 == 1)
			return values.get(middle);
		else 
			return ((values.get(middle)+ values.get(middle - 1))/2);
	}


	@Override
	protected void openPorts() {

		inportIN = openInput("IN");
		inportSize = openInput("SIZE");
		
		outport = openOutput("OUT");

	}

}
