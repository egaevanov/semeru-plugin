package org.semeru.pos;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Vector;
import java.util.logging.Level;

import org.adempiere.util.Callback;
import org.adempiere.util.ProcessUtil;
import org.adempiere.webui.component.Button;
import org.adempiere.webui.component.Checkbox;
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
import org.compiere.model.MBPartner;
import org.compiere.model.MCharge;
import org.compiere.model.MDocType;
import org.compiere.model.MInvoice;
import org.compiere.model.MInvoiceLine;
import org.compiere.model.MLookup;
import org.compiere.model.MLookupFactory;
import org.compiere.model.MPInstance;
import org.compiere.model.MPayment;
import org.compiere.model.MPeriod;
import org.compiere.model.MProcess;
import org.compiere.model.MTaxCategory;
import org.compiere.process.ProcessInfo;
import org.compiere.process.ProcessInfoParameter;
import org.compiere.util.DB;
import org.compiere.util.DisplayType;
import org.compiere.util.Env;
import org.compiere.util.KeyNamePair;
import org.compiere.util.Msg;
import org.compiere.util.Trx;
import org.semeru.pos.model.MSemeruPOS;
import org.semeru.pos.model.X_SM_SemeruPOS;
import org.semeru.pos.model.X_SM_SemeruPOSLine;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zul.Borderlayout;
import org.zkoss.zul.Center;
import org.zkoss.zul.North;
import org.zkoss.zul.South;
import org.zkoss.zul.Space;

/**
 * 
 * @author Tegar N
 *
 * Decoris Pembayaran
 */

public class WSemeruPayment extends SemeruPayment implements IFormController,EventListener<Event>, WTableModelListener, ValueChangeListener{


	//CustomForm
	private CustomForm form = new CustomForm();

	//BorderLayout
	private Borderlayout infoLayout = new Borderlayout();
	private Borderlayout mainLayout = new Borderlayout();
	private Borderlayout salesLayout = new Borderlayout();	
	//Panel
	private Panel salesPanel = new Panel();
	private Panel southPanel = new Panel();
	private Panel paymentPanel = new Panel();
	private Panel parameterPanel = new Panel();

	//Grid
	private Grid salesGrid = GridFactory.newGridLayout();
	private Grid paymentGrid = GridFactory.newGridLayout();
	private Grid parameterGrid = GridFactory.newGridLayout();
	protected BigDecimal totalPrice = Env.ZERO;
	private Map<String, BigDecimal> mapPospay;
	private List<Map<String, BigDecimal>> listPospay;
	private BigDecimal nilaiDpp = Env.ZERO;
	private BigDecimal nilaiPajak = Env.ZERO;
	private BigDecimal nilaiBayar1 = Env.ZERO;
	private BigDecimal nilaiGrandTotal = Env.ZERO;
	private Properties ctx = Env.getCtx();

	//Button
	private Button printBtn = new Button();
	private Button deleteBtn = new Button();
	private Button processBtn = new Button();
	private Button newBtn = new Button();
	
	//TableLine
	private WListbox paymentTable = ListboxFactory.newDataTable();

	//NoNota
	private Label noNotaLabel = new Label("No. Nota :");
	private Textbox noNotaTB = new Textbox();
	
	//Tanggal
	private Label tglLabel = new Label("Tanggal :");
	private Datebox tglField = new Datebox(); 
	
	//Org
	private Label orgLabel = new Label("Toko :");
	private WTableDirEditor orgList;
	
	//BPartner
	private Label pelangganLabel = new Label("Karyawan :");
	private WSearchEditor pelangganSearch = null;
				
	//Charge
	private Label chargeLabel = new Label("Charge :");
	private WTableDirEditor chargeList = null;
		
	//Keterangan
	private Label ketLabel = new Label("Keterangan :");
	private Textbox ketTB = new Textbox();
	
	//Bank
	private Label bankAccountLabel = new Label ("Akun Bank :"); 
	private WTableDirEditor bankAccountSearch = null; 
		
	//TotalLines
	private Label totalLabel = new Label("Total :");
	private Textbox total = new Textbox();
	
	//GrandTotal
	private Label grandtotalLabel = new Label("Grand Total :");
	private Textbox grandtotal = new Textbox();
	
	//SisaPembayaran
	private Label sisaPembayaranLabel = new Label("Sisa Pembayaran :");
	private Textbox sisaPembayaran = new Textbox();
	
	//PaymentRule1
	//private Listbox payruleList1 = ListboxFactory.newDropdownListbox(m_paymentRule1);
	private Textbox payruleList1 = new Textbox();
	private Textbox paymentRule1 = new Textbox();
	
	//IsReceipt
	private Checkbox isReceipt = new Checkbox();
	
	//Formatter
	private DecimalFormat format = DisplayType.getNumberFormat(DisplayType.Amount);		
	
	//Variable Define
	private int AD_Client_ID = Env.getAD_Client_ID(ctx);
	private int AD_User_ID = Env.getAD_User_ID(ctx);
	private int C_Currency_ID = 303;
	private int C_PaymentTerm_ID = 0;
	private int CreatedByPOS_ID = 0;
	private int windowNo = form.getWindowNo();
	private int C_PaymentPrint_ID = 0;
	private Integer rowIndex = null;
	private boolean isBackDate = false;	
	private int m_AD_org_ID = 0;
	private int m_C_BPartner_ID = 0;
	private int m_C_Charge_ID = 0;
	private int m_C_BankAccount_ID_Tunai = 0;
	
	private String jumlahBayar = "0.00";
	private String m_docAction = "CO";
	private int SM_SemeruPOSPrint_ID = 0;
	String msgInfo = "";

	private String AD_Language = Env.getAD_Language(ctx);
	
	public WSemeruPayment() {

		boolean IsOk =dyInit();
		
		if(!IsOk)
			return;
		
		zkInit();

	}
	
