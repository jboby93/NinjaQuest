package com.jboby93.jgl;

import javax.imageio.*;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;

/**
 * Some various functions
 * @author Jace
 */
public class Functions {
	// ======== IMAGE LOADING
	/**
	 * Loads and returns an image from the given filename
	 * @param file
	 * @return
	 */
	public static Image loadImageFromFile(String file) {
		BufferedImage i = null;
		try {
			i = ImageIO.read(new File(file));
		} catch(IOException e) {
			//error
			System.err.println("Functions.loadImageFromFile(): IOException thrown: " + e);
			e.printStackTrace();
		}
		
		return i;
	} //end loadImageFromFile()
	
	/**
	 * Loads and returns an image with the given resource name
	 * @param resource
	 * @return
	 */
	public static Image loadImageFromResource(String resource) {
		BufferedImage i = null;
		
		try(InputStream input = Functions.class.getResourceAsStream(resource)) {
			i = ImageIO.read(input);
		} catch(IOException e) {
			System.err.println("Functions.loadImageFromFile(): IOException thrown: " + e);
			e.printStackTrace();
		}
		
		return i;
	} //end loadImageFromResource()
	
	
	// ======== COLLISION CHECKING
	/**
	 * Determines if there is a collision between two GameObjects.
	 * @param A
	 * @param B
	 * @return
	 * @deprecated You should use GameObject.isCollidingWith() instead.
	 */
	public static boolean checkCollision(GameObject A, GameObject B) {
		return A.isCollidingWith(B);
	} //end checkCollision()
	
	public static boolean checkSolidCollision(GameObject A, GameObject B) {
		//TODO checkSolidCollision - needed?
		
		return false;
	}
	
	
	/**
	 * Various math-related functions
	 * @author Jace
	 */
	public static class Math {
		/**
		 * 
		 * @param bounds
		 * @param rotation The angle in radians
		 * @return
		 */
		public static Rectangle getRotatedBoundingBox(Rectangle bounds, double rotation) {
			AffineTransform t = new AffineTransform();
			
			t.rotate(rotation, bounds.x + (bounds.width / 2), bounds.y + (bounds.height / 2));
			
			return t.createTransformedShape(bounds).getBounds();
			//return null;
		} //end getRotatedBoundingBox()
		
		//TODO JavaDocs for getRotatedBoundingBox()
		public static Rectangle getRotatedBoundingBox(Rectangle bounds, double rotation, Point rotationAnchor) {
			AffineTransform t = new AffineTransform();
			
			t.rotate(rotation, bounds.x + rotationAnchor.x, bounds.y + rotationAnchor.y);
			
			return t.createTransformedShape(bounds).getBounds();
		}
		
		public static Rectangle getRotatedBoundingBox(GameObject obj) {
			AffineTransform t = new AffineTransform();
			
			Rectangle b = new Rectangle(obj.x, obj.y, (int)(obj.getWidth() * obj.getScaleFactorX()),
					(int)(obj.getHeight() * obj.getScaleFactorY()));
			
			t.rotate(obj.rotation.getRadians(), obj.x + (obj.rotationAnchor.x),
					obj.y + (obj.rotationAnchor.y));
			
			
			return t.createTransformedShape(b).getBounds();
		}
		
		/**
		 * Returns the distance between the center-points of two GameObjects
		 * @param A
		 * @param B
		 * @return
		 */
		public static double getDistanceBetweenObjects(GameObject A, GameObject B) {
			return java.lang.Math.sqrt(java.lang.Math.pow(B.getCenter().x - A.getCenter().x, 2)
					+ java.lang.Math.pow(B.getCenter().y - A.getCenter().y, 2));
		}
		
		/**
		 * Returns the distance between two points
		 * @param A
		 * @param B
		 * @return
		 */
		public static double getDistanceBetweenPoints(Point A, Point B) {
			return java.lang.Math.sqrt(java.lang.Math.pow(B.x - A.x, 2) + java.lang.Math.pow(B.y - A.y, 2));
		}
		
		/**
		 * Returns a vector pointing from the center of one GameObject to the center of another GameObject
		 * @param A
		 * @param B
		 * @return
		 */
		public static Vector getVectorBetweenObjects(GameObject A, GameObject B) {
			return new Vector(A.getCenter(), B.getCenter());
		}
		
		/**
		 * Returns a vector pointing from one point to another
		 * @param A
		 * @param B
		 * @return
		 */
		public static Vector getVectorBetweenPoints(Point A, Point B) {
			return new Vector(A, B);
		}
		
		/**
		 * Returns the point that lies midway between two points
		 * @param A
		 * @param B
		 * @return
		 */
		public static Point getMidpoint(Point A, Point B) {
			return new Point((A.x + B.x) / 2, (A.y + B.y) / 2);
		}
		
		/**
		 * Returns a vector pointing from one point to another, with the given maximum length.<br>
		 * <br>
		 * If the length of the vector from 'from' to 'to' is longer than the specified maximum length, the vector is assigned the length 'maxLength'.
		 * @param from
		 * @param to
		 * @param maxLength
		 * @return
		 */
		public static Vector getVectorInDirection(Point from, Point to, double maxLength) {
			double dX = to.x - from.x;
			double dY = to.y - from.y;
			
			//calculate angle
			double theta = java.lang.Math.atan2(dY, dX);
			
			double vX = maxLength * java.lang.Math.cos(theta);
			double vY = maxLength * java.lang.Math.sin(theta);
			
			return new Vector(vX, vY);
		}
		
		/**
		 * 
		 * @param chanceOfTrue
		 * @return
		 */
		public static boolean chance(int chanceOfTrue) {
			java.util.Random r = new java.util.Random();
			int c = r.nextInt(101); //get int between 0 and 100
			return (c < chanceOfTrue);
		} //end chance() // this can probably be made a one-liner
		
		/**
		 * Simulates a coin flip by returning a random boolean value (equivalent to (new java.util.Random()).nextBoolean())
		 * @return true if heads, false if tails
		 */
		public static boolean flipCoin() {
			java.util.Random r = new java.util.Random();
			return r.nextBoolean();
		}
	} //end class Functions.Math
} //end class Functions
