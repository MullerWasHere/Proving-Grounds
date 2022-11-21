package br.com.provinggrounds.game.dungeon;

import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.List;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.UnicodeFont;

import br.com.provinggrounds.game.entity.Body;
import br.com.provinggrounds.game.entity.Body.Outline;
import br.com.provinggrounds.game.entity.Body.Roundness;
import br.com.provinggrounds.game.entity.Entity;
import br.com.provinggrounds.game.entity.MerchantDialog;
import br.com.provinggrounds.game.entity.Notification;
import br.com.provinggrounds.game.entity.Notification.Effect;
import br.com.provinggrounds.game.entity.Notification.Time;
import br.com.provinggrounds.game.entity.impl.Chest;
import br.com.provinggrounds.game.entity.impl.Door;
import br.com.provinggrounds.game.entity.impl.Hole;
import br.com.provinggrounds.game.entity.impl.MeleeEnemy;
import br.com.provinggrounds.game.entity.impl.Merchant;
import br.com.provinggrounds.game.entity.impl.RangedEnemy;
import br.com.provinggrounds.game.entity.impl.Wall;
import br.com.provinggrounds.game.game.Audio;
import br.com.provinggrounds.game.game.Audio.MusicPlaying;
import br.com.provinggrounds.game.game.Fonts;
import br.com.provinggrounds.game.game.MainGame;


public class Room {
	
	static {
		Body.registerClassBody(Room.class, Roundness.MEDIUM, Outline.MINIMUM);
	}
	
	protected int id;
	protected Type type;
	private Body body = Body.getClassBody(Room.class);
	private int openDirection;
	
	//LISTAS
	private List<Entity> entities = new ArrayList<Entity>();
	private List<Entity> toadd = new ArrayList<Entity>();
	ArrayList<Entity> toRemove = new ArrayList<Entity>();
	
	private List<Notification> notifications = new ArrayList<Notification>();
	
	private MerchantDialog dialog = null;
	
	private int enemyCount = 0;
	
	public Room(int id, Type type){
		this.id = id;
		this.type = type;
		decideWalls();
		generateRoom();
		generateExtras(Dungeon.getCurrentLevel());
		checkbugs();
	}
	
	public Room(int id) {
		this(id, Type.generateRoomType());
	}
	
	public void checkbugs(){
//		if(Dungeon.hasRoomAt(Dungeon.Direction.ABOVE, this)){
//			Room room = Dungeon.getRoomAt(Dungeon.Direction.ABOVE, this);
//			if(room.hasTopDoor())
//		}
	}
	
	public void update(GameContainer container, int delta) {
		
		cleanUpEntities();
		cleanUpNotifications();
		
		if (!toadd.isEmpty()) {

			for (Entity ent : toadd) {
				entities.add(ent);
			}
			toadd.clear();
		}
		
		for (Entity enta : entities) {
			if(enta.shouldRemove()){
				continue;
			}
				
			for (Entity entb : entities) {
				if (enta.equals(entb) || entb.shouldRemove())
					continue;
				enta.collideAndCallback(entb, this, delta);
			}
			enta.update(container, delta, this);
		}

		for (Notification not : notifications) {
			not.update(container, delta, this);
		}
		
		if(dialog != null)
			dialog.update(container, this, delta);
		

	}
	
	private void cleanUpEntities(){
		toRemove.clear();
		for(Entity entity : entities){
			if(entity.shouldRemove())
				toRemove.add(entity);
		}
		
		for(Entity entity : toRemove){
			entities.remove(entity);
		}
		toRemove.clear();
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
		g.setColor(body.getColor());
		g.fillRect(0, 0, ROOM_SIZE_WIDTH, ROOM_SIZE_HEIGHT);
		for(Entity entity : entities)
			entity.render(g);
		for(Notification not : notifications)
			not.render(g);
		
		if(dialog != null)
			dialog.render(g);
		g.setColor(Color.white);
	}
	
	

