package org.semeru.ws.model;

import java.math.BigDecimal;

public class SMR_Model_StockOpname_Detail {

	public String IsActive;
	public Integer AD_Org_ID;
	public Integer M_Inventory_ID;
	public Integer M_Locator_ID;
	public Integer M_Product_ID;
	public Integer M_AttributeSetInstance_ID;
	public String InventoryType;
	public BigDecimal QtyCount;
	public String Description;

	public SMR_Model_StockOpname_Detail(String IsActive, Integer AD_Org_ID,
			Integer M_Inventory_ID, Integer M_Locator_ID, Integer M_Product_ID,
			Integer M_AttributeSetInstance_ID, String InventoryType,
			BigDecimal QtyCount, String Description) {

		this.IsActive = IsActive;
		this.AD_Org_ID = AD_Org_ID;
		this.M_Inventory_ID = M_Inventory_ID;
		this.M_Locator_ID = M_Locator_ID;
		this.M_Product_ID = M_Product_ID;
		this.M_AttributeSetInstance_ID = M_AttributeSetInstance_ID;
		this.InventoryType = InventoryType;
		this.QtyCount = QtyCount;
		this.Description = Description;
	}

}
