/**
 * Skeleton.java: Contains the Skeleton monster definition and logic methods
 * Author: jboby93
 * 
 * A skeleton wanders the map toward the player, causing damage on contact.
 * A skeleton can also throw bone projectiles at the player when the player
 * is in the line of fire.
 */

package com.jboby93.ist446demo;

import java.awt.Point;
import java.awt.Image;
import java.awt.Rectangle;

import com.jboby93.jgl.Functions;
import com.jboby93.jgl.GameObject;
import com.jboby93.jgl.Sprite;
import com.jboby93.jgl.Vector;
public class Skeleton extends Monster {

	private static Image sprLeft[];
	private static Image sprRight[];
	private static Image sprUp[];
	private static Image sprDown[];
	
	private int facing = 2; //1 = north, 2 = south, 3 = east, 4 = west
	public int getFacingDirection() { return facing; }
	
	public static final int speed = 2;
	public static final int maxHP = 2;
	
	public static void loadSprites() {
		sprLeft = new Image[] {Functions.loadImageFromFile("resources/images/mobs/skeleton/skeleton_W_walk1.png"),
				Functions.loadImageFromFile("resources/images/mobs/skeleton/skeleton_W_walk2.png")};
		sprRight = new Image[] {Functions.loadImageFromFile("resources/images/mobs/skeleton/skeleton_E_walk1.png"),
				Functions.loadImageFromFile("resources/images/mobs/skeleton/skeleton_E_walk2.png")};
		sprUp = new Image[] {Functions.loadImageFromFile("resources/images/mobs/skeleton/skeleton_N_walk1.png"),
				Functions.loadImageFromFile("resources/images/mobs/skeleton/skeleton_N_walk2.png")};
		sprDown = new Image[] {Functions.loadImageFromFile("resources/images/mobs/skeleton/skeleton_S_walk1.png"),
				Functions.loadImageFromFile("resources/images/mobs/skeleton/skeleton_S_walk2.png")};
		
		App.log("Skeleton.loadSprites(): skeleton sprites loaded");
	} //end loadSprites()
	
	public Skeleton(Point location) {
		super(sprDown, location);
		this.hp = maxHP;
		
		//choose a random facing direction
		facing = (new java.util.Random()).nextInt(4) + 1; //random int from 1-4
		switch(facing) {
		case 1: //north
			this.sprite = new Sprite(sprUp);
			this.velocity = new Vector(0, -speed);
			break;
		case 2: //south
			this.sprite = new Sprite(sprDown);
			this.velocity = new Vector(0, speed);
			break;
		case 3: //east
			this.sprite = new Sprite(sprRight);
			this.velocity = new Vector(speed, 0);
			break;
		case 4: //west
			this.sprite = new Sprite(sprLeft);
			this.velocity = new Vector(-speed, 0);
			break;
		}
		
		this.sprite.setFPS(6);
		this.isVelocityOn = true;
		
		this.contactDamage = 2;
	}
	
	private int ticks = 0;
	private boolean cooling = false; //cooling off to prevent rapid fire
	private int cooloffTime = 1000; //1 second between shots
	
