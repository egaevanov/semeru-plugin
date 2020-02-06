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
/** Generated Model - DO NOT CHANGE */
package org.semeru.pos.model;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.Properties;
import org.compiere.model.*;
import org.compiere.util.Env;

/** Generated Model for SM_SemeruPOS
 *  @author iDempiere (generated) 
 *  @version Release 6.2 - $Id$ */
public class X_SM_SemeruPOS extends PO implements I_SM_SemeruPOS, I_Persistent 
{

	/**
	 *
	 */
	private static final long serialVersionUID = 20190920L;

    /** Standard Constructor */
    public X_SM_SemeruPOS (Properties ctx, int SM_SemeruPOS_ID, String trxName)
    {
      super (ctx, SM_SemeruPOS_ID, trxName);
      /** if (SM_SemeruPOS_ID == 0)
        {
			setPrintSequence (0);
			setPrintSequenceSJ (0);
			setSM_SemeruPOS_ID (0);
        } */
    }

    /** Load Constructor */
    public X_SM_SemeruPOS (Properties ctx, ResultSet rs, String trxName)
    {
      super (ctx, rs, trxName);
    }

    /** AccessLevel
      * @return 3 - Client - Org 
      */
    protected int get_AccessLevel()
    {
      return accessLevel.intValue();
    }

    /** Load Meta Data */
    protected POInfo initPO (Properties ctx)
    {
      POInfo poi = POInfo.getPOInfo (ctx, Table_ID, get_TrxName());
      return poi;
    }

    public String toString()
    {
      StringBuffer sb = new StringBuffer ("X_SM_SemeruPOS[")
        .append(get_ID()).append("]");
      return sb.toString();
    }

	/** Set ApproveDate.
		@param ApproveDate ApproveDate	  */
	public void setApproveDate (Timestamp ApproveDate)
	{
		set_Value (COLUMNNAME_ApproveDate, ApproveDate);
	}

	/** Get ApproveDate.
		@return ApproveDate	  */
	public Timestamp getApproveDate () 
	{
		return (Timestamp)get_Value(COLUMNNAME_ApproveDate);
	}

	public org.compiere.model.I_C_BankAccount getC_BankAccount() throws RuntimeException
    {
		return (org.compiere.model.I_C_BankAccount)MTable.get(getCtx(), org.compiere.model.I_C_BankAccount.Table_Name)
			.getPO(getC_BankAccount_ID(), get_TrxName());	}

	/** Set Bank Account.
		@param C_BankAccount_ID 
		Account at the Bank
	  */
	public void setC_BankAccount_ID (int C_BankAccount_ID)
	{
		if (C_BankAccount_ID < 1) 
			set_Value (COLUMNNAME_C_BankAccount_ID, null);
		else 
			set_Value (COLUMNNAME_C_BankAccount_ID, Integer.valueOf(C_BankAccount_ID));
	}

	/** Get Bank Account.
		@return Account at the Bank
	  */
	public int getC_BankAccount_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_C_BankAccount_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	public org.compiere.model.I_C_BPartner getC_BPartner() throws RuntimeException
    {
		return (org.compiere.model.I_C_BPartner)MTable.get(getCtx(), org.compiere.model.I_C_BPartner.Table_Name)
			.getPO(getC_BPartner_ID(), get_TrxName());	}

	/** Set Business Partner .
		@param C_BPartner_ID 
		Identifies a Business Partner
	  */
	public void setC_BPartner_ID (int C_BPartner_ID)
	{
		if (C_BPartner_ID < 1) 
			set_ValueNoCheck (COLUMNNAME_C_BPartner_ID, null);
		else 
			set_ValueNoCheck (COLUMNNAME_C_BPartner_ID, Integer.valueOf(C_BPartner_ID));
	}

	/** Get Business Partner .
		@return Identifies a Business Partner
	  */
	public int getC_BPartner_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_C_BPartner_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	public org.compiere.model.I_C_BPartner_Location getC_BPartner_Location() throws RuntimeException
    {
		return (org.compiere.model.I_C_BPartner_Location)MTable.get(getCtx(), org.compiere.model.I_C_BPartner_Location.Table_Name)
			.getPO(getC_BPartner_Location_ID(), get_TrxName());	}

	/** Set Partner Location.
		@param C_BPartner_Location_ID 
		Identifies the (ship to) address for this Business Partner
	  */
	public void setC_BPartner_Location_ID (int C_BPartner_Location_ID)
	{
		if (C_BPartner_Location_ID < 1) 
			set_ValueNoCheck (COLUMNNAME_C_BPartner_Location_ID, null);
		else 
			set_ValueNoCheck (COLUMNNAME_C_BPartner_Location_ID, Integer.valueOf(C_BPartner_Location_ID));
	}

	/** Get Partner Location.
		@return Identifies the (ship to) address for this Business Partner
	  */
	public int getC_BPartner_Location_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_C_BPartner_Location_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	public org.compiere.model.I_C_DocType getC_DocType() throws RuntimeException
    {
		return (org.compiere.model.I_C_DocType)MTable.get(getCtx(), org.compiere.model.I_C_DocType.Table_Name)
			.getPO(getC_DocType_ID(), get_TrxName());	}

	/** Set Document Type.
		@param C_DocType_ID 
		Document type or rules
	  */
	public void setC_DocType_ID (int C_DocType_ID)
	{
		if (C_DocType_ID < 0) 
			set_ValueNoCheck (COLUMNNAME_C_DocType_ID, null);
		else 
			set_ValueNoCheck (COLUMNNAME_C_DocType_ID, Integer.valueOf(C_DocType_ID));
	}

