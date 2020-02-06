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

/** Generated Model for i_ordertrx_temp
 *  @author iDempiere (generated) 
 *  @version Release 3.1 - $Id$ */
public class X_i_ordertrx_temp extends PO implements I_i_ordertrx_temp, I_Persistent 
{

	/**
	 *
	 */
	private static final long serialVersionUID = 20190411L;

    /** Standard Constructor */
    public X_i_ordertrx_temp (Properties ctx, int i_ordertrx_temp_ID, String trxName)
    {
      super (ctx, i_ordertrx_temp_ID, trxName);
      /** if (i_ordertrx_temp_ID == 0)
        {
			seti_ordertrx_temp_ID (0);
        } */
    }

    /** Load Constructor */
    public X_i_ordertrx_temp (Properties ctx, ResultSet rs, String trxName)
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
      StringBuffer sb = new StringBuffer ("X_i_ordertrx_temp[")
        .append(get_ID()).append("]");
      return sb.toString();
    }

	/** Set count_order.
		@param count_order count_order	  */
	public void setcount_order (int count_order)
	{
		set_Value (COLUMNNAME_count_order, Integer.valueOf(count_order));
	}

	/** Get count_order.
		@return count_order	  */
	public int getcount_order () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_count_order);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	/** Set Semeru Order Temporary.
		@param i_ordertrx_temp_ID Semeru Order Temporary	  */
	public void seti_ordertrx_temp_ID (int i_ordertrx_temp_ID)
	{
		if (i_ordertrx_temp_ID < 1) 
			set_ValueNoCheck (COLUMNNAME_i_ordertrx_temp_ID, null);
		else 
			set_ValueNoCheck (COLUMNNAME_i_ordertrx_temp_ID, Integer.valueOf(i_ordertrx_temp_ID));
	}

	/** Get Semeru Order Temporary.
		@return Semeru Order Temporary	  */
	public int geti_ordertrx_temp_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_i_ordertrx_temp_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	/** Set insert_order.
		@param insert_order insert_order	  */
	public void setinsert_order (boolean insert_order)
	{
		set_Value (COLUMNNAME_insert_order, Boolean.valueOf(insert_order));
	}

	/** Get insert_order.
		@return insert_order	  */
	public boolean isinsert_order () 
	{
		Object oo = get_Value(COLUMNNAME_insert_order);
		if (oo != null) 
		{
			 if (oo instanceof Boolean) 
				 return ((Boolean)oo).booleanValue(); 
			return "Y".equals(oo);
		}
		return false;
	}

	/** Set order_detail.
		@param order_detail order_detail	  */
	public void setorder_detail (String order_detail)
	{
		set_Value (COLUMNNAME_order_detail, order_detail);
	}

	/** Get order_detail.
		@return order_detail	  */
	public String getorder_detail () 
	{
		return (String)get_Value(COLUMNNAME_order_detail);
	}

	/** Set orders.
		@param orders orders	  */
	public void setorders (String orders)
	{
		set_Value (COLUMNNAME_orders, orders);
	}

	/** Get orders.
		@return orders	  */
	public String getorders () 
	{
		return (String)get_Value(COLUMNNAME_orders);
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
}