/*
 * By:
 * Shekoufeh Gorgi Zadeh
 * 
 * 10.12.2014
 * 
 */
package IndoorAirQuality;

import java.awt.List;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.logging.ErrorManager;

import jdk.nashorn.internal.parser.JSONParser;

import com.jpmorrsn.fbp.engine.Component;
import com.jpmorrsn.fbp.engine.ComponentDescription;
import com.jpmorrsn.fbp.engine.InPort;
import com.jpmorrsn.fbp.engine.InPorts;
import com.jpmorrsn.fbp.engine.InputPort;
import com.jpmorrsn.fbp.engine.OutPort;
import com.jpmorrsn.fbp.engine.OutPorts;
import com.jpmorrsn.fbp.engine.OutputPort;
import com.jpmorrsn.fbp.engine.Packet;

import org.json.simple.*;
import org.json.simple.parser.*;

@ComponentDescription("Simulates Speaker.")
@OutPorts({ @OutPort(value = "VALUE", arrayPort = true) })
@InPorts({ @InPort(value = "JSON"),@InPort(value = "KEY", arrayPort = true)})
public class JsonParser extends Component {

	static final String copyright = " ";

	private OutputPort[] outportValue;

	private InputPort inportJson;
	private InputPort[] inportKey;

	private String jsonStr;
	private ArrayList<String> keyStr = new ArrayList<String>();

	@Override
	protected void execute() throws InterruptedException {

		Packet ip;		
		
		ip = inportJson.receive();
		jsonStr = (String)ip.getContent();
		drop(ip);
		
		int numberOfKeys = inportKey.length;
		for(int i = 0; i<numberOfKeys; ++i){
			ip = inportKey[i].receive();
			keyStr.add((String)ip.getContent());
			drop(ip);
		}
								
		org.json.simple.parser.JSONParser parser = new org.json.simple.parser.JSONParser();
		ContainerFactory containerFactory = new ContainerFactory(){
		    public java.util.List<Integer> creatArrayContainer() {
		      return new LinkedList();
		    }

		    public Map<String, Integer> createObjectContainer() {
		      return new LinkedHashMap<String, Integer>();
		    }
		                        
		  };
		                
		  try{
		    Map json = (Map)parser.parse(jsonStr, containerFactory);
		    Iterator iter = json.entrySet().iterator();
		    Map.Entry entry = (Map.Entry)iter.next();
		    LinkedList firstEnt = (LinkedList)entry.getValue();
		    Packet out;		    
		    int numberOfValue = outportValue.length;
		    for(int i = 0; i<numberOfValue; ++i){
		    	Object value = ((HashMap)((LinkedList)entry.getValue()).getFirst()).get(keyStr.get(i));
		    	out = create(value.toString());
		    	outportValue[i].send(out);
		    }
		    
		  }
		  catch(ParseException pe){
		    System.out.println(pe);
		  }		
	}

	@Override
	protected void openPorts() {

		inportKey = openInputArray("KEY");
		inportJson = openInput("JSON");
		
		outportValue = openOutputArray("VALUE");

	}

}
