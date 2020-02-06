package org.semeru.pos;

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
import org.adempiere.webui.event.ValueChangeEvent;
import org.adempiere.webui.event.ValueChangeListener;
import org.adempiere.webui.event.WTableModelEvent;
import org.adempiere.webui.event.WTableModelListener;
import org.adempiere.webui.panel.ADForm;
import org.adempiere.webui.panel.CustomForm;
import org.adempiere.webui.panel.IFormController;
import org.compiere.minigrid.IMiniTable;
import org.compiere.model.MBPartner;
import org.compiere.model.MClient;
import org.compiere.model.MPInstance;
import org.compiere.model.MProcess;
import org.compiere.process.ProcessInfo;
import org.compiere.process.ProcessInfoParameter;
import org.compiere.util.DB;
import org.compiere.util.Env;
import org.compiere.util.Msg;
import org.compiere.util.Trx;
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
 */

public class WSemeruPembatalanPenjualan extends SemeruPembatalanPenjualan implements IFormController,EventListener<Event>, WTableModelListener, ValueChangeListener{

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
		
		private Properties ctx = Env.getCtx();

		//Button
		private Button printBtn = new Button();
		private Button processBtn = new Button();
		private Button newBtn = new Button();
		
		// Date Return
		private Label dateLabel = new Label("Tanggal Pembatalan :");
		private WDateEditor dateField = new WDateEditor();
		

		
		//TableLine
		private WListbox resultTable = ListboxFactory.newDataTable();
		private WListbox resultTable2 = ListboxFactory.newDataTable();


		//NoNota
		private Label noNotaLabel = new Label("No. Nota :");
		private Textbox noNotaTB = new Textbox();
							
		//Keterangan
		private Label ketLabel = new Label("Keterangan :");
		private Textbox ketTB = new Textbox();
			
		//Variable Define
		private int AD_Client_ID = Env.getAD_Client_ID(ctx);
//		private int C_Currency_ID = 303;
		private int windowNo = form.getWindowNo();
		private Integer rowIndex = null;
//		private String m_docAction = "CO";
		private int C_OrderPrint_ID = 0;
//		private MSemeruPOS smrNew = null;
		boolean isManualDocumentNo = false;
		String msgInfo = "";

		public WSemeruPembatalanPenjualan() {

			dyInit();
			zkInit();

		}

