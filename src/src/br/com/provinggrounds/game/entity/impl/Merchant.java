package br.com.provinggrounds.game.entity.impl;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;

import br.com.provinggrounds.game.dungeon.Dungeon;
import br.com.provinggrounds.game.dungeon.Room;
import br.com.provinggrounds.game.entity.Body;
import br.com.provinggrounds.game.entity.Body.Outline;
import br.com.provinggrounds.game.entity.Body.Roundness;
import br.com.provinggrounds.game.entity.MerchantDialog;
import br.com.provinggrounds.game.entity.Entity;
import br.com.provinggrounds.game.entity.Notification;
import br.com.provinggrounds.game.game.Audio;
import br.com.provinggrounds.game.game.Fonts;
import br.com.provinggrounds.game.game.MainGame;

public class Merchant extends Entity{
	
	private int timerDialog = 0;
	private SELLS sells;
	
	public Merchant(float x, float y){
		super(x,y,48,48,"Um mercador!",Type.MERCHANT);
		showTooltip = true;
		body = new Body(Color.gray, Roundness.NONE, Outline.NONE);
		collidable = false;
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
						0, Fonts.enemyKilledFont, Color.red, Notification.TIME_LONG);
				room.addNotification("+100 gold!", getRectangle().getX(), getRectangle().getY(),
						Notification.TIME_LONG, Fonts.enemyKilledFont, Color.yellow, Notification.TIME_LONG);
				Dungeon.getPlayer().addGold(100);
				removeEntity();
			}
		}
	}
	
	public void retornarResposta(boolean resposta, Room room){
		if(!resposta)
			return;
		if(Dungeon.getPlayer().getGold() >= sells.price){
			Dungeon.getPlayer().addGold(-sells.price);
			room.addNotification("Você comprou " + sells.name +"!", getRectangle().getX(), getRectangle().getY(), 0, Fonts.enemyKilledFont,
					Color.white, Notification.TIME_LONG);
			room.addNotification("O mercador agora ira embora!", getRectangle().getX(), getRectangle().getY(), Notification.TIME_DEFAULT, Fonts.enemyKilledFont,
					Color.white, Notification.TIME_LONG);
			removeEntity();
			switch(sells.id){
			case 0: //POTION
				Dungeon.getPlayer().incHp(1);
				break;
				
			case 1: //AD
				Dungeon.getPlayer().setAtkDmg(Dungeon.getPlayer().getAtkDmg() + 1.0f);
				break;
				
			case 2: //ATK SPD
				Dungeon.getPlayer().setAtkSpdCooldown(Dungeon.getPlayer().getAtkSpdCoolDown() - 10);
				break;
			}
			Audio.audio.playFxCoin();
		}
		else{
			room.addNotification("Você não tem gold suficiente seu bastardo!", getRectangle().getX(), getRectangle().getY(), 0, Fonts.enemyKilledFont,
					Color.red, Notification.TIME_LONG);
		}
	}

	@Override
	public void update(GameContainer c, int delta, Room room) {
		if(timerDialog > 0)
			timerDialog -= delta;
	}
	
	public void setTimerDialog(int timerDialog){
		this.timerDialog = timerDialog;
	}
	
	public static enum SELLS{
		POTION("Pocao",20,0), DANO("Dano de ataque", 100, 1), VELOCIDADE("Velocidade de ataque", 150, 2);
		
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
