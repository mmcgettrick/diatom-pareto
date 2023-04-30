package com.diatom.pareto;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.*;

/**
 * This solution selects all solutions whose objective values fail to meet the objective-by-objective 
 * list of value cutoffs defined in a filter cutoff file, e.g., conf/filters.cfg.   Each time an agent using this selector is activated
 * it re-reads the filter file to obtain the latest cut-offs.  A user can make changes to this filter
 * file while the optimizer is running in order to interactively guide the population towards 
 * candidate solutions that more effectively address the most important criteria of the decision-maker. 
 * For example, although the system will find tradeoffs between two objectives, V and W, it may be
 * that the decision maker does not want to even consider solutions where W>x - so this selector will identify
 * solutions where W > x and, when used in conjunction with a destroyer, will remove ALL solutions from
 * the populations that fail to meet one or more objective value cutoffs defined in the filter file.
 * 
 * @see DefaultDestroyerAgent
 * @author mmcgettrick
 *
 */
public class FilteredSolutionSelector extends SolutionSelector {


	Map<String,Double> filters;
	
	/**
	 * Constructor
	 * @param options key-value pairs of options. One of the options specified should be named
	 * FilteredSolutionSelector (key) with value corresponding to the name of the filter cutoff file.
	 */
	public FilteredSolutionSelector(Map<String, Object> options) {
		super(options);
		this.options.put("name","FilteredSolutionSelector");
        filters = new HashMap<String,Double>();
	}

	/**
	 * Reread the filter file to obtain the latest objective value cutoffs.
	 */
    public void updateFilters() {
    	try {
    		filters.clear();
    		BufferedReader reader = new BufferedReader(new FileReader((String)this.getOption("filterFile")));
    		for(String line = reader.readLine(); line!=null; line = reader.readLine()) {
    			if(line.length()>0 && line.contains("=")) {
    				String[] fields = line.split("=");
    				filters.put(fields[0],Double.parseDouble(fields[1]));
    			}
    		}
    		reader.close();
    	}
    	catch(Exception e) {
    		return;
    	}
    }	
	
	/**
	 * Returns all solutions that fail to meet one or more objective value cutoffs.
	 */
	public Collection<Solution> select(SolutionManager sm) {
		
		// First make sure filters are up to date
		updateFilters();
		
		// Get the objectives and solutions
    	Map<String,Objective> objectives = Optimizer.getInstance().getObjectives();
    	List<Solution> solutions = sm.getSolutions();
    	
    	// If there are no solutions return
    	if(solutions==null||solutions.size()==0) Collections.emptyList();
    	
    	// Set of Solutions to remove
    	Set<Solution> remove = new HashSet<Solution>();
    	
    	// Filter by each objective (order doens't matter)
    	for(Objective objective : objectives.values()) {
    		String name = objective.getName();
    		if(this.filters.containsKey(name)) {
    			double limit = this.filters.get(name);
    			double target = objective.getTarget();
    			
    			for(Solution s : solutions) {
    				double eval = s.getEvaluation(name);
    				if(((limit < target) && (eval <= limit)) ||
    				   ((limit > target) && (eval >= limit)) ||
    				   ((limit == target) && (eval != limit))) {
    					remove.add(s);
    				}
    			}
    		}
    	}
    	return remove;
	}
}
