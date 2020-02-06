package org.semeru.ws.model;

/**
 * 
 * @author Tegar N
 *
 */
public class SMR_Model_Bank{

	public String IsActive;
	public Integer C_Bank_ID = 0;
	public Integer C_BankAccount_ID = 0;
	public String Name;
	public String AccountNo;
	public String BankAccountType;
	public Integer C_Currency_ID;

	
	public SMR_Model_Bank(String IsActive,String Name,Integer C_Bank_ID,Integer C_BankAccount_ID,String AccountNo,String BankAccountType,Integer C_Currency_ID){
		
		this.IsActive = IsActive;
		this.Name = Name;
		this.C_Bank_ID = C_Bank_ID;
		this.C_BankAccount_ID = C_BankAccount_ID;
		this.AccountNo = AccountNo;
		this.BankAccountType = BankAccountType;
		this.C_Currency_ID = C_Currency_ID;

	}
	
}