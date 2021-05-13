package br.com.provinggrounds.game.entity.impl;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;

import br.com.provinggrounds.game.dungeon.Dungeon;
import br.com.provinggrounds.game.dungeon.Room;
import br.com.provinggrounds.game.entity.Body;
import br.com.provinggrounds.game.entity.Entity;
import br.com.provinggrounds.game.entity.Body.Outline;
import br.com.provinggrounds.game.entity.Body.Roundness;

public class Arrow extends Entity{
	
	public Arrow(float x, float y, float vx, float vy){
		super(x, y, ARROW_SIZE, ARROW_SIZE, "", Type.ARROW);
		this.showTooltip = false;
		velocityCap = 0.6f;
		body = new Body(new Color(0x1D1E24), Roundness.MAXIMUM, Outline.MAXIMUM);
		collidable = false;
		removesProjectile = false;
		this.velX = vx;
		this.velY = vy;
	}
	
	@Override
	public void collideAndCallback(Entity other, Room room, int delta) {
		if(other.getRectangle().intersects(getRectangle())){
			if(other.getType() == Type.PLAYER){
				Dungeon.getPlayer().hitByEnemy();
				removeEntity();
			}
		}
	}

	@Override
	public void update(GameContainer c, int delta, Room room) {
		rectangle.setX(rectangle.getX() + velocityCap * delta * velX);
		rectangle.setY(rectangle.getY() + velocityCap * delta * velY);
	}
	
	private static final int ARROW_SIZE = 26;
}
