package com.diatom.pareto.akka;

/**
 * Created by rachlin on 7/29/16.
 */
public final class RunAgentRequest {

    private final String agentName;

    public RunAgentRequest(String agentName) {
        this.agentName = agentName;
    }

    public String getAgentName() {
        return agentName;
    }

}
