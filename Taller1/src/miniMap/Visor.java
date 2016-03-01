package miniMap;

import processing.core.PApplet;
import processing.core.PConstants;
import processing.core.PGraphics;

public class Visor {
	private int x;
	private int y;
	private int width;
	private int height;
	private PGraphics graphics;
	private PApplet parent;
	private float rotationAngle;
	
	public Visor() {
	}
	
	public Visor(PApplet parent, int x, int y, int width, int height) {
		this.parent = parent;
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		this.graphics = parent.createGraphics(width, height);
	}
	
	public Visor(PApplet parent, PGraphics graphics, int x, int y, int width, int height) {
		this.parent = parent;
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		this.graphics = graphics;
	}
	
	public void draw() {
		graphics.beginDraw();
		// graphics.background(255);
		graphics.rectMode(PConstants.CENTER);
		graphics.noFill();
		graphics.stroke(0);
		graphics.translate(x, y);
		graphics.rotate(rotationAngle);
		graphics.rect(0, 0, width, height);
		graphics.endDraw();
		
		// parent.image(graphics, x, y);
	}
	
	public void rotate(float angle) {
		rotationAngle += angle;
	}
	
	public void decreaseWidthIn(int value) {
		width -= value;
	}
	
	public void decreaseHeightIn(int value) {
		height -= value;
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
	
}
