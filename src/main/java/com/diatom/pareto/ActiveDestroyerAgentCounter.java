package com.diatom.pareto;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by rachlin on 7/29/16.
 */
public class ActiveDestroyerAgentCounter {
    private static ActiveDestroyerAgentCounter counter;
    private AtomicInteger count;

    private ActiveDestroyerAgentCounter() {
        count = new AtomicInteger(0);
    }

    public static ActiveDestroyerAgentCounter getInstance()
    {
        if (counter==null)
            counter = new ActiveDestroyerAgentCounter();
        return counter;
    }

    public int increment() { return count.incrementAndGet(); }
    public int decrement() { return count.decrementAndGet(); }
    public int get() { return count.get(); }
    public void reset() { count.set(0); }

}
