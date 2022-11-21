package br.com.provinggrounds.game.entity.wpn;

import br.com.provinggrounds.game.dungeon.Room;
import br.com.provinggrounds.game.entity.Body;
import br.com.provinggrounds.game.entity.Body.Outline;
import br.com.provinggrounds.game.entity.Body.Roundness;
import br.com.provinggrounds.game.entity.impl.Bullet;
import br.com.provinggrounds.game.game.MainGame;

public class Smg extends BaseWeapon{
	
	static {
		Body.registerClassBody(Shotgun.class, Roundness.MEDIUM, Outline.MINIMUM);
	}
	
	public Smg(){
		super(0.3f, 16, 100, 0.9f);
	}
	
	@Override
	protected void shoot(float x, float y, float velX, float velY, Room room) {
		room.addEntity(new Bullet(x, y, getBulletSize(),
				velX + (MainGame.RANDOM.nextFloat() * (0.1f)),
				velY  + (MainGame.RANDOM.nextFloat() * (0.1f)),
				getProjectileVelocity()));
	}

}
