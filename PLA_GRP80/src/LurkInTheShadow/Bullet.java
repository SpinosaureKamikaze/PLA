package LurkInTheShadow;

import java.awt.image.BufferedImage;

public class Bullet extends Projectile {

	public Bullet (Model model, BufferedImage sprite, int rows, int columns, int x, int y,float scale, int id_x, boolean show, IDirection dir) {
		
		super(model, sprite, rows, columns, x, y, scale, id_x, show, dir);

	}
	
}
