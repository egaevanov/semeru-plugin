package org.semeru.pos;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.Vector;
import java.util.logging.Level;

import org.adempiere.webui.component.Messagebox;
import org.adempiere.webui.window.FDialog;
import org.compiere.minigrid.IMiniTable;
import org.compiere.model.MAttributeSetInstance;
import org.compiere.model.MBPartner;
import org.compiere.model.MBankAccount;
import org.compiere.model.MInOut;
import org.compiere.model.MInvoice;
import org.compiere.model.MLocator;
import org.compiere.model.MOrder;
import org.compiere.model.MPayment;
import org.compiere.model.MProduct;
import org.compiere.model.MProductPrice;
import org.compiere.model.MTaxCategory;
import org.compiere.model.MWarehouse;
import org.compiere.model.X_C_POSPayment;
import org.compiere.util.CLogger;
import org.compiere.util.DB;
import org.compiere.util.DisplayType;
import org.compiere.util.Env;
import org.compiere.util.KeyNamePair;
import org.semeru.pos.model.X_SM_SemeruPOS;

/**
 * 
 * @author Tegar N
 *
 */

public class SemeruPOS {

	public CLogger log = CLogger.getCLogger(SemeruPOS.class);

	Vector<Vector<Object>> data = new Vector<Vector<Object>>();

	protected BigDecimal totalPrices = Env.ZERO;
	protected BigDecimal initialDisc = Env.ZERO;
	protected BigDecimal totalDiskons = Env.ZERO;
	protected BigDecimal initialPrice = Env.ZERO;
	protected BigDecimal initialBeforeDisc = Env.ZERO;
	protected BigDecimal totalBeforeDiscounts = Env.ZERO;

	protected DecimalFormat format = DisplayType
			.getNumberFormat(DisplayType.Amount);

	private Properties ctx = Env.getCtx();
	private int AD_Client_ID = Env.getAD_Client_ID(ctx);

