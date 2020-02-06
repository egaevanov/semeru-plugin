package org.semeru.pos;

import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.Vector;
import java.util.logging.Level;

import org.adempiere.util.ProcessUtil;
import org.adempiere.webui.component.Button;
import org.adempiere.webui.component.Combobox;
import org.adempiere.webui.component.ConfirmPanel;
import org.adempiere.webui.component.Datebox;
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
import org.adempiere.webui.component.Window;
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
import org.compiere.model.MPayment;
import org.compiere.model.MPriceList;
import org.compiere.model.MProcess;
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
import org.semeru.pos.model.MSemeruPOS;
import org.semeru.pos.model.X_SM_SemeruPOSLine;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zul.Borderlayout;
import org.zkoss.zul.Center;
import org.zkoss.zul.Decimalbox;
import org.zkoss.zul.Hbox;
import org.zkoss.zul.North;
import org.zkoss.zul.Separator;
import org.zkoss.zul.South;
import org.zkoss.zul.Space;
import org.zkoss.zul.Vbox;

/**
 * 
 * @author Tegar N
 *
 * Semeru Return Penjualan
 *
 */
public class WSemeruReturnPenjualan extends SemeruReturnPenjualan implements IFormController,
EventListener<Event>, WTableModelListener, ValueChangeListener{

	// CustomForm
	private CustomForm formCustomerRMA = new CustomForm();
	
	// BorderLayout
	private Borderlayout mainLayout = new Borderlayout();
	private Borderlayout infoLayout = new Borderlayout();
	private Borderlayout summaryLayout = new Borderlayout();

	private Panel parameterPanel = new Panel();
	private Panel infoPanel = new Panel();
	private Panel southPanel = new Panel();
	
	// Grid
	private Grid summaryGrid = GridFactory.newGridLayout();
	private Grid parameterGrid = GridFactory.newGridLayout();

	//Nama RMA
	private Label namaReturnLabel = new Label("Nama Return :");
	private Textbox namaReturn= new Textbox();
	
	//Gudang
	private Label locatorLabel = new Label("Lokasi :");
	private Combobox locatorSearch = new Combobox();
	
	//Tipe Dokumen
	private Label tipeDokumenLabel = new Label("Tipe Dokumen :");
	private Combobox tipeDokumenField = new Combobox();
	
	//Toko
	private Label TokoLabel = new Label("Toko :");
	private WTableDirEditor TokoSearch = null;
	
	//Nota
	private Label noNotaLabel = new Label("No Nota");
	private WSearchEditor noNota = null; 
	
	//Status
	private Label tanggalReturnLabel = new Label("Tanggal Return :");
	private Datebox tanggalReturn = new Datebox();
	
	// Org
	private Label orgLabel = new Label("Cabang :");
	private WTableDirEditor orgSearch = null;
		
	//Tipe RMA
	private Label tipeRMALabel = new Label("Tipe RMA :");
	private WTableDirEditor tipeRMAList = null;
	
	//No Customer RMA
	private Label noCustomerRMALabel = new Label("No Customer RMA :");
	private Textbox noCustomerRMA = new Textbox();
			
	//No Customer Return
	private Label noCustomerReturnLabel = new Label("No Customer Return :");
	private Textbox noCustomerReturn = new Textbox();
	
	//No AR Credit Memo
	private Label noARCreditMemoLabel = new Label("No AR Credit Memo :");
	private Textbox noARCreditMemo = new Textbox();
	
	//Status
	private Label keteranganLabel = new Label("Keterangan :");
	private Textbox keteranganTextBox = new Textbox();
	
	private Button buatBaru = new Button();
	private Button process = new Button();
	private Button cetakNota = new Button();
	
	// TableLine
	private WListbox returnTable = ListboxFactory.newDataTable();
	
	private Properties ctx = Env.getCtx();
	private int AD_Client_ID  = Env.getAD_Client_ID(ctx);
	private int AD_User_ID = Env.getAD_User_ID(ctx);;
	private int windowNo = formCustomerRMA.getWindowNo();
	private Integer rowIndex = null;
	private int CreatedByPOS_ID = 0;
	private int M_RMA_ID_print = 0;
	private final String ReturnApproval = "ReturnSOApproval";
	private final String ReturnVisor = "ReturnSOSupervisor";


	
	@Override
	public void valueChange(ValueChangeEvent evt) {
		
		try {
			String name = evt.getPropertyName();
			Object value = evt.getNewValue();
			
			if(value == null){
				
				return;
			}	
			
			if(name.equals("SM_SemeruPOS_ID")){
				
				Integer SM_SemeruPOS_ID = Integer.valueOf(value.toString());	
				MSemeruPOS pos= new MSemeruPOS(ctx, SM_SemeruPOS_ID , null);			
				
				StringBuilder SQLGetShipment = new StringBuilder();	
				SQLGetShipment.append("SELECT M_InOut_ID ");
				SQLGetShipment.append("FROM M_InOut ");
				SQLGetShipment.append("WHERE AD_Client_ID = ? ");
				SQLGetShipment.append("AND C_Order_ID = ? ");
				
				int M_InOut_ID = DB.getSQLValueEx(null, SQLGetShipment.toString(), new Object []{AD_Client_ID,pos.getC_Order_ID()});
	
				if(M_InOut_ID  < 0){
					FDialog.info(windowNo, null, "", "Penjualan Belum Di Lakukan Pengiriman Barang", "Info");
					return;
				}
				
				MInOut ship = new MInOut(ctx, M_InOut_ID, null);
				
				Vector<Vector<Object>> data = null;
		
				//get detail					
				ArrayList<Integer> detailList = new ArrayList<Integer>();
				
				StringBuilder sqlShipLine = new StringBuilder();
				sqlShipLine.append("SELECT M_InOutLine_ID ");
				sqlShipLine.append("FROM M_InOutLine ");
				sqlShipLine.append("WHERE AD_Client_ID = ? ");
				sqlShipLine.append("AND M_InOut_ID = ? ");
				
				tanggalReturn.setValue(ship.getMovementDate());
				orgSearch.setValue(ship.getAD_Org_ID());
				
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
					
				data = getTrxData(detailList, returnTable);
				Vector<String> columnNames = getOISColumnNames();

				returnTable.clear();

				// Set Model
				ListModelTable modelP = new ListModelTable(data);
				modelP.addTableModelListener(this);
				returnTable.setData(modelP, columnNames);
				configureMiniTable(returnTable);
				
			}
			
		} catch (Exception e) {
			log.log(Level.SEVERE, this.getClass().getCanonicalName()
					+ ".valueChange - ERROR: " + e.getMessage(), e);
		}
		
	}

	@Override
	public void tableChanged(WTableModelEvent ev) {
		boolean isUpdate = (ev.getType() == WTableModelEvent.CONTENTS_CHANGED);
		
		if (!isUpdate) {
			return;
		} else if (isUpdate) {
			rowIndex = ev.getFirstRow();
			int col = ev.getColumn();
			
			if(col > 0 ){
				
				if(col == 8 ){
					BigDecimal harga = (BigDecimal) returnTable.getValueAt(rowIndex, col);
					if(harga.compareTo(Env.ZERO) < 0){
						FDialog.warn(windowNo, null, "", "Input Harga Tidak Boleh Kurang Dari 0(Nol)", "Info");
						return;
					}
				}
				calculateTableChange(col);
			}
			
		}
		
	}
	
	@Override
	public void onEvent(Event e) throws Exception {
		
		
		if(e.getTarget().equals(process)){
		
			try{
			
				if(noNota.getValue() == null){
					FDialog.info(windowNo, null, "", "Kolom No Nota Tidak Boleh Kosong", "Info");
					return;
				}
				
				if(orgSearch.getValue() == null){
					FDialog.info(windowNo, null, "", "Kolom Cabang Tidak Boleh Kosong", "Info");
					return;
				}
				
				if(tipeRMAList.getValue() == null){
					FDialog.info(windowNo, null, "", "Kolom Tipe RMA Tidak Boleh Kosong", "Info");
					return;
				}
				
				if(locatorSearch.getValue() == null){
					
					FDialog.info(windowNo, null, "", "Kolom Lokasi Tidak Boleh Kosong", "Info");
					return;
				}
				
				
				MSemeruPOS semeruPos = new MSemeruPOS(ctx, (Integer)noNota.getValue(), null);
				Timestamp tglNota = semeruPos.getDateOrdered();
				
				Date tgl = tanggalReturn.getValue();
				SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd 00:00:00");
				String tglTostr = df.format(tgl);
				Timestamp tglReturn = Timestamp.valueOf(tglTostr);
				
				
				if(tglReturn.compareTo(tglNota)<0){
					
					FDialog.info(windowNo, null, "", "Transaksi Return Harus Sama dengan atau Diatas Tanggal Nota", "Info");
					return;
				}
				
				
				for (int i = 0 ; i<returnTable.getRowCount(); i++ ){
					BigDecimal qtyShip = (BigDecimal) returnTable.getValueAt(i, 6);
					BigDecimal qtyRet = (BigDecimal) returnTable.getValueAt(i, 8);
					
					if(qtyRet.compareTo(qtyShip)> 0){
						
						FDialog.info(windowNo, null, "", "Qty Return Tidak Lebih Besar dari Qty Shipment", "Info");
						return;
						
					}
					
				}
				
			
				StringBuilder SQLCekOtorisasi = new StringBuilder();
				SQLCekOtorisasi.append("SELECT description::NUMERIC ");
				SQLCekOtorisasi.append(" FROM AD_Param ");
				SQLCekOtorisasi.append(" WHERE AD_Client_ID = " + AD_Client_ID);
				SQLCekOtorisasi.append(" AND value= '"+ReturnApproval+"'");
				
				int rsOtorisasi = DB.getSQLValueEx(null, SQLCekOtorisasi.toString());
								
				if(rsOtorisasi > 0){
					OtorisasiProcess();
				}else{
					
					process(0);
	
				}
					
						
			} catch (Exception ex) {
			
				log.log(Level.SEVERE, this.getClass().getCanonicalName()+ ".valueChange - ERROR: " + ex.getMessage(), ex);
			
			}
		
		}else if(e.getTarget().equals(buatBaru)){
			
			buatBaru();
			
		}else if(e.getTarget().equals(cetakNota)){
						
			StringBuilder SQLRmaID = new StringBuilder();
			SQLRmaID.append("SELECT M_RMA_ID ");
			SQLRmaID.append("FROM M_RMA ");
			SQLRmaID.append("WHERE AD_Client_ID = ? ");
			SQLRmaID.append("AND DocumentNo = '" + noCustomerRMA.getValue().toString()+"'");

			M_RMA_ID_print = DB.getSQLValueEx(null, SQLRmaID.toString(), new Object[]{AD_Client_ID});
			
			
			String trxName = Trx.createTrxName("CustomerReturnPrint");
			String url = "/home/idempiere/idempiere.gtk.linux.x86_64/idempiere-server/reports/";
			//String url = "D:\\SourceCode\\iDempiereBase\\reports\\";
			MProcess proc = new MProcess(Env.getCtx(), 1000100, trxName);

			MPInstance instance = new MPInstance(proc, proc.getAD_Process_ID());
			ProcessInfo pi = new ProcessInfo("Laporan Return Customer", 1000100);

			pi.setAD_PInstance_ID(instance.getAD_PInstance_ID());
			
			ArrayList<ProcessInfoParameter> list = new ArrayList<ProcessInfoParameter>();
			list.add(new ProcessInfoParameter("M_RMA_ID", M_RMA_ID_print,null, null, null));
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
		
		return this.formCustomerRMA;
	
	}
	
	
	public WSemeruReturnPenjualan() {
		
		AD_User_ID = Env.getAD_User_ID(ctx);

		String sqlKasir = "SELECT C_BPartner_ID FROM AD_User WHERE AD_Client_ID = ? AND AD_User_ID = ?";
		CreatedByPOS_ID = DB.getSQLValueEx(ctx.toString(), sqlKasir.toString(),new Object[] { Env.getAD_Client_ID(ctx), AD_User_ID });

		if(CreatedByPOS_ID < 0){	
			FDialog.warn(windowNo, null, "", "User Tidak Memiliki Akses ke Halaman Presales, Silahkan Hubungi Administrator","Info");
			formCustomerRMA.dispose();
			return;
		}
			
		dyInit();
		init();
		
	}
	
	private void dyInit(){
		AD_User_ID = Env.getAD_User_ID(ctx);

		String sqlKasir = "SELECT C_BPartner_ID FROM AD_User WHERE AD_Client_ID = ? AND AD_User_ID = ?";
		CreatedByPOS_ID = DB.getSQLValueEx(ctx.toString(), sqlKasir.toString(),new Object[] { Env.getAD_Client_ID(ctx), AD_User_ID });

		if(CreatedByPOS_ID < 0){	
			FDialog.warn(windowNo, null, "", "User Tidak Memiliki Akses ke Halaman Presales, Silahkan Hubungi Administrator","Info");
			return;
		}
		
		MLookup orgLookup = MLookupFactory.get(ctx, formCustomerRMA.getWindowNo(), 0,2163, DisplayType.TableDir);
		orgSearch = new WTableDirEditor("AD_Org_ID", true, false, true, orgLookup);
		orgSearch.addValueChangeListener(this);
		orgSearch.setMandatory(true);
		orgSearch.setReadWrite(true);
		
		MLookup lookupClient = MLookupFactory.get(ctx, formCustomerRMA.getWindowNo(), 0,14621, DisplayType.TableDir);
		TokoSearch = new WTableDirEditor("AD_Table_ID", true,false, true, lookupClient);
		TokoSearch.addValueChangeListener(this);
		TokoSearch.setMandatory(true);
		TokoSearch.setValue(AD_Client_ID);
	
		Integer Org_ID = (Integer) orgSearch.getValue();
		if(Org_ID == null){
			Org_ID = 0 ;
		}
	 
		ArrayList<KeyNamePair> list = loadDocType();
		tipeDokumenField.removeAllItems();
		for (KeyNamePair docType : list)
		tipeDokumenField.appendItem(docType.getName());
		tipeDokumenField.setSelectedIndex(0);
		tipeDokumenField.addEventListener(0, "onChange", this);
			
//		MLookup lookupLocator = MLookupFactory.get(ctx, formCustomerRMA.getWindowNo(), 0,1389, DisplayType.TableDir);
//		locatorSearch = new WTableDirEditor("M_Locator_ID", true, false, true,lookupLocator);
//		locatorSearch.addValueChangeListener(this);
//		locatorSearch.setMandatory(true);

		ArrayList<KeyNamePair> listLoc = loadLocator(Org_ID);
		locatorSearch.removeAllItems();
		for (KeyNamePair loc : listLoc){
			locatorSearch.appendItem(loc.getName());
		}
		
		//No Nota
		MLookup lookupNota = MLookupFactory.get(ctx, formCustomerRMA.getWindowNo(), 0, 1000934, DisplayType.Search);
		noNota  = new WSearchEditor("SM_SemeruPOS_ID",true, false, true,lookupNota);
		noNota.addValueChangeListener(this);
		noNota.setMandatory(true);
		
		//No Nota
		MLookup lookupTipeRMA = MLookupFactory.get(ctx, formCustomerRMA.getWindowNo(), 0, 12142, DisplayType.TableDir);
		tipeRMAList  = new WTableDirEditor("M_RMAType_ID",true, false, true,lookupTipeRMA);
		tipeRMAList.addValueChangeListener(this);
		tipeRMAList.setMandatory(true);
		
		
		initialDisplay();
	}
	
	private void initialDisplay(){
		
		returnTable.setEnabled(false);
		namaReturn.setEnabled(false);
		orgSearch.setReadWrite(false);
		locatorSearch.setEnabled(false);
		tipeDokumenField.setEnabled(false);
		tipeRMAList.setReadWrite(false);
		tanggalReturn.setEnabled(false);
		noNota.setReadWrite(false);
		keteranganTextBox.setEnabled(false);
		process.setEnabled(false);
		
	}
	
	
	
	private void init(){	
		
		formCustomerRMA.appendChild(mainLayout);
		mainLayout.setWidth("99%");
		mainLayout.setHeight("100%");
		
		formCustomerRMA.addEventListener(DialogEvents.ON_WINDOW_CLOSE, new EventListener<Event>(){

			@Override
			public void onEvent(Event arg0) throws Exception {

				formCustomerRMA.dispose();
			}
			
		});
		
		North north = new North();
		mainLayout.appendChild(north);

		String grid = "border: 1px solid #C0C0C0; border-radius:5px;";
		north.appendChild(parameterPanel);
		north.setStyle(grid);
		
		Hbox northMainBox = new Hbox();
		parameterPanel.appendChild(northMainBox);
		northMainBox.appendChild(parameterGrid);
		parameterGrid.setWidth("100%");
		parameterGrid.setStyle("Height:100%;");
		
		Rows rows = null;
		Row row = null;

		rows = parameterGrid.newRows();

		// Toko
		row = rows.newRow();
	    row.appendCellChild(TokoLabel.rightAlign(), 1);
		row.appendCellChild(TokoSearch.getComponent(), 1);
		TokoSearch.getComponent().setHflex("true");
		TokoSearch.setReadWrite(false);
				
		// Cabang
		row.appendCellChild(orgLabel.rightAlign(), 1);
		row.appendCellChild(orgSearch.getComponent(), 1);
		orgSearch.getComponent().setHflex("true");
		row.appendCellChild(new Space(), 1);
				
		//Nama Return
		row = rows.newRow();
		row.appendCellChild(namaReturnLabel.rightAlign(),1);
		row.appendCellChild(namaReturn);
		namaReturn.setMaxlength(60);
		namaReturn.setHflex("true");

		//Gudang
		row.appendCellChild(locatorLabel.rightAlign(),1);
		row.appendCellChild(locatorSearch,1);
		locatorSearch.setHflex("true");
		locatorSearch.setReadonly(true);	
		locatorSearch.addEventListener(0, "onChange", this);

		//Tipe Dokumen
		row = rows.newRow();
		row.appendCellChild(tipeDokumenLabel.rightAlign(),1);
		row.appendCellChild(tipeDokumenField,1);
		tipeDokumenField.setHflex("true");
		tipeDokumenField.setHeight("24px");
		tipeDokumenField.setStyle("border-radius:3px;");
		tipeDokumenField.setReadonly(true);

		//Tipe RMA
		row.appendCellChild(tipeRMALabel.rightAlign());
		row.appendCellChild(tipeRMAList.getComponent(),1);
		tipeRMAList.getComponent().setHflex("true");
				
		//Tanggal Return
		row = rows.newRow();
		row.appendCellChild(tanggalReturnLabel.rightAlign());
		row.appendChild(tanggalReturn);
		tanggalReturn.setHflex("true");
		tanggalReturn.setFormat("dd/MM/yyyy");
		
		//Tipe Dokumen
		row.appendCellChild(noNotaLabel.rightAlign());
		row.appendCellChild(noNota.getComponent(),1);
		noNota.getComponent().setHflex("true");

		// ket
		row = rows.newRow();
		row.appendCellChild(keteranganLabel.rightAlign(), 1);
		row.appendCellChild(keteranganTextBox, 1);
		keteranganTextBox.setRows(2);
		keteranganTextBox.setHeight("100%");
		keteranganTextBox.setMaxlength(255);
		keteranganTextBox.setHflex("1");
		row.appendCellChild(new Space(), 1);
				
		row.appendCellChild(buatBaru, 1);
		buatBaru.setLabel("Buat Baru");
		buatBaru.addActionListener(this);
		buatBaru.setHflex("true");
				
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
		southHBox.appendChild(process);
		process.setLabel("Proses Return");
		process.addActionListener(this);
		process.setHflex("true");
				
		southHBox.appendChild(cetakNota);
		cetakNota.setLabel("Cetak Return");
		cetakNota.addActionListener(this);	
		cetakNota.setHflex("true");
		cetakNota.setEnabled(false);
				
		southRow = southRows.newRow();
		southRow.appendCellChild(noCustomerRMALabel.rightAlign(), 1);
		southRow.appendCellChild(noCustomerRMA, 1);
		noCustomerRMA.setHflex("true");
		noCustomerRMA.setReadonly(true);
		southRow.appendCellChild(new Space(), 3);
				
		southRow = southRows.newRow();
		southRow.appendCellChild(noCustomerReturnLabel.rightAlign(), 1);
		southRow.appendCellChild(noCustomerReturn, 1);
		noCustomerReturn.setHflex("true");
		noCustomerReturn.setReadonly(true);
		southRow.appendCellChild(new Space(), 3);
				
		southRow = southRows.newRow();
		southRow.appendCellChild(noARCreditMemoLabel.rightAlign(), 1);
		southRow.appendCellChild(noARCreditMemo, 1);
		noARCreditMemo.setHflex("true");
		noARCreditMemo.setReadonly(true);
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
		center.appendChild(returnTable);
		returnTable.setWidth("100%");
		returnTable.setHeight("100%");
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
	
	
	protected void configureMiniTable(IMiniTable miniTable) {

		miniTable.setColumnClass(0, Boolean.class, true);
		miniTable.setColumnClass(1, String.class, true); 		// 
		miniTable.setColumnClass(2, String.class, true); 		// 2-Product
		miniTable.setColumnClass(3, String.class, true); 		// 3-Lokasi
		miniTable.setColumnClass(4, String.class, true);		// 4-imei
		miniTable.setColumnClass(5, BigDecimal.class, true); 	// 6-harga
		miniTable.setColumnClass(6, BigDecimal.class, true); 	// 8-qty
		miniTable.setColumnClass(7, BigDecimal.class, true); 	// 8-total
		miniTable.setColumnClass(8, BigDecimal.class, true); 	// 8-qty return


		
		miniTable.autoSize();

	}
	
	protected Vector<String> getOISColumnNames() {

		// Header Info
		Vector<String> columnNames = new Vector<String>(9);
		columnNames.add(Msg.getMsg(ctx, "Select"));
		columnNames.add("No");
		columnNames.add("Nama Barang");
		columnNames.add("Lokasi");
		columnNames.add("IMEI");
		columnNames.add("Harga");
		columnNames.add("Qty");
		columnNames.add("Total");
		columnNames.add("Qty Return");


		return columnNames;

	}
	
	public int getIDFromComboBox (Combobox combobox, String tableName,String selectColumnName){
		int result_ID = 0;
		String select_ID = tableName+"_ID";

		String cbValue = combobox.getText();

		StringBuilder sqlPriceList = new StringBuilder();
		
		sqlPriceList.append("SELECT ");
		sqlPriceList.append(select_ID);
		sqlPriceList.append(" FROM ");
		sqlPriceList.append(tableName);
		sqlPriceList.append(" WHERE AD_Client_ID = ? ");
		sqlPriceList.append(" AND "+selectColumnName+ "= " );
		sqlPriceList.append(" '"+cbValue+"'");

		result_ID  = DB.getSQLValueEx(null, sqlPriceList.toString(),AD_Client_ID);

		return result_ID;
		
	} 
	
	public void calculateTableChange(int col){
		
		BigDecimal grandTotal = Env.ZERO;
		for (int i = 0 ; i < returnTable.getRowCount() ; i++){
			
			boolean isSelected = (boolean) returnTable.getValueAt(i, 0); 
			BigDecimal totalHarga = Env.ZERO;
			if(isSelected){
				if(col ==8){
					BigDecimal harga = (BigDecimal) returnTable.getValueAt(i, 5);
					BigDecimal qtyReturn = (BigDecimal) returnTable.getValueAt(i, 8);
					BigDecimal total = harga.multiply(qtyReturn);
					totalHarga = total;
				}
				grandTotal = grandTotal.add(totalHarga);
			}
			
		}
				
	}
	
	
	public void process(int supervisor_id){
		
		Integer SM_SemeruPOS_ID = (Integer) noNota.getValue();
		Integer AD_Org_ID = (Integer) orgSearch.getValue();
		MInOut ret =null;

		MSemeruPOS pos = new MSemeruPOS(ctx, SM_SemeruPOS_ID, null);	
		
		String validation = validation(returnTable);
		
		if(validation != ""){
			
			FDialog.info(windowNo, null, "", validation, "Info");
			return;
			
		}
		
		StringBuilder SQLGetShipment = new StringBuilder();
		SQLGetShipment.append("SELECT M_InOut_ID ");
		SQLGetShipment.append("FROM M_InOut ");
		SQLGetShipment.append("WHERE AD_Client_ID = ? ");
		SQLGetShipment.append("AND C_Order_ID = ? ");
		
		int M_InOut_ID = DB.getSQLValueEx(null, SQLGetShipment.toString(), new Object []{AD_Client_ID,pos.getC_Order_ID()});

		MInOut inOut = new MInOut(ctx, M_InOut_ID, null);
		MOrder ord = new MOrder(ctx, inOut.getC_Order_ID(), null);
		MInvoice ARCreditMemo = null;
		
		String DocStatus = inOut.getDocStatus();
		
		
		if(!DocStatus.equals(MInOut.DOCSTATUS_Completed)){
			
			MInOutLine[] lines = inOut.getLines();
			boolean IsNegativ = false;
			
			StringBuilder SQLGetOnHand = new StringBuilder();
			SQLGetOnHand.append("SELECT SUM(QtyOnHand) ");
			SQLGetOnHand.append(" FROM M_StorageOnHand ");
			SQLGetOnHand.append(" WHERE AD_Client_ID = ?");
			SQLGetOnHand.append(" AND M_Product_ID = ?");
		//	SQLGetOnHand.append(" AND M_Locator_ID = ?");

			
			
			for (MInOutLine line : lines){
				
				BigDecimal QtyReturn = line.getQtyEntered();
				
				BigDecimal QtyOnHand = DB.getSQLValueBDEx(null, SQLGetOnHand.toString(), new Object[]{AD_Client_ID,line.getM_Product_ID()});
				
				if(QtyOnHand == null){
					QtyOnHand = Env.ZERO;
				}
				
				
				if(QtyOnHand.compareTo(QtyReturn)<0){
					IsNegativ = true;
				}
				
			}
			
			if(!IsNegativ){
				FDialog.info(windowNo, null, "", "Silahkan Memproses Complete Surat Jalan No "+inOut.getDocumentNo()+" Dahulu Melalui Menu Shipment (Customer)", "Info");
				return;
			}else{
					
				HashMap<Integer, String> AR = new HashMap<Integer, String>();
				
				StringBuilder SQLCekAR = new StringBuilder();
				SQLCekAR.append("SELECT C_Payment_ID,DocStatus ");
				SQLCekAR.append(" FROM C_Payment ");
				SQLCekAR.append(" WHERE AD_Client_ID = "+ AD_Client_ID);
				SQLCekAR.append(" AND C_Payment_ID IN( ");
				SQLCekAR.append(" SELECT C_Payment_ID  ");
				SQLCekAR.append(" FROM C_POSPayment ");
				SQLCekAR.append(" WHERE AD_Client_ID = "+ AD_Client_ID);				
				SQLCekAR.append(" AND C_Order_ID = "+ inOut.getC_Order_ID()+")");
					
				PreparedStatement pstmt = null;
				ResultSet rs = null;
				try {
					pstmt = DB.prepareStatement(SQLCekAR.toString(), null);

					rs = pstmt.executeQuery();
					while (rs.next()) {
						AR.put(rs.getInt(1), rs.getString(2));
					}

				} catch (SQLException e) {
					log.log(Level.SEVERE, SQLCekAR.toString(), e);
				} finally {
					DB.close(rs, pstmt);
					rs = null;
					pstmt = null;
				}

				
				if(AR.size() <= 0){
					
					inOut.processIt(MInOut.DOCACTION_Void);
					inOut.saveEx();
					
				}else{
					
					
					for (Integer key : AR.keySet()) {
						MPayment pay = new MPayment(ctx, key, null);
						
						if(AR.get(key).equals(MPayment.DOCSTATUS_Completed)){
							pay.processIt(MPayment.DOCACTION_Reverse_Correct);
							pay.saveEx();
							
						}else{
							
							pay.processIt(MPayment.DOCACTION_Void);
							pay.saveEx();
							
						}
						
					}
					
					
					inOut.processIt(MInOut.DOCACTION_Void);
					inOut.saveEx();
					
				}
				
				
				ord.processIt(MOrder.DOCACTION_Close);
				ord.saveEx();
				
				
				StringBuilder SQLUpdate = new StringBuilder();
				SQLUpdate.append("UPDATE SM_SemeruPOS ");
				SQLUpdate.append(" SET Pembayaran1 = 0,paytype1 = '', ");
				SQLUpdate.append(" Pembayaran2 = 0,paytype2 = '', ");
				SQLUpdate.append(" Pembayaran3 = 0,paytype3 = '', ");
				SQLUpdate.append(" Pembayaran4 = 0,paytype4 = '' ");
				SQLUpdate.append(" WHERE AD_Client_ID = "+ AD_Client_ID);
				SQLUpdate.append(" AND SM_SemeruPOS_ID = "+ pos.getSM_SemeruPOS_ID());
				
				DB.executeUpdateEx(SQLUpdate.toString(), null);


				process.setEnabled(false);
				returnTable.setEnabled(false);
				namaReturn.setEnabled(false);
				orgSearch.setReadWrite(false);
				locatorSearch.setEnabled(false);
				tipeDokumenField.setEnabled(false);
				tipeRMAList.setReadWrite(false);
				tanggalReturn.setEnabled(false);
				noNota.setReadWrite(false);
				keteranganTextBox.setEnabled(false);
				
				for (int i = 0 ; i < returnTable.getRowCount(); i++  ){
					returnTable.setValueAt(false, i, 0);
				}
				returnTable.setColumnReadOnly(0, true);
											
				FDialog.info(windowNo, null, "", "Process Return Berhasil", "Info");
				cetakNota.setEnabled(true);	
				
				
				return;
			}
			
		}
		


			
		
		StringBuilder SQLCekInv = new StringBuilder();
		SQLCekInv.append("SELECT C_Invoice_ID");
		SQLCekInv.append(" FROM C_Invoice ");
		SQLCekInv.append(" WHERE AD_Client_ID = "+ AD_Client_ID);
		SQLCekInv.append(" AND C_Order_ID = "+ pos.getC_Order_ID());
		
		int C_InvoiceOld_ID = DB.getSQLValueEx(null, SQLCekInv.toString());

		
		if(C_InvoiceOld_ID <= 0){
			
			FDialog.info(windowNo, null, "", "Invoice untuk Nota "+ pos.getDocumentNo()+ " Belum Terdaftar", "Info");
			return;
			
		}

//		StringBuilder SQLCekAR = new StringBuilder();
//		SQLCekAR.append("SELECT C_Payment_ID");
//		SQLCekAR.append(" FROM C_Payment ");
//		SQLCekAR.append(" WHERE AD_Client_ID = "+ AD_Client_ID);
//		SQLCekAR.append(" AND C_Invoice_ID = "+ C_InvoiceOld_ID);
//		
		//int C_PaymentOld_ID = DB.getSQLValueEx(null, SQLCekAR.toString());
		
		
		Map<Integer, Integer> C_PaymentOld_ID = new HashMap<Integer, Integer>();
		C_PaymentOld_ID = getpayment(AD_Client_ID, C_InvoiceOld_ID,ord.getC_Order_ID());
		
//		if(pay_id <= 0){
//			
//			FDialog.info(windowNo, null, "", "Penerimaan untuk Nota "+ pos.getDocumentNo()+ " Belum Terdaftar", "Info");
//			return;
//			
//		}

		
		MInvoice invCek = new MInvoice(ctx, C_InvoiceOld_ID, null);
		

		
			StringBuilder SQLDocType = new StringBuilder();
			SQLDocType.append("SELECT C_DocType_ID ");
			SQLDocType.append("FROM C_DocType ");
			SQLDocType.append("WHERE AD_Client_ID = ? ");
			SQLDocType.append("AND IsSoTrx = 'Y' ");
			SQLDocType.append("AND DocBaseType = ? ");
			SQLDocType.append("AND DocSubTypeSO = ? ");
			
			String docBaseType = MDocType.DOCBASETYPE_SalesOrder;
			String docSubTypeSO = MDocType.DOCSUBTYPESO_ReturnMaterial;
			int C_DocType_ID = DB.getSQLValue(ctx.toString(),SQLDocType.toString(),new Object[] { AD_Client_ID, docBaseType,docSubTypeSO});
			
			//Create RMA
			MRMA rma = new MRMA(ctx, 0, null);
			rma.setAD_Org_ID(AD_Org_ID);
			rma.setName(namaReturn.getValue());
			rma.setDescription(keteranganTextBox.getValue());
			rma.set_CustomColumn("M_Warehouse_ID", inOut.getM_Warehouse_ID());
			rma.setM_InOut_ID(M_InOut_ID);
			rma.setM_RMAType_ID((int) tipeRMAList.getValue());
			rma.setC_DocType_ID(C_DocType_ID);
			rma.setC_BPartner_ID(inOut.getC_BPartner_ID());
			rma.setSalesRep_ID(inOut.getSalesRep_ID());
			rma.setIsSOTrx(true);
			rma.saveEx();
						
			for (int i = 0 ; i < returnTable.getRowCount() ; i++){
				MRMALine rLine = new MRMALine(ctx, 0, null);
				boolean isSelected = (boolean) returnTable.getValueAt(i, 0);
				
				if(isSelected){
					
					KeyNamePair inLine = (KeyNamePair) returnTable.getValueAt(i, 1);
					int M_InOutLine_ID = inLine.getKey();
					BigDecimal qtyReturn = (BigDecimal) returnTable.getValueAt(i, 8);
					
					MInOutLine inOutLine = new MInOutLine(ctx, M_InOutLine_ID, null);
					MOrderLine ordLine = new MOrderLine(ctx, inOutLine.getC_OrderLine_ID(), null);
					
					rLine.setAD_Org_ID(AD_Org_ID);
					rLine.setM_RMA_ID(rma.getM_RMA_ID());
					rLine.setDescription(rma.getDescription());
					rLine.setM_InOutLine_ID(M_InOutLine_ID);
					rLine.set_CustomColumn("M_AttributeSetInstance_ID", inOutLine.getM_AttributeSetInstance_ID());
					rLine.setM_Product_ID(inOutLine.getM_Product_ID());
					rLine.setQty(qtyReturn);
					rLine.setC_Tax_ID(ordLine.getC_Tax_ID());
					rLine.setAmt(ordLine.getPriceEntered());
					rLine.setLineNetAmt(ordLine.getPriceEntered().multiply(qtyReturn));
					rLine.saveEx();
				}
				
			}
			
			rma.setDocAction(MRMA.DOCSTATUS_Completed);
			rma.processIt(MRMA.ACTION_Complete);
			rma.saveEx();
			
			noCustomerRMA.setValue(rma.getDocumentNo());
			
			//create Customer Return
			MInOut retHead = new MInOut(ctx, 0, null);
			retHead.setAD_Org_ID(AD_Org_ID);
			retHead.setM_RMA_ID(rma.getM_RMA_ID());
			retHead.setPOReference(ord.getDocumentNo());
			retHead.setDescription(rma.getDescription());
			
			int C_DocTypeRet_ID = getIDFromComboBox(tipeDokumenField,MDocType.Table_Name,MDocType.COLUMNNAME_Name);

			ret = new MInOut(ctx, 0, null);

			ret.setAD_Org_ID(AD_Org_ID);
			ret.setDocStatus (MInOut.DOCSTATUS_Drafted);		//	Draft
			ret.setDocAction(MInOut.DOCACTION_Complete);
			ret.setC_DocType_ID (C_DocTypeRet_ID);
			ret.setIsSOTrx(true);
			ret.setM_Warehouse_ID(inOut.getM_Warehouse_ID());
			ret.setMovementType(MInOut.MOVEMENTTYPE_CustomerReturns);
			ret.setDateOrdered (inOut.getDateOrdered());
			
			Date tgl = tanggalReturn.getValue();
			SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd 00:00:00");
			String tglTostr = df.format(tgl);
			Timestamp dateShip = Timestamp.valueOf(tglTostr);
			
			ret.setDateAcct (dateShip);
			ret.setMovementDate(dateShip);
			ret.setC_BPartner_ID(rma.getC_BPartner_ID());
			ret.setC_BPartner_Location_ID(ord.getC_BPartner_Location_ID());
			ret.setM_RMA_ID(rma.getM_RMA_ID());
			ret.setPOReference(inOut.getPOReference()); 
			ret.saveEx();
			noCustomerReturn.setValue(ret.getDocumentNo());
			
			//int Loc_ID = (int) locatorSearch.getValue();
			Integer Loc_ID = getIDFromComboBox(locatorSearch, MLocator.Table_Name,MLocator.COLUMNNAME_Value);
		
			
			MRMALine[] lines = rma.getLines(true);
			for(MRMALine line : lines){
				MInOutLine receipLine = new MInOutLine(ctx, line.getM_InOutLine_ID(), null);
				MInOutLine inLine = new MInOutLine(ctx, 0, null);

				inLine.setAD_Org_ID(AD_Org_ID);
				inLine.setM_InOut_ID(ret.getM_InOut_ID());
				inLine.setM_RMALine_ID(line.getM_RMALine_ID());
				inLine.setDescription(line.getDescription());
				inLine.setQtyEntered(line.getQty());
				inLine.setMovementQty(line.getQty());
				inLine.setC_UOM_ID(receipLine.getC_UOM_ID());
				inLine.setM_Product_ID(line.getM_Product_ID());
				inLine.setQty(line.getQty());
				if(receipLine.getM_AttributeSetInstance_ID() > 0){
					inLine.setM_AttributeSetInstance_ID(receipLine.getM_AttributeSetInstance_ID());
				}
				inLine.setM_Locator_ID(Loc_ID);

				inLine.saveEx();
			}
			
			ret.processIt(MInOut.DOCACTION_Complete);
			ret.saveEx();
			
			ARCreditMemo = new MInvoice(ctx, 0, null);
				
			StringBuilder SQLDocTypeARC = new StringBuilder();
			SQLDocTypeARC.append("SELECT C_DocType_ID ");
			SQLDocTypeARC.append("FROM  C_DocType ");
			SQLDocTypeARC.append("WHERE AD_Client_ID = ?");
			SQLDocTypeARC.append("AND DocBaseType = '" + MDocType.DOCBASETYPE_ARCreditMemo+ "' ");
			SQLDocTypeARC.append("AND IsSoTrx ='Y' ");
			
			int C_DocTypeAPC_ID = DB.getSQLValueEx(null, SQLDocTypeARC.toString(), AD_Client_ID);

			ARCreditMemo.setClientOrg(inOut.getAD_Client_ID(), inOut.getAD_Org_ID());
			ARCreditMemo.setDocStatus(MInvoice.DOCSTATUS_Drafted);		//	Draft
			ARCreditMemo.setDocAction(MInvoice.DOCACTION_Complete);
			ARCreditMemo.setM_PriceList_ID(ord.getM_PriceList_ID());
			ARCreditMemo.setPaymentRule(ord.getPaymentRule());
			ARCreditMemo.setC_DocTypeTarget_ID (C_DocTypeAPC_ID);
			ARCreditMemo.setIsSOTrx(true);
			ARCreditMemo.setDateInvoiced (ret.getDateAcct());
			ARCreditMemo.setDateAcct (ret.getDateAcct());
			ARCreditMemo.setC_BPartner_ID(ret.getC_BPartner_ID());
			ARCreditMemo.setM_RMA_ID(ret.getM_RMA_ID());
			if (ret.getC_Order_ID() != 0)
				{
					MOrder peer = new MOrder (ret.getCtx(), ret.getC_Order_ID(), ret.get_TrxName());
					if (peer.getRef_Order_ID() != 0)
						ARCreditMemo.setC_Order_ID(peer.getRef_Order_ID());
				}
			if (ret.getM_RMA_ID() != 0)
				{
					MRMA peer = new MRMA (ret.getCtx(), ret.getM_RMA_ID(), ret.get_TrxName());
					if (peer.getM_RMA_ID() > 0)
						ARCreditMemo.setM_RMA_ID(peer.getM_RMA_ID());
				}
			ARCreditMemo.saveEx();
			ARCreditMemo.setIsTaxIncluded(getTaxIncluded(ord.getM_PriceList_ID(), ARCreditMemo.getC_Invoice_ID()));
			ARCreditMemo.saveEx();

			noARCreditMemo.setValue(ARCreditMemo.getDocumentNo());	
			
			MInOutLine[] retLines = ret.getLines();
			
			for(MInOutLine line : retLines){
				MInvoiceLine ARCrMemoLine = new MInvoiceLine(ctx, 0, null);
				MRMALine rmaLine = new MRMALine(ctx, line.getM_RMALine_ID(), null);
				MInOutLine receiptLine = new MInOutLine(ctx, rmaLine.getM_InOutLine_ID(), null);
				MOrderLine ordLine = new MOrderLine(ctx, receiptLine.getC_OrderLine_ID(),null);
				ARCrMemoLine.setC_Invoice_ID(ARCreditMemo.getC_Invoice_ID());
				ARCrMemoLine.setRMALine(rmaLine);
				ARCrMemoLine.setPriceList(ordLine.getPriceList());
				ARCrMemoLine.saveEx();
				
			}
			
			ARCreditMemo.processIt(MInvoice.DOCACTION_Complete);
			ARCreditMemo.saveEx();
			
			
			if(!invCek.isPaid()){
				
				createAutoARReceipt(false, invCek.getC_Invoice_ID(),ARCreditMemo.getC_Invoice_ID(), CreatedByPOS_ID, SM_SemeruPOS_ID, ret.getM_InOut_ID(),dateShip,C_PaymentOld_ID,supervisor_id);
				
				
			}else if(invCek.isPaid()){
				
					
					createAutoARReceipt(true,invCek.getC_Invoice_ID(),ARCreditMemo.getC_Invoice_ID(), CreatedByPOS_ID, SM_SemeruPOS_ID, ret.getM_InOut_ID(),dateShip,C_PaymentOld_ID,supervisor_id);
				
			}
			
			
			process.setEnabled(false);
			returnTable.setEnabled(false);
			namaReturn.setEnabled(false);
			orgSearch.setReadWrite(false);
			locatorSearch.setEnabled(false);
			tipeDokumenField.setEnabled(false);
			tipeRMAList.setReadWrite(false);
			tanggalReturn.setEnabled(false);
			noNota.setReadWrite(false);
			keteranganTextBox.setEnabled(false);
			
			for (int i = 0 ; i < returnTable.getRowCount(); i++  ){
				returnTable.setValueAt(false, i, 0);
			}
			returnTable.setColumnReadOnly(0, true);
						
			//confirmationPayment(ARCreditMemo.getC_Invoice_ID(), ret.getM_InOut_ID(), supervisor_id);			

			
			//saveToDecorisWindow(AR_CreditMemo_ID,Return_ID,nilaiBayarTunai,nilaiBayarBank,nilaiBayarLeasing,nilaiBayarHutang,lease.toUpperCase(),Supervisor_ID);
			FDialog.info(windowNo, null, "", "Process Return Berhasil", "Info");
			cetakNota.setEnabled(true);	
		
			
			
			
		
	}

	
	
	public void confirmationPayment(final int AR_CreditMemo_ID,final int Return_ID,final int Supervisor_ID){
		
		//pop up confirmation payment type for return product
		try {
			
			Grid inputGrid = GridFactory.newGridLayout();
			Panel paraPanel = new Panel();
			Rows rows = null;
			Row row = null;
			
			final Label tunai = new Label("Tunai");
			final Label bank = new Label("Bank");
			final Label leasing = new Label("Leasing");
			final Label hutang = new Label("Hutang");
			final Decimalbox payTunai = new Decimalbox();
			final Decimalbox payBank = new Decimalbox();
			final Decimalbox payLeasing = new Decimalbox();
			final Decimalbox payHutang = new Decimalbox();
			
			//Label bankAccount = new Label("Bank :");
			Label leasingProv = new Label("Leasing Provider");

//			MLookup lookupBank = MLookupFactory.get(ctx, formCustomerRMA.getWindowNo(), 0,3880, DisplayType.TableDir);
//			final WTableDirEditor bankEditor = new WTableDirEditor("C_BankAccount_ID", true,false, true, lookupBank);
//			bankEditor.addValueChangeListener(this);
//			bankEditor.setMandatory(true);
				
			MLookup leasingLookup = MLookupFactory.get(ctx, formCustomerRMA.getWindowNo(), 0,331558, DisplayType.List);
			final WTableDirEditor leaseEditor = new WTableDirEditor("leaseprovider", true,false, true, leasingLookup);
			leaseEditor.addValueChangeListener(this);
			
			String title = "Korfirmasi Pembayaran Return";		
			
			final Window w = new Window();
			w.setTitle(title);
			
			Borderlayout mainbBorder = new Borderlayout();
			w.appendChild(mainbBorder);
			w.setWidth("350px");
			w.setHeight("250px");
			w.setBorder("normal");
			w.setPage(this.getForm().getPage());
			w.setClosable(false);
			w.setSizable(true);
			
			mainbBorder.setWidth("100%");
			mainbBorder.setHeight("100%");
			String grid = "border: 1px solid #C0C0C0; border-radius:5px; vertical-align:bottom;overflow:auto;";
			paraPanel.appendChild(inputGrid);

			South south = new South();
			south.setStyle(grid);
			mainbBorder.appendChild(south);
			south.appendChild(paraPanel);
			south.setSplittable(false);
			
			inputGrid.setWidth("100%");
			inputGrid.setStyle("Height:100%;align:left");
			inputGrid.setHflex("min");
			
			rows = inputGrid.newRows();
		
			row = rows.newRow();
			row.appendCellChild(tunai.rightAlign());
			row.appendCellChild(payTunai);
			payTunai.setHflex("true");
			payTunai.setStyle("text-align:right;");
			payTunai.setValue("0.00");
			payTunai.setFormat("#,###,###,##0.00" );
			payTunai.setMaxlength(32);
			//payTunai.addEventListener(0, Events.ON_BLUR, this);

	
			row = rows.newRow();
			row.appendCellChild(bank.rightAlign());
			row.appendCellChild(payBank);
			payBank.setHflex("true");
			payBank.setStyle("text-align:right;");
			payBank.setValue("0.00");
			payBank.setFormat("#,###,###,##0.00" );
			payBank.setMaxlength(32);


			row = rows.newRow();
			row.appendCellChild(leasing.rightAlign());
			row.appendCellChild(payLeasing);
			payLeasing.setHflex("true");
			payLeasing.setStyle("text-align:right;");
			payLeasing.setValue("0.00");
			payLeasing.setFormat("#,###,###,##0.00" );
			payLeasing.setMaxlength(32);
			payLeasing.addEventListener(0, Events.ON_BLUR, new EventListener<Event>() {

				@Override
				public void onEvent(Event ev) throws Exception {

					if (ev.getTarget().equals(payLeasing)) {

						BigDecimal leasingAmt = payLeasing.getValue();
						
						if(leasingAmt == null){	
							leasingAmt = Env.ZERO;
						}
						
						if(leasingAmt.compareTo(Env.ZERO) > 0){
							
							leaseEditor.setReadWrite(true);
							
						}
						
						
					}

				}

			});
			


			row = rows.newRow();
			row.appendCellChild(hutang.rightAlign());
			row.appendCellChild(payHutang);
			payHutang.setHflex("true");
			payHutang.setStyle("text-align:right;");
			payHutang.setValue("0.00");
			payHutang.setFormat("#,###,###,##0.00" );
			payHutang.setMaxlength(32);

//			row = rows.newRow();
//			row.appendCellChild(bankAccount.rightAlign());
//			row.appendCellChild(bankEditor.getComponent(),1);
//			bankEditor.getComponent().setHflex("true");
			
			row = rows.newRow();
			row.appendCellChild(leasingProv.rightAlign());
			row.appendCellChild(leaseEditor.getComponent(),1);
			leaseEditor.getComponent().setHflex("true");
			leaseEditor.setReadWrite(false);

			
			Vbox vbox = new Vbox();
			w.appendChild(vbox);
			vbox.appendChild(new Separator());
			final ConfirmPanel panel = new ConfirmPanel(false, false, false, false, false, false, false);
			vbox.appendChild(panel);
			panel.addActionListener(Events.ON_CLICK, new EventListener<Event>() {

				@Override
				public void onEvent(Event event) throws Exception {
					if (event.getTarget() == panel.getButton(ConfirmPanel.A_CANCEL)) {
						FDialog.info(windowNo, null, "", "", "Info");
						cetakNota.setEnabled(true);			
						w.dispose();
					}
					else if(event.getTarget() == panel.getButton(ConfirmPanel.A_OK)){
						
						BigDecimal nilaiBayarTunai = Env.ZERO;
						BigDecimal nilaiBayarBank = Env.ZERO;
						BigDecimal nilaiBayarLeasing = Env.ZERO;
						BigDecimal nilaiBayarHutang= Env.ZERO;
						String lease = "";

						//validation
						
						
						if(leaseEditor.getValue() != null){
							lease = (String) leaseEditor.getValue();
						}
						
						if(payTunai.getValue() != null){
							nilaiBayarTunai= payTunai.getValue();
						}
						if(payBank.getValue() != null){
							nilaiBayarBank = payBank.getValue();
						}
						if(payLeasing.getValue() != null){
							nilaiBayarLeasing = payLeasing.getValue();
						}
						if(payHutang.getValue() != null){
							nilaiBayarHutang = payHutang.getValue();
						}
						
						if(nilaiBayarTunai.compareTo(Env.ZERO)<0 && 
							nilaiBayarBank.compareTo(Env.ZERO) <0 &&
							nilaiBayarLeasing.compareTo(Env.ZERO)<0 &&
							nilaiBayarHutang.compareTo(Env.ZERO)<0){
							
							
							FDialog.info(windowNo, null, "", "Pembayaran return belum ditentukan", "Info");
							return;
						}
						
						if(nilaiBayarLeasing.compareTo(Env.ZERO) > 0){
							
							if(lease == null || lease == ""){
								
								FDialog.info(windowNo, null, "", "Leasing provider belum di input", "Info");
								return;
								
							}
							
						}
						
							
						saveToSemeruWindow1(AR_CreditMemo_ID,Return_ID,nilaiBayarTunai,nilaiBayarBank,nilaiBayarLeasing,nilaiBayarHutang,lease.toUpperCase(),Supervisor_ID);
						FDialog.info(windowNo, null, "", "Process Return Berhasil", "Info");
						cetakNota.setEnabled(true);			


					}
					
					w.onClose();
					
				}
				
			});
			
			w.addEventListener(DialogEvents.ON_WINDOW_CLOSE, new EventListener<Event>() {

				@Override
				public void onEvent(Event event) throws Exception {
					w.dispose();
				}
			});
		w.doHighlighted();		
			
		} catch (Exception err) {

			log.log(Level.SEVERE, this.getClass().getCanonicalName()
					+ ".valueChange - ERROR: " + err.getMessage(), err);
		}
		//end confirmation alamat
		

		
	}
	
	
public void saveToSemeruWindow1(int AR_CreditMemo_ID,int Return_ID,BigDecimal tunai,BigDecimal bank,BigDecimal leasing,BigDecimal hutang,String lease,int Supervidor_ID){
		
		MInvoice ARCrMemo = new MInvoice(ctx, AR_CreditMemo_ID, null);
		MInOut retur = new MInOut(ctx, Return_ID, null);
		
		MSemeruPOS smrPos = new MSemeruPOS(ctx, 0, null);
		smrPos.setAD_Org_ID(ARCrMemo.getAD_Org_ID());
		smrPos.setC_BPartner_ID( ARCrMemo.getC_BPartner_ID());
		
		StringBuilder sqlBPLoc = new StringBuilder();
		sqlBPLoc.append("SELECT C_BPartner_Location_ID ");
		sqlBPLoc.append("FROM C_BPartner_Location ");
		sqlBPLoc.append("WHERE C_BPartner_ID = ? ");
		int C_BPartner_Location_ID = DB.getSQLValueEx(ctx.toString(),sqlBPLoc.toString(), new Object[] {  retur.getC_BPartner_ID() });

		smrPos.setC_BPartner_Location_ID(C_BPartner_Location_ID);
		smrPos.setDateOrdered(ARCrMemo.getDateInvoiced());
		smrPos.setDescription(ARCrMemo.getDescription());
		smrPos.setM_PriceList_ID(ARCrMemo.getM_PriceList_ID());
		smrPos.setM_Warehouse_ID(retur.getM_Warehouse_ID());
		smrPos.setDeliveryRule(MOrder.DELIVERYRULE_AfterReceipt);
		smrPos.setC_PaymentTerm_ID(ARCrMemo.getC_PaymentTerm_ID());
		smrPos.setPaymentRule(MOrder.PAYMENTRULE_MixedPOSPayment);
		smrPos.setC_Invoice_ID(ARCrMemo.getC_Invoice_ID());
		if(CreatedByPOS_ID > 0){
			smrPos.setCreatedByPos_ID(CreatedByPOS_ID);
		}
		smrPos.setSalesRep_ID(ARCrMemo.getSalesRep_ID());
		smrPos.setdpp(Env.ZERO);
		smrPos.setTaxAmt(Env.ZERO);
		if(Supervidor_ID>0){
			
			smrPos.setSupervisor_ID(Supervidor_ID);
			// Date set to Login Date
			Calendar cal = Calendar.getInstance();
			cal.setTime(Env.getContextAsDate(ctx, "#Date"));
			cal.set(Calendar.HOUR_OF_DAY, 0);
			cal.set(Calendar.MINUTE, 0);
			cal.set(Calendar.SECOND, 0);
			cal.set(Calendar.MILLISECOND, 0);
			//tanggalSearch.setValue(new Timestamp(cal.getTimeInMillis()));
			
			smrPos.set_CustomColumn("ApproveDate", new Timestamp(cal.getTimeInMillis()));
			
		}

		String decPOsdocType = "SELECT C_DocType_ID "
				+ " FROM C_DocType "
				+ " WHERE AD_Client_ID = ?"
				+ " AND DocBaseType = ? ";
		String docDec = "POS";
		
		
		int decDoc_ID = DB.getSQLValueEx(null, decPOsdocType, new Object[]{AD_Client_ID,docDec});
		
		
		smrPos.setC_DocType_ID(decDoc_ID);
		smrPos.setDeliveryViaRule(retur.getDeliveryViaRule());
		
		if(leasing.compareTo(Env.ZERO)> 0){
			smrPos.set_ValueOfColumnReturningBoolean("IsLeasing", true);
			
			if(lease.toUpperCase().equals("SPEKTRA")){
				smrPos.set_ValueOfColumnReturningBoolean("IsSpektra", true);
				
			}

		}
		
		if(tunai.compareTo(Env.ZERO) > 0){
			
			smrPos.setPayType1("TUNAI");
			smrPos.setPembayaran1(tunai);
			
		}
		if(bank.compareTo(Env.ZERO) > 0){
			
			smrPos.setPayType2("BANK");
			smrPos.setPembayaran2(bank);
			
		}
		if(leasing.compareTo(Env.ZERO) > 0){
			
			smrPos.setPayType3("LEASING");
			smrPos.setPembayaran3(leasing);
			
		}
		if(hutang.compareTo(Env.ZERO) > 0){
			
			smrPos.setPayType4("HUTANG");
			smrPos.setPembayaran4(hutang);
			
		}
		
		smrPos.setIsPembatalan(false);
		smrPos.setIsSOTrx(true);
		smrPos.setIsPenjualan(false);
		smrPos.setIsPembayaran(false);
		smrPos.setIsReceipt(false);
		smrPos.setIsReturn(true);
		smrPos.saveEx();
		
		MInvoiceLine[] lines = ARCrMemo.getLines();
		
		for (MInvoiceLine line : lines){
			
			X_SM_SemeruPOSLine smrPOSLine = new X_SM_SemeruPOSLine(ctx, 0,null);
			MInOutLine returnLine = new MInOutLine(ctx, line.getM_InOutLine_ID(), null);
						
			String sqlLine = "SELECT COALESCE(MAX(Line),0)+10 FROM SM_SemeruPOSLine WHERE SM_SemeruPOS_ID =?";
			int ii = DB.getSQLValue(smrPos.get_TrxName(), sqlLine,smrPos.getSM_SemeruPOS_ID());

			// DecorisLine
			smrPOSLine.setAD_Org_ID(smrPos.getAD_Org_ID());
			smrPOSLine.setSM_SemeruPOS_ID(smrPos.getSM_SemeruPOS_ID());
			smrPOSLine.setM_Locator_ID(returnLine.getM_Locator_ID());
			smrPOSLine.setM_Product_ID(line.getM_Product_ID());
			smrPOSLine.setC_UOM_ID(line.getC_UOM_ID());
			smrPOSLine.setC_Tax_ID(line.getC_Tax_ID());
			smrPOSLine.setPriceList(line.getPriceList());
			smrPOSLine.setPriceEntered(line.getPriceEntered());
			smrPOSLine.setQtyOrdered(line.getQtyEntered());
			smrPOSLine.setLineNetAmt(line.getLineNetAmt());
			smrPOSLine.setLine(ii);
			if (line.getM_AttributeSetInstance_ID()> 0) {
				smrPOSLine.setM_AttributeSetInstance_ID(line.getM_AttributeSetInstance_ID());
			}

			smrPOSLine.saveEx();
			
		}
				
	}
	
	
	public String validation(IMiniTable minitable){
		String msg = "";
		String ket = keteranganTextBox.getValue();
		String tipeDoc = tipeDokumenField.getValue();
		Integer Loc_ID = getIDFromComboBox(locatorSearch, MLocator.Table_Name,MLocator.COLUMNNAME_Value);

		
		if(namaReturn.getValue().toString().isEmpty()||namaReturn.getValue()==null){
			msg = "Nama Return Belum Di Isi";
		}
		
		if(tanggalReturn.getValue() == null){
			msg = msg + "\n" +"Tanggal Pengembalian Tidak Boleh Kosong";
		}
		
		if(Loc_ID == null || Loc_ID <= 0){
			msg = msg + "\n" +"Lokasi Belum Di Pilih";
		}
		
		
		if(tipeDoc == null || tipeDoc == "" || tipeDoc.isEmpty()){
			msg = msg + "\n"+"Tipe Dokumen Belum Di Tentukan";
		}
		if(tipeRMAList.getValue()==null){
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
				
				BigDecimal qtyPenerimaan = (BigDecimal) minitable.getValueAt(i, 6);
				BigDecimal qtyReturn = (BigDecimal) minitable.getValueAt(i, 8);
				BigDecimal qtyTotalReturn = QtyTotalRMA.add(qtyReturn);
				if((boolean) minitable.getValueAt(i,0)==true){
					IsSelected = true;	
				}
				
				if((boolean) minitable.getValueAt(i,0)){
					
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
				msg = "Produk Return Belum Ditentukan";
				return msg;
			}
			
		}
			
		return msg;
		
	}
	
	
	private void buatBaru() {

		tipeRMAList.setReadWrite(true);
		tanggalReturn.setEnabled(true);
		keteranganTextBox.setEnabled(true);
		process.setEnabled(true);
		
		TokoSearch.setReadWrite(false);
		orgSearch.setReadWrite(true);
		namaReturn.setEnabled(true);
		namaReturn.setText("");
		locatorSearch.setEnabled(true);
		locatorSearch.setValue(null);
		tipeDokumenField.setEnabled(true);
		noNota.setReadWrite(true);
		noNota.setValue(null);
		noCustomerRMA.setText(null);
		noCustomerRMA.setText(null);
		noARCreditMemo.setText(null);
		cetakNota.setEnabled(false);
		process.setEnabled(true);
		tipeRMAList.setValue(null);
		keteranganTextBox.setText("");
		returnTable.clearTable();
		noCustomerReturn.setText(null);
		orgSearch.setValue(null);
		
		ArrayList<KeyNamePair> listLoc = loadLocator(Env.getAD_Org_ID(ctx));
		locatorSearch.removeAllItems();
		for (KeyNamePair loc : listLoc){
			locatorSearch.appendItem(loc.getName());
		}
	
	}
	
	private ArrayList<KeyNamePair> loadLocator(int AD_Org_ID) {
		ArrayList<KeyNamePair> list = new ArrayList<KeyNamePair>();
		int AD_Client_ID = Env.getAD_Client_ID(Env.getCtx());
		StringBuilder sql = new StringBuilder();
		sql.append("SELECT M_Locator_ID,Value ");
		sql.append("FROM M_Locator ");
		sql.append("WHERE AD_Client_ID = ? ");
		sql.append("AND IsActive = 'Y' ");
		
		if(AD_Org_ID > 0){
			sql.append("AND AD_Org_ID = ? ");
		}

		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			pstmt = DB.prepareStatement(sql.toString(), null);
			pstmt.setInt(1, AD_Client_ID);
			if(AD_Org_ID > 0){
				pstmt.setInt(2, AD_Org_ID);
			}
			rs = pstmt.executeQuery();
			while (rs.next()) {
				list.add(new KeyNamePair(rs.getInt(1), rs.getString(2)));
			}

		} catch (SQLException e) {
			log.log(Level.SEVERE, sql.toString(), e);
		} finally {
			DB.close(rs, pstmt);
			rs = null;
			pstmt = null;
		}

		return list;
	}

	public boolean getTaxIncluded(int m_M_PriceList_ID, int C_Invoice_ID)
	{
		if (m_M_PriceList_ID == 0)
		{
			m_M_PriceList_ID = DB.getSQLValue(null,
				"SELECT M_PriceList_ID FROM C_Invoice WHERE C_Invoice_ID=?",
				C_Invoice_ID);
		}
		MPriceList pl = MPriceList.get(ctx, m_M_PriceList_ID, null);
		return pl.isTaxIncluded();
	}	//	isTaxIncluded
	
	public void OtorisasiProcess(){
			
		
		try {

			Grid inputGrid = GridFactory.newGridLayout();
			Panel paraPanel = new Panel();
			Rows rows = null;
			Row row = null;

			final Textbox userName = new Textbox();
			final Textbox password = new Textbox();
		

			String title = "Konfirmasi Supervisor";
			Label LabelUseName = new Label("Username : ");
			Label LabelPass = new Label("Password : ");
		
			final Window w = new Window();
			w.setTitle(title);

			Borderlayout mainbBorder = new Borderlayout();
			w.appendChild(mainbBorder);
			w.setWidth("300px");
			w.setHeight("150px");
			w.setBorder("normal");
			w.setPage(this.getForm().getPage());
			w.setClosable(true);
			w.setSizable(true);

			mainbBorder.setWidth("100%");
			mainbBorder.setHeight("100%");
			String grid = "border: 1px solid #C0C0C0; border-radius:5px; vertical-align:bottom;overflow:auto;";
			paraPanel.appendChild(inputGrid);

			South south = new South();
			south.setStyle(grid);
			mainbBorder.appendChild(south);
			south.appendChild(paraPanel);
			south.setSplittable(false);

			inputGrid.setWidth("100%");
			inputGrid.setStyle("Height:100%;align:left");
			inputGrid.setHflex("min");

			rows = inputGrid.newRows();

			row = rows.newRow();
			row.appendCellChild(LabelUseName.rightAlign());
			row.appendCellChild(userName, 1);
			userName.setHflex("true");
			
			row = rows.newRow();
			row.appendCellChild(LabelPass.rightAlign());
			row.appendCellChild(password, 1);
			password.setHflex("true");
			password.setType("password");

			Vbox vbox = new Vbox();
			w.appendChild(vbox);
			vbox.appendChild(new Separator());
			final ConfirmPanel panel = new ConfirmPanel(true, false, false,false, false, false, false);
			vbox.appendChild(panel);
			panel.addActionListener(Events.ON_CLICK,
					new EventListener<Event>() {

						@Override
						public void onEvent(Event event) throws Exception {
							if (event.getTarget() == panel.getButton(ConfirmPanel.A_CANCEL)) {
								FDialog.info(windowNo, null, "","Return Belum Terproses,Silahkan Entri Konfirmasi Otorisasi Kembali Untuk Melakukan Proses","Info");
								w.dispose();
							} else if (event.getTarget() == panel.getButton(ConfirmPanel.A_OK)) {
								
								String uName = userName.getValue();
								String passwd = password.getValue();
								StringBuilder SQLCek = new StringBuilder();
								
								SQLCek.append("SELECT count (AD_User_ID) ");
								SQLCek.append(" FROM AD_User");
								SQLCek.append(" WHERE AD_User_ID IN (");
								
								SQLCek.append(" SELECT description::NUMERIC");
								SQLCek.append(" FROM AD_Param");
								SQLCek.append(" WHERE AD_Client_ID = "+AD_Client_ID); 
								SQLCek.append(" AND Value ='"+ ReturnVisor+"')");
								SQLCek.append(" AND Name = '"+uName+"'");
								SQLCek.append(" AND Password = '"+passwd+"'"); 
								
							
								int cek = DB.getSQLValueEx(null, SQLCek.toString());
							
								if(cek > 0){
											
									StringBuilder SQLGetSpv = new StringBuilder();
									SQLGetSpv.append(" SELECT description::NUMERIC");
									SQLGetSpv.append(" FROM AD_Param");
									SQLGetSpv.append(" WHERE AD_Client_ID = "+AD_Client_ID); 
									SQLGetSpv.append(" AND Value ='"+ReturnVisor+"'");
									int supervisor_id = DB.getSQLValueEx(null, SQLGetSpv.toString());

									process(supervisor_id);
									
								}else{
									
									FDialog.info(windowNo, null, "","UserName atau Password Tidak Terdaftar, Silahkan Entri Ulang Otorisasi","Info");
									return;
									
								}
								
								
							}

							w.onClose();


						}

					});

			w.addEventListener(DialogEvents.ON_WINDOW_CLOSE,
					new EventListener<Event>() {

						@Override
						public void onEvent(Event event) throws Exception {
							w.dispose();
						}
					});
			w.doHighlighted();
		
		
		//buatBaru();
		}catch (Exception err) {
			
			log.log(Level.SEVERE, this.getClass().getCanonicalName()+ ".valueChange - ERROR: " + err.getMessage(), err);
		}
		
	}
	
}
