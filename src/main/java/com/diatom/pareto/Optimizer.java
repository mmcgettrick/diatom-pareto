package com.diatom.pareto;

import com.diatom.pareto.config.OptimizerConfiguration;
import com.diatom.pareto.config.OptimizerConfigurationItem;
import com.diatom.pareto.config.OptimizerConfigurationPhase;
import com.diatom.pareto.config.OptimizerConfigurationPhaseAgent;
import com.diatom.pareto.license.LicenseUtils;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

import java.io.File;
import java.util.*;

/**
 * The optimizer is the main application class of the Pareto application framework. It brings
 * together agents (including agent activators and solution selectors), solution objectives, and
 * supporting resources. The optimizer also defines a series of execution phases that define how frequently
 * each agent will be handed control, and the total number of iterations (agent invocations) for each phase.
 * 
 * @see Agent
 * @see Resource
 * @see Objective
 * @see AgentActivator
 * @see SolutionSelector
 * @author mmcgettrick
 *
 */
public class Optimizer {

	private static Optimizer instance;
	
	private Logger logger;
	private OptimizerConfiguration configuration;

	/**
	 * Contructor - produces an empty (unconfigured) optimizer
	 */
	protected Optimizer() {
		PropertyConfigurator.configure("conf/logging.properties");
		this.logger = Logger.getLogger("Optimizer");
	}
	
	/**
	 * Get the optimizer singlton instances
	 * @return
	 */
	public static Optimizer getInstance() {
		if(instance==null) instance = new Optimizer();
		return instance;
	}

    private void initializeOptimizer(OptimizerConfiguration oc) throws Exception
    {
        // Get a handle to the ConfigurationManager
        ConfigurationManager manager = ConfigurationManager.getInstance();

        // Resources
        for(OptimizerConfigurationItem resource : oc.getResources()) {
            Resource r = (Resource) resource.construct();
            manager.getResources().put(r.getName(), r);
        }

        // Activators
        for(OptimizerConfigurationItem activator : oc.getActivators()) {
            AgentActivator a = (AgentActivator) activator.construct();
            manager.getActivators().put(a.getName(), a);
            //this.logger.info("Loaded " + a);
        }

        // Selectors
        for(OptimizerConfigurationItem selector : oc.getSelectors()) {
            SolutionSelector s = (SolutionSelector) selector.construct();
            manager.getSelectors().put(s.getName(), s);
            //this.logger.info("Loaded " + s);
        }

        // Objectives
        for(OptimizerConfigurationItem objective : oc.getObjectives()) {
            Objective o = (Objective) objective.construct();
            manager.getObjectives().put(o.getName(),o);
            //this.logger.info("Loaded " + o);
        }

        // Agents
        for(OptimizerConfigurationItem agent : oc.getAgents()) {
            Agent a = (Agent) agent.construct();
            manager.getAgents().put(a.getName(),a);
            //this.logger.info("Loaded " + a);
        }

        // Load Optimal Solution config
        if(!oc.getOptimalSolution().isEmpty()) {
            for(String objective : oc.getOptimalSolution()) {
                manager.getOptimalSolutionObjectives().add(objective);
            }
            //logger.info("Optimal Solution: " + this.optimalSolutionObjectives);
        }

        // Load Phases
        for(OptimizerConfigurationPhase op : oc.getPhases()) {
            Phase phase = new Phase();
            phase.setName(op.getName());
            phase.setIterations(op.getIterations());
            phase.setSummaryInterval(op.getSummaryInterval());
            for(OptimizerConfigurationPhaseAgent opa : op.getAgents()) {
                phase.addFrequency(opa.getName(),opa.getFrequency());
            }
            manager.getPhases().add(phase);
        }
    }
	
	/**
	 * Load the JSON-formated configuration file.
	 * @param file
	 * @throws Exception
	 */
	public void loadConfiguration(File file) throws Exception {
		
		this.logger.info("### Load Configuration ###");
		OptimizerConfiguration oc = OptimizerConfiguration.readFromFile(file);
		this.logger.info("### Configuration Loaded ###");
		
		/// Parameters ///
		SolutionManager sm = SolutionManager.getInstance();
		SolutionManager.getInstance().setOptimizerId(oc.getOptimizerId());
		this.logger.info("Problem Id: " + sm.getProblemId());
		this.logger.info("Optimizer Id: " + sm.getOptimizerId());
		this.logger.info("License Path: " + oc.getLicensePath());
		
		initializeOptimizer(oc);
		
		this.configuration = oc;
		
		this.logger.info("### End Optimizer Configuration ###");
	}
	
	/**
	 * Return the optimization coonfiguration
	 * @return
	 */
	public OptimizerConfiguration getConfiguration() {
		return configuration;
	}
		
	/**
	 * Get a named resource
	 * @param name
	 * @return
	 */
	public Resource getResource(String name) {
		return ConfigurationManager.getInstance().getResources().get(name);
	}
	
	/**
	 * get a named agent activator
	 * @param name
	 * @return
	 */
	public AgentActivator getActivator(String name) {
		return ConfigurationManager.getInstance().getActivators().get(name);
	}
	
	/**
	 * Get a named solution selector
	 * @param name
	 * @return
	 */
	public SolutionSelector getSelector(String name) {
		return ConfigurationManager.getInstance().getSelectors().get(name);
	}
	
	
	
