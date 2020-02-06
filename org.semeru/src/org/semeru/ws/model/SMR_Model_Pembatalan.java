package org.semeru.ws.model;

public class SMR_Model_Pembatalan {
	
	
	public Integer C_Order_ID; 
	public String Type; 
	public String POReference;

	
	public SMR_Model_Pembatalan(Integer C_Order_ID, String Type,String POReference){
		
		this.C_Order_ID = C_Order_ID;
		
		this.Type = Type;
		this.POReference = POReference;
		
	}

}
