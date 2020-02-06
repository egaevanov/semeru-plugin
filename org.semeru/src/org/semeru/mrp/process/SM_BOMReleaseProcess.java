package org.semeru.mrp.process;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;

import org.compiere.model.MProduct;
import org.compiere.model.MProduction;
import org.compiere.process.ProcessInfoParameter;
import org.compiere.process.SvrProcess;
import org.compiere.util.DB;
import org.semeru.mrp.model.X_SM_BOMRelease;
import org.semeru.mrp.model.X_SM_MPSLine;

public class SM_BOMReleaseProcess extends SvrProcess{

	
	private int p_M_Locator_ID = 0;
	
	@Override
	protected void prepare() {
		
		ProcessInfoParameter[] para = getParameter();
		for (ProcessInfoParameter p:para) {
			String name = p.getParameterName();
			if (p.getParameter() == null)
				;
			else if(name.equals("M_Locator_ID")){
				p_M_Locator_ID = p.getParameterAsInt();
		}
			
			
		}
		
	}

	@Override
	protected String doIt() throws Exception {

		StringBuilder SQLGetData = new StringBuilder();
		SQLGetData.append("SELECT SM_MPSLine_ID,SM_BOMRelease_ID RequiredDate,M_Prodcution_ID,SM_FactoryPlan,LOTNumber,M_Product_ID,Qty ");
		SQLGetData.append(" FROM SM_BOMRelease ");
		SQLGetData.append(" WHERE EXIST ");
		SQLGetData.append(" (SELECT ViewID FROM T_Selection WHERE T_Selection.AD_PInstance_ID=? AND CAST(T_Selection.ViewID AS INTEGER)=SM_BOMRelease.SM_MPSLine_ID) ");		
		
		
		PreparedStatement pstmt = null;
     	ResultSet rs = null;
			try {
				pstmt = DB.prepareStatement(SQLGetData.toString(), null);
				rs = pstmt.executeQuery();
				while (rs.next()) {
					
					X_SM_MPSLine MPSLine = new X_SM_MPSLine(getCtx(), rs.getInt(1), get_TrxName());
					X_SM_BOMRelease bom =new X_SM_BOMRelease(getCtx(), rs.getInt(2), get_TrxName());
					MProduction production = new MProduction(getCtx(), 0, get_TrxName());
												
					MProduct prod = new MProduct(getCtx(), bom.getM_Product_ID(), get_TrxName());
					production.setAD_Org_ID(MPSLine.getAD_Org_ID());
					production.setName("Produksi : "+prod.getName());
					production.setDescription(prod.getName());
					production.setMovementDate(bom.getRequiredDate());
					production.setM_Product_ID(prod.getM_Product_ID());
					production.setM_Locator_ID(p_M_Locator_ID);
					//production.setProductionQty(MPSLine.getQty());
					//production.setC_OrderLine_ID(MPSLine.get_ValueAsInt("C_OrderLine_ID"));
					//production.set_CustomColumn("SM_MPSLine_ID", bom.getSM_MPSLine_ID());

					production.saveEx();	
					
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