		@Override
		public void valueChange(ValueChangeEvent e) {

			String name = e.getPropertyName();
			Object value = e.getNewValue();
			if (log.isLoggable(Level.CONFIG)) log.config(name + "=" + value);
			
			if (value == null)
				return;
			
			if (name.equals("C_Order_ID")){
				
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
			
			if (rowIndex == null)
				return;
						
		}


		@Override
		public void onEvent(Event e) throws Exception {
			
			log.config("");
			
//			String noNota = "";
			
			if (e.getTarget().equals(noNotaTB)){
				resultTable.clear();

//				StringBuilder sqlDc = new StringBuilder();
//				noNota = noNotaTB.getValue().toString();

				
//				sqlDc.append("SELECT C_DecorisPOS_ID ");
//				sqlDc.append("FROM C_DecorisPOS ");
//				sqlDc.append("WHERE DocumentNo = '"+ noNota + "'");
//				sqlDc.append(" AND AD_Client_ID = ? ");
//				sqlDc.append(" AND IsPembatalan = 'N' ");
//
//				int C_DecorisPOS_ID = DB.getSQLValueEx(ctx.toString(), sqlDc.toString(),new Object[]{AD_Client_ID});
//				
//				if (C_DecorisPOS_ID <= 0){
//					FDialog.info(windowNo, null, "", "Dokument Tidak Ada Atau Sudah Pernah DiBatalkan", "Info");
//					return;
//				}
						
				if(noNotaTB.getValue().toString()!= null && noNotaTB.getValue().toString()!= "" ){
					
//					noNota = noNotaTB.getValue().toString();
					Vector<Vector<Object>> data = null;
					
					//get detail					
					ArrayList<Integer> detailList = new ArrayList<Integer>();

//					StringBuilder sqlPOSPayment = new StringBuilder();
//					sqlPOSPayment.append("SELECT C_DecorisPOSLine_ID ");
//					sqlPOSPayment.append("FROM C_DecorisPOSLine ");
//					sqlPOSPayment.append("WHERE AD_Client_ID = ? ");
//					sqlPOSPayment.append("AND C_DecorisPOS_ID IN ");
//					sqlPOSPayment.append("(SELECT C_DecorisPOS_ID ");
//					sqlPOSPayment.append("FROM C_DecorisPOS ");
//					sqlPOSPayment.append("WHERE AD_Client_ID = ? ");
//					sqlPOSPayment.append("AND IsPembatalan = 'N' ");
//					sqlPOSPayment.append("AND DocumentNo = '"+ noNota + "')");
//					
//					PreparedStatement pstmt = null;
//					ResultSet rs = null;
//					try {
//						pstmt = DB.prepareStatement(sqlPOSPayment.toString(), null);
//						pstmt.setInt(1, AD_Client_ID);
//						pstmt.setInt(2, AD_Client_ID);
//
//						rs = pstmt.executeQuery();
//						while (rs.next()) {
//							detailList.add(rs.getInt(1));
//						}
//
//					} catch (SQLException ex) {
//						log.log(Level.SEVERE, sqlPOSPayment.toString(), ex);
//					} finally {
//						DB.close(rs, pstmt);
//						rs = null;
//						pstmt = null;
//					}
					
					
					detailList.add(1);
					detailList.add(2);
					detailList.add(3);
					detailList.add(4);
					detailList.add(5);
					detailList.add(6);
					detailList.add(7);
					detailList.add(8);
					detailList.add(9);
					detailList.add(10);

					data = getTrxData(detailList, resultTable, windowNo, this);
					Vector<String> columnNames = getOISColumnNames();

					resultTable.clear();
					resultTable2.clear();


					// Set Model
					ListModelTable modelP = new ListModelTable(data);
					modelP.addTableModelListener(this);
					resultTable.setData(modelP, columnNames);
					configureMiniTable(resultTable);
					//resultTable.addColumn(Button.class, false, "TEST");
					//final Div d = new Div();
					 // any component could created as a child of grid
					
					//resultTable.setValueAt(value, 1, column);
								
					ListModelTable modelP2 = new ListModelTable(data);
					modelP2.addTableModelListener(this);
					resultTable2.setData(modelP2, columnNames);
					configureMiniTable(resultTable2);
					
					
//					for (int i = 0 ; i < resultTable.getRowCount() ; i++){
//						
//						resultTable.setColumnReadOnly(i, true);
//						
//					}
					
					noNotaTB.setReadonly(true);
					
				}
					
			}else if (e.getTarget().equals(newBtn)){

				noNotaTB.setReadonly(false);
				noNotaTB.setText("");
				for (int i=0 ; i < resultTable.getRowCount(); i++){
					deletedata(0);
				}
				resultTable.clear();
				C_OrderPrint_ID = 0;
				printBtn.setEnabled(false);
//				smrNew = null;
				
				
			}else if (e.getTarget().equals(printBtn)){

				String trxName = Trx.createTrxName("pos");
				String url = "/home/idempiere/idempiere.gtk.linux.x86_64/idempiere-server/reports/";
				//ad process id harcode 1000000
				MProcess proc = new MProcess(Env.getCtx(), 330050, trxName);
				MPInstance instance = new MPInstance(proc,proc.getAD_Process_ID());
				ProcessInfo pi = new ProcessInfo("Print Nota", 330050);
				pi.setAD_PInstance_ID(instance.getAD_PInstance_ID());
					 
				ArrayList<ProcessInfoParameter> list = new ArrayList<ProcessInfoParameter>();
				list.add(new ProcessInfoParameter("C_Order_ID", C_OrderPrint_ID, null,null, null));
				list.add(new ProcessInfoParameter("url_path",url, null,null, null));
	
				ProcessInfoParameter[] pars = new ProcessInfoParameter[list.size()];
				list.toArray(pars);
				pi.setParameter(pars);
				
				Trx trx = Trx.get(trxName, true);
				trx.commit();
					
				ProcessUtil.startJavaProcess(Env.getCtx(), pi, Trx.get(trxName,true));
				
			}else if (e.getTarget().equals(processBtn)){
				
				process();
	
//				StringBuilder sqlDc = new StringBuilder();
//				noNota = noNotaTB.getValue().toString();
//
//				
//			
//				
//				sqlDc.append("SELECT C_DecorisPOS_ID ");
//				sqlDc.append("FROM C_DecorisPOS ");
//				sqlDc.append("WHERE DocumentNo = '"+ noNota + "'");
//				sqlDc.append("AND AD_Client_ID = ? ");
//				sqlDc.append("AND IsPembatalan = 'N' ");
//
//				int C_DecorisPOS_ID = DB.getSQLValueEx(ctx.toString(), sqlDc.toString(),new Object[]{AD_Client_ID});
//				
//				if (C_DecorisPOS_ID <= 0){
//					FDialog.info(windowNo, null, "", "Dokument Tidak Ada Atau Sudah Pernah DiBatalkan", "Info");
//					return;
//				}
//				
//				if (C_DecorisPOS_ID > 0){
//					
//					MDecorisPOS dec = new MDecorisPOS(ctx, C_DecorisPOS_ID, null);
//					Date input = null;
//					
//					if(dateField.getValue()!=null){
//						input = (Date) dateField.getValue();
//						Date trx =  (Date) dec.getDateOrdered();
//						
//						if(input.compareTo(trx)<0){
//							FDialog.warn(windowNo, null, "", "Tanggal Pembatalan Tidak Boleh Lebih Kecil dari Tanggal Transaksi", "Info");
//							return;
//						}
//					}
//
//				
//						SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd 00:00:00");
//						String tglTostr = df.format(input);
//						Timestamp dateOrd = Timestamp.valueOf(tglTostr);
//
//					
//					
//					int C_DecorisPOS_ID_New = dec.copyData(0,dec.isPenjualan(),isManualDocumentNo,dateOrd);
//					
//					if(isManualDocumentNo){
//						dec.setDocumentNo(dec.getDocumentNo()+"^");
//						dec.saveEx();						
//						
//						MOrder ord = new MOrder(dec.getCtx(), dec.getC_Order_ID(), dec.get_TrxName());
//						ord.setDocumentNo(dec.getDocumentNo());
//						ord.saveEx();
//						
//						
//						int M_InOut_ID = checkShipmentRelated(dec.getC_Order_ID());
//						int C_Invoice_ID = checkInvoiceRelated(dec.getC_Order_ID());
//						
//						MInOut InOut = new MInOut(ord.getCtx(), M_InOut_ID, null);
//						InOut.setDocumentNo(dec.getDocumentNo());
//						InOut.saveEx();
//						
//						MInvoice inv = new MInvoice(ord.getCtx(), C_Invoice_ID, null);
//						inv.setDocumentNo(dec.getDocumentNo());
//						inv.saveEx();
//												
//					}
//					
//					
//					//cek validate period status
//					if (!MPeriod.isOpen(ctx, (Timestamp)dec.getDateOrdered(), "SOO", dec.getAD_Org_ID()))
//					{
//						FDialog.warn(windowNo, null, "", "Transaksi Tidak Dapat Diproses Karena Status Period Closed", "Peringatan");
//						return;
//					}
//					
//					decNew = new MDecorisPOS(ctx, C_DecorisPOS_ID_New, null);
//					MOrder OldOrd = new MOrder(ctx, dec.getC_Order_ID(), null);
//					MOrder ord = null;
//					
//					
//						ord = new MOrder(ctx, 0, null);
//						
//						ord.setAD_Org_ID(decNew.getAD_Org_ID());
//						ord.setC_BPartner_ID(decNew.getC_BPartner_ID());
//						ord.setPOReference(decNew.getDocumentNo());
//						ord.setDescription(decNew.getDescription());
//						ord.setDeliveryRule(decNew.getDeliveryRule());
//				 		ord.setC_DocTypeTarget_ID(decNew.getC_DocType_ID());
//						ord.setDeliveryViaRule(decNew.getDeliveryViaRule());
//						ord.setSalesRep_ID(decNew.getSalesRep_ID());
//						ord.setC_Currency_ID(C_Currency_ID);
//						ord.setDateOrdered((Timestamp) dateField.getValue());
//						ord.setM_Warehouse_ID(decNew.getM_Warehouse_ID());
//						ord.setC_PaymentTerm_ID(decNew.getC_PaymentTerm_ID());
//						ord.setM_PriceList_ID(decNew.getM_PriceList_ID());
//						ord.setTotalLines(decNew.getTotalLines());
//						ord.setGrandTotal(decNew.getGrandTotal());
//						ord.setPaymentRule(decNew.getPaymentRule());
//						if(OldOrd.get_ValueAsInt("M_Locator_ID")>0){
//							ord.set_ValueNoCheck("M_Locator_ID", OldOrd.get_ValueAsInt("M_Locator_ID"));
//						}
//						ord.set_CustomColumn("CreatedByPOS_ID", decNew.getCreatedByPOS_ID());
//						ord.setIsSelfService(true);
//						ord.setIsSOTrx(true);
//						if(isManualDocumentNo){
//							ord.setDocumentNo(decNew.getDocumentNo());
//						}
//						ord.saveEx();
//						
//						decNew.setC_Order_ID(ord.getC_Order_ID());
//						decNew.saveEx();
//						
//						//create pos payment
//						StringBuilder sqlOrd = new StringBuilder();
//						sqlOrd.append("SELECT C_Order_ID ");
//						sqlOrd.append("FROM C_DecorisPOS ");
//						sqlOrd.append("WHERE AD_Client_ID = ? ");
//						sqlOrd.append("AND C_DecorisPOS_ID = ? ");
//						int ord_ID = DB.getSQLValueEx(ctx.toString(), sqlOrd.toString(), new Object[]{AD_Client_ID,C_DecorisPOS_ID});
//						
//						
//						StringBuilder sqlPOspay = new StringBuilder();
//						sqlPOspay.append("SELECT C_POSPayment_ID ");
//						sqlPOspay.append("FROM C_POSPayment ");
//						sqlPOspay.append("WHERE AD_Client_ID = ? ");
//						sqlPOspay.append("AND C_Order_ID = ? ");
//	
//						PreparedStatement pstmt = null;
//						ResultSet rs = null;
//						try {
//							pstmt = DB.prepareStatement(sqlPOspay.toString(), null);
//							pstmt.setInt(1, AD_Client_ID);
//							pstmt.setInt(2, ord_ID);
//							rs = pstmt.executeQuery();
//							while (rs.next()) {
//						
//								if (rs.getInt(1) > 0){
//									
//									X_C_POSPayment posPayOld = new X_C_POSPayment(ctx, rs.getInt(1), null);
//									X_C_POSPayment posPayNew = new X_C_POSPayment(ctx, 0, null);
//	
//									posPayNew.setAD_Org_ID(posPayOld.getAD_Org_ID());
//									posPayNew.setC_Order_ID(ord.getC_Order_ID());
//									posPayNew.setC_POSTenderType_ID(posPayOld.getC_POSTenderType_ID());
//									posPayNew.setTenderType(posPayOld.getTenderType());
//									posPayNew.setPayAmt(posPayOld.getPayAmt().negate());
//									posPayNew.set_CustomColumn("LeaseProvider", posPayOld.get_Value("LeaseProvider"));
//									posPayNew.set_CustomColumn("C_BankAccount_ID", posPayOld.get_ValueAsInt("C_BankAccount_ID"));
//									posPayNew.saveEx();
//								
//								}
//								
//							}	
//							
//						} catch (SQLException ex) {
//							log.log(Level.SEVERE, sqlPOspay.toString(), ex);
//						} finally {
//							DB.close(rs, pstmt);
//							rs = null;
//							pstmt = null;
//						}
//	
//						
//						X_C_DecorisPOSLine[] lines = decNew.getLines();
//						for(X_C_DecorisPOSLine line :lines){
//						
//						int Product_ID = line.getM_Product_ID();
//						MProduct prod = new MProduct(ctx, Product_ID, null);
//							
//						int taxCategory = prod.getC_TaxCategory_ID();
//						MOrderLine ordLine = new MOrderLine(ctx, 0, null);
//						MTaxCategory cat = new MTaxCategory(ctx, taxCategory, null);
//						String taxCatName = cat.getName();
//						
//						String sqlTax = "SELECT C_Tax_ID FROM C_Tax WHERE AD_Client_ID = ? AND Name = '"+ taxCatName +"'"; 
//						int C_Tax_ID = DB.getSQLValueEx(cat.get_TrxName(), sqlTax, new Object[]{AD_Client_ID});
//											
//						//ordLine
//						ordLine.setAD_Org_ID(ord.getAD_Org_ID());
//						ordLine.setC_Order_ID(ord.getC_Order_ID());
//						ordLine.setM_Product_ID(line.getM_Product_ID());
//						ordLine.setC_UOM_ID(prod.getC_UOM_ID());
//						ordLine.setQtyEntered(line.getQtyOrdered());
//						ordLine.setQtyOrdered(line.getQtyOrdered());
//						ordLine.setPriceList(line.getPriceList().negate());
//						ordLine.setPriceEntered(line.getPriceEntered().negate());
//						ordLine.setPriceActual(line.getPriceEntered().negate());
//						ordLine.setC_Tax_ID(C_Tax_ID);
//						ordLine.setLineNetAmt(line.getLineNetAmt());
//						if(line.getM_AttributeSetInstance_ID() > 0){
//							ordLine.setM_AttributeSetInstance_ID(line.getM_AttributeSetInstance_ID());
//						}
//						ordLine.setLine(line.getLine());
//						ordLine.saveEx();
//						
//						line.setC_OrderLine_ID(ordLine.getC_OrderLine_ID());
//						line.saveEx();
//													
//						}
//						
//						
//						if (ord != null)
//						{
//							
//							if (m_docAction != null && m_docAction.length() > 0)
//							{
//								ord.setDocAction(m_docAction);
//								if(!ord.processIt (m_docAction)) {
//									log.warning("Invoice Process Failed: " + ord + " - " + ord.getProcessMsg());
//									throw new IllegalStateException("Invoice Process Failed: " + ord + " - " + ord.getProcessMsg());
//								}
//							}
//							ord.saveEx();
//							
//							//set original pos doc to canceled doc
//							dec.setIsPembatalan(true);
//							dec.saveEx();
//							
//							decNew.setProcessed(true);
//							C_OrderPrint_ID = ord.getC_Payment_ID();
//							infoGeneratedDocument(ord.getC_Order_ID(), windowNo); 
//							printBtn.setEnabled(true);
//	
//						}
//			
//						}		
//						
				}
						
		}


		@Override
		public ADForm getForm() {
			return form;

		}
	
		private void zkInit() {

			form.appendChild(mainLayout);
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
			noNotaTB.addEventListener(0, "onBlur", this);
			row.appendCellChild(new Space(), 1);
			
			//tanggal pembatalan
			row = rows.newRow();
			row.appendCellChild(dateLabel.rightAlign(),1);
			row.appendCellChild(dateField.getComponent(), 1);
			dateField.getComponent().setHflex("true");
			
			// ketTB
			row = rows.newRow();
			row.appendCellChild(ketLabel.rightAlign(), 1);
			row.appendCellChild(ketTB, 1);
			ketTB.setRows(2);
			ketTB.setHeight("100%");
			ketTB.setHflex("1");
			row.appendCellChild(new Space(), 1);

			//button new pos
			row = rows.newRow();
			row.appendCellChild(new Space(), 1);
			row.appendCellChild(newBtn, 1);
			newBtn.addActionListener(this);
			newBtn.setHflex("true");
			row.appendCellChild(new Space(), 1);

			// button clear product
			row = rows.newRow();
			row.appendCellChild(new Space(), 1);
			row.appendCellChild(printBtn, 1);
			printBtn.addActionListener(this);
			printBtn.setHflex("true");
			printBtn.setEnabled(false);
			row.appendCellChild(new Space(), 1);
			
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
			
			// button process
			row.appendCellChild(new Space(), 1);
			processBtn.addActionListener(this);
			row.appendCellChild(processBtn, 1);
			processBtn.setHflex("true");
			row.appendCellChild(new Space(), 1);

			
			salesPanel.appendChild(salesLayout);
			salesPanel.setWidth("100%");
			salesPanel.setHeight("100%");
			salesLayout.setWidth("100%");
			salesLayout.setHeight("50%");
			salesLayout.setStyle("border: none");

			
			south = new South();
			south.setStyle("border: none");
			salesLayout.appendChild(south);
			Center center = new Center();
			salesLayout.appendChild(center);
			center.appendChild(resultTable);
			resultTable.setWidth("500px");
//			resultTable.setHeight("50%");
			center.setStyle("border: none; height:100%");
			
//			center.appendChild(resultTable2);
//			resultTable2.setWidth("500px");
			//resultTable.setHeight("100%");
			center = new Center();
			mainLayout.appendChild(center);
			center.appendChild(infoLayout );
			infoLayout .setHflex("1");
			//infoLayout .setVflex("1");
			infoLayout .setStyle("border: none");
//			infoLayout .setWidth("100%");
//			infoLayout .setHeight("100%");

			north = new North();
			north.setStyle("border: none");
			north.setHeight("100%");
			infoLayout .appendChild(north);
			north.appendChild(salesPanel);
			north.setSplittable(true);
			
			center = new Center();
			center.setStyle("border: none");
			infoLayout.appendChild(center);
			
		}
		
		private void dyInit() {
	
			//Set Button Label
			newBtn.setLabel("Baru");
			printBtn.setLabel("Print Nota");
			processBtn.setLabel("Proses Pembatalan");
			
			int AD_User_ID = Env.getAD_User_ID(ctx);


			String sqlKasir = "SELECT C_BPartner_ID FROM AD_User WHERE AD_Client_ID = ? AND AD_User_ID = ?";
			int CreatedByPOS_ID = DB.getSQLValueEx(ctx.toString(), sqlKasir.toString(),new Object[] { Env.getAD_Client_ID(ctx), AD_User_ID });

			if(CreatedByPOS_ID > 0){
				
				String manualDocumentNoSql = "SELECT IsManualDocumentNo FROM C_Pos WHERE AD_Client_ID = ? AND  CreatedByPOS_ID = ? ";
				String isManualDoc  = DB.getSQLValueStringEx(ctx.toString(),manualDocumentNoSql,new Object[] { AD_Client_ID, CreatedByPOS_ID });
				if (isManualDoc.toUpperCase().equals("Y")){
					isManualDocumentNo = true;
				}else{
					isManualDocumentNo = false;
				}
			}
			
			// Date set to Login Date
			Calendar cal = Calendar.getInstance();
			cal.setTime(Env.getContextAsDate(ctx, "#Date"));
			cal.set(Calendar.HOUR_OF_DAY, 0);
			cal.set(Calendar.MINUTE, 0);
			cal.set(Calendar.SECOND, 0);
			cal.set(Calendar.MILLISECOND, 0);
			dateField.setValue(new Timestamp(cal.getTimeInMillis()));
			dateField.addValueChangeListener(this);
		
		}
		
		protected void configureMiniTable(IMiniTable miniTable) {
				
				miniTable.setColumnClass(0, Boolean.class, false);
				miniTable.setColumnClass(1, Integer.class, true); 	
				miniTable.setColumnClass(2, Integer.class, true); 	
				miniTable.setColumnClass(3, Integer.class, true); 	
				miniTable.setColumnClass(4, Integer.class, true); 	
				miniTable.setColumnClass(5, Integer.class, true); 	
				miniTable.setColumnClass(6, Integer.class, true); 	
				miniTable.setColumnClass(7, Integer.class, true); 	
				miniTable.setColumnClass(8, Button.class, false); 	
				miniTable.setColumnClass(9, Combobox.class, false); 
				miniTable.setColumnClass(9, WSearchEditor.class, false); 
				miniTable.setColumnClass(10, WTableDirEditor.class, false); 	

			miniTable.autoSize();

		}

		protected Vector<String> getOISColumnNames() {
			
			Vector<String> columnNames = null;
		
				columnNames = new Vector<String>(9);
				columnNames.add(Msg.getMsg(ctx, "Select"));
				columnNames.add("Line No");
				columnNames.add("Produk ");
				columnNames.add("IMEI");
				columnNames.add("Qty");
				columnNames.add("Lokasi");
				columnNames.add("List Price");
				columnNames.add("Price");
				columnNames.add("BUTTON");
				columnNames.add("LIST");
				columnNames.add("SEARCH");
				columnNames.add("TABLEDIR");
	
			return columnNames;

		}
		
		protected void process() {
			
//			System.out.println(resultTable.getValueAt(1, 8));
//			System.out.println(resultTable.getValueAt(1, 9));
//			System.out.println(resultTable.getValueAt(1, 10));
//			System.out.println(resultTable.getValueAt(1, 11));
			
			Combobox xxx = (Combobox) resultTable.getValueAt(1, 9);
			String combo = xxx.getText();
			System.out.println("Combox Value : " + combo);
			
			WSearchEditor search = (WSearchEditor) resultTable.getValueAt(1, 10); 
			Integer BP_ID = (Integer) search.getValue();
			MBPartner bp = new MBPartner(ctx, BP_ID, null);
			System.out.println("Bussiness Partner :" + bp.getName());
			
			WTableDirEditor table = (WTableDirEditor) resultTable.getValueAt(1, 11); 
			Integer client_id = (Integer)table.getValue();
			MClient cl = new MClient(ctx, client_id, null);
			System.out.println("Client :" + cl.getName());
			
			ketTB.setValue("Combobox Value : "+ combo +"\n" +
					"Bussiness Partner : "+ bp.getName() +"\n"+
					"Client : "+ cl.getName());
			
		}

		
}
