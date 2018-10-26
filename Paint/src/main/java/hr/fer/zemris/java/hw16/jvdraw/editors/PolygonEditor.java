package hr.fer.zemris.java.hw16.jvdraw.editors;

import java.awt.GridLayout;
import java.awt.Point;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.JPanel;

import hr.fer.zemris.java.hw16.jvdraw.JColorArea;
import hr.fer.zemris.java.hw16.jvdraw.geomobjects.Polygon;

public class PolygonEditor extends GeometricalObjectEditor {

	private static final long serialVersionUID = 1L;
	
	private static final String COORDINATE_LABEL = "Point %d coordinate %c : ";
	private static final String OUTLINECOLOR_LABEL = "Outline color";
	private static final String FILLCOLOR_LABEL = "Fill color";
	
	private static final String ERROR_POLYGON_CONVEX = "Given polygon is not convex";
	private static final String ERROR_POLYGON_POINT_TOOCLOSE = "Given polygon has neighbor points that are too close!";

	private Polygon polygon;
	
	private JColorArea outlineColorArea;
	private JColorArea fillColorArea;
	private List<JFormattedTextField> pointFields = new ArrayList<>();
	private List<Point> points = new ArrayList<>();
	
	public PolygonEditor(Polygon polygon) {
		this.polygon = polygon;
		
		this.setLayout(new GridLayout(polygon.getPoints().size()+2, 1));
		
		polygon.getPoints().forEach((pt) -> {
			pointFields.add(setupIntegerLabel(String.format(COORDINATE_LABEL, pointFields.size()/2+1, 'X'), pt.x));
			pointFields.add(setupIntegerLabel(String.format(COORDINATE_LABEL, pointFields.size()/2+1, 'Y'), pt.y));	
		});
		
		JPanel outlineColorPanel = new JPanel(new GridLayout(1, 2));
		JLabel outlineColorLabel = new JLabel(OUTLINECOLOR_LABEL);
		outlineColorArea = new JColorArea(polygon.getOutlineColor());
		outlineColorPanel.add(outlineColorLabel);
		outlineColorPanel.add(outlineColorArea);
		
		this.add(outlineColorPanel);
		
		JPanel fillColorPanel = new JPanel(new GridLayout(1, 2));
		JLabel fillColorLabel = new JLabel(FILLCOLOR_LABEL);
		fillColorArea = new JColorArea(polygon.getFillColor());
		fillColorPanel.add(fillColorLabel);
		fillColorPanel.add(fillColorArea);
		
		this.add(outlineColorPanel);
		this.add(fillColorPanel);
	}
	
	@Override
	public void checkEditing() {
		for (int i = 0, sz = pointFields.size(); i < sz; ++i) {
			checkCoordinate(pointFields.get(i), "" + i/2, i % 2 == 1 ? "Y" : "X");
		}

		for (int i = 0, sz = pointFields.size(); i < sz; i += 2) {
			points.add(new Point((int)pointFields.get(i).getValue(), (int)pointFields.get(i+1).getValue()));
		}
		
		if (!Polygon.isConvex(points)) throw new IllegalArgumentException(ERROR_POLYGON_CONVEX);
		for (int i = 0, sz = points.size(); i < sz; ++i) {
			Point a = points.get(i);
			Point b = points.get((i+1) % sz);
			if (a.distance(b) < Polygon.MINIMAL_POINT_DISTANCE) {
				throw new IllegalArgumentException(ERROR_POLYGON_POINT_TOOCLOSE);
			}
		}
	}

	@Override
	public void acceptEditing() {
		polygon.setFillColor(fillColorArea.getCurrentColor());
		polygon.setOutlineColor(outlineColorArea.getCurrentColor());
		polygon.setPoints(points);
	}

}