	@Override
	public void update() {
		ticks++;
		super.baseUpdate(); //can't you hear that boom, ba-boom boom boom, ba-boom boom base? GOT DAT SUPER.BASE()
		
		// moves straight in one of 4 directions
		// changes randomly with 5% change each few calls to update()
		// changes on collision with wall
		// tries to move toward player if possible
		// if player is in line of fire, throw bone straight at player (in the appropriate NESW direction)
		// on collision with player, set tempInvincible flag and move player away from skeleton
		
		for(GameObject wall : World.getCurrentRoom().walls) {
			if(this.isCollidingWith(wall)) {
				//he did the monster mash!
				
				//reset tick counter; we don't want to randomly change direction when we
				//are trying to move away from the wall we collided with
				ticks = 0;
				
				//get direction of movement
				if(this.isMovingUp()) {
					//make us not collide with the wall
					this.y = wall.getBottom();
					
					//move either left or right
					if(Game.player.isRightOf(this)) {
						//player is to the right
						facing = 3; //move east
						this.velocity = new Vector(speed, 0);
					} else {
						//player is to the left
						facing = 4; //move west
						this.velocity = new Vector(-speed, 0);
					}
				} else if(this.isMovingDown()) {
					//make us not collide with the wall
					this.setBottom(wall.y);
					
					//move either left or right
					if(Game.player.isRightOf(this)) {
						//player is to the right
						facing = 3; //move east
						this.velocity = new Vector(speed, 0);
					} else {
						//player is to the left
						facing = 4; //move west
						this.velocity = new Vector(-speed, 0);
					}					
				} else if(this.isMovingLeft()) {
					//make us not collide with the wall
					this.x = wall.getRight();
					
					//move either up or down
					if(Game.player.isBelow(this)) {
						//player is below
						facing = 2; //south
						this.velocity = new Vector(0, speed);
					} else {
						//player is above
						facing = 1; //north
						this.velocity = new Vector(0, -speed);
					}
				} else if(this.isMovingRight()) {
					//make us not collide with the wall
					this.setRight(wall.x);
					
					//move either up or down
					if(Game.player.isBelow(this)) {
						//player is below
						facing = 2; //south
						this.velocity = new Vector(0, speed);
					} else {
						//player is above
						facing = 1; //north
						this.velocity = new Vector(0, -speed);
					}
				} //end if (direction of movement check)
				
				//it was a graveyard smash!
				this.setSprite();
			} //end if (collision with wall test)
		} //end for (walls)
		
		//exiting the room?
		if(this.isOutOfBounds(new Rectangle(0, 0, 800, 480))) {
			//reverse the velocity in whatever direction we're moving so we go back in
			this.velocity = new Vector(-this.velocity.x, -this.velocity.y);
			if(this.isMovingUp()) facing = 1;
			if(this.isMovingDown()) facing = 2;
			if(this.isMovingRight()) facing = 3;
			if(this.isMovingLeft()) facing = 4;
			this.setSprite();
		} //end if (out of bounds check)
		
		//random chance of changing direction every 15th tick
		if(ticks >= 15 && Functions.Math.chance(5)) {
			ticks = 0;
			
			//move in a direction toward the player
			if(this.isMovingUp()) {
				//move either left or right
				if(Game.player.isRightOf(this)) {
					//player is to the right
					facing = 3; //move east
					this.velocity = new Vector(speed, 0);
				} else {
					//player is to the left
					facing = 4; //move west
					this.velocity = new Vector(-speed, 0);
				}
			} else if(this.isMovingDown()) {
				//move either left or right
				if(Game.player.isRightOf(this)) {
					//player is to the right
					facing = 3; //move east
					this.velocity = new Vector(speed, 0);
				} else {
					//player is to the left
					facing = 4; //move west
					this.velocity = new Vector(-speed, 0);
				}					
			} else if(this.isMovingLeft()) {
				//move either up or down
				if(Game.player.isBelow(this)) {
					//player is below
					facing = 2; //south
					this.velocity = new Vector(0, speed);
				} else {
					//player is above
					facing = 1; //north
					this.velocity = new Vector(0, -speed);
				}
			} else if(this.isMovingRight()) {
				//move either up or down
				if(Game.player.isBelow(this)) {
					//player is below
					facing = 2; //south
					this.velocity = new Vector(0, speed);
				} else {
					//player is above
					facing = 1; //north
					this.velocity = new Vector(0, -speed);
				}
			} //end if (direction of movement check)
			
			this.setSprite();
		} //end if (random chance of changing direction)
		
		//collision with the player?
		if(this.isCollidingWith(Game.player)) {
			Game.player.takeDamage(this.contactDamage);
		}
		
		//now:
		// 1. are we facing the player?
		// 2. is the player in the line of fire?
		if(!cooling && this.isLookingAt(Game.player)) {
			//fire a projectile in this direction
			Game.projectiles.add(new Projectile(Projectile.typeBone, this.getCenter(), facing));
			
			//now, we need to cool off so we don't flood the room with projectiles
			cooling = true;
			Thread t = new Thread(new Runnable() {
				@Override
				public void run() {
					try {
						Thread.sleep(cooloffTime);
					} catch(Exception e) {
						//do nothing
					}
					cooling = false;
				} //end run()
			}); //end thread definition
			t.start();
		} //end if (should fire projectile)
	} //end update()
	
	//determines if an object is in the skeleton's line of fire
	private boolean isLookingAt(GameObject obj) {	
		switch(facing) {
		case 1: //north
			return obj.isAbove(this, true);
		case 2: //south
			return obj.isBelow(this, true);
		case 3: //east
			return obj.isRightOf(this, true);
		case 4: //west
			return obj.isLeftOf(this, true);
		default:
			return false;
		} //end switch
	} //end isLookingAt()
	
	//switch the sprite depending on the direction in which the skeleton is looking
	private void setSprite() {
		boolean wasAnimating = this.sprite.isAnimating();
		this.sprite.stopAnimating();
		
		if(facing == -1) facing = (new java.util.Random()).nextInt(4) + 1; //random int from 1-4
			
		switch(facing) {
		case 1: //north
			this.sprite.setCurrentFrameIndex(0);
			this.sprite = new Sprite(sprUp);
			break; 
		case 2: //south
			this.sprite.setCurrentFrameIndex(0);
			this.sprite = new Sprite(sprDown);
			break;
		case 3: //east
			this.sprite.setCurrentFrameIndex(0);
			this.sprite = new Sprite(sprRight);
			break;
		case 4: //west
			this.sprite.setCurrentFrameIndex(0);
			this.sprite = new Sprite(sprLeft);
			break;
		}
		
		this.sprite.setFPS(6);
		if(wasAnimating) this.sprite.startAnimating();
	} //end setSprite()
} //end class Skeleton
