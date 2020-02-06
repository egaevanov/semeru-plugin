/******************************************************************************
 * Product: iDempiere ERP & CRM Smart Business Solution                       *
 * Copyright (C) 1999-2012 ComPiere, Inc. All Rights Reserved.                *
 * This program is free software, you can redistribute it and/or modify it    *
 * under the terms version 2 of the GNU General Public License as published   *
 * by the Free Software Foundation. This program is distributed in the hope   *
 * that it will be useful, but WITHOUT ANY WARRANTY, without even the implied *
 * warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.           *
 * See the GNU General Public License for more details.                       *
 * You should have received a copy of the GNU General Public License along    *
 * with this program, if not, write to the Free Software Foundation, Inc.,    *
 * 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA.                     *
 * For the text or an alternative of this public license, you may reach us    *
 * ComPiere, Inc., 2620 Augustine Dr. #245, Santa Clara, CA 95054, USA        *
 * or via info@compiere.org or http://www.compiere.org/license.html           *
 *****************************************************************************/
package org.semeru.pos.model;

import java.math.BigDecimal;
import java.sql.Timestamp;
import org.compiere.model.*;
import org.compiere.util.KeyNamePair;

/** Generated Interface for SM_SemeruTemp
 *  @author iDempiere (generated) 
 *  @version Release 6.2
 */
@SuppressWarnings("all")
public interface I_SM_SemeruTemp 
{

    /** TableName=SM_SemeruTemp */
    public static final String Table_Name = "SM_SemeruTemp";

    /** AD_Table_ID=1000108 */
    public static final int Table_ID = MTable.getTable_ID(Table_Name);

    KeyNamePair Model = new KeyNamePair(Table_ID, Table_Name);

    /** AccessLevel = 3 - Client - Org 
     */
    BigDecimal accessLevel = BigDecimal.valueOf(3);

    /** Load Meta Data */

    /** Column name AD_Client_ID */
    public static final String COLUMNNAME_AD_Client_ID = "AD_Client_ID";

	/** Get Client.
	  * Client/Tenant for this installation.
	  */
	public int getAD_Client_ID();

    /** Column name AD_Org_ID */
    public static final String COLUMNNAME_AD_Org_ID = "AD_Org_ID";

	/** Set Organization.
	  * Organizational entity within client
	  */
	public void setAD_Org_ID (int AD_Org_ID);

	/** Get Organization.
	  * Organizational entity within client
	  */
	public int getAD_Org_ID();

    /** Column name ApproveDate */
    public static final String COLUMNNAME_ApproveDate = "ApproveDate";

	/** Set ApproveDate	  */
	public void setApproveDate (Timestamp ApproveDate);

	/** Get ApproveDate	  */
	public Timestamp getApproveDate();

    /** Column name C_BankAccount_ID */
    public static final String COLUMNNAME_C_BankAccount_ID = "C_BankAccount_ID";

	/** Set Bank Account.
	  * Account at the Bank
	  */
	public void setC_BankAccount_ID (int C_BankAccount_ID);

	/** Get Bank Account.
	  * Account at the Bank
	  */
	public int getC_BankAccount_ID();

	public org.compiere.model.I_C_BankAccount getC_BankAccount() throws RuntimeException;

    /** Column name C_BPartner_ID */
    public static final String COLUMNNAME_C_BPartner_ID = "C_BPartner_ID";

	/** Set Business Partner .
	  * Identifies a Business Partner
	  */
	public void setC_BPartner_ID (int C_BPartner_ID);

	/** Get Business Partner .
	  * Identifies a Business Partner
	  */
	public int getC_BPartner_ID();

	public org.compiere.model.I_C_BPartner getC_BPartner() throws RuntimeException;

    /** Column name C_BPartner_Location_ID */
    public static final String COLUMNNAME_C_BPartner_Location_ID = "C_BPartner_Location_ID";

	/** Set Partner Location.
	  * Identifies the (ship to) address for this Business Partner
	  */
	public void setC_BPartner_Location_ID (int C_BPartner_Location_ID);

	/** Get Partner Location.
	  * Identifies the (ship to) address for this Business Partner
	  */
	public int getC_BPartner_Location_ID();

