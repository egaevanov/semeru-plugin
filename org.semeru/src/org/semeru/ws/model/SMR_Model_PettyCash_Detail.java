package org.semeru.ws.model;

import java.math.BigDecimal;

/**
 * 
 * @author Tegar N
 *
 */

public class SMR_Model_PettyCash_Detail {

	public Integer C_Charge_ID;
	public Integer C_Tax_ID;
	public BigDecimal Amount;
	public String Description;

	public SMR_Model_PettyCash_Detail(Integer C_Charge_ID, Integer C_Tax_ID,
			Integer C_BankAccount_ID, BigDecimal Amount,String Description) {

		this.C_Charge_ID = C_Charge_ID; 
		this.C_Tax_ID = C_Tax_ID; 
		this.Amount = Amount; 
		this.Description = Description; 
		
	}

}
