package org.semeru.ws.model;

public class SMR_Model_UOM {
	
	public String IsActive;
	public String Name;
	public Integer C_UOM_ID = 0;
	
	public SMR_Model_UOM(String IsActive,String Name,Integer C_UOM_ID){
		
		this.IsActive = IsActive;
		this.Name = Name;
		this.C_UOM_ID = C_UOM_ID;
	}

}