	/** Get Document Type.
		@return Document type or rules
	  */
	public int getC_DocType_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_C_DocType_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	public org.compiere.model.I_C_Invoice getC_Invoice() throws RuntimeException
    {
		return (org.compiere.model.I_C_Invoice)MTable.get(getCtx(), org.compiere.model.I_C_Invoice.Table_Name)
			.getPO(getC_Invoice_ID(), get_TrxName());	}

	/** Set Invoice.
		@param C_Invoice_ID 
		Invoice Identifier
	  */
	public void setC_Invoice_ID (int C_Invoice_ID)
	{
		if (C_Invoice_ID < 1) 
			set_ValueNoCheck (COLUMNNAME_C_Invoice_ID, null);
		else 
			set_ValueNoCheck (COLUMNNAME_C_Invoice_ID, Integer.valueOf(C_Invoice_ID));
	}

	/** Get Invoice.
		@return Invoice Identifier
	  */
	public int getC_Invoice_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_C_Invoice_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	public org.compiere.model.I_C_Order getC_Order() throws RuntimeException
    {
		return (org.compiere.model.I_C_Order)MTable.get(getCtx(), org.compiere.model.I_C_Order.Table_Name)
			.getPO(getC_Order_ID(), get_TrxName());	}

	/** Set Order.
		@param C_Order_ID 
		Order
	  */
	public void setC_Order_ID (int C_Order_ID)
	{
		if (C_Order_ID < 1) 
			set_ValueNoCheck (COLUMNNAME_C_Order_ID, null);
		else 
			set_ValueNoCheck (COLUMNNAME_C_Order_ID, Integer.valueOf(C_Order_ID));
	}

	/** Get Order.
		@return Order
	  */
	public int getC_Order_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_C_Order_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	public org.compiere.model.I_C_Payment getC_Payment() throws RuntimeException
    {
		return (org.compiere.model.I_C_Payment)MTable.get(getCtx(), org.compiere.model.I_C_Payment.Table_Name)
			.getPO(getC_Payment_ID(), get_TrxName());	}

	/** Set Payment.
		@param C_Payment_ID 
		Payment identifier
	  */
	public void setC_Payment_ID (int C_Payment_ID)
	{
		if (C_Payment_ID < 1) 
			set_ValueNoCheck (COLUMNNAME_C_Payment_ID, null);
		else 
			set_ValueNoCheck (COLUMNNAME_C_Payment_ID, Integer.valueOf(C_Payment_ID));
	}

	/** Get Payment.
		@return Payment identifier
	  */
	public int getC_Payment_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_C_Payment_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	public org.compiere.model.I_C_PaymentTerm getC_PaymentTerm() throws RuntimeException
    {
		return (org.compiere.model.I_C_PaymentTerm)MTable.get(getCtx(), org.compiere.model.I_C_PaymentTerm.Table_Name)
			.getPO(getC_PaymentTerm_ID(), get_TrxName());	}

	/** Set Payment Term.
		@param C_PaymentTerm_ID 
		The terms of Payment (timing, discount)
	  */
	public void setC_PaymentTerm_ID (int C_PaymentTerm_ID)
	{
		if (C_PaymentTerm_ID < 1) 
			set_ValueNoCheck (COLUMNNAME_C_PaymentTerm_ID, null);
		else 
			set_ValueNoCheck (COLUMNNAME_C_PaymentTerm_ID, Integer.valueOf(C_PaymentTerm_ID));
	}

	/** Get Payment Term.
		@return The terms of Payment (timing, discount)
	  */
	public int getC_PaymentTerm_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_C_PaymentTerm_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	/** Set createdbypos_id.
		@param CreatedByPos_ID createdbypos_id	  */
	public void setCreatedByPos_ID (int CreatedByPos_ID)
	{
		if (CreatedByPos_ID < 1) 
			set_ValueNoCheck (COLUMNNAME_CreatedByPos_ID, null);
		else 
			set_ValueNoCheck (COLUMNNAME_CreatedByPos_ID, Integer.valueOf(CreatedByPos_ID));
	}

	/** Get createdbypos_id.
		@return createdbypos_id	  */
	public int getCreatedByPos_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_CreatedByPos_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	/** Set Date Ordered.
		@param DateOrdered 
		Date of Order
	  */
	public void setDateOrdered (Timestamp DateOrdered)
	{
		set_ValueNoCheck (COLUMNNAME_DateOrdered, DateOrdered);
	}

	/** Get Date Ordered.
		@return Date of Order
	  */
	public Timestamp getDateOrdered () 
	{
		return (Timestamp)get_Value(COLUMNNAME_DateOrdered);
	}

	/** Set Transaction Date.
		@param DateTrx 
		Transaction Date
	  */
	public void setDateTrx (Timestamp DateTrx)
	{
		set_ValueNoCheck (COLUMNNAME_DateTrx, DateTrx);
	}

	/** Get Transaction Date.
		@return Transaction Date
	  */
	public Timestamp getDateTrx () 
	{
		return (Timestamp)get_Value(COLUMNNAME_DateTrx);
	}

