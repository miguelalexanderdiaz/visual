package particleSystem;

import java.util.ArrayList;

import processing.core.PApplet;
import processing.core.PImage;
import processing.core.PVector;

public class Main extends PApplet {
	
	ParticleSystem particleSystem;
	
	@Override
	public void settings() {
		size(640, 360);
	}
	
	@Override
	public void setup() {
		PImage particlesTexture = loadImage("./img/texture.png");
		PVector particlesOrigin = new PVector(width / 2, height - 60);

		particleSystem = new ParticleSystem(2, particlesOrigin, particlesTexture);
	}
	
	@Override
	public void draw() {
		background(0);
		
		float dx = map(mouseX, 0, width, -0.2f, 0.2f);
		PVector wind = new PVector(dx, 0);
		particleSystem.applyForce(wind);
		particleSystem.run();
		for (int i = 0; i < 20; i++) {
			particleSystem.addParticle();
		}
	}
	
	class ParticleSystem {
		
		ArrayList<Particle> particles;
		PVector origin;
		PImage particleTexture;
		
		ParticleSystem(int particleAmount, PVector velocity, PImage particleTexture) {
			particles = new ArrayList<Particle>();
			origin = velocity.get();
			this.particleTexture = particleTexture;
			
			for (int i = 0; i < particleAmount; i++) {
				particles.add(new Particle(origin, particleTexture));
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
		
		void applyForce(PVector dir) {
			for (Particle p : particles) {
				p.applyForce(dir);
			}
			
		}
		
		void addParticle() {
			particles.add(new Particle(origin, particleTexture));
		}
		
	}
	
	class Particle {
		PVector position;
		PVector velocity;
		PVector acceleration;
		float lifespan;
		PImage texture;
		
		Particle(PVector position, PImage texture) {
			float vx = random(-0.7f, 0.7f);
			float vy = -random(1, 2f);
			
			acceleration = new PVector(0, 0);
			velocity = new PVector(vx, vy);
			this.position = position.get();
			lifespan = 100;
			this.texture = texture;
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
			ellipse(position.x, position.y, 2, 2);
		}
		
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
