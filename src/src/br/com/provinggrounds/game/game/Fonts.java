package br.com.provinggrounds.game.game;

import java.awt.Font;

import org.newdawn.slick.TrueTypeFont;

public class Fonts {
	public static TrueTypeFont levelChangeFont;
	public static TrueTypeFont enemyKilledFont;
	public static TrueTypeFont hudCurrentLevelFont;
	public static TrueTypeFont tooltipFont;
	private Fonts(){
	}
	
	public static void initialize(){
		enemyKilledFont = new TrueTypeFont(new Font("Arial", Font.PLAIN, 20), true);
		levelChangeFont = new TrueTypeFont(new Font("Verdana", Font.BOLD, 36), true);
		hudCurrentLevelFont = new TrueTypeFont(new Font("Trebuchet MS", Font.PLAIN, 24), true);
		tooltipFont = new TrueTypeFont(new Font("Arial", Font.PLAIN, 20), true); 
	}
}