	/** DeliveryRule AD_Reference_ID=151 */
	public static final int DELIVERYRULE_AD_Reference_ID=151;
	/** After Receipt = R */
	public static final String DELIVERYRULE_AfterReceipt = "R";
	/** Availability = A */
	public static final String DELIVERYRULE_Availability = "A";
	/** Complete Line = L */
	public static final String DELIVERYRULE_CompleteLine = "L";
	/** Complete Order = O */
	public static final String DELIVERYRULE_CompleteOrder = "O";
	/** Force = F */
	public static final String DELIVERYRULE_Force = "F";
	/** Manual = M */
	public static final String DELIVERYRULE_Manual = "M";
	/** Set Delivery Rule.
		@param DeliveryRule 
		Defines the timing of Delivery
	  */
	public void setDeliveryRule (String DeliveryRule)
	{

		set_ValueNoCheck (COLUMNNAME_DeliveryRule, DeliveryRule);
	}

	/** Get Delivery Rule.
		@return Defines the timing of Delivery
	  */
	public String getDeliveryRule () 
	{
		return (String)get_Value(COLUMNNAME_DeliveryRule);
	}

	/** DeliveryViaRule AD_Reference_ID=152 */
	public static final int DELIVERYVIARULE_AD_Reference_ID=152;
	/** Pickup = P */
	public static final String DELIVERYVIARULE_Pickup = "P";
	/** Delivery = D */
	public static final String DELIVERYVIARULE_Delivery = "D";
	/** Shipper = S */
	public static final String DELIVERYVIARULE_Shipper = "S";
	/** Set Delivery Via.
		@param DeliveryViaRule 
		How the order will be delivered
	  */
	public void setDeliveryViaRule (String DeliveryViaRule)
	{

		set_ValueNoCheck (COLUMNNAME_DeliveryViaRule, DeliveryViaRule);
	}

	/** Get Delivery Via.
		@return How the order will be delivered
	  */
	public String getDeliveryViaRule () 
	{
		return (String)get_Value(COLUMNNAME_DeliveryViaRule);
	}

	/** Set Description.
		@param Description 
		Optional short description of the record
	  */
	public void setDescription (String Description)
	{
		set_Value (COLUMNNAME_Description, Description);
	}

	/** Get Description.
		@return Optional short description of the record
	  */
	public String getDescription () 
	{
		return (String)get_Value(COLUMNNAME_Description);
	}

	/** Set Discount Amount.
		@param DiscountAmt 
		Calculated amount of discount
	  */
	public void setDiscountAmt (BigDecimal DiscountAmt)
	{
		set_ValueNoCheck (COLUMNNAME_DiscountAmt, DiscountAmt);
	}

	/** Get Discount Amount.
		@return Calculated amount of discount
	  */
	public BigDecimal getDiscountAmt () 
	{
		BigDecimal bd = (BigDecimal)get_Value(COLUMNNAME_DiscountAmt);
		if (bd == null)
			 return Env.ZERO;
		return bd;
	}

	/** Set Document No.
		@param DocumentNo 
		Document sequence number of the document
	  */
	public void setDocumentNo (String DocumentNo)
	{
		set_ValueNoCheck (COLUMNNAME_DocumentNo, DocumentNo);
	}

	/** Get Document No.
		@return Document sequence number of the document
	  */
	public String getDocumentNo () 
	{
		return (String)get_Value(COLUMNNAME_DocumentNo);
	}

	/** Set DocumentNo Tutup Kas.
		@param DocumentNoTutupKas DocumentNo Tutup Kas	  */
	public void setDocumentNoTutupKas (String DocumentNoTutupKas)
	{
		set_Value (COLUMNNAME_DocumentNoTutupKas, DocumentNoTutupKas);
	}

	/** Get DocumentNo Tutup Kas.
		@return DocumentNo Tutup Kas	  */
	public String getDocumentNoTutupKas () 
	{
		return (String)get_Value(COLUMNNAME_DocumentNoTutupKas);
	}

	/** Set dpp.
		@param dpp dpp	  */
	public void setdpp (BigDecimal dpp)
	{
		set_Value (COLUMNNAME_dpp, dpp);
	}

	/** Get dpp.
		@return dpp	  */
	public BigDecimal getdpp () 
	{
		BigDecimal bd = (BigDecimal)get_Value(COLUMNNAME_dpp);
		if (bd == null)
			 return Env.ZERO;
		return bd;
	}

	/** Set Grand Total.
		@param GrandTotal 
		Total amount of document
	  */
	public void setGrandTotal (BigDecimal GrandTotal)
	{
		set_ValueNoCheck (COLUMNNAME_GrandTotal, GrandTotal);
	}

	/** Get Grand Total.
		@return Total amount of document
	  */
	public BigDecimal getGrandTotal () 
	{
		BigDecimal bd = (BigDecimal)get_Value(COLUMNNAME_GrandTotal);
		if (bd == null)
			 return Env.ZERO;
		return bd;
	}

	/** Set IsCashAdvance.
		@param IsCashAdvance IsCashAdvance	  */
	public void setIsCashAdvance (boolean IsCashAdvance)
	{
		set_Value (COLUMNNAME_IsCashAdvance, Boolean.valueOf(IsCashAdvance));
	}

	/** Get IsCashAdvance.
		@return IsCashAdvance	  */
	public boolean isCashAdvance () 
	{
		Object oo = get_Value(COLUMNNAME_IsCashAdvance);
		if (oo != null) 
		{
			 if (oo instanceof Boolean) 
				 return ((Boolean)oo).booleanValue(); 
			return "Y".equals(oo);
		}
		return false;
	}

