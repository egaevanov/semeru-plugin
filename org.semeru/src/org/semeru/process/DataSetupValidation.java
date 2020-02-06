package org.semeru.process;

import java.math.BigDecimal;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.logging.Level;

import org.compiere.model.MDocType;
import org.compiere.model.MInvoice;
import org.compiere.model.X_C_POSPayment;
import org.compiere.util.CLogger;
import org.compiere.util.DB;
import org.compiere.util.Env;

public class DataSetupValidation {
	
	
	protected static CLogger	log = CLogger.getCLogger(DataSetupValidation.class);


	public static boolean IsValidDataMaster(int AD_Client_ID, int AD_Org_ID,String TableName,String ColumnName, String Value){
		
		boolean rs = true;
		
		StringBuilder SQLCheck = new StringBuilder();
		SQLCheck.append("SELECT "+TableName+"_ID");
		SQLCheck.append(" FROM "+TableName);
		SQLCheck.append(" WHERE AD_Client_ID = "+ AD_Client_ID );
		SQLCheck.append(" AND AD_Org_ID = "+ AD_Org_ID );
		SQLCheck.append(" AND "+ ColumnName + " ='"+Value+"'");

		Integer rslt = DB.getSQLValueEx(null, SQLCheck.toString());
		
		if(rslt < 0 || rslt == null){
			
			rslt = 0;
			
		}
		
		if(rslt > 0){
			rs = false;
		}
		
		return rs;
		
	}
	
