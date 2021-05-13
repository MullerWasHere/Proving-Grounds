package br.com.provinggrounds.game.entity.impl;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;

import br.com.provinggrounds.game.dungeon.Room;
import br.com.provinggrounds.game.entity.Body;
import br.com.provinggrounds.game.entity.Body.Outline;
import br.com.provinggrounds.game.entity.Body.Roundness;
import br.com.provinggrounds.game.entity.Entity;
import br.com.provinggrounds.game.game.MainGame;

public class MeleeEnemy extends Enemy{
	
	public MeleeEnemy(float x, float y) {
		super(MeleeEnemy.MINIMUM_SIZE+MainGame.RANDOM.nextInt(32),x,y);
		body = new Body(Color.red, Roundness.MAXIMUM, Outline.MEDIUM);
		velocityCap = 0.1f;
	}

	@Override
	public void collideAndCallback(Entity other, Room room, int delta) {
		// TODO Auto-generated method stub
		super.collideAndCallback(other, room, delta);
	}

	@Override
	public void update(GameContainer c, int delta, Room room) {
		super.update(c, delta, room);
	}
	
	public static float MINIMUM_SIZE = 32;

}
