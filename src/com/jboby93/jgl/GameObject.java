//TODO file headers for everything in com.jboby93.jgl

package com.jboby93.jgl;

import java.awt.Dimension;
import java.awt.Image;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.geom.Dimension2D;
import java.util.HashMap;
import java.util.Map;

/**
 * An object used to represent an entity in a 2D game world
 * @author Jace
 */
public class GameObject {
	// ======== PHYSICAL PROPERTIES
	/**
	 * The x-coordinate of this object
	 */
	public int x;
	/**
	 * The y-coordinate of this object
	 */
	public int y;
	private int w, h;
	private double scaleW = 1.0, scaleH = 1.0;
	
	private Point initial;
	
	/**
	 * Gets the x-coordinate of the left edge of this object
	 * @return
	 */
	public int getLeft() { return x; }
	/**
	 * Gets the y-coordinate of the top edge of this object
	 * @return
	 */
	public int getTop() { return y; }
	/**
	 * Gets the x-coordinate of the right edge of this object
	 * @return
	 */
	public int getRight() { return x + (int)(w * scaleW); }
	/**
	 * Gets the y-coordinate of the bottom edge of this object
	 * @return
	 */
	public int getBottom() { return y + (int)(h * scaleH); }
	
	/**
	 * Sets the x-coordinate of the left edge of this object
	 * @param value
	 */
	public void setLeft(int value) { x = value; }
	/**
	 * Sets the y-coordinate of the top edge of this object
	 * @param value
	 */
	public void setTop(int value) { y = value; }
	/**
	 * Sets the x-coordinate of the right edge of this object
	 * @param value
	 */
	public void setRight(int value) { x = value - (int)(w * scaleW); }
	/**
	 * Sets the y-coordinate of the bottom edge of this object
	 * @param value
	 */
	public void setBottom(int value) { y = value - (int)(h * scaleH); }
	
	/**
	 * Gets the location of the upper-left corner of this object
	 * @return
	 */
	public Point getLocation() { return new Point(x, y); }
	/**
	 * Sets the location of the upper-left corner of this object
	 * @param value
	 */
	public void setLocation(Point value) { x = value.x; y = value.y; }
	
	/**
	 * Gets the location of the center-point of this object
	 * @return
	 */
	public Point getCenter() {return new Point(x + (int)((w * scaleW) / 2), y + (int)((h * scaleH) / 2)); }
	/**
	 * Sets the location of the center-point of this object
	 * @param value
	 */
	public void setCenter(Point value) {
		x = value.x - (int)((w * scaleW) / 2);
		y = value.y - (int)((h * scaleH) / 2);
	}
	
	/**
	 * Gets the initial location of the object.  This is set when the constructor is called and cannot be changed.
	 * @return
	 */
	public Point getStartLocation() { return initial; }
	
	/**
	 * Gets the width of this object
	 * @return
	 */
	public int getWidth() { return w; }
	/**
	 * Gets the height of this object
	 * @return
	 */
	public int getHeight() { return h; }
	/**
	 * Sets the width of this object
	 * @param value
	 */
	public void setWidth(int value) {
		if(value <= 0) {
			//invalid
		} else {
			w = value;
		}
	}
	/**
	 * Sets the height of this object
	 * @param value
	 */
	public void setHeight(int value) {
		if(value <= 0) {
			//invalid
		} else {
			h = value;
		}
	}
	
	/**
	 * Gets the size of this object
	 * @return
	 */
	public Dimension getSize() { return new Dimension(w, h); }
	/**
	 * Sets the size of this object
	 * @param value
	 */
	public void setSize(Dimension value) {
		if(value.width <= 0 || value.height <= 0) {
			//invalid
		} else {
			w = value.width;
			h = value.height;
		}
	}
	
	/**
	 * Gets the scaling factor in the x-direction
	 * @return
	 */
	public double getScaleFactorX() { return scaleW; }
	/**
	 * Gets the scaling factor in the y-direction
	 * @return
	 */
	public double getScaleFactorY() { return scaleH; }
	/**
	 * Sets the scaling factor in the x-direction
	 * @param value
	 */
	public void setScaleFactorX(double value) { scaleW = value; }
	/**
	 * Sets the scaling factor in the y-direction
	 * @param value
	 */
	public void setScaleFactorY(double value) { scaleH = value; }
	
