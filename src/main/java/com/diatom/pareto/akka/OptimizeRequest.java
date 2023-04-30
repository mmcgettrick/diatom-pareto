package com.diatom.pareto.akka;

/**
 * Created by rachlin on 7/29/16.
 */
public final class OptimizeRequest {

    private final String configuration;

    public OptimizeRequest(String configuration)
    {
        this.configuration = configuration;
    }

    public String getConfiguration() {
        return configuration;
    }

}
