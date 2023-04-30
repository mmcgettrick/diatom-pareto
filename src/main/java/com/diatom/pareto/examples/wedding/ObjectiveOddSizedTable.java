package com.diatom.pareto.examples.wedding;

import com.diatom.pareto.Objective;
import com.diatom.pareto.Solution;

import java.util.Map;

/**
 * Minimize the number of tables having an odd-number of occupants
 * @author John
 *
 */
public class ObjectiveOddSizedTable extends Objective {

	public ObjectiveOddSizedTable(Map<String, Object> options) {
		super(options);
		// TODO Auto-generated constructor stub
	}

	@Override
	public double evaluate(Solution solution) {


		int odds = 0;
		SeatingPlan sp = (SeatingPlan) solution;
		for (int t=0; t<sp.maxTables; t++)
		{
			if (sp.filledSeats(t) % 2 == 1)
				odds++;
		}
		return (double) odds;
	}

}