	/**
	 * Gets the bounding box of this object, taking into account the scale factors
	 * @return
	 */
	public Rectangle getBounds() { return new Rectangle(x, y, (int)(w * scaleW), (int)(h * scaleH)); }
	/**
	 * Sets the location and size of this object given by the provided rectangle
	 * @param value
	 */
	public void setBounds(Rectangle value) {
		if(value.width <= 0 || value.height <= 0) {
			//invalid
		} else {
			x = value.x;
			y = value.y;
			
			//TODO: this doesn't seem right, but nothing has broken
			//so far...
			w = (int)(value.width * scaleW); //w = value.width
			h = (int)(value.height * scaleH); //h = value.height
		}
	}
	
	//add collision bounds to account for rotation
	/**
	 * Gets the bounding box used for collision checking.  This takes both scaling and rotation into account.
	 * @return
	 */
	public Rectangle getCollisionBounds() { return Functions.Math.getRotatedBoundingBox(this); }
	/**
	 * Gets the bounding box of this object, ignoring both rotation and scaling.
	 * @return
	 */
	public Rectangle getUnscaledBounds() { return new Rectangle(x, y, w, h); }
	
	// ======== BEHAVIOR PROPERTIES
	/**
	 * The velocity of this object
	 */
	public Vector velocity;
	/**
	 * Whether velocity is enabled or not.  If this is true, the velocity will be added to the current location when update() is called.
	 */
	public boolean isVelocityOn;
	
	/**
	 * The acceleration of this object
	 */
	public Vector acceleration;
	/**
	 * Whether acceleration is enabled or not.  If this is true, the acceleration will be added to the velocity when update() is called
	 */
	public boolean isAccelerationOn;
	
	/**
	 * Whether this object should be considered a solid object in collision checking
	 */
	public boolean isSolidObject;
	
	/**
	 * Returns true if the object is moving left
	 * @return
	 */
	public boolean isMovingLeft() { return velocity.x < 0; }
	/**
	 * Returns true if the object is moving right
	 * @return
	 */
	public boolean isMovingRight() { return velocity.x > 0; }
	/**
	 * Returns true if the object is moving up
	 * @return
	 */
	public boolean isMovingUp() { return velocity.y < 0; }
	/**
	 * Returns true if the object is moving down
	 * @return
	 */
	public boolean isMovingDown() { return velocity.y > 0; }
	
	/**
	 * A collection of tags.  These are key-value pairs that can be used to store additional information about the object.
	 */
	public Map<String, Object> tags;
	/**
	 * The name of the object
	 */
	public String name;
	
	// ======== VISUAL PROPERTIES
	/**
	 * The sprite used to represent and draw the object.  This can be a primitive shape or image-based.
	 */
	public Sprite sprite;
	
	/**
	 * The rotation of the object
	 */
	public Angle rotation;
	
	/**
	 * The point within the object around which to rotate
	 */
	public Point rotationAnchor;
	
	/**
	 * The origin point of this object's coordinate system, relative to the upper-left corner of the window onto which it is drawn.
	 */
	public Point coordSystemOrigin;
	
	// ======== CONSTRUCTORS
	/**
	 * Creates a new GameObject represented by a box shape at (100, 100) with size (50, 50)
	 */
	public GameObject() {
		//make a box at (100, 100) with size (50, 50)
		this(Sprite.SpriteType.box, 100, 100, 50, 50);
	}
	
	/**
	 * Creates a new GameObject with the given type, location, and size
	 * @param type The type of sprite this object should have
	 * @param x The x-coordinate of this object
	 * @param y The y-coordinate of this object
	 * @param width The width of this object
	 * @param height The height of this object
	 */
	public GameObject(Sprite.SpriteType type, int x, int y, int width, int height) {
		this.x = x; this.y = y; this.w = width; this.h = height;
		if(type == Sprite.SpriteType.image) {
			sprite = new Sprite(type, null);
		} else {
			sprite = new Sprite(type, new ShapeStyle());
		}
		
		this.init();
	}
	
