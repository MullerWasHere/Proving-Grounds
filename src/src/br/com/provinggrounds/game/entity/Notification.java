package br.com.provinggrounds.game.entity;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.TrueTypeFont;

import br.com.provinggrounds.game.dungeon.Room;

public class Notification{
	
	private int timetoshow;
	private int timecounter;
	private int timeleft;
	private float x;
	private float y;
	private float vy;
	private String text;
	private TrueTypeFont font;
	private Color color;
	
	
	public Notification(String text, float x, float y, int timetoshow,  TrueTypeFont font, Color color, int duration){
		this.timetoshow = timetoshow;
		timecounter = 0;
		this.x = x;
		this.y = y;
		this.vy = -0.09f;
		this.text = text;
		this.timeleft = TIME_DEFAULT;
		this.font = font;
		this.color = color;
	}

	public void update(GameContainer c, int delta, Room room) {
		timecounter += delta;
		if(timecounter >= timetoshow && timeleft>0){
			timeleft -= delta;
			y = y + delta * vy;
		}
	}
	
	public void render(Graphics g) {
		if(timecounter >= timetoshow && timeleft>0)
			font.drawString(x, y, text, color);
	}
	
	public boolean shouldRemove(){
		if(timeleft <= 0)
			return true;
		return false;
	}
	
	//TESTES!
	public static int TIME_DEFAULT = 1000;
	public static int TIME_SHORT = 500;
	public static int TIME_LONG = 1500;
}
