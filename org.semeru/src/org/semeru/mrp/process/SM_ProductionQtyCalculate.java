package org.semeru.mrp.process;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;

import org.compiere.process.SvrProcess;
import org.compiere.util.DB;
import org.semeru.mrp.model.X_SM_Production_Qty;

public class SM_ProductionQtyCalculate extends SvrProcess{

	
	private int SM_Product_PlanLine_ID = 0;

	@Override
	protected void prepare() {

		
		SM_Product_PlanLine_ID = getRecord_ID();

	}

	@Override
	protected String doIt() throws Exception {

		
		StringBuilder getFromView3 = new StringBuilder();
		getFromView3.append(" SELECT AD_Org_ID, SM_Product_PlanLine_ID, M_Product_ID, C_Period_ID,ord_qty,prd_qty,ohn_qty ");
		getFromView3.append(" FROM sm_production_qty_draft_v ");
		getFromView3.append(" WHERE sm_product_planline_id =  "+SM_Product_PlanLine_ID);
	
		PreparedStatement pstmt3 = null;
     	ResultSet rs3 = null;
			try {
				pstmt3 = DB.prepareStatement(getFromView3.toString(), null);
				rs3 = pstmt3.executeQuery();
				while (rs3.next()) {
					

					X_SM_Production_Qty prodQty = new X_SM_Production_Qty(getCtx(), 0, get_TrxName());
					prodQty.setAD_Org_ID(rs3.getInt(1));
					prodQty.setSM_Product_PlanLine_ID(rs3.getInt(2));
					prodQty.setM_Product_ID(rs3.getInt(3));
					prodQty.setC_Period_ID(rs3.getInt(4));
					prodQty.setcustorderqty(rs3.getBigDecimal(5));
					prodQty.setProductionQty(rs3.getBigDecimal(6));
					prodQty.setinventoryqty(rs3.getBigDecimal(7));

				
					
					prodQty.saveEx();
					
				}

			} catch (SQLException err) {
				log.log(Level.SEVERE, getFromView3.toString(), err);
			} finally {
				DB.close(rs3, pstmt3);
				rs3 = null;
				pstmt3 = null;
			}
		
		return "";
	}

}