	public org.compiere.model.I_C_BPartner_Location getC_BPartner_Location() throws RuntimeException;

    /** Column name C_DocType_ID */
    public static final String COLUMNNAME_C_DocType_ID = "C_DocType_ID";

	/** Set Document Type.
	  * Document type or rules
	  */
	public void setC_DocType_ID (int C_DocType_ID);

	/** Get Document Type.
	  * Document type or rules
	  */
	public int getC_DocType_ID();

	public org.compiere.model.I_C_DocType getC_DocType() throws RuntimeException;

    /** Column name C_Invoice_ID */
    public static final String COLUMNNAME_C_Invoice_ID = "C_Invoice_ID";

	/** Set Invoice.
	  * Invoice Identifier
	  */
	public void setC_Invoice_ID (int C_Invoice_ID);

	/** Get Invoice.
	  * Invoice Identifier
	  */
	public int getC_Invoice_ID();

	public org.compiere.model.I_C_Invoice getC_Invoice() throws RuntimeException;

    /** Column name C_Order_ID */
    public static final String COLUMNNAME_C_Order_ID = "C_Order_ID";

	/** Set Order.
	  * Order
	  */
	public void setC_Order_ID (int C_Order_ID);

	/** Get Order.
	  * Order
	  */
	public int getC_Order_ID();

	public org.compiere.model.I_C_Order getC_Order() throws RuntimeException;

    /** Column name C_Payment_ID */
    public static final String COLUMNNAME_C_Payment_ID = "C_Payment_ID";

	/** Set Payment.
	  * Payment identifier
	  */
	public void setC_Payment_ID (int C_Payment_ID);

	/** Get Payment.
	  * Payment identifier
	  */
	public int getC_Payment_ID();

	public org.compiere.model.I_C_Payment getC_Payment() throws RuntimeException;

    /** Column name C_PaymentTerm_ID */
    public static final String COLUMNNAME_C_PaymentTerm_ID = "C_PaymentTerm_ID";

	/** Set Payment Term.
	  * The terms of Payment (timing, discount)
	  */
	public void setC_PaymentTerm_ID (int C_PaymentTerm_ID);

	/** Get Payment Term.
	  * The terms of Payment (timing, discount)
	  */
	public int getC_PaymentTerm_ID();

	public org.compiere.model.I_C_PaymentTerm getC_PaymentTerm() throws RuntimeException;

    /** Column name Created */
    public static final String COLUMNNAME_Created = "Created";

	/** Get Created.
	  * Date this record was created
	  */
	public Timestamp getCreated();

    /** Column name CreatedBy */
    public static final String COLUMNNAME_CreatedBy = "CreatedBy";

	/** Get Created By.
	  * User who created this records
	  */
	public int getCreatedBy();

    /** Column name CreatedByPos_ID */
    public static final String COLUMNNAME_CreatedByPos_ID = "CreatedByPos_ID";

	/** Set CreatedByPos_ID	  */
	public void setCreatedByPos_ID (int CreatedByPos_ID);

	/** Get CreatedByPos_ID	  */
	public int getCreatedByPos_ID();

	public org.compiere.model.I_C_BPartner getCreatedByPos() throws RuntimeException;

    /** Column name DateOrdered */
    public static final String COLUMNNAME_DateOrdered = "DateOrdered";

	/** Set Date Ordered.
	  * Date of Order
	  */
	public void setDateOrdered (Timestamp DateOrdered);

	/** Get Date Ordered.
	  * Date of Order
	  */
	public Timestamp getDateOrdered();

    /** Column name DateTrx */
    public static final String COLUMNNAME_DateTrx = "DateTrx";

	/** Set Transaction Date.
	  * Transaction Date
	  */
	public void setDateTrx (Timestamp DateTrx);

	/** Get Transaction Date.
	  * Transaction Date
	  */
	public Timestamp getDateTrx();

    /** Column name DeliveryRule */
    public static final String COLUMNNAME_DeliveryRule = "DeliveryRule";

	/** Set Delivery Rule.
	  * Defines the timing of Delivery
	  */
	public void setDeliveryRule (String DeliveryRule);

	/** Get Delivery Rule.
	  * Defines the timing of Delivery
	  */
	public String getDeliveryRule();

