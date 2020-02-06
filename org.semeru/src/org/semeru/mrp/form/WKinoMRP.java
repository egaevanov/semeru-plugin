package org.semeru.mrp.form;

import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Properties;
import java.util.Vector;
import java.util.logging.Level;

import org.adempiere.util.ProcessUtil;
import org.adempiere.webui.component.Button;
import org.adempiere.webui.component.Grid;
import org.adempiere.webui.component.GridFactory;
import org.adempiere.webui.component.Label;
import org.adempiere.webui.component.ListModelTable;
import org.adempiere.webui.component.ListboxFactory;
import org.adempiere.webui.component.Panel;
import org.adempiere.webui.component.Row;
import org.adempiere.webui.component.Rows;
import org.adempiere.webui.component.Tab;
import org.adempiere.webui.component.Tabpanel;
import org.adempiere.webui.component.Tabs;
import org.adempiere.webui.component.WListbox;
import org.adempiere.webui.editor.WDateEditor;
import org.adempiere.webui.editor.WSearchEditor;
import org.adempiere.webui.editor.WTableDirEditor;
import org.adempiere.webui.event.ValueChangeEvent;
import org.adempiere.webui.event.ValueChangeListener;
import org.adempiere.webui.event.WTableModelEvent;
import org.adempiere.webui.event.WTableModelListener;
import org.adempiere.webui.panel.ADForm;
import org.adempiere.webui.panel.CustomForm;
import org.adempiere.webui.panel.IFormController;
import org.adempiere.webui.util.ZKUpdateUtil;
import org.adempiere.webui.window.FDialog;
import org.compiere.minigrid.IDColumn;
import org.compiere.minigrid.IMiniTable;
import org.compiere.model.MLookup;
import org.compiere.model.MLookupFactory;
import org.compiere.model.MPInstance;
import org.compiere.model.MProcess;
import org.compiere.model.MProduct;
import org.compiere.model.MProduction;
import org.compiere.model.MRequisition;
import org.compiere.model.MRequisitionLine;
import org.compiere.process.ProcessInfo;
import org.compiere.process.ProcessInfoParameter;
import org.compiere.util.CLogger;
import org.compiere.util.DB;
import org.compiere.util.DisplayType;
import org.compiere.util.Env;
import org.compiere.util.KeyNamePair;
import org.compiere.util.Msg;
import org.compiere.util.Trx;
import org.semeru.mrp.model.X_SM_MPSLine;
import org.semeru.mrp.model.X_SM_MRP;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zul.Borderlayout;
import org.zkoss.zul.Caption;
import org.zkoss.zul.Center;
import org.zkoss.zul.Groupbox;
import org.zkoss.zul.Hbox;
import org.zkoss.zul.Hlayout;
import org.zkoss.zul.Listfoot;
import org.zkoss.zul.North;
import org.zkoss.zul.Space;
import org.zkoss.zul.Span;
import org.zkoss.zul.Vlayout;




public class WKinoMRP  extends SM_KinoMRP implements IFormController,EventListener<Event>, WTableModelListener, ValueChangeListener{

	public CLogger log = CLogger.getCLogger(WKinoMRP.class);

	/**
	 * 
	 */

	private CustomForm form = new CustomForm();
	
	
	
	/**********************************************************************
	 * UI Component
	 **********************************************************************/

	private Borderlayout mainLayout = new Borderlayout();
	
	private Tabpanel tabpanel = new Tabpanel();
	private Tabs tabs = new Tabs();
	private Tab tab = new Tab();

	private Panel parameterPanel = new Panel();						//Set field of search condition
	private Grid parameterLayout = GridFactory.newGridLayout();

	//Display Data
	private Panel displayDataPanel = new Panel();
	private Borderlayout displayDataLayout = new Borderlayout();
	
	
	
//	private Panel displayDataPanelSouth = new Panel();
//	private Borderlayout displayDataSouthLayout = new Borderlayout();
	
	
	private Hlayout HlayoutTagihan = new Hlayout();
	private Hlayout HlayoutSummary = new Hlayout();
//	private Hlayout HlayoutAngsuran = new Hlayout();
	private Vlayout Vlayout = new Vlayout();
//	private Vlayout VlayoutSouth = new Vlayout();


//	private Grid griddata = GridFactory.newGridLayout();



//	private Grid matrixGrid  = new Grid();			//main component
	
	
	//Label Component
	private Label Lbl_StartDate = new Label("Start Date");
	private Label Lbl_EndDate = new Label("End Date");
	private Label Lbl_MPS = new Label("MPS");
	private Label Lbl_Period = new Label("Period");
//	private Label Lbl_Product = new Label("Product");
	
	// DateBox Component
	private WDateEditor DB_StartDate = new WDateEditor();
	private WDateEditor DB_EndDate = new WDateEditor();
		