	/**
	 * Creates a new GameObject with the given type, location, size, and drawing style (if a shape-type sprite is chosen)
	 * @param type The type of sprite this object should have
	 * @param x The x-coordinate of this object
	 * @param y The y-coordinate of this object
	 * @param width The width of this object
	 * @param height The height of this object
	 * @param drawStyle
	 */
	public GameObject(Sprite.SpriteType type, int x, int y, int width, int height, ShapeStyle drawStyle) {
		this.x = x; this.y = y; this.w = width; this.h = height;
		if(type == Sprite.SpriteType.image) {
			sprite = new Sprite(type, null);
		} else {
			sprite = new Sprite(type, drawStyle);
		}
		
		this.init();
	}
	
	/**
	 * Creates a new GameObject with the given type, location, and size
	 * @param type
	 * @param location
	 * @param size
	 */
	public GameObject(Sprite.SpriteType type, Point location, Dimension size) {
		this.x = location.x; this.y = location.y;
		this.w = size.width; this.h = size.height;
		if(type == Sprite.SpriteType.image) {
			sprite = new Sprite(type, null);
		} else {
			sprite = new Sprite(type, new ShapeStyle());
		}
		
		this.init();
	}
	
	/**
	 * Creates a new GameObject with the given type, location, size, and drawing style (if a shape-type sprite is chosen)
	 * @param type
	 * @param location
	 * @param size
	 * @param drawStyle
	 */
	public GameObject(Sprite.SpriteType type, Point location, Dimension size, ShapeStyle drawStyle) {
		this.x = location.x; this.y = location.y;
		this.w = size.width; this.h = size.height;
		if(type == Sprite.SpriteType.image) {
			sprite = new Sprite(type, null);
		} else {
			sprite = new Sprite(type, drawStyle);
		}
		
		this.init();
	}
	
	/**
	 * Creates a new GameObject with the given image as the sprite at location (0, 0)
	 * @param img
	 */
	public GameObject(Image img) {
		this.x = 0; this.y = 0;
		this.w = img.getWidth(null); this.h = img.getHeight(null);
		sprite = new Sprite(img);
		
		this.init();
	}
	
	/**
	 * Creates a new GameObject with the given image as the sprite at the given location
	 * @param img
	 * @param location
	 */
	public GameObject(Image img, Point location) {
		this.x = location.x; this.y = location.y;
		this.w = img.getWidth(null); this.h = img.getHeight(null);
		sprite = new Sprite(img);
		
		this.init();
	}
	
	/**
	 * Creates a new GameObject with the given image as the sprite at the given location.  The image will be scaled to fit the provided size
	 * @param img
	 * @param location
	 * @param size
	 */
	public GameObject(Image img, Point location, Dimension size) {
		this.x = location.x; this.y = location.y;
		this.w = img.getWidth(null); this.h = img.getHeight(null);
		sprite = new Sprite(img);
		
		scaleW = size.width / w;
		scaleH = size.height / h;
		
		this.init();
	}
	
	/**
	 * Creates a new GameObject with an animated sprite from the given array of images, at the given location, with a frame rate of 30fps
	 * @param imgs
	 * @param location
	 */
	public GameObject(Image imgs[], Point location) { this(imgs, location, 30); }
	/**
	 * Creates a new GameObject with an animated sprite from the given array of images, at the given location, with the given frame rate
	 * @param imgs
	 * @param location
	 * @param fps
	 */
	public GameObject(Image imgs[], Point location, int fps) {
		this.x = location.x; this.y = location.y;
		this.w = imgs[0].getWidth(null); this.h = imgs[0].getHeight(null);
		sprite = new Sprite(imgs); sprite.setFPS(fps);
		
		this.init();
	}
	
	/**
	 * 
	 * @param imgs
	 * @param location
	 * @param size
	 * @param fps
	 */
	public GameObject(Image imgs[], Point location, Dimension size, int fps) {
		this.x = location.x; this.y = location.y;
		this.w = imgs[0].getWidth(null); this.h = imgs[0].getHeight(null);
		sprite = new Sprite(imgs); sprite.setFPS(fps);
		
		scaleW = size.width / w;
		scaleH = size.height / h;
		
		this.init();
	}
	
