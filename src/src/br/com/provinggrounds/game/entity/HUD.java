package br.com.provinggrounds.game.entity;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;

import br.com.provinggrounds.game.dungeon.Dungeon;
import br.com.provinggrounds.game.dungeon.Room;
import br.com.provinggrounds.game.game.Fonts;
import br.com.provinggrounds.game.game.GameRun;

public class HUD {
	
	public HUD(){
		
	}
	
//	public void update(GameContainer c, int delta){
//		
//	}
	
	public void render(Graphics g){
		g.setColor(new Color(Color.white));
		g.fillRect(0, Room.ROOM_SIZE_HEIGHT, GameRun.WINDOW_WIDTH, GameRun.WINDOW_HEIGHT-Room.ROOM_SIZE_HEIGHT);
		Fonts.hudCurrentLevelFont.drawString(10,
				Room.ROOM_SIZE_HEIGHT+10,"Nivel: " +  String.valueOf(Dungeon.getCurrentLevel()+1), 
				Color.black);
		Fonts.hudCurrentLevelFont.drawString(10,
				Room.ROOM_SIZE_HEIGHT+Fonts.hudCurrentLevelFont.getLineHeight()+10,"Gold: " +  String.valueOf(Dungeon.getPlayer().getGold()),
				Color.black);
		Fonts.hudCurrentLevelFont.drawString(150,
				Room.ROOM_SIZE_HEIGHT+10,
				"HP: ",
				Color.black);
		
		g.setLineWidth(2);
		for(int i = 0;i < Dungeon.getPlayer().getHp(); i++){
			g.setColor(Color.red);
			g.fillRect(150 + Fonts.hudCurrentLevelFont.getWidth("HP: ") + (i*25), Room.ROOM_SIZE_HEIGHT+15, 20, 20);
			g.setColor(Color.black);
			g.drawRect(150 + Fonts.hudCurrentLevelFont.getWidth("HP: ") + (i*25), Room.ROOM_SIZE_HEIGHT+15, 20, 20);
		}
		g.setColor(Color.black);
		for(int i = 0; i < Dungeon.getPlayer().getMaxHp() - Dungeon.getPlayer().getHp(); i++){
			g.fillOval(150 + Fonts.hudCurrentLevelFont.getWidth("HP: ") + (Dungeon.getPlayer().getHp()*25) + (i*25), Room.ROOM_SIZE_HEIGHT+15, 20, 20);
		}
	}
}
