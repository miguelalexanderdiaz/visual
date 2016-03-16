package processing;

import java.util.LinkedList;
import java.util.List;
import java.util.Random;

import processing.core.PApplet;
import processing.core.PGraphics;
import processing.core.PShape;
import processing.core.PVector;
import remixlab.dandelion.core.Camera;
import remixlab.dandelion.geom.Vec;
import remixlab.proscene.Scene;

//HSR
public class HiddenSurfaceRemoval extends PApplet {

	private static Scene scene, auxScene;
	private static List<PShape> shapes;

	// backFace , viewFrustrum, mixed
	private String hiddeSurfaceMethod = "mixed";

	@Override
	public void settings() {
		size(640, 720, P3D);
	}

	public void setup() {

		frameRate(30);

		scene = new Scene(this, createGraphics(640, 360, P3D));
		scene.enableBoundaryEquations();

		auxScene = new Scene(this, createGraphics(640, 360, P3D), 0, 360);
		auxScene.setRadius(500);
		auxScene.showAll();

		shapes = buildComplexScene();
		// Vec p1 = new Vec(-500, -250, -250);
		// Vec p2 = new Vec(500, 250, 250);
		// TODO: To build Octree
	}

	public List<PShape> buildComplexScene() {
		List<PShape> shapes = new LinkedList<PShape>();

		Random random = new Random();
		int numx = 20, numy = 20;

		for (int i = 0; i < numx; i++) {
			for (int j = 0; j < numy; j++) {
				int x = 20 * (i - numx / 2);
				int y = 20 * (j - numy / 2);
				shapes.addAll(createBox(x, y, 0, 10, random.nextInt(numx + numy) + 5));
			}
		}

		return shapes;
	}

	public List<PShape> createBox(int x, int y, int z, int w, int h) {
		// For closed polygons, use normal vectors facing outward
		PShape quad1 = createShape();
		quad1.beginShape(QUAD);
		quad1.vertex(x, y + w, z + h);
		quad1.vertex(x, y, z + h);
		quad1.vertex(x + w, y, z + h);
		quad1.vertex(x + w, y + w, z + h);
		quad1.endShape();

		PShape quad2 = createShape();
		quad2.beginShape(QUAD);
		quad2.vertex(x + w, y, z + h);
		quad2.vertex(x + w, y, z);
		quad2.vertex(x + w, y + w, z);
		quad2.vertex(x + w, y + w, z + h);
		quad2.endShape();

		PShape quad3 = createShape();
		quad3.beginShape(QUAD);
		quad3.vertex(x + w, y + w, z);
		quad3.vertex(x, y + w, z);
		quad3.vertex(x, y + w, z + h);
		quad3.vertex(x + w, y + w, z + h);
		quad3.endShape();

		PShape quad4 = createShape();
		quad4.beginShape(QUAD);
		quad4.vertex(x, y + w, z);
		quad4.vertex(x + w, y + w, z);
		quad4.vertex(x + w, y, z);
		quad4.vertex(x, y, z);
		quad4.endShape();

		PShape quad5 = createShape();
		quad5.beginShape(QUAD);
		quad5.vertex(x, y + w, z + h);
		quad5.vertex(x, y + w, z);
		quad5.vertex(x, y, z);
		quad5.vertex(x, y, z + h);
		quad5.endShape();

		PShape quad6 = createShape();
		quad6.beginShape(QUAD);
		quad6.vertex(x, y, z);
		quad6.vertex(x + w, y, z);
		quad6.vertex(x + w, y, z + h);
		quad6.vertex(x, y, z + h);
		quad6.endShape();

		List<PShape> box = new LinkedList<PShape>();
		box.add(quad1);
		box.add(quad2);
		box.add(quad3);
		box.add(quad4);
		box.add(quad5);
		box.add(quad6);
		return box;
	}

