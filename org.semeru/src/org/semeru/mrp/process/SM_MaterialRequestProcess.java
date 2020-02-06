package org.semeru.mrp.process;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;

import org.compiere.model.MRequisition;
import org.compiere.model.MRequisitionLine;
import org.compiere.process.ProcessInfoParameter;
import org.compiere.process.SvrProcess;
import org.compiere.util.DB;
import org.compiere.util.Env;
import org.semeru.mrp.model.X_SM_MPSLine;
import org.semeru.mrp.model.X_SM_MaterialRequestList;

public class SM_MaterialRequestProcess extends SvrProcess {

	private int p_SM_MPSLine_ID = 0;

	
	@Override
	protected void prepare() {
		
		ProcessInfoParameter[] para = getParameter();
		for (ProcessInfoParameter p:para) {
			String name = p.getParameterName();
			if (p.getParameter() == null)
				;
			else if(name.equals("SM_MPSLine_ID")){
				p_SM_MPSLine_ID = p.getParameterAsInt();
			}
			
			
		}
		
		
	}

	@Override
	protected String doIt() throws Exception {

		StringBuilder SQLGetData = new StringBuilder();
		SQLGetData.append("SELECT SM_MPSLine_ID,SM_MaterialRequestList_ID RequiredDate,M_Prodcution_ID,SM_FactoryPlan,LOTNumber,M_Product_ID,Qty ");
		SQLGetData.append(" FROM SM_MaterialRequestList ");
		SQLGetData.append(" WHERE EXIST ");
		SQLGetData.append(" (SELECT ViewID FROM T_Selection WHERE T_Selection.AD_PInstance_ID=? AND CAST(T_Selection.ViewID AS INTEGER)=SM_MaterialRequestList.SM_MPSLine_ID) ");		
		
		
		X_SM_MPSLine MPSLine = new X_SM_MPSLine(getCtx(), p_SM_MPSLine_ID, get_TrxName());
		
		
		MRequisition requisition = new MRequisition(getCtx(), 0, get_TrxName());
		requisition.setAD_Org_ID(MPSLine.getAD_Org_ID());
		requisition.setC_DocType_ID(1);
		requisition.setAD_User_ID(Env.getAD_User_ID(getCtx()));
		//requisition.setDescription("MPS Line : "+ MPSLine.getName());
		//requisition.setDateRequired(MPSLine.getStartDate());
		requisition.setM_Warehouse_ID(11);
		requisition.setM_PriceList_ID(1);
		//requisition.setDateDoc(MPSLine.getStartDate());		
		requisition.saveEx();
		
		PreparedStatement pstmt = null;
     	ResultSet rs = null;
			try {
				pstmt = DB.prepareStatement(SQLGetData.toString(), null);
				rs = pstmt.executeQuery();
				while (rs.next()) {
					MRequisitionLine requisitionLine = new MRequisitionLine(getCtx(), 0, get_TrxName());
					X_SM_MaterialRequestList MRL = new X_SM_MaterialRequestList(getCtx(), rs.getInt(2), get_TrxName());
					requisitionLine.setAD_Org_ID(requisition.getAD_Org_ID());
					requisitionLine.setC_BPartner_ID(0);
					requisitionLine.setM_Product_ID(MRL.getM_Product_ID());
					requisitionLine.setQty(MRL.getQty());
					requisitionLine.setDescription("");
					requisitionLine.set_CustomColumn("SM_MPSLine_ID", MRL.getSM_MPSLine_ID());
					requisitionLine.saveEx();
				
				}

			} catch (SQLException err) {
				log.log(Level.SEVERE, SQLGetData.toString(), err);
			} finally {
				DB.close(rs, pstmt);
				rs = null;
				pstmt = null;
			}
		
		return null;
	}

}
