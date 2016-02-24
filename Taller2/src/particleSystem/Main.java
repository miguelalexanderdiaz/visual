package particleSystem;

import java.util.ArrayList;

import processing.core.PApplet;
import processing.core.PImage;
import processing.core.PVector;

public class Main extends PApplet {

	ParticleSystem ps;

	@Override
	public void settings() {
		size(640, 360);
		super.settings();
	}

	@Override
	public void setup() {
		PImage img = loadImage("./assets/texture.png");
		ps = new ParticleSystem(0, new PVector(width / 2, height - 60), img);
	}

	@Override
	public void draw() {
		background(0);

		// Calculate a "wind" force based on mouse horizontal position
		float dx = map(mouseX, 0f, width, -0.2f, 0.2f);
		PVector wind = new PVector(dx, 0);
		ps.applyForce(wind);
		ps.run();
		for (int i = 0; i < 2; i++) {
			ps.addParticle();
		}

		// Draw an arrow representing the wind force
		drawVector(wind, new PVector(width / 2, 50, 0), 500);

	}

	// Renders a vector object 'v' as an arrow and a location 'loc'
	void drawVector(PVector v, PVector loc, float scayl) {
		pushMatrix();
		float arrowsize = 4;
		// Translate to location to render vector
		translate(loc.x, loc.y);
		stroke(255);
		// Call vector heading function to get direction (note that pointing up
		// is a heading of 0) and rotate
		rotate(v.heading());
		// Calculate length of vector & scale it to be bigger or smaller if
		// necessary
		float len = v.mag() * scayl;
		// Draw three lines to make an arrow (draw pointing up since we've
		// rotate to the proper direction)
		popMatrix();
	}

	// A class to describe a group of Particles
	// An ArrayList is used to manage the list of Particles

	class ParticleSystem {

		ArrayList<Particle> particles; // An arraylist for all the particles
		PVector origin; // An origin point for where particles are birthed
		PImage img;

		ParticleSystem(int particleAmount, PVector velocity, PImage particleTexture) {
			particles = new ArrayList<Particle>();
			origin = velocity.get();
			img = particleTexture;
			for (int i = 0; i < particleAmount; i++) {
				particles.add(new Particle(origin, img));
			}
		}

		void run() {
			for (int i = particles.size() - 1; i >= 0; i--) {
				Particle p = particles.get(i);
				p.run();
				if (p.isDead()) {
					particles.remove(i);
				}
			}
		}

		// Method to add a force vector to all particles currently in the system
		void applyForce(PVector dir) {
			// Enhanced loop!!!
			for (Particle p : particles) {
				p.applyForce(dir);
			}

		}

		void addParticle() {
			particles.add(new Particle(origin, img));
		}

	}

	class Particle {
		PVector position;
		PVector velocity;
		PVector acceleration;
		float lifespan;
		PImage texture;

		Particle(PVector position, PImage texture) {
			acceleration = new PVector(0, 0);
			float vx = (float) (randomGaussian() * 0.3);
			float vy = (float) ((randomGaussian() * 0.3) - 1.0);
			velocity = new PVector(vx, vy);
			this.position = position.get();
			lifespan = 100;
			this.texture = texture;
		}

		void run() {
			update();
			render();
		}

		// Method to apply a force vector to the Particle object
		// Note we are ignoring "mass" here
		void applyForce(PVector f) {
			acceleration.add(f);
		}

		void update() {
			velocity.add(acceleration);
			position.add(velocity);
			lifespan -= 0.01;
		}

		void render() {
			// renderTexture();
			renderCircle();
		}

		private void renderTexture() {
			imageMode(CENTER);
			tint(255, lifespan);
			image(texture, position.x, position.y);
		}

		private void renderCircle() {
			fill(255, lifespan);
			noStroke();
			ellipse(position.x, position.y, 5, 5);
		}

		// Is the particle still useful?
		boolean isDead() {
			if (lifespan <= 0.0) {
				return true;
			} else {
				return false;
			}
		}
	}

	public static void main(String args[]) {
		PApplet.main(Main.class.getName());
	}

}
