package org.semeru.pos;

import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Properties;
import java.util.Vector;
import java.util.logging.Level;

import org.adempiere.webui.component.Button;
import org.adempiere.webui.component.Combobox;
import org.adempiere.webui.editor.WSearchEditor;
import org.adempiere.webui.editor.WTableDirEditor;
import org.adempiere.webui.event.ValueChangeListener;
import org.adempiere.webui.window.FDialog;
import org.compiere.minigrid.IMiniTable;
import org.compiere.model.MCharge;
import org.compiere.model.MInOut;
import org.compiere.model.MInvoice;
import org.compiere.model.MLookup;
import org.compiere.model.MLookupFactory;
import org.compiere.model.MOrder;
import org.compiere.model.MPayment;
import org.compiere.model.X_C_POSPayment;
import org.compiere.util.CLogger;
import org.compiere.util.DB;
import org.compiere.util.DisplayType;
import org.compiere.util.Env;
import org.compiere.util.KeyNamePair;
import org.semeru.pos.model.X_SM_SemeruPOSLine;


/**
 * 
 * @author Tegar N
 *
 */

public class SemeruPembatalanPenjualan {

	public CLogger log = CLogger.getCLogger(SemeruPembatalanPenjualan.class);
	private Properties ctx = Env.getCtx();
	Vector<Vector<Object>> data = new Vector<Vector<Object>>();
	protected DecimalFormat format = DisplayType.getNumberFormat(DisplayType.Amount);

	private int AD_Client_ID = Env.getAD_Client_ID(ctx);

	//Get poduct
	protected Vector<Vector<Object>> getTrxData(ArrayList<Integer> detailList,IMiniTable MiniTable, int Windono, ValueChangeListener crot) {
			
		
		
		 if (detailList.size() > 0){
		
			for (int i = 0; i < detailList.size(); i++){
				
				Vector<Object> line = new Vector<Object>(9);
				
				MLookup lookupBP = MLookupFactory.get(ctx, Windono, 0, 2893,DisplayType.Search);
				WSearchEditor PelangganSearch = new WSearchEditor("C_BPartner_ID", true, false, true,lookupBP);
				PelangganSearch.addValueChangeListener(crot);
				
				
				MLookup lookupClient = MLookupFactory.get(ctx, Windono, 0,14621, DisplayType.TableDir);
				WTableDirEditor TokoSearch = new WTableDirEditor("AD_Table_ID", true, false, true,lookupClient);
				TokoSearch.addValueChangeListener(crot);
				TokoSearch.setValue(AD_Client_ID);
				
				ArrayList<KeyNamePair> listPL = new ArrayList<KeyNamePair>();
				listPL.add(new KeyNamePair(1, "Satu"));
				listPL.add(new KeyNamePair(2, "Dua"));
				listPL.add(new KeyNamePair(3, "Tiga"));
				
				Combobox combobox = new Combobox();
				combobox.removeAllItems();
				for (KeyNamePair priceList : listPL) {
					combobox.appendItem(priceList.getName());
				}
				
				
				line.add(new Boolean(false));
				line.add(1);
				line.add(2);
				line.add(3);
				line.add(4);
				line.add(5);
				line.add(6);
				line.add(7);
				line.add(new Button("Proses"));
				line.add(combobox);
				line.add(PelangganSearch);
				line.add(TokoSearch);


				data.add(line);
				
			}
		}
		
		return data;
	}
	
	//Get Charge
	protected Vector<Vector<Object>> getChargeData(ArrayList<Integer> detailList,IMiniTable MiniTable) {
			
		Vector<Object> line = new Vector<Object>(1);
		
		
		 if (detailList.size() > 0){
		
			for (int i = 0; i < detailList.size(); i++){
				
				X_SM_SemeruPOSLine detailPOS = new X_SM_SemeruPOSLine(ctx, detailList.get(i), null);
				MCharge charge = new MCharge(ctx, detailPOS.getC_Charge_ID(), null);
				
				
				KeyNamePair kp = new KeyNamePair(charge.getC_Charge_ID(),charge.getName());

				
				line.add(new Boolean(false));
				line.add(kp); 											// 2-Charge
				line.add(detailPOS.getLineNetAmt());					// 3-Price
				
				data.add(line);				
			}
		}
		
		return data;
	}
		
