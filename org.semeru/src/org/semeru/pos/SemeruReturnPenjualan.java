package org.semeru.pos;

import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.Vector;
import java.util.logging.Level;

import org.compiere.minigrid.IMiniTable;
import org.compiere.model.MAllocationHdr;
import org.compiere.model.MAllocationLine;
import org.compiere.model.MAttributeSetInstance;
import org.compiere.model.MDocType;
import org.compiere.model.MInOut;
import org.compiere.model.MInOutLine;
import org.compiere.model.MInvoice;
import org.compiere.model.MLocator;
import org.compiere.model.MOrder;
import org.compiere.model.MOrderLine;
import org.compiere.model.MPayment;
import org.compiere.model.MProduct;
import org.compiere.util.CLogger;
import org.compiere.util.DB;
import org.compiere.util.DisplayType;
import org.compiere.util.Env;
import org.compiere.util.KeyNamePair;
import org.semeru.pos.model.MSemeruPOS;

/**
 * 
 * @author Tegar N
 *
 */
public class SemeruReturnPenjualan {

	public CLogger log = CLogger.getCLogger(SemeruReturnPenjualan.class);
	private Properties ctx = Env.getCtx();
	protected DecimalFormat format = DisplayType.getNumberFormat(DisplayType.Amount);
	
	protected Vector<Vector<Object>> getTrxData(ArrayList<Integer> detailList,IMiniTable MiniTable) {		
		
		Vector<Vector<Object>> data = new Vector<Vector<Object>>();

		
		 if (detailList.size() > 0){
		
			for (int i = 0; i < detailList.size(); i++){
				
				Vector<Object> line = new Vector<Object>(1);

				MInOutLine detailShip = new MInOutLine(ctx, detailList.get(i), null);
				MProduct product = new MProduct(ctx, detailShip.getM_Product_ID(), null);
				MLocator loc = new MLocator(ctx, detailShip.getM_Locator_ID(), null);
				MAttributeSetInstance imei = new MAttributeSetInstance(ctx, detailShip.getM_AttributeSetInstance_ID(), null);
								
				Integer lineNo = detailShip.getLine();
				KeyNamePair kpProd = new KeyNamePair(product.getM_Product_ID(),product.getName());
				KeyNamePair kpLoc = new KeyNamePair(loc.getM_Locator_ID(),loc.getValue());
				KeyNamePair kpLine = new KeyNamePair(detailShip.getM_InOutLine_ID(),lineNo.toString());
				
				String IMEI = imei.getSerNo();
				BigDecimal qty = detailShip.getQtyEntered();
				
				MOrderLine ordLine = new MOrderLine(ctx, detailShip.getC_OrderLine_ID(), null);
				BigDecimal price = ordLine.getPriceEntered();
				BigDecimal total = ordLine.getLineNetAmt();
				
				line.add(new Boolean(true));
				line.add(kpLine);
				line.add(kpProd);
				line.add(kpLoc);
				line.add(IMEI);
				line.add(price);       
				line.add(qty);
				line.add(total);
				line.add(qty);
				data.add(line);
				
			}
		}
		
		return data;
	}

	protected ArrayList<KeyNamePair> loadDocType() {
		ArrayList<KeyNamePair> list = new ArrayList<KeyNamePair>();
		int AD_Client_ID = Env.getAD_Client_ID(Env.getCtx());
		
		StringBuilder SQLDocType = new StringBuilder();
		SQLDocType.append("SELECT C_DocType_ID, Name ");
		SQLDocType.append("FROM C_DocType ");
		SQLDocType.append("WHERE DocBaseType = '" + MDocType.DOCBASETYPE_MaterialReceipt+ "' ");
		SQLDocType.append(" AND IsSOTrx = 'Y' ");
		SQLDocType.append(" AND IsActive = 'Y' ");
		SQLDocType.append(" AND AD_Client_ID = ? ");
		
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			pstmt = DB.prepareStatement(SQLDocType.toString(), null);
			pstmt.setInt(1, AD_Client_ID);

			rs = pstmt.executeQuery();
			while (rs.next()) {
				list.add(new KeyNamePair(rs.getInt(1), rs.getString(2)));
			}

		} catch (SQLException e) {
			log.log(Level.SEVERE, SQLDocType.toString(), e);
		} finally {
			DB.close(rs, pstmt);
			rs = null;
			pstmt = null;
		}

