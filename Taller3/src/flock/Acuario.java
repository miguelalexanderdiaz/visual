package flock;

import java.util.ArrayList;

import processing.core.PApplet;
import processing.core.PImage;
import processing.core.PShape;
import processing.core.PVector;
import remixlab.dandelion.core.Camera;
import remixlab.proscene.Scene;

public class Acuario extends PApplet {

	Flock flock1;
	Flock flock2;
	Scene scene;
	PImage particleTexture1;
	PImage particleTexture2;
	PShape shape1;

	int r = 20;
	TuringMorph turingMorph;
	int width = 1200;
	int height = 700;
	int iteration = 0;
	float radius = 1600;

	public void settings() {
		size(width, height, P3D);

	}

	public void setup() {

		scene = new Scene(this);
		Camera cam = new Camera(scene);
		cam.setSceneRadius(radius+300);
		scene.setCamera(cam);
		turingMorph = new TuringMorph(200);
		particleTexture1 = getRandomTexture();
		particleTexture2 = getRandomTexture();
		shape1 = generateTexturedBoid(r, particleTexture1);
		PShape shape2 = generateTexturedBoid(r, particleTexture2);
		
		flock1 = new Flock();
		flock2 = new Flock();

		// Add an initial set of boids into the system
		for (int i = 0; i < 150; i++) {
			flock1.addBoid(new Boid(random(-radius/2, radius/2), random(-radius/2, radius/2), random(-radius/2, radius/2),
					shape1));
		}
		for (int i = 0; i < 300; i++) {
			flock2.addBoid(new Boid(random(-radius/2, radius/2), random(-radius/2, radius/2), random(-radius/2, radius/2),
					shape2));
		}

	}

	public void draw() {

		background(50);
		flock1.run();
		flock2.run();
		image(particleTexture1, 0, 0);
		
		
		noFill();
		stroke(59);
		sphere(radius);

	}

	// Add a new boid into the System
	public void mousePressed() {
		flock1.addBoid(new Boid(0, 0, 0, shape1));
	}

	// The Flock (a list of Boid objects)

	class Flock {
		ArrayList<Boid> boids; // An ArrayList for all the boids

		Flock() {
			boids = new ArrayList<Boid>(); // Initialize the ArrayList
		}

		void run() {
			for (Boid b : boids) {
				b.run(boids); // Passing the entire list of boids to each boid
								// individually
			}
		}

		void addBoid(Boid b) {
			boids.add(b);
		}

	}

	// The Boid class

	class Boid {

		PVector location;
		PVector velocity;
		PVector acceleration;
		PShape shape;

		float r;
		float maxforce; // Maximum steering force
		float maxspeed; // Maximum speed

		Boid(float x, float y, float z, PShape shape) {
			acceleration = new PVector(0, 0, 0);

			// This is a new PVector method not yet implemented in JS
			// velocity = PVector.random2D();

			// Leaving the code temporarily this way so that this example runs
			// in JS
			float rho = random(TWO_PI);
			float phi = random(TWO_PI);
			float theta = random(TWO_PI);

			velocity = new PVector(rho * cos(theta) * sin(phi), rho
					* sin(theta) * sin(phi), rho * cos(phi));
			location = new PVector(x, y, z);
			r = 20f;
			maxspeed = 6;
			maxforce = 0.5f;
			this.shape = shape;

		}

		void run(ArrayList<Boid> boids) {
			flock(boids);
			update();
			borders();
			render();
		}

		void applyForce(PVector force) {
			// We could add mass here if we want A = F / M
			acceleration.add(force);
		}

		// We accumulate a new acceleration each time based on three rules
		void flock(ArrayList<Boid> boids) {
			PVector sep = separate(boids); // Separation
			PVector ali = align(boids); // Alignment
			PVector coh = cohesion(boids); // Cohesion
			// Arbitrarily weight these forces
			sep.mult(1.5f);
			ali.mult(1.0f);
			coh.mult(1.0f);
			// Add the force vectors to acceleration
			applyForce(sep);
			applyForce(ali);
			applyForce(coh);
		}

		// Method to update location
		void update() {
			// Update velocity
			velocity.add(acceleration);
			// Limit speed
			velocity.limit(maxspeed);
			location.add(velocity);
			// Reset accelertion to 0 each cycle
			acceleration.mult(0);
		}

		// A method that calculates and applies a steering force towards a
		// target
		// STEER = DESIRED MINUS VELOCITY
		PVector seek(PVector target) {
			PVector desired = PVector.sub(target, location); // A vector
																// pointing from
																// the location
																// to the target
			// Scale to maximum speed
			desired.normalize();
			desired.mult(maxspeed);

			// Above two lines of code below could be condensed with new PVector
			// setMag() method
			// Not using this method until Processing.js catches up
			// desired.setMag(maxspeed);

			// Steering = Desired minus Velocity
			PVector steer = PVector.sub(desired, velocity);
			steer.limit(maxforce); // Limit to maximum steering force
			return steer;
		}

