package org.semeru.process;

import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.logging.Level;

import org.compiere.model.MAllocationHdr;
import org.compiere.model.MAllocationLine;
import org.compiere.model.MBPartnerLocation;
import org.compiere.model.MBankAccount;
import org.compiere.model.MBankStatement;
import org.compiere.model.MBankStatementLine;
import org.compiere.model.MDocType;
import org.compiere.model.MInOut;
import org.compiere.model.MInOutLine;
import org.compiere.model.MInventory;
import org.compiere.model.MInventoryLine;
import org.compiere.model.MInvoice;
import org.compiere.model.MInvoiceLine;
import org.compiere.model.MMovement;
import org.compiere.model.MMovementLine;
import org.compiere.model.MOrder;
import org.compiere.model.MOrderLine;
import org.compiere.model.MOrg;
import org.compiere.model.MPayment;
import org.compiere.model.MPriceList;
import org.compiere.model.MProduct;
import org.compiere.model.MProductPricing;
import org.compiere.model.MRMA;
import org.compiere.model.MRMALine;
import org.compiere.process.ProcessInfoParameter;
import org.compiere.process.SvrProcess;
import org.compiere.util.DB;
import org.compiere.util.Env;
import org.semeru.model.X_I_TransactionExt_Temp;
import org.semeru.ws.model.SMR_Model_BankTransfer;
import org.semeru.ws.model.SMR_Model_BayarHutang;
import org.semeru.ws.model.SMR_Model_Movement;
import org.semeru.ws.model.SMR_Model_Movement_Detail;
import org.semeru.ws.model.SMR_Model_Pembatalan;
import org.semeru.ws.model.SMR_Model_Pembatalan_Detail;
import org.semeru.ws.model.SMR_Model_PenerimaanBarang;
import org.semeru.ws.model.SMR_Model_PenerimaanBarang_Detail;
import org.semeru.ws.model.SMR_Model_PengembalianKeAgen;
import org.semeru.ws.model.SMR_Model_PengembalianKeAgen_Detail;
import org.semeru.ws.model.SMR_Model_PettyCash;
import org.semeru.ws.model.SMR_Model_PettyCash_Detail;
import org.semeru.ws.model.SMR_Model_StockOpname;
import org.semeru.ws.model.SMR_Model_StockOpname_Detail;
import org.semeru.ws.model.SMR_Model_TambahSaldo;
import org.semeru.ws.model.SMR_Model_TambahSaldo_Detail;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class SMR_ProcessTransactionExt extends SvrProcess {

	private int p_AD_Client_ID = 0;
	private int p_AD_Org_ID = 0;
	private int p_Trx_ID = 0;
//	private int p_Sequence = 0;
	private final String ERROR = "ERROR";

	
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
				p_Trx_ID = (int)para[i].getParameterAsInt();
			
//			else if(name.equals("sequence")){
//				p_Sequence = (int)para[i].getParameterAsInt();
//
//			}
			else
				log.log(Level.SEVERE, "Unknown Parameter: " + name);
		}
		
	}

	@Override
	protected String doIt() throws Exception {

		String JSonTrx = "";
		String JSonTrxDetail = "";
		int AD_Client_ID = 0;
		int AD_Org_ID= 0;
		int I_TransactionExt_Temp_ID = 0;
		String rslt = "";

		StringBuilder SQLExtractJSon = new StringBuilder();
		SQLExtractJSon.append("SELECT AD_Client_ID,AD_Org_ID,transactions,transaction_detail,i_transactionext_temp_id,pos ");
		SQLExtractJSon.append(" FROM  I_TransactionExt_Temp ");
		SQLExtractJSon.append(" WHERE AD_Client_ID = " + p_AD_Client_ID );
		SQLExtractJSon.append(" AND AD_Org_ID = " + p_AD_Org_ID);
		SQLExtractJSon.append(" AND transaction_id = " + p_Trx_ID);
		SQLExtractJSon.append(" AND insert_transaction = 'N' ");
			
		PreparedStatement pstmt = null;
     	ResultSet rs = null;
			try {
				pstmt = DB.prepareStatement(SQLExtractJSon.toString(), null);
				rs = pstmt.executeQuery();
				while (rs.next()) {
					
					AD_Client_ID = rs.getInt(1);
					AD_Org_ID = rs.getInt(2);
					JSonTrx = rs.getString(3);
					JSonTrxDetail = rs.getString(4);
					I_TransactionExt_Temp_ID = rs.getInt(5);
					String POSType = rs.getString(6); 
					Gson gson = new Gson();
					JsonObject jsonHeader = new JsonObject();
					JsonObject jsonDetail = new JsonObject();
					JsonArray jsonDetailArray = new JsonArray();
					
					JsonParser parserHeader = new JsonParser();
					JsonParser parserDetail = new JsonParser();

					jsonHeader = parserHeader.parse(JSonTrx).getAsJsonObject();				
										
					if(POSType.equals("T")){
						//POS - Bank Transfer
						//Done
						SMR_Model_BankTransfer data = gson.fromJson(jsonHeader.toString(), SMR_Model_BankTransfer.class);
						rslt = generateBankTransfer(AD_Client_ID, AD_Org_ID, data);
					
					}else if(POSType.equals("S")){
						//POS - Add Saldo
						//Done
						jsonDetail = parserDetail.parse(JSonTrxDetail).getAsJsonObject();
						SMR_Model_TambahSaldo dataHeader = gson.fromJson(jsonHeader.toString(), SMR_Model_TambahSaldo.class);
						SMR_Model_TambahSaldo_Detail dataDetail = gson.fromJson(jsonDetail.toString(), SMR_Model_TambahSaldo_Detail.class);
						rslt = generateTambahSaldo(AD_Client_ID, AD_Org_ID, dataHeader,dataDetail);
					
					}else if(POSType.equals("P")){
						//POS - Petty Cash Pengeluaran dan Penerimaan
						//Done
						jsonDetailArray = parserDetail.parse(JSonTrxDetail).getAsJsonArray();
						SMR_Model_PettyCash dataHeader = gson.fromJson(jsonHeader.toString(), SMR_Model_PettyCash.class);
						SMR_Model_PettyCash_Detail[] dataDetail = gson.fromJson(jsonDetailArray.toString(), SMR_Model_PettyCash_Detail[].class);
						rslt = generatePettyCash(AD_Client_ID, AD_Org_ID, dataHeader,dataDetail);
				
					}else if(POSType.equals("H")){
						//POS - Bayar Hutang
						//Done
						SMR_Model_BayarHutang data = gson.fromJson(jsonHeader.toString(), SMR_Model_BayarHutang.class);
						rslt = generateBayarHutang(AD_Client_ID, AD_Org_ID, data);
					
					}else if(POSType.equals("B")){
						//POS - Pembatalan
						//Need To Test
						jsonDetailArray = parserDetail.parse(JSonTrxDetail).getAsJsonArray();
						SMR_Model_Pembatalan dataHeader = gson.fromJson(jsonHeader.toString(), SMR_Model_Pembatalan.class);
						SMR_Model_Pembatalan_Detail[] dataDetail = gson.fromJson(jsonDetailArray.toString(), SMR_Model_Pembatalan_Detail[].class);
						
						for (int i = 0 ; i < 4 ; i++){
							
							rslt = generatePembatalan(AD_Client_ID, AD_Org_ID, dataHeader,dataDetail,i);
							
							if(rslt == ERROR) {
								
								break;
								
							}
							
						}
						
					
					}else if(POSType.equals("M")){
						//Inventory - Penerimaan Barang/Pembelian
						//Done
						jsonDetailArray = parserDetail.parse(JSonTrxDetail).getAsJsonArray();
						SMR_Model_PenerimaanBarang dataHeader = gson.fromJson(jsonHeader.toString(), SMR_Model_PenerimaanBarang.class);
						SMR_Model_PenerimaanBarang_Detail[] dataDetail = gson.fromJson(jsonDetailArray.toString(), SMR_Model_PenerimaanBarang_Detail[].class);
						rslt = generatePenerimaanBarang(AD_Client_ID, AD_Org_ID, dataHeader,dataDetail);
					
					}else if(POSType.equals("V")){
						//Inventory - Pengembalian Barang Ke Agen
						//Done
						jsonDetailArray = parserDetail.parse(JSonTrxDetail).getAsJsonArray();
						SMR_Model_PengembalianKeAgen dataHeader = gson.fromJson(jsonHeader.toString(), SMR_Model_PengembalianKeAgen.class);
						SMR_Model_PengembalianKeAgen_Detail[] dataDetail = gson.fromJson(jsonDetailArray.toString(), SMR_Model_PengembalianKeAgen_Detail[].class);

						rslt = generatePengembalianKeAgen(AD_Client_ID, AD_Org_ID, dataHeader,dataDetail);
					
					}else if(POSType.equals("K")){
						//Inventory - Perpindahan Barang Antar Lokasi
						//Done
						jsonDetailArray = parserDetail.parse(JSonTrxDetail).getAsJsonArray();
						SMR_Model_Movement dataHeader = gson.fromJson(jsonHeader.toString(), SMR_Model_Movement.class);
						SMR_Model_Movement_Detail[] dataDetail = gson.fromJson(jsonDetailArray.toString(), SMR_Model_Movement_Detail[].class);

						rslt = generatePerpindahanBarang(AD_Client_ID, AD_Org_ID, dataHeader,dataDetail);
					
					}else if(POSType.equals("O")){
						//Inventory - Stock Opname
						//Done
						jsonDetailArray = parserDetail.parse(JSonTrxDetail).getAsJsonArray();
						SMR_Model_StockOpname dataHeader = gson.fromJson(jsonHeader.toString(), SMR_Model_StockOpname.class);
						SMR_Model_StockOpname_Detail[] dataDetail = gson.fromJson(jsonDetailArray.toString(), SMR_Model_StockOpname_Detail[].class);
						rslt = generateStockOpname(AD_Client_ID, AD_Org_ID, dataHeader,dataDetail);
					
					}
					 
				}

			} catch (SQLException err) {
				log.log(Level.SEVERE, SQLExtractJSon.toString(), err);
				rslt = ERROR;
			} finally {
				DB.close(rs, pstmt);
				rs = null;
				pstmt = null;
			}
			
		if(rslt.equals(ERROR)){
			
			rollback();
			 
		    X_I_TransactionExt_Temp temp = new X_I_TransactionExt_Temp(getCtx(), I_TransactionExt_Temp_ID, get_TrxName());
		    temp.setinsert_transaction(false);
		    temp.setResult("Transaksi Gagal");
		    temp.saveEx();	  
	    		    	
		 }else{
			
			X_I_TransactionExt_Temp temp = new X_I_TransactionExt_Temp(getCtx(), I_TransactionExt_Temp_ID, get_TrxName());
		    temp.setinsert_transaction(true);
		    temp.setResult(rslt);
		    temp.saveEx();	  
			 
		 }
			
		return "";
	}
	
	
	private String generateBankTransfer(int AD_Client_ID,int AD_Org_ID,SMR_Model_BankTransfer data)
	{

		String rslt = "";
		
		try {
			
			MBankAccount mBankFrom = new MBankAccount(getCtx(),data.C_BankAccountFrom_ID, get_TrxName());
			MBankAccount mBankTo = new MBankAccount(getCtx(),data.C_BankAccountTo_ID, get_TrxName());
			
			Timestamp datestat = DataSetupValidation.convertStringToTimeStamp(data.StatementDate);

			
			MPayment paymentBankFrom = new MPayment(getCtx(), 0 ,  get_TrxName());
			paymentBankFrom.setC_BankAccount_ID(mBankFrom.getC_BankAccount_ID());
		//	paymentBankFrom.setClientOrg(AD_Client_ID, AD_Org_ID);
			paymentBankFrom.setAD_Org_ID(AD_Org_ID);
			paymentBankFrom.setIsReceipt(false);
			//paymentBankFrom.setDocumentNo(p_DocumentNo);
			//paymentBankFrom.setC_ConversionType_ID(p_C_ConversionType_ID);	
			paymentBankFrom.setDateAcct(datestat);
			paymentBankFrom.setDateTrx(datestat);
			paymentBankFrom.setTenderType(MPayment.TENDERTYPE_DirectDeposit);
			paymentBankFrom.setDescription(data.Description);
			paymentBankFrom.setC_BPartner_ID (data.C_BPartner_ID);
			paymentBankFrom.setC_Currency_ID(data.C_Currency_ID);
			paymentBankFrom.setPayAmt(data.AmtTransfer);
			paymentBankFrom.setOverUnderAmt(Env.ZERO);
			paymentBankFrom.setC_DocType_ID(false);
			paymentBankFrom.setC_Charge_ID(data.C_Charge_ID);
			paymentBankFrom.saveEx();
			if(!paymentBankFrom.processIt(MPayment.DOCACTION_Complete)) {
				log.warning("Payment Process Failed: " + paymentBankFrom + " - " + paymentBankFrom.getProcessMsg());
				throw new IllegalStateException("Payment Process Failed: " + paymentBankFrom + " - " + paymentBankFrom.getProcessMsg());
			}
			paymentBankFrom.saveEx();
	
			MPayment paymentBankTo = new MPayment(getCtx(), 0 ,  get_TrxName());
			paymentBankTo.setC_BankAccount_ID(mBankTo.getC_BankAccount_ID());
			//paymentBankTo.setClientOrg(AD_Client_ID, AD_Org_ID);
			paymentBankTo.setAD_Org_ID(AD_Org_ID);
			paymentBankTo.setIsReceipt(true);
			//paymentBankTo.setDocumentNo(p_DocumentNo);
			//paymentBankTo.setC_ConversionType_ID(p_C_ConversionType_ID);	
			paymentBankTo.setDateAcct(datestat);
			paymentBankTo.setDateTrx(datestat);
			paymentBankTo.setTenderType(MPayment.TENDERTYPE_DirectDeposit);
			paymentBankTo.setDescription(data.Description);
			paymentBankTo.setC_BPartner_ID (data.C_BPartner_ID);
			paymentBankTo.setC_Currency_ID(data.C_Currency_ID);
			paymentBankTo.setPayAmt(data.AmtTransfer);
			paymentBankTo.setOverUnderAmt(Env.ZERO);
			paymentBankTo.setC_DocType_ID(true);
			paymentBankTo.setC_Charge_ID(data.C_Charge_ID);
			paymentBankTo.saveEx();
			if (!paymentBankTo.processIt(MPayment.DOCACTION_Complete)) {
				log.warning("Payment Process Failed: " + paymentBankTo + " - " + paymentBankTo.getProcessMsg());
				throw new IllegalStateException("Payment Process Failed: " + paymentBankTo + " - " + paymentBankTo.getProcessMsg());
			}
			paymentBankTo.saveEx();
					
			if(paymentBankFrom != null && paymentBankTo != null){
				rslt = "Transaksi Transfer Berhasil";
			}else{
				rslt = ERROR;
			}
			
		} catch (Exception e) {
			rslt = ERROR;
		}
		return rslt;

	} 
	
	private String generateTambahSaldo(int AD_Client_ID,int AD_Org_ID,SMR_Model_TambahSaldo dataHeader,SMR_Model_TambahSaldo_Detail dataDetail)
	{

		String rslt = "";

		try {
			
			MBankStatement BStmt = new MBankStatement(getCtx(), 0, get_TrxName());
			MBankStatementLine BStmtLine = null;
				
			Timestamp datestat = DataSetupValidation.convertStringToTimeStamp(dataHeader.StatementDate);
			
			MOrg org = new MOrg(getCtx(), AD_Org_ID, get_TrxName());
			//BStmt.setClientOrg(AD_Client_ID, AD_Org_ID);
			BStmt.setAD_Org_ID(AD_Org_ID);
			BStmt.setC_BankAccount_ID(dataHeader.C_BankAccount_ID);
			BStmt.setName("Tambah Saldo "+ org.getName() + dataHeader.StatementDate.toString());
			BStmt.setDescription("Tambah Saldo "+ org.getName() + dataHeader.StatementDate.toString());
			BStmt.setDateAcct(datestat);
			BStmt.saveEx();
						
			if(BStmt != null){
						
				BStmtLine =  new MBankStatementLine(getCtx(), 0, get_TrxName());
				//BStmtLine.setClientOrg(AD_Client_ID, AD_Org_ID);
				BStmtLine.setAD_Org_ID(AD_Org_ID);
				BStmtLine.setC_BankStatement_ID(BStmt.getC_BankStatement_ID());
				BStmtLine.setDescription("Tambah Saldo :" + dataDetail.StmtAmt.toString());
				BStmtLine.setStatementLineDate(datestat);
				BStmtLine.setDateAcct(datestat);
				BStmtLine.setValutaDate(datestat);
				BStmtLine.setStmtAmt(dataDetail.StmtAmt);
				System.out.println(dataDetail.C_Charge_ID);
				BStmtLine.setC_Charge_ID(dataDetail.C_Charge_ID);
				BStmtLine.setC_Currency_ID(dataDetail.C_Currency_ID);
				BStmtLine.saveEx();
			}
			
			if(BStmt != null & BStmtLine != null){
				
				rslt = "Penambahan Saldo Berhasil";
				BStmt.processIt(MBankStatement.DOCACTION_Complete);
				
			}else{
				rslt = ERROR;
			}
		
		} catch (Exception e) {
			rslt = ERROR;
		}
		
		return rslt;

	} 
	

	private String generatePettyCash(int AD_Client_ID,int AD_Org_ID,SMR_Model_PettyCash dataHeader,SMR_Model_PettyCash_Detail[] dataDetail){
		
		String rslt = "";
		
		try {
						
			MPayment pay = null;
			ArrayList<Integer> completePay = new ArrayList<Integer>();

			for(SMR_Model_PettyCash_Detail detail: dataDetail){
			
				pay = new MPayment(getCtx(), 0, get_TrxName());
				Integer docTypePayment_ID = 0;
				//pay.setClientOrg(AD_Client_ID,AD_Org_ID);
				
				Timestamp dateInvoiced = DataSetupValidation.convertStringToTimeStamp(dataHeader.DateInvoiced);

				pay.setAD_Org_ID(AD_Org_ID);
				
				if(dataHeader.IsReceipt.equals("Y")){
					docTypePayment_ID = DataSetupValidation.getChargeDocType(AD_Client_ID,false,true,true);
				}else{
					docTypePayment_ID = DataSetupValidation.getChargeDocType(AD_Client_ID,false,true,false);
					}
				pay.setC_DocType_ID(docTypePayment_ID);
				if(dataHeader.IsReceipt.equals("Y")){
					pay.setIsReceipt(true);
				}else{
					pay.setIsReceipt(false);
				}
				pay.setC_BPartner_ID(dataHeader.C_BPartner_ID);
				pay.setDescription(dataHeader.Description);
				pay.setDateTrx(dateInvoiced);
				pay.setDateAcct(dateInvoiced);
				pay.setC_BankAccount_ID(dataHeader.C_BankAccount_ID);
				pay.setTenderType(MPayment.TENDERTYPE_Cash);
				pay.setPayAmt(detail.Amount);
				pay.setC_Currency_ID(dataHeader.C_Currency_ID);
				pay.setC_Charge_ID(detail.C_Charge_ID);
				pay.saveEx();
				
				if(pay != null){
					pay.processIt(MPayment.DOCACTION_Complete);
					pay.saveEx();
					
					completePay.add(1);
					
				}
			}	
			
			boolean OK = true;
			for(int i=0; i <completePay.size() ; i++ ){
			
				if(completePay.get(i) == 0){
					OK = false;
				}
				
			}
			
			if(OK){
				
				System.out.println(pay.getDocumentNo());

				if(dataHeader.IsReceipt.equals("Y")){
					rslt = "Penerimaan Uang Berhasil";
				}else{
					rslt = "Pengeluaran Uang Berhasil";
				}
				
			}else{
				rslt = ERROR;
			}
			
		
		} catch (Exception e) {
			rslt = ERROR;
		}
		
		return rslt;
		
	}
	
	
	private String generateStockOpname(int AD_Client_ID,int AD_Org_ID,SMR_Model_StockOpname dataHeader,SMR_Model_StockOpname_Detail[] dataDetails)
	{

		String rslt = "";

		try {
			
			MInventory Inven = new MInventory(getCtx(), 0, get_TrxName());
			MInventoryLine InvenLine = null;
			Timestamp dateMove = DataSetupValidation.convertStringToTimeStamp(dataHeader.MovementDate);

			Integer DocType_ID = DataSetupValidation.getDocType_ID(AD_Client_ID, MDocType.DOCBASETYPE_MaterialPhysicalInventory, "N",MDocType.DOCSUBTYPEINV_PhysicalInventory);
			
			Inven.setClientOrg(AD_Client_ID, AD_Org_ID);
			Inven.setM_Warehouse_ID(dataHeader.M_Warehouse_ID);
			Inven.setMovementDate(dateMove);
			Inven.setC_DocType_ID(DocType_ID);
			//Inven.setDescription(dataHeader.Description);
			Inven.saveEx();
						
			if(Inven != null){
				
				for (SMR_Model_StockOpname_Detail dataDetail : dataDetails ){
						
					InvenLine =  new MInventoryLine(getCtx(), 0, get_TrxName());
					//InvenLine.setClientOrg(AD_Client_ID, AD_Org_ID);
					InvenLine.setAD_Org_ID(AD_Org_ID);
					InvenLine.setM_Inventory_ID(Inven.getM_Inventory_ID());
					InvenLine.setM_Locator_ID(dataDetail.M_Locator_ID);
					InvenLine.setM_Product_ID(dataDetail.M_Product_ID);
					//InvenLine.setM_AttributeSetInstance_ID(dataDetail.M_AttributeSetInstance_ID);
					InvenLine.setInventoryType(dataDetail.InventoryType);
					
					BigDecimal qtyBook = DataSetupValidation.setQtyBook(0, dataDetail.M_Product_ID, dataDetail.M_Locator_ID);
					
					InvenLine.setQtyBook(qtyBook);
					InvenLine.setQtyCount(dataDetail.QtyCount);
					InvenLine.saveEx();
				}
			}
			
			if(Inven != null & InvenLine != null){
				
				rslt = "Proses Stock Opname Berhasil";
				Inven.processIt(MInventory.DOCACTION_Complete);
				
			}else{
				rslt = ERROR;
			}
		
		} catch (Exception e) {
			rslt = ERROR;
		}
		
		return rslt;

	} 
	
	
	private String generatePerpindahanBarang(int AD_Client_ID,int AD_Org_ID,SMR_Model_Movement dataHeader,SMR_Model_Movement_Detail[] dataDetails)
	{

		String rslt = "";

		try {
			
			MMovement Move = new MMovement(getCtx(), 0, get_TrxName());
			MMovementLine MoveLine = null;
			
			
			Timestamp dateMove = DataSetupValidation.convertStringToTimeStamp(dataHeader.MovementDate);

			//Move.setClientOrg(AD_Client_ID, AD_Org_ID);
			Move.setAD_Org_ID(AD_Org_ID);
			Move.set_CustomColumn("M_Warehouse_ID", dataHeader.M_Warehouse_ID);
			Move.set_CustomColumn("M_WarehouseTo_ID", dataHeader.M_Warehouse_ID);
			Move.setMovementDate(dateMove);	
			//Move.setDescription(dataHeader.Description);
			Move.saveEx();
						
			if(Move != null){
						
				for (SMR_Model_Movement_Detail dataDetail :dataDetails){
				
				MoveLine =  new MMovementLine(getCtx(), 0, get_TrxName());
				//MoveLine.setClientOrg(AD_Client_ID, AD_Org_ID);
				MoveLine.setAD_Org_ID(AD_Org_ID);
				MoveLine.setM_Movement_ID(Move.getM_Movement_ID());	
				//MoveLine.setDescription(dataDetail.Description);
				//MoveLine.setValue(dataDetail.Value);
				MoveLine.setM_Product_ID(dataDetail.M_Product_ID);
				//MoveLine.setM_AttributeSetInstance_ID(dataDetail.M_AttributeSetInstance_ID);
				MoveLine.setM_Locator_ID(dataDetail.M_LocatorFrom_ID);
				MoveLine.setM_LocatorTo_ID(dataDetail.M_LocatorTo_ID);
				MoveLine.setMovementQty(dataDetail.MovementQty);
				MoveLine.saveEx();
				}
				
			}
			
			if(Move != null & MoveLine != null){
				
				rslt = "Perpindahan Barang Berhasil";
				Move.processIt(MInventory.DOCACTION_Complete);
				
			}else{
				rslt = ERROR;
			}
		
		} catch (Exception e) {
			rslt = ERROR;
		}
		
		return rslt;

	} 
	
	private String generateBayarHutang(int AD_Client_ID,int AD_Org_ID,SMR_Model_BayarHutang dataHeader)
	{

		String rslt = "";

		try {
			MPayment pay = null;
			StringBuilder getInvoice = new StringBuilder();
			getInvoice.append("SELECT C_Invoice_ID ");
			getInvoice.append(" FROM C_Invoice ");
			getInvoice.append(" WHERE AD_Client_ID = "+ AD_Client_ID);
			getInvoice.append(" AND C_Order_ID = " + dataHeader.C_Order_ID);
			getInvoice.append(" AND IsPaid = '"+"N"+"'");
			
			Integer C_Invoice_ID = DB.getSQLValueEx(get_TrxName(), getInvoice.toString());
			
			Timestamp dateTrx = DataSetupValidation.convertStringToTimeStamp(dataHeader.DateTrx);


			
			if(C_Invoice_ID > 0){
				
				MInvoice Inv = new MInvoice(getCtx(), C_Invoice_ID, get_TrxName());
				BigDecimal leftPay = DataSetupValidation.getPaymentLeft(AD_Client_ID, dataHeader.C_Order_ID);
				
				if(leftPay.compareTo(Env.ZERO) > 0){
									
					pay = new MPayment(getCtx(), 0, get_TrxName());
				//	pay.setClientOrg(AD_Client_ID,AD_Org_ID);
					pay.setAD_Org_ID(AD_Org_ID);
					Integer docTypePayment_ID = DataSetupValidation.getChargeDocType(AD_Client_ID,false,true,false);
					pay.setIsReceipt(false);
					pay.setC_DocType_ID(docTypePayment_ID);
					pay.setC_BPartner_ID(Inv.getC_BPartner_ID());
					pay.setDescription(Inv.getDescription());
					pay.setDateTrx(dateTrx);
					pay.setDateAcct(dateTrx);
					pay.setC_BankAccount_ID(dataHeader.C_BankAccount_ID);
					pay.setTenderType(MPayment.TENDERTYPE_Cash);
					pay.setPayAmt(dataHeader.PayAmt);
					pay.setC_Currency_ID(Inv.getC_Currency_ID());
					pay.setC_Invoice_ID(Inv.getC_Invoice_ID());				
					pay.saveEx();			
					
				}
				
			}
			
			if(pay != null){
				
				rslt = "Pembayaran Hutang Berhasil";
				pay.processIt(MPayment.DOCACTION_Complete);
				pay.saveEx();
				
				ArrayList<Integer> RMAPayment = new ArrayList<Integer>();
				RMAPayment = DataSetupValidation.getPaymentRMA(AD_Client_ID, dataHeader.C_Order_ID);
				
				if(RMAPayment != null && RMAPayment.size() > 0){
					
					for(int i = 0 ; i<RMAPayment.size(); i++){
						
						MPayment paymentRMA = new MPayment(getCtx(), RMAPayment.get(i), get_TrxName());
						
						if(paymentRMA.getDocStatus().equals(MPayment.DOCSTATUS_Drafted)||
								paymentRMA.getDocStatus().equals(MPayment.DOCSTATUS_InProgress)){
							
							paymentRMA.processIt(MPayment.DOCACTION_Complete);
							paymentRMA.saveEx();
							
						}
						
					}
					
				}
				
				
			}else{
				rslt = ERROR;
			}
		
		} catch (Exception e) {
			rslt = ERROR;
		}
		
		return rslt;

	} 
	
	
	private String generatePembatalan(int AD_Client_ID,int AD_Org_ID,SMR_Model_Pembatalan dataHeader, SMR_Model_Pembatalan_Detail[] dataDetails, Integer Sequence)
	{

		String rslt = "";
//		Integer detailCount = 0;
	//	MOrder ordNew = null;
//		MOrder OrderNew = null;
		MPayment pay = null;
		final String EDIT = "E";
		final String FULL = "F";

		try {
		
			//HashMap<String, Integer> documentRelated = new HashMap<String, Integer>();
			
			Integer C_Order_ID = dataHeader.C_Order_ID ;
			
			
			if(C_Order_ID > 0){
				
				//documentRelated = DataSetupValidation.getDocumentRelated(AD_Client_ID, C_Order_ID);
				
				MInvoice inv = null;
				MInOut inout = null;
				MOrder order = null;
				
				Integer Pay_ID = 0;
				Integer Inv_ID = 0;
				Integer Ship_ID = 0;
				Integer Ord_ID = 0;

				
				if(Sequence == 0 ){
					
					
					StringBuilder SQLGetInv = new StringBuilder();
					SQLGetInv.append("SELECT C_Invoice_ID ");
					SQLGetInv.append(" FROM C_Invoice ");
					SQLGetInv.append(" WHERE AD_Client_ID = "+ AD_Client_ID);
					SQLGetInv.append(" AND C_Order_ID = " + C_Order_ID);
					SQLGetInv.append(" AND DocStatus = 'CO' ");
					
					Inv_ID = DB.getSQLValueEx(null, SQLGetInv.toString());
					
					if(Inv_ID > 0){
						
						StringBuilder SQLGetPay = new StringBuilder();
						SQLGetPay.append("SELECT C_Payment_ID ");
						SQLGetPay.append(" FROM C_Payment ");
						SQLGetPay.append(" WHERE AD_Client_ID = "+ AD_Client_ID);
						SQLGetPay.append(" AND C_Invoice_ID = " + Inv_ID);
						SQLGetPay.append(" AND DocStatus = 'CO' ");
						
						Pay_ID = DB.getSQLValueEx(null, SQLGetPay.toString());
						
						if(Pay_ID > 0){
							pay = new MPayment(getCtx(), Pay_ID, get_TrxName()); 
							if(pay.getDocStatus().equals(MPayment.DOCSTATUS_Completed)){
								if(!pay.processIt(MPayment.DOCACTION_Reverse_Correct)) {
									rslt = ERROR;
								}	
								
								
							}else if (pay.getDocStatus().equals(MPayment.DOCSTATUS_InProgress) ||pay.getDocStatus().equals(MPayment.DOCSTATUS_Drafted)){
								pay.processIt(MPayment.DOCACTION_Void);			
							}
							pay.saveEx();
							System.out.println("Pembatalan Payment :"+pay.getDocStatus());
						}
						
						
					}
					
						
				}else if(Sequence == 1){
					
					StringBuilder SQLGetInv = new StringBuilder();
					SQLGetInv.append("SELECT C_Invoice_ID ");
					SQLGetInv.append(" FROM C_Invoice ");
					SQLGetInv.append(" WHERE AD_Client_ID = "+ AD_Client_ID);
					SQLGetInv.append(" AND C_Order_ID = " + C_Order_ID);
					SQLGetInv.append(" AND DocStatus = 'CO' ");
					
					Inv_ID = DB.getSQLValueEx(null, SQLGetInv.toString());
					
					
					if(Inv_ID > 0){
						inv = new MInvoice(getCtx(), Inv_ID, get_TrxName());
						if(inv.getDocStatus().equals(MInvoice.DOCSTATUS_Completed)){
							inv.processIt(MInvoice.DOCACTION_Reverse_Correct);
						}else if (inv.getDocStatus().equals(MInvoice.DOCSTATUS_InProgress) ||inv.getDocStatus().equals(MInvoice.DOCSTATUS_Drafted)){
							inv.processIt(MInvoice.DOCACTION_Void);
						}
						inv.saveEx();
						System.out.println("Pembatalan Invoice :"+inv.getDocStatus());
					}
					
				}else if(Sequence == 2){
					

					StringBuilder SQLGetShip = new StringBuilder();
					SQLGetShip.append("SELECT M_InOut_ID ");
					SQLGetShip.append(" FROM M_InOut ");
					SQLGetShip.append(" WHERE AD_Client_ID = "+ AD_Client_ID);
					SQLGetShip.append(" AND C_Order_ID = " + C_Order_ID);
					SQLGetShip.append(" AND DocStatus = 'CO' ");
					
					Ship_ID = DB.getSQLValueEx(null, SQLGetShip.toString());
					
					if(Ship_ID > 0){
						inout = new MInOut(getCtx(), Ship_ID, get_TrxName());
						if(inout.getDocStatus().equals(MInOut.DOCSTATUS_Completed)){
							inout.processIt(MInOut.DOCACTION_Reverse_Correct);
						}else if (inout.getDocStatus().equals(MInOut.DOCSTATUS_InProgress) ||inout.getDocStatus().equals(MInOut.DOCSTATUS_Drafted)){
							inout.processIt(MInOut.DOCACTION_Void);
						}
						inout.saveEx();
						System.out.println("Pembatalan Shipment:"+inout.getDocStatus());
					}
				
				}else if(Sequence == 3 ){
					Ord_ID = C_Order_ID;

					if(Ord_ID > 0){
						order = new MOrder(getCtx(), Ord_ID, get_TrxName());
						order.processIt(MOrder.DOCACTION_Void);
						order.saveEx();
						
						
						StringBuilder SQLGetPay = new StringBuilder();
						SQLGetPay.append("SELECT C_Payment_ID ");
						SQLGetPay.append(" FROM C_Payment ");
						SQLGetPay.append(" WHERE AD_Client_ID = "+ AD_Client_ID);
						SQLGetPay.append(" AND C_Invoice_ID IN (");
						SQLGetPay.append(" 	SELECT C_Invoice_ID ");
						SQLGetPay.append(" 	FROM C_Invoice ");
						SQLGetPay.append(" 	WHERE AD_Client_ID = "+AD_Client_ID);
						SQLGetPay.append(" 	AND C_Order_ID = "+Ord_ID);
						SQLGetPay.append(" 	)");
						
						Pay_ID = DB.getSQLValueEx(null, SQLGetPay.toString());
						MPayment payment = null;
						if(Pay_ID > 0 ){
							payment = new MPayment(getCtx(), Pay_ID, get_TrxName());
							
						}
						
						if(order.getDocStatus().equals(MOrder.DOCSTATUS_Voided)){
							
							if(dataHeader.Type.equals(EDIT)){
								
								MOrder ord = new MOrder(getCtx(), C_Order_ID, null);
								rslt = createOrder(ord, payment, dataDetails,dataHeader.POReference);
								
								//Integer C_OrderNew_ID = ordNew.getC_Order_ID();
								//OrderNew = new MOrder(getCtx(), C_OrderNew_ID, get_TrxName());
							
								
							}else if(dataHeader.Type.equals(FULL)){
								rslt = "Pembatalan Penjualan Berhasil";
								
							}
								
						
						}else {
							
							rslt = "ERROR";
							
						}	
						

					}
					
				}		
					
		}
					
		} catch (Exception e) {
			System.out.println(e);
			rslt = ERROR;
		}
		
		return rslt;

	} 
		
	private String generatePengembalianKeAgen(int AD_Client_ID,int AD_Org_ID,SMR_Model_PengembalianKeAgen dataHeader,SMR_Model_PengembalianKeAgen_Detail[] dataDetail)
	{

		String rslt = "";
		int C_Order_ID = dataHeader.C_Order_ID;
		MInOut MReceipt = null;
		MOrder POrder = null; 
		MInvoice APInvoice = null;
		Boolean IsPaid = false;
		Boolean IsNotPaidFull = false;
		
		
		if(C_Order_ID > 0){
			
			Integer M_InOut_ID = DataSetupValidation.getID(AD_Client_ID, MInOut.Table_Name, MInOut.COLUMNNAME_C_Order_ID, C_Order_ID, null);
			Integer C_Invoice_ID = DataSetupValidation.getID(AD_Client_ID, MInvoice.Table_Name, MInvoice.COLUMNNAME_C_Order_ID, C_Order_ID, null);

			MReceipt = new MInOut(getCtx(), M_InOut_ID, get_TrxName());
			POrder = new MOrder(getCtx(), C_Order_ID, get_TrxName());
			APInvoice = new MInvoice(getCtx(), C_Invoice_ID, get_TrxName());
			IsPaid = APInvoice.isPaid();
			BigDecimal paymentLeft = DataSetupValidation.getPaymentLeft(AD_Client_ID, C_Order_ID);
			
			if(paymentLeft.equals(APInvoice.getGrandTotal())){
				
				IsNotPaidFull = true;
			}
			
		}
		
	
		try {
			
			MRMA rma = null;
			MInOut ret = null;
			MInvoice APCreditMemo = null;
			MPayment pay = null;
			MAllocationHdr Alloc = null;
			
	
			if(MReceipt != null){
							
				StringBuilder SQLDocType = new StringBuilder();
				SQLDocType.append("SELECT C_DocType_ID, Name ");
				SQLDocType.append("FROM C_DocType ");
				SQLDocType.append("WHERE DocBaseType = 'POO' ");
				SQLDocType.append(" AND docSubtypeSO = 'RM' ");
				SQLDocType.append(" AND IsSOTrx = 'N' ");
				SQLDocType.append(" AND IsActive = 'Y' ");
				SQLDocType.append(" AND AD_Client_ID =  " + AD_Client_ID);
				
				Integer C_DocType_ID = DB.getSQLValueEx(get_TrxName(), SQLDocType.toString());
			
				rma = new MRMA (getCtx(), 0, null);
	
				//rma.setClientOrg(AD_Client_ID, MReceipt.getAD_Org_ID());
				rma.setAD_Org_ID(MReceipt.getAD_Org_ID());
				rma.set_ValueNoCheck ("DocumentNo", null);
				rma.setName("Pengembalian "+ POrder.getDocumentNo());
				rma.setDocStatus (MRMA.DOCSTATUS_Drafted);
				rma.setDocAction(MRMA.DOCACTION_Complete);
				rma.setC_DocType_ID (C_DocType_ID);
				rma.setC_BPartner_ID(MReceipt.getC_BPartner_ID());
				rma.setIsSOTrx(false);
				rma.setIsApproved (false);
				rma.setProcessed (false);
				rma.setProcessing(false);
				//rma.set_ValueNoCheck ("M_WareHouse_ID", MReceipt.getM_Warehouse_ID());
				rma.setDescription(dataHeader.Description);
				rma.setSalesRep_ID(POrder.getSalesRep_ID());
				rma.setM_RMAType_ID(DataSetupValidation.getID(AD_Client_ID, "M_RMAType", "IsActive", null, "Y"));
				rma.setC_Order_ID(0);
				//	get Order/Shipment/Receipt link
				if (MReceipt.getC_Order_ID() != 0)
				{
					rma.setC_Order_ID(MReceipt.getC_Order_ID());
				}
				if (MReceipt.getM_InOut_ID() != 0)
				{
					rma.setM_InOut_ID(MReceipt.getM_InOut_ID());
				}
				rma.saveEx();
				
				if(rma != null){
				
					
					MInOutLine[] lines = MReceipt.getLines();
					
					for(MInOutLine line : lines){
					
						int MReceiptProd_ID = line.getM_Product_ID();
						int C_OrderLine_ID = line.getC_OrderLine_ID();
						MOrderLine ordLine = new MOrderLine(getCtx(), C_OrderLine_ID, get_TrxName());
						
						for(SMR_Model_PengembalianKeAgen_Detail detail : dataDetail){
							
							if(MReceiptProd_ID == detail.M_Product_ID){
							
								MRMALine RMALine = new MRMALine(getCtx(), 0, null);
								//RMALine.setClientOrg(AD_Client_ID, rma.getAD_Org_ID());
								RMALine.setAD_Org_ID(rma.getAD_Org_ID());
								RMALine.setM_RMA_ID(rma.getM_RMA_ID());
								RMALine.setDescription(detail.Description);
								RMALine.setM_InOutLine_ID(line.getM_InOutLine_ID());
								RMALine.setM_Product_ID(detail.M_Product_ID);
								RMALine.setQty(detail.QtyReturn);
								if(C_OrderLine_ID > 0){
									RMALine.setC_Tax_ID(ordLine.getC_Tax_ID());
								}
								RMALine.saveEx();
							}
							
						}
					}
					
					rma.processIt(MRMA.DOCACTION_Complete);
					rma.saveEx();
					
					
					ret = new MInOut(getCtx(), 0, null);
					StringBuilder SQLDocTypeReturn = new StringBuilder();
					SQLDocTypeReturn.append("SELECT C_DocType_ID ");
					SQLDocTypeReturn.append("FROM  C_DocType ");
					SQLDocTypeReturn.append("WHERE AD_Client_ID = ?");
					SQLDocTypeReturn.append("AND DocBaseType = '" + MDocType.DOCBASETYPE_MaterialDelivery+ "' ");
					SQLDocTypeReturn.append("AND IsSoTrx ='N' ");

					int C_DocTypeRet_ID = DB.getSQLValueEx(get_TrxName(), SQLDocTypeReturn.toString(), AD_Client_ID);
						
					//ret.set_TrxName(trxName);
					//ret.setClientOrg(MReceipt.getAD_Client_ID(), MReceipt.getAD_Org_ID());
					
					Timestamp dateReturn = DataSetupValidation.convertStringToTimeStamp(dataHeader.DateReturn);

					ret.setAD_Org_ID(MReceipt.getAD_Org_ID());
					ret.setDocStatus (MInOut.DOCSTATUS_Drafted);		//	Draft
					ret.setDocAction(MInOut.DOCACTION_Complete);
					ret.setC_DocType_ID (C_DocTypeRet_ID);
					ret.setIsSOTrx(false);
					ret.setM_Warehouse_ID(MReceipt.getM_Warehouse_ID());
					ret.setMovementType(MInOut.MOVEMENTTYPE_VendorReturns);
					ret.setDateOrdered (MReceipt.getDateOrdered());
					ret.setDateAcct(dateReturn);
					ret.setMovementDate(dateReturn);
					ret.setC_BPartner_ID(rma.getC_BPartner_ID());
					ret.setC_BPartner_Location_ID(MReceipt.getC_BPartner_Location_ID());
					ret.setM_RMA_ID(rma.getM_RMA_ID());
					ret.setPOReference(MReceipt.getPOReference()); 
					ret.saveEx();
					
					if(ret != null){
						
						
						MRMALine[] RMALines = rma.getLines(true);
						for(MRMALine line : RMALines){
							MInOutLine receipLine = new MInOutLine(getCtx(), line.getM_InOutLine_ID(), null);
							MInOutLine inLine = new MInOutLine(getCtx(), 0, null);
						//	inLine.setClientOrg(MReceipt.getAD_Client_ID(),MReceipt.getAD_Org_ID());
							inLine.setAD_Org_ID(MReceipt.getAD_Org_ID());
							inLine.setM_InOut_ID(ret.getM_InOut_ID());
							inLine.setM_RMALine_ID(line.getM_RMALine_ID());
							inLine.setDescription(line.getDescription());
							inLine.setQtyEntered(line.getQty());
							inLine.setMovementQty(line.getQty());
							inLine.setC_UOM_ID(receipLine.getC_UOM_ID());
							inLine.setM_Product_ID(line.getM_Product_ID());
							if(receipLine.getM_AttributeSetInstance_ID() > 0){
								inLine.setM_AttributeSetInstance_ID(receipLine.getM_AttributeSetInstance_ID());
							}
							inLine.setM_Locator_ID(receipLine.getM_Locator_ID());
							
							inLine.saveEx();
						}
						
					}
					
					ret.processIt(MInOut.DOCACTION_Complete);
					ret.saveEx();
					
					APCreditMemo = new MInvoice(getCtx(), 0, null);
					
					StringBuilder SQLDocTypeAPC = new StringBuilder();
					SQLDocTypeAPC.append("SELECT C_DocType_ID ");
					SQLDocTypeAPC.append("FROM  C_DocType ");
					SQLDocTypeAPC.append("WHERE AD_Client_ID = ?");
					SQLDocTypeAPC.append("AND DocBaseType = '" + MDocType.DOCBASETYPE_APCreditMemo+ "' ");
					SQLDocTypeAPC.append("AND IsSoTrx ='N' ");
					
					int C_DocTypeAPC_ID = DB.getSQLValueEx(get_TrxName(), SQLDocTypeAPC.toString(), AD_Client_ID);
		
					APCreditMemo.setClientOrg(MReceipt.getAD_Client_ID(), MReceipt.getAD_Org_ID());
					APCreditMemo.setDocStatus(MInvoice.DOCSTATUS_Drafted);		//	Draft
					APCreditMemo.setDocAction(MInvoice.DOCACTION_Complete);
					APCreditMemo.setM_PriceList_ID(POrder.getM_PriceList_ID());
					APCreditMemo.setPaymentRule(POrder.getPaymentRule());
					APCreditMemo.setC_DocTypeTarget_ID (C_DocTypeAPC_ID);
					APCreditMemo.setIsSOTrx(false);
					APCreditMemo.setDateInvoiced (ret.getDateAcct());
					APCreditMemo.setDateAcct (ret.getDateAcct());
					APCreditMemo.setC_Order_ID(0);
					APCreditMemo.setC_BPartner_ID(ret.getC_BPartner_ID());
					APCreditMemo.setM_RMA_ID(ret.getM_RMA_ID());
					
					MPriceList plist = new MPriceList(getCtx(), POrder.getM_PriceList_ID(), null);
						APCreditMemo.setIsTaxIncluded(plist.isTaxIncluded());
					
					
					if (ret.getC_Order_ID() != 0)
						{
							MOrder peer = new MOrder (ret.getCtx(), ret.getC_Order_ID(), ret.get_TrxName());
							if (peer.getRef_Order_ID() != 0)
								APCreditMemo.setC_Order_ID(peer.getRef_Order_ID());
						}
					if (ret.getM_RMA_ID() != 0)
						{
							MRMA peer = new MRMA (ret.getCtx(), ret.getM_RMA_ID(), ret.get_TrxName());
							if (peer.getM_RMA_ID() > 0)
								APCreditMemo.setM_RMA_ID(peer.getM_RMA_ID());
						}
					APCreditMemo.saveEx();
					
					MInOutLine[] retLines = ret.getLines();
					for(MInOutLine line : retLines){
						
						MInvoiceLine APCrMemoLine = new MInvoiceLine(getCtx(), 0, null);
						MRMALine rmaLine = new MRMALine(getCtx(), line.getM_RMALine_ID(), null);
						MInOutLine receiptLine = new MInOutLine(getCtx(), rmaLine.getM_InOutLine_ID(), null);
						MOrderLine ordLine = new MOrderLine(getCtx(), receiptLine.getC_OrderLine_ID(),null);
						//APCrMemoLine.setClientOrg(line.getAD_Client_ID(), line.getAD_Org_ID());
						APCrMemoLine.setAD_Org_ID(line.getAD_Org_ID());
						APCrMemoLine.setC_Invoice_ID(APCreditMemo.getC_Invoice_ID());
						APCrMemoLine.setC_OrderLine_ID(line.getC_OrderLine_ID());
						APCrMemoLine.setM_InOutLine_ID(line.getM_InOutLine_ID());
						APCrMemoLine.setM_Product_ID(line.getM_Product_ID());
						APCrMemoLine.setDescription(line.getDescription());
						APCrMemoLine.setQtyEntered(line.getQtyEntered());
						APCrMemoLine.setQtyInvoiced(line.getQtyEntered());
						APCrMemoLine.setC_UOM_ID(line.getC_UOM_ID());
						APCrMemoLine.setPriceEntered(ordLine.getPriceEntered());
						APCrMemoLine.setPriceActual(ordLine.getPriceEntered());
						APCrMemoLine.setPriceList(ordLine.getPriceList());
						APCrMemoLine.setLineNetAmt();
						if(line.getM_AttributeSetInstance_ID() > 0){
							APCrMemoLine.setM_AttributeSetInstance_ID(line.getM_AttributeSetInstance_ID());
						}
						APCrMemoLine.setC_Tax_ID(ordLine.getC_Tax_ID());
						APCrMemoLine.saveEx();
					}
					
					APCreditMemo.processIt(MInvoice.DOCACTION_Complete);
					APCreditMemo.saveEx();
					
					if(!IsNotPaidFull || IsPaid){
						pay = new MPayment(getCtx(), 0, get_TrxName());
						//pay.setClientOrg(AD_Client_ID,AD_Org_ID);
						pay.setAD_Org_ID(AD_Org_ID);
						Integer docTypePayment_ID = DataSetupValidation.getChargeDocType(AD_Client_ID,false,true,false);
						pay.setIsReceipt(false);
						pay.setC_DocType_ID(docTypePayment_ID);
						pay.setC_BPartner_ID(APCreditMemo.getC_BPartner_ID());
						pay.setDescription(APCreditMemo.getDescription());
						pay.setDateTrx(APCreditMemo.getDateInvoiced());
						pay.setDateAcct(APCreditMemo.getDateInvoiced());
						
						Integer OldPay = DataSetupValidation.getID(AD_Client_ID, MPayment.Table_Name, MPayment.COLUMNNAME_C_Invoice_ID, APInvoice.getC_Invoice_ID(), null);
						if(OldPay > 0){
							MPayment oldPayment = new MPayment(getCtx(), OldPay, get_TrxName());
							pay.setC_BankAccount_ID(oldPayment.getC_BankAccount_ID());
						}
						
						pay.setTenderType(MPayment.TENDERTYPE_Cash);
						pay.setPayAmt(APCreditMemo.getGrandTotal().negate());
						pay.setC_Currency_ID(APCreditMemo.getC_Currency_ID());
						pay.setC_Invoice_ID(APCreditMemo.getC_Invoice_ID());				
						pay.saveEx();	
						
						if(IsPaid){
							pay.processIt(MPayment.DOCACTION_Complete);
							pay.saveEx();
						}
					}
					
					
					
					if(IsNotPaidFull){
						
						Alloc = new MAllocationHdr(getCtx(), 0, get_TrxName());
						Integer AllocDocType = DataSetupValidation.getDocType_ID(AD_Client_ID, MDocType.DOCBASETYPE_PaymentAllocation, "N", "");
						Alloc.setC_DocType_ID(AllocDocType);
						Alloc.setAD_Org_ID(APInvoice.getAD_Org_ID());
						Alloc.setDescription("AUTO ALLOCATION "+ ("AP INVOICE:"+APInvoice.getDocumentNo()+" & AP CREDIT MEMO:"+APCreditMemo.getDocumentNo()) );
						Alloc.setDateTrx(APCreditMemo.getDateInvoiced());
						Alloc.setDateAcct(APCreditMemo.getDateInvoiced());
						Alloc.setC_Currency_ID(APCreditMemo.getC_Currency_ID());
						Alloc.saveEx();
						
						ArrayList<Integer> invMap = new ArrayList<Integer>();
						
						invMap.add(APInvoice.getC_Invoice_ID());
						invMap.add(APCreditMemo.getC_Invoice_ID());
						
						for(int i = 0 ; i < invMap.size() ; i ++){
							MInvoice inv = new MInvoice(getCtx(), invMap.get(i), get_TrxName());
							MDocType doc = new MDocType(getCtx(), inv.getC_DocType_ID(), get_TrxName());

							MAllocationLine AllocLine = new MAllocationLine(getCtx(), 0, get_TrxName());
							AllocLine.setAD_Org_ID(Alloc.getAD_Org_ID());
							AllocLine.setC_AllocationHdr_ID(Alloc.getC_AllocationHdr_ID());
							AllocLine.setC_BPartner_ID(APCreditMemo.getC_BPartner_ID());
							AllocLine.setAmount(APInvoice.getGrandTotal().negate());
							if(doc.getDocBaseType().equals(MDocType.DOCBASETYPE_APInvoice)){
								AllocLine.setC_Order_ID(APInvoice.getC_Order_ID());	
							}
							AllocLine.setC_Invoice_ID(inv.getC_Invoice_ID());
							AllocLine.saveEx();
							
							
						}

						Alloc.processIt(MAllocationHdr.ACTION_Complete);
						Alloc.saveEx();
						
					}
			
					
				}	
			
			}
			
			if(!IsNotPaidFull || IsPaid){
				
				if(pay != null){
					rslt = "Pengembalian Barang Ke Agen Berhasil";
				}else{
					rslt = ERROR;
				}
				
				
			}else if(IsNotPaidFull){
				
				if(Alloc != null){
					rslt = "Pengembalian Barang Ke Agen Berhasil";
				}else{
					rslt = ERROR;
				}
				
			}else{
				rslt = ERROR;
			}
		
		} catch (Exception e) {
			rslt = ERROR;
		}
		
		return rslt;

	} 
	
	
	private String generatePenerimaanBarang(int AD_Client_ID,int AD_Org_ID,SMR_Model_PenerimaanBarang dataHeader,SMR_Model_PenerimaanBarang_Detail[] dataDetails)
	{

		String rslt = "";
		MOrder POrder = null;
		MInOut MReceipt = null;
		MInvoice APInvoice = null;
		MPayment APPayment = null;
		HashMap<Integer, Integer> Locator_ID = new HashMap<Integer, Integer>();
		//HashMap<Integer, Integer> IMEI_ID = new HashMap<Integer, Integer>();
		

		
		try {
			Timestamp DateReceip = DataSetupValidation.convertStringToTimeStamp(dataHeader.DateReceipt);

		
			POrder = new MOrder(getCtx(), 0, get_TrxName());
			POrder.setClientOrg(AD_Client_ID, AD_Org_ID);
			POrder.setIsSOTrx(false);
			
			StringBuilder SQLDocTypeAPC = new StringBuilder();
			SQLDocTypeAPC.append("SELECT C_DocType_ID ");
			SQLDocTypeAPC.append("FROM  C_DocType ");
			SQLDocTypeAPC.append("WHERE AD_Client_ID = ?");
			SQLDocTypeAPC.append("AND DocBaseType = '" + MDocType.DOCBASETYPE_PurchaseOrder+ "' ");
			SQLDocTypeAPC.append("AND IsSoTrx ='N' ");
			
			int C_DocTypePO_ID = DB.getSQLValueEx(get_TrxName(), SQLDocTypeAPC.toString(), AD_Client_ID);

			POrder.setC_DocTypeTarget_ID(C_DocTypePO_ID);
			POrder.setC_DocType_ID(C_DocTypePO_ID);
			
			POrder.setDateOrdered(DateReceip);
			POrder.setDatePromised(DateReceip);
			POrder.setC_BPartner_ID(dataHeader.C_BPartner_ID);
			
			Integer C_Bpartner_Location = DataSetupValidation.getID(AD_Client_ID, MBPartnerLocation.Table_Name, MBPartnerLocation.COLUMNNAME_C_BPartner_ID, dataHeader.C_BPartner_ID, null);		
			POrder.setC_BPartner_Location_ID(C_Bpartner_Location);
			POrder.setM_Warehouse_ID(dataHeader.M_Warehouse_ID);
			POrder.setPriorityRule(MOrder.PRIORITYRULE_Medium);
			POrder.setM_PriceList_ID(dataHeader.M_PriceList_ID);
			MPriceList pList = new MPriceList(getCtx(), dataHeader.M_PriceList_ID, get_TrxName());
			POrder.setC_Currency_ID(dataHeader.C_Currency_ID);
			POrder.setPaymentRule(MOrder.PAYMENTRULE_MixedPOSPayment);			
			StringBuilder SQLGetPayterm = new StringBuilder();
			SQLGetPayterm.append("SELECT C_PaymentTerm_ID");
			SQLGetPayterm.append(" FROM C_PaymentTerm ");
			SQLGetPayterm.append(" WHERE AD_Client_ID = " + AD_Client_ID);
			SQLGetPayterm.append(" AND IsActive = 'Y' ");
			SQLGetPayterm.append(" AND IsDefault = 'Y' ");
			
			Integer C_PaymentTerm_ID = DB.getSQLValueEx(get_TrxName(), SQLGetPayterm.toString());

			POrder.setC_PaymentTerm_ID(C_PaymentTerm_ID);
			POrder.setDescription(dataHeader.Description);
			POrder.setDeliveryRule(MOrder.DELIVERYRULE_AfterReceipt);
			POrder.setDeliveryViaRule(MOrder.DELIVERYVIARULE_Pickup);
			POrder.setSalesRep_ID(dataHeader.SalesRep_ID);
			POrder.setIsTaxIncluded(pList.isTaxIncluded());
			POrder.setDocAction(MOrder.DOCACTION_Complete);
			POrder.saveEx();
			
			
			if(POrder != null){
				
				//Generate OrderLine
				
				for(SMR_Model_PenerimaanBarang_Detail dataDetail : dataDetails){
				
					MOrderLine ordLine = new MOrderLine(getCtx(), 0, get_TrxName());
				//	ordLine.setClientOrg(AD_Client_ID, AD_Org_ID);
					ordLine.setAD_Org_ID(AD_Org_ID);
					ordLine.setC_BPartner_ID(POrder.getC_BPartner_ID());
					ordLine.setC_BPartner_Location_ID(POrder.getC_BPartner_Location_ID());
					ordLine.setC_Order_ID(POrder.getC_Order_ID());
					ordLine.setM_Product_ID(dataDetail.M_Product_ID);
					
					MProduct product = new MProduct(getCtx(), dataDetail.M_Product_ID, get_TrxName());
					
					ordLine.setC_UOM_ID(product.getC_UOM_ID());
					ordLine.setQtyEntered(dataDetail.QtyEntered);
					ordLine.setQtyOrdered(dataDetail.QtyEntered);
					ordLine.setQtyDelivered(dataDetail.QtyEntered);
					//ordLine.setQtyReserved(ordLine.getQtyOrdered().subtract(ordLine.getQtyDelivered()));
					ordLine.setQtyInvoiced(dataDetail.QtyEntered);
					
					StringBuilder SQLGetTax = new StringBuilder();
					
					SQLGetTax.append("SELECT C_Tax_ID");
					SQLGetTax.append(" FROM C_Tax ");
					SQLGetTax.append(" WHERE AD_Client_ID = " +AD_Client_ID);
					SQLGetTax.append(" AND C_TaxCategory_ID = "+product.getC_TaxCategory_ID());

					int C_Tax_ID = DB.getSQLValueEx(get_TrxName(), SQLGetTax.toString());
					ordLine.setC_Tax_ID(C_Tax_ID);
					
					MProductPricing pp = new MProductPricing(dataDetail.M_Product_ID, dataHeader.C_BPartner_ID,Env.ONE, false,get_TrxName());
					
    	    		Timestamp date = new Timestamp(System.currentTimeMillis());

    	    		String sql = "SELECT plv.M_PriceList_Version_ID "
    	    				+ "FROM M_PriceList_Version plv "
    	    				+ "WHERE plv.AD_Client_ID = ? " + " AND plv.M_PriceList_ID= ? " // 1
    	    				+ " AND plv.ValidFrom <= ? " + "ORDER BY plv.ValidFrom DESC";

    	    		int M_PriceList_Version_ID = DB.getSQLValueEx(get_TrxName(), sql, new Object[] {AD_Client_ID, dataHeader.M_PriceList_ID, date });

    	    		pp.setM_PriceList_Version_ID(M_PriceList_Version_ID);
    	    		pp.setPriceDate(date);

					
					ordLine.setPrice(dataDetail.PriceEntered);
					ordLine.setPriceActual(dataDetail.PriceEntered);
					ordLine.setPriceEntered(dataDetail.PriceEntered);
					ordLine.setPriceList(pp.getPriceList());
					ordLine.setPriceLimit(pp.getPriceLimit());
					ordLine.setLineNetAmt(dataDetail.LineNetAmt);
					//ordLine.setDescription(dataDetail.Description);
//					if(dataDetail.M_AttributeSetInstance_ID > 0){
//						ordLine.setM_AttributeSetInstance_ID(dataDetail.M_AttributeSetInstance_ID);
//					}
					ordLine.saveEx();
					
					Locator_ID.put(ordLine.getC_OrderLine_ID(), dataDetail.M_Locator_ID);
					//IMEI_ID.put(ordLine.getC_OrderLine_ID(), dataDetail.M_AttributeSetInstance_ID);

				}					
				
				POrder.processIt(MOrder.DOCACTION_Complete);
				POrder.saveEx();		
				
				//Create Material Receipt
				MReceipt = new MInOut(getCtx(), 0, get_TrxName());
			//	MReceipt.setClientOrg(POrder.getAD_Client_ID(), POrder.getAD_Org_ID());
				MReceipt.setAD_Org_ID(POrder.getAD_Org_ID());
				StringBuilder SQLDocTypeMR = new StringBuilder();
				SQLDocTypeMR.append("SELECT C_DocType_ID ");
				SQLDocTypeMR.append("FROM  C_DocType ");
				SQLDocTypeMR.append("WHERE AD_Client_ID = ?");
				SQLDocTypeMR.append("AND DocBaseType = '" + MDocType.DOCBASETYPE_MaterialReceipt+ "' ");
				SQLDocTypeMR.append("AND IsSoTrx ='N' ");
				
				int C_DocTypeMR_ID = DB.getSQLValueEx(get_TrxName(), SQLDocTypeMR.toString(), AD_Client_ID);
				
				MReceipt.setC_DocType_ID(C_DocTypeMR_ID);
				MReceipt.setPOReference(POrder.getDocumentNo());
				MReceipt.setC_Order_ID(POrder.getC_Order_ID());
				MReceipt.setDateAcct(DateReceip);
				MReceipt.setMovementDate(DateReceip);
				MReceipt.setC_BPartner_ID(POrder.getC_BPartner_ID());
				MReceipt.setC_BPartner_Location_ID(POrder.getC_BPartner_Location_ID());
				MReceipt.setM_Warehouse_ID(POrder.getM_Warehouse_ID());
				MReceipt.setMovementType(MInOut.MOVEMENTTYPE_VendorReceipts);
				MReceipt.setIsSOTrx(false);
				//MReceipt.set_CustomColumn("C_Decoris_PostingMethod_ID", null);
				MReceipt.saveEx();
				
				//Create Receipt Line
				if(MReceipt != null){
					
					MOrderLine[] ordLines = POrder.getLines();
					
					for (MOrderLine ordLine : ordLines){
						
						MInOutLine MRLine = new MInOutLine(getCtx(), 0, get_TrxName());
						//MRLine.setClientOrg(MReceipt.getAD_Client_ID(), MReceipt.getAD_Org_ID());
						MRLine.setAD_Org_ID(MReceipt.getAD_Org_ID());
						MRLine.setM_InOut_ID(MReceipt.getM_InOut_ID());
						MRLine.setM_Product_ID(ordLine.getM_Product_ID());
						MRLine.setM_Locator_ID(Locator_ID.get(ordLine.getC_OrderLine_ID()));
						//MRLine.setM_AttributeSetInstance_ID(IMEI_ID.get(ordLine.getC_Order_ID()));
						MRLine.setDescription(ordLine.getDescription());
						MRLine.setQtyEntered(ordLine.getQtyOrdered());
						MRLine.setQty(ordLine.getQtyOrdered());
						MRLine.setC_UOM_ID(ordLine.getC_UOM_ID());
						MRLine.setC_OrderLine_ID(ordLine.getC_OrderLine_ID());
						MRLine.setLine(ordLine.getLine());
						MRLine.saveEx();
						
					}
					
					MReceipt.processIt(MInOut.DOCACTION_Complete);
					MReceipt.saveEx();
					
				}
				
				//Create AP Invoice
				APInvoice = new MInvoice(MReceipt.getCtx(), 0, MReceipt.get_TrxName());
				APInvoice.setClientOrg(MReceipt.getAD_Client_ID(), MReceipt.getAD_Org_ID());
				
				
				StringBuilder SQLDocTypeAPInv = new StringBuilder();
				SQLDocTypeAPInv.append("SELECT C_DocType_ID ");
				SQLDocTypeAPInv.append("FROM  C_DocType ");
				SQLDocTypeAPInv.append("WHERE AD_Client_ID = ?");
				SQLDocTypeAPInv.append("AND DocBaseType = '" + MDocType.DOCBASETYPE_APInvoice+ "' ");
				SQLDocTypeAPInv.append("AND IsSoTrx ='N' ");
				
				int C_DocTypeApInv_ID = DB.getSQLValueEx(get_TrxName(), SQLDocTypeAPInv.toString(), AD_Client_ID);
				
				APInvoice.setC_DocType_ID(C_DocTypeApInv_ID);
				APInvoice.setC_DocTypeTarget_ID(C_DocTypeApInv_ID);
				APInvoice.setDateInvoiced(MReceipt.getMovementDate());
				APInvoice.setDateAcct(MReceipt.getDateAcct());
				APInvoice.setC_BPartner_ID(MReceipt.getC_BPartner_ID());
				APInvoice.setC_BPartner_Location_ID(MReceipt.getC_BPartner_Location_ID());
				APInvoice.setM_PriceList_ID(POrder.getM_PriceList_ID());
				APInvoice.setC_Currency_ID(MReceipt.getC_Currency_ID());
				APInvoice.setPaymentRule(POrder.getPaymentRule());
				APInvoice.setC_PaymentTerm_ID(POrder.getC_PaymentTerm_ID());
				APInvoice.setDocAction(MInOut.DOCACTION_Complete);
				APInvoice.setC_Order_ID(POrder.getC_Order_ID());
				APInvoice.setIsSOTrx(false);
				APInvoice.setIsTaxIncluded(POrder.isTaxIncluded());
				APInvoice.setDescription(POrder.getDescription());
				APInvoice.saveEx();
				
				MReceipt.setC_Invoice_ID(APInvoice.getC_Invoice_ID());
				MReceipt.processIt(MInOut.ACTION_Post);
				MReceipt.saveEx();
				
				if(APInvoice != null){
					
					//Generate InvoceLine
					
					MInOutLine[] MRLines = MReceipt.getLines();
					
					for(MInOutLine MRLine : MRLines){
						MInvoiceLine invLine = new MInvoiceLine(MRLine.getCtx(), 0, MRLine.get_TrxName());
						MOrderLine OrdLine = new MOrderLine(getCtx(), MRLine.getC_OrderLine_ID(), MRLine.get_TrxName());
						
						//invLine.setClientOrg(APInvoice.getAD_Client_ID(), APInvoice.getAD_Org_ID());
						invLine.setAD_Org_ID(APInvoice.getAD_Org_ID());
						invLine.setC_Invoice_ID(APInvoice.getC_Invoice_ID());
						invLine.setC_OrderLine_ID(MRLine.getC_OrderLine_ID());
						invLine.setM_InOutLine_ID(MRLine.getM_InOutLine_ID());
						invLine.setLine(MRLine.getLine());
						invLine.setM_Product_ID(MRLine.getM_Product_ID());
						invLine.setQty(MRLine.getQtyEntered());
						invLine.setPrice(OrdLine.getPriceActual());
						invLine.setPriceList(OrdLine.getPriceList());
						invLine.setPriceEntered(OrdLine.getPriceEntered());
						invLine.setPriceLimit(OrdLine.getPriceLimit());
						invLine.setC_Tax_ID(OrdLine.getC_Tax_ID());
						invLine.setTaxAmt();
						invLine.setLineNetAmt();
						invLine.setDescription(OrdLine.getDescription());
						invLine.saveEx();
					}
					
					
					APInvoice.processIt(MInvoice.ACTION_Complete);
					APInvoice.saveEx();
					
				}
				
				if(dataHeader.IsCash.equals("Y")){
				
					//Generate AP Payment
					APPayment = new MPayment(APInvoice.getCtx(), 0, APInvoice.get_TrxName());
						
					//APPayment.setClientOrg(APInvoice.getAD_Client_ID(), APInvoice.getAD_Org_ID());
					APPayment.setAD_Org_ID(APInvoice.getAD_Org_ID());
					APPayment.setIsReceipt(false);
					
					StringBuilder SQLDocTypeAPP = new StringBuilder();
					SQLDocTypeAPP.append("SELECT C_DocType_ID ");
					SQLDocTypeAPP.append(" FROM  C_DocType ");
					SQLDocTypeAPP.append(" WHERE AD_Client_ID = " + AD_Client_ID);
					SQLDocTypeAPP.append(" AND DocBaseType = '" + MDocType.DOCBASETYPE_APPayment+ "' ");
					SQLDocTypeAPP.append(" AND IsSOTrx ='N' ");
					
					System.out.println(SQLDocTypeAPP);
					int C_DocTypeAPP_ID = DB.getSQLValueEx(get_TrxName(), SQLDocTypeAPP.toString());
					
					APPayment.setC_DocType_ID(C_DocTypeAPP_ID);
					APPayment.setC_BankAccount_ID(dataHeader.C_BankAccount_ID);
					APPayment.setDateTrx(APInvoice.getDateInvoiced());
					APPayment.setDateAcct(APInvoice.getDateAcct());
					APPayment.setC_Invoice_ID(APInvoice.getC_Invoice_ID());
					APPayment.setC_BPartner_ID(APInvoice.getC_BPartner_ID());
					APPayment.setPayAmt(APInvoice.getGrandTotal());
					APPayment.setC_Currency_ID(POrder.getC_Currency_ID());
					APPayment.setTenderType(MPayment.TENDERTYPE_Cash);
					APPayment.setDocAction(MPayment.DOCACTION_Complete);
					APPayment.setIsPrepayment(false);					
					APPayment.setDescription(APInvoice.getDescription());
					APPayment.saveEx();
					
					if(APPayment != null){
						APPayment.processIt(MPayment.ACTION_Complete);
						APPayment.saveEx();
					}
					
					if(APPayment.isAllocated()){
						APInvoice.setC_Payment_ID(APPayment.getC_Payment_ID());
						APInvoice.setIsPaid(true);
						APInvoice.saveEx();
					}	
				}
			}
			
			if(dataHeader.IsCash.equals("Y")){			
				if(POrder != null && MReceipt != null && APInvoice != null && APPayment != null){
					rslt = "Proses Penerimaan Barang Berhasil Terinput";	
				}else{
					rslt = "ERROR";
				}
			}else if(dataHeader.IsCash.equals("N")){
				if(POrder != null && MReceipt != null && APInvoice != null){
					rslt = "Proses Penerimaan Barang Berhasil Terinput";	
				}else{
					rslt = "ERROR";
				}
			}
			
		} catch (Exception e) {
			rslt = ERROR;
		}
		
		return rslt;

	}
	
