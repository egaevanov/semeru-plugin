package org.semeru.process;

import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.logging.Level;

import org.compiere.model.MBPartner;
import org.compiere.model.MBPartnerLocation;
import org.compiere.model.MDocType;
import org.compiere.model.MOrder;
import org.compiere.model.MOrderLine;
import org.compiere.model.MProduct;
import org.compiere.process.ProcessInfoParameter;
import org.compiere.process.SvrProcess;
import org.compiere.util.DB;
import org.compiere.util.Env;
import org.semeru.model.X_i_ordertrx_temp;
import org.semeru.ws.model.SMR_Model_Order;
import org.semeru.ws.model.SMR_Model_Order_Detail;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class SMR_ProcessTransaction  extends SvrProcess{

	private int p_AD_Client_ID = 0;
	private int p_AD_Org_ID = 0;
	private int p_Order_ID = 0;

	
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
			
			else if(name.equals("transaction_id"))
				p_Order_ID = (int)para[i].getParameterAsInt();
			
			else
				log.log(Level.SEVERE, "Unknown Parameter: " + name);
		}
		
	}

	@Override
	protected String doIt() throws Exception {
	
		String JSonOrder = "";
		String JSonDetail = "";
		int AD_Client_ID = 0;
		int AD_Org_ID= 0;
		int I_OrderTrx_Temp_ID = 0;

		
		StringBuilder SQLExtractJSon = new StringBuilder();
		SQLExtractJSon.append("SELECT AD_Client_ID,AD_Org_ID,Orders,order_detail,i_ordertrx_temp_id ");
		SQLExtractJSon.append(" FROM  i_ordertrx_temp ");
		SQLExtractJSon.append(" WHERE AD_Client_ID = " + p_AD_Client_ID );
		SQLExtractJSon.append(" AND AD_Org_ID = " + p_AD_Org_ID);
		SQLExtractJSon.append(" AND Transaction_ID = " + p_Order_ID);
		SQLExtractJSon.append(" AND Insert_Order = 'N' ");
		
		
		PreparedStatement pstmt = null;
     	ResultSet rs = null;
     	MOrder ord = new MOrder(getCtx(), 0, get_TrxName());
			try {
				pstmt = DB.prepareStatement(SQLExtractJSon.toString(), null);
				rs = pstmt.executeQuery();
				while (rs.next()) {
					
					AD_Client_ID = rs.getInt(1);
					AD_Org_ID = rs.getInt(2);
					JSonOrder = rs.getString(3);
					JSonDetail = rs.getString(4);
					I_OrderTrx_Temp_ID = rs.getInt(5);
					Gson gson = new Gson();
					JsonObject jsonOrder = new JsonObject();
					JsonParser parserOrder = new JsonParser();
					jsonOrder = parserOrder.parse(JSonOrder).getAsJsonObject();			
					
					JsonArray jsonDetailArray = new JsonArray();
					JsonParser parserDetail = new JsonParser();
					jsonDetailArray = parserDetail.parse(JSonDetail).getAsJsonArray();

				    SMR_Model_Order data = gson.fromJson(jsonOrder.toString(), SMR_Model_Order.class);
					SMR_Model_Order_Detail[] dataDetails = gson.fromJson(jsonDetailArray.toString(), SMR_Model_Order_Detail[].class);
				    
					BigDecimal grandTotal = Env.ZERO;
					
					
				    ord.setClientOrg(AD_Client_ID, AD_Org_ID);
				    ord.setC_BPartner_ID(data.C_BPartner_ID);
				    
					Integer C_Bpartner_Location = DataSetupValidation.getID(AD_Client_ID, MBPartnerLocation.Table_Name, MBPartnerLocation.COLUMNNAME_C_BPartner_ID, data.C_BPartner_ID, null);		

				    ord.setC_BPartner_Location_ID(C_Bpartner_Location);
				    ord.setPOReference("Mobile");
				    ord.setDescription(data.Description);
				    ord.setDeliveryRule(MOrder.DELIVERYRULE_AfterReceipt);
					ord.setDeliveryViaRule("P");				    	
				    
					// process create order
					StringBuilder SQLGetDocType = new StringBuilder();
					SQLGetDocType.append("SELECT C_DocType_ID ");
					SQLGetDocType.append(" FROM C_DocType ");
					SQLGetDocType.append(" WHERE AD_Client_ID = "+ AD_Client_ID);
					SQLGetDocType.append(" AND IsSoTrx = 'Y' ");
					SQLGetDocType.append(" AND DocSubTypeSO = '"+MDocType.DOCSUBTYPESO_POSOrder+"'");
				    
					int C_DocType_ID = DB.getSQLValue(get_TrxName(), SQLGetDocType.toString());
					
					Timestamp DateOrdered = DataSetupValidation.convertStringToTimeStamp(data.DateOrdered);

					
				    ord.setSalesRep_ID(data.SalesRep_ID);
				    ord.setC_Currency_ID(303);
				    ord.setC_DocType_ID(C_DocType_ID);
				    ord.setC_DocTypeTarget_ID(C_DocType_ID);
				    ord.setDateOrdered(DateOrdered);
				    ord.setDateAcct(DateOrdered);
				    ord.setDatePromised(DateOrdered);
				    System.out.println("Warehouse : "+data.M_Warehouse_ID);
				    
				    ord.setM_Warehouse_ID(data.M_Warehouse_ID);
				    ord.setPOReference(data.POReference);
				    ord.set_ValueNoCheck("SMR_CashMethod_ID", data.CashMethod_ID);
				    System.out.println("Cash method : "+data.CashMethod_ID);
				    System.out.println(data.M_Warehouse_ID);
				    
				    MBPartner bp = new MBPartner(getCtx(), data.C_BPartner_ID, get_TrxName());
				    
				    if(bp.getC_PaymentTerm_ID() > 0){
				    	ord.setC_PaymentTerm_ID(bp.getC_PaymentTerm_ID());
				    }else{
				    	ord.setC_PaymentTerm_ID(DataSetupValidation.getDefaultPaymentTerm(AD_Client_ID));
				    }
				    
				    ord.setM_PriceList_ID(data.M_PriceList_ID);
				    ord.setTotalLines(data.TotaLines);
				    ord.setGrandTotal(data.Grandtotal);
				    ord.setPaymentRule(MOrder.PAYMENTRULE_Cash);
//				    ord.set_CustomColumn("CreatedByPOS_ID", data.getCreatedByPOS_ID());
				    ord.setIsSelfService(true);
					ord.setIsSOTrx(true);
					ord.setDocAction(MOrder.DOCACTION_Complete);
				    ord.set_ValueNoCheck("M_Sales_Locator_ID", data.M_Sales_Locator_ID);		
				    ord.set_ValueNoCheck("C_BankAccount_ID", data.C_BankAccount_ID);
				    ord.set_CustomColumn("CreatedBy", data.CreatedBy);
				    ord.set_CustomColumn("C_POS_ID", data.C_POS_ID);
					 
				    Boolean IsMultiLocator = false;
				    
				    ArrayList<Integer> loc_IDs = new ArrayList<Integer>();
				    //add locator to list
				    for (SMR_Model_Order_Detail dataDetail : dataDetails){
				    	loc_IDs.add(dataDetail.M_Locator_ID);    	
				    }
		
				    //cek locator
				    for(int i=0; i<loc_IDs.size() ; i++){
				    	for(int j = i+1; j< loc_IDs.size() ; j++ ){
				    		if(loc_IDs.get(i) == loc_IDs.get(j)){	
				    			IsMultiLocator = true;
				    		}
				    	}
				    }
				    
				    
				    if(IsMultiLocator){
				    	ord.set_CustomColumnReturningBoolean("IsDecorisMultiLocator", true);    	
				    }else{
				    	ord.set_CustomColumn("M_Locator_ID", loc_IDs.get(0));
				    }
				    
				    
				    System.out.println(loc_IDs.get(0));
				    ord.saveEx();
				    
				    int lineNo = 10;
				    for (SMR_Model_Order_Detail dataDetail : dataDetails){
				    
				    	grandTotal = grandTotal.add(dataDetail.LineNetAmt);
				    	
				    	MOrderLine OrderLine = new MOrderLine(getCtx(), 0, get_TrxName());
				    //	OrderLine.setClientOrg(AD_Client_ID, AD_Org_ID);
				    	OrderLine.setAD_Org_ID(AD_Org_ID);
						OrderLine.setC_Order_ID(ord.getC_Order_ID());
						OrderLine.setM_Product_ID(dataDetail.M_Product_ID);
						MProduct product  = new MProduct(getCtx(), dataDetail.M_Product_ID, get_TrxName());

						OrderLine.setC_UOM_ID(product.getC_UOM_ID());
						OrderLine.setQtyEntered(dataDetail.QtyOrdered);
						OrderLine.setQtyOrdered(dataDetail.QtyOrdered);
						OrderLine.setPriceList(dataDetail.PriceList);
						OrderLine.setPriceEntered(dataDetail.PriceEntered);
						OrderLine.setPriceActual(dataDetail.PriceEntered);
						
						//product.get
						
						StringBuilder SQLGetTax = new StringBuilder();
						
						SQLGetTax.append("SELECT C_Tax_ID");
						SQLGetTax.append(" FROM C_Tax ");
						SQLGetTax.append(" WHERE AD_Client_ID = " +AD_Client_ID);
						SQLGetTax.append(" AND C_TaxCategory_ID = "+product.getC_TaxCategory_ID());

						int C_Tax_ID = DB.getSQLValueEx(get_TrxName(), SQLGetTax.toString());
						
						OrderLine.setC_Tax_ID(C_Tax_ID);
						OrderLine.setLineNetAmt(dataDetail.LineNetAmt);
						
//						if(dataDetail.M_AttributeSetInstance_ID > 0){
//							OrderLine.setM_AttributeSetInstance_ID(dataDetail.M_AttributeSetInstance_ID);
//				    	}
						
						if(IsMultiLocator){
							OrderLine.set_CustomColumn("MultiLocator_ID", dataDetail.M_Locator_ID);	
						}
						
						OrderLine.setC_Currency_ID(ord.getC_Currency_ID());
						OrderLine.setLine(lineNo);
						OrderLine.saveEx();
						
						lineNo = lineNo + 10;
						System.out.println(dataDetail.DiscountAmt);
						
						
						if(dataDetail.DiscountAmt.compareTo(Env.ZERO) < 0) {
							
							MOrderLine OrderLineDisc = new MOrderLine(getCtx(), 0, get_TrxName());
						    //	OrderLine.setClientOrg(AD_Client_ID, AD_Org_ID);
							OrderLineDisc.setAD_Org_ID(AD_Org_ID);
							OrderLineDisc.setC_Order_ID(ord.getC_Order_ID());
								
								StringBuilder SQLGetDisc = new StringBuilder();
								SQLGetDisc.append("SELECT M_Product_ID");
								SQLGetDisc.append(" FROM M_Product ");
								SQLGetDisc.append(" WHERE AD_Client_ID = " +AD_Client_ID);
								SQLGetDisc.append(" AND Producttype = 'E'");
								SQLGetDisc.append(" AND Name Like '%Diskon%'");

								int Disc_ID = DB.getSQLValueEx(get_TrxName(), SQLGetDisc.toString());
								
								MProduct productDisc  = new MProduct(getCtx(), Disc_ID, get_TrxName());

								OrderLineDisc.setM_Product_ID(Disc_ID);
								OrderLineDisc.setC_UOM_ID(productDisc.getC_UOM_ID());
								OrderLineDisc.setQtyEntered(Env.ONE);
								OrderLineDisc.setQtyOrdered(Env.ONE);
								OrderLineDisc.setPriceList(new BigDecimal(1000).negate());
								OrderLineDisc.setPriceEntered(dataDetail.DiscountAmt);
								OrderLineDisc.setPriceActual(dataDetail.DiscountAmt);
								StringBuilder SQLGetDiscTax = new StringBuilder();
								
								SQLGetDiscTax.append("SELECT C_Tax_ID");
								SQLGetDiscTax.append(" FROM C_Tax ");
								SQLGetDiscTax.append(" WHERE AD_Client_ID = " +AD_Client_ID);
								SQLGetDiscTax.append(" AND C_TaxCategory_ID = "+productDisc.getC_TaxCategory_ID());

								int C_TaxDisc_ID = DB.getSQLValueEx(get_TrxName(), SQLGetTax.toString());
								
								OrderLineDisc.setC_Tax_ID(C_TaxDisc_ID);
								OrderLineDisc.setLineNetAmt(dataDetail.DiscountAmt);
								OrderLineDisc.saveEx();
						    	grandTotal = grandTotal.add(dataDetail.DiscountAmt);

								
								lineNo = lineNo +10;
						}
												
				    }
				   		    
				    ord.setGrandTotal(grandTotal);
				    ord.saveEx();
				    
//				    String whereClause = "";
//				    whereClause = "Cash";
//				    String SQLTender = "SELECT C_POSTenderType_ID FROM C_POSTenderType WHERE name = '"
//							+ whereClause + "'";
//					int C_POSTenderType_ID = DB.getSQLValueEx(getCtx().toString(),SQLTender.toString());
//				    
//				    DataSetupValidation.createPOSPayment(ord.getAD_Org_ID(), ord.getC_Order_ID(), C_POSTenderType_ID, MPayment.TENDERTYPE_Cash, ord.getGrandTotal(), "", data.C_BankAccount_ID, get_TrxName());			    
				    ord.processIt(MOrder.DOCACTION_Complete);				
				    ord.saveEx();    				    
				    
				}

			} catch (SQLException err) {
				
				rollback();
				
				X_i_ordertrx_temp temp = new X_i_ordertrx_temp(getCtx(), I_OrderTrx_Temp_ID, get_TrxName());
		    	temp.setinsert_order(true);
			    temp.setResult("Transaksi Gagal");
		    	temp.saveEx();	 
			
				log.log(Level.SEVERE, SQLExtractJSon.toString(), err);
				
			} finally {
				
				if(!ord.getDocStatus().equals(MOrder.DOCSTATUS_Completed)){
					rollback();
					
					X_i_ordertrx_temp temp = new X_i_ordertrx_temp(getCtx(), I_OrderTrx_Temp_ID, get_TrxName());
			    	temp.setinsert_order(true);
				    temp.setResult("Transaksi Gagal");
			    	temp.saveEx();
					
				}else{
					
			    	X_i_ordertrx_temp temp = new X_i_ordertrx_temp(getCtx(), I_OrderTrx_Temp_ID, get_TrxName());
			    	temp.setinsert_order(true);
				    temp.setResult("Transaksi Berhasil");
			    	temp.saveEx();
				}
				
				DB.close(rs, pstmt);
				rs = null;
				pstmt = null;
				

				
			}
		
		
		return "";
		
	}

}