	//WTableDirEditor Component
	private WSearchEditor MPSSearch = null;
	private WTableDirEditor Period = null;

	
	//Button Component
	private Button Search = new Button();
	
	
	//private Button Btn_ZoomMPS = new Button();
	private Button Btn_UpdateMRPLine = new Button();
	private Button Btn_PrintMPS = new Button();
	private Button Btn_OrdRelease = new Button();
	private Button Btn_WorkOrd = new Button();
	private Button Btn_OrderFinished = new Button();
	private Button Btn_PrintMRP = new Button();

	
	//WListBox Component
	private WListbox tableMPSLine = ListboxFactory.newDataTable();
	private WListbox tableMRP = ListboxFactory.newDataTable();

	
	private Properties ctx = Env.getCtx();
	

	
	public WKinoMRP() {	
		
		try{
			
			boolean IsOK = dyinit();

			if (!IsOK) {
				return;
			}
			
			zkInit();
			
		}catch(Exception e){
			
			log.log(Level.SEVERE, "", e);
		
		}	
		
	}		
	
	

	@Override
	public void tableChanged(WTableModelEvent event) {

		boolean isUpdate = (event.getType() == WTableModelEvent.CONTENTS_CHANGED);

		
		if (!isUpdate) {
			return;
		} else if (isUpdate) {
			
			System.out.println(tableMPSLine.getSelectedItem());
			int index = event.getIndex0();
			int SM_MPSLine_ID = 0;
			
			if(index >= 0) {
				KeyNamePair kp = (KeyNamePair) tableMPSLine.getValueAt(index, 9);
				SM_MPSLine_ID = kp.getKey();
			}
			
			
			if(SM_MPSLine_ID <= 0) {
			
				return;
			}

			Vector<Vector<Object>> data = getdataMRPLine(SM_MPSLine_ID);
			Vector<String> columnNames = getOISColumnNamesTableMRPLine();

			tableMRP.clear();

			// Set Model
			ListModelTable modelP = new ListModelTable(data);
			modelP.addTableModelListener(this);
			tableMRP.setData(modelP, columnNames);
			configureTableMRPLine(tableMRP);
			
			
			
			
		}
	
	}

	@Override
	public void valueChange(ValueChangeEvent evt) {

//		String name = evt.getPropertyName();

//		Object value = evt.getNewValue();
//		Integer presales_ID = (Integer) noPresales.getValue();
//		Integer preOrder_ID = (Integer) noPreOrder.getValue();
		
		try {
			
		} catch (Exception e) {
			log.log(Level.SEVERE, this.getClass().getCanonicalName()
					+ ".valueChange - ERROR: " + e.getMessage(), e);		}
	
	}

