package com.diatom.pareto.examples.mill;

import com.diatom.pareto.Optimizer;
import com.diatom.pareto.Solution;

import java.util.*;

/**
 * Defines the sequence by which orders in the orderbook are fulfilled
 * on one or more machines
 * @author John
 *
 */
public class ProductionSchedule extends Solution {
	
	Map<Integer,List<Order>> machines = new HashMap<Integer,List<Order>>();
	Map<Order,Integer> assignedMachine = new HashMap<Order,Integer>();
	
	public ProductionSchedule()
	{
		int N = ((MillConfiguration) (Optimizer.getInstance().getResource("mill"))).getIntProperty("machines");
		for (int i=0; i<N; i++)
		{
			Integer machineID = new Integer(i);
			List<Order> machine = new ArrayList<Order>();
			machines.put(machineID, machine);
		}
	}
	
	/**
	 * Get the set of all machine IDs
	 * @return
	 */
	public Set<Integer> getMachineIDs()
	{
		return machines.keySet();
	}
	
	
	/**
	 * Add an order to a specified machine
	 * @param machineID
	 * @param order
	 */
	public void addOrder(int machineID, Order order)
	{
		List<Order> machine = machines.get(machineID);
		if (machine != null)
			machine.add(order);
		assignedMachine.put(order, machineID);
	}
	

	/**
	 * Move the order up one slot on whatever machine it is assigned to
	 * If it is unassigned or if it is already at the front of the list
	 * then do nothing
	 * @param order
	 */
	public void advanceOrder(Order order)
	{
		Integer machineID = assignedMachine.get(order);
		
		if (machineID == null) // unassigned
			return;            // do nothing
		
		int idx = indexOf(order);
		if (idx == 0) // already at the front
			return;   // do nothing
		
		// swap with prior order
		List<Order> machine = getMachine(machineID);
		Order tmp = machine.get(idx-1);
		machine.set(idx-1, order);
		machine.set(idx, tmp);
	}
	
	/**
	 * Get the position of the order on whatever machine it is assigned to
	 * @param order
	 * @return
	 */
	public int indexOf(Order order)
	{
		Integer machineID = assignedMachine.get(order);
		if (machineID==null)
			return -1; // order is not assigned
		else
			return machines.get(machineID).indexOf(order);
	}
	
	
	
	
	/**
	 * Remove an order from the schedule
	 * @param order
	 */
	public void removeOrder(Order order)
	{
		Integer machineID = assignedMachine.get(order);
		if (machineID == null)
			return;  // order isn't assigned
		
		List<Order> machine = machines.get(machineID);
		if (machine != null)
			machine.remove(order);
		assignedMachine.remove(order);
	}
	
	/**
	 * Get all orders assigned to the production schedule
	 * @return
	 */
	public Set<Order> getAssignedOrders()
	{
		return assignedMachine.keySet();
	}
	
	/**
	 * Get the scheduling sequence for the specified machine
	 * @param machineID
	 * @return
	 */
	public List<Order> getMachine(int machineID)
	{
		return machines.get(machineID);
	}
	
	/**
	 * Get the machine ID of a particular order
	 * @param order
	 * @return
	 */
	public Integer getAssignedMachine(Order order)
	{
		return assignedMachine.get(order);
	}

	/**
	 * Get total machine run time
	 * @param machineID
	 * @return
	 */
	public int getMachineTime(int machineID)
	{
		List<Order> machine = getMachine(machineID);
		int setupTime = ((MillConfiguration) (Optimizer.getInstance().getResource("mill"))).getIntProperty("setup_time");

		int totalTime = 0;
		int lastProductCode = -99;
		for (Order order : machine)
		{
			if (order.getProductCode() != lastProductCode)
				totalTime += setupTime;
			totalTime += order.getProductionCycles();
			lastProductCode=order.getProductCode();
		}
		return totalTime;
	}
	
	
	/**
	 * Return set of late orders 
	 * Only scheduled orders can be late
	 * @return
	 */
	public Set<Order> getLateOrders()
	{
		int setupTime = ((MillConfiguration) (Optimizer.getInstance().getResource("mill"))).getIntProperty("setup_time");
		Set<Integer> machineIDs = machines.keySet();
		Set<Order> lateOrders = new HashSet<Order>();
		for (Integer machineID : machineIDs)
		{
			List<Order> schedule = getMachine(machineID);
			int currentTime = 0;
			int lastProductCode = -99;
			for (Order order : schedule)
			{
				if (order.getProductCode() != lastProductCode)
					currentTime += setupTime;
				currentTime += order.productionCycles;
				if (order.getDueDate()>currentTime)
					lateOrders.add(order);
			}
		}
		return lateOrders;
	}
	
	
	/**
	 * The number of assigned orders
	 * @return
	 */
	public int numAssignedOrders()
	{
		return assignedMachine.size();
	}
	
	protected void print()
	{
		TreeSet<Integer> machineIDs = new TreeSet<Integer>(getMachineIDs());
		int setupTime = ((MillConfiguration) (Optimizer.getInstance().getResource("mill"))).getIntProperty("setup_time");

		for (Integer machineID : machineIDs)
		{
			System.out.println("\n\nMACHINE "+machineID);
			int currentTime = 0;
			int lastProduct = -99;
			List<Order> schedule = getMachine(machineID);
			int count = 1;
			System.out.print("#\tID\tStart\tEnd\tDue\tProd\tHI?\tSetup?\tLate?\n\n");
			for (Order order : schedule)
			{
				if (order.getProductCode()!=lastProduct)
					currentTime+=setupTime;
				
				System.out.println(
					count+"\t"+
					order.getOrderID()+"\t"+
					currentTime+"\t"+	
					(currentTime+order.getProductionCycles())+"\t"+
				    order.getDueDate()+"\t"+
					order.getProductCode()+"\t"+
					(order.isHighPriority() ? "HI" : "  ")+"\t"+
				    (order.getProductCode()!=lastProduct ? "SETUP" : "     ")+"\t"+
					(order.getDueDate()<(currentTime+order.getProductionCycles())?"LATE":"    ")
				);
				
				lastProduct = order.getProductCode();
				currentTime += order.getProductionCycles();
				count++;
			}
		}
	}
	
	@Override
	protected void doClone(Solution solution)
	{
		ProductionSchedule ps = (ProductionSchedule) solution;
		ps.machines = new HashMap<Integer,List<Order>>();
		ps.assignedMachine = new HashMap<Order,Integer>();
		
		// clone the machines (deep copy)
		for (Integer machineID : machines.keySet())
		{
			List<Order> currentMachine = machines.get(machineID);
			List<Order> clonedMachine = new ArrayList<Order>();
			for (Order order : currentMachine)
			{
				clonedMachine.add(order); // add order to cloned machine
				ps.assignedMachine.put(order,machineID); // assign the order in the cloned object
			}
			ps.machines.put(machineID,  clonedMachine);
		}
	}
}
