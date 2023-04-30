package com.diatom.pareto;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * A phase defines the run frequency of a collecition of agents, the total number 
 * of agent iterations (invocations) and how often a summary of the population is
 * dumped to the screen. The optimizer solves problems by running a series of phases.
 * 
 * @author Diatom Software
 *
 */
public class Phase {
	
	private String name;    // The name of the phase
	private int iterations; // Total number of iterations for the phase
	private int summaryInterval; // How often the populations is summarized (0=never)
	private Map<String,Integer> frequency; // agent name -> frequency
	private int totalFrequency = 0;
	/**
	 * Constructor
	 */
	public Phase()
	{
		name = "";
		iterations = 0;
		summaryInterval = 0;
		frequency = new HashMap<String,Integer>();
		totalFrequency = 0;
	}

	public String getName()
	{
		return name;
	}
	
	public void setName(String name)
	{
		this.name = name;
	}
	
	public int getIterations() {
		return iterations;
	}

	public void setIterations(int iterations) {
		this.iterations = iterations;
	}

	public int getSummaryInterval() {
		return summaryInterval;
	}

	public void setSummaryInterval(int summaryInterval) {
		this.summaryInterval = summaryInterval;
	}

	
	public void addFrequency(String agent, int freq)
	{
		frequency.put(agent, freq);
		totalFrequency += freq;
	}
	
	public int getFrequency(String agent)
	{
		Integer freq = frequency.get(agent);
		if (freq==null)
			return 0;
		else return freq.intValue();
	}
	
	public Set<String> getAgents()
	{
		return frequency.keySet();
	}
	
	/**
	 * Pick an agent from the phase randomly, weighted by 
	 * invocation frequency
	 * @return The name of an agent
	 */
	public String pickRandomAgent()
	{
		double remaining = Math.random() * totalFrequency;
		for (String agent : getAgents())
		{
			remaining -= getFrequency(agent);
			if (remaining<0)
				return agent;
		}
		return null; // this shouldn't happen!
	}
}