	@Override
	public void onEvent(Event e) throws Exception {
		
		if (e.getTarget().equals(Search)) {
			Vector<Vector<Object>> data = new Vector<>();
			
			Integer SM_MPS_ID = (Integer) MPSSearch.getValue();
			
			if(SM_MPS_ID == null) {
				SM_MPS_ID = 0;
			}
			
			if(SM_MPS_ID <= 0) {
				
				FDialog.info(form.getWindowNo(),null,"","Kolom MPS Belum Di Isi","Info");
				return;
			}
			
			
			Integer C_Period_ID = (Integer) Period.getValue();
			
			if(C_Period_ID == null) {
				C_Period_ID = 0;
			}
			
			if(C_Period_ID <= 0) {
				
				FDialog.info(form.getWindowNo(),null,"","Kolom Period Belum Di Isi","Info");
				return;
			}

			
			Timestamp startDate = (Timestamp) DB_StartDate.getValue();
			Timestamp EndDate =  (Timestamp) DB_EndDate.getValue();

			
			data = getdataMPSLine(SM_MPS_ID,C_Period_ID,startDate,EndDate);
			Vector<String> columnNames = getOISColumnNamesTableMPSLine();

			tableMPSLine.clear();
			tableMPSLine.clearTable();

			// Set Model
			ListModelTable modelP = new ListModelTable(data);
			modelP.addTableModelListener(this);
			tableMPSLine.setData(modelP, columnNames);
			configureTableMPSLine(tableMPSLine);
			
		}
		else if(e.getTarget().equals(Btn_UpdateMRPLine)) {
			
			Integer SM_MPS_ID = (Integer) MPSSearch.getValue();
			
			
			int rowIndex = 0;
			for(int i = 0 ; i<tableMPSLine.getRowCount() ; i++) {
				boolean isselected = false;
				
				isselected = (boolean) tableMPSLine.getValueAt(i, 0);
				if(isselected) {
					rowIndex = i;
				}
				
				
			}
			
			KeyNamePair kp = (KeyNamePair) tableMPSLine.getValueAt(rowIndex, 9);
			int SM_MPSLine_ID = kp.getKey();
			
			if(SM_MPS_ID == null) {
				SM_MPS_ID = 0;
			}
			
			if(SM_MPS_ID > 0 ) {
				updateMRPLine(Env.getAD_Client_ID(ctx),Env.getAD_Org_ID(ctx),SM_MPS_ID,SM_MPSLine_ID);
			}
			
			FDialog.info(form.getWindowNo(),null,"","Process Update MRP Line Berhasil","Info");

			
			
		}
		else if(e.getTarget().equals(Btn_PrintMPS)) {
			
			String trxName = Trx.createTrxName("PrintMPS");
			
			
			Integer SM_MPS_ID = (Integer) MPSSearch.getValue();
			
			if(SM_MPS_ID == null) {
				SM_MPS_ID = 0;
			}
			
			if(SM_MPS_ID <=0) {
				return;
			}
			
			StringBuilder getURL = new StringBuilder();
			getURL.append("SELECT b.DefaultValue ");
			getURL.append(" FROM AD_Process a, AD_Process_Para b ");
			getURL.append(" WHERE a.AD_Process_ID = b.AD_Process_ID ");
			getURL.append(" AND a.AD_Process_ID = 1000014 ");
			getURL.append(" AND b.Name = 'url_path' ");		
			
			String url = DB.getSQLValueString(null, getURL.toString());
			
			MProcess proc = new MProcess(Env.getCtx(), 1000014, trxName);
			MPInstance instance = new MPInstance(proc, proc.getAD_Process_ID());
			ProcessInfo pi = new ProcessInfo("Print MPS", 1000014);
			pi.setAD_PInstance_ID(instance.getAD_PInstance_ID());
						
			ArrayList<ProcessInfoParameter> list = new ArrayList<ProcessInfoParameter>();
			list.add(new ProcessInfoParameter("SM_MPS_ID", SM_MPS_ID, null, null, null));
			list.add(new ProcessInfoParameter("url_path", url, null, null, null));

			ProcessInfoParameter[] pars = new ProcessInfoParameter[list.size()];
			list.toArray(pars);
			pi.setParameter(pars);
						
			Trx trx = Trx.get(trxName, true);
			trx.commit();

			ProcessUtil.startJavaProcess(Env.getCtx(), pi,Trx.get(trxName, true));
			
			
		}else if(e.getTarget().equals(Btn_OrdRelease)) {
			
			Integer SM_MPSLine_ID = 0;
//			Map<Timestamp, Integer> mrpDateDoc = new HashMap<Timestamp, Integer>();
//			Map<Timestamp, Integer> mrpDateReq = new HashMap<Timestamp, Integer>();
			ArrayList<Timestamp> dateReqList = new ArrayList<>();

			
			int rowIndex = 0;
			for(int i = 0 ; i<tableMPSLine.getRowCount() ; i++) {
				
				boolean isselected = false;
				
				isselected = (boolean) tableMPSLine.getValueAt(i, 0);
				
				if(isselected) {
					rowIndex = i;	
				}
				
			}
			
			KeyNamePair kp = (KeyNamePair) tableMPSLine.getValueAt(rowIndex, 9);
			SM_MPSLine_ID = kp.getKey();
			
		
			X_SM_MPSLine mpsLine = new X_SM_MPSLine(ctx, SM_MPSLine_ID, null);
			
			StringBuilder SQLGetDateData = new StringBuilder();
			SQLGetDateData.append("SELECT DISTINCT(mrl.mrpdate) ");
			SQLGetDateData.append(" FROM SM_MRP mr ");
			SQLGetDateData.append(" LEFT JOIN SM_MRPLine mrl ON mrl.SM_MRP_ID = mr.SM_MRP_ID");
			SQLGetDateData.append(" LEFT JOIN SM_MRPElement mre ON mre.SM_MRPElement_ID =mrl.SM_MRPElement_ID ");	
			SQLGetDateData.append(" LEFT JOIN M_Product mp ON mp.M_Product_ID = mr.M_Product_ID ");	
			SQLGetDateData.append(" LEFT JOIN sm_mpsline mpsl ON mr.sm_mpsline_id = mpsl.sm_mps_id AND mr.mpsseq = mpsl.seq ");		
			SQLGetDateData.append(" WHERE mp.ismanufactured = 'N'");
			SQLGetDateData.append(" AND mpsl.SM_MPSLine_ID = "+SM_MPSLine_ID);
			SQLGetDateData.append(" AND mre.value = 'porRec'");
			
			
			PreparedStatement pstmtGetDate = null;
	     	ResultSet rsGetDate = null;
				try {
					pstmtGetDate = DB.prepareStatement(SQLGetDateData.toString(), null);
					rsGetDate = pstmtGetDate.executeQuery();
					while (rsGetDate.next()) {
						
						dateReqList.add(rsGetDate.getTimestamp(1));
					
					}
					
				} catch (SQLException err) {
					log.log(Level.SEVERE, SQLGetDateData.toString(), err);
				} finally {
					DB.close(rsGetDate, pstmtGetDate);
					rsGetDate = null;
					pstmtGetDate = null;
				}

				
				StringBuilder SQLGetData = new StringBuilder();
				SQLGetData.append("SELECT b.m_product_id,a.docdate,b.datereq, a.sm_mrp_id,a.qty FROM ( ");
				SQLGetData.append(" SELECT mp.m_product_id,mrl.mrpdate as docdate,mr.SM_MRP_id,mrl.qty ");
				SQLGetData.append(" FROM SM_MRP mr");
				SQLGetData.append(" LEFT JOIN SM_MRPLine mrl ON mrl.SM_MRP_ID = mr.SM_MRP_ID ");
				SQLGetData.append(" LEFT JOIN SM_MRPElement mre ON mre.SM_MRPElement_ID =mrl.SM_MRPElement_ID");
				SQLGetData.append(" LEFT JOIN M_Product mp ON mp.M_Product_ID = mr.M_Product_ID");
				SQLGetData.append(" LEFT JOIN sm_mpsline mpsl ON mr.sm_mpsline_id = mpsl.sm_mps_id AND mr.mpsseq = mpsl.seq ");		
				SQLGetData.append(" WHERE mp.ismanufactured = 'N' ");
				SQLGetData.append(" AND mpsl.SM_MPSLine_ID = "+SM_MPSLine_ID);
				SQLGetData.append(" AND mre.value = 'porRel'");
				SQLGetData.append(" )a");
				SQLGetData.append(" INNER JOIN");
				SQLGetData.append(" (SELECT mp.m_product_id,mrl.mrpdate as datereq,mr.SM_MRP_id");
				SQLGetData.append(" FROM SM_MRP mr");
				SQLGetData.append(" LEFT JOIN SM_MRPLine mrl ON mrl.SM_MRP_ID = mr.SM_MRP_ID");
				SQLGetData.append(" LEFT JOIN SM_MRPElement mre ON mre.SM_MRPElement_ID =mrl.SM_MRPElement_ID");
				SQLGetData.append(" LEFT JOIN M_Product mp ON mp.M_Product_ID = mr.M_Product_ID");
				SQLGetData.append(" LEFT JOIN sm_mpsline mpsl ON mr.sm_mpsline_id = mpsl.sm_mps_id AND mr.mpsseq = mpsl.seq ");		
				SQLGetData.append(" WHERE mp.ismanufactured = 'N'");
				SQLGetData.append(" AND mpsl.SM_MPSLine_ID = "+SM_MPSLine_ID);
				SQLGetData.append(" AND mre.value = 'porRec') b ON a.SM_MRP_ID = b.SM_MRP_id");
				SQLGetData.append(" ORDER BY datereq");
				
				
				for (int i = 0 ; i<dateReqList.size();i++) {
				
				MRequisition requisition = new MRequisition(ctx, 0, null);
				requisition.setAD_Org_ID(mpsLine.getAD_Org_ID());
				requisition.setC_DocType_ID(1000206);
				requisition.setAD_User_ID(Env.getAD_User_ID(ctx));
				requisition.setDateRequired(dateReqList.get(i));
//				requisition.setDateDoc(new Timestamp(System.currentTimeMillis()));
				
				StringBuilder SQLGetWH = new StringBuilder();
				SQLGetWH.append("SELECT M_WareHouse_ID ");
				SQLGetWH.append(" FROM M_WareHouse ");
				SQLGetWH.append(" WHERE AD_Org_ID = "+mpsLine.getAD_Org_ID());
				Integer wh_ID = DB.getSQLValue(null, SQLGetWH.toString());
			
				requisition.setM_Warehouse_ID(wh_ID);
				requisition.setM_PriceList_ID(1000001);
				requisition.saveEx();
				
				PreparedStatement pstmt = null;
		     	ResultSet rs = null;
		     	
					try {
						pstmt = DB.prepareStatement(SQLGetData.toString(), null);
						rs = pstmt.executeQuery();
						while (rs.next()) {

							int m_product_id = rs.getInt(1);
							Timestamp dateDoc = rs.getTimestamp(2);
							Timestamp dateReq = rs.getTimestamp(3);
//							int sm_mrp_id = rs.getInt(4);
							BigDecimal qty = rs.getBigDecimal(5);
							int count = 10;
				
							if(dateReqList.get(i).equals(dateReq)) {
								
//								X_SM_MRP mrp = new X_SM_MRP(ctx, sm_mrp_id, null);
								MRequisitionLine reqLine = new MRequisitionLine(ctx, 0, null);
								reqLine.setAD_Org_ID(mpsLine.getAD_Org_ID());
								reqLine.setM_Requisition_ID(requisition.getM_Requisition_ID());
								reqLine.setLine(count);
								
								reqLine.setM_Product_ID(m_product_id);
								reqLine.setQty(qty);
								reqLine.saveEx();
								count = count+10;
								
								requisition.setDateDoc(dateDoc);
								
							}
							
							
						}
						
					} catch (SQLException err) {
						log.log(Level.SEVERE, SQLGetData.toString(), err);
					} finally {
						DB.close(rs, pstmt);
						rs = null;
						pstmt = null;
					}			
				
			requisition.saveEx();
				
					
			if(requisition != null) {	
			
				FDialog.info(form.getWindowNo(),null,"","Process Order Release Berhasil","Info");
			}
			
			}	
				
		}else if(e.getTarget().equals(Btn_WorkOrd)) {
			
			Integer SM_MPSLine_ID = 0;
			int rowIndex = 0;
			for(int i = 0 ; i<tableMPSLine.getRowCount() ; i++) {
				boolean isselected = false;
				
				isselected = (boolean) tableMPSLine.getValueAt(i, 0);
				if(isselected) {
					rowIndex = i;
				}
				
				
			}
			
			KeyNamePair kp = (KeyNamePair) tableMPSLine.getValueAt(rowIndex, 9);
			SM_MPSLine_ID = kp.getKey();
			
			if(SM_MPSLine_ID <=0) {
				return;
			}
			
			
			StringBuilder SQLGetData = new StringBuilder();
			SQLGetData.append("SELECT b.m_product_id,a.docdate,b.datereq, a.sm_mrp_id,a.qty FROM ( ");
			SQLGetData.append(" SELECT mp.m_product_id,mrl.mrpdate as docdate,mr.SM_MRP_id,mrl.qty ");
			SQLGetData.append(" FROM SM_MRP mr");
			SQLGetData.append(" LEFT JOIN SM_MRPLine mrl ON mrl.SM_MRP_ID = mr.SM_MRP_ID ");
			SQLGetData.append(" LEFT JOIN SM_MRPElement mre ON mre.SM_MRPElement_ID =mrl.SM_MRPElement_ID");
			SQLGetData.append(" LEFT JOIN M_Product mp ON mp.M_Product_ID = mr.M_Product_ID");
			SQLGetData.append(" LEFT JOIN sm_mpsline mpsl ON mr.sm_mpsline_id = mpsl.sm_mps_id AND mr.mpsseq = mpsl.seq ");
			SQLGetData.append(" WHERE mp.ismanufactured = 'Y' ");
			SQLGetData.append(" AND mpsl.SM_MPSLine_ID = "+SM_MPSLine_ID);
			SQLGetData.append(" AND mre.value = 'porRel'");
			SQLGetData.append(" )a");
			SQLGetData.append(" INNER JOIN");
			SQLGetData.append(" (SELECT mp.m_product_id,mrl.mrpdate as datereq,mr.SM_MRP_id");
			SQLGetData.append(" FROM SM_MRP mr");
			SQLGetData.append(" LEFT JOIN SM_MRPLine mrl ON mrl.SM_MRP_ID = mr.SM_MRP_ID");
			SQLGetData.append(" LEFT JOIN SM_MRPElement mre ON mre.SM_MRPElement_ID =mrl.SM_MRPElement_ID");
			SQLGetData.append(" LEFT JOIN M_Product mp ON mp.M_Product_ID = mr.M_Product_ID");
			SQLGetData.append(" LEFT JOIN sm_mpsline mpsl ON mr.sm_mpsline_id = mpsl.sm_mps_id AND mr.mpsseq = mpsl.seq ");
			SQLGetData.append(" WHERE mp.ismanufactured = 'Y'");
			SQLGetData.append(" AND mpsl.SM_MPSLine_ID = "+SM_MPSLine_ID);
			SQLGetData.append(" AND mre.value = 'porRec') b ON a.SM_MRP_ID = b.SM_MRP_id");
			SQLGetData.append(" ORDER BY datereq");
			
			
			PreparedStatement pstmt = null;
	     	ResultSet rs = null;
				try {
					pstmt = DB.prepareStatement(SQLGetData.toString(), null);
					rs = pstmt.executeQuery();
					while (rs.next()) {
						
						X_SM_MRP mrp = new X_SM_MRP(ctx, rs.getInt(4), null);

						MProduction production = new MProduction(Env.getCtx(), 0, null);
						MProduct prod = new MProduct(ctx, rs.getInt(1), null);
						
						production.setAD_Org_ID(mrp.getAD_Org_ID());
						production.setName("Produksi : "+prod.getName());
						production.setDescription(prod.getName());
						production.setMovementDate(rs.getTimestamp(2));
						production.setDatePromised(rs.getTimestamp(3));
						production.setM_Product_ID(prod.getM_Product_ID());
						production.setM_Locator_ID(1000004);
						production.setProductionQty(rs.getBigDecimal(5));
						production.saveEx();	
						
					}
					
				} catch (SQLException err) {
					log.log(Level.SEVERE, SQLGetData.toString(), err);
				} finally {
					DB.close(rs, pstmt);
					rs = null;
					pstmt = null;
				}

				
				
				FDialog.info(form.getWindowNo(),null,"","Process Work Order Berhasil","Info");

			
		}else if(e.getTarget().equals(Btn_OrderFinished)) {
			
			Integer SM_MPSLine_ID = 0;
			
			int rowIndex = 0;
			for(int i = 0 ; i<tableMPSLine.getRowCount() ; i++) {
				boolean isselected = false;
				
				isselected = (boolean) tableMPSLine.getValueAt(i, 0);
				if(isselected) {
					rowIndex = i;
				}
				
				
			}
			
			
			KeyNamePair kp = (KeyNamePair) tableMPSLine.getValueAt(rowIndex, 9);
			SM_MPSLine_ID = kp.getKey();
			
			if(SM_MPSLine_ID <=0) {
				return;
			}
			
			StringBuilder SQLUpdateMRPLine = new StringBuilder();
			SQLUpdateMRPLine.append("UPDATE SM_MRPLine ");
			SQLUpdateMRPLine.append(" SET Processed = 'Y' ");
			SQLUpdateMRPLine.append(" WHERE SM_MRP_ID IN (  ");
			SQLUpdateMRPLine.append(" SELECT mr.SM_MRP_ID FROM SM_MRP mr ");
			SQLUpdateMRPLine.append(" LEFT JOIN SM_MRPLine mrl ON mrl.SM_MRP_ID = mr.SM_MRP_ID");
			SQLUpdateMRPLine.append(" LEFT JOIN sm_mpsline mpsl ON mr.sm_mpsline_id = mpsl.sm_mps_id AND mr.mpsseq = mpsl.seq ");
			SQLUpdateMRPLine.append(" WHERE mpsl.SM_MPSLine_ID = "+SM_MPSLine_ID+")");
			
			DB.executeUpdate(SQLUpdateMRPLine.toString(),null);
			
			StringBuilder SQLUpdateMRP = new StringBuilder();
			SQLUpdateMRP.append("UPDATE SM_MRP ");
			SQLUpdateMRP.append(" SET Processed = 'Y' ");
			SQLUpdateMRP.append(" WHERE SM_MRP_ID IN (  ");
			SQLUpdateMRP.append(" SELECT mr.SM_MRP_ID FROM SM_MRP mr ");
			SQLUpdateMRP.append(" LEFT JOIN SM_MRPLine mrl ON mrl.SM_MRP_ID = mr.SM_MRP_ID");
			SQLUpdateMRP.append(" LEFT JOIN sm_mpsline mpsl ON mr.sm_mpsline_id = mpsl.sm_mps_id AND mr.mpsseq = mpsl.seq ");
			SQLUpdateMRP.append(" WHERE mpsl.SM_MPSLine_ID = "+SM_MPSLine_ID+")");
			
			DB.executeUpdate(SQLUpdateMRP.toString(),null);

			StringBuilder SQLUpdateMPSLine = new StringBuilder();
			SQLUpdateMPSLine.append("UPDATE SM_MPSLine ");
			SQLUpdateMPSLine.append(" SET Processed = 'Y' ");
			SQLUpdateMPSLine.append(" WHERE SM_MPSLine_ID =  "+SM_MPSLine_ID);
			
			DB.executeUpdate(SQLUpdateMPSLine.toString(),null);
			
			FDialog.info(form.getWindowNo(),null,"","Process Order Finished Berhasil","Info");
		
		}else if(e.getTarget().equals(Btn_PrintMRP)) {
			
			String trxName = Trx.createTrxName("PrintMRP");
			
			Integer SM_MPSLine_ID = 0;
			
			int rowIndex = 0;
			for(int i = 0 ; i<tableMPSLine.getRowCount() ; i++) {
				boolean isselected = false;
				
				isselected = (boolean) tableMPSLine.getValueAt(i, 0);
				if(isselected) {
					rowIndex = i;
				}
				
				
			}
			
			KeyNamePair kp = (KeyNamePair) tableMPSLine.getValueAt(rowIndex, 9);
			SM_MPSLine_ID = kp.getKey();
			
			if(SM_MPSLine_ID <=0) {
				return;
			}
			
			StringBuilder getURL = new StringBuilder();
			getURL.append("SELECT b.DefaultValue ");
			getURL.append(" FROM AD_Process a, AD_Process_Para b ");
			getURL.append(" WHERE a.AD_Process_ID = b.AD_Process_ID ");
			getURL.append(" AND a.AD_Process_ID = 1000015 ");
			getURL.append(" AND b.Name = 'url_path' ");		
			
			String url = "D:\\reports\\";
					//DB.getSQLValueString(null, getURL.toString());
			
			MProcess proc = new MProcess(Env.getCtx(), 1000015, trxName);
			MPInstance instance = new MPInstance(proc, proc.getAD_Process_ID());
			ProcessInfo pi = new ProcessInfo("Print MRP", 1000015);
			pi.setAD_PInstance_ID(instance.getAD_PInstance_ID());
						
			ArrayList<ProcessInfoParameter> list = new ArrayList<ProcessInfoParameter>();
			list.add(new ProcessInfoParameter("SM_MPSLine_ID", SM_MPSLine_ID, null, null, null));
			list.add(new ProcessInfoParameter("url_path", url, null, null, null));

			ProcessInfoParameter[] pars = new ProcessInfoParameter[list.size()];
			list.toArray(pars);
			pi.setParameter(pars);
						
			Trx trx = Trx.get(trxName, true);
			trx.commit();

			ProcessUtil.startJavaProcess(Env.getCtx(), pi,Trx.get(trxName, true));
			
		}
		
	}
	

