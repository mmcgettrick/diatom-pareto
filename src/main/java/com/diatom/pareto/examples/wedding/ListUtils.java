package com.diatom.pareto.examples.wedding;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Created by rachlin on 4/8/16.
 */

/**
 * Shuffle a collection of strings for processing in some order
 */
public class ListUtils {
    public static List<String> shuffle(Collection<String> col)
    {
        List<String> lst = new ArrayList<String>(col);

        int N = lst.size();
        for (int i=0; i<N; i++)
        {
            int pick = (int) (Math.random() * N);
            String temp = lst.get(pick);
            lst.set(pick, lst.get(i));
            lst.set(i, temp);
        }
        return lst;
    }
}
