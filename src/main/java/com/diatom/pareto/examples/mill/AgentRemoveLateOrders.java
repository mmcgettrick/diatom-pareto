package com.diatom.pareto.examples.mill;

import com.diatom.pareto.Solution;
import com.diatom.pareto.WorkerAgent;

import java.util.Collection;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class AgentRemoveLateOrders extends WorkerAgent
{

	public AgentRemoveLateOrders(Map<String, Object> options) {
		super(options);
		// TODO Auto-generated constructor stub
	}



	@Override
	protected Collection<Solution> runWithSolutions(Collection<Solution> selected) {
		ProductionSchedule ps = (ProductionSchedule) selected.iterator().next();
		Set<Order> lateOrders = ps.getLateOrders();
		
		for (Order order : lateOrders)
			ps.removeOrder(order);
		
		Collection<Solution> result = new HashSet<Solution>();
		result.add(ps);
		return result;
	}
}
