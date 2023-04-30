package com.diatom.pareto;

import java.util.Collection;
import java.util.Map;

/**
 * 
 * @author mmcgettrick
 * 
 * DefaultDestroyerAgent handles the most common DestroyerAgent use case, where you would like
 * to remove all Solutions chosen by the Selector. In this case it is unnecessary to 
 * implement a removeSolutions method, as it simply functions as a pass-through.
 *
 */
public class DefaultDestroyerAgent extends DestroyerAgent {

	public DefaultDestroyerAgent(Map<String, Object> options) {
		super(options);
	}

	@Override
	protected Collection<Solution> removeSolutions(Collection<Solution> selected) {
		return selected;
	}

}
