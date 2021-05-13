package br.com.provinggrounds.game.entity;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;

import br.com.provinggrounds.game.dungeon.Dungeon;
import br.com.provinggrounds.game.dungeon.Room;
import br.com.provinggrounds.game.game.Fonts;
import br.com.provinggrounds.game.game.GameRun;

public class HUD {
	
	public HUD(){
		
	}
	
	public void update(GameContainer c, int delta){
		
	}
	
	public void render(Graphics g){
		g.setColor(new Color(0x408068));
		g.fillRect(0, Room.ROOM_SIZE_HEIGHT, GameRun.WINDOW_WIDTH, GameRun.WINDOW_HEIGHT-Room.ROOM_SIZE_HEIGHT);
		g.setColor(new Color(0x407c80));
		g.setLineWidth(4);
		g.drawRect(0, Room.ROOM_SIZE_HEIGHT, GameRun.WINDOW_WIDTH, GameRun.WINDOW_HEIGHT-Room.ROOM_SIZE_HEIGHT);
		Fonts.hudCurrentLevelFont.drawString(10, Room.ROOM_SIZE_HEIGHT+10,"Nivel: " +  String.valueOf(Dungeon.getCurrentLevel()+1));
		Fonts.hudCurrentLevelFont.drawString(10, Room.ROOM_SIZE_HEIGHT+Fonts.hudCurrentLevelFont.getHeight()+10,"Gold: " +  String.valueOf(Dungeon.getPlayer().getGold()));
		Fonts.hudCurrentLevelFont.drawString(150, Room.ROOM_SIZE_HEIGHT+10, "HP: ");
		
		g.setColor(Color.blue);
		for(int i = 0;i < Dungeon.getPlayer().getHp(); i++){
			g.fillOval(150 + Fonts.hudCurrentLevelFont.getWidth("HP: ") + (i*25), Room.ROOM_SIZE_HEIGHT+15, 20, 20);
		}
		g.setColor(Color.black);
		for(int i = 0; i < Dungeon.getPlayer().getMaxHp() - Dungeon.getPlayer().getHp(); i++){
			g.fillOval(150 + Fonts.hudCurrentLevelFont.getWidth("HP: ") + (Dungeon.getPlayer().getHp()*25) + (i*25), Room.ROOM_SIZE_HEIGHT+15, 20, 20);
		}
	}
}
