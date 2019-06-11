package LurkInTheShadow;

import edu.ricm3.game.GameUI;

import java.awt.Dimension;

import LurkInTheShadow.Controller;
import LurkInTheShadow.Model;
import LurkInTheShadow.View;

public class GameMain {

	public static void main(String[] args) {

		Model model = new Model();
		View view = new View(model);
		Controller controller = new Controller(model, view);

		Dimension d = new Dimension(Options.W_WIDTH, Options.W_HEIGHT);
		
		new GameUI(model, view, controller, d);
		
		return;
	}

}
