package br.com.provinggrounds.game.entity.impl;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;

import br.com.provinggrounds.game.dungeon.Dungeon;
import br.com.provinggrounds.game.dungeon.Room;
import br.com.provinggrounds.game.entity.Body;
import br.com.provinggrounds.game.entity.Body.Outline;
import br.com.provinggrounds.game.entity.Body.Roundness;
import br.com.provinggrounds.game.entity.Entity;

public class Door extends Entity{
	
	private int goToRoomId;
	private Dungeon.Direction doorDirection;
	
	public Door(int x, int y, Dungeon.Direction doorDirection, int goToRoomId) {
		super(x, y, DOOR_WIDTH, DOOR_HEIGHT, "", Type.WALL);
		body = new Body(Color.red, Roundness.NONE, Outline.NONE);
		collidable = false;
		this.goToRoomId = goToRoomId;
		this.doorDirection = doorDirection;
		this.isVisible = false;
		this.showTooltip = false;
	}

	@Override
	public void update(GameContainer c, int delta, Room room) { }

	@Override
	public void collideAndCallback(Entity other, Room room, int delta) {
		if(other.getType() == Type.PLAYER && other.getRectangle().intersects(getRectangle())){
			Dungeon.changeRoom(doorDirection, goToRoomId, room);
			//room.changeRoom(doorDirection, goToRoomId);
			room.removeProjectiles();
			//System.out.println("player enters the door to room: " + goToRoomId);
		}
	}
	
	public static int DOOR_WIDTH = 48;
	public static int DOOR_HEIGHT = 48;
}
