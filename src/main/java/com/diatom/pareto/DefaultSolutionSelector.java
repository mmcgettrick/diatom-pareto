package com.diatom.pareto;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * DefaultSolutionSelector is the default selector for Agents.
 * By default it selects nothing.
 * @author mmcgettrick
 *
 */
public class DefaultSolutionSelector extends SolutionSelector {

	public DefaultSolutionSelector() {
		this(new HashMap<String,Object>());
		this.options.put("name","DefaultSolutionSelector");
	}
	
	public DefaultSolutionSelector(Map<String, Object> options) {
		super(options);
	}

	public Collection<Solution> select(SolutionManager sm) {
		return Collections.emptyList();
	}

}