	public static Integer getChargeDocType(int AD_Client_ID,boolean Invoice,boolean payment,boolean IsSoTrx){
		Integer rs = 0;
				
		StringBuilder getDocType = new StringBuilder();
		
		getDocType.append("SELECT C_DocType_ID ");
		getDocType.append(" FROM C_DocType ");
		getDocType.append(" WHERE AD_Client_ID = "+AD_Client_ID);
		
		if(Invoice&&IsSoTrx){
			getDocType.append(" AND DocBaseType = '"+MDocType.DOCBASETYPE_ARInvoice +"'");
		}else if(Invoice&&!IsSoTrx){
			getDocType.append(" AND DocBaseType = '"+MDocType.DOCBASETYPE_APInvoice +"'");
		}else if(payment&&IsSoTrx){
			getDocType.append(" AND DocBaseType = '"+MDocType.DOCBASETYPE_ARReceipt +"'");
		}else if(payment&&!IsSoTrx){
			getDocType.append(" AND DocBaseType = '"+MDocType.DOCBASETYPE_APPayment +"'");
		}
		
		rs = DB.getSQLValueEx(null, getDocType.toString());
		
//		getDocType.append("SELECT C_DocType_ID ");
//		getDocType.append(" FROM C_ChargeType_DocType ");
//		getDocType.append(" WHERE C_ChargeType_ID = "+C_ChargeType_ID);
//		getDocType.append(" AND AD_Client_ID = "+AD_Client_ID);
//	
//		if(IsInvoice){
//			getDocType.append(" AND C_DocType_ID IN (SELECT C_DocType_ID FROM C_DocType WHERE Name like '%Invoice%') ");
//		}
//		if(IsPayment){
//			getDocType.append(" AND C_DocType_ID IN (SELECT C_DocType_ID FROM C_DocType WHERE Name like '%Payment%') ");
//		}
		
		
		return rs;
		
	}
	
	
	public static BigDecimal getPaymentLeft(int AD_Client_ID, int C_Order_ID){
		
		BigDecimal rs = Env.ZERO;
		
		StringBuilder getInvoice = new StringBuilder();
		getInvoice.append("SELECT C_Invoice_ID ");
		getInvoice.append(" FROM C_Invoice ");
		getInvoice.append(" WHERE AD_Client_ID = "+ AD_Client_ID);
		getInvoice.append(" AND C_Order_ID = " + C_Order_ID);
		getInvoice.append(" AND IsPaid = '"+"N"+"'");
		
		Integer C_Invoice_ID = DB.getSQLValueEx(null, getInvoice.toString());
		MInvoice invoice = new MInvoice(Env.getCtx(), C_Invoice_ID, null);
		BigDecimal InvAmt = invoice.getGrandTotal();
		
		if(C_Invoice_ID > 0){
			StringBuilder SQLGetPay = new StringBuilder();
			SQLGetPay.append("SELECT SUM(PayAmt) ");
			SQLGetPay.append(" FROM C_Payment ");
			SQLGetPay.append(" WHERE AD_Client_ID = "+ AD_Client_ID);
			SQLGetPay.append(" AND C_Invoice_ID = " + C_Invoice_ID);
			
			BigDecimal payAmt = DB.getSQLValueBDEx(null, SQLGetPay.toString());
			
			if(payAmt == null){
				payAmt = Env.ZERO;
			}
			
			rs = InvAmt.subtract(payAmt);
			
		}
		
		
		return rs;
		
	}
	
	
	public static HashMap<String, Integer> getDocumentRelated(int AD_Client_ID,int C_Order_ID){
		HashMap<String, Integer> rs = new HashMap<String, Integer>();
		
		
		Integer M_InOut_ID = 0;
		Integer C_Invoice_ID = 0;
		Integer C_Payment_ID = 0;

		
		StringBuilder SQLGetShip = new StringBuilder();
		SQLGetShip.append("SELECT M_InOut_ID ");
		SQLGetShip.append(" FROM M_InOut ");
		SQLGetShip.append(" WHERE AD_Client_ID = "+ AD_Client_ID);
		SQLGetShip.append(" AND C_Order_ID = " + C_Order_ID);
		SQLGetShip.append(" AND DocStatus = 'CO' ");
		
		M_InOut_ID = DB.getSQLValueEx(null, SQLGetShip.toString());
		
		StringBuilder SQLGetInv = new StringBuilder();
		SQLGetInv.append("SELECT C_Invoice_ID ");
		SQLGetInv.append(" FROM C_Invoice ");
		SQLGetInv.append(" WHERE AD_Client_ID = "+ AD_Client_ID);
		SQLGetInv.append(" AND C_Order_ID = " + C_Order_ID);
		SQLGetInv.append(" AND DocStatus = 'CO' ");
		
		C_Invoice_ID = DB.getSQLValueEx(null, SQLGetInv.toString());
		
		if(C_Invoice_ID > 0){
			
			StringBuilder SQLGetPay = new StringBuilder();
			SQLGetPay.append("SELECT C_Payment_ID ");
			SQLGetPay.append(" FROM C_Payment ");
			SQLGetPay.append(" WHERE AD_Client_ID = "+ AD_Client_ID);
			SQLGetPay.append(" AND C_Invoice_ID = " + C_Invoice_ID);
			SQLGetPay.append(" AND DocStatus = 'CO' ");
			
			C_Payment_ID = DB.getSQLValueEx(null, SQLGetPay.toString());
			
		}
		
		rs.put("C_Order", C_Order_ID);

		rs.put("M_InOut", M_InOut_ID);

		rs.put("C_Invoice", C_Invoice_ID);

		rs.put("C_Payment", C_Payment_ID);

		return rs;
		
	}

	
	public static Integer getID(int AD_Client_ID, String TableName,String ColumnName, Integer IntegerValue,String StringValue){
		
		Integer rs = 0;
		
		StringBuilder SQLCheck = new StringBuilder();
		SQLCheck.append("SELECT "+TableName+"_ID");
		SQLCheck.append(" FROM "+TableName);
		SQLCheck.append(" WHERE AD_Client_ID = "+ AD_Client_ID );
		SQLCheck.append(" AND "+ ColumnName + " = ?");
		
		rs = DB.getSQLValueEx(null, SQLCheck.toString(),StringValue != null ? StringValue:IntegerValue);
		
		return rs;
		
	}
	
public static Integer getIDFilterOrg(int AD_Client_ID, String TableName,String ColumnName, Integer IntegerValue,String StringValue,Integer AD_Org_ID){
		
		Integer rs = 0;
		
		StringBuilder SQLCheck = new StringBuilder();
		SQLCheck.append("SELECT "+TableName+"_ID");
		SQLCheck.append(" FROM "+TableName);
		SQLCheck.append(" WHERE AD_Client_ID = "+ AD_Client_ID );
		SQLCheck.append(" AND AD_Org_ID   = " +AD_Org_ID);
		SQLCheck.append(" AND "+ ColumnName + " = ?");

		
		rs = DB.getSQLValueEx(null, SQLCheck.toString(),StringValue != null ? StringValue:IntegerValue);
		
		return rs;
		
	}

	
	
	
	public static ArrayList<Integer> getPaymentRMA(int AD_Client_ID, int C_Order_ID){
		ArrayList<Integer> rs = new ArrayList<Integer>();
		
		StringBuilder SQLGetPaymentRMA = new StringBuilder();
		SQLGetPaymentRMA.append("SELECT cp.C_Payment_ID ");
		SQLGetPaymentRMA.append(" FROM C_Payment cp");
		SQLGetPaymentRMA.append(" INNER JOIN C_Invoice ci ON ci.C_Invoice_ID = cp.C_Invoice_ID AND ci.AD_Client_ID = ? AND ci.IsSoTrx = 'N' AND M_RMA_ID is not null");
		SQLGetPaymentRMA.append(" INNER JOIN M_RMA rma ON rma.M_RMA_ID = ci.M_RMA_ID AND rma.AD_Client_ID = ? AND rma.IsSoTrx = 'N'");
		SQLGetPaymentRMA.append(" INNER JOIN M_InOut mi ON mi.M_InOut_ID = rma.inout_ID AND mi.AD_Client_ID = ? AND mi.IsSoTrx = 'N'");
		SQLGetPaymentRMA.append(" INNER JOIN C_Order co ON co.C_Order_ID = mi.C_Order_ID AND co.AD_Client_ID = ? AND co.IsSoTrx = 'N' AND co.C_Order_ID = " +C_Order_ID);
		SQLGetPaymentRMA.append(" WHERE cp.AD_Client_ID = "+ AD_Client_ID);
		SQLGetPaymentRMA.append(" AND cp.Isreceipt = 'N'");
	
		
		PreparedStatement pstmt = null;
     	ResultSet rslt = null;
			try {
				pstmt = DB.prepareStatement(SQLGetPaymentRMA.toString(), null);
				
				pstmt.setInt(1, AD_Client_ID);
				pstmt.setInt(2, AD_Client_ID);
				pstmt.setInt(3, AD_Client_ID);
				pstmt.setInt(4, AD_Client_ID);
				rslt = pstmt.executeQuery();
				while (rslt.next()) {
					
					rs.add(rslt.getInt(1));
					
				}

			} catch (SQLException err) {
				
			} finally {
				DB.close(rslt, pstmt);
				rslt = null;
				pstmt = null;
			}
		
		
		
		return rs;
		
	}
	
