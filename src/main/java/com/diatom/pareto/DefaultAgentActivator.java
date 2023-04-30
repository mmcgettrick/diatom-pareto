package com.diatom.pareto;

import java.util.HashMap;
import java.util.Map;

/**
 * DefaultAgentActivator is the default activator for Agents.
 * The activate method always returns True meaning that the agent will run whenever
 * it is handed control from the optimizer regardless of the current state of the solution manager.
 * 
 * @author mmcgettrick
 * @see SolutionManager
 */
public class DefaultAgentActivator extends AgentActivator {

	/**
	 * Contructor with no options
	 */
	public DefaultAgentActivator() {
		this(new HashMap<String,Object>());
		this.options.put("name","DefaultAgentActivator");
	}
	
	/**
	 * Constructor with options
	 * @param options
	 */
	public DefaultAgentActivator(Map<String, Object> options) {
		super(options);
	}

	/**
	 * Always returns true - the selected agent always runs.
	 */
	public boolean activate(SolutionManager sm) {
		return true;
	}

}
