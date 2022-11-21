package br.com.provinggrounds.game.entity;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Input;

import br.com.provinggrounds.game.dungeon.Room;

public class MouseManager extends Entity{

	public static MouseManager mouse = new MouseManager();
	private int oldx, oldy;
	private int mouseTimer;
	public static boolean isMouseGrabbed = false;
	
	private MouseManager() {
		super(0, 0, 1, 1, "", Type.MOUSE);
		showTooltip = false;
		oldx = oldy = 0;
		mouseTimer = 0;
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
		if(mouseTimer>SHOW_TP_TIMER && !c.isMouseGrabbed()){
			c.setMouseGrabbed(true);
		}
		mouseTimer += delta;
		Input input = c.getInput();
		this.rectangle.setX(input.getMouseX());
		this.rectangle.setY(input.getMouseY());
		if(oldx != input.getMouseX()){
			oldx = input.getMouseX();
			mouseTimer = 0;
			c.setMouseGrabbed(false);
		}
		if(oldy != input.getMouseY()){
			oldy = input.getMouseY();
			mouseTimer = 0;
			c.setMouseGrabbed(false);
		}
		
		if(input.isMousePressed(Input.MOUSE_RIGHT_BUTTON)){
			c.setMouseGrabbed(!c.isMouseGrabbed());
		}
		
		isMouseGrabbed = c.isMouseGrabbed();
//		if(input.isMousePressed(Input.MOUSE_LEFT_BUTTON)){
//			double angle = Math.atan2(rectangle.getY() - Dungeon.getPlayer().getRectangle().getY(),
//					rectangle.getX() - Dungeon.getPlayer().getRectangle().getX());
//			Arrow arrow = new Arrow(Dungeon.getPlayer().getRectangle().getX(), Dungeon.getPlayer().getRectangle().getY(), (float)Math.cos(angle), (float)Math.sin(angle));
//			Dungeon.getCurrentRoom().addEntity(arrow);
//		}
	}
	
	private static final int SHOW_TP_TIMER = 1500;

}
