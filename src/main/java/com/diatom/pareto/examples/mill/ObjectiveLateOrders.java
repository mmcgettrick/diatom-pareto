package com.diatom.pareto.examples.mill;

import com.diatom.pareto.Objective;
import com.diatom.pareto.Optimizer;
import com.diatom.pareto.Solution;

import java.util.List;
import java.util.Map;

/**
 * Minimize the number of late orders.
 * Only orders that are assigned to a machine can be late.  If unassigned / unfulfilled
 * the order is not late.  
 * @see ObjectiveUnassignedOrders
 * @author John
 *
 */
public class ObjectiveLateOrders extends Objective {

	public ObjectiveLateOrders(Map<String, Object> options) {
		super(options);
		// TODO Auto-generated constructor stub
	}

	@Override
	public double evaluate(Solution solution) {

		ProductionSchedule ps = (ProductionSchedule) solution;
		int setupTime = ((MillConfiguration) (Optimizer.getInstance().getResource("mill"))).getIntProperty("setup_time");

		int late = 0;
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
				if (currentTime>order.getDueDate())
					late++;
				lastProductCode = order.getProductCode();
			}
		}
		return late;
	}

}