    /** Column name DeliveryViaRule */
    public static final String COLUMNNAME_DeliveryViaRule = "DeliveryViaRule";

	/** Set Delivery Via.
	  * How the order will be delivered
	  */
	public void setDeliveryViaRule (String DeliveryViaRule);

	/** Get Delivery Via.
	  * How the order will be delivered
	  */
	public String getDeliveryViaRule();

    /** Column name Description */
    public static final String COLUMNNAME_Description = "Description";

	/** Set Description.
	  * Optional short description of the record
	  */
	public void setDescription (String Description);

	/** Get Description.
	  * Optional short description of the record
	  */
	public String getDescription();

    /** Column name DiscountAmt */
    public static final String COLUMNNAME_DiscountAmt = "DiscountAmt";

	/** Set Discount Amount.
	  * Calculated amount of discount
	  */
	public void setDiscountAmt (BigDecimal DiscountAmt);

	/** Get Discount Amount.
	  * Calculated amount of discount
	  */
	public BigDecimal getDiscountAmt();

    /** Column name DocumentNo */
    public static final String COLUMNNAME_DocumentNo = "DocumentNo";

	/** Set Document No.
	  * Document sequence number of the document
	  */
	public void setDocumentNo (String DocumentNo);

	/** Get Document No.
	  * Document sequence number of the document
	  */
	public String getDocumentNo();

    /** Column name DocumentNoTutupKas */
    public static final String COLUMNNAME_DocumentNoTutupKas = "DocumentNoTutupKas";

	/** Set DocumentNo Tutup Kas	  */
	public void setDocumentNoTutupKas (String DocumentNoTutupKas);

	/** Get DocumentNo Tutup Kas	  */
	public String getDocumentNoTutupKas();

    /** Column name dpp */
    public static final String COLUMNNAME_dpp = "dpp";

	/** Set dpp	  */
	public void setdpp (BigDecimal dpp);

	/** Get dpp	  */
	public BigDecimal getdpp();

    /** Column name GrandTotal */
    public static final String COLUMNNAME_GrandTotal = "GrandTotal";

	/** Set Grand Total.
	  * Total amount of document
	  */
	public void setGrandTotal (BigDecimal GrandTotal);

	/** Get Grand Total.
	  * Total amount of document
	  */
	public BigDecimal getGrandTotal();

    /** Column name IsActive */
    public static final String COLUMNNAME_IsActive = "IsActive";

	/** Set Active.
	  * The record is active in the system
	  */
	public void setIsActive (boolean IsActive);

	/** Get Active.
	  * The record is active in the system
	  */
	public boolean isActive();

    /** Column name IsDebitCard */
    public static final String COLUMNNAME_IsDebitCard = "IsDebitCard";

	/** Set IsDebitCard	  */
	public void setIsDebitCard (boolean IsDebitCard);

	/** Get IsDebitCard	  */
	public boolean isDebitCard();

    /** Column name IsLeasing */
    public static final String COLUMNNAME_IsLeasing = "IsLeasing";

	/** Set IsLeasing	  */
	public void setIsLeasing (boolean IsLeasing);

	/** Get IsLeasing	  */
	public boolean isLeasing();

    /** Column name IsManualDocumentNo */
    public static final String COLUMNNAME_IsManualDocumentNo = "IsManualDocumentNo";

	/** Set IsManualDocumentNo	  */
	public void setIsManualDocumentNo (boolean IsManualDocumentNo);

	/** Get IsManualDocumentNo	  */
	public boolean isManualDocumentNo();

    /** Column name IsMultiLocator */
    public static final String COLUMNNAME_IsMultiLocator = "IsMultiLocator";

	/** Set IsMultiLocator	  */
	public void setIsMultiLocator (boolean IsMultiLocator);

	/** Get IsMultiLocator	  */
	public boolean isMultiLocator();

    /** Column name IsPembatalan */
    public static final String COLUMNNAME_IsPembatalan = "IsPembatalan";

	/** Set IsPembatalan	  */
	public void setIsPembatalan (boolean IsPembatalan);

	/** Get IsPembatalan	  */
	public boolean isPembatalan();

    /** Column name IsPembayaran */
    public static final String COLUMNNAME_IsPembayaran = "IsPembayaran";

