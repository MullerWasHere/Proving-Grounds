package br.com.provinggrounds.game.entity.wpn;


import br.com.provinggrounds.game.dungeon.Room;
import br.com.provinggrounds.game.entity.Body;
import br.com.provinggrounds.game.entity.Body.Outline;
import br.com.provinggrounds.game.entity.Body.Roundness;
import br.com.provinggrounds.game.entity.impl.Bullet;


public class Shotgun extends BaseWeapon{
	
	static {
		Body.registerClassBody(Shotgun.class, Roundness.MEDIUM, Outline.MINIMUM);
	}
	
	public Shotgun(){
		super(0.6f, 18, 600, 0.8f);
	}

	@Override
	protected void shoot(float x, float y, float velX, float velY, Room room) {
		room.addEntity(new Bullet(x, y, getBulletSize(), velX, velY, getProjectileVelocity()));
		if(velY != 0.0f){
			room.addEntity(new Bullet(x, y, getBulletSize(), velX + 0.2f, velY, getProjectileVelocity()));
			room.addEntity(new Bullet(x, y, getBulletSize(), velX + 0.1f, velY,  getProjectileVelocity()));
			room.addEntity(new Bullet(x, y, getBulletSize(), velX - 0.1f, velY, getProjectileVelocity()));
			room.addEntity(new Bullet(x, y, getBulletSize(), velX - 0.2f, velY,  getProjectileVelocity()));
		}else if(velX != 0.0f){
			room.addEntity(new Bullet(x, y, getBulletSize(), velX, velY + 0.2f, getProjectileVelocity()));
			room.addEntity(new Bullet(x, y, getBulletSize(), velX, velY + 0.1f,getProjectileVelocity()));
			room.addEntity(new Bullet(x, y, getBulletSize(), velX, velY - 0.1f,  getProjectileVelocity()));		
			room.addEntity(new Bullet(x, y, getBulletSize(), velX, velY - 0.2f, getProjectileVelocity()));
		}

	}
}
