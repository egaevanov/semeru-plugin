package org.semeru.pos;

import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Properties;
import java.util.Vector;
import java.util.logging.Level;

import org.adempiere.util.ProcessUtil;
import org.adempiere.webui.component.Button;
import org.adempiere.webui.component.Combobox;
import org.adempiere.webui.component.Grid;
import org.adempiere.webui.component.GridFactory;
import org.adempiere.webui.component.Label;
import org.adempiere.webui.component.ListModelTable;
import org.adempiere.webui.component.ListboxFactory;
import org.adempiere.webui.component.Panel;
import org.adempiere.webui.component.Row;
import org.adempiere.webui.component.Rows;
import org.adempiere.webui.component.Textbox;
import org.adempiere.webui.component.WListbox;
import org.adempiere.webui.editor.WDateEditor;
import org.adempiere.webui.editor.WSearchEditor;
import org.adempiere.webui.editor.WTableDirEditor;
import org.adempiere.webui.event.DialogEvents;
import org.adempiere.webui.event.ValueChangeEvent;
import org.adempiere.webui.event.ValueChangeListener;
import org.adempiere.webui.event.WTableModelEvent;
import org.adempiere.webui.event.WTableModelListener;
import org.adempiere.webui.panel.ADForm;
import org.adempiere.webui.panel.CustomForm;
import org.adempiere.webui.panel.IFormController;
import org.adempiere.webui.window.FDialog;
import org.compiere.minigrid.IMiniTable;
import org.compiere.model.MDocType;
import org.compiere.model.MInOut;
import org.compiere.model.MInOutLine;
import org.compiere.model.MInvoice;
import org.compiere.model.MInvoiceLine;
import org.compiere.model.MLocator;
import org.compiere.model.MLookup;
import org.compiere.model.MLookupFactory;
import org.compiere.model.MOrder;
import org.compiere.model.MOrderLine;
import org.compiere.model.MPInstance;
import org.compiere.model.MPriceList;
import org.compiere.model.MProcess;
import org.compiere.model.MProduct;
import org.compiere.model.MRMA;
import org.compiere.model.MRMALine;
import org.compiere.process.ProcessInfo;
import org.compiere.process.ProcessInfoParameter;
import org.compiere.util.DB;
import org.compiere.util.DisplayType;
import org.compiere.util.Env;
import org.compiere.util.KeyNamePair;
import org.compiere.util.Msg;
import org.compiere.util.Trx;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zul.Borderlayout;
import org.zkoss.zul.Center;
import org.zkoss.zul.Hbox;
import org.zkoss.zul.North;
import org.zkoss.zul.South;
import org.zkoss.zul.Space;

/**
 * 
 * @author Tegar N
 * 
 * Semeru Refund Pembelian
 * 
 */

