package br.com.provinggrounds.game.game;

import org.newdawn.slick.SlickException;
import org.newdawn.slick.Sound;

import br.com.provinggrounds.game.dungeon.Dungeon;

public class Audio {
	private Sound[] fxhit;
	private Sound[] fxpowerup;
	private Sound[] fxcoin;
	
	public static Audio audio = new Audio();
	
	private Audio(){

	}
	
	public void initialize(){
		try {
			fxhit = new Sound[3];
			for(int i = 0;i < fxhit.length; i++){
				fxhit[i] = new Sound("res/fx/hit_" + String.valueOf(i+1) + ".wav");
			}
//			fxhit = new Sound("res/fx/hit_1.wav");
//			fxhit = new Sound("res/fx/hit_2.wav");
//			fxhit = new Sound("res/fx/hit_3.wav");
//			fxpowerup1 = new Sound("res/fx/powerup_1.wav");
//			fxpowerup2 = new Sound("res/fx/powerup_2.wav");
//			fxpowerup3 = new Sound("res/fx/powerup_3.wav");
			fxpowerup = new Sound[3];
			for(int i = 0;i < fxpowerup.length; i++){
				fxpowerup[i] = new Sound("res/fx/powerup_" + String.valueOf(i+1) + ".wav");
			}
			
			fxcoin = new Sound[3];
			for(int i = 0;i < fxcoin.length; i++){
				fxcoin[i] = new Sound("res/fx/coin_" + String.valueOf(i+1) + ".wav");
			}
			
		} catch (SlickException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void playFxHit(){
		fxhit[MainGame.RANDOM.nextInt(fxhit.length)].play();
	}
	
	public void playFxPowerUp(){
		fxpowerup[MainGame.RANDOM.nextInt(fxpowerup.length)].play();
	}
	
	public void playFxCoin(){
		fxcoin[MainGame.RANDOM.nextInt(fxcoin.length)].play();
	}
	
	
}
