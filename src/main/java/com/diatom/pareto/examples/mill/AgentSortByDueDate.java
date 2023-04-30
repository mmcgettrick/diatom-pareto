package com.diatom.pareto.examples.mill;

import com.diatom.pareto.Solution;
import com.diatom.pareto.WorkerAgent;

import java.util.*;

public class AgentSortByDueDate extends WorkerAgent
{

	public AgentSortByDueDate(Map<String, Object> options) {
		super(options);
		// TODO Auto-generated constructor stub
	}



	@Override
	protected Collection<Solution> runWithSolutions(Collection<Solution> selected) {
		OrderDueDateComparator comp = new OrderDueDateComparator();
		ProductionSchedule ps = (ProductionSchedule) selected.iterator().next();
		Set<Integer> ids = ps.getMachineIDs();
		for (Integer id : ids)
		{
			List<Order> schedule = ps.getMachine(id);
			Collections.sort(schedule, comp);
		}
		
		Collection<Solution> result = new HashSet<Solution>();
		result.add(ps);
		return result;
	}
}