	//misc. initialization
	private void init() {
		tags = new HashMap<String, Object>();
		velocity = new Vector(0, 0);
		acceleration = new Vector(0, 0);
		initial = new Point(x, y);
		isSolidObject = false;
		
		//scaleW = 1.0; scaleH = 1.0;
		rotation = new Angle(0);
		rotationAnchor = new Point((int)((this.w * scaleW) / 2), (int)((this.h * scaleH) / 2));
		coordSystemOrigin = new Point(0, 0);
	}
	
	// ======== METHODS
	/**
	 * Draws the GameObject to the given buffer, ignoring rotation and applying scaling factors
	 * @param b The buffer on which to draw the object
	 */
	public void draw(Buffer b) { this.draw(b, true, false); }
	/**
	 * Draws the GameObject to the given buffer, ignoring rotation but applying scaling if specified
	 * @param b The buffer on which to draw the object
	 * @param applyScaling Whether to apply scaling factors to the object when it is drawn
	 */
	public void draw(Buffer b, boolean applyScaling) { this.draw(b, applyScaling, false); }
	/**
	 * Draws the GameObject to the given buffer, using the specified parameters to determine whether to apply scaling and rotation
	 * @param b The buffer on which to draw the object
	 * @param applyScaling Whether to apply scaling factors to the object when it is drawn
	 * @param ignoreRotation Whether to ignore the rotation of the object when it is drawn
	 */
	public void draw(Buffer b, boolean applyScaling, boolean ignoreRotation) {
		Dimension drawSize = new Dimension(
				(applyScaling ? (int)((double)w * scaleW) : w),
				(applyScaling ? (int)((double)h * scaleH) : h)
			);
		
		Point drawAt = new Point(x + coordSystemOrigin.x, y + coordSystemOrigin.y);
		
		if(rotation.getDegrees() == 0 || ignoreRotation) {
			//no OR ignored rotation
			sprite.draw(b, new Rectangle(drawAt, drawSize));
		} else {
			sprite.drawRotated(b, new Rectangle(drawAt, drawSize), rotation.getRadians(), rotationAnchor);
		} //end if
	} //end draw()
	
	/**
	 * Draws the GameObject to the given buffer, using the specified point as the (0, 0) origin.
	 * 
	 * For example: an origin of (50, 50), will draw the object at (50 + this.x, 50 + this.y) on the buffer.
	 * @param b
	 * @param origin
	 */
	public void drawRelativeTo(Buffer b, Point origin) { }
	
	/**
	 * Resets the rotation offset point, setting it to its default value (the center-point of this GameObject)
	 */
	public void resetRotationOffset() {
		rotationAnchor = this.getCenter();
	}
	
	/**
	 * Updates the position of the GameObject based on its velocity and acceleration
	 */
	public void update() {
		if(isVelocityOn) {
			if(isAccelerationOn) {
				velocity.x += acceleration.x;
				velocity.y += acceleration.y;
			}
			
			x += velocity.x;
			y += velocity.y;
		}
	}
	
	/**
	 * Checks whether this GameObject is colliding with another GameObject
	 * @param obj
	 * @return
	 */
	public boolean isCollidingWith(GameObject obj) {
		return this.getCollisionBounds().intersects(obj.getCollisionBounds());
	}
	
