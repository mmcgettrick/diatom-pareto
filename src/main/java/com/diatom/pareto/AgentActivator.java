package com.diatom.pareto;

import java.util.Map;

/**
 * 
 * @author mmcgettrick
 *
 * AgentActivator tells an Agent whether or not it is ok to run, based on the current state
 * of the Solution population (in SolutionManager). It contains one method which must be 
 * overridden : activate. The job of this method is to inspect the population and return 
 * true or false depending on whether it is appropriate for the Agent to run. 
 *
 */
public abstract class AgentActivator {

    protected Map<String,Object> options;
    
    /**
     * Constructor
     * @param options Key-value pairs for passing parameters to custom-defined activators
     */
	public AgentActivator(Map<String,Object> options) {
		this.options = options;
	}

	/**
	 * Get the name of the activator
	 * @return
	 */
	public String getName() {
		return (String) this.options.get("name");
	}
	
	/**
	 * Get the value of a named optikon
	 * @param key
	 * @return
	 */
	public Object getOption(String key) {
		return this.options.get(key);
	}
	
	/**
	 * Get all key-value option pairs
	 * @return
	 */
	public Map<String,Object> getOptions() {
		return this.options;
	}
	
	@Override
	public String toString() {
		return "AgentActivator [options=" + options + "]";
	}
	
	/**
	 * Test whether the agent should be activated when handed control from the optimizer.
	 * @param sm The solution manager
	 * @return True if agent should proceed, False otherwise
	 */
	public abstract boolean activate(SolutionManager sm);
	
}
