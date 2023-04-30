package com.diatom.pareto.examples.mill;

import com.diatom.pareto.Solution;
import com.diatom.pareto.WorkerAgent;

import java.util.Collection;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class AgentAdvanceLateOrders extends WorkerAgent
{

	public AgentAdvanceLateOrders(Map<String, Object> options) {
		super(options);
	}



	@Override
	protected Collection<Solution> runWithSolutions(Collection <Solution> selected) {
		
		// Get the one and only selected solution
		// This agent is using the default solution selector "RandomSolutionSelector"
		// which returns a clone of some randomly chosen solution in the population
		ProductionSchedule ps = (ProductionSchedule) selected.iterator().next();
		
		// Find all late orders
		Set<Order> lateOrders = ps.getLateOrders();
		
		// For each late order, move it up in the schedule
		for (Order order : lateOrders)
			ps.advanceOrder(order);

		// Return the modified solution in a collection
		Collection<Solution> result = new HashSet<Solution>();
		result.add(ps);
		return result;
	}
}
