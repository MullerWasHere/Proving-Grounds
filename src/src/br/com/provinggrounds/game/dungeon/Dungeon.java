package br.com.provinggrounds.game.dungeon;

import java.util.ArrayList;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.StateBasedGame;

import br.com.provinggrounds.game.entity.Body;
import br.com.provinggrounds.game.entity.HUD;
import br.com.provinggrounds.game.entity.MouseManager;
import br.com.provinggrounds.game.entity.Notification;
import br.com.provinggrounds.game.entity.Notification.Effect;
import br.com.provinggrounds.game.entity.Notification.Time;
import br.com.provinggrounds.game.entity.impl.Player;
import br.com.provinggrounds.game.entity.impl.Wall;
import br.com.provinggrounds.game.game.Audio;
import br.com.provinggrounds.game.game.Fonts;
import br.com.provinggrounds.game.game.GameRun;
import br.com.provinggrounds.game.game.MainGame;

public class Dungeon {

	private static Player player = new Player();
	private static HUD hud = new HUD();
	private static Room[][] curlevelgrid;
	private static ArrayList<Room[][]> dungeonlevels = new ArrayList<Room[][]>();
	
	public static final int GRID_WIDTH = 9;
	public static final int GRID_HEIGHT = 9;
	
	private static Room curRoom = null;
	private static int curLevel = -1;
	
	private static boolean pause = false;

