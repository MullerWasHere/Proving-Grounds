package br.com.provinggrounds.game.entity.impl;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;

import br.com.provinggrounds.game.dungeon.Dungeon;
import br.com.provinggrounds.game.dungeon.Room;
import br.com.provinggrounds.game.entity.Body;
import br.com.provinggrounds.game.entity.Body.Outline;
import br.com.provinggrounds.game.entity.Body.Roundness;
import br.com.provinggrounds.game.entity.Notification.Effect;
import br.com.provinggrounds.game.entity.Notification.Time;
import br.com.provinggrounds.game.entity.wpn.CommonGun;
import br.com.provinggrounds.game.entity.wpn.Shotgun;
import br.com.provinggrounds.game.entity.wpn.Smg;
import br.com.provinggrounds.game.entity.MerchantDialog;
import br.com.provinggrounds.game.entity.Entity;
import br.com.provinggrounds.game.entity.Notification;
import br.com.provinggrounds.game.game.Audio;
import br.com.provinggrounds.game.game.Fonts;
import br.com.provinggrounds.game.game.MainGame;

public class Merchant extends Entity{
	
	private int timerDialog = 0;
	private SELLS sells;
	
	static {
		Body.registerClassBody(Merchant.class, Roundness.NONE, Outline.MINIMUM);
	}
	
	public Merchant(float x, float y){
		super(x,y,48,48,"Um mercador!",Type.MERCHANT);
		showTooltip = true;
		collidable = true;
		canPassThrough = true;
		removesProjectile = true;
		sells = SELLS.values()[MainGame.RANDOM.nextInt(SELLS.values().length)];
	}

	@Override
	public void collideAndCallback(Entity other, Room room, int delta) {
		if(other.getRectangle().intersects(getRectangle())){
			if(other.getType() == Type.PLAYER && timerDialog<=0){
				room.setDialog(new MerchantDialog("Deseja comprar " + sells.name +" por " + String.valueOf(sells.price) + "?"));
			}
			else if(other.getType() == Type.BULLET){
				room.addNotification("Você matou o mercado!! :O", getRectangle().getX(), getRectangle().getY(),
						Time.NONE, Fonts.enemyKilledFont, Color.red, Notification.Time.LONG, Effect.UP);
				room.addNotification("+100 gold!", getRectangle().getX(), getRectangle().getY(),
						Notification.Time.SHORT, Fonts.enemyKilledFont, Color.yellow, Notification.Time.LONG, Effect.UP);
				Dungeon.getPlayer().addGold(100);
				removeEntity();
				other.removeEntity();
			}
		}
	}
	
	public void retornarResposta(boolean resposta, Room room){
		if(!resposta)
			return;
		if(Dungeon.getPlayer().getGold() >= sells.price){
			Dungeon.getPlayer().addGold(-sells.price);
			room.addNotification("Você comprou " + sells.name +"!", getRectangle().getX(), getRectangle().getY(), Notification.Time.NONE, Fonts.enemyKilledFont,
					Color.white, Notification.Time.LONG, Effect.UP);
			room.addNotification("O mercador agora ira embora!", getRectangle().getX(), getRectangle().getY(), Notification.Time.VERYSHORT, Fonts.enemyKilledFont,
					Color.white, Notification.Time.LONG, Effect.UP);
			removeEntity();
			switch(sells.id){
			case 0: //POTION
				Dungeon.getPlayer().incHp(1);
				break;
				
			case 1: //SGUN
				Dungeon.getPlayer().setWeapon(new Shotgun());
				break;
				
			case 2: //CGUN
				Dungeon.getPlayer().setWeapon(new CommonGun());
				break;
				
			case 3:
				Dungeon.getPlayer().setWeapon(new Smg());
				break;
			}
			Audio.audio.playFxCoin();
		}
		else{
			room.addNotification("Você não tem gold suficiente seu bastardo!", getRectangle().getX(), getRectangle().getY(), Notification.Time.NONE, Fonts.enemyKilledFont,
					Color.red, Notification.Time.LONG, Effect.UP);
		}
	}

	@Override
	public void update(GameContainer c, int delta, Room room) {
		super.update(c, delta, room);
		if(timerDialog > 0)
			timerDialog -= delta;
	}
	
	public void setTimerDialog(int timerDialog){
		this.timerDialog = timerDialog;
	}
	
	public static enum SELLS{
		POTION("Pocao",20,0), SGUN("Shotgun", 600, 1), CGUN("Pistola", 200, 2),
		SMG("SMG", 450, 3);
		
		public final String name;
		public final int price;
		public final int id;
		private SELLS(String name, int price, int id){
			this.name = name;
			this.price = price;
			this.id = id;
		}
	}
}
