package particleSystem;

import java.util.ArrayList;

import processing.core.PApplet;
import processing.core.PImage;
import processing.core.PVector;

public class ParticleSystem {

	private ArrayList<Particle> particles;
	private PVector particleOrigin;
	private PImage particleTexture;
	private int particleAmount;
	private PVector velocity;
	private PVector acceleration;
	private PApplet view;

	ParticleSystem(PApplet view, int particleAmount, PVector particleOrigin,
			PImage particleTexture) {

		this.view = view;
		this.particleAmount = particleAmount;
		this.particleOrigin = particleOrigin;
		this.particleTexture = particleTexture;
		particles = new ArrayList<Particle>();
	}

	void run() {
		addParticles();
		updateParticles();
	}

	private void updateParticles() {
		for (int i = particles.size() - 1; i >= 0; i--) {
			Particle p = particles.get(i);
			p.run();
			if (p.isDead()) {
				particles.remove(i);
			}
		}
	}

	private void addParticles() {
		for (int i = 0; i < particleAmount; i++) {
			addParticle();
		}
	}

	void applyForce(PVector dir) {
		for (Particle p : particles) {
			p.applyForce(dir);
		}

	}

	void addParticle() {
		Particle particle;
		float vx = view.random(-1f, 1f);
		float vy = -view.random(0.5f, 2f);
		float ax = 0;
		float ay = 0;

		velocity = new PVector(vx, vy);
		acceleration = new PVector(ax, ay);
		particle = new Particle(view, particleOrigin.copy(), velocity, acceleration,
				particleTexture);
		particles.add(particle);
	}

}