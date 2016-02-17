package solarSystem;

import java.util.ArrayList;

import processing.core.PApplet;
import processing.core.PImage;
import processing.core.PShape;
import remixlab.proscene.*;

public class SolarSystem extends PApplet {

	Scene scene;

	PImage texture;
	ArrayList<PShape> planets = new ArrayList<>();
	ArrayList<PShape> moons = new ArrayList<>();
	String[] textures = { "sun.jpg", "mercury.jpg", "venus.jpg", "earth.jpg",
			"mars.jpg", "jupiter.jpg", "saturn.png", "uranus.jpg",
			"neptune.jpg" };
	float[] sizes = { 15, 2, 4.5f, 5, 3.5f, 11, 8.5f, 9, 9 };
	float[] sizesMoons = { 0.7f, 1.5f, 1.8f, 0.6f, 1.1f, 1.5f, 1.3f, 0.5f };
	float[] distancesMoons = {  5, 6, 7, 5, 16, 10, 13, 13 };
	int detail = 35;
	float[] distances = { 0.00001f, 18, 25, 35, 45, 75, 95, 115, 145 };

	PShape earth;

	int width = 1600;
	int height = 1000;

	int counter = 0;

	public void settings() {
		size(width, height, P3D);
	}

	public void setup() {
		textureMode(NORMAL);
		textureWrap(REPEAT);
		scene = new Scene(this);
		// scene.eyeFrame().setDamping(0);

		for (int i = 0; i < sizes.length; i++) {
			texture = loadImage("./assets/" + textures[i]);
			planets.add(createSphere(sizes[i], 60, texture));
		}

		for (int i = 0; i < sizesMoons.length; i++) {
			texture = loadImage("./assets/" + textures[i]);
			moons.add(createSphere(sizesMoons[i], 60, texture));
		}

	}

	public void draw() {

		counter++;
		background(0);

		for (int i = 0; i < planets.size(); i++) {

			PShape planet = planets.get(i);
			planet.rotate(0.01f);
			
			float x = distances[i] * cos(1.1f * counter / distances[i]);
			float y = distances[i] * sin(1.1f * counter / distances[i]);
			shape(planet, x, y);
			if (i != 0) {
				PShape moon = moons.get(i-1);
				moon.rotate(0.1f);
				float xMoon = x + distancesMoons[i-1]
						* cos(1.1f * counter / distancesMoons[i-1]);
				float yMoon = y + distancesMoons[i-1]
						* sin(1.1f * counter / distancesMoons[i-1]);
				shape(moon, xMoon, yMoon);
			}

		}
	}

	public PShape createSphere(float r, int detail, PImage tex) {

		PShape sh = createShape();

		final float dA = TWO_PI / detail; // change in angle

		// process the sphere one band at a time
		// going from almost south pole to almost north
		// poles must be handled separately
		float theta2 = -PI / 2 + dA;
		float SHIFT = PI / 2;
		float z2 = sin(theta2); // height off equator
		float rxyUpper = cos(theta2); // closer to equator
		for (int i = 1; i < detail; i++) {
			float theta1 = theta2;
			theta2 = theta1 + dA;
			float z1 = z2;
			z2 = sin(theta2);
			float rxyLower = rxyUpper;
			rxyUpper = cos(theta2); // radius in xy plane
			sh.beginShape(QUAD_STRIP);
			sh.noStroke();
			sh.texture(tex);
			for (int j = 0; j <= detail; j++) {
				float phi = j * dA; // longitude in radians
				float xLower = rxyLower * cos(phi);
				float yLower = rxyLower * sin(phi);
				float xUpper = rxyUpper * cos(phi);
				float yUpper = rxyUpper * sin(phi);
				float u = phi / TWO_PI;
				sh.normal(xUpper, yUpper, z2);
				sh.vertex(r * xUpper, r * yUpper, r * z2, u, (theta2 + SHIFT)
						/ PI);
				sh.normal(xLower, yLower, z1);
				sh.vertex(r * xLower, r * yLower, r * z1, u, (theta1 + SHIFT)
						/ PI);
			}
			sh.endShape();
		}
		return sh;
	}

	public static void main(String[] args) {
		PApplet.main(new String[] { "--present", "solarSystem.SolarSystem" });

	}

}
