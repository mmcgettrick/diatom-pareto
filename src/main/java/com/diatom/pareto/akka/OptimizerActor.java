package com.diatom.pareto.akka;

import akka.actor.ActorRef;
import akka.actor.Props;
import akka.actor.UntypedActor;
import akka.routing.RoundRobinPool;
import com.diatom.pareto.*;
import com.diatom.pareto.config.OptimizerConfiguration;
import com.diatom.pareto.config.OptimizerConfigurationItem;
import com.diatom.pareto.config.OptimizerConfigurationPhase;
import com.diatom.pareto.config.OptimizerConfigurationPhaseAgent;

import java.util.Date;

/**
 * Created by rachlin on 7/29/16.
 */
public class OptimizerActor extends UntypedActor {

    //TODO: Move these to configuration
    private long maxActiveAgentRunRequests = 2500;
    private int poolSize = 4;
    private int sleepPauseMS = 5;
    private int progressFrequency = 1000000;

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

    public void runOptimizer()
    {
        System.out.println(new Date().toString());

        ConfigurationManager manager = ConfigurationManager.getInstance();
        ActiveAgentCounter actives = ActiveAgentCounter.getInstance();
        //ActiveDestroyerAgentCounter activeDestroyers = ActiveDestroyerAgentCounter.getInstance();

        ActorRef agents = getContext().actorOf(new RoundRobinPool(poolSize).props(Props.create(AgentActor.class)), "agentRouter");

        //logger.info("Starting optimization");
        for (Phase phase : manager.getPhases())
        {
            //activeDestroyers.reset();
            //long startTime = System.currentTimeMillis();
            //logger.info("Starting "+phase.getName()+ " phase");
            int iterations = phase.getIterations();
            //int interval = phase.getSummaryInterval();

            int i = 0;
            while (i < iterations)
            {
                if (actives.get() < maxActiveAgentRunRequests) {

                    // run random agent
                    String agentName = phase.pickRandomAgent();
                    RunAgentRequest rar = new RunAgentRequest(phase.pickRandomAgent());
                    actives.increment();
                    agents.tell(rar, getSelf());
                    if (i % progressFrequency == 0)
                        System.out.println("Iteration: " + i + "\tnumActives: " + actives.get());
                    i++;

                }
                else try {
                    Thread.sleep(sleepPauseMS);
                }
                catch (Exception e) { }

                /*
                // Print summary at regular interval
                if ((interval != 0) && ((i+1) % interval == 0))
                {
                    agents.get("duplicates").run();
                    agents.get("dominated").run();
                    agents.get("filtered").run();

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
                */
            }
        }

        /*
        // Clean up solutions
        agents.get("duplicates").run();
        agents.get("dominated").run();
        agents.get("filtered").run();

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


        //logger.info("End: Run");
        */
        System.out.println(new Date().toString());
    }

    public void onReceive(Object msg) throws Throwable {

        System.out.println(msg.toString());

        if (msg instanceof OptimizeRequest) {
            OptimizeRequest or = (OptimizeRequest) msg;
            OptimizerConfiguration oc = OptimizerConfiguration.readFromString(or.getConfiguration());
            initializeOptimizer(oc);
            runOptimizer();

            //TODO: We need a better way of terminating
            System.out.println("Sleeping.");
            Thread.sleep(5000);
            System.out.println("Shutting down.");
            getContext().system().terminate();
        } else {
            unhandled(msg);
        }

    }

}

