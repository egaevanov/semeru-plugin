package org.semeru.process;

import java.io.File;
import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.logging.Level;

import org.compiere.model.MAcctSchema;
import org.compiere.model.MCost;
import org.compiere.model.MCostElement;
import org.compiere.model.MOrg;
import org.compiere.model.MProduct;
import org.compiere.model.MProductCategory;
import org.compiere.model.MProductPrice;
import org.compiere.model.MTaxCategory;
import org.compiere.model.MUOM;
import org.compiere.process.ProcessInfoParameter;
import org.compiere.process.SvrProcess;
import org.compiere.util.CacheMgt;
import org.compiere.util.DB;
import org.compiere.util.Env;
import org.semeru.model.X_I_Product_Stock;
import org.semeru.model.X_M_Brand;
import org.semeru.ws.model.SMR_Model_ProductStock;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonParser;

public class SMR_UploadProduct extends SvrProcess{

	
	String directory = org.compiere.Adempiere.getAdempiereHome() 
			+ File.separator + "data" 
			+ File.separator + "import"
			+ File.separator;

//	private String p_File_Name ="";
//			"[{\"Client\":\"\",\"Organization\":\"WTJ\",\"Product_Value\":\"Sampoerna Mild 1\",\"Product_Name\":\"Sampoerna Mild 1\",\"UOM_Value\":\"Bks\",\"ProductCategoryKey\":\"Rokok\",\"ProductType\":\"I\",\"SalesPrice\":24000,\"WholesalerPrice\":23000,\"PurchasePrice\":22000,\"LocatorValue\":\"Lokasi Cibubur\",\"QuantityCount\":120,\"TaxType\":\"PPN\"},{\"Client\":\"\",\"Organization\":\"WTJ\",\"Product_Value\":\"Sampoerna Menthol 1\",\"Product_Name\":\"Sampoerna Menthol 1\",\"UOM_Value\":\"Bks\",\"ProductCategoryKey\":\"Rokok\",\"ProductType\":\"I\",\"SalesPrice\":24000,\"WholesalerPrice\":23000,\"PurchasePrice\":22000,\"LocatorValue\":\"Lokasi Cibubur\",\"QuantityCount\":50,\"TaxType\":\"PPN\"}]";
	
	private int p_AD_Client_ID = 0;
	private int p_AD_Org_ID = 0;
	private int p_Process_ID = 0;


	@Override
	protected void prepare() {
		
		ProcessInfoParameter[] para = getParameter();
		for (int i = 0; i < para.length; i++)
		{
			String name = para[i].getParameterName();
			if (para[i].getParameter() == null);
			
			else if(name.equals("AD_Client_ID"))
				p_AD_Client_ID = (int)para[i].getParameterAsInt();
			else if(name.equals("AD_Org_ID"))
				p_AD_Org_ID = (int)para[i].getParameterAsInt();
			else if(name.equals("process_id"))
				p_Process_ID = (int)para[i].getParameterAsInt();
			
			else
				log.log(Level.SEVERE, "Unknown Parameter: " + name);
		}
		
		
	}

