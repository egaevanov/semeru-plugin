package org.semeru.mrp.callout;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Properties;
import java.util.logging.Level;

import org.adempiere.base.IColumnCallout;
import org.compiere.model.CalloutEngine;
import org.compiere.model.GridField;
import org.compiere.model.GridTab;
import org.compiere.util.DB;
import org.semeru.mrp.model.I_SM_MPS;

public class SM_CallOutMPS   extends CalloutEngine implements IColumnCallout  {

	@Override
	public String start(Properties ctx, int WindowNo, GridTab mTab, GridField mField, Object value, Object oldValue) {

		if(mField.getColumnName().equals(I_SM_MPS.COLUMNNAME_SM_Product_PlanLine_ID)){
			return callOutProductPlan(ctx, WindowNo, mTab, mField, value);
		}		
		
		return null;
	}
	
	
public String callOutProductPlan (Properties ctx, int WindowNo, GridTab mTab, GridField mField, Object value){
		
		Integer SM_Product_PlanLine_ID = (Integer)mTab.getValue("SM_Product_PlanLine_ID");
		
		StringBuilder SQLGetData = new StringBuilder();
		
		SQLGetData.append("SELECT m_product_id,ohnqty,safetystock,lotsizemethod,leadtime ");
		SQLGetData.append(" FROM sm_mps_draft_v ");
		SQLGetData.append(" WHERE SM_Product_PlanLine_ID = "+SM_Product_PlanLine_ID);
		
		PreparedStatement pstmt = null;
     	ResultSet rs = null;
			try {
				pstmt = DB.prepareStatement(SQLGetData.toString(), null);
				rs = pstmt.executeQuery();
				while (rs.next()) {
					
					mTab.setValue(I_SM_MPS.COLUMNNAME_M_Product_ID, rs.getInt(1));
					mTab.setValue(I_SM_MPS.COLUMNNAME_ohnqty, rs.getBigDecimal(2));
					mTab.setValue(I_SM_MPS.COLUMNNAME_SafetyStock, rs.getBigDecimal(3));
					mTab.setValue(I_SM_MPS.COLUMNNAME_lotsizemethod, rs.getString(4));
					mTab.setValue(I_SM_MPS.COLUMNNAME_leadtime, rs.getInt(5));
				
				}
				
					
				

			} catch (SQLException err) {
				log.log(Level.SEVERE, SQLGetData.toString(), err);
			} finally {
				DB.close(rs, pstmt);
				rs = null;
				pstmt = null;
			}
		
				
	return"";
	
	}

}
