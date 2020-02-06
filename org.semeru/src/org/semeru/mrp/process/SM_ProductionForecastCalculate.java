package org.semeru.mrp.process;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;

import org.compiere.process.SvrProcess;
import org.compiere.util.DB;
import org.semeru.mrp.model.X_SM_COLine;
import org.semeru.mrp.model.X_SM_Forecast;
import org.semeru.mrp.model.X_SM_Product_PlanLine;

public class SM_ProductionForecastCalculate extends SvrProcess {

	
	private int SM_Product_PlanLine_ID = 0;
	
	@Override
	protected void prepare() {

			
		SM_Product_PlanLine_ID = getRecord_ID();
		
		
	}

	@Override
	protected String doIt() throws Exception {

		X_SM_Product_PlanLine line = new X_SM_Product_PlanLine(getCtx(), SM_Product_PlanLine_ID, get_TrxName());
//		X_SM_Product_Plan header  = new X_SM_Product_Plan(getCtx(), line.get_ValueAsInt("SM_Product_Plan"), get_TrxName());
//		MPeriod period = new MPeriod(getCtx(), header.getC_Period_ID(), get_TrxName());
		
		
		
		StringBuilder deleteCoLine = new StringBuilder();
		deleteCoLine.append("DELETE FROM SM_Coline ");
		deleteCoLine.append(" WHERE IsDtf = 'N'");
		DB.executeUpdate(deleteCoLine.toString(),null);
		
		StringBuilder deleteForeCast = new StringBuilder();
		deleteForeCast.append("DELETE FROM SM_ForeCast ");
		DB.executeUpdate(deleteForeCast.toString(),null);
		
		StringBuilder deleteProdQty = new StringBuilder();
		deleteProdQty.append("DELETE FROM SM_Production_Qty ");
		DB.executeUpdate(deleteProdQty.toString(),null);


		
		StringBuilder getFromView1 = new StringBuilder();
		getFromView1.append(" SELECT SM_Product_PlanLine_ID, AD_Org_ID, C_Order_ID, C_BPartner_ID, C_Period_ID, DateOrdered, DatePromised,C_OrderLine_ID, M_Product_ID,Qty,StartDate, EndDate ");
		getFromView1.append(" FROM sm_coline_draft_v ");
		getFromView1.append(" WHERE SM_Product_PlanLine_ID ="+line.getSM_Product_PlanLine_ID());
		

		PreparedStatement pstmt = null;
     	ResultSet rs = null;
			try {
				pstmt = DB.prepareStatement(getFromView1.toString(), null);
				rs = pstmt.executeQuery();
				while (rs.next()) {
					
					X_SM_COLine coline = new X_SM_COLine(getCtx(), 0, get_TrxName());
					coline.setSM_Product_PlanLine_ID(rs.getInt(1));
					coline.setAD_Org_ID(rs.getInt(2));
					coline.setC_Order_ID(rs.getInt(3));
					coline.setC_BPartner_ID(rs.getInt(4));
					coline.setC_Period_ID(rs.getInt(5));
					coline.setDateOrdered(rs.getTimestamp(6));
					coline.setDatePromised(rs.getTimestamp(7));
					coline.setC_OrderLine_ID(rs.getInt(8));
					coline.setM_Product_ID(rs.getInt(9));
					coline.setQtyEntered(rs.getBigDecimal(10));
					coline.setStartDate(rs.getTimestamp(11));
					coline.setEndDate(rs.getTimestamp(12));

					coline.saveEx();

					
				}

			} catch (SQLException err) {
				log.log(Level.SEVERE, getFromView1.toString(), err);
			} finally {
				DB.close(rs, pstmt);
				rs = null;
				pstmt = null;
			}
		

		
			
			StringBuilder getFromView2 = new StringBuilder();
			getFromView2.append(" SELECT AD_Org_ID, SM_Product_PlanLine_ID, M_Product_ID, C_Period_ID, Qty ");
			getFromView2.append(" FROM sm_forecast_draft_v ");
			getFromView2.append(" WHERE sm_product_planline_id = "+SM_Product_PlanLine_ID);
			

			PreparedStatement pstmt2 = null;
	     	ResultSet rs2 = null;
				try {
					pstmt2 = DB.prepareStatement(getFromView2.toString(), null);
					rs2 = pstmt2.executeQuery();
					while (rs2.next()) {
						
						X_SM_Forecast Forecast = new X_SM_Forecast(getCtx(), 0, get_TrxName());
						Forecast.setAD_Org_ID(rs2.getInt(1));
						Forecast.setSM_Product_PlanLine_ID(rs2.getInt(2));
						Forecast.setM_Product_ID(rs2.getInt(3));
						Forecast.setC_Period_ID(rs2.getInt(4));
						Forecast.setQty(rs2.getBigDecimal(5));
						Forecast.saveEx();
						
					}

				} catch (SQLException err) {
					log.log(Level.SEVERE, getFromView2.toString(), err);
				} finally {
					DB.close(rs2, pstmt2);
					rs2 = null;
					pstmt2 = null;
				}
		

		
		return "";
	}

}
