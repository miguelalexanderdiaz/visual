package processing;

import java.util.ArrayList;
import java.util.List;

import fisica.FBody;
import fisica.FCircle;
import fisica.FPoly;
import fisica.FWorld;
import fisica.Fisica;
import processing.core.PApplet;

public class Taller6 extends PApplet {

	FWorld world;
	FCircle head;
	FPoly torso;
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
		world.remove(world.left);
		world.remove(world.right);
		world.remove(world.top);
		world.setEdgesRestitution(0.5f);

		parts = new ArrayList<>();
	}

	public void draw() {
		background(255);

		world.step();
		world.draw(this);

		for (FBody b : parts) {
			b.draw(this);
		}
		if (torso != null) {
			torso.draw(this);
		}
	}

	public void mousePressed() {
		if (world.getBody(mouseX, mouseY) != null) {
			return;
		}

		createSquare(mouseX, mouseY);
	}

	private void createSquare(float x, float y) {
		torso = new FPoly();
		torso.setStrokeWeight(3);
		// poly.setFill(120, 30, 90);
		torso.setDensity(10);
		torso.setRestitution(0.5f);
		torso.vertex(x, y);
		torso.vertex(x + 20, y);
		torso.vertex(x + 20, y + 20);
		torso.vertex(x, y + 20);
		parts.add(torso);
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
