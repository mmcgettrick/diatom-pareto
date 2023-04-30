package com.diatom.pareto;

import java.util.*;

/**
 * A solution to an optimization problem defines a complete specification of
 * whatever it is you are trying to optimize - a schedule, route, or whatever. Solutions
 * are maintained by the SolutionManager and evaluated with respect to all objectives that
 * were registered with the optimizer.
 * 
 * @author mmcgettrick
 *
 */
public abstract class Solution implements Cloneable {

	protected SolutionKey id;
	protected Map<String,Double> evaluationMap;
	protected List<String> agentHistory;
	
	/**
	 * Constructor
	 */
	public Solution() {
		this.id = SolutionManager.getInstance().nextSolutionId();
		this.evaluationMap = new TreeMap<String,Double>();
		this.agentHistory = new ArrayList<String>();
	}
	
	/**
	 * Clone a solution. Subclasses will implement the abstract method doClone in order to 
	 * clone application-specific solution attributes.
	 */
	public final Solution clone() {
		try {
			// Clone local stuff
			Solution s = (Solution) super.clone();
			s.id = SolutionManager.getInstance().nextSolutionId();
			s.evaluationMap = new TreeMap<String,Double>();
			for(String key : this.evaluationMap.keySet()) {
				s.evaluationMap.put(key,new Double(this.evaluationMap.get(key)));
			}
			s.agentHistory = new ArrayList<String>(this.agentHistory);
			// Process subclass related cloning and return
			doClone(s);
			return s;
		}
		catch(CloneNotSupportedException e) {
			return null;
		}
	}
	
	/**
	 * Subclasses should override to handle cloning of implementation specific 
	 * data structures.
	 * @return
	 */
	protected abstract void doClone(Solution solution);
	
	/**
	 * Evaluate a solution with respect to all objectives registered with the optimizer.
	 */
	public final void evaluate() {
		Map<String,Objective> objectives = Optimizer.getInstance().getObjectives();
		if(objectives==null || objectives.size()==0) return;
		for(String objective : objectives.keySet()) {
			Objective o = objectives.get(objective);
			if (o == null)
				System.out.println("Null Objective!");
			double value = o.doEval(this);
			double tpow = Math.pow(10.0, o.getPrecision());
	        double adjustedValue = (double) ((int)(value * tpow + 0.5)) / tpow;
			this.setEvaluation(objective, adjustedValue);
		}
	}
	
	/**
	 * The solution keeps track of all agents that contributed to its construction.
	 * 
	 * @param agent The name of the agent that modified the solution.
	 */
	public void addToAgentHistory(String agent) {
		this.agentHistory.add(agent);
	}
	
	/**
	 * Get the list of agents (agent names) that contributed to the solution's construction
	 * @return
	 */
	public List<String> getAgentHistory() {
		return Collections.unmodifiableList(this.agentHistory);
	}
	
	/**
	 * Get the solution ID
	 * @return
	 */
	public SolutionKey getId() {
		return this.id;
	}
	
	
	/**
	 * Set the solution ID
	 * @param id The ID to be assigned to the solution
	 */
	public void setId(SolutionKey id) {
		this.id = id;
	}
	
	/**
	 * Get the evaluation for a specifically named objective
	 * @param objective The name of the objective
	 * @return
	 */
	public double getEvaluation(String objective) {
		return this.evaluationMap.get(objective);
	}
	
	/**
	 * Get the evaluation map (objectiveName -> Double)
	 * @return
	 */
	public Map<String,Double> getEvaluationMap() {
		return Collections.unmodifiableMap(this.evaluationMap);
	}
	
	/**
	 * Set the evaluation value for a specifically named objective
	 * @param objective The name of the objective
	 * @param value
	 */
	public void setEvaluation(String objective, double value) {
		this.evaluationMap.put(objective, value);
	}
	
	/**
	 * Read the JSONObject and configure the solution accordingly. 
	 * @param json
	 * @throws JSONException 
	 */
	/*
	public final void fromJSON(JSONObject json) throws JSONException {
		// Read all the stuff we keep track of
		JSONObject jsid = json.getJSONObject("id"); 
		SolutionKey id = new SolutionKey();
		id.readFromJSON(jsid);
		this.id = id;
		JSONObject jsEvalMap = json.getJSONObject("evaluationMap");
		this.evaluationMap = MooUtils.convertJSONToStringDoubleMap(jsEvalMap);
		JSONArray jsAgentHistory = json.getJSONArray("agentHistory");
		this.agentHistory = new ArrayList<String>();
		for(int i=0; i < jsAgentHistory.length(); i++) {
			this.agentHistory.add(jsAgentHistory.getString(i));
		}
		// Read the rest
		readFromJSON(json);
	}
	*/
	
