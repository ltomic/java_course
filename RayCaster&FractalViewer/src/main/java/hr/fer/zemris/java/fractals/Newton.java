package hr.fer.zemris.java.fractals;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadFactory;

import hr.fer.zemris.java.fractals.viewer.FractalViewer;
import hr.fer.zemris.java.fractals.viewer.IFractalProducer;
import hr.fer.zemris.java.fractals.viewer.IFractalResultObserver;
import hr.fer.zemris.math.Complex;
import hr.fer.zemris.math.ComplexPolynomial;
import hr.fer.zemris.math.ComplexRootedPolynomial;

/**
 * Displays a fractal derived from Newton-Raphson iteration with the given polynomial defined by the
 * roots entered through console. Example of roots format : "1", "-1 + i0", "i", "0 - i1", "3-i4",
 * "0". When all roots are entered keyword "done" should be entered.
 * @author ltomic
 *
 */
public class Newton {

	/**
	 * Keyword that marks the end of input of roots
	 */
	private static final String WORD_DONE = "done";

	/**
	 * Method called at the beginning of the program.
	 * @param args - no arguments
	 */
	public static void main(String[] args) {
		List<Complex> roots = new ArrayList<>();
		try (Scanner scan = new Scanner(System.in)) {
			for (int i = 1; true; ++i) {
				System.out.print(String.format("Root %d> ", i));
				String line = scan.nextLine().trim();
				if (line.equals(WORD_DONE)) break;
				try {
					roots.add(Complex.parse(line));
				} catch (IllegalArgumentException ex) {
					System.out.println(ex.getMessage());
					i--;
				}
			}
		}

		FractalViewer
				.show(new MojProducer(new ComplexRootedPolynomial(roots.toArray(new Complex[0]))));
	}

	/**
	 * Calculates the pixel color on the given part of the screen and plain.
	 * @author ltomic
	 *
	 */
	public static class JobCalc implements Callable<Void> {
		/**
		 * Minimum real component of number on the given plain
		 */
		double reMin;
		/**
		 * Maximum real component of number on the given plain
		 */
		double reMax;
		/**
		 * Minimum imaginary component of number on the given plain
		 */
		double imMin;
		/**
		 * Maximum imaginary component of number on the given plain
		 */
		double imMax;
		/**
		 * Width of the screen
		 */
		int width;
		/**
		 * Height of the screen
		 */
		int height;
		/**
		 * Starting y coordinate of points to be calculated
		 */
		int yMin;
		/**
		 * Ending y coordinate of points to be calculated
		 */
		int yMax;
		/**
		 * Maximum number of iterations
		 */
		int maxIter;
		/**
		 * Convergence threshold
		 */
		double convergenceThreshold;
		/**
		 * Distance from root threshold
		 */
		double rootTreshold;
		/**
		 * Calculated indexes of root closest to the converged point
		 */
		short[] data;
		/**
		 * Polynomial defining the fractal, saved via its roots
		 */
		ComplexRootedPolynomial rootPolynom;
		/**
		 * Polynomial defining the fractal, saved via its coefficients
		 */
		ComplexPolynomial polynomial;
		/**
		 * Derived polynomial defining the fractal
		 */
		ComplexPolynomial derivedPolynomial;

		/**
		 * Constructs a {@link JobCalc} with provided arguments
		 * @param reMin - minimum real component of number on the given plain
		 * @param reMax - maximum real component of number on the given plain
		 * @param imMin - minimum imaginary component of number on the given plain
		 * @param imMax - maximum imaginary component of number on the given plain
		 * @param width - width of the screen
		 * @param height - height of the screen
		 * @param yMin - starting y coordinate of points to be calculated
		 * @param yMax - ending y coordinate of points to be calculated
		 * @param rootPolynom - polynomial defining the fractal, saved via its roots
		 * @param polynomial - polynomial defining the fractal, saved via its coefficients
		 * @param derivedPolynomial - derived polynomial defining the fractal
		 * @param m - maximum number of iterations
		 * @param convergenceThreshold - convergence threshold
		 * @param rootTreshold - distance from root threshold
		 * @param data - array where the data about points should be stored
		 */
		public JobCalc(double reMin, double reMax, double imMin, double imMax, int width,
				int height, int yMin, int yMax, ComplexRootedPolynomial rootPolynom,
				ComplexPolynomial polynomial, ComplexPolynomial derivedPolynomial, int m,
				double convergenceThreshold, double rootTreshold, short[] data) {
			super();
			this.reMin = reMin;
			this.reMax = reMax;
			this.imMin = imMin;
			this.imMax = imMax;
			this.width = width;
			this.height = height;
			this.yMin = yMin;
			this.yMax = yMax;
			this.rootPolynom = rootPolynom;
			this.polynomial = polynomial;
			this.derivedPolynomial = derivedPolynomial;
			this.maxIter = m;
			this.convergenceThreshold = convergenceThreshold;
			this.rootTreshold = rootTreshold;
			this.data = data;
		}

