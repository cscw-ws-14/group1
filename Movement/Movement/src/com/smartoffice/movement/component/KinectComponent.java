package com.smartoffice.movement.component;

import java.net.SocketException;

import com.jpmorrsn.fbp.engine.Component;
import com.jpmorrsn.fbp.engine.ComponentDescription;
import com.jpmorrsn.fbp.engine.InPort;
import com.jpmorrsn.fbp.engine.InPorts;
import com.jpmorrsn.fbp.engine.InputPort;
import com.jpmorrsn.fbp.engine.OutPort;
import com.jpmorrsn.fbp.engine.OutPorts;
import com.jpmorrsn.fbp.engine.OutputPort;
import com.jpmorrsn.fbp.engine.Packet;
import com.smartoffice.movement.library.OSCeleton;
import com.smartoffice.movement.library.OSCeletonDelegate;
//import com.smartoffice.movement.library.*;


@ComponentDescription("Component for the Kinect Application")

@InPorts({
	@InPort(value = "MESSAGE", description = "message ", type = Boolean.class)
})

@OutPorts({
	@OutPort(value = "VALUE", arrayPort = true),

})
public class KinectComponent extends Component implements OSCeletonDelegate {

	private InputPort inPort;
	private OutputPort outPort[];
	private final String niViewerPath = "C:/Program Files (x86)/OpenNI/Samples/Bin/Release/NiViewer.exe";
	private final String osceletonPath = "C:/OSCeleton-v1.2.1_win32 (1)/OSCeleton-v1.2.1_win32/OSCeleton.exe";

	int calibratedUserId = 0;

	@Override
	protected void execute() throws Exception {
		Packet ip;

		ip = inPort.receive();
		Boolean name = (Boolean) ip.getContent();
		Process niProcess = Runtime.getRuntime().exec(niViewerPath);
		System.out.println("NIViewer started");
		//Thread.sleep(10000);
		Process oscProcess = Runtime.getRuntime().exec(osceletonPath);
		System.out.println("Osceleton started");
		//Thread.sleep(10000);
		drop(ip);
		final Runnable task = new Runnable() {
			@Override
			public void run() {
				try {
					OSCeleton client = new OSCeleton();
					client.setDelegate(getOSCDelegate());
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
		outPort = openOutputArray("VALUE");

	}

	public OSCeletonDelegate getOSCDelegate() 
	{
		return this;
	}

	@Override
	public void onNewUser(Integer userId) {
		Runnable newUser = new Runnable() {

			@Override
			public void run() {
				if(calibratedUserId !=0)
				{
					return;
				}
				System.out.println("Wait for User Calibration...");

			}
		};
		newUser.run();

	}


	@Override
	public void onNewSkeleton(final Integer userId) {
		Runnable newSkel = new Runnable() {
			
			@Override
			public void run() {
				if(calibratedUserId == 0)
				{
					System.out.println("Stand in calibration pose...");
					calibratedUserId  = userId;
				}
				
			}
		};
		newSkel.run();

	}

	@Override
	public void onLostUser(Integer userId) {
		System.out.println("Lost User...");

	}

	@Override
	public void onJoint(String jointName, Integer userId, Float x, Float y,
			Float z) 
	{
		if((!jointName.equals("l_hand"))&&(!jointName.equals("r_hand")))
		{
			return;
		}
		String joint = jointName;
		Packet op = create("\""+ joint +"\"");
		outPort[0].send(op);
		String xVal = x.toString();
		Packet opx = create(xVal);
		outPort[1].send(opx);
		String yVal = y.toString();
		Packet opy = create(yVal);
		outPort[2].send(opy);
		String zVal = z.toString();
		Packet opz = create(zVal);
		outPort[3].send(opz);
		

	}

}
