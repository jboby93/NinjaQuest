package com.jboby93.jgl;

import java.awt.Component;
import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.event.*;

public class GameWindowInputHandler implements KeyListener, MouseListener, MouseMotionListener, MouseWheelListener {	
	private boolean keys[];
	private boolean mouseButtons[];
	private Point mouseLoc;
	private int insetX, insetY;
	
	public GameWindowInputHandler(Component c, int insetLeft, int insetTop) {
		c.addKeyListener(this);
		c.addMouseListener(this);
		c.addMouseMotionListener(this);
		
		insetX = insetLeft; insetY = insetTop;
		
		keys = new boolean[256];
		mouseButtons = new boolean[MouseInfo.getNumberOfButtons()];
		mouseLoc = new Point(0, 0);
	}
	
	// ======== PROPERTIES
	/**
	 * Returns true if the key with the given keycode is pressed
	 * @param keyCode
	 * @return
	 */
	public boolean isKeyDown(int keyCode) {
		if(keyCode > 0 && keyCode < 256) {
			return keys[keyCode];
		}
		
		return false;
	} //end isKeyDown()
	
	public Point getMousePosition() { return new Point(mouseLoc.x - this.insetX, mouseLoc.y - this.insetY); }
	
	// ======== KEYBOARD EVENT HANDLERS
	@Override
	public void keyPressed(KeyEvent e) {
		if(e.getKeyCode() > 0 && e.getKeyCode() < 256) {
			keys[e.getKeyCode()] = true;
		}
	}

	@Override
	public void keyReleased(KeyEvent e) {
		if(e.getKeyCode() > 0 && e.getKeyCode() < 256) {
			keys[e.getKeyCode()] = false;
		}
	}

	@Override
	//not used
	public void keyTyped(KeyEvent e) {}

	// ======== MOUSE EVENT HANDLERS	
	@Override
	public void mouseDragged(MouseEvent arg0) {
		// TODO is mouseDragged() important? possible?
		
	}

	@Override
	public void mouseMoved(MouseEvent arg0) {
		mouseLoc = arg0.getPoint();
	}

	@Override
	public void mouseClicked(MouseEvent arg0) { }

	@Override
	public void mouseEntered(MouseEvent arg0) { }

	@Override
	public void mouseExited(MouseEvent arg0) { }

	@Override
	public void mousePressed(MouseEvent arg0) {
		mouseButtons[arg0.getButton() - 1] = true;
	}

	@Override
	public void mouseReleased(MouseEvent arg0) {
		mouseButtons[arg0.getButton() - 1] = false;
	}

	@Override
	public void mouseWheelMoved(MouseWheelEvent arg0) {
		// TODO is mouseWheelMoved() important? possible?
	}
} //end class GameWindowInputHandler
