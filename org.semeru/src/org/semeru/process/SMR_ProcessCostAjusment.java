package org.semeru.process;

import java.util.logging.Level;

import org.compiere.model.MAcctSchema;
import org.compiere.model.MCost;
import org.compiere.model.MCostElement;
import org.compiere.model.MProduct;
import org.compiere.process.ProcessInfoParameter;
import org.compiere.process.SvrProcess;

public class SMR_ProcessCostAjusment extends SvrProcess{

	
	private int p_M_Produt_ID = 0;
	
	@Override
	protected void prepare() {

		ProcessInfoParameter[] para = getParameter();
		for (int i = 0; i < para.length; i++)
		{
			String name = para[i].getParameterName();
			if (para[i].getParameter() == null);
			
			else if(name.equals("M_Product_ID"))
				p_M_Produt_ID = (int)para[i].getParameterAsInt();
		
			
			else
				log.log(Level.SEVERE, "Unknown Parameter: " + name);
		}
		
	}

	@Override
	protected String doIt() throws Exception {

		MProduct mp = new MProduct(getCtx(), p_M_Produt_ID, get_TrxName());
		Integer elementid = DataSetupValidation.getID(getAD_Client_ID(), MCostElement.Table_Name, MCostElement.COLUMNNAME_CostingMethod, 0, MCostElement.COSTINGMETHOD_AveragePO);
		Integer asid = DataSetupValidation.getID(getAD_Client_ID(), MAcctSchema.Table_Name, MAcctSchema.COLUMNNAME_CostingMethod, 0, MAcctSchema.COSTINGMETHOD_AveragePO);
		
		MAcctSchema as = new MAcctSchema(getCtx(),asid , get_TrxName());					
		MCost cost = new MCost(mp, 0, as, 0, elementid);
		cost.saveEx();
		return null;
	}
	

}