	public static Integer getDocType_ID(int AD_Client_ID, String DocBaseType,String IsSoTrx,String DocSubTypeInv){
		
		Integer rs = 0;
		
		StringBuilder getDocType = new StringBuilder();
		
		getDocType.append("SELECT C_DocType_ID ");
		getDocType.append(" FROM C_DocType ");
		getDocType.append(" WHERE AD_Client_ID = "+AD_Client_ID);
		getDocType.append(" AND DocBaseType ='"+DocBaseType+"'");
		getDocType.append(" AND IsSOTrx = '"+IsSoTrx+"'");
		
		if(DocSubTypeInv != null && DocSubTypeInv != "" && !DocSubTypeInv.isEmpty()){
			getDocType.append(" AND DocSubTypeInv = '"+DocSubTypeInv+"'");
		}

		rs = DB.getSQLValueEx(null, getDocType.toString());
		
		
		return rs;
	}
	
	
	public static BigDecimal setQtyBook (int M_AttributeSetInstance_ID, int M_Product_ID, int M_Locator_ID){
		// Set QtyBook from first storage location
		BigDecimal bd = null;
		String sql = "SELECT QtyOnHand FROM M_StorageOnHand "
			+ "WHERE M_Product_ID=?"	//	1
			+ " AND M_Locator_ID=?"		//	2
			+ " AND M_AttributeSetInstance_ID=?";
		if (M_AttributeSetInstance_ID == 0)
			sql = "SELECT SUM(QtyOnHand) FROM M_StorageOnHand "
			+ "WHERE M_Product_ID=?"	//	1
			+ " AND M_Locator_ID=?";	//	2
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try
		{
			pstmt = DB.prepareStatement(sql, null);
			pstmt.setInt(1, M_Product_ID);
			pstmt.setInt(2, M_Locator_ID);
			if (M_AttributeSetInstance_ID != 0)
				pstmt.setInt(3, M_AttributeSetInstance_ID);
			rs = pstmt.executeQuery();
			if (rs.next())
			{
				bd = rs.getBigDecimal(1);
				if (bd != null)
					return bd;
			} else {
				return Env.ZERO;
			}
		}
		catch (SQLException e)
		{
			log.log(Level.SEVERE, sql, e);
		}
		finally
		{
			DB.close(rs, pstmt);
			rs = null;
			pstmt = null;
		}

		return Env.ZERO;
	}
	
	
	public static void createPOSPayment(int AD_Org_ID, int C_Order_ID,int C_POSTenderType_ID, String tenderType, BigDecimal payAmt,String leasingProvider, int C_BankAccount_ID, String TrxName) {

		//MOrder Order = new MOrder(Env.getCtx(), C_Order_ID, TrxName);

		X_C_POSPayment posPayment = new X_C_POSPayment(Env.getCtx(), 0,TrxName);

		posPayment.setAD_Org_ID(AD_Org_ID);
		posPayment.setC_Order_ID(C_Order_ID);
		posPayment.setC_POSTenderType_ID(C_POSTenderType_ID);
		posPayment.setTenderType(tenderType);
		posPayment.setPayAmt(payAmt);
		if (tenderType.equals("L")) {
			posPayment.set_CustomColumn("LeaseProvider", leasingProvider);
		}
		posPayment.set_CustomColumn("C_BankAccount_ID", C_BankAccount_ID);
		posPayment.saveEx();
	}
	
	
	public static Integer getDefaultPaymentTerm(int AD_Client_ID){
		
		Integer rs = 0;
		
		StringBuilder getPaymentTerm = new StringBuilder();
		
		getPaymentTerm.append("SELECT C_PaymentTerm_ID ");
		getPaymentTerm.append(" FROM C_PaymentTerm ");
		getPaymentTerm.append(" WHERE AD_Client_ID = "+AD_Client_ID);
		getPaymentTerm.append(" AND IsActive ='Y'");
		getPaymentTerm.append(" AND IsDefault = 'Y'");

		rs = DB.getSQLValueEx(null, getPaymentTerm.toString());
		
		
		return rs;
	}
	
