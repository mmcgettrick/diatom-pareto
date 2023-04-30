package com.diatom.pareto.examples.wedding;

import com.diatom.pareto.Objective;
import com.diatom.pareto.Optimizer;
import com.diatom.pareto.Solution;

import java.util.Map;

/**
 * The average age range across all non-empty tables
 * @author John
 *
 */
public class ObjectiveAverageAgeRange extends Objective {

	public ObjectiveAverageAgeRange(Map<String, Object> options) {
		super(options);
		// TODO Auto-generated constructor stub
	}

	@Override
	public double evaluate(Solution solution) {


		int range = 0;
		int tables = 0;
		SeatingPlan sp = (SeatingPlan) solution;
		GuestBook book = (GuestBook) Optimizer.getInstance().getResource("guestbook");

		for (int t=0; t<sp.maxTables; t++)
		{
			if (!sp.isTableEmpty(t))
			{
				int minAge = 999;
				int maxAge = 0;

				for (int s=0; s<sp.seatsPerTable; s++)
				{
					String name = sp.getOccupant(t,s);
					if (!name.equals(sp.EMPTY)) {
						Guest g = book.getGuest(name);
						int age = g.getAge();
						if (age<minAge) minAge = age;
						if (age>maxAge) maxAge = age;
					}
				}
				range += (maxAge -minAge);
				tables++;
			}
		}

		if (tables>0)
		    return (double) range / (double) tables;
		else return 0.0;
	}

}
