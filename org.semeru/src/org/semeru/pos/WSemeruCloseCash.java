package org.semeru.pos;

import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Properties;
import java.util.Vector;
import java.util.logging.Level;

import org.adempiere.util.Callback;
import org.adempiere.util.ProcessUtil;
import org.adempiere.webui.apps.AEnv;
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
import org.adempiere.webui.component.WListbox;
import org.adempiere.webui.editor.WDateEditor;
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
import org.compiere.minigrid.ColumnInfo;
import org.compiere.minigrid.IDColumn;
import org.compiere.minigrid.IMiniTable;
import org.compiere.model.MBPartner;
import org.compiere.model.MClient;
import org.compiere.model.MLookup;
import org.compiere.model.MLookupFactory;
import org.compiere.model.MPInstance;
import org.compiere.model.MProcess;
import org.compiere.process.ProcessInfo;
import org.compiere.process.ProcessInfoParameter;
import org.compiere.util.DB;
import org.compiere.util.DisplayType;
import org.compiere.util.Env;
import org.compiere.util.KeyNamePair;
import org.compiere.util.Trx;
import org.semeru.pos.model.X_SM_SemeruPOS;
import org.semeru.pos.model.X_SM_Semeru_CloseCash;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.Events;
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
 */

public class WSemeruCloseCash extends SemeruCloseCash implements IFormController,
EventListener<Event>, WTableModelListener, ValueChangeListener{

	
	// CustomForm
	private CustomForm form = new CustomForm();
	
	// BorderLayout
	private Borderlayout mainLayout = new Borderlayout();
	private Borderlayout infoLayout = new Borderlayout();
	private Borderlayout summaryLayout = new Borderlayout();

	
	// Panel
	private Panel parameterPanel = new Panel();
	private Panel closeCashPanel = new Panel();
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
	
	// Tanggal Transaksi
	private Label dateTransactionLabel = new Label("Tanggal Transaksi :");
	private WDateEditor dateTransactionField = new WDateEditor();
	private Label until = new Label(" s/d ");
	private WDateEditor dateTransactionField2 = new WDateEditor();
	
	//Nama Kasir
	private Label kasirNameLabel = new Label("Nama Kasir :");
	private Combobox kasirNameField = new Combobox();
	
	//Kriteria Data
	//private String[] criteriaList = new String[] { "Seluruh Data", "Belum Tutup Kas","Sudah Tutup Kas" };
	private Label dataCriteriaLabel = new Label("Kriteria Data :");
	private Combobox dataCriteriaList = new Combobox();

	//Pencarian Data
	private Button searchButton = new Button();
	
	//Proses Tutup Kas
	private Button processCloseCash = new Button();
	
	//Cetak Tutup Kas
	private Button printCloseCash = new Button();
	private Button printCloseCashDetails = new Button();

	
	// Table Data
	private WListbox closeCashTable = ListboxFactory.newDataTableAutoSize();
	
	// Table Data Ringkasan
	private WListbox closeCashTableSummary = ListboxFactory.newDataTable();
		
	
	//variable
	private Properties ctx = Env.getCtx();
	private int AD_Client_ID  = Env.getAD_Client_ID(ctx);
	private int CreatedByPOS_ID = 0;
	private int AD_User_ID = 0;
	private int p_AD_Org_ID = 0;
	private int windowNo = form.getWindowNo();

	//private boolean isSaldoAwal = false;
	private Integer p_Kasir_ID = 0;



	public WSemeruCloseCash(){
		
		boolean  OK = dyInit();

		if(!OK){
			FDialog.warn(windowNo,null,"","User Tidak Mempunyai Akses ke Tutup Kas","Info");
			return;
		}
		
		init();
		
		String msg = cekAkses();
		
		if(msg != null){
			FDialog.warn(windowNo,null,"",msg,"Info");
			
			searchButton.setEnabled(false);
			processCloseCash.setEnabled(false);
			kasirNameField.setEnabled(false);
			dateTransactionField.setReadWrite(false);
			dateTransactionField2.setReadWrite(false);
			
			return;
		}
		
	}
	
	private void init(){
	
		form.appendChild(mainLayout);
		
		form.addEventListener(DialogEvents.ON_WINDOW_CLOSE, new EventListener<Event>(){

			@Override
			public void onEvent(Event arg0) throws Exception {

				//cleanTable(AD_Client_ID,p_AD_Org_ID);
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
		//clientSearch.getComponent().setHflex("true");
		clientSearch.setReadWrite(false);
		
		// Cabang
		row.appendCellChild(orgLabel.rightAlign(), 1);
		row.appendCellChild(org.getComponent(), 1);
		//org.getComponent().setHflex("true");
		row.appendCellChild(new Space(), 1);

	
		//Tanggal Transaksi		
		row = rows.newRow();
		row.appendCellChild(dateTransactionLabel.rightAlign(),1);
		Hbox hBox = new Hbox();
		row.appendCellChild(hBox , 2);
		//hBox.setHflex("true");
		hBox.setAlign("center");
		hBox.appendChild(dateTransactionField.getComponent());
		dateTransactionField.getComponent().setFormat("dd/MM/yyyy");
		//dateTransactionField.getComponent().setWidth("116px");
		hBox.appendChild(until);
		hBox.appendChild(dateTransactionField2.getComponent());
		dateTransactionField2.getComponent().setFormat("dd/MM/yyyy");

		//dateTransactionField2.getComponent().setWidth("116px");

		
		//Nama Kasir
		row = rows.newRow();
		row.appendCellChild(kasirNameLabel.rightAlign(),1);
		row.appendCellChild(kasirNameField, 1);
		//kasirNameField.setHflex("true");
		kasirNameField.setStyle("align:right;");
		kasirNameField.addEventListener(0, "onChange", this);
		kasirNameField.setHeight("24px");
		kasirNameField.setStyle("border-radius:3px;");
		kasirNameField.setMaxlength(60);
		
		//Kriteria Data
		row = rows.newRow();
		row.appendCellChild(dataCriteriaLabel.rightAlign(),1);
		row.appendCellChild(dataCriteriaList, 1);
		dataCriteriaList.setWidth("100px");
		dataCriteriaList.addEventListener(0, "onChange", this);
		dataCriteriaList.setHeight("24px");
		dataCriteriaList.setStyle("border-radius:3px;");
	//	dataCriteriaList.setHflex("true");
		dataCriteriaList.appendItem("Seluruh Data");
		dataCriteriaList.appendItem("Belum Tutup Kas");
		dataCriteriaList.appendItem("Sudah Tutup Kas");
		//dataCriteriaList.setReadonly(true);
		dataCriteriaList.setMaxlength(60);

		
		
		//Pencarian Data
		row = rows.newRow();
		row.appendCellChild(new Space(), 3);
		row.appendCellChild(searchButton,1);
		searchButton.setLabel("Pencarian Data");
		searchButton.addActionListener(this);
		//searchButton.setHflex("true");
		
		
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
		southRow.appendCellChild( new Space(),1);
		Hbox southHBox = new Hbox();
		southRow.appendCellChild(southHBox , 2);
		//southHBox.setHflex("true");
		southHBox.setAlign("right");
		southHBox.appendChild(processCloseCash);
		processCloseCash.setLabel("Proses Tutup Kas");
		processCloseCash.addActionListener(this);
		processCloseCash.setHflex("true");
		
		southHBox.appendChild(printCloseCash);
		printCloseCash.setLabel("Cetak Tutup Kas");
		printCloseCash.addActionListener(this);
		//printCloseCash.setHflex("true");
		
		southHBox.appendChild(printCloseCashDetails);
		printCloseCashDetails.setLabel("Cetak Tutup Kas Details");
		printCloseCashDetails.addActionListener(this);
		//printCloseCashDetails.setHflex("true");
		
		southRow = southRows.newRow();
		southRow.appendCellChild( new Space(),1);
		southRow.appendCellChild(closeCashTableSummary, 2);
		//closeCashTableSummary.setHeight("100px");
		southRow.appendCellChild( new Space(),1);

		
		south = new South();
		closeCashPanel.appendChild(summaryLayout);
		summaryLayout.appendChild(south);
		closeCashPanel.setWidth("100%");
		closeCashPanel.setHeight("100%");
		summaryLayout.setWidth("100%");
		summaryLayout.setHeight("100%");
		
		Center center = new Center();
		summaryLayout.appendChild(center);
		center.appendChild(closeCashTable);
		closeCashTable.setWidth("100%");
	//	closeCashTable.setHeight("100%");
		closeCashTable.addEventListener(Events.ON_SELECT, this);
		center.setStyle(grid);

		center = new Center();
		mainLayout.appendChild(center);
		center.appendChild(infoLayout);
		//infoLayout.setHflex("1");
		infoLayout.setVflex("1");
		infoLayout.setWidth("100%");
		//infoLayout.setHeight("100%");

		north = new North();
		north.setHeight("100%");
		infoLayout.appendChild(north);
		north.appendChild(closeCashPanel);
		north.setSplittable(true);
		center = new Center();
		infoLayout.appendChild(center);
		

	}
	
	private boolean dyInit(){
		
		boolean IsOK = true;
		
		AD_User_ID = Env.getAD_User_ID(ctx);
		int org_id = 0;
		
		String sqlKasir = "SELECT C_BPartner_ID FROM AD_User WHERE AD_Client_ID = ? AND AD_User_ID = ?";
		CreatedByPOS_ID = DB.getSQLValueEx(ctx.toString(), sqlKasir.toString(),new Object[] { Env.getAD_Client_ID(ctx), AD_User_ID });
		
		if(CreatedByPOS_ID <= 0){
			
			IsOK = false;
			
		}
		
		MLookup lookupClient = MLookupFactory.get(ctx, form.getWindowNo(), 0,14621, DisplayType.TableDir);
		clientSearch = new WTableDirEditor("AD_Table_ID", true,false, true, lookupClient);
		clientSearch.addValueChangeListener(this);
		clientSearch.setMandatory(true);
		clientSearch.setValue(AD_Client_ID);
				
		if (CreatedByPOS_ID > 0){
		String sqlOrg = "SELECT AD_Org_ID FROM C_POS WHERE  AD_Client_ID = ? AND CreatedByPOS_ID = ?";
		org_id = DB.getSQLValueEx(ctx.toString(), sqlOrg, new Object[] {AD_Client_ID, CreatedByPOS_ID });
		p_AD_Org_ID = org_id;
		}
		
		MLookup orgLookup = MLookupFactory.get(ctx, form.getWindowNo(), 0,2163, DisplayType.TableDir);
		org = new WTableDirEditor("AD_Org_ID", true, false, true, orgLookup);
		org.addValueChangeListener(this);
		org.setMandatory(true);
		if(org_id > 0){
			org.setValue(org_id);
		}
		org.setReadWrite(false);	
		
		
		printCloseCash.setEnabled(false);
		printCloseCashDetails.setEnabled(false);
	
		return IsOK;
		
	}
	
	private String cekAkses(){
		String msg = null;
		
		int admin_Role_ID = 0;
		//int kasir_Role_ID = 0;
		//int kasirOL_Role_ID = 0;
		int login_Role_ID = Env.getAD_Role_ID(ctx);
		ArrayList<KeyNamePair> kasirList = null;
		ArrayList<Integer> AdminList = new ArrayList<Integer>();
		ArrayList<Integer> kasirListInclMaster = new ArrayList<Integer>();

		Boolean IsAdmin = false;
		Boolean IsKasir = false;


		String SQLRoleAdmin = "SELECT AD_Role_ID FROM AD_Role WHERE AD_Client_ID = ? AND Name Like '%Admin%' AND AD_Role_ID <> 0";
		admin_Role_ID = DB.getSQLValueEx(ctx.toString(), SQLRoleAdmin.toString(),new Object[] {AD_Client_ID});
		
		PreparedStatement pstmtAdm = null;
		ResultSet rsAdm = null;
		try {
			pstmtAdm = DB.prepareStatement(SQLRoleAdmin.toString(), null);
			pstmtAdm.setInt(1, AD_Client_ID);
			rsAdm = pstmtAdm.executeQuery();
			while (rsAdm.next()) {
			
				AdminList.add(rsAdm.getInt(1));
				
			}

		} catch (SQLException ex) {
			log.log(Level.SEVERE, SQLRoleAdmin.toString(), ex);
		} finally {
			DB.close(rsAdm, pstmtAdm);
			rsAdm = null;
			pstmtAdm = null;
		}
		
//		String SQLRoleKasir = "SELECT AD_Role_ID FROM AD_Role WHERE AD_Client_ID = ? AND Name Like '%Kasir%' AND AD_Role_ID <> 0";
//		kasir_Role_ID = DB.getSQLValueEx(ctx.toString(), SQLRoleKasir.toString(),new Object[] {AD_Client_ID});
//		
//		String SQLRoleKasirOL = "SELECT AD_Role_ID FROM AD_Role WHERE AD_Client_ID = ? AND Name Like '%Online%' AND AD_Role_ID <> 0";
//		kasirOL_Role_ID = DB.getSQLValueEx(ctx.toString(), SQLRoleKasirOL.toString(),new Object[] {AD_Client_ID});
//		String SQLRoleKasirOL = "";
		
		StringBuilder SQLGetMasterRole = new StringBuilder();
		SQLGetMasterRole.append("SELECT Description");
		SQLGetMasterRole.append(" FROM AD_Param ");
		SQLGetMasterRole.append(" WHERE AD_Client_ID = 0");
		SQLGetMasterRole.append(" AND Value = '"+ "MasterRoleKasir"+"'");
		
		String masterValue = DB.getSQLValueString(null, SQLGetMasterRole.toString());

		if (masterValue == null){
			msg = " Master Role Belum Ditentukan";
			return msg;
		}
		
		
		StringBuilder SQLGetKasirRole = new StringBuilder();
		SQLGetKasirRole.append("SELECT AD_Role_ID ");
		SQLGetKasirRole.append(" FROM AD_Role");
		SQLGetKasirRole.append(" WHERE AD_Client_ID = ?");
		SQLGetKasirRole.append(" AND AD_Role_ID IN");
		SQLGetKasirRole.append(" (SELECT AD_Role_ID ");
		SQLGetKasirRole.append(" FROM AD_Role_Included ");
		SQLGetKasirRole.append(" WHERE Included_Role_ID IN ");
		SQLGetKasirRole.append(" (SELECT AD_Role_ID ");
		SQLGetKasirRole.append(" FROM AD_Role ");
		SQLGetKasirRole.append(" WHERE AD_Client_ID = 0 ");
		SQLGetKasirRole.append(" AND Name = '" +masterValue+"'");
		SQLGetKasirRole.append(" )) ");

		
		PreparedStatement pstmtMstr = null;
		ResultSet rsMstr = null;
		try {
			pstmtMstr = DB.prepareStatement(SQLGetKasirRole.toString(), null);
			pstmtMstr.setInt(1, AD_Client_ID);
			rsMstr = pstmtMstr.executeQuery();
			while (rsMstr.next()) {
			
				kasirListInclMaster.add(rsMstr.getInt(1));
				
			}

		} catch (SQLException ex) {
			log.log(Level.SEVERE, SQLGetKasirRole.toString(), ex);
			msg = "ERROR";
		} finally {
			DB.close(rsMstr, pstmtMstr);
			rsMstr = null;
			pstmtMstr = null;
		}
						
		//cek IsAdmin
		for(int i = 0 ; i < AdminList.size();i++){
			
			if(login_Role_ID == AdminList.get(i)){
				IsAdmin = true;
			}
			
		}
		
		//cek IsKasir
		for(int i = 0 ; i < kasirListInclMaster.size();i++){
			
			if(login_Role_ID == kasirListInclMaster.get(i)){
				IsKasir = true;
			}
			
		}
		
		
		if (IsAdmin){
			kasirList = loadKasir(true,CreatedByPOS_ID);	
			processCloseCash.setEnabled(false);
		}else if(IsKasir){
			kasirList = loadKasir(false,CreatedByPOS_ID);
			processCloseCash.setEnabled(true);

		}		
		
		if(kasirList == null){
			
			msg = "Kasir Belum Terdaftar Pada Pos Terminal";
			return msg;
			
		}
		
		if(kasirList.size() > 0){
			
			kasirNameField.removeAllItems();
			for (KeyNamePair kasir : kasirList)
				kasirNameField.appendItem(kasir.getName());
			
			if (login_Role_ID == admin_Role_ID){
				kasirNameField.setSelectedIndex(0);
			}else if(IsKasir){
				
				if(kasirNameField.getItemCount() == 1 ){
					kasirNameField.setSelectedIndex(0);
				}else if(kasirNameField.getItemCount() > 1){
					kasirNameField.setSelectedIndex(1);
				}
				kasirNameField.setReadonly(true);
				kasirNameField.setEnabled(false);
			}		
				
		}
		
		
		return msg;
	}
	
	
	//configure
	protected void configureMiniTable(IMiniTable miniTable) {


			miniTable.setColumnClass(0, IDColumn.class, false);
			miniTable.setColumnClass(1, Timestamp.class, true); 	// 2-DateTrx
			miniTable.setColumnClass(2, Timestamp.class, true); 	// 3-CloseCashDate
			miniTable.setColumnClass(3, String.class, true); 		// 4-DocumentNo
			miniTable.setColumnClass(4, Integer.class, true); 			// 5-SeqTutupKas
			miniTable.setColumnClass(5, BigDecimal.class, true); 	// 7-CashIn
			miniTable.setColumnClass(6, BigDecimal.class, true); 	// 8-CashOut
			miniTable.setColumnClass(7, BigDecimal.class, true); 	// 9-TaxAmt
			miniTable.setColumnClass(8, BigDecimal.class, true); 	// 10-Total
			miniTable.setColumnClass(9, BigDecimal.class, true); 	// 11-TotalOmset
			
			miniTable.autoSize();
			
	}
			
	
	protected Vector<String> getOISColumnNames() {

		// Header Info
		Vector<String> columnNames = new Vector<String>(10);
		columnNames.add("Select");
		columnNames.add("Tgl. Transaksi");
		columnNames.add("Tgl. Tutup Kas");
		columnNames.add("Nomor");
		columnNames.add("Urutan");
		columnNames.add("Saldo Awal");
		columnNames.add("Kas Masuk");
		columnNames.add("Kas Keluar");
		columnNames.add("Pajak");
		columnNames.add("Total");
		columnNames.add("Total Omset");

		return columnNames;

	}
	
	
	//configure
		protected void configureMiniTableSummary(IMiniTable miniTable) {

			miniTable.setColumnClass(0, BigDecimal.class, true); 	// 1-Cash
			miniTable.setColumnClass(1, BigDecimal.class, true); 	// 2-BankPayment
			miniTable.setColumnClass(2, BigDecimal.class, true); 	// 3-LeasingPayment
			miniTable.setColumnClass(3, BigDecimal.class, true); 	// 4-LainLain
			miniTable.setColumnClass(4, BigDecimal.class, true); 	// 5-Hutang

			miniTable.autoSize();

		}
		
		protected Vector<String> getOISColumnNamesSummary() {

			// Header Info
			Vector<String> columnNamesSummary = new Vector<String>(4);
			columnNamesSummary.add("Tunai");
			columnNamesSummary.add("Bank");
			columnNamesSummary.add("Spektra");
			columnNamesSummary.add("Lain-Lain");
			columnNamesSummary.add("Hutang");


			return columnNamesSummary;

		}
	
	
	
	@Override
	public ADForm getForm() {
		return form;
	}
	
	@Override
	public void valueChange(ValueChangeEvent evt) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void tableChanged(WTableModelEvent e) {
		boolean isUpdate = (e.getType() == WTableModelEvent.CONTENTS_CHANGED);
		System.out.println(isUpdate);
	
		
//		int col = e.getColumn();
//		int row = e.getLastRow();
//		int before = -1;
//		ArrayList<Integer> ListRow = new ArrayList<Integer>();
//		
//		for (int i = 0 ; i <closeCashTable.getRowCount(); i++){
//			if((boolean)closeCashTable.getValueAt(i, 0)==true){	
//				if()
//				ListRow.add(i);
//			}
//		}
//		
//		for(int i = 0 ; i < ListRow.size() ; i++){
//			
//			if(ListRow.){
//				
//			}
//			
//		}
//			
//		
//		if(before < 0){
//			before = row;
//		}
	
//		String kasirName = kasirNameField.getValue();
//				
//		StringBuilder SQLKasir_ID = new StringBuilder();		
//		SQLKasir_ID.append("SELECT C_BPartner_ID ");
//		SQLKasir_ID.append("FROM C_BPartner ");
//		SQLKasir_ID.append("WHERE AD_Client_ID = ?");
//		SQLKasir_ID.append("AND Name = '" + kasirName + "'");
//
//		Integer p_Kasir_ID = DB.getSQLValueEx(null, SQLKasir_ID.toString(), AD_Client_ID);


		
//		if (!isUpdate) {
//			return;
//		} else if (isUpdate) {
//			
//			int col = e.getColumn();
//			int row = e.getLastRow();
//			
//			if (col == 0 ){
//				
//				boolean isCek = (boolean) closeCashTable.getValueAt(row, 0);
//				
//				if(isCek){
//				
//					for (int i = 0 ; i < closeCashTable.getRowCount(); i++){
//					
//						if(i != row){
//						
//							closeCashTable.setValueAt(false, i, 0);
//						
//						}
//					}
//				}
//				
//			}
//			
//		}
		
		//
//		for (int i = 0 ; i < closeCashTable.getRowCount(); i++){
//			
//			boolean isCek = (boolean) closeCashTable.getValueAt(i, 0);
//			Timestamp dateTrx = null;
//			Timestamp dateCloseCash = null;
//			String documentNo = "";
//
//			int seqTutupKas = 0;
//
//			
//			if (isCek){
//				dateTrx = (Timestamp) closeCashTable.getValueAt(i, 1);
//				dateCloseCash = (Timestamp) closeCashTable.getValueAt(i, 2);
//				documentNo = (String) closeCashTable.getValueAt(i, 3);
//				seqTutupKas = (int) closeCashTable.getValueAt(i, 4);
//				
//				Vector<Vector<Object>> dataSummary = getCloseCashSummary(AD_Client_ID,p_AD_Org_ID, dateTrx, dateCloseCash,p_Kasir_ID ,documentNo,seqTutupKas);
//				Vector<String> columnNamesSummary = getOISColumnNamesSummary();
//				
//				closeCashTableSummary.clear();
//
//				
//				// Set Model
//				ListModelTable modelP = new ListModelTable(dataSummary);
//				modelP.addTableModelListener(this);
//				closeCashTableSummary.setData(modelP, columnNamesSummary);
//				configureMiniTableSummary(closeCashTableSummary);
//				dataSummary.removeAllElements();		
//				
//			}else{
//				closeCashTableSummary.clear();
//				closeCashTableSummary.clearTable();;
//
//
//			}
//			
//		}
		
	}

	@Override
	public void onEvent(Event e) throws Exception {
		log.config("");
		
		if(e.getTarget().equals(searchButton)){
			
			if(dateTransactionField.getValue()==null ){				
				FDialog.info(windowNo, null, "", "Tanggal Transaksi From Tidak Boleh Kosong", "Info");
				return;
			}
			
			if(dateTransactionField2.getValue()==null ){				
				FDialog.info(windowNo, null, "", "Tanggal Transaksi To Tidak Boleh Kosong", "Info");
				return;
			}
			
			if(kasirNameField.getValue()==null || kasirNameField.getValue() == "" ||kasirNameField.getValue().isEmpty()){
				
				FDialog.info(windowNo, null, "", "Nama Kasir Tidak Boleh Kosong", "Info");
				return;
				
			}
			
			if(dataCriteriaList.getValue()==null || dataCriteriaList.getValue() == "" ||dataCriteriaList.getValue().isEmpty()){
				
				FDialog.info(windowNo, null, "", "Kriteria Data Tidak Boleh Kosong", "Info");
				return;
			}
			
			
			
			
			search();
			printCloseCash.setEnabled(true);
			printCloseCashDetails.setEnabled(true);
			
		}else if(e.getTarget().equals(processCloseCash)){
			
			FDialog.ask(windowNo, null,"Konfirmasi" ,"", "Anda Yakin Melakukan Tutup Kas?", new Callback<Boolean>() {
				
				@Override
				public void onCallback(Boolean result) {

					if(result){
						process();
					}else{
						return;			
					}
					
				}
			});
			
		}else if(e.getTarget().equals(printCloseCashDetails)){
			
			Timestamp dateTrx = null;
			Timestamp dateCloseCash = null;
			int User_Print_ID = 0;
			String kasirName = "";
	 		
			kasirName = kasirNameField.getValue();
			if(kasirName == null){
				kasirName = "";
			}
			
			StringBuilder SQLPrintUser = new StringBuilder();
			SQLPrintUser.append(" SELECT AD_User_ID ");
			SQLPrintUser.append(" FROM AD_User ");
			SQLPrintUser.append(" WHERE AD_Client_ID = ? AND C_BPartner_ID = ( SELECT C_BPartner_ID ");
			SQLPrintUser.append(" FROM C_BPartner ");
			SQLPrintUser.append(" WHERE AD_Client_ID = ? AND Name = '"+ kasirName +"')");

			User_Print_ID = DB.getSQLValueEx(null, SQLPrintUser.toString(), new Object[]{AD_Client_ID,AD_Client_ID});

			
			//
			if(closeCashTable.getRowCount()==0){
				return;
			}
			
			//cek have checked data
			boolean IsChecked = false;
			int row = closeCashTable.getSelectedRow();
			
			if(row >= 0 ){
				IsChecked = true;
			}
					
			if(!IsChecked){
				FDialog.info(windowNo, null, "", "Tidak Ada Transaksi Terpilih Untuk Di Cetak","Info");
				return;
			}
			
			
			dateTrx = (Timestamp) closeCashTable.getValueAt(row, 1);
			dateCloseCash = (Timestamp) closeCashTable.getValueAt(row, 2);
					
			if(dateCloseCash == null){
				FDialog.info(windowNo, null, "", "Transaksi Tidak Dapat Dicetak Karena Belum Dilakukan Tutup Kas", "Info");
				return;
			}
					
						
			String trxName = Trx.createTrxName("PrintCloseCashDetails");
			String url = "/home/idempiere/idempiere.gtk.linux.x86_64/idempiere-server/reports/";
			//String url = "D:\\SourceCode\\iDempiereBase\\reports\\";
			//String url = "D:\\SourceCode\\SIM-Base\\reports\\";

			MProcess proc = new MProcess(Env.getCtx(), 1000101, trxName);
			MPInstance instance = new MPInstance(proc, proc.getAD_Process_ID());
			ProcessInfo pi = new ProcessInfo("Print Tutup Kas", 1000101);
			pi.setAD_PInstance_ID(instance.getAD_PInstance_ID());
						
			ArrayList<ProcessInfoParameter> list = new ArrayList<ProcessInfoParameter>();
			list.add(new ProcessInfoParameter("url_path", url, null, null, null));
			list.add(new ProcessInfoParameter("AD_Client_ID", AD_Client_ID, null, null, null));
			list.add(new ProcessInfoParameter("DateOrdered", dateTrx, null, null, null));
			list.add(new ProcessInfoParameter("DateOrdered2", dateTrx, null, null, null));
			list.add(new ProcessInfoParameter("CreatedByPOS_ID", User_Print_ID, null, null, null));


			ProcessInfoParameter[] pars = new ProcessInfoParameter[list.size()];
			list.toArray(pars);
			pi.setParameter(pars);
						
			Trx trx = Trx.get(trxName, true);
			trx.commit();

			ProcessUtil.startJavaProcess(Env.getCtx(), pi,Trx.get(trxName, true));
			
			
		}else if(e.getTarget().equals(printCloseCash)){
			
			Timestamp dateTrx = null;
			Timestamp dateCloseCash = null;
			int User_Print_ID = 0;
			String kasirName = "";
	 		
			kasirName = kasirNameField.getValue();
			if(kasirName == null){
				kasirName = "";
			}
			
			StringBuilder SQLPrintUser = new StringBuilder();
			SQLPrintUser.append(" SELECT AD_User_ID ");
			SQLPrintUser.append(" FROM AD_User ");
			SQLPrintUser.append(" WHERE AD_Client_ID = ? AND C_BPartner_ID = ( SELECT C_BPartner_ID ");
			SQLPrintUser.append(" FROM C_BPartner ");
			SQLPrintUser.append(" WHERE AD_Client_ID = ? AND Name = '"+ kasirName +"')");

			User_Print_ID = DB.getSQLValueEx(null, SQLPrintUser.toString(), new Object[]{AD_Client_ID,AD_Client_ID});

			
			//
			if(closeCashTable.getRowCount()==0){
				return;
			}
			
			//cek have checked data
			boolean IsChecked = false;
			int row = closeCashTable.getSelectedRow();
			
			if(row >= 0 ){
				IsChecked = true;
			}
					
			if(!IsChecked){
				FDialog.info(windowNo, null, "", "Tidak Ada Transaksi Terpilih Untuk Di Cetak","Info");
				return;
			}
			
			
			dateTrx = (Timestamp) closeCashTable.getValueAt(row, 1);
			dateCloseCash = (Timestamp) closeCashTable.getValueAt(row, 2);
					
			if(dateCloseCash == null){
				FDialog.info(windowNo, null, "", "Transaksi Tidak Dapat Dicetak Karena Belum Dilakukan Tutup Kas", "Info");
				return;
			}
					
						
			String trxName = Trx.createTrxName("PrintCloseCash");
			String url = "/home/idempiere/idempiere.gtk.linux.x86_64/idempiere-server/reports/";
			//String url = "D:\\SourceCode\\iDempiereBase\\reports\\";
			//String url = "D:\\SourceCode\\SIM-Base\\reports\\";

			MProcess proc = new MProcess(Env.getCtx(), 1000073, trxName);
			MPInstance instance = new MPInstance(proc, proc.getAD_Process_ID());
			ProcessInfo pi = new ProcessInfo("Print Tutup Kas", 1000073);
			pi.setAD_PInstance_ID(instance.getAD_PInstance_ID());
						
			ArrayList<ProcessInfoParameter> list = new ArrayList<ProcessInfoParameter>();
			list.add(new ProcessInfoParameter("url_path", url, null, null, null));
			list.add(new ProcessInfoParameter("AD_Client_ID", AD_Client_ID, null, null, null));
			list.add(new ProcessInfoParameter("DateOrdered", dateTrx, null, null, null));
			list.add(new ProcessInfoParameter("DateOrdered2", dateTrx, null, null, null));
			list.add(new ProcessInfoParameter("CreatedByPOS_ID", User_Print_ID, null, null, null));


			ProcessInfoParameter[] pars = new ProcessInfoParameter[list.size()];
			list.toArray(pars);
			pi.setParameter(pars);
						
			Trx trx = Trx.get(trxName, true);
			trx.commit();

			ProcessUtil.startJavaProcess(Env.getCtx(), pi,Trx.get(trxName, true));
			
		}else if(AEnv.contains(closeCashTable, e.getTarget())){
			
			int row = closeCashTable.getSelectedRow();
			
			Timestamp dateTrx = (Timestamp) closeCashTable.getValueAt(row, 1);
			Timestamp dateCloseCash = (Timestamp) closeCashTable.getValueAt(row, 2);
			String documentNo = (String) closeCashTable.getValueAt(row, 3);
			Integer seqTutupKas = (Integer) closeCashTable.getValueAt(row, 4);
			
			Vector<Vector<Object>> dataSummary = getCloseCashSummary(AD_Client_ID,p_AD_Org_ID, dateTrx, dateCloseCash,p_Kasir_ID ,documentNo,seqTutupKas);
			Vector<String> columnNamesSummary = getOISColumnNamesSummary();
			
			closeCashTableSummary.clear();

			
			// Set Model
			ListModelTable modelP = new ListModelTable(dataSummary);
			modelP.addTableModelListener(this);
			closeCashTableSummary.setData(modelP, columnNamesSummary);
			configureMiniTableSummary(closeCashTableSummary);
			dataSummary.removeAllElements();	
			
			
		}

	
	}
	
	private void search(){
		
		Timestamp date1 = (Timestamp) dateTransactionField.getValue();
		Timestamp date2 = (Timestamp) dateTransactionField2.getValue();

		String kasirName = "";
 		
		kasirName = kasirNameField.getValue();
		if(kasirName == null){
			kasirName = "";
		}
 		

		StringBuilder SQLKasir_ID = new StringBuilder();		
		SQLKasir_ID.append("SELECT C_BPartner_ID ");
		SQLKasir_ID.append("FROM C_BPartner ");
		SQLKasir_ID.append("WHERE AD_Client_ID = ?");
		SQLKasir_ID.append("AND Name = '" + kasirName + "'");

		p_Kasir_ID = DB.getSQLValueEx(null, SQLKasir_ID.toString(), AD_Client_ID);
		
		if (p_Kasir_ID == null){
			p_Kasir_ID = 0;
		}
		
		if (p_Kasir_ID == -1){
			p_Kasir_ID = 0;
		}
		
		String kriteriaData = (String) dataCriteriaList.getValue().toString().toUpperCase();
		String kriteria = "";
		
		if (kriteriaData.equals("SELURUH DATA")){
			kriteria = "A";
		}else if(kriteriaData.equals("BELUM TUTUP KAS")){
			kriteria = "B";
		}else if(kriteriaData.equals("SUDAH TUTUP KAS")){
			kriteria = "C";
		}
		
		//Vector<String> columnNames = getOISColumnNames();
		
		ColumnInfo[] layout = null;
		
					layout = new ColumnInfo[] {
					new ColumnInfo("",					".", IDColumn.class, false, false, ""), 	//
					new ColumnInfo("Tgl. Transaksi",    ".", Timestamp.class),						//
					new ColumnInfo("Tgl. Tutup Kas",	".", Timestamp.class),   					//  3
					new ColumnInfo("Nomor",         	".", String.class),
					new ColumnInfo("Urutan", 			".", Integer.class),   						//  5
					new ColumnInfo("Kas Masuk",    		".", BigDecimal.class),
					new ColumnInfo("Kas Keluar",        ".", BigDecimal.class),
					new ColumnInfo("Pajak",      		".", BigDecimal.class),
					new ColumnInfo("Total",          	".", BigDecimal.class),
					new ColumnInfo("Total Omset",      	".", BigDecimal.class)
					
					
			};
		

		
		
		closeCashTable.prepareTable(layout, "", "", false, "");
		closeCashTable.setCheckmark(true);
		tableLoad(AD_Client_ID,p_AD_Org_ID, date1, date2, p_Kasir_ID,kriteria,closeCashTable);
		Vector<Vector<Object>> data = getCloseCashData(AD_Client_ID,p_AD_Org_ID, date1, date2, p_Kasir_ID,kriteria,closeCashTable);

		// Set Model
		ListModelTable modelP = new ListModelTable(data);
		modelP.addTableModelListener(this);
		//closeCashTable.setData(modelP, null);
		//configureMiniTable(closeCashTable);
		data.removeAllElements();	
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
	
	
	public void process(){
		
		
		Timestamp p_DateTrx  = null;
		int AD_User_ID = Env.getAD_User_ID(ctx);
		String clientValue = "";
		String posName = "";
		String kasirName = "";
		String documentNo = "";
		Integer seqTutupKas = 0;
		int SM_SemeruPOS_ID =0;
		boolean ok = false;
		
		X_SM_SemeruPOS decPOS = null;
		X_SM_Semeru_CloseCash dec_closeCash = null;
		
		
		if (closeCashTable.getRowCount()==0){
			return;
		}
		
		//cek have checked data
		boolean IsChecked = false;
		int row = closeCashTable.getSelectedRow();
		
		if(row >= 0 ){
			IsChecked = true;
		}
			
		if(!IsChecked){
			FDialog.info(windowNo, null, "", "Tidak Ada Transaksi Terpilih Untuk Dilakukan Tutup Kas","Info");
			return;
		}
		
		Timestamp dateTrx = null;
		Timestamp dateCloseCash = null;
			
		
		dateTrx = (Timestamp) closeCashTable.getValueAt(row, 1);
		dateCloseCash = (Timestamp) closeCashTable.getValueAt(row, 2);
		p_DateTrx = dateTrx;

				
		if (dateCloseCash != null){
					
			FDialog.info(windowNo, null, "", "Transaksi Sudah Dilakukan Tutup Kas, Tidak Dapat Di Proses Kembali","Info");
			return;
		}	
				
		String p_dateSeq =new SimpleDateFormat("dd-MM-yyyy").format(p_DateTrx);
		String msg = "";
								
		//Kasir_ID
		String sqlKasir = "SELECT C_BPartner_ID FROM AD_User WHERE AD_Client_ID = ? AND AD_User_ID = ?";
		CreatedByPOS_ID =  DB.getSQLValueEx(ctx.toString(), sqlKasir.toString(), new Object[]{AD_Client_ID,AD_User_ID});
				
		//POSName
		String sqlPOSName = "SELECT Name FROM C_POS WHERE AD_Client_ID = ? AND CreatedByPOS_ID = ?";
		posName =  DB.getSQLValueStringEx(ctx.toString(), sqlPOSName.toString(), new Object[]{AD_Client_ID,CreatedByPOS_ID});
				
		//Client Value
		MClient client = new MClient(ctx, AD_Client_ID, null);
		clientValue = client.getValue();
				
		if (clientValue == "" ||clientValue == null || clientValue.isEmpty()){
			FDialog.info(windowNo, null, "", "Toko Tidak Ditemukan","Info");
			return;
		}
				
		String sqlAD_Org = "SELECT AD_Org_ID FROM C_POS WHERE AD_Client_ID = ? AND CreatedByPOS_ID = ?";
		int Org_ID = DB.getSQLValueEx(null, sqlAD_Org, new Object[]{AD_Client_ID,CreatedByPOS_ID});
		
		if(Org_ID <= 0){
			FDialog.info(windowNo, null, "", "Organization tidak ditemukan, Silakan Hubungi Admin", "Info");
			return;
		}
					
		
		int kasir_ID = ((int) getIDFromComboBox(kasirNameField,MBPartner.Table_Name,MBPartner.COLUMNNAME_Name));
		MBPartner ksr = new MBPartner(ctx, kasir_ID, null);
		kasirName = ksr.getName();
		
		
		//seq tutup kas
		StringBuilder sqlSeq = new StringBuilder();
		sqlSeq.append("SELECT MAX(seqTutupKas) ");
		sqlSeq.append("FROM SM_SemeruPOS ");
		sqlSeq.append("WHERE AD_Client_ID= ? ");
		sqlSeq.append("AND CreatedByPOS_ID = ? ");
		sqlSeq.append("AND DateOrdered = '" + dateTrx.toString() + "'");

					
		seqTutupKas = DB.getSQLValueEx(ctx.toString(), sqlSeq.toString(), new Object []{AD_Client_ID,CreatedByPOS_ID});	
				
//		if (seqTutupKas == null){
//			seqTutupKas = 0;
//		}
				
		if (seqTutupKas >= 0){
			seqTutupKas = seqTutupKas + 1;
		}
				
				
		documentNo = clientValue+"/"+posName+"/"+p_dateSeq+"/"+kasirName+"/"+"00"+seqTutupKas.toString() ;
				
		//getData Tutup Kas
		StringBuilder sqlTutupKas = new StringBuilder();
		sqlTutupKas.append(" SELECT "); //1
		sqlTutupKas.append(" AD_Client_ID, "); //1
		sqlTutupKas.append(" AD_Org_ID, ");
		sqlTutupKas.append(" AD_PInstance_ID, ");
		sqlTutupKas.append(" CashIn, ");
		sqlTutupKas.append(" CashOut, "); //5
		sqlTutupKas.append(" CloseCashDate, ");
		sqlTutupKas.append(" CurrentBalance, ");
		sqlTutupKas.append(" DateTrx, ");
		sqlTutupKas.append(" DocumentNoTutupKas, ");
		sqlTutupKas.append(" TaxAmt, "); //10
		sqlTutupKas.append(" Total, ");
		sqlTutupKas.append(" TrxCount, ");
		sqlTutupKas.append(" CreatedByPOS_ID, ");
		sqlTutupKas.append(" SeqTutupKas, ");
		sqlTutupKas.append(" Cash, "); //15
		sqlTutupKas.append(" BankPayment, ");
		sqlTutupKas.append(" LeasingPayment, ");
		sqlTutupKas.append(" Hutang, ");
		sqlTutupKas.append(" OtherLeasingPayment, ");
		sqlTutupKas.append(" totalOmset "); //20
		sqlTutupKas.append(" FROM sm_semeru_closecash_summary_v ");
		sqlTutupKas.append(" WHERE AD_Client_ID= ? ");
		sqlTutupKas.append(" AND CreatedByPOS_ID = ? ");
		sqlTutupKas.append(" AND DateTrx = ?");	
		sqlTutupKas.append(" AND CloseCashDate IS NULL");	

				
		BigDecimal bayarTunai = Env.ZERO;	
		BigDecimal bayarBank = Env.ZERO;		
		BigDecimal bayarLeasingSpektra = Env.ZERO;	
		BigDecimal bayarLeasingOther = Env.ZERO;	
		BigDecimal bayarLainlain  = Env.ZERO;
		BigDecimal kasKeluar  = Env.ZERO;	
		BigDecimal taxAmt  = Env.ZERO;
		BigDecimal curBal  = Env.ZERO;
		BigDecimal totalOmset = Env.ZERO;
		//BigDecimal pengurang = Env.ZERO;

				
		Calendar cal = Calendar.getInstance();
		cal.setTime(Env.getContextAsDate(ctx, "#Date"));
		cal.set(Calendar.HOUR_OF_DAY, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);
		cal.set(Calendar.MILLISECOND, 0);
		Timestamp now = new Timestamp(cal.getTimeInMillis());
					
	
		int countTrx = 0; 
					
		PreparedStatement pstmtCloseCash = null;
		ResultSet rsCloseCash = null;
		try {
			pstmtCloseCash = DB.prepareStatement(sqlTutupKas.toString(), null);
			pstmtCloseCash.setInt(1, AD_Client_ID);
			pstmtCloseCash.setInt(2, CreatedByPOS_ID);
			pstmtCloseCash.setTimestamp(3, p_DateTrx);
			rsCloseCash = pstmtCloseCash.executeQuery();
			while (rsCloseCash.next()) {
						
				bayarTunai = rsCloseCash.getBigDecimal(4);
				kasKeluar = rsCloseCash.getBigDecimal(5);
				bayarBank = rsCloseCash.getBigDecimal(16);
				bayarLeasingSpektra = rsCloseCash.getBigDecimal(17);
				bayarLainlain = rsCloseCash.getBigDecimal(18);
				bayarLeasingOther =rsCloseCash.getBigDecimal(19);
				totalOmset = rsCloseCash.getBigDecimal(20);
				taxAmt = rsCloseCash.getBigDecimal(10);
				curBal = rsCloseCash.getBigDecimal(7);
				//pengurang = kasKeluar.add(bayarBank).add(bayarLeasingSpektra).add(bayarLeasingOther).add(bayarLainlain);		
				
						
				dec_closeCash = new X_SM_Semeru_CloseCash(ctx, 0, null);
				dec_closeCash.setAD_Org_ID(Org_ID);
				dec_closeCash.setDocumentNo(documentNo);
				dec_closeCash.setCloseCashDate(now);
				dec_closeCash.setDateTrx(p_DateTrx);
				//dec_closeCash.setCash(bayarTunai.subtract(pengurang));
				dec_closeCash.setCash(rsCloseCash.getBigDecimal(15));
				dec_closeCash.setBankPayment(bayarBank);
				dec_closeCash.setLeasingPayment(bayarLeasingSpektra);
				dec_closeCash.set_ValueOfColumn("OtherLeasingPayment", bayarLeasingOther);
				dec_closeCash.set_ValueOfColumn("totalOmset", totalOmset);
				dec_closeCash.setLainLain(bayarLainlain);
				dec_closeCash.setCashIn(bayarTunai);
				dec_closeCash.setCashOut(kasKeluar);
				dec_closeCash.setSeqTutupKas(seqTutupKas);
				dec_closeCash.setCreatedByPos_ID(p_Kasir_ID);
				dec_closeCash.setTaxAmt(taxAmt);
				dec_closeCash.setCurrentBalance(curBal);
				dec_closeCash.saveEx();
				ok = true;
			}

		} catch (SQLException ex) {
			log.log(Level.SEVERE, sqlTutupKas.toString(), ex);
			ok = false;
		} finally {
			DB.close(rsCloseCash, pstmtCloseCash);
			rsCloseCash = null;
			pstmtCloseCash = null;
		}
					
				
		if(!ok){		
			msg = "Tutup Kas Tidak Berhasil,Tidak Ada Dokumen Untuk Tutup Kas";
			FDialog.info(windowNo, null, "", msg, "Info");
			return;
		}
					
				
				
		StringBuilder sqlUpdateTutupKas = new StringBuilder();
		sqlUpdateTutupKas.append("SELECT SM_SemeruPOS_ID ");
		sqlUpdateTutupKas.append(" FROM SM_SemeruPOS ");
		sqlUpdateTutupKas.append(" WHERE AD_Client_ID= ? ");
		sqlUpdateTutupKas.append(" AND CreatedByPOS_ID = ? ");
		sqlUpdateTutupKas.append(" AND IsTutupKas = 'N' ");
		sqlUpdateTutupKas.append(" AND DateOrdered = ?");

				
				
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			pstmt = DB.prepareStatement(sqlUpdateTutupKas.toString(), null);
			pstmt.setInt(1, AD_Client_ID);
			pstmt.setInt(2, CreatedByPOS_ID);
			pstmt.setTimestamp(3, p_DateTrx);
			rs = pstmt.executeQuery();
			while (rs.next()) {
					
				countTrx++;
				SM_SemeruPOS_ID = rs.getInt(1);
				decPOS = new X_SM_SemeruPOS(ctx, SM_SemeruPOS_ID, null);
				decPOS.setIsTutupKas(true);
				decPOS.setDocumentNoTutupKas(documentNo);
				decPOS.setSeqTutupKas(seqTutupKas);
				decPOS.set_ValueNoCheck("sm_semeru_CloseCash_ID", dec_closeCash.getSM_Semeru_CloseCash_ID());
				decPOS.saveEx();
					
			}

		} catch (SQLException ex) {
			log.log(Level.SEVERE, sqlUpdateTutupKas.toString(), ex);
			msg = "Error";
		} finally {
			DB.close(rs, pstmt);
			rs = null;
			pstmt = null;
		}
				
		if(dec_closeCash !=null){
			dec_closeCash.setTrxCount(countTrx);
			dec_closeCash.saveEx();
			msg = "Tutup Kas Berhasil Dilakukan";
		}
				
		
		
		
		//cleanTable(AD_Client_ID,p_AD_Org_ID);
		//insertTempTable();
		FDialog.info(windowNo, null, "", "Proses Tutup Kas Berhasil", "Info");
		search();
		
		
		int User_Print_ID = 0;
		String kasirFieldName = "";
 		
		kasirFieldName = kasirNameField.getValue();
		if(kasirFieldName == null){
			kasirFieldName = "";
		}
		
		StringBuilder SQLPrintUser = new StringBuilder();
		SQLPrintUser.append(" SELECT AD_User_ID ");
		SQLPrintUser.append(" FROM AD_User ");
		SQLPrintUser.append(" WHERE AD_Client_ID = ? AND C_BPartner_ID = ( SELECT C_BPartner_ID ");
		SQLPrintUser.append(" FROM C_BPartner ");
		SQLPrintUser.append(" WHERE AD_Client_ID = ? AND Name = '"+ kasirFieldName +"')");

		User_Print_ID = DB.getSQLValueEx(null, SQLPrintUser.toString(), new Object[]{AD_Client_ID,AD_Client_ID});

		
		
		//
		String trxName = Trx.createTrxName("PrintCloseCash");
		String url = "/home/idempiere/idempiere.gtk.linux.x86_64/idempiere-server/reports/";
		//String url = "D:\\SourceCode\\iDempiereBase\\reports\\";
		MProcess proc = new MProcess(Env.getCtx(), 1000073, trxName);
		MPInstance instance = new MPInstance(proc, proc.getAD_Process_ID());
		ProcessInfo pi = new ProcessInfo("Print Tutup Kas", 1000073);
		pi.setAD_PInstance_ID(instance.getAD_PInstance_ID());
		
		ArrayList<ProcessInfoParameter> list = new ArrayList<ProcessInfoParameter>();
		list.add(new ProcessInfoParameter("url_path", url, null, null, null));
		list.add(new ProcessInfoParameter("AD_Client_ID", AD_Client_ID, null, null, null));
		list.add(new ProcessInfoParameter("DateOrdered", p_DateTrx, null, null, null));
		list.add(new ProcessInfoParameter("DateOrdered2", p_DateTrx, null, null, null));
		list.add(new ProcessInfoParameter("CreatedByPOS_ID", User_Print_ID, null, null, null));


		ProcessInfoParameter[] pars = new ProcessInfoParameter[list.size()];
		list.toArray(pars);
		pi.setParameter(pars);
		
		Trx trx = Trx.get(trxName, true);
		trx.commit();

		ProcessUtil.startJavaProcess(Env.getCtx(), pi,Trx.get(trxName, true));

		
	}
	
}