	/** Set IsDebitCard.
		@param IsDebitCard IsDebitCard	  */
	public void setIsDebitCard (boolean IsDebitCard)
	{
		set_Value (COLUMNNAME_IsDebitCard, Boolean.valueOf(IsDebitCard));
	}

	/** Get IsDebitCard.
		@return IsDebitCard	  */
	public boolean isDebitCard () 
	{
		Object oo = get_Value(COLUMNNAME_IsDebitCard);
		if (oo != null) 
		{
			 if (oo instanceof Boolean) 
				 return ((Boolean)oo).booleanValue(); 
			return "Y".equals(oo);
		}
		return false;
	}

	/** Set IsLeasing.
		@param IsLeasing IsLeasing	  */
	public void setIsLeasing (boolean IsLeasing)
	{
		set_Value (COLUMNNAME_IsLeasing, Boolean.valueOf(IsLeasing));
	}

	/** Get IsLeasing.
		@return IsLeasing	  */
	public boolean isLeasing () 
	{
		Object oo = get_Value(COLUMNNAME_IsLeasing);
		if (oo != null) 
		{
			 if (oo instanceof Boolean) 
				 return ((Boolean)oo).booleanValue(); 
			return "Y".equals(oo);
		}
		return false;
	}

	/** Set IsManualDocumentNo.
		@param IsManualDocumentNo IsManualDocumentNo	  */
	public void setIsManualDocumentNo (boolean IsManualDocumentNo)
	{
		set_Value (COLUMNNAME_IsManualDocumentNo, Boolean.valueOf(IsManualDocumentNo));
	}

	/** Get IsManualDocumentNo.
		@return IsManualDocumentNo	  */
	public boolean isManualDocumentNo () 
	{
		Object oo = get_Value(COLUMNNAME_IsManualDocumentNo);
		if (oo != null) 
		{
			 if (oo instanceof Boolean) 
				 return ((Boolean)oo).booleanValue(); 
			return "Y".equals(oo);
		}
		return false;
	}

	/** Set IsMultiLocator.
		@param IsMultiLocator IsMultiLocator	  */
	public void setIsMultiLocator (boolean IsMultiLocator)
	{
		set_Value (COLUMNNAME_IsMultiLocator, Boolean.valueOf(IsMultiLocator));
	}

	/** Get IsMultiLocator.
		@return IsMultiLocator	  */
	public boolean isMultiLocator () 
	{
		Object oo = get_Value(COLUMNNAME_IsMultiLocator);
		if (oo != null) 
		{
			 if (oo instanceof Boolean) 
				 return ((Boolean)oo).booleanValue(); 
			return "Y".equals(oo);
		}
		return false;
	}

	/** Set IsPembatalan.
		@param IsPembatalan IsPembatalan	  */
	public void setIsPembatalan (boolean IsPembatalan)
	{
		set_Value (COLUMNNAME_IsPembatalan, Boolean.valueOf(IsPembatalan));
	}

	/** Get IsPembatalan.
		@return IsPembatalan	  */
	public boolean isPembatalan () 
	{
		Object oo = get_Value(COLUMNNAME_IsPembatalan);
		if (oo != null) 
		{
			 if (oo instanceof Boolean) 
				 return ((Boolean)oo).booleanValue(); 
			return "Y".equals(oo);
		}
		return false;
	}

	/** Set IsPembayaran.
		@param IsPembayaran IsPembayaran	  */
	public void setIsPembayaran (boolean IsPembayaran)
	{
		set_Value (COLUMNNAME_IsPembayaran, Boolean.valueOf(IsPembayaran));
	}

	/** Get IsPembayaran.
		@return IsPembayaran	  */
	public boolean isPembayaran () 
	{
		Object oo = get_Value(COLUMNNAME_IsPembayaran);
		if (oo != null) 
		{
			 if (oo instanceof Boolean) 
				 return ((Boolean)oo).booleanValue(); 
			return "Y".equals(oo);
		}
		return false;
	}

	/** Set IsPenjualan.
		@param IsPenjualan IsPenjualan	  */
	public void setIsPenjualan (boolean IsPenjualan)
	{
		set_Value (COLUMNNAME_IsPenjualan, Boolean.valueOf(IsPenjualan));
	}

	/** Get IsPenjualan.
		@return IsPenjualan	  */
	public boolean isPenjualan () 
	{
		Object oo = get_Value(COLUMNNAME_IsPenjualan);
		if (oo != null) 
		{
			 if (oo instanceof Boolean) 
				 return ((Boolean)oo).booleanValue(); 
			return "Y".equals(oo);
		}
		return false;
	}

	/** Set IsPickup.
		@param IsPickup IsPickup	  */
	public void setIsPickup (boolean IsPickup)
	{
		set_Value (COLUMNNAME_IsPickup, Boolean.valueOf(IsPickup));
	}

	/** Get IsPickup.
		@return IsPickup	  */
	public boolean isPickup () 
	{
		Object oo = get_Value(COLUMNNAME_IsPickup);
		if (oo != null) 
		{
			 if (oo instanceof Boolean) 
				 return ((Boolean)oo).booleanValue(); 
			return "Y".equals(oo);
		}
		return false;
	}

	/** Set Printed.
		@param IsPrinted 
		Indicates if this document / line is printed
	  */
	public void setIsPrinted (boolean IsPrinted)
	{
		set_ValueNoCheck (COLUMNNAME_IsPrinted, Boolean.valueOf(IsPrinted));
	}

