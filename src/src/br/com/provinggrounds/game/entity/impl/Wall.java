package br.com.provinggrounds.game.entity.impl;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;

import br.com.provinggrounds.game.dungeon.Room;
import br.com.provinggrounds.game.entity.Body;
import br.com.provinggrounds.game.entity.Body.Outline;
import br.com.provinggrounds.game.entity.Body.Roundness;
import br.com.provinggrounds.game.entity.Entity;

public class Wall extends Entity{
	
	public Wall(float x, float y, float width, float height){
		super(x, y, width, height, "", Type.WALL);
		this.showTooltip = false;
		body = new Body(new Color(0x48573D), Roundness.NONE, Outline.NONE);
	}
	
	@Override
	public void update(GameContainer c, int delta, Room room) {
		
	}
	
	@Override
	public void collideAndCallback(Entity other, Room room, int delta) {
		
	}
	
	public static final int WALL_WIDTH = 32;
	public static final int WALL_HEIGHT = 32;


}
