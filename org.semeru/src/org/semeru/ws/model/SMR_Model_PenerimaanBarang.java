package org.semeru.ws.model;

public class SMR_Model_PenerimaanBarang {

	public String IsActive;
	public Integer C_BPartner_ID;
	public String IsCash;
	public Integer C_BankAccount_ID;
	public String Description;
	public Integer M_Warehouse_ID;
	public Integer M_PriceList_ID;
	public Integer C_Currency_ID;
	public String DateReceipt;
	public Integer SalesRep_ID;
	public Integer AD_Org_ID;
	public Integer CreatedBy;
	public Integer C_POS_ID;



	public SMR_Model_PenerimaanBarang(String IsActive, Integer c_BPartner_ID,
			String IsCash, Integer C_BankAccount_ID, String Description,
			Integer M_Warehouse_ID, Integer M_PriceList_ID, 
			Integer C_Currency_ID,String DateReceipt,Integer SalesRep_ID,Integer AD_Org_ID,Integer CreatedBy, Integer C_POS_ID) {

		this.IsActive = IsActive;
		this.C_BPartner_ID = C_BankAccount_ID;
		this.IsCash = IsCash;
		this.C_BankAccount_ID = C_BankAccount_ID;
		this.Description = Description;
		this.M_Warehouse_ID = M_Warehouse_ID;
		this.M_PriceList_ID = M_PriceList_ID;
		this.DateReceipt = DateReceipt;
		this.SalesRep_ID = SalesRep_ID;
		this.AD_Org_ID = AD_Org_ID;
		this.C_Currency_ID = C_Currency_ID;
		this.CreatedBy = CreatedBy;
		this.C_POS_ID = C_POS_ID;

	}
	
}
