package com.diatom.pareto;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * RandomSolutionSelector is the default selector for WorkerAgents.
 * Selects one random solution from the population.
 * @author mmcgettrick
 *
 */
public class RandomSolutionSelector extends SolutionSelector {

	public RandomSolutionSelector() {
		this(new HashMap<String,Object>());
		this.options.put("name","RandomSolutionSelector");
	}
	
	public RandomSolutionSelector(Map<String, Object> options) {
		super(options);
	}

	public Collection<Solution> select(SolutionManager sm) {
		ArrayList<Solution> result = new ArrayList<Solution>();
		result.add(sm.getCloneOfRandomSolution());
		return result;
	}

}
