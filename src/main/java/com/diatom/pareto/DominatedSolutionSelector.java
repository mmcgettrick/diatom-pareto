package com.diatom.pareto;

import java.util.*;

/**
 * Selects all solutions that are dominated by one or more other solutions. Solution A dominates
 * Solution B if A is better than or as good as solution B with respect to every objective, and
 * strictly better than B with respect to at least one objective. This selector is mainly employed by the 
 * DefaultDestroyerAgent for periodically removing all dominated solutions from the population 
 * in order to drive the evolution of the population towards solutions that are closer to objective targets.
 * 
 * @see DefaultDestroyerAgent
 * @see Objective
 * 
 * @author mmcgettrick
 *
 */
public class DominatedSolutionSelector extends SolutionSelector {


	/**
	 * Constructor
	 * @param options key-value map of options if applicable
	 */
	public DominatedSolutionSelector(Map<String, Object> options) {
		super(options);
		this.options.put("name","DominatedSolutionSelector");
	}

	/**
	 * Returns the dominated solutions in the solution manager
	 */
	public Collection<Solution> select(SolutionManager sm) {

		synchronized(this) {
			// Get the solutions
			List<Solution> solutions = sm.getSolutions();

			// If there are no solutions return
			if (solutions == null || solutions.size() == 0) return Collections.emptyList();

			// Set of Solutions to remove
			Set<Solution> remove = new HashSet<Solution>();

			// Mark dominated solutions for removal
			int n = solutions.size();
			for (int i = 0; i < n; i++) {
				Solution solutionA = solutions.get(i);
				if (!remove.contains(solutionA)) {
					for (int j = 0; j < n; j++) {
						if (i != j) {
							Solution solutionB = solutions.get(j);
							if (!remove.contains(solutionB)) {
								if (solutionA.dominates(solutionB)) {
									remove.add(solutionB);
								}
							}
						}
					}
				}
			}

			return remove;
		}

	}

}
