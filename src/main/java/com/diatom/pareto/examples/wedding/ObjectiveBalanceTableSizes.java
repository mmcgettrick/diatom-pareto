package com.diatom.pareto.examples.wedding;

import com.diatom.pareto.Objective;
import com.diatom.pareto.Solution;

import java.util.Map;

/**
 * Minimize the average number of empty seats across tables having at least one empty seat
 * @author John Rachlin
 *
 */
public class ObjectiveBalanceTableSizes extends Objective {

	public ObjectiveBalanceTableSizes(Map<String, Object> options) {
		super(options);
		// TODO Auto-generated constructor stub
	}

	@Override
	public double evaluate(Solution solution) {

		int partialTables = 0;
		int emptyChairs = 0;
		SeatingPlan sp = (SeatingPlan) solution;
		for (int t=0; t<sp.maxTables; t++)
			if (!sp.isTableEmpty(t) && !sp.isTableFull(t))
			{
				partialTables++;
				emptyChairs += sp.emptySeats(t);
			}

		if (partialTables == 0) return 0.0;
		else return (double) emptyChairs / (double) partialTables;
	}

}
