package org.semeru.process;

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
import org.semeru.pos.model.X_SM_SemeruPOSLine;
import org.semeru.pos.model.X_SM_SemeruTemp;


public class SM_POSProcess extends SvrProcess{

	
	int p_AD_Client_ID = 0;
	int p_AD_Org_ID = 0;
	int p_SM_SemeruTemp_ID = 0;
	private Map<String, BigDecimal> MapPosPay;
	String m_DocAction = ""; 

	MSemeruPOS SMPOS = null;
	X_SM_SemeruTemp SMPOS_Temp = null;
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
				p_SM_SemeruTemp_ID = (int)para[i].getParameterAsInt();
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
		
			SMPOS_Temp = new X_SM_SemeruTemp(getCtx(), p_SM_SemeruTemp_ID, get_TrxName());
			
			SMPOS = new MSemeruPOS(getCtx(), 0, get_TrxName());
						
			//Save Data From Temp Table
			SMPOS.setDeliveryViaRule(SMPOS_Temp.getDeliveryViaRule());
			SMPOS.setC_DocType_ID(SMPOS_Temp.getC_DocType_ID());
			SMPOS.setAD_Org_ID(SMPOS_Temp.getAD_Org_ID());
			SMPOS.setC_BPartner_ID(SMPOS_Temp.getC_BPartner_ID());
			SMPOS.setC_BPartner_Location_ID(SMPOS_Temp.getC_BPartner_Location_ID());
			SMPOS.setDateOrdered(SMPOS_Temp.getDateOrdered());
			SMPOS.setDescription(SMPOS_Temp.getDescription());
			SMPOS.setM_PriceList_ID(SMPOS_Temp.getM_PriceList_ID());
			SMPOS.setDiscountAmt(SMPOS_Temp.getDiscountAmt());
			SMPOS.setM_Warehouse_ID(SMPOS_Temp.getM_Warehouse_ID());
			SMPOS.setDeliveryRule(SMPOS_Temp.getDeliveryRule());
			SMPOS.setC_PaymentTerm_ID(SMPOS_Temp.getC_PaymentTerm_ID());
			SMPOS.setPaymentRule(SMPOS_Temp.getPaymentRule());
			SMPOS.setCreatedByPos_ID(SMPOS_Temp.getCreatedByPos_ID());
			SMPOS.setSalesRep_ID(SMPOS_Temp.getSalesRep_ID());
			SMPOS.setIsPickup(SMPOS_Temp.isPickup());
			SMPOS.setdpp(SMPOS_Temp.getdpp());
			SMPOS.setGrandTotal(SMPOS_Temp.getGrandTotal());
			SMPOS.setTotalLines(SMPOS_Temp.getTotalLines());
			SMPOS.setTaxAmt(SMPOS_Temp.getTaxAmt());
			SMPOS.setPayType1(SMPOS_Temp.getPayType1());
			SMPOS.setPayType2(SMPOS_Temp.getPayType2());
			SMPOS.setPayType3(SMPOS_Temp.getPayType3());
			SMPOS.setPayType4(SMPOS_Temp.getPayType4());
			SMPOS.setIsLeasing(SMPOS_Temp.isLeasing());
			SMPOS.setDocumentNo(SMPOS_Temp.getDocumentNo());
			SMPOS.setPembayaran1(SMPOS_Temp.getPembayaran1());
			SMPOS.setTotalKembalian(SMPOS_Temp.getTotalKembalian());
			SMPOS.setTotalTunai(SMPOS_Temp.getTotalTunai());
			SMPOS.setPembayaran2(SMPOS_Temp.getPembayaran2());
			SMPOS.setPembayaran3(SMPOS_Temp.getPembayaran3());
			SMPOS.setPembayaran4(SMPOS_Temp.getPembayaran4());
			SMPOS.setIsPembatalan(SMPOS_Temp.isPembayaran());
			SMPOS.setIsSOTrx(SMPOS_Temp.isSOTrx());
			SMPOS.setIsPenjualan(SMPOS_Temp.isPenjualan());
			SMPOS.setIsPembayaran(SMPOS_Temp.isPembayaran());
			SMPOS.setIsReceipt(SMPOS_Temp.isReceipt());
			SMPOS.setOrderDocType_ID(SMPOS_Temp.getOrderDocType_ID());
			SMPOS.setIsMultiLocator(SMPOS_Temp.isMultiLocator());
			SMPOS.setLocatorNoMulti_ID(SMPOS_Temp.getLocatorNoMulti_ID());
			SMPOS.setC_BankAccount_ID(SMPOS_Temp.getC_BankAccount_ID());
			SMPOS.setLeaseProvider(SMPOS_Temp.getLeaseProvider());
			SMPOS.set_CustomColumn("IsDebitCard", SMPOS_Temp.get_ValueAsBoolean("IsDebitCard"));
			SMPOS.set_CustomColumn("ApproveDate", SMPOS_Temp.get_Value("ApproveDate"));
			SMPOS.setSupervisor_ID(SMPOS_Temp.getSupervisor_ID());	
			SMPOS.save();
			
