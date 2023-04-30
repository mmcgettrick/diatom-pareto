package com.diatom.pareto;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicLong;


/**
 * The solution manager is the population of solutions created, modified, and removed by agents.
 * It supports a wide-range of helper utilities for extracting solutions by reference or copy and for generated
 * solution summaries for output purposes.
 * 
 * @author mmcgettrick
 
 */
public class SolutionManager {

	private static SolutionManager instance;
	
	/* private String serviceUrl; */
	private Random random;
	
	private long problemId;
	private long optimizerId;
	private AtomicLong nextId;
	private Map<SolutionKey, Solution> solutions;
	
	/**
	 * Constructor
	 */
	private SolutionManager() {
		// this.serviceUrl = "";
		this.random = new Random();
		this.problemId = 0;
		this.optimizerId = 0;
		this.nextId = new AtomicLong(0);
		this.solutions = Collections.synchronizedMap(new HashMap<SolutionKey, Solution>());
	}
	
	/**
	 * Get singleton instance
	 * @return
	 */
	public static SolutionManager getInstance() {
		if(instance==null) instance = new SolutionManager();
		return instance;
	}
	
	/**
	 * Get the next solution from the population - used for iteration.
	 * @return
	 */
	public SolutionKey nextSolutionId() {
		return new SolutionKey(this.problemId,this.optimizerId, nextId.addAndGet(1));
	}

	/*
	public String getServiceUrl() {
		return this.serviceUrl;
	}
	
	public void setServiceUrl(String serviceUrl) {
		this.serviceUrl = serviceUrl;
	}
	*/
	/**
	 * Add a solution to the population
	 * @param solution
	 */
	public synchronized void addSolution(Solution solution) {
		this.solutions.put(solution.getId(), solution);
	}
	
	/**
	 * Add a collection of solutions to the population
	 * @param solutions
	 */
	public synchronized void addSolutions(Collection<Solution> solutions) {
		for(Solution s : solutions) {
			this.addSolution(s);
		}
	}
	
	/**
	 * Return a copy of a randomly selected solution
	 * @return A random solution (copy) or NULL if population is empty
	 */
	public synchronized Solution getCloneOfRandomSolution() {
		List<SolutionKey> keys = new ArrayList<SolutionKey>(solutions.keySet());
		SolutionKey key = keys.get(random.nextInt(keys.size()));
		return (Solution) this.solutions.get(key).clone();
	}

	/**
	 * Get all solutions in the population
	 * @return
	 */
	public synchronized List<Solution> getSolutions() {
		return Collections.unmodifiableList(new ArrayList<Solution>(solutions.values()));
	}
	
	/**
	 * Get the size of the population
	 * @return
	 */
	public int numberOfSolutions() {
		return this.solutions.size();
	}
	
	/**
	 * Remove a specified solution from the population
	 * @param solution
	 */
	public synchronized void removeSolution(Solution solution) {
		this.solutions.remove(solution.getId());
	}
	
	/**
	 * Remove a collection of solutions from the population
	 * @param solutions
	 */
	public synchronized void removeSolutions(Collection<Solution> solutions) {
		for (Solution s : solutions) {
			this.removeSolution(s);
		}
	}
	
	/**
	 * Get the problem ID associated with all solutions in the population
	 * @return
	 */
	public long getProblemId() {
		return problemId;
	}

	/**
	 * The "optimal" solution is based on a sorting order defined in the optimizer's configuration file. 
	 * Specifying this sort criteria is optimal.  If not specified, this solution will return null.  It is used 
	 * as a conveniences for helping to monitor the progress of the optimizer, and for highlighting 
	 * a solution that may be of particular interest to a decision maker.
	 * @return
	 */
	public Solution getOptimalSolution() {
		List<String> optObjNames = Optimizer.getInstance().getOptimalSolutionObjectives();
		if(optObjNames.size()==0) return null;
		List<Solution> sortableSolutions = new ArrayList<Solution>(solutions.values());
		Collections.sort(sortableSolutions, new SolutionComparator(optObjNames));
		Solution s = sortableSolutions.iterator().next();
		return s;
	}

	public long getOptimizerId() {
		return optimizerId;
	}

	public void setProblemId(long problemId) {
		this.problemId = problemId;
	}

	public void setOptimizerId(long optimizerId) {
		this.optimizerId = optimizerId;
	}

	public void printAgentFrequencies() {
		Map<String,Integer> agentFrequencies = new HashMap<String,Integer>();
		for(Solution solution : this.solutions.values()) {
			List<String> afreq = solution.getAgentHistory();
			for(String agt : afreq) {
				int count = (agentFrequencies.containsKey(agt)) ? agentFrequencies.get(agt) : 0;
				count++;
				agentFrequencies.put(agt,count);
			}
		}
		for(String agt : agentFrequencies.keySet()) {
			System.out.println(agt + "\t" + agentFrequencies.get(agt));
		}
	}
	
	public void printSolutionSummaries() {
		StringBuffer header = new StringBuffer();
		header.append(String.format("%20s","Ident"));
		header.append("\tGen");
		for(String objective : Optimizer.getInstance().getObjectives().keySet()) {
			header.append("\t" + objective);
		}
		System.out.println(header);
		for(Solution solution : this.solutions.values()) {
			solution.printEvaluationSummary();
		}
	}
	
	/**
	 * Print the details of every solution
	 */
	public void printSolutions() {

		StringBuffer header = new StringBuffer();
		header.append(String.format("%20s","Ident"));
		header.append("\tGen");
		for(String objective : Optimizer.getInstance().getObjectives().keySet()) {
			header.append("\t" + objective);
		}

		for(Solution solution : this.solutions.values())
		{
			System.out.println("\n\n\n"+header);
			solution.printEvaluationSummary();
			solution.print();		
		}
	}
	
	/**
	 * Print the optimal solution
	 */
	public void printOptimalSolution()
	{
		Solution optimal = getOptimalSolution();
		if (optimal != null)
		{
			optimal.printEvaluationSummary();
			optimal.print();	
		}
	}
	
	
	public void storeSolutions() {
		//TODO do something!
		//System.out.println("storeSolutions() called - nothing has been implemented");
	}
	
	public void storeOptimalSolution() {
		//TODO do something!
		//System.out.println("storeOptimalSolution() called - nothing has been implmented");
	}
	
}
