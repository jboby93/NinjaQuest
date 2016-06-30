/**
 * Game.java: Contains the logic, rendering, and input handling for the game
 * Author: jboby93
 */

package com.jboby93.ist446demo;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.KeyEvent;
import java.io.IOException;
import java.util.ArrayList;

import javax.swing.JOptionPane;

import com.jboby93.jgl.*;

public class Game extends GameWindow {
	private boolean should_quit; //if false, we go to main menu on game over
	public boolean shouldQuitProgram() { return should_quit; }
	
	public static Player player;
	public World map;
	//public int currentRoom = 0;
	
	public static ArrayList<Projectile> projectiles;
	private boolean spaceWasPressed = false;
	
	private String filename = "[default]"; //"resources/boss_testmap.ini"; //"[default]";
	public void setMapFile(String value) { filename = value; }
	public String getMapFile() { return filename; }
	
	public static boolean debug = false;
	public static String lastAction = "constructor call";
	
	public static boolean bossDefeated = false;
	
	@Override
	protected void Initialize() throws IOException {
		//initialize sprites
		Player.loadSprites();
		Skeleton.loadSprites();
		Zombie.loadSprites();
		Skeleton.loadSprites();
		Boss.loadSprites();
		Item.loadSprites();
		Projectile.loadSprites();
		Gate.loadSprites();
		
		//initialize projectiles list
		projectiles = new ArrayList<Projectile>();
		
		//reset some things
		bossDefeated = false;
		
		//initialize an empty world
		World.getWorld();
		
		//try loading the world
		try {
			if(filename.equals("[default]")) { filename = "resources/map.ini"; }
			World.loadWorld(filename);
		} catch (IOException e) {
			App.log("Game.Initialize(): error loading map file");
			lastAction = "loading map file";
			throw e;
		} //end try
		
		//get the starting room
		World.currentRoom = World.getStartRoom();
		
		//create player
		player = new Player(World.getStartPoint());
		player.isVelocityOn = true;
		player.sprite.shouldLoopAnimation = true;
		player.sprite.startAnimating();
		
		//set window title
		this.setTitle(App.appName);
	} //end Initialize()
	
	@Override
	protected void ProcessInput(GameWindowInputHandler e) {
		if(e.isKeyDown(KeyEvent.VK_ESCAPE)) {
			//escape key is pressed
			should_quit = this.showExitPrompt();
		}
		
		//left/right
		if(e.isKeyDown(KeyEvent.VK_LEFT) || e.isKeyDown(KeyEvent.VK_A)) {
			player.velocity.x = -Player.speed;
		} else if(e.isKeyDown(KeyEvent.VK_RIGHT) || e.isKeyDown(KeyEvent.VK_D)) {
			player.velocity.x = Player.speed;
		} else {
			player.velocity.x = 0;
		}
		//up/down
		if(e.isKeyDown(KeyEvent.VK_UP) || e.isKeyDown(KeyEvent.VK_W)) {
			player.velocity.y = -Player.speed;
		} else if(e.isKeyDown(KeyEvent.VK_DOWN) || e.isKeyDown(KeyEvent.VK_S)) {
			player.velocity.y = Player.speed;
		} else {
			player.velocity.y = 0;
		}
		
		//fire projectile
		if(e.isKeyDown(KeyEvent.VK_SPACE)) {
			if(!spaceWasPressed) {
				//set flag so that holding space does not fire endless projectiles
				spaceWasPressed = true;
				
				//fire player projectile
				Game.player.fireProjectile();
			}
		} else {
			spaceWasPressed = false;
		}
	} //end ProcessInput()
	
