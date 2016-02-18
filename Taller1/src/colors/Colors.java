package colors;

import processing.core.PApplet;
import processing.core.PGraphics;
import processing.core.PImage;

public class Colors extends PApplet {
	PGraphics overlayer;
	PImage background;

	int width = 1600;
	int height = 1000;
	int bigSquareSize = 75;
	int smallSquareSize = bigSquareSize / 3;
	float offset = 25;
	int color, selectedColor = 0;
	Boolean freeColor = true;

	@Override
	public void settings() {
		size(width, height);
	}

	@Override
	public void setup() {
		overlayer = createGraphics(width, height);
		background = loadImage("./assets/fractal.jpg");
		rectMode(CENTER);
	}

	@Override
	public void draw() {
		background(background);
		// selector
		color = get(mouseX, mouseY);
		fill(color);
		rect((offset * 2) + mouseX, offset + mouseY, bigSquareSize, bigSquareSize);
		// red component
		fill(red(color), 0, 0);
		rect(offset + bigSquareSize + mouseX, (offset - smallSquareSize) + mouseY, smallSquareSize,
				smallSquareSize);
		// green component
		fill(0, green(color), 0);
		rect(offset + bigSquareSize + mouseX, offset + mouseY, smallSquareSize, smallSquareSize);
		// blue component
		fill(0, 0, blue(color));
		rect(offset + bigSquareSize + mouseX, offset + smallSquareSize + mouseY, smallSquareSize,
				smallSquareSize);

		// drawing zone
		overlayer.beginDraw();
		overlayer.strokeWeight(4);
		if (mousePressed == true) {
			if (freeColor) {
				selectedColor = color;
				freeColor = false;
			}
			overlayer.stroke(selectedColor);
			overlayer.line(mouseX, mouseY, pmouseX, pmouseY);
		}
		overlayer.endDraw();
		image(overlayer, 0, 0);
	}

	@Override
	public void mouseReleased() {
		freeColor = true;
	}

	public static void main(String args[]) {
		PApplet.main(new String[] { "--present", "colors.Colors" });
	}

}
