package br.com.provinggrounds.game.state;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;

import br.com.provinggrounds.game.dungeon.Dungeon;
import br.com.provinggrounds.game.game.Audio;
import br.com.provinggrounds.game.game.Fonts;
import br.com.provinggrounds.game.game.MainGame;

public class GameState extends BasicGameState {
	private Dungeon dungeon;

	@Override
	public void init(GameContainer container, StateBasedGame game)
			throws SlickException {
		dungeon = new Dungeon();
	}

	@Override
	public void render(GameContainer container, StateBasedGame game, Graphics g)
			throws SlickException {
		g.setAntiAlias(true);
		dungeon.render(container, game, g);
	}

	@Override
	public void update(GameContainer container, StateBasedGame game, int delta)
			throws SlickException {
		dungeon.update(container, game, delta);
		Audio.audio.audioUpdate(delta, Dungeon.getCurrentRoom());
	}

	@Override
	public int getID() {
		return MainGame.STATE_GAME;
	}
	
	public static void enterGame(){
		Audio.audio.playMsCalm();
	}

}
