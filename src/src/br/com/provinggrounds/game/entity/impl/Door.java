package br.com.provinggrounds.game.entity.impl;

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
	
	static {
		Body.registerClassBody(Door.class, Roundness.NONE, Outline.MINIMUM);
	}
	
	public Door(int x, int y, Dungeon.Direction doorDirection, int goToRoomId) {
		super(x, y, DOOR_WIDTH, DOOR_HEIGHT, "", Type.WALL);
		collidable = true;
		canPassThrough = true;
		this.goToRoomId = goToRoomId;
		this.doorDirection = doorDirection;
		this.isVisible = false;
		this.showTooltip = false;
	}

	@Override
	public void update(GameContainer c, int delta, Room room) {
		super.update(c, delta, room);
	}

	@Override
	public void collideAndCallback(Entity other, Room room, int delta) {
		if(other.getType() == Type.PLAYER && other.getRectangle().intersects(getRectangle())){

			Dungeon.changeRoom(doorDirection, goToRoomId, room);
			Room roomtg = Dungeon.getRoomAt(doorDirection, room);
			if(doorDirection == Dungeon.Direction.ABOVE && !roomtg.hasBottomDoor()){
				System.out.println("replicado");
			}
			if(doorDirection == Dungeon.Direction.DOWN && !roomtg.hasTopDoor()){
				System.out.println("replicado");
			}
			if(doorDirection == Dungeon.Direction.LEFT && !roomtg.hasRightDoor()){
				System.out.println("replicado");
			}
			if(doorDirection == Dungeon.Direction.RIGHT && !roomtg.hasLeftDoor()){
				System.out.println("replicado");
			}
			//room.changeRoom(doorDirection, goToRoomId);
			//room.removeProjectiles();
			//System.out.println("player enters the door to room: " + goToRoomId);
		}
	}
	
	public static int DOOR_WIDTH = 48;
	public static int DOOR_HEIGHT = 48;
}
