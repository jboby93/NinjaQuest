package com.jboby93.jgl;

import java.awt.Color;

//TODO JavaDocs
public class ShapeStyle {
	public Pen outline;
	public Color fill;
	public boolean drawOutline;
	public boolean drawFill;
	
	public ShapeStyle() {
		outline = new Pen();
		fill = Color.white;
		
		drawOutline = true;
		drawFill = true;
	}
	
	public ShapeStyle(Color fillColor, Color outlineColor) {
		fill = fillColor;
		outline = new Pen(outlineColor);
		
		drawOutline = true;
		drawFill = true;
	}
}
