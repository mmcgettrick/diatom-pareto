package com.diatom.pareto.config;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Object representation of the JSON-formated optimizer configuration file.
 * While in theory it would be possible to define an optimizer configuration programmatically,
 * the usual method is to store configuration settings in a file and then to pass the name of this file
 * to the optimizer when starting an optimization run.
 * @author John
 *
 */
public class OptimizerConfiguration {

	private int optimizerId;
	private int problemId;
	private String licensePath;
	
	private List<OptimizerConfigurationItem> resources;
	private List<OptimizerConfigurationItem> activators;
	private List<OptimizerConfigurationItem> selectors;
	private List<OptimizerConfigurationItem> agents;
	private List<OptimizerConfigurationItem> objectives;
	
	private List<String> optimalSolution;
	
	private List<OptimizerConfigurationPhase> phases;
	
	public OptimizerConfiguration() {
		this.optimizerId = 0;
		this.problemId = 0;
		this.resources = new ArrayList<OptimizerConfigurationItem>();
		this.activators = new ArrayList<OptimizerConfigurationItem>();
		this.selectors = new ArrayList<OptimizerConfigurationItem>();
		this.agents = new ArrayList<OptimizerConfigurationItem>();
		this.objectives = new ArrayList<OptimizerConfigurationItem>();
		this.optimalSolution = new ArrayList<String>();
		this.phases = new ArrayList<OptimizerConfigurationPhase>();
	}
	
	
	public void addActivator(OptimizerConfigurationItem activator) {
		this.activators.add(activator);
	}
	
	public void addSelector(OptimizerConfigurationItem selector) {
		this.selectors.add(selector);
	}
	
	public void addAgent(OptimizerConfigurationItem agent) {
		this.agents.add(agent);
	}
	
	public void addObjective(OptimizerConfigurationItem objective) {
		this.objectives.add(objective);
	}
	
	public void addResource(OptimizerConfigurationItem resource) {
		this.resources.add(resource);
	}
	
	public void addPhase(OptimizerConfigurationPhase phase) {
		this.phases.add(phase);
	}
	
	public void addOptimalSolution(String objective) {
		this.optimalSolution.add(objective);
	}

	public int getOptimizerId() {
		return optimizerId;
	}


	public int getProblemId() {
		return problemId;
	}
	
	public String getLicensePath() {
		return licensePath;
	}


	public void setLicensePath(String licensePath) {
		this.licensePath = licensePath;
	}


	public List<OptimizerConfigurationItem> getResources() {
		return resources;
	}


	public List<OptimizerConfigurationItem> getActivators() {
		return activators;
	}


	public List<OptimizerConfigurationItem> getSelectors() {
		return selectors;
	}


	public List<OptimizerConfigurationItem> getAgents() {
		return agents;
	}


	public List<OptimizerConfigurationItem> getObjectives() {
		return objectives;
	}


	public List<OptimizerConfigurationPhase> getPhases() {
		return phases;
	}


	public List<String> getOptimalSolution() {
		return optimalSolution;
	}


	public void setOptimizerId(int optimizerId) {
		this.optimizerId = optimizerId;
	}


	public void setProblemId(int problemId) {
		this.problemId = problemId;
	}


	public void setOptimalSolution(List<String> optimalSolution) {
		this.optimalSolution = optimalSolution;
	}


	public static OptimizerConfiguration readFromFile(File file) throws Exception {
		BufferedReader reader = new BufferedReader(new FileReader(file));
		String configuration = "";
		for(String line = reader.readLine(); line!=null; line = reader.readLine()) {
			configuration += line;
		}
		reader.close();
		return readFromString(configuration);
	}
	
	private static void processItems(OptimizerConfiguration oc, JSONObject config, String itemsName, String callbackName) throws Exception {
		if(!config.containsKey(itemsName)) return;
		JSONArray items = config.getJSONArray(itemsName);
		Method callback = oc.getClass().getMethod(callbackName, OptimizerConfigurationItem.class);
		for(int i=0; i < items.size(); i++) {
			OptimizerConfigurationItem item = processItem(items.getJSONObject(i));
			callback.invoke(oc,item);
		}
	}
	
