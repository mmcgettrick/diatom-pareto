package com.diatom.pareto.akka;

import akka.actor.UntypedActor;
import com.diatom.pareto.ActiveAgentCounter;
import com.diatom.pareto.ActiveDestroyerAgentCounter;
import com.diatom.pareto.Agent;
import com.diatom.pareto.ConfigurationManager;

/**
 * Created by rachlin on 7/29/16.
 */
public class AgentActor extends UntypedActor {

    @Override
    public void onReceive(Object msg) throws Throwable {
        if (msg instanceof RunAgentRequest) {
            RunAgentRequest rar = (RunAgentRequest) msg;

            String agentName = rar.getAgentName();
            Agent agent = ConfigurationManager.getInstance().getAgents().get(agentName);
            agent.run();
            ActiveAgentCounter.getInstance().decrement();

        } else {
            unhandled(msg);
        }
    }

}