	/**
	 * Returns a (Name -> Agent) Map of all agents registered with this Optimizer.
	 * @return Name to Agent Map.
	 */
	public Map<String,Agent> getAgents() {
		return Collections.unmodifiableMap(ConfigurationManager.getInstance().getAgents());
	}
	
	/**
	 * Returns the Logger.
	 * @return The Logger.
	 */
	public Logger getLogger() {
		return this.logger;
	}
		
	/**
	 * Returns a random Agent, based on the frequencies specified in the configuration
	 * file for the given phase.  
	 * @param phase The phase from which to return a random agent.
	 * @return An Agent.
	 */
	public Agent getRandomAgentForPhase(Phase phase) {
		
		String agentName = phase.pickRandomAgent();
		return ConfigurationManager.getInstance().getAgents().get(agentName);
	}
	
	/**
	 * Returns a (Name -> Objective) Map of all objectives registered with this Optimizer.
	 * @return Name to Objective Map.
	 */
	public Map<String,Objective> getObjectives() {
		return Collections.unmodifiableMap(ConfigurationManager.getInstance().getObjectives());
	}
	
	/**
	 * Returns the ordered list of objective names to be used when determining the 
	 * optimal solution. These values are configured in the <code>optimalSolution</code> 
	 * section of the configuration file. 
	 * @return The ordered list of objective names, or null if no optimalSolution was defined.
	 */
	public List<String> getOptimalSolutionObjectives() {
		return ConfigurationManager.getInstance().getOptimalSolutionObjectives();
	}
	
	/**
	 * Run the optimizer!
	 */
	protected void run() 
	{
		
		logger.info("Starting optimization");
		for (Phase phase : ConfigurationManager.getInstance().getPhases())
		{
			long startTime = System.currentTimeMillis();
			logger.info("Starting "+phase.getName()+ " phase");
			int iterations = phase.getIterations();
			int interval = phase.getSummaryInterval();
			for (int i=0; i<iterations; i++)
			{
				// Check for kill switch
	        	boolean exists = (new File("killfile.txt")).exists();
	            if(exists) {
	            	this.logger.info("Exiting because killfile.txt was detected.");
	            	break;
	            }
				
				// run random agent
				Agent agent = getRandomAgentForPhase(phase);
				agent.run();
				
				
				// Print summary at regular interval
				if ((interval != 0) && ((i+1) % interval == 0))
				{
                    ConfigurationManager.getInstance().getAgents().get("duplicates").run();
                    ConfigurationManager.getInstance().getAgents().get("dominated").run();
                    ConfigurationManager.getInstance().getAgents().get("filtered").run();
					
					System.out.println("\n\nCurrent Phase      : "+phase.getName());
	                System.out.println("Current Invocation : " + (i+1));
	                System.out.println("Population Size    : " + SolutionManager.getInstance().numberOfSolutions());
	                SolutionManager.getInstance().printSolutionSummaries();
	                System.out.println();
	               
	        	    //TODO temporary work-around for lack of DB support
	        	    //TODO store the population in the cloud
	        	    SolutionManager.getInstance().storeSolutions();
	        	    
	        	    Solution solution = SolutionManager.getInstance().getOptimalSolution();
	        	    long currentTime = System.currentTimeMillis();
	        	    long elapsed = (currentTime - startTime) / 1000;
	        	    long mm = elapsed / 60;
	        	    long ss = elapsed % 60;
	        	    if(solution!=null) {
	        	    	// solution.print();
	        	    	System.out.println("Optimal:\t" + mm + "m " + ss + "s\t" + (i+1));
	        	    	solution.printEvaluationSummary();
	        	    	
	        	    }
				}
			}
		}
		
		

		
		// Clean up solutions
        ConfigurationManager.getInstance().getAgents().get("duplicates").run();
        ConfigurationManager.getInstance().getAgents().get("dominated").run();
        ConfigurationManager.getInstance().getAgents().get("filtered").run();
		
		// Print final results
		System.out.println("\nFinal Results");
		System.out.println("Population Size    : " + SolutionManager.getInstance().numberOfSolutions());
	    SolutionManager.getInstance().printSolutionSummaries();
	    
	    System.out.println("\nAgent Frequencies");
	    SolutionManager.getInstance().printAgentFrequencies();

	    System.out.println("\nDetailed Solutions\n");
		SolutionManager.getInstance().printSolutions();

	    System.out.println("\nOPTIMAL SOLUTION\n");
		SolutionManager.getInstance().printOptimalSolution();
		
	    //TODO temporary work-around for lack of DB support
	    //TODO store the population in the cloud
	    SolutionManager.getInstance().storeSolutions();
	    
	    
		this.logger.info("End: Run");
		
	}
	
	/**
	 * Executes the Optimizer.
	 * args[0] - path/to/config.json
	 * @throws Exception
	 */
	public static void main(String[] args) throws Exception {
		try {
			if(args.length==0) { 
				System.out.println("Usage: runOptimizer path/to/config.json");
			}
			else {
				// Load Configuration
				Optimizer optimizer = Optimizer.getInstance();
				optimizer.loadConfiguration(new File(args[0]));
								
				// Check license
				String licensePath = optimizer.getConfiguration().getLicensePath();
				if(licensePath.length()>0) licensePath += File.pathSeparator;
				if(!LicenseUtils.isValidKey(new File(licensePath + "pareto.key"))) System.exit(0);
								
				// Go!
				optimizer.run();
				//SolutionManager.getInstance().storeOptimalSolution();
			}   
		}
		catch(Exception e) {
			e.printStackTrace();
		}
	}
	
}
