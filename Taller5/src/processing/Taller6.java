package processing;

import java.util.ArrayList;
import java.util.List;

import fisica.FBody;
import fisica.FBox;
import fisica.FCircle;
import fisica.FDistanceJoint;
import fisica.FPoly;
import fisica.FWorld;
import fisica.Fisica;
import processing.core.PApplet;
import remixlab.dandelion.geom.Point;

public class Taller6 extends PApplet {

	FWorld world;
	FCircle head;
	FPoly leftLeg;
	FPoly rigthLeg;
	List<FBody> parts;

	@Override
	public void settings() {
		size(400, 400);
	}
	// http://www.jangaroo.net/files/examples/flash/box2d/

	public void setup() {
		smooth();

		Fisica.init(this);
		world = new FWorld();
		world.setGravity(0, 500);
		world.setEdges();
		// world.remove(world.left);
		// world.remove(world.right);
		world.remove(world.top);
		world.setEdgesRestitution(0.5f);

		FBox b = createBox(110, height - 20, 200, 10);
		FBox b2 = createBox(100, height - 30, 180, 10);
		b.setStatic(true);
		b2.setStatic(true);
		world.add(b);
		world.add(b2);
		createBody(100, 100);
		createBody(200, 100);
	}

	private void createBody(float Ox, float Oy) {
		FCircle head = createCircle(Ox, Oy, 20);
		world.add(head);
		FBody torso = createPart(head, Ox, Oy, 40, 60, 0, -30, 0, 10);
		FBody arm1 = createPart(torso, Ox, Oy, 10, 40, 5, -20, -20, -30);
		FBody arm2 = createPart(torso, Ox, Oy, 10, 40, -5, -20, 20, -30);
		FBody leg1 = createPart(torso, Ox, Oy, 10, 40, 0, -20, 10, 30);
		FBody leg2 = createPart(torso, Ox, Oy, 10, 40, 0, -20, -10, 30);
		createPart(arm1, Ox, Oy, 10, 20, 0, -10, 0, 20);
		createPart(arm2, Ox, Oy, 10, 20, 0, 10, 0, 20);
		createPart(leg1, Ox, Oy, 10, 20, 0, -10, 0, 20);
		createPart(leg2, Ox, Oy, 10, 20, 0, -10, 0, 20);
	}

	public FBody createPart(FBody body, float x, float y, float width, float height, float ax1, float ay1, float ax2,
			float ay2) {
		FBody part = createBox(x, y, width, height);
		FDistanceJoint joint = new FDistanceJoint(part, body);
		joint.setAnchor1(ax1, ay1);
		joint.setAnchor2(ax2, ay2);
		world.add(part);
		world.add(joint);
		return part;
	}

	public void draw() {
		background(255);
		world.step();
		world.draw(this);
	}

	public void mousePressed() {
		if (world.getBody(mouseX, mouseY) != null) {
			return;
		}

		// createSquare(mouseX, mouseY);
	}

	private FBox createBox(float x, float y, float width, float height) {
		FBox t = new FBox(width, height);
		t.setStrokeWeight(3);
		// poly.setFill(120, 30, 90);
		t.setDensity(10);
		t.setRestitution(0.5f);
		t.setPosition(x, y);
		return t;
	}

	private FCircle createCircle(float x, float y, float r) {
		FCircle t = new FCircle(r);
		t.setStrokeWeight(3);
		// poly.setFill(120, 30, 90);
		t.setDensity(10);
		t.setRestitution(0.5f);
		t.setPosition(x, y);
		return t;
	}

	public void keyPressed() {
		if (key == BACKSPACE) {
			FBody hovered = world.getBody(mouseX, mouseY);
			if (hovered != null && hovered.isStatic() == false) {
				world.remove(hovered);
			}
		} else {
			try {
				saveFrame("screenshot.png");
			} catch (Exception e) {
			}
		}
	}

	public static void main(String args[]) {
		PApplet.main(Taller6.class.getName());
	}
}
