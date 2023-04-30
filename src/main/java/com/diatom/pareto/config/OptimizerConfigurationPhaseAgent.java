package com.diatom.pareto.config;

public class OptimizerConfigurationPhaseAgent {

	private String name;
	private int frequency;
	
	public OptimizerConfigurationPhaseAgent(String name, int frequency) {
		this.name = name;
		this.frequency = frequency;
	}
	
	public OptimizerConfigurationPhaseAgent(String name) {
		this.name = name;
		this.frequency = -1;
	}
	
	public String getName() {
		return name;
	}
	
	public int getFrequency() {
		return frequency;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public void setFrequency(int frequency) {
		this.frequency = frequency;
	}
	
	
	
}
