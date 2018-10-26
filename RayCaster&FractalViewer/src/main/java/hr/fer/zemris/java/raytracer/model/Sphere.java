package hr.fer.zemris.java.raytracer.model;

/**
 * This is a model of a sphere. Sphere is defined by center, radius and coefficients for the
 * diffuse and reflective component in Phong's model for coloring objects.
 * @author ltomic
 *
 */
public class Sphere extends GraphicalObject  {

	/**
	 * Position of spheres center
	 */
	Point3D center;
	/**
	 * Sphere radius
	 */
	double radius; 
	/**
	 * Coefficient for diffuse component for red color
	 */
	double kdr; 
	/**
	 * Coefficient for diffuse component for green color
	 */
	double kdg; 
	/**
	 * Coefficient for diffuse component for blue color
	 */
	double kdb;
	/**
	 * Coefficient for reflective component for red color
	 */
	double krr; 
	/**
	 * Coefficient for reflective component for green color
	 */
	double krg; 
	/**
	 * Coefficient for reflective component for blue color
	 */
	double krb; 
	/**
	 * 
	 */
	double krn;
	
	/**
	 * Constructs a Sphere object with provided parameters
	 * @param center - sphere center position
	 * @param radius - sphere radius
	 * @param kdr - coefficient for diffuse component for red color
	 * @param kdg - coefficient for diffuse component for green color
	 * @param kdb - coefficient for diffuse component for blue color
	 * @param krr - coefficient for reflective component for red color
	 * @param krg - coefficient for reflective component for green color
	 * @param krb - coefficient for reflective component for blue color
	 * @param krn - coefficient n for reflective component
	 */
	public Sphere(Point3D center, double radius, double kdr, double kdg, double kdb, double krr, double krg, double krb,
			double krn) {
		super();
		this.center = center;
		this.radius = radius;
		this.kdr = kdr;
		this.kdg = kdg;
		this.kdb = kdb;
		this.krr = krr;
		this.krg = krg;
		this.krb = krb;
		this.krn = krn;
	}
	
	/**
	 * Specific {@link RayIntersection} class representing intersection
	 * between a {@link Sphere} and {@link Ray}
	 * @author ltomic
	 *
	 */
	private class SphereRayIntersection extends RayIntersection {
		
		/**
		 * Constructs a {@link SphereRayIntersection} with provided arguments
		 * @param point - point of intersection
		 * @param distance - distance between origin of ray and point of intersection
		 * @param outer - is intersection outer
		 */
		public SphereRayIntersection(Point3D point, double distance, boolean outer) {
			super(point, distance, outer);
		}
		
		@Override
		public Point3D getNormal() {
			return super.getPoint().sub(center).normalize();
		}

		@Override
		public double getKdr() {
			return kdr;
		}

		@Override
		public double getKdg() {
			return kdg;
		}

		@Override
		public double getKdb() {
			return kdb;
		}

		@Override
		public double getKrr() {
			return krr;
		}

		@Override
		public double getKrg() {
			return krg;
		}

		@Override
		public double getKrb() {
			return krb;
		}

		@Override
		public double getKrn() {
			return krn;
		}
		
	}
	
	@Override
	public RayIntersection findClosestRayIntersection(Ray ray) {
		Point3D centerToOrigin = ray.start.sub(center);
		double tmp = ray.direction.scalarProduct(centerToOrigin);
		double deter = tmp*tmp - Math.pow(centerToOrigin.norm(), 2) + radius*radius;
		double precision = 1E-6;
		
		if (deter-precision < 0) return null;
		double sqrtDeter = Math.sqrt(deter);
		
		double firstDistance = -tmp + sqrtDeter;
		double secondDistance = -tmp - sqrtDeter;
		Point3D firstIntersection = firstDistance-precision < 0 ?
				null : ray.start.add(ray.direction.scalarMultiply(firstDistance));
		Point3D secondIntersection = secondDistance-precision < 0 ?
				null : ray.start.add(ray.direction.scalarMultiply(secondDistance));
		
		Point3D intersection = (secondIntersection == null || 
				(firstIntersection != null && firstDistance < secondDistance)) ?
				firstIntersection : secondIntersection;
		
		if (intersection == null) return null;
		
		double distance = ray.start.sub(intersection).norm();
		boolean outer = distance > intersection.sub(center).norm();
		return new SphereRayIntersection(intersection, distance, outer);
	}

	
}
