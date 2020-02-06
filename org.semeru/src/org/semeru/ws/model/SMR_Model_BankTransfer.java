package org.semeru.ws.model;

import java.math.BigDecimal;
import java.sql.Timestamp;

/**
 * 
 * @author Tegar N
 *
 */
public class SMR_Model_BankTransfer {

	public String IsActive;
	public Integer C_BankAccountFrom_ID;
	public Integer C_BankAccountTo_ID;
	public Integer C_BPartner_ID;
	public Integer C_Currency_ID;
	public Integer C_Charge_ID;
	public BigDecimal AmtTransfer;
	public String Description;
	public String StatementDate;
	public Timestamp DateAcct;
	public Integer AD_Org_ID;

	public SMR_Model_BankTransfer(String IsActive,
			Integer C_BankAccountFrom_ID, Integer C_BankAccountTo_ID,
			Integer C_BPartner_ID, Integer C_Currency_ID, Integer C_Charge_ID,
			BigDecimal AmtTransfer, String Description, String StatementDate,
			Timestamp DateAcct, Integer AD_Org_ID) {

		this.IsActive = IsActive;
		this.C_BankAccountFrom_ID = C_BankAccountFrom_ID;
		this.C_BankAccountTo_ID = C_BankAccountTo_ID;
		this.C_BPartner_ID = C_BPartner_ID;
		this.C_Currency_ID = C_Currency_ID;
		this.C_Charge_ID = C_Charge_ID;
		this.AmtTransfer = AmtTransfer;
		this.Description = Description;
		this.StatementDate = StatementDate;
		this.DateAcct = DateAcct;
		this.AD_Org_ID = AD_Org_ID;

	}

}