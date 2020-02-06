package org.semeru.mrp.form;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.DecimalFormat;
import java.util.Properties;
import java.util.Vector;
import java.util.logging.Level;

import org.compiere.model.MProduct;
import org.compiere.util.CLogger;
import org.compiere.util.DB;
import org.compiere.util.DisplayType;
import org.compiere.util.Env;
import org.compiere.util.KeyNamePair;

public class SM_KinoMRP {

	
	public CLogger log = CLogger.getCLogger(SM_KinoMRP.class);
	private Properties ctx = Env.getCtx();
	Vector<Vector<Object>> data = new Vector<Vector<Object>>();
	Vector<Vector<Object>> data2 = new Vector<Vector<Object>>();

	protected DecimalFormat format = DisplayType.getNumberFormat(DisplayType.Amount);
//	private int AD_Client_ID = Env.getAD_Client_ID(ctx);
	

	
	protected Vector<Vector<Object>> getdataMPSLine(int SM_MPS_ID,int C_Period_ID,Timestamp DateStart, Timestamp DateEnd) {
//		Timestamp now = new Timestamp(System.currentTimeMillis()); 
		Vector<Vector<Object>> dataMPSLine = new Vector<Vector<Object>>();

		
		StringBuilder SQLGetData = new StringBuilder();
		SQLGetData.append("SELECT ml.week,ml.mpsdate, ml.frs,ml.cor,ml.pab,ml.atp,ml.mps,ml.isdtf,ml.sm_mpsline_id,m.M_Product_ID ");
		SQLGetData.append(" FROM SM_MPSLine ml");
		SQLGetData.append(" LEFT JOIN SM_MPS m ON m.SM_MPS_ID = ml.SM_MPS_ID ");
		SQLGetData.append(" WHERE ml.SM_MPS_ID = "+SM_MPS_ID);
		SQLGetData.append(" AND ml.C_Period_ID = "+C_Period_ID);

		

		if (DateStart != null && DateEnd == null){
			SQLGetData.append(" AND (ml.mpsdate > '"+DateStart+"') ");
		}else if (DateStart == null && DateEnd != null){
			SQLGetData.append(" AND (ml.mpsdate < '"+DateEnd+"') ");
		}else if (DateStart != null && DateEnd != null){
			SQLGetData.append(" AND (ml.mpsdate BETWEEN '"+DateStart+"' AND '"+DateEnd+"') ");
		}
		
		PreparedStatement pstmt = null;
     	ResultSet rs = null;
			try {
				pstmt = DB.prepareStatement(SQLGetData.toString(), null);
				rs = pstmt.executeQuery();
				while (rs.next()) {
					
					int SM_MPSLine_ID = rs.getInt(9); 
					int M_Product_ID = rs.getInt(10); 
					MProduct product = new MProduct(ctx, M_Product_ID, null);
					
					KeyNamePair kp = new KeyNamePair(SM_MPSLine_ID, rs.getString(8));
					KeyNamePair kProd = new KeyNamePair(M_Product_ID, product.getName());


					Vector<Object> line = new Vector<Object>(4);

					line.add(new Boolean(false));			//check
					line.add(kProd);			//week
					line.add(rs.getBigDecimal(1));			//week
					line.add(rs.getTimestamp(2));			//day
					line.add(rs.getBigDecimal(3));			//forecast
					line.add(rs.getBigDecimal(4));			//customer order
					line.add(rs.getBigDecimal(5));			//pab
					line.add(rs.getBigDecimal(6));			//atp
					line.add(rs.getBigDecimal(7));			//mps
					line.add(kp);				//dtf

					dataMPSLine.add(line);
				}
				
					
				

			} catch (SQLException err) {
				log.log(Level.SEVERE, SQLGetData.toString(), err);
			} finally {
				DB.close(rs, pstmt);
				rs = null;
				pstmt = null;
			}
		
		
		
		
		return dataMPSLine;
		
	}

	
	
protected Vector<Vector<Object>> getdataMRPLine(int SM_MPSLine_ID) {
		
		Vector<Vector<Object>> dataMRPLine = new Vector<Vector<Object>>();

		StringBuilder SQLGetData = new StringBuilder();
//		SQLGetData.append("SELECT hd.M_product_ID,hd.bomgrouplevel,ln.week,ln.mrpdate,ln.gre,ln.pab,ln.net,ln.prc,ln.prl,ln.processed");
//		SQLGetData.append(" FROM SM_MRPLine ln ");
//		SQLGetData.append(" LEFT JOIN SM_MRP hd ON hd.SM_MRP_ID = ln.SM_MRP_ID");
//		SQLGetData.append(" LEFT JOIN SM_MRPElement me hd ON me.SM_MRPElement_ID = ln.SM_MRPElement_ID");
//		SQLGetData.append(" WHERE hd.SM_MPSLine_ID = "+SM_MPSLine_ID);
		
		
		SQLGetData.append(" SELECT mrph.m_product_id,");
		SQLGetData.append(" mrph.bomgrouplevel,");
		SQLGetData.append(" ab.qty AS groreq,");
		SQLGetData.append(" cd.qty AS proohn,");
		SQLGetData.append(" ef.qty AS netreq,");
		SQLGetData.append(" gh.qty AS porrec,");
		SQLGetData.append(" ij.qty AS porrel,");
		SQLGetData.append(" ij.processed AS processed");
		SQLGetData.append(" FROM  sm_mrp mrph");
		SQLGetData.append(" LEFT JOIN  sm_mpsline mpsl ON mrph.sm_mpsline_id = mpsl.sm_mps_id AND mrph.mpsseq = mpsl.seq");
		SQLGetData.append(" LEFT JOIN  ( SELECT a.sm_mrp_id, a.mrpdate, a.week, a.qty FROM sm_mrpline a, sm_mrpelement b WHERE a.sm_mrpelement_id = b.sm_mrpelement_id AND b.value = 'groReq' ) ab ON mrph.sm_mrp_id = ab.sm_mrp_id");
		SQLGetData.append(" LEFT JOIN  ( SELECT c.sm_mrp_id, c.mrpdate, c.week, c.qty FROM sm_mrpline c, sm_mrpelement d WHERE c.sm_mrpelement_id = d.sm_mrpelement_id AND d.value = 'proOhn' ) cd ON mrph.sm_mrp_id = cd.sm_mrp_id");
		SQLGetData.append(" LEFT JOIN  ( SELECT e.sm_mrp_id, e.mrpdate, e.week, e.qty FROM sm_mrpline e, sm_mrpelement f WHERE e.sm_mrpelement_id = f.sm_mrpelement_id AND f.value = 'netReq' ) ef ON mrph.sm_mrp_id = ef.sm_mrp_id");
		SQLGetData.append(" LEFT JOIN  ( SELECT g.sm_mrp_id, g.mrpdate, g.week, g.qty FROM sm_mrpline g, sm_mrpelement h WHERE g.sm_mrpelement_id = h.sm_mrpelement_id AND h.value = 'porRec' ) gh ON mrph.sm_mrp_id = gh.sm_mrp_id");
		SQLGetData.append(" LEFT JOIN  ( SELECT i.sm_mrp_id, i.mrpdate, i.week, i.qty, i.processed FROM sm_mrpline i, sm_mrpelement j WHERE i.sm_mrpelement_id = j.sm_mrpelement_id AND j.value = 'porRel' ) ij ON mrph.sm_mrp_id = ij.sm_mrp_id");
		SQLGetData.append(" WHERE  mpsl.sm_mpsline_id = "+SM_MPSLine_ID);
		
		
		PreparedStatement pstmt = null;
     	ResultSet rs = null;
			try {
				pstmt = DB.prepareStatement(SQLGetData.toString(), null);
				rs = pstmt.executeQuery();
				while (rs.next()) {
					
					MProduct prod = new MProduct(ctx, rs.getInt(1), null);
					KeyNamePair kp = new KeyNamePair(prod.getM_Product_ID(), prod.getName());

					
					
					Vector<Object> line = new Vector<Object>(11);

					line.add(kp);			
					line.add(rs.getInt(2));			
					line.add(rs.getBigDecimal(3));			//
					line.add(rs.getBigDecimal(4));			//
					line.add(rs.getBigDecimal(5));			//
					line.add(rs.getBigDecimal(6));			//
					line.add(rs.getBigDecimal(7));			//
					line.add(rs.getString(8));			//

					dataMRPLine.add(line);
				}
				
					
				

			} catch (SQLException err) {
				log.log(Level.SEVERE, SQLGetData.toString(), err);
			} finally {
				DB.close(rs, pstmt);
				rs = null;
				pstmt = null;
			}
		
		
		
		
		return dataMRPLine;
		
	}


