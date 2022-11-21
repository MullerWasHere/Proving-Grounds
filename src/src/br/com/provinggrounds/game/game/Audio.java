package br.com.provinggrounds.game.game;

import java.util.ArrayList;

import org.newdawn.slick.Music;
import org.newdawn.slick.MusicListener;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.Sound;

import br.com.provinggrounds.game.dungeon.Room;

public class Audio implements MusicListener{
	private Sound[] fxhit;
	private Sound[] fxpowerup;
	private Sound[] fxcoin;
	private Sound fxGameOver;
	
	private Music[] msCalm, msCombat;
	
	private ArrayList<Music> musicList;
	
	private MusicPlaying musicPlaying;
	//private MusicPlaying curRoomMusic;
	
	private float curVolume = 0.0f;
	private int curMsCalmIndex = -1;
	private int curMsCombatIndex = -1;
	
	public static Audio audio = new Audio();
	public void initialize(){
		musicList = new ArrayList<Music>();
		try {
			fxhit = new Sound[3];
			for(int i = 0;i < fxhit.length; i++){
				fxhit[i] = new Sound("res/fx/hit_" + String.valueOf(i+1) + ".wav");
			}
			
			fxpowerup = new Sound[3];
			for(int i = 0;i < fxpowerup.length; i++){
				fxpowerup[i] = new Sound("res/fx/powerup_" + String.valueOf(i+1) + ".wav");
			}
			
			fxcoin = new Sound[3];
			for(int i = 0;i < fxcoin.length; i++){
				fxcoin[i] = new Sound("res/fx/coin_" + String.valueOf(i+1) + ".wav");
			}
			
			fxGameOver = new Sound("res/fx/gameover.ogg");
			
			
			msCalm = new Music[2];
			for(int i = 0;i < msCalm.length; i++){
				msCalm[i] = new Music("res/music/calm_" + String.valueOf(i+1) + ".ogg");
				msCalm[i].addListener(this);
				musicList.add(msCalm[i]);
			}
			
			msCombat = new Music[2];
			for(int i = 0;i < msCombat.length; i++){
				msCombat[i] = new Music("res/music/combat_" + String.valueOf(i+1) + ".ogg");
				msCombat[i].addListener(this);
				musicList.add(msCombat[i]);
			}
			
			
			musicPlaying = MusicPlaying.NONE;
			
			
			
		} catch (SlickException e) {
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
	
	public void playFxGameOver(){
		fxGameOver.play();
	}
	
	public void playMsCalm(){
		curMsCalmIndex = MainGame.RANDOM.nextInt(msCalm.length);
		msCalm[curMsCalmIndex].play(1, curVolume);
		musicPlaying = MusicPlaying.MUSIC_CALM;
	}
	
	public void resumeMsCalm(){
		stopMusic();
		System.out.println("resuming calm");
		if(curMsCalmIndex == -1)
			playMsCalm();
		else
			msCalm[curMsCalmIndex].play();
	}
	
	public void playMsCombat(){
		curMsCombatIndex = MainGame.RANDOM.nextInt(msCombat.length);
		msCombat[curMsCombatIndex].play(1, curVolume);
		musicPlaying = MusicPlaying.MUSIC_COMBAT;
	}
	
	public void resumeMsCombat(){
		stopMusic();
		System.out.println("resuming combat");
		if(curMsCombatIndex == -1)
			playMsCombat();
		else
			msCombat[curMsCombatIndex].play();
	}
	
	public void stopMusic(){
		for(int i = 0;i < musicList.size(); i++){
			if(musicList.get(i).playing()){
				musicList.get(i).stop();
			}
		}
		curVolume = 0.0f;
		musicPlaying = MusicPlaying.NONE;
	}
	
	public enum MusicPlaying{
		NONE, MUSIC_CALM, MUSIC_COMBAT;
		
		MusicPlaying(){
			
		}
	}

	@Override
	public void musicEnded(Music music) {
		stopMusic();
		if(musicPlaying == MusicPlaying.MUSIC_CALM){
			playMsCalm();
		}else if(musicPlaying == MusicPlaying.MUSIC_COMBAT){
			playMsCombat();
		}
	}

	@Override
	public void musicSwapped(Music music, Music newMusic) {
		
	}
	
	public void audioUpdate(int delta, Room room){
		if(room.getRoomType().getMusicType() != musicPlaying){
			stopMusic();
			this.musicPlaying = room.getRoomType().getMusicType();
			if(musicPlaying == MusicPlaying.MUSIC_CALM){
				playMsCalm();
			}else if(musicPlaying == MusicPlaying.MUSIC_COMBAT){
				playMsCombat();
			}
			curVolume = 0.0f;
		}
			
			
		if(curVolume < .3f){
			curVolume += 0.00008f * delta;
			if(musicPlaying == MusicPlaying.MUSIC_CALM){
				msCalm[curMsCalmIndex].setVolume(curVolume);
			}
			else if(musicPlaying == MusicPlaying.MUSIC_COMBAT){
				msCombat[curMsCombatIndex].setVolume(curVolume);
			}
		}
	}
	
	
}
