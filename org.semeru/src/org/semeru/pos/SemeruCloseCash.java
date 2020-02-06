package org.semeru.pos;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Properties;
import java.util.Vector;
import java.util.logging.Level;

import org.compiere.minigrid.IMiniTable;
import org.compiere.util.CLogger;
import org.compiere.util.DB;
import org.compiere.util.Env;
import org.compiere.util.KeyNamePair;

/**
 * 
 * @author Tegar N
 *
 */

public class SemeruCloseCash {
	
	public CLogger log = CLogger.getCLogger(SemeruCloseCash.class);
	Vector<Vector<Object>> data = new Vector<Vector<Object>>();
	Vector<Vector<Object>> dataSummary = new Vector<Vector<Object>>();
	
	private Properties ctx = Env.getCtx();
	private int AD_Client_ID  = Env.getAD_Client_ID(ctx);

	
	
	protected Vector<Vector<Object>> getCloseCashData(int AD_Client_ID,int AD_Org_ID, Timestamp DateTrx1,Timestamp DateTrx2,int CreatedBYPOS_ID,String KriteriaData,IMiniTable MiniTable) {
		
		
		StringBuilder SQLGetCloceCashData = new StringBuilder();
		
		Timestamp now = new Timestamp(System.currentTimeMillis()); 
		
		SQLGetCloceCashData.append("SELECT Datetrx,CloseCashDate,DocumentNoTutupKas,seqTutupKas,");
		SQLGetCloceCashData.append(" CurrentBalance,CashIn,CashOut,TaxAmt,Total,TotalOmset");
		SQLGetCloceCashData.append(" FROM sm_semeru_closecash_summary_v ");
		SQLGetCloceCashData.append(" WHERE AD_Client_ID = ? ");
		SQLGetCloceCashData.append(" AND AD_Org_ID = ? ");
		
		if (DateTrx1 != null && DateTrx2 == null){
			SQLGetCloceCashData.append(" AND (DateTrx BETWEEN '"+DateTrx1+"' AND '"+now+"') ");
		}else if (DateTrx1 != null && DateTrx2 != null){
			SQLGetCloceCashData.append(" AND (DateTrx BETWEEN '"+DateTrx1+"' AND '"+DateTrx2+"') ");
		}
		
		if (CreatedBYPOS_ID >= 0){
			SQLGetCloceCashData.append(" AND CreatedByPOS_ID = "+ CreatedBYPOS_ID);
		}
		
		if (!KriteriaData.isEmpty() || KriteriaData != "" ){
			
			if(KriteriaData.equals("B")){
				SQLGetCloceCashData.append(" AND CloseCashDate IS NULL ");

			}else if(KriteriaData.equals("C")){
				SQLGetCloceCashData.append(" AND CloseCashDate IS NOT NULL ");

			}	
		}

		SQLGetCloceCashData.append(" ORDER BY DateTrx ASC ");

			

		PreparedStatement pstmtClsd = null;
		ResultSet rsClsd = null;
		
		try {
			pstmtClsd = DB.prepareStatement(SQLGetCloceCashData.toString(), null);
			pstmtClsd.setInt(1, AD_Client_ID);
			pstmtClsd.setInt(2, AD_Org_ID);


			rsClsd = pstmtClsd.executeQuery();
			while (rsClsd.next()) {
			Vector<Object> line =null;

				line = new Vector<Object>(9);
				line.add(new Boolean(false));
				line.add(rsClsd.getTimestamp(1));
				line.add(rsClsd.getTimestamp(2));
				line.add(rsClsd.getString(3));
				line.add(rsClsd.getInt(4));
				line.add(rsClsd.getBigDecimal(6));
				line.add(rsClsd.getBigDecimal(7));
				line.add(rsClsd.getBigDecimal(8));
				line.add(rsClsd.getBigDecimal(9));
				line.add(rsClsd.getBigDecimal(10));
			
				
				data.add(line);
			}

		} catch (SQLException e) {
			log.log(Level.SEVERE, SQLGetCloceCashData.toString(), e);
		} finally {
			DB.close(rsClsd, pstmtClsd);
			rsClsd = null;
			pstmtClsd = null;
		}
		
		
		return data;
		
	}
	
	
	protected Vector<Vector<Object>> getCloseCashSummary(int AD_Client_ID,int AD_Org_ID, Timestamp DateTrx,Timestamp CloseCashDate,int CreatedByPOS_ID,String documentNo,Integer seqTutupKas ) {
	
		StringBuilder SQLSummary = new StringBuilder();
		SQLSummary.append(" SELECT Cash,BankPayment,LeasingPayment,Hutang,OtherLeasingPayment ");
		SQLSummary.append(" FROM sm_semeru_closecash_summary_v ");
		SQLSummary.append(" WHERE AD_Client_ID = ? ");
		SQLSummary.append(" AND AD_Org_ID = ? ");
		SQLSummary.append(" AND DateTrx = ? ");
		SQLSummary.append(" AND CreatedByPOS_ID = ? ");

		
		if(CloseCashDate != null){
			SQLSummary.append(" AND CloseCashDate =  '" + CloseCashDate +"' ");
			SQLSummary.append(" AND DocumentNoTutupKas =  '" + documentNo +"' ");
			SQLSummary.append(" AND seqTutupKas =  '" + seqTutupKas +"'");
		}else if(CloseCashDate == null){
			SQLSummary.append(" AND CloseCashDate IS NULL ");
		}

		
		PreparedStatement pstmtSmry = null;
		ResultSet rsSmry = null;
		
		try {
			pstmtSmry = DB.prepareStatement(SQLSummary.toString(), null);
			pstmtSmry.setInt(1, AD_Client_ID);
			pstmtSmry.setInt(2, AD_Org_ID);
			pstmtSmry.setTimestamp(3, DateTrx);
			pstmtSmry.setInt(4, CreatedByPOS_ID);


			rsSmry = pstmtSmry.executeQuery();
			while (rsSmry.next()) {
				
			Vector<Object> line = new Vector<Object>(4);

				line.add(rsSmry.getBigDecimal(1));
				line.add(rsSmry.getBigDecimal(2));
				line.add(rsSmry.getBigDecimal(3));
				line.add(rsSmry.getBigDecimal(5));
				line.add(rsSmry.getBigDecimal(4));

				dataSummary.add(line);
			}

		} catch (SQLException e) {
			log.log(Level.SEVERE, SQLSummary.toString(), e);
		} finally {
			DB.close(rsSmry, pstmtSmry);
			rsSmry = null;
			pstmtSmry = null;
		}
		
		
		return dataSummary;
		
	}

