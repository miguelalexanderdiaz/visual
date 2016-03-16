package processing;

import remixlab.proscene.*;

import java.util.Random;

import processing.core.PApplet;
import processing.core.PGraphics;
import processing.core.PShape;
import remixlab.dandelion.core.*;
import remixlab.dandelion.geom.*;

public class ViewFrustumCulling extends PApplet {

	OctreeNode Root;
	Scene scene, auxScene;
	PGraphics canvas, auxCanvas;

	@Override
	public void settings() {
		size(640, 720, P3D);
	}

	public void setup() {
		// declare and build the octree hierarchy
		Vec p = new Vec(100, 70, 130);
		Root = new OctreeNode(p, Vec.multiply(p, -1.0f));
		Root.buildBoxHierarchy(4);

		canvas = createGraphics(640, 360, P3D);
		scene = new Scene(this, canvas);
		scene.enableBoundaryEquations();
		scene.setGridVisualHint(false);

		auxCanvas = createGraphics(640, 360, P3D);
		// Note that we pass the upper left corner coordinates where the scene
		// is to be drawn (see drawing code below) to its constructor.
		auxScene = new Scene(this, auxCanvas, 0, 360);
		// auxScene.camera().setType(Camera.Type.ORTHOGRAPHIC);
		auxScene.setAxesVisualHint(false);
		auxScene.setGridVisualHint(false);
		auxScene.setRadius(200);
		auxScene.showAll();
	}

	public void draw() {
		background(0);
		handleMouse();
		canvas.beginDraw();
		scene.beginDraw();
		canvas.background(0);
		Root.drawIfAllChildrenAreVisible(scene.pg(), scene.camera());
		scene.endDraw();
		canvas.endDraw();
		image(canvas, 0, 0);

		auxCanvas.beginDraw();
		auxScene.beginDraw();
		auxCanvas.background(0);
		Root.drawIfAllChildrenAreVisible(auxScene.pg(), scene.camera());
		auxScene.pg().pushStyle();
		auxScene.pg().stroke(255, 255, 0);
		auxScene.pg().fill(255, 255, 0, 160);
		auxScene.drawEye(scene.eye());
		auxScene.pg().popStyle();
		auxScene.endDraw();
		auxCanvas.endDraw();
		// We retrieve the scene upper left coordinates defined above.
		image(auxCanvas, auxScene.originCorner().x(), auxScene.originCorner().y());
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
		PApplet.main(ViewFrustumCulling.class.getCanonicalName());
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
			int x = (int) random(10, 100);
			int y = 20;
			int z = 0;
			int w = 10;
			int h = 20;

			pg.beginShape();
			pg.beginShape(QUAD);
			pg.vertex(x + w, y, z + h);
			pg.vertex(x + w, y, z);
			pg.vertex(x + w, y + w, z);
			pg.vertex(x + w, y + w, z + h);
			pg.endShape();
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
