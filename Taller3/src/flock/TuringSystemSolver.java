package flock;

import java.util.Random;

import processing.core.PImage;

public class TuringSystemSolver {

	private int iterations;
	private double CA, CB;

	private ImageProcesor imageProcessor;
	private double[][] Ao, Bo, An, Bn;
	private int width, height;
	private Random rand = new Random();

	public TuringSystemSolver(ImageProcesor imageProcessor, int width, int height) {
		this.imageProcessor = imageProcessor;
		Ao = new double[width][height];
		An = new double[width][height];
		Bo = new double[width][height];
		Bn = new double[width][height];
		this.width = width;
		this.height = height;
	}

	public PImage solve(int iterations, double CA, double CB) {
		this.iterations = iterations;
		this.CA = CA;
		this.CB = CB;
		return solveImpl();
	}

	private PImage solveImpl() {
		int n, i, j, iplus1, iminus1, jplus1, jminus1;
		double DiA, ReA, DiB, ReB;
		initMatrix();

		for (n = 0; n < iterations; ++n) {
			for (i = 0; i < height; ++i) {
				iplus1 = i + 1;
				iminus1 = i - 1;
				if (i == 0) {
					iminus1 = height - 1;
				}
				if (i == (height - 1)) {
					iplus1 = 0;
				}

				for (j = 0; j < width; ++j) {
					jplus1 = j + 1;
					jminus1 = j - 1;
					if (j == 0) {
						jminus1 = width - 1;
					}
					if (j == (width - 1)) {
						jplus1 = 0;
					}

					DiA = CA * ((((Ao[iplus1][j] - (2.0 * Ao[i][j])) + Ao[iminus1][j]
							+ Ao[i][jplus1]) - (2.0 * Ao[i][j])) + Ao[i][jminus1]);
					ReA = (Ao[i][j] * Bo[i][j]) - Ao[i][j] - 12.0;
					An[i][j] = Ao[i][j] + (0.01 * (ReA + DiA));
					if (An[i][j] < 0.0) {
						An[i][j] = 0.0;
					}

					DiB = CB * ((((Bo[iplus1][j] - (2.0 * Bo[i][j])) + Bo[iminus1][j]
							+ Bo[i][jplus1]) - (2.0 * Bo[i][j])) + Bo[i][jminus1]);
					ReB = 16.0 - (Ao[i][j] * Bo[i][j]);
					Bn[i][j] = Bo[i][j] + (0.01 * (ReB + DiB));
					if (Bn[i][j] < 0.0) {
						Bn[i][j] = 0.0;
					}
				}
			}
			swapBuffers();
		}

		return imageProcessor.processData(An, width, height);
	}

	public void initMatrix() {
		for (int i = 0; i < height; ++i) {
			for (int j = 0; j < width; ++j) {
				Ao[i][j] = (rand.nextDouble() * 12.0) + (rand.nextGaussian() * 2.0);
				Bo[i][j] = (rand.nextDouble() * 12.0) + (rand.nextGaussian() * 2.0);
			}
		}
	}

	private void swapBuffers() {
		double[][] temp = Ao;
		Ao = An;
		An = temp;
		temp = Bo;
		Bo = Bn;
		Bn = temp;
	}

}
