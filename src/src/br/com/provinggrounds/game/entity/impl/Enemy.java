package br.com.provinggrounds.game.entity.impl;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;

import br.com.provinggrounds.game.dungeon.Dungeon;
import br.com.provinggrounds.game.dungeon.Room;
import br.com.provinggrounds.game.entity.Entity;
import br.com.provinggrounds.game.entity.Notification;
import br.com.provinggrounds.game.entity.Notification.Effect;
import br.com.provinggrounds.game.entity.Notification.Time;
import br.com.provinggrounds.game.game.Audio;
import br.com.provinggrounds.game.game.Fonts;
import br.com.provinggrounds.game.game.MainGame;

public class Enemy extends Entity{

	protected int ticks;
	protected boolean canTouchPlayer;
	protected float hp, maxhp;
	protected float visualhp;
	protected int lifeBarTimer;
	
	public Enemy(float size, float x, float y) {
		super(size, Type.MELEE_ENEMY);
		lifeBarTimer = 0;
		if(MainGame.RANDOM.nextBoolean())
			setVelX(1);
		else
			setVelX(-1);
		
		if(MainGame.RANDOM.nextBoolean())
			setVelY(1);
		else
			setVelY(-1);
		velocityCap = 0.001f;
		setX(x);
		setY(y);
		collidable = false;
		canPassThrough = true;
		canTouchPlayer = true;
		showTooltip = true;
		tooltip = new String("Inimigo!");
		visualhp = maxhp = hp = MainGame.RANDOM.nextFloat() * (Dungeon.getCurrentLevel() + 0.5f) + 2.f;
		
		removesProjectile = true;
	}

	@Override
	public void collideAndCallback(Entity other, Room room, int delta) {		
		if (other.getRectangle().intersects(getRectangle())) {
			if (other.getType() == Type.WALL) {
				collideWall();
			}
			
			if (other.getType() == Type.BULLET) {
				if(other.shouldRemove() || shouldRemove())
					return;
				hp -= Dungeon.getPlayer().getAtkDmg();
				lifeBarTimer = 1500;
				if (hp <= 0.0f) {
					lifeBarTimer = 0;
					if(MainGame.RANDOM.nextInt(10) > 4){
						int goldGiven = MainGame.RANDOM.nextInt(Dungeon
								.getCurrentLevel() * 5 + 2) + 5;
						room.addNotification(String.valueOf(goldGiven) + " gold!",
								getRectangle().getX() + 10, getRectangle().getY(),
								Time.NONE, Fonts.enemyKilledFont, Color.yellow,
								Notification.Time.SHORT, Effect.GRAVITY);
						// Dungeon.getCurrentLevel()
						Dungeon.getPlayer().addGold(goldGiven);
						Audio.audio.playFxCoin();
					}



					if (MainGame.RANDOM.nextInt(30) > 25) {// 30 25
						room.addEntity(new Potion(getRectangle().getX(),
								getRectangle().getY()));
					}
					removeEntity();
					room.decEnemyCount();
					
				}
				other.removeEntity();
			}
			
			if (other.getType() == Type.PLAYER) {
				((Player) other).hitByEnemy();
			}
		}
	}

	@Override
	public void update(GameContainer c, int delta, Room room) {
		//NOMES INCORRETOS E FUNÇÕES DE TESTE
		super.update(c, delta, room);
		if (lifeBarTimer > 0)
			lifeBarTimer -= delta;

		if (visualhp < hp) {
			visualhp += 0.1f;
		} else if (visualhp > hp) {
			visualhp -= 0.1f;
		}

		float dist = Math.abs(hp - visualhp);
		if (dist <= 0.1f && dist != 0.0f) {
			visualhp = hp;
		}
		
		ticks+=delta;
		wander();
		move(delta);
	}
	
	public void collideWall(){
		ticks = 0;
		setVelX(-getVelX());
		setVelY(-getVelY());
	}
	
	public void move(int delta){
		float displacementX = (float) ((getVelocityCap() * getVelX()) * delta);
		float displacementY = (float) ((getVelocityCap() * getVelY()) * delta);
		
		if (rectangle.getX() + displacementX >= Wall.WALL_WIDTH
				|| rectangle.getX() + rectangle.getWidth() + displacementX <= Room.ROOM_SIZE_WIDTH
						- Wall.WALL_WIDTH) {
			rectangle.setX(rectangle.getX() + displacementX);
		} else {
			if(rectangle.getX() + displacementX < Wall.WALL_WIDTH)
				rectangle.setX(Wall.WALL_WIDTH+1);
			else if(rectangle.getX() + rectangle.getWidth() + displacementX > Room.ROOM_SIZE_WIDTH - Wall.WALL_WIDTH)
				rectangle.setX(Room.ROOM_SIZE_WIDTH - Wall.WALL_WIDTH - 1);
		}

		if (rectangle.getY() + displacementY >= Wall.WALL_HEIGHT
				|| rectangle.getY() + rectangle.getHeight() + displacementY <= Room.ROOM_SIZE_HEIGHT
						- Wall.WALL_HEIGHT) {
			rectangle.setY(rectangle.getY() + displacementY);
		} else {
			if(rectangle.getY() + displacementY < Wall.WALL_HEIGHT)
				rectangle.setY(Wall.WALL_HEIGHT+1);
			else if(rectangle.getY() + rectangle.getHeight() + displacementY > Room.ROOM_SIZE_HEIGHT- Wall.WALL_HEIGHT)
				rectangle.setY(Room.ROOM_SIZE_HEIGHT - Wall.WALL_HEIGHT - 1);
		}

	}
	
	@Override
	public void render(Graphics g) {
		super.render(g);
		if (lifeBarTimer > 0 && hp > 0.0f) {
			g.setColor(Color.red);
			g.fillRect(getRectangle().getX(), getRectangle().getY() - 20,
					100 * (visualhp / maxhp), 10);
			g.setColor(Color.black);
			g.fillRect(getRectangle().getX() + (100 * (visualhp / maxhp)),
					getRectangle().getY() - 20,
					100 * ((maxhp - visualhp) / maxhp), 10);
		}

	}
	
	protected void wander(){
		if(ticks>=TICKS_CHANGEDIRECTION){
			ticks=0;
			if(MainGame.RANDOM.nextInt(10) > 7){
				setVelX(-getVelX());
			}
			if(MainGame.RANDOM.nextInt(10) > 7){
				setVelY(-getVelY());
			}
		}
	}
	
	
	public static int TICKS_CHANGEDIRECTION = 700;

}