	public void draw() {
		handleMouse();
		surface.setTitle("Frames: " + frameRate);

		scene.pg().beginDraw();
		scene.beginDraw();
		mainDrawing(scene);
		scene.endDraw();
		scene.pg().endDraw();
		image(scene.pg(), 0, 0);

		auxScene.pg().beginDraw();
		auxScene.beginDraw();
		mainDrawing(auxScene);

		auxScene.pg().pushStyle();
		auxScene.pg().stroke(255, 255, 0);
		auxScene.pg().fill(255, 255, 0, 160);
		auxScene.drawEye(scene.eye());
		auxScene.pg().popStyle();

		auxScene.endDraw();
		auxScene.pg().endDraw();
		image(auxScene.pg(), auxScene.originCorner().x(), auxScene.originCorner().y());
	}

	public void mainDrawing(Scene scene) {
		scene.pg().background(0);

		for (PShape shape : shapes) {
			switch (hiddeSurfaceMethod) {
			case "backFace":
				if (backFaceCulling(shape)) {
					scene.pg().shape(shape);
				}
				break;
			case "viewFrustrum":
				if (viewFrustrumCulling(shape)) {
					scene.pg().shape(shape);
				}

				break;
			case "mixed":
				if (backFaceCulling(shape) && viewFrustrumCulling(shape)) {
					scene.pg().shape(shape);
				}
				break;
			default:
				scene.pg().shape(shape);
			}
		}

	}

	// Back-face culling
	public Boolean backFaceCulling(PShape shape) {
		float vertexNumber = shape.getVertexCount();
		PVector avg = new PVector(0, 0, 0);

		for (int i = 0; i < vertexNumber; i++) {
			avg.add(shape.getVertex(i));
		}

		avg.div(vertexNumber);

		return scene.camera().isFaceFrontFacing(Scene.toVec(avg), Scene.toVec(shape.getNormal(0)));
	}

	// View Frustrum culling
	public Boolean viewFrustrumCulling(PShape shape) {
		float vertexNumber = shape.getVertexCount();

		for (int i = 0; i < vertexNumber; i++) {
			if (scene.eye().isPointVisible(Scene.toVec(shape.getVertex(i))))
				return true;
		}

		return false;
	}

	public void handleMouse() {
		if (mouseY < 360) {
			scene.enableMotionAgent();
			scene.enableKeyboardAgent();
			auxScene.disableMotionAgent();
			auxScene.disableKeyboardAgent();
		} else {
			scene.disableMotionAgent();
			scene.disableKeyboardAgent();
			auxScene.enableMotionAgent();
			auxScene.enableKeyboardAgent();
		}
	}

	public static void main(String[] args) {
		PApplet.main(HiddenSurfaceRemoval.class.getCanonicalName());
	}

	class OctreeNode {
		Vec p1, p2;
		OctreeNode child[];
		int level;

		OctreeNode(Vec P1, Vec P2) {
			p1 = P1;
			p2 = P2;
			child = new OctreeNode[8];
		}

		public void draw(PGraphics pg) {
		}

		public void drawIfAllChildrenAreVisible(PGraphics pg, Camera camera) {
			Camera.Visibility vis = camera.boxVisibility(p1, p2);
			if (vis == Camera.Visibility.VISIBLE)
				draw(pg);
			else if (vis == Camera.Visibility.SEMIVISIBLE)
				if (child[0] != null)
					for (int i = 0; i < 8; ++i)
						child[i].drawIfAllChildrenAreVisible(pg, camera);
				else
					draw(pg);
		}

		public void buildBoxHierarchy(int l) {
			level = l;
			Vec middle = Vec.multiply(Vec.add(p1, p2), 1 / 2.0f);
			
			for (int i = 0; i < 8; ++i) {
				// point in one of the 8 box corners
				Vec point = new Vec(((i & 4) != 0) ? p1.x() : p2.x(), ((i & 2) != 0) ? p1.y() : p2.y(),
						((i & 1) != 0) ? p1.z() : p2.z());
				if (level > 0) {
					child[i] = new OctreeNode(point, middle);
					child[i].buildBoxHierarchy(level - 1);
				} else
					child[i] = null;
			}
		}
	}

}