public class WSemeruRefundPembelian extends SemeruRefundPembelian implements IFormController,
EventListener<Event>, WTableModelListener, ValueChangeListener  {
	
	// CustomForm
	private CustomForm form = new CustomForm();
	
	// BorderLayout
	private Borderlayout mainLayout = new Borderlayout();
	private Borderlayout infoLayout = new Borderlayout();
	private Borderlayout summaryLayout = new Borderlayout();

	// Panel
	private Panel parameterPanel = new Panel();
	private Panel infoPanel = new Panel();
	private Panel southPanel = new Panel();

	// Grid
	private Grid summaryGrid = GridFactory.newGridLayout();
	private Grid parameterGrid = GridFactory.newGridLayout();

	// Toko
	private Label clientLabel = new Label("Toko :");
	private WTableDirEditor clientSearch = null;
	
	// Org
	private Label orgLabel = new Label("Cabang :");
	private WTableDirEditor org = null;
	
	//Nama Return
	private Label namaReturnLabel = new Label("Nama Return :");
	private Textbox namaReturnField = new Textbox();
	
	//Gudang
	private Label locatorLabel = new Label("Lokasi :");
	private WTableDirEditor locatorSearch = null;
	
	//Tipe Dokumen
	private Label tipeDokumenLabel = new Label("Tipe Dokumen :");
	private Combobox tipeDokumenField = new Combobox();
	
	//Tipe RMA
	private Label tipeRMALabel = new Label("Tipe RMA :");
	private WTableDirEditor tipeRMAField = null;
	
	// Tanggal Return
	private Label dateReturnLabel = new Label("Tanggal Perpindahan :");
	private WDateEditor dateReturnField = new WDateEditor();
	
	// Nomor Surat Jalan
	private Label noPenerimaanLabel = new Label("Penerimaan Barang :");
	private WSearchEditor noPenerimaanField = null;
								
	//Keterangan
	private Label ketLabel = new Label("Keterangan :");
	private Textbox ketField = new Textbox();
			
	//No RMA
	private Label noRMALabel = new Label("No Vendor RMA :");
	private Textbox noRMAField = new Textbox();
	
	//No Return
	private Label noReturnLabel = new Label("No Vendor Return :");
	private Textbox noReturnField = new Textbox();
	
	//No Credit Memo
	private Label noCreditMemoLabel = new Label("No AP Credit Memo :");
	private Textbox noCreditMemoField = new Textbox();
	
	//Proses Return
	private Button newReturn = new Button();
	
	//Proses Return
	private Button processReturn = new Button();
	
	//Print Return
	private Button printReturn = new Button();
	
	//Table Return
	private WListbox ReturnTable = ListboxFactory.newDataTable();

	//variable
	private Properties ctx = Env.getCtx();
	private int AD_Client_ID  = Env.getAD_Client_ID(ctx);
	private int windowNo = form.getWindowNo();
	private int M_InOut_Print_ID = 0;
	
	
	public WSemeruRefundPembelian() {
		dyInit();
		init();	
	}
	
	private void dyInit() {
		
		//Lookup Client
		MLookup lookupClient = MLookupFactory.get(ctx, form.getWindowNo(), 0,14621, DisplayType.TableDir);
		clientSearch = new WTableDirEditor("AD_Table_ID", true,false, true, lookupClient);
		clientSearch.addValueChangeListener(this);
		clientSearch.setMandatory(true);
		clientSearch.setValue(AD_Client_ID);
		
		//Lookup Org
		MLookup orgLookup = MLookupFactory.get(ctx, form.getWindowNo(), 0,2163, DisplayType.TableDir);
		org = new WTableDirEditor("AD_Org_ID", true, false, true, orgLookup);
		org.addValueChangeListener(this);
		org.setMandatory(true);
		

		MLookup lookupLocator = MLookupFactory.get(ctx, form.getWindowNo(), 0,1389, DisplayType.TableDir);
		locatorSearch = new WTableDirEditor("M_Locator_ID", true, false, true,lookupLocator);
		locatorSearch.addValueChangeListener(this);
		locatorSearch.setMandatory(true);
		
		//Lookup Tipe Dokumen 12118
		//MLookupInfo lookupInfoDocType = MLookupFactory.getLookupInfo(ctx, form.getWindowNo(), 12118, DisplayType.TableDir, Env.getLanguage(ctx), "C_DocType_ID", 321, false,"");

		
		ArrayList<KeyNamePair> list = loadDocType();
		tipeDokumenField.removeAllItems();
		for (KeyNamePair docType : list)
		tipeDokumenField.appendItem(docType.getName());
		tipeDokumenField.setSelectedIndex(0);
		tipeDokumenField.addEventListener(0, "onChange", this);
		
		//Lookup Tipe RMA - 12142
		MLookup lookupRMAType = MLookupFactory.get(ctx, form.getWindowNo(),0, 12142, DisplayType.TableDir);
		tipeRMAField = new WTableDirEditor("M_RMAType_ID", true, false,true, lookupRMAType);
		tipeRMAField.addValueChangeListener(this);
		tipeRMAField.setMandatory(true);
		
		//Lookup Penerimaan Barang-3521
		MLookup lookupPenerimaan = MLookupFactory.get(ctx, form.getWindowNo(),0, 3521, DisplayType.TableDir);
		noPenerimaanField = new WSearchEditor("M_InOut_ID", true, false,true, lookupPenerimaan);
		noPenerimaanField.addValueChangeListener(this);
		noPenerimaanField.setMandatory(true);
		
		// Date set to Login Date
		Calendar cal = Calendar.getInstance();
		cal.setTime(Env.getContextAsDate(ctx, "#Date"));
		cal.set(Calendar.HOUR_OF_DAY, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);
		cal.set(Calendar.MILLISECOND, 0);
		dateReturnField.setValue(new Timestamp(cal.getTimeInMillis()));
		dateReturnField.addValueChangeListener(this);
		
		initialUI();
		
	
	}

	
	private void initialUI() {
		
		namaReturnField.setReadonly(true);
		org.setReadWrite(false);
		locatorSearch.setReadWrite(false);
		tipeDokumenField.setEnabled(false);
		tipeRMAField.setReadWrite(false);
		dateReturnField.setReadWrite(false);
		noPenerimaanField.setReadWrite(false);
		ketField.setEnabled(false);
		ketField.setReadonly(true);
		processReturn.setEnabled(false);
		printReturn.setEnabled(false);
		
		
	}
	
	private void init() {
		
		form.appendChild(mainLayout);
		
		form.addEventListener(DialogEvents.ON_WINDOW_CLOSE, new EventListener<Event>(){
	
			@Override
			public void onEvent(Event arg0) throws Exception {
				form.dispose();
			}
				
		});
		
		mainLayout.setWidth("99%");
		mainLayout.setHeight("100%");
		
		North north = new North();
		mainLayout.appendChild(north);
		
		String grid = "border: 1px solid #C0C0C0; border-radius:5px;";
		north.appendChild(parameterPanel);
		north.setStyle(grid);
		
		parameterPanel.appendChild(parameterGrid);
		parameterGrid.setWidth("100%");
		parameterGrid.setStyle("Height:28%;");
		//parameterGrid.setHflex("min");
	
		Rows rows = null;
		Row row = null;
	
		rows = parameterGrid.newRows();
		
		// Toko
		row = rows.newRow();
		row.appendCellChild(clientLabel.rightAlign(), 1);
		row.appendCellChild(clientSearch.getComponent(), 1);
		clientSearch.getComponent().setHflex("true");
		clientSearch.setReadWrite(false);
		
		// Cabang
		row.appendCellChild(orgLabel.rightAlign(), 1);
		row.appendCellChild(org.getComponent(), 1);
		org.getComponent().setHflex("true");
		row.appendCellChild(new Space(), 1);
		
		//Nama Return
		row = rows.newRow();
		row.appendCellChild(namaReturnLabel.rightAlign(),1);
		row.appendCellChild(namaReturnField);
		namaReturnField.setHflex("true");

		//Gudang
		row.appendCellChild(locatorLabel.rightAlign(),1);
		row.appendCellChild(locatorSearch.getComponent(),1);
		locatorSearch.getComponent().setHflex("true");
		
		//Tipe Dokumen
		row = rows.newRow();
		row.appendCellChild(tipeDokumenLabel.rightAlign(),1);
		row.appendCellChild(tipeDokumenField,1);
		tipeDokumenField.setHflex("true");
		tipeDokumenField.setHeight("24px");
		tipeDokumenField.setStyle("border-radius:3px;");
		
		//Tipe RMA
		row.appendCellChild(tipeRMALabel.rightAlign());
		row.appendCellChild(tipeRMAField.getComponent(),1);
		tipeRMAField.getComponent().setHflex("true");
		
		//Tanggal Return
		row = rows.newRow();
		row.appendCellChild(dateReturnLabel.rightAlign());
		row.appendChild(dateReturnField.getComponent());
		dateReturnField.getComponent().setHflex("true");
		
		//Tipe Dokumen
		row.appendCellChild(noPenerimaanLabel.rightAlign());
		row.appendCellChild(noPenerimaanField.getComponent(),1);
		noPenerimaanField.getComponent().setHflex("true");

		// ket
		row = rows.newRow();
		row.appendCellChild(ketLabel.rightAlign(), 1);
		row.appendCellChild(ketField, 1);
		ketField.setRows(2);
		ketField.setHeight("100%");
		ketField.setHflex("1");
		row.appendCellChild(new Space(), 1);
		
		row.appendCellChild(newReturn, 1);
		newReturn.setLabel("Pengembalian Baru");
		newReturn.addActionListener(this);
		newReturn.setHflex("true");
		
		// SouthPanel
		South south = new South();
		mainLayout.appendChild(south);
		south.setStyle(grid);
		south.appendChild(southPanel);
		southPanel.appendChild(summaryGrid);
		
		Rows southRows = null;
		Row southRow = null;
	
		southRows = summaryGrid.newRows();
		summaryGrid.setStyle("Height:50%;");
	
		southRow = southRows.newRow();
		southRow.appendCellChild( new Space(),3);
		
		Hbox southHBox = new Hbox();
		southRow.appendCellChild(southHBox , 2);
		southHBox.setHflex("true");
		southHBox.setAlign("right");
		southHBox.appendChild(processReturn);
		processReturn.setLabel("Proses Pengembalian");
		processReturn.addActionListener(this);
		processReturn.setHflex("true");
		
		southHBox.appendChild(printReturn);
		printReturn.setLabel("Cetak Return");
		printReturn.addActionListener(this);	
		printReturn.setHflex("true");
		
		southRow = southRows.newRow();
		southRow.appendCellChild(noRMALabel.rightAlign(), 1);
		southRow.appendCellChild(noRMAField, 1);
		noRMAField.setHflex("true");
		noRMAField.setReadonly(true);
		southRow.appendCellChild(new Space(), 3);
		
		southRow = southRows.newRow();
		southRow.appendCellChild(noReturnLabel.rightAlign(), 1);
		southRow.appendCellChild(noReturnField, 1);
		noReturnField.setHflex("true");
		noReturnField.setReadonly(true);
		southRow.appendCellChild(new Space(), 3);
		
		southRow = southRows.newRow();
		southRow.appendCellChild(noCreditMemoLabel.rightAlign(), 1);
		southRow.appendCellChild(noCreditMemoField, 1);
		noCreditMemoField.setHflex("true");
		noCreditMemoField.setReadonly(true);
		southRow.appendCellChild(new Space(), 3);
		
		south = new South();
		infoPanel.appendChild(summaryLayout);
		summaryLayout.appendChild(south);
		infoPanel.setWidth("100%");
		infoPanel.setHeight("100%");
		summaryLayout.setWidth("100%");
		summaryLayout.setHeight("100%");
		
		Center center = new Center();
		summaryLayout.appendChild(center);
		center.appendChild(ReturnTable);
		ReturnTable.setWidth("100%");
		ReturnTable.setHeight("100%");
		center.setStyle(grid);
	
		center = new Center();
		mainLayout.appendChild(center);
		center.appendChild(infoLayout);
		infoLayout.setHflex("1");
		infoLayout.setVflex("1");
		infoLayout.setWidth("100%");
		infoLayout.setHeight("100%");
	
		north = new North();
		north.setHeight("100%");
		infoLayout.appendChild(north);
		north.appendChild(infoPanel);
		north.setSplittable(true);
		center = new Center();
		infoLayout.appendChild(center);
		
	}


	@Override
	public void valueChange(ValueChangeEvent e) {
		
		try {
			
			String name = e.getPropertyName();
			Object value = e.getNewValue();
			
			if (value == null){
				return;
			}
			
			
			if (name.equals("M_InOut_ID")){
				ReturnTable.clear();

				StringBuilder sqlReceipt = new StringBuilder();
				Integer noPenerimaan = Integer.valueOf(value.toString());
				
				if(noPenerimaan == null){
					noPenerimaan = 0;
					return;
				}
				
				
				sqlReceipt.append("SELECT M_InOut_ID ");
				sqlReceipt.append("FROM M_InOut mi ");
				sqlReceipt.append("WHERE mi.AD_Client_ID = ? ");
				sqlReceipt.append(" AND mi.IsSOTrx = 'N' ");
				sqlReceipt.append(" AND mi.M_InOut_ID = ? ");

				
				int M_InOut_ID = DB.getSQLValueEx(ctx.toString(), sqlReceipt.toString(),new Object[]{AD_Client_ID,noPenerimaan});

				if (M_InOut_ID <= 0){
					FDialog.info(windowNo, null, "", "Dokumen Tidak Ada Atau Sudah Pernah DiBatalkan", "Info");
					return;
				}
				
				if(noPenerimaan > 0){
				
					ReturnTable.clearTable();
					Vector<Vector<Object>> data = new Vector<Vector<Object>>();
					
					//get detail					
					ArrayList<Integer> detailList = new ArrayList<Integer>();

					StringBuilder sqlShipLine = new StringBuilder();
					sqlShipLine.append("SELECT M_InOutLine_ID ");
					sqlShipLine.append("FROM M_InOutLine mil ");
					sqlShipLine.append("INNER JOIN M_InOut mi ON mi.M_Inout_ID = mil.M_InOut_ID ");
					sqlShipLine.append("WHERE mil.AD_Client_ID = ? ");
					sqlShipLine.append("AND mi.IsSOTrx = 'N' ");
					sqlShipLine.append("AND mi.M_InOut_ID = ? ");
					
					PreparedStatement pstmt = null;
					ResultSet rs = null;
					try {
						pstmt = DB.prepareStatement(sqlShipLine.toString(), null);
						pstmt.setInt(1, AD_Client_ID);
						pstmt.setInt(2, M_InOut_ID);


						rs = pstmt.executeQuery();
						while (rs.next()) {
							detailList.add(rs.getInt(1));
						}

					} catch (SQLException ex) {
						log.log(Level.SEVERE, sqlShipLine.toString(), ex);
					} finally {
						DB.close(rs, pstmt);
						rs = null;
						pstmt = null;
					}
					
					data = getTrxData(detailList, ReturnTable);
					Vector<String> columnNames = getOISColumnNames();

					ReturnTable.clear();

					// Set Model
					ListModelTable modelP = new ListModelTable(data);
					modelP.addTableModelListener(this);
					ReturnTable.setData(modelP, columnNames);
					configureMiniTable(ReturnTable);
									
				}
				
			}else if (name.equals("M_Locator_ID")){
				
				Integer M_Locator_ID= Integer.valueOf(value.toString());
				if(M_Locator_ID > 0){
					
					MLocator loc = new MLocator(ctx, M_Locator_ID, null);
					KeyNamePair kpLoc = new KeyNamePair(loc.getM_Locator_ID(),loc.getValue());

					for (int i = 0 ; i < ReturnTable.getRowCount() ; i++ ){
						boolean IsSelected = (boolean) ReturnTable.getValueAt(i, 0);
						if(IsSelected){
							
							ReturnTable.setValueAt(kpLoc, i, 4);
							
						}
						
						
					}
					
				}
				
			}

			
			
			
		} catch (Exception ex) {
			log.log(Level.SEVERE, this.getClass().getCanonicalName() 
			+ ".valueChange - ERROR: " + ex.getMessage(), ex);
		}
		
		
	}

	@Override
	public void tableChanged(WTableModelEvent e) {
		boolean isUpdate = (e.getType() == WTableModelEvent.CONTENTS_CHANGED);
		Integer rowIndex = null;

		if (!isUpdate) {
			return;
		}  else if (isUpdate) {
			rowIndex = e.getFirstRow();
			int col = e.getColumn();
			if (col ==6 ) {
				BigDecimal qty = (BigDecimal) ReturnTable.getValueAt(rowIndex, col);
				String convQty = qty.toString();
					
				if(convQty.length() > 22){
					String fixQtyStr = convQty.substring(0, 22);				
					BigDecimal fixQty = new BigDecimal(fixQtyStr);
					ReturnTable.setValueAt(fixQty, rowIndex, col);
						
				}
			}
		}
		
	}

	@Override
	public void onEvent(Event e) throws Exception {

		if(e.getTarget().equals(processReturn)){
			
			if(ReturnTable.getRowCount() == 0){
				return;
			}
			
			
			String validation = validation(ReturnTable);
			
			if(validation != ""){
				
				FDialog.info(windowNo, null, "", validation, "Info");
				return;
				
			}
			
			try {
				
				
				//get DocType
				ArrayList<KeyNamePair> list = loadDocType();
				KeyNamePair docPair = list.get(tipeDokumenField.getSelectedIndex());
				Integer C_DocType_ID = docPair.getKey();
				int M_InOut_ID = (int) noPenerimaanField.getValue();
				MInOut ret =null;
				MInvoice APCreditMemo = null;
				MInOut inOut = new MInOut(ctx, M_InOut_ID, null);
				MOrder order = new MOrder(ctx, inOut.getC_Order_ID(), null);
				
				
				//checkStock
				for(int i = 0; i < ReturnTable.getRowCount(); i++){
					String checkQty= "";
					boolean IsSelected = (boolean) ReturnTable.getValueAt(i, 0);
					
					if(IsSelected){
						KeyNamePair KPInoutLine = (KeyNamePair) ReturnTable.getValueAt(i, 1);
						int inoutLine_ID = KPInoutLine.getKey();
						MInOutLine lineChek = new MInOutLine(ctx, inoutLine_ID, null);
						KeyNamePair KPImei = (KeyNamePair) ReturnTable.getValueAt(i, 2);
						int imei_ID = KPImei.getKey();
						KeyNamePair KPLoc = (KeyNamePair) ReturnTable.getValueAt(i, 4);
						int loc_ID = KPLoc.getKey();
						BigDecimal qty = (BigDecimal) ReturnTable.getValueAt(i, 6);
						 	
						checkQty = checkOnhand(AD_Client_ID, inOut.getAD_Org_ID(), lineChek.getM_Product_ID(), loc_ID, imei_ID,qty);
						
						if(checkQty != "" && !checkQty.isEmpty()){
							FDialog.info(windowNo, null, "","Process Return Tidak Bisa dilakukan "+"\n"+ checkQty, "Info");
							return;
						}
					}
				}
				
				MRMA rma = new MRMA (ctx, 0, null);

				rma.setAD_Org_ID(inOut.getAD_Org_ID());
				rma.set_ValueNoCheck ("DocumentNo", null);
				rma.setDocStatus (MRMA.DOCSTATUS_Drafted);
				rma.setDocAction(MRMA.DOCACTION_Complete);
				rma.setC_DocType_ID (C_DocType_ID);
				rma.setC_BPartner_ID(order.getC_BPartner_ID());
				rma.setIsSOTrx(false);
				rma.setIsApproved (false);
				rma.setProcessed (false);
				rma.setProcessing(false);
				rma.set_ValueNoCheck ("M_WareHouse_ID", inOut.getM_Warehouse_ID());
				rma.setName(namaReturnField.getValue().toString());
				rma.setDescription(ketField.getValue().toString());
				rma.setSalesRep_ID(inOut.getSalesRep_ID());
				rma.setM_RMAType_ID((int) tipeRMAField.getValue());
				rma.setC_Order_ID(0);
				//	get Order/Shipment/Receipt link
				if (inOut.getC_Order_ID() != 0)
				{
					rma.setC_Order_ID(inOut.getC_Order_ID());
				}
				if (inOut.getM_InOut_ID() != 0)
				{
					rma.setM_InOut_ID(M_InOut_ID);
				}
				rma.saveEx();
				noRMAField.setValue(rma.getDocumentNo());

				for(int i = 0; i < ReturnTable.getRowCount(); i++){
					
					Boolean IsSelected = (Boolean) ReturnTable.getValueAt(i, 0);
					KeyNamePair InOutLine = (KeyNamePair) ReturnTable.getValueAt(i, 1);
					
					if(IsSelected){
						MOrderLine orderLine= null;
						MInOutLine inLine = new MInOutLine(ctx, InOutLine.getKey(), null);
						if(inLine.getC_OrderLine_ID() > 0){
							orderLine = new MOrderLine(ctx, inLine.getC_OrderLine_ID(), null);
						}
						MRMALine line = new MRMALine(ctx, 0, null);
						line.setAD_Org_ID(rma.getAD_Org_ID());
						line.setM_RMA_ID(rma.getM_RMA_ID());
						line.setDescription((String) ReturnTable.getValueAt(i, 3));
						line.setM_InOutLine_ID(inLine.getM_InOutLine_ID());
						line.setM_Product_ID(inLine.getM_Product_ID());
						line.setQty((BigDecimal) ReturnTable.getValueAt(i, 6));
						if(orderLine != null){
							line.setC_Tax_ID(orderLine.getC_Tax_ID());
						}
						line.saveEx();

					}
					
				}
					
				rma.processIt(MRMA.DOCACTION_Complete);
				rma.saveEx();

				ret = new MInOut(ctx, 0, null);
				StringBuilder SQLDocType = new StringBuilder();
				SQLDocType.append("SELECT C_DocType_ID ");
				SQLDocType.append("FROM  C_DocType ");
				SQLDocType.append("WHERE AD_Client_ID = ?");
				SQLDocType.append("AND DocBaseType = '" + MDocType.DOCBASETYPE_MaterialDelivery+ "' ");
				SQLDocType.append("AND IsSoTrx ='N' ");

				int C_DocTypeRet_ID = DB.getSQLValueEx(null, SQLDocType.toString(), AD_Client_ID);
					
				//ret.set_TrxName(trxName);
				ret.setAD_Org_ID(inOut.getAD_Org_ID());
				ret.setDocStatus (MInOut.DOCSTATUS_Drafted);		//	Draft
				ret.setDocAction(MInOut.DOCACTION_Complete);
				ret.setC_DocType_ID (C_DocTypeRet_ID);
				ret.setIsSOTrx(false);
				ret.setM_Warehouse_ID(inOut.getM_Warehouse_ID());
				ret.setMovementType(MInOut.MOVEMENTTYPE_VendorReturns);
				ret.setDateOrdered (inOut.getDateOrdered());
				ret.setDateAcct ((Timestamp) dateReturnField.getValue());
				ret.setMovementDate((Timestamp) dateReturnField.getValue());
				ret.setC_BPartner_ID(rma.getC_BPartner_ID());
				ret.setC_BPartner_Location_ID(order.getC_BPartner_Location_ID());
				//ret.setRef_InOut_ID(inOut.getM_InOut_ID());
				ret.setM_RMA_ID(rma.getM_RMA_ID());
				ret.setPOReference(inOut.getPOReference()); 
				ret.saveEx();
				noReturnField.setValue(ret.getDocumentNo());
				
				MRMALine[] lines = rma.getLines(true);
				for(MRMALine line : lines){
					MInOutLine receipLine = new MInOutLine(ctx, line.getM_InOutLine_ID(), null);
					MInOutLine inLine = new MInOutLine(ctx, 0, null);
					inLine.setAD_Org_ID(inOut.getAD_Org_ID());
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
					for(int i =0 ; i<ReturnTable.getRowCount() ; i++){
						KeyNamePair InOutPair = (KeyNamePair) ReturnTable.getValueAt(i, 1);
						int M_InOutLine_ID = InOutPair.getKey();
						
						if(M_InOutLine_ID == receipLine.getM_InOutLine_ID()){
							
							KeyNamePair loc_ID = (KeyNamePair) ReturnTable.getValueAt(i, 4);
							inLine.setM_Locator_ID(loc_ID.getKey());
							
						}
					}
					
					inLine.saveEx();
				}
				
				ret.processIt(MInOut.DOCACTION_Complete);
				ret.saveEx();
				
				M_InOut_Print_ID = ret.getM_InOut_ID();
				APCreditMemo = new MInvoice(ctx, 0, null);
					
				StringBuilder SQLDocTypeAPC = new StringBuilder();
				SQLDocTypeAPC.append("SELECT C_DocType_ID ");
				SQLDocTypeAPC.append("FROM  C_DocType ");
				SQLDocTypeAPC.append("WHERE AD_Client_ID = ?");
				SQLDocTypeAPC.append("AND DocBaseType = '" + MDocType.DOCBASETYPE_APCreditMemo+ "' ");
				SQLDocTypeAPC.append("AND IsSoTrx ='N' ");
				
				int C_DocTypeAPC_ID = DB.getSQLValueEx(null, SQLDocTypeAPC.toString(), AD_Client_ID);
	
				APCreditMemo.setAD_Org_ID(inOut.getAD_Org_ID());
				APCreditMemo.setDocStatus(MInvoice.DOCSTATUS_Drafted);		//	Draft
				APCreditMemo.setDocAction(MInvoice.DOCACTION_Complete);
				APCreditMemo.setM_PriceList_ID(order.getM_PriceList_ID());
				APCreditMemo.setPaymentRule(order.getPaymentRule());
				APCreditMemo.setC_DocTypeTarget_ID (C_DocTypeAPC_ID);
				APCreditMemo.setIsSOTrx(false);
				APCreditMemo.setDateInvoiced (ret.getDateAcct());
				APCreditMemo.setDateAcct (ret.getDateAcct());
				APCreditMemo.setC_Order_ID(0);
				APCreditMemo.setC_BPartner_ID(ret.getC_BPartner_ID());
				APCreditMemo.setM_RMA_ID(ret.getM_RMA_ID());
				
				MPriceList plist = new MPriceList(ctx, order.getM_PriceList_ID(), null);
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
				noCreditMemoField.setValue(APCreditMemo.getDocumentNo());	
				
				MInOutLine[] retLines = ret.getLines();
				for(MInOutLine line : retLines){
					
					MInvoiceLine APCrMemoLine = new MInvoiceLine(ctx, 0, null);
					MRMALine rmaLine = new MRMALine(ctx, line.getM_RMALine_ID(), null);
					MInOutLine receiptLine = new MInOutLine(ctx, rmaLine.getM_InOutLine_ID(), null);
					MOrderLine ordLine = new MOrderLine(ctx, receiptLine.getC_OrderLine_ID(),null);
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
				
				processReturn.setEnabled(false);
				lockData(true);
				
				
								
			} catch (Exception ex) {
				
				log.log(Level.SEVERE, this.getClass().getCanonicalName()+ ".valueChange - ERROR: " + ex.getMessage(), ex);
				
			}
			
			
			for(int i =0 ; i < ReturnTable.getRowCount() ; i++){
				
				ReturnTable.setValueAt(false, i, 0);
			
			}
			
			ReturnTable.setColumnReadOnly(0, true);
			//refreshTable();
			
		}else if(e.getTarget().equals(newReturn)){
			
			lockData(false);
			ReturnTable.clearTable();
			
		}else if(e.getTarget().equals(printReturn)){
			
			String trxName = Trx.createTrxName("ReturnToVendorPrint");
			String url = "/home/idempiere/idempiere.gtk.linux.x86_64/idempiere-server/reports/";
			//String url = "D:\\SourceCode\\iDempiereBase\\reports\\";
			MProcess proc = new MProcess(Env.getCtx(), 1000060, trxName);
			MPInstance instance = new MPInstance(proc, proc.getAD_Process_ID());
			ProcessInfo pi = new ProcessInfo("Laporan Vendor Return", 1000060);
			pi.setAD_PInstance_ID(instance.getAD_PInstance_ID());
			
			ArrayList<ProcessInfoParameter> list = new ArrayList<ProcessInfoParameter>();
			list.add(new ProcessInfoParameter("M_InOut_ID", M_InOut_Print_ID,null, null, null));
			list.add(new ProcessInfoParameter("url_path", url, null, null, null));
			
			ProcessInfoParameter[] pars = new ProcessInfoParameter[list.size()];
			list.toArray(pars);
			pi.setParameter(pars);
			
			Trx trx = Trx.get(trxName, true);
			trx.commit();

			ProcessUtil.startJavaProcess(Env.getCtx(), pi,Trx.get(trxName, true));
			
		}
					
	}

	
	public void refreshTable(){
		
		ReturnTable.clear();

		StringBuilder sqlReceipt = new StringBuilder();
		Integer noPenerimaan = (Integer) noPenerimaanField.getValue();
		
		if(noPenerimaan == null){
			noPenerimaan = 0;
			return;
		}
		
		
		sqlReceipt.append("SELECT M_InOut_ID ");
		sqlReceipt.append("FROM M_InOut mi ");
		sqlReceipt.append("WHERE mi.AD_Client_ID = ? ");
		sqlReceipt.append(" AND mi.IsSOTrx = 'N' ");
		sqlReceipt.append(" AND mi.M_InOut_ID = ? ");

		
		int M_InOut_ID = DB.getSQLValueEx(ctx.toString(), sqlReceipt.toString(),new Object[]{AD_Client_ID,noPenerimaan});

		if (M_InOut_ID <= 0){
			FDialog.info(windowNo, null, "", "Dokument Tidak Ada Atau Sudah Pernah DiBatalkan", "Info");
			return;
		}
		
		if(noPenerimaan > 0){
		
			Vector<Vector<Object>> data = null;
			
			//get detail					
			ArrayList<Integer> detailList = new ArrayList<Integer>();

			StringBuilder sqlShipLine = new StringBuilder();
			sqlShipLine.append("SELECT M_InOutLine_ID ");
			sqlShipLine.append("FROM M_InOutLine mil ");
			sqlShipLine.append("INNER JOIN M_InOut mi ON mi.M_Inout_ID = mil.M_InOut_ID ");
			sqlShipLine.append("WHERE mil.AD_Client_ID = ? ");
			sqlShipLine.append("AND mi.IsSOTrx = 'N' ");
			sqlShipLine.append("AND mi.M_InOut_ID = ? ");
			
			PreparedStatement pstmt = null;
			ResultSet rs = null;
			try {
				pstmt = DB.prepareStatement(sqlShipLine.toString(), null);
				pstmt.setInt(1, AD_Client_ID);
				pstmt.setInt(2, M_InOut_ID);


				rs = pstmt.executeQuery();
				while (rs.next()) {
					detailList.add(rs.getInt(1));
				}

			} catch (SQLException ex) {
				log.log(Level.SEVERE, sqlShipLine.toString(), ex);
			} finally {
				DB.close(rs, pstmt);
				rs = null;
				pstmt = null;
			}
			
			data = getTrxData(detailList, ReturnTable);
			Vector<String> columnNames = getOISColumnNames();

			ReturnTable.clear();

			// Set Model
			ListModelTable modelP = new ListModelTable(data);
			modelP.addTableModelListener(this);
			ReturnTable.setData(modelP, columnNames);
			configureMiniTable(ReturnTable);
							
		}
		
		
	}
	
	
	public String validation(IMiniTable minitable){
		String msg = "";
		String ket = ketField.getValue();
		String tipeDoc = tipeDokumenField.getValue();
		
		if(namaReturnField.getValue().toString().isEmpty()||namaReturnField.getValue()==null){
			msg = "Nama Return Belum Di Isi";
		}
		
		if(dateReturnField.getValue() == null){
			msg = msg + "\n" +"Tanggal Pengembalian Tidak Boleh Kosong";
		}
		
		if(locatorSearch.getValue() == null){
			msg = msg + "\n" +"Gudang Belum Di Pilih";
		}
		
		if(tipeDoc == null || tipeDoc == "" || tipeDoc.isEmpty()){
			msg = msg + "\n"+"Tipe Dokumen Belum Di Tentukan";
		}
		if(tipeRMAField.getValue()==null){
			msg = msg + "\n"+"Tipe RMA Belum Di Tentukan";
		}
		
		if(ket.length() > 255){
			msg = msg + "\n"+"Jumlah Huruf Pada Kolom Keterangan Tidak Boleh Melebihi 255";
		}
		if(msg == ""){
			Boolean IsSelected = false;
			for (int i=0 ; i<minitable.getRowCount() ; i++){
				
				KeyNamePair InOutLine = (KeyNamePair) minitable.getValueAt(i, 1);
				MInOutLine inLine = new MInOutLine(ctx, InOutLine.getKey(), null);

				
				StringBuilder SQLQtyTotalRMA = new StringBuilder();
				SQLQtyTotalRMA.append("SELECT SUM (rml.Qty) ");
				SQLQtyTotalRMA.append("FROM M_RMALine rml ");
				SQLQtyTotalRMA.append("LEFT JOIN M_RMA rm ON rm.M_RMA_ID = rml.M_RMA_ID ");
				SQLQtyTotalRMA.append("WHERE rml.AD_Client_ID = ? ");
				SQLQtyTotalRMA.append("AND rml.M_InOutLine_ID = ? ");
				SQLQtyTotalRMA.append("AND rm.DocStatus = 'CO' ");

				
				BigDecimal QtyTotalRMA = DB.getSQLValueBDEx(null, SQLQtyTotalRMA.toString(), new Object[]{AD_Client_ID,inLine.getM_InOutLine_ID()});
				if(QtyTotalRMA == null){
					QtyTotalRMA = Env.ZERO;
				}
				
				BigDecimal qtyPenerimaan = (BigDecimal) minitable.getValueAt(i, 5);
				BigDecimal qtyReturn = (BigDecimal) minitable.getValueAt(i, 6);
				BigDecimal qtyTotalReturn = QtyTotalRMA.add(qtyReturn);
				String ketDetail = (String) minitable.getValueAt(i, 3);
				if((boolean) minitable.getValueAt(i,0)==true){
					IsSelected = true;	
				}
				
				if((boolean) minitable.getValueAt(i,0)){
				
					if(ketDetail.length() > 255){
						msg = "Jumlah Huruf Pada Kolom Keterangan Tidak Boleh Melebihi 255";
						return msg;
					}
					
					if(qtyReturn.compareTo(Env.ZERO) <= 0){
						msg = "Qty Pengembalian harus lebih besar dari Nol";
						return msg;
					}
					
					if(qtyReturn.compareTo(qtyPenerimaan) > 0){
						msg = "Qty Pengembalian Tidak Boleh Melebihi Qty Penerimaan";	
					}
					else if(qtyTotalReturn.compareTo(qtyPenerimaan) > 0){
						msg = "Qty Pengembalian Tidak Boleh Melebihi Qty Penerimaan "+"\n"+" "
								+ "(Mungkin Sudah pernah Terbuat RMA Atas Penerimaan Produk Yang Anda Pilih)";	
					}
					
				}
			}

			if(!IsSelected){
				msg = "Produk pengembalian Belum Ditentukan";
				return msg;
			}
			
		}
			
		return msg;
		
	}
	
	@Override
	public ADForm getForm() {
		return form;
	}
	
	protected void configureMiniTable(IMiniTable miniTable) {
		
		miniTable.setColumnClass(0, Boolean.class, false);
		miniTable.setColumnClass(1, int.class, true); 			// 2-produk
		miniTable.setColumnClass(2, int.class, true); 			// 3-IMEI
		miniTable.setColumnClass(3, String.class, false); 		// 4-Keterangan
		miniTable.setColumnClass(4, int.class, true); 			// 5-locator
		miniTable.setColumnClass(5, BigDecimal.class, true); 	// 6-qty
		miniTable.setColumnClass(6, BigDecimal.class, false); 	// 7-qty Return
		
		miniTable.autoSize();

	}

	protected Vector<String> getOISColumnNames() {
		
		// Header Info
		Vector<String> columnNames = new Vector<String>(7);
		columnNames.add(Msg.getMsg(ctx, "Select"));
		columnNames.add("Produk");
		columnNames.add("IMEI");
		columnNames.add("Keterangan Pengembalian");
		columnNames.add("Lokasi");
		columnNames.add("Qty Penerimaan");
		columnNames.add("Qty Pengembalian");
		return columnNames;
	
	}
	
	public void lockData(boolean IsLock){
	
	if(IsLock){
		org.setReadWrite(false);
		namaReturnField.setReadonly(true);
		locatorSearch.setReadWrite(false);
		tipeDokumenField.setReadonly(true);
		tipeDokumenField.setEnabled(false);
		tipeRMAField.setReadWrite(false);
		dateReturnField.setReadWrite(false);
		noPenerimaanField.setReadWrite(false);
		ketField.setReadonly(true);
		ketField.setEnabled(false);
		//ReturnTable.setEnabled(false);
		printReturn.setEnabled(true);
		
		
	}else if(!IsLock){
		org.setReadWrite(true);
		namaReturnField.setReadonly(false);
		locatorSearch.setReadWrite(true);
		tipeDokumenField.setReadonly(false);
		tipeDokumenField.setEnabled(true);
		tipeRMAField.setReadWrite(true);
		dateReturnField.setReadWrite(true);
		noPenerimaanField.setReadWrite(true);
		ketField.setEnabled(true);
		ketField.setReadonly(false);
		ketField.setValue(null);
		org.setValue(0);
		namaReturnField.setValue(null);
		locatorSearch.setValue(0);
		tipeDokumenField.setReadonly(false);
		tipeDokumenField.setEnabled(true);
		tipeRMAField.setValue(0);
		Calendar cal = Calendar.getInstance();
		cal.setTime(Env.getContextAsDate(ctx, "#Date"));
		cal.set(Calendar.HOUR_OF_DAY, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);
		cal.set(Calendar.MILLISECOND, 0);
		dateReturnField.setValue(new Timestamp(cal.getTimeInMillis()));
		noPenerimaanField.setValue(null);
		processReturn.setEnabled(true);
		M_InOut_Print_ID = 0;
		noRMAField.setValue(null);
		noCreditMemoField.setValue(null);
		noReturnField.setValue(null);			
	}
	}
	
	public String checkOnhand(int AD_Client_ID, int AD_Org_ID, int M_Product_ID, int M_Locator_ID,int M_AttributeSetInstance_ID,BigDecimal Qty){
		String msg = "";
		BigDecimal onHand = Env.ZERO;
		StringBuilder SQLCheckOnHand = new StringBuilder();
		SQLCheckOnHand.append("SELECT SUM(ms.QtyOnHand) ");
		SQLCheckOnHand.append("FROM M_Storage ms ");
		SQLCheckOnHand.append("WHERE ms.AD_Client_ID = ? ");
		SQLCheckOnHand.append("AND ms.AD_Org_ID = ? ");
		SQLCheckOnHand.append("AND ms.M_Product_ID = ? ");
		SQLCheckOnHand.append("AND ms.M_Locator_ID = ? ");
		if(M_AttributeSetInstance_ID > 0){
			SQLCheckOnHand.append("AND ms.M_AttributeSetInstance_ID = ? ");
		}
		if(M_AttributeSetInstance_ID > 0){
			onHand = DB.getSQLValueBD(null, SQLCheckOnHand.toString(), new Object[]{AD_Client_ID,AD_Org_ID,M_Product_ID,M_Locator_ID,M_AttributeSetInstance_ID});
		}else if(M_AttributeSetInstance_ID == 0){
			onHand = DB.getSQLValueBD(null, SQLCheckOnHand.toString(), new Object[]{AD_Client_ID,AD_Org_ID,M_Product_ID,M_Locator_ID});
		}
		
		MProduct prod = new MProduct(ctx, M_Product_ID, null);
		
		if(Qty.compareTo(onHand) > 0){
			
			if(msg == ""){
				msg = "Qty Return Produk "+prod.getName()+" Lebih Besar Dari Stock On Hand"+"\n";
			}else if(msg != ""){
				msg = msg + "Qty Produk "+prod.getName()+" Return Lebih Besar Dari Stock On Hand"+"\n";

			}
		}
		
		return msg;
		
	}
	
}
