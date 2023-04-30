package com.diatom.pareto.examples.mill;

import com.diatom.pareto.Objective;
import com.diatom.pareto.Solution;

import java.util.Map;

/**
 * Minimize the difference between the length of the longest and shortest machine schedule
 * @author John
 *
 */
public class ObjectiveMachineBalancing extends Objective {

	public ObjectiveMachineBalancing(Map<String, Object> options) {
		super(options);
		// TODO Auto-generated constructor stub
	}

	@Override
	public double evaluate(Solution solution) {

		ProductionSchedule ps = (ProductionSchedule) solution;
		
		int minTime = -1;
		int maxTime = -1;
		for (Integer machineID : ps.getMachineIDs())
		{
			int time = ps.getMachineTime(machineID);
			if ((minTime == -1) || (time<minTime))
				minTime = time;
			if ((maxTime == -1) || (time>maxTime))
				maxTime = time;
		}
		return (double) (maxTime - minTime); // minTime==0 ? 0.0 : (double) maxTime / (double) minTime;
	}

}
