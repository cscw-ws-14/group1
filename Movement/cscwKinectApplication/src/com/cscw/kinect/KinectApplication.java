package com.cscw.kinect;

import java.net.SocketException;

//import org.mt4j.MTApplication;
//import org.mt4j.sceneManagement.Iscene;

public class KinectApplication extends MTApplication implements OSCeletonDelegate{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	MainScene mainScene;
	GameScene gameScene;

	private Integer calibratedUserId = 0;

	public KinectApplication() 
	{
		super();
	}

	public static void main()
	{
		initialize();
	}
	@Override
	public void startUp() 
	{
		mainScene = new MainScene(this, "Welcome to your 2min exercise");
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

	public OSCeletonDelegate getOSCDelegate() 
	{
		return this;
	}

	public Integer getCalibratedUserId() 
	{
		return calibratedUserId;
	}

	@Override
	public void onNewUser(Integer userId) 
	{
	

	}

	@Override
	public void onNewSkeleton(final Integer userId) 
	{
		

	}

	@Override
	public void onLostUser(Integer userId) 
	{
		
	}

	@Override
	public void onJoint(final String jointName, final Integer userId, final Float x, final Float y,
			final Float z) 
	{
		
	}

}


