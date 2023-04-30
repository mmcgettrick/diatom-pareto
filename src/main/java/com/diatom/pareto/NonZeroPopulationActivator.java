package com.diatom.pareto;

import java.util.HashMap;
import java.util.Map;

/**
 * NonZeroPopulationActivator will only return true if there is a non-zero population. 
 * This activator is handy for cases when an Agent should only run if solutions exist.
 * @author mmcgettrick
 *
 */
public class NonZeroPopulationActivator extends AgentActivator {

	public NonZeroPopulationActivator() {
		this(new HashMap<String,Object>());
		this.options.put("name","NonZeroPopulationActivator");
	}
	
	public NonZeroPopulationActivator(Map<String, Object> options) {
		super(options);
	}
	
	public boolean activate(SolutionManager sm) {
		return sm.numberOfSolutions() > 0 ? true : false;
	}

}
