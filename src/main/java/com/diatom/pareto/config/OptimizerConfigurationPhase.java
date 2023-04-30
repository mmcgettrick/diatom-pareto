package com.diatom.pareto.config;

import java.util.ArrayList;
import java.util.List;

public class OptimizerConfigurationPhase {
	
	private String name;
	private int iterations;
	private int summaryInterval;
	private List<OptimizerConfigurationPhaseAgent> agents;
	
	public OptimizerConfigurationPhase(String name, int iterations, int summaryInterval) {
		this.name = name;
		this.iterations = iterations;
		this.summaryInterval = summaryInterval;
		this.agents = new ArrayList<OptimizerConfigurationPhaseAgent>();
	}
	
	public OptimizerConfigurationPhase(String name, int iterations, int summaryInterval, 
			List<OptimizerConfigurationPhaseAgent> agents) {
		this.name = name;
		this.iterations = iterations;
		this.summaryInterval = summaryInterval;
		this.agents = agents;
	}
	
	public void addAgent(OptimizerConfigurationPhaseAgent agent) {
		this.agents.add(agent);
	}

	public String getName() {
		return name;
	}
	
	public int getIterations() {
		return iterations;
	}
	
	public int getSummaryInterval() {
		return summaryInterval;
	}
	
	public List<OptimizerConfigurationPhaseAgent> getAgents() {
		return agents;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public void setIterations(int iterations) {
		this.iterations = iterations;
	}
	
	public void setSummaryInterval(int summaryInterval) {
		this.summaryInterval = summaryInterval;
	}
	
}
