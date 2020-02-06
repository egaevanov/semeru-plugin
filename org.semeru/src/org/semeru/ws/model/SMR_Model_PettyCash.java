package org.semeru.ws.model;

import java.math.BigDecimal;

/**
 * 
 * @author Tegar N
 *
 */

public class SMR_Model_PettyCash {

	public String IsActive;
	public Integer AD_Org_ID;
	public String DateInvoiced;
	public Integer C_BPartner_ID;
	public Integer C_BPartner_Location_ID;
	public Integer M_PriceList_ID;
	public Integer C_Currency_ID;
	public String PaymentRule;
	public Integer C_PaymentTerm_ID;
	public String Description;
	public String IsReceipt;
	public Integer C_BankAccount_ID;
	public BigDecimal PayAmt;

	public SMR_Model_PettyCash(String IsActive, Integer AD_Org_ID,
			String DateInvoiced, Integer C_BPartner_ID,
			Integer C_BPartner_Location_ID, Integer M_PriceList_ID,
			Integer C_Currency_ID, String PaymentRule,
			Integer C_PaymentTerm_ID, String Description,String IsReceipt,
			Integer C_BankAccount_ID,BigDecimal PayAmt) {
	
		this.IsActive = IsActive;		
		this.AD_Org_ID = AD_Org_ID;
		this.DateInvoiced = DateInvoiced;
		this.C_BPartner_ID = C_BPartner_ID;
		this.C_BPartner_Location_ID = C_BPartner_Location_ID;
		this.M_PriceList_ID = M_PriceList_ID;
		this.C_Currency_ID = C_Currency_ID;
		this.PaymentRule = PaymentRule;
		this.C_PaymentTerm_ID = C_PaymentTerm_ID;
		this.Description = Description;
		this.IsReceipt = IsReceipt;
		this.C_BankAccount_ID = C_BankAccount_ID;
		this.PayAmt = PayAmt;
		
	}

}
