package com.diatom.pareto;

import java.util.Collection;
import java.util.Map;

/**
 * A solution selector is an algorithm for choosing solutions from the current population for modification
 * or removal. Any agent can define its own selector, thus imbuing the agent with the "intellegence" to
 * define what to work on. Some destroyers are nothing more than a selector - they remove all solutions
 * selected from the population in some fashion. The default solution selector is simply to return an empty list.
 * Worker agents, by default, use the RandomSolutionSelector to pick a single random solution from the population.
 * The extracted solution is a clone of the solution still in the population. The selector must define
 * whether it is returning the original solutions (by reference) or copies (clones) of solutions.
 * 
 * @see DefaultSolutionSelector
 * @see RandomSolutionSelector
 * @author mmcgettrick
 *
 */
public abstract class SolutionSelector {

protected Map<String,Object> options;
    
	/**
	 * Constructor
	 * @param options key-value map of options
	 */
	public SolutionSelector(Map<String,Object> options) {
		this.options = options;
	}
	
	/**
	 * Get the name of the selector
	 * @return
	 */
	public String getName() {
		return (String) this.options.get("name");
	}
	
	/**
	 * Get the value of a specifically-named option
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
		return "SolutionSelector [options=" + options + "]";
	}
	
	/**
	 * This abstract method is overridden by any subclassing Selector
	 * @param sm
	 * @return
	 */
	public abstract Collection<Solution> select(SolutionManager sm); 
	
}
