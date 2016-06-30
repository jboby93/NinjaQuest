/**
 * Monster.java: The base class for all monsters in the game
 * Author: jboby93
 */

package com.jboby93.ist446demo;

import java.awt.Image;
import java.awt.Point;
import com.jboby93.jgl.GameObject;

/**
 * The base class of all monsters.  Create a new monster by using this as a base class for a new monster class.
 * @author Jace
 *
 */
public abstract class Monster extends GameObject {
	public Monster(Image img[], Point location) {
		super(img, location);
	}
	
	/**
	 * Calls the GameObject.update() method to update location from velocity and acceleration
	 */
	public void baseUpdate() { super.update(); }
	
	/**
	 * Override this to add specialized logic and AI to your monster
	 */
	public abstract void update();
	
	protected Type type;
	public Type getType() { return this.type; }
	public enum Type {zombie, skeleton, boss};
	
	protected int hp;
	public int getHP() { return hp; }
	
	public void takeDamage(int damage) { hp -= damage; if(hp < 0) hp = 0; }
	public boolean isDead() { return (hp <= 0); }
	
	//the damage given to the player when colliding with the monster
	protected int contactDamage;
	public int getDamageOnContact() { return contactDamage; }
} //end class Monster
