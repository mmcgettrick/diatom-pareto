package com.diatom.pareto.examples.mill;

import com.diatom.pareto.Objective;
import com.diatom.pareto.Optimizer;
import com.diatom.pareto.Solution;

import java.util.List;
import java.util.Map;

/**
 * Minimize the number of late HIGH PRIORITY orders.
 * Only orders that are assigned to a machine can be late.  If unassigned / unfulfilled
 * the order is not late.  
 * @see ObjectiveUnassignedOrders
 * @author John
 *
 */
public class ObjectiveLateHighPriorityOrders extends Objective {

	public ObjectiveLateHighPriorityOrders(Map<String, Object> options) {
		super(options);
	}

	@Override
	public double evaluate(Solution solution) {

		ProductionSchedule ps = (ProductionSchedule) solution;
		
		int late = 0;
		int setupTime = ((MillConfiguration) (Optimizer.getInstance().getResource("mill"))).getIntProperty("setup_time");
		for (Integer machineID : ps.getMachineIDs())
		{
			List<Order> currentMachine = ps.getMachine(machineID);
			int currentTime = 0;
			int lastProductCode = -99;
			for (Order order : currentMachine)
			{
				if (order.getProductCode() != lastProductCode)
					currentTime += setupTime;
				currentTime += order.getProductionCycles();
				if ((currentTime>order.getDueDate()) && (order.isHighPriority()))
					late++;
				lastProductCode = order.getProductCode();
			}
		}
		return late;
	}

}
