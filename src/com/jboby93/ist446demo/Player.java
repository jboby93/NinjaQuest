/**
 * Player.java: Contains the Player definition, logic, and drawing
 * Author: jboby93
 */

package com.jboby93.ist446demo;

import java.awt.Color;
import java.awt.Font;
import java.awt.Image;
import java.awt.Point;
import java.awt.Rectangle;
import java.util.ArrayList;

import com.jboby93.jgl.Buffer;
import com.jboby93.jgl.Functions;
import com.jboby93.jgl.GameObject;
import com.jboby93.jgl.Vector;

public class Player extends GameObject {
	public int hp;
	public boolean isDead() { return hp <= 0; }
	
	public ArrayList<Item> inventory; //this is unused currently
	public ArrayList<Integer> keys;   //the list of key IDs that the player has
	public int ammo; 				  //the amount of ammo the player has
	
	public static final int speed = 5; 		//rate of movement (moves this many pixels on each game tick)
	public static final int maxHP = 10;
	public static final int maxAmmo = 100;
	
	private static Image sprLeft[];
	private static Image sprLeftStill[];
	
	private static Image sprRight[];
	private static Image sprRightStill[];
	
	private static Image sprUp[];
	private static Image sprUpStill[];
	
	private static Image sprDown[];
	private static Image sprDownStill[];
	
	//HUD sprites
	private static Image ui_heart;
	private static Image ui_halfheart;
	private static Image ui_emptyheart;
	private static Image ui_star;
	
	/**
	 * 0 = north, 1 = east, 2 = south, 3 = west
	 */
	private int facing = 1; //initially facing east
	
	//because I effed up when coding the direction IDs earlier during development
	public int getAimDirection() {
		if(facing == 0) return 1; //never
		if(facing == 1) return 3; //eat
		if(facing == 2) return 2; //soggy
		if(facing == 3) return 4; //waffles
		
		//this should never happen
		return 1;
	} //end getAimDirection()
	
	public static void loadSprites() {
		sprLeftStill = new Image[] {Functions.loadImageFromFile("resources/images/player/player_W_still.png")};
		sprRightStill = new Image[] {Functions.loadImageFromFile("resources/images/player/player_E_still.png")};
		sprUpStill = new Image[] {Functions.loadImageFromFile("resources/images/player/player_N_still.png")};
		sprDownStill = new Image[] {Functions.loadImageFromFile("resources/images/player/player_S_still.png")};
		
		sprLeft = new Image[] {Functions.loadImageFromFile("resources/images/player/player_W_walk1.png"),
				Functions.loadImageFromFile("resources/images/player/player_W_walk2.png")};
		sprRight = new Image[] {Functions.loadImageFromFile("resources/images/player/player_E_walk1.png"),
				Functions.loadImageFromFile("resources/images/player/player_E_walk2.png")};
		sprUp = new Image[] {Functions.loadImageFromFile("resources/images/player/player_N_walk1.png"),
				Functions.loadImageFromFile("resources/images/player/player_N_walk2.png")};
		sprDown = new Image[] {Functions.loadImageFromFile("resources/images/player/player_S_walk1.png"),
				Functions.loadImageFromFile("resources/images/player/player_S_walk2.png")};
		
		ui_heart = Functions.loadImageFromFile("resources/images/ui/heart.png");
		ui_halfheart = Functions.loadImageFromFile("resources/images/ui/heart_half.png");
		ui_emptyheart = Functions.loadImageFromFile("resources/images/ui/heart_empty.png");
		ui_star = Functions.loadImageFromFile("resources/images/ui/star.png");
		
		App.log("Player.loadSprites(): player and HUD sprites loaded");
	} //end loadSprites()
	
	public Player(Point location) {
		super(sprRightStill, location);
		
		//the sprites are 32x32 and I was too lazy to resize them all to 48x48
		this.setScaleFactorX(1.25);
		this.setScaleFactorY(1.25);
		
		this.sprite.setFPS(8);
		
		this.hp = 10;
		this.ammo = 0;
		
		this.keys = new ArrayList<Integer>();
		App.log("Player(): initialized the player");
	}
	
