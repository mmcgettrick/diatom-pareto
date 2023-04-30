package com.diatom.pareto.examples.wedding;

import com.diatom.pareto.Objective;
import com.diatom.pareto.Solution;

import java.util.Map;

/**
 * Minimize the number of empty seats in the smallest table
 * @author John
 *
 */
public class ObjectiveSmallestTable extends Objective {

	public ObjectiveSmallestTable(Map<String, Object> options) {
		super(options);
		// TODO Auto-generated constructor stub
	}

	@Override
	public double evaluate(Solution solution) {


		int emptyMax = 0;
		SeatingPlan sp = (SeatingPlan) solution;
		for (int t=0; t<sp.maxTables; t++)
		{
			if (sp.isTableEmpty(t)==false) {
				int empty = sp.emptySeats(t);
				if (empty > emptyMax)
					emptyMax = empty;
			}
		}
		return (double) emptyMax;
	}

}
