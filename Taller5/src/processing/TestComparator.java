package processing;

import java.util.Comparator;

import processing.core.PShape;
import processing.core.PVector;
import remixlab.dandelion.core.Camera;
import remixlab.proscene.Scene;

public class TestComparator implements Comparator<PShape> {

	private Camera camera;

	public TestComparator(Camera camera) {
		super();
		this.camera = camera;
	}

	public Camera getCamera() {
		return camera;
	}

	public void setCamera(Camera camera) {
		this.camera = camera;
	}

	@Override
	public int compare(PShape p1, PShape p2) {
		PVector eyeCoords = Scene.toPVector(camera.eyeCoordinatesOf(Scene.toVec(p1.getVertex(0))));
		// Scene.toVec(arg0)
		// camera.projectedCoordinatesOf(arg0)
		int result = 1;

		PVector max;
		float maxDistP1 = 0;
		float maxDistP2 = 0;

		for (int i = 0; i < 3; i++) {
			PVector v = p1.getVertex(i);
			float dist = eyeCoords.dist(v);

			if (dist > maxDistP1) {
				maxDistP1 = dist;
			}
		}

		for (int i = 0; i < 3; i++) {
			PVector v = p2.getVertex(i);
			float dist = eyeCoords.dist(v);

			if (dist > maxDistP2) {
				maxDistP2 = dist;
			}
		}

		float distanciaP2 = eyeCoords.dist(p2.getVertex(0));

		System.out.println("Distancia a p1 " + maxDistP1);
		System.out.println("Distancia a p2 " + maxDistP2);

		if (maxDistP1 > maxDistP2) {
			result = -1;
		} else {
			result = 1;
		}

		return result;
	}

}