/**
 * World.java: Contains the World definition and the methods to load a world from a map file
 * Author: jboby93
 */

package com.jboby93.ist446demo;

import java.awt.Color;
import java.awt.Point;
import java.awt.Rectangle;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

public class World {
	private static World world = null; //singleton instance
	
	public static Map<Integer, Room> rooms; //list of all rooms in the world
	private static int start = -1;
	public static int getStartRoom() { return start; }
	private static Point startPoint;
	public static Point getStartPoint() { return startPoint; }
	
	public static int currentRoom;
	public static Room getCurrentRoom() { return rooms.get(currentRoom); }
	
	public World() {
		if(world == null) {
			world = this;
			rooms = new HashMap<Integer, Room>();
			currentRoom = -1;
			App.log("World(): created the world");
		}
	}
	
	public static World getWorld() {
		if(world == null) {
			world = new World();
		}
		
		return world;
	}
	
	public static Map<Integer, Room> getRooms() { return rooms; }
	
	
	public static void loadWorld(String file) throws IOException {
		//clear any existing world data
		rooms.clear();
		
		//read data from the file
		App.log("World.loadWorld(): opening file " + file);
		ArrayList<String> rawContents = new ArrayList<String>();
		File rFile = new File(file);
		try(BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(rFile), "UTF-8"))) {
			String str = "";
			while((str = reader.readLine()) != null) {
				rawContents.add(str);
			}
					
			reader.close();
		} catch (IOException e) {
			throw e;
		}
		
		//rawContents now contains a list of each line in the file
		String[] cmd;
		
		for(String str : rawContents) {
			cmd = null;
			
			//if the line is blank, ignore it
			if(str.length() == 0) continue;
			
			//if the first character is '#', it's a comment. ignore it
			if(str.charAt(0) == '#') continue;
			
			//tokens are separated by spaces
			//the first token is the command, the rest of them are arguments
			cmd = str.split(" ");
			
			//run the loaded command
			runMapCommand(cmd);
		} //end for
		
		//done!
		App.log("World.loadWorld(): added " + rooms.size() + " rooms to the world");
	} //end loadWorld()
	
	private static void runMapCommand(String tokens[]) {
		int curRoom = rooms.size() - 1; //zero-based index
		
		//arg0 = command
		switch(tokens[0].toLowerCase()) {
		case "add_room":
			//arg1 [optional]: background color (default is Color.white)
			curRoom++;
			
			//add a new room
			rooms.put(curRoom, new Room());
			
			//set color if one is provided
			if(tokens.length > 1) { rooms.get(curRoom).setBackColor(parseHexColor(tokens[1])); }
			
			break;
		case "add_monster":
			//arg1 = type
			//arg2 = x; arg3 = y
			switch(tokens[1]) {
			case "zombie":
				rooms.get(curRoom).addMonster(Monster.Type.zombie, 
						new Point(Integer.parseInt(tokens[2]), Integer.parseInt(tokens[3])));
				break;
			case "skeleton":
				rooms.get(curRoom).addMonster(Monster.Type.skeleton,
						new Point(Integer.parseInt(tokens[2]), Integer.parseInt(tokens[3])));
				break;
			case "boss":
				rooms.get(curRoom).addMonster(Monster.Type.boss,
						new Point(Integer.parseInt(tokens[2]), Integer.parseInt(tokens[3])));
				break;
			}
			break;
		case "add_wall":
			//arg1 = x
			//arg2 = y
			//arg3 = width
			//arg4 = height
			//arg5 = color (in hex: FF8800)
			rooms.get(curRoom).addWall(new Rectangle(Integer.parseInt(tokens[1]),
						Integer.parseInt(tokens[2]), 
						Integer.parseInt(tokens[3]),
						Integer.parseInt(tokens[4])),
					parseHexColor(tokens[5]));
			break;
		case "add_item":
			//arg1 = type; arg2 = data value
			//arg3 = x; arg4 = y
			switch(tokens[1].toLowerCase()) {
			case "health":
				rooms.get(curRoom).addItem(Item.Type.health, Integer.parseInt(tokens[2]), 
						new Point(Integer.parseInt(tokens[3]), Integer.parseInt(tokens[4])));
				break;
			case "ninjastar":
				rooms.get(curRoom).addItem(Item.Type.ninjaStar, Integer.parseInt(tokens[2]), 
						new Point(Integer.parseInt(tokens[3]), Integer.parseInt(tokens[4])));
				break;
			case "key":
				rooms.get(curRoom).addItem(Item.Type.key, Integer.parseInt(tokens[2]), 
						new Point(Integer.parseInt(tokens[3]), Integer.parseInt(tokens[4])));
				break;
			}
			break;
		case "add_gate":
			//arg1 = key ID; arg2 = x; arg3 = y
			rooms.get(curRoom).addGate(Integer.parseInt(tokens[1]),
					new Point(Integer.parseInt(tokens[2]), Integer.parseInt(tokens[3])));
			break;
		case "add_path":
			//arg1 = direction; arg2 = destination room
			int dest = Integer.parseInt(tokens[2]);
			
			switch(tokens[1].toLowerCase()) {
			case "north":
				rooms.get(curRoom).addNorthPath(dest);
				break;
			case "south":
				rooms.get(curRoom).addSouthPath(dest);
				break;
			case "east":
				rooms.get(curRoom).addEastPath(dest);
				break;
			case "west":
				rooms.get(curRoom).addWestPath(dest);
				break;
			} //end switch
			
			break;
		case "set_player_start":
			//arg1 = x; arg2 = y
			start = curRoom;
			startPoint = new Point(Integer.parseInt(tokens[1]), Integer.parseInt(tokens[2]));
			
			break;
		default:
			//invalid token
			//TODO ignore invalid tokens, or throw exception?
			break;
		} //end switch
	} //end runMapCommand()
	
	//returns a Color object corresponding to the input hex color (RRGGBB)
	private static Color parseHexColor(String c) {
		return new Color(
	            Integer.valueOf(c.substring(0, 2), 16),
	            Integer.valueOf(c.substring(2, 4), 16),
	            Integer.valueOf(c.substring(4, 6), 16));
	}
	
	//get the system new line separator (for file I/O)
	protected static final String NL = System.getProperty("line.separator");
	
	/**
	 * Returns a string consisting of [length] characters from the beginning of the string.  
	 * This method is equivalent to Strings.Left(str, length) in VB.NET.
	 * @param str
	 * @param length
	 * @return
	 */
	private static String Left(String str, int length) {
		return str.substring(0, Math.min(length, str.length()));
	}
	
	/**
	 * Starts any needed animation threads in this room
	 */
	public static void unfreezeRoom(int index) {
		for(Monster m : rooms.get(index).monsters) {
			if(m.sprite.getFrames().length > 1)
				m.sprite.startAnimating();
		}
		
		for(Item i : rooms.get(index).items) {
			if(i.sprite.getFrames().length > 1)
				i.sprite.stopAnimating();
		}
	}
	
	/**
	 * Stops all animation threads in this room
	 */
	public static void freezeRoom(int index) {
		for(Monster m : rooms.get(index).monsters) {
			m.sprite.stopAnimating();
		}
		
		for(Item i : rooms.get(index).items) {
			i.sprite.stopAnimating();
		}
	}
	
	/**
	 * Destroys the world :)
	 */
	public static void dispose() {
		for(Entry<Integer, Room> e : rooms.entrySet()) {
			e.getValue().dispose();
		}
		
		rooms.clear();
		
		App.log("World.dispose(): destroyed the world :)");
	} //end dispose()
} //end class World
