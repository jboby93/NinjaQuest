/**
 * Projectile.java: Contains the Projectile definition and logic
 * Author: jboby93
 */

package com.jboby93.ist446demo;

import com.jboby93.jgl.Functions;
import com.jboby93.jgl.GameObject;
import com.jboby93.jgl.Sprite;
import com.jboby93.jgl.Vector;

import java.awt.Image;
import java.awt.Point;
import java.awt.Rectangle;

public class Projectile extends GameObject {
	int type;
	int damage; //damage done on contact
	boolean dead;
	boolean friendly; //if true, the projectile does not hurt the player
	
	public int getDamage() { return damage; }
	public boolean isDead() { return dead; }
	public boolean isFriendly() { return friendly; }
	
	//rate of movement for each projectile type
	public static final int starSpeed = 10;
	public static final int boneSpeed = 9;
	public static final int fireSpeed = 10;
	public static final int magicSpeed = 9;
	
	public static Image bone[];
	public static Image star[];
	public static Image fire[];
	public static Image magic[];
	
	public static void loadSprites() {
		bone = new Image[] {Functions.loadImageFromFile("resources/images/projectiles/bone1.png"),
				Functions.loadImageFromFile("resources/images/projectiles/bone2.png"),
				Functions.loadImageFromFile("resources/images/projectiles/bone3.png"),
				Functions.loadImageFromFile("resources/images/projectiles/bone4.png")};
		star = new Image[] {Functions.loadImageFromFile("resources/images/projectiles/star1.png"),
				Functions.loadImageFromFile("resources/images/projectiles/star2.png"),
				Functions.loadImageFromFile("resources/images/projectiles/star3.png")};
		fire = new Image[] {Functions.loadImageFromFile("resources/images/projectiles/fire1.png"),
				Functions.loadImageFromFile("resources/images/projectiles/fire2.png"),
				Functions.loadImageFromFile("resources/images/projectiles/fire3.png"),
				Functions.loadImageFromFile("resources/images/projectiles/fire4.png")};
		magic = new Image[] {Functions.loadImageFromFile("resources/images/projectiles/magic1.png"),
				Functions.loadImageFromFile("resources/images/projectiles/magic2.png")};
		App.log("Projectile.loadSprites(): projectile sprites loaded");
	} //end loadSprites()
	
	//type constants
	public static final int typeBone = 1;
	public static final int typeStar = 2;
	public static final int typeFire = 3;
	public static final int typeMagic = 4;
	
	/**
	 * Creates a new projectile
	 * @param type
	 * @param origin
	 * @param direction 1 = North, 2 = South, 3 = East, 4 = West
	 */
	public Projectile(int type, Point origin, int direction) {
		this.type = type;
		this.dead = false;
		
		int speed = 0;
		
		//set sprite and damage based on projectile type
		switch(this.type) {
		case typeBone: //skeleton's bone
			this.friendly = false;
			this.damage = 1;
			speed = boneSpeed;
			
			this.setWidth(24);
			this.setHeight(24);
			this.sprite = new Sprite(bone);
			this.sprite.setFPS(10);
			break;
		case typeStar: //player's star
			this.friendly = true;
			this.damage = 1;
			speed = starSpeed;
			
			this.setWidth(16);
			this.setHeight(16);
			this.sprite = new Sprite(star);
			this.sprite.setFPS(15);
			
			//System.out.println("Projectile(): created new player projectile");
			
			break;
		case typeFire: //boss fire
			this.friendly = false;
			this.damage = 2;
			speed = fireSpeed;
			
			this.setWidth(32);
			this.setHeight(32);
			this.sprite = new Sprite(fire);
			this.sprite.setFPS(9);
			break;
		case typeMagic:
			this.friendly = false;
			this.damage = 1;
			speed = magicSpeed;
			
			this.setWidth(32);
			this.setHeight(32);
			this.sprite = new Sprite(magic);
			this.sprite.setFPS(9);
			break;
		} //end switch (determine projectile type)
		
		//set origin point
		this.setCenter(origin);
		
		//set velocity
		this.isVelocityOn = true;
		switch(direction) {
		case 1: //north
			this.velocity = new Vector(0, -speed);
			break;
		case 2: //south
			this.velocity = new Vector(0, speed);
			break;
		case 3: //east
			this.velocity = new Vector(speed, 0);
			break;
		case 4: //west
			this.velocity = new Vector(-speed, 0);
			break;
		}
		this.sprite.startAnimating();
	} //end constructor
	
	public void update() {
		//update the projectile's location
		super.update();
		
		//are we out of the playing area?
		if(this.isOutOfBounds(new Rectangle(0, 0, 800, 480))) {
			this.dead = true; return;
		}
		
		//hit a wall?
		for(GameObject w : World.getCurrentRoom().walls) {
			if(this.isCollidingWith(w)) { this.dead = true; return; }
		}
		
		//are we a non-friendly projectile colliding with the player?
		if(!this.friendly && this.isCollidingWith(Game.player)) {
			Game.player.takeDamage(this.damage); this.dead = true; return;
		}
		
		//are we a friendly projectile colliding with a monster?
		for(Monster m : World.getCurrentRoom().monsters) {
			if(this.friendly && this.isCollidingWith(m)) { m.takeDamage(this.damage); this.dead = true; return; }
		}
	} //end update
} //end class Projectile
