package hr.fer.zemris.java.raytracer;

import hr.fer.zemris.java.raytracer.viewer.RayTracerViewer;

import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveAction;

import hr.fer.zemris.java.raytracer.model.*;

/**
 * Shows a predefined scene from the {@link RayTracerViewer} class.
 * @author ltomic
 *
 */
public class RayCasterParallel {

	/**
	 * Method that is called at the beginning of the program. Creates a
	 * predefines scene from the {@link RayTracerViewer} with following parameters :
	 * eye : (10, 0, 0)
	 * view : (0, 0, 0)
	 * view-up vector : (0, 0, 10)
	 * horizontal-width : 20
	 * vertical-width : 20
	 * 
	 * @param args - no arguments are expected
	 */
	public static void main(String[] args) {
		RayTracerViewer.show(getIRayTracerProducer(),
				new Point3D(10, 0, 0), 
				new Point3D(0, 0, 0),
				new Point3D(0, 0, 10), 
				20, 20);
	}

	/**
	 * Uses divide-and-conquer approach to cast rays to the scene thus parallelizing
	 * the process. The approach splits the task of ray casting of whole screen into
	 * tasks of ray casting for the screen segments with threshold as maximum height.
	 * @author ltomic
	 *
	 */
	public static class Job extends RecursiveAction {

		private static final long serialVersionUID = 1L;
		/**
		 * Starting coordinate on y-axis
		 */
		int yMin;
		/**
		 * Ending coordinate on y-axis (inclusive)
		 */
		int yMax;
		/**
		 * Width of whole screen
		 */
		int width;
		/**
		 * Height of whole screen
		 */
		int height;
		/**
		 * Normalized vector representing x-axis
		 */
		Point3D xAxis;
		/**
		 * Normalized vector representing y-axis
		 */
		Point3D yAxis;
		/**
		 * Normalized vector representing z-axis
		 */
		Point3D zAxis;
		/**
		 * Horizontal width of observed space
		 */
		double horizontal;
		/**
		 * Vertical width of observed space
		 */
		double vertical;
		/**
		 * Coordinates of upper left screen corner 
		 */
		Point3D screenCorner;
		/**
		 * Model of the scene that contains information about all objects and light sources
		 * in the scene.
		 */
		Scene scene;
		/**
		 * Position of the observer.
		 */
		Point3D eye;
		/**
		 * Red component of pixel color
		 */
		short[] red;
		/**
		 * Green component of pixel color
		 */
		short[] green;
		/**
		 * Blue component of pixel color
		 */
		short[] blue;
		/**
		 * Tasks screen segment maximum height
		 */
		static final int threshold = 16;

		public Job(int yMin, int yMax, int width, int height, Point3D xAxis, Point3D yAxis,
				Point3D zAxis, double horizontal, double vertical, Point3D screenCorner,
				Scene scene, Point3D eye, short[] red, short[] green, short[] blue) {
			super();
			this.yMin = yMin;
			this.yMax = yMax;
			this.width = width;
			this.height = height;
			this.xAxis = xAxis;
			this.yAxis = yAxis;
			this.zAxis = zAxis;
			this.horizontal = horizontal;
			this.vertical = vertical;
			this.screenCorner = screenCorner;
			this.scene = scene;
			this.eye = eye;
			this.red = red;
			this.green = green;
			this.blue = blue;
		}

		@Override
		protected void compute() {
			if (yMax - yMin + 1 <= threshold) {
				computeDirect();
				return;
			}
			invokeAll(
					new Job(yMin, yMin + (yMax - yMin) / 2, width, height, xAxis, yAxis, zAxis,
							horizontal, vertical, screenCorner, scene, eye, red, green, blue),
					new Job(yMin + (yMax - yMin) / 2 + 1, yMax, width, height, xAxis, yAxis, zAxis,
							horizontal, vertical, screenCorner, scene, eye, red, green, blue));
		}

		/**
		 * Calls the method that does the task.
		 */
		public void computeDirect() {
			RayTracer.compute(yMin, yMax, width, height, xAxis, yAxis, zAxis, horizontal, vertical,
					screenCorner, scene, eye, red, green, blue);
		}

	}

	/**
	 * Returns a implementation of {@link IRayTracerProducer} that parallelizes the
	 * process of ray casting that uses a Phong's model for coloring.
	 * @return
	 */
	private static IRayTracerProducer getIRayTracerProducer() {
		return new IRayTracerProducer() {
			@Override
			public void produce(Point3D eye, Point3D view, Point3D viewUp, double horizontal,
					double vertical, int width, int height, long requestNo,
					IRayTracerResultObserver observer) {
				System.out.println("Započinjem izračune...");
				short[] red = new short[width * height];
				short[] green = new short[width * height];
				short[] blue = new short[width * height];
				Point3D zAxis = view.sub(eye).normalize();
				Point3D yAxis = viewUp.sub(zAxis.scalarMultiply(zAxis.scalarProduct(viewUp)))
						.normalize();
				Point3D xAxis = zAxis.vectorProduct(yAxis).normalize();

				Point3D screenCorner = view.sub(xAxis.scalarMultiply(horizontal / 2))
						.add(yAxis.scalarMultiply(vertical / 2));

				Scene scene = RayTracerViewer.createPredefinedScene();

				ForkJoinPool pool = new ForkJoinPool();
				pool.invoke(new Job(0, height - 1, width, height, xAxis, yAxis, zAxis, horizontal,
						vertical, screenCorner, scene, eye, red, green, blue));
				pool.shutdown();

				System.out.println("Izračuni gotovi...");
				observer.acceptResult(red, green, blue, requestNo);
				System.out.println("Dojava gotova...");
			}
		};
	}
}
