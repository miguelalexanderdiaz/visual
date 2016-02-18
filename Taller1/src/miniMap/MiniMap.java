package miniMap;

import processing.core.PApplet;
import processing.core.PGraphics;
import processing.core.PImage;
import processing.event.MouseEvent;

public class MiniMap extends PApplet {

	PGraphics mainArea;
	PGraphics miniMap;
	PImage background;

	int counter = 0;
	int width = 480;
	int height = 640;
	float miniMapScale=.30f;
	int widthMiniMap = (int) (width * miniMapScale);
	int heightMiniMap = (int) (height * miniMapScale);
	float zoomFactor = 1;
	float x,y=0;
	public void settings() {
		size(width, height, P3D);
	}

	public void setup() {
		surface.setResizable(true);
		mainArea = createGraphics(width, height);
		miniMap = createGraphics(widthMiniMap, heightMiniMap);
		background = loadImage("./assets/tree.jpg");
		println(widthMiniMap);
		println(heightMiniMap);
	}

	public void draw() {
		counter++;
		background(255);
		float[] minimapCoord=miniMapCoord();
		float[] mainAreaCoord=mainAreaCoord(minimapCoord[0], minimapCoord[1]);

		mainArea.beginDraw();
		mainArea.imageMode(CENTER);
		mainArea.background(255);
		mainArea.scale(zoomFactor);
		
		println(mainAreaCoord[0]+", "+mainAreaCoord[1]);
		mainArea.image(background, -mainAreaCoord[0]+width,-mainAreaCoord[1]+height);



		//mainArea.image(background, -x, -y);
		if (counter % 60 == 0) {
			//System.out.println("mX:" + (-x) + ", mY:" + (-y));
		}

		// pg2.image(background, 0, 0);
		// pg2.beginCamera();
		// pg2.camera();
		// pg2.rotateX(-PI/6+(mouseX%1000)*0.05f);
		// pg2.rotateY(-PI/6+(mouseY%1000)*0.05f);
		// pg2.endCamera();

		// pg2.translate(50, 50, 0);
		// pg2.rotateY(PI/3);
		// pg2.box(45);
		mainArea.endDraw();
		image(mainArea, 0, 0);

		miniMap.beginDraw();
		// miniMap.image(background, 0, 0);
		// pg.stroke(255);
		// pg.line(40, 40, mouseX, mouseY);
		
		miniMap.image(background, 0, 0, widthMiniMap, heightMiniMap);
		miniMap.stroke(15);
		miniMap.noFill();
		miniMap.strokeWeight(4);
		miniMap.rectMode(CENTER);
		
		miniMap.rect(minimapCoord[0], minimapCoord[1],widthMiniMap/zoomFactor,heightMiniMap/zoomFactor);
		//println(minimapCoord[0]+", "+(minimapCoord[0]+10f)+", "+minimapCoord[1]+", "+(minimapCoord[1]+10f));
		
		miniMap.endDraw();
		image(miniMap, width - widthMiniMap, height - heightMiniMap);

	}

	public void mouseWheel(MouseEvent event) {

		zoomFactor -= event.getCount();
		if (zoomFactor < 1f) {
			zoomFactor = 1;
		}

		println(zoomFactor);
	}
	public float[] miniMapCoord(){
		float x = mouseX;
		float y = mouseY;
		if (x < width - widthMiniMap) {
			x = width - widthMiniMap;
		}
		x=-(width-widthMiniMap-x);
		if (y < height - heightMiniMap) {
			y = height - heightMiniMap;
		}
		y=-(height-heightMiniMap-y);
		float [] answer={x,y};
		return answer;
	}
	
	public float[] mainAreaCoord(float xMinimap, float yMinimap){
		float x,y;
		x=xMinimap*(width/widthMiniMap);
		y=yMinimap*(height/heightMiniMap);
		float [] answer={x,y};
		return answer;
	}

	public static void main(String args[]) {
		PApplet.main(MiniMap.class.getName());
	}

}
