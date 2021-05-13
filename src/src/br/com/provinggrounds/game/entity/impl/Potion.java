package br.com.provinggrounds.game.entity.impl;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;

import br.com.provinggrounds.game.dungeon.Dungeon;
import br.com.provinggrounds.game.dungeon.Room;
import br.com.provinggrounds.game.entity.Body;
import br.com.provinggrounds.game.entity.Body.Outline;
import br.com.provinggrounds.game.entity.Body.Roundness;
import br.com.provinggrounds.game.entity.Entity;
import br.com.provinggrounds.game.entity.Notification;
import br.com.provinggrounds.game.game.Audio;
import br.com.provinggrounds.game.game.Fonts;
import br.com.provinggrounds.game.game.MainGame;

public class Potion extends Entity{

	private long expiresTimer = 0;
	private POTIONTYPE potiontype;
	protected Potion(float x, float y) {
		//super(18, Entity.Type.POTION);
		super(x,y,POTION_SIZE,POTION_SIZE,"Poção de vida!",Entity.Type.POTION);
		potiontype = POTIONTYPE.HP;
		if(MainGame.RANDOM.nextInt(20) > POTIONTYPE.MAXHP.chance){
			System.out.println("vida maxima!");
			tooltip = "Poção de vida máxima!";
			body = new Body(Color.cyan, Roundness.MEDIUM, Outline.NONE);
			potiontype = POTIONTYPE.MAXHP;
		}
		else if(MainGame.RANDOM.nextInt(20) > POTIONTYPE.ATKDMG.chance){
			tooltip = "Poção de dano de ataque!";
			body = new Body(Color.green, Roundness.MEDIUM, Outline.NONE);
			potiontype = POTIONTYPE.ATKDMG;
		}
		else if(MainGame.RANDOM.nextInt(20) > POTIONTYPE.ATKSPD.chance){
			tooltip = "Poção de velocidade de ataque!";
			body = new Body(Color.cyan, Roundness.MEDIUM, Outline.NONE);
			potiontype = POTIONTYPE.ATKSPD;
		}else{
			body = new Body(Color.pink, Roundness.MEDIUM, Outline.NONE);
			potiontype = POTIONTYPE.HP;
		}
		collidable = false;
		showTooltip = true;
		
		removesProjectile = false;
	}

	@Override
	public void collideAndCallback(Entity other, Room room, int delta) {
		if(other.getRectangle().intersects(getRectangle())){
			if(other.getType() == Type.PLAYER){
				switch(potiontype.id){
				case 0:
					Dungeon.getPlayer().incHp(1);
					break;
				case 1:
					Dungeon.getPlayer().incMaxHp(1);
					break;
				case 2:
					Dungeon.getPlayer().setAtkDmg(Dungeon.getPlayer().getAtkDmg() + 1.0f);
					break;
				case 3:
					Dungeon.getPlayer().setAtkSpdCooldown(Dungeon.getPlayer().getAtkSpdCoolDown() - 10);
					break;
				}
				Audio.audio.playFxPowerUp();
				room.addNotification("Você pega uma " + potiontype.titulo, getRectangle().getX(), getRectangle().getY(), 0, Fonts.enemyKilledFont, Color.white, Notification.TIME_LONG);
				removeEntity();
			}
		}
	}

	@Override
	public void update(GameContainer c, int delta, Room room) {
		if(expiresTimer == 0)
			expiresTimer = c.getTime()+POTION_EXPIRE;
		if(c.getTime() >= expiresTimer){
			removeEntity();
		}
		
	}

	private static final int POTION_SIZE = 24;
	private static final int POTION_EXPIRE = 5000;
	
	private enum POTIONTYPE{
		HP(0,0, "de vida"), MAXHP(1,15, "de vida máxima"), ATKDMG(2,17, "de dano de ataque"), ATKSPD(3,16, "de velocidade de ataque");
		public final int id;
		public final int chance;
		public final String titulo;
		private POTIONTYPE(int id, int chance, String titulo){
			this.id = id;
			this.chance = chance;
			this.titulo = titulo;
		}
	}
}
