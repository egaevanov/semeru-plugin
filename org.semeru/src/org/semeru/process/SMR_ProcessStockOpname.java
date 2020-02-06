package org.semeru.process;

import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.logging.Level;

import org.compiere.model.MDocType;
import org.compiere.model.MInventory;
import org.compiere.model.MInventoryLine;
import org.compiere.model.MLocator;
import org.compiere.process.ProcessInfoParameter;
import org.compiere.process.SvrProcess;
import org.compiere.util.DB;
import org.compiere.util.Env;
import org.semeru.model.X_I_Product_Stock;

public class SMR_ProcessStockOpname extends SvrProcess{

	private int p_AD_Org_ID = 0;
	
	@Override
	protected void prepare() {
		
		ProcessInfoParameter[] para = getParameter();
		for (int i = 0; i < para.length; i++)
		{
			String name = para[i].getParameterName();
			if (para[i].getParameter() == null);
			
			else if(name.equals("AD_Org_ID"))
				p_AD_Org_ID = (int)para[i].getParameterAsInt();
			
			else
				log.log(Level.SEVERE, "Unknown Parameter: " + name);
		}
		
	}
	

	@Override
	protected String doIt() throws Exception {
		
		
		if(p_AD_Org_ID <= 0) {			
			return "Organization Tidak Ada";
		}
		
		StringBuilder SQLGetData = new StringBuilder();
		SQLGetData.append("SELECT M_Product_ID, Locator_Value,QtyCount,I_Product_Stock_ID");
		SQLGetData.append(" FROM  I_Product_Stock ");
		SQLGetData.append(" WHERE AD_Client_ID = " + getAD_Client_ID() );
		SQLGetData.append(" AND AD_Org_ID = " + p_AD_Org_ID);
		SQLGetData.append(" AND File_Name Is NULL ");
		SQLGetData.append(" AND IsImportProduct = 'Y'");
		SQLGetData.append(" AND IsImportStock = 'N'");

		
		String rslt = "";
		
		PreparedStatement pstmt = null;
     	ResultSet rs = null;
			try {
				pstmt = DB.prepareStatement(SQLGetData.toString(), null);
				rs = pstmt.executeQuery();
				while (rs.next()) {
					
					MInventory Inven = new MInventory(getCtx(), 0, get_TrxName());
					MInventoryLine InvenLine = null;
					Timestamp dateMove = new Timestamp(System.currentTimeMillis());
					
					Integer DocType_ID = DataSetupValidation.getDocType_ID(Env.getAD_Client_ID(getCtx()), MDocType.DOCBASETYPE_MaterialPhysicalInventory, "N",MDocType.DOCSUBTYPEINV_PhysicalInventory);
					Integer M_Locator_ID = DataSetupValidation.getIDFilterOrg(getAD_Client_ID(), MLocator.Table_Name, MLocator.COLUMNNAME_Value, 0, rs.getString(2),p_AD_Org_ID);
					int M_Product_ID = rs.getInt(1);
					int I_Product_Stock_ID =  rs.getInt(4);
					
					MLocator loc = new MLocator(getCtx(), M_Locator_ID, get_TrxName());
									
					Inven.setAD_Org_ID(p_AD_Org_ID);
					Inven.setM_Warehouse_ID(loc.getM_Warehouse_ID());
					Inven.setMovementDate(dateMove);
					Inven.setC_DocType_ID(DocType_ID);
					Inven.setDescription("Stock Opname");
					Inven.saveEx();
					if(Inven != null){
											
							InvenLine =  new MInventoryLine(getCtx(), 0, get_TrxName());
							InvenLine.setAD_Org_ID(p_AD_Org_ID);
							InvenLine.setM_Inventory_ID(Inven.getM_Inventory_ID());
							InvenLine.setM_Locator_ID(loc.getM_Locator_ID());
							InvenLine.setM_Product_ID(M_Product_ID);
							InvenLine.setInventoryType("D");
							
							BigDecimal qtyBook = DataSetupValidation.setQtyBook(0, M_Product_ID, loc.getM_Locator_ID());
							
							InvenLine.setQtyBook(qtyBook);
							InvenLine.setQtyCount(rs.getBigDecimal(3));
							InvenLine.saveEx();
												    	
					}
					
					if(Inven.processIt(MInventory.ACTION_Complete)) {
			    		X_I_Product_Stock tempProdStock = new X_I_Product_Stock(getCtx(), I_Product_Stock_ID, get_TrxName());
			    		tempProdStock.setIsImportStock(true);
			    		tempProdStock.saveEx();	   		
			    	}
				}
				
				} catch (SQLException err) {
				
					log.log(Level.SEVERE, SQLGetData.toString(), err);
					rollback();
					
				} finally {
					DB.close(rs, pstmt);
					rs = null;
					pstmt = null;
					
					
				}
							
 
		return rslt;
	}

}
