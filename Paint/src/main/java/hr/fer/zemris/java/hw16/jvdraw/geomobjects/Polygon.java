package hr.fer.zemris.java.hw16.jvdraw.geomobjects;

import java.awt.Color;
import java.awt.Point;
import java.util.ArrayList;
import java.util.List;

import hr.fer.zemris.java.hw16.jvdraw.Vector3;
import hr.fer.zemris.java.hw16.jvdraw.editors.GeometricalObjectEditor;
import hr.fer.zemris.java.hw16.jvdraw.editors.PolygonEditor;

public class Polygon extends GeometricalObject {
	
	public static final int MINIMAL_POINT_DISTANCE = 3;
	
	private Color outlineColor;
	private Color fillColor;
	private List<Point> points = new ArrayList<>();
	
	public Polygon() {
	}

	public Polygon(Color outlineColor, Color fillColor, List<Point> points) {
		super();
		this.outlineColor = outlineColor;
		this.fillColor = fillColor;
		this.points = points;
	}

	@Override
	public void accept(GeometricalObjectVisitor v) {
		v.visit(this);
	}

	@Override
	public GeometricalObjectEditor createGeometricalObjectEditor() {
		return new PolygonEditor(this);
	}

	@Override
	public String toDoc() {
		StringBuilder doc = new StringBuilder("FPOLY ");
		doc.append(points.size() + " ");
		
		points.forEach((pt) -> {
			doc.append(pt.x + " " + pt.y + " ");
		});
		
		doc.append(outlineColor.getRed() + " " + outlineColor.getGreen() + " " + outlineColor.getBlue() + " ");
		doc.append(fillColor.getRed() + " " + fillColor.getGreen() + " " + fillColor.getBlue());
		
		return doc.toString();
	}
	
	public void addPoint(Point pt) {
		points.add(pt);
	}
	
	public void removeLastPoint() {
		points.remove(points.size()-1);
	}
	
	public int numberOfPoints() {
		return points.size();
	}
	
	public Point getPoint(int index) {
		return points.get(index);
	}
	
	public boolean isConvexWithNewPoint(Point pt) {
		points.add(pt);
		boolean result = isConvex();
		points.remove(points.size()-1);
		
		return result;
	}
	
	public boolean isConvex() {
		return isConvex(points);
	}
	
	public static boolean isConvex(List<Point> points) {
		int flag = 0;
		
		for (int i = 0, sz = points.size(); i < sz; ++i) {
			Point a = points.get(i);
			Point b = points.get((i+1) % sz);
			Point c = points.get((i+2) % sz);
			Vector3 first = new Vector3(b.x - a.x, b.y - a.y, 0);
			Vector3 second = new Vector3(c.x - a.x, c.y - a.y, 0);
			double z = first.cross(second).getZ();
			
			if (z == 0) continue;
			if (flag == 0) {
				flag = z > 0 ? 1 : -1;
				continue;
			}
			
			if (flag > 0 && z < 0 || flag < 0 && z > 0) return false;
		}
		
		return true;
	}

	public Color getOutlineColor() {
		return outlineColor;
	}

	public void setOutlineColor(Color outlineColor) {
		this.outlineColor = outlineColor;
	}

	public Color getFillColor() {
		return fillColor;
	}

	public void setFillColor(Color fillColor) {
		this.fillColor = fillColor;
	}

	public List<Point> getPoints() {
		return points;
	}

	public void setPoints(List<Point> points) {
		this.points = points;
	}
	
	@Override
	public String toString() {
		StringBuilder repr = new StringBuilder("Polygon :");
		points.forEach((pt) -> {
			repr.append("(" + pt.x + "," + pt.y + ") ");
		});
		
		return repr.toString();
	}

}
