package org.semeru.pos;

import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.DecimalFormat;
import java.util.Properties;
import java.util.Vector;
import java.util.logging.Level;

import org.compiere.minigrid.IMiniTable;
import org.compiere.model.MCharge;
import org.compiere.model.MInvoice;
import org.compiere.model.MPayment;
import org.compiere.util.CLogger;
import org.compiere.util.DB;
import org.compiere.util.DisplayType;
import org.compiere.util.Env;
import org.compiere.util.KeyNamePair;

/**
 * 
 * @author Tegar N
 *
 */

public class SemeruPayment {

	
	
	public CLogger log = CLogger.getCLogger(SemeruPayment.class);

	Vector<Vector<Object>> data = new Vector<Vector<Object>>();
	
	protected DecimalFormat format = DisplayType.getNumberFormat(DisplayType.Amount);
	
	private Properties ctx = Env.getCtx();
	protected BigDecimal totalPrices = Env.ZERO;
	protected BigDecimal initialPrice = Env.ZERO;
	protected BigDecimal totalLines = Env.ZERO;
	
	
	//Get Charge
	protected Vector<Vector<Object>> getChargeData(int C_Charge_ID,IMiniTable MiniTable) {
			
		MCharge charge = new MCharge(ctx, C_Charge_ID, null);
		Vector<Object> line = new Vector<Object>(3);
		
		
		KeyNamePair kp = new KeyNamePair(charge.getC_Charge_ID(),charge.getName());
		
		line.add(new Boolean(false));
		line.add(kp); 						// 2-Charge
		line.add(Env.ZERO);					// 3-Price
		
		data.add(line);
		
		reCalculate(MiniTable);
		
		return data;
	}
	
	//Remove Line
	public Vector<Vector<Object>> deletedata(int rowindex) {
		
		data.remove(rowindex);
		return data;
		
	}
	
	//Remove Line
		public Vector<Vector<Object>> deleteData(IMiniTable IMiniTable) {
			
			int count = 0;
			for(int i = 0 ; i < IMiniTable.getRowCount(); i ++){
				boolean isSelected = (boolean) IMiniTable.getValueAt(i, 0);
				
				
				if (isSelected){
					data.remove(i - count);
					count++;
				}
			}
			return data;
			
		}
	
	public void reCalculate(IMiniTable miniTable) {

		for (int i = 0; i < miniTable.getRowCount(); i++) {
			
			BigDecimal price = (BigDecimal) miniTable.getValueAt(i, 2);
			
			if (i == 0) {
				
				initialPrice = price;

				totalLines = initialPrice;
				
			} else {
							
				totalLines = initialPrice.add(price);
				initialPrice = totalLines;
			
			}
			
			totalPrices = totalLines;
			
		}
	}
	
	public void tableChangeCalculate(int row, int col, IMiniTable miniTable,int windowNo) {
		if (col == 2 ) {

			for (int i = 0; i < miniTable.getRowCount(); i++) {
		
				
				BigDecimal price = (BigDecimal) miniTable.getValueAt(i, 2);

				
				if (i == 0) {
					
					initialPrice = price;

					totalLines = initialPrice;
					
				} else {
								
					totalLines = initialPrice.add(price);
					initialPrice = totalLines;
				
				}
				
				totalPrices = totalLines;
				
			}
		}
	}
	
	public String checkReqInput(int AD_Org_ID, int C_BPartner_ID,int C_BankAccount_ID,Timestamp tglTrx, boolean IsFullPaid,String Pembayaran1,String TipeBayar1, IMiniTable MiniTable ){
		
		String msg = "";
		
		if (AD_Org_ID == 0){
			msg = "Kolom Cabang Harus Di Isi";
		}else if(C_BPartner_ID ==0){
			msg = "Kolom Karyawan Belum Di Isi";
		}else if (tglTrx == null){
			msg = "Tanggal Transaksi Belum Di Isi";
		}else if(MiniTable.getRowCount() == 0){
			msg = "Belum Ada Input Charge";
		}else if(Pembayaran1 =="0.00"){
			msg = "Pembayaran Masih Kosong";
		}else if(TipeBayar1.toUpperCase().equals("BANK")&& C_BankAccount_ID == 0){
			msg = "Bank Account Belum Terisi";
		}else if (!IsFullPaid){
			msg = "Total Pembayaran Masih Kurang Dari Total Yang Harus Dibayar";	
		}
		
		for (int i = 0 ; i < MiniTable.getRowCount() ; i++){
			
			KeyNamePair charge = (KeyNamePair)MiniTable.getValueAt(i, 1);
			String chargeName = charge.toString();
			
			BigDecimal price = (BigDecimal) MiniTable.getValueAt(i, 2);
			
			if (price.compareTo(Env.ZERO) <= 0){
				
				msg = "Belum Ada Input Pembayaran untuk Charge " + chargeName;
				
			}
			
		}
			
		return msg;
		
	}
	
	public String infoGeneratedDocumentInvoice(int C_Invoice_ID,int WindowNo){

		String msg = "";
		
		if (C_Invoice_ID > 0 ){
		
			//Info Invoice
			String msgInv = "\n"+ "No Faktur : Belum Terbentuk" ;
			if (C_Invoice_ID > 0){
				MInvoice inv = new MInvoice(Env.getCtx(),C_Invoice_ID,null);
				String noInv = inv.getDocumentNo();
				String statusInv = inv.getDocStatusName();
				msgInv = "No Faktur : " + noInv+"("+statusInv+")";
			}
	
			//infoResult
			msg = msgInv;
						
		}
		
		return msg;
	}

	public String infoGeneratedDocumentPayment(int C_Payment_ID,boolean IsSoTrx,int WindowNo){
		
		String msg = "";
		
		//Payment Info
		String payType = "";

		if(IsSoTrx){
			payType = "Penerimaan";
		}else if (!IsSoTrx){
			payType = "Pengeluaran";
		}
		
		String msgPay = "\n"+ "No "+payType+" : Belum Terbentuk" ;
		
		if (C_Payment_ID > 0){
							
			MPayment pay = new MPayment(ctx, C_Payment_ID, null);
			String NoPay = pay.getDocumentNo();
			String StatusPay = pay.getDocStatusName();
		
			msgPay = "\n"+ "No "+payType+" : "+NoPay+"("+StatusPay+")";

		}	
		msg = msgPay;
		
		return msg;
	}
	
	 protected int updateData(int SM_SemeruPOS_ID){
		 
			StringBuilder SQLExFunction = new StringBuilder();
	        SQLExFunction.append("SELECT update_print_info_pos(?)");
	        int rslt = 0;
	         
	     	PreparedStatement pstmt = null;
	     	ResultSet rs = null;
				try {
					pstmt = DB.prepareStatement(SQLExFunction.toString(), null);
					pstmt.setInt(1, SM_SemeruPOS_ID);	
					rs = pstmt.executeQuery();
					while (rs.next()) {
						rslt = rs.getInt(1);
					}

				} catch (SQLException err) {
					log.log(Level.SEVERE, SQLExFunction.toString(), err);
				} finally {
					DB.close(rs, pstmt);
					rs = null;
					pstmt = null;
				}
				
				
			 return rslt;
		 }

}
