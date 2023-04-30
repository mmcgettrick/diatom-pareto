package com.diatom.pareto.examples.wedding;

import com.diatom.pareto.Solution;
import com.diatom.pareto.WorkerAgent;
import com.diatom.pareto.ConfigurationManager;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

/**
 * Swap two guests chosen at random.
 * The swap only occurs if the guests are both parties of one OR if they are both in the same party
 * Give up after 10 tries
 */
public class AgentSwapTwoRandomGuests extends WorkerAgent
{

	public AgentSwapTwoRandomGuests(Map<String, Object> options) {
		super(options);
		// TODO Auto-generated constructor stub
	}


	@Override
	protected Collection<Solution> runWithSolutions(Collection<Solution> selected) {
		SeatingPlan sp = (SeatingPlan) selected.iterator().next();
		GuestBook book = (GuestBook) ConfigurationManager.getInstance().getResources().get("guestbook");
		List<String> guests = book.getGuestList();
		int N = guests.size();
		int tries = 0;

		boolean success = false;
		while (!success && tries<10) {

			String g1 = guests.get((int) (N * Math.random()));
			String g2 = guests.get((int) (N * Math.random()));
			success = sp.swapGuests(g1, g2, false);  // don't override party affiliation!
			tries++;
		}

		Collection<Solution> result = new HashSet<Solution>();
		result.add(sp);
		return result;
		
	}
}
