package hr.fer.zemris.java.raytracer;

import hr.fer.zemris.java.raytracer.model.GraphicalObject;
import hr.fer.zemris.java.raytracer.model.LightSource;
import hr.fer.zemris.java.raytracer.model.Point3D;
import hr.fer.zemris.java.raytracer.model.Ray;
import hr.fer.zemris.java.raytracer.model.RayIntersection;
import hr.fer.zemris.java.raytracer.model.Scene;

public class RayTracer {

	private static final double PRECISION = 1E-6;

	public static void compute(int yMin, int yMax, int width, int height, Point3D xAxis, Point3D yAxis, Point3D zAxis,
			double horizontal, double vertical, Point3D screenCorner, Scene scene, Point3D eye, short[] red,
			short[] green, short[] blue) {
		int offset = yMin * width;
		short[] rgb = new short[3];
		for (int y = yMin; y <= yMax; y++) {
			for (int x = 0; x < width; x++) {
				Point3D screenPoint = calcScreenPoint(screenCorner, x, y, xAxis, yAxis, 
						width, height, horizontal, vertical);
				Ray ray = Ray.fromPoints(eye, screenPoint);
				tracer(scene, ray, eye, rgb);
				red[offset] = rgb[0] > 255 ? 255 : rgb[0];
				green[offset] = rgb[1] > 255 ? 255 : rgb[1];
				blue[offset] = rgb[2] > 255 ? 255 : rgb[2];
				offset++;
			}
		}
	}

	public static Point3D calcScreenPoint(Point3D screenCorner, int x, int y, 
			Point3D xAxis, Point3D yAxis, int width, int height, double horizontal, double vertical) {
		return screenCorner.add(xAxis.scalarMultiply(x / (width - 1.0) * horizontal))
				.sub(yAxis.scalarMultiply(y / (height - 1.0) * vertical));
	}

	public static void tracer(Scene scene, Ray ray, Point3D eye, short[] rgb) {
		rgb[0] = rgb[1] = rgb[2] = 0;
		
		RayIntersection closestIntersection = findClosestIntersection(scene, ray);
		if (closestIntersection == null) return;
		
		determineColorFor(scene, closestIntersection, eye, rgb);
	}

	private static void determineColorFor(Scene scene, RayIntersection intersection, Point3D eye, short[] rgb) {
		rgb[0] = rgb[1] = rgb[2] = 15;
		
		for (LightSource i : scene.getLights()) {
			Ray lightRay = Ray.fromPoints(i.getPoint(), intersection.getPoint());
			RayIntersection lightIntersection = findClosestIntersection(scene, lightRay);
			
			double objectIntersectionDistance = intersection.getPoint().sub(i.getPoint()).norm();
			if (lightIntersection != null && objectIntersectionDistance > PRECISION + lightIntersection.getDistance())
				continue;
			calcColorComponents(i, intersection, eye, rgb);
		}
	}

	private static void calcColorComponents(LightSource light, RayIntersection intersection, Point3D eye, short[] rgb) {
		Point3D interPoint = intersection.getPoint();
		Point3D toLight = light.getPoint().sub(interPoint).normalize();
		Point3D toEye = eye.sub(interPoint).normalize();
		Point3D normal = intersection.getNormal();

		Point3D reflected = getReflectedVector(normal, toLight.scalarMultiply(-1)).normalize();

		double coefReflected = reflected.scalarProduct(toEye);
		if (coefReflected + PRECISION < 0) {
			coefReflected = 0;
		}
		coefReflected = Math.pow(coefReflected, intersection.getKrn());
		double coefDiffuse = toLight.scalarProduct(normal);
		if (coefDiffuse + PRECISION < 0) {
			coefDiffuse = 0;
		}
		double red = intersection.getKrr() * coefReflected + coefDiffuse * intersection.getKdr();
		double green = intersection.getKrg() * coefReflected + coefDiffuse * intersection.getKdg();
		double blue = intersection.getKrb() * coefReflected + coefDiffuse * intersection.getKdb();
		red *= light.getR();
		green *= light.getG();
		blue *= light.getB();

		rgb[0] = red + rgb[0] > 255 ? 255 : (short) (red + rgb[0]);
		rgb[1] = green + rgb[1] > 255 ? 255 : (short) (green + rgb[1]);
		rgb[2] = blue + rgb[2] > 255 ? 255 : (short) (blue + rgb[2]);
	}

	private static Point3D getReflectedVector(Point3D normal, Point3D incident) {
		normal = normal.normalize();
		incident = incident.normalize();
		return incident.sub(normal.scalarMultiply(2 * normal.scalarProduct(incident)));
	}

	private static RayIntersection findClosestIntersection(Scene scene, Ray ray) {
		RayIntersection closestIntersection = null;
		double distance = 0;
		
		for (GraphicalObject i : scene.getObjects()) {
			RayIntersection intersection = i.findClosestRayIntersection(ray);
			if (intersection == null) continue;
			if (closestIntersection == null || distance > PRECISION + intersection.getDistance()) {
				closestIntersection = intersection;
				distance = intersection.getDistance();
			}
		}
		return closestIntersection;
	}
}
