package LurkInTheShadow;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.geom.Ellipse2D;
import java.awt.image.BufferedImage;
import java.util.Iterator;



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
	public boolean m_show;
	public int power;
	public int life;
	public int speed;
	public boolean on_move;
	public int puissance_eclairage;
	
	
	int lampe_x;
	int lampe_y;
	double lampe_width;
	double lampe_height;
	boolean m_dead;
	
	
	
	IAutomaton automate;
	IDirection m_dir; // doit etre NORTH,SOUTH,EAST ou WEST
	public IType m_type; // Definit le type (allié, ennemi, rocher, etc) de ce
							// component.

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
		m_model.componentsToAdd.add(this);
		model.nbElements++;
		power=0;
		speed=0;
		model.light = false;
		splitSprite();
		
		lampe_x=75;
		lampe_y=75;
		lampe_width=2.4*lampe_x;
		lampe_height=2.4*lampe_y;
		m_dead = false;
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

	public boolean is_in_case(int x, int y) {// x et y sont les coord de la case
		if ((m_x >= x + 32) // trop à droite
				|| (m_x + m_w <= x) // trop à gauche
				|| (m_y >= y + 32) // trop en bas
				|| (m_y + m_h <= y)) { // trop en haut
			return false;
		}
		return true;
	}
	
	boolean Vision(Component c) {
		Ellipse2D.Double player = new Ellipse2D.Double(m_model.mainPlayed.m_x - lampe_x, m_model.mainPlayed.m_y - lampe_y, lampe_width, lampe_height);
		Rectangle objet = c.getBounds();

		if (player.intersects(objet)) {
			return true;
		}
		return m_model.light;
		
	}

	public void Afficher() {
		Iterator<Component> iter = m_model.components.listIterator();

		while (iter.hasNext()) {
			Component c = iter.next();
			
			if(c instanceof Queen) {
				int i=0;
				i++;
			}
			
			if (Vision(c)) {
				c.m_show = true;
			} else {
				c.m_show = false;
			}
		}
	}


	public void step(long now) throws Interpreter_Exception {
		long elapsed = now - m_lastMove;
		
		if(automate==null) {
			return;
		}

		if (elapsed > 60L) {
			m_lastMove = now;
			automate.step(this);
			
		}
		
		//Timer Reine (~10sec)
		if (m_model.mainPlayed instanceof Queen){
			if (m_model.reine.timer==0){
				m_model.reine.timer=now;
			}
			if(now-m_model.reine.timer>10000){
				m_model.reine.timer=0;
				m_model.mainPlayed.Get2();
			}
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
				m_sprites[(i * m_ncols) + j] = m_sprite.getSubimage(x, y, m_w,
						m_h);
			}
		}
	}

	
	public double distance(Component c1, Component c2) {
		int a = (c2.m_x - c1.m_x) * (c2.m_x - c1.m_x);
		int b = (c2.m_y - c1.m_y) * (c2.m_y - c1.m_y);

		return (Math.sqrt((double) a + b));
	}

	public void Get1() {
		
		if (this instanceof Queen) {
			this.setAutomate(m_model.queen);
			this.m_type=IType.ADVERSAIRE;
			m_model.perso1.m_type=IType.PLAYER;
			m_model.perso1.setAutomate(m_model.Player);
			m_model.perso2.setAutomate(m_model.spawn1);
			m_model.perso3.setAutomate(m_model.spawn2);
			m_model.mainPlayed=m_model.perso1;
			slow_world();
		}
		else {
			this.setAutomate(m_model.perso1.automate);
			m_model.perso1.setAutomate(m_model.Player);
			this.m_type=IType.TEAM;
			m_model.perso1.m_type=IType.PLAYER;
			m_model.mainPlayed=m_model.perso1;
		}
		m_model.map.firstCase();

	}


	public void Get2() {

		if (this instanceof Queen) {
			this.setAutomate(m_model.queen);
			this.m_type=IType.ADVERSAIRE;
			m_model.perso2.m_type=IType.PLAYER;
			m_model.perso1.setAutomate(m_model.spawn1);
			m_model.perso2.setAutomate(m_model.Player);
			m_model.perso3.setAutomate(m_model.spawn2);
			m_model.mainPlayed=m_model.perso2;
			slow_world();
		}
		else {
			this.setAutomate(m_model.perso2.automate);
			m_model.perso2.setAutomate(m_model.Player);
			this.m_type=IType.TEAM;
			m_model.perso2.m_type=IType.PLAYER;
			m_model.mainPlayed=m_model.perso2;
		}
		m_model.map.firstCase();

	}

	public void Get3() {

		if (this instanceof Queen) {
			this.setAutomate(m_model.queen);
			this.m_type=IType.ADVERSAIRE;
			m_model.perso3.m_type=IType.PLAYER;
			m_model.perso1.setAutomate(m_model.spawn1);
			m_model.perso2.setAutomate(m_model.spawn2);
			m_model.perso3.setAutomate(m_model.Player);
			m_model.mainPlayed=m_model.perso3;
			slow_world();
		}
		else {
			this.setAutomate(m_model.perso3.automate);
			m_model.perso3.setAutomate(m_model.Player);
			this.m_type=IType.TEAM;
			m_model.perso3.m_type=IType.PLAYER;
			m_model.mainPlayed=m_model.perso3;
		}
		m_model.map.firstCase();

	}
	
	public void GetQueen() { //Pour la queen

		m_model.perso1.setAutomate(m_model.spawn1);//Automates à changer
		m_model.perso2.setAutomate(m_model.spawn1);
		
		m_model.perso3.setAutomate(m_model.spawn1);
		m_model.reine.setAutomate(m_model.Player);
		this.m_type=IType.TEAM;
		m_model.reine.m_type=IType.PLAYER;
		m_model.mainPlayed=m_model.reine;
		
		if (!(this instanceof Queen)) {
			speed_up_world();
		}
		m_model.map.firstCase();
		
	}
	
	public void slow_world() {
		Iterator<Ally> iter = m_model.allies.iterator();
		while (iter.hasNext()) {
			Ally a = iter.next();
			a.speed = a.speed/2;
		}
		
		Iterator<Monster> iterM = m_model.monstres.iterator();
		while (iterM.hasNext()) {
			Monster m = iterM.next();
			m.speed = m.speed/2;
		}
	}
	
	public void speed_up_world() {
		Iterator<Ally> iter = m_model.allies.iterator();
		while (iter.hasNext()) {
			Ally a = iter.next();
			a.speed = a.speed*2;
		}
		
		Iterator<Monster> iterM = m_model.monstres.iterator();
		while (iterM.hasNext()) {
			Monster m = iterM.next();
			m.speed = m.speed*2;
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
	
	public boolean jump(){
		return true;
	}
	
	public boolean Throw(){
		return true;
	}
	
	public boolean pick(){
		return true;
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
	
	//A Overrider
	public boolean egg(){
		return true;
	}
	
	//A Overrider
	public boolean kamikaze(){
		return true;
	}
	
	public void paint(Graphics g) {
		Image img = m_sprites[m_idx];
		int w = (int) (m_scale * m_w);
		int h = (int) (m_scale * m_h);
		g.drawImage(img, (m_x-m_model.mainPlayed.m_x)%1024+512, (m_y-m_model.mainPlayed.m_y)%768+384, w, h, null);
	}
	
	public void paintMap(Graphics g) {

		Image img = m_sprites[m_idx];
		int w = (int) (m_scale * m_w);
		int h = (int) (m_scale * m_h);
		g.drawImage(img, (m_x-m_model.mainPlayed.m_x)+512, (m_y-m_model.mainPlayed.m_y)+384, w, h, null);
	}
	
}
