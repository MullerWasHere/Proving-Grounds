package br.com.provinggrounds.game.entity.impl;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;

import br.com.provinggrounds.game.dungeon.Dungeon;
import br.com.provinggrounds.game.dungeon.Room;
import br.com.provinggrounds.game.entity.Body;
import br.com.provinggrounds.game.entity.Body.Outline;
import br.com.provinggrounds.game.entity.Body.Roundness;
import br.com.provinggrounds.game.entity.Entity;
import br.com.provinggrounds.game.game.MainGame;

public class Hole extends Entity{



	public Hole() {
		super(STAIR_SIZE, Type.STAIRS);
		int cornerx = MainGame.RANDOM.nextInt(2);
		int cornery = MainGame.RANDOM.nextInt(2);
		this.rectangle.setX(Wall.WALL_WIDTH + WALL_DISTANCE + (cornerx * (Room.ROOM_SIZE_WIDTH-WALL_DISTANCE*2-Wall.WALL_WIDTH*2-STAIR_SIZE)));
		this.rectangle.setY(Wall.WALL_HEIGHT + WALL_DISTANCE + (cornery * (Room.ROOM_SIZE_HEIGHT-WALL_DISTANCE*2-Wall.WALL_HEIGHT*2-STAIR_SIZE)));
		body = new Body(Color.black, Roundness.MAXIMUM, Outline.NONE);
		collidable = false;
		showTooltip = true;
		tooltip = new String("Um buraco.. nao caia nele!");
	}

	@Override
	public void collideAndCallback(Entity other, Room room, int delta) {
		if(other.getRectangle().intersects(getRectangle())){
			if(other.getType() == Type.PLAYER){
				System.out.println("Stairs!!");
				Dungeon.changeLevel(room.getRoomId(), 1);
			}
		}
	}

	@Override
	public void update(GameContainer c, int delta, Room room) {
		// TODO Auto-generated method stub
		
	}
	
	private static final int STAIR_SIZE = 48;
	private static final int WALL_DISTANCE = 16;

}