	public void decideWalls() { //REFAZER
		if(type==Type.TUTORIAL1){
			openDirection |= OpenDirection.RIGHT.getDirection();
			return;
		}else if(type == Type.TUTORIAL2){
			openDirection |= OpenDirection.LEFT.getDirection();
			openDirection |= OpenDirection.RIGHT.getDirection();
			return;
		}else if(type == Type.TUTORIAL3){
			openDirection |= OpenDirection.LEFT.getDirection();
			openDirection |= OpenDirection.RIGHT.getDirection();
			return;
		}
		
		openDirection = 0;
		//melhorar
		do{
			if(MainGame.RANDOM.nextInt(10) > 2 && Dungeon.hasRoomAt(Dungeon.Direction.ABOVE, this)){
				openDirection |= OpenDirection.UP.getDirection();
			}
			if(MainGame.RANDOM.nextInt(10) > 2 && Dungeon.hasRoomAt(Dungeon.Direction.LEFT, this)){
				openDirection |= OpenDirection.LEFT.getDirection();
			}
			if(MainGame.RANDOM.nextInt(10) > 2 && Dungeon.hasRoomAt(Dungeon.Direction.DOWN, this)){
				openDirection |= OpenDirection.DOWN.getDirection();
			}
			if(MainGame.RANDOM.nextInt(10) > 2 && Dungeon.hasRoomAt(Dungeon.Direction.RIGHT, this)){
				openDirection |= OpenDirection.RIGHT.getDirection();
			}
		}while(openDirection == 0);
		
		if(Dungeon.hasRoomAt(Dungeon.Direction.ABOVE, this)){
			Room room = Dungeon.getRoomAt(Dungeon.Direction.ABOVE, this);
			if(room != null && room.hasBottomDoor()){
				openDirection |= OpenDirection.UP.getDirection();
			}else if(room != null && !room.hasBottomDoor()){
				openDirection &= ~OpenDirection.UP.getDirection();
			}
		}
		if(Dungeon.hasRoomAt(Dungeon.Direction.DOWN, this)){
			Room room = Dungeon.getRoomAt(Dungeon.Direction.DOWN, this);
			if(room != null && room.hasTopDoor()){
				openDirection |= OpenDirection.DOWN.getDirection();
			}else if(room != null && !room.hasTopDoor()){
				openDirection &= ~OpenDirection.DOWN.getDirection();
			}
		}
		if(Dungeon.hasRoomAt(Dungeon.Direction.LEFT, this)){
			Room room = Dungeon.getRoomAt(Dungeon.Direction.LEFT, this);
			if(room != null && room.hasRightDoor()){
				openDirection |= OpenDirection.LEFT.getDirection();
			}else if(room != null && !room.hasLeftDoor()){
				openDirection &= ~OpenDirection.LEFT.getDirection();
			}
		}
		if(Dungeon.hasRoomAt(Dungeon.Direction.RIGHT, this)){
			Room room = Dungeon.getRoomAt(Dungeon.Direction.RIGHT, this);
			if(room != null && room.hasLeftDoor()){
				openDirection |= OpenDirection.RIGHT.getDirection();
			}else if(room != null && !room.hasRightDoor()){
				openDirection &= ~OpenDirection.RIGHT.getDirection();
			}
		}
		
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
		if(type == Type.TUTORIAL1){
			addNotification("Bem vindo ao Proving Grounds!", 50, 70, Color.white);
			addNotification("Mova-se com as teclas ", 50, 70 + Fonts.levelChangeFont.getLineHeight(), Color.white);
			addNotification("W A S D", 50 + Fonts.levelChangeFont.getWidth("Mova-se com as teclas "),
					70 + Fonts.levelChangeFont.getLineHeight(), Color.blue);
			addNotification("Para continuar com o tutorial", 50, 70 + Fonts.levelChangeFont.getLineHeight() * 2, Color.white);
			addNotification("Siga para a sala a esquerda", 50, 70 + Fonts.levelChangeFont.getLineHeight() * 3, Color.white);
			addNotification("Proxima sala >", 900, 300, Color.white);
			return;
		}else if(type == Type.TUTORIAL2){
			addNotification("Use as setas direcionais", 50, 70, Color.white);
			addNotification("Para atirar", 50, 70 + Fonts.levelChangeFont.getLineHeight(), Color.white);
			return;
		}else if(type == Type.TUTORIAL3){
			addNotification("TODO", 50, 70, Color.white);
			return;
		}
		
		if(type == Type.ENEMYROOM){
			type.setMusicType(MusicPlaying.MUSIC_COMBAT);
			int nEnemies = MainGame.RANDOM.nextInt(3) + 1;
			enemyCount += nEnemies;
			for(int i = 0;i < nEnemies; i++){
				entities.add(new RangedEnemy( MainGame.RANDOM.nextInt(500) + 100,
						MainGame.RANDOM.nextInt(300) + 80));
			}
			nEnemies = MainGame.RANDOM.nextInt(4) + 1;
			enemyCount += nEnemies;
			System.out.println(enemyCount);
			
			for(int i = 0;i < nEnemies; i++){
				entities.add(new MeleeEnemy( MainGame.RANDOM.nextInt(500) + 100,
						MainGame.RANDOM.nextInt(300) + 80));
			}
		}
		if(MainGame.RANDOM.nextInt(20) > 15)
			entities.add(new Hole());
		if(type == Type.EMPTYROOM && MainGame.RANDOM.nextInt(10) > 8){
			entities.add(new Merchant(150, 150));
		}
	}
	
