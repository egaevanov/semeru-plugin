package org.semeru.ws.model;

import java.math.BigDecimal;

/**
 * 
 * @author Tegar N
 *
 */

public class SMR_Model_Order {

	public String IsActive;
	public String OrderReference;
	public Integer C_BPartner_ID;
	public Integer C_BPartner_Location_ID;
	public String DateOrdered;
	public Integer M_Warehouse_ID;
	public Integer SalesRep_ID;
	public Integer M_PriceList_ID;
	public Integer PaymentRule;
	public Integer C_Currency_ID;
	public Integer C_PaymentTerm_ID;
	public BigDecimal Grandtotal;
	public BigDecimal TotaLines;
	public String DeliveryViaRule;// Pickup & Non Pickup
	public String Description;
	public String IsPickUP;
	public Integer C_TaxCategory_ID;
	public Integer C_BankAccount_ID;
	public Integer M_Sales_Locator_ID;
	public String IsDiscount;
	public BigDecimal DiscountAmt;
	public String POReference;
	public Integer CashMethod_ID;
	public Integer CreatedBy;
	public Integer C_POS_ID;
	

	
	public SMR_Model_Order(String IsActive, String OrderReference,Integer C_BPartner_ID, Integer C_BPartner_Location_ID,
			String DateOrdered, Integer M_Warehouse_ID, Integer SalesRep_ID,Integer M_PriceList_ID, Integer PaymentRule, 
			Integer C_Currency_ID,Integer C_PaymentTerm_ID, BigDecimal Grandtotal,String DeliveryViaRule, String Description,
			String IsPickUP,Integer C_TaxCategory_ID, Integer C_BankAccount_ID,Integer M_Sales_Locator_ID,String IsDiscount, 
			BigDecimal DiscountAmt,String POReference, Integer CashMethod_ID, Integer CreatedBy, Integer C_POS_ID) {

		this.IsActive = IsActive;
		this.OrderReference = OrderReference;
		this.C_BPartner_ID = C_BPartner_ID;
		this.C_BPartner_Location_ID = C_BPartner_Location_ID;
		this.DateOrdered = DateOrdered;
		this.M_Warehouse_ID = M_Warehouse_ID;
		this.SalesRep_ID = SalesRep_ID;
		this.M_PriceList_ID = M_PriceList_ID;
		this.PaymentRule = PaymentRule;
		this.C_Currency_ID = C_Currency_ID;
		this.C_PaymentTerm_ID = C_PaymentTerm_ID;
		this.Grandtotal = Grandtotal;
		this.DeliveryViaRule = DeliveryViaRule;
		this.Description = Description;
		this.IsPickUP = IsPickUP;
		this.C_TaxCategory_ID = C_TaxCategory_ID;
		this.C_BankAccount_ID = C_BankAccount_ID;
		this.M_Sales_Locator_ID = M_Sales_Locator_ID;
		this.IsDiscount = IsDiscount;
		this.DiscountAmt = DiscountAmt;
		this.POReference = POReference;
		this.CashMethod_ID = CashMethod_ID;
		this.CreatedBy = CreatedBy;
		this.C_POS_ID = C_POS_ID;
		
	}

}
