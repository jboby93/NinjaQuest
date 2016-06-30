package com.jboby93.jgl;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Stroke;

/**
 * An object that stores a Color and a Stroke that can be used to draw shapes
 * @author Jace
 *
 */
public class Pen {
	private Stroke s;
	private Color c;
	
	/**
	 * Gets the color of this pen
	 * @return
	 */
	public Color getColor() { return c; }
	/**
	 * Sets the color of this pen
	 * @param value
	 */
	public void setColor(Color value) { c = value; }
	
	/**
	 * Gets the Stroke object represented by this pen
	 * @return
	 */
	public Stroke getStroke() { return s; }
	/**
	 * Sets the Stroke object represented by this pen
	 * @param value
	 */
	public void setStroke(Stroke value) { s = value; }
	
	/**
	 * Gets the width of this pen
	 * @return
	 */
	public float getWidth() { return ((BasicStroke) s).getLineWidth(); }
	/**
	 * Sets the width of this pen
	 * @param value
	 */
	public void setWidth(float value) { s = new BasicStroke(value, this.getCaps(), BasicStroke.JOIN_BEVEL) ;}
	
	/**
	 * Gets the cap style of this pen.  See BasicStroke.getEndCap()
	 * @return
	 */
	public int getCaps() { return ((BasicStroke) s).getEndCap(); }
	/**
	 * Sets the cap style of this pen
	 * @param value
	 */
	public void setCaps(int value) { s = new BasicStroke(this.getWidth(), value, BasicStroke.JOIN_BEVEL); }
	
	/**
	 * Creates a black pen of width 1
	 */
	public Pen() {
		s = new BasicStroke();
		c = Color.black;
	}
	
	/**
	 * Creates a pen of width 1 with the given color
	 * @param color
	 */
	public Pen(Color color) {
		s = new BasicStroke();
		c = color;
	}
	
	/**
	 * Creates a pen from the given color and stroke
	 * @param color
	 * @param stroke
	 */
	public Pen(Color color, Stroke stroke) {
		s = stroke;
		c = color;
	}
	
	/**
	 * Creates a pen with the given color and width
	 * @param color
	 * @param width
	 */
	public Pen(Color color, float width) {
		s = new BasicStroke(width);
		c = color;
	}
	
	/**
	 * Creates a pen with the given color, width, and cap style (see BasicStroke.getEndCap())
	 * @param color
	 * @param width
	 * @param caps
	 */
	public Pen(Color color, float width, int caps) {
		s = new BasicStroke(width, caps, BasicStroke.JOIN_BEVEL);
		c = color;
	}
} //end class Pen
