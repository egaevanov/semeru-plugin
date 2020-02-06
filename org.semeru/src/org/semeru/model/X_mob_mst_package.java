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
import java.util.Properties;
import org.compiere.model.*;
import org.compiere.util.Env;

/** Generated Model for mob_mst_package
 *  @author iDempiere (generated) 
 *  @version Release 6.2 - $Id$ */
public class X_mob_mst_package extends PO implements I_mob_mst_package, I_Persistent 
{

	/**
	 *
	 */
	private static final long serialVersionUID = 20191015L;

    /** Standard Constructor */
    public X_mob_mst_package (Properties ctx, int mob_mst_package_ID, String trxName)
    {
      super (ctx, mob_mst_package_ID, trxName);
      /** if (mob_mst_package_ID == 0)
        {
			setmob_mst_package_ID (0);
        } */
    }

    /** Load Constructor */
    public X_mob_mst_package (Properties ctx, ResultSet rs, String trxName)
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
      StringBuffer sb = new StringBuffer ("X_mob_mst_package[")
        .append(get_ID()).append("]");
      return sb.toString();
    }

	/** Set amt_value.
		@param amt_value amt_value	  */
	public void setamt_value (BigDecimal amt_value)
	{
		set_Value (COLUMNNAME_amt_value, amt_value);
	}

	/** Get amt_value.
		@return amt_value	  */
	public BigDecimal getamt_value () 
	{
		BigDecimal bd = (BigDecimal)get_Value(COLUMNNAME_amt_value);
		if (bd == null)
			 return Env.ZERO;
		return bd;
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

	/** Set is_pocess.
		@param is_pocess is_pocess	  */
	public void setis_pocess (boolean is_pocess)
	{
		set_Value (COLUMNNAME_is_pocess, Boolean.valueOf(is_pocess));
	}

	/** Get is_pocess.
		@return is_pocess	  */
	public boolean is_pocess () 
	{
		Object oo = get_Value(COLUMNNAME_is_pocess);
		if (oo != null) 
		{
			 if (oo instanceof Boolean) 
				 return ((Boolean)oo).booleanValue(); 
			return "Y".equals(oo);
		}
		return false;
	}

	/** Set mob_mst_package.
		@param mob_mst_package_ID mob_mst_package	  */
	public void setmob_mst_package_ID (int mob_mst_package_ID)
	{
		if (mob_mst_package_ID < 1) 
			set_ValueNoCheck (COLUMNNAME_mob_mst_package_ID, null);
		else 
			set_ValueNoCheck (COLUMNNAME_mob_mst_package_ID, Integer.valueOf(mob_mst_package_ID));
	}

	/** Get mob_mst_package.
		@return mob_mst_package	  */
	public int getmob_mst_package_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_mob_mst_package_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	/** Set mob_mst_package_UU.
		@param mob_mst_package_UU mob_mst_package_UU	  */
	public void setmob_mst_package_UU (String mob_mst_package_UU)
	{
		set_ValueNoCheck (COLUMNNAME_mob_mst_package_UU, mob_mst_package_UU);
	}

	/** Get mob_mst_package_UU.
		@return mob_mst_package_UU	  */
	public String getmob_mst_package_UU () 
	{
		return (String)get_Value(COLUMNNAME_mob_mst_package_UU);
	}

	/** Set name_package.
		@param name_package name_package	  */
	public void setname_package (String name_package)
	{
		set_Value (COLUMNNAME_name_package, name_package);
	}

	/** Get name_package.
		@return name_package	  */
	public String getname_package () 
	{
		return (String)get_Value(COLUMNNAME_name_package);
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

	/** Set value_package.
		@param value_package value_package	  */
	public void setvalue_package (String value_package)
	{
		set_Value (COLUMNNAME_value_package, value_package);
	}

	/** Get value_package.
		@return value_package	  */
	public String getvalue_package () 
	{
		return (String)get_Value(COLUMNNAME_value_package);
	}
}