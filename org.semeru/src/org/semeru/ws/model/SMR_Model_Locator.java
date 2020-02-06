package org.semeru.ws.model;

/**
 * 
 * @author Tegar N
 *
 */
public class SMR_Model_Locator{

	public String IsActive;
	public Integer M_Warehouse_ID;
	public Integer M_Locator_ID;
	public Integer C_Location_ID;
	public String Name;
	public String IsDisallowNegativeInv;
	//public String Postal;
	//public String Address;
	public Integer C_City_ID;

	
	public SMR_Model_Locator(String IsActive,Integer M_Warehouse_ID,String Name,Integer C_Location_ID,String IsDisallowNegativeInv,
			Integer C_City_ID,Integer M_Locator_ID){
		
		this.IsActive = IsActive;
		this.M_Warehouse_ID = M_Warehouse_ID; 
		this.Name = Name;
		this.C_Location_ID = C_Location_ID;
		this.IsDisallowNegativeInv = IsDisallowNegativeInv;
		//this.Postal = Postal;
		//this.Address = Address;
		this.C_City_ID = C_City_ID;
		this.M_Locator_ID = M_Locator_ID;
		
	}

}
