package com.diatom.pareto;

import java.util.Map;

/**
 * Resources define input parameters and contraints for a particular problem instances, and may be
 * accessed from objectives and agents as needed in order to carry out their methods.
 * Each resource is registered into the optimzer from which it can be accessed.
 * 
 * For example:
 * 
 * MyResource resource = (MyResource) (Optimizer.getInstance().getResource("resourceName"));
 * 
 * @author mmcgettrick
 *
 */
public abstract class Resource {

	protected Map<String,Object> options;
    
	/**
	 * Constructor
	 * @param options key-value pairs of options
	 */
	public Resource(Map<String,Object> options) {
		this.options = options;
	}
	
	/**
	 * Get the name of the resource
	 * @return
	 */
	public String getName() {
		return (String) this.options.get("name");
	}
	
}
