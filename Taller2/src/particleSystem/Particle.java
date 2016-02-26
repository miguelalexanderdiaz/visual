package particleSystem;

import processing.core.PApplet;
import processing.core.PConstants;
import processing.core.PImage;
import processing.core.PVector;

public class Particle {
	private PVector position;
	private PVector velocity;
	private PVector acceleration;
	private float lifespan;
	private PImage texture;
	private PApplet view;

	public Particle(PApplet view, PVector position, PVector velocity, PVector acceleration,
			PImage texture) {
		this.view = view;
		this.position = position;
		this.velocity = velocity;
		this.acceleration = acceleration;
		this.texture = texture;
		lifespan = 100;
	}

	void run() {
		update();
		render();
	}

	void applyForce(PVector f) {
		acceleration.add(f);
	}

	void update() {
		velocity.add(acceleration);
		position.add(velocity);
		lifespan -= 0.01;
	}

	void render() {
		renderTexture();
		// renderCircle();
	}

	private void renderTexture() {
		view.imageMode(PConstants.CENTER);
		view.tint(255, lifespan);
		view.image(texture, position.x, position.y);
	}

	@SuppressWarnings("unused")
	private void renderCircle() {
		view.fill(255, lifespan);
		view.noStroke();
		view.ellipse(position.x, position.y, 2, 2);
	}

	boolean isDead() {
		if (lifespan <= 0.0) {
			return true;
		} else {
			return false;
		}
	}
}