	@Override
	public ADForm getForm() {
		return form;
	} 
	
	
	private void zkInit() throws Exception{
		//Tabpanel tabpanels = new Tabpanel();
		Label label = new Label("TAB 1");

		tab.setParent(tabs);
		tab.setLabel("GOGOGOGO");
		tab.setClosable(true);
		tab.setId("GOGO");
		tab.appendChild(label);		
		tabpanel.appendChild(form);	
		form.appendChild(mainLayout);
		ZKUpdateUtil.setHeight(form, "100%");

		/*Main Layout(Borderlayout)*/
		ZKUpdateUtil.setWidth(mainLayout, "100%");
		ZKUpdateUtil.setHeight(mainLayout, "100%");

		//Main Layout(Borderlayout)-North
		North north = new North();
		mainLayout.appendChild(north);
	
		//Search Parameter Panel
		north.appendChild(parameterPanel);
		north.setStyle("border: none");
		parameterPanel.appendChild(parameterLayout); 		//parameterLayout = Grid
		ZKUpdateUtil.setWidth(parameterLayout, "100%");
		Rows parameterLayoutRows = parameterLayout.newRows();
		Row row = null;

		row = parameterLayoutRows.newRow();
		Groupbox searchGB = new Groupbox();
		row.appendCellChild(searchGB,10);

		System.out.println(row.getZclass());

		searchGB.appendChild(new Caption(Msg.getMsg(Env.getCtx(), "Input Parameter Header")));
		Grid searchGrid  = new Grid();
		searchGrid.setStyle("background-color: #E9F0FF");
		searchGrid.setStyle("border: none");
		searchGB.appendChild(searchGrid);
		searchGrid.setHflex("1");
				
		Rows rows = searchGrid.newRows();
			
		
		//row 2			
		row = rows.newRow();
		Hbox hbox1 = new Hbox();
		row.appendCellChild(hbox1);

		hbox1.appendChild(Lbl_MPS.rightAlign());
		hbox1.appendChild(new Space());
		hbox1.appendChild(new Space());
		hbox1.appendChild(new Span());
		hbox1.appendChild(MPSSearch.getComponent());
		//MPSSearch.getComponent().setWidth("315px");
		
		row = rows.newRow();
		Hbox hbox2 = new Hbox();
		row.appendCellChild(hbox2);

		hbox2.appendChild(Lbl_Period.rightAlign());
		hbox2.appendChild(new Space());
		hbox2.appendChild(new Span());
		hbox2.appendChild(new Span());
		hbox2.appendChild(Period.getComponent());
		Period.getComponent().setWidth("315px");
		
		//row 1
		row = rows.newRow();
		Hbox hbox3 = new Hbox();
		row.appendCellChild(hbox3);
		hbox3.appendChild(Lbl_StartDate);
		hbox3.appendChild(DB_StartDate.getComponent());
		hbox3.appendChild(Lbl_EndDate);
		hbox3.appendChild(DB_EndDate.getComponent());
		//row.appendCellChild(Lbl_StartDate.rightAlign(),1);
		//row.appendCellChild(DB_StartDate.getComponent(),1);
		//DB_StartDate.getComponent().setHflex("true");

		//row.appendCellChild(Lbl_EndDate.rightAlign(),1);
		//row.appendCellChild(DB_EndDate.getComponent(),1);
		//DB_EndDate.getComponent().setHflex("true");

		row = rows.newRow();
		row.appendCellChild(Search);
		Search.setLabel("Search");
		Search.addActionListener(this);
		Search.setWidth("200px");

		//Edit Area
		Center center = new Center();
		center.setStyle("padding-top: 5px");
		mainLayout.appendChild(center);
		center.appendChild(displayDataPanel);
		displayDataPanel.appendChild(displayDataLayout);//Borderlayout
		ZKUpdateUtil.setWidth(displayDataPanel, "100%");
		ZKUpdateUtil.setHeight(displayDataPanel, "100%");
		ZKUpdateUtil.setWidth(displayDataLayout, "100%");
		ZKUpdateUtil.setHeight(displayDataLayout, "100%");
		displayDataLayout.setStyle("border: 1px");
		
		center = new Center();
		displayDataLayout.appendChild(center);
		center.appendChild(Vlayout);
		ZKUpdateUtil.setWidth(Vlayout, "100%");
		ZKUpdateUtil.setHeight(Vlayout, "100%");
		Vlayout.appendChild(tableMPSLine);
		tableMPSLine.setWidth("100%");
		tableMPSLine.appendChild(new Listfoot());
		
	
		
		Vlayout.appendChild(HlayoutTagihan);

		Vlayout.appendChild(tableMRP);
		Vlayout.appendChild(HlayoutSummary);
			
		
	//	HlayoutSummary.appendChild(Btn_ZoomMPS);
	//	Btn_ZoomMPS.setHflex("true");
	//	Btn_ZoomMPS.setLabel("Zoom to MPS");
	//	Btn_ZoomMPS.addActionListener(this);
		
		HlayoutSummary.appendChild(Btn_PrintMPS);
		Btn_PrintMPS.setHflex("true");
		Btn_PrintMPS.setLabel("Print MPS");
		Btn_PrintMPS.addActionListener(this);
		
		HlayoutSummary.appendChild(Btn_UpdateMRPLine);
		Btn_UpdateMRPLine.setHflex("true");
		Btn_UpdateMRPLine.setLabel("Calc MRP");
		Btn_UpdateMRPLine.addActionListener(this);
		

		HlayoutSummary.appendChild(Btn_PrintMRP);
		Btn_PrintMRP.setHflex("true");
		Btn_PrintMRP.setLabel("Print MRP");
		Btn_PrintMRP.addActionListener(this);
		
		
		HlayoutSummary.appendChild(Btn_OrdRelease);
		Btn_OrdRelease.setHflex("true");
		Btn_OrdRelease.setLabel("Order Release Process");
		Btn_OrdRelease.addActionListener(this);

		HlayoutSummary.appendChild(Btn_WorkOrd);
		Btn_WorkOrd.setHflex("true");
		Btn_WorkOrd.setLabel("Work Order Process");
		Btn_WorkOrd.addActionListener(this);


		HlayoutSummary.appendChild(Btn_OrderFinished);
		Btn_OrderFinished.setHflex("true");
		Btn_OrderFinished.setLabel("Order Finished");
		Btn_OrderFinished.addActionListener(this);


	}
	

protected void configureTableMPSLine(IMiniTable miniTable) {
		
		miniTable.setColumnClass(0, IDColumn.class, false);
		miniTable.setColumnClass(1, KeyNamePair.class, true); // 2-week
		miniTable.setColumnClass(2, BigDecimal.class, true); // 2-week
		miniTable.setColumnClass(3, Timestamp.class, true); // 3-day
		miniTable.setColumnClass(4, BigDecimal.class, true); // 4-Forecast
		miniTable.setColumnClass(5, BigDecimal.class, true); // 5-Cus Order
		miniTable.setColumnClass(6, BigDecimal.class, true); // 6-PAB
		miniTable.setColumnClass(7, BigDecimal.class, true); // 7-ATP
		miniTable.setColumnClass(8, BigDecimal.class, true); // 8-MPS
		miniTable.setColumnClass(9, KeyNamePair.class, true); // 9-DTF

		miniTable.autoSize();

}

protected Vector<String> getOISColumnNamesTableMPSLine() {
	
	Vector<String> columnNames = null;

		columnNames = new Vector<String>(9);
		columnNames.add("Select");
		columnNames.add("Product");
		columnNames.add("Week");
		columnNames.add("Day");
		columnNames.add("Forecast");
		columnNames.add("Customer Order");
		columnNames.add("PAB");
		columnNames.add("ATP");
		columnNames.add("MPS");
		columnNames.add("DTF");


	return columnNames;

}