		void render() {
			// Draw a triangle rotated in the direction of velocity
			float velX=velocity.x*0.5f;
			float velY=velocity.y*0.5f;
			float velZ=velocity.z*0.5f;
			/*
			float rho = sqrt(pow(velocity.x, 2) + pow(velocity.y, 2)+ pow(velocity.z,2));
			float phi = atan(sqrt(pow(velocity.x, 2) + pow(velocity.y, 2))/velocity.z);
			float theta = atan(velocity.y / velocity.x);
			*/
			float rho = sqrt(pow(velX, 2) + pow(velY, 2)+ pow(velZ,2));
			float phi = atan(sqrt(pow(velX, 2) + pow(velY, 2))/velZ);
			float theta = atan(velY / velX);

			pushMatrix();
			translate(location.x, location.y, location.z);
			rotateX(rho * cos(theta) * sin(phi));
			rotateY(rho * sin(theta) * sin(phi));
			rotateZ(rho * cos(phi));
			shape(shape);
			popMatrix();
		}

		// Wraparound
		void borders() {
			
			float magnitude=(radius-sqrt(pow(location.x,2)+pow(location.y,2)+pow(location.z,2)))*1.8f;		
			/*
			velocity.x-=location.x/(pow(magnitude,2));
			velocity.y-=location.y/(pow(magnitude,2));
			velocity.z-=location.z/(pow(magnitude,2));
			*/
			
			acceleration.sub(new PVector(location.x/(pow(magnitude,2)),location.y/(pow(magnitude,2)),location.z/(pow(magnitude,2))));
			

			
		
		}

		// Separation
		// Method checks for nearby boids and steers away
		PVector separate(ArrayList<Boid> boids) {
			float desiredseparation = 25.0f;
			PVector steer = new PVector(0, 0, 0);
			int count = 0;
			// For every boid in the system, check if it's too close
			for (Boid other : boids) {
				float d = PVector.dist(location, other.location);
				// If the distance is greater than 0 and less than an arbitrary
				// amount (0 when you are yourself)
				if ((d > 0) && (d < desiredseparation)) {
					// Calculate vector pointing away from neighbor
					PVector diff = PVector.sub(location, other.location);
					diff.normalize();
					diff.div(d); // Weight by distance
					steer.add(diff);
					count++; // Keep track of how many
				}
			}
			// Average -- divide by how many
			if (count > 0) {
				steer.div((float) count);
			}

			// As long as the vector is greater than 0
			if (steer.mag() > 0) {
				// First two lines of code below could be condensed with new
				// PVector setMag() method
				// Not using this method until Processing.js catches up
				// steer.setMag(maxspeed);

				// Implement Reynolds: Steering = Desired - Velocity
				steer.normalize();
				steer.mult(maxspeed);
				steer.sub(velocity);
				steer.limit(maxforce);
			}
			return steer;
		}

		// Alignment
		// For every nearby boid in the system, calculate the average velocity
		PVector align(ArrayList<Boid> boids) {
			float neighbordist = 70;
			PVector sum = new PVector(0, 0, 0);
			int count = 0;
			for (Boid other : boids) {
				float d = PVector.dist(location, other.location);
				if ((d > 0) && (d < neighbordist)) {
					sum.add(other.velocity);
					count++;
				}
			}
			if (count > 0) {
				sum.div((float) count);
				// First two lines of code below could be condensed with new
				// PVector setMag() method
				// Not using this method until Processing.js catches up
				// sum.setMag(maxspeed);

				// Implement Reynolds: Steering = Desired - Velocity
				sum.normalize();
				sum.mult(maxspeed);
				PVector steer = PVector.sub(sum, velocity);
				steer.limit(maxforce);
				return steer;
			} else {
				return new PVector(0, 0, 0);
			}
		}

		// Cohesion
		// For the average location (i.e. center) of all nearby boids, calculate
		// steering vector towards that location
		PVector cohesion(ArrayList<Boid> boids) {
			float neighbordist = 5;
			PVector sum = new PVector(0, 0, 0); // Start with empty vector to
												// accumulate all locations
			int count = 0;
			for (Boid other : boids) {
				float d = PVector.dist(location, other.location);
				if ((d > 0) && (d < neighbordist)) {
					sum.add(other.location); // Add location
					count++;
				}
			}
			if (count > 0) {
				sum.div(count);
				return seek(sum); // Steer towards the location
			} else {
				return new PVector(0, 0, 0);
			}
		}
	}

	public PImage getRandomTexture() {
		float ca = random(0, 10);
		float cb = random(0, 10);

		return turingMorph.getSquareTexture(2000, ca, cb);
	}

	public PShape generateTexturedBoid(float r, PImage texture) {
		PShape sh = createShape();
		
		 sh.beginShape(TRIANGLES); sh.texture(texture); sh.vertex(0, -r *
		 2,0,0); sh.vertex(-r, r * 2,texture.width,0); sh.vertex(r, r *
		 2,texture.width/2,texture.height);
		  
		 sh.endShape(CLOSE);
	
		/*
		sh.beginShape(TRIANGLE_STRIP);
		sh.noStroke();
		sh.texture(texture);
		sh.vertex(2 * r, 3.5f * r, 0, texture.height / 2);
		sh.vertex(3 * r, 4.5f * r, texture.width / 4, 0);
		sh.vertex(3 * r, 2.5f * r, texture.width / 4, texture.height);
		sh.vertex(5 * r, 4 * r, 0, texture.width / 2, 0);
		sh.vertex(5 * r, 3 * r, texture.width / 2, texture.height);
		sh.vertex(5.5f * r, 3.5f * r, 3 * texture.width / 4, texture.height / 2);
		sh.vertex(7 * r, 4.5f * r, texture.width, 0);
		sh.vertex(7 * r, 2.5f * r, texture.width, texture.height);
		sh.endShape(CLOSE);
		*/
		return sh;

	}

	public static void main(String args[]) {
		PApplet.main(Acuario.class.getName());
	}
}