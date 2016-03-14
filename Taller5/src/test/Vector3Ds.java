package utilidades;

import org.apache.commons.math3.geometry.euclidean.threed.Vector3D;

import processing.core.PVector;

public class Vector3Ds {

	public static Vector3D getVector3D(PVector point) {
		return new Vector3D(point.x, point.y, point.z);
	}
	
}
