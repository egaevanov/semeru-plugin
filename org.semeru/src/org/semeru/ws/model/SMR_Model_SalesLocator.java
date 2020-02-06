package org.semeru.ws.model;

/**
 * 
 * @author Tegar N
 *
 */

	public class SMR_Model_SalesLocator{

	public String IsActive;
	public String Address1;
	public String Address2;
	public String Description;
	public Integer M_Sales_Locator_ID;
	public String Name;

		public SMR_Model_SalesLocator(String IsActive,String Address1,String Address2,String Description,Integer M_Sales_Locator_ID,String Name){
			
		this.IsActive = IsActive;
		this.Address1 = Address1;
		this.Address2 = Address2;
		this.Description = Description;
		this.M_Sales_Locator_ID = M_Sales_Locator_ID;
		this.Name = Name;
		
		}
}
