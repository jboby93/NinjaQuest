/**
 * App.java: The driver for the game.  Loads and displays the TitleScreen and the Game
 */

package com.jboby93.ist446demo;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Calendar;
import javax.swing.JOptionPane;

public class App {
	//program info constants
	public static final String appName = "Ninja Quest";
	public static final String version = "1.0";
	public static final String author = "jboby93";
	public static final String buildDate = "2/17s/2015";
	
	public static void main(String args[]) {
		//display program info to the console
		log("Ninja Quest v1.0");
		log("Author: " + author);
		log("Build Date: " + buildDate);
		System.out.println("");
		
		//define the title screen
		TitleScreen title = new TitleScreen();
		
		//define the game
		Game game = new Game();
		
		//enclose everything in a try-catch block so we can catch errors
		try {
			do {
				//show title screen
				App.log("App.main(): showing the title screen");
				title.start();
				if(title.userChoseQuit()) break; //exit the loop if the user chose to quit
				
				//are there command line arguments?
				boolean nextIsFile = false;
				for(int i = 0; i < args.length; i++) {
					if(nextIsFile) {
						//this is a filename
						nextIsFile = false;
						game.setMapFile(args[i]);
					} else {
						switch(args[i].toLowerCase()) {
						case "-file": //specifies a map file to load
						case "-f":
							nextIsFile = true;
							App.log("App.main(): custom map file is specified!");
							break;
						case "-debug": //produces additional logging errors
						case "-d":
							Game.debug = true;
							App.log("App.main(): debug flag is ON");
							break;
						} //end switch
					} //end if
				} //end for
				
				App.log("App.main(): starting the game");
				game.start();	
				
				//reset the game engine
				game = null;
				game = new Game();
				App.log("App.main(): game engine is reset");
			} while(!game.shouldQuitProgram()); //this allows return to main menu after death
		} catch(Exception e) {
			App.log("App.main(): oh dear :(");
			e.printStackTrace();
			
			//get stack trace as a string
			StringWriter stack = new StringWriter();
			e.printStackTrace(new PrintWriter(stack));
			
			JOptionPane.showMessageDialog(null,
					"Something really bad happened :(  Please send this error to the developer at jboby93@gmail.com.\n\n" + 
					stack, "Fatal Error", JOptionPane.ERROR_MESSAGE);
		} //end try
		
		//we are done
		App.log("App.main(): exiting");
		System.exit(0);
	} //end main()
	
	public static void log(String msg) {
		System.out.println(getTimeStamp() + " " + msg);
	}
	
	private static String getTimeStamp() {
		Calendar c = Calendar.getInstance();
		return "[" + c.get(Calendar.HOUR_OF_DAY) + ":" + c.get(Calendar.MINUTE) + ":" + c.get(Calendar.SECOND) + "]"; 
	}
} //end class App
