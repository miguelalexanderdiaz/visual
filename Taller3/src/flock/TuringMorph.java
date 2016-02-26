package flock;

import java.awt.Color;

import processing.core.PImage;

public class TuringMorph {

	private TuringSystemSolver turingSystemSolver;
	private ImageProcesor imageProcessor;

	public TuringMorph(int size) {
		imageProcessor = new ImageProcesor();
		initSolver(size);
	}

	private void initSolver(final int size) {
		turingSystemSolver = new TuringSystemSolver(imageProcessor, size, size);
	}

	public PImage getSquareTexture(int iterations, float ca, float cb) {
		randomColour();
		return turingSystemSolver.solve(iterations, ca, cb);
	}

	public PImage getSquareTexture(int iterations, float ca, float cb, Color c1, Color c2) {
		imageProcessor.setColors(c1, c2);
		return turingSystemSolver.solve(iterations, ca, cb);
	}

	private void randomColour() {
		float hue = (float) Math.random();
		float sat = (float) Math.random();
		float bri = (float) Math.random() * 0.667f;
		Color min = new Color(Color.HSBtoRGB(hue, sat, bri));

		hue += ((float) Math.random() * 26f) / 180f;
		bri = 0.75f + ((float) Math.random() / 4f);
		Color max = new Color(Color.HSBtoRGB(hue, sat, bri));

		if (Math.random() >= 0.5f) {
			Color temp = min;
			min = max;
			max = temp;
		}

		imageProcessor.setColors(min, max);
	}

}
