package com.diatom.pareto;

import java.util.Map;

/**
 * An objective is a function f(Solution)->Double that quantifies some aspect of the solution.
 * Objectives are registered with the optimizer.  Any solution added to the SolutionManager is
 * automatically evaluted with respect to each registered objective. Objectives have a defined target value,
 * a precision that determines the number of decimal digit to display, and whether or not the objective is
 * "active" meaning that it factors into non-dominated set calculations. If an objective is not active, 
 * it will still be evaluated, but only for informational purposes, and will NOT factor into
 * comparisons that determine whether one solution dominates another.
 * 
 * @author mmcgettrick
 *
 */
public abstract class Objective {

	protected Map<String,Object> options;
    
	/**
	 * Constructor
	 * @param options key-value pairs used for configuring custom-objectives
	 */
	public Objective(Map<String,Object> options) {
		this.options = options;
	}

	/**
	 * Get the name of the objective
	 * @return
	 */
	public String getName() {
		return (String) this.options.get("name");
	}

	/**
	 * Get the numerical target of the objective. The optimizer tries to minimize the difference between
	 * the difference between the current objective value and the objective target.
	 * @return
	 */
	public Double getTarget() {
		return (Double) this.options.get("target");
	}
/**
 * Gete the precision of the objective - number of digits to store upon evaluation and to display
 * for printing purposes. By default, the precision of any objective is 2.
 * @return
 */
	public Integer getPrecision() {
		Integer precision = (Integer) this.options.get("precision");
		if (precision == null)
			return new Integer(2);
		else return precision;
	}

	/**
	 * Tests whether the objecive is currently active, i.e., factors into non-dominated set calculations.
	 * @return
	 */
	public Boolean isActive() {
		return (Boolean) this.options.get("active");
	}

	/**
	 * Set the objective target value
	 * @param target
	 */
	public void setTarget(Double target) {
		this.options.put("target",target);
	}

	/**
	 * Set the objective as active / inactive
	 * @param active True - set objective as active, False - Informational only.
	 */
	public void setActive(Boolean active) {
		this.options.put("active",active);
	}
	
	/**
	 * Get the named option
	 * @param key The name of the option
	 * @return The value of the option
	 */
	public Object getOption(String key) {
		return this.options.get(key);
	}
	
	/**
	 * Get all key-value option pairs.
	 * @return
	 */
	public Map<String,Object> getOptions() {
		return this.options;
	}
	
	@Override
	public String toString() {
		return "Objective [options=" + options + "]";
	}
	
	/**
	 * 
	 * @param solution
	 * @return
	 */
	public double doEval(Solution solution) {
		double result = this.evaluate(solution);
		return result;
	}

	/**
	 * Evaluate a solution with respect to this objective. 
	 * @param solution The solution to be evaluated.
	 * @return The evaluation result.
	 */
	public abstract double evaluate(Solution solution);
    
}
