package flock;

import java.awt.Color;
import java.awt.image.BufferedImage;

import processing.core.PImage;

public class ImageProcesor {

	private BufferedImage image;
	private int[][] scaledData;
	private int[] colours;

	ImageProcesor() {
		colours = new int[256];
	}

	public void setColors(Color cmin, Color cmax) {
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

	private static int scale(int i, int lo, int hi) {
		return lo + (((hi - lo) * i) / 255);
	}

	public PImage processData(double data[][], int width, int height) {
		image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
		scaledData = new int[width][height];

		int i, j;

		double high = Double.NEGATIVE_INFINITY;
		double low = Double.POSITIVE_INFINITY;
		for (i = 0; i < width; ++i) {
			for (j = 0; j < height; ++j) {
				double val = data[i][j];
				if (val > high) {
					high = val;
				} else if (val < low) {
					low = val;
				}
			}
		}

		// scale data to lie within 0--255
		for (i = 0; i < width; ++i) {
			for (j = 0; j < height; ++j) {
				int scaled = (int) (((data[i][j] - low) * 255.0) / (high - low));
				scaled = Math.max(0, Math.min(255, scaled));
				scaledData[i][j] = scaled;
			}
		}

		return createImage();
	}

	private PImage createImage() {
		int width = image.getWidth();
		int height = image.getHeight();

		for (int y = 0; y < height; ++y) {
			for (int x = 0; x < width; ++x) {
				image.setRGB(x, y, colours[scaledData[x][y]]);
			}
		}

		return new PImage(image);
	}

}
