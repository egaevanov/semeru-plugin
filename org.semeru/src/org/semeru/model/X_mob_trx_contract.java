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

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.Properties;
import org.compiere.model.*;
import org.compiere.util.Env;

/** Generated Model for mob_trx_contract
 *  @author iDempiere (generated) 
 *  @version Release 3.1 - $Id$ */
public class X_mob_trx_contract extends PO implements I_mob_trx_contract, I_Persistent 
{

	/**
	 *
	 */
	private static final long serialVersionUID = 20190715L;

    /** Standard Constructor */
    public X_mob_trx_contract (Properties ctx, int mob_trx_contract_ID, String trxName)
    {
      super (ctx, mob_trx_contract_ID, trxName);
      /** if (mob_trx_contract_ID == 0)
        {
			setcontract_no (0);
			setmob_trx_contract_ID (0);
			setpackage_id (0);
			setuser_id (0);
        } */
    }

    /** Load Constructor */
    public X_mob_trx_contract (Properties ctx, ResultSet rs, String trxName)
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
      StringBuffer sb = new StringBuffer ("X_mob_trx_contract[")
        .append(get_ID()).append("]");
      return sb.toString();
    }

	/** Set amt_last_paid.
		@param amt_last_paid amt_last_paid	  */
	public void setamt_last_paid (BigDecimal amt_last_paid)
	{
		set_Value (COLUMNNAME_amt_last_paid, amt_last_paid);
	}

	/** Get amt_last_paid.
		@return amt_last_paid	  */
	public BigDecimal getamt_last_paid () 
	{
		BigDecimal bd = (BigDecimal)get_Value(COLUMNNAME_amt_last_paid);
		if (bd == null)
			 return Env.ZERO;
		return bd;
	}

	/** Set amt_ots.
		@param amt_ots amt_ots	  */
	public void setamt_ots (BigDecimal amt_ots)
	{
		set_Value (COLUMNNAME_amt_ots, amt_ots);
	}

	/** Get amt_ots.
		@return amt_ots	  */
	public BigDecimal getamt_ots () 
	{
		BigDecimal bd = (BigDecimal)get_Value(COLUMNNAME_amt_ots);
		if (bd == null)
			 return Env.ZERO;
		return bd;
	}

	/** Set contract_no.
		@param contract_no contract_no	  */
	public void setcontract_no (int contract_no)
	{
		set_Value (COLUMNNAME_contract_no, Integer.valueOf(contract_no));
	}

	/** Get contract_no.
		@return contract_no	  */
	public int getcontract_no () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_contract_no);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	/** Set contract_status.
		@param contract_status contract_status	  */
	public void setcontract_status (String contract_status)
	{
		set_Value (COLUMNNAME_contract_status, contract_status);
	}

	/** Get contract_status.
		@return contract_status	  */
	public String getcontract_status () 
	{
		return (String)get_Value(COLUMNNAME_contract_status);
	}

	/** Set is_group.
		@param is_group is_group	  */
	public void setis_group (boolean is_group)
	{
		set_Value (COLUMNNAME_is_group, Boolean.valueOf(is_group));
	}

	/** Get is_group.
		@return is_group	  */
	public boolean is_group () 
	{
		Object oo = get_Value(COLUMNNAME_is_group);
		if (oo != null) 
		{
			 if (oo instanceof Boolean) 
				 return ((Boolean)oo).booleanValue(); 
			return "Y".equals(oo);
		}
		return false;
	}

	/** Set last_mutation_date.
		@param last_mutation_date last_mutation_date	  */
	public void setlast_mutation_date (Timestamp last_mutation_date)
	{
		set_Value (COLUMNNAME_last_mutation_date, last_mutation_date);
	}

	/** Get last_mutation_date.
		@return last_mutation_date	  */
	public Timestamp getlast_mutation_date () 
	{
		return (Timestamp)get_Value(COLUMNNAME_last_mutation_date);
	}

	/** Set last_package_id.
		@param last_package_id last_package_id	  */
	public void setlast_package_id (int last_package_id)
	{
		set_Value (COLUMNNAME_last_package_id, Integer.valueOf(last_package_id));
	}

	/** Get last_package_id.
		@return last_package_id	  */
	public int getlast_package_id () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_last_package_id);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	/** Set last_paid_date.
		@param last_paid_date last_paid_date	  */
	public void setlast_paid_date (Timestamp last_paid_date)
	{
		set_Value (COLUMNNAME_last_paid_date, last_paid_date);
	}

	/** Get last_paid_date.
		@return last_paid_date	  */
	public Timestamp getlast_paid_date () 
	{
		return (Timestamp)get_Value(COLUMNNAME_last_paid_date);
	}

	/** Set mob_trx_contract.
		@param mob_trx_contract_ID mob_trx_contract	  */
	public void setmob_trx_contract_ID (int mob_trx_contract_ID)
	{
		if (mob_trx_contract_ID < 1) 
			set_ValueNoCheck (COLUMNNAME_mob_trx_contract_ID, null);
		else 
			set_ValueNoCheck (COLUMNNAME_mob_trx_contract_ID, Integer.valueOf(mob_trx_contract_ID));
	}

	/** Get mob_trx_contract.
		@return mob_trx_contract	  */
	public int getmob_trx_contract_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_mob_trx_contract_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	/** Set mob_trx_contract_UU.
		@param mob_trx_contract_UU mob_trx_contract_UU	  */
	public void setmob_trx_contract_UU (String mob_trx_contract_UU)
	{
		set_ValueNoCheck (COLUMNNAME_mob_trx_contract_UU, mob_trx_contract_UU);
	}

	/** Get mob_trx_contract_UU.
		@return mob_trx_contract_UU	  */
	public String getmob_trx_contract_UU () 
	{
		return (String)get_Value(COLUMNNAME_mob_trx_contract_UU);
	}

	/** Set next_due_date.
		@param next_due_date next_due_date	  */
	public void setnext_due_date (Timestamp next_due_date)
	{
		set_Value (COLUMNNAME_next_due_date, next_due_date);
	}

	/** Get next_due_date.
		@return next_due_date	  */
	public Timestamp getnext_due_date () 
	{
		return (Timestamp)get_Value(COLUMNNAME_next_due_date);
	}

	/** Set package_id.
		@param package_id package_id	  */
	public void setpackage_id (int package_id)
	{
		set_Value (COLUMNNAME_package_id, Integer.valueOf(package_id));
	}

	/** Get package_id.
		@return package_id	  */
	public int getpackage_id () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_package_id);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	/** Set user_id.
		@param user_id user_id	  */
	public void setuser_id (int user_id)
	{
		set_Value (COLUMNNAME_user_id, Integer.valueOf(user_id));
	}

	/** Get user_id.
		@return user_id	  */
	public int getuser_id () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_user_id);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}
}