package flock;

import processing.core.PApplet;
import processing.core.PImage;
import processing.core.PVector;

public class TestingTexture extends PApplet {
	private PImage particleTexture;
	TuringMorph2 t = new TuringMorph2();
	
	@Override
	public void settings() {
		size(640, 360);
	}
	
	@Override
	public void setup() {
		PVector particlesOrigin = new PVector(width / 2, height - 60);
		t.initComponents();
		t.okButtonActionPerformed(null);
	}
	
	@Override
	public void draw() {
		background(0);
		
		if (t.getVisualizer().getImage2() != null) {
			particleTexture = t.getVisualizer().getImage2();
			image(particleTexture, 0, 0, 640, 360);
		}
	}
	
	public static void main(String args[]) {
		PApplet.main(TestingTexture.class.getName());
	}
	
}
