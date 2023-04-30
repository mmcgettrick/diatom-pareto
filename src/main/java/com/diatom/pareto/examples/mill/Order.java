package com.diatom.pareto.examples.mill;
/**
 * A mill order specifies a product type, a due date (target time unit), 
 * number of time units required to manufacture, and whether the order is 
 * high priority
 * @author John
 *
 */

public class Order {
	
	int orderID;     // The order ID

	int productCode; // a product code.  Switching from one product code
	                 // to another incurs a "setup cost"   In this model
					 // we assume that a setup takes zero time
	
	int dueDate;     // Time interval when manufacture of the product
	                 // must be completed
	
	boolean highPriority; // Is the order high-priority?  Late high-priority
	                      // orders are particularly bad!
	
	int productionCycles; // Number of machine cycles requireed to fulfill the order
	                      // The order must be completed by its due date
	                      // in order to be "on time"

	
	/**
	 * Constructor
	 * @param orderID
	 * @param productCode
	 * @param dueDate
	 * @param highPriority
	 * @param productionCycles
	 */
	public Order(int orderID, int productCode, int dueDate, boolean highPriority,
			int productionCycles) {
		super();
		this.orderID = orderID;
		this.productCode = productCode;
		this.dueDate = dueDate;
		this.highPriority = highPriority;
		this.productionCycles = productionCycles;
	}


	public int getOrderID() {
		return orderID;
	}


	public void setOrderID(int orderID) {
		this.orderID = orderID;
	}


	public int getProductCode() {
		return productCode;
	}


	public void setProductCode(int productCode) {
		this.productCode = productCode;
	}


	public int getDueDate() {
		return dueDate;
	}


	public void setDueDate(int dueDate) {
		this.dueDate = dueDate;
	}


	public boolean isHighPriority() {
		return highPriority;
	}


	public void setHighPriority(boolean highPriority) {
		this.highPriority = highPriority;
	}


	public int getProductionCycles() {
		return productionCycles;
	}


	public void setProductionCycles(int productionCycles) {
		this.productionCycles = productionCycles;
	}


	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + dueDate;
		result = prime * result + (highPriority ? 1231 : 1237);
		result = prime * result + orderID;
		result = prime * result + productCode;
		result = prime * result + productionCycles;
		return result;
	}


	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Order other = (Order) obj;
		if (dueDate != other.dueDate)
			return false;
		if (highPriority != other.highPriority)
			return false;
		if (orderID != other.orderID)
			return false;
		if (productCode != other.productCode)
			return false;
		if (productionCycles != other.productionCycles)
			return false;
		return true;
	}
	
	
}
