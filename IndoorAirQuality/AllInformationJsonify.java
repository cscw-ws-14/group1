/*
 * By:
 * Shekoufeh Gorgi Zadeh
 * 
 * 11.01.2015
 * 
 */
package IndoorAirQuality;

import java.awt.List;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;
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

@ComponentDescription("Gathers all the information, and whenever there are changes, it sends information forward to be shown.")
@OutPorts({ @OutPort(value = "OUT")})
@InPorts({ @InPort(value = "IN", arrayPort = true, optional = true)})
public class AllInformationJsonify extends Component {

	static final String copyright = " ";

	private OutputPort outportOut;

	private InputPort[] inportIn;
	
	private HashMap<String, String> dataMap = null;
	
	@Override
	protected void execute() throws InterruptedException {

		if(dataMap == null){
			dataMap = new HashMap<String, String>();
			
			dataMap.put("timestamp", "0");
			dataMap.put("beep", "false");
			dataMap.put("level", "0");
			dataMap.put("door", "false");
			dataMap.put("window", "false");
		}
				
		Packet ip;

		String data, key, value;
		StringTokenizer dataTokenizer;
		
		int length = inportIn.length;
		for(int i = 0; i<length; ++i){
			ip = inportIn[i].receive();
			if(ip!=null){
				data = (String)ip.getContent();
				drop(ip);
				
				dataTokenizer = new StringTokenizer(data,":");
				key = dataTokenizer.nextToken();
				value = dataTokenizer.nextToken();
				
				dataMap.put(key, value);
			}
		}
		
		Timestamp timeStamp = new Timestamp(System.currentTimeMillis());;
		dataMap.put("timestamp", timeStamp.toString());
		String JsonStr = "{";
		for(String datakey : dataMap.keySet()){
			JsonStr = JsonStr + datakey + ":" + dataMap.get(datakey) + ","; 
		}
		JsonStr = JsonStr.substring(0, JsonStr.length()-1)+"}";
		
		Packet out;
		out = create(JsonStr);
		outportOut.send(out);
	}


	@Override
	protected void openPorts() {

		inportIn = openInputArray("IN");
		
		outportOut = openOutput("OUT");

	}

}