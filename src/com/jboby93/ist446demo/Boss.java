/**
 * Boss.java: Contains the Boss monster definition, logic, and drawing methods
 * Author: jboby93
 * 
 * The Boss is a Mage that casts spells directly at the player, and will cast
 * fire spells that spread in each of the four map directions.
 */

package com.jboby93.ist446demo;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Image;
import java.awt.Point;
import java.awt.Rectangle;

import com.jboby93.jgl.Buffer;
import com.jboby93.jgl.Functions;
import com.jboby93.jgl.GameObject;
import com.jboby93.jgl.ShapeStyle;
import com.jboby93.jgl.Sprite;
import com.jboby93.jgl.Vector;

public class Boss extends Monster {

	private static Image sprLeft[];
	private static Image sprRight[];
	private static Image sprUp[];
	private static Image sprDown[];
	private static boolean imageFailed = false;
	
	private int facing = 2; //1 = north, 2 = south, 3 = east, 4 = west
	public int getFacingDirection() { return facing; }
	
	public static final int speed = 2;
	public static final int maxHP = 16;
	
	public static void loadSprites() {
		//I got lazy so the boss looks the same regardless of the direction he is facing
		sprDown = new Image[] {Functions.loadImageFromFile("resources/images/mobs/boss/boss1.png"),
			Functions.loadImageFromFile("resources/images/mobs/boss/boss2.png")};
		sprUp = new Image[] {Functions.loadImageFromFile("resources/images/mobs/boss/boss1.png"),
			Functions.loadImageFromFile("resources/images/mobs/boss/boss2.png")};
		sprLeft = new Image[] {Functions.loadImageFromFile("resources/images/mobs/boss/boss1.png"),
			Functions.loadImageFromFile("resources/images/mobs/boss/boss2.png")};
		sprRight = new Image[] {Functions.loadImageFromFile("resources/images/mobs/boss/boss1.png"),
			Functions.loadImageFromFile("resources/images/mobs/boss/boss2.png")};
		App.log("Boss.loadSprites(): boss sprites loaded");
	} //end loadSprites()
	
	public Boss(Point location) {
		super(sprDown, location);
		this.hp = maxHP;
		
		if(!imageFailed) {
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
			} //end switch
		} else {
			//if image cannot be loaded (to make it easier to debug while the final sprite is not finished
			this.sprite = new Sprite(Sprite.SpriteType.box, new ShapeStyle(new Color(0, 0, 128), new Color(0, 0, 128)));
			//why does this work? recall: animation functions do nothing if the sprite is not animatable
		} // end if
				
		this.sprite.setFPS(6);
		this.isVelocityOn = true;
		this.contactDamage = 3;
	} //end constructor

	private int fireTick = 0;
	private int fireWaitTime = 1000;
	private boolean fireWaiting = false;
	private int magicTick = 0;
	private int ticks = 0;
	
	public void takeDamage(int value) {
		super.takeDamage(value);
		
		if(hp == 0) {
			Game.bossDefeated = true;
		}
	} //end takeDamage()
	
	@Override
	public void update() {
		magicTick++;
		if(!fireWaiting) fireTick++;
		ticks++;
		super.baseUpdate();
		
		//the universal mob AI (I should really move this to some function in Monster)
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
		}
		
		//random chance of changing direction every 15th tick
		if(ticks >= 20 && Functions.Math.chance(5)) {	
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
		
		//now, finally
		//the boss AI
		//60 ticks = 1 second
		if(fireTick > 120) {
			fireTick = 0;
			if(!fireWaiting && Functions.Math.chance(55)) {
				//create the fire in each direction
				Game.projectiles.add(new Projectile(Projectile.typeFire, this.getCenter(), 1));
				Game.projectiles.add(new Projectile(Projectile.typeFire, this.getCenter(), 2));
				Game.projectiles.add(new Projectile(Projectile.typeFire, this.getCenter(), 3));
				Game.projectiles.add(new Projectile(Projectile.typeFire, this.getCenter(), 4));
				
				fireWaiting = true;
				Thread t = new Thread(new Runnable() {
					@Override
					public void run() {
						try {
							Thread.sleep(fireWaitTime);
						} catch(Exception e) {
							//do nothing
						}
						
						fireWaiting = false;
					} //end run
				}); //end thread declaration
				
				t.start();
			} //end if (chance)
		} //end if (fire)
		
		if(magicTick > 90) {
			magicTick = 0;
			if(Functions.Math.chance(75)) {
				//create the new spell
				Projectile p = new Projectile(Projectile.typeMagic, this.getCenter(), 0);
				
				p.velocity = Functions.Math.getVectorInDirection(this.getCenter(), Game.player.getCenter(), Projectile.magicSpeed);
				
				Game.projectiles.add(p);
			} //end if (chance)
		} //end if (magic)		
	} //end update()
	
	public void draw(Buffer b) {
		super.draw(b);
		
		//draw a health bar at the top of the screen
		//x = 150, y = 12
		//width = 500, height = 10
		int fillW = (int) Math.ceil(((double)this.hp / (double)maxHP) * 500);
		
		//draw the background bar
		b.setDrawOpacity(0.5f);
		b.fillRect(Color.black, new Point(150, 12), new Dimension(500, 10));
		b.resetDrawOpacity();
		
		//draw the health portion of the bar
		b.fillRect((((double)this.hp / (double)maxHP) < 0.4 ? Color.red : Color.yellow), new Point(150, 12), new Dimension(fillW, 10));
		
		//draw a label in the middle
		
	} //end draw()
	
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
	
	private void setSprite() {
		if(imageFailed) return; //do nothing if there are no sprites
		
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
} //end class Boss
