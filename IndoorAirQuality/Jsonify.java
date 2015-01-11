/*
 * By:
 * Shekoufeh Gorgi Zadeh
 * 
 * 11.01.2015
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

@ComponentDescription("Wrap the message into jason")
@OutPorts({ @OutPort(value = "MESSAGE") })
@InPorts({ @InPort(value = "KEY", arrayPort=true),
		   @InPort(value = "VALUE", arrayPort=true)})
public class Jsonify extends Component {

	static final String copyright = " ";

	private OutputPort outportMessage;

	private InputPort[] inportKeys, inportValues;

	private int keyLength;
	private ArrayList<String> keys = new ArrayList<String>();
	private ArrayList<String> values = new ArrayList<String>();
	@Override
	protected void execute() throws InterruptedException {

		keyLength = inportKeys.length;
		Packet ip;

		for(int i = 0; i<keyLength; ++i){
			ip = inportKeys[i].receive();
			keys.add((String)ip.getContent());
			drop(ip);
		}
		
		int valueLength = inportValues.length;
		for(int i = 0; i<valueLength; ++i){
			ip = inportValues[i].receive();
			values.add((String)ip.getContent());
			drop(ip);
		}
		
		String json = "{";
		int i = 0;
		try{
			for(; i<keyLength;++i){
				json = json+keys.get(i)+":"+values.get(i)+",";
			}
		}catch(Exception e){
			for(; i<keyLength;++i){
				json = json+keys.get(i)+":,";
			}
		}
		json = json.substring(0, json.length()-1)+"}";
		
		Packet out;
		out = create(json);
		outportMessage.send(out);
	
	}


	@Override
	protected void openPorts() {

		inportKeys = openInputArray("KEY");
		inportValues = openInputArray("VALUE");

		outportMessage = openOutput("MESSAGE");

	}

}