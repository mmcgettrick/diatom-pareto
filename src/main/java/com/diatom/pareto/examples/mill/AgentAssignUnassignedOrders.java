package com.diatom.pareto.examples.mill;

import com.diatom.pareto.Optimizer;
import com.diatom.pareto.Solution;
import com.diatom.pareto.WorkerAgent;

import java.util.Collection;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class AgentAssignUnassignedOrders extends WorkerAgent
{

	public AgentAssignUnassignedOrders(Map<String, Object> options) {
		super(options);
		// TODO Auto-generated constructor stub
	}



	@Override
	protected Collection<Solution> runWithSolutions(Collection<Solution> selected) {
		ProductionSchedule ps = (ProductionSchedule) selected.iterator().next();

		int numMachines = ps.getMachineIDs().size();
		Orderbook book = (Orderbook) (Optimizer.getInstance().getResource("book"));
		int N = book.size();
		
		Set<Order> assignedOrders = ps.getAssignedOrders();
		
		for (int i=0; i<N; i++)
		{
			Order order = book.getOrder(i);
			if (assignedOrders.contains(order)==false) // order is unassigned
			{
				int machineID = (int) (Math.random() * numMachines);
				ps.addOrder(machineID, order);
			}
		}
		
		Collection<Solution> result = new HashSet<Solution>();
		result.add(ps);
		return result;
	}
}