	@Override
	public void valueChange(ValueChangeEvent e) {
		String name = e.getPropertyName();
		Object value = e.getNewValue();
		if (log.isLoggable(Level.CONFIG)) log.config(name + "=" + value);
		
		if (value == null)
			return;
		
		if (name.equals("AD_Org_ID")){
			
			orgList.setValue(value);
			m_AD_org_ID = ((Integer)value).intValue();
			
		}else if (name.equals("C_BPartner_ID")){
			
			pelangganSearch.setValue(value);
			m_C_BPartner_ID = ((Integer)value).intValue();
			
			if ((int)pelangganSearch.getValue() > 0){
				int BPartner_ID = (int) pelangganSearch.getValue();
				MBPartner bp = new MBPartner(ctx, BPartner_ID, null);
				if (bp.getC_PaymentTerm_ID()>0){
					C_PaymentTerm_ID = bp.getC_PaymentTerm_ID();
					
				}else {
					String sqlterm = "SELECT C_PaymentTerm_ID FROM C_PaymentTerm WHERE AD_Client_ID = ? AND IsDefault = 'Y' ";
					C_PaymentTerm_ID = DB.getSQLValueEx(ctx.toString(), sqlterm.toString(), new Object[]{Env.getAD_Client_ID(ctx)});
					
				}
			}else {
				String sqlterm = "SELECT C_PaymentTerm_ID FROM C_PaymentTerm WHERE AD_Client_ID = ? AND IsDefault = 'Y' ";
				C_PaymentTerm_ID = DB.getSQLValueEx(ctx.toString(), sqlterm.toString(), new Object[]{Env.getAD_Client_ID(ctx)});
			}
			
		}else if (name.equals("C_Charge_ID")){
			
			chargeList.setValue(value);
			m_C_Charge_ID = ((Integer)value).intValue();
			
			Vector<Vector<Object>> data = getChargeData(m_C_Charge_ID,paymentTable);
			Vector<String> columnNames = getOISColumnNames();

			paymentTable.clear();

			// Set Model
			ListModelTable modelP = new ListModelTable(data);
			modelP.addTableModelListener(this);
			paymentTable.setData(modelP, columnNames);
			configureMiniTable(paymentTable);
			
			reCalculate(paymentTable);
			calculate();
			
			if(paymentTable.getRowCount() > 0){
				
				deleteBtn.setEnabled(true);
			}
			
		}
	}

	@Override
	public void tableChanged(WTableModelEvent e) {
		boolean isUpdate = (e.getType() == WTableModelEvent.CONTENTS_CHANGED);
		
		if (!isUpdate)
		{
			return;
		}
		
		rowIndex = e.getFirstRow();
		int col = e.getColumn();
		
		if (rowIndex == null)
			return;
		
		tableChangeCalculate(rowIndex, col, paymentTable, windowNo);
		calculate();
		
		if (paymentTable.getRowCount() > 0){
			
			deleteBtn.setEnabled(true);
		}
				
	}

