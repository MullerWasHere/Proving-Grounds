package br.com.provinggrounds.game.entity;

import org.newdawn.slick.Color;

public class Body {
	private Color color;
	private Color outlineColor;
	private int outlineWidth;
	private int roundnessFactor;

	public Body(Color color, Roundness shape, Outline outline) {
		this.color = color;
		this.roundnessFactor = shape.roundnessFactor;
		this.outlineWidth = outline.outlineWidth;
		
		if(outlineWidth > 0)
		{
			float[] hsb = java.awt.Color.RGBtoHSB((int)(color.r * 255), (int)(color.g * 255), (int)(color.b * 255), null);
			hsb[1] *= 1.6;
			if(hsb[1] > 100) hsb[1] = 100;
			outlineColor = new Color(java.awt.Color.HSBtoRGB(hsb[0], hsb[1], hsb[2]));
		}
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
		NONE(0), MEDIUM(4), MAXIMUM(8);
		
		public final int outlineWidth;
		
		private Outline(int width)
		{
			this.outlineWidth = width;
		}
	}
}
