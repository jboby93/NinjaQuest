package com.jboby93.jgl;

import javax.swing.JOptionPane;

//TODO JavaDocs
public class LibInfo {
	public static final String name = "JavaGL";
	public static final String author = "jboby93";
	public static final String version = "beta";
	public static final double versionNumber = 0.6;
	public static final String buildDate = "2/27/2015";
	public static final int DEBUG = 1; //0 for release
	
	public static void showAboutDialog() {
		String msg = name + " - a Java library for simple development of 2D games\n" + 
					 "Author: " + author + "\n" + 
					 "Version: " + version + " (" + versionNumber + ")" + (DEBUG == 1 ? " (debug)" : "") + "\n" + 
					 "Build Date: " + buildDate + "\n";
		
		JOptionPane.showMessageDialog(null,
				msg, "About JavaGL", JOptionPane.INFORMATION_MESSAGE);
	} //end showAboutDialog()
	
	public static String getOSName() { return System.getProperty("os.name"); }
	
	public static final int OS_WINDOWS = 1;
	public static final int OS_MACOSX = 2;
	public static final int OS_LINUX = 3;
	
	public static int getOSType() {
		if(System.getProperty("os.name").toLowerCase().contains("win")) return OS_WINDOWS;
		if(System.getProperty("os.name").toLowerCase().contains("mac")) return OS_MACOSX;
		
		return -1;
	}
	
	public static void dbg(String debugMessage) {
		if(LibInfo.DEBUG == 1) System.out.println("[JavaGL-Debug]: " + debugMessage);
	}
} //end class LibInfo