	@Override
	protected String doIt() throws Exception {
		String rslt = "";
		
		StringBuilder SQLExtractJSon = new StringBuilder();
		SQLExtractJSon.append("SELECT AD_Client_ID,AD_Org_ID,I_Product_Stock_ID,File_Name ");
		SQLExtractJSon.append(" FROM  I_Product_Stock ");
		SQLExtractJSon.append(" WHERE AD_Client_ID = " + p_AD_Client_ID );
		SQLExtractJSon.append(" AND AD_Org_ID = " + p_AD_Org_ID);
		SQLExtractJSon.append(" AND IsImportStock = 'N' ");
		SQLExtractJSon.append(" AND Process_ID =  " +p_Process_ID);

		
		PreparedStatement pstmt = null;
     	ResultSet rs = null;
		
		try {
			
			pstmt = DB.prepareStatement(SQLExtractJSon.toString(), null);
			rs = pstmt.executeQuery();
			while (rs.next()) {
			
				String JSonString = rs.getString(4);
							
				Gson gson = new Gson();
				JsonArray json = new JsonArray();
				JsonParser parser = new JsonParser();
			    json = parser.parse(JSonString).getAsJsonArray();
	//		    json = asd.getAsJsonObject();		    
			    
		    	SMR_Model_ProductStock[] DataMaps = gson.fromJson(json.toString(), SMR_Model_ProductStock[].class);
		    	
		    	for (SMR_Model_ProductStock DataMap : DataMaps) {
		    	
				X_I_Product_Stock tempProdStock = new X_I_Product_Stock(getCtx(), 0, get_TrxName());
				Integer AD_Org_ID = DataSetupValidation.getID(getAD_Client_ID(), MOrg.Table_Name, MOrg.COLUMNNAME_Value, 0, DataMap.Organization);
		
				System.out.println(AD_Org_ID);
				System.out.println("Prod. Value : "+DataMap.Product_Value);
				System.out.println("prod. Name : "+DataMap.Product_Name);
				System.out.println("Loc. Value : "+DataMap.LocatorValue);
				System.out.println("Prod. Cat : "+DataMap.ProductCategoryKey);
				System.out.println("Prod. Type : "+DataMap.ProductType);
				System.out.println("Purchase Price : "+DataMap.PurchasePrice);
				System.out.println("Sales Price : "+DataMap.SalesPrice);
				System.out.println("WholeSales Price : "+DataMap.WholeSalerPrice);
				System.out.println("Qty Count : "+DataMap.QuantityCount);
				System.out.println("Tax Type : "+DataMap.TaxType);
				System.out.println("UOM Value : "+DataMap.UOM_Value);
				System.out.println("Brand Value : "+DataMap.Brand_Value);

			
				tempProdStock.setAD_Org_ID(AD_Org_ID);
				tempProdStock.setIsImportProduct(false);
				tempProdStock.setIsImportStock(false);
				tempProdStock.setLocator_Value(DataMap.LocatorValue);
				tempProdStock.setProduct_Name(DataMap.Product_Name);
				tempProdStock.setProduct_Value(DataMap.Product_Value);
				tempProdStock.setProductCategory_Value(DataMap.ProductCategoryKey);
				tempProdStock.setProductType(DataMap.ProductType);
				tempProdStock.set_CustomColumn("process_id", p_Process_ID);
				tempProdStock.set_CustomColumn("Brand_Value",DataMap.Brand_Value);

				
				String buy = DataMap.PurchasePrice;
				Double dbuy = Double.valueOf(buy);
				tempProdStock.setPurchasePrice(BigDecimal.valueOf(dbuy));
				
				String QtyCount = DataMap.QuantityCount;
				Double dQtyCount = Double.valueOf(QtyCount);
				tempProdStock.setQtyCount(BigDecimal.valueOf(dQtyCount));
				
				String rtl = DataMap.SalesPrice;
				Double drtl = Double.valueOf(rtl);
				tempProdStock.setSalesPrice(BigDecimal.valueOf(drtl));
				
				tempProdStock.setTaxType(DataMap.TaxType);
				tempProdStock.setUOM_Value(DataMap.UOM_Value);
				
				String grs = DataMap.WholeSalerPrice;
				Double dgrs = Double.valueOf(grs);
				tempProdStock.setWholesalerPrice(BigDecimal.valueOf(dgrs));
							
				tempProdStock.saveEx();
				
				MProduct product = CreatePRoduct(tempProdStock);
				
				if(product == null) {
					rollback();
				}
	    	
		    	}
	    	}
		} catch (Exception e) {
			rollback();
		}
		 finally {
				DB.close(rs, pstmt);
				rs = null;
				pstmt = null;
			}
			
		return rslt;
	}
	
	
	public  MProduct CreatePRoduct(X_I_Product_Stock tempProductStock) {
		
		MProduct rs = null;

		try {
				
			MProduct product = new MProduct(getCtx(), 0, get_TrxName());
	
			product.setAD_Org_ID(tempProductStock.getAD_Org_ID());
			
			Integer C_UOM_ID = DataSetupValidation.getIDFilterOrg(getAD_Client_ID(), MUOM.Table_Name, MUOM.COLUMNNAME_X12DE355, 0, tempProductStock.getUOM_Value(),tempProductStock.getAD_Org_ID());
			product.setC_UOM_ID(C_UOM_ID);
			
			Integer M_Brand_ID = DataSetupValidation.getIDFilterOrg(getAD_Client_ID(), X_M_Brand.Table_Name, X_M_Brand.COLUMNNAME_Name, 0, tempProductStock.get_ValueAsString("Brand_Value"),tempProductStock.getAD_Org_ID());
			product.set_CustomColumn("M_Brand_ID", M_Brand_ID);

			
			Integer M_Product_Category_ID = DataSetupValidation.getID(getAD_Client_ID(), MProductCategory.Table_Name, MProductCategory.COLUMNNAME_Name, 0, tempProductStock.getProductCategory_Value());
			product.setM_Product_Category_ID(M_Product_Category_ID);
	
			Integer C_TaxCategory_ID = DataSetupValidation.getID(getAD_Client_ID(), MTaxCategory.Table_Name, MTaxCategory.COLUMNNAME_Name, 0, tempProductStock.getTaxType());
			product.setC_TaxCategory_ID(C_TaxCategory_ID);
	
			product.setValue(tempProductStock.getProduct_Value());
			product.setName(tempProductStock.getProduct_Name());
			product.setProductType(tempProductStock.getProductType());
			product.setIsStocked(true);
			product.setIsPurchased(true);
			product.setIsSold(true);
			product.saveEx();
			
			Env.reset(false);	
			CacheMgt.get().reset();
				
			Integer elementid = DataSetupValidation.getID(getAD_Client_ID(), MCostElement.Table_Name, MCostElement.COLUMNNAME_CostingMethod, 0, MCostElement.COSTINGMETHOD_AveragePO);
			Integer asid = DataSetupValidation.getID(getAD_Client_ID(), MAcctSchema.Table_Name, MAcctSchema.COLUMNNAME_CostingMethod, 0, MAcctSchema.COSTINGMETHOD_AveragePO);
			
			MProductPrice pricingBuy = new MProductPrice(getCtx(), 0 , get_TrxName());		
			MProductPrice pricingRtl = new MProductPrice(getCtx(), 0 , get_TrxName());			
			MProductPrice pricingRgs = new MProductPrice(getCtx(), 0 , get_TrxName());
			
			StringBuilder SQLPurchase = new StringBuilder();
			SQLPurchase.append("SELECT a.description::numeric");
			SQLPurchase.append(" FROM ad_param a ");
			SQLPurchase.append(" WHERE a.value = 'PurchasePrice'");
	
			Integer M_PriceList_Version_Purchase_ID = DB.getSQLValueEx(get_TrxName(), SQLPurchase.toString());
			pricingBuy.setM_PriceList_Version_ID(M_PriceList_Version_Purchase_ID);
			pricingBuy.setM_Product_ID(product.getM_Product_ID());
			pricingBuy.setPriceList(tempProductStock.getPurchasePrice());
			pricingBuy.setPriceStd(tempProductStock.getPurchasePrice());
			pricingBuy.setPriceLimit(tempProductStock.getPurchasePrice());
			pricingBuy.saveEx();
				
			StringBuilder SQLGrs = new StringBuilder();
			SQLGrs.append("SELECT a.description::numeric");
			SQLGrs.append(" FROM ad_param a ");
			SQLGrs.append(" WHERE a.value = 'WholeSalerPrice'");
			
			Integer M_PriceList_Version_Grs_ID = DB.getSQLValueEx(get_TrxName(), SQLGrs.toString());
			pricingRgs.setM_PriceList_Version_ID(M_PriceList_Version_Grs_ID);
			pricingRgs.setM_Product_ID(product.getM_Product_ID());
			pricingRgs.setPriceList(tempProductStock.getWholesalerPrice());
			pricingRgs.setPriceStd(tempProductStock.getWholesalerPrice());
			pricingRgs.setPriceLimit(tempProductStock.getWholesalerPrice());
			pricingRgs.saveEx();		
		
			
			StringBuilder SQLRtl = new StringBuilder();
			SQLRtl.append("SELECT a.description::numeric");
			SQLRtl.append(" FROM ad_param a ");
			SQLRtl.append(" WHERE a.value = 'RetailSalesPrice'");
			
			Integer M_PriceList_Version_Rtl_ID = DB.getSQLValueEx(get_TrxName(), SQLRtl.toString());
			pricingRtl.setM_PriceList_Version_ID(M_PriceList_Version_Rtl_ID);
			pricingRtl.setM_Product_ID(product.getM_Product_ID());
			pricingRtl.setPriceList(tempProductStock.getSalesPrice());
			pricingRtl.setPriceStd(tempProductStock.getSalesPrice());
			pricingRtl.setPriceLimit(tempProductStock.getSalesPrice());
			pricingRtl.saveEx();
			
			MAcctSchema as = new MAcctSchema(getCtx(),asid , get_TrxName());					
			MCost cost = new MCost(product, 0, as, 0, elementid);
			cost.setCurrentCostPrice(tempProductStock.getPurchasePrice());
			cost.saveEx();		
			
			System.out.println("Product Created = "+product.getName());
			System.out.println("---------------------------------------");

			
			tempProductStock.set_CustomColumn("M_Product_ID", product.getM_Product_ID());
			tempProductStock.setIsImportProduct(true);
			tempProductStock.saveEx();
			
			rs = product;
		
		}catch (Exception e) {

			System.out.println(e);
		}
		
		return rs;
		
	}

}
