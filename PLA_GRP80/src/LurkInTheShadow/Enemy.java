package LurkInTheShadow;

import java.awt.image.BufferedImage;

public class Enemy extends Character {
	int faction;

	public Enemy(Model m, int x, int y, int w, int h, float scale, BufferedImage sprite, int rows, int col, int id_x,
			int[] spritesGoUp, int[] spritesGoDown, int[] spritesGoLeft, int[] spritesGoRight, boolean show, int HP,
			int damage, int intensity) {
		super(m, x, y, w, h, scale, sprite, rows, col, id_x, spritesGoUp, spritesGoDown, spritesGoLeft, spritesGoRight,
				show, HP, damage, intensity);
		this.faction = 1;
	}
}
