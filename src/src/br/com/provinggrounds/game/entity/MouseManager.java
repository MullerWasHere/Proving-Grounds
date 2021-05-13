package br.com.provinggrounds.game.entity;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Input;

import br.com.provinggrounds.game.dungeon.Dungeon;
import br.com.provinggrounds.game.dungeon.Room;
import br.com.provinggrounds.game.entity.impl.Arrow;

public class MouseManager extends Entity{

	public static MouseManager mouse = new MouseManager();
	
	private MouseManager() {
		super(0, 0, 1, 1, "", Type.MOUSE);
		showTooltip = false;
	}

	@Override
	public void collideAndCallback(Entity other, Room room, int delta) {
	}
	
	public boolean isMouseOver(Entity ent){
		if(getRectangle().intersects(ent.getRectangle()))
			return true;
		return false;
	}

	@Override
	public void update(GameContainer c, int delta, Room room) {
		Input input = c.getInput();
		this.rectangle.setX(input.getMouseX());
		this.rectangle.setY(input.getMouseY());
//		if(input.isMousePressed(Input.MOUSE_LEFT_BUTTON)){
//			double angle = Math.atan2(rectangle.getY() - Dungeon.getPlayer().getRectangle().getY(),
//					rectangle.getX() - Dungeon.getPlayer().getRectangle().getX());
//			Arrow arrow = new Arrow(Dungeon.getPlayer().getRectangle().getX(), Dungeon.getPlayer().getRectangle().getY(), (float)Math.cos(angle), (float)Math.sin(angle));
//			Dungeon.getCurrentRoom().addEntity(arrow);
//		}
	}

}
