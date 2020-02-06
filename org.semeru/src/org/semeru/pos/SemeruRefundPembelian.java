package org.semeru.pos;

import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Properties;
import java.util.Vector;
import java.util.logging.Level;

import org.compiere.minigrid.IMiniTable;
import org.compiere.model.MAttributeSetInstance;
import org.compiere.model.MInOutLine;
import org.compiere.model.MLocator;
import org.compiere.model.MProduct;
import org.compiere.util.CLogger;
import org.compiere.util.DB;
import org.compiere.util.Env;
import org.compiere.util.KeyNamePair;

/**
 * 
 * @author Tegar N
 *
 */

public class SemeruRefundPembelian {
	public CLogger log = CLogger.getCLogger(SemeruRefundPembelian.class);
	Vector<Vector<Object>> data = null;
	private Properties ctx = Env.getCtx();
	
	protected Vector<Vector<Object>> getTrxData(ArrayList<Integer> detailList,IMiniTable MiniTable) {
		if (detailList.size() > 0){
			data = new Vector<Vector<Object>>();
			for (int i = 0; i < detailList.size(); i++){
					
				Vector<Object> line = new Vector<Object>(1);

				MInOutLine detailShipLine = new MInOutLine(ctx, detailList.get(i), null);
				MProduct product = new MProduct(ctx, detailShipLine.getM_Product_ID(), null);
				MLocator loc = new MLocator(ctx, detailShipLine.getM_Locator_ID(), null);
				MAttributeSetInstance imei = new MAttributeSetInstance(ctx, detailShipLine.getM_AttributeSetInstance_ID(), null);
				KeyNamePair kpProd = new KeyNamePair(detailShipLine.getM_InOutLine_ID(),product.getName());
				KeyNamePair kpLoc = new KeyNamePair(detailShipLine.getM_Locator_ID(),loc.getValue());
				KeyNamePair kpImei = new KeyNamePair(imei.getM_AttributeSetInstance_ID(),imei.getSerNo());
				BigDecimal qty = detailShipLine.getQtyEntered();


					
				line.add(new Boolean(false));
				line.add(kpProd);
				line.add(kpImei);
				line.add("");
				line.add(kpLoc);
				line.add(qty);
				line.add(Env.ZERO);
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
		SQLDocType.append("WHERE DocBaseType = 'POO' ");
		SQLDocType.append(" AND docSubtypeSO = 'RM' ");
		SQLDocType.append(" AND IsSOTrx = 'N' ");
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

}
