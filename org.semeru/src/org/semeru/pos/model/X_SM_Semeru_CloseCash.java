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

/** Generated Model for SM_Semeru_CloseCash
 *  @author iDempiere (generated) 
 *  @version Release 6.2 - $Id$ */
public class X_SM_Semeru_CloseCash extends PO implements I_SM_Semeru_CloseCash, I_Persistent 
{

	/**
	 *
	 */
	private static final long serialVersionUID = 20190926L;

    /** Standard Constructor */
    public X_SM_Semeru_CloseCash (Properties ctx, int SM_Semeru_CloseCash_ID, String trxName)
    {
      super (ctx, SM_Semeru_CloseCash_ID, trxName);
      /** if (SM_Semeru_CloseCash_ID == 0)
        {
			setSM_Semeru_CloseCash_ID (0);
        } */
    }

    /** Load Constructor */
    public X_SM_Semeru_CloseCash (Properties ctx, ResultSet rs, String trxName)
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
      StringBuffer sb = new StringBuffer ("X_SM_Semeru_CloseCash[")
        .append(get_ID()).append("]");
      return sb.toString();
    }

	/** Set Bank Payment.
		@param BankPayment Bank Payment	  */
	public void setBankPayment (BigDecimal BankPayment)
	{
		set_Value (COLUMNNAME_BankPayment, BankPayment);
	}

	/** Get Bank Payment.
		@return Bank Payment	  */
	public BigDecimal getBankPayment () 
	{
		BigDecimal bd = (BigDecimal)get_Value(COLUMNNAME_BankPayment);
		if (bd == null)
			 return Env.ZERO;
		return bd;
	}

	/** Set Cash.
		@param Cash Cash	  */
	public void setCash (BigDecimal Cash)
	{
		set_Value (COLUMNNAME_Cash, Cash);
	}

	/** Get Cash.
		@return Cash	  */
	public BigDecimal getCash () 
	{
		BigDecimal bd = (BigDecimal)get_Value(COLUMNNAME_Cash);
		if (bd == null)
			 return Env.ZERO;
		return bd;
	}

	/** Set CashIn.
		@param CashIn CashIn	  */
	public void setCashIn (BigDecimal CashIn)
	{
		set_Value (COLUMNNAME_CashIn, CashIn);
	}

	/** Get CashIn.
		@return CashIn	  */
	public BigDecimal getCashIn () 
	{
		BigDecimal bd = (BigDecimal)get_Value(COLUMNNAME_CashIn);
		if (bd == null)
			 return Env.ZERO;
		return bd;
	}

	/** Set CashOut.
		@param CashOut CashOut	  */
	public void setCashOut (BigDecimal CashOut)
	{
		set_Value (COLUMNNAME_CashOut, CashOut);
	}

	/** Get CashOut.
		@return CashOut	  */
	public BigDecimal getCashOut () 
	{
		BigDecimal bd = (BigDecimal)get_Value(COLUMNNAME_CashOut);
		if (bd == null)
			 return Env.ZERO;
		return bd;
	}

	/** Set CloseCashDate.
		@param CloseCashDate CloseCashDate	  */
	public void setCloseCashDate (Timestamp CloseCashDate)
	{
		set_Value (COLUMNNAME_CloseCashDate, CloseCashDate);
	}

	/** Get CloseCashDate.
		@return CloseCashDate	  */
	public Timestamp getCloseCashDate () 
	{
		return (Timestamp)get_Value(COLUMNNAME_CloseCashDate);
	}

	/** Set CreatedByPos_ID.
		@param CreatedByPos_ID CreatedByPos_ID	  */
	public void setCreatedByPos_ID (int CreatedByPos_ID)
	{
		if (CreatedByPos_ID < 1) 
			set_ValueNoCheck (COLUMNNAME_CreatedByPos_ID, null);
		else 
			set_ValueNoCheck (COLUMNNAME_CreatedByPos_ID, Integer.valueOf(CreatedByPos_ID));
	}

	/** Get CreatedByPos_ID.
		@return CreatedByPos_ID	  */
	public int getCreatedByPos_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_CreatedByPos_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	/** Set Current balance.
		@param CurrentBalance 
		Current Balance
	  */
	public void setCurrentBalance (BigDecimal CurrentBalance)
	{
		set_ValueNoCheck (COLUMNNAME_CurrentBalance, CurrentBalance);
	}

	/** Get Current balance.
		@return Current Balance
	  */
	public BigDecimal getCurrentBalance () 
	{
		BigDecimal bd = (BigDecimal)get_Value(COLUMNNAME_CurrentBalance);
		if (bd == null)
			 return Env.ZERO;
		return bd;
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

	/** Set Lain Lain.
		@param LainLain Lain Lain	  */
	public void setLainLain (BigDecimal LainLain)
	{
		set_Value (COLUMNNAME_LainLain, LainLain);
	}

	/** Get Lain Lain.
		@return Lain Lain	  */
	public BigDecimal getLainLain () 
	{
		BigDecimal bd = (BigDecimal)get_Value(COLUMNNAME_LainLain);
		if (bd == null)
			 return Env.ZERO;
		return bd;
	}

	/** Set Leasing Payment.
		@param LeasingPayment Leasing Payment	  */
	public void setLeasingPayment (BigDecimal LeasingPayment)
	{
		set_Value (COLUMNNAME_LeasingPayment, LeasingPayment);
	}

	/** Get Leasing Payment.
		@return Leasing Payment	  */
	public BigDecimal getLeasingPayment () 
	{
		BigDecimal bd = (BigDecimal)get_Value(COLUMNNAME_LeasingPayment);
		if (bd == null)
			 return Env.ZERO;
		return bd;
	}

	/** Set Other Leasing Payment.
		@param OtherLeasingPayment Other Leasing Payment	  */
	public void setOtherLeasingPayment (BigDecimal OtherLeasingPayment)
	{
		set_Value (COLUMNNAME_OtherLeasingPayment, OtherLeasingPayment);
	}

	/** Get Other Leasing Payment.
		@return Other Leasing Payment	  */
	public BigDecimal getOtherLeasingPayment () 
	{
		BigDecimal bd = (BigDecimal)get_Value(COLUMNNAME_OtherLeasingPayment);
		if (bd == null)
			 return Env.ZERO;
		return bd;
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

	/** Set SM_Semeru_CloseCash_UU.
		@param SM_Semeru_CloseCash_UU SM_Semeru_CloseCash_UU	  */
	public void setSM_Semeru_CloseCash_UU (String SM_Semeru_CloseCash_UU)
	{
		set_ValueNoCheck (COLUMNNAME_SM_Semeru_CloseCash_UU, SM_Semeru_CloseCash_UU);
	}

	/** Get SM_Semeru_CloseCash_UU.
		@return SM_Semeru_CloseCash_UU	  */
	public String getSM_Semeru_CloseCash_UU () 
	{
		return (String)get_Value(COLUMNNAME_SM_Semeru_CloseCash_UU);
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

	/** Set Total Omset.
		@param TotalOmset Total Omset	  */
	public void setTotalOmset (BigDecimal TotalOmset)
	{
		set_Value (COLUMNNAME_TotalOmset, TotalOmset);
	}

	/** Get Total Omset.
		@return Total Omset	  */
	public BigDecimal getTotalOmset () 
	{
		BigDecimal bd = (BigDecimal)get_Value(COLUMNNAME_TotalOmset);
		if (bd == null)
			 return Env.ZERO;
		return bd;
	}

	/** Set Trx Count.
		@param TrxCount Trx Count	  */
	public void setTrxCount (int TrxCount)
	{
		set_Value (COLUMNNAME_TrxCount, Integer.valueOf(TrxCount));
	}

	/** Get Trx Count.
		@return Trx Count	  */
	public int getTrxCount () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_TrxCount);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}
}