	public static String getXYZLocator(int AD_Client_ID, int M_Warehouse_ID) throws Exception{
		
		String rs = "";
		ArrayList<String> cek = new ArrayList<String>();
		
		StringBuilder SQLGetXYZ = new StringBuilder();
		SQLGetXYZ.append("SELECT x");
		SQLGetXYZ.append(" FROM M_Locator ");
		SQLGetXYZ.append(" WHERE AD_Client_ID = ? ");
		SQLGetXYZ.append(" AND M_Warehouse_ID = ? ");
		

		PreparedStatement pstmt = null;
		ResultSet rslt = null;
		try
		{
			pstmt = DB.prepareStatement(SQLGetXYZ.toString(), null);
			pstmt.setInt(1, AD_Client_ID);
			pstmt.setInt(2, M_Warehouse_ID);
			rslt = pstmt.executeQuery();
			while (rslt.next()){
				cek.add(rslt.getString(1));
			} 
		}
		catch (SQLException e)
		{
			log.log(Level.SEVERE, SQLGetXYZ.toString(), e);
			throw new Exception(e.getLocalizedMessage());
		}
		finally
		{
			DB.close(rslt, pstmt);
			rs = null;
			pstmt = null;
		}

		
		
		//cek
		Integer initialCek = 10;
		boolean OK = true;

		do{
			
			if(cek.contains(initialCek.toString())){
				OK = false;
				initialCek = initialCek + 1;
			}else{
				OK = true;
			}
			
		}while (!OK);
		
		
		
		rs = initialCek.toString();
		
		return rs;
		
	}
	
	public static String getValueElement(int AD_Client_ID){
		
		
		StringBuilder SQLGetValue = new StringBuilder();
		String SetValueStr = "";
		String SetValueStrPrefix = "1112.";


		
		SQLGetValue.append("SELECT MAX(right(value,3))::numeric");
		SQLGetValue.append(" FROM C_Elementvalue");
		SQLGetValue.append(" WHERE LEFT(value,4)");
		SQLGetValue.append(" IN");
		SQLGetValue.append(" (SELECT left(value,4)");
		SQLGetValue.append(" FROM C_Elementvalue");
		SQLGetValue.append(" WHERE AD_Client_ID = ?");
		SQLGetValue.append(" AND name = 'Bank' )");
		SQLGetValue.append(" AND AD_Client_ID = ?");


		Integer value = DB.getSQLValueEx(null, SQLGetValue.toString(), new Object[]{AD_Client_ID,AD_Client_ID});
		Integer SetValue = (value+1);
		SetValueStr = SetValue.toString();
		
		if(SetValueStr.length() == 1){	
			SetValueStr = "00"+SetValueStr;
		}else if(SetValueStr.length() == 2){
			SetValueStr = "0"+SetValueStr;
		}
		
		return SetValueStrPrefix+SetValueStr;
		
		
	}

	
	public static Timestamp convertStringToTimeStamp(String strDate) throws ParseException {
		
		Timestamp rs = null;
		
//		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
		Date date = Date.valueOf(strDate);
		
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		
		rs = new Timestamp(cal.getTimeInMillis());
		
	    return rs;
	      
	}
	
}
