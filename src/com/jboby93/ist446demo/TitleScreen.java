/**
 * TitleScreen.java: The GameWindow that displays and handles the game's title screen
 * Author: jboby93
 */

package com.jboby93.ist446demo;

import java.awt.Color;
import java.awt.Image;
import java.awt.Point;
import java.awt.event.KeyEvent;

import com.jboby93.jgl.Buffer;
import com.jboby93.jgl.Functions;
import com.jboby93.jgl.GameObject;
import com.jboby93.jgl.GameWindow;
import com.jboby93.jgl.GameWindowInputHandler;

public class TitleScreen extends GameWindow {
	private boolean should_quit = false;
	public boolean userChoseQuit() { return should_quit; }
	
	int menuHover = 1; //1 = Play, 2 = Help, 3 = Exit
	GameObject menuCursor;
	GameObject menuOptions;
	Image mainBack;
	Image controlsBack;
	Image helpBack;
	Image exitBack;
	
	//private String helpPlay = "Starts the game";
	//private String helpControls = "Displays the controls";
	//private String helpAbout = "Displays info about the game and developer";
	//private String helpExit = "Exits the game";
	
	//private String curHelpText = "Starts the game";
	
	@Override
	protected void Initialize() {
		//the key
		menuCursor = new GameObject(Functions.loadImageFromFile("resources/images/menu/cursor.png"), new Point(249, 267));

		//the choices
		menuOptions = new GameObject(Functions.loadImageFromFile("resources/images/menu/choices.png"), new Point(295, 266));
		
		//the menu backgrounds
		mainBack = Functions.loadImageFromFile("resources/images/menu/mainBack.png");
		controlsBack = Functions.loadImageFromFile("resources/images/menu/controlsBack.png");
		helpBack = Functions.loadImageFromFile("resources/images/menu/helpBack.png");
		exitBack = Functions.loadImageFromFile("resources/images/menu/exitBack.png");
	} //end Initialize()

	private boolean wasKeyPressed = false;
	
	@Override
	protected void ProcessInput(GameWindowInputHandler e) {
		//up/down
		if(e.isKeyDown(KeyEvent.VK_UP)) {
			if(!wasKeyPressed) {
				wasKeyPressed = true;
				if(menuHover > 1) { 
					//move up
					menuHover--;
				}
			}
		} else if(e.isKeyDown(KeyEvent.VK_DOWN)) {
			if(!wasKeyPressed) {
				wasKeyPressed = true;
				if(menuHover < 4) {
					//move down
					menuHover++;
				}
			}
		} else {
			wasKeyPressed = false;
		} //end if (left/right/press check)
		
		//enter/space
		if(e.isKeyDown(KeyEvent.VK_ENTER) || e.isKeyDown(KeyEvent.VK_SPACE)) {
			//do the relevant deed
			switch(menuHover) {
			case 1: //Play Game
				should_quit = false;
				this.setIsRunning(false);
				break;
			case 2: //Controls
				
				break;
			case 3: //About

				break;
			case 4: //Exit Game
				should_quit = true;
				this.setIsRunning(false);
				break;
			} //end switch
		} //end if (space/enter)
	} //end ProcessInput()

	@Override
	protected void GameLogic() {
		//determine where to put the menu options
		switch(menuHover) {
		case 1:
			menuOptions.y = 266;
			break;
		case 2:
			menuOptions.y = 233;
			break;
		case 3:
			menuOptions.y = 203;
			break;
		case 4: 
			menuOptions.y = 168;
			break;
		} //end switch
	} //end GameLogic()

	@Override
	protected void Render(Buffer b) {
		//clear buffer
		b.clear(Color.white);
		
		//determine the back image to draw
		switch(menuHover) {
		case 1:
			b.drawImage(mainBack, new Point(0, 0));
			break;
		case 2:
			b.drawImage(controlsBack, new Point(0, 0));
			break;
		case 3:
			b.drawImage(helpBack, new Point(0, 0));
			break;
		case 4: 
			b.drawImage(exitBack, new Point(0, 0));
			break;
		}
		
		//draw the menu text
		menuOptions.draw(b);
		
		//draw the key
		menuCursor.draw(b);
		
		//render
		b.render();
	} //end Render()

	@Override
	protected void Shutdown() {
		menuOptions.dispose();
		menuCursor.dispose();
	} //end Shutdown()
} //end class TitleScreen
