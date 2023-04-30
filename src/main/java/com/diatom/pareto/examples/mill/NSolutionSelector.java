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
public class NSolutionSelector extends SolutionSelector {

	public NSolutionSelector() {
		this(new HashMap<String,Object>());
	}
	
	public NSolutionSelector(Map<String, Object> options) {
		super(options);
	}

	public Collection<Solution> select(SolutionManager sm) {
		ArrayList<Solution> result = new ArrayList<Solution>();
		Integer N = (Integer) options.get("N");
		if (N == null) N = 1; 
		for (int i=0; i<N; i++)
			result.add(sm.getCloneOfRandomSolution());
		
		return result;
	}

}
