package org.semeru.ws.model;

import java.math.BigDecimal;

public class SMR_Model_Pembatalan_Detail {

	public String IsActive;
	public Integer M_Product_ID;
	public BigDecimal QtyOrdered;
	public BigDecimal PriceList;
	public BigDecimal PriceEntered;
	public BigDecimal PriceActual;
	public Integer M_AttributeSetInstance_ID;
	public BigDecimal LineNetAmt;
	public Integer M_Locator_ID;

		
	public SMR_Model_Pembatalan_Detail(String IsActive,Integer M_Product_ID,BigDecimal QtyOrdered,Integer M_Locator_ID, 
			BigDecimal PriceList,BigDecimal PriceEntered, BigDecimal PriceActual,
			Integer M_AttributeSetInstance_ID, BigDecimal LineNetAmt){
		
		this.IsActive = IsActive;
		this.M_Product_ID = M_Product_ID;
		this.QtyOrdered = QtyOrdered;
		this.PriceList = PriceList;
		this.PriceEntered = PriceEntered;
		this.PriceActual = PriceActual;
		this.M_AttributeSetInstance_ID = M_AttributeSetInstance_ID;
		this.LineNetAmt = LineNetAmt;
		this.M_Locator_ID = M_Locator_ID;
		
	}

}
