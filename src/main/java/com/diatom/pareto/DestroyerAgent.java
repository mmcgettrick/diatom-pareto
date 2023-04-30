package com.diatom.pareto;

import java.util.Collection;
import java.util.Map;


/**
 * A type of Agent which removes solutions rather than copying and 
 * modifying an existing solution. Agents wishing to subclass this need only
 * implement the removeSolutions method, returning the solutions to be removed.
 * 
 * Default activator: NonZeroPopulationActivator
 * Default selector: DefaultSolutionSelector
 * 
 * @author mmcgettrick
 *
 */
public abstract class DestroyerAgent extends Agent {

	public DestroyerAgent(Map<String,Object> options) {
		super(options);
		if(!options.containsKey("activator")) {
			this.setActivator(new NonZeroPopulationActivator());
		}
		if(!options.containsKey("selector")) {
			this.setSelector(new DefaultSolutionSelector());
		}
	}
	
	public final void run() {
		synchronized (this) {
			SolutionManager sm = SolutionManager.getInstance();
			if (activator.activate(sm)) {
				Collection<Solution> selected = selector.select(sm);
				Collection<Solution> solutions = removeSolutions(selected);
				sm.removeSolutions(solutions);
			}
		}
	}
	
	/**
	 * 
	 * @return the solutions to remove
	 */
	protected abstract Collection<Solution> removeSolutions(Collection<Solution> selected);
	
}
