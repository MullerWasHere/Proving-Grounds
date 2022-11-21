package br.com.provinggrounds.game.entity.impl;

import org.newdawn.slick.GameContainer;

import br.com.provinggrounds.game.dungeon.Room;
import br.com.provinggrounds.game.entity.Body;
import br.com.provinggrounds.game.entity.Body.Outline;
import br.com.provinggrounds.game.entity.Body.Roundness;
import br.com.provinggrounds.game.entity.Entity;

public class Bullet extends Entity{

	static {
		Body.registerClassBody(Bullet.class, Roundness.MAXIMUM, Outline.MAXIMUM);
	}
	
	public static int bid = 0;
	public int id;
	private float bulletSize;
	
	public Bullet(float x, float y, float bulletSize, float velX, float velY, float vel) {
		super(x, y, bulletSize, bulletSize, "", Type.BULLET);
		this.showTooltip = false;
		this.velocityCap = vel;
		collidable = true;
		canPassThrough = true;
		removesProjectile = false;
		setVelX(velX);
		setVelY(velY);
		id = ++bid;
		this.bulletSize = bulletSize;
	}
	
	@Override
	public void update(GameContainer c, int delta, Room room) {
		super.update(c, delta, room);
		
		
//		Direction dir = getDirection();
//		if(dir == Direction.DOWN){
//			setVelY(1);
//		}
//		else if(dir == Direction.UP){
//			setVelY(-1);
//		}
//		else if(dir == Direction.LEFT){
//			setVelX(-1);
//		}
//		else{
//			setVelX(1);
//		}
		
		rectangle.setX(rectangle.getX() + (getVelocityCap() * getVelX() * delta));
		rectangle.setY(rectangle.getY() + (getVelocityCap() * getVelY() * delta));
		if(getRectangle().getX() < 0 || getRectangle().getX() > Room.ROOM_SIZE_WIDTH)
			removeEntity();
		else if(getRectangle().getY() < 0 || getRectangle().getY() > Room.ROOM_SIZE_HEIGHT)
			removeEntity();
	}

	@Override
	public void collideAndCallback(Entity other, Room room, int delta) {
		if(shouldRemove())
			return;
		if(other.getRectangle().intersects(getRectangle())){
			if(other.shouldRemoveProjectile()){
				if(other.getType() == Type.MELEE_ENEMY){
					other.collideAndCallback(this, room, delta);
				}
				removeEntity();
			}
		}
	}
	
//	private static final int SIDE_SIZE = 16;
}
