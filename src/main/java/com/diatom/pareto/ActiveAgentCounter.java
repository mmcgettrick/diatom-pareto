package com.diatom.pareto;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by rachlin on 7/29/16.
 */
public class ActiveAgentCounter {
    private static ActiveAgentCounter counter;
    private AtomicInteger count;

    private ActiveAgentCounter() {
        count = new AtomicInteger(0);
    }

    public static ActiveAgentCounter getInstance()
    {
        if (counter==null)
            counter = new ActiveAgentCounter();
        return counter;
    }

    public int increment() { return count.incrementAndGet(); }
    public int decrement() { return count.decrementAndGet(); }
    public int get() { return count.get(); }

}
