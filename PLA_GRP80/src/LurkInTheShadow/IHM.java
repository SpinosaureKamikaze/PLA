package LurkInTheShadow;

import java.awt.Color;
import java.awt.Point;
import java.awt.Graphics;

public class IHM {

	Model m_model;
	Point m_origin;
	int m_w, m_h;
	Color m_color;

	Score m_score;
	IHM m_test;

	IHM(Model model, int x, int y, int width, int height, Color color) {

		m_model = model;
		m_origin = new Point(x, y);
		m_w = width;
		m_h = height;
		m_color = color;
	}

	/**
	 * paints this square on the screen.
	 * 
	 * @param g
	 **/

	void paint(Graphics g) {
		g.setColor(m_color);
		g.fillRect(m_origin.x, m_origin.y, m_w, m_h);
	}
}
