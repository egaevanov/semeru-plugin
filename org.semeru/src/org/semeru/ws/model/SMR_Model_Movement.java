package org.semeru.ws.model;

public class SMR_Model_Movement {

	public String IsActive;
	public Integer M_Warehouse_ID;
	public String MovementDate;
	//public String Description;

	public SMR_Model_Movement(String IsActive,Integer M_Warehouse_ID,
			String MovementDate) {
				
		this.IsActive = IsActive;
		this.M_Warehouse_ID = M_Warehouse_ID;
		this.MovementDate = MovementDate;
	//	this.Description = Description;
	
	}

}
