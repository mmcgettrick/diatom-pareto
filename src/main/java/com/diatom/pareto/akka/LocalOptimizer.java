package com.diatom.pareto.akka;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;

import java.nio.file.Files;
import java.nio.file.Paths;


/**
 * Created by rachlin on 7/29/16.
 */
public class LocalOptimizer {

    public static void main(String[] args) throws Exception
    {
        if (args.length != 1)
        {
            System.err.println("USAGE: LocalOptimizer <config>");
            System.exit(1);
        }

        // Parse input parameters
        String configFile = args[0];

        // Read configuration
        String configuration = new String(Files.readAllBytes(Paths.get(configFile)));
        OptimizeRequest request = new OptimizeRequest(configuration);


        // create actor system
        ActorSystem system = ActorSystem.create("localOptimizer");
        Props props = Props.create(OptimizerActor.class);
        ActorRef optimizerActor = system.actorOf(props, "optimizerActor");

        // Call the actor
        optimizerActor.tell(request, null);
    }

}