	/** Get Printed.
		@return Indicates if this document / line is printed
	  */
	public boolean isPrinted () 
	{
		Object oo = get_Value(COLUMNNAME_IsPrinted);
		if (oo != null) 
		{
			 if (oo instanceof Boolean) 
				 return ((Boolean)oo).booleanValue(); 
			return "Y".equals(oo);
		}
		return false;
	}

	/** Set IsPrintedSJ.
		@param IsPrintedSJ IsPrintedSJ	  */
	public void setIsPrintedSJ (boolean IsPrintedSJ)
	{
		set_Value (COLUMNNAME_IsPrintedSJ, Boolean.valueOf(IsPrintedSJ));
	}

	/** Get IsPrintedSJ.
		@return IsPrintedSJ	  */
	public boolean isPrintedSJ () 
	{
		Object oo = get_Value(COLUMNNAME_IsPrintedSJ);
		if (oo != null) 
		{
			 if (oo instanceof Boolean) 
				 return ((Boolean)oo).booleanValue(); 
			return "Y".equals(oo);
		}
		return false;
	}

	/** Set Receipt.
		@param IsReceipt 
		This is a sales transaction (receipt)
	  */
	public void setIsReceipt (boolean IsReceipt)
	{
		set_Value (COLUMNNAME_IsReceipt, Boolean.valueOf(IsReceipt));
	}

	/** Get Receipt.
		@return This is a sales transaction (receipt)
	  */
	public boolean isReceipt () 
	{
		Object oo = get_Value(COLUMNNAME_IsReceipt);
		if (oo != null) 
		{
			 if (oo instanceof Boolean) 
				 return ((Boolean)oo).booleanValue(); 
			return "Y".equals(oo);
		}
		return false;
	}

	/** Set IsReturn.
		@param IsReturn IsReturn	  */
	public void setIsReturn (boolean IsReturn)
	{
		set_Value (COLUMNNAME_IsReturn, Boolean.valueOf(IsReturn));
	}

	/** Get IsReturn.
		@return IsReturn	  */
	public boolean isReturn () 
	{
		Object oo = get_Value(COLUMNNAME_IsReturn);
		if (oo != null) 
		{
			 if (oo instanceof Boolean) 
				 return ((Boolean)oo).booleanValue(); 
			return "Y".equals(oo);
		}
		return false;
	}

	/** Set Sales Transaction.
		@param IsSOTrx 
		This is a Sales Transaction
	  */
	public void setIsSOTrx (boolean IsSOTrx)
	{
		set_ValueNoCheck (COLUMNNAME_IsSOTrx, Boolean.valueOf(IsSOTrx));
	}

	/** Get Sales Transaction.
		@return This is a Sales Transaction
	  */
	public boolean isSOTrx () 
	{
		Object oo = get_Value(COLUMNNAME_IsSOTrx);
		if (oo != null) 
		{
			 if (oo instanceof Boolean) 
				 return ((Boolean)oo).booleanValue(); 
			return "Y".equals(oo);
		}
		return false;
	}

	/** Set IsSpektra.
		@param IsSpektra IsSpektra	  */
	public void setIsSpektra (boolean IsSpektra)
	{
		set_Value (COLUMNNAME_IsSpektra, Boolean.valueOf(IsSpektra));
	}

	/** Get IsSpektra.
		@return IsSpektra	  */
	public boolean isSpektra () 
	{
		Object oo = get_Value(COLUMNNAME_IsSpektra);
		if (oo != null) 
		{
			 if (oo instanceof Boolean) 
				 return ((Boolean)oo).booleanValue(); 
			return "Y".equals(oo);
		}
		return false;
	}

	/** Set IsTutupKas.
		@param IsTutupKas IsTutupKas	  */
	public void setIsTutupKas (boolean IsTutupKas)
	{
		set_Value (COLUMNNAME_IsTutupKas, Boolean.valueOf(IsTutupKas));
	}

	/** Get IsTutupKas.
		@return IsTutupKas	  */
	public boolean isTutupKas () 
	{
		Object oo = get_Value(COLUMNNAME_IsTutupKas);
		if (oo != null) 
		{
			 if (oo instanceof Boolean) 
				 return ((Boolean)oo).booleanValue(); 
			return "Y".equals(oo);
		}
		return false;
	}

	/** Set LeaseProvider.
		@param LeaseProvider LeaseProvider	  */
	public void setLeaseProvider (String LeaseProvider)
	{
		set_Value (COLUMNNAME_LeaseProvider, LeaseProvider);
	}

	/** Get LeaseProvider.
		@return LeaseProvider	  */
	public String getLeaseProvider () 
	{
		return (String)get_Value(COLUMNNAME_LeaseProvider);
	}

	/** Set LocatorNoMulti_ID.
		@param LocatorNoMulti_ID LocatorNoMulti_ID	  */
	public void setLocatorNoMulti_ID (int LocatorNoMulti_ID)
	{
		if (LocatorNoMulti_ID < 1) 
			set_Value (COLUMNNAME_LocatorNoMulti_ID, null);
		else 
			set_Value (COLUMNNAME_LocatorNoMulti_ID, Integer.valueOf(LocatorNoMulti_ID));
	}