	//Remove Line
	public Vector<Vector<Object>> deletedata(int rowindex) {
			
		data.remove(rowindex);
		return data;
			
	}
		
	
	public void infoGeneratedDocument(int C_Order_ID, int WindowNo){

		String message = "";
		String sqlInOut = "SELECT M_InOut_ID FROM M_InOut WHERE AD_Client_ID = ? AND C_Order_ID = ?";
		String sqlInvoice = "SELECT C_Invoice_ID FROM C_Invoice WHERE AD_Client_ID = ? AND C_Order_ID = ?";
		String sqlPosPay = "SELECT C_Payment_ID,TenderType FROM C_POSPayment WHERE AD_Client_ID = ? AND C_Order_ID = ?";
		
		int M_InOut_ID = DB.getSQLValueEx(Env.getCtx().toString(), sqlInOut.toString(), new Object[]{AD_Client_ID,C_Order_ID});
		
		int C_Invoice_ID = DB.getSQLValueEx(Env.getCtx().toString(), sqlInvoice.toString(), new Object[]{AD_Client_ID,C_Order_ID});
		
		
		//Info order
		MOrder order = new MOrder(Env.getCtx(), C_Order_ID, null);
		String noSo = order.getDocumentNo();
		String statusSO= order.getDocStatusName();
		String msgOrder = "No Sales Order : " + noSo +"("+statusSO+")";
		
		//Info Shipment
		MInOut InOut = new MInOut(Env.getCtx(), M_InOut_ID, null);
		String noShip = InOut.getDocumentNo();
		String statusShip = InOut.getDocStatusName();
		String msgInOut = "\n"+ "No Surat Jalan : " + noShip +"("+statusShip+")";
		
		//Info Invoice
		String msgInv = "\n"+ "No Faktur : Belum Terbentuk" ;
		if (C_Invoice_ID > 0){
			MInvoice inv = new MInvoice(Env.getCtx(),C_Invoice_ID,null);
			String noInv = inv.getDocumentNo();
			String statusInv = inv.getDocStatusName();
			msgInv = "\n"+ "No Faktur : " + noInv+"("+statusInv+")";
		}

		//infoResult
		message = msgOrder + msgInOut + msgInv;
		
		//Info AR Receipt
		String msgAR = "\n"+ "No Penerimaan :  Belum Terbentuk ";
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			pstmt = DB.prepareStatement(sqlPosPay.toString(), null);
			pstmt.setInt(1, AD_Client_ID);
			pstmt.setInt(2, C_Order_ID);
			rs = pstmt.executeQuery();
			while (rs.next()) {
				int C_Payment_ID = rs.getInt(1);
				MPayment AR = new MPayment(Env.getCtx(), C_Payment_ID, null);
				String noAR = AR.getDocumentNo();
				String payType = rs.getString(2);
				String payAmt = format.format(AR.getPayAmt());
				String statusAR = AR.getDocStatusName();
				
				if (payType.equals("L")){
					payType = "Leasing";
				}else if (payType.equals("X")){
					payType = "Tunai";
				}
						
				msgAR = "\n"+ "No Penerimaan : " + noAR +"("+payType+" : "+payAmt+" - "+statusAR+")";
				message = message + msgAR;
				msgAR = "";
			}

		} catch (SQLException e) {
			log.log(Level.SEVERE, sqlPosPay.toString(), e);
		} finally {
			DB.close(rs, pstmt);
			rs = null;
			pstmt = null;
		}
		
		message = message+msgAR;
		FDialog.info(WindowNo,null,"",message,"Dokumen Terbentuk");
		
	}
	
	public void createPOSPayment(int AD_Org_ID,int C_Order_ID,int C_POSTenderType_ID, String tenderType, 
			BigDecimal payAmt,String leasingProvider, int C_BankAccount_ID){
		
		MOrder order = new MOrder(Env.getCtx(), C_Order_ID, null); 
		
		X_C_POSPayment posPayment = new X_C_POSPayment(order.getCtx(), 0, order.get_TrxName());
		
		posPayment.setAD_Org_ID(AD_Org_ID);
		posPayment.setC_Order_ID(C_Order_ID);
		posPayment.setC_POSTenderType_ID(C_POSTenderType_ID);
		posPayment.setTenderType(tenderType);
		posPayment.setPayAmt(payAmt);
		posPayment.set_CustomColumn("LeaseProvider", leasingProvider);
		posPayment.set_CustomColumn("C_BankAccount_ID", C_BankAccount_ID);
		posPayment.saveEx();
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
	
	public int checkShipmentRelated(int C_Order_ID){
		int M_Inout_ID = 0;
		
		String sqlInOut = "SELECT M_InOut_ID FROM M_InOut WHERE AD_Client_ID = ? AND C_Order_ID = ?";
		M_Inout_ID = DB.getSQLValueEx(Env.getCtx().toString(), sqlInOut, new Object[]{AD_Client_ID,C_Order_ID});
		
		return M_Inout_ID;
	}
	
	public int checkInvoiceRelated(int C_Order_ID){
		int C_Invoice_ID = 0;
		
		String sqlInvoice = "SELECT C_Invoice_ID FROM C_Invoice WHERE AD_Client_ID = ? AND C_Order_ID = ?";
		C_Invoice_ID  = DB.getSQLValueEx(Env.getCtx().toString(), sqlInvoice, new Object[]{AD_Client_ID,C_Order_ID});
		
		return C_Invoice_ID ;
	}
	
}