	public void addNotification(String text, float x, float y, Time delay,  UnicodeFont font, Color color, Time duration, Effect effect){
		notifications.add(new Notification(text, x, y, delay, font, color, duration, effect));
	}
	
	public void addNotification(String text, float x, float y, Color color){
		addNotification(text, x, y, Time.NONE, Fonts.levelChangeFont, color, Time.LONG, Effect.STATIC);
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
			if(notification.getEffect() != Effect.STATIC)
				toRemove.add(notification);
		}
		
		for(Notification notification : toRemove){
			notifications.remove(notification);
		}	
	}
	
	
//	public void setDoor(int doorPos){
//		openDirection |= doorPos;
//	}
	
	public int getRoomId() {
		return id;
	}
	
	public boolean hasTopDoor(){
		return (openDirection & OpenDirection.UP.getDirection()) == OpenDirection.UP.getDirection();
	}
	
	public boolean hasBottomDoor(){
		return (openDirection & OpenDirection.DOWN.getDirection()) == OpenDirection.DOWN.getDirection();
	}
	
	public boolean hasLeftDoor(){
		return (openDirection & OpenDirection.LEFT.getDirection()) == OpenDirection.LEFT.getDirection();
	}
	
	public boolean hasRightDoor(){
		return (openDirection & OpenDirection.RIGHT.getDirection()) == OpenDirection.RIGHT.getDirection();
	}

	public Type getRoomType() {
		return type;
	}
	
	public void decEnemyCount(){
		this.enemyCount--;
		System.out.println(this.enemyCount);
		if(enemyCount == 0){
		type.setMusicType(MusicPlaying.MUSIC_CALM);
			if(MainGame.RANDOM.nextInt(20) > 13){
				addEntity(new Chest(250, 250));
			}
		}
	}

	public void addEntity(Entity ent) {
		this.toadd.add(ent);
 	}
	
	public Rectangle getRectangle()
	{
		return new Rectangle(ROOM_SIZE_WIDTH, ROOM_SIZE_HEIGHT);
	}

	public enum Type {
		EMPTYROOM(9, "Sala vazia", Audio.MusicPlaying.MUSIC_CALM), ENEMYROOM(4, "Sala inimigo", Audio.MusicPlaying.MUSIC_COMBAT),
		TUTORIAL1(10,"Tutorial 1", Audio.MusicPlaying.MUSIC_CALM), TUTORIAL2(10,"Tutorial 2", Audio.MusicPlaying.MUSIC_CALM),
		TUTORIAL3(10,"Tutorial 3", Audio.MusicPlaying.MUSIC_CALM);
		
		public int chance;
		private String nome;
		MusicPlaying musicType;
		Type(int chance, String nome,MusicPlaying musicType){
			this.chance = chance;
			this.nome = nome;
			this.musicType = musicType;
		}
		
		public void setMusicType(MusicPlaying musicType){
			this.musicType = musicType;
		}
		
		public String getNome(){
			return this.nome;
		}
		
		public static Type generateRoomType(){
			Type type = EMPTYROOM;
			for(int i = 0;i < Type.values().length; i++){
				if(MainGame.RANDOM.nextInt(10) > Type.values()[i].chance){
					type = Type.values()[i];
					break;
				}
			}
			return type;
		}
		
		public MusicPlaying getMusicType(){
			return musicType;
		}
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
	public static int ROOM_SIZE_WIDTH = 1280;
	public static int ROOM_SIZE_HEIGHT = 640;
}
