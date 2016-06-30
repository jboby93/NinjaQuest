package com.jboby93.jgl;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Point;

public class UILabel extends UIElement {
	private Color color;
	public Color getColor() { return color; }
	public void setColor(Color value) { color = value; }
	
	private Font font;
	public Font getFont() { return font; }
	public void setFont(Font value) { font = value; }
	
	private String text;
	public String getText() { return text; }
	public void setText(String value) {text = value; }
	
	public UILabel() { this("", new Point(12, 12), Color.black, UIElement.getDefaultFont()); }
	public UILabel(String text) { this(text, new Point(12, 12), Color.black, UIElement.getDefaultFont()); }
	public UILabel(String text, Point location) { this(text, location, Color.black, UIElement.getDefaultFont()); }
	public UILabel(String text, Point location, Color fontColor) { this(text, location, fontColor, UIElement.getDefaultFont()); }
	public UILabel(String text, Point location, Color fontColor, Font font) {
		this.text = text;
		this.loc = location;
		this.color = fontColor;
		this.font = font;
	} //end constructor
	
	@Override
	public void draw(Buffer b) {
		//get each line of the text
		String t[] = this.text.split("\n");
		
		//get the height of the font
		FontMetrics fm = b.getFontMetrics(this.font);
		int h = fm.getHeight();
		int y = loc.y;
		
		for(String s : t) {
			b.drawText(s,
					new Point(this.loc.x, y),
					this.color, this.font);
			y += h + 4;
		} //end for
	} //end draw()
} //end class UILabel