	private static OptimizerConfigurationItem processItem(JSONObject item) {
		Map<String,Object> optionsMap = new HashMap<String,Object>();
		JSONArray options = item.getJSONArray("options");
		for(int j=0; j < options.size(); j++) {
			JSONObject opt = options.getJSONObject(j);
			String name = opt.getString("name");
			String type = opt.getString("type");
			if(type.equals("Integer")) optionsMap.put(name,opt.getInt("value")); 
			else if(type.equals("Double")) optionsMap.put(name,opt.getDouble("value"));
			else if(type.equals("Boolean")) optionsMap.put(name,opt.getBoolean("value"));
			else optionsMap.put(name,opt.getString("value"));
		}
		String className = item.getString("className");
		return new OptimizerConfigurationItem(className,optionsMap);
	}
	
	public static OptimizerConfiguration readFromString(String configuration) throws Exception {
		try {
			OptimizerConfiguration oc = new OptimizerConfiguration();
			JSONObject config = JSONObject.fromObject(configuration);
			JSONObject optConfig = config.getJSONObject("optimizer");
			
			// Parameters
			oc.setProblemId(optConfig.getInt("problemId"));
			oc.setOptimizerId(optConfig.getInt("optimizerId"));
			oc.setLicensePath(optConfig.getString("licensePath"));
						
			/// Resources ///
			processItems(oc,optConfig,"resources","addResource");
						
			/// Objectives ///
			processItems(oc,optConfig,"objectives","addObjective");
						
			/// Activators ///
			processItems(oc,optConfig,"activators","addActivator");
			
			/// Selectors ///
			processItems(oc,optConfig,"selectors","addSelector");
			
			/// Load Core Selectors ///
			Map<String,Object> domSelOptions = new HashMap<String,Object>();
			domSelOptions.put("name","DominatedSolutionSelector");
			oc.addSelector(new OptimizerConfigurationItem("com.diatom.pareto.DominatedSolutionSelector",domSelOptions));
			
			Map<String,Object> dupSelOptions = new HashMap<String,Object>();
			dupSelOptions.put("name","DuplicateSolutionSelector");
			oc.addSelector(new OptimizerConfigurationItem("com.diatom.pareto.DuplicateSolutionSelector",dupSelOptions));
			
			Map<String,Object> filterSelOptions = new HashMap<String,Object>();
			filterSelOptions.put("name","FilteredSolutionSelector");
			filterSelOptions.put("filterFile", "conf/filters.cfg");
			oc.addSelector(new OptimizerConfigurationItem("com.diatom.pareto.FilteredSolutionSelector",filterSelOptions));

			/// Agents ///
			processItems(oc,optConfig,"agents","addAgent");
			
			/// Load Core Agents ///
			Map<String,Object> domOptions = new HashMap<String,Object>();
			domOptions.put("name","dominated");
			domOptions.put("selector","DominatedSolutionSelector");
			oc.addAgent(new OptimizerConfigurationItem("com.diatom.pareto.DefaultDestroyerAgent",domOptions));

			Map<String,Object> dupOptions = new HashMap<String,Object>();
			dupOptions.put("name","duplicates");
			dupOptions.put("selector","DuplicateSolutionSelector");
			oc.addAgent(new OptimizerConfigurationItem("com.diatom.pareto.DefaultDestroyerAgent",dupOptions));
			
			Map<String,Object> filtOptions = new HashMap<String,Object>();
			filtOptions.put("name","filtered");
			filtOptions.put("selector","FilteredSolutionSelector");
			oc.addAgent(new OptimizerConfigurationItem("com.diatom.pareto.DefaultDestroyerAgent",filtOptions));
			
			/// Load Optimal Solution config
			if(optConfig.has("optimalSolution")) {
				JSONArray jsOpt = optConfig.getJSONArray("optimalSolution");
				for(int i=0; i < jsOpt.size(); i++) {
					oc.addOptimalSolution(jsOpt.getString(i));
				}
			}
			
			// Load Phases
			JSONArray phaseConfig = optConfig.getJSONArray("phases");
			for(int i=0; i < phaseConfig.size(); i++) {
				JSONObject phs = phaseConfig.getJSONObject(i);
				OptimizerConfigurationPhase op = new OptimizerConfigurationPhase(
						phs.getString("name"), 
						phs.getInt("iterations"), 
						phs.getInt("summaryInterval")
				);
				JSONArray phsAgents = phs.getJSONArray("agents");
				for(int j=0; j<phsAgents.size(); j++) {
					JSONObject agentFreq = phsAgents.getJSONObject(j);
					op.addAgent(new OptimizerConfigurationPhaseAgent(  
							agentFreq.getString("name"),
							agentFreq.getInt("frequency")
					));
				}
				oc.addPhase(op);
			}
			
			return oc;
		}
		catch(Exception e) {
			e.printStackTrace();
			throw new Exception("Failed to read configuration!");
		}
	}
}
