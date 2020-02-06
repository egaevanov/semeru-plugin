package org.semeru.ws.model;

/**
 * 
 * @author Tegar N
 *
 */

	public class SMR_Model_ProductCategory{

	public String IsActive;
	public String Name;
	public String Description;
	public String MMPolicy;
	public Integer M_Product_Category_ID;
	
	public SMR_Model_ProductCategory(String IsActive,String Name,String Description,String MMPolicy,Integer M_Product_Category_ID){
		
		this.IsActive = IsActive;
		this.Name = Name;
		this.Description = Description;
		this.MMPolicy = MMPolicy;
		this.M_Product_Category_ID = M_Product_Category_ID; 

	}
	
}
