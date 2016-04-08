package processing;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.math3.analysis.interpolation.SplineInterpolator;
import org.apache.commons.math3.analysis.polynomials.PolynomialSplineFunction;

import processing.core.PApplet;
import processing.core.PGraphics;
import remixlab.dandelion.geom.Vec;
import remixlab.proscene.InteractiveFrame;
import remixlab.proscene.Scene;

public class Splines extends PApplet {

	Scene scene, auxScene;
	PGraphics canvas, auxCanvas;
	private List<InteractiveFrame> interactiveFrames;
	SplineInterpolator sp;
	private float x;
	private float y;

	@Override
	public void settings() {
		size(640, 720, P3D);
	}

	@Override
	public void setup() {
		scene = new Scene(this);
		// scene.setVisualHints(scene.AXES);

		scene.setVisualHints(scene.PICKING);
		interactiveFrames = new ArrayList<>();
		addInteractiveFrame(-40, 0);
		addInteractiveFrame(-30, 10);
		addInteractiveFrame(0, 0);
		addInteractiveFrame(30, 10);
		addInteractiveFrame(40, 0);
		sp = new SplineInterpolator();
	}

	@Override
	public void draw() {
		background(0);
		stroke(0, 255, 0);
		calcSpline();
	}

	private void addInteractiveFrame(float x, float y) {
		InteractiveFrame interactiveFrame = new InteractiveFrame(scene);
		interactiveFrame.setMotionBinding(LEFT, "translate");
		interactiveFrame.translate(x, y);
		interactiveFrames.add(interactiveFrame);
	}

	public void calcSpline() {
		PolynomialSplineFunction interpolate = null;
		double x[] = new double[interactiveFrames.size()];
		double y[] = new double[interactiveFrames.size()];
		double z[] = new double[interactiveFrames.size()];
		float zmax = 1;
		float zmin = 0;
		
		for (int j = 0; j < interactiveFrames.size(); j++) {
			Vec position = interactiveFrames.get(j).position();
			x[j] = position.x();
			y[j] = position.y();
			z[j] = position.z();
		}
		
		for (double element : z) {
			if (element > zmax) {
				zmax = (float) element;
			}
			if (element < zmin) {
				zmin = (float) element;
			}
		}

		try {
			interpolate = sp.interpolate(x, y);
		} catch (Exception e) {
			// e.printStackTrace();
			return;
		}

		for (int j = 0; j < (x.length - 1); j++) {
			float x1 = (float) x[j];
			float z1 = (float) z[j];

			while (x1 < x[j + 1]) {
				float y1 = (float) interpolate.value(x1);
				drawRectangle(x1, y1, zmin, zmax);
				x1 += 0.01;
			}
		}
	}

	private void drawRectangle(float x, float y, float z0, float z1) {
		line(x, y, z0, x, y, z1);
	}

	public static void main(String[] args) {
		PApplet.main(Splines.class.getCanonicalName());
	}

}
