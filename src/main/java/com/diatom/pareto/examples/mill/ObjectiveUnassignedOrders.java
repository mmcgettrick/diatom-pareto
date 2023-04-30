package com.diatom.pareto.examples.mill;

import com.diatom.pareto.Objective;
import com.diatom.pareto.Optimizer;
import com.diatom.pareto.Solution;

import java.util.Map;

/**
 * Minimize the number of orders not assigned to any machine
 * @author John
 *
 */
public class ObjectiveUnassignedOrders extends Objective {

	public ObjectiveUnassignedOrders(Map<String, Object> options) {
		super(options);
		// TODO Auto-generated constructor stub
	}

	@Override
	public double evaluate(Solution solution) {

		ProductionSchedule ps = (ProductionSchedule) solution;
		int assigned = ps.numAssignedOrders();
		Orderbook book = (Orderbook) Optimizer.getInstance().getResource("book");
		return book.size() - assigned;
	}

}