	public Dungeon()
	{
		Audio.audio.initialize();
		try {
			Fonts.initialize();
		} catch (SlickException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		startNewGame();
	}

	public void update(GameContainer container, StateBasedGame game, int delta) {
		if (player.getHp() == 0) {
			Input input = container.getInput();
			
			if(input.isKeyPressed(Input.KEY_R)){
				startNewGame();
			}else if(input.isKeyPressed(Input.KEY_SPACE)){
				if(player.getGold() >= 1000){
					player.setHp(player.getMaxHp()/2);
					player.addGold(-1000);
				}
			}
		} else {
			MouseManager.mouse.update(container, delta, null);
			player.event(container, delta, getCurrentRoom());
			if(pause)
				return;
			getCurrentRoom().update(container, delta);
		}

	}

	public void render(GameContainer container, StateBasedGame game, Graphics g)
			throws SlickException {
		if(player.getHp() == 0){
			int y = GameRun.WINDOW_HEIGHT/3;
			String gameOver = "Game Over!";
			String returnGame = "Se voce tiver 1000 de gold, aperte 'Espaço' para continuar";
			String restart = "Aperte 'R' para recomeçar";
			
			Fonts.levelChangeFont.drawString(GameRun.WINDOW_WIDTH/2 - Fonts.levelChangeFont.getWidth(gameOver)/2, y, gameOver);
			y += Fonts.levelChangeFont.getLineHeight();
			Fonts.levelChangeFont.drawString(GameRun.WINDOW_WIDTH/2 - Fonts.levelChangeFont.getWidth(restart)/2, y, restart);
			
			Fonts.levelChangeFont.drawString(GameRun.WINDOW_WIDTH/2 - Fonts.levelChangeFont.getWidth(returnGame)/2, GameRun.WINDOW_HEIGHT - Fonts.levelChangeFont.getLineHeight(), returnGame);
		}
		else {
			hud.render(g);			
			getCurrentRoom().render(g);
		}
	}
	
	public static Room getCurrentRoom()
	{
		return curRoom;
	}
	
	public static int getCurrentLevel(){
		return curLevel;
	}
	
	public static Player getPlayer(){
		return player;
	}
	
	public static void generateLevelGrid(int px, int py, Room.Type type){
		Room[][] newlevelgrid = new Room[Dungeon.GRID_WIDTH][Dungeon.GRID_HEIGHT];
		curlevelgrid = newlevelgrid;
		if(px < 0 || py < 0)
		{
			px = MainGame.RANDOM.nextInt(Dungeon.GRID_WIDTH);
			py = MainGame.RANDOM.nextInt(Dungeon.GRID_HEIGHT);
		}

		if(type==null)
			newlevelgrid[px][py] = new Room(px + Dungeon.GRID_WIDTH * py);
		else
			newlevelgrid[px][py] = new Room(px + Dungeon.GRID_WIDTH * py, type);
		
		curRoom = newlevelgrid[px][py];
		//Audio.audio.playMsCalm();
//		curRoom.decideWalls();
//		curRoom.generateRoom();
//		curRoom.generateExtras(level);
		
//		for(int x = 0; x < Dungeon.GRID_WIDTH; x++){
//			for(int y = 0;y < Dungeon.GRID_HEIGHT; y++){
//				newlevelgrid[x][y] = new Room(x + Dungeon.GRID_WIDTH * y);
//			}
//		}

		
//		for(int x = 0; x < Dungeon.GRID_WIDTH; x++){
//			for(int y = 0;y < Dungeon.GRID_HEIGHT; y++){
//				newlevelgrid[x][y].decideWalls();
//			}
//		}
//		
//		for(int x = 0; x < Dungeon.GRID_WIDTH; x++){
//			for(int y = 0;y < Dungeon.GRID_HEIGHT; y++){
//				newlevelgrid[x][y].generateRoom();
//			}
//		}
//		
//		for(int x = 0; x < Dungeon.GRID_WIDTH; x++){
//			for(int y = 0;y < Dungeon.GRID_HEIGHT; y++){
//				newlevelgrid[x][y].generateExtras(level);
//			}
//		}
				
		dungeonlevels.add(newlevelgrid);
	}
	
	public static void generateLevelGrid(int px, int py){
		generateLevelGrid(px, py, null);
	}
	
	public static void addRoomToGrid(int px, int py, Room.Type type){
		curlevelgrid[px][py] = new Room(px + Dungeon.GRID_WIDTH * py, type);
	}
	
	public void startNewGame(){
		
		dungeonlevels.clear();
		curLevel = 0;
		curRoom = null;
		player = new Player();
		generateLevelGrid(2,2,Room.Type.TUTORIAL1);
		addRoomToGrid(3,2,Room.Type.TUTORIAL2);
		addRoomToGrid(4,2,Room.Type.TUTORIAL3);
	}

//	private void addPlayerToGrid() {
//		curRoom = null;
//		curLevel++;
//		//curlevelgrid = dungeonlevels.get(0);
//		for (int x = 0; x < Dungeon.GRID_WIDTH && curRoom == null; x++) {
//			for (int y = 0; y < Dungeon.GRID_HEIGHT && curRoom == null; y++) {
//				if (curlevelgrid[x][y].getRoomType() == Type.EMPTYROOM) {
//					curRoom = curlevelgrid[x][y];
//				}
//			}
//		}
//	}
	
	public static void changeRoom(Direction doorDirection, int roomId, Room room){
		room.removeNotifications();
		Body.redefineColors();
		Dungeon.curRoom = curlevelgrid[roomId%Dungeon.GRID_WIDTH][roomId/Dungeon.GRID_HEIGHT];
		int x,y;
		x = room.getRoomId()%Dungeon.GRID_WIDTH;
		y = room.getRoomId()/Dungeon.GRID_HEIGHT;
		
		if(doorDirection == Direction.ABOVE){
			player.setY(Room.ROOM_SIZE_HEIGHT-Wall.WALL_HEIGHT-5);
			y--;
		}else if(doorDirection == Direction.DOWN){
			player.setY(10);
			y++;
		}else if(doorDirection == Direction.LEFT){
			player.setX(Room.ROOM_SIZE_WIDTH-Wall.WALL_WIDTH-5);
			x--;
		}else{
			player.setX(5);
			x++;
		}
		if(curRoom == null){
			curlevelgrid[x][y] = new Room(x + Dungeon.GRID_WIDTH * y);
			curRoom = curlevelgrid[x][y];
		}
	}
	
	public static void placePlayerAt(Room room){
		//TERMINAR FUNÇÃO
		curRoom = room;
	}
	
	public static Room getRoom(int x, int y){
		return curlevelgrid[x][y];
	}
	
	public static void changeLevel(int curRoomId, int amount){
		if(curLevel + amount < 0)
			return;
		if(curLevel + amount > dungeonlevels.size()-1){
//			boolean quit = false;
			curRoom.removeNotifications();
			generateLevelGrid(curRoomId%Dungeon.GRID_WIDTH,curRoomId/Dungeon.GRID_HEIGHT);
			curlevelgrid = dungeonlevels.get(dungeonlevels.size()-1);
			//curRoom = curlevelgrid[curRoomId%Dungeon.GRID_WIDTH][curRoomId/Dungeon.GRID_HEIGHT];
			curRoom.addNotification("Voce desce mais um nivel... +25 gold!", 200, 200, Time.NONE ,Fonts.levelChangeFont, Color.white, Notification.Time.LONG,Effect.UP);
			Audio.audio.playFxPowerUp();
			player.addGold(25);
//			for(int x = 0; x < GRID_WIDTH && quit == false; x++){
//				for(int y = 0; y < GRID_HEIGHT && quit == false; y++){
//					if(curlevelgrid[x][y].getRoomId() == curRoomId){
//						curRoom.removeNotifications();
//						curRoom = curlevelgrid[x][y];
//						curRoom.addNotification("Voce desce mais um nivel... +25 gold!", 200, 200, 0,Fonts.levelChangeFont, Color.white, Notification.TIME_LONG);
//						Audio.audio.playFxPowerUp();
//						player.addGold(25);
//						quit = true;
//					}
//				}
//			}
		}
		curLevel += amount;
	}
	
	public static boolean hasRoomAt(Direction direction, Room room){
		int roomX, roomY;
		roomX = room.getRoomId()%Dungeon.GRID_WIDTH;
		roomY = room.getRoomId()/Dungeon.GRID_HEIGHT;
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
	
	public static void togglePause(){
		pause = !pause;
	}
	
	
	public enum Direction{
		ABOVE, DOWN, LEFT, RIGHT;
	}
}
