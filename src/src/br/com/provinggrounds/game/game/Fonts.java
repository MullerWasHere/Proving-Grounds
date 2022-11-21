package br.com.provinggrounds.game.game;

import java.awt.Color;
import java.awt.Font;
	
import org.newdawn.slick.SlickException;
import org.newdawn.slick.UnicodeFont;
import org.newdawn.slick.font.effects.ColorEffect;

public class Fonts {
	public static UnicodeFont levelChangeFont;
	public static UnicodeFont enemyKilledFont;
	public static UnicodeFont hudCurrentLevelFont;
	public static UnicodeFont tooltipFont;
	
	@SuppressWarnings("unchecked")
	public static void initialize() throws SlickException{
		enemyKilledFont = new UnicodeFont(new Font("Arial", Font.PLAIN, 20));
		enemyKilledFont.getEffects().add(new ColorEffect(Color.yellow));
		enemyKilledFont.addAsciiGlyphs();
		enemyKilledFont.loadGlyphs();
		
		levelChangeFont = new UnicodeFont(new Font("Verdana", Font.BOLD, 36));
		levelChangeFont.getEffects().add(new ColorEffect(Color.white));
		levelChangeFont.addAsciiGlyphs();
		levelChangeFont.loadGlyphs();
		
		hudCurrentLevelFont = new UnicodeFont(new Font("Trebuchet MS", Font.PLAIN, 24));
		hudCurrentLevelFont.getEffects().add(new ColorEffect(Color.black));
		hudCurrentLevelFont.addAsciiGlyphs();
		hudCurrentLevelFont.loadGlyphs();
		
		tooltipFont = new UnicodeFont(new Font("Arial", Font.PLAIN, 20));
		tooltipFont.getEffects().add(new ColorEffect(Color.black));
		tooltipFont.addAsciiGlyphs();
		tooltipFont.loadGlyphs();
	}
}
