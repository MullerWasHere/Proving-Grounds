package br.com.provinggrounds.game.entity.impl;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;

import br.com.provinggrounds.game.dungeon.Dungeon;
import br.com.provinggrounds.game.dungeon.Room;
import br.com.provinggrounds.game.entity.Body;
import br.com.provinggrounds.game.entity.Entity;
import br.com.provinggrounds.game.entity.Body.Outline;
import br.com.provinggrounds.game.entity.Body.Roundness;
import br.com.provinggrounds.game.game.MainGame;

public class RangedEnemy extends Enemy{

	int shootCooldown;
	public RangedEnemy(float x, float y) {
		super(MINIMUM_SIZE+MainGame.RANDOM.nextInt(12),x,y);
		body = new Body(Color.pink, Roundness.MAXIMUM, Outline.MEDIUM);
		velocityCap = 0.1f;
		shootCooldown = SHOOT_DEFAULT_BASE_TIME + MainGame.RANDOM.nextInt(SHOOT_RANDOM_VALUE);
	}
	
	
	
	@Override
	public void collideAndCallback(Entity other, Room room, int delta) {
		super.collideAndCallback(other, room, delta);
	}

	@Override
	public void render(Graphics g) {
		super.render(g);
	}
	
	@Override
	public void update(GameContainer c, int delta, Room room) {
		super.update(c, delta, room);
		shootCooldown -= delta;
		if(shootCooldown <= 0){
			double angle = Math.atan2(Dungeon.getPlayer().getRectangle().getY() - rectangle.getY(),
					Dungeon.getPlayer().getRectangle().getX() - rectangle.getX());
			float variationX = MainGame.RANDOM.nextFloat() * (0.3f - 0.01f) + 0.01f;
			float variationY = MainGame.RANDOM.nextFloat()  * (0.3f - 0.01f) + 0.01f;
			if(MainGame.RANDOM.nextBoolean())
				variationX = -variationX;
			if(MainGame.RANDOM.nextBoolean())
				variationY = -variationY;
			
			Arrow arrow = new Arrow(getRectangle().getX(), getRectangle().getY(), (float)Math.cos(angle) + variationX, (float)Math.sin(angle) + variationY);
			room.addEntity(arrow);
			shootCooldown = SHOOT_DEFAULT_BASE_TIME + MainGame.RANDOM.nextInt(SHOOT_RANDOM_VALUE);
		}
	}
	
	private static final int MINIMUM_SIZE = 26;
	private static final int SHOOT_DEFAULT_BASE_TIME = 1000;
	private static final int SHOOT_RANDOM_VALUE = 500;
}
