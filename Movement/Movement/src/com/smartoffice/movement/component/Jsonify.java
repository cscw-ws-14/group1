/*
 * By:
 * Shekoufeh Gorgi Zadeh
 * 
 * 11.01.2015
 * 
 */
package com.smartoffice.movement.component;

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
@InPorts({ @InPort(value = "KEY", arrayPort = true),
	@InPort(value = "VALUE", arrayPort = true)})
public class Jsonify extends Component {

	static final String copyright = " ";

	private OutputPort outportMessage;

	private InputPort inportKeys[], inportValues[];

	private String keys = new String();
	private ArrayList<String> keyList = new ArrayList<String>() ;
	private String values = new String();
	private ArrayList<String> valueList = new ArrayList<String>() ;
	@Override
	protected void execute() throws InterruptedException {

		Packet ip;

		int numberOfKeys = inportKeys.length;
		for(int i = 0;i<numberOfKeys;i++)
		{
			ip = inportKeys[i].receive();
			keyList.add((String)ip.getContent());
			drop(ip);
		}
			
		int numberofValues = inportValues.length;
		for(int i =0;i<numberofValues;i++)
		{
			ip = inportValues[i].receive();
			valueList.add((String)ip.getContent());
			drop(ip);
		}
		
		String json = "{";
		int i = 0;
		try{
			for(; i<keyList.size();++i){
				json = json+keyList.get(i)+":"+valueList.get(i)+",";
			}
		}catch(Exception e){
			for(; i<keyList.size();++i){
				json = json+keyList.get(i)+":,";
			}
		}
		keyList.clear();
		valueList.clear();
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