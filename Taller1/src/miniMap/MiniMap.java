package miniMap;

import processing.core.PApplet;
import processing.core.PGraphics;
import processing.core.PImage;

public class MiniMap {
	private int x;
	private int y;
	private int width;
	private int height;
	private PGraphics graphics;
	private PApplet parent;
	private PImage background;

	public MiniMap() {
	}

	public MiniMap(PApplet parent, int x, int y, int width, int height) {
		this.parent = parent;
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		this.graphics = parent.createGraphics(width, height);
	}

	public void draw() {
		graphics.beginDraw();
		graphics.background(255);
		graphics.stroke(0);
		graphics.image(getBackground(), 0, 0, width, height);
		graphics.endDraw();
	}

	public void show() {
		parent.image(graphics, x, y);
	}

	public int getX() {
		return x;
	}

	public void setX(int x) {
		this.x = x;
	}

	public int getY() {
		return y;
	}

	public void setY(int y) {
		this.y = y;
	}

	public int getWidth() {
		return width;
	}

	public void setWidth(int width) {
		this.width = width;
	}

	public int getHeight() {
		return height;
	}

	public void setHeight(int height) {
		this.height = height;
	}

	public PGraphics getGraphics() {
		return graphics;
	}

	public PImage getBackground() {
		return background;
	}

	public void setBackground(PImage background) {
		this.background = background;
	}

}
