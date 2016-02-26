package particleSystem;

import processing.core.PApplet;
import processing.core.PImage;
import processing.core.PVector;

public class View extends PApplet {
	
	ParticleSystem particleSystem;
	
	@Override
	public void settings() {
		size(640, 360);
	}
	
	@Override
	public void setup() {
		PImage particlesTexture = loadImage("./img/texture.png");
		PVector particlesOrigin = new PVector(width / 2, height - 60);
		
		particleSystem = new ParticleSystem(this, 4, particlesOrigin, particlesTexture);
	}
	
	@Override
	public void draw() {
		background(0);
		
		float dx = map(mouseX, 0, width, -0.005f, 0.005f);
		PVector wind = new PVector(dx, 0);
		particleSystem.applyForce(wind);
		particleSystem.run();
	}
	
}