	/** Get LocatorNoMulti_ID.
		@return LocatorNoMulti_ID	  */
	public int getLocatorNoMulti_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_LocatorNoMulti_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	public org.compiere.model.I_M_PriceList getM_PriceList() throws RuntimeException
    {
		return (org.compiere.model.I_M_PriceList)MTable.get(getCtx(), org.compiere.model.I_M_PriceList.Table_Name)
			.getPO(getM_PriceList_ID(), get_TrxName());	}

	/** Set Price List.
		@param M_PriceList_ID 
		Unique identifier of a Price List
	  */
	public void setM_PriceList_ID (int M_PriceList_ID)
	{
		if (M_PriceList_ID < 1) 
			set_Value (COLUMNNAME_M_PriceList_ID, null);
		else 
			set_Value (COLUMNNAME_M_PriceList_ID, Integer.valueOf(M_PriceList_ID));
	}

	/** Get Price List.
		@return Unique identifier of a Price List
	  */
	public int getM_PriceList_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_M_PriceList_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	public org.compiere.model.I_M_Warehouse getM_Warehouse() throws RuntimeException
    {
		return (org.compiere.model.I_M_Warehouse)MTable.get(getCtx(), org.compiere.model.I_M_Warehouse.Table_Name)
			.getPO(getM_Warehouse_ID(), get_TrxName());	}

	/** Set Warehouse.
		@param M_Warehouse_ID 
		Storage Warehouse and Service Point
	  */
	public void setM_Warehouse_ID (int M_Warehouse_ID)
	{
		if (M_Warehouse_ID < 1) 
			set_ValueNoCheck (COLUMNNAME_M_Warehouse_ID, null);
		else 
			set_ValueNoCheck (COLUMNNAME_M_Warehouse_ID, Integer.valueOf(M_Warehouse_ID));
	}

	/** Get Warehouse.
		@return Storage Warehouse and Service Point
	  */
	public int getM_Warehouse_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_M_Warehouse_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	/** Set OrderDocType_ID.
		@param OrderDocType_ID OrderDocType_ID	  */
	public void setOrderDocType_ID (int OrderDocType_ID)
	{
		if (OrderDocType_ID < 1) 
			set_Value (COLUMNNAME_OrderDocType_ID, null);
		else 
			set_Value (COLUMNNAME_OrderDocType_ID, Integer.valueOf(OrderDocType_ID));
	}

	/** Get OrderDocType_ID.
		@return OrderDocType_ID	  */
	public int getOrderDocType_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_OrderDocType_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	/** PaymentRule AD_Reference_ID=195 */
	public static final int PAYMENTRULE_AD_Reference_ID=195;
	/** Cash = B */
	public static final String PAYMENTRULE_Cash = "B";
	/** Credit Card = K */
	public static final String PAYMENTRULE_CreditCard = "K";
	/** Direct Deposit = T */
	public static final String PAYMENTRULE_DirectDeposit = "T";
	/** Check = S */
	public static final String PAYMENTRULE_Check = "S";
	/** On Credit = P */
	public static final String PAYMENTRULE_OnCredit = "P";
	/** Direct Debit = D */
	public static final String PAYMENTRULE_DirectDebit = "D";
	/** Mixed POS Payment = M */
	public static final String PAYMENTRULE_MixedPOSPayment = "M";
	/** Set Payment Rule.
		@param PaymentRule 
		How you pay the invoice
	  */
	public void setPaymentRule (String PaymentRule)
	{

		set_ValueNoCheck (COLUMNNAME_PaymentRule, PaymentRule);
	}

	/** Get Payment Rule.
		@return How you pay the invoice
	  */
	public String getPaymentRule () 
	{
		return (String)get_Value(COLUMNNAME_PaymentRule);
	}

	/** Set PayType1.
		@param PayType1 PayType1	  */
	public void setPayType1 (String PayType1)
	{
		set_Value (COLUMNNAME_PayType1, PayType1);
	}

	/** Get PayType1.
		@return PayType1	  */
	public String getPayType1 () 
	{
		return (String)get_Value(COLUMNNAME_PayType1);
	}

	/** Set PayType2.
		@param PayType2 PayType2	  */
	public void setPayType2 (String PayType2)
	{
		set_Value (COLUMNNAME_PayType2, PayType2);
	}

	/** Get PayType2.
		@return PayType2	  */
	public String getPayType2 () 
	{
		return (String)get_Value(COLUMNNAME_PayType2);
	}

	/** Set PayType3.
		@param PayType3 PayType3	  */
	public void setPayType3 (String PayType3)
	{
		set_Value (COLUMNNAME_PayType3, PayType3);
	}

	/** Get PayType3.
		@return PayType3	  */
	public String getPayType3 () 
	{
		return (String)get_Value(COLUMNNAME_PayType3);
	}

	/** Set PayType4.
		@param PayType4 PayType4	  */
	public void setPayType4 (String PayType4)
	{
		set_Value (COLUMNNAME_PayType4, PayType4);
	}

	/** Get PayType4.
		@return PayType4	  */
	public String getPayType4 () 
	{
		return (String)get_Value(COLUMNNAME_PayType4);
	}

	/** Set Pembayaran1.
		@param Pembayaran1 Pembayaran1	  */
	public void setPembayaran1 (BigDecimal Pembayaran1)
	{
		set_Value (COLUMNNAME_Pembayaran1, Pembayaran1);
	}

	/** Get Pembayaran1.
		@return Pembayaran1	  */
	public BigDecimal getPembayaran1 () 
	{
		BigDecimal bd = (BigDecimal)get_Value(COLUMNNAME_Pembayaran1);
		if (bd == null)
			 return Env.ZERO;
		return bd;
	}

