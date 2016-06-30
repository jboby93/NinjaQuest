/**
 * Buffer.java - a simple double buffer object
 * Author: jboby93
 */

package com.jboby93.jgl;

import java.awt.AlphaComposite;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Graphics;
import java.awt.RenderingHints;
import java.awt.Stroke;
import java.awt.image.BufferedImage;

import javax.swing.JLabel;

//TODO parameter descriptions for JavaDocs

/**
 * An object providing double-buffered drawing
 * @author Jace
 *
 */
public class Buffer {
	Graphics window;
	Graphics2D buffer;
	BufferedImage back;
	
	int w; int h;
	int offsetX; int offsetY;
	
	/**
	 * Returns the width of the graphics buffer
	 * @return
	 */
	public int getWidth() { return w; }
	/**
	 * Returns the height of the graphics buffer
	 * @return
	 */
	public int getHeight() { return h; }
	
	public int getOffsetX() { return offsetX; }
	public int getOffsetY() { return offsetY; }
	
	public Buffer(Component c, int width, int height, int drawOffsetX, int drawOffsetY) {
		LibInfo.dbg("Buffer(): creating Buffer object linked to component  = " + c.toString());
		LibInfo.dbg("Buffer(): width = " + width + "; height = " + height);
		LibInfo.dbg("Buffer(): drawOffsetX = " + drawOffsetX + "; drawOffsetY = " + drawOffsetY);
		
		w = width;
		h = height;
		offsetX = drawOffsetX;
		offsetY = drawOffsetY;
		
		LibInfo.dbg("Buffer(): getting Graphics context from window");
		window = c.getGraphics();
		
		LibInfo.dbg("Buffer(): initializing back buffer image");
		back = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
		buffer = (Graphics2D)back.getGraphics();
		
		window.fillRect(0, 0, width, height);
		this.clear(Color.black);
		//this.setAntiAliasing(true);
		//this.setRenderingQuality(true);
	} //end constructor
	
	/**
	 * Clears the internal buffer with the given color
	 * @param c
	 */
	public void clear(Color c) {
		buffer.setColor(c);
		buffer.fillRect(0, 0, w, h);
		//buffer.fillRect(offsetX, offsetY, w, h);
	}
	
	/**
	 * Draws the contents of the internal buffer to the window or component with which this Buffer was initialized
	 */
	public void render() {
		window.drawImage(back, offsetX, offsetY, null);
	}
	
	// =================== DRAWING FUNCTIONS ==================== //
	// ======== STROKES (PEN / DRAWING STYLE)
	// ======== [SEE Pen.Java]
	/*public void setStroke(Stroke s) {
		buffer.setStroke(s);
	}
	
	public void resetStroke() {
		buffer.setStroke(new BasicStroke());
	}
	*/
	public static Stroke createStroke(float width) {
		return Buffer.createStroke(width, BasicStroke.CAP_BUTT);
	}
	
	public static Stroke createStroke(float width, int cap) {
		return new BasicStroke(width, cap, BasicStroke.JOIN_BEVEL);
	}
	