	protected void updateMRPLine(int AD_Client_ID, int AD_org_ID, int SM_MPS_ID,int SM_MPSLine_ID) {
		
		
		StringBuilder calMrp = new StringBuilder();
		calMrp.append("SELECT adempiere.f_mrp(? , ? , ? , 2 )");

		PreparedStatement pstmtLinecalMrp = null;
     	ResultSet rsLinecalMrp = null;
			try {
				pstmtLinecalMrp = DB.prepareStatement(calMrp.toString(), null);
				pstmtLinecalMrp.setInt(1, AD_Client_ID);	
				pstmtLinecalMrp.setInt(2, AD_org_ID);	
				pstmtLinecalMrp.setInt(3, SM_MPS_ID);	

				rsLinecalMrp = pstmtLinecalMrp.executeQuery();
				while (rsLinecalMrp.next()) {
					
				}

			} catch (SQLException err) {
				log.log(Level.SEVERE, calMrp.toString(), err);
			} finally {
				DB.close(rsLinecalMrp, pstmtLinecalMrp);
				rsLinecalMrp = null;
				pstmtLinecalMrp = null;
			}
		

//		StringBuilder deleteMRPLine = new StringBuilder();
//		deleteMRPLine.append("SELECT delete_mrpline(?)");
//
//		StringBuilder deleteMRP = new StringBuilder();
//		deleteMRP.append("SELECT delete_mrp(?)");
//
//		PreparedStatement pstmtLineLine = null;
//     	ResultSet rsLineLine = null;
//			try {
//				pstmtLineLine = DB.prepareStatement(deleteMRPLine.toString(), null);
//				pstmtLineLine.setInt(1, SM_MPS_ID);	
//				rsLineLine = pstmtLineLine.executeQuery();
//				while (rsLineLine.next()) {
//					
//				}
//
//			} catch (SQLException err) {
//				log.log(Level.SEVERE, deleteMRPLine.toString(), err);
//			} finally {
//				DB.close(rsLineLine, pstmtLineLine);
//				rsLineLine = null;
//				pstmtLineLine = null;
//			}
//			
//			PreparedStatement pstmtLineHdr = null;
//	     	ResultSet rsLineHdr = null;
//				try {
//					pstmtLineHdr = DB.prepareStatement(deleteMRP.toString(), null);
//					pstmtLineHdr.setInt(1, SM_MPS_ID);	
//					rsLineHdr = pstmtLineHdr.executeQuery();
//					while (rsLineHdr.next()) {
//						
//					}
//
//				} catch (SQLException err) {
//					log.log(Level.SEVERE, deleteMRP.toString(), err);
//				} finally {
//					DB.close(rsLineHdr, pstmtLineHdr);
//					rsLineHdr = null;
//					pstmtLineHdr = null;
//				}
//		
//		
//		StringBuilder getMRPFromView = new StringBuilder();
//		getMRPFromView.append(" SELECT AD_Org_ID, SM_MPS_ID, seq, M_Product_ID, level, lotsizemethod, leadtime, mps, safetystock, mpsdate, bomline");
//		getMRPFromView.append(" FROM sm_mrp_draft_v ");
//		getMRPFromView.append(" WHERE SM_MPS_ID = "+SM_MPS_ID);
//		
//		PreparedStatement pstmt = null;
//     	ResultSet rs = null;
//			try {
//				pstmt = DB.prepareStatement(getMRPFromView.toString(), null);
//				rs = pstmt.executeQuery();
//				while (rs.next()) {
//				
//					X_SM_MRP mrp = new X_SM_MRP(ctx, 0, null);
//					mrp.setAD_Org_ID(rs.getInt(1));
//					mrp.setSM_MPS_ID(SM_MPS_ID);
//					mrp.setSM_MPSLine_ID(SM_MPSLine_ID);
//					mrp.setM_Product_ID(rs.getInt(4));
//					mrp.setbomgrouplevel(rs.getBigDecimal(5));
//					mrp.setlotsizemethod(rs.getString(6));
//					mrp.setleadtime(rs.getInt(7));
//					mrp.setmps(rs.getBigDecimal(8));
//					mrp.setSafetyStock(rs.getBigDecimal(9));
//					mrp.saveEx();
//					
//				}
//
//			} catch (SQLException err) {
//				log.log(Level.SEVERE, getMRPFromView.toString(), err);
//			} finally {
//				DB.close(rs, pstmt);
//				rs = null;
//				pstmt = null;
//			}
//
//
//		StringBuilder getMRPLineFromView = new StringBuilder();
//		getMRPLineFromView.append(" SELECT AD_Org_ID, SM_MRP_ID, SM_MPS_ID, gre, src, pab, net, prc, prl, mrpdate, week, seq");
//		getMRPLineFromView.append(" FROM sm_mrpline_draft_v ");
//		getMRPLineFromView.append(" WHERE SM_MPS_ID = "+SM_MPS_ID);
//		
//		PreparedStatement pstmtLine = null;
//     	ResultSet rsLine = null;
//			try {
//				pstmtLine = DB.prepareStatement(getMRPLineFromView.toString(), null);
//				rsLine = pstmtLine.executeQuery();
//				while (rsLine.next()) {
//				
//					X_SM_MRPLine mrpLine = new X_SM_MRPLine(ctx, 0, null);
//					
//					
//					mrpLine.setAD_Org_ID(rsLine.getInt(1));
//					mrpLine.setSM_MRP_ID(rsLine.getInt(2));
//					mrpLine.setgre(rsLine.getBigDecimal(4));
//					mrpLine.setsrc(rsLine.getBigDecimal(5));
//					mrpLine.setpab(rsLine.getBigDecimal(6)); 	
//					mrpLine.setnet(rsLine.getBigDecimal(7));
//					mrpLine.setprc(rsLine.getBigDecimal(8));
//					mrpLine.setprl(rsLine.getBigDecimal(9));
//					mrpLine.setmrpdate(rsLine.getTimestamp(10));
//					mrpLine.setweek(rsLine.getInt(11));
//					mrpLine.setseq(rsLine.getInt(12));
//
//					mrpLine.saveEx();
//					
//				}
//
//			} catch (SQLException err) {
//				log.log(Level.SEVERE, getMRPLineFromView.toString(), err);
//			} finally {
//				DB.close(rsLine, pstmtLine);
//				rsLine = null;
//				pstmtLine = null;	
//			}
//
//	
//			//update
//			
//			
//			
//			
//			
//			StringBuilder updateGre = new StringBuilder();
//			updateGre.append("SELECT update_mrpline_gre(sm_mrp_id,'LFL',seq) FROM sm_mrpline order by seq");
//
//			PreparedStatement pstmtLineGre = null;
//	     	ResultSet rsLineGre = null;
//				try {
//					pstmtLineGre = DB.prepareStatement(updateGre.toString(), null);
//					rsLineGre = pstmtLineGre.executeQuery();
//					while (rsLineGre.next()) {
//						
//					}
//
//				} catch (SQLException err) {
//					log.log(Level.SEVERE, updateGre.toString(), err);
//				} finally {
//					DB.close(rsLineGre, pstmtLineGre);
//					rsLineGre = null;
//					pstmtLineGre = null;
//				}
//			
//			
//			
//			
//			StringBuilder updateSrc = new StringBuilder();
//			updateSrc.append("SELECT update_mrpline_src(sm_mrp_id,'LFL',seq) FROM sm_mrpline order by seq");
//			
//			PreparedStatement pstmtLineSrc = null;
//	     	ResultSet rsLineSrc = null;
//				try {
//					pstmtLineSrc = DB.prepareStatement(updateSrc.toString(), null);
//					rsLineSrc = pstmtLineSrc.executeQuery();
//					while (rsLineSrc.next()) {
//						
//					}
//
//				} catch (SQLException err) {
//					log.log(Level.SEVERE, updateSrc.toString(), err);
//				} finally {
//					DB.close(rsLineSrc, pstmtLineSrc);
//					rsLineSrc = null;
//					pstmtLineSrc = null;
//				}
//
//			
//			StringBuilder updatePab = new StringBuilder();
//			updatePab.append("SELECT update_mrpline_pab(sm_mrp_id,'LFL',seq) FROM sm_mrpline order by seq");
//
//			PreparedStatement pstmtLinePab = null;
//	     	ResultSet rsLinePab = null;
//				try {
//					pstmtLinePab = DB.prepareStatement(updatePab.toString(), null);
//					rsLinePab = pstmtLinePab.executeQuery();
//					while (rsLinePab.next()) {
//						
//					}
//
//				} catch (SQLException err) {
//					log.log(Level.SEVERE, updatePab.toString(), err);
//				} finally {
//					DB.close(rsLinePab, pstmtLinePab);
//					rsLinePab = null;
//					pstmtLinePab = null;
//				}
//
//			
//
//			StringBuilder updateNet = new StringBuilder();
//			updateNet.append("SELECT update_mrpline_net(sm_mrp_id,'LFL',seq) FROM sm_mrpline order by seq");
//
//			PreparedStatement pstmtLineNet = null;
//	     	ResultSet rsLineNet = null;
//				try {
//					pstmtLineNet = DB.prepareStatement(updateNet.toString(), null);
//					rsLineNet = pstmtLineNet.executeQuery();
//					while (rsLineNet.next()) {
//						
//					}
//
//				} catch (SQLException err) {
//					log.log(Level.SEVERE, updateNet.toString(), err);
//				} finally {
//					DB.close(rsLineNet, pstmtLineNet);
//					rsLineNet = null;
//					pstmtLineNet = null;
//				}
//			
//
//			StringBuilder updatePrc = new StringBuilder();
//			updatePrc.append("SELECT update_mrpline_prc(sm_mrp_id,'LFL',seq) FROM sm_mrpline order by seq");
//			
//			PreparedStatement pstmtLinePrc = null;
//	     	ResultSet rsLinePrc = null;
//				try {
//					pstmtLinePrc = DB.prepareStatement(updatePrc.toString(), null);
//					rsLinePrc = pstmtLinePrc.executeQuery();
//					while (rsLinePrc.next()) {
//						
//					}
//
//				} catch (SQLException err) {
//					log.log(Level.SEVERE, updatePrc.toString(), err);
//				} finally {
//					DB.close(rsLinePrc, pstmtLinePrc);
//					rsLinePrc = null;
//					pstmtLinePrc = null;
//				}
//			
//			
//			
//			StringBuilder updatePrl = new StringBuilder();
//			updatePrl.append("SELECT update_mrpline_prl(sm_mrp_id,'LFL',seq) FROM sm_mrpline order by seq");
//
//			PreparedStatement pstmtLinePrl = null;
//	     	ResultSet rsLinePrl = null;
//				try {
//					pstmtLinePrl = DB.prepareStatement(updatePrl.toString(), null);
//					rsLinePrl = pstmtLinePrl.executeQuery();
//					while (rsLinePrl.next()) {
//						
//					}
//
//				} catch (SQLException err) {
//					log.log(Level.SEVERE, updatePrl.toString(), err);
//				} finally {
//					DB.close(rsLinePrl, pstmtLinePrl);
//					rsLinePrl = null;
//					pstmtLinePrl = null;
//				}
			
			
	}
	
	
}
