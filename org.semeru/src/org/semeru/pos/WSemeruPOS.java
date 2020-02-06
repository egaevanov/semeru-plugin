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
import java.util.Properties;
import java.util.Vector;
import java.util.logging.Level;

import org.adempiere.util.Callback;
import org.adempiere.util.ProcessUtil;
import org.adempiere.webui.component.Button;
import org.adempiere.webui.component.Checkbox;
import org.adempiere.webui.component.Combobox;
import org.adempiere.webui.component.ConfirmPanel;
import org.adempiere.webui.component.Datebox;
import org.adempiere.webui.component.Grid;
import org.adempiere.webui.component.GridFactory;
import org.adempiere.webui.component.Label;
import org.adempiere.webui.component.ListModelTable;
import org.adempiere.webui.component.ListboxFactory;
import org.adempiere.webui.component.NumberBox;
import org.adempiere.webui.component.Panel;
import org.adempiere.webui.component.Row;
import org.adempiere.webui.component.Rows;
import org.adempiere.webui.component.Textbox;
import org.adempiere.webui.component.WListbox;
import org.adempiere.webui.component.Window;
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
import org.compiere.model.MAttributeSetInstance;
import org.compiere.model.MBPartner;
import org.compiere.model.MBPartnerLocation;
import org.compiere.model.MBankAccount;
import org.compiere.model.MCity;
import org.compiere.model.MDocType;
import org.compiere.model.MInOut;
import org.compiere.model.MInOutLine;
import org.compiere.model.MLocation;
import org.compiere.model.MLocator;
import org.compiere.model.MLookup;
import org.compiere.model.MLookupFactory;
import org.compiere.model.MOrder;
import org.compiere.model.MPInstance;
import org.compiere.model.MPeriod;
import org.compiere.model.MPriceList;
import org.compiere.model.MProcess;
import org.compiere.model.MProduct;
import org.compiere.model.MRMA;
import org.compiere.model.MRegion;
import org.compiere.model.MTaxCategory;
import org.compiere.model.MWarehouse;
import org.compiere.process.ProcessInfo;
import org.compiere.process.ProcessInfoParameter;
import org.compiere.util.DB;
import org.compiere.util.DisplayType;
import org.compiere.util.Env;
import org.compiere.util.KeyNamePair;
import org.compiere.util.Msg;
import org.compiere.util.Trx;
import org.semeru.pos.model.X_SM_SemeruLineTemp;
import org.semeru.pos.model.X_SM_SemeruPOS;
import org.semeru.pos.model.X_SM_SemeruTemp;
import org.semeru.process.SM_POSProcess;
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
 *         Decoris Pembayaran
 *
 */
