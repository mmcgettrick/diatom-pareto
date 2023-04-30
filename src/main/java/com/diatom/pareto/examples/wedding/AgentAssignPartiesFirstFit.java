package com.diatom.pareto.examples.wedding;

import com.diatom.pareto.CreatorAgent;
import com.diatom.pareto.Solution;
import com.diatom.pareto.ConfigurationManager;

import java.util.*;

public class AgentAssignPartiesFirstFit extends CreatorAgent
{

	public AgentAssignPartiesFirstFit(Map<String, Object> options) {
		super(options);
		// TODO Auto-generated constructor stub
	}


	@Override
	protected Collection<Solution> createNewSolutions() {

		// get guestbook
		GuestBook book = (GuestBook) ConfigurationManager.getInstance().getResources().get("guestbook");

		// get seating configuration
		SeatingConfiguration config = (SeatingConfiguration) (ConfigurationManager.getInstance().getResources().get("config"));


		SeatingPlan sp = new SeatingPlan(book, config);

		List<String> parties = new ArrayList<String>(book.getParties());
		parties = ListUtils.shuffle(parties);

		for (String party : parties)
		{
			int size = book.getParty(party).size();
			int table = sp.firstAvailableTable(size);
			if (table != -1)
				sp.assignParty(party, table);
		}
		
		Collection<Solution> result = new HashSet<Solution>();
		result.add(sp);
		return result;
	}

}
