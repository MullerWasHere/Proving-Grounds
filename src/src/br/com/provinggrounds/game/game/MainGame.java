package br.com.provinggrounds.game.game;

import java.util.Random;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.StateBasedGame;

import br.com.provinggrounds.game.state.GameState;
import br.com.provinggrounds.game.state.MainMenuState;

public class MainGame extends StateBasedGame {

	public MainGame() {
		super("Proving Grounds");
	}

	@Override
	public void initStatesList(GameContainer container) throws SlickException {		
		this.addState(new MainMenuState());
		this.addState(new GameState());
	}
	
	public static final Random RANDOM = new Random();

	public static final int STATE_GAME = 0;
	public static final int STATE_MAINMENU = 1;
	
}
