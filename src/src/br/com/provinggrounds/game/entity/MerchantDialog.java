package br.com.provinggrounds.game.entity;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;

import br.com.provinggrounds.game.dungeon.Room;
import br.com.provinggrounds.game.game.Fonts;
import br.com.provinggrounds.game.game.GameRun;

public class MerchantDialog {
	private String texto;
	private boolean resposta;
	
	public MerchantDialog(String texto){
		this.texto = texto;
		this.resposta = false;
	}
	
	public void render(Graphics g){
		g.setColor(new Color(0, 0, 0, .5f));
		g.fillRect(0, 0, GameRun.WINDOW_WIDTH, GameRun.WINDOW_HEIGHT);
		g.setColor(Color.black);
		g.fillRect(0, Room.ROOM_SIZE_HEIGHT-200, Room.ROOM_SIZE_WIDTH, 200);
		Fonts.levelChangeFont.drawString((Room.ROOM_SIZE_WIDTH-Fonts.levelChangeFont.getWidth(texto))/2, Room.ROOM_SIZE_HEIGHT-175, texto);
		Fonts.levelChangeFont.drawString(370, Room.ROOM_SIZE_HEIGHT-Fonts.levelChangeFont.getHeight(texto)-70, "Nao");
		Fonts.levelChangeFont.drawString(Fonts.levelChangeFont.getWidth("Não")+450, Room.ROOM_SIZE_HEIGHT-Fonts.levelChangeFont.getHeight(texto)-70, "Sim");
		g.setColor(Color.white);
		if(resposta == false)
			g.drawRect(360, Room.ROOM_SIZE_HEIGHT-Fonts.levelChangeFont.getHeight(texto)-80,
				Fonts.levelChangeFont.getWidth("Nao")+20, 10 + Fonts.levelChangeFont.getHeight("Nao")+10);
		else
			g.drawRect(Fonts.levelChangeFont.getWidth("Não")+440, Room.ROOM_SIZE_HEIGHT-Fonts.levelChangeFont.getHeight(texto)-80,
					Fonts.levelChangeFont.getWidth("Sim") + 20, 10 + Fonts.levelChangeFont.getHeight("Sim")+10);
	}
	
	public void update(GameContainer c, Room room, int delta){
		Input input = c.getInput();
		if(input.isKeyPressed(Input.KEY_SPACE)){
			room.getOutOfDialog();
		}
		else if(input.isKeyPressed(Input.KEY_LEFT)){
			resposta = false;
		}
		else if(input.isKeyPressed(Input.KEY_RIGHT)){
			resposta = true;
		}
	}
	
	public void changeResposta(boolean resposta){
		this.resposta = resposta;
	}
	
	public boolean getResposta(){
		return resposta;
	}
}
