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
		cp5.addSlider("mySlider").setPosition(20, 20).setSize(200, 20);
		cp5.addButton("myButton").setPosition(20, 60).setSize(100, 40);
	}

	@Override
	public void draw() {
		background(0);
		beginShape(QUAD);
		shader(shader);
		shader.set("offset", offset);
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
	}

	public static void main(String args[]) {
		PApplet.main(Clase.class.getName());
	}
}