	/** Set IsPembayaran	  */
	public void setIsPembayaran (boolean IsPembayaran);

	/** Get IsPembayaran	  */
	public boolean isPembayaran();

    /** Column name IsPenjualan */
    public static final String COLUMNNAME_IsPenjualan = "IsPenjualan";

	/** Set IsPenjualan	  */
	public void setIsPenjualan (boolean IsPenjualan);

	/** Get IsPenjualan	  */
	public boolean isPenjualan();

    /** Column name IsPickup */
    public static final String COLUMNNAME_IsPickup = "IsPickup";

	/** Set IsPickup	  */
	public void setIsPickup (boolean IsPickup);

	/** Get IsPickup	  */
	public boolean isPickup();

    /** Column name IsPrinted */
    public static final String COLUMNNAME_IsPrinted = "IsPrinted";

	/** Set Printed.
	  * Indicates if this document / line is printed
	  */
	public void setIsPrinted (boolean IsPrinted);

	/** Get Printed.
	  * Indicates if this document / line is printed
	  */
	public boolean isPrinted();

    /** Column name IsPrintedSJ */
    public static final String COLUMNNAME_IsPrintedSJ = "IsPrintedSJ";

	/** Set IsPrintedSJ	  */
	public void setIsPrintedSJ (boolean IsPrintedSJ);

	/** Get IsPrintedSJ	  */
	public boolean isPrintedSJ();

    /** Column name IsReceipt */
    public static final String COLUMNNAME_IsReceipt = "IsReceipt";

	/** Set Receipt.
	  * This is a sales transaction (receipt)
	  */
	public void setIsReceipt (boolean IsReceipt);

	/** Get Receipt.
	  * This is a sales transaction (receipt)
	  */
	public boolean isReceipt();

    /** Column name IsReturn */
    public static final String COLUMNNAME_IsReturn = "IsReturn";

	/** Set IsReturn	  */
	public void setIsReturn (boolean IsReturn);

	/** Get IsReturn	  */
	public boolean isReturn();

    /** Column name IsSOTrx */
    public static final String COLUMNNAME_IsSOTrx = "IsSOTrx";

	/** Set Sales Transaction.
	  * This is a Sales Transaction
	  */
	public void setIsSOTrx (boolean IsSOTrx);

	/** Get Sales Transaction.
	  * This is a Sales Transaction
	  */
	public boolean isSOTrx();

    /** Column name IsSpektra */
    public static final String COLUMNNAME_IsSpektra = "IsSpektra";

	/** Set IsSpektra	  */
	public void setIsSpektra (boolean IsSpektra);

	/** Get IsSpektra	  */
	public boolean isSpektra();

    /** Column name IsTutupKas */
    public static final String COLUMNNAME_IsTutupKas = "IsTutupKas";

	/** Set IsTutupKas	  */
	public void setIsTutupKas (boolean IsTutupKas);

	/** Get IsTutupKas	  */
	public boolean isTutupKas();

    /** Column name LeaseProvider */
    public static final String COLUMNNAME_LeaseProvider = "LeaseProvider";

	/** Set LeaseProvider	  */
	public void setLeaseProvider (String LeaseProvider);

	/** Get LeaseProvider	  */
	public String getLeaseProvider();

    /** Column name LocatorNoMulti_ID */
    public static final String COLUMNNAME_LocatorNoMulti_ID = "LocatorNoMulti_ID";

	/** Set LocatorNoMulti_ID	  */
	public void setLocatorNoMulti_ID (int LocatorNoMulti_ID);

	/** Get LocatorNoMulti_ID	  */
	public int getLocatorNoMulti_ID();

    /** Column name M_PriceList_ID */
    public static final String COLUMNNAME_M_PriceList_ID = "M_PriceList_ID";

	/** Set Price List.
	  * Unique identifier of a Price List
	  */
	public void setM_PriceList_ID (int M_PriceList_ID);

	/** Get Price List.
	  * Unique identifier of a Price List
	  */
	public int getM_PriceList_ID();

	public org.compiere.model.I_M_PriceList getM_PriceList() throws RuntimeException;

    /** Column name M_Warehouse_ID */
    public static final String COLUMNNAME_M_Warehouse_ID = "M_Warehouse_ID";

