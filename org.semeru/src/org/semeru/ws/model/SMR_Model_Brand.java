package org.semeru.ws.model;

/**
 * 
 * @author Tegar N
 *
 */
public class SMR_Model_Brand{

	public String IsActive;
	public String Name;
	public Integer M_Brand_ID = 0;
	
	public SMR_Model_Brand(String IsActive,String Name,Integer M_Brand_ID){
		
		this.IsActive = IsActive;
		this.Name = Name;
		this.M_Brand_ID = M_Brand_ID;
	}
	
}