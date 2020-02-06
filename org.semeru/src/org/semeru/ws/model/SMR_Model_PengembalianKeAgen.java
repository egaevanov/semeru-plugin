package org.semeru.ws.model;

public class SMR_Model_PengembalianKeAgen {

	public String IsActive;
	public Integer C_Order_ID;
	public String Name;
	public String Description;
	public String DateReturn;
	public Integer C_BankAccount_ID;

	public SMR_Model_PengembalianKeAgen(String IsActive, Integer C_Order_ID, String Name, String Description,
			String DateReturn, Integer C_BankAccount_ID) {

		this.IsActive = IsActive;
		this.C_Order_ID = C_Order_ID;
		this.Name = Name;
		this.Description = Description;
		this.DateReturn = DateReturn;
		this.C_BankAccount_ID = C_BankAccount_ID;
	}

}
