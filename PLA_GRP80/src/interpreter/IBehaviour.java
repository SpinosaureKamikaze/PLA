package interpreter;
import java.util.List;
import java.util.LinkedList;
import java.util.ListIterator;
import LurkInTheShadow.*;


public class IBehaviour {

	IState source ;
	List<ITransition> transitions ;
	
	public IBehaviour(IState source, List<ITransition> transitions){
		this.source = source ; 
		this.transitions = transitions ;
	}
	
	public IState step(Component e) throws Interpreter_Exception{
		// - selectionne la première transition faisable
		// - lève une exception si aucune transition possible
		List<ITransition> true_trans = new LinkedList<ITransition>();
		
		ListIterator<ITransition> iter = transitions.listIterator();
		while (iter.hasNext()) {
			ITransition t = iter.next();
			if (t.condition.eval(e)) {
				true_trans.add(t);
			}
		}
		
		if (true_trans.size()!=0) {
			int index = ((int)(Math.random()*100))%true_trans.size(); //Choix aléatoire de transition
			return (true_trans.get(index)).exec(e);
		}
		
		throw new Interpreter_Exception("Aucune transition possible \n");
	}
	
}
