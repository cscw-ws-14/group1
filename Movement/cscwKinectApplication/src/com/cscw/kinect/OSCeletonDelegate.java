package com.cscw.kinect;

public interface OSCeletonDelegate {

	public void onNewUser(Integer userId);
	
	public void onNewSkeleton(Integer userId);
	
	public void onLostUser(Integer userId);
	
	public void onJoint(String jointName, Integer userId, Float x, Float y, Float z);
	
}
