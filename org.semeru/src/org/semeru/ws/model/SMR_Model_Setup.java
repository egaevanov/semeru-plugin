package org.semeru.ws.model;

public class SMR_Model_Setup {
	
	public Integer AD_Client_ID = 0;
	public Integer AD_Org_ID = 0;
	public String Name = "";
	public String Value = "";
	
	public SMR_Model_Setup(Integer AD_Client_ID, Integer AD_Org_ID, String Name, String Value) {
		
		this.AD_Client_ID = AD_Client_ID;
		this.AD_Org_ID = AD_Org_ID;
		this.Name = Name;
		this.Value = Value;
		
	}

}
