package com.diatom.pareto.examples.wedding;

import com.diatom.pareto.Objective;
import com.diatom.pareto.Optimizer;
import com.diatom.pareto.Solution;

import java.util.Map;

/**
 * Maximize the percentage of adjacent guest pairs who have something in common.
 * They are either part of the same party or they have one or more category tags in common
 * @author John
 *
 */
public class ObjectiveSomethingInCommon extends Objective {

	public ObjectiveSomethingInCommon(Map<String, Object> options) {
		super(options);
		// TODO Auto-generated constructor stub
	}

	@Override
	public double evaluate(Solution solution) {


		int inCommon = 0;
		int pairs = 0;
		SeatingPlan sp = (SeatingPlan) solution;
		GuestBook book = (GuestBook) Optimizer.getInstance().getResource("guestbook");

		for (int t=0; t<sp.maxTables; t++)
		{
			if (!sp.isTableEmpty(t))
			{
				Guest gs = book.getGuest(sp.getOccupant(t,0));

				for (int s=0; s<sp.seatsPerTable; s++)
				{
					int n = (s+1) % sp.seatsPerTable;
					Guest gn = book.getGuest(sp.getOccupant(t,n));
					if (gs!=null && gn!=null)
					{
						if (gs.getParty().equalsIgnoreCase(gn.getParty()) || (book.areCompatible(gs.getName(), gn.getName())))
							inCommon++;

						pairs++;
					}
					gs = gn;
				}
			}
		}

		if (pairs>0)
		    return (double) inCommon / (double) pairs;
		else return 0.0;
	}

}
