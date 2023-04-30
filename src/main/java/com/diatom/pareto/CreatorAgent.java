package com.diatom.pareto;

import java.util.Collection;
import java.util.Map;


/**
 * A type of Agent which creates new solutions rather than copying and 
 * modifying an existing solution. Agents wishing to subclass this need only
 * implement the createNewSolution method, returning the new solution.
 * @author mmcgettrick
 *
 */
public abstract class CreatorAgent extends Agent {

	public CreatorAgent(Map<String, Object> options) {
		super(options);
	}

	/**
	 * Run a creator agent.  New solutions are created.  Each newly created
	 * solution is evaluted, then added the the Solution Manager
	 * @see SolutionManager
	 * @see Solution
	 */
	public final void run() {
		SolutionManager sm = SolutionManager.getInstance();
		if(activator.activate(sm)) {
			Collection<Solution> result = createNewSolutions();
			for(Solution s : result) {
				s.addToAgentHistory(this.getName());
				s.evaluate();
				SolutionManager.getInstance().addSolution(s);
			}
		}
	}
	
	/**
	 * This method must be implemented for any creator agent.
	 * @return the newly created solutions
	 */
	protected abstract Collection<Solution> createNewSolutions();
	
}
