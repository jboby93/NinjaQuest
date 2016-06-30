package com.jboby93.jgl;

import java.awt.*;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.awt.image.BufferedImage;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.WindowConstants;

public abstract class GameWindow extends JFrame {
	
	private static final long serialVersionUID = 1L;
	
	private int bufOffsetX;
	protected int getInsetLeft() { return bufOffsetX; }
	
	private int bufOffsetY;
	protected int getInsetTop() { return bufOffsetY; }
	
	protected int getInsetBottom() { return this.getInsets().bottom; }
	protected int getInsetRight() { return this.getInsets().right; }
	
	private String closePrompt;
	private boolean isShuttingDown;
	private int rawWidth;
	public int getBufferWidth() { return rawWidth; }
	
	private int rawHeight;
	public int getBufferHeight() { return rawHeight; }
	
	/**
	 * Initializes a new GameWindow with default dimensions (800, 480)
	 */
	public GameWindow() {
		//call the constructor with default size 800, 480
		this(800, 480); 
	}
	
	/**
	 * Initializes a new GameWindow with the given window dimensions.
	 * @param width
	 * @param height
	 */
	public GameWindow(int width, int height) {
		super();
		
		LibInfo.dbg("GameWindow(): size for new window: (" + width + ", " + height + ")");
		
		int insetsX = this.getInsets().left + this.getInsets().right;
		int insetsY = this.getInsets().top + this.getInsets().bottom;
		
		//bufOffsetX = this.getInsets().left;
		//bufOffsetY = this.getInsets().top;
		
		this.setSize(width + insetsX, height + insetsY);
		rawWidth = width; rawHeight = height;
		this.setResizable(false);
		
		//center the window on the screen
		this.setLocationRelativeTo(null);
		
		this.setTitle("GameWindow");
		
		//set up the window close listener
		this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		//this.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
		//this.addWindowListener(this);
		isShuttingDown = false;
		//set default close prompt
		closePrompt = "Are you sure you want to exit?";
		
		//not running right now
		isRunning = false;
	}
	
	/*
	public void windowClosing(WindowEvent e) {
		if(!isShuttingDown) {
			switch(JOptionPane.showInternalOptionDialog(null,
					closePrompt, "Confirm Exit", JOptionPane.YES_NO_OPTION,
					JOptionPane.QUESTION_MESSAGE, null, null, null)) {
				case JOptionPane.YES_OPTION:
					//isShuttingDown = true;
					isRunning = false;
					
					//clean up
					//Shutdown();
					//this.dispose();
					break;
				case JOptionPane.NO_OPTION:
					//do nothing
					break;
				case JOptionPane.CLOSED_OPTION:
					//user clicked X, treat the same as No
					break;
			} //end switch
		} else {
			this.dispose();	
		} //end if	
	} //end closeWindowHandler()
	*/
	
	//game-related stuff
	//private BufferedImage backBuffer;
	private boolean isRunning;
	public boolean getIsRunning() { return isRunning; }
	public void setIsRunning(boolean value) { isRunning = value; }
	
	private int iFPS = 60;
	
	//Graphics g; //the window graphics context
	//Graphics bbg; //the graphics context for the buffer
	private GameWindowInputHandler input;
	
	private Buffer buf;
	
	private void init() throws Exception {
		LibInfo.dbg("GameWindow.init(): initializing GameWindow");
		
		//LibInfo.dbg("GameWindow.init(): checking OS");
		//if(LibInfo.getOSType() == LibInfo.OS_MACOSX) {
		//	//LibInfo.dbg("GameWindow.init(): setting additional properties for Mac OS X");
		//	//System.setProperty("apple.laf.useScreenMenuBar", "true");
		//	//System.setProperty("apple.awt.showGrowBox", "false");
		//}
		
		//get the window insets
		int insetsX = this.getInsets().left + this.getInsets().right;
		int insetsY = this.getInsets().top + this.getInsets().bottom;
		
		LibInfo.dbg("GameWindow.init(): window insets: left: " + this.getInsets().left);
		LibInfo.dbg("GameWindow.init():                right: " + this.getInsets().right);
		LibInfo.dbg("GameWindow.init():                top: " + this.getInsets().top);
		LibInfo.dbg("GameWindow.init():                bottom: " + this.getInsets().bottom);
		LibInfo.dbg("GameWindow.init(): os name: " + LibInfo.getOSName());
		
		bufOffsetX = this.getInsets().left;
		bufOffsetY = this.getInsets().top;
		
		
		if(LibInfo.getOSType() == LibInfo.OS_MACOSX){
			//bug fix: buffer doesn't draw fully to window (bottom is cut off by amount equal to top inset)
			this.getRootPane().setPreferredSize(new Dimension(rawWidth, rawHeight));
			this.getContentPane().setPreferredSize(new Dimension(rawWidth, rawHeight));
			this.pack();
		} else {
			this.setSize(rawWidth + insetsX, rawHeight + insetsY);
		}
		
		//this.setSize(rawWidth + (LibInfo.getOSType() == LibInfo.OS_MACOSX ? 0 : insetsX), 
		//			 rawHeight + (LibInfo.getOSType() == LibInfo.OS_MACOSX ? 0 : insetsY));
		LibInfo.dbg("GameWindow.init(): raw window size: " + new Dimension(rawWidth, rawHeight).toString());
		LibInfo.dbg("GameWindow.init(): corrected window size: " + this.getSize().toString());
		
		this.setResizable(false);
		
		LibInfo.dbg("GameWindow.init(): initializing window buffer");
		if(LibInfo.getOSType() == LibInfo.OS_MACOSX){
			buf = new Buffer(this, rawWidth, rawHeight + this.getInsets().top, bufOffsetX, bufOffsetY);
		} else {
			buf = new Buffer(this, rawWidth, rawHeight, bufOffsetX, bufOffsetY);
		}
		
		LibInfo.dbg("GameWindow.init(): buffer size: " + buf.getWidth() + "x" + buf.getHeight());
		
		//prepare input listener
		LibInfo.dbg("GameWindow.init(): initializing input handler");
		input = new GameWindowInputHandler(this, bufOffsetX, bufOffsetY);
		
		//call the abstract Initialize() to run user startup code
		LibInfo.dbg("GameWindow.init(): transferring control to user code -> Initialize()");
		Initialize();
		LibInfo.dbg("GameWindow.init(): returned from user code <- Initialize()");
	}
	
