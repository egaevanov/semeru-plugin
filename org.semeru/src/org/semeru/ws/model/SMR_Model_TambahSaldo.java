package org.semeru.ws.model;

public class SMR_Model_TambahSaldo {
	
	public Integer AD_Org_ID;
	public String IsActive;
	public Integer C_BankAccount_ID;
	public String StatementDate;
	
	public SMR_Model_TambahSaldo(Integer AD_Org_ID,String IsActive, Integer C_BankAccount_ID,String StatementDate) {

		this.AD_Org_ID = AD_Org_ID;
		this.IsActive = IsActive;
		this.C_BankAccount_ID = C_BankAccount_ID;
		this.StatementDate = StatementDate;
	
	}

}
