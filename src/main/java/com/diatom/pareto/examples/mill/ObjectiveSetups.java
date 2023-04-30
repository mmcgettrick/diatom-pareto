package com.diatom.pareto.examples.mill;

import com.diatom.pareto.Objective;
import com.diatom.pareto.Solution;

import java.util.List;
import java.util.Map;

/**
 * Minimize the number of machine setups.  A setup is incurred when the product code for order X
 * @see ObjectiveUnassignedOrders
 * @author John
 *
 */
public class ObjectiveSetups extends Objective {

	public ObjectiveSetups(Map<String, Object> options) {
		super(options);
		// TODO Auto-generated constructor stub
	}

	@Override
	public double evaluate(Solution solution) {

		ProductionSchedule ps = (ProductionSchedule) solution;
		
		int setups=0;
		for (Integer machineID : ps.getMachineIDs())
		{
			List<Order> currentMachine = ps.getMachine(machineID);
			int lastCode = -99;
			for (Order order : currentMachine)
			{
				int currentCode = order.getProductCode();
				if (currentCode != lastCode)
					setups++;
				lastCode = currentCode;
			}
		}
		return setups;
	}

}
