package com.diatom.pareto.examples.wedding;

import com.diatom.pareto.Objective;
import com.diatom.pareto.Solution;

import java.util.Map;

/**
 * Minimize the number of table violations.  A table violation occurs when guest A isn't supposted to be at the same table as guest B
 * @author John
 *
 */
public class ObjectiveTableViolations extends Objective {

	public ObjectiveTableViolations(Map<String, Object> options) {
		super(options);
		// TODO Auto-generated constructor stub
	}

	@Override
	public double evaluate(Solution solution) {


		int violations = 0;
		SeatingPlan sp = (SeatingPlan) solution;
		for (int t=0; t<sp.maxTables; t++)
			violations += sp.tableViolations(t);

		return (double) violations;
	}

}
