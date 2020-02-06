package org.semeru.ws.model;

public class SMR_Model_Organization {

	public String IsActive;
	public String Name;
	public String Description;
	public String Address1;
	public String Address2;
	public String City;
	public String Postal;

	public SMR_Model_Organization(String IsActive, String Name,
			String Description, String Address1, String Address2, String City,
			String Postal) {
		
		this.IsActive = IsActive;
		this.Name = Name;
		this.Description = Description;
		this.Address1 = Address1;
		this.Address2 = Address2;
		this.City = City;
		this.Postal = Postal;
		
	}

}
