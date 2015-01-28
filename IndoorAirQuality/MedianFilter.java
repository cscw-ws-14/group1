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

@ComponentDescription("Finds the median from the input of size of SIZE. (Gets SIZE-times values from IN, and"
		+ "then finds Median).")
@OutPorts({ @OutPort(value = "OUT") })
@InPorts({ @InPort(value = "IN"), @InPort(value = "SIZE", optional = true)})
public class MedianFilter extends Component {

	static final String copyright = " ";

	private OutputPort outport;

	private InputPort inportIN, inportSize;
	
	private ArrayList<Integer> values = new ArrayList<Integer>();
	
	private int counter = 0;
	private int size;
	
	private boolean firstTime = true;
	private String extraInfo;

	@Override
	protected void execute() throws InterruptedException {

		Packet ip;		
					
		ip = inportSize.receive();
		if(ip!=null){
			size = Integer.parseInt((String)ip.getContent());
			drop(ip);
		}
			
		ip = inportIN.receive();
		String inputData =  (String)ip.getContent();
		int voc = getValue(inputData);
		extraInfo = getExtraInfo(inputData);
		values.add(voc);
//		System.out.print("  ["+voc+"]   ");
		drop(ip);
//		System.out.println("--------------------"+voc);
		counter++;
		if(counter == 36){
			System.out.println("a");
		}
		
		
		if(counter > size){
			Packet out;
			values.remove(0);
			int median = findMedian();
			out = create(median+":extraInfo:"+extraInfo);
			outport.send(out);
			
//			System.out.println(counter+")))median"+median);

		}
		else{
			Packet out = create(voc+":extraInfo:"+extraInfo);
			outport.send(out);
		}
	}


	private int findMedian() {
		ArrayList<Integer> tmp = new ArrayList<Integer>();
		tmp = (ArrayList<Integer>) values.clone();
		Collections.sort(tmp);
		int valuesSize = tmp.size();
		int middle = size/2;
		if(size%2 == 1)
			return tmp.get(middle);
		else 
			return ((tmp.get(middle)+ tmp.get(middle - 1))/2);
	}


	@Override
	protected void openPorts() {

		inportIN = openInput("IN");
		inportSize = openInput("SIZE");
		
		outport = openOutput("OUT");

	}

	private String getExtraInfo(String info) {
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
}
