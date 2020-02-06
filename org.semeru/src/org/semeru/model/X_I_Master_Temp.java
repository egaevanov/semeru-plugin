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

/** Generated Model for I_Master_Temp
 *  @author iDempiere (generated) 
 *  @version Release 3.1 - $Id$ */
public class X_I_Master_Temp extends PO implements I_I_Master_Temp, I_Persistent 
{

	/**
	 *
	 */
	private static final long serialVersionUID = 20190206L;

    /** Standard Constructor */
    public X_I_Master_Temp (Properties ctx, int I_Master_Temp_ID, String trxName)
    {
      super (ctx, I_Master_Temp_ID, trxName);
      /** if (I_Master_Temp_ID == 0)
        {
        } */
    }

    /** Load Constructor */
    public X_I_Master_Temp (Properties ctx, ResultSet rs, String trxName)
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
      StringBuffer sb = new StringBuffer ("X_I_Master_Temp[")
        .append(get_ID()).append("]");
      return sb.toString();
    }

	/** Set count_master.
		@param count_master count_master	  */
	public void setcount_master (int count_master)
	{
		set_Value (COLUMNNAME_count_master, Integer.valueOf(count_master));
	}

	/** Get count_master.
		@return count_master	  */
	public int getcount_master () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_count_master);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	/** Set I_Master_Temp_ID.
		@param I_Master_Temp_ID I_Master_Temp_ID	  */
	public void setI_Master_Temp_ID (int I_Master_Temp_ID)
	{
		if (I_Master_Temp_ID < 1) 
			set_ValueNoCheck (COLUMNNAME_I_Master_Temp_ID, null);
		else 
			set_ValueNoCheck (COLUMNNAME_I_Master_Temp_ID, Integer.valueOf(I_Master_Temp_ID));
	}

	/** Get I_Master_Temp_ID.
		@return I_Master_Temp_ID	  */
	public int getI_Master_Temp_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_I_Master_Temp_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	/** Set insert_master.
		@param insert_master insert_master	  */
	public void setinsert_master (boolean insert_master)
	{
		set_Value (COLUMNNAME_insert_master, Boolean.valueOf(insert_master));
	}

	/** Get insert_master.
		@return insert_master	  */
	public boolean isinsert_master () 
	{
		Object oo = get_Value(COLUMNNAME_insert_master);
		if (oo != null) 
		{
			 if (oo instanceof Boolean) 
				 return ((Boolean)oo).booleanValue(); 
			return "Y".equals(oo);
		}
		return false;
	}

	/** Set master.
		@param master master	  */
	public void setmaster (String master)
	{
		set_Value (COLUMNNAME_master, master);
	}

	/** Get master.
		@return master	  */
	public String getmaster () 
	{
		return (String)get_Value(COLUMNNAME_master);
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
}