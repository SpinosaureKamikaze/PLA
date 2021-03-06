package LurkInTheShadow;

import java.awt.Color;
import java.awt.Graphics;
import java.util.Iterator;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import map_creator.Fleche;
import map_creator.MiniMap;
import edu.ricm3.game.GameView;
import edu.ricm3.game.Options;

public class View extends GameView {

	private static final long serialVersionUID = 1L;

	Color m_background = Color.black;
	long m_last;
	int m_npaints;
	int m_fps;
	Model m_model;


	BufferedImage background;
	BufferedImage minimap;

	public View(Model m) {
		m_model = m;
	}

	public void step(long now) {
		m_model.step(now);

	}

	private void computeFPS() {
		long now = System.currentTimeMillis();
		if (now - m_last > 1000L) {
			m_fps = m_npaints;
			m_last = now;
			m_npaints = 0;
		}
		m_game.setFPS(m_fps, null);

		m_npaints++;
	}

	@Override
	protected void _paint(Graphics g) {
		computeFPS();

		g.setColor(m_background);
		g.fillRect(0, 0, getWidth(), getHeight());

		int i1 = m_model.map.iViewport; // limite i
		int j1 = m_model.map.jViewport; // limite j

		int nbCasei = 24;
		int nbCasej = 32;

		Component c;

		for (int i = 0; i < nbCasei; i++) {
			j1 = m_model.map.jViewport;
			for (int j = 0; j < nbCasej; j++) {

				c = m_model.ElementsTore[i1][j1];
				if (c.m_show == true || m_model.mainPlayed == m_model.reine) { //Verifie l'affichage possible et si on est la reine
					c.paint(g);
				}
				j1++;
			}
			i1++;
		}

		Iterator<Component> iterC = m_model.mobileComponents.iterator();

		while (iterC.hasNext()) {
			c = iterC.next();

			if ((c.m_dead == false && c.m_show == true) || (c.m_dead == false &&  m_model.mainPlayed instanceof Queen)) { //Verifie si l'objet est dans le rayon du joueur
				c.paintMap(g);

			}
		}

		Iterator<Component> iterI = m_model.items.iterator();

		while (iterI.hasNext()) {
			c = iterI.next();
			if (c.m_show == true || m_model.mainPlayed instanceof Queen) { // Verifie si l'objet est dans le rayon du joueur			 
				c.paintMap(g);
			}
		}

		//Barre de temps//
//		if (m_model.mainPlayed instanceof Queen) {
//			g.setColor(Color.red);
//			if (m_model.reine.timer < 3000) {
//				g.fillRect(m_model.map.jViewport + 480,
//						m_model.map.iViewport + 352, 100, 10);
//			}
//			if (m_model.reine.timer >= 3000 && m_model.reine.timer < 6000) {
//				g.fillRect(m_model.map.jViewport + 480,
//						m_model.map.iViewport + 352, 75, 10);
//			}
//			if (m_model.reine.timer >= 6000 && m_model.reine.timer < 9000) {
//				g.fillRect(m_model.map.jViewport + 480,
//						m_model.map.iViewport + 352, 50, 10);
//			}
//			if (m_model.reine.timer > 9000) {
//				g.fillRect(m_model.map.jViewport + 480,
//						m_model.map.iViewport + 352, 25, 10);
//			}
//	}

			MiniMap minimap = m_model.minimap;
			Fleche fleche = m_model.fleche;

			minimap.paint(g);
			fleche.paint(g);
			
			Battery battery=m_model.battery;
			battery.paint(g);
			
			m_model.score.paint(g);
			
			


	}
}