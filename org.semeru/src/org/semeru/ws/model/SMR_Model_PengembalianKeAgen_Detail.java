package org.semeru.ws.model;

import java.math.BigDecimal;

public class SMR_Model_PengembalianKeAgen_Detail {

	public String IsActive;
	public Integer M_Product_ID;
	public BigDecimal QtyReturn;
	public Integer C_UOM_ID;
	public BigDecimal PriceList;
	public BigDecimal PriceEntered;
	public BigDecimal PriceActual;
	public Integer M_AttributeSetInstance_ID;
	public BigDecimal LineNetAmt;
	public String Description;
	public Integer M_Locator_ID;


	public SMR_Model_PengembalianKeAgen_Detail(String IsActive,
			Integer M_Product_ID, BigDecimal QtyReturn, Integer C_UOM_ID,
			BigDecimal PriceList, BigDecimal PriceEntered,
			BigDecimal PriceActual, Integer M_AttributeSetInstance_ID,
			BigDecimal LineNetAmt, String Description,Integer M_Locator_ID) {
		
		 this.IsActive = IsActive;
		 this.M_Product_ID = M_Product_ID;
		 this.QtyReturn = QtyReturn;
		 this.C_UOM_ID = C_UOM_ID;
		 this.PriceList = PriceList;
		 this.PriceEntered = PriceEntered;
		 this.PriceActual = PriceActual;
		 this.M_AttributeSetInstance_ID = M_AttributeSetInstance_ID;
		 this.LineNetAmt = LineNetAmt;
		 this.Description = Description;
		 this.M_Locator_ID = M_Locator_ID;

	}

}
