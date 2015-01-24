package com.cscw.kinect;

import java.util.Hashtable;

import org.mt4j.MTApplication;
import org.mt4j.components.visibleComponents.shapes.MTRectangle;
import org.mt4j.util.MT4jSettings;
import org.mt4j.util.math.Vector3D;


import processing.core.PApplet;
import processing.core.PImage;
import processing.core.PGraphics;
import processing.opengl.PGraphicsOpenGL;

public class StickMan extends MTRectangle {
	int ballSize = 10;
	MTApplication application;
	
	float width = this.getWidthXYGlobal();
	float height = this.getWidthXYGlobal();
	PGraphicsOpenGL pGraphics = new PGraphicsOpenGL();
	Hashtable<Integer, Skeleton> skels = new Hashtable<Integer, Skeleton>();

	public StickMan(PImage texture, PApplet applet) {
		super(texture, applet);

	}
	public StickMan create(MTApplication application,int id) {
		PImage texture = application.loadImage("images" + MTApplication.separator + "calibrationpose.jpg");
		StickMan instance = new StickMan(texture, application);
		instance.application = application;
		instance.setAnchor(PositionAnchor.CENTER);
		Skeleton skeleton = new Skeleton(id);
		skels.put(id, skeleton);
		
		if (MT4jSettings.getInstance().isOpenGlMode()) {
			instance.setUseDirectGL(true);
		}
		
		instance.setPositionGlobal(new Vector3D(application.width/2f + instance.getWidthXYGlobal()/2f + instance.getWidthXYGlobal()/4f, 
				application.height/2f));
		draw();
				
		return instance;
	}

	
	/* incoming osc message are forwarded to the oscEvent method. */
	// Here you can easily see the format of the OSC messages sent. For each user, the joints are named with 
	// the joint named followed by user ID (head0, neck0 .... r_foot0; head1, neck1.....)
	void onJoint(String jointName, Integer userId, Float x, Float y, Float z) {
		Skeleton s = skels.get(userId);
		if (s == null) {
			s = new Skeleton(userId);
			skels.put(userId, s);
		}
		if (jointName.equals("head")) {
			s.headCoords[0] = x;
			s.headCoords[1] = y;
			s.headCoords[2] = z;
		}
		else if (jointName.equals("neck")) {
			s.neckCoords[0] = x;
			s.neckCoords[1] = y;
			s.neckCoords[2] = z;
		}
		else if (jointName.equals("r_collar")) {
			s.rCollarCoords[0] = x;
			s.rCollarCoords[1] = y;
			s.rCollarCoords[2] = z;
		}
		else if (jointName.equals("r_shoulder")) {
			s.rShoulderCoords[0] = x;
			s.rShoulderCoords[1] = y;
			s.rShoulderCoords[2] = z;
		}
		else if (jointName.equals("r_elbow")) {
			s.rElbowCoords[0] = x;
			s.rElbowCoords[1] = y;
			s.rElbowCoords[2] = z;
		}
		else if (jointName.equals("r_wrist")) {
			s.rWristCoords[0] = x;
			s.rWristCoords[1] = y;
			s.rWristCoords[2] = z;
		}
		else if (jointName.equals("r_hand")) {
			s.rHandCoords[0] = x;
			s.rHandCoords[1] = y;
			s.rHandCoords[2] = z;
		}
		else if (jointName.equals("r_finger")) {
			s.rFingerCoords[0] = x;
			s.rFingerCoords[1] = y;
			s.rFingerCoords[2] = z;
		}
		else if (jointName.equals("r_collar")) {
			s.lCollarCoords[0] = x;
			s.lCollarCoords[1] = y;
			s.lCollarCoords[2] = z;
		}  
		else if (jointName.equals("l_shoulder")) {
			s.lShoulderCoords[0] = x;
			s.lShoulderCoords[1] = y;
			s.lShoulderCoords[2] = z;
		}
		else if (jointName.equals("l_elbow")) {
			s.lElbowCoords[0] = x;
			s.lElbowCoords[1] = y;
			s.lElbowCoords[2] = z;
		}
		else if (jointName.equals("l_wrist")) {
			s.lWristCoords[0] = x;
			s.lWristCoords[1] = y;
			s.lWristCoords[2] = z;
		}
		else if (jointName.equals("l_hand")) {
			s.lHandCoords[0] = x;
			s.lHandCoords[1] = y;
			s.lHandCoords[2] = z;
		}
		else if (jointName.equals("l_finger")) {
			s.lFingerCoords[0] = x;
			s.lFingerCoords[1] = y;
			s.lFingerCoords[2] = z;
		}
		else if (jointName.equals("torso")) {
			s.torsoCoords[0] = x;
			s.torsoCoords[1] = y;
			s.torsoCoords[2] = z;
		}
		else if (jointName.equals("r_hip")) {
			s.rHipCoords[0] = x;
			s.rHipCoords[1] = y;
			s.rHipCoords[2] = z;
		} 
		else if (jointName.equals("r_knee")) {
			s.rKneeCoords[0] = x;
			s.rKneeCoords[1] = y;
			s.rKneeCoords[2] = z;
		} 
		else if (jointName.equals("r_ankle")) {
			s.rAnkleCoords[0] = x;
			s.rAnkleCoords[1] = y;
			s.rAnkleCoords[2] = z;
		} 
		else if (jointName.equals("r_foot")) {
			s.rFootCoords[0] = x;
			s.rFootCoords[1] = y;
			s.rFootCoords[2] = z;
		} 
		else if (jointName.equals("l_hip")) {
			s.lHipCoords[0] = x;
			s.lHipCoords[1] = y;
			s.lHipCoords[2] = z;
		} 
		else if (jointName.equals("l_knee")) {
			s.lKneeCoords[0] = x;
			s.lKneeCoords[1] = y;
			s.lKneeCoords[2] = z;
		} 
		else if (jointName.equals("l_ankle")) {
			s.lAnkleCoords[0] = x;
			s.lAnkleCoords[1] = y;
			s.lAnkleCoords[2] = z;
		} 
		else if (jointName.equals("l_foot")) {
			s.lFootCoords[0] = x;
			s.lFootCoords[1] = y;
			s.lFootCoords[2] = z;
		} 
	}



