package org.semeru.process;

import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.logging.Level;

import org.compiere.model.MBPartner;
import org.compiere.model.MDocType;
import org.compiere.model.MInvoice;
import org.compiere.model.MInvoiceLine;
import org.compiere.model.MOrg;
import org.compiere.model.MPriceList;
import org.compiere.process.ProcessInfoParameter;
import org.compiere.process.SvrProcess;
import org.compiere.util.DB;
import org.compiere.util.Env;

public class SMR_ProcessGenerateInvPay extends SvrProcess{

	
	private final String Error = "ERROR";
	
	private int p_AD_Org_ID = 0;
//	private int p_contract = 0;
	private Timestamp p_paid_date = null;
	private BigDecimal p_amt_paid = Env.ZERO;
//	private BigDecimal p_amt_mutation_after = Env.ZERO;
//	private BigDecimal p_amt_mutation_before = Env.ZERO; 
//	private String p_is_extend = "" ;
//	private String p_is_mutation = "";
//	private String p_isInvoiced = "";
//	private String p_isPayReleased = "";
	private int p_reg_mth = 0;
	private int p_orgtrx_id = 0;
	private int C_PaymentTerm_ID = 0;
//	private int p_mob_trx_hist_payment_id = 0;

	
	@Override
	protected void prepare() {
		
		ProcessInfoParameter[] para = getParameter();
		for (int i = 0; i < para.length; i++)
		{
			String name = para[i].getParameterName();
			if (para[i].getParameter() == null);
			
//			else if(name.equals("AD_Client_ID"))
//				p_AD_Client_ID = (int)para[i].getParameterAsInt();
//			
//			else if(name.equals("AD_Org_ID"))
//				p_AD_Org_ID = (int)para[i].getParameterAsInt();
//			
//			else if(name.equals("transaction_id"))
//				p_Order_ID = (int)para[i].getParameterAsInt();
			
			else
				log.log(Level.SEVERE, "Unknown Parameter: " + name);
		}
		
	}

