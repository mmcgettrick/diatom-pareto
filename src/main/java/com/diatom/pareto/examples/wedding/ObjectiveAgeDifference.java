package com.diatom.pareto.examples.wedding;

import com.diatom.pareto.Objective;
import com.diatom.pareto.Optimizer;
import com.diatom.pareto.Solution;

import java.util.Map;

/**
 * Minimize the age difference between people sitting next to each other and belonging to different parties
 * @author John
 *
 */
public class ObjectiveAgeDifference extends Objective {

	public ObjectiveAgeDifference(Map<String, Object> options) {
		super(options);
		// TODO Auto-generated constructor stub
	}

	@Override
	public double evaluate(Solution solution) {


		int diff = 0;
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
					if (gs!=null && gn!=null && !gs.getParty().equalsIgnoreCase(gn.getParty()))
					{
						diff += Math.abs(gs.getAge() - gn.getAge());
						pairs++;
					}
					gs = gn;
				}
			}
		}

		if (pairs>0)
		    return (double) diff / (double) pairs;
		else return 0.0;
	}

}
