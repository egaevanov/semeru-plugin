package org.semeru.ws.model;

import java.math.BigDecimal;

public class SMR_Model_Movement_Detail {

	public Integer M_Product_ID;
	public Integer M_LocatorFrom_ID;
	public Integer M_LocatorTo_ID;
	public BigDecimal MovementQty;

	public SMR_Model_Movement_Detail(Integer M_Movement_ID, Integer M_Product_ID, Integer M_LocatorFrom_ID,
			Integer M_LocatorTo_ID, BigDecimal MovementQty) {
	
		this.M_Product_ID = M_Product_ID;
		this.M_LocatorFrom_ID = M_LocatorFrom_ID;
		this.M_LocatorTo_ID = M_LocatorTo_ID;
		this.MovementQty = MovementQty;

	}
}
