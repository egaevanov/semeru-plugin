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

/** Generated Model for I_Setup_Temp
 *  @author iDempiere (generated) 
 *  @version Release 6.2 - $Id$ */
public class X_I_Setup_Temp extends PO implements I_I_Setup_Temp, I_Persistent 
{

	/**
	 *
	 */
	private static final long serialVersionUID = 20200115L;

    /** Standard Constructor */
    public X_I_Setup_Temp (Properties ctx, int I_Setup_Temp_ID, String trxName)
    {
      super (ctx, I_Setup_Temp_ID, trxName);
      /** if (I_Setup_Temp_ID == 0)
        {
			setI_Setup_Temp_ID (0);
        } */
    }

    /** Load Constructor */
    public X_I_Setup_Temp (Properties ctx, ResultSet rs, String trxName)
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
      StringBuffer sb = new StringBuffer ("X_I_Setup_Temp[")
        .append(get_ID()).append("]");
      return sb.toString();
    }

	/** Set count_setup.
		@param count_setup count_setup	  */
	public void setcount_setup (int count_setup)
	{
		set_Value (COLUMNNAME_count_setup, Integer.valueOf(count_setup));
	}

	/** Get count_setup.
		@return count_setup	  */
	public int getcount_setup () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_count_setup);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	/** Set insert_setup.
		@param insert_setup insert_setup	  */
	public void setinsert_setup (boolean insert_setup)
	{
		set_Value (COLUMNNAME_insert_setup, Boolean.valueOf(insert_setup));
	}

	/** Get insert_setup.
		@return insert_setup	  */
	public boolean isinsert_setup () 
	{
		Object oo = get_Value(COLUMNNAME_insert_setup);
		if (oo != null) 
		{
			 if (oo instanceof Boolean) 
				 return ((Boolean)oo).booleanValue(); 
			return "Y".equals(oo);
		}
		return false;
	}

	/** Set I_Setup_Temp.
		@param I_Setup_Temp_ID I_Setup_Temp	  */
	public void setI_Setup_Temp_ID (int I_Setup_Temp_ID)
	{
		if (I_Setup_Temp_ID < 1) 
			set_ValueNoCheck (COLUMNNAME_I_Setup_Temp_ID, null);
		else 
			set_ValueNoCheck (COLUMNNAME_I_Setup_Temp_ID, Integer.valueOf(I_Setup_Temp_ID));
	}

	/** Get I_Setup_Temp.
		@return I_Setup_Temp	  */
	public int getI_Setup_Temp_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_I_Setup_Temp_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	/** Set possetup.
		@param possetup possetup	  */
	public void setpossetup (String possetup)
	{
		set_Value (COLUMNNAME_possetup, possetup);
	}

	/** Get possetup.
		@return possetup	  */
	public String getpossetup () 
	{
		return (String)get_Value(COLUMNNAME_possetup);
	}

	/** Set process_id.
		@param process_id process_id	  */
	public void setprocess_id (int process_id)
	{
		set_Value (COLUMNNAME_process_id, Integer.valueOf(process_id));
	}

	/** Get process_id.
		@return process_id	  */
	public int getprocess_id () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_process_id);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	/** Set Result.
		@param Result 
		Result of the action taken
	  */
	public void setResult (String Result)
	{
		set_Value (COLUMNNAME_Result, Result);
	}

	/** Get Result.
		@return Result of the action taken
	  */
	public String getResult () 
	{
		return (String)get_Value(COLUMNNAME_Result);
	}

	/** Set setuptype.
		@param setuptype setuptype	  */
	public void setsetuptype (String setuptype)
	{
		set_Value (COLUMNNAME_setuptype, setuptype);
	}

	/** Get setuptype.
		@return setuptype	  */
	public String getsetuptype () 
	{
		return (String)get_Value(COLUMNNAME_setuptype);
	}
}