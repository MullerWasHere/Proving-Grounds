package br.com.provinggrounds.game.dungeon;

import java.awt.Rectangle;
import java.util.ArrayList;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.TrueTypeFont;


import br.com.provinggrounds.game.entity.MerchantDialog;
import br.com.provinggrounds.game.entity.Entity;
import br.com.provinggrounds.game.entity.Notification;
import br.com.provinggrounds.game.entity.impl.Door;
import br.com.provinggrounds.game.entity.impl.Hole;
import br.com.provinggrounds.game.entity.impl.MeleeEnemy;
import br.com.provinggrounds.game.entity.impl.Merchant;
import br.com.provinggrounds.game.entity.impl.RangedEnemy;
import br.com.provinggrounds.game.entity.impl.Wall;
import br.com.provinggrounds.game.game.MainGame;

public class Room {
	protected int id;
	protected Type type;
	private Color floorColor = new Color(0x617055);
	private int openDirection;
	
	//LISTAS
	private ArrayList<Entity> entities = new ArrayList<Entity>();
	private ArrayList<Entity> toadd = new ArrayList<Entity>();
	
	private ArrayList<Notification> notifications = new ArrayList<Notification>();
	
	private MerchantDialog dialog = null;
	
	
	public Room(int id) {
		this.type = Room.Type.EMPTYROOM;
		this.id = id;
		type = Type.values()[MainGame.RANDOM.nextInt(Type.values().length)];
	}


	public void update(GameContainer container, int delta) {
		cleanUpEntities();
		cleanUpNotifications();
		
		for (Entity enta : entities) {
			for (Entity entb : entities) {
				if (enta.equals(entb))
					continue;
				enta.collideAndCallback(entb, this, delta);
			}
		}

		for (Entity ent : entities) {
			ent.update(container, delta, this);
		}
		
		for(Notification not : notifications){
			not.update(container, delta, this);
		}
		
		if(dialog != null)
			dialog.update(container, this, delta);
		
		for(Entity ent : toadd){
			entities.add(ent);
		}
		toadd.clear();
		
//		if(changingRoom){
//			ArrayList<Entity> clearList = new ArrayList<Entity>();
//			for(Entity entity : entities){
//				clearList.add(entity);
//			}
//			for(Entity entity : clearList){
//				entities.remove(entity);
//			}
//		}
	}
	
	private void cleanUpEntities(){
		ArrayList<Entity> toRemove = new ArrayList<Entity>();
		for(Entity entity : entities){
			if(entity.shouldRemove())
				toRemove.add(entity);
		}
		
		for(Entity entity : toRemove){
			entities.remove(entity);
		}
	}
	
	private void cleanUpNotifications(){
		ArrayList<Notification> toRemove = new ArrayList<Notification>();
		for(Notification notification : notifications){
			if(notification.shouldRemove())
				toRemove.add(notification);
		}
		
		for(Notification notification : toRemove){
			notifications.remove(notification);
		}	
	}
	
	public void render(Graphics g) {
		g.setColor(floorColor);
		g.fillRect(0, 0, ROOM_SIZE_WIDTH, ROOM_SIZE_HEIGHT);
		for(Entity entity : entities)
			entity.render(g);
		for(Notification not : notifications)
			not.render(g);
		
		if(dialog != null)
			dialog.render(g);
		g.setColor(Color.white);
		//g.drawString("Current level: " + String.valueOf(Dungeon.getCurrentLevel()), 120, 10);
	}
	
	

