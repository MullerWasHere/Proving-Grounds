package br.com.provinggrounds.game.entity.impl;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.geom.Rectangle;

import br.com.provinggrounds.game.dungeon.Dungeon;
import br.com.provinggrounds.game.dungeon.Room;
import br.com.provinggrounds.game.entity.Body;
import br.com.provinggrounds.game.entity.Body.Outline;
import br.com.provinggrounds.game.entity.Body.Roundness;
import br.com.provinggrounds.game.entity.wpn.BaseWeapon;
import br.com.provinggrounds.game.entity.wpn.CommonGun;
import br.com.provinggrounds.game.entity.Entity;
import br.com.provinggrounds.game.game.Audio;

public class Player extends Entity {
	
	private int gold;
	private int hp;
	private int invulTimer;
	private int maxhp, maxmaxhp;
	private boolean inDialog = false;
	private boolean col = false;
	private BaseWeapon wpn;
	
	static {
		Body.registerClassBody(Player.class, Roundness.MEDIUM, Outline.MEDIUM);
	}

	public Player() {
		super(SIDE_SIZE, Type.PLAYER);
		body = Body.getClassBody(Player.class);
		velocityCap = .3f;
		showTooltip = true;
		tooltip = new String("Você '-'");
		gold = 0;
		hp = maxhp = 3;
		maxmaxhp = 8;
		removesProjectile = false;
		wpn = new CommonGun();
	}

	@Override
	public void collideAndCallback(Entity other, Room room, int delta) {
		
		//APOS ISSO CHECK COM PAREDE
		if (!other.isCollidable() || col) //TESTE
			return;
		
		if(other.canPass())
			return;
		
		float displacementX = (getVelocityCap() * getVelX()) * delta;
		float displacementY = (getVelocityCap() * getVelY()) * delta;
		Rectangle newrect = new Rectangle(getRectangle().getX(), getRectangle()
				.getY(), getRectangle().getWidth(), getRectangle().getHeight());
		Rectangle entrect = new Rectangle(other.getRectangle().getX(), other
				.getRectangle().getY(), other.getRectangle().getWidth(), other
				.getRectangle().getHeight());
		newrect.setX(newrect.getX() + displacementX);
		newrect.setY(newrect.getY());

		if (newrect.intersects(entrect)) {
			setVelX(0);
			if (newrect.getX() > entrect.getX()) {

				getRectangle().setX(entrect.getMaxX() + 1);
			}
		}

		newrect = new Rectangle(getRectangle().getX(), getRectangle().getY(),
				getRectangle().getWidth(), getRectangle().getHeight());
		entrect = new Rectangle(other.getRectangle().getX(), other
				.getRectangle().getY(), other.getRectangle().getWidth(), other
				.getRectangle().getHeight());
		newrect.setX(newrect.getX());
		newrect.setY(newrect.getY() + displacementY);
		if (newrect.intersects(entrect)) {
			setVelY(0);
			if (newrect.getY() > entrect.getY()) {
				getRectangle().setY(entrect.getMaxY() + 1);
			}
		}
	}

