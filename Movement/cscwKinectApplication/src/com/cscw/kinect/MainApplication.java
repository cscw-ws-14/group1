package com.cscw.kinect;

import java.net.SocketException;
public class MainApplication implements OSCeletonDelegate{

	/**
	 * @param args
	 */
	public static void main(String[] args) 
	{
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
