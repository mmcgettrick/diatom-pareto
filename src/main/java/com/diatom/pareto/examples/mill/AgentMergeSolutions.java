package com.diatom.pareto.examples.mill;

import com.diatom.pareto.Optimizer;
import com.diatom.pareto.Solution;
import com.diatom.pareto.WorkerAgent;

import java.util.*;


/**
 * Merge two solutions. Two solutions are randomly chosen from the population. 
 * For each order, we randomly choose one of the solutions and assign the order
 * to whatever machine that solution assigns the order to. After all orders are assigned,
 * each machine is sorted by order due date.
 * 
 * @author John Rachlin
 *
 */
public class AgentMergeSolutions extends WorkerAgent
{

	public AgentMergeSolutions(Map<String, Object> options) {
		super(options);
		// TODO Auto-generated constructor stub
	}



	@Override
	protected Collection<Solution> runWithSolutions(Collection<Solution> selected) {
		
		List<Solution> selectedList = new ArrayList<Solution>(selected);
		OrderDueDateComparator comp = new OrderDueDateComparator();
		ProductionSchedule ps = (ProductionSchedule) selected.iterator().next();
		Orderbook book = (Orderbook) (Optimizer.getInstance().getResource("book"));
		Set<Integer> ids = ps.getMachineIDs();
		
		// for each order...
		for (int i=0; i<book.size(); i++)
		{
			Order order = book.getOrder(i);
			int solutionPick = (int) (Math.random() * 2);
			ProductionSchedule picked = (ProductionSchedule) selectedList.get(solutionPick);
			Integer machineID = picked.getAssignedMachine(order);
			ps.addOrder(machineID, order);
		}
		
		
		// sort the machines
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