	public void decideWalls() {
		do{
			if(MainGame.RANDOM.nextInt(10) > 4 && Dungeon.hasRoomAt(Dungeon.Direction.ABOVE, this)){
				openDirection |= OpenDirection.UP.getDirection();
				Room room = Dungeon.getRoomAt(Dungeon.Direction.ABOVE, this);
				room.setDoor(OpenDirection.DOWN.getDirection());
			}
			if(MainGame.RANDOM.nextInt(10) > 4 && Dungeon.hasRoomAt(Dungeon.Direction.LEFT, this)){
				openDirection |= OpenDirection.LEFT.getDirection();
				Room room = Dungeon.getRoomAt(Dungeon.Direction.LEFT, this);
				room.setDoor(OpenDirection.RIGHT.getDirection());
			}
			if(MainGame.RANDOM.nextInt(10) > 4 && Dungeon.hasRoomAt(Dungeon.Direction.DOWN, this)){
				openDirection |= OpenDirection.DOWN.getDirection();
				Room room = Dungeon.getRoomAt(Dungeon.Direction.DOWN, this);
				room.setDoor(OpenDirection.UP.getDirection());
			}
			if(MainGame.RANDOM.nextInt(10) > 4 && Dungeon.hasRoomAt(Dungeon.Direction.RIGHT, this)){
				openDirection |= OpenDirection.RIGHT.getDirection();
				Room room = Dungeon.getRoomAt(Dungeon.Direction.RIGHT, this);
				room.setDoor(OpenDirection.LEFT.getDirection());
			}
		}while(openDirection == 0);
	}
	
	
	public void generateRoom(){		
		entities.add(Dungeon.getPlayer());
		
		//top
		if((openDirection & OpenDirection.UP.getDirection()) == OpenDirection.UP.getDirection()){ //TOP DOOR GENERATED
			entities.add(new Wall(0, 0, (ROOM_SIZE_WIDTH / 2)
					- Door.DOOR_WIDTH, Wall.WALL_HEIGHT));
			entities.add(new Door((ROOM_SIZE_WIDTH / 2) - Door.DOOR_WIDTH / 2,
					-Door.DOOR_HEIGHT, Dungeon.Direction.ABOVE,Dungeon.getRoomIdAt(Dungeon.Direction.ABOVE,this)));
			entities.add(new Wall((ROOM_SIZE_WIDTH / 2) + Door.DOOR_WIDTH, 0,
					(ROOM_SIZE_WIDTH)
							- ((ROOM_SIZE_WIDTH / 2) + Door.DOOR_WIDTH),
					Wall.WALL_HEIGHT));
		}else{
			entities.add(new Wall(0, 0, ROOM_SIZE_WIDTH, Wall.WALL_HEIGHT));
		}

		
		//bottom
		if((openDirection & OpenDirection.DOWN.getDirection()) == OpenDirection.DOWN.getDirection()){
			entities.add(new Wall(0, ROOM_SIZE_HEIGHT - Wall.WALL_HEIGHT,
					(ROOM_SIZE_WIDTH / 2) - Door.DOOR_WIDTH, Wall.WALL_HEIGHT));
			entities.add(new Door((ROOM_SIZE_WIDTH / 2) - Door.DOOR_WIDTH/2,
					ROOM_SIZE_HEIGHT, Dungeon.Direction.DOWN, Dungeon.getRoomIdAt(Dungeon.Direction.DOWN, this)));
			entities.add(new Wall((ROOM_SIZE_WIDTH / 2) + Door.DOOR_WIDTH,
					ROOM_SIZE_HEIGHT - Wall.WALL_HEIGHT,
					(ROOM_SIZE_WIDTH)
							- ((ROOM_SIZE_WIDTH / 2) + Door.DOOR_WIDTH),
					Wall.WALL_HEIGHT));
		}else{
			entities.add(new Wall(0, ROOM_SIZE_HEIGHT - Wall.WALL_HEIGHT,ROOM_SIZE_WIDTH, Wall.WALL_HEIGHT));
		}

		
		if((openDirection & OpenDirection.LEFT.getDirection()) == OpenDirection.LEFT.getDirection()){
			//LEFT
			entities.add(new Wall(0, 0, Wall.WALL_WIDTH,
					(ROOM_SIZE_HEIGHT / 2) - Door.DOOR_HEIGHT));
			entities.add(new Door(-Door.DOOR_WIDTH,
					(ROOM_SIZE_HEIGHT / 2) - Door.DOOR_HEIGHT/2,Dungeon.Direction.LEFT ,Dungeon.getRoomIdAt(Dungeon.Direction.LEFT, this)));
			entities.add(new Wall(0,
					(ROOM_SIZE_HEIGHT / 2) + Door.DOOR_HEIGHT,
					Wall.WALL_WIDTH, (ROOM_SIZE_HEIGHT)
							- (ROOM_SIZE_HEIGHT / 2) - Door.DOOR_HEIGHT));
		}
		else{
			entities.add(new Wall(0, 0, Wall.WALL_WIDTH,ROOM_SIZE_HEIGHT));
		}

		
		if((openDirection & OpenDirection.RIGHT.getDirection()) == OpenDirection.RIGHT.getDirection()){
			//RIGHT
			entities.add(new Wall(ROOM_SIZE_WIDTH - Wall.WALL_WIDTH, 0,
					Wall.WALL_WIDTH, (ROOM_SIZE_HEIGHT / 2) - Door.DOOR_HEIGHT));
			entities.add(new Door(ROOM_SIZE_WIDTH,
					ROOM_SIZE_HEIGHT / 2 - Door.DOOR_HEIGHT/2,Dungeon.Direction.RIGHT ,Dungeon.getRoomIdAt(Dungeon.Direction.RIGHT, this)));
			entities.add(new Wall(ROOM_SIZE_WIDTH - Wall.WALL_WIDTH,
					(ROOM_SIZE_HEIGHT / 2) + Door.DOOR_HEIGHT,
					Wall.WALL_WIDTH, (ROOM_SIZE_HEIGHT)
							- (ROOM_SIZE_HEIGHT / 2) - Door.DOOR_HEIGHT));
		}else{
			entities.add(new Wall(ROOM_SIZE_WIDTH - Wall.WALL_WIDTH, 0,Wall.WALL_WIDTH, ROOM_SIZE_HEIGHT) );
		}
		
	}
	
