package flock;

import processing.core.PApplet;
import processing.core.PImage;

public class TestingTexture extends PApplet {
	private static final int HEIGHT = 360;
	private static final int WIDTH = 640;
	private PImage particleTexture1;
	private PImage particleTexture2;
	TuringMorph turingMorph;

	@Override
	public void settings() {
		size(WIDTH, HEIGHT);
	}

	@Override
	public void setup() {
		turingMorph = new TuringMorph(100);

		particleTexture1 = getRandomTexture();
		particleTexture2 = getRandomTexture();
	}

	@Override
	public void draw() {
		background(0);

		image(particleTexture1, 0, 0);
		image(particleTexture2, 200, 0);
	}

	public PImage getRandomTexture() {
		float ca = random(0, 10);
		float cb = random(0, 10);

		return turingMorph.getSquareTexture(2000, ca, cb);
	}

	public static void main(String args[]) {
		PApplet.main(TestingTexture.class.getName());
	}

}