	/**
	 * Write the solution to a JSONObject for storage. 
	 * @param json
	 */
	/*
	public final void toJSON(JSONObject json) throws JSONException {
		// Write all the stuff we keep track of
		JSONObject jsid = new JSONObject();
		this.id.writeToJSON(jsid);
		json.put("id",jsid);
		JSONObject jsEvalMap = MooUtils.convertStringDoubleMapToJSON(this.evaluationMap);
		json.put("evaluationMap",jsEvalMap);
		JSONArray jsAgentHistory = new JSONArray(this.agentHistory);
		json.put("agentHistory",jsAgentHistory);
		// Write the rest
		this.writeToJSON(json);
	}
	*/
	
	/**
	 * Subclasses should override to read additional implementation specific 
	 * data from the JSONObject.
	 * @param json
	 */
	/*
	public void readFromJSON(JSONObject json) throws JSONException {
		// Read all the stuff we keep track of
		JSONObject jsid = json.getJSONObject("id"); 
		SolutionKey id = new SolutionKey();
		id.readFromJSON(jsid);
		this.id = id;
		JSONObject jsEvalMap = json.getJSONObject("evaluationMap");
		this.evaluationMap = MooUtils.convertJSONToStringDoubleMap(jsEvalMap);
		JSONArray jsAgentHistory = json.getJSONArray("agentHistory");
		this.agentHistory = new ArrayList<String>();
		for(int i=0; i < jsAgentHistory.length(); i++) {
			this.agentHistory.add(jsAgentHistory.getString(i));
		}
	}
	*/
	
	/**
	 * Subclasses should override to write additional implementation specific 
	 * data to the JSONObject. 
	 * @param json
	 */
	/*
	public void writeToJSON(JSONObject json) throws JSONException {
		// Write all the stuff we keep track of
		JSONObject jsid = new JSONObject();
		this.id.writeToJSON(jsid);
		json.put("id",jsid);
		JSONObject jsEvalMap = MooUtils.convertStringDoubleMapToJSON(this.evaluationMap);
		json.put("evaluationMap",jsEvalMap);
		JSONArray jsAgentHistory = new JSONArray(this.agentHistory);
		json.put("agentHistory",jsAgentHistory);
	}
	*/
	
	/**
	 * Print's a one-line evaluation summary of the solution, respecting 
	 * the precision of each objective.
	 */
	public void printEvaluationSummary() {
		Map<String,Objective> omap = Optimizer.getInstance().getObjectives();
		StringBuffer summary = new StringBuffer();
		summary.append(String.format("%20s",this.id));
		summary.append("\t");
		summary.append(this.agentHistory.size());
		for(String objective : this.evaluationMap.keySet()) {
			Objective o = omap.get(objective);
			int precision = o.getPrecision();
			String numberFormat="%5."+precision+"f";
			if(precision > 0) {
				summary.append("\t" + String.format(numberFormat, this.evaluationMap.get(objective)));
			}
			else {
				summary.append("\t" + String.format("%5d",this.evaluationMap.get(objective).intValue()));
			}
				
		}
		System.out.println(summary);
	}
	
	/**
	 * Print the details of a solution (abstract method implemented by subclasses).
	 */
	protected abstract void print();

	/**
	 * Tests whether the solution dominates input solution. Solution A dominates Solution B
	 * if A is better than or equal to B with respect to every objective criteria,
	 * and strictly better with respect to at least one objective criteria.
	 * @param solution The input solution to be compared
	 * @return True if *this* solution dominates the solution passed to the function
	 */
	public boolean dominates(Solution solution) {
		// If each evaluation is equal or superior, then *this* dominates
		boolean superior = false;
		Map<String,Objective> objectiveMap = Optimizer.getInstance().getObjectives();
		for(String objective : evaluationMap.keySet()) {
			Objective o = objectiveMap.get(objective);
			if(o!=null && o.isActive()) {
				double target = o.getTarget();
				double evalA = Math.abs(target - this.getEvaluation(objective));
				double evalB = Math.abs(target - solution.getEvaluation(objective));
				if(evalA > evalB) return false;
				if(evalA < evalB) superior = true;
			}	
		}
		return (superior==true) ? true : false;
	}
	
	/**
	 * Tests whether the solution is a duplicate of the solution passed to the function.
	 * Two solutions A and B are considered duplicates if their evaluation criteria are all equal.
	 * 
	 * @param solution Solution to be compared
	 * @return True if duplicate, False otherwise.
	 */
	public boolean duplicates(Solution solution) {
		// If any of the evaluations do not match then it is not a duplicate
		for(String objective : evaluationMap.keySet()) {
			if(this.getEvaluation(objective)!=solution.getEvaluation(objective)) return false;
		}
		// If we get this far it must be a duplicate
		return true;
	}
	
}
