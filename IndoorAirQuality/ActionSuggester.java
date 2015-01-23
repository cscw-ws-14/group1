/*
 * By:
 * Shekoufeh Gorgi Zadeh
 * 
 * 10.12.2014
 * 
 */
package IndoorAirQuality;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Random;
import java.util.RandomAccess;
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

@ComponentDescription("Simulates air sensor. Sends out VOC")
@OutPorts({ @OutPort(value = "OUT") })
@InPorts({ @InPort(value = "IN"),
		   @InPort(value = "TEMPERATURE"),
		   @InPort(value = "THRESHOLD")})
public class ActionSuggester extends Component {

	static final String copyright = " ";

	private OutputPort outportState;

	private InputPort inportIn, inportTemp, inportThreshold;
	
	
	private int threshold, voc;
	private float temperator = 10;
	private boolean door, window;
	
	private boolean firstTime = true;
	private String info;
	private double timeout = 1;

	@Override
	protected void execute()  {

		Packet ip;

		if(firstTime)
		{
			ip = inportThreshold.receive();
			threshold = Integer.parseInt((String)ip.getContent());
			drop(ip);
			firstTime = false;
		}
		
		ip = inportIn.receive();
		String sendInfo;
		if(ip!=null){
			info = (String)ip.getContent();
			sendInfo = info;
			drop(ip);
		}
		
		ip = inportTemp.receive();
		if(ip!=null){
			temperator =  Float.parseFloat((String)ip.getContent());
			drop(ip);
		}
		
		sendInfo = info;
		voc = extractValue(sendInfo);
		sendInfo = removeVOCAddTimeStamp(sendInfo);
		Packet out;
		int diff;

		if(voc >= threshold){
			diff = voc - threshold;
			if(diff >= 700){
				
				out = create("{"+sendInfo+",\"door\":true,\"window\":true}");
				outportState.send(out);

				
			} else if(temperator < 10){
				
				out = create("{"+sendInfo+",\"door\":true,\"window\":false}");
				outportState.send(out);
				
			} else {
				out = create("{"+sendInfo+",\"door\":false,\"window\":true}");
				outportState.send(out);
			}
		}
		else {
			out = create("{"+sendInfo+",\"door\":false,\"window\":false}");
			outportState.send(out);
			
		}
		
	}


	private String removeVOCAddTimeStamp(String info) {
		System.out.println("-------------------------"+info);
		StringTokenizer infoTokenizer = new StringTokenizer(info,",");
		StringTokenizer elementTokenizer;
		String result="";
		String key;
		while(infoTokenizer.hasMoreTokens()){
			elementTokenizer = new StringTokenizer(infoTokenizer.nextToken(),":");
			key = elementTokenizer.nextToken();
			if(!key.equals("\"VOC\"")){
				result=result+key+":"+elementTokenizer.nextToken()+",";
			}
				
		}
		Timestamp timeStamp = new Timestamp(System.currentTimeMillis());
		result = "\"type\":"+"\"airquality\""+","+result;
		result = "\"timestamp\":"+timeStamp.getTime()+","+result;
		return result.substring(0, result.length()-1);
	}


	@Override
	protected void openPorts() {

		inportTemp = openInput("TEMPERATURE");
		inportThreshold = openInput("THRESHOLD");
		inportIn = openInput("IN");

		outportState = openOutput("OUT");
		

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
}