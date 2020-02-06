package org.semeru.ws.model;

/**
 * 
 * @author Tegar N
 *
 */

	public class SMR_Model_TaxCategory{

	public String IsActive;
	public String Name;
	public String Description;
	public String IsWithholding;
	public Integer C_Tax_Category_ID;

	public SMR_Model_TaxCategory(String IsActive,String Name, String Description, String IsWithholding, Integer C_Tax_Category_ID){
		
	this.IsActive = IsActive;
	this.Name = Name;
	this.Description = Description;
	this.IsWithholding = IsWithholding;
	this.C_Tax_Category_ID = C_Tax_Category_ID;
	
	}

}
