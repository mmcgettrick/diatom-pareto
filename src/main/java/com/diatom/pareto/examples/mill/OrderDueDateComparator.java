package com.diatom.pareto.examples.mill;

import java.util.Comparator;

public class OrderDueDateComparator implements Comparator<Order>{

	public int compare(Order o1, Order o2) {
		return o1.getDueDate() - o2.getDueDate();
	}

}
