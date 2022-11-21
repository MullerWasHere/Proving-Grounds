package br.com.provinggrounds.game.entity;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.TrueTypeFont;
import org.newdawn.slick.UnicodeFont;

import br.com.provinggrounds.game.dungeon.Room;
import br.com.provinggrounds.game.game.Fonts;

public class Notification{
	
	private int timetoshow;
	private int timecounter;
	private int timeleft;
	private float x;
	private float y;
	private float vy;
	private float ay;
	private String text;
	private UnicodeFont font;
	private Color color;
	private Effect effect;
	
	
	public Notification(String text, float x, float y, Time delay,  UnicodeFont font, Color color, Time duration, Effect effect){
		this.timecounter = 0;
		
		if(effect == Effect.UP)
			this.vy = -0.15f;
		else
			this.vy = -0.25f;
		this.ay = 1.0f;
		
		this.x = x;
		this.y = y;
		this.timetoshow = delay.getDuration();
		this.text = text;
		this.timeleft = duration.getDuration();
		this.font = font;
		this.color = color;
		this.effect = effect;
	}
	
	public Notification(String text, float x, float y){
		this(text, x, y, Time.NONE, Fonts.levelChangeFont, Color.white, Time.DEFAULT, Effect.STATIC);
	}

	public void update(GameContainer c, int delta, Room room) {
		timecounter += delta;
		if(timecounter >= timetoshow && timeleft>0 && effect != Effect.STATIC){
			timeleft -= delta;
			
			if(effect == Effect.UP)
				y = y + delta * vy;
			else if(effect == Effect.GRAVITY){
				y = y + delta * vy * ay;
				//System.out.println("sad");
				ay -= 0.03f;
			}
		}
	}
	
	public Effect getEffect(){
		return effect;
	}
	
	public void render(Graphics g) {
		if((timecounter >= timetoshow && timeleft>0) || effect == Effect.STATIC )
			font.drawString(x, y, text, color);
	}
	
	public boolean shouldRemove(){
		if(timeleft <= 0 && effect != Effect.STATIC)
			return true;
		return false;
	}
	
	public enum Effect{
		STATIC, UP, GRAVITY;
	}
	
	public enum Time{
		NONE(0), VERYSHORT(500), SHORT(1000), DEFAULT(2000), LONG(3500);
		
		private int duration;
		
		Time(int duration){
			this.duration = duration;
		}
		
		public int getDuration(){
			return this.duration;
		}
	}
}
