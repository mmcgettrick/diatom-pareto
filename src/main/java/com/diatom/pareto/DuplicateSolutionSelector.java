package com.diatom.pareto;

import java.util.*;

/**
 * Finds duplicate solutions in the solution population.   Solutions A and B are duplicates
 * if their evaluation criteria are identical, even if they have some underlying structural difference.
 * Duplicate calculations consider ONLY the objective criteria.  If underlying differences are 
 * important, the differences should probably be captured as a separate objective.  Note that if
 * A and B are duplicates only A (or B) will be actually selected for subsequent removal by the DefaultDestroyerAgent
 * that has been configured to remove duplicates.  (One of the duplicated solutions will be retained in the population.)
 * 
 * @see DefaultDestroyerAgent
 * @author mmcgettrick
 *
 */
public class DuplicateSolutionSelector extends SolutionSelector {


	/**
	 * Constructor
	 * @param options key-value map of options
	 */
	public DuplicateSolutionSelector(Map<String, Object> options) {
		super(options);
		this.options.put("name","DuplicateSolutionSelector");
	}

	/**
	 * Returns the duplicated solutions in the solution manager
	 */
	public Collection<Solution> select(SolutionManager sm) {

		synchronized (this) {
			// Get the solutions
			List<Solution> solutions = sm.getSolutions();

			// If there are no solutions return
			if (solutions == null || solutions.size() == 0) return Collections.emptyList();

			// Set of Solutions to remove
			Set<Solution> remove = new HashSet<Solution>();

			// Mark duplicated solutions for removal
			int n = solutions.size();
			for (int i = 0; i < n; i++) {
				Solution solutionA = solutions.get(i);
				if (!remove.contains(solutionA)) {
					for (int j = i + 1; j < n; j++) {
						Solution solutionB = solutions.get(j);
						if (!remove.contains(solutionB)) {
							if (solutionA.duplicates(solutionB)) {
								remove.add(solutionB);
							}
						}
					}
				}
			}

			return remove;
		}
	}

}