	void drawBone(float joint1[], float joint2[]) {
		if ((joint1[0] == -1 && joint1[1] == -1) || (joint2[0] == -1 && joint2[1] == -1))
			return;

		float dx = (joint2[0] - joint1[0]) * width;
		float dy = (joint2[1] - joint1[1]) * height;
		float steps = (float) (2 * Math.sqrt(Math.pow(dx,2) + Math.pow(dy,2)) / ballSize);
		float step_x = dx / steps / width;
		float step_y = dy / steps / height;

		for (int i=0; i<=steps; i++) {
			pGraphics.ellipse((joint1[0] + (i*step_x))*width, 
					(joint1[1] + (i*step_y))*height, 
					ballSize, ballSize);
		}
	}


	void draw() {
		for (Skeleton s: skels.values()) {
			//Head
			pGraphics.ellipse(s.headCoords[0]*width, 
					s.headCoords[1]*height + 10, 
					ballSize*5, ballSize*6);

			//Head to neck 
			drawBone(s.headCoords, s.neckCoords);
			//Center upper body
			//drawBone(lShoulderCoords, rShoulderCoords);
			drawBone(s.headCoords, s.rShoulderCoords);
			drawBone(s.headCoords, s.lShoulderCoords);
			drawBone(s.neckCoords, s.torsoCoords);
			//Right upper body
			drawBone(s.rShoulderCoords, s.rElbowCoords);
			drawBone(s.rElbowCoords, s.rHandCoords);
			//Left upper body
			drawBone(s.lShoulderCoords, s.lElbowCoords);
			drawBone(s.lElbowCoords, s.lHandCoords);
			//Torso
			//drawBone(rShoulderCoords, rHipCoords);
			//drawBone(lShoulderCoords, lHipCoords);
			drawBone(s.rHipCoords, s.torsoCoords);
			drawBone(s.lHipCoords, s.torsoCoords);
			//drawBone(lHipCoords, rHipCoords);
			//Right leg
			drawBone(s.rHipCoords, s.rKneeCoords);
			drawBone(s.rKneeCoords, s.rFootCoords);
			//  drawBone(rFootCoords, lHipCoords);
			//Left leg
			drawBone(s.lHipCoords, s.lKneeCoords);
			drawBone(s.lKneeCoords, s.lFootCoords);
			//  drawBone(lFootCoords, rHipCoords);

			for (float j[]: s.allCoords) {
				pGraphics.ellipse(j[0]*width, j[1]*height, ballSize*2, ballSize*2);
			}  

		}

	}
}
