package com.diatom.pareto;

import java.util.Collection;
import java.util.Map;


/**
 * The most common type of Agent, WorkerAgent subclasses will create a copy
 * of an existing solution, modify it in some way, and add the modified 
 * solution back to the pool. Subclasses need only implement the 
 * runWithSolution method, making any necessary modifications to the solution
 * parameter. All the singing and dancing of getting the solution and saving 
 * the modified solution is handled automatically.
 * 
 * Default activator: NonZeroPopulationActivator
 * Default selector: RandomSolutionSelector
 * 
 * @author mmcgettrick
 *
 */
public abstract class WorkerAgent extends Agent {

	public WorkerAgent(Map<String,Object> options) {
		super(options);
		if(!options.containsKey("activator")) {
			this.setActivator(new NonZeroPopulationActivator());
		}
		if(!options.containsKey("selector")) {
			this.setSelector(new RandomSolutionSelector());
		}
	}
	
	public final void run() {
		SolutionManager sm = SolutionManager.getInstance();
		if(activator.activate(sm)) {
			Collection<Solution> selected = selector.select(sm);
			Collection<Solution> result = runWithSolutions(selected);
			for(Solution s : result) {
				s.addToAgentHistory(this.getName());
				s.evaluate();
				SolutionManager.getInstance().addSolution(s);
			}
		}
	}

	/**
	 * Worker Agents should implement this method to modify an existing solution.
	 * All the singing and dancing of getting the solution and saving the modified 
	 * solution to the database is handled automatically, simply modify the 
	 * solution.
	 * @param selected The solutions to be worked on (aka modified).
	 */
	protected abstract Collection<Solution> runWithSolutions(Collection<Solution> selected);

}
