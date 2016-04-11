package processing;

import controlP5.ControlP5;
import processing.core.PApplet;
import processing.core.PImage;
import processing.opengl.PShader;
import remixlab.proscene.Scene;

public class Clase2 extends PApplet {
	private Scene scene;
	PShader shader;
	PImage img;
	float offset;
	ControlP5 cp5;

	@Override
	public void settings() {
		size(640, 480, P3D);
	}

	public void setup() {
		scene = new Scene(this);

		cp5 = new ControlP5(this);
		cp5.setAutoDraw(false);

		cp5.addSlider("sliderValue").setPosition(10, 10).setRange(0, 255);
	}

	public void draw() {
		background(cp5.getValue("sliderValue"));

		scene.beginScreenDrawing();
		cp5.draw();
		scene.endScreenDrawing();
	}

	public static void main(String args[]) {
		PApplet.main(Clase2.class.getName());
	}
}
