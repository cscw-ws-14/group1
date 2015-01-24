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
@InPorts({ @InPort(value = "KEY"),
	@InPort(value = "VALUE")})
public class Jsonify extends Component {

	static final String copyright = " ";

	private OutputPort outportMessage;

	private InputPort inportKeys, inportValues;

	private String keys = new String();
	private String[] keyList ;
	private String values = new String();
	private String[] valueList;
	@Override
	protected void execute() throws InterruptedException {

		Packet ip;

		ip = inportKeys.receive();
		keys = ((String)ip.getContent());
		drop(ip);
		keyList = keys.split(";");

		ip = inportValues.receive();
		values = ((String)ip.getContent());
		drop(ip);
		valueList = values.split(";");


		String json = "{";
		int i = 0;
		try{
			for(; i<keyList.length;++i){
				json = json+keyList[i]+":"+valueList[i]+",";
			}
		}catch(Exception e){
			for(; i<keyList.length;++i){
				json = json+keyList[i]+":,";
			}
		}
		json = json.substring(0, json.length()-1)+"}";

		Packet out;
		out = create(json);
		outportMessage.send(out);

	}


	@Override
	protected void openPorts() {

		inportKeys = openInput("KEY");
		inportValues = openInput("VALUE");

		outportMessage = openOutput("MESSAGE");

	}

}