	/** Set Warehouse.
	  * Storage Warehouse and Service Point
	  */
	public void setM_Warehouse_ID (int M_Warehouse_ID);

	/** Get Warehouse.
	  * Storage Warehouse and Service Point
	  */
	public int getM_Warehouse_ID();

	public org.compiere.model.I_M_Warehouse getM_Warehouse() throws RuntimeException;

    /** Column name OrderDocType_ID */
    public static final String COLUMNNAME_OrderDocType_ID = "OrderDocType_ID";

	/** Set OrderDocType_ID	  */
	public void setOrderDocType_ID (int OrderDocType_ID);

	/** Get OrderDocType_ID	  */
	public int getOrderDocType_ID();

    /** Column name PaymentRule */
    public static final String COLUMNNAME_PaymentRule = "PaymentRule";

	/** Set Payment Rule.
	  * How you pay the invoice
	  */
	public void setPaymentRule (String PaymentRule);

	/** Get Payment Rule.
	  * How you pay the invoice
	  */
	public String getPaymentRule();

    /** Column name PayType1 */
    public static final String COLUMNNAME_PayType1 = "PayType1";

	/** Set PayType1	  */
	public void setPayType1 (String PayType1);

	/** Get PayType1	  */
	public String getPayType1();

    /** Column name PayType2 */
    public static final String COLUMNNAME_PayType2 = "PayType2";

	/** Set PayType2	  */
	public void setPayType2 (String PayType2);

	/** Get PayType2	  */
	public String getPayType2();

    /** Column name PayType3 */
    public static final String COLUMNNAME_PayType3 = "PayType3";

	/** Set PayType3	  */
	public void setPayType3 (String PayType3);

	/** Get PayType3	  */
	public String getPayType3();

    /** Column name PayType4 */
    public static final String COLUMNNAME_PayType4 = "PayType4";

	/** Set PayType4	  */
	public void setPayType4 (String PayType4);

	/** Get PayType4	  */
	public String getPayType4();

    /** Column name Pembayaran1 */
    public static final String COLUMNNAME_Pembayaran1 = "Pembayaran1";

	/** Set Pembayaran1	  */
	public void setPembayaran1 (BigDecimal Pembayaran1);

	/** Get Pembayaran1	  */
	public BigDecimal getPembayaran1();

    /** Column name Pembayaran2 */
    public static final String COLUMNNAME_Pembayaran2 = "Pembayaran2";

	/** Set Pembayaran2	  */
	public void setPembayaran2 (BigDecimal Pembayaran2);

	/** Get Pembayaran2	  */
	public BigDecimal getPembayaran2();

    /** Column name Pembayaran3 */
    public static final String COLUMNNAME_Pembayaran3 = "Pembayaran3";

	/** Set Pembayaran3	  */
	public void setPembayaran3 (BigDecimal Pembayaran3);

	/** Get Pembayaran3	  */
	public BigDecimal getPembayaran3();

    /** Column name Pembayaran4 */
    public static final String COLUMNNAME_Pembayaran4 = "Pembayaran4";

	/** Set Pembayaran4	  */
	public void setPembayaran4 (BigDecimal Pembayaran4);

	/** Get Pembayaran4	  */
	public BigDecimal getPembayaran4();

    /** Column name Processed */
    public static final String COLUMNNAME_Processed = "Processed";

	/** Set Processed.
	  * The document has been processed
	  */
	public void setProcessed (boolean Processed);

	/** Get Processed.
	  * The document has been processed
	  */
	public boolean isProcessed();

    /** Column name SalesRep_ID */
    public static final String COLUMNNAME_SalesRep_ID = "SalesRep_ID";

	/** Set Sales Representative.
	  * Sales Representative or Company Agent
	  */
	public void setSalesRep_ID (int SalesRep_ID);

	/** Get Sales Representative.
	  * Sales Representative or Company Agent
	  */
	public int getSalesRep_ID();

	public org.compiere.model.I_AD_User getSalesRep() throws RuntimeException;

    /** Column name SeqTutupKas */
    public static final String COLUMNNAME_SeqTutupKas = "SeqTutupKas";

	/** Set SeqTutupKas	  */
	public void setSeqTutupKas (int SeqTutupKas);

