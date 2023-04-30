package com.diatom.pareto.examples.mill;

import com.diatom.pareto.Solution;
import com.diatom.pareto.SolutionManager;
import com.diatom.pareto.SolutionSelector;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * DefaultSolutionSelector is the default selector for Agents.
 * Selects one random solution from the population.
 * @author mmcgettrick
 *
 */
public class RandomSolutionSelector extends SolutionSelector {

	public RandomSolutionSelector() {
		this(new HashMap<String,Object>());
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