	// ======== EFFECTS
	/**
	 * Sets the opacity of future draw operations
	 * @param opacity
	 */
	public void setDrawOpacity(float opacity) {
		buffer.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, opacity));
	}
	
	/**
	 * Resets the opacity of future draw operations to 1.0f (100% opaque)
	 */
	public void resetDrawOpacity() {
		buffer.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1.0f));
	}
	
	public void setAntiAliasing(boolean use) {
		buffer.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
				(use ? RenderingHints.VALUE_ANTIALIAS_ON : RenderingHints.VALUE_ANTIALIAS_OFF)
			);
	}
	
	public void setRenderingQuality(boolean preferQuality) {
		buffer.setRenderingHint(RenderingHints.KEY_RENDERING,
				(preferQuality ? RenderingHints.VALUE_RENDER_QUALITY : RenderingHints.VALUE_RENDER_SPEED)
			);
	}
	
	// ======== SHAPES
	
	public void drawPoint(Color c, Point p) {
		buffer.setColor(c);
		buffer.setStroke(new BasicStroke());
		buffer.drawOval(p.x, p.y, 1, 1);
	}
	
	/**
	 * Draws the given rectangle using the specified pen
	 * @param p
	 * @param r
	 */
	public void drawRect(Pen p, Rectangle r) {
		buffer.setColor(p.getColor());
		buffer.setStroke(p.getStroke());
		buffer.drawRect(r.x, r.y, r.width, r.height);
	}
	
	/**
	 * Draws a rectangle with the given location and size using the specified pen
	 * @param p
	 * @param location
	 * @param size
	 */
	public void drawRect(Pen p, Point location, Dimension size) {
		buffer.setColor(p.getColor());
		buffer.setStroke(p.getStroke());
		buffer.drawRect(location.x, location.y, size.width, size.height);		
	}
	
	/**
	 * Draws a rectangle with the given location and size using the specified pen
	 * @param p
	 * @param x
	 * @param y
	 * @param width
	 * @param height
	 */
	public void drawRect(Pen p, int x, int y, int width, int height) {
		buffer.setColor(p.getColor());
		buffer.setStroke(p.getStroke());
		buffer.drawRect(x, y, width, height);
	}
	
	/**
	 * Draws a filled rectangle with the given bounds using the specified color
	 * @param c
	 * @param r
	 */
	public void fillRect(Color c, Rectangle r) {
		buffer.setColor(c);
		buffer.fillRect(r.x, r.y, r.width, r.height);
	}
	
	/**
	 * Draws a filled rectangle with the given location and size using the specified color
	 * @param c
	 * @param location
	 * @param size
	 */
	public void fillRect(Color c, Point location, Dimension size) {
		buffer.setColor(c);
		buffer.fillRect(location.x, location.y, size.width, size.height);
	}
	
	/**
	 * Draws a filled and outlined rectangle with the given bounds.  The specified pen determines the outline style, and the color is used to fill the rectangle
	 * @param fill
	 * @param outline
	 * @param r
	 */
	public void drawOutlinedRect(Color fill, Pen outline, Rectangle r) {
		this.fillRect(fill, r);
		this.drawRect(outline, r);
	}
	
	/**
	 * Draws a circle with the given center-point and radius using the specified pen
	 * @param p
	 * @param center
	 * @param radius
	 */
	public void drawCircle(Pen p, Point center, int radius) {
		buffer.setColor(p.getColor());
		buffer.setStroke(p.getStroke());
		
		Rectangle r = new Rectangle(center.x - radius, center.y - radius, 2 * radius, 2 * radius);
		buffer.drawOval(r.x, r.y, r.width, r.height);
	}
	
	/**
	 * Draws a filled circle with the given center-point and radius using the specified color
	 * @param c
	 * @param center
	 * @param radius
	 */
	public void fillCircle(Color c, Point center, int radius) {
		buffer.setColor(c);
		
		Rectangle r = new Rectangle(center.x - radius, center.y - radius, 2 * radius, 2 * radius);
		buffer.fillOval(r.x, r.y, r.width, r.height);
	}
	
	/**
	 * Draws a filled and outlined circle with the given center-point and radius using the specified fill color and outline pen
	 * @param fill
	 * @param outline
	 * @param center
	 * @param radius
	 */
	public void drawOutlinedCircle(Color fill, Pen outline, Point center, int radius) {
		this.fillCircle(fill, center, radius);
		this.drawCircle(outline, center, radius);
	}
	
	/**
	 * Draws an oval within the given bounds using the specified pen
	 * @param p
	 * @param r
	 */
	public void drawOval(Pen p, Rectangle r) {
		buffer.setColor(p.getColor());
		buffer.setStroke(p.getStroke());
		buffer.drawOval(r.x, r.y, r.width, r.height);
	}
	
	/**
	 * Draws an oval with the given center-point and radii using the specified pen
	 * @param p
	 * @param center
	 * @param radiusX
	 * @param radiusY
	 */
	public void drawOval(Pen p, Point center, int radiusX, int radiusY) {
		Point origin = new Point(center.x - radiusX, center.y - radiusY);
		buffer.setColor(p.getColor());
		buffer.setStroke(p.getStroke());
		buffer.drawOval(origin.x, origin.y, 2 * radiusX, 2 * radiusY);
	}
	
	/**
	 * Draws a filled oval within the given bounds using the specified color
	 * @param c
	 * @param r
	 */
	public void fillOval(Color c, Rectangle r) {
		buffer.setColor(c);
		buffer.fillOval(r.x, r.y, r.width, r.height);
	}
	
	/**
	 * Draws a filled oval with the given center-point and radii using the specified color
	 * @param c
	 * @param center
	 * @param radiusX
	 * @param radiusY
	 */
	public void fillOval(Color c, Point center, int radiusX, int radiusY) {
		Point origin = new Point(center.x - radiusX, center.y - radiusY);
		buffer.setColor(c);
		buffer.fillOval(origin.x, origin.y, 2 * radiusX, 2 * radiusY);
	}
	
	/**
	 * Draws a filled and outlined oval with the given bounds using the specified fill color and outline pen
	 * @param fill
	 * @param outline
	 * @param r
	 */
	public void drawOutlinedOval(Color fill, Pen outline, Rectangle r) {
		this.fillOval(fill, r);
		this.drawOval(outline, r);
	}
	
	/**
	 * Draws a filled and outlined oval with the given center-point and radii using the specified fill color and outline pen
	 * @param fill
	 * @param outline
	 * @param center
	 * @param radiusX
	 * @param radiusY
	 */
	public void drawOutlinedOval(Color fill, Pen outline, Point center, int radiusX, int radiusY) {
		this.fillOval(fill, center, radiusX, radiusY);
		this.drawOval(outline, center, radiusX, radiusY);
	}
	
	// ======== TEXT
	
	/**
	 * Draws text to the buffer at the specified location using the default Java-provided font.  The default color is Color.black.
	 * @param str
	 * @param location
	 */
	public void drawText(String str, Point location) {
		this.drawText(str, location, Color.black, this.getSystemFont());
	}
	
	/**
	 * Draws text to the buffer at the specified location using the default Java-provided font in the specified color
	 * @param str
	 * @param location
	 * @param c
	 */
	public void drawText(String str, Point location, Color c) {
		this.drawText(str, location, c, this.getSystemFont());
	}
	
	/**
	 * Draws text to the buffer at the specified location using the specified font and color
	 * @param str
	 * @param location
	 * @param c
	 * @param f
	 */
	public void drawText(String str, Point location, Color c, Font f) {
		buffer.setFont(f);
		buffer.setColor(c);
		buffer.drawString(str, location.x, location.y);
	}
	
	/**
	 * Returns the default font provided by Java.  Equivalent to javax.swing.JLabel.getFont()
	 * @return
	 */
	public Font getSystemFont() {
		return new JLabel().getFont();
	}
	
	public FontMetrics getFontMetrics(Font f) {
		return buffer.getFontMetrics(f);
	}
	
	// ======== IMAGES
	
	/**
	 * Draws an image to the buffer at the given location
	 * @param i
	 * @param location
	 */
	public void drawImage(Image i, Point location) {
		buffer.drawImage(i, location.x, location.y, null);
	}
	
	/**
	 * Draws an image to the buffer with the given location and size
	 * @param i
	 * @param location
	 * @param size
	 */
	public void drawImage(Image i, Point location, Dimension size) {
		buffer.drawImage(i, location.x, location.y, size.width, size.height, null);
	}
	
	/**
	 * Returns a flipped image
	 * @param i The input image
	 * @param flipHorizontal Whether the image should be flipped horizontally across the y-axis
	 * @param flipVertical Whether the image should be flipped vertically across the x-axis
	 * @return
	 */
	public static Image flipImage(Image i, boolean flipHorizontal, boolean flipVertical) {
		BufferedImage f = new BufferedImage(i.getWidth(null), i.getHeight(null), BufferedImage.TYPE_INT_ARGB);
		Graphics2D g = (Graphics2D) f.getGraphics();
		
		int x, y, w, h;
		x = 0; y = 0;
		w = i.getWidth(null); h = i.getHeight(null);
		
		if(flipHorizontal) {
			x += w;
			w = -w;
		}
		
		if(flipVertical) {
			y += h;
			h = -h;
		}
		
		g.drawImage(i, x, y, w, h, null);
		g.dispose();
		
		return f;
	} //end flipImage()
	
} //end Buffer
