package org.semeru.component;

import java.lang.reflect.Constructor;
import java.sql.ResultSet;
import java.util.HashMap;
import java.util.Properties;

import org.adempiere.base.IModelFactory;
import org.compiere.model.I_C_Invoice;
import org.compiere.model.I_C_POS;
import org.compiere.model.PO;
import org.compiere.util.Env;
import org.semeru.model.I_I_Brand_Upload;
import org.semeru.model.I_I_Master_Temp;
import org.semeru.model.I_I_Product_Stock;
import org.semeru.model.I_I_Setup_Temp;
import org.semeru.model.I_I_TransactionExt_Temp;
import org.semeru.model.I_M_Brand;
import org.semeru.model.I_M_Sales_Locator;
import org.semeru.model.I_SMR_CashMethod;
import org.semeru.model.I_SMR_LoginAuth;
import org.semeru.model.I_i_ordertrx_temp;
import org.semeru.mrp.model.I_SM_DRP;
import org.semeru.mrp.model.I_SM_MRP;

/**
 * 
 * @author Tegar N
 *
 */

public class SMR_ModelFactory implements IModelFactory{

	private static HashMap<String, String> mapTableModels = new HashMap<String, String>();
	static
	{
		
		mapTableModels.put(I_I_Master_Temp.Table_Name, "org.semeru.model.X_I_Master_Temp");
		mapTableModels.put(I_I_TransactionExt_Temp.Table_Name, "org.semeru.model.X_I_TransactionExt_Temp");
		mapTableModels.put(I_M_Sales_Locator.Table_Name, "org.semeru.model.X_M_Sales_Locator");
		mapTableModels.put(I_i_ordertrx_temp.Table_Name, "org.semeru.model.X_i_ordertrx_temp");
		mapTableModels.put(I_M_Brand.Table_Name, "org.semeru.model.X_M_Brand");
		
		mapTableModels.put(I_SM_DRP.Table_Name, "org.semeru.model.X_SM_DRP");
		mapTableModels.put(I_SM_MRP.Table_Name, "org.semeru.model.X_SM_MRP");
		
		mapTableModels.put(I_SMR_CashMethod.Table_Name, "org.semeru.model.X_SMR_CashMethod");
		mapTableModels.put(I_SMR_LoginAuth.Table_Name, "org.semeru.model.X_SMR_LoginAuth");

		mapTableModels.put(I_I_Product_Stock.Table_Name, "org.semeru.model.X_I_Product_Stock");

		mapTableModels.put(I_C_Invoice.Table_Name, "org.semeru.model.MInvoiceSemeru");
		mapTableModels.put(I_C_POS.Table_Name, "org.semeru.model.X_C_POS");
		mapTableModels.put(I_I_Setup_Temp.Table_Name, "org.semeru.model.X_I_Setup_Temp");
		mapTableModels.put(I_I_Brand_Upload.Table_Name, "org.semeru.model.X_I_Brand_Upload");


		
			
	}
	
	@Override
	public Class<?> getClass(String tableName) {
		
		if (mapTableModels.containsKey(tableName)) {
			Class<?> act = null;
			try {
				act = Class.forName(mapTableModels.get(tableName));
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			}
				return act;
		
		} else 
			return null;
	}

	@Override
	public PO getPO(String tableName, int Record_ID, String trxName) {
		
		if (mapTableModels.containsKey(tableName)) {
			Class<?> clazz = null;
			Constructor<?> ctor = null;
			PO object = null;
			try {
				clazz = Class.forName(mapTableModels.get(tableName));
				ctor = clazz.getDeclaredConstructor(Properties.class, int.class, String.class);
				object = (PO) ctor.newInstance(new Object[] {Env.getCtx(), Record_ID, trxName});
				
			} catch (Exception e) {
				e.printStackTrace();
			}
				return object;
		} else 	   
		   return null;
	}

	@Override
	public PO getPO(String tableName, ResultSet rs, String trxName) {
	
		if (mapTableModels.containsKey(tableName)) {
			Class<?> clazz = null;
			Constructor<?> ctor = null;
			PO object = null;
			try {
				clazz = Class.forName(mapTableModels.get(tableName));
				ctor = clazz.getDeclaredConstructor(Properties.class, ResultSet.class, String.class);
				object = (PO) ctor.newInstance(new Object[] {Env.getCtx(), rs, trxName});
				
			} catch (Exception e) {
				e.printStackTrace();
			}
				return object;
				
		} else  
			return null;
	}

	
}
