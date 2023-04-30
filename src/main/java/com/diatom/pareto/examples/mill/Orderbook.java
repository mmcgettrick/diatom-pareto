package com.diatom.pareto.examples.mill;

import com.diatom.pareto.Resource;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;


/**
 * A collection of orders
 * @author John
 *
 */
public class Orderbook extends Resource {
	


	
	List<Order> book = new ArrayList<Order>();
	
	
	public Orderbook(Map<String, Object> options) {
		super(options);
		if (options == null)
			generate(200, 10, 1100, 1, 10, 10, 0.10);
		else
		{
			String filename = (String) options.get("filename");
			read(filename);
		}
	}
	
	
	public void addOrder(Order order)
	{
		book.add(order);
	}
	


	public Order getOrder(int i)
	{
		try
		{
			return book.get(i);
		} catch (Exception e)
		{
			System.err.println(e.getMessage());
			return null;
		}
	}
	



	/**
	 * Return the size of the orderbook
	 * @return
	 */
	public int size()
	{
		return book==null ? 0 : book.size();
	}





	public void read(String filename) {
		try
		{
			BufferedReader in = new BufferedReader(new FileReader(filename));
			String line = in.readLine();
			while (line != null) {
				String[] tokens = line.split("\t");
				int id = new Integer(tokens[0]).intValue();
				int code = new Integer(tokens[1]).intValue();
				int due = new Integer(tokens[2]).intValue();
				int cycles = new Integer(tokens[3]).intValue();
				String highString = tokens[4];
				boolean high = false;
				if (highString.equalsIgnoreCase("true"))
					high = true;
				Order order = new Order(id, code, due, high, cycles);
				book.add(order);
				line = in.readLine();
			}
			in.close();
		} catch (Exception e)
		{
			System.err.println(e.getMessage());
			System.exit(1);
		}
	}
	
	
	

	/**
	 * Generate a random orderbook
	 * @param numOrders
	 * @param minDuedate
	 * @param maxDuedate
	 * @param minCycles
	 * @param maxCycles
	 * @param numProductCodes
	 * @param pctPriority
	 */
	public void generate(int numOrders, 
						 int minDuedate, int maxDuedate, 
						 int minCycles, int maxCycles,
						 int numProductCodes, double pctPriority)
	{
		for (int i=0; i<numOrders; i++)
		{
			int orderID = i;
			int dueDate = minDuedate + (int)(Math.random() * ((maxDuedate - minDuedate)+1));
			int cycles = minCycles+(int)(Math.random()*((maxCycles-minCycles)+1));
			int productCode = (int) (Math.random() * numProductCodes);
			boolean highPriority = Math.random()<pctPriority ? true : false;
			Order newOrder = new Order(orderID, productCode, dueDate, highPriority, cycles);
			addOrder(newOrder);
		}
	}
	
	/**
	 * Print the orderbook
	 */
	public void print()
	{
		for (Order order : book)
		{
			System.out.println(order.getOrderID()+"\t"+order.getProductCode()+"\t"+order.getDueDate()+"\t"+order.getProductionCycles()+"\t"+order.isHighPriority());
		}
	}

	
	
	public static void main(String[] args)
	{
		/*
		Map<String,Object> options = new HashMap<String,Object>();
		options.put("name", "book");
		options.put("filename", "conf/orderbook.txt");
		Orderbook book  = new Orderbook(options);
		book.print();
		System.out.println(book.size()+" orders read.");
		*/
		
		
		Orderbook book = new Orderbook(null);
		book.generate(100, 10, 400, 5, 10, 5, .10);
		book.print();
		
		
	}
	
}