	/** Get SeqTutupKas	  */
	public int getSeqTutupKas();

    /** Column name SM_Semeru_CloseCash_ID */
    public static final String COLUMNNAME_SM_Semeru_CloseCash_ID = "SM_Semeru_CloseCash_ID";

	/** Set SM_Semeru_CloseCash	  */
	public void setSM_Semeru_CloseCash_ID (int SM_Semeru_CloseCash_ID);

	/** Get SM_Semeru_CloseCash	  */
	public int getSM_Semeru_CloseCash_ID();

	public I_SM_Semeru_CloseCash getSM_Semeru_CloseCash() throws RuntimeException;

    /** Column name SM_SemeruPOS_ID */
    public static final String COLUMNNAME_SM_SemeruPOS_ID = "SM_SemeruPOS_ID";

	/** Set SM_SemeruPOS	  */
	public void setSM_SemeruPOS_ID (int SM_SemeruPOS_ID);

	/** Get SM_SemeruPOS	  */
	public int getSM_SemeruPOS_ID();

	public I_SM_SemeruPOS getSM_SemeruPOS() throws RuntimeException;

    /** Column name SM_SemeruTemp_ID */
    public static final String COLUMNNAME_SM_SemeruTemp_ID = "SM_SemeruTemp_ID";

	/** Set SM_SemeruTemp_ID	  */
	public void setSM_SemeruTemp_ID (int SM_SemeruTemp_ID);

	/** Get SM_SemeruTemp_ID	  */
	public int getSM_SemeruTemp_ID();

	public I_SM_SemeruTemp getSM_SemeruTemp() throws RuntimeException;

    /** Column name SM_SemeruTemp_UU */
    public static final String COLUMNNAME_SM_SemeruTemp_UU = "SM_SemeruTemp_UU";

	/** Set SM_SemeruTemp_UU	  */
	public void setSM_SemeruTemp_UU (String SM_SemeruTemp_UU);

	/** Get SM_SemeruTemp_UU	  */
	public String getSM_SemeruTemp_UU();

    /** Column name Supervisor_ID */
    public static final String COLUMNNAME_Supervisor_ID = "Supervisor_ID";

	/** Set Supervisor.
	  * Supervisor for this user/organization - used for escalation and approval
	  */
	public void setSupervisor_ID (int Supervisor_ID);

	/** Get Supervisor.
	  * Supervisor for this user/organization - used for escalation and approval
	  */
	public int getSupervisor_ID();

	public org.compiere.model.I_AD_User getSupervisor() throws RuntimeException;

    /** Column name TaxAmt */
    public static final String COLUMNNAME_TaxAmt = "TaxAmt";

	/** Set Tax Amount.
	  * Tax Amount for a document
	  */
	public void setTaxAmt (BigDecimal TaxAmt);

	/** Get Tax Amount.
	  * Tax Amount for a document
	  */
	public BigDecimal getTaxAmt();

    /** Column name TotalKembalian */
    public static final String COLUMNNAME_TotalKembalian = "TotalKembalian";

	/** Set TotalKembalian	  */
	public void setTotalKembalian (BigDecimal TotalKembalian);

	/** Get TotalKembalian	  */
	public BigDecimal getTotalKembalian();

    /** Column name TotalLines */
    public static final String COLUMNNAME_TotalLines = "TotalLines";

	/** Set Total Lines.
	  * Total of all document lines
	  */
	public void setTotalLines (BigDecimal TotalLines);

	/** Get Total Lines.
	  * Total of all document lines
	  */
	public BigDecimal getTotalLines();

    /** Column name TotalTunai */
    public static final String COLUMNNAME_TotalTunai = "TotalTunai";

	/** Set TotalTunai	  */
	public void setTotalTunai (BigDecimal TotalTunai);

	/** Get TotalTunai	  */
	public BigDecimal getTotalTunai();

    /** Column name Updated */
    public static final String COLUMNNAME_Updated = "Updated";

	/** Get Updated.
	  * Date this record was updated
	  */
	public Timestamp getUpdated();

    /** Column name UpdatedBy */
    public static final String COLUMNNAME_UpdatedBy = "UpdatedBy";

	/** Get Updated By.
	  * User who updated this records
	  */
	public int getUpdatedBy();
}