	@Override
	public void onEvent(Event e) throws Exception {
		log.config("");
		
		if (e.getTarget().equals(deleteBtn)){
			
			
			//deletedata(rowIndex);
			deleteData(paymentTable);
			
			
			paymentTable.clear();
			
			Vector<String> columnNames = getOISColumnNames();
			
			ListModelTable modelP = new ListModelTable(data);
			
			modelP.addTableModelListener(this);
			
			paymentTable.setData(modelP, columnNames);
			
			configureMiniTable(paymentTable);

			if (paymentTable.getRowCount() == 0){
				total.setText("0.00");
				sisaPembayaran.setText("0.00");
				grandtotal.setText("0.00");
				totalPrices = Env.ZERO;
				deleteBtn.setEnabled(false);
				return;
			}
			
			// recalculate component
			reCalculate(paymentTable);
			calculate();
			
		}else if (e.getTarget().equals(printBtn)){

		     updateData(SM_SemeruPOSPrint_ID);
			 String trxName = Trx.createTrxName("printpembayaran");
			 int AD_Process_ID = 0;
			 String url = "/home/idempiere/idempiere.gtk.linux.x86_64/idempiere-server/reports/";
			//String url = "D:\\SourceCode\\iDempiereBase\\reports\\";
			 
			 if (isReceipt.isChecked()){
				 AD_Process_ID = 330062;
			 }else if (!isReceipt.isChecked()){
				 AD_Process_ID = 330063;
			 }
			 
			 MProcess proc = new MProcess(Env.getCtx(), AD_Process_ID, trxName);
			 MPInstance instance = new MPInstance(proc,proc.getAD_Process_ID());
			 ProcessInfo pi = new ProcessInfo("Print Bukti Pembayaran", AD_Process_ID);
			 pi.setAD_PInstance_ID(instance.getAD_PInstance_ID());
			 ArrayList<ProcessInfoParameter> list = new ArrayList<ProcessInfoParameter>();
			 list.add(new ProcessInfoParameter("C_Payment_ID", C_PaymentPrint_ID, null,null, null));
			 list.add(new ProcessInfoParameter("url_path",url, null,null, null));
			 ProcessInfoParameter[] pars = new ProcessInfoParameter[list.size()];
			 list.toArray(pars);
			 pi.setParameter(pars);
			 //
			 Trx trx = Trx.get(trxName, true);
			 trx.commit();
			
			 ProcessUtil.startJavaProcess(Env.getCtx(), pi, Trx.get(trxName,true));
			
		}else if (e.getTarget().equals(newBtn)){
			clearData();
		}else if (e.getTarget().equals(paymentRule1)){

			Double dBayar1 = 0.00;
			Double dBayar2 = 0.00;
			
			if (paymentRule1.getText().isEmpty()) {
				dBayar1 = 0.00;
			}else if(!paymentRule1.getText().isEmpty()){
				if (AD_Language.toUpperCase().equals("EN_US")){
					dBayar1 = Double.valueOf(paymentRule1.getText().replaceAll(",", ""));
				}else if(AD_Language.toUpperCase().equals("IN_ID")){
					String dby1 = paymentRule1.getText().replaceAll("\\.", "").replaceAll(",", ".");
					dBayar1 = Double.valueOf(dby1);
				}			
			}
			
			BigDecimal bayar1 = BigDecimal.valueOf(dBayar1);
			BigDecimal sisaByar = bayar1.subtract(nilaiGrandTotal);
			BigDecimal totalbayar = bayar1;
			
			paymentRule1.setText(format.format(bayar1));
			
			if (sisaByar.compareTo(Env.ZERO) < 0) {
				
				sisaPembayaranLabel.setText("Kurang Bayar");
				sisaPembayaran.setText(format.format(sisaByar.abs()));
				
			} else {
				
				sisaPembayaranLabel.setText("Sisa Bayar");
				sisaPembayaran.setText(format.format(sisaByar.abs()));
			
			}
		
			
			totalbayar = BigDecimal.valueOf(dBayar1).add(BigDecimal.valueOf(dBayar2));
			sisaByar = totalbayar.subtract(nilaiGrandTotal);
	
			jumlahBayar = totalbayar.toString();
			
		}else if (e.getTarget().equals(processBtn)){
			Integer AD_Org_ID = (Integer) orgList.getValue();
			//cek validate period status
			Date tgl = tglField.getValue();
			
			if(tgl == null){
				
				FDialog.info(windowNo, null, "", "Field Tanggal Tidak Boleh Kosong	", "Info");
				return;
			}
			
			SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd 00:00:00");
			String tglTostr = df.format(tgl);
			Timestamp dateShip = Timestamp.valueOf(tglTostr);
			
			if(AD_Org_ID == null){
				FDialog.info(windowNo, null, "", "Field Toko Tidak Boleh Kosong	", "Info");
				return;
			}
			
			if (!MPeriod.isOpen(ctx, dateShip, "SOO", AD_Org_ID))
			{
				FDialog.warn(windowNo, null, "", "Transaksi Tidak Dapat Diproses Karena Status Period Closed", "Peringatan");
				return;
			}
			
			if(paymentTable.getRowCount() <= 0){
				FDialog.info(windowNo, null, "", "Anda Belum Memasukan Pembayaran", "Info");
				return;
			}
			
			if(ketTB.getValue().length() >255){
				
				FDialog.info(windowNo, null, "", "Keterangan tidak bisa lebih dari 255 karakter", "Info");
				return;
			}
			
			//validasi pembayaran
			Double dBayar1 = 0.00;
			
			if (paymentRule1.getText().isEmpty()) {
				dBayar1 = 0.00;
			}else if(!paymentRule1.getText().isEmpty()){
				if (AD_Language.toUpperCase().equals("EN_US")){
					dBayar1 = Double.valueOf(paymentRule1.getText().replaceAll(",", ""));
				}else if(AD_Language.toUpperCase().equals("IN_ID")){
					String dby1 = paymentRule1.getText().replaceAll("\\.", "").replaceAll(",", ".");
					dBayar1 = Double.valueOf(dby1);
				}			
			}
			BigDecimal bayar1 = BigDecimal.valueOf(dBayar1);
			BigDecimal sisaByar = bayar1.subtract(nilaiGrandTotal);
			
			if(sisaByar.compareTo(Env.ZERO)>0){
				
				FDialog.info(windowNo, null, "", "Nilai pembayaran / penerimaan harus sama dengan tagihan ", "Info");
				return;
			}
			
			
			FDialog.ask(windowNo, null,"Konfirmasi", "", "Proses Pembayaran?", new Callback<Boolean>() {
				
				@Override
				public void onCallback(Boolean result) {

					if(result){
						process();		
					}else{			
						return;
					}
				}
			});
			
			
		}
	
			
	}
	@Override
	public ADForm getForm() {
		return form;
	}

