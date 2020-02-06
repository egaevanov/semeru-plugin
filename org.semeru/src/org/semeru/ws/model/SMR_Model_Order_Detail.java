package org.semeru.ws.model;

import java.math.BigDecimal;

/**
 * 
 * @author Tegar N
 *
 */

public class SMR_Model_Order_Detail {

	public String IsActive;
	public Integer M_Product_ID;
	public Integer C_UOM_ID;
	public BigDecimal QtyOrdered;
	public BigDecimal PriceList;
	public BigDecimal PriceEntered;
	public BigDecimal PriceActual;
	public Integer M_AttributeSetInstance_ID;
	public BigDecimal LineNetAmt;
	public Integer M_Locator_ID; 
	public BigDecimal DiscountAmt;

	public SMR_Model_Order_Detail(String IsActive, Integer M_Product_ID,
			Integer C_UOM_ID, BigDecimal QtyOrdered, BigDecimal PriceList,
			BigDecimal PriceEntered, BigDecimal PriceActual,
			Integer M_AttributeSetInstance_ID, BigDecimal LineNetAmt, Integer M_Locator_ID, BigDecimal DiscountAmt) {

		this.IsActive = IsActive;
		this.M_Product_ID = M_Product_ID;
		this.C_UOM_ID = C_UOM_ID;
		this.QtyOrdered = QtyOrdered;
		this.PriceList = PriceList;
		this.PriceEntered = PriceEntered;
		this.PriceActual = PriceActual;
		this.M_AttributeSetInstance_ID = M_AttributeSetInstance_ID;
		this.LineNetAmt = LineNetAmt;
		this.M_Locator_ID = M_Locator_ID;
		this.DiscountAmt = DiscountAmt;
			
	}

}
