package com.jboby93.jgl;

import java.awt.Font;
import java.awt.Point;
import javax.swing.JLabel;

public abstract class UIElement {
	protected Point loc;
	public Point getLocation() { return loc; }
	public void setLocation(Point value) { loc = value; }
	
	public static Font getDefaultFont() { return new JLabel().getFont();}
	
	public abstract void draw(Buffer b);
} //end class UIElement
