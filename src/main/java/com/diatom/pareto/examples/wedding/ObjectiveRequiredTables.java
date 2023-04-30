package com.diatom.pareto.examples.wedding;

import com.diatom.pareto.Objective;
import com.diatom.pareto.Solution;

import java.util.Map;

/**
 * Minimize the difference between the length of the longest and shortest machine schedule
 * @author John
 *
 */
public class ObjectiveRequiredTables extends Objective {

	public ObjectiveRequiredTables(Map<String, Object> options) {
		super(options);
		// TODO Auto-generated constructor stub
	}

	@Override
	public double evaluate(Solution solution) {

		int required = 0;
		SeatingPlan sp = (SeatingPlan) solution;
		for (int t=0; t<sp.maxTables; t++)
			if (!sp.isTableEmpty(t))
				required++;

		return (double) required;
	}

}
