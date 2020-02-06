package org.semeru.pos;

import java.math.BigDecimal;
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
import java.util.Map;
import java.util.Properties;
import java.util.Vector;
import java.util.logging.Level;

import org.adempiere.util.ProcessUtil;
import org.adempiere.webui.component.Button;
import org.adempiere.webui.component.Combobox;
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
import org.compiere.model.MDocType;
import org.compiere.model.MInvoice;
import org.compiere.model.MLookup;
import org.compiere.model.MLookupFactory;
import org.compiere.model.MOrder;
import org.compiere.model.MPInstance;
import org.compiere.model.MPayment;
import org.compiere.model.MProcess;
import org.compiere.process.ProcessInfo;
import org.compiere.process.ProcessInfoParameter;
import org.compiere.util.DB;
import org.compiere.util.DisplayType;
import org.compiere.util.Env;
import org.compiere.util.Msg;
import org.compiere.util.Trx;
import org.semeru.pos.model.MSemeruPOS;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zul.Borderlayout;
import org.zkoss.zul.Center;
import org.zkoss.zul.Decimalbox;
import org.zkoss.zul.North;
import org.zkoss.zul.South;
import org.zkoss.zul.Space;

/**
 * 
 * @author Tegar N
 *
 * Semeru Penerimaan Piutang
 *
 */

