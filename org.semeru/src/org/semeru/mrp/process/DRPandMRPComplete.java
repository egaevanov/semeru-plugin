package org.semeru.mrp.process;

import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.logging.Level;

import org.compiere.model.MDocType;
import org.compiere.model.MInOut;
import org.compiere.model.MInOutLine;
import org.compiere.model.MOrder;
import org.compiere.model.MOrderLine;
import org.compiere.model.MProduction;
import org.compiere.model.MTable;
import org.compiere.process.ProcessInfoParameter;
import org.compiere.process.SvrProcess;
import org.compiere.util.CLogger;
import org.compiere.util.DB;
import org.compiere.util.Env;
import org.semeru.mrp.model.X_SM_DRP;
import org.semeru.mrp.model.X_SM_DRPLine;
import org.semeru.mrp.model.X_SM_MRP;
import org.semeru.mrp.model.X_SM_MRPLine;

public class DRPandMRPComplete extends SvrProcess {
	
	private int p_Record_ID = 0;
	private int p_AD_Table_ID = 0;
	private String p_DocAction = "";

	private X_SM_DRP DRP = null;
	private X_SM_MRP MRP = null;
	MInOut shipment = null;
	public static CLogger log = CLogger.getCLogger(DRPandMRPComplete.class);

	
	@Override
	protected void prepare() {

		
		ProcessInfoParameter[] para = getParameter();

		for (int i = 0 ; i < para.length ;i++){
			
			String name = para[i].getParameterName();
			
			if(para[i].getParameter()==null)
				;
			else if(name.equals("DocStatus"))
				p_DocAction = (String)para[i].getParameterAsString();
		
			else
				log.log(Level.SEVERE, "Unknown Parameter: " + name);
			
		}
		
		p_Record_ID = getRecord_ID();
		p_AD_Table_ID = getTable_ID();


	}

