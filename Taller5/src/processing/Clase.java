package processing;

import controlP5.*;
import processing.core.PApplet;
import processing.core.PImage;
import processing.core.PShape;
import processing.opengl.PShader;
import remixlab.proscene.Scene;

public class Clase extends PApplet {
	private Scene scene;
	PShader shader;
	PImage img;
	float offset;
	ControlP5 cp5;
	private PImage im2;

	@Override
	public void settings() {
		size(640, 480, P3D);
	}

	@Override
	public void setup() {
		scene = new Scene(this);
		shader = loadShader("shader/frag2.glsl", "shader/vertex1.glsl");
		img = loadImage("img/w.jpg");
		cp5 = new ControlP5(this);
		cp5.setAutoDraw(false);
		cp5.addSlider("s").setPosition(20, 20).setSize(200, 20).setRange(0, 100).setValue(1);
		im2 = loadImage("img/w2.jpg");
	}

	@Override
	public void draw() {
		background(0);

		beginShape(QUAD);
		shader(shader);
		shader.set("offset", offset);
		shader.set("A", cp5.getValue("s"));
		offset += 0.1;
		
		

		texture(img);
		img.resize(100, 100);
		//// fill(0,0,0);
		vertex(0, 0, 0, 0);
		//// fill(255,0,0);
		vertex(0, 100, 100, 0);
		//// fill(0,255,0);
		vertex(100, 100, 100, 100);
		//// fill(0,0,255);
		vertex(100, 0, 0, 100);
		endShape();

		resetShader();
		scene.beginScreenDrawing();
		cp5.draw();
		scene.endScreenDrawing();
	}

	public static void main(String args[]) {
		PApplet.main(Clase.class.getName());
	}
}
