package org.semeru.ws.model;

import java.math.BigDecimal;

public class SMR_Model_BayarHutang {

	public String IsActive;
	public Integer C_Order_ID;
	public BigDecimal PayAmt;
	public String DateTrx;
	public Integer C_BankAccount_ID;
	public String TenderType;

	public SMR_Model_BayarHutang(String IsActive, Integer C_Order_ID,
			BigDecimal PayAmt, String DateTrx, Integer C_BankAccount_ID,
			String TenderType) {
		
		this.IsActive = IsActive;
		this.C_Order_ID = C_Order_ID;
		this.PayAmt = PayAmt;
		this.DateTrx = DateTrx;
		this.C_BankAccount_ID = C_BankAccount_ID;
		this.TenderType = TenderType;
	}

}