			boolean isMultiLocator = SMPOS.isMultiLocator();
			
			//Call Line From TempLine Table
			StringBuilder SQLCallLine = new StringBuilder();
			SQLCallLine.append("SELECT SM_SemeruLineTemp_ID ");
			SQLCallLine.append("FROM SM_SemeruLineTemp ");
			SQLCallLine.append("WHERE AD_Client_ID = ? ");
			SQLCallLine.append("AND SM_SemeruTemp_ID = ? ");
			
			PreparedStatement pstmt = null;
			ResultSet rs = null;
			try {
				pstmt = DB.prepareStatement(SQLCallLine.toString(), get_TrxName());
				pstmt.setInt(1, SMPOS.getAD_Client_ID());
				pstmt.setInt(2, p_SM_SemeruTemp_ID);
				rs = pstmt.executeQuery();
				while (rs.next()) {
					X_SM_SemeruLineTemp SMPOSLine_Temp = new X_SM_SemeruLineTemp(getCtx(), rs.getInt(1), get_TrxName());
					X_SM_SemeruPOSLine DecPOSLine = new X_SM_SemeruPOSLine(getCtx(), 0, get_TrxName());					
					
					DecPOSLine.setAD_Org_ID(SMPOS.getAD_Org_ID());
					DecPOSLine.setSM_SemeruPOS_ID(SMPOS.getSM_SemeruPOS_ID());
					DecPOSLine.setM_Product_ID(SMPOSLine_Temp.getM_Product_ID());
					DecPOSLine.setC_UOM_ID(SMPOSLine_Temp.getC_UOM_ID());
					DecPOSLine.setC_Tax_ID(SMPOSLine_Temp.getC_Tax_ID());
					DecPOSLine.setPriceList(SMPOSLine_Temp.getPriceList());
					DecPOSLine.setPriceEntered(SMPOSLine_Temp.getPriceEntered());
					DecPOSLine.setQtyOrdered(SMPOSLine_Temp.getQtyOrdered());
					DecPOSLine.setLineNetAmt(SMPOSLine_Temp.getLineNetAmt());
					DecPOSLine.setLine(SMPOSLine_Temp.getLine());
					DecPOSLine.setM_AttributeSetInstance_ID(SMPOSLine_Temp.getM_AttributeSetInstance_ID());
					if(isMultiLocator){
						DecPOSLine.setM_Locator_ID(SMPOSLine_Temp.getM_Locator_ID());		
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
			Order.setAD_Org_ID(SMPOS.getAD_Org_ID());
			Order.setC_BPartner_ID(SMPOS.getC_BPartner_ID());
			Order.setC_BPartner_Location_ID(SMPOS.getC_BPartner_Location_ID());
			Order.setPOReference(SMPOS.getDocumentNo());
			Order.setDescription(SMPOS.getDescription());
			Order.setDeliveryRule(SMPOS.getDeliveryRule());
			Order.setDeliveryViaRule(SMPOS.getDeliveryViaRule());
			Order.setSalesRep_ID(SMPOS.getSalesRep_ID());
			Order.setC_Currency_ID(303);
			Order.setC_DocType_ID(SMPOS.getOrderDocType_ID());
			Order.setC_DocTypeTarget_ID(SMPOS.getOrderDocType_ID());
			if (SMPOS.isManualDocumentNo()) {
				Order.setDocumentNo(SMPOS.getDocumentNo());
			}
	
			if (!isMultiLocator && SMPOS.getLocatorNoMulti_ID() != 0) {
				Order.set_ValueNoCheck("M_Locator_ID", SMPOS.getLocatorNoMulti_ID());
			}
	
			Order.setDateOrdered(SMPOS.getDateOrdered());
			Order.setDateAcct(SMPOS.getDateOrdered());
			Order.setDatePromised(SMPOS.getDateOrdered());
			Order.setM_Warehouse_ID(SMPOS.getM_Warehouse_ID());
			Order.setC_PaymentTerm_ID(SMPOS.getC_PaymentTerm_ID());
			Order.setM_PriceList_ID(SMPOS.getM_PriceList_ID());
			Order.setTotalLines(SMPOS.getTotalLines());
			Order.setGrandTotal(SMPOS.getGrandTotal());
			Order.setPaymentRule(SMPOS.getPaymentRule());
			Order.set_CustomColumn("CreatedByPOS_ID", SMPOS.getCreatedByPos_ID());
			Order.setIsSelfService(true);
			Order.setIsSOTrx(SMPOS.isSOTrx());
			Order.setPOReference(SMPOS.getDocumentNo());
		//	Order.set_CustomColumn("IsDecorisMultiLocator", SMPOS.isMultiLocator());
			Order.save();		
	
			MSemeruPOS Dec = new MSemeruPOS(getCtx(), SMPOS.getSM_SemeruPOS_ID(),get_TrxName());
	
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
			//	OrderLine.set_CustomColumn("MultiLocator_ID", Line.getM_Locator_ID());
				OrderLine.save();
	
				Line.setC_OrderLine_ID(OrderLine.getC_OrderLine_ID());
				Line.save();
	
			}
	
	
			// add payment			
			MapPosPay = new HashMap<String, BigDecimal>();

			if (SMPOS.getPembayaran1().compareTo(Env.ZERO) > 0) {		
				MapPosPay.put(SMPOS.getPayType1(), SMPOS.getPembayaran1());
			}
			
			if (SMPOS.getPembayaran2().compareTo(Env.ZERO) > 0) {
				MapPosPay.put(SMPOS.getPayType2(), SMPOS.getPembayaran2());
			}
			
			if (SMPOS.getPembayaran3().compareTo(Env.ZERO) > 0) {
				MapPosPay.put(SMPOS.getPayType3(), SMPOS.getPembayaran3());
			}
			
			if (SMPOS.getPembayaran4().compareTo(Env.ZERO) > 0) {
				MapPosPay.put(SMPOS.getPayType4(), SMPOS.getPembayaran4());
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
							C_BankAcct_ID = DB.getSQLValueEx(getCtx().toString(),SQLBank.toString(), new Object[] {SMPOS.getCreatedByPos_ID(), SMPOS.getAD_Client_ID() });
							
						} else if (TipeBayar.equals("BANK")) {
							Integer b_acct_id = SMPOS.get_ValueAsInt("C_BankAccount_ID");
							C_BankAcct_ID = b_acct_id;
						}
	
					} else if (TipeBayar.equals("LEASING")) {
	
						TenderName = MPayment.TENDERTYPE_CreditCard;
						WhereClause = "Leasing";
//						String SQLBank = "SELECT C_BankAccount_ID FROM C_BankAccount WHERE leaseprovider = '"
//								+ SMPOS.get_ValueAsString("leaseprovider")
//								+ "'"
//								+ " AND AD_Client_ID = "
//								+ Env.getAD_Client_ID(getCtx());
						C_BankAcct_ID = SMPOS_Temp.getC_BankAccount_ID();
	
					}
	
					String SQLTender = "SELECT C_POSTenderType_ID FROM C_POSTenderType WHERE name = '"
							+ WhereClause + "'";
					int C_POSTenderType_ID = DB.getSQLValueEx(getCtx().toString(),SQLTender.toString());
					createPOSPayment(Order.getAD_Org_ID(), Order.getC_Order_ID(),C_POSTenderType_ID, TenderName, PayAmt, SMPOS.get_ValueAsString("leaseprovider").toString(),C_BankAcct_ID);
	
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
			SMPOS.setC_Order_ID(Order.getC_Order_ID());
			SMPOS.save();
			SMPOS_Temp.setC_Order_ID(Order.getC_Order_ID());
			SMPOS_Temp.setSM_SemeruPOS_ID(SMPOS.getSM_SemeruPOS_ID());
			SMPOS_Temp.save();
			
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
		posPayment.set_ValueNoCheck("C_BankAccount_ID", C_BankAccount_ID);
		posPayment.save();
	}
	
}
