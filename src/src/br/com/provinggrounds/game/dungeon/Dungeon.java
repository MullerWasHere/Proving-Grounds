package br.com.provinggrounds.game.dungeon;

import java.util.ArrayList;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.StateBasedGame;

import br.com.provinggrounds.game.dungeon.Room.Type;
import br.com.provinggrounds.game.entity.HUD;
import br.com.provinggrounds.game.entity.MouseManager;
import br.com.provinggrounds.game.entity.Notification;
import br.com.provinggrounds.game.entity.impl.Player;
import br.com.provinggrounds.game.entity.impl.Wall;
import br.com.provinggrounds.game.game.Audio;
import br.com.provinggrounds.game.game.Fonts;

public class Dungeon {

	private static Player player = new Player();
	private static HUD hud = new HUD();
	private static Room[][] curlevelgrid;
	private static ArrayList<Room[][]> dungeonlevels = new ArrayList<Room[][]>();
	
	public static final int GRID_WIDTH = 9;
	public static final int GRID_HEIGHT = 9;
	
	private static Room curRoom = null;
	private static int curLevel = -1;

	public Dungeon()
	{
		Audio.audio.initialize();
		Fonts.initialize();
		generateLevelGrid();
		addPlayerToGrid();
	}

	public void update(GameContainer container, StateBasedGame game, int delta) {
		if (player.getHp() == 0) {
			Input input = container.getInput();
			if(input.isKeyPressed(Input.KEY_R)){
				restartGame();
			}
		} else {
			MouseManager.mouse.update(container, delta, null);
			player.event(container, delta, getCurrentRoom());
			getCurrentRoom().update(container, delta);
			hud.update(container, delta);
			// System.out.println(delta);
		}

	}

	public void render(GameContainer container, StateBasedGame game, Graphics g)
			throws SlickException {
		if(player.getHp() == 0)
			Fonts.levelChangeFont.drawString(200, 200, "GAME OVER!");
		else{
			getCurrentRoom().render(g);
			hud.render(g);			
		}

		
		//player.render(g);
		//g.drawString(String.valueOf(roomPoint.y), 250, 250);
	}
	
	public static Room getCurrentRoom()
	{
		//System.out.println(levelgrid[roomPoint.x % Dungeon.GRID_WIDTH][roomPoint.y / Dungeon.GRID_HEIGHT].getRoomId());
		//return levelgrid[Dungeon.roomPoint.x % Dungeon.GRID_WIDTH][Dungeon.roomPoint.y / Dungeon.GRID_HEIGHT];
		return curRoom;
	}
	
	public static int getCurrentLevel(){
		return curLevel;
	}
	
	public static Player getPlayer(){
		return player;
	}
	
	public static void generateLevelGrid(){
		int level = getCurrentLevel() + 1;
		Room[][] newlevelgrid = new Room[Dungeon.GRID_WIDTH][Dungeon.GRID_HEIGHT];
		curlevelgrid = newlevelgrid;
		//levelgrid = new Room[Dungeon.GRID_WIDTH][Dungeon.GRID_HEIGHT];
		for(int x = 0; x < Dungeon.GRID_WIDTH; x++){
			for(int y = 0;y < Dungeon.GRID_HEIGHT; y++){
				newlevelgrid[x][y] = new Room(x + Dungeon.GRID_WIDTH * y);
			}
		}
		
		for(int x = 0; x < Dungeon.GRID_WIDTH; x++){
			for(int y = 0;y < Dungeon.GRID_HEIGHT; y++){
				newlevelgrid[x][y].decideWalls();
			}
		}
		
		for(int x = 0; x < Dungeon.GRID_WIDTH; x++){
			for(int y = 0;y < Dungeon.GRID_HEIGHT; y++){
				newlevelgrid[x][y].generateRoom();
			}
		}
		
		for(int x = 0; x < Dungeon.GRID_WIDTH; x++){
			for(int y = 0;y < Dungeon.GRID_HEIGHT; y++){
				newlevelgrid[x][y].generateExtras(level);
			}
		}
				
		dungeonlevels.add(newlevelgrid);
	}
	
	public void restartGame(){
		
		dungeonlevels.clear();
		curLevel = -1;
		curRoom = null;
		player = new Player();
		generateLevelGrid();
		addPlayerToGrid();
	}

