package test;
import java.util.ArrayList;

import processing.core.PApplet;
import processing.core.PGraphics;
import processing.opengl.PGraphicsOpenGL;
import remixlab.proscene.CameraProfile;
import remixlab.proscene.Scene;

/**
 * @author Sebastian
 * 
 */
@SuppressWarnings("serial")
public class PrincipalHSR extends PApplet {

	private Scene scene;
	private ArrayList<Box> boxes;
	private PGraphics upLeftCanvas;
	private PGraphics upRightCanvas;
	private PGraphics bottomRightCanvas;
	private PGraphics bottomLeftCanvas;

	public void setup() {
		int numberOfBoxes = 1;

		size(640, 480, P3D);
		upLeftCanvas = createGraphics(width / 2, height / 2, P3D);
		upRightCanvas = createGraphics(width / 2, height / 2);
		bottomRightCanvas = createGraphics(width / 2, height / 2);
		bottomLeftCanvas = createGraphics(width / 2, height / 2);

		scene = new Scene(this, (PGraphicsOpenGL) upLeftCanvas);

		// add the click actions to all camera profiles
		CameraProfile[] camProfiles = scene.getCameraProfiles();
		for (int i = 0; i < camProfiles.length; i++) {
			// middle click will show all the scene:
			camProfiles[i].setClickBinding(Scene.Button.MIDDLE, Scene.ClickAction.SHOW_ALL);
			// right click will will set the arcball reference point:
			camProfiles[i].setClickBinding(Scene.Button.RIGHT, Scene.ClickAction.ARP_FROM_PIXEL);
			// double click with the middle button while pressing SHIFT will
			// reset the arcball reference point:
			camProfiles[i].setClickBinding(Scene.Modifier.SHIFT.ID, Scene.Button.MIDDLE, 2,
					Scene.ClickAction.RESET_ARP);
		}

		scene.setGridIsDrawn(false);
		scene.setAxisIsDrawn(true);
		scene.setRadius(200);
		scene.enableFrustumEquationsUpdate();
		scene.showAll();

		boxes = new ArrayList<Box>(numberOfBoxes);
		// create an array of boxes with random positions, sizes and colors
		for (int i = 0; i < numberOfBoxes; i++) {
			boxes.add(new Box(scene));

		}
	}

	public void draw() {
		upLeftCanvas.beginDraw();
		upLeftCanvas.hint(PGraphics.DISABLE_DEPTH_TEST);
		// Manejar eventos mouse segun posicion

		scene.beginDraw();
		scene.renderer().background(0);
		for (Box box : boxes) {
			box.draw();
		}
		scene.endDraw();
		upLeftCanvas.endDraw();
		image(upLeftCanvas, 0, 0);

		// Obtener lista de planos con coordenadas proyectadas de la camara
		ArrayList<Triangle3D> planos = new ArrayList<Triangle3D>();
		for (Box box : boxes) {
			planos.addAll(box.getProjectedCameraCoord(scene.camera()));
		}

		// Dibujar canvas derecho
		upRightCanvas.beginDraw();
		upRightCanvas.background(255);
		upRightCanvas.fill(0);
		upRightCanvas.text("Ambas Pruebas", 0, 10);
		ArrayList<Triangle3D> planosURC = new ArrayList<Triangle3D>(planos);
		Pintor.dibujarPlanos(upRightCanvas, planosURC, Pintor.TODAS_PRUEBAS);

		upRightCanvas.endDraw();
		image(upRightCanvas, width / 2, 0);

		// Dibujar canvas inferior izquierdo
		bottomLeftCanvas.beginDraw();
		bottomLeftCanvas.background(200);
		bottomLeftCanvas.fill(0);
		bottomLeftCanvas.text("Solo ordenar por Z", 0, 10);

		ArrayList<Triangle3D> planosBLC = new ArrayList<Triangle3D>(planos);
		Pintor.dibujarPlanos(bottomLeftCanvas, planosBLC, Pintor.Z_COMPARATOR);
		bottomLeftCanvas.endDraw();
		image(bottomLeftCanvas, 0, height / 2);

		// Dibujar canvas inferior derecho
		bottomRightCanvas.beginDraw();
		bottomRightCanvas.background(55);
		bottomRightCanvas.fill(255);
		bottomRightCanvas.text("Solo pruebas de espacios", 0, 10);

		ArrayList<Triangle3D> planosBRC = new ArrayList<Triangle3D>(planos);
		Pintor.dibujarPlanos(bottomRightCanvas, planosBRC, Pintor.ESPACIOS_PRUEBAS);
		bottomRightCanvas.endDraw();

		image(bottomRightCanvas, width / 2, height / 2);
	}
}