	protected Vector<String> getOISColumnNamesTableMRPLine() {
	
		Vector<String> columnNames = null;
	
			columnNames = new Vector<String>(9);
			columnNames.add("BOM");
			columnNames.add("Lvl");
			columnNames.add("Gross Requirment");
			columnNames.add("Project On Hand");
			columnNames.add("Net Requirment");
			columnNames.add("Planned Order Receipt");
			columnNames.add("Planned Order Release");
			columnNames.add("Order Finished");

		return columnNames;

	}


	protected void configureTableMRPLine(IMiniTable miniTable) {
		
		miniTable.setColumnClass(0, KeyNamePair.class, true); //BOM
		miniTable.setColumnClass(1, Integer.class, true); // 2-lv
		miniTable.setColumnClass(2, BigDecimal.class, true); // 5-Gross Req
		miniTable.setColumnClass(3, BigDecimal.class, true); // 6-Project OH
		miniTable.setColumnClass(4, BigDecimal.class, true); // 7-Net
		miniTable.setColumnClass(5, BigDecimal.class, true); // 8-Plan Ord Receipt
		miniTable.setColumnClass(6, BigDecimal.class, true); // 9-Plan Ord release
		miniTable.setColumnClass(7, String.class, true); // 11-Ord Finish

	
		miniTable.autoSize();

	}

	
	private boolean dyinit() {
		boolean IsOK = true;

		MLookup MPSLookUp = MLookupFactory.get(ctx, form.getWindowNo(), 0,1001001, DisplayType.Search);
		MPSSearch = new WSearchEditor("SM_MPS_ID", true, false, true,MPSLookUp);
		MPSSearch.addValueChangeListener(this);
		MPSSearch.setMandatory(true);
		
		MLookup PeriodLookUp = MLookupFactory.get(ctx, form.getWindowNo(), 0,837, DisplayType.TableDir);
		Period = new WTableDirEditor("C_Period_ID", true, false, true,PeriodLookUp);
		Period.addValueChangeListener(this);
		Period.setMandatory(true);

		return IsOK;

	}
	
}
