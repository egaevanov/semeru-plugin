package org.semeru.pos;

import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;

import org.compiere.model.MOrder;
import org.compiere.model.MOrderLine;
import org.compiere.model.MPayment;
import org.compiere.model.X_C_POSPayment;
import org.compiere.process.ProcessInfoParameter;
import org.compiere.process.SvrProcess;
import org.compiere.util.DB;
import org.compiere.util.Env;
import org.semeru.pos.model.MSemeruPOS;
import org.semeru.pos.model.X_SM_SemeruLineTemp;
import org.semeru.pos.model.X_SM_SemeruPOS;
import org.semeru.pos.model.X_SM_SemeruPOSLine;
import org.semeru.pos.model.X_SM_SemeruTemp;

public class SemeruProcessPOS extends SvrProcess{

	
	int p_AD_Client_ID = 0;
	int p_AD_Org_ID = 0;
	int p_C_DecorisTemp_ID = 0;
	private Map<String, BigDecimal> MapPosPay;
	String m_DocAction = ""; 

	X_SM_SemeruPOS DecPos = null;
	X_SM_SemeruTemp DecPos_Temp = null;
	MOrder Order = null;
	
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
			else if(name.equals("SM_SemeruTemp_ID"))
				p_C_DecorisTemp_ID = (int)para[i].getParameterAsInt();
			else if(name.equals("DocAction"))
				m_DocAction = (String)para[i].getParameterAsString();
	
