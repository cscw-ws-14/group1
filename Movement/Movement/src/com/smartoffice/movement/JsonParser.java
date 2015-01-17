/*
 * By:
 * Shekoufeh Gorgi Zadeh
 * 
 * 10.12.2014
 * 
 */
package com.smartoffice.movement;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.Map;
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
		   
//		  Map json = null;
//		  Iterator iter = null;
//		  Map.Entry entry = null;
//		  LinkedList firstEnt = null;
//		  
		String jstr;  
		Packet out;
		for(int i = 0, j = 0;i<numberOfKeys;++i){
//			System.out.println(jsonStr);
			jstr = jsonStr;
			int indexOfEvent = jstr.indexOf("\""+keyStr.get(i)+"\"");
			jstr = jstr.substring(indexOfEvent, jstr.length());
			int ketIndex = jstr.indexOf("}");
			int kommaIndex = jstr.indexOf(",");
			if(ketIndex < kommaIndex && ketIndex!=-1)
				jstr = jstr.substring(0, ketIndex);
			else if(kommaIndex<ketIndex && kommaIndex!=-1)
				jstr = jstr.substring(0, kommaIndex);
			jstr = jstr.replaceAll("\\{", "").replaceAll("\\}", "").replaceAll("\\ ", "");
			StringTokenizer jsonTokenizer = new StringTokenizer(jstr, "':");
			jsonTokenizer.nextToken();
			String result = jsonTokenizer.nextToken();
			out = create(result);
			outportValue[j].send(out);
			j++;
			
		}
		  
//		  try{
//		    json = (Map)parser.parse(jsonStr, containerFactory);
//		    iter = json.entrySet().iterator();
//		    entry = (Map.Entry)iter.next();
//		    firstEnt = (LinkedList)entry.getValue();
//		    Packet out;		    
//		    int numberOfValue = outportValue.length;
//		    for(int i = 0; i<numberOfValue; ++i){
//		    	Object value = ((HashMap)((LinkedList)entry.getValue()).getFirst()).get(keyStr.get(i));
//		    	out = create(value.toString());
//		    	outportValue[i].send(out);
//		    }
//		    
//		  }
//		  catch(Exception pe){
////			jsonStr = jsonStr.replaceAll("\\{", "").replaceAll("\\}", "").replaceAll("\\ ", "");
//			int indexOfEvent = jsonStr.indexOf("\"v\"");
//			jsonStr = jsonStr.substring(indexOfEvent, jsonStr.length());
//			int ketIndex = jsonStr.indexOf("}");
//			int kommaIndex = jsonStr.indexOf(",");
//			if(ketIndex < kommaIndex && ketIndex!=-1)
//				jsonStr = jsonStr.substring(0, ketIndex);
//			else if(kommaIndex<ketIndex && kommaIndex!=-1)
//				jsonStr = jsonStr.substring(0, kommaIndex);
//			jsonStr = jsonStr.replaceAll("\\{", "").replaceAll("\\}", "").replaceAll("\\ ", "");
//			StringTokenizer jsonTokenizer = new StringTokenizer(jsonStr, "':");
//			jsonTokenizer.nextToken();
//			String result = jsonTokenizer.nextToken();
//			int j = 0;
//			Packet out;
//			String value;
//			while(jsonTokenizer.hasMoreTokens()){
//				String element = jsonTokenizer.nextToken();
//				for(int i = 0;i<numberOfKeys;++i){
//					if(element.equals(keyStr.get(i))){
//						value =jsonTokenizer.nextToken();
//						System.out.println(value);
//						out = create(value);
//						outportValue[j++].send(out);
//						break;
//					}
//				}
//				
//			}
//		    System.out.println(pe);
		  	
	}

	@Override
	protected void openPorts() {

		inportKey = openInputArray("KEY");
		inportJson = openInput("JSON");
		
		outportValue = openOutputArray("VALUE");

	}

}
