package com.jboby93.jgl;

import java.awt.Point;

//TODO  JavaDocs
public class PolarCoord {
	private double angle; //radians
	private double r;
	private Point origin;
	
	public double getRadius() { return r; }
	public void setRadius(double value) { r = value; }
	
	public Point getOrigin() { return origin; }
	public void setOrigin(Point value) { origin = value; }
	
	public double getRadians() { return angle; }
	public double getDegrees() { return (180 / Math.PI); }
	
	public void setRadians(double value) { angle = value; }
	public void setDegrees(double value) { angle = value * (Math.PI / 180); }
	
	public double getX() { return origin.x + (r * Math.cos(angle)); }
	public double getY() { return origin.y + (r * Math.sin(angle)); }
	
	public PolarCoord(double radius, double degrees) {
		r = radius;
		angle = degrees * (Math.PI / 180);
		
		origin = new Point(0, 0);
	}
	
	public PolarCoord(double radius, Angle angle) {
		r = radius;
		this.angle = angle.getRadians();
		
		origin = new Point(0, 0);
	}
	
	public static PolarCoord fromXY(double x, double y) {
		double a = Math.atan2(y, x);
		
		//are we in quadrant 2?
		if(x < 0 && y >= 0) a += Math.PI;
		//3?
		if(x < 0 && y < 0) a += Math.PI;
		//4?
		if(x >= 0 && y < 0) a += (2 * Math.PI);
		
		return new PolarCoord(Math.sqrt(Math.pow(x, 2) + Math.pow(y, 2)), new Angle(a, true));
	}
	
	public static PolarCoord fromXY(int x, int y) {
		double a = Math.atan2(y, x);
		
		//are we in quadrant 2?
		if(x < 0 && y >= 0) a += Math.PI;
		//3?
		if(x < 0 && y < 0) a += Math.PI;
		//4?
		if(x >= 0 && y < 0) a += (2 * Math.PI);
		
		return new PolarCoord(Math.sqrt(Math.pow(x, 2) + Math.pow(y, 2)), new Angle(a, true));
	}
	
	public static PolarCoord fromXY(Point point) {
		double a = Math.atan2(point.y, point.x);
		
		//are we in quadrant 2?
		if(point.x < 0 && point.y >= 0) a += Math.PI;
		//3?
		if(point.x < 0 && point.y < 0) a += Math.PI;
		//4?
		if(point.x >= 0 && point.y < 0) a += (2 * Math.PI);
		
		return new PolarCoord(Math.sqrt(Math.pow(point.x, 2) + Math.pow(point.y, 2)), new Angle(a, true));
	}
	
	public Point toXY() {
		return new Point((int)this.getX(), (int)(this.getY()));
	}
}
