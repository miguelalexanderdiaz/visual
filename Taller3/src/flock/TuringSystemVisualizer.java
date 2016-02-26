package flock;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;

import javax.swing.JComponent;

import processing.core.PImage;

/**
 * A component that displays a visualization of a {@link TuringSystemSolver}
 * result.
 */
class TuringSystemVisualizer extends JComponent {
	public static final Color DEFAULT_CMIN = new Color(0x3c0009);
	public static final Color DEFAULT_CMAX = new Color(0xffff3a);
	private PImage image2;

	/**
	 * Create a visualizer for a {@link TuringSystemSolver} with a maximum size
	 * of <code>width</code> x <code>height</code>.
	 *
	 * @param width
	 *            the maximum width of the systems to be visualized
	 * @param height
	 *            the maximum height of the systems to be visualized
	 */
	TuringSystemVisualizer(int width, int height) {
		super();
		setSize(width, height);
		colours = new int[256];
		setColors(DEFAULT_CMIN, DEFAULT_CMAX);
	}

	public PImage getImage2() {
		return image2;
	}

	/**
	 * Set the colours to use for visualizing results. The image colours will
	 * vary smoothly from <code>cmin</code> (representing the minimum value in
	 * the system) to <code>cmax</code> (representing the maximum value).
	 *
	 * @param cmin
	 *            colour to use for the minimum value
	 * @param cmax
	 *            colour to use for the maximum value
	 */
	public synchronized void setColors(Color cmin, Color cmax) {
		int Rlo = cmin.getRed();
		int Glo = cmin.getGreen();
		int Blo = cmin.getBlue();
		int Rhi = cmax.getRed();
		int Ghi = cmax.getGreen();
		int Bhi = cmax.getBlue();

		for (int i = 0; i < 256; ++i) {
			colours[i] = (scale(i, Rlo, Rhi) << 16) | (scale(i, Glo, Ghi) << 8)
					| scale(i, Blo, Bhi);
		}
	}

	/**
	 * Calculate an interpolated colour component that is i/255 of the way
	 * between lo and hi.
	 */
	private static int scale(int i, int lo, int hi) {
		return lo + (((hi - lo) * i) / 255);
	}

	/**
	 * Return the colours currently used to visualize results. The colours are
	 * returned in an array in <code>cmin</code>, <code>cmax</code> order (see
	 * {@link #setColors}).
	 *
	 * @return an array of the colours used for the extreme values in the system
	 */
	public Color[] getColors() {
		return new Color[] { new Color(colours[0]), new Color(colours[255]) };
	}

	/**
	 * Set the tiling mode of the component. If tiling is enabled, the component
	 * will create a tiled image by repeating the visualization until the entire
	 * component is filled. Otherwise, the tile is centered in the middle of the
	 * component.
	 *
	 * @param tile
	 *            whether or not to enable tiling
	 */
	public void setTiled(boolean tile) {
		this.tile = tile;
		repaint();
	}

	/**
	 * Return <code>true</code> if the component will tile the image.
	 *
	 * @return <code>true</code> if tiling is enabled
	 */
	public boolean isTiled() {
		return tile;
	}

	/**
	 * Update the visualiztion with a new data set, causing the display to be
	 * redrawn.
	 */
	public void updateImage(double data[][], int width, int height) {
		image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
		scaledData = new int[width][height];

		int i, j;

		// find range of concentrations
		double high = Double.NEGATIVE_INFINITY;
		double low = Double.POSITIVE_INFINITY;
		for (i = 0; i < height; ++i) {
			for (j = 0; j < width; ++j) {
				double val = data[i][j];
				if (val > high) {
					high = val;
				} else if (val < low) {
					low = val;
				}
			}
		}

		// scale data to lie within 0--255
		for (i = 0; i < height; ++i) {
			for (j = 0; j < width; ++j) {
				int scaled = (int) (((data[i][j] - low) * 255.0) / (high - low));
				scaled = Math.max(0, Math.min(255, scaled));
				scaledData[i][j] = scaled;
			}
		}

		updateImage();
	}

	/**
	 * Redraw the image using the current scaled data and colour set.
	 */
	private synchronized void updateImage() {
		int width = image.getWidth();
		int height = image.getHeight();

		// update the image using the scaled data
		for (int y = 0; y < height; ++y) {
			for (int x = 0; x < width; ++x) {
				image.setRGB(x, y, colours[scaledData[x][y]]);
			}
		}

		image2 = new PImage(image);
		repaint();
	}

	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		if (image == null) {
			return;
		}

		if (tile) {
			for (int y = 0; y < getHeight(); y += image.getHeight()) {
				for (int x = 0; x < getWidth(); x += image.getWidth()) {
					g.drawImage(image, x, y, null);
				}
			}
		} else {
			g.drawImage(image, (getWidth() - image.getWidth()) / 2,
					(getHeight() - image.getHeight()) / 2, null);
		}
	}

	private BufferedImage image;
	private int[][] scaledData;
	private int[] colours;
	private volatile boolean tile = false;
}
