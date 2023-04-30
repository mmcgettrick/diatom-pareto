package com.diatom.pareto;

import java.util.Map;

/**
 * Agents create, modify, and remove solutions from the population of candidate solutions. 
 * Creator agents bootstrap the population with new solutions. A worker agent takes as input one or more 
 * existing solutions, performs some operation, and produces new solutions to be added to
 * the solution population. Destroyer agents are responsible for removing bad solutions from
 * the population and provide the selective pressure for driving the evolution of the solution 
 * population towards addressing registered objectives.
 * 
 * @author mmcgettrick
 * @see CreatorAgent
 * @see WorkerAgent
 * @see DestroyerAgent
 * @see SolutionManager
 *
 */
public abstract class Agent implements Runnable {

	protected AgentActivator activator = new DefaultAgentActivator();
	protected SolutionSelector selector = new DefaultSolutionSelector();
	protected Map<String,Object> options;
	
	protected Agent(Map<String,Object> options) {
		this.options = options;
		if(this.options.containsKey("activator")) {
			this.activator = Optimizer.getInstance().getActivator((String)this.options.get("activator"));
		}
		if(this.options.containsKey("selector")) {
			this.selector = Optimizer.getInstance().getSelector((String)this.options.get("selector"));
		}
	}
	
	/**
	 * Get the name of the agent
	 * @return
	 */
	public String getName() {
		return (String) this.options.get("name");
	}
	
	/**
	 * Get the value of a named option associated with the agent
	 * @param key
	 * @return
	 */
	public Object getOption(String key) {
		return this.options.get(key);
	}
	
	/**
	 * Get the key-value map for the agent
	 * @return
	 */
	public Map<String,Object> getOptions() {
		return this.options;
	}
	
	/**
	 * Get the agent activator - the class responsible for deciding whether the
	 * agent should run when it is handed control from the optimizer
	 * @return The activator
	 * @see AgentActivator
	 */
	public AgentActivator getActivator() {
		return activator;
	}

	/**
	 * Get the solution selector - the class responsible for deciding which solutions to 
	 * extract from the Solution manager for further processing
	 * @return The selector
	 * @see SolutionSelector
	 */
	public SolutionSelector getSelector() {
		return selector;
	}

	/**
	 * Set the activator for the agent
	 * @param activator The AgentActivator instance
	 */
	public void setActivator(AgentActivator activator) {
		this.activator = activator;
	}

	/**
	 * Set the selector for the aent
	 * @param selector The SolutionSelector instance
	 */
	public void setSelector(SolutionSelector selector) {
		this.selector = selector;
	}

	@Override
	/**
	 * Output agent as a string
	 */
	public String toString() {
		return "Agent [options=" + options + "]";
	}

	/**
	 * Run the agent. 
	 */
	public abstract void run();
	
}
