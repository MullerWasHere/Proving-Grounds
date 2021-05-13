package br.com.provinggrounds.game.state;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;

import br.com.provinggrounds.game.game.MainGame;

public class MainMenuState extends BasicGameState {

	@Override
	public void init(GameContainer container, StateBasedGame game)
			throws SlickException {
	}

	@Override
	public void render(GameContainer container, StateBasedGame game, Graphics g)
			throws SlickException {
		// render stuff
		g.drawString("Menu Principal", 0, 0);
	}

	@Override
	public void update(GameContainer container, StateBasedGame game, int delta)
			throws SlickException {
		Input input = container.getInput();
		if (input.isKeyPressed(Input.KEY_SPACE)) {
			game.enterState(MainGame.STATE_GAME);
		}
	}

	@Override
	public int getID() {
		return MainGame.STATE_MAINMENU;
	}

}