public class WSemeruPOS extends SemeruPOS implements
		IFormController, EventListener<Event>, WTableModelListener,
		ValueChangeListener {

	// CustomForm
	private CustomForm form = new CustomForm();

	// BorderLayout
	private Borderlayout infoLayout = new Borderlayout();
	private Borderlayout mainLayout = new Borderlayout();
	private Borderlayout salesLayout = new Borderlayout();

	// Panel
	private Panel parameterPanel = new Panel();
	private Panel salesPanel = new Panel();
	private Panel southPanel = new Panel();

	// Grid
	private Grid parameterGrid = GridFactory.newGridLayout();
	private Grid parameterGrid2 = GridFactory.newGridLayout();
	private Grid parameterGrid3 = GridFactory.newGridLayout();
	private Grid parameterGrid4 = GridFactory.newGridLayout();

	private Grid southGrid1 = GridFactory.newGridLayout();
	private Grid southGrid2 = GridFactory.newGridLayout();
	private Grid southGrid3 = GridFactory.newGridLayout();
	private Grid southGrid4 = GridFactory.newGridLayout();

	private Grid southButtonGrid = GridFactory.newGridLayout();

	// No PreSales
	//private Label NoPresalesLabel = new Label("No Presales :");
	//private WSearchEditor noPresales = null;

	// No PreOrder
	//private Label NoPreOrderLabel = new Label("PreOrder :");
	//private WSearchEditor noPreOrder = null;

	protected BigDecimal totalPrice = Env.ZERO;
	protected BigDecimal totalDiskon = Env.ZERO;
	protected BigDecimal totalBeforeDiscount = Env.ZERO;
	private BigDecimal nilaiGrandTotal = Env.ZERO;
	private BigDecimal nilaiDpp = Env.ZERO;
	private BigDecimal nilaiPajak = Env.ZERO;
	private Properties ctx = Env.getCtx();

	private int M_Pricelist_ID = 0;
	private int AD_User_ID = 0;
	private int AD_Client_ID = Env.getAD_Client_ID(ctx);
	private int C_PaymentTerm_ID = 0;
	private int CreatedByPOS_ID = 0;
	private int C_Currency_ID = 303;
	private int windowNo = form.getWindowNo();
	private int C_OrderPrint_ID = 0;
	private int C_DecorisPOSPrint_ID = 0;
	private String AD_Language = Env.getAD_Language(ctx);

	private Integer rowIndex = null;
	private Integer productID = 0;
	private Integer bpartnerId = 0;

	// private Integer imeiIndex = null;

	private String m_docAction = "CO";

	private boolean isBackDate = false;
	private boolean isThermalPrint = false;
	private boolean isForceLimitPrice = false;
	private boolean isCompleteMultiLocator = false;
	private boolean isManualDocumentNo = false;
	private final String SOProcesSuperVisor = "ProcessSOSupervisor";

	// Button
	private Button plusButton = new Button();
	private Button plusButton2 = new Button();
	private Button processButton = new Button();
	private Button pelangganBaru = new Button();
	private Button printSuratJalan = new Button();

	private Button printThermalButton = new Button();
	private Button printButton = new Button();
	private Button buatBaru = new Button();

	// Status
	private Label diskonLabel = new Label("Diskon :");
	private Textbox diskonTextBox = new Textbox();

	private Label totalBelanjaLabel = new Label("Total Belanja :");
	private Textbox totalBelanjaTextBox = new Textbox();

	private Label totalBayarTunaiLabel = new Label("Bayar Tunai :");
	private Textbox totalBayarTunaiTextBox = new Textbox();

	private Label kembalianLabel = new Label("Kembalian :");
	private Textbox kembalianTextBox = new Textbox();

	private Label DPPLabel = new Label("DPP :");
	private Textbox DPPTextBox = new Textbox();

	private Label PPNLabel = new Label("PPN :");
	private Textbox PPNTextBox = new Textbox();

	private Label space = new Label(" ");
	private Textbox spaceBox = new Textbox();

	// Status
	private Label totalBrutoLabel = new Label("Total Bruto :");
	private Textbox totalBrutoTextBox = new Textbox();

	private Button deleteItem = new Button();

	// Status
	private Label TotalHeaderLabel = new Label("Total :");
	private Textbox TotalHeaderTextBox = new Textbox();

	// Status
	private Label totalBayarLabel = new Label("Total Bayar :");
	private Textbox totalBayarTextBox = new Textbox();

	// Status
	private Label kembaliLabel = new Label("Kembali :");
	private Textbox kembaliTextBox = new Textbox();

	// Status
	private Label keteranganLabel = new Label("Keterangan :");
	private Textbox keteranganTextBox = new Textbox();

	// TableLine
	private WListbox salesTable = ListboxFactory.newDataTable();

	// Toko
	private Label TokoLabel = new Label("Toko :");
	private WTableDirEditor TokoSearch = null;

	// BPartner
	private Label PelangganLabel = new Label("Pelanggan :");
	private WSearchEditor PelangganSearch = null;

	// Gudang
	private Label GudangLabel = new Label("Gudang :");
	private Combobox GudangSearch = new Combobox();

	// Sales
	private Label LokasiLabel = new Label("Lokasi :");
	private Combobox Lokasi = new Combobox();

	// Product
	private Label ProductLabel = new Label("Product :");
	private WSearchEditor ProductSearch = null;

	// Org
	private Label orgLabel = new Label("Cabang :");
	private WTableDirEditor orgSearch = null;

	// Leasing provider
	private Label leasingProviderLabel = new Label("Leasing :");
	private WTableDirEditor leasingProviderSearch = null;

	// DateOrdered
	private Label tanggalLabel = new Label("Tanggal :");
	private Datebox tanggalSearch = new Datebox();

	// SalesRep
	private Label salesLabel = new Label("Sales :");
	private WTableDirEditor salesSearch = null;

	// Sales
	private Label IMEILabel = new Label("IMEI :");
	private Combobox IMEIList = new Combobox();

	// IMei
	private Label bankAccountLabel = new Label("Akun Bank :");
	private WTableDirEditor bankAccountSearch = null;

	// NoNota
	private Label noNotaLabel = new Label("No. Nota :");
	private Textbox noNota = new Textbox();

	// NoNotaLama
	private Label noNotaOldLabel = new Label("No. Nota Lama :");
	private WSearchEditor noNotaOld = null;

	// Daftar harga
	private Label DaftarHargaLabel = new Label("Daftar Harga :");
	private Combobox DaftarHargaSearch = new Combobox();

	// PaymentRule1
	private Button payruleBoxTunai = new Button();
	private Decimalbox paymentTunai = new Decimalbox();

	// PaymentRule2
	private Button payruleBoxBank = new Button();
	private Decimalbox paymentBank = new Decimalbox();

	// PaymentRule3
	private Button payruleBoxLeasing = new Button();
	private Decimalbox paymentLeasing = new Decimalbox();

	// PaymentRule4
	private Button payruleBoxHutang = new Button();
	private Decimalbox paymentHutang = new Decimalbox();

	// Formatter
	private DecimalFormat format = DisplayType
			.getNumberFormat(DisplayType.Amount);

	// Pik Up
	// private Label PickUpLabel = new Label("Pickup :");
	private Checkbox PickUp = new Checkbox();

	private Label isDebitLabel = new Label("Debit Card :");
	private Checkbox IsDebit = new Checkbox();

	private int BankAccountLeasing = 0;

	int inout_ID = 0;

	public WSemeruPOS() {

		boolean IsOK = dyinit();

		if (!IsOK) {
			return;
		}

		init();

		String msg = cekAkses();

		if (msg != "")
			return;
	}

	private void init() {

		form.appendChild(mainLayout);
		mainLayout.setWidth("99%");
		mainLayout.setHeight("100%");

		form.addEventListener(DialogEvents.ON_WINDOW_CLOSE,
				new EventListener<Event>() {

					@Override
					public void onEvent(Event arg0) throws Exception {
						form.dispose();
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

		// No Nota
		row = rows.newRow();
		row.appendCellChild(noNotaLabel.rightAlign(), 1);
		row.appendCellChild(noNota, 2);
		//noNota.setHflex("true");

		row = rows.newRow();
		row.appendCellChild(noNotaOldLabel.rightAlign(), 1);
		row.appendCellChild(noNotaOld.getComponent(), 2);
		//noNotaOld.getComponent().setHflex("true");

		row = rows.newRow();
		row.appendCellChild(PelangganLabel.rightAlign(), 1);
		Hbox pelangganHbox = new Hbox();
		row.appendCellChild(pelangganHbox, 2);
		pelangganHbox.appendChild(PelangganSearch.getComponent());
		//PelangganSearch.getComponent().setHflex("true");

		pelangganHbox.appendChild(pelangganBaru);
		pelangganBaru.addActionListener(this);
		pelangganBaru.setLabel("+");

		row = rows.newRow();
		row.appendCellChild(DaftarHargaLabel.rightAlign(), 1);
		row.appendCellChild(DaftarHargaSearch, 2);
		//DaftarHargaSearch.setHflex("true");

		// row = rows.newRow();
		// row.appendCellChild(new Space(), 1);
		// row.appendCellChild(deleteItem, 2);

		row = rows.newRow();
		row.appendCellChild(new Space(), 1);
		Hbox pickupHbox = new Hbox();
		row.appendCellChild(pickupHbox, 2);
		pickupHbox.appendChild(deleteItem);
		// row.appendCellChild(PickUpLabel.rightAlign(), 1);
		pickupHbox.appendChild(PickUp);
		PickUp.setLabel("Pickup");
		//deleteItem.setHflex("true");
		deleteItem.setLabel("Hapus Detail");
		deleteItem.addActionListener(this);

		northMainBox.appendChild(parameterGrid2);
		parameterGrid2.setWidth("100%");
		parameterGrid2.setStyle("Height:100%;");

		Rows rows2 = null;
		Row row2 = null;
		rows2 = parameterGrid2.newRows();

		row2 = rows2.newRow();
		row2.appendCellChild(TokoLabel.rightAlign(), 1);
		row2.appendCellChild(TokoSearch.getComponent(), 2);
		//TokoSearch.getComponent().setHflex("true");

//		row2 = rows2.newRow();
//		row2.appendCellChild(NoPresalesLabel.rightAlign(), 1);
//		row2.appendCellChild(noPresales.getComponent(), 2);
////		noPresales.getComponent().setHflex("true");

		if (AD_Language.toUpperCase().equals("EN_US")) {
			tanggalSearch.setFormat("dd/MM/yyyy");
		} else if (AD_Language.toUpperCase().equals("IN_ID")) {
			tanggalSearch.setFormat("dd/MM/yyyy");
		}

		row2 = rows2.newRow();
		row2.appendCellChild(tanggalLabel.rightAlign(), 1);
		row2.appendCellChild(tanggalSearch, 2);
		//tanggalSearch.setHflex("true");

		row2 = rows2.newRow();
		row2.appendCellChild(GudangLabel.rightAlign(), 1);
		row2.appendCellChild(GudangSearch, 2);
		//GudangSearch.setHflex("true");
		GudangSearch.addEventListener(0, "onChange", this);

		row2 = rows2.newRow();
		row2.appendCellChild(ProductLabel.rightAlign(), 1);
		row2.appendCellChild(ProductSearch.getComponent(), 2);
		//ProductSearch.getComponent().setHflex("true");

		northMainBox.appendChild(parameterGrid3);
		parameterGrid3.setWidth("100%");
		parameterGrid3.setStyle("Height:100%;");
		Rows rows3 = null;
		Row row3 = null;
		rows3 = parameterGrid3.newRows();

		row3 = rows3.newRow();
		row3.appendCellChild(orgLabel.rightAlign(), 1);
		row3.appendCellChild(orgSearch.getComponent(), 2);
		//orgSearch.getComponent().setHflex("true");

//		row3 = rows3.newRow();
//		row3.appendCellChild(NoPreOrderLabel.rightAlign(), 1);
//		row3.appendCellChild(noPreOrder.getComponent(), 2);
////		noPreOrder.getComponent().setHflex("true");

		row3 = rows3.newRow();
		row3.appendCellChild(salesLabel.rightAlign(), 1);
		row3.appendCellChild(salesSearch.getComponent(), 2);
		//salesSearch.getComponent().setHflex("true");

		row3 = rows3.newRow();
		row3.appendCellChild(LokasiLabel.rightAlign(), 1);
		row3.appendCellChild(Lokasi, 2);
		//Lokasi.setHflex("true");
		Lokasi.addEventListener(0, "onChange", this);

		row3 = rows3.newRow();
		row3.appendCellChild(IMEILabel.rightAlign(), 1);
		row3.appendCellChild(IMEIList, 2);
		//IMEIList.setHflex("true");
		IMEIList.addEventListener(0, "onChange", this);

		Rows rows4 = null;
		Row row4 = null;
		northMainBox.appendChild(parameterGrid4);
		parameterGrid4.setWidth("100%");
		parameterGrid4.setStyle("Height:100%;");

		rows4 = parameterGrid4.newRows();
		row4 = rows4.newRow();
		row4.appendCellChild(TotalHeaderLabel.rightAlign(), 1);
		row4.appendCellChild(TotalHeaderTextBox, 2);
		//TotalHeaderTextBox.setHflex("true");
		TotalHeaderTextBox
				.setStyle("font-size:20px;font-weight:bold;text-align:right;border: 1px solid #000000;");
		TotalHeaderTextBox.setHeight("50px");
		TotalHeaderTextBox.setText("0");

		row4 = rows4.newRow();
		row4.appendCellChild(totalBayarLabel.rightAlign(), 1);
		row4.appendCellChild(totalBayarTextBox, 2);
		//totalBayarTextBox.setHflex("true");
		totalBayarTextBox
				.setStyle("font-size:20px;font-weight:bold;text-align:right;border: 1px solid #000000;");
		totalBayarTextBox.setHeight("50px");
		totalBayarTextBox.setText("0");

		row4 = rows4.newRow();
		row4.appendCellChild(kembaliLabel.rightAlign(), 1);
		row4.appendCellChild(kembaliTextBox, 2);
		//kembaliTextBox.setHflex("true");
		kembaliTextBox
				.setStyle("font-size:20px;font-weight:bold;text-align:right;border: 1px solid #000000;");
		kembaliTextBox.setHeight("50px");
		kembaliTextBox.setText("0");

		// SouthPanel
		South south = new South();
		mainLayout.appendChild(south);
		south.setStyle(grid);
		south.appendChild(southPanel);
		Hbox southMainBox = new Hbox();
		southPanel.appendChild(southMainBox);

		Hbox subMainBox1 = new Hbox();
		southMainBox.appendChild(subMainBox1);
		subMainBox1.appendChild(southGrid1);
		subMainBox1
				.setStyle("border: 1px solid #C0C0C0 ;padding:1px;border-radius:3px;margin-top:1px;margin-left:1px");
		southGrid1.setWidth("100%");
		//// southGrid1.setHflex("min");

		Rows southRows1 = null;
		Row southRow1 = null;

		southRows1 = southGrid1.newRows();

		southRow1 = southRows1.newRow();
		Hbox pembayaran1 = new Hbox();
		southRow1.appendChild(pembayaran1);
		pembayaran1.setWidth("100%");
		pembayaran1.appendChild(payruleBoxTunai);
		payruleBoxTunai.setStyle("align:right;");
		payruleBoxTunai.setHeight("24px");
		payruleBoxTunai
				.setStyle("border-radius:3px;border: 1px solid #C0C0C0 ;");
		payruleBoxTunai.setEnabled(false);
		payruleBoxTunai.setLabel("Tunai");
		payruleBoxTunai.setWidth("75px");
		payruleBoxTunai.addActionListener(this);

		pembayaran1.appendChild(paymentTunai);
		paymentTunai.setStyle("text-align:right;");
		paymentTunai.setValue("0.00");
		paymentTunai.addEventListener(0, Events.ON_BLUR, this);
		paymentTunai.addEventListener(0, Events.ON_FOCUS, this);
		//paymentTunai.setHflex("true");
		paymentTunai.setFormat("#,###,###,##0.00");
		paymentTunai.setWidth("100%");
		paymentTunai.setMaxlength(32);

		southRow1 = southRows1.newRow();
		Hbox pembayaran2 = new Hbox();
		southRow1.appendChild(pembayaran2);
		pembayaran2.setWidth("100%");
		pembayaran2.appendChild(payruleBoxBank);
		payruleBoxBank.setHeight("24px");
		payruleBoxBank
				.setStyle("border-radius:3px;border: 1px solid #C0C0C0 ;");
		payruleBoxBank.setEnabled(false);
		payruleBoxBank.setLabel("Bank");
		payruleBoxBank.setWidth("75px");
		payruleBoxBank.addActionListener(this);

		pembayaran2.appendChild(paymentBank);
		paymentBank.setStyle("text-align:right;");
		paymentBank.setValue("0.00");
		paymentBank.addEventListener(0, Events.ON_BLUR, this);
		paymentBank.addEventListener(0, Events.ON_FOCUS, this);
		//paymentBank.setHflex("true");
		paymentBank.setFormat("#,###,###,##0.00");
		paymentBank.setWidth("100%");
		paymentBank.setMaxlength(32);

		southRow1 = southRows1.newRow();
		Hbox pembayaran3 = new Hbox();
		pembayaran3.setWidth("100%");
		southRow1.appendChild(pembayaran3);
		pembayaran3.appendChild(payruleBoxLeasing);
		payruleBoxLeasing.setHeight("24px");
		payruleBoxLeasing
				.setStyle("border-radius:3px;border: 1px solid #C0C0C0 ;");
		payruleBoxLeasing.setEnabled(false);
		payruleBoxLeasing.setLabel("Leasing");
		payruleBoxLeasing.setWidth("75px");
		payruleBoxLeasing.addActionListener(this);

		pembayaran3.appendChild(paymentLeasing);
		paymentLeasing.setStyle("text-align:right;");
		paymentLeasing.setValue("0.00");
		paymentLeasing.addEventListener(0, Events.ON_BLUR, this);
		paymentLeasing.addEventListener(0, Events.ON_FOCUS, this);
		//paymentLeasing.setHflex("true");
		paymentLeasing.setFormat("#,###,###,##0.00");
		paymentLeasing.setWidth("100%");
		paymentLeasing.setMaxlength(32);

		southRow1 = southRows1.newRow();
		Hbox pembayaran4 = new Hbox();
		pembayaran4.setWidth("100%");
		southRow1.appendChild(pembayaran4);
		pembayaran4.appendChild(payruleBoxHutang);
		payruleBoxHutang.setHeight("24px");
		payruleBoxHutang
				.setStyle("border-radius:3px;border: 1px solid #C0C0C0 ;");
		payruleBoxHutang.setEnabled(false);
		payruleBoxHutang.setLabel("Hutang");
		payruleBoxHutang.setWidth("75px");
		payruleBoxHutang.addActionListener(this);

		pembayaran4.appendChild(paymentHutang);
		paymentHutang.setStyle("text-align:right;");
		paymentHutang.setValue("0.00");
		paymentHutang.addEventListener(0, Events.ON_BLUR, this);
		paymentHutang.addEventListener(0, Events.ON_FOCUS, this);
		//paymentHutang.setHflex("true");
		paymentHutang.setFormat("#,###,###,##0.00");
		paymentHutang.setWidth("100%");
		paymentHutang.setMaxlength(32);

		southMainBox.appendChild(southGrid2);
		southGrid2.setWidth("100%");
		southGrid2.setStyle("Height:100%;");

		Rows southRows2 = null;
		Row southRow2 = null;

		southRows2 = southGrid2.newRows();
		southRow2 = southRows2.newRow();

		southRow2.appendCellChild(leasingProviderLabel.rightAlign(), 1);
		southRow2.appendCellChild(leasingProviderSearch.getComponent(), 2);
		//leasingProviderLabel.setHflex("true");
		//leasingProviderSearch.getComponent().setHflex("true");

		southRow2 = southRows2.newRow();
		southRow2.appendCellChild(bankAccountLabel.rightAlign(), 1);
		southRow2.appendCellChild(bankAccountSearch.getComponent(), 2);
		//bankAccountLabel.setHflex("true");
		//bankAccountSearch.getComponent().setHflex("true");

		southRow2 = southRows2.newRow();
		southRow2.appendCellChild(isDebitLabel.rightAlign(), 1);
		southRow2.appendCellChild(IsDebit);
		IsDebit.addActionListener(this);
		IsDebit.setEnabled(false);
		//// bankAccountLabel.setHflex("true");
		//// bankAccountSearch.getComponent().setHflex("true");

		southRow2 = southRows2.newRow();
		southRow2.appendCellChild(keteranganLabel.rightAlign(), 1);
		southRow2.appendCellChild(keteranganTextBox, 2);
		//keteranganTextBox.setHflex("true");
		keteranganTextBox.setRows(2);
		keteranganTextBox.setMaxlength(255);

		southMainBox.appendChild(southGrid3);
		southGrid3.setWidth("100%");
		southGrid3.setStyle("Height:100%;");

		Rows southRows3 = null;
		Row southRow3 = null;

		southRows3 = southGrid3.newRows();

		southRow3 = southRows3.newRow();
		southRow3.appendCellChild(totalBrutoLabel.rightAlign(), 1);
		southRow3.appendCellChild(totalBrutoTextBox, 2);
		totalBrutoTextBox.setStyle("text-align:right;");
		totalBrutoTextBox.setText("0");
		//totalBrutoTextBox.setHflex("true");
		//totalBrutoTextBox.setHflex("true");

		southRow3 = southRows3.newRow();
		southRow3.appendCellChild(totalBelanjaLabel.rightAlign(), 1);
		southRow3.appendCellChild(totalBelanjaTextBox, 2);
		totalBelanjaLabel.setStyle("font-weight:bold;");
		totalBelanjaTextBox.setStyle("font-weight:bold;text-align:right;");
		totalBelanjaTextBox.setStyle("text-align:right;");
		totalBelanjaTextBox.setText("0");
		//totalBelanjaLabel.setHflex("true");
		//totalBelanjaTextBox.setHflex("true");

		southRow3 = southRows3.newRow();
		southRow3.appendCellChild(totalBayarTunaiLabel.rightAlign(), 1);
		southRow3.appendCellChild(totalBayarTunaiTextBox, 2);
		totalBayarTunaiLabel.setStyle("font-weight:bold;");
		totalBayarTunaiTextBox.setStyle("font-weight:bold;text-align:right;");
		totalBayarTunaiTextBox.setText("0");
		//totalBayarTunaiLabel.setHflex("true");
		//totalBayarTunaiTextBox.setHflex("true");

		southRow3 = southRows3.newRow();
		southRow3.appendCellChild(DPPLabel.rightAlign(), 1);
		southRow3.appendCellChild(DPPTextBox, 2);
		DPPTextBox.setText("0");
		DPPTextBox.setStyle("text-align:right;");
		//DPPLabel.setHflex("true");
		//DPPTextBox.setHflex("true");

		southMainBox.appendChild(southGrid4);
		southGrid3.setWidth("100%");
		southGrid3.setStyle("Height:100%;");

		Rows southRows4 = null;
		Row southRow4 = null;

		southRows4 = southGrid4.newRows();

		southRow4 = southRows4.newRow();
		southRow4.appendCellChild(diskonLabel.rightAlign(), 1);
		southRow4.appendCellChild(diskonTextBox, 2);
		diskonTextBox.setStyle("text-align:right;");
		diskonTextBox.setText("0");
		//diskonLabel.setHflex("true");
		//diskonTextBox.setHflex("true");

		southRow4 = southRows4.newRow();
		southRow4.appendCellChild(kembalianLabel.rightAlign(), 1);
		southRow4.appendCellChild(kembalianTextBox, 2);
		kembalianLabel.setStyle("font-weight:bold;");
		kembalianTextBox.setStyle("font-weight:bold;text-align:right;");
		kembalianTextBox.setText("0");
		//kembalianLabel.setHflex("true");
		//kembalianTextBox.setHflex("true");

		southRow4 = southRows4.newRow();
		southRow4.appendCellChild(space.rightAlign(), 1);
		southRow4.appendCellChild(spaceBox, 2);
		spaceBox.setStyle("border: 0px solid #FFFFFF ;opacity: 0;");

		southRow4 = southRows4.newRow();
		southRow4.appendCellChild(PPNLabel.rightAlign(), 1);
		southRow4.appendCellChild(PPNTextBox, 2);
		PPNTextBox.setText("0");
		PPNTextBox.setStyle("text-align:right;");
		//PPNLabel.setHflex("true");
		//PPNTextBox.setHflex("true");

		Hbox south2 = new Hbox();
		southPanel.appendChild(south2);
		south2.appendChild(southButtonGrid);

		Rows southBtns = null;
		Row southBtn = null;

		southBtns = southButtonGrid.newRows();
		southBtn = southBtns.newRow();

		southButtonGrid.setWidth("100%");
		southButtonGrid.setStyle("Height:100%;");

		southBtn.appendChild(buatBaru);
		buatBaru.setLabel("Buat baru");
		buatBaru.addActionListener(this);
		//buatBaru.setHflex("true");

		southBtn.appendCellChild(processButton, 1);
		processButton.setLabel("Proses");
		processButton.addActionListener(this);
		//processButton.setHflex("true");

		southBtn.appendCellChild(printThermalButton, 1);
		printThermalButton.setLabel("Nota Kecil");
		printThermalButton.addActionListener(this);
		//printThermalButton.setHflex("true");

		southBtn.appendCellChild(printButton, 1);
		printButton.setLabel("Nota Besar");
		printButton.addActionListener(this);
		//printButton.setHflex("true");

		southBtn.appendCellChild(printSuratJalan, 1);
		printSuratJalan.setLabel("Surat Jalan");
		printSuratJalan.addActionListener(this);
		//printSuratJalan.setHflex("true");

		south = new South();
		salesPanel.appendChild(salesLayout);
		salesLayout.appendChild(south);
		salesPanel.setWidth("100%");
		salesPanel.setHeight("100%");
		salesLayout.setWidth("100%");
		salesLayout.setHeight("100%");

		Center center = new Center();
		salesLayout.appendChild(center);
		center.appendChild(salesTable);
		salesLayout.setWidth("100%");
		salesLayout.setHeight("100%");
		center.setStyle(grid);

		center = new Center();
		mainLayout.appendChild(center);
		center.appendChild(infoLayout);
		infoLayout.setWidth("100%");
		infoLayout.setHeight("100%");

		north = new North();
		north.setHeight("100%");
		infoLayout.appendChild(north);
		north.appendChild(salesPanel);
		north.setSplittable(false);
		center = new Center();
		infoLayout.appendChild(center);

	}

	private boolean dyinit() {
		AD_User_ID = Env.getAD_User_ID(ctx);
		boolean IsOK = true;
		int PL_ID = 0;

		// Detect user as kasir
		String sqlKasir = "SELECT C_BPartner_ID FROM AD_User WHERE AD_Client_ID = ? AND AD_User_ID = ?";
		CreatedByPOS_ID = DB.getSQLValueEx(ctx.toString(), sqlKasir.toString(),
				new Object[] { Env.getAD_Client_ID(ctx), AD_User_ID });

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

		// get value Org by kasir
		String sqlOrg = "SELECT AD_Org_ID FROM C_POS WHERE  AD_Client_ID = ? AND CreatedByPOS_ID = ?";
		int org_id = DB.getSQLValueEx(ctx.toString(), sqlOrg, new Object[] {
				AD_Client_ID, CreatedByPOS_ID });

		// Initial value Cabang
		MLookup orgLookup = MLookupFactory.get(ctx, form.getWindowNo(), 0,
				2163, DisplayType.TableDir);
		orgSearch = new WTableDirEditor("AD_Org_ID", true, false, true,
				orgLookup);
		orgSearch.addValueChangeListener(this);
		orgSearch.setMandatory(true);
		orgSearch.setValue(org_id);

		if (CreatedByPOS_ID > 0) {

			// Detect allow feature multi locator
			String sqlCompleteMultiLocator = "SELECT IsCompleteMultiLocator FROM C_POS WHERE  AD_Client_ID = ? AND CreatedByPOS_ID = ?";
			String completeMultiLocator = DB.getSQLValueStringEx(
					ctx.toString(), sqlCompleteMultiLocator, new Object[] {
							AD_Client_ID, CreatedByPOS_ID });

			if (completeMultiLocator.toUpperCase().equals("Y")) {
				isCompleteMultiLocator = true;
			} else {
				isCompleteMultiLocator = false;
			}

			// Detect allow feature backdate
			String sqlIsBackDate = "SELECT IsBackDate FROM C_POS WHERE  AD_Client_ID = ? AND CreatedByPOS_ID = ?";
			String backDate = DB.getSQLValueStringEx(ctx.toString(),
					sqlIsBackDate,
					new Object[] { AD_Client_ID, CreatedByPOS_ID });

			if (backDate.toUpperCase().equals("Y")) {
				isBackDate = true;
			} else {
				isBackDate = false;
			}

			if (isBackDate) {
				tanggalSearch.setEnabled(true);
			} else {
				tanggalSearch.setEnabled(false);
			}

			String sqlGetPL = "SELECT M_PriceList_ID FROM C_POS WHERE  AD_Client_ID = ? AND CreatedByPOS_ID = ?";
			PL_ID = DB.getSQLValueEx(ctx.toString(), sqlGetPL, new Object[] {
					AD_Client_ID, CreatedByPOS_ID });

			// Detect feature thermal print
			String thermalPrintSql = "SELECT IsThermalPrint FROM C_POS WHERE  AD_Client_ID = ? AND CreatedByPOS_ID = ?";
			String isThermal = DB.getSQLValueStringEx(ctx.toString(),
					thermalPrintSql, new Object[] { AD_Client_ID,
							CreatedByPOS_ID });

			if (isThermal.toUpperCase().equals("Y")) {
				isThermalPrint = true;
			} else {
				isThermalPrint = false;
			}

			// Detect feature manual dokument
			String manualDocumentNoSql = "SELECT IsManualDocumentNo FROM C_Pos WHERE AD_Client_ID = ? AND  CreatedByPOS_ID = ? ";
			String isManualDoc = DB.getSQLValueStringEx(ctx.toString(),
					manualDocumentNoSql, new Object[] { AD_Client_ID,
							CreatedByPOS_ID });
			if (isManualDoc.toUpperCase().equals("Y")) {
				isManualDocumentNo = true;
			} else {
				isManualDocumentNo = false;
			}

		}

		// Initial value bank lookup
		MLookup lookupBank = MLookupFactory.get(ctx, form.getWindowNo(), 0,
				3880, DisplayType.TableDir);
		bankAccountSearch = new WTableDirEditor("C_BankAccount_ID", true,
				false, true, lookupBank);
		bankAccountSearch.addValueChangeListener(this);
		bankAccountSearch.setMandatory(true);

		// Initial value client lokup
		MLookup lookupClient = MLookupFactory.get(ctx, form.getWindowNo(), 0,
				14621, DisplayType.TableDir);
		TokoSearch = new WTableDirEditor("AD_Table_ID", true, false, true,
				lookupClient);
		TokoSearch.addValueChangeListener(this);
		TokoSearch.setMandatory(true);
		TokoSearch.setValue(AD_Client_ID);

		// Initial value BP lookup
		MLookup lookupBP = MLookupFactory.get(ctx, form.getWindowNo(), 0, 2893,
				DisplayType.Search);
		PelangganSearch = new WSearchEditor("C_BPartner_ID", true, false, true,
				lookupBP);
		PelangganSearch.addValueChangeListener(this);
		PelangganSearch.setMandatory(true);

		// Initial value presales lookup
		// 1002329-UAT
//		MLookup lookupPreSales = MLookupFactory.get(ctx, form.getWindowNo(), 0,
//				1002249, DisplayType.Search);
//		noPresales = new WSearchEditor("C_Decoris_PreSales_ID", true, false,
//				true, lookupPreSales);
//		noPresales.addValueChangeListener(this);
//		noPresales.setMandatory(true);

		// Initial value presales lookup
		// 1007664-UAT
//		MLookup lookupPreOrder = MLookupFactory.get(ctx, form.getWindowNo(), 0,
//				1002326, DisplayType.Search);
//		noPreOrder = new WSearchEditor("C_Decoris_PreOrder_ID", true, false,
//				true, lookupPreOrder);
//		noPreOrder.addValueChangeListener(this);
//		noPreOrder.setMandatory(true);

		// Initial value product lookup
		MLookup lookupProduct = MLookupFactory.get(ctx, form.getWindowNo(), 0,
				1402, DisplayType.Search);
		ProductSearch = new WSearchEditor("M_Product_ID", true, false, true,
				lookupProduct);
		ProductSearch.addValueChangeListener(this);
		ProductSearch.setReadWrite(false);
		ProductSearch.setMandatory(true);

		// Initial value product lookup
		MLookup lookupNotaOld = MLookupFactory.get(ctx, form.getWindowNo(), 0,
				1001495, DisplayType.Search);
		noNotaOld = new WSearchEditor("SM_SemeruPOS_ID", true, false, true,
				lookupNotaOld);
		noNotaOld.addValueChangeListener(this);
		noNotaOld.setReadWrite(false);
		noNotaOld.setMandatory(true);

		Integer Org_ID = (Integer) orgSearch.getValue();
		if (Org_ID == null) {
			FDialog.info(windowNo, null, "",
					"Cabang tidak di ketahui, Silakan hub. Admin", "Info");
			IsOK = false;
			return IsOK;
		}

		// Initial value Warehouse
		ArrayList<KeyNamePair> listWH = loadWarehouse(Org_ID);
		Integer warehouse_ID = 0;
		GudangSearch.removeAllItems();
		for (KeyNamePair list : listWH) {
			GudangSearch.appendItem(list.getName());
		}

		if (listWH.size() > 0) {
			warehouse_ID = getIDFromComboBox(GudangSearch,
					MWarehouse.Table_Name, MWarehouse.COLUMNNAME_Name);
		}
	

		// Intial value locator
		ArrayList<KeyNamePair> listLoc = loadLocator(warehouse_ID, Org_ID);
		Lokasi.removeAllItems();
		for (KeyNamePair priceList : listLoc) {
			Lokasi.appendItem(priceList.getName());
		}

		// Initial value daftar harga
		ArrayList<KeyNamePair> listPL = loadPriceList();
		int countPL = 0;
		int indexPL = 0;
		MPriceList pList = new MPriceList(ctx, PL_ID, null);
		DaftarHargaSearch.removeAllItems();
		for (KeyNamePair priceList : listPL) {
			DaftarHargaSearch.appendItem(priceList.getName());

			if (priceList.getName().equals(pList.getName())) {
				indexPL = countPL;
			}
			countPL++;
		}
		DaftarHargaSearch.setSelectedIndex(indexPL);

		// Initial value sales lookup
		MLookup salesRepLookup = MLookupFactory.get(ctx, form.getWindowNo(), 0,
				2186, DisplayType.TableDir);
		salesSearch = new WTableDirEditor("SalesRep_ID", true, false, true,
				salesRepLookup);
		salesSearch.addValueChangeListener(this);
		salesSearch.setMandatory(true);

		// Initial value leasing lookup
		MLookup leasingLookup = MLookupFactory.get(ctx, form.getWindowNo(), 0,
				1001555, DisplayType.List);
		leasingProviderSearch = new WTableDirEditor("LeaseProvider", true,
				false, true, leasingLookup);
		leasingProviderSearch.addValueChangeListener(this);

		// Date set to Login Date
		Calendar cal = Calendar.getInstance();
		cal.setTime(Env.getContextAsDate(ctx, "#Date"));
		cal.set(Calendar.HOUR_OF_DAY, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);
		cal.set(Calendar.MILLISECOND, 0);
		tanggalSearch.setValue(new Timestamp(cal.getTimeInMillis()));

		// initial IsDebitCard
		// IsDebitCardAllowed = cekDebitCardAllow();

		initialDisplay();

		return IsOK;

	}

	protected void configureMiniTable(IMiniTable miniTable) {

		miniTable.setColumnClass(0, Boolean.class, false);
		miniTable.setColumnClass(1, String.class, true); // 2-Product
		miniTable.setColumnClass(2, BigDecimal.class, false); // 3-Qty
		miniTable.setColumnClass(3, BigDecimal.class, true); // 4-priceList
		miniTable.setColumnClass(4, BigDecimal.class, false); // 5-price
		miniTable.setColumnClass(5, String.class, true); // 6-diskon
		miniTable.setColumnClass(6, BigDecimal.class, true); // 7-totalprice
		miniTable.setColumnClass(7, String.class, true); // 8-locator
		miniTable.setColumnClass(8, String.class, true); // 9-imei
		miniTable.setColumnClass(9, String.class, true); // 10-tipepajak

		miniTable.autoSize();

	}

	protected Vector<String> getOISColumnNames() {

		// Header Info
		Vector<String> columnNames = new Vector<String>(10);
		columnNames.add(Msg.getMsg(ctx, "Select"));
		columnNames.add(Msg.translate(ctx, "M_Product_ID"));
		columnNames.add(Msg.translate(ctx, "Quantity"));
		columnNames.add(Msg.translate(ctx, "M_PriceList_ID"));
		columnNames.add("Price");
		columnNames.add("Diskon");
		columnNames.add("Total Price");
		columnNames.add("Locator");
		columnNames.add("IMEI");
		columnNames.add("Tipe Pajak");

		return columnNames;

	}

	protected void configureMiniTableExistingPresales(IMiniTable miniTable) {

		miniTable.setColumnClass(0, Boolean.class, false);
		miniTable.setColumnClass(1, String.class, true); // 2-Product
		miniTable.setColumnClass(2, BigDecimal.class, false); // 3-Qty
		miniTable.setColumnClass(3, BigDecimal.class, true); // 4-priceList
		miniTable.setColumnClass(4, BigDecimal.class, false); // 5-price
		miniTable.setColumnClass(5, String.class, true); // 6-diskon
		miniTable.setColumnClass(6, BigDecimal.class, true); // 7-totalprice
		miniTable.setColumnClass(7, String.class, true); // 8-locator
		miniTable.setColumnClass(8, String.class, true); // 9-imei
		miniTable.setColumnClass(9, String.class, true); // 10-tipepajak

		miniTable.autoSize();

	}

	protected Vector<String> getOISColumnNamesExistingPresales() {

		// Header Info
		Vector<String> columnNames = new Vector<String>(10);
		columnNames.add(Msg.getMsg(ctx, "Select"));
		columnNames.add(Msg.translate(ctx, "M_Product_ID"));
		columnNames.add(Msg.translate(ctx, "Quantity"));
		columnNames.add(Msg.translate(ctx, "M_PriceList_ID"));
		columnNames.add("Price");
		columnNames.add("Diskon");
		columnNames.add("Total Price");
		columnNames.add("Locator");
		columnNames.add("IMEI");
		columnNames.add("Tipe Pajak");

		return columnNames;

	}

	@Override
	public void valueChange(ValueChangeEvent evt) {
		try {
			String name = evt.getPropertyName();

			Object value = evt.getNewValue();
//			Integer presales_ID = (Integer) noPresales.getValue();
//			Integer preOrder_ID = (Integer) noPreOrder.getValue();
//
//			if (presales_ID == null) {
//				presales_ID = 0;
//			}
//
//			if (preOrder_ID == null) {
//				preOrder_ID = 0;
//			}

			if (value == null) {

				if (bpartnerId > 0) {
					PelangganSearch.setValue(bpartnerId);
				}

				if (name.equals(MProduct.COLUMNNAME_M_Product_ID)) {
					IMEIList.removeAllItems();
				}

				if (name.equals("C_Decoris_PreSales_ID")
						|| name.equals("C_Decoris_PreOrder_ID")) {
					salesTable.clear();
					salesTable.clearTable();
					salesTable.removeAllItems();

					salesSearch.setValue(null);
					DaftarHargaSearch.setValue(null);
					GudangSearch.setValue(null);
					tanggalSearch.setValue(null);
					PickUp.setChecked(false);
					PickUp.setEnabled(false);
					PelangganSearch.setValue(null);
					pelangganBaru.setEnabled(false);
					Lokasi.setValue(null);
					leasingProviderSearch.setValue(null);
					bankAccountSearch.setValue(null);
					deleteItem.setEnabled(false);
					orgSearch.setReadWrite(false);

					// pembayaran
					paymentTunai.setValue("0.00");
					paymentBank.setValue("0.00");
					paymentLeasing.setValue("0.00");
					paymentHutang.setValue("0.00");

					paymentTunai.setReadonly(false);
					paymentBank.setReadonly(false);
					paymentLeasing.setReadonly(false);
					paymentHutang.setReadonly(false);

					totalBayarTextBox.setText(format.format(0));
					totalBayarTextBox.setText(format.format(0));
					totalBrutoTextBox.setText(format.format(0));
					TotalHeaderTextBox.setText(format.format(0));
					kembaliTextBox.setText(format.format(0));
					totalBayarTextBox.setText(format.format(0));
					diskonTextBox.setText(format.format(0));
					totalBelanjaTextBox.setText(format.format(0));
					totalBayarTunaiTextBox.setText(format.format(0));
					kembalianTextBox.setText(format.format(0));
					DPPTextBox.setText(format.format(0));
					PPNTextBox.setText(format.format(0));

//					if (name.equals("C_Decoris_PreOrder_ID")) {
//						noPresales.setValue(null);
//						noPresales.setReadWrite(false);
//					} else if (name.equals("C_Decoris_PreSales_ID")) {
//
//						noPreOrder.setValue(null);
//						noPreOrder.setReadWrite(false);
//
//					}

				}

				else {

					try {
						WSearchEditor.createBPartner(form.getWindowNo());
						WSearchEditor.createProduct(form.getWindowNo());
					} catch (Exception e) {
						log.log(Level.SEVERE, this.getClass()
								.getCanonicalName() + ".valueChange ERROR: ", e);
					}
				}

				return;
			}
			// BPartner
			if (name.equals("C_BPartner_ID")) {

				PelangganSearch.setValue(value);
				bpartnerId = ((Integer) value).intValue();
				if ((int) PelangganSearch.getValue() > 0) {
					int BPartner_ID = (int) PelangganSearch.getValue();
					MBPartner bp = new MBPartner(ctx, BPartner_ID, null);
					if (bp.getC_PaymentTerm_ID() > 0) {

						C_PaymentTerm_ID = bp.getC_PaymentTerm_ID();

					} else {
						String sqlterm = "SELECT C_PaymentTerm_ID FROM C_PaymentTerm WHERE AD_Client_ID = ? AND IsDefault = 'Y' ";
						C_PaymentTerm_ID = DB.getSQLValueEx(ctx.toString(),
								sqlterm.toString(),
								new Object[] { Env.getAD_Client_ID(ctx) });

					}
				} else {
					String sqlterm = "SELECT C_PaymentTerm_ID FROM C_PaymentTerm WHERE AD_Client_ID = ? AND IsDefault = 'Y' ";
					C_PaymentTerm_ID = DB.getSQLValueEx(ctx.toString(),
							sqlterm.toString(),
							new Object[] { Env.getAD_Client_ID(ctx) });
				}

				String sPriceList = DaftarHargaSearch.getText();
				if (sPriceList != null && sPriceList != ""
						&& Lokasi.getText() != null && Lokasi.getText() != "") {

					ProductSearch.setReadWrite(true);

				} else {
					ProductSearch.setReadWrite(false);

				}

			} else if (name.equals("M_Product_ID")) {

				ProductSearch.setValue(value);
				productID = ((Integer) value).intValue();

				if (productID != null) {
					Integer M_LocatorPOS_ID = getIDFromComboBox(Lokasi,
							MLocator.Table_Name, MLocator.COLUMNNAME_Value);
					MLocator loc = new MLocator(ctx, M_LocatorPOS_ID, null);

					String sqlLocator = "SELECT M_Locator_ID FROM M_StorageOnHand WHERE AD_Client_ID = ? AND M_Product_ID = ? AND M_Locator_ID = ?";
					int M_LocatorProd_ID = DB.getSQLValueEx(ctx.toString(),
							sqlLocator, new Object[] { AD_Client_ID, productID,
									M_LocatorPOS_ID });
					M_Pricelist_ID = getIDFromComboBox(DaftarHargaSearch,
							MPriceList.Table_Name, MPriceList.COLUMNNAME_Name);
					MProduct prodPOS = new MProduct(ctx, productID, null);

					if (!prodPOS.getProductType().equals("E")
							&& M_LocatorProd_ID != M_LocatorPOS_ID) {

						if (PickUp.isSelected()) {
							FDialog.warn(form.getWindowNo(), null, "",
									"Produk yang anda pilih tidak ada di lokasi "
											+ loc.getValue(), "Peringatan");
							return;
						}
					}

					if (prodPOS.getM_AttributeSet_ID() > 0) {

						// TODO
						try {

							Grid inputGrid = GridFactory.newGridLayout();
							Panel paraPanel = new Panel();
							Rows rows = null;
							Row row = null;

							final Decimalbox Qty = new Decimalbox();

							String title = "Korfirmasi Qty IMEI";
							Label LabelQty = new Label("Qty Ordered");

							// Date
							Calendar cal = Calendar.getInstance();
							cal.setTime(new Date());
							cal.set(Calendar.HOUR_OF_DAY, 0);
							cal.set(Calendar.MINUTE, 0);
							cal.set(Calendar.SECOND, 0);
							cal.set(Calendar.MILLISECOND, 0);

							final Window w = new Window();
							w.setTitle(title);

							Borderlayout mainbBorder = new Borderlayout();
							w.appendChild(mainbBorder);
							w.setWidth("250px");
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
////							inputGrid.setHflex("min");

							rows = inputGrid.newRows();

							row = rows.newRow();
							row.appendCellChild(LabelQty.rightAlign());
							row.appendCellChild(Qty);
							//Qty.setHflex("true");

							Vbox vbox = new Vbox();
							w.appendChild(vbox);
							vbox.appendChild(new Separator());
							final ConfirmPanel panel = new ConfirmPanel(true,
									false, false, false, false, false, false);
							vbox.appendChild(panel);
							panel.addActionListener(Events.ON_CLICK,
									new EventListener<Event>() {

										@Override
										public void onEvent(Event event)
												throws Exception {
											if (event.getTarget() == panel
													.getButton(ConfirmPanel.A_CANCEL)) {
												FDialog.info(windowNo, null,
														"",
														"Tambah Produk Dibatalkan");
												w.dispose();
											} else if (event.getTarget() == panel
													.getButton(ConfirmPanel.A_OK)) {
												Integer M_LocatorPOSin_ID = getIDFromComboBox(
														Lokasi,
														MLocator.Table_Name,
														MLocator.COLUMNNAME_Value);
												Vector<Vector<Object>> data = getProductIMEIData(
														productID,
														M_Pricelist_ID,
														bpartnerId,
														M_LocatorPOSin_ID, 0,
														salesTable,
														Qty.getValue());
												Vector<String> columnNames = getOISColumnNames();

												salesTable.clear();

												// Set Model
												ListModelTable modelP = new ListModelTable(
														data);
												modelP.addTableModelListener(WSemeruPOS.this);
												salesTable.setData(modelP,
														columnNames);
												configureMiniTable(salesTable);

												ArrayList<KeyNamePair> list = loadImei(
														productID,
														M_LocatorPOSin_ID);
												IMEIList.removeAllItems();
												for (KeyNamePair imei : list)
													IMEIList.appendItem(imei
															.getName());
												IMEIList.setSelectedIndex(0);

												reCalculate(salesTable);
												calculate();

											}

											w.onClose();

										}

									});

							w.addEventListener(DialogEvents.ON_WINDOW_CLOSE,
									new EventListener<Event>() {

										@Override
										public void onEvent(Event event)
												throws Exception {
											w.dispose();
										}
									});
							w.doHighlighted();

						} catch (Exception err) {

							log.log(Level.SEVERE,
									this.getClass().getCanonicalName()
											+ ".valueChange - ERROR: "
											+ err.getMessage(), err);
						}

					} else {

						Vector<Vector<Object>> data = getProductData(productID,
								M_Pricelist_ID, bpartnerId, M_LocatorPOS_ID, 0,
								salesTable);
						Vector<String> columnNames = getOISColumnNames();

						salesTable.clear();

						// Set Model
						ListModelTable modelP = new ListModelTable(data);
						modelP.addTableModelListener(this);
						salesTable.setData(modelP, columnNames);
						configureMiniTable(salesTable);

						ArrayList<KeyNamePair> list = loadImei(productID,
								M_LocatorPOS_ID);
						IMEIList.removeAllItems();
						for (KeyNamePair imei : list)
							IMEIList.appendItem(imei.getName());
						IMEIList.setSelectedIndex(0);

						reCalculate(salesTable);
						calculate();

					}

				} else {
					Integer M_LocatorPOS_ID = getIDFromComboBox(Lokasi,
							MLocator.Table_Name, MLocator.COLUMNNAME_Value);
					ArrayList<KeyNamePair> list = loadImei(productID,
							M_LocatorPOS_ID);
					IMEIList.removeAllItems();
					for (KeyNamePair imei : list)
						IMEIList.appendItem(imei.getName());
					IMEIList.setSelectedIndex(0);

				}
			} else if (name.equals("C_BPartner_ID")) {
				String sPriceList = DaftarHargaSearch.getText();
				if (sPriceList != null && sPriceList != ""
						&& Lokasi.getValue() != null) {

					ProductSearch.setReadWrite(true);

				} else {
					ProductSearch.setReadWrite(false);

				}

			} else if (name.equals("AD_Org_ID")) {

				// Warehouse
				Integer Org_ID = (Integer) orgSearch.getValue();
				if (Org_ID == null) {
					Org_ID = 0;
				}

				ArrayList<KeyNamePair> listWH = loadWarehouse(Org_ID);
				Integer warehouse_ID = 0;
				GudangSearch.removeAllItems();
				for (KeyNamePair priceList : listWH) {
					GudangSearch.appendItem(priceList.getName());
				}
				GudangSearch.setSelectedIndex(0);
				Lokasi.setEnabled(true);

				if (listWH.size() > 0) {
					warehouse_ID = getIDFromComboBox(GudangSearch,
							MWarehouse.Table_Name, MWarehouse.COLUMNNAME_Name);
				}
				

				ArrayList<KeyNamePair> listLoc = loadLocator(warehouse_ID,
						Org_ID);
				Lokasi.removeAllItems();
				for (KeyNamePair loc : listLoc)
					Lokasi.appendItem(loc.getName());

				if (listLoc.size() > 0) {
					Lokasi.setSelectedIndex(0);
				}

			} else if (name.equals("C_DecorisPOS_ID")) {

				boolean IsValidNotaOld = cekAllowOldNota(AD_Client_ID);

				int C_DecorisPOS_ID = ((Integer) value).intValue();
				X_SM_SemeruPOS decp = new X_SM_SemeruPOS(ctx, C_DecorisPOS_ID, null);

				if (!IsValidNotaOld) {

					if (decp != null) {

						Vector<Vector<Object>> data = getOrderOldNota(C_DecorisPOS_ID);
						Vector<String> columnNames = getOISColumnNamesExistingPresales();

						salesTable.clear();

						// Set Model
						ListModelTable modelP = new ListModelTable(data);
						modelP.addTableModelListener(this);
						salesTable.setData(modelP, columnNames);
						configureMiniTableExistingPresales(salesTable);

						reCalculate(salesTable);
						calculate();

					}

					if (decp.getM_PriceList_ID() > 0) {
						MPriceList PL = new MPriceList(ctx,
								decp.getM_PriceList_ID(), null);
						ArrayList<KeyNamePair> listPL = new ArrayList<KeyNamePair>();
						listPL.add(new KeyNamePair(PL.getM_PriceList_ID(), PL.getName()));
						DaftarHargaSearch.removeAllItems();
						for (KeyNamePair priceL : listPL) {
							DaftarHargaSearch.appendItem(priceL.getName());
						}

						if (listPL.size() > 0) {
							DaftarHargaSearch.setSelectedIndex(0);
						}

					}

					if (decp.get_ValueAsInt("M_Warehouse_ID") > 0) {
						MWarehouse WH = new MWarehouse(ctx,
								decp.getM_Warehouse_ID(), null);
						ArrayList<KeyNamePair> listWH = new ArrayList<KeyNamePair>();
						listWH.add(new KeyNamePair(WH.getM_Warehouse_ID(), WH
								.getName()));
						GudangSearch.removeAllItems();
						for (KeyNamePair imei : listWH)
							GudangSearch.appendItem(imei.getName());
						GudangSearch.setSelectedIndex(0);

						if (listWH.size() > 0) {
							GudangSearch.setSelectedIndex(0);
						}

						ArrayList<KeyNamePair> listLoc = loadLocator(
								decp.getM_Warehouse_ID(), decp.getAD_Org_ID());
						Lokasi.removeAllItems();
						for (KeyNamePair loc : listLoc)
							Lokasi.appendItem(loc.getName());

						if (listLoc.size() > 0) {
							Lokasi.setSelectedIndex(0);
						}

					}

					for (int i = 0; i < salesTable.getRowCount(); i++) {
						salesTable.setValueAt(false, i, 0);
					}

					salesTable.setColumnReadOnly(0, false);
					salesTable.setColumnReadOnly(2, false);
					salesTable.setColumnReadOnly(4, false);

					TokoSearch.setValue(decp.getAD_Client_ID());
					IMEIList.setEnabled(true);
					// noPresales.setValue(decp.getC_Decoris_PreSales_ID());
					salesSearch.setValue(decp.getSalesRep_ID());
					salesSearch.setReadWrite(true);
					orgSearch.setValue(decp.getAD_Org_ID());
					//DaftarHargaSearch.setReadonly(true);
					//DaftarHargaSearch.setEnabled(false);
					//GudangSearch.setReadonly(false);
					GudangSearch.setEnabled(true);
					ProductSearch.setReadWrite(true);
					tanggalSearch.setValue(decp.getDateOrdered());
					tanggalSearch.setReadonly(false);
					PickUp.setChecked(true);
					PickUp.setEnabled(true);
					PelangganSearch.setValue(decp.getC_BPartner_ID());
					PelangganSearch.setReadWrite(true);
					pelangganBaru.setEnabled(false);
					printThermalButton.setEnabled(false);
					printButton.setEnabled(false);
					keteranganTextBox.setReadonly(false);
					//noPreOrder.setReadWrite(false);
					Lokasi.setReadonly(false);
					Lokasi.setEnabled(true);
					deleteItem.setEnabled(true);
					orgSearch.setReadWrite(false);
					processButton.setEnabled(true);

					paymentTunai.setReadonly(false);
					paymentBank.setReadonly(false);
					paymentLeasing.setReadonly(false);
					paymentHutang.setReadonly(false);
					leasingProviderSearch.setReadWrite(false);
					bankAccountSearch.setReadWrite(false);

					//noPresales.setReadWrite(false);
					salesTable.setEnabled(true);

				} else {

					Integer ExistRMA = cekRMAExist(AD_Client_ID,
							C_DecorisPOS_ID);

					if (ExistRMA > 0) {
						MRMA rma = new MRMA(ctx, ExistRMA, null);
						FDialog.info(windowNo, null, "",
								"Nota Sudah Pernah Direturn dengan No RMA "
										+ rma.getDocumentNo(), "Info");
						return;

					} else {

						if (decp != null) {

							Vector<Vector<Object>> data = getOrderOldNota(C_DecorisPOS_ID);
							Vector<String> columnNames = getOISColumnNamesExistingPresales();

							salesTable.clear();

							// Set Model
							ListModelTable modelP = new ListModelTable(data);
							modelP.addTableModelListener(this);
							salesTable.setData(modelP, columnNames);
							configureMiniTableExistingPresales(salesTable);

							reCalculate(salesTable);
							calculate();

						}

						if (decp.getM_PriceList_ID() > 0) {
							MPriceList PL = new MPriceList(ctx,
									decp.getM_PriceList_ID(), null);
							ArrayList<KeyNamePair> listPL = new ArrayList<KeyNamePair>();
							listPL.add(new KeyNamePair(PL.getM_PriceList_ID(),
									PL.getName()));
							DaftarHargaSearch.removeAllItems();
							for (KeyNamePair priceL : listPL) {
								DaftarHargaSearch.appendItem(priceL.getName());
							}

							if (listPL.size() > 0) {
								DaftarHargaSearch.setSelectedIndex(0);
							}

						}

						if (decp.get_ValueAsInt("M_Warehouse_ID") > 0) {
							MWarehouse WH = new MWarehouse(ctx,
									decp.getM_Warehouse_ID(), null);
							ArrayList<KeyNamePair> listWH = new ArrayList<KeyNamePair>();
							listWH.add(new KeyNamePair(WH.getM_Warehouse_ID(),
									WH.getName()));
							GudangSearch.removeAllItems();
							for (KeyNamePair imei : listWH)
								GudangSearch.appendItem(imei.getName());
							GudangSearch.setSelectedIndex(0);

							if (listWH.size() > 0) {
								GudangSearch.setSelectedIndex(0);
							}

						}

						for (int i = 0; i < salesTable.getRowCount(); i++) {
							salesTable.setValueAt(false, i, 0);
						}

						salesTable.setColumnReadOnly(0, true);
						salesTable.setColumnReadOnly(2, true);
						salesTable.setColumnReadOnly(4, true);

						TokoSearch.setValue(decp.getAD_Client_ID());
						IMEIList.setEnabled(false);
						// noPresales.setValue(decp.getC_Decoris_PreSales_ID());
						salesSearch.setValue(decp.getSalesRep_ID());
						salesSearch.setReadWrite(false);
						orgSearch.setValue(decp.getAD_Org_ID());
						//DaftarHargaSearch.setReadonly(true);
						//DaftarHargaSearch.setEnabled(false);
					//	GudangSearch.setReadonly(true);
					//
					//	GudangSearch.setEnabled(false);
						ProductSearch.setReadWrite(false);
						tanggalSearch.setValue(decp.getDateOrdered());
						tanggalSearch.setReadonly(true);
						PickUp.setChecked(true);
						PickUp.setEnabled(true);
						PelangganSearch.setValue(decp.getC_BPartner_ID());
						PelangganSearch.setReadWrite(false);
						pelangganBaru.setEnabled(false);
						printThermalButton.setEnabled(false);
						printButton.setEnabled(false);
						keteranganTextBox.setReadonly(false);

						Lokasi.setReadonly(true);
						Lokasi.setEnabled(false);
						deleteItem.setEnabled(false);
						orgSearch.setReadWrite(false);
						processButton.setEnabled(true);

						paymentTunai.setReadonly(false);
						paymentBank.setReadonly(false);
						paymentLeasing.setReadonly(false);
						paymentHutang.setReadonly(false);
						leasingProviderSearch.setReadWrite(false);
						bankAccountSearch.setReadWrite(false);

						//noPresales.setReadWrite(false);
						salesTable.setEnabled(false);

					}

				}
			}
		} catch (Exception e) {
			log.log(Level.SEVERE, this.getClass().getCanonicalName()
					+ ".valueChange - ERROR: " + e.getMessage(), e);
		}
	}

	@Override
	public void onEvent(Event e) throws Exception {

		log.config("");
		if (e.getTarget().equals(IMEIList)) {
			imei();
		} else if (e.getTarget().equals(deleteItem)) {
			delete();
		} else if (e.getTarget().equals(IsDebit)) {

			boolean debitCheck = IsDebit.isChecked();

			boolean isAllow = false;
			StringBuilder SQLCekChargeAllow = new StringBuilder();
			SQLCekChargeAllow.append("SELECT Description::Numeric ");
			SQLCekChargeAllow.append(" FROM AD_Param");
			SQLCekChargeAllow.append(" WHERE AD_Client_ID = " + AD_Client_ID);
			SQLCekChargeAllow.append(" AND Value = '" + "ChargeSOBankAllowed"
					+ "'");

			int allow = DB.getSQLValueEx(null, SQLCekChargeAllow.toString());

			if (allow == 1) {
				isAllow = true;
			}

			if (isAllow) {

				if (debitCheck) {

					if ((Integer) bankAccountSearch.getValue() == null) {
						FDialog.info(
								windowNo,
								null,
								"",
								"Bank Account Belum Ditentukan, Silahkan Pilih Akun Bank Terlebih Dahulu",
								"Info");
						IsDebit.setChecked(false);
						return;
					}

					if ((Integer) bankAccountSearch.getValue() <= 0) {

						FDialog.info(
								windowNo,
								null,
								"",
								"Bank Account Belum Ditentukan, Silahkan Pilih Akun Bank Terlebih Dahulu",
								"Info");
						IsDebit.setChecked(false);
						return;
					} else {
						boolean isExist = false;
						int bankAcct_ID = (int) bankAccountSearch.getValue();
						MBankAccount bank = new MBankAccount(ctx, bankAcct_ID,
								null);

						int prod_ID = bank.get_ValueAsInt("M_Product_ID");
						BigDecimal chPercent = (BigDecimal) bank
								.get_Value("ChargePercent");

						for (int i = 0; i < salesTable.getRowCount(); i++) {

							KeyNamePair prod = (KeyNamePair) salesTable
									.getValueAt(i, 1);
							if (prod.getKey() == prod_ID) {

								isExist = true;
							}

						}

						if (!isExist) {

							if (prod_ID <= 0
									|| chPercent.compareTo(Env.ZERO) < 0) {

								FDialog.info(
										windowNo,
										null,
										"",
										"Bank Account Yang Dipilih Belum Tersetup Untuk Debit Card",
										"Info");
								IsDebit.setChecked(false);
								return;

							} else {

								BigDecimal bankAmt = paymentBank.getValue();

								BigDecimal priceDebit = bankAmt.multiply(
										chPercent).multiply((Env.ONE).negate());

								Vector<Vector<Object>> data = getProductDataDebit(
										prod_ID, M_Pricelist_ID, bpartnerId, 0,
										0, salesTable, priceDebit);
								Vector<String> columnNames = getOISColumnNames();

								salesTable.clear();

								// Set Model
								ListModelTable modelP = new ListModelTable(data);
								modelP.addTableModelListener(this);
								salesTable.setData(modelP, columnNames);
								configureMiniTable(salesTable);

								paymentBank.setValue(bankAmt.add(priceDebit));

								reCalculate(salesTable);
								calculate();

								calculatePaymentrule(nilaiGrandTotal, false,
										true, false, false);

							}
						}
					}
				}

			}
		} else if (e.getTarget().equals(payruleBoxTunai)) {
			calculatePaymentrule(nilaiGrandTotal, true, false, false, false);
		} else if (e.getTarget().equals(payruleBoxBank)) {
			calculatePaymentrule(nilaiGrandTotal, false, true, false, false);
			if (paymentBank.getValue().compareTo(Env.ZERO) > 0) {
				bankAccountSearch.setReadWrite(true);
				IsDebit.setEnabled(true);
			}
		} else if (e.getTarget().equals(payruleBoxLeasing)) {
			calculatePaymentrule(nilaiGrandTotal, false, false, true, false);
			if (paymentLeasing.getValue().compareTo(Env.ZERO) > 0) {
				leasingProviderSearch.setReadWrite(true);
			}
		} else if (e.getTarget().equals(payruleBoxHutang)) {
			calculatePaymentrule(nilaiGrandTotal, false, false, false, true);
		} else if (e.getTarget().equals(processButton)) {

			FDialog.ask(windowNo, null, "Konfirmasi", "",
					"Anda Yakin Untuk Memproses Transaksi?",
					new Callback<Boolean>() {

						@Override
						public void onCallback(Boolean result) {

							if (result) {

								BigDecimal NilaiHutang = paymentHutang.getValue();
								if (NilaiHutang.compareTo(Env.ZERO) > 0) {

									StringBuilder SQLValidationSODebt = new StringBuilder();
									SQLValidationSODebt.append("SELECT description::numeric ");
									SQLValidationSODebt.append(" FROM ad_param");
									SQLValidationSODebt.append(" WHERE value = 'ValidateSODebtAllowed'");
									SQLValidationSODebt.append(" AND isactive = 'Y'");
									SQLValidationSODebt.append(" AND AD_Client_ID = "+ AD_Client_ID);

									Integer IsAllowDept = DB.getSQLValueEx(
											null,
											SQLValidationSODebt.toString());

									if (IsAllowDept > 0) {

										StringBuilder SQLDefaultPaymentTerm = new StringBuilder();
										SQLDefaultPaymentTerm.append("SELECT COUNT(ad_param_id) ");
										SQLDefaultPaymentTerm.append(" FROM ad_param ");
										SQLDefaultPaymentTerm.append(" WHERE value = 'DefaultPymTermSODebt' ");
										SQLDefaultPaymentTerm.append(" AND isactive = 'Y' ");
										SQLDefaultPaymentTerm.append(" AND AD_Client_ID = "+ AD_Client_ID);
										SQLDefaultPaymentTerm.append(" AND AD_Org_ID = "+ (Integer) orgSearch.getValue());

										Integer cekDefTerm = DB.getSQLValueEx(null, SQLDefaultPaymentTerm.toString());

										if (cekDefTerm <= 0) {

											FDialog.warn(windowNo,null,"","Default PaymenTerm Belum Di Setup","Peringatan");
											return;

										}

										Integer bp_id = (Integer) PelangganSearch.getValue();

										StringBuilder SQLGetOpenBalance = new StringBuilder();
										SQLGetOpenBalance.append("SELECT totalopenbalance + "+ NilaiHutang);
										SQLGetOpenBalance.append(" FROM c_bpartner ");
										SQLGetOpenBalance.append(" WHERE c_bpartner_id = "+ bp_id);
										SQLGetOpenBalance.append(" AND isactive = 'Y' ");
										SQLGetOpenBalance.append(" AND AD_Client_ID = "+ AD_Client_ID);

										BigDecimal OpenBalance = DB.getSQLValueBDEx(null,SQLGetOpenBalance.toString());
										MBPartner bpart = new MBPartner(ctx,bp_id, null);
										BigDecimal crLimit = bpart.getSO_CreditLimit();

										if (OpenBalance.compareTo(crLimit) > 0) {
											FDialog.warn(windowNo,null,"",
													"Penjualan Tidak Dapat Diproses Karena Batas Hutang Pelanggan Sudah Melebihi Credit Limit",
													"Peringatan");
											return;
										}

										StringBuilder SQLDueDate = new StringBuilder();
										SQLDueDate.append("SELECT duedate ");
										SQLDueDate.append(" FROM decoris_duedate_cust_v ");
										SQLDueDate.append(" WHERE c_bpartner_id = "+ bp_id);
										SQLDueDate.append(" AND AD_Client_ID = "+ AD_Client_ID);

										Date ordered = (Date) tanggalSearch.getValue();
										Date Due = (Date) DB.getSQLValueTS(null, SQLDueDate.toString());

										if (Due != null) {

											if (ordered.compareTo(Due) > 0) {

												FDialog.ask(windowNo,
														null,
														"Konfirmasi",
														"",
														"Penjualan Tidak Dapat DiProses karena Pelanggan masih memiliki Piutang yang belum Lunas. Apakah Anda ingin melanjutkan Proses Penjualan? ",
														new Callback<Boolean>() {

															@Override
															public void onCallback(
																	Boolean result) {

																if (result) {

																	try {

																		Grid inputGrid = GridFactory
																				.newGridLayout();
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
																		w.setPage(form.getPage());
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
																		//inputGrid.setHflex("min");

																		rows = inputGrid.newRows();

																		row = rows.newRow();
																		row.appendCellChild(LabelUseName.rightAlign());
																		row.appendCellChild(userName,1);
																		//userName.setHflex("true");

																		row = rows.newRow();
																		row.appendCellChild(LabelPass.rightAlign());
																		row.appendCellChild(password,1);
																		//password.setHflex("true");
																		password.setType("password");

																		Vbox vbox = new Vbox();
																		w.appendChild(vbox);
																		vbox.appendChild(new Separator());
																		final ConfirmPanel panel = new ConfirmPanel(
																				true,
																				false,
																				false,
																				false,
																				false,
																				false,
																				false);
																		vbox.appendChild(panel);
																		panel.addActionListener(
																				Events.ON_CLICK,
																				new EventListener<Event>() {

																					@Override
																					public void onEvent(
																							Event event)
																							throws Exception {
																						if (event.getTarget() == panel.getButton(ConfirmPanel.A_CANCEL)) {
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
																							SQLCek.append(" WHERE AD_Client_ID = "+ AD_Client_ID);
																							SQLCek.append(" AND Value ='"+ SOProcesSuperVisor+ "')");
																							SQLCek.append(" AND Name = '"+ uName+ "'");
																							SQLCek.append(" AND Password = '"+ passwd+ "'");

																							int cek = DB.getSQLValueEx(null,SQLCek.toString());

																							if (cek > 0) {

																								StringBuilder SQLGetSpv = new StringBuilder();
																								SQLGetSpv.append(" SELECT description::NUMERIC");
																								SQLGetSpv.append(" FROM AD_Param");
																								SQLGetSpv.append(" WHERE AD_Client_ID = "+ AD_Client_ID);
																								SQLGetSpv.append(" AND Value ='"+ SOProcesSuperVisor+ "'");
																								int supervisor_id = DB.getSQLValueEx(null,SQLGetSpv.toString());

																								final Integer SuperVisor_ID = supervisor_id;

																								Calendar cal = Calendar.getInstance();
																								cal.setTime(Env.getContextAsDate(ctx,"#Date"));
																								cal.set(Calendar.HOUR_OF_DAY,0);
																								cal.set(Calendar.MINUTE,0);
																								cal.set(Calendar.SECOND,0);
																								cal.set(Calendar.MILLISECOND,0);
																								final Timestamp DateApprove = new Timestamp(cal.getTimeInMillis());

																								StringBuilder SQLGetTerm = new StringBuilder();
																								SQLGetTerm.append(" SELECT description::numeric");
																								SQLGetTerm.append(" FROM ad_param");
																								SQLGetTerm.append(" WHERE AD_Client_ID = " + AD_Client_ID);
																								SQLGetTerm.append(" AND value = 'DefaultPymTermSODebt'");
																								SQLGetTerm.append(" AND isactive = 'Y'");
																								SQLGetTerm.append(" AND AD_Org_ID = "+ (Integer) orgSearch.getValue());

																								final Integer C_PayTerm_ID = DB.getSQLValueEx(null,SQLGetTerm.toString());

																								ValidationPriceLimit(true,SuperVisor_ID,DateApprove,C_PayTerm_ID);

																							} else {

																								FDialog.info(
																										windowNo,
																										null,
																										"",
																										"UserName atau Password Tidak Terdaftar, Silahkan Entri Ulang Otorisasi",
																										"Info");
																								return;

																							}

																						}

																						w.onClose();

																					}

																				});

																		w.addEventListener(
																				DialogEvents.ON_WINDOW_CLOSE,
																				new EventListener<Event>() {

																					@Override
																					public void onEvent(
																							Event event)
																							throws Exception {
																						w.dispose();
																					}
																				});
																		w.doHighlighted();

																	} catch (Exception err) {

																		log.log(Level.SEVERE,
																				this.getClass()
																						.getCanonicalName()
																						+ ".valueChange - ERROR: "
																						+ err.getMessage(),
																				err);
																	}

																} else {
																	// When
																	// Confirmation
																	// SO is No
																	return;
																}

															}
														});

											} else {

												final Integer C_PayTerm_ID = getPaymentTerm(AD_Client_ID);
												ValidationPriceLimit(true, 0, null,C_PayTerm_ID);
												

											}
										} else {

											final Integer C_PayTerm_ID = getPaymentTerm(AD_Client_ID);
											ValidationPriceLimit(true, 0, null, C_PayTerm_ID);

										}
									} else {
										ValidationPriceLimit(false, 0, null, 0);

									}
									// End Validation Pembayaran Hutang

								} else {
									// Not Hutang
									ValidationPriceLimit(false, 0, null, 0);

								}

							} else {
								return;
							}

						}
					});

		} else if (e.getTarget().equals(buatBaru)) {
			newOrder();
		} else if ((e.getTarget().equals(pelangganBaru))) {
			addBP();
		} else if (e.getTarget().equals(DaftarHargaSearch)) {
			priceList();
		} else if (e.getTarget().equals(printButton)) {
			updateData(C_DecorisPOSPrint_ID, false);
			printNota();
		} else if (e.getTarget().equals(printThermalButton)) {
			updateData(C_DecorisPOSPrint_ID, false);
			printThermal();
		} else if (e.getTarget().equals(printSuratJalan)) {
			updateData(C_DecorisPOSPrint_ID, true);
			printSuratJalan();
		} else if (e.getTarget().equals(DaftarHargaSearch)) {
			int listSize = DaftarHargaSearch.getItemCount();
			if (listSize <= 1)
				return;

			Integer PriceList_ID = getIDFromComboBox(DaftarHargaSearch,
					MPriceList.Table_Name, MPriceList.COLUMNNAME_Name);

			M_Pricelist_ID = PriceList_ID;

		} else if (e.getTarget().equals(GudangSearch)) {

			Lokasi.setReadonly(false);
			Lokasi.setEnabled(true);

			Integer warehouse_ID = getIDFromComboBox(GudangSearch,
					MWarehouse.Table_Name, MWarehouse.COLUMNNAME_Name);
		
			Integer Org_ID = (Integer) orgSearch.getValue();
			if (Org_ID == null) {
				Org_ID = 0;
			}

			ArrayList<KeyNamePair> listLoc = loadLocator(warehouse_ID, Org_ID);
			Lokasi.removeAllItems();
			for (KeyNamePair priceList : listLoc)
				Lokasi.appendItem(priceList.getName());

			if (listLoc.size() > 0) {

				Lokasi.setSelectedIndex(0);

			}

			if (Lokasi.getValue() != null && PelangganSearch.getValue() != null
					&& DaftarHargaSearch.getValue() != null) {

				ProductSearch.setReadWrite(true);

			}

		} else if (e.getTarget().equals(Lokasi)) {

			if (DaftarHargaSearch.getValue() != null
					&& PelangganSearch.getValue() != null) {
				Integer M_LocatorPOS_ID = getIDFromComboBox(Lokasi,
						MLocator.Table_Name, MLocator.COLUMNNAME_Value);
				ProductSearch.setReadWrite(true);
				if (ProductSearch != null) {

					ArrayList<KeyNamePair> list = loadImei(productID,
							M_LocatorPOS_ID);
					IMEIList.removeAllItems();
					for (KeyNamePair imei : list)
						IMEIList.appendItem(imei.getName());
					IMEIList.setSelectedIndex(0);
				}

			} else {
				ProductSearch.setReadWrite(false);

			}

		} else if (e.getTarget().equals(paymentTunai)
				|| e.getTarget().equals(paymentBank)
				|| e.getTarget().equals(paymentLeasing)
				|| e.getTarget().equals(paymentHutang)) {

			String event = e.getName();

			if (event.equals(Events.ON_FOCUS)) {
				//
				// if (AD_Language.toUpperCase().equals("IN_ID")) {
				// if (e.getTarget().equals(paymentTunai)) {
				// //String sTunai =
				// paymentTunai.getValue().toString().replaceAll("\\.",
				// "").replaceAll(",", ".");
				// //BigDecimal asd = paymentTunai.getValue();
				// //String asda = asd.toString();
				// //Double tTunai = Double.valueOf(asd.toString());
				// //paymentTunai.setFormat("#########");
				// //paymentTunai.setValue(BigDecimal.valueOf(tTunai));
				// } else if (e.getTarget().equals(paymentBank)) {
				// String sBank =
				// paymentBank.getValue().toString().replaceAll("\\.",
				// "").replaceAll(",", ".");
				// Double tBank = Double.valueOf(sBank);
				// paymentBank.setFormat("#########");
				// paymentBank.setValue(BigDecimal.valueOf(tBank));
				// } else if (e.getTarget().equals(paymentLeasing)) {
				// String sLeasing =
				// paymentLeasing.getValue().toString().replaceAll("\\.",
				// "").replaceAll(",", ".");
				// Double tLeasing = Double.valueOf(sLeasing);
				// paymentLeasing.setFormat("#########");
				// paymentLeasing.setValue(BigDecimal.valueOf(tLeasing));
				// } else if (e.getTarget().equals(paymentHutang)) {
				// String sHutang =
				// paymentHutang.getValue().toString().replaceAll("\\.",
				// "").replaceAll(",", ".");
				// Double tHutang = Double.valueOf(sHutang);
				// paymentHutang.setFormat("#########");
				// paymentHutang.setValue(BigDecimal.valueOf(tHutang));
				// }
				// }

			} else if (event.equals(Events.ON_BLUR)) {

				BigDecimal payTunai = paymentTunai.getValue();
				BigDecimal payBank = paymentBank.getValue();
				BigDecimal payLeasing = paymentLeasing.getValue();
				BigDecimal payHutang = paymentHutang.getValue();

				if (payTunai == null || payTunai.toString().equals("0")) {
					paymentTunai.setValue("0.00");
					payTunai = Env.ZERO;
				}

				if (payBank == null || payBank.toString().equals("0")) {
					paymentBank.setValue("0.00");
					payBank = Env.ZERO;
					bankAccountSearch.setValue(null);
					bankAccountSearch.setReadWrite(false);
					IsDebit.setEnabled(false);

				} else if (payBank.compareTo(Env.ZERO) > 0) {
					bankAccountSearch.setReadWrite(true);
					IsDebit.setEnabled(true);
				}

				if (payLeasing == null || payLeasing.toString().equals("0")) {
					paymentLeasing.setValue("0.00");
					payLeasing = Env.ZERO;
					leasingProviderSearch.setValue(null);
					leasingProviderSearch.setReadWrite(false);
				} else if (payLeasing.compareTo(Env.ZERO) > 0) {
					leasingProviderSearch.setReadWrite(true);
				}

				if (payHutang == null || payHutang.toString().equals("0")) {
					paymentHutang.setValue("0.00");
					payHutang = Env.ZERO;
				}

				BigDecimal totalPay = payTunai.add(payBank);
				BigDecimal totalPay2 = payTunai.add(payBank).add(payLeasing)
						.add(payHutang);

				paymentLeasing.setValue(payLeasing);
				paymentHutang.setValue(payHutang);
				paymentTunai.setValue(payTunai);
				paymentBank.setValue(payBank);
				totalBayarTextBox.setText(format.format(totalPay2));
				totalBayarTunaiTextBox.setText(format.format(totalPay));
				paymentTunai.setFormat("#,###,###,##0.00");
				paymentBank.setFormat("#,###,###,##0.00");
				paymentLeasing.setFormat("#,###,###,##0.00");
				paymentHutang.setFormat("#,###,###,##0.00");

				Double tBlanja = 0.00;

				if (totalBelanjaTextBox.getText().isEmpty()) {
					tBlanja = 0.00;
				} else if (!totalBelanjaTextBox.getText().isEmpty()) {
					if (AD_Language.toUpperCase().equals("EN_US")) {
						tBlanja = Double.valueOf(totalBelanjaTextBox.getText()
								.replaceAll(",", ""));
					} else if (AD_Language.toUpperCase().equals("IN_ID")) {
						String dblanja = totalBelanjaTextBox.getText()
								.replaceAll("\\.", "").replaceAll(",", ".");
						tBlanja = Double.valueOf(dblanja);
					}
				}

				BigDecimal totalBelanja = BigDecimal.valueOf(tBlanja);
				boolean isPass = false;
				Decimalbox triger = null;
				if (e.getTarget().equals(paymentTunai)) {
					isPass = paymentAlgoritm(payBank, payLeasing, payHutang,
							payTunai);
					triger = paymentTunai;
				} else if (e.getTarget().equals(paymentBank)) {
					isPass = paymentAlgoritm(payTunai, payLeasing, payHutang,
							payBank);
					triger = paymentBank;
				} else if (e.getTarget().equals(paymentLeasing)) {
					isPass = paymentAlgoritm(payTunai, payBank, payHutang,
							payLeasing);
					triger = paymentLeasing;
				} else if (e.getTarget().equals(paymentHutang)) {
					isPass = paymentAlgoritm(payTunai, payBank, payLeasing,
							payHutang);
					triger = paymentHutang;
				}

				if (totalBelanja.compareTo(Env.ZERO) <= 0) {

					if (!triger.isReadonly()) {
						FDialog.info(
								windowNo,
								null,
								"",
								"Belum Ada Item Untuk Di Bayar, Silakan Input Produk Terlebih Dahulu",
								"Info Pembayaran");
						triger.setValue(Env.ZERO);
						return;
					}
				}

				if (triger == paymentBank || triger == paymentLeasing
						|| triger == paymentHutang) {

					if (triger.getValue().compareTo(totalBelanja) > 0) {

						FDialog.info(
								windowNo,
								null,
								"",
								"Input Amount Tidak Boleh Melebihi Total Belanja",
								"Info Pembayaran");
						triger.setValue(Env.ZERO);
						return;

					}

				}

				if (triger == paymentTunai || triger == paymentBank) {

					if (payLeasing.compareTo(Env.ZERO) > 0
							|| payHutang.compareTo(Env.ZERO) > 0) {
						if (!isPass) {
							FDialog.info(
									windowNo,
									null,
									"",
									"Pembayaran Tidak Boleh Melebihi Total Belanja,Silahkan Cek Kembali",
									"Info Pembayaran");
							BigDecimal trigerPay = triger.getValue();
							if (trigerPay == null) {
								trigerPay = Env.ZERO;
							}

							totalPay = totalPay.subtract(trigerPay);
							totalPay2 = totalPay2.subtract(trigerPay);
							triger.setValue(Env.ZERO);

							totalBayarTunaiTextBox.setText(format
									.format(totalPay));
							totalBayarTextBox.setText(format.format(totalPay2));

							return;
						}
					} else if (payLeasing.compareTo(Env.ZERO) == 0
							|| payHutang.compareTo(Env.ZERO) == 0) {
						BigDecimal kembalian = totalPay.subtract(totalBelanja);
						if (kembalian.compareTo(Env.ONE) < 0) {
							kembalian = Env.ZERO;
						}

						kembalianTextBox.setText(format.format(kembalian));
						kembaliTextBox.setText(format.format(kembalian));

					}

				} else if (triger == paymentHutang || triger == paymentLeasing) {

					if (!isPass) {
						BigDecimal trigerPay = triger.getValue();
						if (trigerPay == null) {
							trigerPay = Env.ZERO;
						}
						totalPay2 = totalPay2.subtract(trigerPay);
						totalBayarTextBox.setText(format.format(totalPay2));

						FDialog.info(
								windowNo,
								null,
								"",
								"Pembayaran Tidak Boleh Melebihi Total Belanja,Silahkan Cek Kembali",
								"Info Pembayaran");
						triger.setValue(Env.ZERO);
						return;
					}

				}

			}

		}
	}

	@Override
	public ADForm getForm() {

		return form;
	}

	@Override
	public void tableChanged(WTableModelEvent e) {
		boolean isUpdate = (e.getType() == WTableModelEvent.CONTENTS_CHANGED);

		String sqlIsForceLimitPrice = "SELECT IsForceLimitPrice FROM C_POS WHERE  AD_Client_ID = ? AND CreatedByPOS_ID = ? AND M_PriceList_ID = ?";
		String forceLimitPrice = DB.getSQLValueStringEx(ctx.toString(),
				sqlIsForceLimitPrice, new Object[] { AD_Client_ID,
						CreatedByPOS_ID, M_Pricelist_ID });

		if (forceLimitPrice == null) {

			forceLimitPrice = "N";
		}

		if (forceLimitPrice.toUpperCase().equals("Y")) {
			isForceLimitPrice = true;
		} else {
			isForceLimitPrice = false;
		}

		// Not a table update
		if (!isUpdate) {
			return;
		} else if (isUpdate) {
			// imeiIndex = e.getFirstRow();
			rowIndex = e.getFirstRow();
			int col = e.getColumn();
			if (col > 0) {

				M_Pricelist_ID = getIDFromComboBox(DaftarHargaSearch,
						MPriceList.Table_Name, MPriceList.COLUMNNAME_Name);
				tableChangeCalculate(rowIndex, col, salesTable,
						form.getWindowNo(), isForceLimitPrice, M_Pricelist_ID);
				calculate();

			}
		}
	}

	public void calculate() {

		final double div = 0.90909091;
		BigDecimal divider = BigDecimal.valueOf(div);
		BigDecimal subTotal = Env.ZERO;

		totalPrice = totalPrices.setScale(2, RoundingMode.HALF_UP);
		nilaiGrandTotal = totalPrice.setScale(2, RoundingMode.HALF_UP);
		totalDiskon = totalDiskons.setScale(2, RoundingMode.HALF_DOWN);
		totalBeforeDiscount = totalBeforeDiscounts.setScale(2,
				RoundingMode.HALF_UP);

		for (int i = 0; i < salesTable.getRowCount(); i++) {
			String pajak = (String) salesTable.getValueAt(i, 9);

			if (pajak.toUpperCase().equals("PPN")) {
				Double dsub = 0.00;
				if (AD_Language.toUpperCase().equals("EN_US")) {
					dsub = Double.valueOf((String) salesTable.getValueAt(i, 6)
							.toString().replaceAll(",", ""));
				} else if (AD_Language.toUpperCase().equals("IN_ID")) {
					String sub = (String) salesTable.getValueAt(i, 6)
							.toString().replaceAll("\\.", "")
							.replaceAll(",", ".");
					dsub = Double.valueOf(sub);
				}
				BigDecimal sub = BigDecimal.valueOf(dsub);
				subTotal = subTotal.add(sub);
			}
		}
		nilaiDpp = (subTotal.multiply(divider)).setScale(2,
				RoundingMode.HALF_DOWN);
		nilaiPajak = (subTotal.subtract(nilaiDpp)).setScale(2,
				RoundingMode.HALF_DOWN);

		DPPTextBox.setText(format.format(nilaiDpp));
		PPNTextBox.setText(format.format(nilaiPajak));
		diskonTextBox.setText(format.format(totalDiskon));
		totalBelanjaTextBox.setText(format.format(nilaiGrandTotal));
		totalBrutoTextBox.setText(format.format(nilaiGrandTotal
				.add(totalDiskon)));
		TotalHeaderTextBox.setText(format.format(nilaiGrandTotal));

	}

	public void lockData() {
		IMEIList.setEnabled(false);
		deleteItem.setEnabled(false);
		PickUp.setEnabled(false);
		kembalianTextBox.setReadonly(true);
		processButton.setEnabled(true);
		tanggalSearch.setEnabled(false);
		orgSearch.setReadWrite(false);
		PelangganSearch.setReadWrite(false);
		salesSearch.setReadWrite(false);
//		GudangSearch.setEnabled(false);
		Lokasi.setEnabled(false);
		ProductSearch.setReadWrite(false);
		leasingProviderSearch.setReadWrite(false);
		bankAccountSearch.setReadWrite(false);
		noNotaOld.setReadWrite(false);
		for (int i = 0; i < salesTable.getRowCount(); i++) {
			salesTable.setValueAt(false, i, 0);
		}
		IsDebit.setEnabled(false);
		salesTable.setColumnReadOnly(0, true);
		salesTable.setColumnReadOnly(2, true);
		salesTable.setColumnReadOnly(4, true);
		salesTable.setEnabled(false);
//		noPresales.setReadWrite(false);
//		noPreOrder.setReadWrite(false);
		DaftarHargaSearch.setEnabled(false);
		paymentTunai.setReadonly(true);
		paymentBank.setReadonly(true);
		paymentLeasing.setReadonly(true);
		paymentHutang.setReadonly(true);
		keteranganTextBox.setReadonly(true);

	}

	public void newOrder() {
		m_docAction = "CO";
		salesTable.setEnabled(true);
		// bankAccountLabel.setVisible(false);
		// bankAccountSearch.setVisible(false);
		noNotaOld.setValue(null);

		orgSearch.setReadWrite(true);
		pelangganBaru.setEnabled(true);
		tanggalSearch.setEnabled(true);
		deleteItem.setEnabled(true);
		IsDebit.setEnabled(false);
		IsDebit.setChecked(false);

		bankAccountSearch.setReadWrite(false);
		bankAccountSearch.setValue(null);
		IMEIList.setEnabled(true);
		IMEIList.removeAllItems();
		deleteItem.setEnabled(true);
		PickUp.setEnabled(true);
		kembalianTextBox.setEnabled(true);
		processButton.setEnabled(true);
		plusButton.setEnabled(true);
		printButton.setEnabled(false);
		printThermalButton.setEnabled(false);
		printSuratJalan.setEnabled(false);
		plusButton2.setVisible(false);
		tanggalSearch.setEnabled(true);
		PelangganSearch.setReadWrite(true);
		salesSearch.setReadWrite(true);
		salesSearch.setValue(null);
		GudangSearch.setEnabled(true);
		Lokasi.setEnabled(true);
		ProductSearch.setReadWrite(true);
		leasingProviderSearch.setReadWrite(false);
		plusButton.setLabel("+");
		noNotaOld.setReadWrite(true);

		DPPTextBox.setText("0.00");
		totalBelanjaTextBox.setText("0.00");
		PPNTextBox.setText("0.00");
		diskonTextBox.setText("0.00");
		totalBelanjaTextBox.setText("0.00");
		totalBrutoTextBox.setText("0.00");
		totalBayarTunaiTextBox.setText("0.00");
		kembalianTextBox.setText("0.00");
		paymentTunai.setValue("0.00");
		paymentBank.setValue("0.00");
		paymentLeasing.setValue("0.00");
		paymentHutang.setValue("0.00");
		totalBayarTextBox.setText("0.00");
		TotalHeaderTextBox.setText("0.00");

		PickUp.setChecked(true);
		ProductSearch.setReadWrite(false);

		nilaiDpp = Env.ZERO;
		totalPrice = Env.ZERO;
		nilaiPajak = Env.ZERO;
		totalDiskon = Env.ZERO;
		totalPrices = Env.ZERO;
		totalDiskons = Env.ZERO;
		nilaiGrandTotal = Env.ZERO;
		totalBeforeDiscounts = Env.ZERO;

		keteranganTextBox.setValue(null);
		ProductSearch.setValue(null);
		PelangganSearch.setValue(null);
		leasingProviderSearch.setValue(null);
		IMEIList.setValue(null);
		noNota.setText("");
		C_OrderPrint_ID = 0;
		pelangganBaru.setEnabled(true);

		if (salesTable.getRowCount() > 0) {
			for (int i = 0; i < salesTable.getRowCount(); i++) {
				deletedata(0);
			}
			salesTable.clear();
			salesTable.setRowCount(0);
		}

		// Date set to Login Date
		Calendar cal = Calendar.getInstance();
		cal.setTime(Env.getContextAsDate(ctx, "#Date"));
		cal.set(Calendar.HOUR_OF_DAY, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);
		cal.set(Calendar.MILLISECOND, 0);
		tanggalSearch.setValue(new Timestamp(cal.getTimeInMillis()));

		ArrayList<KeyNamePair> listPL = loadPriceList();
		DaftarHargaSearch.removeAllItems();
		for (KeyNamePair priceList : listPL) {
			DaftarHargaSearch.appendItem(priceList.getName());
		}

		GudangSearch.setValue(null);
		Lokasi.setValue(null);
		DaftarHargaSearch.setSelectedIndex(0);
		DaftarHargaSearch.setEnabled(true);
		orgSearch.setReadWrite(true);
		printThermalButton.setEnabled(false);
		printButton.setEnabled(false);
		paymentTunai.setReadonly(false);
		paymentBank.setReadonly(false);
		paymentLeasing.setReadonly(false);
		paymentHutang.setReadonly(false);
		ProductSearch.setReadWrite(false);
//		noPresales.setValue(null);
//		noPreOrder.setValue(null);
		payruleBoxTunai.setEnabled(true);
		payruleBoxBank.setEnabled(true);
		payruleBoxLeasing.setEnabled(true);
		payruleBoxHutang.setEnabled(true);

		salesTable.setEnabled(true);
//		noPresales.setReadWrite(true);
//		noPreOrder.setReadWrite(true);
		DaftarHargaSearch.setEnabled(true);
		keteranganTextBox.setReadonly(false);
		keteranganTextBox.setEnabled(true);
//		noPresales.getComponent().setFocus(true);

	}

	private boolean multiLocatorCheck(IMiniTable miniTable) {

		String locA = "";
		String locN = "";
		boolean isMultiLocator = false;

		if (miniTable.getRowCount() > 1) {
			for (int i = 0; i < miniTable.getRowCount(); i++) {

				if (i == 0) {
					KeyNamePair KL1 = (KeyNamePair) miniTable.getValueAt(i, 7);
					locA = KL1.getID();
				} else {
					KeyNamePair KLn = (KeyNamePair) miniTable.getValueAt(i, 7);
					locN = KLn.getID();
				}

				// cek locator
				if (locA != "" && locN != "") {
					if (!locA.toUpperCase().equals(locN.toUpperCase())) {

						isMultiLocator = true;

						return isMultiLocator;
					}
				}
			}
		}

		return isMultiLocator;
	}

	private String cekAkses() {
		String Msg = "";
		if (CreatedByPOS_ID <= 0) {
			noNota.setReadonly(true);
			tanggalSearch.setEnabled(false);
			orgSearch.setReadWrite(false);
			PelangganSearch.setReadWrite(false);
			salesSearch.setReadWrite(false);
			//GudangSearch.setEnabled(false);
			Lokasi.setEnabled(false);
			ProductSearch.setReadWrite(false);
			IMEIList.setEnabled(false);
			keteranganTextBox.setReadonly(true);

			salesTable.setEnabled(false);
			processButton.setEnabled(false);
			deleteItem.setEnabled(false);
			buatBaru.setEnabled(false);
			PickUp.setEnabled(false);
			plusButton.setEnabled(false);

			FDialog.info(windowNo, null, "","User Tidak Mempunyai Akses ke POS", "Info");
			Msg = "error";
		}

		return Msg;
	}

	private void addBP() {
		Grid inputGrid = GridFactory.newGridLayout();
		Panel paraPanel = new Panel();
		Rows rows = null;
		Row row = null;

		final WDateEditor TTL = new WDateEditor();
		final Textbox BPName = new Textbox();
		final Textbox BPMotherName = new Textbox();
		final Textbox Address1 = new Textbox();
		final Textbox Address2 = new Textbox();
		final Textbox EMail = new Textbox();
		final Decimalbox ZIP = new Decimalbox();
		final Textbox Telepon1 = new Textbox();
		final Textbox Telepon2 = new Textbox();
		final Textbox BPIDCardNumber = new Textbox();
		final Textbox SearchKey = new Textbox();
		final NumberBox CreditLimit = new NumberBox(true);
		final Checkbox isNamekey = new Checkbox();
		final Checkbox isBpIDKey = new Checkbox();

		// Opsional Mandatory
		final String birthday = "POS_AddCust_Birthday";
		final String Addres2 = "POS_AddCust_Address2";
		final String KTPNo = "POS_AddCust_KTPNo";
		final String zipcode = "POS_AddCust_ZIP";
		final String phone2 = "POS_AddCust_Phone2";
		final String email = "POS_AddCust_Email";

		String title = "Tambah Pelanggan";
		Label LabelCity = new Label("Kota(*)");
		Label LabelBPName = new Label("Nama(*)");
		Label LabelBPMotherName = new Label("Nama Ibu(*)");
		Label LabelBPIDCardNumber = new Label("No KTP");
		Label LabelTTL = new Label("Tanggal Lahir(*)");
		Label LabelAddress1 = new Label("Alamat1(*)");
		Label LabelAddress2 = new Label("Alamat2");
		Label LabelZIP = new Label("ZIP");
		Label LabelTelepon1 = new Label("Telepon1(*)");
		Label LabelTelepon2 = new Label("Telepon2");
		Label LabelEMail = new Label("E-Mail");
		Label LabelCreditLimit = new Label("Batas Kredit");
		Label LabelSearchKey = new Label("Search Key");
		Label Tanda = new Label("Inputan Bertanda (*) Tidak Boleh Kosong");

		// City-7048
		MLookup lookupCity = MLookupFactory.get(ctx, form.getWindowNo(), 0,7048, DisplayType.TableDir);

		final WTableDirEditor cityList = new WTableDirEditor("C_City_ID", true,false, true, lookupCity);
		cityList.addValueChangeListener(this);
		cityList.setMandatory(true);

		// get credit limit from pos terminal
		String getCreditLimit = "SELECT CreditLimit FROM C_POS WHERE AD_Client_ID = ? AND CreatedByPOS_ID = ?";
		BigDecimal creditLimit = DB.getSQLValueBDEx(null, getCreditLimit,new Object[] { AD_Client_ID, CreatedByPOS_ID });

		if (creditLimit == null) {
			creditLimit = Env.ZERO;
		}

		// Date
		Calendar cal = Calendar.getInstance();
		cal.setTime(Env.getContextAsDate(ctx, "#Date"));
		cal.set(Calendar.HOUR_OF_DAY, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);
		cal.set(Calendar.MILLISECOND, 0);
		// TTL.setValue(new Timestamp(cal.getTimeInMillis()));
		TTL.addValueChangeListener(this);

		final Window w = new Window();
		w.setTitle(title);

		Borderlayout mainbBorder = new Borderlayout();
		w.appendChild(mainbBorder);
		w.setWidth("450px");
		w.setHeight("510px");
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
		south.setSize("90%");

		inputGrid.setWidth("100%");
		inputGrid.setStyle("Height:100%;");

		rows = inputGrid.newRows();

		// Search Key
		row = rows.newRow();
		row.appendCellChild(LabelSearchKey.rightAlign());
		row.appendCellChild(SearchKey);
		//SearchKey.setHflex("true");

		// Nama
		row = rows.newRow();
		row.appendCellChild(LabelBPName.rightAlign());
		row.appendCellChild(BPName);
		row.appendCellChild(isNamekey);
		isNamekey.setChecked(true);
		isNamekey.setLabel("Set Search Key");
		isNamekey.addEventListener(Events.ON_CHECK, new EventListener<Event>() {

			@Override
			public void onEvent(Event ev) throws Exception {

				if (!isNamekey.isChecked())
					isNamekey.setChecked(true);
				if (isBpIDKey.isChecked())
					isBpIDKey.setChecked(false);

				if (isNamekey.isChecked()) {
					String setSearchKey = BPName.getValue().toString();
					SearchKey.setValue(setSearchKey);
				}

			}
		});

		//BPName.setHflex("true");
		BPName.addEventListener(Events.ON_BLUR, new EventListener<Event>() {

			@Override
			public void onEvent(Event ev) throws Exception {

				if (ev.getTarget().equals(BPName)) {

					if (isNamekey.isChecked()) {
						String setSearchKey = BPName.getValue().toString();
						SearchKey.setValue(setSearchKey);
					}
				}

			}

		});

		// TTL
		row = rows.newRow();
		row.appendCellChild(LabelTTL.rightAlign());
		row.appendCellChild(TTL.getComponent(), 1);
		//TTL.getComponent().setHflex("true");

		// Nama Ibu
		row = rows.newRow();
		row.appendCellChild(LabelBPMotherName.rightAlign());
		row.appendCellChild(BPMotherName);
		//BPMotherName.setHflex("true");

		// No KTP
		row = rows.newRow();
		row.appendCellChild(LabelBPIDCardNumber.rightAlign());
		row.appendCellChild(BPIDCardNumber);
		row.appendCellChild(isBpIDKey);
		isBpIDKey.setChecked(false);
		isBpIDKey.setLabel("Set Search Key");
		isBpIDKey.addActionListener(this);
		isBpIDKey.addEventListener(Events.ON_CHECK, new EventListener<Event>() {

			@Override
			public void onEvent(Event ev) throws Exception {

				if (!isBpIDKey.isChecked())
					isBpIDKey.setChecked(true);
				if (isNamekey.isChecked())
					isNamekey.setChecked(false);

				if (isBpIDKey.isChecked()) {
					SearchKey.setValue(BPIDCardNumber.getValue().toString());
				}
			}
		});

		//BPIDCardNumber.setHflex("true");
		BPIDCardNumber.addEventListener(Events.ON_BLUR,
				new EventListener<Event>() {

					@Override
					public void onEvent(Event ev) throws Exception {

						if (ev.getTarget().equals(BPIDCardNumber)) {

							if (isBpIDKey.isChecked()) {
								SearchKey.setValue(BPIDCardNumber.getValue()
										.toString());
							}
						}

					}

				});

		// Alamat 1
		row = rows.newRow();
		row.appendCellChild(LabelAddress1.rightAlign());
		row.appendCellChild(Address1);
		Address1.setRows(2);
		//Address1.setHflex("true");

		// Alamat 2
		row = rows.newRow();
		row.appendCellChild(LabelAddress2.rightAlign());
		row.appendCellChild(Address2);
		Address2.setRows(2);
		//Address2.setHflex("true");

		// City
		row = rows.newRow();
		row.appendCellChild(LabelCity.rightAlign());
		row.appendCellChild(cityList.getComponent(), 1);
		//cityList.getComponent().setHflex("true");

		// ZIP
		row = rows.newRow();
		row.appendCellChild(LabelZIP.rightAlign());
		row.appendCellChild(ZIP);
		//ZIP.setHflex("true");

		// Telepon 1
		row = rows.newRow();
		row.appendCellChild(LabelTelepon1.rightAlign());
		row.appendCellChild(Telepon1);
		//Telepon1.setHflex("true");

		// Telepon 2
		row = rows.newRow();
		row.appendCellChild(LabelTelepon2.rightAlign());
		row.appendCellChild(Telepon2);
		//Telepon2.setHflex("true");

		// E-Mail
		row = rows.newRow();
		row.appendCellChild(LabelEMail.rightAlign());
		row.appendCellChild(EMail);
		//EMail.setHflex("true");

		// Credit Limit
		row = rows.newRow();
		row.appendCellChild(LabelCreditLimit.rightAlign());
		row.appendCellChild(CreditLimit);
		//CreditLimit.setHflex("true");
		CreditLimit.setValue(creditLimit);

		// Credit Limit
		row = rows.newRow();
		row.appendCellChild(Tanda.rightAlign(), 2);
		//Tanda.setHflex("true");

		Vbox vbox = new Vbox();
		w.appendChild(vbox);
		vbox.appendChild(new Separator());
		final ConfirmPanel panel = new ConfirmPanel(true, false, false, false,
				false, false, false);
		vbox.appendChild(panel);
		panel.addActionListener(Events.ON_CLICK, new EventListener<Event>() {

			public void onEvent(Event event) throws Exception {
				if (event.getTarget() == panel.getButton(ConfirmPanel.A_CANCEL)) {
					w.dispose();
				} else if (event.getTarget() == panel
						.getButton(ConfirmPanel.A_OK)) {

					boolean mndtry_bday = false;
					boolean mndtry_addrss = false;
					boolean mndtry_ktp = false;
					boolean mndtry_zip = false;
					boolean mndtry_phone2 = false;
					boolean mndtry_email = false;

					StringBuilder SQLMandatory = new StringBuilder();
					SQLMandatory.append("SELECT Value,Description ");
					SQLMandatory.append(" FROM AD_Param ");
					SQLMandatory.append(" WHERE value IN ( ");
					SQLMandatory.append("'" + birthday + "', ");
					SQLMandatory.append("'" + Addres2 + "', ");
					SQLMandatory.append("'" + KTPNo + "', ");
					SQLMandatory.append("'" + zipcode + "', ");
					SQLMandatory.append("'" + phone2 + "', ");
					SQLMandatory.append("'" + email + "' ");
					SQLMandatory.append(" )");

					PreparedStatement pstmt = null;
					ResultSet rs = null;
					try {
						pstmt = DB.prepareStatement(SQLMandatory.toString(),
								null);

						rs = pstmt.executeQuery();
						while (rs.next()) {

							if (rs.getString(1).toUpperCase() == birthday
									.toUpperCase()
									&& rs.getString(2).toUpperCase() == "Y") {
								mndtry_bday = true;
							} else if (rs.getString(1).toUpperCase() == Addres2
									.toUpperCase()
									&& rs.getString(2).toUpperCase() == "Y") {
								mndtry_addrss = true;
							} else if (rs.getString(1).toUpperCase() == KTPNo
									.toUpperCase()
									&& rs.getString(2).toUpperCase() == "Y") {
								mndtry_ktp = true;
							} else if (rs.getString(1).toUpperCase() == zipcode
									.toUpperCase()
									&& rs.getString(2).toUpperCase() == "Y") {
								mndtry_zip = true;
							} else if (rs.getString(1).toUpperCase() == phone2
									.toUpperCase()
									&& rs.getString(2).toUpperCase() == "Y") {
								mndtry_phone2 = true;
							} else if (rs.getString(1).toUpperCase() == email
									.toUpperCase()
									&& rs.getString(2).toUpperCase() == "Y") {
								mndtry_email = true;
							}

						}

					} catch (SQLException e) {
						log.log(Level.SEVERE, SQLMandatory.toString(), e);
					} finally {
						DB.close(rs, pstmt);
						rs = null;
						pstmt = null;
					}

					String p_SearchKey = SearchKey.getValue().toString();

					// exist search key validation
					StringBuilder SQLCekExistSearchKey = new StringBuilder();
					SQLCekExistSearchKey.append("SELECT C_BPartner_ID ");
					SQLCekExistSearchKey.append("FROM C_BPartner ");
					SQLCekExistSearchKey.append("WHERE Value ='");
					SQLCekExistSearchKey.append(p_SearchKey + "'");
					SQLCekExistSearchKey.append(" AND AD_Client_ID = ?");

					int cekPartner_ID = DB.getSQLValueEx(ctx.toString(),
							SQLCekExistSearchKey.toString(),
							new Object[] { AD_Client_ID });

					if (cekPartner_ID > 0) {
						FDialog.info(
								windowNo,
								null,
								"",
								"Penambahan Pelanggan Tidak Berhasil, Search Key Pelanggan Duplikat",
								"Info");
						return;
					}

					MLocation loc = new MLocation(ctx, 0, null);
					MCity city = null;
					MRegion reg = null;
					Integer C_City_ID = (Integer) cityList.getValue();
					String msg = "";
					if (C_City_ID == null) {
						C_City_ID = 0;
					}

					// Validasi
					if (SearchKey.getValue().toString() == ""
							|| SearchKey.getValue().isEmpty()) {
						msg = msg + "Search Key Tidak Boleh Kosong" + "\n";
					}

					if (BPIDCardNumber.getValue().toString().length() != 16
							&& BPName.getValue().isEmpty()) {
						msg = msg
								+ "No KTP dan Nama Pelanggan Tidak Boleh Kosong Semua"
								+ "\n";
					}

					if (BPName.getValue().toString() == ""
							|| BPName.getValue().isEmpty()) {
						msg = msg + "Nama Pelanggan Belum Di Isi " + "\n";
					}

					if (TTL.getValue() != null) {
						Date input = (Date) TTL.getValue();
						Date now = new Date();

						Calendar cal1 = Calendar.getInstance();
						cal1.setTime(input);
						int yearInput = cal1.get(Calendar.YEAR);

						Calendar cal2 = Calendar.getInstance();
						cal1.setTime(now);
						int yearNow = cal2.get(Calendar.YEAR);

						if (yearInput >= yearNow) {
							msg = msg
									+ "Input Tahun Lahir Harus Lebih Kecil Dari Tahun Sekarang "
									+ "\n";
						}
					}
					if (BPMotherName.getValue().toString() == ""
							|| BPMotherName.getValue().isEmpty()) {
						msg = msg + "Nama Ibu Belum Di Isi " + "\n";
					}
					// if (BPIDCardNumber.getValue().toString() == "" ||
					// BPIDCardNumber.getValue().isEmpty()) {
					// msg = msg + "No KTP Belum Di Isi " + "\n";
					// }
					if (Address1.getValue().toString() == ""
							|| Address1.getValue().isEmpty()) {
						msg = msg + "Alamat Belum Di Isi " + "\n";
					}
					if (C_City_ID <= 0 || C_City_ID == null) {
						msg = msg + "Kota Belum Di Pilih " + "\n";
					}
					if (Telepon1.getValue().toString() == ""
							&& Telepon1.getValue().isEmpty()) {
						msg = msg + "Telepon1 Belum Di Isi " + "\n";

					}
					if (CreditLimit.getValue() == null) {
						msg = msg + "Batas Kredit Belum Di Isi " + "\n";
					}

					if (TTL.getValue() == null && mndtry_bday) {
						msg = msg + "Tanggal Lahir Belum Di Isi " + "\n";
					}

					if (Address2.getValue() == null && mndtry_addrss) {
						msg = msg + "Address2 Belum Di Isi " + "\n";
					}

					if (BPIDCardNumber.getValue() == null && mndtry_ktp) {
						msg = msg + "No KTP Belum Di Isi " + "\n";
					}

					if (ZIP.getValue() == null && mndtry_zip) {
						msg = msg + "ZIP Belum Di Isi " + "\n";
					}

					if (Telepon2.getValue() == null && mndtry_phone2) {
						msg = msg + "Phone 2 Belum Di Isi " + "\n";
					}

					if (EMail.getValue() == null && mndtry_email) {
						msg = msg + "EMail Belum Di Isi " + "\n";
					}

					if (msg != "") {
						FDialog.info(windowNo, null, "", msg, "Info");
						return;
					}

					MBPartner bp = new MBPartner(ctx, 0, null);
					bp.setAD_Org_ID(0);
					bp.setValue(p_SearchKey);
					bp.setName(BPName.getValue().toString());
					bp.setSO_CreditLimit(CreditLimit.getValue());
					bp.set_CustomColumn("IbuKandung", BPMotherName.getValue()
							.toString());
					bp.set_CustomColumn("NoKTP", BPIDCardNumber.getValue()
							.toString());
					bp.setIsCustomer(true);
					bp.saveEx();

					int C_Region_ID = 1000000;// Indonesia
					if (C_City_ID > 0) {
						city = new MCity(ctx, C_City_ID, null);
					}
					if (C_Region_ID > 0) {
						reg = new MRegion(ctx, C_Region_ID, null);
					}

					loc.setAD_Org_ID(0);
					loc.setAddress1(Address1.getValue().toString());
					loc.setAddress2(Address2.getValue().toString());
					loc.setC_City_ID(C_City_ID);
					loc.setC_Region_ID(C_Region_ID);
					loc.setC_Country_ID(209);
					loc.setCity(city.getName());
					loc.setRegionName(reg.getName());
					loc.saveEx();

					MBPartnerLocation BPLoc = new MBPartnerLocation(ctx, 0,
							null);
					BPLoc.setAD_Org_ID(0);
					BPLoc.setC_BPartner_ID(bp.getC_BPartner_ID());
					BPLoc.setName(loc.getCity());
					BPLoc.setC_Location_ID(loc.getC_Location_ID());
					BPLoc.setPhone(Telepon1.getValue().toString());
					BPLoc.setPhone2(Telepon2.getValue().toString());
					if (TTL.getValue() != null) {
						BPLoc.set_ValueNoCheck("Birthday", TTL.getValue());
					}
					BPLoc.setIsShipTo(true);
					BPLoc.setIsBillTo(true);
					BPLoc.setIsPayFrom(true);
					BPLoc.setIsRemitTo(true);
//					BPLoc.set_CustomColumn("IsTax", true);
					BPLoc.saveEx();

					if (bp.getC_BPartner_ID() > 0) {
						String BPNameRs = bp.getName();
						FDialog.info(windowNo, null, "",
								"Input Data Pelanggan Dengan Nama " + BPNameRs
										+ " Berhasil Disimpan", "Info");
						PelangganSearch.setValue(bp.getC_BPartner_ID());
					} else if (bp.getC_BPartner_ID() <= 0) {
						FDialog.info(windowNo, null, "",
								"Penambahan Pelanggan Tidak Berhasil", "Info");
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
						// TODO
					}
				});
		w.doHighlighted();
	}

	public int getIDFromComboBox(Combobox combobox, String tableName,
			String selectColumnName) {
		int result_ID = 0;
		String select_ID = tableName + "_ID";

		String cbValue = combobox.getText();

		StringBuilder sqlPriceList = new StringBuilder();

		sqlPriceList.append("SELECT ");
		sqlPriceList.append(select_ID);
		sqlPriceList.append(" FROM ");
		sqlPriceList.append(tableName);
		sqlPriceList.append(" WHERE AD_Client_ID = ? ");
		sqlPriceList.append(" AND " + selectColumnName + "= ");
		sqlPriceList.append(" '" + cbValue + "'");

		result_ID = DB.getSQLValueEx(null, sqlPriceList.toString(),
				AD_Client_ID);

		return result_ID;

	}

	private ArrayList<KeyNamePair> loadWarehouse(int AD_Org_ID) {
		ArrayList<KeyNamePair> list = new ArrayList<KeyNamePair>();
		int AD_Client_ID = Env.getAD_Client_ID(Env.getCtx());
		StringBuilder sql = new StringBuilder();
		sql.append("SELECT M_Warehouse_ID,Name ");
		sql.append("FROM M_Warehouse ");
		sql.append(" WHERE AD_Client_ID = ? ");
		sql.append(" AND AD_Org_ID = ? ");
		sql.append(" AND IsActive = 'Y' ");
//		sql.append("AND M_Warehouse_ID IN ");
//		sql.append(" (SELECT DISTINCT (M_Warehouse_ID) ");
//		sql.append(" FROM M_Warehouse_Access ");
//		sql.append(" WHERE AD_Role_ID = ? )");

		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			pstmt = DB.prepareStatement(sql.toString(), null);
			pstmt.setInt(1, AD_Client_ID);
			pstmt.setInt(2, AD_Org_ID);
			//pstmt.setInt(3, Env.getAD_Role_ID(ctx));

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

	private ArrayList<KeyNamePair> loadLocator(int M_Warehouse_ID, int AD_Org_ID) {
		ArrayList<KeyNamePair> list = new ArrayList<KeyNamePair>();
		int AD_Client_ID = Env.getAD_Client_ID(Env.getCtx());
		StringBuilder sql = new StringBuilder();
		sql.append("SELECT M_Locator_ID,Value ");
		sql.append("FROM M_Locator ");
		sql.append("WHERE AD_Client_ID = ? ");
		sql.append("AND AD_Org_ID = ? ");
		sql.append("AND IsActive = 'Y' ");
		sql.append("AND M_Warehouse_ID = ? ");

		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			pstmt = DB.prepareStatement(sql.toString(), null);
			pstmt.setInt(1, AD_Client_ID);
			pstmt.setInt(2, AD_Org_ID);
			pstmt.setInt(3, M_Warehouse_ID);

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

	private ArrayList<KeyNamePair> loadPriceList() {
		ArrayList<KeyNamePair> list = new ArrayList<KeyNamePair>();
		int AD_Client_ID = Env.getAD_Client_ID(Env.getCtx());
		StringBuilder sql = new StringBuilder();
		sql.append("SELECT M_PriceList_ID,Name ");
		sql.append("FROM M_PriceList ");
		sql.append("WHERE AD_Client_ID = ? ");
		sql.append("AND IsActive = 'Y' ");
		sql.append("AND IsSOPriceList = 'Y' ");

		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			pstmt = DB.prepareStatement(sql.toString(), null);
			pstmt.setInt(1, AD_Client_ID);

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

	private void process(boolean IsOtorisasiHutang, boolean IsOtorisasiPriceLimit,int Spv_ID,
			Timestamp apprvDate, int C_Payterm_ID) {

		String ErrorMsg = validation();
		if (ErrorMsg != "") {
			FDialog.info(windowNo, null, "", ErrorMsg, "Info");
			return;
		}

		// add new validation cash
		int BPCash = 0;

		BPCash = mustTunai();
		if (BPCash > 0) {

			int BpPOS = (int) PelangganSearch.getValue();
			BigDecimal bank = paymentBank.getValue();
			BigDecimal hutang = paymentHutang.getValue();
			BigDecimal leasing = paymentLeasing.getValue();

			MBPartner bpcsh = new MBPartner(ctx, BpPOS, null);

			if (BPCash == BpPOS) {

				if (bank.compareTo(Env.ZERO) > 0
						|| hutang.compareTo(Env.ZERO) > 0
						|| leasing.compareTo(Env.ZERO) > 0) {

					FDialog.info(windowNo, null, "",
							"Pembayaran Atas Pelanggan " + bpcsh.getName()
									+ " Harus Full Tunai", "Info");
					return;
				}

			}

		}

		if (M_Pricelist_ID == 0) {

			Integer PriceList_ID = getIDFromComboBox(DaftarHargaSearch,
					MPriceList.Table_Name, MPriceList.COLUMNNAME_Name);

			M_Pricelist_ID = PriceList_ID;
		}

		if (!isForceLimitPrice) {

			for (int i = 0; i < salesTable.getRowCount(); i++) {

				BigDecimal price = (BigDecimal) salesTable.getValueAt(i, 4);
				KeyNamePair prod = (KeyNamePair) salesTable.getValueAt(i, 1);

				Timestamp date = new Timestamp(System.currentTimeMillis());

				String sql = "SELECT plv.M_PriceList_Version_ID "
						+ "FROM M_PriceList_Version plv "
						+ "WHERE plv.AD_Client_ID = ? "
						+ " AND plv.M_PriceList_ID= ? " // 1
						+ " AND plv.ValidFrom <= ? "
						+ "ORDER BY plv.ValidFrom DESC";

				int M_PriceList_Version_ID = DB.getSQLValueEx(null, sql,
						new Object[] { AD_Client_ID, M_Pricelist_ID, date });

				String sqlProductPrice = "SELECT PriceLimit FROM M_ProductPrice WHERE AD_Client_ID = ? AND M_PriceList_Version_ID = ? AND M_Product_ID = ?";
				BigDecimal priceLimit = DB.getSQLValueBDEx(null,sqlProductPrice, new Object[] { AD_Client_ID,M_PriceList_Version_ID, prod.getKey() });

				if (price.compareTo(priceLimit) < 0) {
					FDialog.warn(windowNo,null,"","Harga Yang Anda Inputkan Atas Produk "+ prod.getName()+ " Lebih Rendah Dari Limit Harga","Peringatan");
					return;
				}

			}
		}

		if (C_PaymentTerm_ID <= 0) {

			String sqlterm = "SELECT C_PaymentTerm_ID FROM C_PaymentTerm WHERE AD_Client_ID = ? AND IsDefault = 'Y' ";
			C_PaymentTerm_ID = DB.getSQLValueEx(ctx.toString(),
					sqlterm.toString(),
					new Object[] { Env.getAD_Client_ID(ctx) });

		}

		if (C_PaymentTerm_ID <= 0) {

			FDialog.info(windowNo, null, "", "PaymentTerm belum ditentukan",
					"Peringatan");
			return;
		}

		Integer AD_Org_ID = (Integer) orgSearch.getValue();

		// cek validate period status

		Date tgl = tanggalSearch.getValue();
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd 00:00:00");
		String tglTostr = df.format(tgl);

		Timestamp dateOrd = Timestamp.valueOf(tglTostr);

		if (!MPeriod.isOpen(ctx, dateOrd, "SOO", AD_Org_ID)) {
			FDialog.warn(
					windowNo,
					null,
					"",
					"Transaksi Tidak Dapat Diproses Karena Status Period Closed",
					"Peringatan");
			return;
		}

		String msg = "";
		String docType = "";

		Integer SalesRep_ID = (Integer) salesSearch.getValue();
		Integer C_BPartner_ID = (Integer) PelangganSearch.getValue();
		Integer M_WareHouse_ID = getIDFromComboBox(GudangSearch,MWarehouse.Table_Name, MWarehouse.COLUMNNAME_Name);
		Integer C_BankAccount_ID = (Integer) bankAccountSearch.getValue();

		String leaseProv = (String) leasingProviderSearch.getValue();
		String tipeBayar1 = payruleBoxTunai.getLabel();
		String tipeBayar2 = payruleBoxBank.getLabel();
		String tipeBayar3 = payruleBoxLeasing.getLabel();
		String tipeBayar4 = payruleBoxHutang.getLabel();

		Double disc = 0.00;

		if (AD_Language.toUpperCase().equals("EN_US")) {

			disc = Double.valueOf(diskonTextBox.getText().replaceAll(",", ""));
		} else if (AD_Language.toUpperCase().equals("IN_ID")) {
			String ddisc = diskonTextBox.getText().replaceAll("\\.", "")
					.replaceAll(",", ".");
			disc = Double.valueOf(ddisc);
		}

		BigDecimal grandtot = nilaiGrandTotal;
		BigDecimal payment1 = paymentTunai.getValue();
		BigDecimal payment2 = paymentBank.getValue();
		BigDecimal payment3 = paymentLeasing.getValue();
		BigDecimal payment4 = paymentHutang.getValue();
		BigDecimal totalDisc = BigDecimal.valueOf(disc);

		IMiniTable miniTable = salesTable;

		int C_DocType_ID = 0;

		if (AD_Org_ID == null) {
			AD_Org_ID = 0;
		}
		if (C_BPartner_ID == null) {
			C_BPartner_ID = 0;
		}
//		if (M_WareHouse_ID == null) {
//			M_WareHouse_ID = 0;
//		}
		if (SalesRep_ID == null) {
			SalesRep_ID = 0;
		}
		if (C_BankAccount_ID == null) {
			C_BankAccount_ID = 0;
		}

		// cek required input
		msg = checkBeforeProcess(AD_Org_ID, M_Pricelist_ID, C_BPartner_ID,
				M_WareHouse_ID, C_PaymentTerm_ID, SalesRep_ID, CreatedByPOS_ID,
				C_Currency_ID, C_BankAccount_ID, payruleBoxTunai.getLabel(),
				leaseProv, payment2, payment3, salesTable);

		if (msg != "") {
			FDialog.warn(form.getWindowNo(), null, msg, "", "Peringatan");
			return;
		}

		String checkLimit = creditLimitValidation(C_BPartner_ID);
		if (checkLimit != "") {
			FDialog.warn(form.getWindowNo(), null, checkLimit, "", "Peringatan");
			return;
		}

		String cekImei = "";
		cekImei = checkImeiExist(salesTable, windowNo);
		if (cekImei != "") {
			return;
		}

		// cek Lease Provider
		if (leasingProviderSearch.getValue() != null) {

			// leaseProvid =
			// cekBankAccount(leasingProviderSearch.getValue().toString());

			int validLeasingBankAcct = cekBankAcctLeasing(AD_Client_ID,
					leasingProviderSearch.getValue().toString());

			if (validLeasingBankAcct <= 0) {

				FDialog.info(
						windowNo,
						null,
						"",
						"Silahkan setup Akun Bank atas leasing tersebut terlebih dahulu pada menuBank / Cash tab Bank Account subtab Leasing Provider",
						"Info Process POS");
				return;

			}

			BankAccountLeasing = getBankAcctLeasing(AD_Client_ID,
					leasingProviderSearch.getValue().toString());

			if (leasingProviderSearch.getValue().equals("Spektra")) {
				BigDecimal payLeasing = paymentLeasing.getValue();

				StringBuilder getMinimumLeasing = new StringBuilder();
				getMinimumLeasing.append("SELECT Description::numeric ");
				getMinimumLeasing.append(" FROM AD_Param ");
				getMinimumLeasing
						.append(" WHERE Value = 'Min_Pembiayaan_Spektra' ");

				BigDecimal minimumLeasing = DB.getSQLValueBD(null,
						getMinimumLeasing.toString());

				if (payLeasing.compareTo(minimumLeasing) < 0) {

					FDialog.warn(
							form.getWindowNo(),
							null,
							"Nilai Pembayaran Leasing Kurang dari Nilai Minimum",
							"", "Peringatan");
					return;
				}
			}
		}

		// cek Stok
		boolean isStocked = true;
		String Stocked = checkStok(salesTable, windowNo);

		if (Stocked != "") {
			isStocked = false;
		} else if (Stocked == "") {
			isStocked = true;
		}

		if (!isStocked && PickUp.isChecked()) {

			FDialog.warn(form.getWindowNo(), null, "", Stocked, "Peringatan");
			return;
		}

		// cek must have imei
		for (int i = 0; i < salesTable.getRowCount(); i++) {
			KeyNamePair cekProd = (KeyNamePair) salesTable.getValueAt(i, 1);
			int prod_id = cekProd.getKey();
			MProduct pr = new MProduct(ctx, prod_id, null);
			int M_AttributeSet_ID = pr.getM_AttributeSet_ID();
			if (M_AttributeSet_ID > 0) {

				boolean isStockIMEI = true;
				KeyNamePair Loc = (KeyNamePair) miniTable.getValueAt(i, 7);

				String imei = (String) miniTable.getValueAt(i, 8);
				String sqlIMEI = "SELECT M_AttributeSetInstance_ID FROM M_AttributeSetInstance WHERE AD_Client_ID = ? AND serno= '"
						+ imei + "'";
				Integer Imeiid = DB.getSQLValueEx(pr.get_TrxName(), sqlIMEI,
						new Object[] { AD_Client_ID });

				String sqlStock = "SELECT M_Product_ID FROM M_StorageOnHand WHERE AD_Client_ID = ? AND M_Product_ID = ? AND M_Locator_ID = ? AND QtyOnHand > 0 AND M_AttributeSetInstance_ID = ?";

				Integer mProd = DB.getSQLValueEx(pr.get_TrxName(), sqlStock,
						new Object[] { AD_Client_ID, prod_id, Loc.getKey(),
								Imeiid });

				if (mProd <= 0 || mProd == null) {
					isStockIMEI = false;
				}

				if (!isStockIMEI && !PickUp.isChecked()) {
					m_docAction = "PR";
				}
			}
		}

		// cek multiLocator
		boolean isMultiLoc = multiLocatorCheck(salesTable);

		Integer M_Locator_ID = 0;
		if (!isMultiLoc) {

			for (int i = 0; i < salesTable.getRowCount(); i++) {
				KeyNamePair rs = (KeyNamePair) salesTable.getValueAt(i, 7);
				if (rs != null) {
					M_Locator_ID = rs.getKey();
				}
			}

			if (M_Locator_ID < 0) {
				M_Locator_ID = ((int) getIDFromComboBox(Lokasi,
						MLocator.Table_Name, MLocator.COLUMNNAME_M_Locator_ID));

				if (M_Locator_ID < 0 || M_Locator_ID == null) {
					M_Locator_ID = 0;
				}
			}

		}

		// multilocator
		if (isCompleteMultiLocator) {
			if (isMultiLoc && !isStocked) {
				PickUp.setChecked(false);
			} else if (isMultiLoc && isStocked && PickUp.isChecked()) {
				PickUp.setChecked(true);
			}
		} else {
			if (isMultiLoc) {
				PickUp.setChecked(false);
			}
		}
		// end

		BigDecimal comparator = BigDecimal.valueOf(0.00);
		if (payment1.compareTo(comparator) == 0) {
			payment1 = Env.ZERO;
		}
		if (payment2.compareTo(comparator) == 0) {
			payment2 = Env.ZERO;
		}
		if (payment3.compareTo(comparator) == 0) {
			payment3 = Env.ZERO;
		}
		if (payment4.compareTo(comparator) == 0) {
			payment4 = Env.ZERO;
		}

		if (payment1 == Env.ZERO && payment2 == Env.ZERO
				&& payment3 == Env.ZERO && payment4 == Env.ZERO) {

			FDialog.warn(form.getWindowNo(), null, "",
					"Silahkan Entri Pembayaran Terlebih Dahulu", "Peringatan");
			return;

		}

		boolean isTunai = false;
		boolean isFullTunai = false;

		boolean isBank = false;
		boolean isFullBank = false;

		boolean isLeasing = false;
		boolean isFullLeasing = false;

		boolean isHutang = false;
		boolean isFullHutang = false;

		if (payment1.compareTo(Env.ZERO) > 0) {
			isTunai = true;
			if (payment1.compareTo(grandtot) >= 0) {
				isFullTunai = true;
			}
		}

		if (payment2.compareTo(Env.ZERO) > 0) {
			isBank = true;
			if (payment2.compareTo(grandtot) >= 0) {
				isFullBank = true;
			}
		}

		if (payment3.compareTo(Env.ZERO) > 0) {
			isLeasing = true;
			if (payment3.compareTo(grandtot) >= 0) {
				isFullLeasing = true;
			}
		}
		if (payment4.compareTo(Env.ZERO) > 0) {
			isHutang = true;
			if (payment4.compareTo(grandtot) >= 0) {
				isFullHutang = true;
			}
		}

		// process create order
		String sql = "SELECT C_DocType_ID FROM C_DocType "
				+ " WHERE IsSoTrx = 'Y' " + " AND AD_Client_ID = ?"
				+ " AND DocSubTypeSO = ?";

		X_SM_SemeruTemp decPos_temp = new X_SM_SemeruTemp(ctx, 0, null);

		boolean isPick = PickUp.isChecked();

		if (isFullTunai) {

			if (isPick) {

				docType = MDocType.DOCSUBTYPESO_POSOrder;
				C_DocType_ID = DB.getSQLValue(ctx.toString(), sql.toString(),
						new Object[] { Env.getAD_Client_ID(ctx), docType });
				decPos_temp.setDeliveryViaRule("P");

			} else if (!isPick) {

				docType = MDocType.DOCSUBTYPESO_PrepayOrder;
				C_DocType_ID = DB.getSQLValue(ctx.toString(), sql.toString(),
						new Object[] { Env.getAD_Client_ID(ctx), docType });
				decPos_temp.setDeliveryViaRule("D");

			}

		} else if (isFullBank) {

			if (isPick) {

				docType = MDocType.DOCSUBTYPESO_OnCreditOrder;
				C_DocType_ID = DB.getSQLValue(ctx.toString(), sql.toString(),
						new Object[] { Env.getAD_Client_ID(ctx), docType });
				decPos_temp.setDeliveryViaRule("P");

			} else if (!isPick) {

				docType = MDocType.DOCSUBTYPESO_PrepayOrder;
				C_DocType_ID = DB.getSQLValue(ctx.toString(), sql.toString(),
						new Object[] { Env.getAD_Client_ID(ctx), docType });
				decPos_temp.setDeliveryViaRule("D");

			}

		} else if ((isTunai && !isFullTunai) && (isBank && !isFullBank)) {

			if (isPick) {

				docType = MDocType.DOCSUBTYPESO_OnCreditOrder;
				C_DocType_ID = DB.getSQLValue(ctx.toString(), sql.toString(),
						new Object[] { Env.getAD_Client_ID(ctx), docType });
				decPos_temp.setDeliveryViaRule("P");

			} else if (!isPick) {

				docType = MDocType.DOCSUBTYPESO_PrepayOrder;
				C_DocType_ID = DB.getSQLValue(ctx.toString(), sql.toString(),
						new Object[] { Env.getAD_Client_ID(ctx), docType });
				decPos_temp.setDeliveryViaRule("D");

			}

		} else if ((isTunai && !isFullTunai) && (isHutang && !isFullHutang)) {

			if (isPick) {

				docType = MDocType.DOCSUBTYPESO_OnCreditOrder;
				C_DocType_ID = DB.getSQLValue(ctx.toString(), sql.toString(),
						new Object[] { Env.getAD_Client_ID(ctx), docType });
				decPos_temp.setDeliveryViaRule("P");

			} else if (!isPick) {

				docType = MDocType.DOCSUBTYPESO_PrepayOrder;
				C_DocType_ID = DB.getSQLValue(ctx.toString(), sql.toString(),
						new Object[] { Env.getAD_Client_ID(ctx), docType });
				decPos_temp.setDeliveryViaRule("D");

			}

		} else if ((isTunai && !isFullTunai) && (isLeasing && !isFullLeasing)) {

			if (isPick) {

				docType = MDocType.DOCSUBTYPESO_OnCreditOrder;
				C_DocType_ID = DB.getSQLValue(ctx.toString(), sql.toString(),
						new Object[] { Env.getAD_Client_ID(ctx), docType });
				decPos_temp.setDeliveryViaRule("P");

			} else if (!isPick) {

				docType = MDocType.DOCSUBTYPESO_PrepayOrder;
				C_DocType_ID = DB.getSQLValue(ctx.toString(), sql.toString(),
						new Object[] { Env.getAD_Client_ID(ctx), docType });
				decPos_temp.setDeliveryViaRule("D");

			}

		} else if (isFullHutang) {

			if (isPick) {

				docType = MDocType.DOCSUBTYPESO_OnCreditOrder;
				C_DocType_ID = DB.getSQLValue(ctx.toString(), sql.toString(),
						new Object[] { Env.getAD_Client_ID(ctx), docType });
				decPos_temp.setDeliveryViaRule("P");

			} else if (!isPick) {

				docType = MDocType.DOCSUBTYPESO_StandardOrder;
				C_DocType_ID = DB.getSQLValue(ctx.toString(), sql.toString(),
						new Object[] { Env.getAD_Client_ID(ctx), docType });
				decPos_temp.setDeliveryViaRule("D");

			}

		} else if (isFullLeasing) {

			if (isPick) {

				docType = MDocType.DOCSUBTYPESO_OnCreditOrder;
				C_DocType_ID = DB.getSQLValue(ctx.toString(), sql.toString(),
						new Object[] { Env.getAD_Client_ID(ctx), docType });
				decPos_temp.setDeliveryViaRule("P");

			} else if (!isPick) {

				docType = MDocType.DOCSUBTYPESO_StandardOrder;
				C_DocType_ID = DB.getSQLValue(ctx.toString(), sql.toString(),
						new Object[] { Env.getAD_Client_ID(ctx), docType });
				decPos_temp.setDeliveryViaRule("D");

			}

		} else if ((isBank && !isFullBank) && (isHutang && !isFullHutang)) {
			if (isPick) {

				docType = MDocType.DOCSUBTYPESO_OnCreditOrder;
				C_DocType_ID = DB.getSQLValue(ctx.toString(), sql.toString(),
						new Object[] { Env.getAD_Client_ID(ctx), docType });
				decPos_temp.setDeliveryViaRule("P");

			} else if (!isPick) {

				docType = MDocType.DOCSUBTYPESO_StandardOrder;
				C_DocType_ID = DB.getSQLValue(ctx.toString(), sql.toString(),
						new Object[] { Env.getAD_Client_ID(ctx), docType });
				decPos_temp.setDeliveryViaRule("D");

			}
		} else if ((isBank && !isFullBank) && (isLeasing && !isFullLeasing)) {
			if (isPick) {

				docType = MDocType.DOCSUBTYPESO_OnCreditOrder;
				C_DocType_ID = DB.getSQLValue(ctx.toString(), sql.toString(),
						new Object[] { Env.getAD_Client_ID(ctx), docType });
				decPos_temp.setDeliveryViaRule("P");

			} else if (!isPick) {

				docType = MDocType.DOCSUBTYPESO_PrepayOrder;
				C_DocType_ID = DB.getSQLValue(ctx.toString(), sql.toString(),
						new Object[] { Env.getAD_Client_ID(ctx), docType });
				decPos_temp.setDeliveryViaRule("D");

			}

		} else if ((isHutang && !isFullHutang) && (isLeasing && !isFullLeasing)) {

			if (isPick) {

				docType = MDocType.DOCSUBTYPESO_OnCreditOrder;
				C_DocType_ID = DB.getSQLValue(ctx.toString(), sql.toString(),
						new Object[] { Env.getAD_Client_ID(ctx), docType });
				decPos_temp.setDeliveryViaRule("P");

			} else if (!isPick) {

				docType = MDocType.DOCSUBTYPESO_StandardOrder;
				C_DocType_ID = DB.getSQLValue(ctx.toString(), sql.toString(),
						new Object[] { Env.getAD_Client_ID(ctx), docType });
				decPos_temp.setDeliveryViaRule("D");

			}

		}

		String decPOsdocType = "SELECT C_DocType_ID " + " FROM C_DocType "
				+ " WHERE AD_Client_ID = ?" + " AND DocBaseType = ? ";
		String docDec = "POS";

		int decDoc_ID = DB.getSQLValueEx(null, decPOsdocType, new Object[] {AD_Client_ID, docDec });
		decPos_temp.setC_DocType_ID(decDoc_ID);
		decPos_temp.setAD_Org_ID((int) orgSearch.getValue());

		StringBuilder sqlBPLoc = new StringBuilder();
		sqlBPLoc.append("SELECT C_BPartner_Location_ID ");
		sqlBPLoc.append("FROM C_BPartner_Location ");
		sqlBPLoc.append("WHERE C_BPartner_ID = ? ");
		int C_BPartner_Location_ID = DB.getSQLValueEx(ctx.toString(),sqlBPLoc.toString(), new Object[] { C_BPartner_ID });
		decPos_temp.setC_BPartner_ID((int) PelangganSearch.getValue());
		decPos_temp.setC_BPartner_Location_ID(C_BPartner_Location_ID);
		decPos_temp.setOrderDocType_ID(C_DocType_ID);
		decPos_temp.setDateOrdered(dateOrd);
		decPos_temp.setDescription((String) keteranganTextBox.getValue().toString());
		decPos_temp.setM_PriceList_ID(M_Pricelist_ID);
		decPos_temp.setDiscountAmt(totalDisc);
		decPos_temp.setM_Warehouse_ID((int) getIDFromComboBox(GudangSearch,MWarehouse.Table_Name, MWarehouse.COLUMNNAME_Name));
		decPos_temp.setDeliveryRule(MOrder.DELIVERYRULE_AfterReceipt);
		decPos_temp.setPaymentRule(MOrder.PAYMENTRULE_MixedPOSPayment);
		decPos_temp.setCreatedByPos_ID(CreatedByPOS_ID);
		decPos_temp.setSalesRep_ID(SalesRep_ID);
		decPos_temp.setIsPickup(isPick);
		decPos_temp.setGrandTotal(nilaiGrandTotal);
		decPos_temp.setTotalLines(totalPrice);
		decPos_temp.setTaxAmt(nilaiPajak);

		if (IsDebit.isChecked()) {

			decPos_temp.set_CustomColumnReturningBoolean("IsDebitCard", true);
		}

		// IsOtorisasi Hutang
		if (IsOtorisasiHutang || IsOtorisasiPriceLimit) {
			
			if(Spv_ID > 0){
				decPos_temp.setSupervisor_ID(Spv_ID);
			}
		}

		if (IsOtorisasiHutang || IsOtorisasiPriceLimit ) {
			if(C_Payterm_ID > 0){
				decPos_temp.setC_PaymentTerm_ID(C_Payterm_ID);
			}
		} else {
			decPos_temp.setC_PaymentTerm_ID(C_PaymentTerm_ID);
		}

		if (IsOtorisasiHutang || IsOtorisasiPriceLimit ) {
			
			if(apprvDate != null){
				decPos_temp.set_CustomColumn("ApproveDate", apprvDate);
			}

		}
		// END IsOtorisasi Hutang
		

		if (payment1.compareTo(Env.ZERO) > 0) {
			decPos_temp.setPayType1(tipeBayar1.toUpperCase());
		}
		if (payment2.compareTo(Env.ZERO) > 0) {
			decPos_temp.setPayType2(tipeBayar2.toUpperCase());
		}
		if (payment3.compareTo(Env.ZERO) > 0) {
			decPos_temp.setPayType3(tipeBayar3.toUpperCase());
			decPos_temp.set_ValueOfColumnReturningBoolean("IsLeasing", true);
			if (leasingProviderSearch.getValue().toString().toUpperCase()
					.equals("SPEKTRA")) {
				decPos_temp
						.set_ValueOfColumnReturningBoolean("IsSpektra", true);

			}
		}
		if (payment4.compareTo(Env.ZERO) > 0) {
			decPos_temp.setPayType4(tipeBayar4.toUpperCase());
		}

		// manual documentno
		if (isManualDocumentNo) {
			decPos_temp.setDocumentNo(noNota.getValue());
		}

		if (payment1.compareTo(Env.ZERO) > 0) {

			Double kmbli = 0.00;

			if (kembalianTextBox.getText().isEmpty()) {
				kmbli = 0.00;
			} else if (!kembalianTextBox.getText().isEmpty()) {
				if (AD_Language.toUpperCase().equals("EN_US")) {
					kmbli = Double.valueOf(kembalianTextBox.getText()
							.replaceAll(",", ""));
				} else if (AD_Language.toUpperCase().equals("IN_ID")) {
					String dTtlBlj = kembalianTextBox.getText()
							.replaceAll("\\.", "").replaceAll(",", ".");
					kmbli = Double.valueOf(dTtlBlj);
				}
			}
			BigDecimal kembalian = BigDecimal.valueOf(kmbli);

			decPos_temp.setPembayaran1(payment1.subtract(kembalian));
			decPos_temp.set_CustomColumn("TotalKembalian", kembalian);
			decPos_temp.set_CustomColumn("TotalTunai", paymentTunai.getValue());
		}
		if (payment2.compareTo(Env.ZERO) > 0) {
			decPos_temp.setPembayaran2(payment2);
			decPos_temp.setC_BankAccount_ID((Integer) bankAccountSearch
					.getValue());
		}
		if (payment3.compareTo(Env.ZERO) > 0) {
			decPos_temp.setPembayaran3(payment3);
			decPos_temp.setLeaseProvider(leaseProv);
			decPos_temp.setC_BankAccount_ID(BankAccountLeasing);

		}

		if (payment4.compareTo(Env.ZERO) > 0) {
			decPos_temp.setPembayaran4(payment4);
		}

		decPos_temp.setIsPembatalan(false);
		decPos_temp.setIsSOTrx(true);
		decPos_temp.setIsPenjualan(true);
		decPos_temp.setIsPembayaran(false);
		decPos_temp.setIsReceipt(false);
		decPos_temp.setOrderDocType_ID(C_DocType_ID);
		decPos_temp.setLocatorNoMulti_ID(M_Locator_ID);
		if (isMultiLoc) {
			decPos_temp.setIsMultiLocator(true);
		}
		decPos_temp.saveEx();

//		Integer C_Decoris_PreSales_ID = (Integer) noPresales.getValue();
//		if (C_Decoris_PreSales_ID == null) {
//			C_Decoris_PreSales_ID = 0;
//		}

		for (int i = 0; i < miniTable.getRowCount(); i++) {
			boolean isSelect = (boolean) miniTable.getValueAt(i, 0);
			BigDecimal qty = (BigDecimal) miniTable.getValueAt(i, 2);
			BigDecimal price = (BigDecimal) miniTable.getValueAt(i, 4);
			BigDecimal totalPrice = qty.multiply(price);
			KeyNamePair product = (KeyNamePair) miniTable.getValueAt(i, 1);
			String serno = (String) miniTable.getValueAt(i, 8);
			KeyNamePair loc = (KeyNamePair) miniTable.getValueAt(i, 7);

			int M_Locator_ID_Multi = 0;

			String sqlProduct = "SELECT M_Product_ID FROM M_Product WHERE AD_Client_ID = ? AND name = '"
					+ product.toString() + "'";
			int Product_ID = DB.getSQLValue(null, sqlProduct.toString(),
					new Object[] { AD_Client_ID });
			MProduct prod = new MProduct(ctx, Product_ID, null);

			String sqlSerno = "SELECT M_AttributeSetInstance_ID FROM M_AttributeSetInstance WHERE AD_Client_ID = ? AND Serno = '"
					+ serno.toString() + "'";
			int M_AttributeSetInstance_ID = DB.getSQLValue(null,
					sqlSerno.toString(), new Object[] { AD_Client_ID });

			int taxCategory = prod.getC_TaxCategory_ID();

			if (isSelect) {
				miniTable.setValueAt(false, i, 0);
				miniTable.setColumnReadOnly(i, true);
			}

			MTaxCategory cat = new MTaxCategory(ctx, taxCategory, null);
			String taxCatName = cat.getName();

			String sqlTax = "SELECT C_Tax_ID FROM C_Tax WHERE AD_Client_ID = ? AND Name = '"
					+ taxCatName + "'";
			int C_Tax_ID = DB.getSQLValueEx(cat.get_TrxName(), sqlTax,
					new Object[] { AD_Client_ID });

			X_SM_SemeruLineTemp decPOSLine_temp = new X_SM_SemeruLineTemp(ctx,
					0, decPos_temp.get_TrxName());

			String sqlLine = "SELECT COALESCE(MAX(Line),0)+10 FROM SM_SemeruLineTemp WHERE SM_SemeruTemp_ID =?";
			int ii = DB.getSQLValue(decPos_temp.get_TrxName(), sqlLine,
					decPos_temp.getSM_SemeruTemp_ID());

			// DecorisLine
			decPOSLine_temp.setAD_Org_ID(decPos_temp.getAD_Org_ID());
			decPOSLine_temp.setSM_SemeruTemp_ID(decPos_temp
					.getSM_SemeruTemp_ID());

			if (isMultiLoc && prod.getProductType().toUpperCase().equals("I")) {
				M_Locator_ID_Multi = loc.getKey();

			}

			if (M_Locator_ID_Multi <= 0) {
				M_Locator_ID_Multi = M_Locator_ID;
			}

			if (prod.getProductType().toString().toUpperCase().equals("I")) {
				if (!isMultiLoc) {
					decPOSLine_temp.setM_Locator_ID(M_Locator_ID);
				} else if (isMultiLoc) {
					decPOSLine_temp.setM_Locator_ID(M_Locator_ID_Multi);
				}
			}
			decPOSLine_temp.setM_Product_ID(Product_ID);
			decPOSLine_temp.setC_UOM_ID(prod.getC_UOM_ID());
			decPOSLine_temp.setC_Tax_ID(C_Tax_ID);
			decPOSLine_temp.setPriceList((BigDecimal) miniTable.getValueAt(i, 3));
			decPOSLine_temp.setPriceEntered((BigDecimal) miniTable.getValueAt(i, 4));
			decPOSLine_temp.setQtyOrdered((BigDecimal) miniTable.getValueAt(i,2));
			decPOSLine_temp.setLineNetAmt(totalPrice);
			decPOSLine_temp.setLine(ii);
			if (M_AttributeSetInstance_ID > 0) {
				decPOSLine_temp
						.setM_AttributeSetInstance_ID(M_AttributeSetInstance_ID);
			}

			decPOSLine_temp.saveEx();
		}

		// Process
		StringBuilder SQLGetProcess = new StringBuilder();
		SQLGetProcess.append("SELECT Description ");
		SQLGetProcess.append(" FROM AD_Param ");
		SQLGetProcess.append(" WHERE Value = 'SemeruPos'");

		String process_id = DB.getSQLValueStringEx(null,SQLGetProcess.toString());
		if(process_id == null) {
			process_id = "0";
		}
		Integer ID = Integer.valueOf(process_id);
		// UAT//1000110;
		String trxName = Trx.createTrxName("generatepenjualan");
		MProcess proc = new MProcess(Env.getCtx(), ID, trxName);
		MPInstance instance = new MPInstance(proc, proc.getAD_Process_ID());
		ProcessInfo pi = new ProcessInfo("Generate Penjualan", ID);
		pi.setAD_PInstance_ID(instance.getAD_PInstance_ID());

		ArrayList<ProcessInfoParameter> list = new ArrayList<ProcessInfoParameter>();
		list.add(new ProcessInfoParameter("AD_Client_ID", decPos_temp.getAD_Client_ID(), null, null, null));
		list.add(new ProcessInfoParameter("AD_Org_ID", decPos_temp.getAD_Org_ID(), null, null, null));
		list.add(new ProcessInfoParameter("SM_SemeruTemp_ID", decPos_temp.getSM_SemeruTemp_ID(), null, null, null));
		list.add(new ProcessInfoParameter("DocAction", m_docAction, null, null,null));

		ProcessInfoParameter[] pars = new ProcessInfoParameter[list.size()];
		list.toArray(pars);
		pi.setParameter(pars);

		Trx trx = Trx.get(trxName, true);
		trx.commit();

		@SuppressWarnings("unused")
		boolean ada = new SM_POSProcess().startProcess(Env.getCtx(), pi,Trx.get(trxName, true));

		X_SM_SemeruTemp decPos_temp2nd = new X_SM_SemeruTemp(decPos_temp.getCtx(), decPos_temp.getSM_SemeruTemp_ID(), null);

		int C_OrderResult_ID = decPos_temp2nd.getC_Order_ID();

		if (C_OrderResult_ID <= 0) {

			FDialog.info(windowNo,null,"","Process Transaksi Gagal, Silakan Cek Kelengkapan Data Atau Hubungi Admin","Info Process POS");
			return;

		}

		// After Process Transaction
		C_OrderPrint_ID = C_OrderResult_ID;
		printButton.setEnabled(true);
		printSuratJalan.setEnabled(true);
		if (isThermalPrint) {
			printThermalButton.setEnabled(true);
		}
		pelangganBaru.setEnabled(false);

		if (isMultiLoc) {

			int M_InOut_ID = checkShipmentRelated(decPos_temp2nd
					.getC_Order_ID());
			if (M_InOut_ID > 0) {
				inout_ID = M_InOut_ID;
			}

			if (M_InOut_ID > 0) {

				MInOut InOut = new MInOut(ctx, M_InOut_ID, null);
				String DocStatus = InOut.getDocStatus();
				MInOutLine lines[] = InOut.getLines();

				if (DocStatus.equals(MInOut.DOCSTATUS_Drafted)) {

					for (int i = 0; i < miniTable.getRowCount(); i++) {

						KeyNamePair product = (KeyNamePair) miniTable.getValueAt(i, 1);
						KeyNamePair POSLoc = (KeyNamePair) miniTable.getValueAt(i, 7);
						int POSLineNo = (i + 1) * 10;

						int POSProd_ID = product.getKey();
						int POSLoc_ID = POSLoc.getKey();
						BigDecimal POSqQty = ((BigDecimal) miniTable.getValueAt(i, 2)).setScale(2);

						for (MInOutLine line : lines) {
							int shipProd_ID = line.getM_Product_ID();
							BigDecimal shipQty = line.getQtyEntered().setScale(
									2);
							int shipLineNo = line.getLine();

							if (POSProd_ID == shipProd_ID
									&& POSqQty.compareTo(shipQty) == 0
									&& POSLineNo == shipLineNo) {

								if (POSLoc_ID > 0) {
									line.setM_Locator_ID(POSLoc_ID);
									line.saveEx();
								}
							}

						}
					}
				}
			}

		}

		// From Here Must make Temp2nd

		int M_InOut_ID = checkShipmentRelated(decPos_temp2nd.getC_Order_ID());
		inout_ID = M_InOut_ID;
		int C_Invoice_ID = checkInvoiceRelated(decPos_temp2nd.getC_Order_ID());
		int C_DecorisPOS_ID = decPos_temp2nd.get_ValueAsInt("SM_SemeruPOS_ID");
		X_SM_SemeruPOS decPos = new X_SM_SemeruPOS(decPos_temp2nd.getCtx(),
				C_DecorisPOS_ID, decPos_temp2nd.get_TrxName());
		decPos.setC_Invoice_ID(C_Invoice_ID);
		decPos.setProcessed(true);
		noNota.setText(decPos.getDocumentNo());

//		Integer preOrder_ID = (Integer) noPreOrder.getValue();
//		if (preOrder_ID == null) {
//			preOrder_ID = 0;
//		}
//		if (preOrder_ID > 0) {
//			decPos.set_CustomColumn("C_Decoris_PreOrder_ID", preOrder_ID);
//		}
		decPos.saveEx();
		
		C_DecorisPOSPrint_ID = decPos.getSM_SemeruPOS_ID();

		lockData();
		processButton.setEnabled(false);

		if (isPick) {
			infoGeneratedDocument(decPos_temp2nd.getC_Order_ID(),
					form.getWindowNo());
		}

		if (!isPick) {

			try {

				Grid inputGrid = GridFactory.newGridLayout();
				Panel paraPanel = new Panel();
				Rows rows = null;
				Row row = null;

				final Datebox tglKirimInput = new Datebox();
				final Textbox penerimaInput = new Textbox();
				final Textbox alamatPenerimaInput = new Textbox();
				final Textbox noTlpInput = new Textbox();
				final Checkbox bedaAlamatCheck = new Checkbox();

				String title = "Korfirmasi Alamat";
				Label LabelBedaAlamat = new Label("Alamat Kirim Berbeda? : ");
				Label LabelTgl = new Label("Tgl. Kirim : ");
				Label LabelPenerima = new Label("Penerima : ");
				Label LabelAlamat = new Label("Alamat : ");
				Label LabelNoTlp = new Label("No. Tlp : ");

				// Date
				Calendar cal = Calendar.getInstance();
				cal.setTime(new Date());
				cal.set(Calendar.HOUR_OF_DAY, 0);
				cal.set(Calendar.MINUTE, 0);
				cal.set(Calendar.SECOND, 0);
				cal.set(Calendar.MILLISECOND, 0);

				final Window w = new Window();
				w.setTitle(title);

				Borderlayout mainbBorder = new Borderlayout();
				w.appendChild(mainbBorder);
				w.setWidth("350px");
				w.setHeight("275px");
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
				//inputGrid.setHflex("min");

				rows = inputGrid.newRows();

				row = rows.newRow();
				row.appendCellChild(LabelBedaAlamat.rightAlign());
				row.appendCellChild(bedaAlamatCheck);
				//bedaAlamatCheck.setHflex("true");
				bedaAlamatCheck.addEventListener(Events.ON_CHECK,
						new EventListener<Event>() {

							@Override
							public void onEvent(Event ev) throws Exception {

								boolean isDiffAlamat = bedaAlamatCheck
										.isChecked();

								if (!isDiffAlamat) {
									tglKirimInput.setReadonly(true);
									penerimaInput.setReadonly(true);
									alamatPenerimaInput.setReadonly(true);
									noTlpInput.setReadonly(true);
								} else if (isDiffAlamat) {
									tglKirimInput.setReadonly(false);
									penerimaInput.setReadonly(false);
									alamatPenerimaInput.setReadonly(false);
									noTlpInput.setReadonly(false);
								}
							}
						});

				row = rows.newRow();
				row.appendCellChild(LabelTgl.rightAlign());
				row.appendCellChild(tglKirimInput, 1);
				//tglKirimInput.setHflex("true");
				tglKirimInput.setReadonly(true);

				tglKirimInput.setFormat("dd/MM/yyyy");

				cal.add(Calendar.DATE, 1);
				tglKirimInput.setValue(cal.getTime());

				row = rows.newRow();
				row.appendCellChild(LabelPenerima.rightAlign());
				row.appendCellChild(penerimaInput);
				//penerimaInput.setHflex("true");
				penerimaInput.setReadonly(true);

				row = rows.newRow();
				row.appendCellChild(LabelAlamat.rightAlign());
				row.appendCellChild(alamatPenerimaInput);
				//alamatPenerimaInput.setHflex("true");
				alamatPenerimaInput.setRows(4);
				alamatPenerimaInput.setReadonly(true);

				row = rows.newRow();
				row.appendCellChild(LabelNoTlp.rightAlign());
				row.appendCellChild(noTlpInput);
				//noTlpInput.setHflex("true");
				noTlpInput.setReadonly(true);

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
									FDialog.info(windowNo, null, "","Presales Berhasil Di Proses","Info");
									w.dispose();
								} else if (event.getTarget() == panel.getButton(ConfirmPanel.A_OK)) {
									Timestamp tglKirim = null;
									String penerima = "";
									String alamatPenerima = "";
									String noTlp = "";
									boolean isBedaAlamat = bedaAlamatCheck.isChecked();

									String msgValidation = "";

									penerima = penerimaInput.getValue();
									alamatPenerima = penerimaInput.getValue();
									noTlp = noTlpInput.getValue();

									int M_InOut_ID = inout_ID;

									if (M_InOut_ID <= 0) {
										w.onClose();
										w.dispose();
									}

									if (tglKirimInput.getValue() != null) {
										Date tgl = tglKirimInput.getValue();
										SimpleDateFormat df = new SimpleDateFormat(
												"yyyy-MM-dd 00:00:00");
										String tglTostr = df.format(tgl);
										Timestamp dateShip = Timestamp
												.valueOf(tglTostr);
										tglKirim = dateShip;
									}

									MInOut ino = new MInOut(ctx, M_InOut_ID,
											null);

									if (tglKirim == null) {

										FDialog.info(
												windowNo,
												null,
												"",
												"Tanggal Kirim Belum Ditentukan",
												"Info");
										return;

									}

									ino.setMovementDate(tglKirim);

									// validation
									if (isBedaAlamat) {

										if (penerima == null
												|| penerima.isEmpty()
												|| penerima == "") {
											msgValidation = msgValidation
													+ "\n"
													+ "Nama Penerima Belum Di Isi";
										}

										if (alamatPenerima == null
												|| alamatPenerima.isEmpty()
												|| alamatPenerima == "") {
											msgValidation = msgValidation
													+ "\n"
													+ "Alamat Pengiriman Belum Di Isi";
										}

										if (noTlp == null || noTlp.isEmpty()
												|| noTlp == "") {
											msgValidation = msgValidation
													+ "\n"
													+ "No Tlp Penerima Belum Di Isi";
										}

										if (msgValidation != "") {
											FDialog.info(windowNo, null, "",
													msgValidation, "Info");
											return;
										}

										ino.set_CustomColumn("Penerima",
												penerima);
										ino.set_CustomColumn("AlamatPenerima",
												alamatPenerima);
										ino.set_CustomColumn("NoTlpPenerima",
												noTlp);

									}
									ino.saveEx();

									infoGeneratedDocument(ino.getC_Order_ID(),
											form.getWindowNo());

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

			} catch (Exception err) {

				log.log(Level.SEVERE, this.getClass().getCanonicalName()
						+ ".valueChange - ERROR: " + err.getMessage(), err);
			}
			// end confirmation alamat
			ClearTableLine(AD_Client_ID, decPos_temp2nd);

		}

	}

	private void ClearTableLine(int AD_Client_ID,
			X_SM_SemeruTemp SM_SemeruTemp) {

		StringBuilder SQLDelete = new StringBuilder();
		SQLDelete.append("SELECT SM_SemeruLineTemp_ID ");
		SQLDelete.append(" FROM SM_SemeruLineTemp ");
		SQLDelete.append(" WHERE AD_Client_ID = ? ");
		SQLDelete.append(" AND SM_SemeruTemp_ID = ? ");

		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			pstmt = DB.prepareStatement(SQLDelete.toString(), null);
			pstmt.setInt(1, AD_Client_ID);
			pstmt.setInt(2, SM_SemeruTemp.getSM_SemeruTemp_ID());
			rs = pstmt.executeQuery();
			while (rs.next()) {

				X_SM_SemeruLineTemp line = new X_SM_SemeruLineTemp(ctx,
						rs.getInt(1), SM_SemeruTemp.get_TrxName());
				line.delete(true, SM_SemeruTemp.get_TrxName());
			}

		} catch (SQLException e) {
			log.log(Level.SEVERE, SQLDelete.toString(), e);
		} finally {
			DB.close(rs, pstmt);
			rs = null;
			pstmt = null;
		}

		SM_SemeruTemp.delete(true, SM_SemeruTemp.get_TrxName());

	}

	private void delete() {

		int rowCount = 0;
		for (int i = 0; i < salesTable.getRowCount(); i++) {
			boolean isSelect = (boolean) salesTable.getValueAt(i, 0);

			if (isSelect) {
				rowCount++;
			}

		}
		for (int i = 0; i < rowCount; i++) {

			for (int j = 0; j < salesTable.getRowCount(); j++) {
				boolean isSelect = (boolean) salesTable.getValueAt(j, 0);
				if (isSelect) {
					deletedata(j);
					// salesTable.clear();

					Vector<String> columnNames = getOISColumnNames();

					ListModelTable modelP = new ListModelTable(data);

					modelP.addTableModelListener(this);

					salesTable.setData(modelP, columnNames);

					configureMiniTable(salesTable);

					if (salesTable.getRowCount() == 0) {
						totalBayarTextBox.setText("0.00");
						kembalianTextBox.setText("0.00");
						kembaliTextBox.setText("0.00");
						paymentTunai.setText("0.00");
						totalBayarTunaiTextBox.setText("0.00");
						diskonTextBox.setText("0.00");
						totalBrutoTextBox.setText("0.00");
						totalBelanjaTextBox.setText("0.00");
						DPPTextBox.setText("0.00");
						PPNTextBox.setText("0.00");
						TotalHeaderTextBox.setText("0.00");
						totalPrices = Env.ZERO;
						totalDiskons = Env.ZERO;
						return;
					}

					// recalculate component
					reCalculate(salesTable);
					calculate();
				}
			}
		}
	}

	private void imei() {

		// int listSize = IMEIList.getItemCount();
		// if (listSize <= 1)
		// return;
		//
		// String serno = IMEIList.getSelectedItem().getId();
		//
		// String sql = "SELECT M_AttributeSetInstance_ID"
		// + " FROM M_AttributeSetInstance " + " WHERE AD_Client_ID = ?"
		// + " AND Serno = '" + serno + "'";
		//
		// Integer M_AttributeSetInstance_ID = DB.getSQLValueEx(null,
		// sql,AD_Client_ID);
		// MAttributeSetInstance imei = new
		// MAttributeSetInstance(ctx,M_AttributeSetInstance_ID, null);
		//
		// for (int i = 0; i < salesTable.getRowCount(); i++) {
		//
		// String imeiProd = imei.getSerNo();
		// String imeiTab = (String) salesTable.getValueAt(i, 8);
		// if (imeiProd.equalsIgnoreCase(imeiTab)) {
		//
		// FDialog.warn(form.getWindowNo(), null,
		// "","Product Dengan IMEI Tersebut Sudah Terinput","Peringatan");
		// IMEIList.setValue(null);
		// return;
		//
		// }
		// ;
		// }
		//
		// if (imeiIndex != null) {
		// KeyNamePair product = (KeyNamePair)
		// salesTable.getValueAt(rowIndex,1);
		//
		// String sqlProduct =
		// "SELECT M_Product_ID FROM M_Product WHERE AD_Client_ID = ? AND name = '"+
		// product.toString() + "'";
		// int M_Product_ID =
		// DB.getSQLValue(ctx.toString(),sqlProduct.toString(), AD_Client_ID);
		//
		// String sqlImei =
		// "SELECT M_Product_ID FROM M_StorageOnHand WHERE AD_Client_ID = ? AND M_AttributeSetInstance_ID = ?";
		// int M_ProductIMEI_ID = DB.getSQLValueEx(ctx.toString(), sqlImei,new
		// Object[] { AD_Client_ID, M_AttributeSetInstance_ID });
		//
		// if (M_Product_ID != M_ProductIMEI_ID) {
		//
		// FDialog.warn(form.getWindowNo(), null,
		// "","IMEI yang anda inputkan tidak sesuai dengan produk","Peringatan");
		// IMEIList.setValue(null);
		// return;
		//
		// }
		//
		// int lastRow = salesTable.getRowCount() - 1;
		// salesTable.setValueAt(imei.getSerNo(), lastRow, 8);
		// imeiIndex = null;
		//
		// } else {
		//
		// int M_ProductIMEI_ID;
		// int M_LocatorProd_ID;
		// int M_LocatorPOS_ID = getIDFromComboBox(Lokasi,MLocator.Table_Name,
		// MLocator.COLUMNNAME_Value);
		//
		// MLocator loc = new MLocator(ctx, M_LocatorPOS_ID, null);
		//
		// String sqlImei =
		// "SELECT M_Product_ID FROM M_StorageOnHand WHERE M_AttributeSetInstance_ID = ? AND QtyOnHand > 0";
		// M_ProductIMEI_ID = DB.getSQLValueEx(ctx.toString(), sqlImei,new
		// Object[] { M_AttributeSetInstance_ID });
		//
		// if (M_ProductIMEI_ID < 0) {
		// FDialog.warn(form.getWindowNo(), null,
		// "","Produk dengan IMEI yang anda pilih tidak ada di lokasi " +
		// loc.getValue(), "Peringatan");
		// IMEIList.setValue(null);
		// return;
		// }
		//
		// String sqlLocator =
		// "SELECT M_Locator_ID FROM M_StorageOnHand WHERE M_AttributeSetInstance_ID = ? AND QtyOnHand > 0";
		// M_LocatorProd_ID = DB.getSQLValueEx(ctx.toString(), sqlLocator,new
		// Object[] { M_AttributeSetInstance_ID });
		//
		// if (M_LocatorProd_ID != M_LocatorPOS_ID) {
		//
		// FDialog.warn(form.getWindowNo(), null,
		// "","Produk dengan IMEI yang anda pilih tidak ada di lokasi "+
		// loc.getValue(), "Peringatan");
		// IMEIList.setValue(null);
		// return;
		//
		// }
		//
		// salesTable.clear();
		//
		// Vector<Vector<Object>> data =
		// getProductData(M_ProductIMEI_ID,M_Pricelist_ID, bpartnerId,
		// M_LocatorPOS_ID,M_AttributeSetInstance_ID, salesTable);
		// Vector<String> columnNames = getOISColumnNames();
		//
		// salesTable.clear();
		//
		// // Set Model
		// ListModelTable modelP = new ListModelTable(data);
		// modelP.addTableModelListener(this);
		// salesTable.setData(modelP, columnNames);
		// configureMiniTable(salesTable);
		//
		// reCalculate(salesTable);
		// calculate();
		// IMEIList.setValue(null);
		// imeiIndex = null;
		// }

		int listSize = IMEIList.getItemCount();
		if (listSize <= 1)
			return;

		String serno = IMEIList.getSelectedItem().getId();

		String sql = "SELECT M_AttributeSetInstance_ID"
				+ " FROM M_AttributeSetInstance " + " WHERE AD_Client_ID = ?"
				+ " AND Serno = '" + serno + "'";

		Integer M_AttributeSetInstance_ID = DB.getSQLValueEx(null, sql,
				AD_Client_ID);

		if (M_AttributeSetInstance_ID <= 0) {

			FDialog.warn(form.getWindowNo(), null, "",
					"IMEI Yang Anda Inputkan Bermasalah, Silahkan Cek IMEI",
					"Peringatan");
			IMEIList.setValue(null);
			return;

		}

		MAttributeSetInstance imei = new MAttributeSetInstance(ctx,
				M_AttributeSetInstance_ID, null);

		int countSelected = 0;
		int rowidx = 0;

		for (int i = 0; i < salesTable.getRowCount(); i++) {
			boolean isSelect = (boolean) salesTable.getValueAt(i, 0);
			if (isSelect) {
				countSelected++;
			}
		}

		for (int i = 0; i < salesTable.getRowCount(); i++) {

			String imeiProd = imei.getSerNo();
			String imeiTab = (String) salesTable.getValueAt(i, 8);
			boolean isSelect = (boolean) salesTable.getValueAt(i, 0);

			if (countSelected > 1) {
				FDialog.warn(form.getWindowNo(), null, "",
						"Tidak Boleh Memilih 2 Detail Untuk Penggantian IMEI",
						"Peringatan");
				IMEIList.setValue(null);
				return;
			} else if (countSelected == 1) {

				if (isSelect) {
					if (imeiProd.equalsIgnoreCase(imeiTab)) {
						FDialog.warn(form.getWindowNo(), null, "",
								"Product Dengan IMEI Tersebut Sudah Terinput",
								"Peringatan");
						IMEIList.setValue(null);
						return;
					} else {
						int M_Loc_ID = getIDFromComboBox(Lokasi,
								MLocator.Table_Name, MLocator.COLUMNNAME_Value);
						MLocator loc = new MLocator(ctx, M_Loc_ID, null);
						rowidx = i;

						KeyNamePair LKP = (KeyNamePair) salesTable.getValueAt(
								i, 7);
						String locTble = LKP.getName();

						if (loc.getValue().toUpperCase()
								.equals(locTble.toUpperCase())) {
							salesTable.setValueAt(imei.getSerNo(), i, 8);
						} else {
							FDialog.warn(
									form.getWindowNo(),
									null,
									"",
									"Locator Detail Tidak Boleh Berbeda Dengan Locator Product Untuk Penggantian Nomor IMEI",
									"Peringatan");
							IMEIList.setValue(null);
							return;
						}

					}
				}

			} else if (countSelected == 0) {

				FDialog.warn(form.getWindowNo(), null, "",
						"Silahkan Pilih Product Detail Untuk Penggantian IMEI",
						"Peringatan");
				IMEIList.setValue(null);
				return;

			}

		}

		if (rowidx > 0) {
			KeyNamePair product = (KeyNamePair) salesTable
					.getValueAt(rowidx, 1);

			String sqlProduct = "SELECT M_Product_ID FROM M_Product WHERE AD_Client_ID = ? AND name = '"
					+ product.toString() + "'";
			int M_Product_ID = DB.getSQLValue(ctx.toString(),
					sqlProduct.toString(), AD_Client_ID);

			String sqlImei = "SELECT M_Product_ID FROM M_StorageOnHand WHERE AD_Client_ID = ? AND M_AttributeSetInstance_ID = ?";
			int M_ProductIMEI_ID = DB.getSQLValueEx(ctx.toString(), sqlImei,
					new Object[] { AD_Client_ID, M_AttributeSetInstance_ID });

			if (M_Product_ID != M_ProductIMEI_ID) {

				FDialog.warn(form.getWindowNo(), null, "",
						"IMEI yang anda inputkan tidak sesuai dengan produk",
						"Peringatan");
				IMEIList.setValue(null);
				return;

			}
		}

	}

	private void priceList() {

		Integer M_PL_ID = getIDFromComboBox(DaftarHargaSearch,
				MPriceList.Table_Name, MPriceList.COLUMNNAME_Name);

		if (M_PL_ID <= 0) {
			return;
		}

		StringBuilder sqlPriceList = new StringBuilder();
		sqlPriceList
				.append("SELECT M_PriceList_ID FROM M_PriceList WHERE AD_Client_ID =? AND IsActive = 'Y' AND isSoPriceList= 'Y'"
						+ " AND  M_PriceList_ID IN "
						+ "(SELECT M_PriceList_ID FROM C_POS WHERE  AD_Client_ID = ? AND CreatedByPOS_ID = ? AND M_PriceList_ID = ?)");

		M_Pricelist_ID = DB.getSQLValueEx(ctx.toString(),
				sqlPriceList.toString(), new Object[] { AD_Client_ID,
						AD_Client_ID, CreatedByPOS_ID, M_PL_ID });

	}

	private void printNota() {

		String trxName = Trx.createTrxName("posprint");
		// String url = "D:\\SourceCode\\SIM-Base\\reports\\";
		String url = "/home/idempiere/idempiere.gtk.linux.x86_64/idempiere-server/reports/";
		MProcess proc = new MProcess(Env.getCtx(), 1000074, trxName);
		MPInstance instance = new MPInstance(proc, proc.getAD_Process_ID());
		ProcessInfo pi = new ProcessInfo("Print Nota Penjualan", 1000074);
		pi.setAD_PInstance_ID(instance.getAD_PInstance_ID());

		String namatoko = "";
		String phone = "";
		String phone2 = "";
		String fax = "";
		String email = "";
		String alamat = "";
		String kota = "";

		if (CreatedByPOS_ID > 0) {

			String getNamaToko = "SELECT NamaToko FROM C_POS WHERE  AD_Client_ID = ? AND CreatedByPOS_ID = ?";
			namatoko = DB.getSQLValueStringEx(ctx.toString(), getNamaToko,
					new Object[] { AD_Client_ID, CreatedByPOS_ID });

			String getPhone = "SELECT TeleponToko FROM C_POS WHERE  AD_Client_ID = ? AND CreatedByPOS_ID = ?";
			phone = DB.getSQLValueStringEx(ctx.toString(), getPhone,
					new Object[] { AD_Client_ID, CreatedByPOS_ID });

			String getPhone2 = "SELECT TeleponToko2 FROM C_POS WHERE  AD_Client_ID = ? AND CreatedByPOS_ID = ?";
			phone2 = DB.getSQLValueStringEx(ctx.toString(), getPhone2,
					new Object[] { AD_Client_ID, CreatedByPOS_ID });

			String getFax = "SELECT Fax FROM C_POS WHERE  AD_Client_ID = ? AND CreatedByPOS_ID = ?";
			fax = DB.getSQLValueStringEx(ctx.toString(), getFax, new Object[] {
					AD_Client_ID, CreatedByPOS_ID });

			String getEmail = "SELECT EMail FROM C_POS WHERE  AD_Client_ID = ? AND CreatedByPOS_ID = ?";
			email = DB.getSQLValueStringEx(ctx.toString(), getEmail,
					new Object[] { AD_Client_ID, CreatedByPOS_ID });

			String getAlamat = "SELECT AlamatToko FROM C_POS WHERE  AD_Client_ID = ? AND CreatedByPOS_ID = ?";
			alamat = DB.getSQLValueStringEx(ctx.toString(), getAlamat,
					new Object[] { AD_Client_ID, CreatedByPOS_ID });

			String getKota = "SELECT Kota FROM C_POS WHERE  AD_Client_ID = ? AND CreatedByPOS_ID = ?";
			kota = DB.getSQLValueStringEx(ctx.toString(), getKota,
					new Object[] { AD_Client_ID, CreatedByPOS_ID });

		}

		ArrayList<ProcessInfoParameter> list = new ArrayList<ProcessInfoParameter>();
		list.add(new ProcessInfoParameter("C_Order_ID", C_OrderPrint_ID, null,
				null, null));
		list.add(new ProcessInfoParameter("url_path", url, null, null, null));
		list.add(new ProcessInfoParameter("NamaToko", namatoko, null, null,
				null));
		list.add(new ProcessInfoParameter("TeleponToko", phone, null, null,
				null));
		list.add(new ProcessInfoParameter("TeleponToko2", phone2, null, null,
				null));
		list.add(new ProcessInfoParameter("Fax", fax, null, null, null));
		list.add(new ProcessInfoParameter("EMail", email, null, null, null));
		list.add(new ProcessInfoParameter("AlamatToko", alamat, null, null,
				null));
		list.add(new ProcessInfoParameter("Kota", kota, null, null, null));

		ProcessInfoParameter[] pars = new ProcessInfoParameter[list.size()];
		list.toArray(pars);
		pi.setParameter(pars);

		Trx trx = Trx.get(trxName, true);
		trx.commit();

		ProcessUtil.startJavaProcess(Env.getCtx(), pi, Trx.get(trxName, true));

		StringBuilder sqlIsPrint = new StringBuilder();
		sqlIsPrint.append("UPDATE C_DecorisPOS SET IsPrinted = 'Y' ");
		sqlIsPrint.append("WHERE AD_Client_ID = ? ");
		sqlIsPrint.append("AND C_DecorisPOS_ID = ?");

		DB.executeUpdateEx(sqlIsPrint.toString(), new Object[] { AD_Client_ID,
				C_DecorisPOSPrint_ID }, null);

	}

	private void printThermal() {

		String ket1 = "";
		String ket2 = "";
		String namatoko = "";
		String phone = "";
		String phone2 = "";
		String fax = "";
		String email = "";
		String alamat = "";
		String kota = "";

		if (CreatedByPOS_ID > 0) {

			String sqlket1 = "SELECT ParameterThermalStruk1 FROM C_POS WHERE  AD_Client_ID = ? AND CreatedByPOS_ID = ?";
			ket1 = DB.getSQLValueStringEx(ctx.toString(), sqlket1,
					new Object[] { AD_Client_ID, CreatedByPOS_ID });

			String sqlket2 = "SELECT ParameterThermalStruk2 FROM C_POS WHERE  AD_Client_ID = ? AND CreatedByPOS_ID = ?";
			ket2 = DB.getSQLValueStringEx(ctx.toString(), sqlket2,
					new Object[] { AD_Client_ID, CreatedByPOS_ID });

			String getNamaToko = "SELECT NamaToko FROM C_POS WHERE  AD_Client_ID = ? AND CreatedByPOS_ID = ?";
			namatoko = DB.getSQLValueStringEx(ctx.toString(), getNamaToko,
					new Object[] { AD_Client_ID, CreatedByPOS_ID });

			String getPhone = "SELECT TeleponToko FROM C_POS WHERE  AD_Client_ID = ? AND CreatedByPOS_ID = ?";
			phone = DB.getSQLValueStringEx(ctx.toString(), getPhone,
					new Object[] { AD_Client_ID, CreatedByPOS_ID });

			String getPhone2 = "SELECT TeleponToko2 FROM C_POS WHERE  AD_Client_ID = ? AND CreatedByPOS_ID = ?";
			phone2 = DB.getSQLValueStringEx(ctx.toString(), getPhone2,
					new Object[] { AD_Client_ID, CreatedByPOS_ID });

			String getFax = "SELECT Fax FROM C_POS WHERE  AD_Client_ID = ? AND CreatedByPOS_ID = ?";
			fax = DB.getSQLValueStringEx(ctx.toString(), getFax, new Object[] {
					AD_Client_ID, CreatedByPOS_ID });

			String getEmail = "SELECT EMail FROM C_POS WHERE  AD_Client_ID = ? AND CreatedByPOS_ID = ?";
			email = DB.getSQLValueStringEx(ctx.toString(), getEmail,
					new Object[] { AD_Client_ID, CreatedByPOS_ID });

			String getAlamat = "SELECT AlamatToko FROM C_POS WHERE  AD_Client_ID = ? AND CreatedByPOS_ID = ?";
			alamat = DB.getSQLValueStringEx(ctx.toString(), getAlamat,
					new Object[] { AD_Client_ID, CreatedByPOS_ID });

			String getKota = "SELECT Kota FROM C_POS WHERE  AD_Client_ID = ? AND CreatedByPOS_ID = ?";
			kota = DB.getSQLValueStringEx(ctx.toString(), getKota,
					new Object[] { AD_Client_ID, CreatedByPOS_ID });

		}

		String trxName = Trx.createTrxName("posprintthermal");
		MProcess proc = new MProcess(Env.getCtx(), 1000084, trxName);
		MPInstance instance = new MPInstance(proc, proc.getAD_Process_ID());
		ProcessInfo pi = new ProcessInfo("Print Struk Penjualan", 1000084);
		pi.setAD_PInstance_ID(instance.getAD_PInstance_ID());

		ArrayList<ProcessInfoParameter> list = new ArrayList<ProcessInfoParameter>();
		list.add(new ProcessInfoParameter("C_Order_ID", C_OrderPrint_ID, null,
				null, null));
		list.add(new ProcessInfoParameter("ket1", ket1, null, null, null));
		list.add(new ProcessInfoParameter("ket2", ket2, null, null, null));
		list.add(new ProcessInfoParameter("NamaToko", namatoko, null, null,
				null));
		list.add(new ProcessInfoParameter("TeleponToko", phone, null, null,
				null));
		list.add(new ProcessInfoParameter("TeleponToko2", phone2, null, null,
				null));
		list.add(new ProcessInfoParameter("Fax", fax, null, null, null));
		list.add(new ProcessInfoParameter("EMail", email, null, null, null));
		list.add(new ProcessInfoParameter("AlamatToko", alamat, null, null,
				null));
		list.add(new ProcessInfoParameter("Kota", kota, null, null, null));

		ProcessInfoParameter[] pars = new ProcessInfoParameter[list.size()];
		list.toArray(pars);
		pi.setParameter(pars);

		Trx trx = Trx.get(trxName, true);
		trx.commit();

		ProcessUtil.startJavaProcess(Env.getCtx(), pi, Trx.get(trxName, true));

		StringBuilder sqlIsPrint = new StringBuilder();
		sqlIsPrint.append("UPDATE C_DecorisPOS SET IsPrinted = 'Y' ");
		sqlIsPrint.append("WHERE AD_Client_ID = ? ");
		sqlIsPrint.append("AND C_DecorisPOS_ID = ?");

		DB.executeUpdateEx(sqlIsPrint.toString(), new Object[] { AD_Client_ID,
				C_DecorisPOSPrint_ID }, null);

	}

	private void printSuratJalan() {

		String trxName = Trx.createTrxName("PrintSJ");
		// String url = "D:\\SourceCode\\iDempiereBase\\reports\\";
		String url = "/home/idempiere/idempiere.gtk.linux.x86_64/idempiere-server/reports/";
		MProcess proc = new MProcess(Env.getCtx(), 330053, trxName);
		MPInstance instance = new MPInstance(proc, proc.getAD_Process_ID());
		ProcessInfo pi = new ProcessInfo("Print SJ", 330053);
		pi.setAD_PInstance_ID(instance.getAD_PInstance_ID());

		String namatoko = "";
		String phone = "";
		String phone2 = "";
		String fax = "";
		String email = "";
		String alamat = "";
		String kota = "";
		int M_InOutPrint_ID = checkShipmentRelated(C_OrderPrint_ID);
		if (M_InOutPrint_ID <= 0)
			return;

		if (CreatedByPOS_ID > 0) {

			String getNamaToko = "SELECT NamaToko FROM C_POS WHERE  AD_Client_ID = ? AND CreatedByPOS_ID = ?";
			namatoko = DB.getSQLValueStringEx(ctx.toString(), getNamaToko,
					new Object[] { AD_Client_ID, CreatedByPOS_ID });

			String getPhone = "SELECT TeleponToko FROM C_POS WHERE  AD_Client_ID = ? AND CreatedByPOS_ID = ?";
			phone = DB.getSQLValueStringEx(ctx.toString(), getPhone,
					new Object[] { AD_Client_ID, CreatedByPOS_ID });

			String getPhone2 = "SELECT TeleponToko2 FROM C_POS WHERE  AD_Client_ID = ? AND CreatedByPOS_ID = ?";
			phone2 = DB.getSQLValueStringEx(ctx.toString(), getPhone2,
					new Object[] { AD_Client_ID, CreatedByPOS_ID });

			String getFax = "SELECT Fax FROM C_POS WHERE  AD_Client_ID = ? AND CreatedByPOS_ID = ?";
			fax = DB.getSQLValueStringEx(ctx.toString(), getFax, new Object[] {
					AD_Client_ID, CreatedByPOS_ID });

			String getEmail = "SELECT EMail FROM C_POS WHERE  AD_Client_ID = ? AND CreatedByPOS_ID = ?";
			email = DB.getSQLValueStringEx(ctx.toString(), getEmail,
					new Object[] { AD_Client_ID, CreatedByPOS_ID });

			String getAlamat = "SELECT AlamatToko FROM C_POS WHERE  AD_Client_ID = ? AND CreatedByPOS_ID = ?";
			alamat = DB.getSQLValueStringEx(ctx.toString(), getAlamat,
					new Object[] { AD_Client_ID, CreatedByPOS_ID });

			String getKota = "SELECT Kota FROM C_POS WHERE  AD_Client_ID = ? AND CreatedByPOS_ID = ?";
			kota = DB.getSQLValueStringEx(ctx.toString(), getKota,
					new Object[] { AD_Client_ID, CreatedByPOS_ID });

		}

		ArrayList<ProcessInfoParameter> list = new ArrayList<ProcessInfoParameter>();
		list.add(new ProcessInfoParameter("M_InOut_ID", M_InOutPrint_ID, null,
				null, null));
		list.add(new ProcessInfoParameter("url_path", url, null, null, null));
		list.add(new ProcessInfoParameter("NamaToko", namatoko, null, null,
				null));
		list.add(new ProcessInfoParameter("TeleponToko", phone, null, null,
				null));
		list.add(new ProcessInfoParameter("TeleponToko2", phone2, null, null,
				null));
		list.add(new ProcessInfoParameter("Fax", fax, null, null, null));
		list.add(new ProcessInfoParameter("EMail", email, null, null, null));
		list.add(new ProcessInfoParameter("AlamatToko", alamat, null, null,
				null));
		list.add(new ProcessInfoParameter("Kota", kota, null, null, null));

		ProcessInfoParameter[] pars = new ProcessInfoParameter[list.size()];
		list.toArray(pars);
		pi.setParameter(pars);

		Trx trx = Trx.get(trxName, true);
		trx.commit();

		ProcessUtil.startJavaProcess(Env.getCtx(), pi, Trx.get(trxName, true));

		StringBuilder sqlIsPrint = new StringBuilder();
		sqlIsPrint.append("UPDATE C_DecorisPOS SET IsPrintedsj = 'Y' ");
		sqlIsPrint.append("WHERE AD_Client_ID = ? ");
		sqlIsPrint.append("AND C_DecorisPOS_ID = ?");

		DB.executeUpdateEx(sqlIsPrint.toString(), new Object[] { AD_Client_ID,
				C_DecorisPOSPrint_ID }, null);

	}

	private boolean paymentAlgoritm(BigDecimal FirstPay, BigDecimal Secondpay,
			BigDecimal ThirdPay, BigDecimal TrigerPay) {

		Double tBlanja = 0.00;
		boolean isPass = true;

		if (totalBelanjaTextBox.getText().isEmpty()) {
			tBlanja = 0.00;
		} else if (!totalBelanjaTextBox.getText().isEmpty()) {
			if (AD_Language.toUpperCase().equals("EN_US")) {
				tBlanja = Double.valueOf(totalBelanjaTextBox.getText()
						.replaceAll(",", ""));
			} else if (AD_Language.toUpperCase().equals("IN_ID")) {
				String dblanja = totalBelanjaTextBox.getText()
						.replaceAll("\\.", "").replaceAll(",", ".");
				tBlanja = Double.valueOf(dblanja);
			}
		}

		BigDecimal totalBelanja = BigDecimal.valueOf(tBlanja);
		BigDecimal totalByar = FirstPay.add(Secondpay).add(ThirdPay);

		BigDecimal kurangBayar = totalBelanja.subtract(totalByar);

		if (TrigerPay.compareTo(kurangBayar) > 0) {

			isPass = false;

		}

		return isPass;

	}

//	private void calculateDPPExistingPresales() {
//
//		BigDecimal totalDPP = Env.ZERO;
//		BigDecimal totalPPN = Env.ZERO;
//		for (int i = 0; i < salesTable.getRowCount(); i++) {
//
//			BigDecimal DPP = Env.ZERO;
//			BigDecimal PPN = Env.ZERO;
//
//			KeyNamePair kp = (KeyNamePair) salesTable.getValueAt(i, 1);
//
//			int M_Product_ID = kp.getKey();
//
//			MProduct pr = new MProduct(ctx, M_Product_ID, null);
//
//			StringBuilder SQLTaxRate = new StringBuilder();
//			SQLTaxRate.append("SELECT Rate ");
//			SQLTaxRate.append("FROM C_Tax ");
//			SQLTaxRate.append("WHERE AD_Client_ID = ? ");
//			SQLTaxRate.append("AND C_TaxCategory_ID = ? ");
//
//			BigDecimal taxRate = DB.getSQLValueBDEx(null,
//					SQLTaxRate.toString(),
//					new Object[] { AD_Client_ID, pr.getC_TaxCategory_ID() });
//
//			if (taxRate.compareTo(Env.ZERO) > 0) {
//
//				BigDecimal ttlHarga = (BigDecimal) salesTable.getValueAt(i, 6);
//				BigDecimal divider = new BigDecimal("1.1");
//				DPP = ttlHarga.divide(divider, 2, RoundingMode.HALF_UP);
//				PPN = ttlHarga.subtract(DPP);
//
//			}
//
//			totalDPP = totalDPP.add(DPP);
//			totalPPN = totalPPN.add(PPN);
//		}
//
//		DPPTextBox.setText(format.format(totalDPP));
//		PPNTextBox.setText(format.format(totalPPN));
//
//	}

	private void initialDisplay() {

		// Initial dispay field
		noNota.setReadonly(true);
		noNota.setEnabled(false);

//		noPresales.setReadWrite(false);
		PelangganSearch.setReadWrite(false);
//		noPreOrder.setReadWrite(false);
		salesSearch.setReadWrite(false);
		//DaftarHargaSearch.setEnabled(false);
		GudangSearch.setEnabled(false);
		Lokasi.setEnabled(false);
		IMEIList.setEnabled(false);
		PickUp.setEnabled(false);
		paymentTunai.setReadonly(true);
		paymentBank.setReadonly(true);
		paymentLeasing.setReadonly(true);
		paymentHutang.setReadonly(true);
		leasingProviderSearch.setReadWrite(false);
		bankAccountSearch.setReadWrite(false);
		keteranganTextBox.setReadonly(true);
		processButton.setEnabled(false);
		printThermalButton.setEnabled(false);
		printSuratJalan.setEnabled(false);
		printButton.setEnabled(false);
		IsDebit.setChecked(false);
		noNotaOld.setReadWrite(false);

		TokoSearch.setReadWrite(false);

		orgSearch.getComponent().setReadonly(true);
		orgSearch.setReadWrite(false);
		pelangganBaru.setEnabled(false);
		tanggalSearch.setEnabled(false);
		deleteItem.setEnabled(false);

		//DaftarHargaSearch.setReadonly(true);
		//GudangSearch.setReadonly(true);
		Lokasi.setReadonly(true);
		ProductSearch.setReadWrite(false);
		IMEIList.setReadonly(true);

		payruleBoxTunai.setEnabled(false);
		payruleBoxBank.setEnabled(false);
		payruleBoxLeasing.setEnabled(false);
		payruleBoxHutang.setEnabled(false);

		totalBrutoTextBox.setEnabled(true);
		totalBrutoTextBox.setReadonly(true);

		totalBayarTextBox.setReadonly(true);
		totalBayarTextBox.setEnabled(true);

		kembaliTextBox.setReadonly(true);
		kembaliTextBox.setEnabled(true);

		TotalHeaderTextBox.setReadonly(true);
		TotalHeaderTextBox.setEnabled(true);

		totalBelanjaTextBox.setReadonly(true);
		totalBelanjaTextBox.setEnabled(true);

		totalBayarTunaiTextBox.setReadonly(true);
		totalBayarTunaiTextBox.setEnabled(true);

		diskonTextBox.setReadonly(true);
		diskonTextBox.setEnabled(true);

		kembalianTextBox.setReadonly(true);
		kembalianTextBox.setEnabled(true);

		DPPTextBox.setReadonly(true);
		DPPTextBox.setEnabled(true);

		PPNTextBox.setReadonly(true);
		PPNTextBox.setEnabled(true);

		PickUp.setSelected(true);

	}

	public String validation() {
		String msg = "";

		BigDecimal ptunai = paymentTunai.getValue();
		BigDecimal pbank = paymentBank.getValue();
		BigDecimal pleasing = paymentLeasing.getValue();
		BigDecimal phutang = paymentHutang.getValue();
		BigDecimal jmlByar = ptunai.add(pbank).add(pleasing).add(phutang);
		BigDecimal totalBlanja = Env.ZERO;

		Double dTotalBlj = 0.00;

		if (totalBelanjaTextBox.getText().isEmpty()) {
			dTotalBlj = 0.00;
		} else if (!totalBelanjaTextBox.getText().isEmpty()) {
			if (AD_Language.toUpperCase().equals("EN_US")) {
				dTotalBlj = Double.valueOf(totalBelanjaTextBox.getText()
						.replaceAll(",", ""));
			} else if (AD_Language.toUpperCase().equals("IN_ID")) {
				String dTtlBlj = totalBelanjaTextBox.getText()
						.replaceAll("\\.", "").replaceAll(",", ".");
				dTotalBlj = Double.valueOf(dTtlBlj);
			}
		}

		totalBlanja = BigDecimal.valueOf(dTotalBlj);

		if (ptunai.compareTo(Env.ZERO) < 0 || pbank.compareTo(Env.ZERO) < 0
				|| pleasing.compareTo(Env.ZERO) < 0
				|| phutang.compareTo(Env.ZERO) < 0) {
			msg = msg + "\n" + "Metode Pembayaran Tidak Boleh Kurang Dari Nol";

		}

		if (pbank.compareTo(Env.ZERO) > 0 || pleasing.compareTo(Env.ZERO) > 0
				|| phutang.compareTo(Env.ZERO) > 0) {

			if (jmlByar.compareTo(totalBlanja) < 0) {

				msg = msg + "\n" + "Total Pembayaran Kurang dari Total Belanja";
			} else if (jmlByar.compareTo(totalBlanja) > 0) {
				msg = msg + "\n" + "Total Pembayaran Lebih dari Total Belanja";
			}
		} else if (ptunai.compareTo(Env.ZERO) > 0
				&& pbank.compareTo(Env.ZERO) == 0
				&& pleasing.compareTo(Env.ZERO) == 0
				&& phutang.compareTo(Env.ZERO) == 0) {

			if (jmlByar.compareTo(totalBlanja) < 0) {

				msg = msg + "\n" + "Total Pembayaran Kurang dari Total Belanja";
			}

		}

		return msg;
	}

	public String creditLimitValidation(int C_BPartner_ID) {

		String msg = "";
		BigDecimal phutang = paymentHutang.getValue();

		if (C_BPartner_ID > 0) {

			if (phutang.compareTo(Env.ZERO) > 0) {

				MBPartner bp = new MBPartner(ctx, C_BPartner_ID, null);
				BigDecimal creditLimit = bp.getSO_CreditLimit();
				BigDecimal openBalance = bp.getTotalOpenBalance();
				BigDecimal currentBalance = creditLimit.subtract(openBalance);

				if (currentBalance.compareTo(phutang) < 0) {

					msg = "Pelanggan Sudah Melewati Batas Limit Kredit";

				}

			}

		}

		return msg;

	}

	private void calculatePaymentrule(BigDecimal grandtotal, boolean IsTunai,
			boolean IsBank, boolean IsLeasing, boolean IsHutang) {

		BigDecimal ttlPembayaran = paymentTunai.getValue()
				.add(paymentBank.getValue()).add(paymentLeasing.getValue())
				.add(paymentHutang.getValue());

		if (IsTunai) {
			if (paymentTunai.getValue().compareTo(Env.ZERO) == 0) {
				paymentTunai.setValue(grandtotal.subtract(ttlPembayaran));
				totalBayarTunaiTextBox.setText(format.format(paymentTunai
						.getValue()));
			}
		} else if (IsBank) {
			if (paymentBank.getValue().compareTo(Env.ZERO) == 0) {
				paymentBank.setValue(grandtotal.subtract(ttlPembayaran));
			}
		} else if (IsLeasing) {
			if (paymentLeasing.getValue().compareTo(Env.ZERO) == 0) {
				paymentLeasing.setValue(grandtotal.subtract(ttlPembayaran));
			}
		} else if (IsHutang) {
			if (paymentHutang.getValue().compareTo(Env.ZERO) == 0) {
				paymentHutang.setValue(grandtotal.subtract(ttlPembayaran));
			}
		}

		totalBayarTextBox.setText(format.format(paymentTunai.getValue().add(
				paymentBank.getValue()
						.add(paymentLeasing.getValue().add(
								paymentHutang.getValue())))));

	}
	
	
	
	
	private void ValidationPriceLimit(final boolean IsOtorisasiHutang,final Integer SuperVisor_ID, final Timestamp DateApprove,final Integer C_PayTerm_ID ){
		
		boolean IsUnderLimit = false;
		
		StringBuilder SQLCekValidaSOLimit = new StringBuilder(); 
		SQLCekValidaSOLimit.append("SELECT description::numeric");
		SQLCekValidaSOLimit.append(" FROM ad_param");
		SQLCekValidaSOLimit.append(" WHERE value = 'ValidateSOPriceLimitAllowed'");
		SQLCekValidaSOLimit.append(" AND ad_client_id = "+ AD_Client_ID);
		SQLCekValidaSOLimit.append(" AND isactive = 'Y'");

		Integer rslt = DB.getSQLValueEx(null, SQLCekValidaSOLimit.toString());
		ArrayList<String> listProdUnderLimit = new ArrayList<String>();
		
		for(int i = 0 ; i<salesTable.getRowCount() ;i++){
			
			KeyNamePair prodKey = (KeyNamePair) salesTable.getValueAt(i, 1);
			Integer prodId = prodKey.getKey();
			
//			MProductPricing pp = new MProductPricing(prodId, (Integer)PelangganSearch.getValue(),Env.ONE, true);
			String sqlProductPrice = "SELECT PriceLimit FROM M_ProductPrice WHERE AD_Client_ID = ? AND M_PriceList_Version_ID = ? AND M_Product_ID = ?";

			Timestamp date = new Timestamp(System.currentTimeMillis());

			String sql = "SELECT plv.M_PriceList_Version_ID "
					+ "FROM M_PriceList_Version plv "
					+ "WHERE plv.AD_Client_ID = ? " + " AND plv.M_PriceList_ID= ? " // 1
					+ " AND plv.ValidFrom <= ? " + "ORDER BY plv.ValidFrom DESC";

			Integer pL_ID = getIDFromComboBox(DaftarHargaSearch,MPriceList.Table_Name, MPriceList.COLUMNNAME_Name);

			
			int M_PriceList_Version_ID = DB.getSQLValueEx(null, sql, new Object[] {AD_Client_ID, pL_ID, date });
			BigDecimal priceLimit = DB.getSQLValueBDEx(null,sqlProductPrice, new Object[] { AD_Client_ID,M_PriceList_Version_ID, prodId });

//			pp.setM_PriceList_Version_ID(M_PriceList_Version_ID);
//			pp.setPriceDate(date);
			
			BigDecimal priceEnt = (BigDecimal) salesTable.getValueAt(i, 4);
			BigDecimal priceLmt = priceLimit;
			if(priceEnt.compareTo(priceLmt) < 0){
				listProdUnderLimit.add(prodKey.getName());
				IsUnderLimit = true;
			}
				
		}

		if(IsUnderLimit){
			
			if(rslt <= 0){
				
				String prodList = "";
				
				for(int i = 0; i < listProdUnderLimit.size() ; i++){
					if(prodList == ""){
						prodList = prodList + listProdUnderLimit.get(i);
					}else{
						prodList = prodList +", " +listProdUnderLimit.get(i);
					}
				}
				 
				FDialog.warn(windowNo,null,"","Penjualan Tidak Dapat DiProses Karena Harga "+ prodList+" dibawah Harga Price Limit. Silahkan Edit Harga Terlebih Dahulu ","Peringatan");
				return;
			
				
			}else if(rslt > 0){
				
				String prodList = "";
				
				for(int i = 0; i < listProdUnderLimit.size() ; i++){
					if(prodList == ""){
						prodList = prodList + listProdUnderLimit.get(i);
					}else{
						prodList = prodList +", " +listProdUnderLimit.get(i);
					}
				}
				 
				
			FDialog.ask(windowNo,null,"Konfirmasi","","Harga Produk "+ prodList+" dibawah Harga Price Limit. Apakah Anda Ingin Melanjutkan Proses? ",new Callback<Boolean>(){
					
							@Override
							public void onCallback(Boolean result) {
							
								if(result){
									
//									boolean valid = false;
									
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
										w.setPage(form.getPage());
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
										////inputGrid.setHflex("min");

										rows = inputGrid.newRows();

										row = rows.newRow();
										row.appendCellChild(LabelUseName.rightAlign());
										row.appendCellChild(userName,1);
										//userName.setHflex("true");

										row = rows.newRow();
										row.appendCellChild(LabelPass.rightAlign());
										row.appendCellChild(password,1);
										//password.setHflex("true");
										password.setType("password");

										Vbox vbox = new Vbox();
										w.appendChild(vbox);
										vbox.appendChild(new Separator());
										final ConfirmPanel panel = new ConfirmPanel(true,false,false,false,false,false,false);
										vbox.appendChild(panel);
									
										panel.addActionListener(Events.ON_CLICK,new EventListener<Event>() {

													@Override
													public void onEvent(Event event)
															throws Exception {
														if (event.getTarget() == panel.getButton(ConfirmPanel.A_CANCEL)) {
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
															SQLCek.append(" WHERE AD_Client_ID = "+ AD_Client_ID);
															SQLCek.append(" AND Value ='"+ SOProcesSuperVisor+ "')");
															SQLCek.append(" AND Name = '"+ uName+ "'");
															SQLCek.append(" AND Password = '"+ passwd+ "'");

															int cek = DB.getSQLValueEx(null,SQLCek.toString());
															
															StringBuilder SQLGetSpv = new StringBuilder();
															SQLGetSpv.append(" SELECT description::NUMERIC");
															SQLGetSpv.append(" FROM AD_Param");
															SQLGetSpv.append(" WHERE AD_Client_ID = "+ AD_Client_ID);
															SQLGetSpv.append(" AND Value ='"+ SOProcesSuperVisor+ "'");
															int supervisor_id = DB.getSQLValueEx(null,SQLGetSpv.toString());

															final Integer Spv_ID = supervisor_id;

															Calendar cal = Calendar.getInstance();
															cal.setTime(Env.getContextAsDate(ctx,"#Date"));
															cal.set(Calendar.HOUR_OF_DAY,0);
															cal.set(Calendar.MINUTE,0);
															cal.set(Calendar.SECOND,0);
															cal.set(Calendar.MILLISECOND,0);
															final Timestamp DateApp = new Timestamp(cal.getTimeInMillis());

															StringBuilder SQLGetTerm = new StringBuilder();
															SQLGetTerm.append(" SELECT description::numeric");
															SQLGetTerm.append(" FROM ad_param");
															SQLGetTerm.append(" WHERE AD_Client_ID = " + AD_Client_ID);
															SQLGetTerm.append(" AND value = 'DefaultPymTermSODebt'");
															SQLGetTerm.append(" AND isactive = 'Y'");
															SQLGetTerm.append(" AND AD_Org_ID = "+ (Integer) orgSearch.getValue());

															final Integer C_PayTrm_ID = DB.getSQLValueEx(null,SQLGetTerm.toString());
															

															if (cek > 0) {
																if(IsOtorisasiHutang){
																	process(IsOtorisasiHutang,true,SuperVisor_ID,DateApprove,C_PayTerm_ID);
																}else{
																	process(IsOtorisasiHutang,true,Spv_ID,DateApp,C_PayTrm_ID);
																	
																}
																
															} else {

																FDialog.info(windowNo,null,"","UserName atau Password Tidak Terdaftar, Silahkan Entri Ulang Otorisasi",
																		"Info");
																
																return;

															}

														}

														w.onClose();

													}

												});

										w.addEventListener(DialogEvents.ON_WINDOW_CLOSE,new EventListener<Event>() {

													@Override
													public void onEvent(Event event) throws Exception {
														w.dispose();
													}
													
												});
										w.doHighlighted();

									} catch (Exception err) {

										log.log(Level.SEVERE,this.getClass().getCanonicalName() + ".valueChange - ERROR: "+ err.getMessage(),err);
									}
							
								}else{
									return;
								}
								
							}
							
							
				});		
				
			}
			
			
		}else{
			process(IsOtorisasiHutang,false,SuperVisor_ID,DateApprove,C_PayTerm_ID);

		}
		return;
			
	}
	

}