	@Override
	protected String doIt() throws Exception {

		if(p_Record_ID > 0) {
			
			MTable table  = new MTable(getCtx(), p_AD_Table_ID, get_TrxName());
			
			
			if(table.getTableName().equals(X_SM_DRP.Table_Name)) {
				DRP = new X_SM_DRP(getCtx(), p_Record_ID, get_TrxName());
				
				ArrayList<Integer> Ref_ID_List = new ArrayList<>();

				if(p_DocAction != null && p_DocAction != "" && !p_DocAction.isEmpty()) {
					
					StringBuilder getRef_ID = new StringBuilder();
					getRef_ID.append(" SELECT DISTINCT C_Order_ID ");
					getRef_ID.append(" FROM SM_DRPLine ");
					getRef_ID.append(" WHERE SM_DRP_ID ="+DRP.getSM_DRP_ID());
					
					PreparedStatement pstmt = null;
			     	ResultSet rs = null;
						try {
							pstmt = DB.prepareStatement(getRef_ID.toString(), null);
							rs = pstmt.executeQuery();
							while (rs.next()) {
								int Ref_ID = rs.getInt(1);

								Ref_ID_List.add(Ref_ID);
						    	   			 				  
							}

						} catch (SQLException err) {
							log.log(Level.SEVERE, getRef_ID.toString(), err);
						} finally {
							DB.close(rs, pstmt);
							rs = null;
							pstmt = null;
						}
					
					
					for (int i = 0 ; i< Ref_ID_List.size() ; i++) {
						
						MOrder Ord = new MOrder(getCtx(), Ref_ID_List.get(i), get_TrxName());
						MDocType dt = MDocType.get(Env.getCtx(), Ord.getC_DocType_ID());

						MInOut shipment = new MInOut (Ord, dt.getC_DocTypeShipment_ID(), DRP.getMovementDate());
						shipment.saveEx();
						
						StringBuilder getRefLine_ID = new StringBuilder();
						getRefLine_ID.append(" SELECT SM_DRPLine_ID,C_OrderLine_ID ");
						getRefLine_ID.append(" FROM SM_DRPLine ");
						getRefLine_ID.append(" WHERE C_Order_ID ="+Ref_ID_List.get(i));
						

						PreparedStatement pstmtLine = null;
				     	ResultSet rsLine = null;
							try {
								pstmtLine = DB.prepareStatement(getRefLine_ID.toString(), null);
								rsLine = pstmtLine.executeQuery();
								while (rsLine.next()) {

									X_SM_DRPLine DRPLine = new X_SM_DRPLine(getCtx(), rsLine.getInt(1), get_TrxName());
									MOrderLine OrdLine = new MOrderLine(getCtx(), rsLine.getInt(2), get_TrxName()); 

									MInOutLine ioLine = new MInOutLine(shipment);
									BigDecimal MovementQty = DRPLine.getQty(); 
									
									ioLine.setC_OrderLine_ID(OrdLine.getC_OrderLine_ID());
									ioLine.setLine(OrdLine.getLine());
									ioLine.setC_UOM_ID(OrdLine.getC_UOM_ID());
									ioLine.setM_Product_ID(DRPLine.getM_Product_ID());
									ioLine.setDescription(DRPLine.getDescription());
									ioLine.setC_Project_ID(OrdLine.getC_Project_ID());
									ioLine.setC_ProjectPhase_ID(OrdLine.getC_ProjectPhase_ID());
									ioLine.setC_ProjectTask_ID(OrdLine.getC_ProjectTask_ID());
									ioLine.setC_Activity_ID(OrdLine.getC_Activity_ID());
									ioLine.setC_Campaign_ID(OrdLine.getC_Campaign_ID());
									ioLine.setM_Locator_ID(DRPLine.getM_Locator_ID());
									ioLine.setQty(MovementQty);
									ioLine.setQtyEntered(MovementQty);
									ioLine.saveEx();
									
									DRPLine.set_CustomColumn("M_InOutLine_ID", ioLine.getM_InOutLine_ID());
									DRPLine.saveEx();
									
									if(p_DocAction.equals("CO")) {
										
										DRPLine.setProcessed(true);
										DRPLine.saveEx();

								
									}
							    	   			 				  
								}

							} catch (SQLException err) {
								log.log(Level.SEVERE, getRefLine_ID.toString(), err);
							} finally {
								DB.close(rsLine, pstmtLine);
								rsLine = null;
								pstmtLine = null;
							}
						
						
						
					}
						
						
					DRP.setDocStatus(p_DocAction);

										
					
					if(p_DocAction.equals("CO")) {
						
						DRP.setProcessed(true);
				
					}else {
						DRP.setProcessed(false);

					}
					DRP.saveEx();
				}
				
				
			
			}else if(table.getTableName().equals(X_SM_MRP.Table_Name)) {
				MRP = new X_SM_MRP(getCtx(), p_Record_ID, get_TrxName());
				ArrayList<Integer> MRPLine_ID_List = new ArrayList<>();

				
				if(p_DocAction != null && p_DocAction != "" && !p_DocAction.isEmpty()) {
					
					
					StringBuilder getRef_ID = new StringBuilder();
					getRef_ID.append(" SELECT SM_MRPLine_ID ");
					getRef_ID.append(" FROM SM_MRPLine ");
					getRef_ID.append(" WHERE SM_MRP_ID ="+MRP.getSM_MRP_ID());
					
					PreparedStatement pstmt = null;
			     	ResultSet rs = null;
						try {
							pstmt = DB.prepareStatement(getRef_ID.toString(), null);
							rs = pstmt.executeQuery();
							while (rs.next()) {
								int SM_MRPLine_ID = rs.getInt(1);

								X_SM_MRPLine MRPLine = new X_SM_MRPLine(getCtx(), SM_MRPLine_ID, get_TrxName());
								
								MRPLine_ID_List.add(MRPLine.getSM_MRPLine_ID());
								
								
							}

						} catch (SQLException err) {
							log.log(Level.SEVERE, getRef_ID.toString(), err);
						} finally {
							DB.close(rs, pstmt);
							rs = null;
							pstmt = null;
						}
					
						
						for (int i = 0 ; i< MRPLine_ID_List.size() ; i++) {
							
							X_SM_MRPLine MRPLine = new X_SM_MRPLine(getCtx(), MRPLine_ID_List.get(i), get_TrxName());
							MProduction production = new MProduction(getCtx(), 0, get_TrxName());
														
//							MProduct prod = new MProduct(getCtx(), MRPLine.getM_Product_ID(), get_TrxName());
//							production.setAD_Org_ID(MRP.getAD_Org_ID());
//							production.setName("Produksi : "+prod.getName());
//							production.setDescription(prod.getName());
//							production.setMovementDate(MRP.getMovementDate());
//							production.setM_Product_ID(prod.getM_Product_ID());
//							production.setM_Locator_ID(MRPLine.getM_Locator_ID());
//							production.setProductionQty(MRPLine.getQty());
//							production.setC_OrderLine_ID(MRPLine.get_ValueAsInt("C_OrderLine_ID"));
//							production.saveEx();	
							
							MRPLine.set_CustomColumn("M_Production_ID", production.getM_Production_ID());
							MRPLine.saveEx();
							
//							StringBuilder getBOM = new StringBuilder();
//							getBOM.append(" SELECT M_Product_BOM_ID ");
//							getBOM.append(" FROM M_Product_BOM ");
//							getBOM.append(" WHERE M_Product_ID ="+MRPLine.getM_Product_ID());
//							
//							int count = 10;
//							PreparedStatement pstmtBOM = null;
//					     	ResultSet rsBOM = null;
//								try {
//									pstmtBOM = DB.prepareStatement(getRef_ID.toString(), null);
//									rsBOM = pstmtBOM.executeQuery();
//									while (rsBOM.next()) {
//										int M_Product_BOM_ID = rsBOM.getInt(1);
//
//										MProductionLine prodLine = new MProductionLine(getCtx(), 0, get_TrxName());
//										MProductBOM BOM = new MProductBOM(getCtx(), M_Product_BOM_ID, get_TrxName());
//										
//										prodLine.setAD_Org_ID(production.getAD_Org_ID());
//										prodLine.setM_Production_ID(production.getM_Production_ID());
//										prodLine.setLine(count);
//										prodLine.setM_Product_ID(BOM.getM_Product_ID());
//										
//										prodLine.saveEx();
//										
//										count++;
//									}
//
//								} catch (SQLException err) {
//									log.log(Level.SEVERE, getRef_ID.toString(), err);
//								} finally {
//									DB.close(rsBOM, pstmtBOM);
//									rsBOM = null;
//									pstmtBOM = null;
//								}

							if(p_DocAction.equals("CO")) {
								
								MRPLine.setProcessed(true);
								MRPLine.saveEx();
							
							}
							
							
						}

				
					//MRP.setDocStatus(p_DocAction);
					if(p_DocAction.equals("CO")) {
						
						MRP.setProcessed(true);

				
					}else {
						MRP.setProcessed(false);

					}
					MRP.saveEx();
				}
				
				


			}
			
		}
		
		
		return "";
	}
	
}
