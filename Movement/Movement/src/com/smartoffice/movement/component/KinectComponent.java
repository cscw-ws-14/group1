package com.smartoffice.movement.component;

import java.net.SocketException;

import com.jpmorrsn.fbp.engine.Component;
import com.jpmorrsn.fbp.engine.ComponentDescription;
import com.jpmorrsn.fbp.engine.InPort;
import com.jpmorrsn.fbp.engine.InPorts;
import com.jpmorrsn.fbp.engine.InputPort;
import com.jpmorrsn.fbp.engine.OutPort;
import com.jpmorrsn.fbp.engine.OutPorts;
import com.jpmorrsn.fbp.engine.Packet;
import com.smartoffice.movement.library.*;


@ComponentDescription("Component for the Kinect Application")

@InPorts({
        @InPort(value = "MESSAGE", description = "message ", type = Boolean.class)
})

@OutPorts({
        @OutPort(value = "YES", optional = true),
   
})
public class KinectComponent extends Component implements OSCeletonDelegate{
	
	private InputPort inPort;
	private final String niViewerPath = "C:/Program Files (x86)/OpenNI/Samples/Bin/Release/NiViewer.exe";
	private final String osceletonPath = "C:/OSCeleton-v1.2.1_win32 (1)/OSCeleton-v1.2.1_win32/OSCeleton.exe";

	@Override
	protected void execute() throws Exception {
		Packet ip;

		ip = inPort.receive();
		Boolean name = (Boolean) ip.getContent();
		Process niProcess = Runtime.getRuntime().exec(niViewerPath);
		System.out.println("NIViewer started");
		Thread.sleep(10000);
		Process oscProcess = Runtime.getRuntime().exec(osceletonPath);
		System.out.println("Osceleton started");
		Thread.sleep(10000);
		drop(ip);
		final Runnable task = new Runnable() {
			@Override
			public void run() {
				try {
					OSCeleton client = new OSCeleton();
					//client.setDelegate(delegate);
					client.run();
					
				} catch (SocketException e) {
					e.printStackTrace();
					System.exit(1);
				}
			}
		};
		task.run();
		
	}

	@Override
	protected void openPorts() {
		inPort = openInput("MESSAGE");
		
	}

	@Override
	public void onNewUser(Integer userId) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onNewSkeleton(Integer userId) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onLostUser(Integer userId) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onJoint(String jointName, Integer userId, Float x, Float y,
			Float z) {
		// TODO Auto-generated method stub
		
	}

}