	/** Set Pembayaran2.
		@param Pembayaran2 Pembayaran2	  */
	public void setPembayaran2 (BigDecimal Pembayaran2)
	{
		set_Value (COLUMNNAME_Pembayaran2, Pembayaran2);
	}

	/** Get Pembayaran2.
		@return Pembayaran2	  */
	public BigDecimal getPembayaran2 () 
	{
		BigDecimal bd = (BigDecimal)get_Value(COLUMNNAME_Pembayaran2);
		if (bd == null)
			 return Env.ZERO;
		return bd;
	}

	/** Set Pembayaran3.
		@param Pembayaran3 Pembayaran3	  */
	public void setPembayaran3 (BigDecimal Pembayaran3)
	{
		set_Value (COLUMNNAME_Pembayaran3, Pembayaran3);
	}

	/** Get Pembayaran3.
		@return Pembayaran3	  */
	public BigDecimal getPembayaran3 () 
	{
		BigDecimal bd = (BigDecimal)get_Value(COLUMNNAME_Pembayaran3);
		if (bd == null)
			 return Env.ZERO;
		return bd;
	}

	/** Set Pembayaran4.
		@param Pembayaran4 Pembayaran4	  */
	public void setPembayaran4 (BigDecimal Pembayaran4)
	{
		set_Value (COLUMNNAME_Pembayaran4, Pembayaran4);
	}

	/** Get Pembayaran4.
		@return Pembayaran4	  */
	public BigDecimal getPembayaran4 () 
	{
		BigDecimal bd = (BigDecimal)get_Value(COLUMNNAME_Pembayaran4);
		if (bd == null)
			 return Env.ZERO;
		return bd;
	}

	/** Set PrintDate.
		@param PrintDate PrintDate	  */
	public void setPrintDate (Timestamp PrintDate)
	{
		set_Value (COLUMNNAME_PrintDate, PrintDate);
	}

	/** Get PrintDate.
		@return PrintDate	  */
	public Timestamp getPrintDate () 
	{
		return (Timestamp)get_Value(COLUMNNAME_PrintDate);
	}

	/** Set PrintDateSJ.
		@param PrintDateSJ PrintDateSJ	  */
	public void setPrintDateSJ (Timestamp PrintDateSJ)
	{
		set_Value (COLUMNNAME_PrintDateSJ, PrintDateSJ);
	}

	/** Get PrintDateSJ.
		@return PrintDateSJ	  */
	public Timestamp getPrintDateSJ () 
	{
		return (Timestamp)get_Value(COLUMNNAME_PrintDateSJ);
	}

	/** Set PrintSequence.
		@param PrintSequence PrintSequence	  */
	public void setPrintSequence (int PrintSequence)
	{
		set_Value (COLUMNNAME_PrintSequence, Integer.valueOf(PrintSequence));
	}

