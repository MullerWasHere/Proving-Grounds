package br.com.provinggrounds.game.entity.wpn;

import br.com.provinggrounds.game.dungeon.Room;
import br.com.provinggrounds.game.entity.Body;
import br.com.provinggrounds.game.entity.Body.Outline;
import br.com.provinggrounds.game.entity.Body.Roundness;

public abstract class BaseWeapon {
	
	private float ad;
	private Body body;
	private float bulletSize;
	private int gunTimer;
	private int gunCooldown;
	private float projectileVelocity;
	
	static {
		Body.registerClassBody(BaseWeapon.class, Roundness.MEDIUM, Outline.MEDIUM);
	}
	
	public BaseWeapon(){
		//look
		this.ad = 0.5f;
		this.bulletSize = 16;
		this.gunCooldown = 150;
		this.projectileVelocity = 0.7f;
	}
	
	public BaseWeapon(float ad, float bulletSize, int gunCooldown, float projectileVelocity){
		this.ad = ad;
		this.bulletSize = bulletSize;
		this.gunCooldown = gunCooldown;
		this.projectileVelocity = projectileVelocity;
	}
	
	protected abstract void shoot(float x, float y, float velX, float velY, Room room);
	
	public void tryShoot(float x, float y, float velX, float velY, Room room){
		if(gunTimer >= gunCooldown){
			shoot(x, y, velX, velY, room);
			gunTimer = 0;
		}
	}
	
	public void updateTimer(int delta){
		gunTimer += delta;
	}
	
	public int getGunCooldown(){
		return this.gunCooldown;
	}
	
	public void setGunCooldown(int cooldown){
		this.gunCooldown = cooldown;
	}
	
	public float getBulletSize(){
		return bulletSize;
	}
	
	public float getAD(){
		return ad;
	}

	public void setAD(float ad) {
		this.ad = ad;
	}
	
	public Body getBody(){
		return this.body;
	}
	
	public float bulletSize(){
		return this.bulletSize;
	}
	
	public float getProjectileVelocity(){
		return this.projectileVelocity;
	}
}
