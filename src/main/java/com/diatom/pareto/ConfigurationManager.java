package com.diatom.pareto;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 * Created by rachlin on 7/29/16.
 */
public class ConfigurationManager {

    private static ConfigurationManager manager;

    private Map<String,Resource> resources;
    private Map<String,AgentActivator> activators;
    private Map<String,SolutionSelector> selectors;
    private Map<String,Agent> agents;
    private Map<String,Objective> objectives;

    private List<String> optimalSolutionObjectives;
    private List<Phase> phases;


    private ConfigurationManager() {
        this.resources = new TreeMap<String, Resource>();
        this.activators = new TreeMap<String, AgentActivator>();
        this.selectors = new TreeMap<String, SolutionSelector>();
        this.agents = new TreeMap<String,Agent>();
        this.objectives = new TreeMap<String,Objective>();
        this.optimalSolutionObjectives = new ArrayList<String>();
        this.phases = new ArrayList<Phase>();
    }

    public static ConfigurationManager getInstance() {
        if (manager==null) manager = new ConfigurationManager();
        return manager;
    }

    public Map<String, Resource> getResources() {
        return resources;
    }

    public Map<String, AgentActivator> getActivators() {
        return activators;
    }

    public Map<String, SolutionSelector> getSelectors() {
        return selectors;
    }

    public Map<String, Agent> getAgents() {
        return agents;
    }

    public Map<String, Objective> getObjectives() {
        return objectives;
    }

    public List<String> getOptimalSolutionObjectives() {
        return optimalSolutionObjectives;
    }

    public List<Phase> getPhases() {
        return phases;
    }
}
