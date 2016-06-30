/**
 * Item.java: Contains the Item definition and logic methods
 * Author: jboby93
 * 
 * There are three types of items in the game:
 * - Health - restores player health
 * - Ninja Star - gives the player ninja stars to throw at monsters
 * - Key - unlocks gates with the corresponding key ID
 */

package com.jboby93.ist446demo;

import java.awt.Image;
import java.awt.Point;

import com.jboby93.jgl.Functions;
import com.jboby93.jgl.GameObject;
import com.jboby93.jgl.Sprite;

public class Item extends GameObject {
	
	private static Image spr[]; //the health sprites
	private static Image sprStar[];
	private static Image sprKey;
	
	public static void loadSprites() {
		spr = new Image[] {Functions.loadImageFromFile("resources/images/items/heart1.png"),
				Functions.loadImageFromFile("resources/images/items/heart2.png"),
				Functions.loadImageFromFile("resources/images/items/heart3.png"),
				Functions.loadImageFromFile("resources/images/items/heart4.png"),
				Functions.loadImageFromFile("resources/images/items/heart5.png")};
		sprStar = new Image[] {Functions.loadImageFromFile("resources/images/items/star_other.png"),
				Functions.loadImageFromFile("resources/images/items/star10.png"),
				Functions.loadImageFromFile("resources/images/items/star15.png"),
				Functions.loadImageFromFile("resources/images/items/star20.png"),
				Functions.loadImageFromFile("resources/images/items/star25.png"),
				Functions.loadImageFromFile("resources/images/items/star30.png")};
		sprKey = Functions.loadImageFromFile("resources/images/items/key.png");
		
		App.log("Item.loadSprites(): item sprites loaded");
	} //end loadSprites()
	
	public Item(Type type, int data, Point location) {		
		this.type = type;
		this.data = data;
		Image i = null;
		
		//determine the sprite to use based on the item's type
		switch(type) {
		case health:
			switch(data) {
			case 1: i = spr[0]; break;
			case 2: i = spr[1]; break;
			case 3: i = spr[2]; break;
			case 4: i = spr[3]; break;
			case 5: i = spr[4]; break;
			default: i = spr[0]; break;
			}
			break;
		case ninjaStar:
			switch(data) {
			case 10:
				i = sprStar[1];
				break;
			case 15:
				i = sprStar[2];
				break;
			case 20:
				i = sprStar[3];
				break;
			case 25:
				i = sprStar[4];
				break;
			case 30:
				i = sprStar[5];
				break;
			default:
				i = sprStar[0];
				break;
			}
			break;
		case key:
			i = sprKey;
			break;
		default:
			//...?
			App.log("Item(): invalid item type?");
			break;
		}
		
		this.setWidth(32); this.setHeight(32);
		this.sprite = new Sprite(i);
		this.setLocation(location);
	}
	
	public void update() {
		//items don't move, is this call to update() needed?
		super.update();
		
		if(this.isCollidingWith(Game.player)) {
			switch(this.type) {
			case health:
				//restores health: 1hp = 1 half of a heart; max = 10
				//TODO if health is full, don't pick up the item
				Game.player.hp += this.data;
				if(Game.player.hp > Player.maxHP) Game.player.hp = Player.maxHP;
				break;
			case key:
				//adds the key ID to the list of keys owned by the player
				Game.player.keys.add(this.data);
				break;
			case ninjaStar:
				//gives the player ammo; max is 100
				//TODO if ammo is full, don't pick up the item
				Game.player.ammo += this.data;
				if(Game.player.ammo > Player.maxAmmo) Game.player.ammo = Player.maxAmmo; 
				break;			
			}
			
			//set the dead flag so the game loop removes it from the room
			this.dead = true;
		} //end collision check
	} //end update()
	
	public enum Type {health, ninjaStar, key};
	private Type type;
	private int data;
	private boolean dead;
	
	public Type getType() { return this.type; }
	public int getDataValue() { return this.data; }
	public boolean isDead() { return this.dead; }
}