	/** Get PrintSequence.
		@return PrintSequence	  */
	public int getPrintSequence () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_PrintSequence);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	/** Set PrintSequenceSJ.
		@param PrintSequenceSJ PrintSequenceSJ	  */
	public void setPrintSequenceSJ (int PrintSequenceSJ)
	{
		set_Value (COLUMNNAME_PrintSequenceSJ, Integer.valueOf(PrintSequenceSJ));
	}

	/** Get PrintSequenceSJ.
		@return PrintSequenceSJ	  */
	public int getPrintSequenceSJ () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_PrintSequenceSJ);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	/** Set Processed.
		@param Processed 
		The document has been processed
	  */
	public void setProcessed (boolean Processed)
	{
		set_Value (COLUMNNAME_Processed, Boolean.valueOf(Processed));
	}

	/** Get Processed.
		@return The document has been processed
	  */
	public boolean isProcessed () 
	{
		Object oo = get_Value(COLUMNNAME_Processed);
		if (oo != null) 
		{
			 if (oo instanceof Boolean) 
				 return ((Boolean)oo).booleanValue(); 
			return "Y".equals(oo);
		}
		return false;
	}

	public org.compiere.model.I_AD_User getSalesRep() throws RuntimeException
    {
		return (org.compiere.model.I_AD_User)MTable.get(getCtx(), org.compiere.model.I_AD_User.Table_Name)
			.getPO(getSalesRep_ID(), get_TrxName());	}

	/** Set Sales Representative.
		@param SalesRep_ID 
		Sales Representative or Company Agent
	  */
	public void setSalesRep_ID (int SalesRep_ID)
	{
		if (SalesRep_ID < 1) 
			set_ValueNoCheck (COLUMNNAME_SalesRep_ID, null);
		else 
			set_ValueNoCheck (COLUMNNAME_SalesRep_ID, Integer.valueOf(SalesRep_ID));
	}

	/** Get Sales Representative.
		@return Sales Representative or Company Agent
	  */
	public int getSalesRep_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_SalesRep_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	/** Set SeqTutupKas.
		@param SeqTutupKas SeqTutupKas	  */
	public void setSeqTutupKas (int SeqTutupKas)
	{
		set_Value (COLUMNNAME_SeqTutupKas, Integer.valueOf(SeqTutupKas));
	}

	/** Get SeqTutupKas.
		@return SeqTutupKas	  */
	public int getSeqTutupKas () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_SeqTutupKas);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	public I_SM_Semeru_CloseCash getSM_Semeru_CloseCash() throws RuntimeException
    {
		return (I_SM_Semeru_CloseCash)MTable.get(getCtx(), I_SM_Semeru_CloseCash.Table_Name)
			.getPO(getSM_Semeru_CloseCash_ID(), get_TrxName());	}

	/** Set SM_Semeru_CloseCash.
		@param SM_Semeru_CloseCash_ID SM_Semeru_CloseCash	  */
	public void setSM_Semeru_CloseCash_ID (int SM_Semeru_CloseCash_ID)
	{
		if (SM_Semeru_CloseCash_ID < 1) 
			set_ValueNoCheck (COLUMNNAME_SM_Semeru_CloseCash_ID, null);
		else 
			set_ValueNoCheck (COLUMNNAME_SM_Semeru_CloseCash_ID, Integer.valueOf(SM_Semeru_CloseCash_ID));
	}

	/** Get SM_Semeru_CloseCash.
		@return SM_Semeru_CloseCash	  */
	public int getSM_Semeru_CloseCash_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_SM_Semeru_CloseCash_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	/** Set SM_SemeruPOS.
		@param SM_SemeruPOS_ID SM_SemeruPOS	  */
	public void setSM_SemeruPOS_ID (int SM_SemeruPOS_ID)
	{
		if (SM_SemeruPOS_ID < 1) 
			set_ValueNoCheck (COLUMNNAME_SM_SemeruPOS_ID, null);
		else 
			set_ValueNoCheck (COLUMNNAME_SM_SemeruPOS_ID, Integer.valueOf(SM_SemeruPOS_ID));
	}

	/** Get SM_SemeruPOS.
		@return SM_SemeruPOS	  */
	public int getSM_SemeruPOS_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_SM_SemeruPOS_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	/** Set SM_SemeruPOS_UU.
		@param SM_SemeruPOS_UU SM_SemeruPOS_UU	  */
	public void setSM_SemeruPOS_UU (String SM_SemeruPOS_UU)
	{
		set_ValueNoCheck (COLUMNNAME_SM_SemeruPOS_UU, SM_SemeruPOS_UU);
	}

	/** Get SM_SemeruPOS_UU.
		@return SM_SemeruPOS_UU	  */
	public String getSM_SemeruPOS_UU () 
	{
		return (String)get_Value(COLUMNNAME_SM_SemeruPOS_UU);
	}

	public org.compiere.model.I_AD_User getSupervisor() throws RuntimeException
    {
		return (org.compiere.model.I_AD_User)MTable.get(getCtx(), org.compiere.model.I_AD_User.Table_Name)
			.getPO(getSupervisor_ID(), get_TrxName());	}

	/** Set Supervisor.
		@param Supervisor_ID 
		Supervisor for this user/organization - used for escalation and approval
	  */
	public void setSupervisor_ID (int Supervisor_ID)
	{
		if (Supervisor_ID < 1) 
			set_ValueNoCheck (COLUMNNAME_Supervisor_ID, null);
		else 
			set_ValueNoCheck (COLUMNNAME_Supervisor_ID, Integer.valueOf(Supervisor_ID));
	}

	/** Get Supervisor.
		@return Supervisor for this user/organization - used for escalation and approval
	  */
	public int getSupervisor_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_Supervisor_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	/** Set Tax Amount.
		@param TaxAmt 
		Tax Amount for a document
	  */
	public void setTaxAmt (BigDecimal TaxAmt)
	{
		set_Value (COLUMNNAME_TaxAmt, TaxAmt);
	}

	/** Get Tax Amount.
		@return Tax Amount for a document
	  */
	public BigDecimal getTaxAmt () 
	{
		BigDecimal bd = (BigDecimal)get_Value(COLUMNNAME_TaxAmt);
		if (bd == null)
			 return Env.ZERO;
		return bd;
	}

	/** Set TotalKembalian.
		@param TotalKembalian TotalKembalian	  */
	public void setTotalKembalian (BigDecimal TotalKembalian)
	{
		set_Value (COLUMNNAME_TotalKembalian, TotalKembalian);
	}

	/** Get TotalKembalian.
		@return TotalKembalian	  */
	public BigDecimal getTotalKembalian () 
	{
		BigDecimal bd = (BigDecimal)get_Value(COLUMNNAME_TotalKembalian);
		if (bd == null)
			 return Env.ZERO;
		return bd;
	}

	/** Set Total Lines.
		@param TotalLines 
		Total of all document lines
	  */
	public void setTotalLines (BigDecimal TotalLines)
	{
		set_ValueNoCheck (COLUMNNAME_TotalLines, TotalLines);
	}

	/** Get Total Lines.
		@return Total of all document lines
	  */
	public BigDecimal getTotalLines () 
	{
		BigDecimal bd = (BigDecimal)get_Value(COLUMNNAME_TotalLines);
		if (bd == null)
			 return Env.ZERO;
		return bd;
	}

	/** Set TotalTunai.
		@param TotalTunai TotalTunai	  */
	public void setTotalTunai (BigDecimal TotalTunai)
	{
		set_Value (COLUMNNAME_TotalTunai, TotalTunai);
	}

	/** Get TotalTunai.
		@return TotalTunai	  */
	public BigDecimal getTotalTunai () 
	{
		BigDecimal bd = (BigDecimal)get_Value(COLUMNNAME_TotalTunai);
		if (bd == null)
			 return Env.ZERO;
		return bd;
	}
}