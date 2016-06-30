/**
 * Room.java: Contains the Room definition
 * Author: jboby93
 * 
 * A Room is a section of the game world that contains walls, monsters, items, gates, and
 * paths to other rooms in the world.
 */

package com.jboby93.ist446demo;

import com.jboby93.jgl.*;

import java.awt.Color;
import java.awt.Point;
import java.awt.Rectangle;
import java.util.ArrayList;

public class Room {
	//lists that hold everything
	public ArrayList<GameObject> walls;
	public ArrayList<Monster> monsters;
	public ArrayList<Item> items;
	public ArrayList<Gate> gates;
	
	//integers that store path destinations
	//-1 = no room in that direction
	private int left, right, up, down;
	
	//background color of the room
	private Color backColor;
	
	public int getRoomToWest() { return this.left; }
	public int getRoomToEast() { return this.right; }
	public int getRoomToNorth() { return this.up; }
	public int getRoomToSouth() { return this.down; }
	
	public Color getBackColor() { return backColor; }
	public void setBackColor(Color value) { backColor = value; }
	
	public Room() {
		left = -1; right = -1; up = -1; down = -1;
		walls = new ArrayList<GameObject>();
		monsters = new ArrayList<Monster>();
		items = new ArrayList<Item>();
		gates = new ArrayList<Gate>();
		
		backColor = Color.white; //TODO: change this to a light yellow (FFF6CE is nice)
	}
	
	//adds a wall to the room
	public void addWall(Rectangle r, Color c) {
		walls.add(new GameObject(Sprite.SpriteType.box, r.x, r.y, r.width, r.height, new ShapeStyle(c, c)));
	} //end addWall()
	
	//adds a monster to the room
	public void addMonster(Monster.Type type, Point location) {
		switch(type) {
		case boss:
			monsters.add(new Boss(location));
			break;
		case skeleton:
			monsters.add(new Skeleton(location));
			break;
		case zombie:
			monsters.add(new Zombie(location));
			break;
		default:
			//...?
			break;
		} //end switch
	} //end addMonster()
	
	//adds an item to the room
	public void addItem(Item.Type type, int data, Point location) {
		items.add(new Item(type, data, location));
	} //end addItem()
	
	//adds a gate to the room
	public void addGate(int keyID, Point location) {
		gates.add(new Gate(keyID, location));
	} //end addGate()
	
	//set the path destinations
	public void addWestPath(int toRoom) { left = toRoom; }
	
	public void addEastPath(int toRoom) { right = toRoom; }
	
	public void addNorthPath(int toRoom) { up = toRoom; }

	public void addSouthPath(int toRoom) { down = toRoom; }
	
	/**
	 * Destroys this room
	 */
	public void dispose() {
		for(GameObject w : walls) { w.dispose(); }
		for(Monster m : monsters) { m.dispose(); }
		for(Item i : items) { i.dispose(); }
		for(Gate g : gates) { g.dispose(); }
		
		walls.clear();
		monsters.clear();
		items.clear();
		gates.clear();
	} //end dispose()
} //end class Room
