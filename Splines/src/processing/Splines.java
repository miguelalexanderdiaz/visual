package processing;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.math3.analysis.interpolation.SplineInterpolator;
import org.apache.commons.math3.analysis.polynomials.PolynomialSplineFunction;

import processing.core.PApplet;
import processing.core.PGraphics;
import processing.event.MouseEvent;
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

	public void setup() {
		scene = new Scene(this);
		// scene.setVisualHints(scene.AXES);

		scene.setVisualHints(scene.PICKING);
		interactiveFrames = new ArrayList<>();
		addInteractiveFrame(-40, 0);
		addInteractiveFrame(-30, 40);
		addInteractiveFrame(0, 0);
		addInteractiveFrame(30, 40);
		addInteractiveFrame(40, 0);
		sp = new SplineInterpolator();
	}

	public void draw() {
		background(0);
		stroke(255);
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

		for (int j = 0; j < interactiveFrames.size(); j++) {
			Vec position = interactiveFrames.get(j).position();
			x[j] = position.x();
			y[j] = position.y();
			z[j] = position.z();
		}

		try {
			interpolate = sp.interpolate(x, y);
		} catch (Exception e) {
			//e.printStackTrace();
			return;
		}

		for (int j = 0; j < x.length - 1; j++) {
			float x1 = (float) x[j];
			float z1 = (float) z[j];

			while (x1 < x[j + 1]) {
				float y1 = (float) interpolate.value(x1);
				point(x1, y1, z1);
				drawRectangle(x1, y1, z1);
				x1 += 0.01;
			}
		}
	}

	private void drawRectangle(float x, float y, float z) {
		line(x, y, 0, x, y, z);
	}

	public static void main(String[] args) {
		PApplet.main(Splines.class.getCanonicalName());
	}

}
