package br.com.provinggrounds.game.entity;

import java.util.HashMap;

import org.newdawn.slick.Color;

import br.com.provinggrounds.game.game.MainGame;

public class Body {
	private Color color;
	private Color outlineColor;
	private int outlineWidth;
	private int roundnessFactor;
	
	private static float saturation = .25f + MainGame.RANDOM.nextInt(50) * .01f;
	private static float brightness = .50f + MainGame.RANDOM.nextInt(25) * .01f;
	private static float outlineBrightness = brightness * .5f;
	
	private static final HashMap<Class<?>, Body> mutableBodies = new HashMap<>();
	
	public static void redefineColors()
	{
		for(Body b : mutableBodies.values()) {
			b.color = new Color(java.awt.Color.HSBtoRGB(
					MainGame.RANDOM.nextFloat(),
					saturation,
					brightness));
		}
	}
	
	public static void registerClassBody(Class<?> entityClass, Roundness shape, Outline outline)
	{
		mutableBodies.put(entityClass, new Body(
				MainGame.RANDOM.nextFloat(),	//hue
				shape, outline));
	}
	
	private Body(float hue, Roundness shape, Outline outline) {
		this.color = new Color(java.awt.Color.HSBtoRGB(hue,	 saturation, brightness));
		this.roundnessFactor = shape.roundnessFactor;
		this.outlineWidth = outline.outlineWidth;
		outlineColor = new Color(java.awt.Color.HSBtoRGB(hue, saturation, outlineBrightness));
	}
	
	public static Body getClassBody(Class<?> entityClass) {
		return mutableBodies.get(entityClass);
	}
	public Color getColor() {
		return color;
	}

	public Color getOutlineColor() {
		return outlineColor;
	}

	public int getOutlineWidth() {
		return outlineWidth;
	}

	public int getRoundnessFactor() {
		return roundnessFactor;
	}
	
	public boolean isOutlined()
	{
		return outlineWidth > 0;
	}

	public enum Roundness {
		NONE(0), MEDIUM(4), MAXIMUM(16);
		
		public final int roundnessFactor;
		
		private Roundness(int factor) {
			this.roundnessFactor = factor;
		}
	};
	
	public enum Outline {
		MINIMUM(2), MEDIUM(4), MAXIMUM(8);
		
		public final int outlineWidth;
		
		private Outline(int width)
		{
			this.outlineWidth = width;
		}
	}
}
