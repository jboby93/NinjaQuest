package com.jboby93.jgl;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Point;

public class UIProgressBar extends UIElement {
	private Color back;
	public Color getBackColor() { return back; }
	public void setBackColor(Color value) { back = value; }
	
	private Color fill;
	public Color getFillColor() { return fill; }
	public void setFillColor(Color value) { fill = value; }
	
	private int width, height;
	public int getWidth() { return width; }
	public int getHeight() { return height; }
	public void setWidth(int value) { width = value; }
	public void setHeight(int value) { height = value; }
	
	private int max;
	public int getMaxValue() { return max; }
	public void setMaxValue(int value) { max = value; }
	
	private int value;
	public int getValue() { return value; }
	public void setValue(int value) { this.value = value; }
	
	public double getPercentFilled() { return (value / max) * 100; }
	public void setPercentFilled(double value) { this.value = (int)((value / 100) * this.max); }
	
	public UIProgressBar() { this(new Point(12, 12), 100, 50.0); }
	
	public UIProgressBar(int width, double percentFilled) { this(new Point(12, 12), width, percentFilled); }
	
	public UIProgressBar(Point location, int width, double percentFilled) {
		this.width = width;
		this.loc = location;
		
		this.back = Color.gray;
		this.fill = Color.green;
		
		this.max = 100;
		this.value = (int)(percentFilled);
		this.height = 20;
	}
	
	@Override
	public void draw(Buffer b) {
		//draw the background box
		b.fillRect(back, loc, new Dimension(width, height));
		
		//now, draw the filled portion
		b.fillRect(fill, loc, new Dimension(
				(int)(width * (double)(this.value / this.max)),
				height));
	} //end draw()
	
	public Point getCenter() { return new Point(loc.x + (width / 2), loc.y + (height / 2)); }
	public void setCenter(Point value) { loc = new Point(value.x - (width / 2), value.y - (height / 2)); }
} //end class UIProgressBar