private String createOrder(MOrder ordFrom, MPayment payment,SMR_Model_Pembatalan_Detail[] details,String POReference){
		
		String rs = null;
		
		MOrder ordTo = new MOrder(Env.getCtx(), 0, get_TrxName());
		
		
		if(POReference == null) {
			POReference = "";
		}
		
		ordTo.setAD_Org_ID(ordFrom.getAD_Org_ID());
		ordTo.setC_BPartner_ID(ordFrom.getC_BPartner_ID());
		ordTo.setDescription("Edit Penjualan :" + ordFrom.getDocumentNo());
		ordTo.setDeliveryRule(ordFrom.getDeliveryRule());
		ordTo.setDeliveryViaRule(ordFrom.getDeliveryViaRule());	
		ordTo.setSalesRep_ID(ordFrom.getSalesRep_ID());
		ordTo.setC_Currency_ID(ordFrom.getC_Currency_ID());
		ordTo.setC_DocType_ID(ordFrom.getC_DocType_ID());
		ordTo.setC_DocTypeTarget_ID(ordFrom.getC_DocTypeTarget_ID());
		ordTo.setDateOrdered(ordFrom.getDateOrdered());
		ordTo.setDateAcct(ordFrom.getDateAcct());
	    ordTo.setDatePromised(ordFrom.getDatePromised());
	    ordTo.setM_Warehouse_ID(ordFrom.getM_Warehouse_ID());	    
	    ordTo.setM_PriceList_ID(ordFrom.getM_PriceList_ID());
	    //ordTo.setTotalLines(ordFrom.getTotalLines());
	    //ordTo.setGrandTotal(ordFrom.getGrandTotal());
	    ordTo.setPaymentRule(ordFrom.getPaymentRule());
	    ordTo.setIsSelfService(true);
	    ordTo.setIsSOTrx(ordFrom.isSOTrx());
	    ordTo.setDocAction(MOrder.DOCACTION_Complete);
	    ordTo.set_ValueNoCheck("M_Sales_Locator_ID", ordFrom.get_Value("M_Sales_Locator_ID"));			
	    ordTo.setC_PaymentTerm_ID(ordFrom.getC_PaymentTerm_ID());
	    ordTo.set_CustomColumn("M_Locator_ID",ordFrom.get_Value("M_Locator_ID"));
		Integer C_Bpartner_Location = DataSetupValidation.getID(ordFrom.getAD_Client_ID(), MBPartnerLocation.Table_Name, MBPartnerLocation.COLUMNNAME_C_BPartner_ID, ordFrom.getC_BPartner_ID(), null);
	    ordTo.setC_BPartner_Location_ID(C_Bpartner_Location);
		ordTo.setPOReference(POReference);
	    
	    ordTo.saveEx();
	    
	    
		int line = 0;
		int detailCounts = 0;
		BigDecimal grandTotal = Env.ZERO;
		for(SMR_Model_Pembatalan_Detail dataDetail : details){
			
			MOrderLine OrderLine = new MOrderLine(getCtx(), 0, get_TrxName());

			line = line+10;
			OrderLine.setLine(line);
	    	OrderLine.setAD_Org_ID(ordTo.getAD_Org_ID());
			OrderLine.setC_Order_ID(ordTo.getC_Order_ID());
			OrderLine.setM_Product_ID(dataDetail.M_Product_ID);
			OrderLine.setC_BPartner_Location_ID(ordTo.getC_BPartner_Location_ID());
			OrderLine.setM_Warehouse_ID(ordTo.getM_Warehouse_ID());
			OrderLine.setDateOrdered(ordTo.getDateOrdered());										
			MProduct prod = new MProduct(getCtx(), dataDetail.M_Product_ID, get_TrxName());
			OrderLine.setC_UOM_ID(prod.getC_UOM_ID());
			OrderLine.setQtyEntered(dataDetail.QtyOrdered);
			OrderLine.setQtyOrdered(dataDetail.QtyOrdered);
			
			System.out.println(dataDetail.QtyOrdered);
			OrderLine.setPriceList(dataDetail.PriceList);
			OrderLine.setPriceEntered(dataDetail.PriceEntered);
			OrderLine.setPriceActual(dataDetail.PriceEntered);
			
			MProduct product  = new MProduct(getCtx(), dataDetail.M_Product_ID, get_TrxName());
			//product.get
			
			StringBuilder SQLGetTax = new StringBuilder();
			
			SQLGetTax.append("SELECT C_Tax_ID");
			SQLGetTax.append(" FROM C_Tax ");
			SQLGetTax.append(" WHERE AD_Client_ID = " +ordTo.getAD_Client_ID());
			SQLGetTax.append(" AND C_TaxCategory_ID = "+product.getC_TaxCategory_ID());

			int C_Tax_ID = DB.getSQLValueEx(get_TrxName(), SQLGetTax.toString());
			
			OrderLine.setC_Tax_ID(C_Tax_ID);
			OrderLine.setLineNetAmt(dataDetail.LineNetAmt);
			System.out.println(dataDetail.LineNetAmt);
			//OrderLine.setM_AttributeSetInstance_ID(dataDetail.M_AttributeSetInstance_ID);
			OrderLine.setC_Currency_ID(ordTo.getC_Currency_ID());
			OrderLine.saveEx();
			grandTotal = grandTotal.add(dataDetail.LineNetAmt);
			detailCounts++;
			
		}
	    
		ordTo.setTotalLines(grandTotal);
		ordTo.setGrandTotal(grandTotal);
		ordTo.saveEx();
		
		if(ordTo != null && detailCounts > 0){
			
			 String whereClause = "";
			    whereClause = "Cash";
			    String SQLTender = "SELECT C_POSTenderType_ID FROM C_POSTenderType WHERE name = '"
						+ whereClause + "'";
				int C_POSTenderType_ID = DB.getSQLValueEx(getCtx().toString(),SQLTender.toString());
			    
			    DataSetupValidation.createPOSPayment(ordTo.getAD_Org_ID(), ordTo.getC_Order_ID(), C_POSTenderType_ID, MPayment.TENDERTYPE_Cash, ordTo.getGrandTotal(), "", payment.getC_BankAccount_ID(), ordTo.get_TrxName());

			
			if(ordTo.processIt(MOrder.DOCACTION_Complete)){
				ordTo.saveEx();
				System.out.println("New Order :"+ordTo.getDocStatus());
				rs = "Pembatalan Penjualan Berhasil";

			}else {
				rs = ERROR;
			}

			
		}else{
			rs = ERROR;
		}
	    	
		return rs;
		
	}
	
}
