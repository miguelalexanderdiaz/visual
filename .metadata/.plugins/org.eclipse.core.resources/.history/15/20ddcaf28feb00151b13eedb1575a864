package test;
import org.apache.commons.math3.geometry.euclidean.threed.Vector3D;

import processing.core.PGraphics;
import processing.core.PVector;
import utilidades.Vector3Ds;

public class Triangle3D {
	private PVector[] points;
	Box caja;
	private int red = -1, green = -1, blue = -1;
	String name;
	private PVector normal;

	public Triangle3D(PVector point1, PVector point2, PVector point3, Box caja) {
		points = new PVector[3];
		this.points[0] = point1;
		this.points[1] = point2;
		this.points[2] = point3;
		this.caja = caja;
		this.calcNormal();
	}

	public Triangle3D(PVector point2, PVector point3, Box caja) {
		this(new PVector(0, 0, 0), point2, point3, caja);
	}

	public void drawOn(PGraphics canvas) {
		canvas.pushStyle();
		if (red == -1) {
			canvas.fill(caja.getColor());
		} else {
			canvas.fill(red, green, blue);
		}
		canvas.beginShape();
		for (PVector point : points) {
			if (canvas.is2D()) {
				canvas.vertex(point.x, point.y);
			} else {
				canvas.vertex(point.x, point.y, point.z);
			}
		}
		canvas.endShape(PGraphics.CLOSE);
		canvas.popStyle();
	}

	public PVector getNormal() {
		return normal;
	}

	private void calcNormal() {
		PVector a = PVector.sub(points[1], points[0]);
		PVector b = PVector.sub(points[2], points[0]);
		Vector3D a2 = Vector3Ds.getVector3D(a);
		Vector3D b2 = Vector3Ds.getVector3D(b);
		Vector3D result = Vector3D.crossProduct(a2, b2);
		normal = new PVector((float) result.getX(), (float) result.getY(), (float) result.getZ());
	}

	public PVector[] getPoints() {
		return points;
	}

	public Triangle3D getCopy(PVector a, PVector b, PVector c) {
		Triangle3D copy = new Triangle3D(a, b, c, this.caja);
		copy.name = this.name;
		copy.setColor(red, green, this.blue);
		return copy;
	}

	public void setColor(int r, int g, int b) {
		this.red = r;
		this.green = g;
		this.blue = b;
	}

	public float maxX() {
		return Math.max(points[0].x, Math.max(points[1].x, points[2].x));
	}

	public float minX() {
		return Math.min(points[0].x, Math.min(points[1].x, points[2].x));
	}

	public float maxY() {
		return Math.max(points[0].y, Math.max(points[1].y, points[2].y));
	}

	public float minY() {
		return Math.min(points[0].y, Math.min(points[1].y, points[2].y));
	}

	public float maxZ() {
		return Math.max(points[0].z, Math.max(points[1].z, points[2].z));
	}

	public float minZ() {
		return Math.min(points[0].z, Math.min(points[1].z, points[2].z));
	}

}
