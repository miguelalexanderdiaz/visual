package processing;

import java.util.Comparator;

import processing.core.PShape;
import processing.core.PVector;
import remixlab.dandelion.core.Camera;

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
		// PVector eyeCoords =
		// Scene.toPVector(camera.eyeCoordinatesOf(Scene.toVec(p1.getVertex(0))));
		// Scene.toVec(arg0)
		// camera.projectedCoordinatesOf(arg0)
		// Uso de las pruebas del algoritmo del pintor
		
		// 1ra Prueba
		if (traslape(p1, p2) == false) {
			return 0;
		}
		
		// 2da Prueba:
		if (detrasDe(p1, p2) == p1.getVertexCount()) {
			return -1;
		}
		
		// 3ra Prueba
		if (enFrenteDe(p1, p2) == true) {
			return -1;
		}
		
		return 1;
	}
	
	private boolean enFrenteDe(PShape p1, PShape p2) {
		boolean negarNormal = false;
		
		if (detrasDe(p1, p2) == 0) {
			return false;
		}
		
		if (normal(p2).z < 0) {
			negarNormal = true;
		}
		
		for (float angle : anglesFromNormal(p2, p1, negarNormal)) {
			if ((float) Math.toDegrees(angle) > 90) {
				return false;
			}
		}
		
		return true;
	}
	
	private boolean traslape(PShape p1, PShape p2) {
		float intersectionMax = Math.min(maxX(p1), maxX(p2));
		float intersectionMin = Math.max(minX(p1), minX(p2));
		
		// Translape con respecto a X
		if (intersectionMin < intersectionMax) {
			return true;
		}
		
		// Translape con respecto a Y
		intersectionMax = Math.min(maxY(p1), maxY(p2));
		intersectionMin = Math.max(minY(p1), minY(p2));
		
		if (intersectionMin < intersectionMax) {
			return true;
		}
		
		return false;
	}
	
	private int detrasDe(PShape p1, PShape p2) {
		boolean negarNormal = false;
		int n = 0;
		
		if (normal(p2).z < 0) {
			negarNormal = true;
		}
		
		Float[] angles = anglesFromNormal(p2, p1, negarNormal);
		
		for (float angle : angles) {
			if ((float) Math.toDegrees(angle) <= 90) {
				n++;
			}
		}
		
		return n;
	}
	
	private Float[] anglesFromNormal(PShape p2, PShape p1, boolean negarNormal) {
		Float[] anglesArray = new Float[p1.getVertexCount()];
		PVector normal = normal(p2);
		
		if (negarNormal) {
			normal = normal.mult(-1);
		}
		
		for (int i = 0; i < anglesArray.length; i++) {
			PVector point = p1.getVertex(i);
			PVector vector = PVector.sub(point, p2.getVertex(0));
			
			float angle;
			if (vector.mag() == 0) {
				// 90� por que esta en el plano
				angle = (float) Math.toRadians(90);
			} else {
				angle = PVector.angleBetween(normal, vector);
				
			}
			
			anglesArray[i] = angle;
		}
		return anglesArray;
	}
	
	private PVector normal(PShape p) {
		PVector a = PVector.sub(p.getVertex(1), p.getVertex(0));
		PVector b = PVector.sub(p.getVertex(2), p.getVertex(0));
		
		return a.cross(b);
	}
	
	public float maxX(PShape p) {
		return Math.max(p.getVertexX(0), Math.max(p.getVertexX(1), p.getVertexX(2)));
	}
	
	public float minX(PShape p) {
		return Math.min(p.getVertexX(0), Math.min(p.getVertexX(1), p.getVertexX(2)));
	}
	
	public float maxY(PShape p) {
		return Math.max(p.getVertexY(0), Math.max(p.getVertexY(1), p.getVertexY(2)));
	}
	
	public float minY(PShape p) {
		return Math.min(p.getVertexY(0), Math.min(p.getVertexY(1), p.getVertexY(2)));
	}
	
	public float maxZ(PShape p) {
		return Math.max(p.getVertexZ(0), Math.max(p.getVertexZ(1), p.getVertexZ(2)));
	}
	
	public float minZ(PShape p) {
		return Math.min(p.getVertexZ(0), Math.min(p.getVertexZ(1), p.getVertexZ(2)));
	}
}