	//initial UI
	private void zkInit(){
		
		form.appendChild(mainLayout);
		form.addEventListener(DialogEvents.ON_WINDOW_CLOSE, new EventListener<Event>(){
			
			@Override
			public void onEvent(Event arg0) throws Exception {
				form.dispose();
			}
						
			});
		
		mainLayout.setWidth("99%");
		mainLayout.setHeight("100%");
		String grid = "border: 1px solid #C0C0C0; border-radius:5px;";
		parameterPanel.appendChild(parameterGrid);

		North north = new North();
		north.setStyle(grid);
		mainLayout.appendChild(north);
		north.appendChild(parameterPanel);
		north.setSplittable(true);

		Rows rows = null;
		Row row = null;

		parameterGrid.setWidth("100%");
		parameterGrid.setStyle("Height:28%;");

		rows = parameterGrid.newRows();

		// No Nota
		row = rows.newRow();
		row.appendCellChild(noNotaLabel.rightAlign(),1);
		row.appendCellChild(noNotaTB,1);
		noNotaTB.setHflex("true");
		noNotaTB.setReadonly(true);

		// tanggal
		row.appendCellChild(tglLabel.rightAlign(), 1);
		tglField.setHflex("true");
		row.appendCellChild(tglField, 1);
		tglField.setReadonly(true);
		tglField.setHflex("true");
		tglField.setFormat("dd/MM/yyyy");
		tglField.setEnabled(false);
		
		//space
		row.appendCellChild(new Space(), 1);
		
		//Org
		row = rows.newRow();
		row.appendCellChild(orgLabel.rightAlign(), 1);
		orgList.getComponent().setHflex("true");
		row.appendCellChild(orgList.getComponent(), 1);
		orgList.showMenu();

		//Pelanggan
		row.appendCellChild(pelangganLabel.rightAlign(), 1);
		pelangganSearch.getComponent().setHflex("true");
		row.appendCellChild(pelangganSearch.getComponent(), 1);
		
		//space
		row.appendCellChild(new Space(), 1);
		
		// Charge
		row = rows.newRow();
		row.appendCellChild(chargeLabel.rightAlign(), 1);
		chargeList.getComponent().setHflex("true");
		row.appendCellChild(chargeList.getComponent(), 1);
		
		// ketTB
		row.appendCellChild(ketLabel.rightAlign(), 1);
		row.appendCellChild(ketTB, 1);
		ketTB.setRows(2);
		ketTB.setHeight("100%");
		ketTB.setHflex("1");
		ketTB.setMaxlength(255);
		
		isReceipt.setSelected(false);
		isReceipt.setLabel("Penerimaan");
		isReceipt.addActionListener(this);
		row.appendCellChild(isReceipt, 1);
			
		//SouthPanel
		South south = new South();
		south.setStyle(grid);
		mainLayout.appendChild(south);
		south.appendChild(southPanel);
		southPanel.appendChild(salesGrid);
		rows = salesGrid.newRows();

		row = rows.newRow();
		row.appendCellChild(paymentPanel, 3);		
		paymentPanel.appendChild(paymentGrid );
		rows = paymentGrid .newRows();
		row = rows.newRow();
		
		// paymentrule1
		row.appendCellChild(payruleList1, 1);
		payruleList1.setEnabled(false);
		payruleList1.setStyle("border-radius:3px; font-weight: bold; color:#000000 ");
		payruleList1.setText("Tunai");
		payruleList1.setHflex("true");
				
		row.appendCellChild(paymentRule1, 2);
		paymentRule1.setStyle("text-align:right;");
		paymentRule1.setText("0.00");
		paymentRule1.addEventListener(0, "onBlur", this);
		paymentRule1.setHflex("true");
		paymentRule1.setMaxlength(22);
			
		//bankAccount
		row.appendCellChild(bankAccountLabel.rightAlign(),1);
		row.appendCellChild(bankAccountSearch.getComponent(), 2);
		bankAccountLabel.setVisible(false);
		bankAccountSearch.setVisible(false);
		
		// totalLines
		row.appendCellChild(totalLabel.rightAlign(), 1);
		total.setText("0.00");
		total.setStyle("text-align:right;");
		total.setReadonly(true);
		row.appendCellChild(total, 2);
		total.setHflex("true");


		// paymentrule2
		row = rows.newRow();

		row.appendCellChild(new Space(), 4);
		
		// grantotal
		row.appendCellChild(new Space(), 2);
		row.appendCellChild(grandtotalLabel.rightAlign(), 1);
		grandtotal.setText("0.00");
		row.appendCellChild(grandtotal, 2);
		grandtotal.setReadonly(true);
		grandtotal.setStyle("text-align:right;");
		grandtotal.setHflex("true");
		

		//sisabayar
		row = rows.newRow();
		row.appendCellChild(sisaPembayaranLabel.rightAlign(), 1);
		sisaPembayaran.setText("0.00");
		row.appendCellChild(sisaPembayaran, 2);
		sisaPembayaran.setReadonly(true);
		sisaPembayaran.setStyle("text-align:right;");
		sisaPembayaran.setHflex("true");
		

		row = rows.newRow();
		row.appendCellChild(new Space(), 7);

		// button process
		row = rows.newRow();
		row.appendCellChild(new Space(), 1);

		row.appendCellChild(newBtn,2);
		newBtn.addActionListener(this);
		newBtn.setHflex("true");
		newBtn.setTooltiptext("Buat Penerimaan/Pembayaran Baru");	
		
		row.appendCellChild(deleteBtn,2);
		deleteBtn.addActionListener(this);
		deleteBtn.setHflex("true");
		deleteBtn.setTooltiptext("Hapus Detail");
		deleteBtn.setEnabled(false);
				
		row.appendCellChild(processBtn, 2);
		processBtn.setHflex("true");
		processBtn.addActionListener(this);
		processBtn.setTooltiptext("Proses Pembayaran");
		
		row.appendCellChild(printBtn, 2);
		printBtn.setHflex("true");
		printBtn.addActionListener(this);
		printBtn.setEnabled(false);
		printBtn.setTooltiptext("Print Bukti Pembayaran/Penerimaan");
		
		row.appendCellChild(new Space(), 1);

		salesPanel.appendChild(salesLayout);
		salesPanel.setWidth("100%");
		salesPanel.setHeight("100%");
		salesLayout.setWidth("100%");
		salesLayout.setHeight("100%");
		salesLayout.setStyle("border: none");

		south = new South();
		south.setStyle("border: none");
		salesLayout.appendChild(south);
		Center center = new Center();
		salesLayout.appendChild(center);
		center.appendChild(paymentTable);
		paymentTable.setWidth("100%");
		paymentTable.setHeight("100%");
		center.setStyle("border: none; height:100%");

		center = new Center();
		mainLayout.appendChild(center);
		center.appendChild(infoLayout );
		infoLayout .setHflex("1");
		infoLayout .setVflex("1");
		infoLayout .setStyle("border: none");
		infoLayout .setWidth("100%");
		infoLayout .setHeight("100%");

		north = new North();
		north.setStyle("border: none");
		north.setHeight("100%");
		infoLayout .appendChild(north);
		north.appendChild(salesPanel);
		north.setSplittable(true);
		center = new Center();
		center.setStyle("border: none");
		infoLayout .appendChild(center);

	}
		