	/**
	 * Shows the GameWindow, calls Initialize(), and starts the game loop
	 * @throws Exception 
	 */
	public void start() throws Exception {
		LibInfo.dbg("GameWindow.start(): showing the GameWindow");
		this.setVisible(true);
		
		//run initialization stuff
		LibInfo.dbg("GameWindow.start(): calling init()");
		init();
		
		//all systems go
		LibInfo.dbg("GameWindow.start(): starting the GameLoop()");
		isRunning = true;
		GameLoop();
	}
	
	/**
	 * Displays an error dialog from the provided throwable/exception object
	 * @param e The Throwable/Exception object
	 * @param rethrow Whether to throw the error again after displaying the message box
	 * @throws Throwable
	 */
	public void showExceptionDialog(Throwable e, boolean rethrow) throws Throwable {
		//display error
		
		
		if(rethrow) throw e;
	}
	
	protected void GameLoop() throws Exception {
		while(isRunning) {
			long time = System.currentTimeMillis();
			
			ProcessInput(input);
			GameLogic();
			//draw();
			Render(buf);
			
			time = (1000 / iFPS) - (System.currentTimeMillis() - time);
			if(time > 0) {
				try {
					Thread.sleep(time);
				} catch(Exception e) {}
			}
		} //end while (game loop)
		
		LibInfo.dbg("GameWindow.GameLoop(): exited the game loop");
		
		isShuttingDown = true;
		
		LibInfo.dbg("GameWindow.GameLoop(): transferring control to user code -> Shutdown()");
		Shutdown();
		LibInfo.dbg("GameWindow.GameLoop(): returned from user code <- Shutdown()");
		
		LibInfo.dbg("GameWindow.GameLoop(): hiding the window");
		this.setVisible(false);
		//this.windowClosing(null);
	} //end GameLoop()
	
	/**
	 * Displays the exit prompt with options "Yes" or "No", to confirm whether the user wants to exit the application.
	 * @return true if the user selects Yes; false otherwise.
	 */
	//TODO: Should this just be a void that sets the isRunning state appropriately?
	protected boolean showExitPrompt() {
		LibInfo.dbg("GameWindow.showExitPrompt(): preparing to show exit prompt");
		
		boolean r = false;
		switch(JOptionPane.showOptionDialog(this,
				closePrompt, "Confirm Exit", JOptionPane.YES_NO_OPTION,
				JOptionPane.QUESTION_MESSAGE, null, null, null)) {
			case JOptionPane.YES_OPTION:
				//isShuttingDown = true;
				LibInfo.dbg("GameWindow.showExitPrompt(): user chose yes; setting isRunning to false");
				isRunning = false;
				
				//clean up
				//Shutdown();
				//this.dispose();
				r = true;
				break;
			case JOptionPane.NO_OPTION:
				//do nothing
				LibInfo.dbg("GameWindow.showExitPrompt(): user chose no");
				break;
			case JOptionPane.CLOSED_OPTION:
				//user clicked X, treat the same as No
				LibInfo.dbg("GameWindow.showExitPrompt(): user closed the dialog");
				break;
		} //end switch
		
		return r;
	}
	
	/**
	 * Contains initialization code for the game.  This is executed before the game loop starts.
	 */
	protected abstract void Initialize() throws Exception;
	
	/**
	 * Contains code that handles input
	 * @param input An object that can be used to poll for keyboard and mouse events
	 */
	protected abstract void ProcessInput(GameWindowInputHandler input);
	
	/**
	 * Contains the logic for your game
	 * @throws Exception 
	 */
	protected abstract void GameLogic() throws Exception;
	
	/**
	 * Contains code to draw things to the window.  To render the buffer to the window, make sure the last line of this method is b.Render().
	 * @param b
	 */
	protected abstract void Render(Buffer b) throws Exception;
	
	/**
	 * Contains shutdown procedures that are executed after the game loop exits, and before the window closes.
	 */
	protected abstract void Shutdown() throws Exception;
}
