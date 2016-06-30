package com.jboby93.jgl;

import java.awt.Point;

/**
 * Represents a vector in 2D space
 * @author Jace
 *
 */
public class Vector {
	/**
	 * The x-component of this vector
	 */
	public double x;
	/**
	 * The y-component of this vector
	 */
	public double y;
	
	/**
	 * Creates a new vector with the given x- and y-components
	 * @param _x
	 * @param _y
	 */
	public Vector(double _x, double _y) {
		x = _x; y = _y;
	}
	
	/**
	 * Creates a new vector pointing from one point to another
	 * @param from
	 * @param to
	 */
	public Vector(Point from, Point to) {
		x = to.x - from.x;
		y = to.y - from.y;
	}
	
	/**
	 * Creates a new vector from the given length and angle
	 * @param length
	 * @param angle
	 */
	public Vector(double length, Angle angle) {
		x = length * Math.cos(angle.getRadians());
		y = length * Math.sin(angle.getRadians());
		
	}
	
	/**
	 * Gets the length of this vector
	 * @return
	 */
	public double getLength() {
		return Math.sqrt((x * x) + (y * y));
	}
	
	/**
	 * Sets the length of this vector while preserving its angle
	 * @param value
	 */
	public void setLength(double value) {
		double theta = this.getAngle().getRadians();
		x = value * Math.cos(theta);
		y = value * Math.sin(theta);
	}
	
	/**
	 * Gets the angle of this vector
	 * @return
	 */
	public Angle getAngle() {
		return new Angle(Math.atan2(y, x), true);
	}
	
	/**
	 * Sets the angle of this vector while preserving its length
	 * @param value
	 */
	public void setAngle(Angle value) {
		double length = this.getLength();
		double theta = value.getRadians();
		
		x = length * Math.cos(theta);
		y = length * Math.sin(theta);
	}
	
	/**
	 * Gets a normal vector
	 * @return
	 */
	public Vector getNormal() {
		return new Vector(x / getLength(), y / getLength());
	}
	
	public static Vector Normalize(Vector v) {
		return new Vector(v.x / v.getLength(), v.y / v.getLength());
	}
	
	/**
	 * Adds another vector to this one
	 * @param v
	 */
	public void add(Vector v) {
		x += v.x;
		y += v.y;
	}
	
	/**
	 * Subtracts a vector from this one
	 * @param v
	 */
	public void subtract(Vector v) {
		x -= v.x;
		y -= v.y;
	}
	
	/**
	 * Scales this vector by multiplying each component with a constant
	 * @param c
	 */
	public void scale(double c) {
		x = x * c;
		y = y * c;
	}
	
	/**
	 * Scales this vector using the given scale factors
	 * @param xScale
	 * @param yScale
	 */
	public void scale(double xScale, double yScale) {
		x = x * xScale;
		y = y * yScale;
	}
	
	/**
	 * Determines whether this vector has the same components as another vector
	 * @param v
	 * @return
	 */
	public boolean equals(Vector v) {
		return (x == v.x) && (y == v.y); 
	}
	
	/**
	 * Returns the dot product of two vectors
	 * @param a
	 * @param b
	 * @return
	 */
	public static double getDotProduct(Vector a, Vector b) {
		Angle theta = Vector.getAngleBetweenVectors(a, b);
		return (a.getLength() * b.getLength() * Math.cos(theta.getRadians()));
	}
	
	/**
	 * Returns the angle between two vectors
	 * @param v1
	 * @param v2
	 * @return
	 */
	public static Angle getAngleBetweenVectors(Vector v1, Vector v2) {
		if(v1.getLength() == 0 || v2.getLength() == 0) {
			return new Angle(0);
		} else {
			return new Angle(Math.acos(((v1.x * v2.x) + (v1.y * v2.y)) / (v1.getLength() * v2.getLength())), true);
		}
	}
	
	/**
	 * Gets the string representation of this vector: <x, y>
	 */
	public String toString() {
		return "<" + x + ", " + y + ">";
	}
}
