package org.semeru.ws.model;

import java.math.BigDecimal;

public class SMR_Model_TambahSaldo_Detail {

	public Integer AD_Org_ID;
	public String IsActive;
	public Integer C_BankStatement_ID;
//	public Timestamp StatementLineDate;
//	public Timestamp DateAcct;
//	public Timestamp ValutaDate;
	public BigDecimal StmtAmt;
	public Integer C_Charge_ID;
	public Integer C_Currency_ID;

	public SMR_Model_TambahSaldo_Detail(Integer AD_Org_ID, String IsActive,
			Integer C_BankStatement_ID, BigDecimal StmtAmt,Integer C_Charge_ID,Integer C_Currency_ID) {

		this.AD_Org_ID = AD_Org_ID;
		this.IsActive = IsActive;
		this.C_BankStatement_ID = C_BankStatement_ID;
//		this.StatementLineDate = StatementLineDate;
//		this.DateAcct = DateAcct;
//		this.ValutaDate = ValutaDate;
		this.StmtAmt = StmtAmt;
		this.C_Charge_ID = C_Charge_ID;
		this.C_Currency_ID = C_Currency_ID;
	}

}
