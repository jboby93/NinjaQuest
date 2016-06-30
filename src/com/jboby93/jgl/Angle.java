package com.jboby93.jgl;

/**
 * An object used to represent an angle in both degrees and radians
 * @author Jace
 *
 */
public class Angle {
	private double deg;
	
	/**
	 * Gets the angle in degrees
	 * @return
	 */
	public double getDegrees() { return deg; }
	
	/**
	 * Gets the angle in radians
	 * @return
	 */
	public double getRadians() { return deg * (Math.PI / 180); }
	
	/**
	 * Sets the angle in degrees
	 * @param value
	 */
	public void setDegrees(double value) { deg = value; }
	
	/**
	 * Sets the angle in radians
	 * @param value
	 */
	public void setRadians(double value) { deg = value * ( 180 / Math.PI); }
	
	/**
	 * Creates a new angle using degrees
	 * @param degrees
	 */
	public Angle(double degrees) {
		this(degrees, false);
	}
	
	/**
	 * Creates a new angle using the given value as either degrees or radians
	 * @param value
	 * @param isRadians If true, the provided value is interpreted as radians
	 */
	public Angle(double value, boolean isRadians) {
		if(isRadians) {
			deg = value * (180 / Math.PI);
		} else {
			deg = value;
		}
	}
	
	public void addDegrees(double value) {
		deg += value;
	}
	
	public void subtractDegrees(double value) {
		deg -= value;
	}
	
	//TODO test add/subtract-Radians() methods
	public void addRadians(double value) {
		deg += (value * ( 180 / Math.PI));
	}
	
	public void subtractRadians(double value) {
		deg -= (value * ( 180 / Math.PI));
	}
	
	public String toString() {
		return this.toString(true);
	}
	
	/**
	 * Returns a string representation of this object
	 * @param returnDegrees If true, only the degrees representation is returned; if false, both degrees and radians are returned
	 * @return
	 */
	public String toString(boolean returnDegrees) {
		if(returnDegrees) {
			return deg + "°";
		} else {
			return (deg * (Math.PI / 180)) + " radians";
		}
	}
}