public class WSemeruPenerimaanPiutang extends SemeruPenerimaanPiutang implements IFormController,
EventListener<Event>, WTableModelListener, ValueChangeListener  {
	
	// CustomForm
	private CustomForm form = new CustomForm();
	
	// BorderLayout
	private Borderlayout mainLayout = new Borderlayout();
	private Borderlayout infoLayout = new Borderlayout();
	private Borderlayout summaryLayout = new Borderlayout();

	// Panel
	private Panel parameterPanel = new Panel();
	private Panel penerimaanPanel = new Panel();
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
	
	//Nopembayaran
	private Label noPembayaranLabel = new Label("No Pembayaran");
	private Textbox noPembayaranTextBox = new Textbox();
	
	// Tanggal Transaksi
	private Label dateTransactionLabel = new Label("Tanggal Transaksi :");
	private Datebox dateTransactionField = new Datebox();
	
	// Pelanggan
	private Label PelangganLabel = new Label("Pelanggan :");
	private WSearchEditor PelangganSearch = null;
	
	//Nota
	private Label noNotaLabel = new Label("No Nota");
	private WSearchEditor noNota = null; 
	
	//cetak nota
	private Button cetakNota = new Button();
	
	//buat baru
	private Button buatbaru = new Button();
	
	//Proses pelunasan
	private Button processPenerimaan = new Button();
	
	//Table Peluanasan
	private WListbox pembayaranTable = ListboxFactory.newDataTable();
	
	//Bank
	private Label bankAccountLabel = new Label ("Akun Bank :"); 
	private WTableDirEditor bankAccountSearch = null; 
	
	//tipe pembayaran
	
	private Combobox tipeBayar = new Combobox();
	
	//Amount pembayaran
	private Decimalbox amtPay = new Decimalbox();	

	//keterangan
	
	private Label keteranganLabel = new Label("Keterangan");
	private Textbox keteranganTextBox = new Textbox();
	
	private Label totalPitutaLabel = new Label("Total Piutang");
	private Label sudahDibayarLabel = new Label("Sudah Bibayar");
	private Label sisaPitutaLabel = new Label("Sisa Piutang");

	
	private Textbox totalPiutang = new Textbox();
	private Textbox sudahDibayar = new Textbox();
	private Textbox sisaPiutang = new Textbox();
	
	
	//variable
	private Properties ctx = Env.getCtx();
	private int AD_Client_ID  = Env.getAD_Client_ID(ctx);
	private int windowNo = form.getWindowNo();
	private DecimalFormat format = DisplayType.getNumberFormat(DisplayType.Amount);
	private int CreatedByPOS_ID = 0;
	private int AD_User_ID = 0;
	private int SM_SemeruPOSPrint_ID = 0;
	
	//private int AD_Usr_ID = Env.getAD_User_ID(ctx);
	
	public WSemeruPenerimaanPiutang(){
		dyInit();
		init();	
	}
	
	private void dyInit() {
		
		
		AD_User_ID = Env.getAD_User_ID(ctx);

		String sqlKasir = "SELECT C_BPartner_ID FROM AD_User WHERE AD_Client_ID = ? AND AD_User_ID = ?";
		CreatedByPOS_ID = DB.getSQLValueEx(ctx.toString(), sqlKasir.toString(),new Object[] { Env.getAD_Client_ID(ctx), AD_User_ID });

		if(CreatedByPOS_ID < 0){	
			FDialog.warn(windowNo, null, "", "User Tidak Memiliki Akses ke Halaman Presales, Silahkan Hubungi Administrator","Info");
			return;
		}
		
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
		
		//Lookup BP
		MLookup lookupBP = MLookupFactory.get(ctx, form.getWindowNo(), 0, 2893,DisplayType.Search);
		PelangganSearch = new WSearchEditor("C_BPartner_ID", true, false, true,lookupBP);
		PelangganSearch.addValueChangeListener(this);
		PelangganSearch.setMandatory(true);	
		
		//bank
		MLookup lookupBank = MLookupFactory.get(ctx, form.getWindowNo(),0, 3880, DisplayType.TableDir);
		bankAccountSearch = new WTableDirEditor("C_BankAccount_ID", true, false, true,lookupBank);
		bankAccountSearch.addValueChangeListener(this);
		bankAccountSearch.setMandatory(true);
	
		//No Nota
		MLookup lookupInvoice = MLookupFactory.get(ctx, form.getWindowNo(), 0, 1000934, DisplayType.Search);
		noNota  = new WSearchEditor("C_DecorisPOS_ID",true, false, true,lookupInvoice);
		noNota.addValueChangeListener(this);
		noNota.setMandatory(true);
		
		ArrayList<String> listTipeBayar = new ArrayList<String>();
		listTipeBayar.add("TUNAI");
		listTipeBayar.add("BANK");
		
		for(int i = 0 ; i <listTipeBayar.size() ; i++){
			tipeBayar.appendItem(listTipeBayar.get(i));
		}
		tipeBayar.setSelectedIndex(0);
		
		
		Calendar cal = Calendar.getInstance();
		cal.setTime(Env.getContextAsDate(ctx, "#Date"));
		cal.set(Calendar.HOUR_OF_DAY, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);
		cal.set(Calendar.MILLISECOND, 0);
		dateTransactionField.setValue(new Timestamp(cal.getTimeInMillis()));

		
		noNota.setReadWrite(false);
		dateTransactionField.setReadonly(true);
		dateTransactionField.setEnabled(false);
		amtPay.setReadonly(true);
		bankAccountSearch.setReadWrite(false);
		keteranganTextBox.setReadonly(true);
		processPenerimaan.setEnabled(false);
		
		
		
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
		row.appendCellChild(new Space(), 1);
		row.appendCellChild(orgLabel.rightAlign(), 1);
		row.appendCellChild(org.getComponent(), 1);
		org.getComponent().setHflex("true");
		org.setReadWrite(false);
		row.appendCellChild(new Space(), 1);
		
		row = rows.newRow();
		row.appendCellChild(noPembayaranLabel.rightAlign(),1);
		row.appendCellChild(noPembayaranTextBox, 1);
		noPembayaranTextBox.setHflex("true");
		noPembayaranTextBox.setEnabled(false);
		
		row.appendCellChild(new Space(), 1);
		row.appendCellChild(noNotaLabel.rightAlign(),1 );
		row.appendCellChild(noNota.getComponent(), 1);
		noNota.getComponent().setHflex("true");			
	
		//Tanggal Transaksi		
		row = rows.newRow();
		row.appendCellChild(dateTransactionLabel.rightAlign(),1);
		row.appendCellChild(dateTransactionField,1);
		dateTransactionField.setHflex("true");
		dateTransactionField.setFormat("dd/MM/yyyy");
	
		//Nama Pelanggan
		row.appendCellChild(new Space(), 1);

		row.appendCellChild(PelangganLabel.rightAlign(),1);
		row.appendCellChild(PelangganSearch.getComponent(), 1);
		PelangganSearch.getComponent().setHflex("true");
		PelangganSearch.setReadWrite(false);
		
				
//		row.appendCellChild(tutupKasFlag, 1);
//		tutupKasFlag.setHflex("true");
//		tutupKasFlag.setSelected(true);
//		tutupKasFlag.setLabel("Tutup Kas");
//		tutupKasFlag.addActionListener(this);
//		tutupKasFlag.setEnabled(false);
		
				
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
		southRow.appendCellChild(tipeBayar, 1);
		tipeBayar.setHflex("true");
		tipeBayar.setReadonly(true);
		tipeBayar.setReadonly(true);
		tipeBayar.addEventListener(0, "onChange", this);


		southRow.appendCellChild(amtPay, 2);
		amtPay.setStyle("text-align:right;");
		amtPay.setValue("0");
		amtPay.addEventListener(0, Events.ON_BLUR, this);
		amtPay.addEventListener(0, Events.ON_FOCUS, this);
		amtPay.setHflex("true");
		amtPay.setFormat("#,###,###,##0.00" );
		amtPay.setWidth("100%");
		amtPay.setMaxlength(22);
		
		//bankAccount
		southRow.appendCellChild(bankAccountLabel.rightAlign(),1);
		southRow.appendCellChild(bankAccountSearch.getComponent(), 2);
		bankAccountSearch.getComponent().setHflex("true");
		
		southRow.appendCellChild(totalPitutaLabel.rightAlign(),1);
		southRow.appendCellChild(totalPiutang, 2);
		totalPiutang.setHflex("true");
		totalPiutang.setReadonly(true);
		totalPiutang.setText("0");
		totalPiutang.setStyle("text-align:right;");
		
		southRow = southRows.newRow();		
		southRow.appendCellChild( new Space(),3);
		southRow.appendCellChild(keteranganLabel.rightAlign(),1);
		southRow.appendCellChild(keteranganTextBox,2);
		keteranganTextBox.setHflex("true");
		keteranganTextBox.setRows(3);
		keteranganTextBox.setMaxlength(255);
		
		southRow.appendCellChild(sudahDibayarLabel.rightAlign(),1);
		southRow.appendCellChild(sudahDibayar, 2);
		sudahDibayar.setHflex("true");
		sudahDibayar.setReadonly(true);
		sudahDibayar.setText("0");
		sudahDibayar.setStyle("text-align:right;");



		southRow = southRows.newRow();
		southRow.appendCellChild( new Space(),6);

		southRow.appendCellChild(sisaPitutaLabel.rightAlign(),1);
		southRow.appendCellChild(sisaPiutang, 2);
		sisaPiutang.setHflex("true");
		sisaPiutang.setReadonly(true);
		sisaPiutang.setText("0");
		sisaPiutang.setStyle("text-align:right;");


		southRow = southRows.newRow();
		southRow.appendCellChild( new Space(),1);
		
		southRow = southRows.newRow();		
		southRow.appendCellChild(buatbaru, 3);
		buatbaru.setLabel("Buat Baru");
		buatbaru.addActionListener(this);
		buatbaru.setHflex("true");
		
		southRow.appendCellChild(processPenerimaan, 3);
		processPenerimaan.setLabel("Proses");
		processPenerimaan.addActionListener(this);
		processPenerimaan.setHflex("true");
		
		southRow.appendCellChild(cetakNota, 3);
		cetakNota.setLabel("Cetak Nota");
		cetakNota.addActionListener(this);
		cetakNota.setHflex("true");
		cetakNota.setEnabled(false);

		
		
		south = new South();
		penerimaanPanel.appendChild(summaryLayout);
		summaryLayout.appendChild(south);
		penerimaanPanel.setWidth("100%");
		penerimaanPanel.setHeight("100%");
		summaryLayout.setWidth("100%");
		summaryLayout.setHeight("100%");
		
		Center center = new Center();
		summaryLayout.appendChild(center);
		center.appendChild(pembayaranTable);
		pembayaranTable.setWidth("100%");
		pembayaranTable.setHeight("100%");
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
		north.appendChild(penerimaanPanel);
		north.setSplittable(true);
		center = new Center();
		infoLayout.appendChild(center);
		
	}


	@Override
	public void valueChange(ValueChangeEvent evt) {
		try {
		
			String name = evt.getPropertyName();

			Object value = evt.getNewValue();
			
			if(value == null){
				
				return;
			}
			
			if (name.equals(MSemeruPOS.COLUMNNAME_SM_SemeruPOS_ID)){
				
				Integer SM_SemeruPOS_ID = Integer.valueOf(value.toString());
				
				MSemeruPOS dec = new MSemeruPOS(ctx, SM_SemeruPOS_ID, null);
				Integer C_Order_ID = dec.getC_Order_ID(); 
				
				StringBuilder SQLInv = new StringBuilder();
				SQLInv.append("SELECT C_Invoice_ID " );
				SQLInv.append("FROM C_Invoice " );
				SQLInv.append("WHERE AD_Client_ID = ? " );
				SQLInv.append("AND C_Order_ID = ? "  );

				Integer C_Invoice_ID = DB.getSQLValueEx(null, SQLInv.toString(), new Object[]{AD_Client_ID,C_Order_ID});
				
				if(C_Invoice_ID > 0){
					
					MInvoice inv = new MInvoice(ctx, C_Invoice_ID, null);
					
					int AD_Org_ID = inv.getAD_Org_ID();
					
					// Date set to Login Date
					Calendar cal = Calendar.getInstance();
					cal.setTime(Env.getContextAsDate(ctx, "#Date"));
					cal.set(Calendar.HOUR_OF_DAY, 0);
					cal.set(Calendar.MINUTE, 0);
					cal.set(Calendar.SECOND, 0);
					cal.set(Calendar.MILLISECOND, 0);
					dateTransactionField.setValue(new Timestamp(cal.getTimeInMillis()));
					
					//Timestamp dateOrder = inv.getDateOrdered();
					int C_BPartner_ID = inv.getC_BPartner_ID();
					BigDecimal totalPiutangPlg = Env.ZERO;
					BigDecimal totalPiutangTerbayar = Env.ZERO;
					BigDecimal sisaPiutangPlg = Env.ZERO;

					
					//dateTransactionField.setValue(dateOrder);
					org.setValue(AD_Org_ID);
					PelangganSearch.setValue(C_BPartner_ID);
										
					//
					if(C_Order_ID > 0){
						MOrder ord = new MOrder(ctx, C_Order_ID, null);
						totalPiutangPlg = ord.getGrandTotal();
					}else{
						totalPiutangPlg = inv.getGrandTotal();
					}
					
					//
					StringBuilder SQLterbayar = new StringBuilder();
					
					SQLterbayar.append("SELECT SUM(a.Amount) ");
					SQLterbayar.append("FROM C_AllocationLine a ");
					SQLterbayar.append("LEFT JOIN C_Invoice b ON a.C_Invoice_ID = b.C_Invoice_ID ");
					SQLterbayar.append("WHERE a.AD_Client_ID = ? ");
					SQLterbayar.append("AND a.C_Invoice_ID = ? ");
					SQLterbayar.append("AND b.IsSOTrx = 'Y' ");

					totalPiutangTerbayar = DB.getSQLValueBDEx(null, SQLterbayar.toString(), new Object[]{AD_Client_ID,C_Invoice_ID});
					
					if(totalPiutangTerbayar == null){
						totalPiutangTerbayar = Env.ZERO;
					}
					
					sisaPiutangPlg = totalPiutangPlg.subtract(totalPiutangTerbayar);
					totalPiutang.setText(format.format(totalPiutangPlg));
					sudahDibayar.setText(format.format(totalPiutangTerbayar));
					sisaPiutang.setText(format.format(sisaPiutangPlg));
					keteranganTextBox.setValue(inv.getDescription());
					
					if(sisaPiutangPlg.compareTo(Env.ZERO)<= 0){
						
						processPenerimaan.setEnabled(false);
					}
					
					if(sisaPiutangPlg.compareTo(Env.ZERO)<= 0 ){
						
						processPenerimaan.setEnabled(false);
					}
					
					Vector<Vector<Object>> data = null;
					
					//get detail					
					ArrayList<Integer> detailList = new ArrayList<Integer>();

					StringBuilder sqlInvLine = new StringBuilder();
					sqlInvLine.append("SELECT C_InvoiceLine_ID ");
					sqlInvLine.append("FROM C_InvoiceLine ");
					sqlInvLine.append("WHERE AD_Client_ID = ? ");
					sqlInvLine.append("AND C_Invoice_ID = ? ");
					
					
					PreparedStatement pstmt = null;
					ResultSet rs = null;
					try {
						pstmt = DB.prepareStatement(sqlInvLine.toString(), null);
						pstmt.setInt(1, AD_Client_ID);
						pstmt.setInt(2, C_Invoice_ID);

						rs = pstmt.executeQuery();
						while (rs.next()) {
							detailList.add(rs.getInt(1));
						}

					} catch (SQLException ex) {
						log.log(Level.SEVERE, sqlInvLine.toString(), ex);
					} finally {
						DB.close(rs, pstmt);
						rs = null;
						pstmt = null;
					}
					
					data = getTrxData(detailList, pembayaranTable);
					Vector<String> columnNames = getOISColumnNames();

					pembayaranTable.clear();

					// Set Model
					ListModelTable modelP = new ListModelTable(data);
					modelP.addTableModelListener(this);
					pembayaranTable.setData(modelP, columnNames);
					configureMiniTable(pembayaranTable);
								
					
					for (int i = 0 ; i < pembayaranTable.getRowCount() ; i++){
						
						pembayaranTable.setColumnReadOnly(i, true);
						
					}
						
				}else {
					
					return;
				}
				
				
			}
			
			
			
		} catch (Exception e) {
			log.log(Level.SEVERE, this.getClass().getCanonicalName()
				+ ".valueChange - ERROR: " + e.getMessage(), e);
		}
		
	}

	@Override
	public void tableChanged(WTableModelEvent e) {
		
	}

	@Override
	public void onEvent(Event e) throws Exception {
	
		if(e.getTarget().equals(processPenerimaan)){
			
			Integer C_DecorisPOS_ID = (Integer) noNota.getValue();
			
			if(C_DecorisPOS_ID == null){	
				C_DecorisPOS_ID = 0;
			}
			
			
			if(C_DecorisPOS_ID == 0){
				
				return;
			}
			
			
			if(tipeBayar.getValue().toString().equals("BANK")){
				
				Integer C_BankAccount_ID = (Integer) bankAccountSearch.getValue();
				
				if(C_BankAccount_ID == null){
					C_BankAccount_ID = 0;
				}
			
				
				if(C_BankAccount_ID == 0){
					
					FDialog.info(windowNo, null, "", "Bank Account Belum Di Isi", "Info");
					return;
				}
				
			}
			
			
			process(C_DecorisPOS_ID);
			processPenerimaan.setEnabled(false);
			cetakNota.setEnabled(true);
			noNota.setReadWrite(false);
			dateTransactionField.setReadonly(true);
			amtPay.setReadonly(true);
			bankAccountSearch.setReadWrite(false);
			keteranganTextBox.setReadonly(true);
			dateTransactionField.setEnabled(false);
			tipeBayar.setEnabled(false);
						
			
		} else if(e.getTarget().equals(buatbaru)){
			
			buatBaru();
			
		}else if(e.getTarget().equals(cetakNota)){
			
		     updateData(SM_SemeruPOSPrint_ID);
			 String trxName = Trx.createTrxName("printpembayaran");
			 //int AD_Process_ID = 0;
			 String url = "/home/idempiere/idempiere.gtk.linux.x86_64/idempiere-server/reports/";
			 //String url = "D:\\SourceCode\\iDempiereBase\\reports\\";
			 
			 StringBuilder SQLPayID = new StringBuilder();
			 SQLPayID.append("SELECT C_Payment_ID ");
			 SQLPayID.append("FROM C_Payment ");
			 SQLPayID.append("WHERE AD_Client_ID = ? ");
			 SQLPayID.append("AND DocumentNo = '" + noPembayaranTextBox.getValue().toString()+"'");

			 int C_PaymentPrint_ID = DB.getSQLValueEx(null, SQLPayID.toString(), new Object[]{AD_Client_ID});
			 
			 
			 MProcess proc = new MProcess(Env.getCtx(), 330062, trxName);
			 MPInstance instance = new MPInstance(proc,proc.getAD_Process_ID());
			 ProcessInfo pi = new ProcessInfo("Print Bukti Pembayaran", 330062);
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
			
			
		}else if(e.getTarget().equals(amtPay)){
			
			if(e.getName().equals(Events.ON_BLUR)){
				if(amtPay.getValue().compareTo(Env.ZERO) > 0){
					String sqlBank = "";
					sqlBank = "SELECT C_BankAccount_ID FROM C_POS WHERE CreatedByPOS_ID = ? AND AD_Client_ID = ?";
					int C_BankAcct_ID = DB.getSQLValueEx(ctx.toString(),sqlBank.toString(), new Object[] {CreatedByPOS_ID, AD_Client_ID });
					bankAccountSearch.setValue(C_BankAcct_ID);
					
				}
			}
		}
		
	}

	@Override
	public ADForm getForm() {
		return form;
	}
	
	//Table Leasing
	protected void configureMiniTable(IMiniTable miniTable) {
		miniTable.setColumnClass(0, Boolean.class, false);
		miniTable.setColumnClass(1, String.class, true); 		// 2-Product
		miniTable.setColumnClass(2, BigDecimal.class, true); 	// 3-QTY
		miniTable.setColumnClass(3, BigDecimal.class, true); 	// 4-Harga
		miniTable.setColumnClass(4, BigDecimal.class, true); 	// 5-Total

		miniTable.autoSize();

	}

	protected Vector<String> getOISColumnNames() {
		
		// Header Info
		Vector<String> columnNames = new Vector<String>(6);
		columnNames.add(Msg.getMsg(ctx, "Select"));
		columnNames.add("Produk");
		columnNames.add("Qty");
		columnNames.add("Harga");
		columnNames.add("Total");


		return columnNames;
	
	}
	
	private void  process(int C_DecorisPOS_ID){
		
				
		if(C_DecorisPOS_ID > 0 ){

			MSemeruPOS dec = new MSemeruPOS(ctx, C_DecorisPOS_ID, null);
			MInvoice inv = new MInvoice(ctx, dec.getC_Invoice_ID(), null);
			Map<String, BigDecimal> tenderType = new HashMap<String, BigDecimal>();
			
			//
			StringBuilder SQLDocTypeARR = new StringBuilder();
			SQLDocTypeARR.append("SELECT C_DocType_ID ");
			SQLDocTypeARR.append("FROM  C_DocType ");
			SQLDocTypeARR.append("WHERE AD_Client_ID = ?");
			SQLDocTypeARR.append("AND DocBaseType = '" + MDocType.DOCBASETYPE_ARReceipt+ "' ");
			SQLDocTypeARR.append("AND IsSoTrx ='Y' ");
			
			int C_DocTypeARR_ID = DB.getSQLValueEx(null, SQLDocTypeARR.toString(), AD_Client_ID);

			if(tipeBayar.getValue().toString().equals("TUNAI")){
				tenderType.put("TUNAI", amtPay.getValue());
			}
			
			if(tipeBayar.getValue().toString().equals("BANK")){
				tenderType.put("BANK", amtPay.getValue());
				
			}
			
			
			String NoReceipt = "";
			int count = 0;
			for (String key : tenderType.keySet()) {
				count++;
				String tipebayar = key.toUpperCase();
				BigDecimal payAmt = tenderType.get(key);
				String tenderName = "";
//				int C_BankAcct_ID = 0;
				
				if (tipebayar.equals("TUNAI")||tipebayar.equals("BANK")) {
					//String sqlBank = "";
					tenderName = MPayment.TENDERTYPE_Cash;

					if (tipebayar.equals("TUNAI")) {

						//sqlBank = "SELECT C_BankAccount_ID FROM C_POS WHERE CreatedByPOS_ID = ? AND AD_Client_ID = ?";
//						C_BankAcct_ID = (int) bankAccountSearch.getValue();
						
					} else if (tipebayar.equals("BANK")) {
						Integer b_acct_id = (Integer) bankAccountSearch.getValue();
						if (b_acct_id == null) {
							b_acct_id = 0;
						}
//						C_BankAcct_ID = b_acct_id;
						
					}

				}
				
				MPayment pay = new MPayment(ctx, 0, null);
				pay.setAD_Org_ID(inv.getAD_Org_ID());

				pay.setC_DocType_ID(C_DocTypeARR_ID);
				pay.setIsReceipt(true);
				pay.setC_BPartner_ID(inv.getC_BPartner_ID());
				pay.setDescription(keteranganTextBox.getText());
				
				Date tgl = dateTransactionField.getValue();
				SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd 00:00:00");
				String tglTostr = df.format(tgl);
				Timestamp dateTrx = Timestamp.valueOf(tglTostr);
				pay.setDateTrx(dateTrx);
				pay.setDateAcct(dateTrx);
				pay.setTenderType(tenderName);
				pay.setC_Currency_ID(inv.getC_Currency_ID());
				pay.set_ValueNoCheck("CreatedByPOS_ID", CreatedByPOS_ID);
				pay.setC_Invoice_ID(inv.getC_Invoice_ID());
				pay.setPayAmt(payAmt);
				pay.setC_Currency_ID(inv.getC_Currency_ID());
				pay.setC_BankAccount_ID((int)bankAccountSearch.getValue());
				pay.saveEx();
				pay.processIt(MPayment.DOCACTION_Complete);
				pay.saveEx();
				
				
				String decPOsdocType = "SELECT C_DocType_ID "
						+ " FROM C_DocType "
						+ " WHERE AD_Client_ID = ?"
						+ " AND DocBaseType = ? ";
				String docDec = "POS";
				
				
				int decDoc_ID = DB.getSQLValueEx(null, decPOsdocType, new Object[]{AD_Client_ID,docDec});
				
				
				MSemeruPOS smrPos = new MSemeruPOS(ctx, 0, null);
				smrPos.setC_DocType_ID(decDoc_ID);
				smrPos.setAD_Org_ID(pay.getAD_Org_ID());
				smrPos.setC_BPartner_ID(pay.getC_BPartner_ID());
				StringBuilder sqlBPLoc = new StringBuilder();
				sqlBPLoc.append("SELECT C_BPartner_Location_ID ");
				sqlBPLoc.append("FROM C_BPartner_Location ");
				sqlBPLoc.append("WHERE C_BPartner_ID = ? ");
				int C_BPartner_Location_ID = DB.getSQLValueEx(ctx.toString(),
						sqlBPLoc.toString(), new Object[] { pay.getC_BPartner_ID() });
				smrPos.setC_BPartner_Location_ID(C_BPartner_Location_ID);
				smrPos.setDateOrdered(pay.getDateTrx());
				smrPos.setDescription((String) keteranganTextBox.getValue().toString());
				smrPos.setDiscountAmt(Env.ZERO);
				smrPos.setCreatedByPos_ID(CreatedByPOS_ID);
				smrPos.setdpp(Env.ZERO);
				smrPos.setGrandTotal(pay.getPayAmt());
				smrPos.setTotalLines(pay.getPayAmt());
				smrPos.setTaxAmt(pay.getTaxAmt());
				smrPos.setC_Payment_ID(pay.getC_Payment_ID());
				smrPos.setC_Invoice_ID(pay.getC_Invoice_ID());
				smrPos.setC_Order_ID(dec.getC_Order_ID());
				
				if(tipebayar == "TUNAI"){
					smrPos.setPayType1("TUNAI");
					smrPos.setPembayaran1(payAmt);
				}
				
				if(tipebayar == "BANK"){
					smrPos.setPayType2("BANK");
					smrPos.setPembayaran2(payAmt);
				}
				smrPos.set_ValueOfColumnReturningBoolean("IsLeasing", false);
				smrPos.set_CustomColumn("TotalKembalian", Env.ZERO);
				smrPos.setIsPembatalan(false);
				smrPos.setIsSOTrx(true);
				smrPos.setIsPenjualan(false);
				smrPos.setIsPembayaran(true);
				smrPos.setIsReceipt(false);
				smrPos.saveEx();
				
				SM_SemeruPOSPrint_ID = smrPos.getSM_SemeruPOS_ID();
						
				if(count == 1){
					NoReceipt = pay.getDocumentNo();
				}else{
					
					NoReceipt = NoReceipt +" , "+ pay.getDocumentNo();
				}
			}	

			noPembayaranTextBox.setValue(NoReceipt);
			FDialog.info(windowNo, null, "", "Dokumen Penerimaan "+NoReceipt+" sudah Terbuat atas Dokumen Penjualan "+dec.getDocumentNo(), "Info");

			
		}
				
	}	
	
	
	private void buatBaru(){
		
		
		noNota.setReadWrite(true);
		dateTransactionField.setReadonly(false);
		dateTransactionField.setEnabled(true);
		amtPay.setReadonly(false);
		bankAccountSearch.setReadWrite(true);
		keteranganTextBox.setReadonly(false);
		processPenerimaan.setEnabled(true);
		tipeBayar.setEnabled(true);
		
		
		noPembayaranTextBox.setText(null);
		org.setValue(null);
		noNota.setValue(null);
		PelangganSearch.setValue(null);
		amtPay.setValue("0");
		bankAccountSearch.setValue(null);
		keteranganTextBox.setText(null);
		totalPiutang.setText("0");
		sudahDibayar.setText("0");
		sisaPiutang.setText("0");
		pembayaranTable.removeAllItems();
		pembayaranTable.clearTable();
		processPenerimaan.setEnabled(true);
		cetakNota.setEnabled(false);

		Calendar cal = Calendar.getInstance();
		cal.setTime(Env.getContextAsDate(ctx, "#Date"));
		cal.set(Calendar.HOUR_OF_DAY, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);
		cal.set(Calendar.MILLISECOND, 0);
		dateTransactionField.setValue(new Timestamp(cal.getTimeInMillis()));
		
		
	}
}
