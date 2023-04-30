package com.diatom.pareto;

import java.util.Comparator;
import java.util.List;
import java.util.Map;

/**
 * Compares two solutions for purposes of determining whether one solution dominates another
 * @author mmcgettrick
 *
 */
public class SolutionComparator implements Comparator<Solution> {

	private List<String> objectiveSortOrder;
	
	public SolutionComparator(List<String> objectiveSortOrder) {
		this.objectiveSortOrder = objectiveSortOrder;
	}

	public int compare(Solution s1, Solution s2) {
		Map<String,Objective> objectiveMap = Optimizer.getInstance().getObjectives();
		for(String objectiveName : objectiveSortOrder) {
			Objective objective = objectiveMap.get(objectiveName);
			double target = objective.getTarget();
			double diff1 = Math.abs(target - s1.getEvaluation(objectiveName));
			double diff2 = Math.abs(target - s2.getEvaluation(objectiveName));
			/*
			System.out.println("Comparing " + s1.getId() + " " + s2.getId() + "\t" +
				objectiveName + "\t" +
				target + "\t" +
				s1.getEvaluation(objectiveName) + "\t" + 
				s2.getEvaluation(objectiveName) + "\t" + 
				diff1 + "\t" +
				diff2
			);
			*/
			if(diff1 < diff2) {
				//System.out.println(s1.getId() + " is better.");
				return -1;
			}
			else if(diff1 > diff2) {
				//System.out.println(s2.getId() + " is better.");
				return 1;
			}
			// Otherwise continue
		}
		// If we get here then all sortable objectives were equal... in that case order by id
		//System.out.println("Equal solutions - sorting by Id");
		return (s1.getId().getSolutionId() < s2.getId().getSolutionId()) ? -1 : 1; 
	}

}