	protected ArrayList<KeyNamePair> loadKasir(boolean isAdmin, int CreatedByPOS_ID) {
		
		ArrayList<KeyNamePair> listKasir = new ArrayList<KeyNamePair>();

		StringBuilder SQLLoadKasir = new StringBuilder();
		SQLLoadKasir.append(" SELECT a.C_BPartner_ID,a.Name ");
		SQLLoadKasir.append(" FROM ( ");

		SQLLoadKasir.append(" SELECT null AS C_BPartner_ID,'' AS Name ");
		SQLLoadKasir.append(" UNION ");

		SQLLoadKasir.append(" SELECT C_BPartner_ID,Name ");
		SQLLoadKasir.append(" FROM C_BPartner ");
		SQLLoadKasir.append(" WHERE AD_Client_ID = ? ");
		SQLLoadKasir.append(" AND C_BPartner_ID IN ( ");

		SQLLoadKasir.append(" SELECT DISTINCT CreatedByPOS_ID ");
		SQLLoadKasir.append(" FROM C_POS ");
		SQLLoadKasir.append(" WHERE AD_Client_ID = ? ");
		SQLLoadKasir.append(" AND IsActive = 'Y' ");
		SQLLoadKasir.append(" AND Name NOT LIKE '%Maintenance%' ");
		
		if (isAdmin){
			SQLLoadKasir.append("))a");
		}
		
		if (!isAdmin){
			SQLLoadKasir.append(" AND CreatedByPOS_ID  = "+CreatedByPOS_ID + "))a");
		}
		SQLLoadKasir.append(" ORDER BY a.Name ");

		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			pstmt = DB.prepareStatement(SQLLoadKasir.toString(), null);
			pstmt.setInt(1, AD_Client_ID);
			pstmt.setInt(2, AD_Client_ID);

			rs = pstmt.executeQuery();
			while (rs.next()) {
				listKasir.add(new KeyNamePair(rs.getInt(1), rs.getString(2)));
			}

		} catch (SQLException e) {
			log.log(Level.SEVERE, SQLLoadKasir.toString(), e);
		} finally {
			DB.close(rs, pstmt);
			rs = null;
			pstmt = null;
		}

		
		
		
		return listKasir;
	
	}
	
	protected void tableLoad (int AD_Client_ID,int AD_Org_ID, Timestamp DateTrx1,Timestamp DateTrx2,int CreatedBYPOS_ID,String KriteriaData,IMiniTable MiniTable)
	{
	
		
		StringBuilder SQLGetCloceCashData = new StringBuilder();
		
		Timestamp now = new Timestamp(System.currentTimeMillis()); 
		
		SQLGetCloceCashData.append("SELECT 1,Datetrx,CloseCashDate,DocumentNoTutupKas,seqTutupKas,");
		SQLGetCloceCashData.append(" CashIn,CashOut,TaxAmt,Total,TotalOmset");
		SQLGetCloceCashData.append(" FROM sm_semeru_closecash_summary_v ");
		SQLGetCloceCashData.append(" WHERE AD_Client_ID =  " + AD_Client_ID);
		SQLGetCloceCashData.append(" AND AD_Org_ID =  " + AD_Org_ID);
		
		if (DateTrx1 != null && DateTrx2 == null){
			SQLGetCloceCashData.append(" AND (DateTrx BETWEEN '"+DateTrx1+"' AND '"+now+"') ");
		}else if (DateTrx1 != null && DateTrx2 != null){
			SQLGetCloceCashData.append(" AND (DateTrx BETWEEN '"+DateTrx1+"' AND '"+DateTrx2+"') ");
		}
		
		if (CreatedBYPOS_ID >= 0){
			SQLGetCloceCashData.append(" AND CreatedByPOS_ID = "+ CreatedBYPOS_ID);
		}
		
		if (!KriteriaData.isEmpty() || KriteriaData != "" ){
			
			if(KriteriaData.equals("B")){
				SQLGetCloceCashData.append(" AND CloseCashDate IS NULL ");

			}else if(KriteriaData.equals("C")){
				SQLGetCloceCashData.append(" AND CloseCashDate IS NOT NULL ");

			}	
		}

		SQLGetCloceCashData.append(" ORDER BY DateTrx ASC ");
		
		log.finest(SQLGetCloceCashData.toString());
		Statement stmt = null;
		ResultSet rs = null;
		try
		{
			stmt = DB.createStatement();
			rs = stmt.executeQuery(SQLGetCloceCashData.toString());
			
			
			MiniTable.loadTable(rs);
		}
		catch (SQLException e)
		{
			log.log(Level.SEVERE, SQLGetCloceCashData.toString(), e);
		}
		finally
		{
			DB.close(rs,stmt);
			rs = null;stmt = null;
		}
	}   //  tableLoad


}