	public void getOutOfDialog(){
		Dungeon.getPlayer().setInDialog(false);
		for(Entity ent : entities){
			if(ent.getType() == Entity.Type.MERCHANT){
				((Merchant)ent).setTimerDialog(1500);
				((Merchant)ent).retornarResposta(dialog.getResposta(), this);
			}
		}
		this.dialog = null;
	}
	
	public void setDialog(MerchantDialog dialog){
		if(this.dialog == null){
			this.dialog = dialog;
			Dungeon.getPlayer().setInDialog(true);
		}
			
	}
	
	public void generateExtras(int dungeonlevel){
		if(type == Type.ENEMYROOM){
			int nEnemies = MainGame.RANDOM.nextInt(3) + 1;
			for(int i = 0;i < nEnemies; i++){
				entities.add(new RangedEnemy( MainGame.RANDOM.nextInt(500) + 100,
						MainGame.RANDOM.nextInt(300) + 80));
			}
			nEnemies = MainGame.RANDOM.nextInt(4) + 1;
			for(int i = 0;i < nEnemies; i++){
				entities.add(new MeleeEnemy( MainGame.RANDOM.nextInt(500) + 100,
						MainGame.RANDOM.nextInt(300) + 80));
			}
		}
		if(MainGame.RANDOM.nextInt(20) > 15 && dungeonlevel < 20)
			entities.add(new Hole());
		if(type == Type.EMPTYROOM && MainGame.RANDOM.nextInt(10) > 8){
			entities.add(new Merchant(150, 150));
		}
	}
	
	public void addNotification(String text, float x, float y, int timetoshow, TrueTypeFont font, Color color, int duration){
		notifications.add(new Notification(text, x, y, timetoshow,font,color, duration));
	}
	
	public void removeProjectiles(){
		for(Entity ent : entities){
			if(ent.getType() == Entity.Type.BULLET)
				ent.removeEntity();
		}
	}
	
	public void removeNotifications(){
		ArrayList<Notification> toRemove = new ArrayList<Notification>();
		for(Notification notification : notifications){
			toRemove.add(notification);
		}
		
		for(Notification notification : toRemove){
			notifications.remove(notification);
		}	
	}
	
	
	public void setDoor(int doorPos){
		openDirection |= doorPos;
		//System.out.println("Setting: " + doorPos);
	}
	
	public int getRoomId() {
		return id;
	}
	
	public boolean hasTopDoor(){
		return (openDirection & OpenDirection.UP.getDirection()) != 0;
	}
	
	public boolean hasBottomDoor(){
		return (openDirection & OpenDirection.DOWN.getDirection()) != 0;
	}
	
	public boolean hasLeftDoor(){
		return (openDirection & OpenDirection.LEFT.getDirection()) != 0;
	}
	
	public boolean hasRightDoor(){
		return (openDirection & OpenDirection.RIGHT.getDirection()) != 0;
	}

	public Type getRoomType() {
		return type;
	}

	public void addEntity(Entity ent) {
		this.toadd.add(ent);
	}
	
	public Rectangle getRectangle()
	{
		return new Rectangle(ROOM_SIZE_WIDTH, ROOM_SIZE_HEIGHT);
	}

	public enum Type {
		EMPTYROOM, ENEMYROOM
	};
	
	public Rectangle getValidSpace(){
		Rectangle rect = new Rectangle(ROOM_SIZE_WIDTH-(2*Wall.WALL_WIDTH)-SAFEDISTANCE, ROOM_SIZE_HEIGHT-(2*Wall.WALL_HEIGHT) - SAFEDISTANCE);
		return rect;
	}
	
	public static int SAFEDISTANCE = 50;
	
	public enum OpenDirection{
		UP(1), LEFT(2), DOWN(4), RIGHT(8);
		
		public final int flag;
		
		private OpenDirection(int flag){
			this.flag = flag;
		}
		
		public int getDirection(){
			return flag;
		}
	}
	public static int ROOM_SIZE_WIDTH = 1024;
	public static int ROOM_SIZE_HEIGHT = 480;
}