	/**
	 * Bounces this GameObject off of another GameObject
	 * @param obj
	 */
	public void bounceOffOf(GameObject obj) {
		//need rotated collision bounds here
		Rectangle cBounds = this.getBounds(); //getRotatedBoundingBox(this.getBounds(), this.rotation);
		double dx = cBounds.x - this.x;
		double dy = cBounds.y - this.y;
		
		//this is essentially the bouncing part of CheckSolidCollision from VBGL4
		boolean xVelFlipped = false;
		boolean yVelFlipped = false;
		
		if(this.x < obj.x) {
			this.velocity.x = -this.velocity.x;
			xVelFlipped = true;
		}
		if(this.getRight() > obj.getRight() && !xVelFlipped) {
			this.velocity.x = -this.velocity.x;
			xVelFlipped = true;
		}
		
		if(this.y < obj.y) {
			this.velocity.y = -this.velocity.y;
			yVelFlipped = true;
		}
		if(this.getBottom() > obj.getBottom() && !yVelFlipped) {
			this.velocity.y = -this.velocity.y;
			yVelFlipped = true;
		}
		
		//fix the vacuum effect:
		//  if we were moving down at time of collision, set bottom coord to top of collision object
		boolean xVacuumFixed = false;
		boolean yVacuumFixed = false;
		
		if(this.isMovingUp() && yVelFlipped) {
			this.setBottom((int)(obj.y - Math.abs(dy)));
			yVacuumFixed = true;
		}
		
		//now for the other 3 directions
		if(!yVacuumFixed && this.isMovingDown() && yVelFlipped) {
			this.y = (int)(obj.getBottom() + Math.abs(dy));
			yVacuumFixed = true;
		}
		
		if(this.isMovingLeft() && xVelFlipped) {
			this.setRight((int)(obj.x - Math.abs(dx)));
			xVacuumFixed = true;
		}
		
		if(!xVacuumFixed && this.isMovingRight() && xVelFlipped) {
			this.x = (int)(obj.getRight() + Math.abs(dx));
			xVacuumFixed = true;
		}
		
		//2-16: can we bounce off of a rotated object with realistic physics?
		if(this.rotation.getDegrees() != 0) {
			
		} //end if (is there rotation?)
	} //end bounceOffOf()
	
	public boolean isLeftOf(GameObject obj) { return this.isLeftOf(obj, false); }
	public boolean isLeftOf(GameObject obj, boolean strict) {
		if(strict) {
			return (this.x < obj.x) && (this.getCenter().y > obj.y) && (this.getCenter().y < obj.getBottom());
		} else {
			return (this.getRight() < obj.getCenter().x);
		}
	}
	
	public boolean isRightOf(GameObject obj) { return this.isRightOf(obj, false); }
	public boolean isRightOf(GameObject obj, boolean strict) {
		if(strict) {
			return (this.getRight() > obj.getRight()) && (this.getCenter().y > obj.y) && (this.getCenter().y < obj.getBottom());
		} else {
			return (this.x > obj.getCenter().x);
		}
	}
	
	public boolean isAbove(GameObject obj) { return this.isAbove(obj, false); }
	public boolean isAbove(GameObject obj, boolean strict) {
		if(strict) {
			return (this.getBottom() < obj.getBottom()) && (this.getCenter().x > obj.x) && (this.getCenter().x < obj.getRight());
		} else {
			return (this.getBottom() < obj.getCenter().y);
		}
	}
	
	public boolean isBelow(GameObject obj) { return this.isBelow(obj, false); }
	public boolean isBelow(GameObject obj, boolean strict) {
		if(strict) {
			return (this.getBottom() > obj.getBottom()) && (this.getCenter().x > obj.x) && (this.getCenter().x < obj.getRight());
		} else {
			return (this.y > obj.getCenter().y);
		}
	}
	
	public boolean isOutOfBounds(Rectangle bounds) { return this.isOutOfBounds(bounds, false); }
	public boolean isOutOfBounds(Rectangle bounds, boolean strict) {
		if(strict) {
			//return ((this.x >= bounds.x) && (this.getRight() <= (bounds.x + bounds.width))) &&
			//	   ((this.y >= bounds.y) && (this.getBottom() <= (bounds.y + bounds.height)));
			return ((this.x < bounds.x) || (this.getRight() > (bounds.x + bounds.width))) ||
					   ((this.y < bounds.y) || (this.getBottom() > (bounds.y + bounds.height)));
		} else {
			//return ((this.getRight() >= bounds.x) && (this.x <= (bounds.x + bounds.width))) &&
			//		((this.getBottom() >= bounds.y) && (this.y <= (bounds.y + bounds.height)));
			return ((this.getRight() < bounds.x) || (this.x > (bounds.x + bounds.width))) ||
					((this.getBottom() < bounds.y) || (this.y > (bounds.y + bounds.height)));
		}
	}
	
	public void dispose() {
		this.sprite.dispose();
	}
}
