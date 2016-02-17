package miniMap;

import processing.core.PApplet;
import processing.core.PGraphics;
import processing.core.PImage;
import processing.event.MouseEvent;

public class MiniMap extends PApplet {

	PGraphics mainArea;
	PGraphics miniMap;
	PImage background;
	
	int counter=0;
	int width=480;
	int height=640;
	int widthSecondary=(int)(width*.30f);
	int heightSecondary=(int)(height*.30f);
	float zoomFactor=1;
	
	public void settings() {
		size(width, height,P3D);
	}

	public void setup() {
		surface.setResizable(true);
		mainArea = createGraphics(width, height);
		miniMap = createGraphics(widthSecondary, heightSecondary);
		background = loadImage("./assets/tree.jpg");

	}

	public void draw() {
		counter++;
		background(GRAY);

		
		miniMap.beginDraw();
		miniMap.image(background, 0, 0);
		//pg.stroke(255);
		//pg.line(40, 40, mouseX, mouseY);
		miniMap.noFill();
		miniMap.strokeWeight(4);
		miniMap.rectMode(CENTER);
		miniMap.rect(0f+mouseX, 0f+mouseY, widthSecondary, heightSecondary);
		miniMap.endDraw();
		image(miniMap, 0, 0);
		
		mainArea.beginDraw();
		mainArea.background(0);
		//mainArea.scale(2f);
		//mainArea.imageMode(CENTER);
		//mainArea.image(background, -mouseX+(width/2+widthSecondary/2), -mouseY+(height/2+heightSecondary/2));
		mainArea.image(background, 0,0,width*.30f,height*.30f);
		if (counter%60==0){
			System.out.println("mX:"+(-mouseX)+", mY:"+(-mouseY));
		}
		
		//pg2.image(background, 0, 0);
		//pg2.beginCamera();
		//pg2.camera();
		//pg2.rotateX(-PI/6+(mouseX%1000)*0.05f);
		//pg2.rotateY(-PI/6+(mouseY%1000)*0.05f);
		//pg2.endCamera();

		//pg2.translate(50, 50, 0);
		//pg2.rotateY(PI/3);
		//pg2.box(45);
		mainArea.endDraw();

		image(mainArea, 380, 540);

	}
	
	public void mouseWheel(MouseEvent event) {
		
		  zoomFactor -= event.getCount();
		  if (zoomFactor<1f){
			  zoomFactor=1;
		  }

		  println(zoomFactor);
		}

	public static void main(String args[]) {
		PApplet.main(MiniMap.class.getName());
	}

}
