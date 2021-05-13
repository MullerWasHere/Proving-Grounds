package br.com.provinggrounds.game.entity;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.geom.Rectangle;

import br.com.provinggrounds.game.dungeon.Room;
import br.com.provinggrounds.game.game.Fonts;

public abstract class Entity {
	public Body getBody() {
		return body;
	}
	
	private boolean removeEntity = false;

	protected Body body;
	protected Rectangle rectangle;
	protected Direction direction;
	protected boolean isVisible;

	protected boolean collidable = true;
	protected boolean showTooltip = false;
	protected float velX, velY;
	protected Type type;
	protected float velocityCap;
	
	protected boolean removesProjectile = true;
	
	
	protected String tooltip;
	protected Entity(float x, float y, float width, float height, String tooltip, Type type)
	{
		isVisible = true;
		this.type = type;
		this.rectangle = new Rectangle(
				x, y,
				width, height);
		this.tooltip = new String(tooltip);
	}
	
	protected Entity(float width, float height, Type type)
	{
		this(Room.ROOM_SIZE_WIDTH / 2, Room.ROOM_SIZE_HEIGHT / 2, width, height, "", type);
	}
	
	protected Entity(float size, Type type)
	{
		this(size, size, type);
	}

	public void render(Graphics g) {
		if(!isVisible)
			return;
		int roundFactor = body.getRoundnessFactor();
		
		g.setColor(body.getColor());
		g.fillRoundRect(rectangle.getX(), rectangle.getY(), rectangle.getWidth(), rectangle.getHeight(), roundFactor);
		
		if (body.isOutlined()) {
			g.setLineWidth(body.getOutlineWidth());
			g.setColor(body.getOutlineColor());
			g.drawRoundRect(rectangle.getX(), rectangle.getY(), rectangle.getWidth(), rectangle.getHeight(), roundFactor);
		}
		
		if(MouseManager.mouse.isMouseOver(this) && showTooltip){			
			g.setColor(Color.black);
			int width = Fonts.tooltipFont.getWidth(tooltip);
			int height = Fonts.tooltipFont.getHeight(tooltip);
			g.fillRect(MouseManager.mouse.getRectangle().getX()+5, MouseManager.mouse.getRectangle().getY()+5,
					width + 20,height+10);
			//g.setColor(Color.white);
			//g.drawString(tooltip, MouseManager.mouse.getRectangle().getX()+15, MouseManager.mouse.getRectangle().getY()+10);
			Fonts.tooltipFont.drawString(MouseManager.mouse.getRectangle().getX()+15,
					MouseManager.mouse.getRectangle().getY()+10, tooltip, Color.white);
		}
	}

	public abstract void collideAndCallback(Entity other, Room room, int delta);
	
	public abstract void update(GameContainer c, int delta, Room room);
	

	public void event(GameContainer c, int delta, Room room) {return;}
	
	public Rectangle getRectangle() {
		return rectangle;
	}
	
	public Type getType() {
		return type;
	}
	
	public boolean isMoving() {
		return velX != 0 || velY != 0;
	}
	
	public Direction getDirection() {
		return direction;
	}

	public boolean isCollidable() {
		return collidable;
	}

	public float getVelX() {
		return velX;
	}

	public float getVelY() {
		return velY;
	}

	public void setDirection(Direction direction) {
		this.direction = direction;
	}
	
	public void setX(float x){
		rectangle.setX(x);
	}
	
	public void setY(float y){
		rectangle.setY(y);
	}

	public float getVelocityCap() {
		return velocityCap;
	}
	
	public boolean shouldRemove(){
		return removeEntity;
	}
	
	public boolean shouldRemoveProjectile(){
		return removesProjectile;
	}

	public void setVelX(float velX) {
		this.velX = velX;
	}

	public void setVelY(float velY) {
		this.velY = velY;
	}
	
	public void removeEntity(){
		removeEntity = true;
	}

	public static enum Direction {
		UP, LEFT, DOWN, RIGHT
	};

	public static enum Type {
		PLAYER, DOOR, WALL, BULLET, MELEE_ENEMY, STAIRS, NOTIFICATION, MOUSE, POTION, MERCHANT, ARROW
	};
}
