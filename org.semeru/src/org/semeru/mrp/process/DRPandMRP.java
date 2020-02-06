package org.semeru.mrp.process;

import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;

import org.compiere.model.MTable;
import org.compiere.process.ProcessInfoParameter;
import org.compiere.process.SvrProcess;
import org.compiere.util.DB;
import org.semeru.mrp.model.X_SM_DRP;
import org.semeru.mrp.model.X_SM_DRPLine;
import org.semeru.mrp.model.X_SM_MRP;

public class DRPandMRP extends SvrProcess{

	private int p_AD_Client_ID = 0;
	private int p_AD_Org_ID = 0;
	private int p_Record_ID = 0;
	private int p_AD_Table_ID = 0;
	private int p_C_Period_ID = 0;

	private boolean IsDRP = false;
	private boolean IsMRP = false;
	private X_SM_DRP DRP = null;
	private X_SM_MRP MRP = null;

	@Override
	protected void prepare() {
		
		ProcessInfoParameter[] para = getParameter();

		for (int i = 0 ; i < para.length ;i++){
			
			String name = para[i].getParameterName();
			
			if(para[i].getParameter()==null)
				;
			else if(name.equals("AD_Client_ID"))
				p_AD_Client_ID = (int)para[i].getParameterAsInt();
			
			else if(name.equals("AD_Org_ID"))
				p_AD_Org_ID = (int)para[i].getParameterAsInt();
			
			else if(name.equals("C_Period_ID"))
				p_C_Period_ID = (int)para[i].getParameterAsInt();
	
			else
				log.log(Level.SEVERE, "Unknown Parameter: " + name);
			
		}

		p_Record_ID = getRecord_ID();
		p_AD_Table_ID = getTable_ID();
		
		
	}

	@Override
	protected String doIt() throws Exception {

		MTable table  = new MTable(getCtx(), p_AD_Table_ID, get_TrxName());
		
		StringBuilder deleteLine = new StringBuilder();
		deleteLine.append("DELETE FROM ");
		
		
		if(table.getTableName().equals(X_SM_DRP.Table_Name)) {
			IsDRP = true;
			DRP = new X_SM_DRP(getCtx(), p_Record_ID, get_TrxName());
			
			deleteLine.append(" SM_DRPLine");
			deleteLine.append(" WHERE SM_DRP_ID ="+DRP.getSM_DRP_ID());

		
		}else if(table.getTableName().equals(X_SM_MRP.Table_Name)) {
			IsMRP = true;
			MRP = new X_SM_MRP(getCtx(), p_Record_ID, get_TrxName());
			
			deleteLine.append(" SM_MRPLine");
			deleteLine.append(" WHERE SM_MRP_ID ="+MRP.getSM_MRP_ID());


		}
		
		DB.executeUpdate(deleteLine.toString(), get_TrxName());

		StringBuilder SQLGetSimulation = new StringBuilder();
		SQLGetSimulation.append("SELECT AD_Client_ID,AD_Org_ID,"); //1,2
		SQLGetSimulation.append(" C_BPartner_ID,M_Product_ID,"); //3,4
		SQLGetSimulation.append(" M_Locator_ID,Qty,"); //5,6
		SQLGetSimulation.append(" C_Order_ID,C_OrderLine_ID,"); //7,8
		SQLGetSimulation.append(" C_DocTypeTarget_ID,C_Period_ID"); //9,10
		if(IsDRP) {
			SQLGetSimulation.append(" FROM sm_drp_simulation_v "); //9,10
		}else if(IsMRP) {
			SQLGetSimulation.append(" FROM sm_mrp_simulation_v "); //9,10
		}
		SQLGetSimulation.append(" WHERE AD_Client_ID = ? ");
		SQLGetSimulation.append(" AND AD_Org_ID = ?");
		SQLGetSimulation.append(" AND C_Period_ID = ?");
		
		PreparedStatement pstmt = null;
     	ResultSet rs = null;
			try {
				pstmt = DB.prepareStatement(SQLGetSimulation.toString(), null);
				pstmt.setInt(1, p_AD_Client_ID);
				pstmt.setInt(2, p_AD_Org_ID);
				pstmt.setInt(3, p_C_Period_ID);
				rs = pstmt.executeQuery();
				
				int count = 1;
				while (rs.next()) {		
					
					//int AD_Org_ID = rs.getInt(2);
					int C_BPartner_ID = rs.getInt(3);
					int M_Product_ID = rs.getInt(4);
					int M_Locator_ID = rs.getInt(5);
					BigDecimal Qty = rs.getBigDecimal(6);
					int Ref_ID = rs.getInt(7);
					int RefLine_ID = rs.getInt(8);
					int C_DocType_ID = rs.getInt(9);
					//int C_Period_ID = rs.getInt(10);

					
					if(IsDRP) {
						X_SM_DRPLine DRPLine= new X_SM_DRPLine(getCtx(), 0, get_TrxName());	
						
						DRPLine.setAD_Org_ID(DRP.getAD_Org_ID());
						DRPLine.setSM_DRP_ID(DRP.getSM_DRP_ID());
						DRPLine.setM_Product_ID(M_Product_ID);
						DRPLine.setC_BPartner_ID(C_BPartner_ID);
						DRPLine.setM_Locator_ID(M_Locator_ID);
						DRPLine.setQty(Qty);
						DRPLine.set_CustomColumn("C_Order_ID", Ref_ID);
						DRPLine.set_CustomColumn("C_OrderLine_ID", RefLine_ID);
						DRPLine.setC_DocType_ID(C_DocType_ID);
						DRPLine.setName("Line "+count);
						DRPLine.setDescription("Line "+count);
						DRPLine.saveEx();
							
					}else if(IsMRP) {
//						X_SM_MRPLine MRPLine= new X_SM_MRPLine(getCtx(), 0, get_TrxName());
						
//						MRPLine.setAD_Org_ID(MRP.getAD_Org_ID());
//						MRPLine.setSM_MRP_ID(MRP.getSM_MRP_ID());
//						MRPLine.setM_Product_ID(M_Product_ID);
//						MRPLine.setC_BPartner_ID(C_BPartner_ID);
//						MRPLine.setM_Locator_ID(M_Locator_ID);
//						MRPLine.setQty(Qty);
//						MRPLine.set_CustomColumn("C_OrderLine_ID", RefLine_ID);
//						MRPLine.setC_DocType_ID(C_DocType_ID);
//						MRPLine.setName("Line "+count);
//						MRPLine.setDescription("Line "+count);
//						MRPLine.saveEx();
						
					}
						
					count++;
				}

			} catch (SQLException err) {
				log.log(Level.SEVERE, SQLGetSimulation.toString(), err);
				rollback();
			} finally {
				DB.close(rs, pstmt);
				rs = null;
				pstmt = null;
			}
			
			
		return null;
	}

}
