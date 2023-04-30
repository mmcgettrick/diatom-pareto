package com.diatom.pareto.examples.mill;

import com.diatom.pareto.CreatorAgent;
import com.diatom.pareto.Optimizer;
import com.diatom.pareto.Solution;

import java.util.Collection;
import java.util.HashSet;
import java.util.Map;

public class AgentAssignOrdersRandomly extends CreatorAgent
{

	public AgentAssignOrdersRandomly(Map<String, Object> options) {
		super(options);
		// TODO Auto-generated constructor stub
	}


	@Override
	protected Collection<Solution> createNewSolutions() {
		// get orderbook
		Orderbook book = (Orderbook) (Optimizer.getInstance().getResource("book"));
		int numMachines = ((MillConfiguration) (Optimizer.getInstance().getResource("mill"))).getIntProperty("machines");
		int N = book.size();
		ProductionSchedule ps = new ProductionSchedule();
		
		// shuffle orders
		int[] pick = new int[N];
		for (int i=0; i<N; i++)
			pick[i] = i;
		
		for (int i=0; i<N; i++)
		{
			int choose = (int) (Math.random() * N);
			int temp = pick[i];
			pick[i] = pick[choose];
			pick[choose] = temp;
		}
		
		// For each order, assign to a random machine
		for (int i=0; i<N; i++)
		{
			Order order = book.getOrder(pick[i]);
			int machineID = (int) (Math.random() * numMachines);
			ps.addOrder(machineID, order);
		}
		
		Collection<Solution> result = new HashSet<Solution>();
		result.add(ps);
		return result;
	}

}
