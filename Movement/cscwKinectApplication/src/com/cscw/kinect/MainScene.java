package com.cscw.kinect;

import org.mt4j.MTApplication;
import org.mt4j.components.TransformSpace;
import org.mt4j.components.visibleComponents.font.FontManager;
import org.mt4j.components.visibleComponents.font.IFont;
import org.mt4j.components.visibleComponents.shapes.MTRectangle;
import org.mt4j.components.visibleComponents.shapes.MTRectangle.PositionAnchor;
import org.mt4j.components.visibleComponents.widgets.MTTextArea;
import org.mt4j.sceneManagement.AbstractScene;
import org.mt4j.sceneManagement.transition.BlendTransition;
import org.mt4j.sceneManagement.transition.FadeTransition;
import org.mt4j.util.MT4jSettings;
import org.mt4j.util.MTColor;
import org.mt4j.util.math.Vector3D;
import org.mt4j.util.opengl.GLFBO;

import processing.core.PImage;

public class MainScene extends AbstractScene {

	MTTextArea statusText;

	IFont smallfont, largefont;

	public MainScene(MTApplication mtApplication, String name) {
		super(mtApplication, name);
		// clear screen's canvas

		largefont = FontManager.getInstance().createFont(mtApplication, "Bauhaus93", 48, new MTColor(0, 0, 0, 255), new MTColor(0, 0, 0, 255));
		smallfont = FontManager.getInstance().createFont(mtApplication, "Bauhaus93", 32, new MTColor(0, 0, 0, 255), new MTColor(0, 0, 0, 255));

		setClearColor(new MTColor(255f, 255f, 255f, 255f));

		// Title
		MTTextArea titleText = new MTTextArea(mtApplication, null); 
		titleText.setAnchor(PositionAnchor.CENTER);
		titleText.setNoFill(true);
		titleText.setNoStroke(true);
		titleText.setEnabled(false);
		titleText.setText("Perform pose given below to calibrate");
		titleText.setPositionGlobal(new Vector3D(mtApplication.width/2f, titleText.getHeightXY(TransformSpace.LOCAL)));
		getCanvas().addChild(titleText);

		// Status
		statusText = new MTTextArea(mtApplication, null);
		statusText.setAnchor(PositionAnchor.CENTER);
		statusText.setNoFill(true);
		statusText.setNoStroke(true);
		statusText.setEnabled(false);
		getCanvas().addChild(statusText);
		setStatus("Waiting...");

		// Show the hero pose hint
		String folder = "images" + MTApplication.separator;
		PImage texture = mtApplication.loadImage(folder + "calibrationpose.jpg");
		MTRectangle r = new MTRectangle(texture, mtApplication);
		r.setAnchor(PositionAnchor.CENTER);
		Vector3D position = new Vector3D(mtApplication.width/2f, mtApplication.height/2f);
		r.setPositionGlobal(position);
		r.setNoStroke(true);
		r.setEnabled(false);
		getCanvas().addChild(r);

		//Set a scene transition - Flip transition only available using opengl supporting the FBO extenstion
		if (MT4jSettings.getInstance().isOpenGlMode() && GLFBO.isSupported(mtApplication))
			this.setTransition(new BlendTransition(mtApplication, 700));
		else{
			this.setTransition(new FadeTransition(mtApplication, 1700));
		}
	}

	@Override
	public void init() {
		setStatus("Waiting...");
		System.out.println("Init");


	}

	@Override
	public void shutDown() {
		System.out.println("ShutDown");
	}

	public void setStatus(String text) {
		statusText.setText(text);
		statusText.setPositionGlobal(new Vector3D(getMTApplication().width/2f, getMTApplication().height - statusText.getHeightXY(TransformSpace.LOCAL)));
	}

}
