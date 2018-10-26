package hr.fer.zemris.java.hw16.jvdraw.geomobjects;

/**
 * Visitor for {@link GeometricalObject} in Visitor pattern.
 * @author ltomic
 *
 */
public interface GeometricalObjectVisitor {

	/**
	 * Visits {@link Line}.
	 * @param line - line to visit
	 */
	public abstract void visit(Line line);

	/**
	 * Visits {@link Circle}
	 * @param circle - circle to visit
	 */
	public abstract void visit(Circle circle);

	/**
	 * Visits {@link FilledCircle}
	 * @param filledCircle - filled circleto visit
	 */
	public abstract void visit(FilledCircle filledCircle);
	
	public abstract void visit(Polygon polygon);
}
