package br.com.provinggrounds.game.game;

import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.ScalableGame;
import org.newdawn.slick.SlickException;

public class GameRun {
	public static void main(String[] args) {
		try {
			AppGameContainer app = new AppGameContainer(new ScalableGame(new MainGame(), WINDOW_WIDTH, WINDOW_HEIGHT, true));
			app.setDisplayMode(WINDOW_WIDTH, WINDOW_HEIGHT, false);
			app.setResizable(true);
			app.setTargetFrameRate(60);
			//app.setFullscreen(true);
			app.setShowFPS(true);
			app.start();
		} catch (SlickException e) {
			e.printStackTrace();
		}
	}
	public static final int WINDOW_HEIGHT = 576;
	public static final int WINDOW_WIDTH = 1024;
	
}
