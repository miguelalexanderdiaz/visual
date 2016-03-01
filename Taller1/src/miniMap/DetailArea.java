package miniMap;

import processing.core.PApplet;
import processing.core.PConstants;
import processing.core.PGraphics;
import processing.core.PImage;

public class DetailArea {
	private int x;
	private int y;
	private int width;
	private int height;
	private PGraphics graphics;
	private PApplet parent;
	private PImage background;
	private float rotationAngle;
	
	public DetailArea() {
	}
	
	public DetailArea(PApplet parent, int x, int y, int width, int height) {
		this.parent = parent;
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		this.graphics = parent.createGraphics(width, height);
	}
	
	public void draw() {
		graphics.beginDraw();
		graphics.background(0);
		graphics.stroke(0);
		graphics.imageMode(PConstants.CENTER);
		graphics.translate(width / 2, height / 2);
		graphics.rotate(rotationAngle);
		graphics.image(getBackground(), 0, 0, width, height);
		graphics.endDraw();
	}
	
	public void show() {
		parent.image(graphics, x, y);
	}
	
	public void rotate(float angle) {
		rotationAngle += angle;
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