	@Override
	protected String doIt() throws Exception {

		String rslt = ""; 

		
		StringBuilder SQLGetHisPay = new StringBuilder();
		SQLGetHisPay.append("SELECT his.AD_Client_ID, ");
		SQLGetHisPay.append(" his.AD_Org_ID,");
		SQLGetHisPay.append(" his.contract_no,");
		SQLGetHisPay.append(" his.paid_date,");
		SQLGetHisPay.append(" his.amt_paid,");
		SQLGetHisPay.append(" his.amt_mutation_after,");
		SQLGetHisPay.append(" his.amt_mutation_before,");
		SQLGetHisPay.append(" his.is_extend,");
		SQLGetHisPay.append(" his.is_mutation,");
		SQLGetHisPay.append(" his.isInvoiced,");
		SQLGetHisPay.append(" his.isPayReleased,");
		SQLGetHisPay.append(" his.reg_mth, ");
		SQLGetHisPay.append(" con.AD_org_ID as orgtrx_id, ");
		SQLGetHisPay.append(" his.mob_trx_hist_payment_id ");
		SQLGetHisPay.append(" FROM mob_trx_hist_payment his");
		SQLGetHisPay.append(" LEFT JOIN mob_trx_contract con on con.contract_no =  his.contract_no");
		SQLGetHisPay.append(" WHERE his.AD_Client_ID = "+ Env.getAD_Client_ID(getCtx()));
		SQLGetHisPay.append(" AND his.isPayReleased = 'N'");	
		
		
		PreparedStatement pstmt = null;
     	ResultSet rs = null;
			try {
				pstmt = DB.prepareStatement(SQLGetHisPay.toString(), null);
				rs = pstmt.executeQuery();
				while (rs.next()) {
					
					p_AD_Org_ID = rs.getInt(2);
//					p_contract = rs.getInt(3);
					p_paid_date = rs.getTimestamp(4);
					p_amt_paid = rs.getBigDecimal(5);
//					p_amt_mutation_after = rs.getBigDecimal(6);
//					p_amt_mutation_before = rs.getBigDecimal(7);
//					p_is_extend = rs.getString(8);
//					p_is_mutation = rs.getString(9); 
//					p_isInvoiced = rs.getString(10);
//					p_isPayReleased = rs.getString(11);
					p_reg_mth = rs.getInt(12);
					p_orgtrx_id = rs.getInt(13); 
//					p_mob_trx_hist_payment_id = rs.getInt(14);
					
					
					for(int i=0; i < p_reg_mth ; i++) {
						
						MInvoice inv = new MInvoice(getCtx(), 0, get_TrxName());
						
						inv.setAD_Org_ID(p_AD_Org_ID);
						inv.setDocStatus(MInvoice.DOCSTATUS_Drafted);		//	Draft
						inv.setDocAction(MInvoice.DOCACTION_Complete);
						
						StringBuilder getPriceList = new StringBuilder();
						getPriceList.append("SELECT M_PriceList_ID");
						getPriceList.append(" FROM M_PriceList ");
						getPriceList.append(" WHERE AD_Client_ID = "+Env.getAD_Client_ID(getCtx()));
						getPriceList.append(" AND IsActive = 'Y' ");
						getPriceList.append(" AND IsSoPriceList = 'Y' ");
						getPriceList.append(" AND IsDefault = 'Y' ");

						int M_PriceList_ID = DB.getSQLValueEx(get_TrxName(), getPriceList.toString());
						
						inv.setM_PriceList_ID(M_PriceList_ID);
						inv.setPaymentRule(MInvoice.PAYMENTRULE_Cash); 	
						
						StringBuilder SQLDocTypeARI = new StringBuilder();
						SQLDocTypeARI.append("SELECT C_DocType_ID ");
						SQLDocTypeARI.append("FROM  C_DocType ");
						SQLDocTypeARI.append("WHERE AD_Client_ID = "+Env.getAD_Client_ID(getCtx()));
						SQLDocTypeARI.append("AND DocBaseType = '" + MDocType.DOCBASETYPE_ARInvoice+ "' ");
						SQLDocTypeARI.append("AND IsSoTrx ='Y' ");
						
						int C_DocTypeARI_ID = DB.getSQLValueEx(null, SQLDocTypeARI.toString());
						
						inv.setC_DocTypeTarget_ID (C_DocTypeARI_ID);
						inv.setIsSOTrx(true);
						
						if(i== 0) {
							inv.setDateInvoiced (p_paid_date);
							inv.setDateAcct (p_paid_date);
							inv.setDescription("Invoice : "+i+1);
						}else {
							
							Calendar c = Calendar.getInstance();
							c.setTime(p_paid_date); 
							Date finalDate = new Date();
							Timestamp dateAcct = null;


							c.add(Calendar.MONTH, i);
							finalDate = c.getTime();
							SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd 00:00:00");
							String tglTostr = df.format(finalDate);

							dateAcct = Timestamp.valueOf(tglTostr);
							
							inv.setDateInvoiced (dateAcct);
							inv.setDateAcct (dateAcct);
							inv.setDescription("Invoice : "+i+1);

							
						}
						
						
						MOrg org = new MOrg(getCtx(), p_orgtrx_id, get_TrxName());
						StringBuilder SQLGetBP = new StringBuilder();
						SQLGetBP.append("SELECT C_BPartner_ID ");
						SQLGetBP.append(" FROM  C_BPartner ");
						SQLGetBP.append(" WHERE AD_Client_ID = "+Env.getAD_Client_ID(getCtx()));
						SQLGetBP.append(" AND AD_Org_ID = "+p_orgtrx_id);
						SQLGetBP.append(" AND Value ='"+org.getValue()+"'");
						
						int C_BP_ID = DB.getSQLValueEx(get_TrxName(), SQLGetBP.toString());
						
						MBPartner bp = new MBPartner(getCtx(), C_BP_ID, get_TrxName());
						
						if (bp.getC_PaymentTerm_ID()>0){
							C_PaymentTerm_ID = bp.getC_PaymentTerm_ID();
							
						}else {
							String sqlterm = "SELECT C_PaymentTerm_ID FROM C_PaymentTerm WHERE AD_Client_ID = ? AND IsDefault = 'Y' ";
							C_PaymentTerm_ID = DB.getSQLValueEx(get_TrxName(), sqlterm.toString(), new Object[] {Env.getAD_Client_ID(getCtx())});
							
						}
						
						inv.setC_BPartner_ID(C_BP_ID);
						inv.setC_BPartner_Location_ID(bp.getPrimaryC_BPartner_Location_ID());
						inv.setC_PaymentTerm_ID(C_PaymentTerm_ID);
						MPriceList plist = new MPriceList(getCtx(), M_PriceList_ID, get_TrxName());
						inv.setIsTaxIncluded(plist.isTaxIncluded());
						inv.setTotalLines(p_amt_paid.divide(new BigDecimal(p_reg_mth)));
						inv.setGrandTotal(p_amt_paid.divide(new BigDecimal(p_reg_mth)));
						inv.setC_Currency_ID(303);
					
						inv.saveEx();

						if(inv != null) {
							
							MInvoiceLine invLine = new MInvoiceLine(getCtx(), 0, get_TrxName());
							
							invLine.setAD_Org_ID(inv.getAD_Org_ID());
							invLine.setC_Invoice_ID(inv.getC_Invoice_ID());
							invLine.setC_Charge_ID(1000014);
							invLine.setQty(Env.ONE);
							invLine.setDescription("Pembayaran Ke "+i+1);
							invLine.setQtyEntered(Env.ONE);
							invLine.setQtyInvoiced(Env.ONE);
							invLine.setPriceEntered(p_amt_paid.divide(new BigDecimal(p_reg_mth)));
							invLine.setPriceActual(p_amt_paid.divide(new BigDecimal(p_reg_mth)));
							invLine.setPriceList(p_amt_paid.divide(new BigDecimal(p_reg_mth)));
							invLine.setLineNetAmt();							
							invLine.setC_Tax_ID(1000001);
							invLine.saveEx();
							
						}
						
						inv.setDocAction("CO");
						if(inv.processIt(MInvoice.ACTION_Complete)) {
							
							inv.saveEx();
							
							
						}
						else {
								//rollback();
								log.warning("Invoice Process Failed: " + inv + " - " + inv.getProcessMsg());
								throw new IllegalStateException("Invoice Process Failed: " + inv + " - " + inv.getProcessMsg());
						}

						
					}						
					
			}
				
				

			} catch (SQLException err) {
				
				log.log(Level.SEVERE, SQLGetHisPay.toString(), err);
				rslt = Error;
				rollback();
				
			} finally {
				DB.close(rs, pstmt);
				rs = null;
				pstmt = null;
			}
		
		return rslt;
	}

}