	private void addPlayerToGrid() {
		curRoom = null;
		curLevel++;
		//curlevelgrid = dungeonlevels.get(0);
		for (int x = 0; x < Dungeon.GRID_WIDTH && curRoom == null; x++) {
			for (int y = 0; y < Dungeon.GRID_HEIGHT && curRoom == null; y++) {
				if (curlevelgrid[x][y].getRoomType() == Type.EMPTYROOM) {
					curRoom = curlevelgrid[x][y];
				}
			}
		}
	}
	
	public static void changeRoom(Direction doorDirection, int roomId, Room room){
		room.removeNotifications();
		Dungeon.curRoom = curlevelgrid[roomId%Dungeon.GRID_WIDTH][roomId/Dungeon.GRID_HEIGHT];
		
		if(doorDirection == Direction.ABOVE){
			player.setY(Room.ROOM_SIZE_HEIGHT-Wall.WALL_HEIGHT-5);
		}else if(doorDirection == Direction.DOWN){
			//player.setX(GameRun.WINDOW_WIDTH/2 - player.getRectangle().getWidth() /2);
			//player.setY(0 + player.getRectangle().getHeight());
			player.setY(10);
		}else if(doorDirection == Direction.LEFT){
			player.setX(Room.ROOM_SIZE_WIDTH-Wall.WALL_WIDTH-5);
			//player.setY((GameRun.WINDOW_HEIGHT/2) - player.getRectangle().getHeight()/2);
		}else{
			player.setX(5);
			//player.setY((GameRun.WINDOW_HEIGHT/2) - player.getRectangle().getHeight()/2);
		}
	}
	
	public static void changeLevel(int curRoomId, int amount){
		if(curLevel + amount < 0)
			return;
		if(curLevel + amount > dungeonlevels.size()-1){
			generateLevelGrid();
			curlevelgrid = dungeonlevels.get(dungeonlevels.size()-1);
			boolean quit = false;
			for(int x = 0; x < GRID_WIDTH && quit == false; x++){
				for(int y = 0; y < GRID_HEIGHT && quit == false; y++){
					if(curlevelgrid[x][y].getRoomId() == curRoomId){
						curRoom.removeNotifications();
						curRoom = curlevelgrid[x][y];
						curRoom.addNotification("Voce desce mais um nivel... +25 gold!", 200, 200, 0,Fonts.levelChangeFont, Color.white, Notification.TIME_LONG);
						Audio.audio.playFxPowerUp();
						player.addGold(25);
						quit = true;
					}
				}
			}
		}
		curLevel += amount;
	}
	
	public static boolean hasRoomAt(Direction direction, Room room){
		int roomX, roomY;
		roomX = room.getRoomId()%Dungeon.GRID_WIDTH;
		roomY = room.getRoomId()/Dungeon.GRID_HEIGHT;
		//System.out.println(roomX + "-" + roomY );
		if(direction == Direction.ABOVE &&
				roomY== 0)
			return false;
		if(direction == Direction.DOWN &&
				roomY == Dungeon.GRID_HEIGHT - 1)
			return false;
		if(direction == Direction.LEFT &&
				roomX == 0)
			return false;
		if(direction == Direction.RIGHT &&
				roomX == Dungeon.GRID_WIDTH - 1)
			return false;
		return true;
	}
	
	
	
	public static int getRoomIdAt(Direction direction, Room curRoom){
		int roomX, roomY;
		roomX = curRoom.getRoomId()%Dungeon.GRID_WIDTH;
		roomY = curRoom.getRoomId()/Dungeon.GRID_HEIGHT;
		if(direction == Direction.ABOVE){
			roomY--;
		}
		else if(direction == Direction.DOWN){
			roomY++;
		}
		else if(direction == Direction.LEFT){
			roomX--;
		}else{
			roomX++;
		}
		
		return roomX + Dungeon.GRID_HEIGHT * roomY;
	}
	
	public static Room getRoomAt(Direction direction, Room lolroom){
		if(!hasRoomAt(direction, lolroom))
			return null;
		int roomX, roomY;
		roomX = lolroom.getRoomId()%Dungeon.GRID_WIDTH;
		roomY = lolroom.getRoomId()/Dungeon.GRID_HEIGHT;
		if(direction == Direction.ABOVE){
			roomY--;
		}
		else if(direction == Direction.DOWN){
			roomY++;
		}
		else if(direction == Direction.LEFT){
			roomX--;
		}else{
			roomX++;
		}
		
		return curlevelgrid[roomX][roomY];
	}
	
	
	public enum Direction{
		ABOVE, DOWN, LEFT, RIGHT;
	}
}
