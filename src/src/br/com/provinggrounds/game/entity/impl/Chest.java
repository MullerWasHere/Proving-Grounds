package br.com.provinggrounds.game.entity.impl;

import org.newdawn.slick.Color;

import br.com.provinggrounds.game.dungeon.Room;
import br.com.provinggrounds.game.entity.Body;
import br.com.provinggrounds.game.entity.Body.Outline;
import br.com.provinggrounds.game.entity.Body.Roundness;
import br.com.provinggrounds.game.entity.Entity;
import br.com.provinggrounds.game.entity.Notification;
import br.com.provinggrounds.game.entity.wpn.CommonGun;
import br.com.provinggrounds.game.entity.wpn.Shotgun;
import br.com.provinggrounds.game.entity.wpn.Smg;
import br.com.provinggrounds.game.game.Fonts;
import br.com.provinggrounds.game.game.MainGame;

public class Chest extends Entity{
	Gives gives;
	static {
		Body.registerClassBody(Chest.class, Roundness.NONE, Outline.MINIMUM);
	}
	
	public Chest(float x, float y){
		super(x,y,48,48,"Um bau!!",Type.CHEST);
		showTooltip = true;
		collidable = true;
		canPassThrough = true;
		removesProjectile = true;
		gives = Gives.generateDrop();
	}

	@Override
	public void collideAndCallback(Entity other, Room room, int delta) {
		if(other.getRectangle().intersects(getRectangle())){
			if(other.getType() == Type.PLAYER){
				if(gives == Gives.POTION){
					((Player)(other)).incHp(1);
					room.addNotification("Você achou uma poção!", getRectangle().getX(), getRectangle().getY(),
							Notification.Time.NONE, Fonts.enemyKilledFont, Color.white, Notification.Time.DEFAULT, Notification.Effect.UP);
				}else if(gives == Gives.CGUN){
					((Player)(other)).setWeapon(new CommonGun());
					room.addNotification("Você pegou uma pistola", getRectangle().getX(), getRectangle().getY(),
							Notification.Time.NONE, Fonts.enemyKilledFont, Color.white, Notification.Time.DEFAULT, Notification.Effect.UP);
				}else if(gives == Gives.SGUN){
					((Player)(other)).setWeapon(new Shotgun());
					room.addNotification("Você pegou uma shotgun", getRectangle().getX(), getRectangle().getY(),
							Notification.Time.NONE, Fonts.enemyKilledFont, Color.white, Notification.Time.DEFAULT, Notification.Effect.UP);
				}else if(gives == Gives.SMG){
					((Player)(other)).setWeapon(new Smg());
					room.addNotification("Você pegou uma smg", getRectangle().getX(), getRectangle().getY(),
							Notification.Time.NONE, Fonts.enemyKilledFont, Color.white, Notification.Time.DEFAULT, Notification.Effect.UP);
				}
				removeEntity();
			}
		}
	}
	
	public static enum Gives{
		POTION("Pocao",2), CGUN("Pistola", 3), SGUN("Shotgun", 4), SMG("SMG", 5);
		
		public final String name;
		public final int chance;
		private Gives(String name, int chance){
			this.name = name;
			this.chance = chance;
		}
		
		public static Gives generateDrop(){
			Gives gives = SGUN;
			for(int i = 0;i < Gives.values().length; i++){
				if(MainGame.RANDOM.nextInt(10) > Gives.values()[i].chance){
					gives = Gives.values()[i];
				}
			}
			return gives;
		}
	}
}