	//lookup for UI
	private boolean dyInit(){
		boolean IsOK = true;
		
		String sqlKasir = "SELECT C_BPartner_ID FROM AD_User WHERE AD_Client_ID = ? AND AD_User_ID = ?";
		CreatedByPOS_ID =  DB.getSQLValueEx(ctx.toString(), sqlKasir.toString(), new Object[]{Env.getAD_Client_ID(ctx),AD_User_ID});
	
		String sqlBank = "SELECT C_BankAccount_ID FROM C_POS WHERE CreatedByPOS_ID = ? AND AD_Client_ID = ?"; 
		m_C_BankAccount_ID_Tunai = DB.getSQLValueEx(ctx.toString(), sqlBank.toString(), new Object[]{CreatedByPOS_ID,AD_Client_ID});
	
		// validation if user is not kasir
		if (CreatedByPOS_ID < 0) {
			FDialog.warn(
					windowNo,
					null,
					"",
					"User Tidak Memiliki Akses ke Halaman Presales, Silahkan Hubungi Administrator",
					"Info");
			IsOK = false;
			return IsOK;
		}
		
		
		String sqlIsBackDate = "SELECT IsBackDate FROM C_POS WHERE  AD_Client_ID = ? AND CreatedByPOS_ID = ?";
		String backDate = DB.getSQLValueStringEx(ctx.toString(), sqlIsBackDate, new Object[]{AD_Client_ID,CreatedByPOS_ID});
		
		if (backDate.toUpperCase().equals("Y")){
			isBackDate = true;
		}else{
			isBackDate = false;	
		}
		
		if(isBackDate){
			tglField.setEnabled(true);
			tglField.setReadonly(false);
		}else{
			tglField.setEnabled(false);
			tglField.setReadonly(true);

		}
		
		//Set Button Label
		newBtn.setLabel("Baru");
		//clearBtn.setLabel("Hapus Charge");
		deleteBtn.setLabel("Hapus Detail");
		printBtn.setLabel("Print Nota");
		processBtn.setLabel("Proses");
	
		//Lookup Org
		MLookup orgLookup = MLookupFactory.get(ctx,form.getWindowNo(), 0, 2163, DisplayType.TableDir);
		orgList = new WTableDirEditor("AD_Org_ID", true, false, true, orgLookup);
		orgList.addValueChangeListener(this);
		orgList.setMandatory(true);
		
		//Lookup BP
		MLookup lookupBP = MLookupFactory.get(ctx, form.getWindowNo(),0, 2893, DisplayType.Search);
		pelangganSearch = new WSearchEditor("C_BPartner_ID", true, false, true,lookupBP);
		pelangganSearch.addValueChangeListener(this);
		pelangganSearch.setMandatory(true);
		
		//Lookup Charge
		MLookup chargeLookup = MLookupFactory.get(ctx,form.getWindowNo(), 0, 3333, DisplayType.TableDir);
		chargeList = new WTableDirEditor("C_Charge_ID", true, false, true, chargeLookup);
		chargeList.addValueChangeListener(this);
		chargeList.setMandatory(true);
		
		MLookup lookupBank = MLookupFactory.get(ctx, form.getWindowNo(),0, 3880, DisplayType.TableDir);
		bankAccountSearch = new WTableDirEditor("C_BankAccount_ID", true, false, true,lookupBank);
		bankAccountSearch.addValueChangeListener(this);
		bankAccountSearch.setMandatory(true);
		
		// Date set to Login Date
		Calendar cal = Calendar.getInstance();
		cal.setTime(Env.getContextAsDate(ctx, "#Date"));
		cal.set(Calendar.HOUR_OF_DAY, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);
		cal.set(Calendar.MILLISECOND, 0);
		
		tglField.setValue(cal.getTime());
		
		orgList.setReadWrite(false);
		pelangganSearch.setReadWrite(false);
		chargeList.setReadWrite(false);
		ketTB.setReadonly(true);
		paymentRule1.setEnabled(false);
		isReceipt.setEnabled(false);
		processBtn.setEnabled(false);
				
		return IsOK;

	}
	
	protected void configureMiniTable(IMiniTable miniTable) {
		
		miniTable.setColumnClass(0, Boolean.class, false);
		miniTable.setColumnClass(1, String.class, true); 		// 2-Charge
		miniTable.setColumnClass(2, BigDecimal.class, false); 	// 3-Price

		miniTable.autoSize();

	}

	protected Vector<String> getOISColumnNames() {
		
		// Header Info
		Vector<String> columnNames = new Vector<String>(3);
		columnNames.add(Msg.getMsg(ctx, "Select"));
		columnNames.add(Msg.translate(ctx, "C_Charge_ID"));
		columnNames.add("Harga");

		return columnNames;
	
	}
	
	public void calculate() {
		
		totalPrice = totalPrices.setScale(2,RoundingMode.HALF_UP);
		nilaiGrandTotal = totalPrice.setScale(2,RoundingMode.HALF_UP);
		
		grandtotal.setText(format.format(nilaiGrandTotal));
		total.setText(format.format(nilaiGrandTotal));

		
		if (payruleList1.getValue().toString().equalsIgnoreCase("Tunai"))
				{
			
			if (!paymentRule1.getText().isEmpty()) {
				Double dBayar = Double.valueOf(jumlahBayar);
				BigDecimal bayar1 = BigDecimal.valueOf(dBayar);
				BigDecimal sisaByar = (bayar1.subtract(nilaiGrandTotal)).setScale(2,RoundingMode.HALF_DOWN);
				if (sisaByar.compareTo(Env.ZERO) < 0) {
					
					sisaPembayaranLabel.setText("Kurang Bayar");
					sisaPembayaran.setText(format.format(sisaByar.abs()));
				
				} else {
					
					sisaPembayaranLabel.setText("Sisa Bayar");
					sisaPembayaran.setText(format.format(sisaByar.abs()));
				
				}
			}
		}
	}
	
	private void lockData(){
		
		deleteBtn.setEnabled(false);
		isReceipt.setEnabled(false);
		payruleList1.setEnabled(false);
		paymentRule1.setReadonly(true);
		sisaPembayaran.setReadonly(true);
		processBtn.setEnabled(false);
		tglField.setEnabled(false);
		orgList.setReadWrite(false);
		pelangganSearch.setReadWrite(false);
		chargeList.setReadWrite(false);
		bankAccountSearch.setReadWrite(false);
		paymentTable.setEnabled(false);
		
	}
	