	@Override
	protected void GameLogic() {
		//is the player dead?
		if(Game.player.isDead()) {
			App.log("Game.GameLogic(): Player got dead");
			JOptionPane.showMessageDialog(null,
					"You've died!", "Game Over", JOptionPane.ERROR_MESSAGE);
			this.setIsRunning(false);
			should_quit = false;
			return;
		} //end if (player death check)
		
		//is the boss dead?
		if(bossDefeated) {
			App.log("Game.GameLogic(): Boss got dead");
			JOptionPane.showMessageDialog(null,
					"The evil mage has been defeated, and the terrible curse placed upon the land is broken.  Congratulations!",
					"You Win", JOptionPane.INFORMATION_MESSAGE);
			this.setIsRunning(false);
			should_quit = false;
			return;
		} //end if (boss defeated check)
		
		//update the player
		player.update();
		
		//check for player/wall collisions
		for(GameObject w : World.getCurrentRoom().walls) {
			if(player.isCollidingWith(w)) {
				//is the player stuck? this can happen with one-way paths and getting stuck in the wall
				//usually, the player will be partially out of bounds
				//if(player.x < 0) player.x = w.getRight();
				
				//hack: set player location to the location before player.update() was called
				//this prevents glitching through walls
				player.setLocation(player.getPreviousLocation()); //player.bounceOffOf(w);
			}
		} //end for (wall checking)
		
		//update items
		for(int i = 0; i < World.getCurrentRoom().items.size(); i++) {
			World.getCurrentRoom().items.get(i).update();
			
			if(World.getCurrentRoom().items.get(i).isDead()) {
				App.log("Game.GameLogic(): Player got " + World.getCurrentRoom().items.get(i).getType());
				World.getCurrentRoom().items.get(i).dispose();
				World.getCurrentRoom().items.remove(i);
				i--;
			}
		} //end for (items update)
		
		//update projectiles
		for(int i = 0; i < projectiles.size(); i++) {
			projectiles.get(i).update();
			
			if(projectiles.get(i).isDead()) {
				//System.out.println("GameLogic(): projectile destroyed");
				projectiles.get(i).dispose();
				projectiles.remove(i);
				i--;
			}
		} //end for (projectiles update)
		
		//update monsters
		for(int i = 0; i < World.getCurrentRoom().monsters.size(); i++) {
			World.getCurrentRoom().monsters.get(i).update();
			
			if(World.getCurrentRoom().monsters.get(i).isDead()) {
				App.log("Game.GameLogic(): Monster " + World.getCurrentRoom().monsters.get(i).getType() + " was slain");
				World.getCurrentRoom().monsters.get(i).dispose();
				World.getCurrentRoom().monsters.remove(i);
				i--;
			}
		} //end for (monsters update)
		
		//update gates
		for(int i = 0; i < World.getCurrentRoom().gates.size(); i++) {
			World.getCurrentRoom().gates.get(i).update();
			
			if(World.getCurrentRoom().gates.get(i).isDead()) {
				App.log("Game.GameLogic(): Gate " + World.getCurrentRoom().gates.get(i).getKeyID() + " unlocked");
				World.getCurrentRoom().gates.get(i).dispose();
				World.getCurrentRoom().gates.remove(i);
				i--;
			}
		} //end for (gates update)
		
		//have we left the room...
		//going north?
		if(player.getCenter().y < 0) {
			//is there a connection?
			if(World.getCurrentRoom().getRoomToNorth() >= 0) {
				World.freezeRoom(World.currentRoom);
				projectiles.clear();
				World.currentRoom = World.getCurrentRoom().getRoomToNorth();
				player.setCenter(new Point(player.getCenter().x, this.getBufferHeight() - this.getInsetTop()));
				World.unfreezeRoom(World.currentRoom);
				
				//now make sure we aren't stuck in any walls
				for(GameObject w : World.getCurrentRoom().walls) {
					if(player.isCollidingWith(w)) {
						boolean xFixed = false;
						boolean yFixed = false;
						if(player.x < w.x) {
							player.x = w.getRight();
							xFixed = true;
						} else if(player.getRight() > w.getRight()) {
							xFixed = true;
							player.setRight(w.x);
						}
						
						if(player.y < w.y) {
							player.y = w.getBottom();
							yFixed = true;
						} else if(player.getBottom() > w.getBottom()) {
							yFixed = true;
							player.setBottom(w.y);
						}
					}
				} //end if (walls check)
			} else {
				//..... whoops?
				//prevent the player from leaving the room in this direction
				App.log("Game.GameLogic(): Player tried to go north but no path is defined");
				player.setCenter(new Point(player.getCenter().x, 0));
			}
		} //end if (north)
		//going south?
		if(player.getCenter().y > this.getBufferHeight()) {
			//is there a connection?
			if(World.getCurrentRoom().getRoomToSouth() >= 0) {
				World.freezeRoom(World.currentRoom);
				projectiles.clear();
				World.currentRoom = World.getCurrentRoom().getRoomToSouth();
				player.setCenter(new Point(player.getCenter().x, this.getInsetTop()));
				World.unfreezeRoom(World.currentRoom);
				
				//now make sure we aren't stuck in any walls
				for(GameObject w : World.getCurrentRoom().walls) {
					if(player.isCollidingWith(w)) {
						boolean xFixed = false;
						boolean yFixed = false;
						if(player.x < w.x) {
							player.x = w.getRight();
							xFixed = true;
						} else if(player.getRight() > w.getRight()) {
							xFixed = true;
							player.setRight(w.x);
						}
						
						if(player.y < w.getCenter().y) {
							player.y = w.getBottom();
							yFixed = true;
						} else if(player.getBottom() > w.getBottom()) {
							yFixed = true;
							player.setBottom(w.y);
						}
					}
				} //end if (walls check)
			} else {
				//..... whoops?
				//prevent the player from leaving the room in this direction
				App.log("Game.GameLogic(): Player tried to go south but no path is defined");
				player.setCenter(new Point(player.getCenter().x, this.getBufferHeight()));
			}
		} //end if (south)
		//going east?
		if(player.getCenter().x > this.getBufferWidth()) {
			//is there a connection?
			if(World.getCurrentRoom().getRoomToEast() >= 0) {
				World.freezeRoom(World.currentRoom);
				projectiles.clear();
				World.currentRoom = World.getCurrentRoom().getRoomToEast();
				player.setCenter(new Point(this.getInsetLeft(), player.getCenter().y));
				World.unfreezeRoom(World.currentRoom);
				
				//now make sure we aren't stuck in any walls
				for(GameObject w : World.getCurrentRoom().walls) {
					if(player.isCollidingWith(w)) {
						boolean xFixed = false;
						boolean yFixed = false;
						if(player.x < w.x) {
							player.x = w.getRight();
							xFixed = true;
						} else if(player.getRight() > w.getRight()) {
							xFixed = true;
							player.setRight(w.x);
						}
						
						if(player.y < w.y) {
							player.y = w.getBottom();
							yFixed = true;
						} else if(player.getBottom() > w.getBottom()) {
							yFixed = true;
							player.setBottom(w.y);
						}
					}
				} //end if (walls check)
			} else {
				//..... whoops?
				//prevent the player from leaving the room in this direction
				App.log("Game.GameLogic(): Player tried to go east but no path is defined");
				player.setCenter(new Point(this.getBufferWidth(), player.getCenter().y));
			}
		} //end if (east)
		//going west?
		if(player.getCenter().x < 0) {
			//is there a connection?
			if(World.getCurrentRoom().getRoomToWest() >= 0) {
				World.freezeRoom(World.currentRoom);
				projectiles.clear();
				World.currentRoom = World.getCurrentRoom().getRoomToWest();
				player.setCenter(new Point(this.getBufferWidth() - this.getInsetRight(), player.getCenter().y));
				World.unfreezeRoom(World.currentRoom);
				
				//now make sure we aren't stuck in any walls
				for(GameObject w : World.getCurrentRoom().walls) {
					if(player.isCollidingWith(w)) {
						boolean xFixed = false;
						boolean yFixed = false;
						if(player.x < w.x) {
							player.x = w.getRight();
							xFixed = true;
						} else if(player.getRight() > w.getRight()) {
							xFixed = true;
							player.setRight(w.x);
						}
						
						if(player.y < w.y) {
							player.y = w.getBottom();
							yFixed = true;
						} else if(player.getBottom() > w.getBottom()) {
							yFixed = true;
							player.setBottom(w.y);
						}
					}
				} //end if (walls check)
			} else {
				//..... whoops?
				//prevent the player from leaving the room in this direction
				App.log("Game.GameLogic(): Player tried to go west but no path is defined");
				player.setCenter(new Point(0, player.getCenter().y));
			}
		} //end if (west)
	} //end GameLogic()

	@Override
	protected void Render(Buffer b) {
		//clear the buffer
		//b.clear(Color.white);
		b.clear(World.getCurrentRoom().getBackColor());
		
		//render the walls, items, and monsters in the current room
		for(GameObject w : World.getCurrentRoom().walls) { w.draw(b); }
		for(Item i : World.getCurrentRoom().items) { i.draw(b); }
		for(Projectile p : projectiles) { p.draw(b); }
		for(Gate g : World.getCurrentRoom().gates) { g.draw(b); }
		for(Monster m : World.getCurrentRoom().monsters){ m.draw(b); }
		
		//render the player
		player.draw(b);
		
		
		//render the scene to the window
		b.render();
	} //end Render()

	@Override
	protected void Shutdown() {
		App.log("Game.Shutdown(): disposing of resources");
		World.dispose();
		for(Projectile p : projectiles) { p.dispose(); }
		projectiles.clear();
		player.dispose();
	} //end Shutdown()
} //end class Game
