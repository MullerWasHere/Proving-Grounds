package br.com.provinggrounds.game.entity.impl;

import org.newdawn.slick.GameContainer;

import br.com.provinggrounds.game.dungeon.Dungeon;
import br.com.provinggrounds.game.dungeon.Room;
import br.com.provinggrounds.game.entity.Body;
import br.com.provinggrounds.game.entity.Body.Outline;
import br.com.provinggrounds.game.entity.Body.Roundness;
import br.com.provinggrounds.game.entity.Entity;
import br.com.provinggrounds.game.game.MainGame;

public class MeleeEnemy extends Enemy{
	
	static{
		Body.registerClassBody(MeleeEnemy.class, Roundness.MAXIMUM, Outline.MEDIUM);
	}
	
	private boolean fury;
	private int furyTimer = FURY_TIMER;
	private int furyTimeoutTimer = 0;
	
	public MeleeEnemy(float x, float y) {
		super(MeleeEnemy.MINIMUM_SIZE+MainGame.RANDOM.nextInt(32),x,y);
		velocityCap = NORMAL_VELOCITY;
		fury = false;
	}

	@Override
	public void collideAndCallback(Entity other, Room room, int delta) {
		super.collideAndCallback(other, room, delta);
	}

	@Override
	public void update(GameContainer c, int delta, Room room) {
		super.update(c, delta, room);
		furyTimer -= delta;
		if(furyTimer <= 0){
			furyTimer = FURY_TIMER + MainGame.RANDOM.nextInt(4500) + 1500;
			if(MainGame.RANDOM.nextInt(10) > 4){
				fury = true;
				velocityCap = FURY_VELOCITY;
				furyTimeoutTimer = 0;
				System.out.println("INTO THE FIRE!!");
			}
		}
		if(fury){
			furyTimeoutTimer += delta;
			if(furyTimeoutTimer >= FURY_TIMEOUT){
				fury = false;
				velocityCap = NORMAL_VELOCITY;
			}else{
				double angle = Math.atan2(Dungeon.getPlayer().getRectangle().getY() - rectangle.getY(),
						Dungeon.getPlayer().getRectangle().getX() - rectangle.getX());
				setVelX((float)Math.cos(angle));
				setVelY((float)Math.sin(angle));
			}
		}
	}
	
	@Override
	protected void wander() {
		if(!fury)
			super.wander();
	}
	
	@Override
	public void collideWall() {
		if(!fury)
			super.collideWall();
	}
	
//	@Override
//	public void move(int delta) {
//		//double angle = Math.atan2(rectangle.getY() - Dungeon.getPlayer().getRectangle().getY(),
////		rectangle.getX() - Dungeon.getPlayer().getRectangle().getX());
////Arrow arrow = new Arrow(Dungeon.getPlayer().getRectangle().getX(), Dungeon.getPlayer().getRectangle().getY(), (float)Math.cos(angle), (float)Math.sin(angle));
////Dungeon.getCurrentRoom().addEntity(arrow);
//		
//	}
	
	public static float MINIMUM_SIZE = 32;
	private static final float NORMAL_VELOCITY = 0.1F;
	private static final float FURY_VELOCITY = 0.25f;
	
	private static final int FURY_TIMER = 1000;
	private static final int FURY_TIMEOUT = 2500;

}