	private void clearData(){
			
		paymentTable.setEnabled(true);
		bankAccountLabel.setVisible(false);
		bankAccountSearch.setVisible(false);
		bankAccountSearch.setReadWrite(true);
		deleteBtn.setEnabled(true);
		isReceipt.setEnabled(true);
		paymentRule1.setReadonly(false);
		sisaPembayaran.setEnabled(true);
		processBtn.setEnabled(true);
		printBtn.setEnabled(false);
		tglField.setEnabled(true);
		orgList.setReadWrite(true);
		pelangganSearch.setReadWrite(true);
		chargeList.setReadWrite(true);
		msgInfo = "";
		paymentRule1.setEnabled(true);
		jumlahBayar = "0.00";
		total.setText("0.00");
		grandtotal.setText("0.00");
		paymentRule1.setText("0.00");
		sisaPembayaran.setText("0.00");
		noNotaTB.setText("");
		
		isReceipt.setChecked(false);
		
		nilaiDpp = Env.ZERO;
		totalPrice = Env.ZERO;
		nilaiPajak = Env.ZERO;
		nilaiBayar1 = Env.ZERO;
		totalPrices = Env.ZERO;
		nilaiGrandTotal = Env.ZERO;
		
		ketTB.setValue(null);
		ketTB.setEnabled(true);
		ketTB.setReadonly(false);
		chargeList.setValue(null);
		pelangganSearch.setValue(null);
		
		
		// Date
		Calendar cal = Calendar.getInstance();
		cal.setTime(new Date());
		cal.set(Calendar.HOUR_OF_DAY, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);
		cal.set(Calendar.MILLISECOND, 0);			
		
		tglField.setValue(cal.getTime());

		for (int i=0 ; i < paymentTable.getRowCount(); i++){
			deletedata(0);
		}
		paymentTable.clear();
		paymentTable.setRowCount(0);
	}
	
	
	private void process(){
		Integer AD_Org_ID = (Integer)orgList.getValue();
		
		
		Date tgl = tglField.getValue();
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd 00:00:00");
		String tglTostr = df.format(tgl);
		Timestamp dateShip = Timestamp.valueOf(tglTostr);
		
		Double bayar1 = 0.00;
		
		if (AD_Language.toUpperCase().equals("EN_US")){
			bayar1 = Double.valueOf(paymentRule1.getText().replaceAll(",", ""));
		}else if(AD_Language.toUpperCase().equals("IN_ID")){
			String dby1 = paymentRule1.getText().replaceAll("\\.", "").replaceAll(",", ".");
			bayar1 = Double.valueOf(dby1);

		}
		
		mapPospay = new HashMap<String, BigDecimal>();
		
		String msg = "";
		String pembayaran1 = (String)paymentRule1.getValue();
		String tipeBayar1 = payruleList1.getValue();

		Integer C_BPartner_ID  = (Integer)pelangganSearch.getValue();
		Integer C_BankAccount_ID =(Integer) bankAccountSearch.getValue();

		
		
		Timestamp tglTrx = dateShip;
					
		StringBuilder sqlBPLoc= new StringBuilder();
		sqlBPLoc.append("SELECT C_BPartner_Location_ID ");
		sqlBPLoc.append("FROM C_BPartner_Location ");
		sqlBPLoc.append("WHERE C_BPartner_ID = ? ");
		int C_BPartner_Location_ID = DB.getSQLValueEx(ctx.toString(), sqlBPLoc.toString(), new Object[]{m_C_BPartner_ID});
		
		nilaiBayar1 = BigDecimal.valueOf(bayar1);
		
		BigDecimal grandtot = nilaiGrandTotal;
		BigDecimal payment1 = nilaiBayar1;
		BigDecimal totalbayar = nilaiBayar1;
		
		IMiniTable miniTable = paymentTable;
		
		boolean IsFullPaid = false;
		boolean penerimaan = isReceipt.isChecked();
		boolean IsSoTrx = false;
		if (AD_Org_ID == null){
			AD_Org_ID = 0;	
		}if (C_BPartner_ID == null){
			C_BPartner_ID = 0;
		}if (C_BankAccount_ID== null){
			C_BankAccount_ID = 0;
		}if(totalbayar.compareTo(grandtot) == 0){
			IsFullPaid = true;
		}

		msg = checkReqInput(AD_Org_ID, C_BPartner_ID , C_BankAccount_ID,tglTrx, IsFullPaid, pembayaran1,tipeBayar1, miniTable);
		if (msg != ""){
			FDialog.warn(form.getWindowNo(),null, msg,"","Peringatan");
			return;
		}
		
		
		//check isReceipt
		StringBuilder sqlPayDocType = new StringBuilder();
			sqlPayDocType.append("SELECT C_DocType_ID");
			sqlPayDocType.append(" FROM C_DocType");
			sqlPayDocType.append(" WHERE AD_Client_ID = ?");
			sqlPayDocType.append(" AND DocBaseType = ?");
		
		StringBuilder sqlInvDocType = new StringBuilder();
			sqlInvDocType.append("SELECT C_DocType_ID");
			sqlInvDocType.append(" FROM C_DocType");
			sqlInvDocType.append(" WHERE AD_Client_ID = ?");
			sqlInvDocType.append(" AND DocBaseType = ?");
			
		StringBuilder sqlPL = new StringBuilder();
			sqlPL.append("SELECT M_PriceList_ID");
			sqlPL.append(" FROM M_PriceList");
			sqlPL.append(" WHERE AD_Client_ID = ?");
				
		String payDocBaseType = "";
		String invDocBaseType = "";
		
		if(penerimaan){
			payDocBaseType = MDocType.DOCBASETYPE_ARReceipt;
			invDocBaseType = MDocType.DOCBASETYPE_ARInvoice;
			sqlInvDocType.append(" AND Name = 'AR Invoice'");
			sqlPL.append(" AND IsSoPriceList = 'Y' ");
			IsSoTrx = true;
		}else if (!penerimaan){
			payDocBaseType = MDocType.DOCBASETYPE_APPayment;
			invDocBaseType = MDocType.DOCBASETYPE_APInvoice;
			sqlInvDocType.append(" AND Name = 'AP Invoice'");
			sqlPL.append(" AND IsSoPriceList = 'N' ");
			IsSoTrx = false;
		}
		
		int pay_C_DocType_ID = DB.getSQLValueEx(ctx.toString(), sqlPayDocType.toString(), new Object[]{AD_Client_ID,payDocBaseType});
		int inv_C_DocType_ID = DB.getSQLValueEx(ctx.toString(), sqlInvDocType.toString(), new Object[]{AD_Client_ID,invDocBaseType});
		int M_PriceList_ID = DB.getSQLValueEx(ctx.toString(), sqlPL.toString(), new Object[]{AD_Client_ID});
		//record for POS
		X_SM_SemeruPOS smPOS = new X_SM_SemeruPOS(ctx, 0, null);
		
		smPOS.setAD_Org_ID(m_AD_org_ID);
		smPOS.setC_BPartner_ID(m_C_BPartner_ID);
		smPOS.setDateOrdered(tglTrx);
		smPOS.setDateTrx(tglTrx);
		smPOS.setM_PriceList_ID(M_PriceList_ID);
		smPOS.setDescription((String)ketTB.getValue().toString());
		smPOS.setC_BPartner_Location_ID(C_BPartner_Location_ID);
		
		String decPOsdocType = "SELECT C_DocType_ID "
				+ " FROM C_DocType "
				+ " WHERE AD_Client_ID = ?"
				+ " AND DocBaseType = ? ";
		String docDec = "POS";
		
		
		int decDoc_ID = DB.getSQLValueEx(null, decPOsdocType, new Object[]{AD_Client_ID,docDec});
		
		
		smPOS.setC_DocType_ID(decDoc_ID);
		smPOS.setC_PaymentTerm_ID(C_PaymentTerm_ID);
		smPOS.setPaymentRule("X");
		smPOS.setIsSOTrx(IsSoTrx);
		smPOS.setIsReceipt(penerimaan);
		smPOS.setCreatedByPos_ID(CreatedByPOS_ID);
		smPOS.setdpp(nilaiDpp);
		smPOS.setGrandTotal(nilaiGrandTotal);
		smPOS.setTotalLines(totalPrices);
		smPOS.setTaxAmt(nilaiPajak);
		smPOS.setPayType1(tipeBayar1.toUpperCase());
		smPOS.setPembayaran1(payment1);
		smPOS.setIsPembatalan(false);
		smPOS.setIsPenjualan(false);
		smPOS.setIsPembayaran(true);
		smPOS.saveEx();

		
		SM_SemeruPOSPrint_ID = smPOS.getSM_SemeruPOS_ID();
		
		for (int i = 0; i < miniTable.getRowCount(); i++){
		
		boolean isSelect = (boolean)miniTable.getValueAt(i,0);
		KeyNamePair charge = (KeyNamePair) miniTable.getValueAt(i, 1);
		BigDecimal price = (BigDecimal) miniTable.getValueAt(i, 2);
		int C_Charge_ID = charge.getKey();
		MCharge ch = new MCharge(ctx, C_Charge_ID, null);
		int taxCategory = ch.getC_TaxCategory_ID();
		
		MTaxCategory cat = new MTaxCategory(ctx, taxCategory, null);
		String taxCatName = cat.getName();
		
		String sqlTax = "SELECT C_Tax_ID FROM C_Tax WHERE AD_Client_ID = ? AND Name = '"+ taxCatName +"'"; 
		int C_Tax_ID = DB.getSQLValueEx(cat.get_TrxName(), sqlTax, new Object[]{AD_Client_ID});
		
		if (isSelect){
			miniTable.setValueAt(false, i, 0);
			miniTable.setColumnReadOnly(i, true);
		}
		
		X_SM_SemeruPOSLine decPOSLine = new X_SM_SemeruPOSLine(ctx, 0, smPOS.get_TrxName());
		
		String sqlLine = "SELECT COALESCE(MAX(Line),0)+10 FROM SM_SemeruPOSLine WHERE AD_Client_ID = ? AND SM_SemeruPOS_ID =?";
		int ii = DB.getSQLValue (smPOS.get_TrxName(), sqlLine, new Object[]{AD_Client_ID,smPOS.getSM_SemeruPOS_ID()});
		
		
		decPOSLine.setAD_Org_ID(smPOS.getAD_Org_ID());
		decPOSLine.setSM_SemeruPOS_ID(smPOS.getSM_SemeruPOS_ID());
		decPOSLine.setC_Charge_ID(ch.getC_Charge_ID());
		decPOSLine.setPriceEntered(price);
		decPOSLine.setQtyOrdered(Env.ONE);
		decPOSLine.setLineNetAmt((BigDecimal) miniTable.getValueAt(i, 2));
		decPOSLine.setC_Tax_ID(C_Tax_ID);
		decPOSLine.setLine(ii);
		decPOSLine.saveEx();
		
		listPospay = new ArrayList<Map<String,BigDecimal>>();
				
		MPayment pay = null;
		MInvoice inv = null;
		MInvoiceLine invLine = null;
		
		String msgInv = "";
		String msgPay = "";
		
		StringBuilder sqlSemeruPOS = new StringBuilder();
		sqlSemeruPOS.append("SELECT PayType1,PayType2,Pembayaran1,Pembayaran2 ");
		sqlSemeruPOS.append("FROM SM_SemeruPOS ");
		sqlSemeruPOS.append("WHERE AD_Client_ID =? ");
		sqlSemeruPOS.append("AND SM_SemeruPOS_ID =? ");
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			pstmt = DB.prepareStatement(sqlSemeruPOS.toString(), null);
			pstmt.setInt(1, AD_Client_ID);
			pstmt.setInt(2, smPOS.getSM_SemeruPOS_ID());
			rs = pstmt.executeQuery();
			while (rs.next()) {
				if(rs.getString(1)!=null && rs.getBigDecimal(3).compareTo(Env.ZERO) > 0 ){
				mapPospay.put(rs.getString(1), rs.getBigDecimal(3));
				}if(rs.getString(2)!=null && rs.getBigDecimal(4).compareTo(Env.ZERO) > 0 ){
				mapPospay.put(rs.getString(2), rs.getBigDecimal(4));
				}
				listPospay.add(mapPospay);

				//Note:tambahkan looping jika mungkin lebih dari 2 document payment
				inv = new MInvoice(ctx, 0, null);
				
				inv.setAD_Org_ID(smPOS.getAD_Org_ID());
				inv.setC_DocType_ID(inv_C_DocType_ID);
				inv.setDescription(smPOS.getDescription());
				inv.setDateAcct(smPOS.getDateOrdered());
				inv.setDateInvoiced(smPOS.getDateOrdered());
				inv.setC_BPartner_ID(smPOS.getC_BPartner_ID());
				inv.setC_BPartner_Location_ID(smPOS.getC_BPartner_Location_ID());
				inv.setC_PaymentTerm_ID(smPOS.getC_PaymentTerm_ID());
				inv.setPaymentRule(smPOS.getPaymentRule());
				inv.setTotalLines(smPOS.getTotalLines());
				inv.setGrandTotal(smPOS.getGrandTotal());
				inv.setM_PriceList_ID(smPOS.getM_PriceList_ID());
				inv.setIsSOTrx(smPOS.isSOTrx());
				inv.saveEx();
				
				smPOS.setC_Invoice_ID(inv.getC_Invoice_ID());
				smPOS.saveEx();
				

				MSemeruPOS dec = new MSemeruPOS(ctx, smPOS.getSM_SemeruPOS_ID(), null);

				X_SM_SemeruPOSLine DecLines[] = dec.getLines();
				
				for (X_SM_SemeruPOSLine line : DecLines){
					invLine = new MInvoiceLine(ctx, 0, null);

					invLine.setAD_Org_ID(inv.getAD_Org_ID());
					invLine.setC_Invoice_ID(inv.getC_Invoice_ID());
					invLine.setC_Charge_ID(line.getC_Charge_ID());
					invLine.setQty(Env.ONE);
					invLine.setQtyEntered(Env.ONE);
					invLine.setQtyInvoiced(Env.ONE);
					invLine.setPrice(line.getPriceEntered());
					invLine.setPriceEntered(line.getPriceEntered());
					invLine.setLineNetAmt(line.getPriceEntered());
					invLine.setC_Tax_ID(line.getC_Tax_ID());
					invLine.saveEx();
					
					line.setC_InvoiceLine_ID(invLine.getC_InvoiceLine_ID());
					line.saveEx();
				}
			
				if (inv != null)
				{
					if (m_docAction != null && m_docAction.length() > 0)
					{
						inv.setDocAction(m_docAction);
						if(!inv.processIt (m_docAction)) {
							log.warning("Invoice Process Failed: " + inv + " - " + inv.getProcessMsg());
							throw new IllegalStateException("Invoice Process Failed: " + inv + " - " + inv.getProcessMsg());
						}
					}
					inv.saveEx();
					msgInv = infoGeneratedDocumentInvoice(inv.getC_Invoice_ID(), windowNo);

				}
				
				if (inv.getDocStatus().equals(MInvoice.DOCSTATUS_Completed)){
				
					for (String key : mapPospay.keySet() ){

						String tipebayar = key.toUpperCase();
						BigDecimal payAmt = mapPospay.get(key);

						
						pay = new MPayment(ctx, 0, null);
						pay.setAD_Org_ID(smPOS.getAD_Org_ID());
						pay.setC_DocType_ID(pay_C_DocType_ID);
						pay.setIsReceipt(smPOS.isReceipt());
						pay.setC_BPartner_ID(smPOS.getC_BPartner_ID());
						pay.setDescription(smPOS.getDescription());
						pay.setDateTrx(smPOS.getDateOrdered());
						pay.setDateAcct(smPOS.getDateOrdered());
						if (tipebayar.equals("TUNAI")){
							pay.setC_BankAccount_ID(m_C_BankAccount_ID_Tunai);
							pay.setTenderType(MPayment.TENDERTYPE_Cash);
						}else if (tipebayar.equals("BANK")){
							pay.setC_BankAccount_ID(C_BankAccount_ID);
							pay.setTenderType(MPayment.TENDERTYPE_Cash);
						}
						pay.setPayAmt(payAmt);
						pay.setC_Currency_ID(C_Currency_ID);
						pay.set_CustomColumn("CreatedByPOS_ID", CreatedByPOS_ID);
						pay.setC_Invoice_ID(inv.getC_Invoice_ID());
						pay.saveEx();
						
						smPOS.setC_Payment_ID(pay.getC_Payment_ID());
						smPOS.saveEx();
						
						if (pay != null)
						{
							
							if (m_docAction != null && m_docAction.length() > 0)
							{
								pay.setDocAction(m_docAction);
								if(!pay.processIt (m_docAction)) {
									log.warning("Invoice Process Failed: " + pay + " - " + inv.getProcessMsg());
									throw new IllegalStateException("Invoice Process Failed: " + pay + " - " + pay.getProcessMsg());
								}
							}
							pay.saveEx();
							//TODO
							C_PaymentPrint_ID = pay.getC_Payment_ID();
							msgPay = msgPay+ infoGeneratedDocumentPayment(pay.getC_Payment_ID(), IsSoTrx, windowNo);
							
						}
						
					}
					
				}	
				
				
			}
			
			msgInfo = msgInv+msgPay;
			ketTB.setReadonly(true);
			ketTB.setEnabled(false);
			
		} catch (SQLException ex) {
			log.log(Level.SEVERE, sqlSemeruPOS.toString(), ex);
		} finally {
			DB.close(rs, pstmt);
			rs = null;
			pstmt = null;
		}
			
	}
		printBtn.setEnabled(true);
		FDialog.info(windowNo, null, null, msgInfo, "Dokumen Terbentuk");
		noNotaTB.setText(smPOS.getDocumentNo().toString());
		smPOS.setProcessed(true);
		lockData();

	}
	
}
