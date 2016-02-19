package miniMap;

import processing.core.PApplet;
import processing.core.PImage;
import processing.event.MouseEvent;

public class Main extends PApplet {

	private static final float ROTATION_FACTOR = PI / 16;
	private static float MAP_SCALE = .30f;
	private PImage imageSection;
	private PImage image;
	private int width = 480;
	private int height = 640;
	private int miniMapWidth;
	private int miniMapHeight;
	private int xRealPosition;
	private int yRealPosition;
	private Visor visor;
	private MiniMap miniMap;
	private DetailArea detailArea;

	@Override
	public void settings() {
		size(width, height, P2D);
	}

	@Override
	public void setup() {
		miniMapWidth = (int) (width * MAP_SCALE);
		miniMapHeight = (int) (height * MAP_SCALE);

		image = loadImage("./assets/tree.jpg");
		miniMap = new MiniMap(this, 0, 0, miniMapWidth, miniMapHeight);
		visor = new Visor(this, miniMap.getGraphics(), miniMapWidth / 2, miniMapHeight / 2,
				miniMapWidth, miniMapHeight);
		detailArea = new DetailArea(this, 0, 0, width, height);
	}

	@Override
	public void draw() {
		background(255);

		updateMiniMapYCoord();
		updateMiniMapXCoord();
		xRealPosition = ((visor.getX() * width) / (miniMapWidth))
				- (((visor.getWidth() / 2) * width) / (miniMapWidth));
		yRealPosition = ((visor.getY() * height) / miniMapHeight)
				- (((visor.getHeight() / 2) * width) / (miniMapWidth));
		int widthTarget = (visor.getWidth() * width) / miniMapWidth;
		int heightTarget = (visor.getHeight() * height) / miniMapHeight;

		imageSection = image.get(xRealPosition, yRealPosition, widthTarget, heightTarget);
		detailArea.setBackground(imageSection);
		detailArea.draw();
		detailArea.show();

		miniMap.setBackground(image);
		miniMap.draw();

		visor.draw();
		miniMap.show();
	}

	@Override
	public void mousePressed(MouseEvent event) {
		switch (event.getButton()) {
		case LEFT:
			visor.rotate(ROTATION_FACTOR * -1);
			detailArea.rotate(ROTATION_FACTOR * -1);
			break;

		case RIGHT:
			visor.rotate(ROTATION_FACTOR);
			detailArea.rotate(ROTATION_FACTOR);
			break;

		default:
			break;
		}
	}

	@Override
	public void mouseWheel(MouseEvent event) {
		int zoomFactor = 2 * event.getCount();

		visor.decreaseWidthIn(zoomFactor);
		visor.decreaseHeightIn(zoomFactor);
	}

	public void updateMiniMapYCoord() {
		float visorArea = visor.getHeight() / 2;

		if ((visorArea < mouseY) && (mouseY < (miniMapHeight - visorArea))) {
			visor.setY(mouseY);
		}
	}

	public void updateMiniMapXCoord() {
		float visorArea = visor.getWidth() / 2;

		if ((visorArea < mouseX) && (mouseX < (miniMapWidth - visorArea))) {
			visor.setX(mouseX);
		}
	}

	public static void main(String args[]) {
		PApplet.main(Main.class.getName());
	}

}
