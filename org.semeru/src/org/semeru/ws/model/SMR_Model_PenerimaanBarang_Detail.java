package org.semeru.ws.model;

import java.math.BigDecimal;

public class SMR_Model_PenerimaanBarang_Detail {

	public String IsActive;
	public Integer M_Product_ID;
	public Integer M_AttributeSetInstance_ID;
	public Integer M_Locator_ID;
	public BigDecimal QtyEntered;
	public BigDecimal PriceList;
	public BigDecimal PriceEntered;
	public BigDecimal PriceActual;
	public BigDecimal PriceLimit;
	public BigDecimal LineNetAmt;
	public String Description;
	

	public SMR_Model_PenerimaanBarang_Detail(String IsActive,
			Integer M_Product_ID, Integer M_AttributeSetInstance_ID,
			Integer M_Locator_ID, BigDecimal QtyEntered, BigDecimal PriceList,
			BigDecimal PriceEntered, BigDecimal PriceActual,BigDecimal PriceLimit,
			BigDecimal LineNetAmt,String Description) {

		this.IsActive = IsActive;
		this.M_Product_ID = M_Product_ID;
		this.M_AttributeSetInstance_ID = M_AttributeSetInstance_ID;
		this.M_Locator_ID = M_Locator_ID;
		this.QtyEntered = QtyEntered;
		this.PriceEntered = PriceEntered; 
		this.PriceActual = PriceActual;
		this.PriceLimit = PriceLimit;
		this.LineNetAmt = LineNetAmt;
		this.Description = Description;

	}
		

}