		return list;
	}
	
	protected void createAutoARReceipt(boolean isPaidInv,int C_Inv_ID,int CreditMemo_ID, int CreatedByPOS_ID,int C_DecorisPOS_ID,int Return_ID,Timestamp DateTrx, Map<Integer, Integer> MapPay,int Supervisor_ID){
		
		MInvoice CrMemoReturn = new MInvoice(ctx, CreditMemo_ID, null);
		//MPayment ARSO =  new MPayment(ctx, C_PaymentSO_ID, null);
		int AD_Client_ID = Env.getAD_Client_ID(ctx);
		//
		StringBuilder SQLDocTypeARR = new StringBuilder();
		SQLDocTypeARR.append("SELECT C_DocType_ID ");
		SQLDocTypeARR.append("FROM  C_DocType ");
		SQLDocTypeARR.append("WHERE AD_Client_ID = ?");
		SQLDocTypeARR.append("AND DocBaseType = '" + MDocType.DOCBASETYPE_ARReceipt+ "' ");
		SQLDocTypeARR.append("AND IsSoTrx ='Y' ");
		
		//int C_DocTypeARR_ID = DB.getSQLValueEx(null, SQLDocTypeARR.toString(), Env.getAD_Client_ID(ctx));

		String SQLGetbankAcct= "SELECT C_BankAccount_ID FROM C_POS WHERE CreatedByPOS_ID = ? AND AD_Client_ID = ?";
		int C_BankAcct_ID = DB.getSQLValueEx(ctx.toString(),SQLGetbankAcct.toString(), new Object[] {CreatedByPOS_ID, AD_Client_ID });

		MSemeruPOS smr = new MSemeruPOS(ctx, C_DecorisPOS_ID, null);
		
		
		if(isPaidInv){
			
			for (Integer key : MapPay.keySet()) {

			
				MPayment payOld = new MPayment(ctx, MapPay.get(key), null);

				MPayment pay = new MPayment(ctx, 0, null);
				pay.setAD_Org_ID(CrMemoReturn.getAD_Org_ID());
				pay.setC_DocType_ID(payOld.getC_DocType_ID());
				pay.setIsReceipt(payOld.isReceipt());
				pay.setC_BPartner_ID(payOld.getC_BPartner_ID());
				pay.setDescription(payOld.getDescription());
				pay.setDateTrx(CrMemoReturn.getDateAcct());
				pay.setDateAcct(CrMemoReturn.getDateAcct());
				pay.setTenderType(payOld.getTenderType());
				pay.setC_Currency_ID(payOld.getC_Currency_ID());
				pay.set_ValueNoCheck("CreatedByPOS_ID", payOld.get_ValueAsInt("CreatedByPOS_ID"));
				pay.setC_Invoice_ID(CrMemoReturn.getC_Invoice_ID());
				pay.setPayAmt(payOld.getPayAmt().negate());
				pay.setC_BankAccount_ID(payOld.getC_BankAccount_ID());
				pay.saveEx();
				pay.processIt(MPayment.DOCACTION_Complete);
				pay.saveEx();

			}
			String decPOsdocType = "SELECT C_DocType_ID "
					+ " FROM C_DocType "
					+ " WHERE AD_Client_ID = ?"
					+ " AND DocBaseType = ? ";
			String docDec = "POS";
			
			
			int decDoc_ID = DB.getSQLValueEx(null, decPOsdocType, new Object[]{AD_Client_ID,docDec});
					
			
			MSemeruPOS smrPos = new MSemeruPOS(ctx, 0, null);
			smrPos.setC_DocType_ID(decDoc_ID);
			smrPos.setAD_Org_ID(smr.getAD_Org_ID());
			smrPos.setC_BPartner_ID(smr.getC_BPartner_ID());
			StringBuilder sqlBPLoc = new StringBuilder();
			sqlBPLoc.append("SELECT C_BPartner_Location_ID ");
			sqlBPLoc.append("FROM C_BPartner_Location ");
			sqlBPLoc.append("WHERE C_BPartner_ID = ? ");
			int C_BPartner_Location_ID = DB.getSQLValueEx(ctx.toString(),
					sqlBPLoc.toString(), new Object[] { smr.getC_BPartner_ID() });
			smrPos.setC_BPartner_Location_ID(C_BPartner_Location_ID);
			smrPos.setDateOrdered(CrMemoReturn.getDateInvoiced());
			smrPos.setDescription(smr.getDescription());
			smrPos.setDiscountAmt(Env.ZERO);
			smrPos.setCreatedByPos_ID(CreatedByPOS_ID);
			smrPos.setdpp(Env.ZERO);
			smrPos.setGrandTotal(smr.getGrandTotal());
			smrPos.setTotalLines(smr.getTotalLines());
			smrPos.setTaxAmt(smr.getTaxAmt());
			smrPos.setC_Payment_ID(smr.getC_Payment_ID());
			smrPos.setC_Invoice_ID(smr.getC_Invoice_ID());
			smrPos.setC_Order_ID(CrMemoReturn.getC_Order_ID());
			smrPos.setPayType1(smr.getPayType1());
			smrPos.setPembayaran1(smr.getPembayaran1().add(smr.getPembayaran4()));
			smrPos.setPayType2(smr.getPayType2());
			smrPos.setPembayaran2(smr.getPembayaran2());
			smrPos.setPayType3(smr.getPayType3());
			smrPos.setPembayaran3(smr.getPembayaran3());
			//smrPos.setPayType4(dec.getPayType4());
			//smrPos.setPembayaran4(dec.getPembayaran4());
			smrPos.set_ValueOfColumnReturningBoolean("IsLeasing", false);
			smrPos.set_CustomColumn("TotalKembalian", Env.ZERO);
			smrPos.setIsPembatalan(false);
			smrPos.setIsSOTrx(true);
			smrPos.setIsPenjualan(false);
			smrPos.setIsPembayaran(true);
			smrPos.setIsReceipt(false);
			smrPos.setIsReturn(true);
			smrPos.setSupervisor_ID(Supervisor_ID);
			smrPos.set_CustomColumn("ApproveDate", CrMemoReturn.getDateAcct());
			smrPos.setLeaseProvider(smr.getLeaseProvider());
			smrPos.saveEx();
			
			
		}else if(!isPaidInv){
			
			
			StringBuilder SQLGetARAmt = new StringBuilder();
			SQLGetARAmt.append("SELECT Coalesce(SUM(PayAmt),0) ");
			SQLGetARAmt.append(" FROM C_Payment ");
			SQLGetARAmt.append(" WHERE AD_Client_ID =  " + AD_Client_ID);
			SQLGetARAmt.append(" AND C_Invoice_ID =  " + C_Inv_ID);

			BigDecimal totalAR = DB.getSQLValueBDEx(null, SQLGetARAmt.toString());
			
			BigDecimal retAmt = CrMemoReturn.getGrandTotal().subtract(totalAR);
			
			
			if(retAmt.compareTo(CrMemoReturn.getGrandTotal()) < 0){
				
				int decPay_ID = 0;
				if(retAmt.compareTo(Env.ZERO) > 0){
					decPay_ID = CreateARReceiptSO(C_DecorisPOS_ID,Return_ID,CreditMemo_ID,retAmt,CreatedByPOS_ID,C_BankAcct_ID);
				}
				MSemeruPOS smrPosOld = new MSemeruPOS(ctx, C_DecorisPOS_ID, null);
				
				MSemeruPOS smrPos = new MSemeruPOS(ctx, 0, null);
				BigDecimal payAmt = Env.ZERO;
				BigDecimal taxAmt = Env.ZERO;
				BigDecimal payTunaiARSisa = Env.ZERO;
				if(decPay_ID > 0){
					MSemeruPOS smrPosARSisa = new MSemeruPOS(ctx, decPay_ID, null);
					MapPay.put(MapPay.size(), smrPosARSisa.getC_Payment_ID());
					payTunaiARSisa = smrPosARSisa.getPembayaran1();
				}
				
				for (Integer key : MapPay.keySet()){
				
					MPayment pay = new MPayment(ctx, 0, null);
					MPayment OldPay = new MPayment(ctx, MapPay.get(key), null);
					
					if(OldPay.getDocStatus().equals(MPayment.DOCSTATUS_InProgress)){
						OldPay.processIt(MPayment.DOCACTION_Complete);
						OldPay.saveEx();
						
					}
	
					
					pay.setAD_Org_ID(CrMemoReturn.getAD_Org_ID());

					pay.setC_DocType_ID(OldPay.getC_DocType_ID());
					pay.setIsReceipt(OldPay.isReceipt());
					pay.setC_BPartner_ID(OldPay.getC_BPartner_ID());
					pay.setDescription(OldPay.getDescription());
					pay.setDateTrx(OldPay.getDateTrx());
					pay.setDateAcct(OldPay.getDateAcct());
					pay.setTenderType(OldPay.getTenderType());
					pay.setC_Currency_ID(OldPay.getC_Currency_ID());
					pay.set_ValueNoCheck("CreatedByPOS_ID", OldPay.get_ValueAsInt("CreatedByPOS_ID"));
					pay.setC_Invoice_ID(CreditMemo_ID);
					pay.setPayAmt(OldPay.getPayAmt().negate());
					pay.setC_BankAccount_ID(OldPay.getC_BankAccount_ID());
					pay.saveEx();
					pay.processIt(MPayment.DOCACTION_Complete);
					pay.saveEx();
					
					payAmt = payAmt.add( pay.getPayAmt());
					taxAmt = taxAmt.add(pay.getTaxAmt());
						
				}
				
				String decPOsdocType = "SELECT C_DocType_ID "
						+ " FROM C_DocType "
						+ " WHERE AD_Client_ID = ?"
						+ " AND DocBaseType = ? ";
				String docDec = "POS";
				
				
				int decDoc_ID = DB.getSQLValueEx(null, decPOsdocType, new Object[]{AD_Client_ID,docDec});
						
				
				smrPos.setC_DocType_ID(decDoc_ID);
				smrPos.setAD_Org_ID(CrMemoReturn.getAD_Org_ID());
				smrPos.setC_BPartner_ID(CrMemoReturn.getC_BPartner_ID());
				StringBuilder sqlBPLoc = new StringBuilder();
				sqlBPLoc.append("SELECT C_BPartner_Location_ID ");
				sqlBPLoc.append("FROM C_BPartner_Location ");
				sqlBPLoc.append("WHERE C_BPartner_ID = ? ");
				int C_BPartner_Location_ID = DB.getSQLValueEx(ctx.toString(),
						sqlBPLoc.toString(), new Object[] { CrMemoReturn.getC_BPartner_ID() });
				smrPos.setC_BPartner_Location_ID(C_BPartner_Location_ID);
				smrPos.setDateOrdered(CrMemoReturn.getDateInvoiced());
				smrPos.setDescription(CrMemoReturn.getDescription());
				smrPos.setDiscountAmt(Env.ZERO);
				smrPos.setCreatedByPos_ID(CreatedByPOS_ID);
				smrPos.setdpp(Env.ZERO);
				smrPos.setGrandTotal(payAmt);
				smrPos.setTotalLines(payAmt);
				smrPos.setTaxAmt(taxAmt);
				smrPos.setSupervisor_ID(Supervisor_ID);
				smrPos.set_CustomColumn("ApproveDate", CrMemoReturn.getDateAcct());
				//smrPos.setC_Payment_ID(pay.getC_Payment_ID());
				
				//String PayType1 = smrPosOld.getPayType1();
				String PayType2 = smrPosOld.getPayType2();
				String PayType3 = smrPosOld.getPayType3();
	
				smrPos.setPayType1(smrPosOld.getPayType1());
				smrPos.setPembayaran1(smrPosOld.getPembayaran1().add(payTunaiARSisa));
				if(PayType2 != null && PayType2 != "" && !PayType2.isEmpty()){
					smrPos.setPayType2(smrPosOld.getPayType2());
					smrPos.setPembayaran2(smrPosOld.getPembayaran2());
				}
				if(PayType3 != null && PayType3 != "" && !PayType3.isEmpty()){
					smrPos.setPayType3(smrPosOld.getPayType3());
					smrPos.setPembayaran3(smrPosOld.getPembayaran3());
					smrPos.setLeaseProvider(smr.getLeaseProvider());
				}
				
				
				smrPos.setC_Invoice_ID(CrMemoReturn.getC_Invoice_ID());
				smrPos.setC_Order_ID(CrMemoReturn.getC_Order_ID());
				smrPos.set_ValueOfColumnReturningBoolean("IsLeasing", false);
				smrPos.set_CustomColumn("TotalKembalian", Env.ZERO);
				smrPos.setIsPembatalan(false);
				smrPos.setIsSOTrx(true);
				smrPos.setIsPenjualan(false);
				smrPos.setIsPembayaran(true);
				smrPos.setIsReceipt(false);
				smrPos.setIsReturn(true);
				smrPos.saveEx();
				
				
			}else if(retAmt.compareTo(CrMemoReturn.getGrandTotal())==0){
				
				Map<Integer, Integer> AllocLine = new HashMap<Integer, Integer>();
				MAllocationHdr AllocHdr = new MAllocationHdr(ctx, 0, null);
				MInOut retur = new MInOut(ctx, Return_ID, null);
				StringBuilder SQLGetDocType = new StringBuilder();
				
				AllocLine.put(0, C_Inv_ID);
				AllocLine.put(1, CreditMemo_ID);
				
				SQLGetDocType.append("SELECT C_DocType_ID ");
				SQLGetDocType.append(" FROM C_DocType ");
				SQLGetDocType.append(" WHERE AD_Client_ID =  "+AD_Client_ID);
				SQLGetDocType.append(" AND DocBaseType = '"+ MDocType.DOCBASETYPE_PaymentAllocation+"'");
				
				int docType = DB.getSQLValueEx(null, SQLGetDocType.toString());

				
				AllocHdr.setAD_Org_ID(CrMemoReturn.getAD_Org_ID());
				AllocHdr.setDescription("Allocation " + retur.getDocumentNo());
				AllocHdr.setDateTrx(DateTrx);
				AllocHdr.setC_DocType_ID(docType);
				AllocHdr.setC_Currency_ID(CrMemoReturn.getC_Currency_ID());
				AllocHdr.setDateAcct(DateTrx);
				AllocHdr.saveEx();
				
				for(Integer key:AllocLine.keySet()){
					MInvoice in = new MInvoice(ctx, AllocLine.get(key), null);

					BigDecimal Amt = Env.ZERO;
					
					MDocType docIn = new MDocType(ctx, in.getC_DocType_ID(), null);
					if(docIn.getDocBaseType().equals(MDocType.DOCBASETYPE_ARCreditMemo)){
						Amt = in.getGrandTotal().negate();
					}else if(docIn.getDocBaseType().equals(MDocType.DOCBASETYPE_ARInvoice)){
						Amt = in.getGrandTotal();	
					}
					
					
					
					MAllocationLine line = new MAllocationLine(AllocHdr);
					line.setC_AllocationHdr_ID(AllocHdr.getC_AllocationHdr_ID());
					line.setAD_Org_ID(AllocHdr.getAD_Org_ID());
					line.setC_BPartner_ID(retur.getC_BPartner_ID());
					line.setC_Order_ID(in.getC_Order_ID());
					line.setC_Invoice_ID(AllocLine.get(key));
					line.setAmount(Amt);
					line.saveEx();
					
				}
			AllocHdr.processIt(MAllocationHdr.DOCACTION_Complete);
			AllocHdr.saveEx();
			
			
			String decPOsdocType = "SELECT C_DocType_ID "
					+ " FROM C_DocType "
					+ " WHERE AD_Client_ID = ?"
					+ " AND DocBaseType = ? ";
			String docDec = "POS";
			
			
			int decDoc_ID = DB.getSQLValueEx(null, decPOsdocType, new Object[]{AD_Client_ID,docDec});
					
			MSemeruPOS smrPos = new MSemeruPOS(ctx, 0, null);

			smrPos.setC_DocType_ID(decDoc_ID);
			smrPos.setAD_Org_ID(CrMemoReturn.getAD_Org_ID());
			smrPos.setC_BPartner_ID(CrMemoReturn.getC_BPartner_ID());
			StringBuilder sqlBPLoc = new StringBuilder();
			sqlBPLoc.append("SELECT C_BPartner_Location_ID ");
			sqlBPLoc.append("FROM C_BPartner_Location ");
			sqlBPLoc.append("WHERE C_BPartner_ID = ? ");
			int C_BPartner_Location_ID = DB.getSQLValueEx(ctx.toString(),
					sqlBPLoc.toString(), new Object[] { CrMemoReturn.getC_BPartner_ID() });
			smrPos.setC_BPartner_Location_ID(C_BPartner_Location_ID);
			smrPos.setDateOrdered(CrMemoReturn.getDateInvoiced());
			smrPos.setDescription(CrMemoReturn.getDescription());
			smrPos.setDiscountAmt(Env.ZERO);
			smrPos.setCreatedByPos_ID(CreatedByPOS_ID);
			smrPos.setdpp(Env.ZERO);
			smrPos.setGrandTotal(CrMemoReturn.getGrandTotal());
			smrPos.setTotalLines(CrMemoReturn.getTotalLines());
			smrPos.setTaxAmt(Env.ZERO);
			smrPos.setSupervisor_ID(Supervisor_ID);
			smrPos.set_CustomColumn("ApproveDate", CrMemoReturn.getDateAcct());
			//smrPos.setC_Payment_ID(pay.getC_Payment_ID());
			
			smrPos.setPayType1(smr.getPayType1());
			smrPos.setPembayaran1(smr.getPembayaran1());
			smrPos.setPayType2(smr.getPayType2());
			smrPos.setPembayaran2(smr.getPembayaran2());
			smrPos.setPayType3(smr.getPayType3());
			smrPos.setPembayaran3(smr.getPembayaran3());
			smrPos.setPayType4(smr.getPayType4());
			smrPos.setPembayaran4(smr.getPembayaran4());
			
			smrPos.setC_Invoice_ID(CrMemoReturn.getC_Invoice_ID());
			smrPos.set_ValueOfColumnReturningBoolean("IsLeasing", false);
			smrPos.set_CustomColumn("TotalKembalian", Env.ZERO);
			smrPos.setIsPembatalan(false);
			smrPos.setIsSOTrx(true);
			smrPos.setIsPenjualan(false);
			smrPos.setIsPembayaran(true);
			smrPos.setIsReceipt(false);
			smrPos.setIsReturn(true);
			smrPos.saveEx();
			
			
			}
			
			
			
			
			
			
		}
		
		
	}
	
	
	private int CreateARReceiptSO(int C_DecorisPOS_ID, int Return_ID, int CreditMemo_ID,BigDecimal payAmt,
			int createdByPOS_ID, int C_BankAcct_ID){
		int rs = 0;
		 int AD_Client_ID = Env.getAD_Client_ID(ctx);
		
		if(C_DecorisPOS_ID > 0 ){

			MSemeruPOS dec = new MSemeruPOS(ctx, C_DecorisPOS_ID, null);
			MInvoice inv = new MInvoice(ctx, dec.getC_Invoice_ID(), null);
			//Map<String, BigDecimal> tenderType = new HashMap<String, BigDecimal>();
			MInOut retur = new MInOut(ctx, Return_ID, null);
			MInvoice creditMemo = new MInvoice(ctx, CreditMemo_ID, null);
			
			//
			StringBuilder SQLDocTypeARR = new StringBuilder();
			SQLDocTypeARR.append("SELECT C_DocType_ID ");
			SQLDocTypeARR.append("FROM  C_DocType ");
			SQLDocTypeARR.append("WHERE AD_Client_ID = ?");
			SQLDocTypeARR.append("AND DocBaseType = '" + MDocType.DOCBASETYPE_ARReceipt+ "' ");
			SQLDocTypeARR.append("AND IsSoTrx ='Y' ");
			
			int C_DocTypeARR_ID = DB.getSQLValueEx(null, SQLDocTypeARR.toString(), Env.getAD_Client_ID(ctx));
				
				MPayment pay = new MPayment(ctx, 0, null);
				pay.setAD_Org_ID(inv.getAD_Org_ID());

				pay.setC_DocType_ID(C_DocTypeARR_ID);
				pay.setIsReceipt(true);
				pay.setC_BPartner_ID(inv.getC_BPartner_ID());
				pay.setDescription("Atas Nota Return No "+retur.getDocumentNo()+ " ,Credit Memo "+creditMemo.getDocumentNo());
				
				
				Calendar cal = Calendar.getInstance();
				cal.setTime(Env.getContextAsDate(ctx, "#Date"));
				cal.set(Calendar.HOUR_OF_DAY, 0);
				cal.set(Calendar.MINUTE, 0);
				cal.set(Calendar.SECOND, 0);
				cal.set(Calendar.MILLISECOND, 0);				
				Timestamp dateTrx = new Timestamp(cal.getTimeInMillis());
				pay.setDateTrx(dateTrx);
				pay.setDateAcct(dateTrx);
				//pay.setTenderType(MPayment.TENDERTYPE_MixPaymentPOS);
				pay.setC_Currency_ID(inv.getC_Currency_ID());
				pay.set_ValueNoCheck("CreatedByPOS_ID", createdByPOS_ID);
				pay.setC_Invoice_ID(inv.getC_Invoice_ID());
				pay.setPayAmt(payAmt);
				pay.setC_Currency_ID(inv.getC_Currency_ID());
				pay.setC_BankAccount_ID(C_BankAcct_ID);
				pay.saveEx();
				pay.processIt(MPayment.DOCACTION_Complete);
				pay.saveEx();
				
				
				
				
				String decPOsdocType = "SELECT C_DocType_ID "
						+ " FROM C_DocType "
						+ " WHERE AD_Client_ID = ?"
						+ " AND DocBaseType = ? ";
				String docDec = "POS";
				
				
				int decDoc_ID = DB.getSQLValueEx(null, decPOsdocType, new Object[]{AD_Client_ID,docDec});
				
				
				MSemeruPOS smrPos = new MSemeruPOS(ctx, 0, null);
				smrPos.setC_DocType_ID(decDoc_ID);
				smrPos.setAD_Org_ID(pay.getAD_Org_ID());
				smrPos.setC_BPartner_ID(pay.getC_BPartner_ID());
				StringBuilder sqlBPLoc = new StringBuilder();
				sqlBPLoc.append("SELECT C_BPartner_Location_ID ");
				sqlBPLoc.append("FROM C_BPartner_Location ");
				sqlBPLoc.append("WHERE C_BPartner_ID = ? ");
				int C_BPartner_Location_ID = DB.getSQLValueEx(ctx.toString(),
						sqlBPLoc.toString(), new Object[] { pay.getC_BPartner_ID() });
				smrPos.setC_BPartner_Location_ID(C_BPartner_Location_ID);
				smrPos.setDateOrdered(creditMemo.getDateInvoiced());
				//smrPos.setDateTrx(creditMemo.getDateInvoiced());
				smrPos.setDescription(pay.getDescription());
				smrPos.setDiscountAmt(Env.ZERO);
				smrPos.setCreatedByPos_ID(createdByPOS_ID);
				smrPos.setdpp(Env.ZERO);
				smrPos.setGrandTotal(pay.getPayAmt());
				smrPos.setTotalLines(pay.getPayAmt());
				smrPos.setTaxAmt(pay.getTaxAmt());
				smrPos.setC_Payment_ID(pay.getC_Payment_ID());
				smrPos.setC_Invoice_ID(pay.getC_Invoice_ID());
				smrPos.setC_Order_ID(dec.getC_Order_ID());
				smrPos.setPayType1("TUNAI");
				smrPos.setPembayaran1(payAmt);
				smrPos.set_ValueOfColumnReturningBoolean("IsLeasing", false);
				smrPos.set_CustomColumn("TotalKembalian", Env.ZERO);
				smrPos.setIsPembatalan(false);
				smrPos.setIsSOTrx(true);
				smrPos.setIsPenjualan(false);
				smrPos.setIsPembayaran(true);
				smrPos.setIsReceipt(false);
				smrPos.saveEx();
				
				
				rs = smrPos.getSM_SemeruPOS_ID();
		}
		
		
		return rs;
	}	
	
	
	protected Map<Integer, Integer> getpayment(int AD_Client_ID,int C_InvoiceOld_ID, int Order_ID){
		Map<Integer, Integer> C_PaymentOld_ID = new HashMap<Integer, Integer>();
		
		StringBuilder SQLCekAR = new StringBuilder();
		SQLCekAR.append("SELECT C_Payment_ID");
		SQLCekAR.append(" FROM C_Payment ");
		SQLCekAR.append(" WHERE AD_Client_ID = "+ AD_Client_ID);
		SQLCekAR.append(" AND C_Invoice_ID = "+ C_InvoiceOld_ID);
		
		
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		int count = 0;
		try {
			pstmt = DB.prepareStatement(SQLCekAR.toString(), null);

			rs = pstmt.executeQuery();
			while (rs.next()) {
				C_PaymentOld_ID.put(count,rs.getInt(1));
				count++;
			}

		} catch (SQLException ex) {
			log.log(Level.SEVERE, SQLCekAR.toString(), ex);
		} finally {
			DB.close(rs, pstmt);
			rs = null;
			pstmt = null;
		}
		
		
		return C_PaymentOld_ID;
		
		
	}
	
	
