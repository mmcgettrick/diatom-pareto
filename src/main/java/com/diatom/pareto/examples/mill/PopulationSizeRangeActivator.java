package com.diatom.pareto.examples.mill;

import com.diatom.pareto.AgentActivator;
import com.diatom.pareto.SolutionManager;

import java.util.HashMap;
import java.util.Map;

/**
 * NonZeroPopulationActivator will only return true if there is a non-zero population. 
 * This activator is handy for cases when an Agent should only run if solutions exist.
 * @author mmcgettrick
 *
 */
public class PopulationSizeRangeActivator extends AgentActivator {

	public PopulationSizeRangeActivator() {
		this(new HashMap<String,Object>());
	}
	
	public PopulationSizeRangeActivator(Map<String, Object> options) {
		super(options);
	}
	
	public boolean activate(SolutionManager sm) {
		Integer min = (Integer) options.get("min");
		Integer max = (Integer) options.get("max");
		int current = sm.numberOfSolutions();
		
		if ((min != null) && (current < min.intValue()))
			return false;
		
		if ((max != null) && (current > max.intValue()))
			return false;
		
		return true;
	}

}