	/**
	 * 
	 * Created by :TEGAR N
	 * 
	 */
	protected ArrayList<KeyNamePair> loadPriceList(int CreatedByPos_ID) {
		ArrayList<KeyNamePair> list = new ArrayList<KeyNamePair>();
		int AD_Client_ID = Env.getAD_Client_ID(Env.getCtx());
		StringBuilder sql = new StringBuilder();
		sql.append("SELECT M_PriceList_ID,Name FROM M_PriceList WHERE AD_Client_ID =? AND IsActive = 'Y' AND isSoPriceList= 'Y'"
				+ " AND  M_PriceList_ID IN "
				+ "(SELECT M_PriceList_ID FROM C_POS WHERE  AD_Client_ID = ? AND CreatedByPOS_ID = ?)");
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			pstmt = DB.prepareStatement(sql.toString(), null);
			pstmt.setInt(1, AD_Client_ID);
			pstmt.setInt(2, AD_Client_ID);
			pstmt.setInt(3, CreatedByPos_ID);

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

	protected ArrayList<KeyNamePair> loadImei(int M_Product_ID, int M_Locator_ID) {
		ArrayList<KeyNamePair> list = new ArrayList<KeyNamePair>();
		int AD_Client_ID = Env.getAD_Client_ID(Env.getCtx());
		StringBuilder sql = new StringBuilder();
		sql.append(" SELECT ");
		sql.append(" a.M_AttributeSetInstance_ID, ");
		sql.append(" a.Serno ");
		sql.append(" FROM ( ");
		sql.append(" SELECT null as M_AttributeSetInstance_ID, ");
		sql.append(" '' as Serno"); 
		sql.append(" UNION ");
		sql.append(" SELECT M_AttributeSetInstance_ID, ");
		sql.append(" Serno ");
		sql.append(" FROM M_AttributeSetInstance "); 
		sql.append(" WHERE AD_Client_ID = ? ");
		sql.append(" AND M_AttributeSetInstance_ID IN ( ");
		sql.append(" SELECT M_AttributeSetInstance_ID "); 
		sql.append(" FROM M_StorageOnHand ");
		sql.append(" WHERE  AD_Client_ID = ? ");
		sql.append(" AND M_Product_ID = ? AND M_Locator_ID = ? "); 
		sql.append(" AND QtyOnHand > 0))a " ); 
		sql.append(" ORDER BY a.Serno ");
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			pstmt = DB.prepareStatement(sql.toString(), null);
			pstmt.setInt(1, AD_Client_ID);
			pstmt.setInt(2, AD_Client_ID);
			pstmt.setInt(3, M_Product_ID);
			pstmt.setInt(4, M_Locator_ID);

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

	public Vector<Vector<Object>> deletedata(int rowindex) {

		data.remove(rowindex);
		return data;

	}

	public void tableChangeCalculate(int row, int col, IMiniTable miniTable,
			int windowNo, boolean IsForceLimitPrice, int M_PriceList_ID) {
		if (col == 2 || col == 3 || col == 4) {

			for (int i = 0; i < miniTable.getRowCount(); i++) {

				String imei = (String) miniTable.getValueAt(i, 8);
				
				if(imei.isEmpty()||imei == null || imei =="" ){
					imei = "-";
				}

				BigDecimal qty = (BigDecimal) miniTable.getValueAt(i, 2);
				BigDecimal price = (BigDecimal) miniTable.getValueAt(i, 4);
				BigDecimal priceList = (BigDecimal) miniTable.getValueAt(i, 3);
				KeyNamePair prod = (KeyNamePair) miniTable.getValueAt(i, 1);

				BigDecimal totalPrice = qty.multiply(price);
				BigDecimal beforeDisc = qty.multiply(priceList);
				BigDecimal disc = priceList.subtract(price).multiply(qty);

				BigDecimal totalDisc = Env.ZERO;
				BigDecimal totalLines = Env.ZERO;
				BigDecimal totalBeforeDisc = Env.ZERO;

				Timestamp date = new Timestamp(System.currentTimeMillis());

				String sql = "SELECT plv.M_PriceList_Version_ID "
						+ "FROM M_PriceList_Version plv "
						+ "WHERE plv.AD_Client_ID = ? "
						+ " AND plv.M_PriceList_ID= ? " // 1
						+ " AND plv.ValidFrom <= ? "
						+ "ORDER BY plv.ValidFrom DESC";

				int M_PriceList_Version_ID = DB.getSQLValueEx(null, sql,
						new Object[] { AD_Client_ID, M_PriceList_ID, date });

				String sqlProductPrice = "SELECT PriceLimit FROM M_ProductPrice WHERE AD_Client_ID = ? AND M_PriceList_Version_ID = ? AND M_Product_ID = ?";
				BigDecimal priceLimit = DB.getSQLValueBDEx(null,sqlProductPrice, new Object[] { AD_Client_ID,M_PriceList_Version_ID, prod.getKey() });

				Double dPersen = ((Double.valueOf(priceList.toString()) - Double
						.valueOf(price.toString())) / Double.valueOf(priceList
						.toString())) * 100;
				BigDecimal persentase = BigDecimal.valueOf(dPersen).setScale(2,
						RoundingMode.HALF_DOWN);

				if (persentase.compareTo(Env.ZERO) < 0) {
					persentase = Env.ZERO;
				}

				MProduct prodd = new MProduct(ctx, prod.getKey(), null);
				
				if (col == 2 && prodd.getM_AttributeSet_ID()>0 ) {

					if (qty.compareTo(Env.ONE) > 0) {
						FDialog.warn(windowNo,null,"","Order untuk produk IMEI terdaftar tidak bisa lebih dari 1 ","Peringatan");
						miniTable.setValueAt(Env.ONE, i, 2);

						return;
					}

				}

				// validasi pricelist
				if (col == 4 && IsForceLimitPrice) {

					/*
					 * temp cmd for update limit price
					 * 
					 * 
					 * Timestamp date = new
					 * Timestamp(System.currentTimeMillis());
					 * 
					 * String sql = "SELECT plv.M_PriceList_Version_ID " +
					 * "FROM M_PriceList_Version plv " +
					 * "WHERE plv.AD_Client_ID = ? " +
					 * " AND plv.M_PriceList_ID= ? " // 1 +
					 * " AND plv.ValidFrom <= ? " +
					 * "ORDER BY plv.ValidFrom DESC";
					 * 
					 * int M_PriceList_Version_ID = DB.getSQLValueEx(null,
					 * sql,new Object []{AD_Client_ID,M_PriceList_ID, date});
					 * 
					 * String sqlProductPrice =
					 * "SELECT M_ProductPrice_ID FROM M_ProductPrice WHERE AD_Client_ID = ? AND M_PriceList_Version_ID = ? AND M_Product_ID = ?"
					 * ; int M_ProductPrice_ID = DB.getSQLValueEx(null,
					 * sqlProductPrice,new Object
					 * []{AD_Client_ID,M_PriceList_Version_ID, prod.getKey()});
					 * 
					 * MProductPrice newPrice = new MProductPrice(ctx,
					 * M_ProductPrice_ID, null);
					 * newPrice.set_ValueNoCheck("PriceLimit", price);
					 * newPrice.saveEx();
					 */
				} else if (col == 4 && !IsForceLimitPrice) {

					if (price.compareTo(priceLimit) < 0) {
						FDialog.warn(windowNo,null,"","Harga Yang Anda Inputkan Lebih Rendah Dari Limit Harga","Peringantan");
						return;
					}

				}

				if (i == 0) {

					initialDisc = disc;
					initialPrice = totalPrice;
					initialBeforeDisc = beforeDisc;

					totalDisc = initialDisc;
					totalLines = initialPrice;
					totalBeforeDisc = initialBeforeDisc;

				} else {

					totalDisc = initialDisc.add(disc);
					initialDisc = totalDisc;

					totalLines = initialPrice.add(totalPrice);
					initialPrice = totalLines;

					totalBeforeDisc = initialBeforeDisc.add(beforeDisc);
					initialBeforeDisc = totalBeforeDisc;

				}

				totalPrices = totalLines;
				totalDiskons = totalDisc;
				totalBeforeDiscounts = totalBeforeDisc;
				if (totalDiskons.compareTo(Env.ZERO) < 0) {
					totalDiskons = Env.ZERO;
				}

				miniTable.setValueAt(format.format(totalPrice), i, 6);
				miniTable.setValueAt(persentase, i, 5);

			}
		}
	}

	public void reCalculate(IMiniTable miniTable) {

		for (int i = 0; i < miniTable.getRowCount(); i++) {

			BigDecimal qty = (BigDecimal) miniTable.getValueAt(i, 2);
			BigDecimal price = (BigDecimal) miniTable.getValueAt(i, 4);
			BigDecimal priceList = (BigDecimal) miniTable.getValueAt(i, 3);

			BigDecimal totalPrice = qty.multiply(price);
			BigDecimal beforeDisc = qty.multiply(priceList);
			BigDecimal disc = priceList.subtract(price).multiply(qty);

			BigDecimal totalDisc = Env.ZERO;
			BigDecimal totalLines = Env.ZERO;
			BigDecimal totalBeforeDisc = Env.ZERO;

			Double dPersen = ((Double.valueOf(priceList.toString()) - Double
					.valueOf(price.toString())) / Double.valueOf(priceList
					.toString())) * 100;
			BigDecimal persentase = BigDecimal.valueOf(dPersen).setScale(2,
					RoundingMode.HALF_DOWN);
			if (persentase.compareTo(Env.ZERO) < 0) {
				persentase = Env.ZERO;
			}

			if (i == 0) {

				initialDisc = disc;
				initialPrice = totalPrice;
				initialBeforeDisc = beforeDisc;

				totalDisc = initialDisc;
				totalLines = initialPrice;
				totalBeforeDisc = initialBeforeDisc;

			} else {

				totalDisc = initialDisc.add(disc);
				initialDisc = totalDisc;

				totalLines = initialPrice.add(totalPrice);
				initialPrice = totalLines;

				totalBeforeDisc = initialBeforeDisc.add(beforeDisc);
				initialBeforeDisc = totalBeforeDisc;
			}

			totalPrices = totalLines;
			totalDiskons = totalDisc;
			totalBeforeDiscounts = totalBeforeDisc;

			if (totalDiskons.compareTo(Env.ZERO) < 0) {
				totalDiskons = Env.ZERO;
			}

			miniTable.setValueAt(format.format(totalPrice), i, 6);
			miniTable.setValueAt(persentase, i, 5);

		}
	}

	public String checkBeforeProcess(int AD_Org_ID, int M_PriceList_ID,
			int C_BPartner_ID, int M_Warehouse_ID, int C_PaymentTerm_ID,
			int SalesRep_ID, int Kasir_ID, int C_Currency_ID,
			int C_BankAccount_ID, String Pembayaran1, String LeaseProv,
			BigDecimal TipeByarBank, BigDecimal TipeByarLeasing, IMiniTable MiniTable) {

		String msg = "";

		MWarehouse wh = new MWarehouse(ctx, M_Warehouse_ID, null);
		MBPartner bp = new MBPartner(ctx, C_BPartner_ID, null);
		String sqlBPLoc = "SELECT C_BPartner_Location_ID "
				+ " FROM C_BPartner_Location "
				+ " WHERE AD_Client_ID = ? AND C_BPartner_ID =? ";

		int C_BPartner_Location_ID = DB.getSQLValueEx(null, sqlBPLoc,
				new Object[] { AD_Client_ID, C_BPartner_ID });

		if (AD_Org_ID == 0) {
			msg = "Kolom Cabang Harus Di Isi";
		} else if (C_BPartner_ID == 0) {
			msg = "Kolom Pelanggan Belum Di Isi";
		} else if (C_BPartner_Location_ID <= 0) {
			msg = "Belum Ada Alamat Untuk Pelanggan "
					+ bp.getName()
					+ ",Silakan Isikan Alamat Terlebih Dahulu Pada Window Pelanggan";
		} else if (MiniTable.getRowCount() == 0) {
			msg = "Belum Ada Input Produk";
		} else if (M_Warehouse_ID == 0) {
			msg = "Kolom Warehouse Belum Di Isi";
		} else if (wh.getName().toUpperCase().equals("STANDARD")) {
			msg = "Kolom Warehouse Belum Di Isi Dengan Benar";
		} else if (C_PaymentTerm_ID == 0) {
			msg = "Kolom Tempo Belum Di Isi";
		} else if (SalesRep_ID == 0) {
			msg = "Kolom Sales Belum Terisi";
		} else if (Kasir_ID == 0) {
			msg = "Kolom Kasir Belum Terisi";
		} else if (Pembayaran1 == "0.00") {
			msg = "Pembayaran Masih Kosong";
		} else if (C_Currency_ID == 0) {
			msg = "Kolom Mata Uang Belum Terisi";
		} else if (TipeByarLeasing.compareTo(Env.ZERO) >0
				&& LeaseProv == null) {
			msg = "Kolom Leasing Tidak Boleh Kosong Jika Memilih Metode Pembayaran Leasing";
		} else if (TipeByarBank.compareTo(Env.ZERO) >0
				&& C_BankAccount_ID == 0) {
			msg = "Kolom Bank Tidak Boleh Kosong Jika Memilih Metode Pembayaran Bank";
		} else if(MiniTable.getRowCount() > 1){
			
			for (int i = 0 ; i < MiniTable.getRowCount(); i++){
				
				BigDecimal qty= (BigDecimal) MiniTable.getValueAt(i, 2);
				KeyNamePair mproduct_pair =  (KeyNamePair) MiniTable.getValueAt(i, 1);
				Integer mproduct_id = mproduct_pair.getKey();
				if(qty.compareTo(Env.ZERO) ==0){
					MProduct prod = new MProduct(ctx, mproduct_id, null);
					msg = "Qty Untuk Produk "+ prod.getName()+" Tidak Boleh 0";
					
				}
			}
		}
		return msg;

	}

	public void createPOSPayment(int AD_Org_ID, int C_Order_ID,
			int C_POSTenderType_ID, String tenderType, BigDecimal payAmt,
			String leasingProvider, int C_BankAccount_ID) {

		MOrder order = new MOrder(Env.getCtx(), C_Order_ID, null);

		X_C_POSPayment posPayment = new X_C_POSPayment(order.getCtx(), 0,
				order.get_TrxName());

		posPayment.setAD_Org_ID(AD_Org_ID);
		posPayment.setC_Order_ID(C_Order_ID);
		posPayment.setC_POSTenderType_ID(C_POSTenderType_ID);
		posPayment.setTenderType(tenderType);
		posPayment.setPayAmt(payAmt);
		if (tenderType.equals("L")) {
			posPayment.set_CustomColumn("LeaseProvider", leasingProvider);
		}
		posPayment.set_CustomColumn("C_BankAccount_ID", C_BankAccount_ID);
		posPayment.saveEx();
	}

	public void infoGeneratedDocument(int C_Order_ID, int WindowNo) {

		String message = "";
		String sqlInOut = "SELECT M_InOut_ID FROM M_InOut WHERE AD_Client_ID = ? AND C_Order_ID = ?";
		String sqlInvoice = "SELECT C_Invoice_ID FROM C_Invoice WHERE AD_Client_ID = ? AND C_Order_ID = ?";
		String sqlPosPay = "SELECT C_Payment_ID,TenderType,C_BankAccount_ID FROM C_POSPayment WHERE AD_Client_ID = ? AND C_Order_ID = ?";

		int M_InOut_ID = DB.getSQLValueEx(Env.getCtx().toString(),
				sqlInOut.toString(), new Object[] { AD_Client_ID, C_Order_ID });

		Integer C_Invoice_ID = DB.getSQLValueEx(Env.getCtx().toString(),
				sqlInvoice.toString(),
				new Object[] { AD_Client_ID, C_Order_ID });

//		if (C_Invoice_ID == null) {
//			C_Invoice_ID = 0;
//		}

		// Info order
		MOrder order = new MOrder(Env.getCtx(), C_Order_ID, null);
		String noSo = order.getDocumentNo();
		String statusSO = order.getDocStatusName();
		String msgOrder = "No Sales Order : " + noSo + "(" + statusSO + ")";

		// Info Shipment
		String msgInOut = "\n" + "No Surat Jalan : Belum Terbentuk";
		MInOut InOut = new MInOut(Env.getCtx(), M_InOut_ID, null);
		if (M_InOut_ID > 0) {
			String noShip = InOut.getDocumentNo();
			String statusShip = InOut.getDocStatusName();
			msgInOut = "\n" + "No Surat Jalan : " + noShip + "(" + statusShip
					+ ")";
		}
		// Info Invoice
		String msgInv = "\n" + "No Faktur : Belum Terbentuk";
		if (C_Invoice_ID > 0) {
			MInvoice inv = new MInvoice(Env.getCtx(), C_Invoice_ID, null);
			String noInv = inv.getDocumentNo();
			String statusInv = inv.getDocStatusName();
			msgInv = "\n" + "No Faktur : " + noInv + "(" + statusInv + ")";
		}

		// infoResult
		message = msgOrder + msgInOut + msgInv;

		// Info AR Receipt
		String msgAR = "\n" + "No Penerimaan :  Belum Terbentuk ";
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			pstmt = DB.prepareStatement(sqlPosPay.toString(), null);
			pstmt.setInt(1, AD_Client_ID);
			pstmt.setInt(2, C_Order_ID);
			rs = pstmt.executeQuery();
			while (rs.next()) {
				int C_Payment_ID = rs.getInt(1);
				MPayment AR = new MPayment(Env.getCtx(), C_Payment_ID, null);
				String noAR = AR.getDocumentNo();
				String payType = rs.getString(2);
				String payAmt = format.format(AR.getPayAmt());
				String statusAR = AR.getDocStatusName();

				if (payType.equals("L")) {
					payType = "Leasing";
				} else if (payType.equals("X")) {
					
					int C_BankAccount_ID = rs.getInt(3);
					
					MBankAccount ba = new MBankAccount(ctx, C_BankAccount_ID, null);
					
					if(ba.getBankAccountType().equals("B")){
						payType = "Tunai";

					}else{
						payType = "Bank";

					}
					
				}
				
				if (C_Payment_ID > 0) {
					msgAR = "\n" + "No Penerimaan : " + noAR + "(" + payType+ " : " + payAmt + " - " + statusAR + ")";
					message = message + msgAR;
					msgAR = "";
				}
			}

		} catch (SQLException e) {
			log.log(Level.SEVERE, sqlPosPay.toString(), e);
		} finally {
			DB.close(rs, pstmt);
			rs = null;
			pstmt = null;
		}

		String baseMsg = "Transaksi Telah Terproses: \n";
		message = baseMsg+ " \n "+message + msgAR;
		FDialog.info(WindowNo, null, "", message, "Dokumen Terbentuk");

	}

	public int checkShipmentRelated(int C_Order_ID) {
		int M_Inout_ID = 0;
		MOrder asda = new MOrder(ctx, C_Order_ID, null);

		String sqlInOut = "SELECT M_InOut_ID FROM M_InOut WHERE AD_Client_ID = ? AND C_Order_ID = ?";
		M_Inout_ID = DB.getSQLValueEx(asda.get_TrxName(), sqlInOut,
				new Object[] { AD_Client_ID, C_Order_ID });

		if(M_Inout_ID <= 0){
		
		int C_DocTypeShipment_ID = DB.getSQLValue(null,
				"SELECT C_DocTypeShipment_ID FROM C_DocType WHERE C_DocType_ID=?",
				asda.getC_DocType_ID());
		MInOut asd = new MInOut(asda, C_DocTypeShipment_ID, asda.getDateOrdered());
		
		M_Inout_ID = asd.getM_InOut_ID();
		}
		return M_Inout_ID;
	}

	public int checkInvoiceRelated(int C_Order_ID) {
		int C_Invoice_ID = 0;

		String sqlInvoice = "SELECT C_Invoice_ID FROM C_Invoice WHERE AD_Client_ID = ? AND C_Order_ID = ?";
		C_Invoice_ID = DB.getSQLValueEx(Env.getCtx().toString(), sqlInvoice,
				new Object[] { AD_Client_ID, C_Order_ID });

		return C_Invoice_ID;
	}

	public String checkStok(IMiniTable miniTable, int windowNo) {
		String isStocked = "";
		String sqlStock = "SELECT M_Product_ID FROM M_StorageOnHand WHERE AD_Client_ID = ? AND M_Product_ID = ? AND M_Locator_ID = ? AND QtyOnHand > 0";
		String sqlQty = "SELECT SUM(QtyOnHand) FROM M_StorageOnHand WHERE AD_Client_ID = ? AND M_Product_ID = ? AND M_Locator_ID = ?";
		// cek summary product qty
		Map<Integer, BigDecimal> prodMap;
		Map<Integer, Map<Integer, BigDecimal>> prodLocMap;
		prodLocMap = new HashMap<Integer, Map<Integer, BigDecimal>>();

		for (int i = 0; i < miniTable.getRowCount(); i++) {

			KeyNamePair product = (KeyNamePair) miniTable.getValueAt(i, 1);
			KeyNamePair Loc = (KeyNamePair) miniTable.getValueAt(i, 7);
			BigDecimal QtyOrder = (BigDecimal) miniTable.getValueAt(i, 2);

			int product_ID = product.getKey();
			MProduct pro = new MProduct(ctx, product_ID, null);
			int Loc_ID = 0;

			if(pro.getProductType().equals(MProduct.PRODUCTTYPE_Item)){
				
				Loc_ID = Loc.getKey();
			}
			
			if(Loc_ID > 0 ){
				MLocator locator = new MLocator(ctx, Loc_ID, null);
				int result_ID = DB.getSQLValueEx(Env.getCtx().toString(), sqlStock,
						new Object[] { AD_Client_ID, product_ID, Loc_ID });
				BigDecimal QtyStock = DB.getSQLValueBDEx(Env.getCtx().toString(),
						sqlQty, new Object[] { AD_Client_ID, product_ID, Loc_ID });
	
				MProduct prod = new MProduct(ctx, product_ID, null);
				if (prod.getProductType().toUpperCase().equals("I")) {
	
					prodMap = new HashMap<Integer, BigDecimal>();
					if (prodLocMap.isEmpty()) {
						prodMap.put(Loc_ID, QtyOrder);
						prodLocMap.put(product_ID, prodMap);
					}
	
					if (result_ID < 0) {
						isStocked = "Product "
								+ product.getName()
								+ " Tidak Ada Stok Di Lokasi "
								+ locator.getValue()
								+ " Silakan Tidak Menceklist Pick Up Dan Mengisi Tanggal Pengiriman";
					} else if (result_ID > 0) {
	
						if (QtyStock.compareTo(QtyOrder) < 0) {
	
							isStocked = "Stok Product " + product.getName()
									+ " Di Lokasi " + locator.getValue()
									+ " Kurang Dari Qty Penjualan";
	
						}
	
					} else if (prodLocMap.size() >= 1) {
	
						BigDecimal qtySum = Env.ZERO;
	
						if (prodLocMap.containsKey(product_ID)) {
	
							HashMap<Integer, BigDecimal> n = (HashMap<Integer, BigDecimal>) prodLocMap
									.get(product_ID);
	
							if (n.containsKey(Loc_ID)) {
	
								BigDecimal qtyProd = n.get(Loc_ID);
								qtySum = qtyProd.add(QtyOrder);
								n.put(Loc_ID, qtySum);
	
							} else {
								n.put(Loc_ID, QtyOrder);
							}
	
							if (qtySum.compareTo(QtyStock) > 0) {
								isStocked = "Stok Product " + product.getName()
										+ " Di Lokasi " + locator.getValue()
										+ " Kurang Dari Qty Penjualan";
								return isStocked;
							}
						} else {
							prodMap.put(Loc_ID, QtyOrder);
							prodLocMap.put(product_ID, prodMap);
						}
	
					}
					// end
	
				}

		}
	}
		return isStocked;
	}

	public String checkImeiExist(IMiniTable miniTable, int windowNo) {

		String msg = "";

		for (int i = 0; i < miniTable.getRowCount(); i++) {
			boolean IsHaveImei = false;
			KeyNamePair product = (KeyNamePair) miniTable.getValueAt(i, 1);
			String imei = (String) miniTable.getValueAt(i, 8);

			String sqlProduct = "SELECT M_Product_ID FROM M_Product WHERE AD_Client_ID = ? AND name = '"
					+ product.toString() + "'";
			int Product_ID = DB.getSQLValue(ctx.toString(),
					sqlProduct.toString(), new Object[] { AD_Client_ID });

			String sqlChekImei = "SELECT M_AttributeSetInstance_ID FROM M_StorageOnHand "
					+ "WHERE AD_Client_ID = ? AND M_Product_ID = ? AND M_AttributeSetInstance_ID <> 0 AND QtyOnHand > 0";

			int M_AttributeSetInstance_ID = DB.getSQLValueEx(Env.getCtx()
					.toString(), sqlChekImei, new Object[] { AD_Client_ID,
					Product_ID });

			if (M_AttributeSetInstance_ID > 0) {
				IsHaveImei = true;
			}

			if (IsHaveImei) {
				if (imei.equals("-")) {
					FDialog.warn(
							windowNo,
							null,
							"",
							"Produk "
									+ product
									+ " memiliki IMEI, mohon input IMEI terlebih dahulu",
							"Peringatan");
					msg = "Error";
				}
			}

		}

		return msg;
	}

	public String cekBankAccount(String leaseProv) {

		StringBuilder SQLleaseProvider = new StringBuilder();
		Integer C_BankAccount_ID = 0;
		String msg = "";

		SQLleaseProvider.append("SELECT C_BankAccount_ID ");
		SQLleaseProvider.append("FROM C_BankAccount ");
		SQLleaseProvider.append("WHERE AD_Client_ID = ? ");
		SQLleaseProvider.append("AND LeaseProvider = '" + leaseProv + "'");

		C_BankAccount_ID = DB.getSQLValueEx(Env.getCtx().toString(),
				SQLleaseProvider.toString(), new Object[] { AD_Client_ID });

		if (C_BankAccount_ID == null || C_BankAccount_ID == -1) {
			C_BankAccount_ID = 0;
		}

		if (C_BankAccount_ID == 0) {
			msg = "Bank Account Untuk Leasing Yang dipilih Belum Ditentukan";
		}

		return msg;
	}

//	protected Vector<Vector<Object>> getPreSalesData(int C_Decoris_PreSales_ID) {
//
//		if (C_Decoris_PreSales_ID > 0) {
//			data = new Vector<Vector<Object>>();
//			StringBuilder SQLGetPreSalesData = new StringBuilder();
//			
//			X_C_Decoris_PreSales presales = new X_C_Decoris_PreSales(ctx, C_Decoris_PreSales_ID, null);
//
//			SQLGetPreSalesData.append("SELECT M_Product_ID,M_Locator_ID,M_AttributeSetInstance_ID,PriceEntered,PersentaseDiskon,QtyEntered,LineNetAmt,PriceList ");
//			SQLGetPreSalesData.append("FROM C_Decoris_PreSalesLine ");
//			SQLGetPreSalesData.append("WHERE AD_Client_ID = ? ");
//			SQLGetPreSalesData.append("AND C_Decoris_PreSales_ID = ?");
//
//			
//			
//			
//			PreparedStatement pstmt = null;
//			ResultSet rs = null;
//
//			try {
//				pstmt = DB
//						.prepareStatement(SQLGetPreSalesData.toString(), null);
//				pstmt.setInt(1, AD_Client_ID);
//				pstmt.setInt(2, C_Decoris_PreSales_ID);
//				rs = pstmt.executeQuery();
//
//				while (rs.next()) {
//
//					int M_Product_ID = rs.getInt(1);
//					int M_Locator_ID = rs.getInt(2);
//					int M_AttributeSetInstance_ID = rs.getInt(3);
//					String IMEI = "";
//
//					if (M_Product_ID > 0) {
//
//						MProduct prod = new MProduct(ctx, M_Product_ID, null);
//
//						KeyNamePair kp = new KeyNamePair(prod.getM_Product_ID(), prod.getName());
//						KeyNamePair kl = null;
//						if (M_AttributeSetInstance_ID > 0) {
//
//							MAttributeSetInstance imei = new MAttributeSetInstance(
//									ctx, M_AttributeSetInstance_ID, null);
//							IMEI = imei.getSerNo();
//
//						}
//
//						if (M_Locator_ID > 0) {
//
//							MLocator locator = new MLocator(ctx, M_Locator_ID,
//									null);
//							kl = new KeyNamePair(locator.getM_Locator_ID(),
//									locator.getValue());
//
//						}
//						
//						
//						
//						
//						boolean issotrx = true;
//
//	    	    		MProductPricing pp = new MProductPricing(prod.getM_Product_ID(), presales.getC_BPartner_ID(),Env.ONE, issotrx);
//
//	    	    		Timestamp date = new Timestamp(System.currentTimeMillis());
//
//	    	    		String sql = "SELECT plv.M_PriceList_Version_ID "
//	    	    				+ "FROM M_PriceList_Version plv "
//	    	    				+ "WHERE plv.AD_Client_ID = ? " + " AND plv.M_PriceList_ID= ? " // 1
//	    	    				+ " AND plv.ValidFrom <= ? " + "ORDER BY plv.ValidFrom DESC";
//
//	    	    		int M_PriceList_Version_ID = DB.getSQLValueEx(null, sql, new Object[] {presales.getAD_Client_ID(), presales.getM_PriceList_ID(), date });
//
//	    	    		pp.setM_PriceList_Version_ID(M_PriceList_Version_ID);
//	    	    		pp.setPriceDate(date);
//						
//						
//						MTaxCategory taxCat = new MTaxCategory(ctx, prod.getC_TaxCategory_ID(), null);
//
//
//						Vector<Object> line = new Vector<Object>(10);
//
//						line.add(new Boolean(false));
//						line.add(kp);
//						line.add(rs.getBigDecimal(6)); //qty
//						line.add(pp.getPriceList()); //pricelist
//						line.add(rs.getBigDecimal(4)); //price
//						
////						Double dPersen = ((Double.valueOf(pp.getPriceList().toString()) - Double
////								.valueOf(rs.getBigDecimal(4).toString())) / Double.valueOf(pp.getPriceList()
////								.toString())) * 100;
////						BigDecimal persentase = BigDecimal.valueOf(dPersen).setScale(2,
////								RoundingMode.HALF_DOWN);
////						
////						if(persentase.compareTo(Env.ZERO)<0){
////							persentase = Env.ZERO;
////						}
//						
////						line.add(persentase); //diskon
//						line.add(rs.getBigDecimal(5)); //diskon
//
//						line.add(rs.getBigDecimal(7)); //total
//						line.add(kl);	//locator
//						line.add(IMEI); //imei
//						line.add(taxCat.getName());			// 10-TipePajak
//
//						data.add(line);
//					}
//
//				}
//
//			} catch (SQLException e) {
//				log.log(Level.SEVERE, SQLGetPreSalesData.toString(), e);
//			} finally {
//				DB.close(rs, pstmt);
//				rs = null;
//				pstmt = null;
//			}
//
//		}
//
//		return data;
//
//	}

	protected Vector<Vector<Object>> getProductData(int M_Product_ID,
			int M_PriceList_ID, int C_BPartner_ID, int M_Locator_ID,
			int M_AttributeSetInstance_ID, IMiniTable MiniTable) {

//		boolean issotrx = true;
		String IMEI = "-";
		
		Timestamp date = new Timestamp(System.currentTimeMillis());

		String sql = "SELECT plv.M_PriceList_Version_ID "
				+ "FROM M_PriceList_Version plv "
				+ "WHERE plv.AD_Client_ID = ? " + " AND plv.M_PriceList_ID= ? " // 1
				+ " AND plv.ValidFrom <= ? " + "ORDER BY plv.ValidFrom DESC";

		int M_PriceList_Version_ID = DB.getSQLValueEx(null, sql, new Object[] {AD_Client_ID, M_PriceList_ID, date });

	
		//Get ProdPrice_ID
		StringBuilder getProdPrice = new StringBuilder();
		getProdPrice.append("SELECT M_ProductPrice_ID  ");
		getProdPrice.append(" FROM M_ProductPrice  ");
		getProdPrice.append(" WHERE M_Product_ID = "+M_Product_ID);
		getProdPrice.append(" AND M_PriceList_Version_ID = "+M_PriceList_Version_ID);
		getProdPrice.append(" AND AD_Client_ID =  "+AD_Client_ID);

		Integer M_ProductPrice_ID = DB.getSQLValue(null, getProdPrice.toString());
		
		
		
		MProductPrice sad = new MProductPrice(ctx, M_ProductPrice_ID,null);

		System.out.println(sad.getPriceLimit());
		System.out.println(sad.getPriceList());
		System.out.println(sad.getPriceStd());

		
		if (sad.getPriceList().compareTo(Env.ZERO) == 0) {
			Messagebox msgbox = new Messagebox();
			msgbox.show("Error !, Produk Belum Terdaftar Dalam PriceList",
					"Error", Messagebox.OK, Messagebox.ERROR);
			return data;
		}

		if (M_AttributeSetInstance_ID > 0) {

			MAttributeSetInstance imei = new MAttributeSetInstance(
					Env.getCtx(), M_AttributeSetInstance_ID, null);
			IMEI = imei.getSerNo();

		}

		MProduct product = new MProduct(Env.getCtx(), M_Product_ID, null);
		// MTaxCategory taxCat = new MTaxCategory(ctx,
		// product.getC_TaxCategory_ID(), null);

		KeyNamePair kl = null;

		if (M_Locator_ID > 0) {

			MLocator locator = new MLocator(ctx, M_Locator_ID, null);
			kl = new KeyNamePair(locator.getM_Locator_ID(), locator.getValue());

		}
		
		if (product.getProductType().equals("E")) {
			kl = new KeyNamePair(0, "-");

		}

		Vector<Object> line = new Vector<Object>(10);

		KeyNamePair kp = new KeyNamePair(product.getM_Product_ID(),product.getName());
		MTaxCategory taxCat = new MTaxCategory(ctx, product.getC_TaxCategory_ID(), null);

		line.add(new Boolean(false));
		line.add(kp); 										// 1-Product
		line.add(Env.ONE); 									// 2-qty
		line.add(sad.getPriceList()); 						// 3-pricelist
		line.add(sad.getPriceList()); 						// 4-Price
		line.add("0.00"); 									// 5-diskon
		line.add(sad.getPriceList());						// 6-total-price
//		if (!product.getProductType().equals("E")) {
//			line.add(kl);
//		} else if (product.getProductType().equals("E")) {
//			line.add("-");
//		}
		line.add(kl);
		line.add(IMEI); 									// 7-IMEI
		line.add(taxCat.getName());							// 8-TipePajak

	

		totalPrices = totalPrices.add(sad.getPriceList());
		data.add(line);

		reCalculate(MiniTable);

		return data;
	}

	
//	protected Vector<Vector<Object>> getPreOrderData(int C_Decoris_PreOrder_ID) {
//
//		if (C_Decoris_PreOrder_ID > 0) {
//			data = new Vector<Vector<Object>>();
//			StringBuilder SQLGetPreSalesData = new StringBuilder();
//			
//			X_C_Decoris_PreOrder preOrder = new X_C_Decoris_PreOrder(ctx, C_Decoris_PreOrder_ID, null);
//
//			SQLGetPreSalesData.append("SELECT M_Product_ID,M_Locator_ID,M_AttributeSetInstance_ID,PriceEntered,QtyEntered,LineNetAmt ");
//			SQLGetPreSalesData.append("FROM C_Decoris_PreOrderLine ");
//			SQLGetPreSalesData.append("WHERE AD_Client_ID = ? ");
//			SQLGetPreSalesData.append("AND C_Decoris_PreOrder_ID = ?");
//
//
//			PreparedStatement pstmt = null;
//			ResultSet rs = null;
//
//			try {
//				pstmt = DB
//						.prepareStatement(SQLGetPreSalesData.toString(), null);
//				pstmt.setInt(1, AD_Client_ID);
//				pstmt.setInt(2, C_Decoris_PreOrder_ID);
//				rs = pstmt.executeQuery();
//
//				while (rs.next()) {
//
//					int M_Product_ID = rs.getInt(1);
//					int M_Locator_ID = rs.getInt(2);
//					int M_AttributeSetInstance_ID = rs.getInt(3);
//					String IMEI = "";
//
//					if (M_Product_ID > 0) {
//
//						MProduct prod = new MProduct(ctx, M_Product_ID, null);
//
//						KeyNamePair kp = new KeyNamePair(prod.getM_Product_ID(), prod.getName());
//						KeyNamePair kl = null;
//						if (M_AttributeSetInstance_ID > 0) {
//
//							MAttributeSetInstance imei = new MAttributeSetInstance(
//									ctx, M_AttributeSetInstance_ID, null);
//							IMEI = imei.getSerNo();
//
//						}
//
//						if (M_Locator_ID > 0) {
//
//							MLocator locator = new MLocator(ctx, M_Locator_ID,
//									null);
//							kl = new KeyNamePair(locator.getM_Locator_ID(),
//									locator.getValue());
//
//						}
//						
//						
//						
//						
//						boolean issotrx = true;
//
//	    	    		MProductPricing pp = new MProductPricing(prod.getM_Product_ID(), preOrder.getC_BPartner_ID(),Env.ONE, issotrx);
//
//	    	    		Timestamp date = new Timestamp(System.currentTimeMillis());
//
//	    	    		String sql = "SELECT plv.M_PriceList_Version_ID "
//	    	    				+ "FROM M_PriceList_Version plv "
//	    	    				+ "WHERE plv.AD_Client_ID = ? " + " AND plv.M_PriceList_ID= ? " // 1
//	    	    				+ " AND plv.ValidFrom <= ? " + "ORDER BY plv.ValidFrom DESC";
//
//	    	    		int M_PriceList_Version_ID = DB.getSQLValueEx(null, sql, new Object[] {preOrder.getAD_Client_ID(), preOrder.getM_PriceList_ID(), date });
//
//	    	    		pp.setM_PriceList_Version_ID(M_PriceList_Version_ID);
//	    	    		pp.setPriceDate(date);
//						
//						
//						MTaxCategory taxCat = new MTaxCategory(ctx, prod.getC_TaxCategory_ID(), null);
//
//
//						Vector<Object> line = new Vector<Object>(10);
//
//						line.add(new Boolean(false));
//						line.add(kp);
//						line.add(rs.getBigDecimal(5)); //qty
//						line.add(pp.getPriceList()); //pricelist
//						line.add(rs.getBigDecimal(4)); //price
//						line.add(Env.ZERO); //diskon
//						line.add(rs.getBigDecimal(6)); //total
//						line.add(kl);	//locator
//						line.add(IMEI); //imei
//						line.add(taxCat.getName());			// 10-TipePajak
//
//						data.add(line);
//					}
//
//				}
//
//			} catch (SQLException e) {
//				log.log(Level.SEVERE, SQLGetPreSalesData.toString(), e);
//			} finally {
//				DB.close(rs, pstmt);
//				rs = null;
//				pstmt = null;
//			}
//
//		}
//
//		return data;
//
//	}
	
	
	
	protected Vector<Vector<Object>> getOrderOldNota(int C_DecorisPOS_ID) {

		if (C_DecorisPOS_ID > 0) {
			data = new Vector<Vector<Object>>();
			StringBuilder SQLGetPreSalesData = new StringBuilder();
			
			X_SM_SemeruPOS decPOS = new X_SM_SemeruPOS(ctx, C_DecorisPOS_ID, null);

			SQLGetPreSalesData.append("SELECT M_Product_ID,M_Locator_ID,M_AttributeSetInstance_ID,PriceEntered,QtyOrdered,LineNetAmt ");
			SQLGetPreSalesData.append("FROM C_DecorisPOSLine ");
			SQLGetPreSalesData.append("WHERE AD_Client_ID = ? ");
			SQLGetPreSalesData.append("AND C_DecorisPOS_ID = ?");


			PreparedStatement pstmt = null;
			ResultSet rs = null;

			try {
				pstmt = DB
						.prepareStatement(SQLGetPreSalesData.toString(), null);
				pstmt.setInt(1, AD_Client_ID);
				pstmt.setInt(2, C_DecorisPOS_ID);
				rs = pstmt.executeQuery();

				while (rs.next()) {

					int M_Product_ID = rs.getInt(1);
					int M_Locator_ID = rs.getInt(2);
					
					if(M_Locator_ID == 0){
						
						//M_Locator_ID = decPOS.getLocatorNoMulti_ID();
						//temp
					}
					
					int M_AttributeSetInstance_ID = rs.getInt(3);
					String IMEI = "";

					if (M_Product_ID > 0) {

						MProduct prod = new MProduct(ctx, M_Product_ID, null);

						KeyNamePair kp = new KeyNamePair(prod.getM_Product_ID(), prod.getName());
						KeyNamePair kl = null;
						if (M_AttributeSetInstance_ID > 0) {

							MAttributeSetInstance imei = new MAttributeSetInstance(ctx, M_AttributeSetInstance_ID, null);
							IMEI = imei.getSerNo();

						}
						
						if(prod.getProductType().equals(MProduct.PRODUCTTYPE_ExpenseType)){
							kl = new KeyNamePair(0,"-");

						}

						if (M_Locator_ID > 0) {

							MLocator locator = new MLocator(ctx, M_Locator_ID,null);
							kl = new KeyNamePair(locator.getM_Locator_ID(),locator.getValue());

						}
						
						
						
						
//						boolean issotrx = true;

//	    	    		MProductPricing pp = new MProductPricing(prod.getM_Product_ID(), decPOS.getC_BPartner_ID(),Env.ONE, issotrx);

	    	    		Timestamp date = new Timestamp(System.currentTimeMillis());
	    	    		
	    	    		
	    	    		String sqlProductPrice = "SELECT PriceList FROM M_ProductPrice WHERE AD_Client_ID = ? AND M_PriceList_Version_ID = ? AND M_Product_ID = ?";

	    	    		String sql = "SELECT plv.M_PriceList_Version_ID "
	    	    				+ "FROM M_PriceList_Version plv "
	    	    				+ "WHERE plv.AD_Client_ID = ? " + " AND plv.M_PriceList_ID= ? " // 1
	    	    				+ " AND plv.ValidFrom <= ? " + "ORDER BY plv.ValidFrom DESC";

	    	    		int M_PriceList_Version_ID = DB.getSQLValueEx(null, sql, new Object[] {decPOS.getAD_Client_ID(), decPOS.getM_PriceList_ID(), date });

//	    	    		pp.setM_PriceList_Version_ID(M_PriceList_Version_ID);
//	    	    		pp.setPriceDate(date);
	    				BigDecimal priceLimit = DB.getSQLValueBDEx(null,sqlProductPrice, new Object[] { AD_Client_ID,M_PriceList_Version_ID, prod.getM_Product_ID() });

						
						MTaxCategory taxCat = new MTaxCategory(ctx, prod.getC_TaxCategory_ID(), null);


						Vector<Object> line = new Vector<Object>(10);

						line.add(new Boolean(false));
						line.add(kp);
						line.add(rs.getBigDecimal(5)); //qty
						line.add(priceLimit); //pricelist
						line.add(rs.getBigDecimal(4)); //price
						line.add(Env.ZERO); //diskon
						line.add(rs.getBigDecimal(6)); //total
						line.add(kl);	//locator
						line.add(IMEI); //imei
						line.add(taxCat.getName());			// 10-TipePajak

						data.add(line);
					}

				}

			} catch (SQLException e) {
				log.log(Level.SEVERE, SQLGetPreSalesData.toString(), e);
			} finally {
				DB.close(rs, pstmt);
				rs = null;
				pstmt = null;
			}

		}

		return data;

	}
	
	
	
	 protected int updateData(int C_DecorisPOS_ID, boolean isSJ){
		 
			StringBuilder SQLExFunction = new StringBuilder();
			
			if(!isSJ){
				SQLExFunction.append("SELECT update_print_info_pos(?)");
	        }else if(isSJ){
				SQLExFunction.append("SELECT update_print_info_sj(?)");
	        }
	        int rslt = 0;
	         
	     	PreparedStatement pstmt = null;
	     	ResultSet rs = null;
				try {
					pstmt = DB.prepareStatement(SQLExFunction.toString(), null);
					pstmt.setInt(1, C_DecorisPOS_ID);	
					rs = pstmt.executeQuery();
					while (rs.next()) {
						rslt = rs.getInt(1);
					}

				} catch (SQLException err) {
					log.log(Level.SEVERE, SQLExFunction.toString(), err);
				} finally {
					DB.close(rs, pstmt);
					rs = null;
					pstmt = null;
				}
				
				
			 return rslt;
		 }
	 
	 
		protected Vector<Vector<Object>> getProductIMEIData(int M_Product_ID,int M_PriceList_ID, int C_BPartner_ID, int M_Locator_ID,
				int M_AttributeSetInstance_ID, IMiniTable MiniTable, BigDecimal Qty) {

//			boolean issotrx = true;
			String IMEI = "-";
	//		MProductPricing pp = new MProductPricing(M_Product_ID, C_BPartner_ID,Env.ONE, issotrx);

			
			String sqlProductPrice = "SELECT PriceList FROM M_ProductPrice WHERE AD_Client_ID = ? AND M_PriceList_Version_ID = ? AND M_Product_ID = ?";

   
			
			Timestamp date = new Timestamp(System.currentTimeMillis());

			String sql = "SELECT plv.M_PriceList_Version_ID "
					+ "FROM M_PriceList_Version plv "
					+ "WHERE plv.AD_Client_ID = ? " + " AND plv.M_PriceList_ID= ? " // 1
					+ " AND plv.ValidFrom <= ? " + "ORDER BY plv.ValidFrom DESC";

			int M_PriceList_Version_ID = DB.getSQLValueEx(null, sql, new Object[] {AD_Client_ID, M_PriceList_ID, date });

			BigDecimal priceList = DB.getSQLValueBDEx(null,sqlProductPrice, new Object[] { AD_Client_ID,M_PriceList_Version_ID,M_Product_ID });

			
//			pp.setM_PriceList_Version_ID(M_PriceList_Version_ID);
//			pp.setPriceDate(date);

			if (priceList.compareTo(Env.ZERO) == 0) {
				Messagebox msgbox = new Messagebox();
				msgbox.show("Error !, Produk Belum Terdaftar Dalam PriceList","Error", Messagebox.OK, Messagebox.ERROR);
				return data;
			}

			MProduct product = new MProduct(Env.getCtx(), M_Product_ID, null);
			KeyNamePair kl = null;

			if (M_Locator_ID > 0) {

				MLocator locator = new MLocator(ctx, M_Locator_ID, null);
				kl = new KeyNamePair(locator.getM_Locator_ID(), locator.getValue());

			}

			Integer QtyEnter = Qty.intValueExact();

			for (int i = 0 ; i < QtyEnter ; i++){


				Vector<Object> line = new Vector<Object>(10);
				KeyNamePair kp = new KeyNamePair(product.getM_Product_ID(),product.getName());
				MTaxCategory taxCat = new MTaxCategory(ctx, product.getC_TaxCategory_ID(), null);
				line.add(new Boolean(false));
				line.add(kp); 										// 1-Product
				line.add(Env.ONE); 									// 2-qty
				line.add(priceList); 						// 3-pricelist
				line.add(priceList); 						// 4-Price
				line.add("0.00"); 									// 5-diskon
				line.add(priceList);						// 6-total-price
				if (!product.getProductType().equals("E")) {
					line.add(kl);
				} else if (product.getProductType().equals("E")) {
					line.add("");
				}
				line.add(IMEI); 									// 7-IMEI
				line.add(taxCat.getName());							// 8-TipePajak

				
				totalPrices = totalPrices.add(priceList);
				data.add(line);
			}
			
		

			reCalculate(MiniTable);

			return data;
		}
		
		
		protected boolean IsPassStatus(int C_Decoris_PreOrder_ID){
			 
			StringBuilder SQLGetParam = new StringBuilder();
			SQLGetParam.append("SELECT description FROM ad_param WHERE value = 'Approve_PreOrder_FIFAPPS'");
			
			StringBuilder StatusPreOrder = new StringBuilder();
			StatusPreOrder.append(" SELECT state ");
			StatusPreOrder.append(" FROM C_Decoris_PreOrderHDr ");
			StatusPreOrder.append(" WHERE C_Decoris_PreOrder_ID = "+ C_Decoris_PreOrder_ID);       
			String POStatus = DB.getSQLValueStringEx(null, StatusPreOrder.toString());
			
			boolean rslt = false;
			
			if (POStatus == null){
				
				return rslt;
			}
	         
	     	PreparedStatement pstmt = null;
	     	ResultSet rs = null;
				try {
					pstmt = DB.prepareStatement(SQLGetParam.toString(), null);
					rs = pstmt.executeQuery();
					while (rs.next()) {
						
						if(POStatus.trim().toUpperCase().equals(rs.getString(1).trim().toUpperCase())){
							
							rslt = true;
							
						}
						
					}

				} catch (SQLException err) {
					log.log(Level.SEVERE, SQLGetParam.toString(), err);
				} finally {
					DB.close(rs, pstmt);
					rs = null;
					pstmt = null;
				}
				
				
			 return rslt;
		 }
		
		
		protected int mustTunai(){
			int rs = 0;
			final String CusSOCash = "CustomerSOCash";
			StringBuilder cek = new StringBuilder();
			
			
			cek.append("SELECT Description::NUMERIC");
			cek.append(" FROM AD_Param ");
			cek.append(" WHERE AD_Client_ID = ? ");
			cek.append(" AND value = '" +CusSOCash+"'");

			rs = DB.getSQLValueEx(null, cek.toString(), new Object[]{AD_Client_ID});

			
			return rs;
			
		}
		
		protected boolean cekDebitCardAllow(){
			
			boolean isAllow = false;
			
			StringBuilder SQLCek = new StringBuilder();
			
			SQLCek.append("SELECT description::NUMERIC ");
			SQLCek.append("FROM AD_Param ");
			SQLCek.append("WHERE AD_Client_ID = " + AD_Client_ID);
			SQLCek.append("AND Value = 'ChargeSOBankAllowed'");
		
			int rsCek = DB.getSQLValueEx(null, SQLCek.toString());
			if(rsCek > 0){
				
				isAllow = true;
			}
			
			return isAllow;
		}
	
		protected Vector<Vector<Object>> getProductDataDebit(int M_Product_ID,
				int M_PriceList_ID, int C_BPartner_ID, int M_Locator_ID,
				int M_AttributeSetInstance_ID, IMiniTable MiniTable,BigDecimal priceDebitCard) {

//			boolean issotrx = true;
			String IMEI = "-";

			
			Timestamp date = new Timestamp(System.currentTimeMillis());
    		
    		
    		String sqlProductPrice = "SELECT PriceList FROM M_ProductPrice WHERE AD_Client_ID = ? AND M_PriceList_Version_ID = ? AND M_Product_ID = ?";

    		String sql = "SELECT plv.M_PriceList_Version_ID "
    				+ "FROM M_PriceList_Version plv "
    				+ "WHERE plv.AD_Client_ID = ? " + " AND plv.M_PriceList_ID= ? " // 1
    				+ " AND plv.ValidFrom <= ? " + "ORDER BY plv.ValidFrom DESC";

    		int M_PriceList_Version_ID = DB.getSQLValueEx(null, sql, new Object[] {AD_Client_ID, M_PriceList_ID, date });

			BigDecimal priceLimit = DB.getSQLValueBDEx(null,sqlProductPrice, new Object[] { AD_Client_ID,M_PriceList_Version_ID, M_Product_ID});

			if (priceLimit.compareTo(Env.ZERO) == 0) {
				Messagebox msgbox = new Messagebox();
				msgbox.show("Error !, Produk Belum Terdaftar Dalam PriceList",
						"Error", Messagebox.OK, Messagebox.ERROR);
				return data;
			}

			if (M_AttributeSetInstance_ID > 0) {

				MAttributeSetInstance imei = new MAttributeSetInstance(
						Env.getCtx(), M_AttributeSetInstance_ID, null);
				IMEI = imei.getSerNo();

			}

			MProduct product = new MProduct(Env.getCtx(), M_Product_ID, null);
			// MTaxCategory taxCat = new MTaxCategory(ctx,
			// product.getC_TaxCategory_ID(), null);

			KeyNamePair kl = null;

			if (M_Locator_ID > 0) {

				MLocator locator = new MLocator(ctx, M_Locator_ID, null);
				kl = new KeyNamePair(locator.getM_Locator_ID(), locator.getValue());

			}
			
			if (product.getProductType().equals("E")) {
				kl = new KeyNamePair(0, "-");

			}

			Vector<Object> line = new Vector<Object>(10);

			KeyNamePair kp = new KeyNamePair(product.getM_Product_ID(),product.getName());
			MTaxCategory taxCat = new MTaxCategory(ctx, product.getC_TaxCategory_ID(), null);

			
			line.add(new Boolean(false));
			line.add(kp); 										// 1-Product
			line.add(Env.ONE); 									// 2-qty
			line.add(priceLimit); 						// 3-pricelist
			line.add(priceDebitCard); 						// 4-Price
			line.add("0.00"); 									// 5-diskon
			line.add(priceDebitCard.multiply(Env.ONE));						// 6-total-price
//			if (!product.getProductType().equals("E")) {
//				line.add(kl);
//			} else if (product.getProductType().equals("E")) {
//				line.add("-");
//			}
			line.add(kl);
			line.add(IMEI); 									// 7-IMEI
			line.add(taxCat.getName());							// 8-TipePajak

		

			totalPrices = totalPrices.add(priceLimit);
			data.add(line);

			reCalculate(MiniTable);

			return data;
		}
		
		protected boolean cekAllowOldNota (int AD_Client_ID){
			boolean rs = false;
			
			StringBuilder SQLCekAllowOlNota = new StringBuilder();
			SQLCekAllowOlNota.append("SELECT Description::Numeric ");
			SQLCekAllowOlNota.append(" FROM AD_Param");
			SQLCekAllowOlNota.append(" WHERE AD_Client_ID = " + AD_Client_ID);
			SQLCekAllowOlNota.append(" AND Value = '"+"GetOldNotaToSOAllowed"+"'" );
			
			Integer valid = DB.getSQLValueEx(null, SQLCekAllowOlNota.toString());
			
			
			if(valid == 1){
				
				rs = true;
			}
			
			return rs;
			
		}
		
		
		protected Integer cekRMAExist(int AD_Client_ID,int C_DecorisPOS_ID){
			
			Integer rs = 0;
			
			StringBuilder SQLCekRMAExist = new StringBuilder();
			SQLCekRMAExist.append("SELECT rma.M_RMA_ID ");
			SQLCekRMAExist.append(" FROM C_DecorisPOS pos");
			SQLCekRMAExist.append(" LEFT JOIN  M_InOut mio ON pos.C_Order_ID = mio.C_Order_ID");
			SQLCekRMAExist.append(" LEFT JOIN  M_RMA rma ON mio.M_InOut_ID = rma.InOut_ID" );
			SQLCekRMAExist.append(" WHERE pos.AD_Client_ID =  "+AD_Client_ID);
			SQLCekRMAExist.append(" AND pos.C_DecorisPOS_ID =  "+C_DecorisPOS_ID);
			SQLCekRMAExist.append(" AND rma.DocStatus = 'CO'" );
			
			rs = DB.getSQLValueEx(null, SQLCekRMAExist.toString());
			
			if(rs == null || rs < 0){	
				rs = 0;
			}
			
			return rs ;	
		}
		
		protected int cekBankAcctLeasing(int AD_Client_ID, String LeasingPeovider){
			
			int rs = 0;
			
			StringBuilder SQLCekBankLeasing = new StringBuilder();
			SQLCekBankLeasing.append("SELECT COUNT(c_bankaccount_id)");
			SQLCekBankLeasing.append(" FROM c_bankaccount_leaseprovider");
			SQLCekBankLeasing.append(" WHERE AD_Client_ID = "+ AD_Client_ID);
			SQLCekBankLeasing.append(" AND leaseprovider = '" + LeasingPeovider + "'");
			SQLCekBankLeasing.append(" AND isactive = 'Y'");
			
			rs = DB.getSQLValueEx(null, SQLCekBankLeasing.toString());
			
			return rs;
			
		}
		
		
		protected int getBankAcctLeasing(int AD_Client_ID, String LeasingProvider){
			
			int rs = 0;
			
			StringBuilder SQLCekBankLeasing = new StringBuilder();
			SQLCekBankLeasing.append("SELECT C_BankAccount_ID");
			SQLCekBankLeasing.append(" FROM c_bankaccount_leaseprovider");
			SQLCekBankLeasing.append(" WHERE AD_Client_ID = "+ AD_Client_ID);
			SQLCekBankLeasing.append(" AND leaseprovider = '" + LeasingProvider + "'");
			SQLCekBankLeasing.append(" AND isactive = 'Y'");
			
			rs = DB.getSQLValueEx(null, SQLCekBankLeasing.toString());
			
			return rs;
			
		}
		
		protected Integer getPaymentTerm(int AD_Client_ID){
			Integer C_PayTerm_ID = 0;
			
			StringBuilder SQLGetTerm = new StringBuilder();
			SQLGetTerm.append(" SELECT description::numeric");
			SQLGetTerm.append(" FROM ad_param");
			SQLGetTerm.append(" WHERE AD_Client_ID = "+AD_Client_ID); 
			SQLGetTerm.append(" AND value = 'DefaultPymTermSODebt'"); 
			SQLGetTerm.append(" AND isactive = 'Y'");
			 
			C_PayTerm_ID = DB.getSQLValueEx(null, SQLGetTerm.toString());
			
			return C_PayTerm_ID;
		}
		
}
