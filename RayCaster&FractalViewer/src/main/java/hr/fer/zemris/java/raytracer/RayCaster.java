package hr.fer.zemris.java.raytracer;

import hr.fer.zemris.java.raytracer.viewer.RayTracerViewer;

import hr.fer.zemris.java.raytracer.model.*;

/**
 * Shows a predefined scene from the {@link RayTracerViewer} class.
 * @author ltomic
 *
 */
public class RayCaster {

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
	 * Returns a implementation of {@link IRayTracerProducer} that calculates
	 * the color of the pixels for the given scene.
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

				RayTracer.compute(0, height - 1, width, height, xAxis, yAxis, zAxis, horizontal,
						vertical, screenCorner, scene, eye, red, green, blue);

				System.out.println("Izračuni gotovi...");
				observer.acceptResult(red, green, blue, requestNo);
				System.out.println("Dojava gotova...");
			}
		};
	}
}