public void saveToDecorisWindow(int AR_CreditMemo_ID,int Return_ID,String lease,int Supervidor_ID,int createdByPOS_ID){
		
		MInvoice ARCrMemo = new MInvoice(ctx, AR_CreditMemo_ID, null);
		MInOut retur = new MInOut(ctx, Return_ID, null);
		
		MSemeruPOS smrPos = new MSemeruPOS(ctx, 0, null);
		smrPos.setAD_Org_ID(ARCrMemo.getAD_Org_ID());
		smrPos.setC_BPartner_ID( ARCrMemo.getC_BPartner_ID());
		
		StringBuilder sqlBPLoc = new StringBuilder();
		sqlBPLoc.append("SELECT C_BPartner_Location_ID ");
		sqlBPLoc.append("FROM C_BPartner_Location ");
		sqlBPLoc.append("WHERE C_BPartner_ID = ? ");
		int C_BPartner_Location_ID = DB.getSQLValueEx(ctx.toString(),sqlBPLoc.toString(), new Object[] {  retur.getC_BPartner_ID() });

		smrPos.setC_BPartner_Location_ID(C_BPartner_Location_ID);
		smrPos.setDateOrdered(ARCrMemo.getDateInvoiced());
		smrPos.setDescription(ARCrMemo.getDescription());
		smrPos.setM_PriceList_ID(ARCrMemo.getM_PriceList_ID());
		smrPos.setM_Warehouse_ID(retur.getM_Warehouse_ID());
		smrPos.setDeliveryRule(MOrder.DELIVERYRULE_AfterReceipt);
		smrPos.setC_PaymentTerm_ID(ARCrMemo.getC_PaymentTerm_ID());
		smrPos.setPaymentRule(MOrder.PAYMENTRULE_MixedPOSPayment);
		smrPos.setC_Invoice_ID(ARCrMemo.getC_Invoice_ID());
		if(createdByPOS_ID > 0){
			smrPos.setCreatedByPos_ID(createdByPOS_ID);
		}
		smrPos.setSalesRep_ID(ARCrMemo.getSalesRep_ID());
		smrPos.setdpp(Env.ZERO);
		smrPos.setTaxAmt(Env.ZERO);
		if(Supervidor_ID>0){
			
			smrPos.setSupervisor_ID(Supervidor_ID);
			// Date set to Login Date
			Calendar cal = Calendar.getInstance();
			cal.setTime(Env.getContextAsDate(ctx, "#Date"));
			cal.set(Calendar.HOUR_OF_DAY, 0);
			cal.set(Calendar.MINUTE, 0);
			cal.set(Calendar.SECOND, 0);
			cal.set(Calendar.MILLISECOND, 0);
			//tanggalSearch.setValue(new Timestamp(cal.getTimeInMillis()));
			
			smrPos.set_CustomColumn("ApproveDate", new Timestamp(cal.getTimeInMillis()));
			
		}

		String decPOsdocType = "SELECT C_DocType_ID "
				+ " FROM C_DocType "
				+ " WHERE AD_Client_ID = ?"
				+ " AND DocBaseType = ? ";
		String docDec = "POS";
		
		
		int decDoc_ID = DB.getSQLValueEx(null, decPOsdocType, new Object[]{Env.getAD_Client_ID(ctx),docDec});
		smrPos.setC_DocType_ID(decDoc_ID);
		smrPos.setDeliveryViaRule(retur.getDeliveryViaRule());
		smrPos.setIsPembatalan(false);
		smrPos.setIsSOTrx(true);
		smrPos.setIsPenjualan(false);
		smrPos.setIsPembayaran(false);
		smrPos.setIsReceipt(false);
		smrPos.setIsReturn(true);
		smrPos.saveEx();
				
	}
	
	
}
