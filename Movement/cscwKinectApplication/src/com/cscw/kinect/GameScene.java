package com.cscw.kinect;

import org.mt4j.MTApplication;
import org.mt4j.components.MTComponent;
import org.mt4j.components.TransformSpace;
import org.mt4j.components.visibleComponents.font.FontManager;
import org.mt4j.components.visibleComponents.font.IFont;
import org.mt4j.components.visibleComponents.shapes.MTRectangle.PositionAnchor;
import org.mt4j.components.visibleComponents.widgets.MTTextArea;
import org.mt4j.sceneManagement.AbstractScene;
import org.mt4j.util.MTColor;
import org.mt4j.util.math.Vector3D;

public class GameScene extends AbstractScene 
{
	Integer score;
	MTTextArea scoreText;
	IFont scoreFont;
	MTComponent jointsLayer;
	StickMan stickMan;

	public GameScene(MTApplication mtApplication, String name) 
	{
		super(mtApplication, name);
		scoreFont = FontManager.getInstance().createFont(mtApplication, "Bauhaus93", 52, new MTColor(0, 0, 0, 255), new MTColor(0, 0, 0, 255));
		
		// clear screen's canvas
		setClearColor(new MTColor(0f, 0f, 0f, 255f));
		
		getCanvas().setDepthBufferDisabled(true);
		jointsLayer = new MTComponent(getMTApplication());
		getCanvas().addChild(jointsLayer);
		
		// Score title
		scoreText = new MTTextArea(mtApplication, scoreFont); 
		scoreText.setAnchor(PositionAnchor.LOWER_LEFT);
		scoreText.setNoFill(true);
		scoreText.setNoStroke(true);
		scoreText.setEnabled(false);
		scoreText.setText("0");
		scoreText.setPositionGlobal(new Vector3D(30f, mtApplication.getHeight() - scoreText.getHeightXY(TransformSpace.LOCAL)/2f));
		getCanvas().addChild(scoreText);

	}

	@Override
	public void init() 
	{
		// TODO Auto-generated method stub

	}

	@Override
	public void shutDown() 
	{
		// TODO Auto-generated method stub

	}
	
	public void addPlayer(Integer userId) {
		MTComponent component = jointsLayer.getChildByName("playerLayer#" + userId);
		if (component != null) {
			return;
		}
		
		MTComponent userLayer = new MTComponent(getMTApplication());
		userLayer.setName("playerLayer#" + userId);
		stickMan.create(getMTApplication(), userId);
		
		/*
		MTEllipse ellipse;
		for(String jointName : OSCeleton.JOINTS) {
			ellipse = new MTEllipse(getMTApplication(), new Vector3D(getMTApplication().getWidth()/2.0f, getMTApplication().getHeight()/2.0f, 0), 10, 10);
			ellipse.setName(jointName);
			ellipse.setFillColor(MTColor.RED);
			userLayer.addChild(ellipse);
		}
		*/

		jointsLayer.addChild(userLayer);
	}
	
	public void removePlayer(Integer userId) {
		
		MTComponent component = jointsLayer.getChildByName("playerLayer#" + userId);
		if (component != null) {
			component.removeFromParent();
			component.destroy();
		}
	}
	
	public void updatePlayerJoint(String jointName, Integer userId, Float x, Float y, Float z) {
		MTComponent playerLayer = jointsLayer.getChildByName("playerLayer#" + userId);
		if (playerLayer == null) {
			addPlayer(userId);
			playerLayer = jointsLayer.getChildByName("playerLayer#" + userId);
		}
		stickMan.onJoint(jointName,userId,x,y,z);
		
		//if (!jointName.equals(OSCeleton.L_HAND) && !jointName.equals(OSCeleton.R_HAND)) {
			//return;
		//}
		
		//MTRectangle joint = (MTRectangle)playerLayer.getChildByName(jointName);
		//Vector3D position = new Vector3D(getMTApplication().getWidth()*x.floatValue(), getMTApplication().getHeight()*y.floatValue());//, 400.0f*z.floatValue());
				
		//if (joint != null) {
		//	joint.setPositionGlobal(position);
			
		//} else {
			
			
			//playerLayer.addChild(joint);

		//}
		
	}

}