	@Override
	public void event(GameContainer c, int delta, Room room) {
		Input input = c.getInput();
		setVelY(0);
		setVelX(0);
		
		if (!inDialog) {
			if (input.isKeyDown(Input.KEY_W)) {
				setVelY(-1);
				setDirection(Direction.UP);
			} else if (input.isKeyDown(Input.KEY_S)) {
				setDirection(Direction.DOWN);
				setVelY(1);
			}

			if (input.isKeyDown(Input.KEY_D)) {
				setDirection(Direction.RIGHT);
				setVelX(1);
			} else if (input.isKeyDown(Input.KEY_A)) {
				setDirection(Direction.LEFT);
				setVelX(-1);
			}
			
			if(input.isKeyPressed(Input.KEY_P)){
				Dungeon.togglePause();
			}
			
			if(input.isKeyPressed(Input.KEY_M)){
				Audio.audio.playMsCalm();
			}
			
			if(input.isKeyPressed(Input.KEY_SPACE) && room.getRoomType() == Room.Type.TUTORIAL1)
				Dungeon.placePlayerAt(Dungeon.getRoom(4, 2));
			
			if(input.isKeyPressed(Input.KEY_O))
				col = !col;

			//Bullet b = null;
			if (input.isKeyDown(Input.KEY_UP)) {
				// room.addEntity(new Bullet(Direction.UP, getRectangle()
				// .getX() + getRectangle().getWidth() / 2,
				// getRectangle().getY() - BULLET_DISTANCE_PLAYER));
				wpn.tryShoot(getRectangle().getX() + getRectangle().getWidth()/2,
						getRectangle().getY() - BULLET_DISTANCE_PLAYER, 0, -1, room);
			} else if (input.isKeyDown(Input.KEY_DOWN)) {
				// room.addEntity(new Bullet(Direction.DOWN, getRectangle()
				// .getX() + getRectangle().getWidth() / 2,
				// getRectangle().getY() + getRectangle().getHeight()
				// + BULLET_DISTANCE_PLAYER));
				wpn.tryShoot(getRectangle().getX() + getRectangle().getWidth()/2,
						getRectangle().getY() +getRectangle().getHeight() + BULLET_DISTANCE_PLAYER,
						0, 1, room);
			} else if (input.isKeyDown(Input.KEY_LEFT)) {
				// room.addEntity(new Bullet(Direction.LEFT, getRectangle()
				// .getX() - BULLET_DISTANCE_PLAYER, getRectangle()
				// .getY() + rectangle.getHeight() / 2));
				wpn.tryShoot(getRectangle().getX() - BULLET_DISTANCE_PLAYER,
						getRectangle().getY() + rectangle.getHeight() / 2, -1, 0, room);
			} else if (input.isKeyDown(Input.KEY_RIGHT)) {
				// room.addEntity(new Bullet(Direction.RIGHT, getRectangle()
				// .getX()
				// + getRectangle().getWidth()
				// + BULLET_DISTANCE_PLAYER, getRectangle().getY()
				// + getRectangle().getHeight() / 2));
				wpn.tryShoot(getRectangle().getX() + getRectangle().getWidth() + BULLET_DISTANCE_PLAYER,
						getRectangle().getY() + getRectangle().getHeight()/2, 1, 0, room);
			}
		}
	}

	@Override
	public void update(GameContainer c, int delta, Room room) {
		if(invulTimer > 0)
			invulTimer-=delta;
		float displacementX = (getVelocityCap() * getVelX()) * delta;
		float displacementY = (getVelocityCap() * getVelY()) * delta;

		rectangle.setX(rectangle.getX() + displacementX);
		rectangle.setY(rectangle.getY() + displacementY);
		wpn.updateTimer(delta);
	}
	
	@Override
	public void render(Graphics g) {
		if(invulTimer > 0 && invulTimer % 100 == 0)
			super.render(g);
		if(invulTimer <= 0)
			super.render(g);
	}
	
	public void setInDialog(boolean inDialog){
		this.inDialog = inDialog;
	}
	
	public void hitByEnemy(){
		if(invulTimer <= 0){
			hp--;
			if(hp == 0){
				Audio.audio.playFxGameOver();
			}
			invulTimer = 900;
			Audio.audio.playFxHit();
			Audio.audio.stopMusic();
		}
	}
	
	public int getHp(){
		return hp;
	}
	
	public void setHp(int hp){
		this.hp = hp;
	}
	
	public int getMaxHp(){
		return maxhp;
	}
	
	public void incMaxHp(int amount){
		if(maxhp + amount < maxmaxhp){
			maxhp += amount;
			hp += amount;
		}

	}
	
	public void incHp(int hp){
		if(this.hp + hp <= maxhp)
			this.hp += hp;
	}
	
	public void setAtkDmg(float atkdmg){
		wpn.setAD(atkdmg);
	}
	
	public float getAtkDmg(){
		return wpn.getAD();
	}
	
	public void setWeapon(BaseWeapon wpn){
		this.wpn = wpn;
	}
	
	public void setAtkSpdCooldown(int atkspdCooldown){
		this.wpn.setGunCooldown(atkspdCooldown);
	}
	
	public int getAtkSpdCoolDown(){
		return this.wpn.getGunCooldown();
	}
	
	public void addGold(int amount){
		this.gold+=amount;
	}

	public int getGold() {
		return gold;
	}

	private static int SIDE_SIZE = 32;
	private static int BULLET_DISTANCE_PLAYER = 15;
	
}
