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
import org.compiere.model.MInvoiceLine;
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

public class SemeruPenerimaanPiutang {

	public CLogger log = CLogger.getCLogger(SemeruPenerimaanPiutang.class);
	Vector<Vector<Object>> dataSummary = new Vector<Vector<Object>>();
	private Properties ctx = Env.getCtx();

	
	//Get poduct
	protected Vector<Vector<Object>> getTrxData(ArrayList<Integer> detailList,IMiniTable MiniTable) {
		Vector<Vector<Object>> data = new Vector<Vector<Object>>();

			 if (detailList.size() > 0){
			
				for (int i = 0; i < detailList.size(); i++){
					
					Vector<Object> line = new Vector<Object>(1);

					MInvoiceLine invLine = new MInvoiceLine(ctx, detailList.get(i), null);
					MProduct product = new MProduct(ctx, invLine.getM_Product_ID(), null);
					
					
					KeyNamePair kpProd = new KeyNamePair(product.getM_Product_ID(),product.getName());
					BigDecimal qty = invLine.getQtyInvoiced();
					BigDecimal Price = invLine.getPriceEntered();
					BigDecimal total = invLine.getLineNetAmt();

					
					line.add(new Boolean(false));
					line.add(kpProd);
					line.add(qty);
					line.add(Price);
					line.add(total);
					data.add(line);
					
				}
			}
			
			return data;
		}
	
	
	 protected int updateData(int C_DecorisPOS_ID){
		 
			StringBuilder SQLExFunction = new StringBuilder();
	        SQLExFunction.append("SELECT update_print_info_pos(?)");
	        int rslt = 0;
	         
	     	PreparedStatement pstmt = null;
	     	ResultSet rs = null;
				try {
					pstmt = DB.prepareStatement(SQLExFunction.toString(), null);
					pstmt.setInt(1, C_DecorisPOS_ID);	
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
