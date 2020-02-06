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

/** Generated Model for mob_trx_hist_payment2
 *  @author iDempiere (generated) 
 *  @version Release 3.1 - $Id$ */
public class X_mob_trx_hist_payment2 extends PO implements I_mob_trx_hist_payment2, I_Persistent 
{

	/**
	 *
	 */
	private static final long serialVersionUID = 20190718L;

    /** Standard Constructor */
    public X_mob_trx_hist_payment2 (Properties ctx, int mob_trx_hist_payment2_ID, String trxName)
    {
      super (ctx, mob_trx_hist_payment2_ID, trxName);
      /** if (mob_trx_hist_payment2_ID == 0)
        {
			setcontract_no (0);
			setmob_trx_hist_payment2_ID (0);
        } */
    }

    /** Load Constructor */
    public X_mob_trx_hist_payment2 (Properties ctx, ResultSet rs, String trxName)
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
      StringBuffer sb = new StringBuffer ("X_mob_trx_hist_payment2[")
        .append(get_ID()).append("]");
      return sb.toString();
    }

	/** Set AD_TrxOrg_ID.
		@param AD_TrxOrg_ID AD_TrxOrg_ID	  */
	public void setAD_TrxOrg_ID (int AD_TrxOrg_ID)
	{
		if (AD_TrxOrg_ID < 1) 
			set_Value (COLUMNNAME_AD_TrxOrg_ID, null);
		else 
			set_Value (COLUMNNAME_AD_TrxOrg_ID, Integer.valueOf(AD_TrxOrg_ID));
	}

	/** Get AD_TrxOrg_ID.
		@return AD_TrxOrg_ID	  */
	public int getAD_TrxOrg_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_AD_TrxOrg_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	/** Set amt_mutation_after.
		@param amt_mutation_after amt_mutation_after	  */
	public void setamt_mutation_after (BigDecimal amt_mutation_after)
	{
		set_Value (COLUMNNAME_amt_mutation_after, amt_mutation_after);
	}

	/** Get amt_mutation_after.
		@return amt_mutation_after	  */
	public BigDecimal getamt_mutation_after () 
	{
		BigDecimal bd = (BigDecimal)get_Value(COLUMNNAME_amt_mutation_after);
		if (bd == null)
			 return Env.ZERO;
		return bd;
	}

	/** Set amt_mutation_before.
		@param amt_mutation_before amt_mutation_before	  */
	public void setamt_mutation_before (BigDecimal amt_mutation_before)
	{
		set_Value (COLUMNNAME_amt_mutation_before, amt_mutation_before);
	}

	/** Get amt_mutation_before.
		@return amt_mutation_before	  */
	public BigDecimal getamt_mutation_before () 
	{
		BigDecimal bd = (BigDecimal)get_Value(COLUMNNAME_amt_mutation_before);
		if (bd == null)
			 return Env.ZERO;
		return bd;
	}

	/** Set amt_paid.
		@param amt_paid amt_paid	  */
	public void setamt_paid (BigDecimal amt_paid)
	{
		set_Value (COLUMNNAME_amt_paid, amt_paid);
	}

	/** Get amt_paid.
		@return amt_paid	  */
	public BigDecimal getamt_paid () 
	{
		BigDecimal bd = (BigDecimal)get_Value(COLUMNNAME_amt_paid);
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

	/** Set is_extend.
		@param is_extend is_extend	  */
	public void setis_extend (boolean is_extend)
	{
		set_Value (COLUMNNAME_is_extend, Boolean.valueOf(is_extend));
	}

	/** Get is_extend.
		@return is_extend	  */
	public boolean is_extend () 
	{
		Object oo = get_Value(COLUMNNAME_is_extend);
		if (oo != null) 
		{
			 if (oo instanceof Boolean) 
				 return ((Boolean)oo).booleanValue(); 
			return "Y".equals(oo);
		}
		return false;
	}

	/** Set is_mutation.
		@param is_mutation is_mutation	  */
	public void setis_mutation (boolean is_mutation)
	{
		set_Value (COLUMNNAME_is_mutation, Boolean.valueOf(is_mutation));
	}

	/** Get is_mutation.
		@return is_mutation	  */
	public boolean is_mutation () 
	{
		Object oo = get_Value(COLUMNNAME_is_mutation);
		if (oo != null) 
		{
			 if (oo instanceof Boolean) 
				 return ((Boolean)oo).booleanValue(); 
			return "Y".equals(oo);
		}
		return false;
	}

	/** Set mob_trx_hist_payment2.
		@param mob_trx_hist_payment2_ID mob_trx_hist_payment2	  */
	public void setmob_trx_hist_payment2_ID (int mob_trx_hist_payment2_ID)
	{
		if (mob_trx_hist_payment2_ID < 1) 
			set_ValueNoCheck (COLUMNNAME_mob_trx_hist_payment2_ID, null);
		else 
			set_ValueNoCheck (COLUMNNAME_mob_trx_hist_payment2_ID, Integer.valueOf(mob_trx_hist_payment2_ID));
	}

	/** Get mob_trx_hist_payment2.
		@return mob_trx_hist_payment2	  */
	public int getmob_trx_hist_payment2_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_mob_trx_hist_payment2_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	/** Set mob_trx_hist_payment2_UU.
		@param mob_trx_hist_payment2_UU mob_trx_hist_payment2_UU	  */
	public void setmob_trx_hist_payment2_UU (String mob_trx_hist_payment2_UU)
	{
		set_ValueNoCheck (COLUMNNAME_mob_trx_hist_payment2_UU, mob_trx_hist_payment2_UU);
	}

	/** Get mob_trx_hist_payment2_UU.
		@return mob_trx_hist_payment2_UU	  */
	public String getmob_trx_hist_payment2_UU () 
	{
		return (String)get_Value(COLUMNNAME_mob_trx_hist_payment2_UU);
	}

	/** Set paid_date.
		@param paid_date paid_date	  */
	public void setpaid_date (Timestamp paid_date)
	{
		set_Value (COLUMNNAME_paid_date, paid_date);
	}

	/** Get paid_date.
		@return paid_date	  */
	public Timestamp getpaid_date () 
	{
		return (Timestamp)get_Value(COLUMNNAME_paid_date);
	}
}