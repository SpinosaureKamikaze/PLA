package LurkInTheShadow;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.util.Iterator;
import java.util.LinkedList;

import edu.ricm3.game.Options;
import interpreter.IAutomaton;
import interpreter.Interpreter_Exception;



public class Component {
	BufferedImage m_sprite;
	public int m_w, m_h;
	public int m_x;
	public int m_y;
	int m_nrows, m_ncols;
	int m_step;
	int m_nsteps;
	public int m_idx;
	public float m_scale;
	long m_lastMove, m_lastReverse;
	public BufferedImage[] m_sprites;
	public Model m_model;
	public int screen;
	public boolean m_show;
	
	public int power;
	public int life;
	
	IAutomaton automate;
	IDirection m_dir; // doit etre NORTH,SOUTH,EAST ou WEST
	public IType m_type; // Definit le type (allié, ennemi, rocher, etc) de ce component.

	public Component(Model model, BufferedImage sprite, int rows, int columns, int x, int y, int h, int w, float scale,
			int id_x, boolean show) {
		m_model = model;
		m_sprite = sprite;
		m_ncols = columns;
		m_nrows = rows;
		m_x = x;
		m_y = y;
		m_h=h;
		m_w=w;
		m_idx=id_x;
		m_scale = scale;
		m_show = show;
		m_dir = IDirection.NORTH; //dir par defaut
		m_model.nbElements++;
		m_model.components.add(this);
		splitSprite();
	}
	
	public void setAutomate(IAutomaton aut) {
		automate = aut;
	}

	public void setType(IType type) {
		m_type = type;
	}

	public int x() {
		return m_x;
	}

	public int y() {
		return m_y;
	}

	public IDirection dir() {
		return m_dir;
	}
	
	public IType type() {
		return m_type;
	}

	public boolean is_in_case(int x, int y) {//x et y sont les coord de la case
		if ((m_x >= x + 32) // trop à droite
				|| (m_x + m_w <= x) // trop à gauche
				|| (m_y >= y + 32) // trop en bas
				|| (m_y + m_h <= y)) { // trop en haut
			return false;
		}
		else {
			return true;
		}
	}

	public void step(long now) throws Interpreter_Exception {
		long elapsed = now - m_lastMove;

		if (elapsed > 60L) {
			m_lastMove = now;
			automate.step(this);
		}
	}

	public Rectangle getBounds() {
		return new Rectangle(m_x, m_y, m_w, m_h);
	}

	protected void splitSprite() {
		int width = m_sprite.getWidth(null);
		int height = m_sprite.getHeight(null);
		m_sprites = new BufferedImage[m_nrows * m_ncols];
		m_w = width / m_ncols;
		m_h = height / m_nrows;
		m_step = 1;
		for (int i = 0; i < m_nrows; i++) {
			for (int j = 0; j < m_ncols; j++) {
				int x = j * m_w;
				int y = i * m_h;
				m_sprites[(i * m_ncols) + j] = m_sprite.getSubimage(x, y, m_w, m_h);
			}
		}
	}

	boolean Collision(Component c) {
		Rectangle r1 = this.getBounds();
		Rectangle r2 = c.getBounds();

		if (r1.intersects(r2)) {
			return true;
		} else
			return false;

	}

	/*boolean CollisionTotale() {
		if (Options.SHOW_M1) {
			Iterator<Component> iter = this.m_model.ElementsM1.iterator();
			Component tmp = iter.next();
			while (iter.hasNext()) {
				if (tmp instanceof Obstacle) {
					if (this.Collision(tmp)) {
						return true;
					}
				}
				tmp = iter.next();
			}
		}
		if (Options.SHOW_M2) {
			Iterator<Component> iter = m_model.ElementsM2.iterator();
			Component tmp = iter.next();
			while (iter.hasNext()) {
				if (tmp instanceof Obstacle) {
					if (this.Collision(tmp)) {
						return true;
					}
				}
				tmp = iter.next();
			}
		}
		if (Options.SHOW_M3) {
			Iterator<Component> iter = m_model.ElementsM3.iterator();
			Component tmp = iter.next();
			while (iter.hasNext()) {
				if (tmp instanceof Obstacle) {
					if (this.Collision(tmp)) {
						return true;
					}
				}
				tmp = iter.next();
			}
		}
		if (Options.SHOW_M4) {
			Iterator<Component> iter = m_model.ElementsM4.iterator();
			Component tmp = iter.next();
			while (iter.hasNext()) {
				if (tmp instanceof Obstacle) {
					if (this.Collision(tmp)) {
						return true;
					}
				}
				tmp = iter.next();
			}
		}

		return false;
	}*/
	
	public double distance(Component c1, Component c2) {
		int a = (c2.m_x - c1.m_x) * (c2.m_x - c1.m_x);
		int b = (c2.m_y - c1.m_y) * (c2.m_y - c1.m_y);

		return (Math.sqrt((double) a + b));
	}

	public void Get1() {
		
		this.setAutomate(m_model.perso1.automate);
		m_model.perso1.setAutomate(m_model.Player);
		this.m_type=IType.TEAM;
		m_model.perso1.m_type=IType.PLAYER;

	}

	public void Get2() {


		this.setAutomate(m_model.perso2.automate);
		m_model.perso2.setAutomate(m_model.Player);
		this.m_type=IType.TEAM;
		m_model.perso2.m_type=IType.PLAYER;

	}

	public void Get3() {



		this.setAutomate(m_model.perso3.automate);
		m_model.perso3.setAutomate(m_model.Player);
		this.m_type=IType.TEAM;
		m_model.perso3.m_type=IType.PLAYER;
		

	}
	
	public void paint(Graphics g) {

		Image img = m_sprites[m_idx];
		int w = (int) (m_scale * m_w);
		int h = (int) (m_scale * m_h);
		g.drawImage(img, m_x, m_y, w, h, null);

	}
	
	//A Overrider
	public boolean move(IDirection d) {
		return true;
	}

	//A Overrider
	public boolean hit(IDirection d) {
		return true;
	}
	
	//A Overrider
	public boolean pop(IDirection d) {
		return true;
	}
	
	//A Overrider
	public boolean wizz(IDirection d) {
		return true;
	}
	
	//A Overrider
	public boolean turn(IDirection d) {
		return true;
	}
	

}

