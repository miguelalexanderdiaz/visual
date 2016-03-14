package test;

import java.util.ArrayList;

import com.jogamp.opengl.math.Quaternion;

import processing.core.PApplet;
import processing.core.PGraphics;
import processing.core.PVector;
import remixlab.dandelion.core.Camera;
import remixlab.proscene.InteractiveFrame;
import remixlab.proscene.Scene;

public class Box {
	
	private InteractiveFrame iFrame;
	private float width, height, depth;
	private int color;
	private PGraphics canvas;
	private Scene scene;
	ArrayList<Triangle3D> triangles = new ArrayList<Triangle3D>(12);
	
	Box(Scene scene) {
		iFrame = new InteractiveFrame(scene);
		canvas = scene.renderer();
		this.scene = scene;
		
		// sets size randomly
		int maxSize = 100;
		int minSize = 50;
		width = scene.parent.random(minSize, maxSize);
		height = scene.parent.random(minSize, maxSize);
		depth = scene.parent.random(minSize, maxSize);
		
		// set box's triangles
		setTriangles();
		
		// sets color randomly
		color = scene.parent.color(scene.parent.random(0, 255), scene.parent.random(0, 255),
				scene.parent.random(0, 255));
				
		float low = -100;
		float high = 100;
		iFrame.setPosition(new PVector(scene.parent.random(low, high),
				scene.parent.random(low, high), scene.parent.random(low, high)));
	}
	
	public void draw() {
		
		canvas.pushMatrix();
		canvas.pushStyle();
		// Multiply matrix to get in the frame coordinate system.
		// scene.parent.applyMatrix(iFrame.matrix()) is handy but inefficient
		iFrame.applyTransformation(); // optimum
		
		if (iFrame.grabsMouse()) {
			canvas.fill(255, 0, 0);
			scene.drawAxis(PApplet.max(width, height, depth) * 1.3f);
		} else {
			canvas.fill(getColor());
		}
		
		// Draw the box
		for (int i = 0; i < triangles.size(); i++) {
			this.triangles.get(i).drawOn(canvas);
		}
		
		canvas.popStyle();
		canvas.popMatrix();
	}
	
	public ArrayList<Triangle3D> getProjectedCameraCoord(Camera camera) {
		ArrayList<Triangle3D> planes = new ArrayList<Triangle3D>(triangles.size());
		
		for (Triangle3D vertice : triangles) {
			ArrayList<PVector> points = new ArrayList<PVector>(vertice.getPoints().length);
			for (PVector point : vertice.getPoints()) {
				PVector newPoint = getOrientation().rotate(point);
				newPoint = PVector.add(newPoint, getPosition());
				newPoint = camera.projectedCoordinatesOf(newPoint);
				points.add(newPoint);
			}
			planes.add(vertice.getCopy(points.get(0), points.get(1), points.get(2)));
		}
		return planes;
	}
	
	public void setSize(float myW, float myH, float myD) {
		width = myW;
		height = myH;
		depth = myD;
	}
	
	private void setTriangles() {
		Triangle3D triangle;
		triangle = new Triangle3D(new PVector(0, 0, 0), new PVector(0, width, 0),
				new PVector(0, width, height), this);
		triangle.setColor(255, 0, 0);
		triangle.name = "rojo";
		triangles.add(triangle);
		triangle = new Triangle3D(new PVector(0, 0, 0), new PVector(0, 0, height),
				new PVector(0, width, height), this);
		triangle.setColor(255, 0, 255);
		triangle.name = "MAGENTA";
		triangles.add(triangle);
		// triangle = new Triangle3D(new PVector(0, 0, height), new PVector(0,
		// width, height),
		// new PVector(depth, width, height), this);
		// triangles.add(triangle);
		triangle = new Triangle3D(new PVector(0, 0, height), new PVector(depth, 0, height),
				new PVector(depth, width, height), this);
		triangle.setColor(0, 255, 0);
		triangle.name = "verde";
		triangles.add(triangle);
		triangle = new Triangle3D(new PVector(0, 0, 0), new PVector(0, 0, height),
				new PVector(depth, 0, height), this);
		triangle.setColor(127, 127, 127);
		triangle.name = "GRIS";
		triangles.add(triangle);
		triangle = new Triangle3D(new PVector(0, 0, 0), new PVector(depth, 0, height),
				new PVector(depth, 0, 0), this);
		triangle.setColor(0, 255, 255);
		triangle.name = "cian";
		triangles.add(triangle);
		// triangle = new Triangle3D(new PVector(depth, 0, 0), new
		// PVector(depth, width, 0),
		// new PVector(depth, width, height), this);
		// triangles.add(triangle);
		// triangle = new Triangle3D(new PVector(depth, 0, 0), new
		// PVector(depth, 0, height),
		// new PVector(depth, width, height), this);
		// triangles.add(triangle);
		triangle = new Triangle3D(new PVector(0, 0, 0), new PVector(depth, 0, 0),
				new PVector(0, width, 0), this);
		triangle.setColor(0, 0, 255);
		triangle.name = "azul";
		triangles.add(triangle);
		// triangle = new Triangle3D(new PVector(depth, width, 0), new
		// PVector(depth, 0, 0),
		// new PVector(0, width, 0), this);
		// triangles.add(triangle);
		triangle = new Triangle3D(new PVector(0, width, 0), new PVector(depth, width, 0),
				new PVector(depth, width, height), this);
		triangle.setColor(255, 255, 0);
		triangle.name = "amarillo";
		triangles.add(triangle);
		// triangle = new Triangle3D(new PVector(0, width, 0), new PVector(0,
		// width, height),
		// new PVector(depth, width, height), this);
		// triangles.add(triangle);
	}
	
	public int getColor() {
		return color;
	}
	
	public void setColor(int myC) {
		color = myC;
	}
	
	public PVector getPosition() {
		return iFrame.position();
	}
	
	public void setPosition(PVector pos) {
		iFrame.setPosition(pos);
	}
	
	public Quaternion getOrientation() {
		return iFrame.orientation();
	}
	
	public void setOrientation(PVector v) {
		PVector to = PVector.sub(v, iFrame.position());
		iFrame.setOrientation(new Quaternion(new PVector(0, 1, 0), to));
	}
}
