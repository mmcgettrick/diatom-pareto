package com.diatom.pareto.akka;

/**
 * Created by rachlin on 7/29/16.
 */
public final class RunAgentResponse {

    private final String agentName;

    public RunAgentResponse(String agentName) {
        this.agentName = agentName;
    }

    public String getAgentName() {
        return agentName;
    }

}
