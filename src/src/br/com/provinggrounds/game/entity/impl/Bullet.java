package br.com.provinggrounds.game.entity.impl;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;

import br.com.provinggrounds.game.dungeon.Room;
import br.com.provinggrounds.game.entity.Body;
import br.com.provinggrounds.game.entity.Body.Outline;
import br.com.provinggrounds.game.entity.Body.Roundness;
import br.com.provinggrounds.game.entity.Entity;

public class Bullet extends Entity{

	public Bullet(Direction direction, float x, float y) {
		super(x, y, SIDE_SIZE, SIDE_SIZE, "", Type.BULLET);
		this.direction = direction; 
		this.showTooltip = false;
		velocityCap = 0.7f;
		body = new Body(new Color(0x1D1E24), Roundness.MAXIMUM, Outline.MAXIMUM);
		collidable = false;
		removesProjectile = false;
	}
	
	@Override
	public void update(GameContainer c, int delta, Room room) {
		Direction dir = getDirection();
		if(dir == Direction.DOWN){
			setVelY(1);
		}
		else if(dir == Direction.UP){
			setVelY(-1);
		}
		else if(dir == Direction.LEFT){
			setVelX(-1);
		}
		else{
			setVelX(1);
		}
		
		rectangle.setX(rectangle.getX() + (getVelocityCap() * getVelX() * delta));
		rectangle.setY(rectangle.getY() + (getVelocityCap() * getVelY() * delta));
	}

	@Override
	public void collideAndCallback(Entity other, Room room, int delta) {

		if(other.getRectangle().intersects(getRectangle())){
			if(other.shouldRemoveProjectile()){
				removeEntity();
			}
		}
	}
	
	private static final int SIDE_SIZE = 16;
}
