package com.diatom.pareto.examples.mill;

import com.diatom.pareto.Solution;
import com.diatom.pareto.WorkerAgent;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

public class AgentMoveRandomOrder extends WorkerAgent
{

	public AgentMoveRandomOrder(Map<String, Object> options) {
		super(options);
		// TODO Auto-generated constructor stub
	}


	@Override
	protected Collection<Solution> runWithSolutions(Collection<Solution> selected) {
		ProductionSchedule ps = (ProductionSchedule) selected.iterator().next();
		int M = ps.getMachineIDs().size();
		int sourceMachine = (int) (Math.random() * M);
		int targetMachine = (int) (Math.random() * M);
		List<Order> scheduleSourceMachine = ps.getMachine(sourceMachine);
		int numOrders = scheduleSourceMachine.size();
		if (numOrders > 0)
		{
			int pick = (int) (Math.random() * numOrders);
			Order order = scheduleSourceMachine.get(pick);
			ps.removeOrder(order);
			ps.addOrder(targetMachine, order);
		}
		
		Collection<Solution> result = new HashSet<Solution>();
		result.add(ps);
		return result;
		
	}
}
