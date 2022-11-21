package br.com.provinggrounds.game.entity.impl;

import org.newdawn.slick.GameContainer;

import br.com.provinggrounds.game.dungeon.Dungeon;
import br.com.provinggrounds.game.dungeon.Room;
import br.com.provinggrounds.game.entity.Body;
import br.com.provinggrounds.game.entity.Body.Outline;
import br.com.provinggrounds.game.entity.Body.Roundness;
import br.com.provinggrounds.game.entity.Entity;

public class Arrow extends Entity{
	
	static
	{
		Body.registerClassBody(Arrow.class, Roundness.MAXIMUM, Outline.MAXIMUM);
	}
	
	public Arrow(float x, float y, float vx, float vy){
		super(x, y, ARROW_SIZE, ARROW_SIZE, "", Type.ARROW);
		this.showTooltip = false;
		velocityCap = 0.4f;
		collidable = true;
		canPassThrough = true;
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
		super.update(c, delta, room);
		rectangle.setX(rectangle.getX() + velocityCap * delta * velX);
		rectangle.setY(rectangle.getY() + velocityCap * delta * velY);
		if(getRectangle().getX() < 0 || getRectangle().getX() > Room.ROOM_SIZE_WIDTH)
			removeEntity();
		else if(getRectangle().getY() < 0 || getRectangle().getY() > Room.ROOM_SIZE_HEIGHT)
			removeEntity();
	}
	
	private static final int ARROW_SIZE = 24;
}
