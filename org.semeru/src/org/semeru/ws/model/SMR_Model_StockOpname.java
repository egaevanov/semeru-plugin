package org.semeru.ws.model;

public class SMR_Model_StockOpname {

	public String IsActive;
	public Integer AD_Org_ID;
	public String Description;
	public Integer M_Warehouse_ID;
	public String MovementDate ;	
	
	public SMR_Model_StockOpname(String IsActive,Integer AD_Org_ID,String Description,Integer M_Warehouse_ID,String MovementDate){
		
		this.IsActive = IsActive;
		this.AD_Org_ID = AD_Org_ID;
		this.Description = Description;
		this.M_Warehouse_ID = M_Warehouse_ID;
		this.MovementDate = MovementDate;
		
	}
		
}