			else
				log.log(Level.SEVERE, "Unknown Parameter: " + name);
			
		}
		
	}

	@Override
	protected String doIt() throws Exception {
		boolean ok = true;
		
		try{
		
			DecPos_Temp = new X_SM_SemeruTemp(getCtx(), p_C_DecorisTemp_ID, get_TrxName());
			
			DecPos = new X_SM_SemeruPOS(getCtx(), 0, get_TrxName());
						
			//Save Data From Temp Table
			DecPos.setDeliveryViaRule(DecPos_Temp.getDeliveryViaRule());
			DecPos.setC_DocType_ID(DecPos_Temp.getC_DocType_ID());
			DecPos.setAD_Org_ID(DecPos_Temp.getAD_Org_ID());
			DecPos.setC_BPartner_ID(DecPos_Temp.getC_BPartner_ID());
			DecPos.setC_BPartner_Location_ID(DecPos_Temp.getC_BPartner_Location_ID());
			DecPos.setDateOrdered(DecPos_Temp.getDateOrdered());
			DecPos.setDescription(DecPos_Temp.getDescription());
			DecPos.setM_PriceList_ID(DecPos_Temp.getM_PriceList_ID());
			DecPos.setDiscountAmt(DecPos_Temp.getDiscountAmt());
			DecPos.setM_Warehouse_ID(DecPos_Temp.getM_Warehouse_ID());
			DecPos.setDeliveryRule(DecPos_Temp.getDeliveryRule());
			DecPos.setC_PaymentTerm_ID(DecPos_Temp.getC_PaymentTerm_ID());
			DecPos.setPaymentRule(DecPos_Temp.getPaymentRule());
			DecPos.setCreatedByPos_ID(DecPos_Temp.getCreatedByPos_ID());
			DecPos.setSalesRep_ID(DecPos_Temp.getSalesRep_ID());
			DecPos.setIsPickup(DecPos_Temp.isPickup());
			DecPos.setdpp(DecPos_Temp.getdpp());
			DecPos.setGrandTotal(DecPos_Temp.getGrandTotal());
			DecPos.setTotalLines(DecPos_Temp.getTotalLines());
			DecPos.setTaxAmt(DecPos_Temp.getTaxAmt());
			DecPos.setPayType1(DecPos_Temp.getPayType1());
			DecPos.setPayType2(DecPos_Temp.getPayType2());
			DecPos.setPayType3(DecPos_Temp.getPayType3());
			DecPos.setPayType4(DecPos_Temp.getPayType4());
			DecPos.setIsLeasing(DecPos_Temp.isLeasing());
			DecPos.setDocumentNo(DecPos_Temp.getDocumentNo());
			DecPos.setPembayaran1(DecPos_Temp.getPembayaran1());
			DecPos.setTotalKembalian(DecPos_Temp.getTotalKembalian());
			DecPos.setTotalTunai(DecPos_Temp.getTotalTunai());
			DecPos.setPembayaran2(DecPos_Temp.getPembayaran2());
			DecPos.setPembayaran3(DecPos_Temp.getPembayaran3());
			DecPos.setPembayaran4(DecPos_Temp.getPembayaran4());
			DecPos.setIsPembatalan(DecPos_Temp.isPembayaran());
			DecPos.setIsSOTrx(DecPos_Temp.isSOTrx());
			DecPos.setIsPenjualan(DecPos_Temp.isPenjualan());
			DecPos.setIsPembayaran(DecPos_Temp.isPembayaran());
			DecPos.setIsReceipt(DecPos_Temp.isReceipt());
			DecPos.setOrderDocType_ID(DecPos_Temp.getOrderDocType_ID());
			DecPos.setIsMultiLocator(DecPos_Temp.isMultiLocator());
			DecPos.setLocatorNoMulti_ID(DecPos_Temp.getLocatorNoMulti_ID());
			DecPos.setC_BankAccount_ID(DecPos_Temp.getC_BankAccount_ID());
			DecPos.setLeaseProvider(DecPos_Temp.getLeaseProvider());
			DecPos.set_CustomColumn("IsDebitCard", DecPos_Temp.get_ValueAsBoolean("IsDebitCard"));
			DecPos.set_CustomColumn("ApproveDate", DecPos_Temp.get_Value("ApproveDate"));
			DecPos.setSupervisor_ID(DecPos_Temp.getSupervisor_ID());	
			DecPos.save();
			
			boolean isMultiLocator = DecPos.isMultiLocator();
			
			//Call Line From TempLine Table
			StringBuilder SQLCallLine = new StringBuilder();
			SQLCallLine.append("SELECT C_DecorisLineTemp_ID ");
			SQLCallLine.append("FROM C_DecorisLineTemp ");
			SQLCallLine.append("WHERE AD_Client_ID = ? ");
			SQLCallLine.append("AND C_DecorisTemp_ID = ? ");
			
			PreparedStatement pstmt = null;
			ResultSet rs = null;
			try {
				pstmt = DB.prepareStatement(SQLCallLine.toString(), get_TrxName());
				pstmt.setInt(1, DecPos.getAD_Client_ID());
				pstmt.setInt(2, p_C_DecorisTemp_ID);
				rs = pstmt.executeQuery();
				while (rs.next()) {
					X_SM_SemeruLineTemp DecPosLine_Temp = new X_SM_SemeruLineTemp(getCtx(), rs.getInt(1), get_TrxName());
					X_SM_SemeruPOSLine DecPOSLine = new X_SM_SemeruPOSLine(getCtx(), 0, get_TrxName());					
					
					DecPOSLine.setAD_Org_ID(DecPos.getAD_Org_ID());
					DecPOSLine.setSM_SemeruPOS_ID(DecPos.getSM_SemeruPOS_ID());
					DecPOSLine.setM_Product_ID(DecPosLine_Temp.getM_Product_ID());
					DecPOSLine.setC_UOM_ID(DecPosLine_Temp.getC_UOM_ID());
					DecPOSLine.setC_Tax_ID(DecPosLine_Temp.getC_Tax_ID());
					DecPOSLine.setPriceList(DecPosLine_Temp.getPriceList());
					DecPOSLine.setPriceEntered(DecPosLine_Temp.getPriceEntered());
					DecPOSLine.setQtyOrdered(DecPosLine_Temp.getQtyOrdered());
					DecPOSLine.setLineNetAmt(DecPosLine_Temp.getLineNetAmt());
					DecPOSLine.setLine(DecPosLine_Temp.getLine());
					DecPOSLine.setM_AttributeSetInstance_ID(DecPosLine_Temp.getM_AttributeSetInstance_ID());
					if(isMultiLocator){
						DecPOSLine.setM_Locator_ID(DecPosLine_Temp.getM_Locator_ID());		
					}
					DecPOSLine.save();
					
				}

			} catch (SQLException e) {
				log.log(Level.SEVERE, SQLCallLine.toString(), e);
				ok = false;
			} finally {
				DB.close(rs, pstmt);
				rs = null;
				pstmt = null;
			}
			
			Order = new MOrder(getCtx(), 0, get_TrxName());
			Order.setAD_Org_ID(DecPos.getAD_Org_ID());
			Order.setC_BPartner_ID(DecPos.getC_BPartner_ID());
			Order.setC_BPartner_Location_ID(DecPos.getC_BPartner_Location_ID());
			Order.setPOReference(DecPos.getDocumentNo());
			Order.setDescription(DecPos.getDescription());
			Order.setDeliveryRule(DecPos.getDeliveryRule());
			Order.setDeliveryViaRule(DecPos.getDeliveryViaRule());
			Order.setSalesRep_ID(DecPos.getSalesRep_ID());
			Order.setC_Currency_ID(303);
			Order.setC_DocType_ID(DecPos.getOrderDocType_ID());
			Order.setC_DocTypeTarget_ID(DecPos.getOrderDocType_ID());
			if (DecPos.isManualDocumentNo()) {
				Order.setDocumentNo(DecPos.getDocumentNo());
			}
	
			if (!isMultiLocator && DecPos.getLocatorNoMulti_ID() != 0) {
				Order.set_ValueNoCheck("M_Locator_ID", DecPos.getLocatorNoMulti_ID());
			}
	
			Order.setDateOrdered(DecPos.getDateOrdered());
			Order.setDateAcct(DecPos.getDateOrdered());
			Order.setDatePromised(DecPos.getDateOrdered());
			Order.setM_Warehouse_ID(DecPos.getM_Warehouse_ID());
			Order.setC_PaymentTerm_ID(DecPos.getC_PaymentTerm_ID());
			Order.setM_PriceList_ID(DecPos.getM_PriceList_ID());
			Order.setTotalLines(DecPos.getTotalLines());
			Order.setGrandTotal(DecPos.getGrandTotal());
			Order.setPaymentRule(DecPos.getPaymentRule());
			Order.set_CustomColumn("CreatedByPOS_ID", DecPos.getCreatedByPos_ID());
			Order.setIsSelfService(true);
			Order.setIsSOTrx(DecPos.isSOTrx());
			Order.setPOReference(DecPos.getDocumentNo());
			Order.set_CustomColumn("IsDecorisMultiLocator", DecPos.isMultiLocator());
			Order.save();		
	
			MSemeruPOS Dec = new MSemeruPOS(getCtx(), DecPos.getSM_SemeruPOS_ID(),get_TrxName());
	
			X_SM_SemeruPOSLine DecLines[] = Dec.getLines();
	
			for (X_SM_SemeruPOSLine Line : DecLines) {
	
				MOrderLine OrderLine = new MOrderLine(getCtx(), 0, get_TrxName());
	
				// OrderLine
				OrderLine.setAD_Org_ID(Order.getAD_Org_ID());
				OrderLine.setC_Order_ID(Order.getC_Order_ID());
				OrderLine.setM_Product_ID(Line.getM_Product_ID());
				OrderLine.setC_UOM_ID(Line.getC_UOM_ID());
				OrderLine.setQtyEntered(Line.getQtyOrdered());
				OrderLine.setQtyOrdered(Line.getQtyOrdered());
				OrderLine.setPriceList(Line.getPriceList());
				OrderLine.setPriceEntered(Line.getPriceEntered());
				OrderLine.setPriceActual(Line.getPriceEntered());
				OrderLine.setC_Tax_ID(Line.getC_Tax_ID());
				OrderLine.setLineNetAmt(Line.getLineNetAmt());
				OrderLine.setM_AttributeSetInstance_ID(Line.getM_AttributeSetInstance_ID());
				OrderLine.setC_Currency_ID(Order.getC_Currency_ID());
				OrderLine.set_CustomColumn("MultiLocator_ID", Line.getM_Locator_ID());
				OrderLine.save();
	
				Line.setC_OrderLine_ID(OrderLine.getC_OrderLine_ID());
				Line.save();
	
			}
	
	
			// add payment			
			MapPosPay = new HashMap<String, BigDecimal>();

			if (DecPos.getPembayaran1().compareTo(Env.ZERO) > 0) {		
				MapPosPay.put(DecPos.getPayType1(), DecPos.getPembayaran1());
			}
			
			if (DecPos.getPembayaran2().compareTo(Env.ZERO) > 0) {
				MapPosPay.put(DecPos.getPayType2(), DecPos.getPembayaran2());
			}
			
			if (DecPos.getPembayaran3().compareTo(Env.ZERO) > 0) {
				MapPosPay.put(DecPos.getPayType3(), DecPos.getPembayaran3());
			}
			
			if (DecPos.getPembayaran4().compareTo(Env.ZERO) > 0) {
				MapPosPay.put(DecPos.getPayType4(), DecPos.getPembayaran4());
			}
	
			for (String Key : MapPosPay.keySet()) {
	
				String TipeBayar = Key.toUpperCase();
				BigDecimal PayAmt = MapPosPay.get(Key);
				String WhereClause = "";
				String TenderName = "";
				int C_BankAcct_ID = 0;
	
				if (TipeBayar.equals("TUNAI") || TipeBayar.equals("LEASING")
						|| TipeBayar.equals("BANK")) {
	
					if (TipeBayar.equals("TUNAI") || TipeBayar.equals("BANK")) {
						String SQLBank = "";
						TenderName = MPayment.TENDERTYPE_Cash;
						WhereClause = "Cash";
	
						if (TipeBayar.equals("TUNAI")) {
	
							SQLBank = "SELECT C_BankAccount_ID FROM C_POS WHERE CreatedByPOS_ID = ? AND AD_Client_ID = ?";
							C_BankAcct_ID = DB.getSQLValueEx(getCtx().toString(),SQLBank.toString(), new Object[] {DecPos.getCreatedByPos_ID(), DecPos.getAD_Client_ID() });
							
						} else if (TipeBayar.equals("BANK")) {
							Integer b_acct_id = DecPos.get_ValueAsInt("C_BankAccount_ID");
							C_BankAcct_ID = b_acct_id;
						}
	
					} else if (TipeBayar.equals("LEASING")) {
	
						TenderName = MPayment.TENDERTYPE_CreditCard;
						WhereClause = "Leasing";
//						String SQLBank = "SELECT C_BankAccount_ID FROM C_BankAccount WHERE leaseprovider = '"
//								+ DecPos.get_ValueAsString("leaseprovider")
//								+ "'"
//								+ " AND AD_Client_ID = "
//								+ Env.getAD_Client_ID(getCtx());
						C_BankAcct_ID = DecPos_Temp.getC_BankAccount_ID();
	
					}
	
					String SQLTender = "SELECT C_POSTenderType_ID FROM C_POSTenderType WHERE name = '"
							+ WhereClause + "'";
					int C_POSTenderType_ID = DB.getSQLValueEx(getCtx().toString(),SQLTender.toString());
					createPOSPayment(Order.getAD_Org_ID(), Order.getC_Order_ID(),
							C_POSTenderType_ID, TenderName, PayAmt, DecPos.get_ValueAsString("leaseprovider").toString(),C_BankAcct_ID);
	
				}
			}
	
			if (Order != null) {
	
				//m_DocAction = "CO";
				if (m_DocAction != null && m_DocAction.length() > 0) {
					Order.setDocAction(m_DocAction);
					if (!m_DocAction.equals("PR")) {
						if (!Order.processIt(m_DocAction)) {
							log.warning("Order Process Failed: " + Order + " - "+ Order.getProcessMsg());
							throw new IllegalStateException("Invoice Process Failed: " + Order + " - "+ Order.getProcessMsg());
						}
					}
				}
				Order.save();
	
			}
							
		}catch (Exception e){
			
			log.log(Level.SEVERE, "Generate Order - " , e);
			ok = false;
			
		}
		
		
		
		if ( ok ){
			DecPos.setC_Order_ID(Order.getC_Order_ID());
			DecPos.save();
			DecPos_Temp.setC_Order_ID(Order.getC_Order_ID());
			DecPos_Temp.setSM_SemeruPOS_ID(DecPos.getSM_SemeruPOS_ID());
			DecPos_Temp.save();
			
			commitEx();
		}else{ 
			rollback();
		}	
		
		return "";
	}

	
	
	public void createPOSPayment(int AD_Org_ID, int C_Order_ID,int C_POSTenderType_ID, String tenderType, BigDecimal payAmt,String leasingProvider, int C_BankAccount_ID) {

		MOrder Order = new MOrder(Env.getCtx(), C_Order_ID, get_TrxName());

		X_C_POSPayment posPayment = new X_C_POSPayment(Order.getCtx(), 0,get_TrxName());

		posPayment.setAD_Org_ID(AD_Org_ID);
		posPayment.setC_Order_ID(C_Order_ID);
		posPayment.setC_POSTenderType_ID(C_POSTenderType_ID);
		posPayment.setTenderType(tenderType);
		posPayment.setPayAmt(payAmt);
		if (tenderType.equals("L")) {
			posPayment.set_CustomColumn("LeaseProvider", leasingProvider);
		}
		posPayment.set_CustomColumn("C_BankAccount_ID", C_BankAccount_ID);
		posPayment.save();
	}
	
}
