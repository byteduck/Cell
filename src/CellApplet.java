import java.awt.Component;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Scanner;

import javax.swing.JDesktopPane;
import javax.swing.JEditorPane;
import javax.swing.JFrame;
import javax.swing.JInternalFrame;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.event.HyperlinkEvent;
import javax.swing.event.HyperlinkListener;
import javax.swing.text.DefaultCaret;

import processing.core.PApplet;
import processing.core.PImage;
import saito.objloader.OBJModel;

public class CellApplet extends PApplet {
	String dataPath = "";
	LoaderData loader;
	PImage[] images = new PImage[90];
	float imgTimer = 0f;
	int currentImage = 0;
	float deltaTime = 0f;
	long lastMillis = 0;
	float rot = 0f;
	boolean doneLoading = false;
	OBJModel dna;

	public void setup() {
		size(960, 540, OPENGL);
		dataPath = dataPath("").replace("\\", "/");
		loader = new LoaderData(images,dataPath,this);
		Thread t = new Thread(new LoadingThread(loader));
		t.setName("Image Loading Thread");
		t.start();
		dna = new OBJModel(this, dataPath+"/dna.obj");
		dna.scale(10);
		noStroke();
		dna.translateToCenter();
	}

	public void draw() {
		updateTime();
		clear();
		translate(960/2,540/2,0);
		if (loader.currentFile < 89 || !doneLoading) {
			drawdna();
			rot+=deltaTime;
			rot%=360f;
			int percent = (int) (((float) loader.currentFile / 89f) * 100f);
			if(keyPressed && percent >= 100){
				doneLoading = true;
			}
			textSize(20);
			textAlign(CENTER);
			text("Loading... " + percent + "%", 0, 0);
			if (percent < 25) {
				text("Replicating Chromosomes and Organelles...", 0, 30);
			} else if (percent < 50) {
				text("Splitting Chromosomes...", 0, 30);
			} else if (percent < 75) {
				text("Splitting Cell...", 0, 30);
			} else if (percent < 100) {
				text("Re-forming Nuclear Membrane...", 0, 30);
			} else if(percent >= 100){
				text("Done loading. Press any key to continue.", 0, 30);
			}
		} else {
			image(images[currentImage], -width/2, -height/2, width, height);
			imgTimer += deltaTime;
			if (imgTimer > 1f / 30f && keyPressed) {
				if (keyCode == LEFT) {
					currentImage++;
				} else if (keyCode == RIGHT) {
					currentImage--;
				}
			}
			imgTimer %= 1f / 30f;
			if (currentImage <= -1) {
				currentImage = 89;
			}
			currentImage %= 90;
		}
	}
	
	void drawdna(){
		pushMatrix();
		pushStyle();
		translate(-width/2,0,-100f);
		rotate(rot,0,1,0);
		dna.draw();
		rotate(-rot,0,1,0);
		translate(width/2,0,100f);
		translate(width/2,0,-100f);
		rotate(rot,0,1,0);
		dna.draw();
		rotate(-rot,0,1,0);
		translate(-width/2,0,100f);
		popStyle();
		popMatrix();
	}

	void updateTime() {
		deltaTime = (float) (millis() - lastMillis) / 1000f;
		lastMillis = millis();
	}
}