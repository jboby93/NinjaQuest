/**
 * Gate.java: Contains the definition of the Gate world object
 * Author: jboby93
 * 
 * Gates are unlocked by key items with the corresponding key ID.
 */

package com.jboby93.ist446demo;

import java.awt.Image;
import java.awt.Point;

import com.jboby93.jgl.Functions;
import com.jboby93.jgl.GameObject;

public class Gate extends GameObject {
	public static Image spr;
	public static void loadSprites() {
		spr = Functions.loadImageFromFile("resources/images/items/gate.png");
		
		App.log("Gate.loadSprite(): gate sprite loaded");
	} //end loadSprites()
	
	private int key;
	public int getKeyID() { return this.key; }
	private boolean dead = false;
	public boolean isDead() { return this.dead; }
	
	public Gate(int keyID, Point location) {
		super(spr, location);
		key = keyID;
	} //end constructor
	
	public void update() {
		//the gate doesn't move so there's no need to call super.update()
		//all we need to check is collisions with the player
		if(this.isCollidingWith(Game.player)) {
			//and does mr. harry potter have his key....
			if(Game.player.keys.contains(this.key)) {
				//very well, very well.
				int i = Game.player.keys.indexOf(this.key);
				Game.player.keys.remove(i);
				this.dead = true;
			} else {
				//thieves beware...
				Game.player.setLocation(Game.player.getPreviousLocation());
				//Game.player.bounceOffOf(this);
			} //end if
		} //end collision with player check
	} //end update()
} //end class Gate
