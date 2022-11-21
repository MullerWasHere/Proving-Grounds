package br.com.provinggrounds.game.entity.wpn;

import br.com.provinggrounds.game.dungeon.Room;
import br.com.provinggrounds.game.entity.Body;
import br.com.provinggrounds.game.entity.Body.Outline;
import br.com.provinggrounds.game.entity.Body.Roundness;
import br.com.provinggrounds.game.entity.impl.Bullet;

public class CommonGun extends BaseWeapon{
	
	static {
		Body.registerClassBody(CommonGun.class, Roundness.MEDIUM, Outline.MEDIUM);
	}
	public CommonGun(){
		super(0.7f, 12, 260, 0.6f);
	}

	@Override
	protected void shoot(float x, float y, float velX, float velY, Room room) {
		room.addEntity(new Bullet(x, y, getBulletSize(), velX, velY, getProjectileVelocity()));
	}
}
