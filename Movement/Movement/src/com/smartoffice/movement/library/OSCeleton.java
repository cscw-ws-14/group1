package com.smartoffice.movement.library;

import java.net.SocketException;
import java.util.Date;
import com.illposed.osc.OSCListener;
import com.illposed.osc.OSCMessage;
import com.illposed.osc.OSCPortIn;

public class OSCeleton {
	
	public final static String HEAD = "head";
	public final static String NECK = "neck";
	public final static String TORSO = "torso";
	public final static String R_SHOULDER = "r_shoulder";
	public final static String R_ELBOW = "r_elbow";
	public final static String R_HAND = "r_hand"; 
	public final static String L_SHOULDER = "l_shoulder";
	public final static String L_ELBOW = "l_elbow";
	public final static String L_HAND = "l_hand";
	public final static String R_HIP = "r_hip";
	public final static String R_KNEE = "r_knee";
	public final static String R_ANKLE = "r_ankle";
	public final static String R_FOOT = "r_foot";
	public final static String L_HIP = "l_hip";
	public final static String L_KNEE = "l_knee";
	public final static String L_ANKLE = "l_ankle";
	public final static String L_FOOT = "l_foot";
	
	public final static String[] JOINTS = {HEAD, NECK, TORSO, R_SHOULDER, R_ELBOW, R_HAND, L_SHOULDER, L_ELBOW, L_HAND, R_HIP, R_KNEE, R_ANKLE, R_FOOT, L_HIP, L_KNEE, L_ANKLE, L_FOOT};
		
	private OSCPortIn receiver;
	private OSCeletonDelegate delegate;

	public OSCeleton() throws SocketException {
		receiver = new OSCPortIn(7110);
		receiver.addListener("/new_user", new OSCListener() {
			@Override
			public void acceptMessage(Date date, OSCMessage msg) {
				Object[] args = msg.getArguments();
				getDelegate().onNewUser((Integer)args[0]);
			}
		});
		receiver.addListener("/new_skel", new OSCListener() {
			@Override
			public void acceptMessage(Date date, OSCMessage msg) {
				Object[] args = msg.getArguments();
				getDelegate().onNewSkeleton((Integer)args[0]);
			}
		});
		receiver.addListener("/lost_user", new OSCListener() {
			@Override
			public void acceptMessage(Date date, OSCMessage msg) {
				Object[] args = msg.getArguments();
				getDelegate().onLostUser((Integer)args[0]);
			}
		});
		receiver.addListener("/joint", new OSCListener() {
			@Override
			public void acceptMessage(Date date, OSCMessage msg) {
				Object[] args = msg.getArguments();
				getDelegate().onJoint(args[0].toString(), (Integer)args[1], (Float)args[2], (Float)args[3], (Float)args[4]);
			}
		});
	}
	
	public OSCeletonDelegate getDelegate() {
		if (this.delegate != null) {
			return this.delegate;
		}
		return new OSCeletonDelegate() {
			@Override
			public void onNewUser(Integer userId) {
				System.out.println("default onNewUser() invoked");
			}

			@Override
			public void onNewSkeleton(Integer userId) {
				System.out.println("default onNewSkeleton() invoked");
			}

			@Override
			public void onLostUser(Integer userId) {
				System.out.println("default onLostUser() invoked");
			}

			@Override
			public void onJoint(String jointName, Integer userId, Float x, Float y, Float z) {
				System.out.println("default onJoint() invoked");
			}
		};
	}
	
	public void setDelegate(OSCeletonDelegate delegate) {
		this.delegate = delegate;
	}
	
	public void run() {
		System.out.println("Starting...");
		receiver.startListening();
	}
	
	public void stop() {
		receiver.stopListening();
	}
	
}
