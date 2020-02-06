package org.semeru.mrp.callout;

import java.math.BigDecimal;
import java.util.Properties;

import org.adempiere.base.IColumnCallout;
import org.compiere.model.CalloutEngine;
import org.compiere.model.GridField;
import org.compiere.model.GridTab;
import org.compiere.util.DB;
import org.semeru.mrp.model.I_SM_Product_PlanLine;

public class SM_CalloutProdPlan extends CalloutEngine implements IColumnCallout {

	@Override
	public String start(Properties ctx, int WindowNo, GridTab mTab, GridField mField, Object value, Object oldValue) {

		if(mField.getColumnName().equals(I_SM_Product_PlanLine.COLUMNNAME_M_Product_ID)){
			return callOutProduct(ctx, WindowNo, mTab, mField, value);
		}		
		
		
		return null;
	}
	
	
	public String callOutProduct (Properties ctx, int WindowNo, GridTab mTab, GridField mField, Object value){
		
		Integer M_Product_ID = (Integer)mTab.getValue("M_Product_ID");
		Integer SM_Product_Plan_ID = (Integer)mTab.getValue("SM_Product_Plan_ID");
		
		StringBuilder SQLGetQty = new StringBuilder();
		
		SQLGetQty.append("SELECT Qty ");
		SQLGetQty.append(" FROM sm_product_planline_draft_v ");
		SQLGetQty.append(" WHERE sm_product_plan_id = "+SM_Product_Plan_ID);
		SQLGetQty.append(" AND m_product_id = "+M_Product_ID);
		
		BigDecimal Qty = DB.getSQLValueBD(null, SQLGetQty.toString());
		mTab.setValue(I_SM_Product_PlanLine.COLUMNNAME_Qty, Qty);


		
		StringBuilder SQLGetProdLine = new StringBuilder();
		
		SQLGetProdLine.append("SELECT m_product_line_id ");
		SQLGetProdLine.append(" FROM sm_product_planline_draft_v ");
		SQLGetProdLine.append(" WHERE sm_product_plan_id = "+SM_Product_Plan_ID);
		SQLGetProdLine.append(" AND m_product_id = "+M_Product_ID);
		Integer prodLine = DB.getSQLValueEx(null, SQLGetProdLine.toString());
		mTab.setValue(I_SM_Product_PlanLine.COLUMNNAME_M_Product_Line_ID, prodLine);
		
				
	return"";
	
	}

}
