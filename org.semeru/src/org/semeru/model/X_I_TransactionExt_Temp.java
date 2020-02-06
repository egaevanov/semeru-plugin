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
package org.semeru.model;

import java.sql.ResultSet;
import java.util.Properties;
import org.compiere.model.*;

/** Generated Model for I_TransactionExt_Temp
 *  @author iDempiere (generated) 
 *  @version Release 3.1 - $Id$ */
public class X_I_TransactionExt_Temp extends PO implements I_I_TransactionExt_Temp, I_Persistent 
{

	/**
	 *
	 */
	private static final long serialVersionUID = 20190222L;

    /** Standard Constructor */
    public X_I_TransactionExt_Temp (Properties ctx, int I_TransactionExt_Temp_ID, String trxName)
    {
      super (ctx, I_TransactionExt_Temp_ID, trxName);
      /** if (I_TransactionExt_Temp_ID == 0)
        {
			setI_TransactionExt_Temp_ID (0);
        } */
    }

    /** Load Constructor */
    public X_I_TransactionExt_Temp (Properties ctx, ResultSet rs, String trxName)
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
      StringBuffer sb = new StringBuffer ("X_I_TransactionExt_Temp[")
        .append(get_ID()).append("]");
      return sb.toString();
    }

	/** Set count_trx.
		@param count_trx count_trx	  */
	public void setcount_trx (int count_trx)
	{
		set_Value (COLUMNNAME_count_trx, Integer.valueOf(count_trx));
	}

	/** Get count_trx.
		@return count_trx	  */
	public int getcount_trx () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_count_trx);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	/** Set I_TransactionExt_Temp.
		@param I_TransactionExt_Temp_ID I_TransactionExt_Temp	  */
	public void setI_TransactionExt_Temp_ID (int I_TransactionExt_Temp_ID)
	{
		if (I_TransactionExt_Temp_ID < 1) 
			set_ValueNoCheck (COLUMNNAME_I_TransactionExt_Temp_ID, null);
		else 
			set_ValueNoCheck (COLUMNNAME_I_TransactionExt_Temp_ID, Integer.valueOf(I_TransactionExt_Temp_ID));
	}

	/** Get I_TransactionExt_Temp.
		@return I_TransactionExt_Temp	  */
	public int getI_TransactionExt_Temp_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_I_TransactionExt_Temp_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	/** Set insert_transaction.
		@param insert_transaction insert_transaction	  */
	public void setinsert_transaction (boolean insert_transaction)
	{
		set_Value (COLUMNNAME_insert_transaction, Boolean.valueOf(insert_transaction));
	}

	/** Get insert_transaction.
		@return insert_transaction	  */
	public boolean isinsert_transaction () 
	{
		Object oo = get_Value(COLUMNNAME_insert_transaction);
		if (oo != null) 
		{
			 if (oo instanceof Boolean) 
				 return ((Boolean)oo).booleanValue(); 
			return "Y".equals(oo);
		}
		return false;
	}

	/** Set pos.
		@param pos pos	  */
	public void setpos (String pos)
	{
		set_ValueNoCheck (COLUMNNAME_pos, pos);
	}

	/** Get pos.
		@return pos	  */
	public String getpos () 
	{
		return (String)get_Value(COLUMNNAME_pos);
	}

	/** Set Result.
		@param Result 
		Result of the action taken
	  */
	public void setResult (String Result)
	{
		set_ValueNoCheck (COLUMNNAME_Result, Result);
	}

	/** Get Result.
		@return Result of the action taken
	  */
	public String getResult () 
	{
		return (String)get_Value(COLUMNNAME_Result);
	}

	/** Set transaction_detail.
		@param transaction_detail transaction_detail	  */
	public void settransaction_detail (String transaction_detail)
	{
		set_Value (COLUMNNAME_transaction_detail, transaction_detail);
	}

	/** Get transaction_detail.
		@return transaction_detail	  */
	public String gettransaction_detail () 
	{
		return (String)get_Value(COLUMNNAME_transaction_detail);
	}

	/** Set transaction_id.
		@param transaction_id transaction_id	  */
	public void settransaction_id (int transaction_id)
	{
		set_Value (COLUMNNAME_transaction_id, Integer.valueOf(transaction_id));
	}

	/** Get transaction_id.
		@return transaction_id	  */
	public int gettransaction_id () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_transaction_id);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	/** Set transactions.
		@param transactions transactions	  */
	public void settransactions (String transactions)
	{
		set_Value (COLUMNNAME_transactions, transactions);
	}

	/** Get transactions.
		@return transactions	  */
	public String gettransactions () 
	{
		return (String)get_Value(COLUMNNAME_transactions);
	}
}