	public void fireProjectile() {
		if(this.ammo > 0) {
			Game.projectiles.add(new Projectile(Projectile.typeStar, this.getCenter(), this.getAimDirection()));
			ammo--;
		}
	} //end fireProjectile()
	
	private boolean immune = false; //if true, no damage can be taken and player is drawn slightly transparent
	public void takeDamage(int damage) {
		if(immune) return;
		
		hp -= damage;
		if(hp < 0) { hp = 0; } else {
			//grant immunity for 2 seconds
			immune = true;
			
			//launch a new thread that simply sleeps, and when done sleeping, sets immune = false
			Thread t = new Thread(new Runnable() {
				@Override
				public void run() {
					try {
						Thread.sleep(2000);
					} catch(Exception e) {
						//do nothing
					}
					
					immune = false;
				} //end run()
			}); //end thread definition
			
			t.start();
		} //end if (hp < 0)
	} //end takeDamage()

	private Point prev; //stores the location of the player before super.update() is called
	public Point getPreviousLocation() { return prev; }
	
	public void update() {
		//capture the current location
		prev = this.getLocation();
		
		//call GameObject.update() which updates the player's location
		super.update();
		
		//set facing direction
		if(isMovingUp()) facing = 0;
		if(isMovingRight()) facing = 1;
		if(isMovingDown()) facing = 2;
		if(isMovingLeft()) facing = 3;
		
		//update the sprite
		switch(facing) {
		case 0: //north
			if(velocity.equals(new Vector(0, 0))) {
				//not moving
				sprite.setCurrentFrameIndex(0);
				sprite.setFrames(sprUpStill);
			} else {
				sprite.setFrames(sprUp);
			}
			break;
		case 1: //east
			if(velocity.equals(new Vector(0, 0))) {
				//not moving
				sprite.setCurrentFrameIndex(0);
				sprite.setFrames(sprRightStill);
			} else {
				sprite.setFrames(sprRight);
			}
			break;
		case 2: //south
			if(velocity.equals(new Vector(0, 0))) {
				//not moving
				sprite.setCurrentFrameIndex(0);
				sprite.setFrames(sprDownStill);
			} else {
				sprite.setFrames(sprDown);
			}
			break;
		case 3: //west
			if(velocity.equals(new Vector(0, 0))) {
				//not moving
				sprite.setCurrentFrameIndex(0);
				sprite.setFrames(sprLeftStill);
			} else {
				sprite.setFrames(sprLeft);
			}
			break;
		} //end switch
	} //end update()
	
	//overridden draw() method to also draw the HUD
	public void draw(Buffer b) {
		//if immune, draw the sprite partially transparent
		this.sprite.opacity = (immune ? 0.50 : 1.0);
		
		//draw the player sprite
		super.draw(b);
		
		//draw the ui
		b.setDrawOpacity(0.5f);
		b.fillRect(Color.black, new Rectangle(0, 428, 280, 52));
		b.resetDrawOpacity();
		
		int totalHP = hp;
		int h = 5;
		int fullHearts = (int) Math.floor(hp / 2);
		int heartX = 12;
		for(int i = 1; i <= fullHearts; i++) {
			b.drawImage(ui_heart, new Point(heartX, 440));
			heartX += 34; //32 + 2
			h--;
		}
		totalHP -= fullHearts * 2;
		if(totalHP % 2 == 1) { b.drawImage(ui_halfheart, new Point(heartX, 440)); totalHP--; heartX += 34; h--; }
		//any empties?
		while(h > 0) {
			h--;
			b.drawImage(ui_emptyheart, new Point(heartX, 440));
			heartX += 34;
		}
		
		//draw ammo display now
		b.drawImage(ui_star, new Point(heartX, 440));
		if(this.ammo >= 10) {
			//draw in white text
			b.drawText("" + this.ammo, new Point(heartX + 34, 468), Color.white, new Font(Font.SANS_SERIF, Font.BOLD, 36));
		} else {
			//draw in yellow if > 1, or red if = 0
			b.drawText("" + this.ammo, new Point(heartX + 34, 468), 
					(this.ammo == 0 ? Color.red : Color.yellow), new Font(Font.SANS_SERIF, Font.BOLD, 36));
		} //end if (draw ammo text)		
		
		//TODO: draw the keys currently held by the player
	} //end draw()
} //end class Player