		@Override
		public Void call() {
			int offset = yMin * width;

			for (int y = yMin; y <= yMax; ++y) {
				for (int x = 0; x < width; ++x) {
					Complex c = map_to_complex_plain(x, y, width, height, reMin, reMax, imMin,
							imMax);
					Complex zn = c;
					Complex zn1 = c;
					for (int iter = 0; iter < maxIter;) {
						Complex numerator = polynomial.apply(zn);
						Complex denominator = derivedPolynomial.apply(zn);
						Complex fraction = numerator.divide(denominator);
						zn1 = zn.sub(fraction);
						double module = zn1.sub(zn).module();
						zn = zn1;
						iter++;
						if (module <= convergenceThreshold) break;
					}
					int index = rootPolynom.indexOfClosestRootFor(zn1, rootTreshold);
					data[offset++] = (short) (index + 1);
				}
			}

			return null;
		}

		/**
		 * Maps a pixel of screen to the part of the complex plain
		 * @param x - pixel x coordinate
		 * @param y - pixel y coordinate
		 * @param width - screen width
		 * @param height - screen height
		 * @param reMin - upper left corner of complex plain x coordinate
		 * @param reMax - lower right corner of complex plain x coordinate
		 * @param imMin - upper left corner of complex plain y coordinate
		 * @param imMax - lower right corner of complex plain y coordinate
		 * @return mapped point of complex plain
		 */
		public static Complex map_to_complex_plain(double x, double y, double width, double height,
				double reMin, double reMax, double imMin, double imMax) {
			return new Complex(reMin + x / width * (reMax - reMin),
					imMax - y / height * (imMax - imMin));
		}

		/**
		 * Calculates a single Newton-Raphson iteration
		 * @param polynomial - polynomial to iterate
		 * @param derived- derived polynomial to iterate
		 * @param c - point in which to iterate
		 * @return iterated point
		 */
		public static Complex calc_iteration(ComplexPolynomial polynomial,
				ComplexPolynomial derived, Complex c) {
			Complex numerator = polynomial.apply(c);
			Complex denominator = derived.apply(c);
			Complex fraction = numerator.divide(denominator);
			return c.sub(fraction);
		}

	}

	/**
	 * Implements a {@link IFractalProducer} that uses {@link JobCalc} to generate data to visualize
	 * the fractal. Generation of data is parallelized by splitting a task of generating data for
	 * the whole screen into multiple tasks where only a one horizontal part of the screen is
	 * calculated.
	 * @author ltomic
	 *
	 */
	public static class MojProducer implements IFractalProducer {

		/**
		 * Polynomial to be used to visualize the fractal
		 */
		private ComplexRootedPolynomial rootPolynomial;
		/**
		 * Pool of threads
		 */
		ExecutorService pool;

		/**
		 * Constructs a {@link MojProducer} with given parameters and initializes the thread pool.
		 * @param rootPolynomial - polynomial used for visualizing the fractal
		 */
		public MojProducer(ComplexRootedPolynomial rootPolynomial) {
			this.rootPolynomial = rootPolynomial;
			this.pool = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors(),
					new ThreadFactory() {

						@Override
						public Thread newThread(Runnable r) {
							Thread t = new Thread(r);
							t.setDaemon(true);
							return t;
						}
					});
		}

		@Override
		public void produce(double reMin, double reMax, double imMin, double imMax, int width,
				int height, long requestNo, IFractalResultObserver observer) {
			System.out.println("Zapocinjem izracun...");
			ComplexPolynomial polynomial = rootPolynomial.toComplexPolynom();
			ComplexPolynomial derivedPolynomial = polynomial.derive();
			int m = 16 * 16 * 16;
			double moduleLimit = 1E-3;
			double rootClosenessLimit = 1E-3;
			short data[] = new short[width * height];
			final int trackNum = 8 * Runtime.getRuntime().availableProcessors();
			int trackHeight = height / trackNum;

			List<Future<Void>> results = new ArrayList<>();

			for (int i = 0; i < trackNum; ++i) {
				int yMin = i * trackHeight;
				int yMax = (i + 1) * trackHeight - 1;
				if (i == trackNum - 1) {
					yMax = height - 1;
				}

				JobCalc job = new JobCalc(reMin, reMax, imMin, imMax, width, height, yMin, yMax,
						rootPolynomial, polynomial, derivedPolynomial, m, moduleLimit,
						rootClosenessLimit, data);
				results.add(pool.submit(job));
			}

			for (Future<Void> job : results) {
				try {
					job.get();
				} catch (InterruptedException | ExecutionException e) {
				}
			}

			System.out.println("Racunanje gotovo. Idem obavijestiti promatraca tj. GUI!");
			observer.acceptResult(data, (short) (rootPolynomial.order() + 1), requestNo);

		}

	}

}
