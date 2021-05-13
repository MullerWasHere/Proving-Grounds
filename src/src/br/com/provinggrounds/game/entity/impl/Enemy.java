package br.com.provinggrounds.game.entity.impl;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;

import br.com.provinggrounds.game.dungeon.Dungeon;
import br.com.provinggrounds.game.dungeon.Room;
import br.com.provinggrounds.game.entity.Body;
import br.com.provinggrounds.game.entity.Entity;
import br.com.provinggrounds.game.entity.Body.Outline;
import br.com.provinggrounds.game.entity.Body.Roundness;
import br.com.provinggrounds.game.entity.Notification;
import br.com.provinggrounds.game.game.Fonts;
import br.com.provinggrounds.game.game.MainGame;

public class Enemy extends Entity{

	protected int ticks;
	protected boolean canTouchPlayer;
	protected float hp;
	
	public Enemy(float size, float x, float y) {
		super(size, Type.MELEE_ENEMY);
		body = new Body(new Color(0xCC2929), Roundness.NONE, Outline.MEDIUM);
		if(MainGame.RANDOM.nextBoolean())
			setVelX(1);
		else
			setVelX(-1);
		
		if(MainGame.RANDOM.nextBoolean())
			setVelY(1);
		else
			setVelY(-1);
		velocityCap = 0.08f;
		setX(x);
		setY(y);
		collidable = false;
		canTouchPlayer = true;
		showTooltip = true;
		tooltip = new String("Inimigo!");
		hp = MainGame.RANDOM.nextFloat() * (Dungeon.getCurrentLevel() - 2) + 2;
	}

	@Override
	public void collideAndCallback(Entity other, Room room, int delta) {
		if (other.getRectangle().intersects(getRectangle())) {
			if (other.getType() == Type.WALL) {
				ticks = 0;
				setVelX(-getVelX());
				setVelY(-getVelY());
			}
			
			if(other.getType() == Type.BULLET){
				hp -= Dungeon.getPlayer().getAtkDmg();
				if(hp<=0.0f){
					int goldGiven = MainGame.RANDOM.nextInt(Dungeon.getCurrentLevel()*5+2) + 5;
					room.addNotification(String.valueOf(goldGiven) + " gold!", getRectangle().getX() + 10, getRectangle().getY(), 100,Fonts.enemyKilledFont,Color.yellow,
							Notification.TIME_DEFAULT);
					//Dungeon.getCurrentLevel()
					Dungeon.getPlayer().addGold(goldGiven);
					if(MainGame.RANDOM.nextInt(30) > 25){//30 25
						room.addEntity(new Potion(getRectangle().getX(), getRectangle().getY()));
					}
					removeEntity();
				}
			}
			
			if(other.getType() == Type.PLAYER){
				((Player)other).hitByEnemy();
			}
		}else{
			
		}
	}

	@Override
	public void update(GameContainer c, int delta, Room room) {
		//NOMES INCORRETOS E FUNÇÕES DE TESTE
		ticks+=delta;
		wander();
		float displacementX = (float) ((getVelocityCap() * getVelX()) * delta);
		float displacementY = (float) ((getVelocityCap() * getVelY()) * delta);

		rectangle.setX(rectangle.getX() + displacementX);
		rectangle.setY(rectangle.getY() + displacementY);
		
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
