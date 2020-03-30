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

/** Generated Model for I_Brand_Upload
 *  @author iDempiere (generated) 
 *  @version Release 6.2 - $Id$ */
public class X_I_Brand_Upload extends PO implements I_I_Brand_Upload, I_Persistent 
{

	/**
	 *
	 */
	private static final long serialVersionUID = 20200330L;

    /** Standard Constructor */
    public X_I_Brand_Upload (Properties ctx, int I_Brand_Upload_ID, String trxName)
    {
      super (ctx, I_Brand_Upload_ID, trxName);
      /** if (I_Brand_Upload_ID == 0)
        {
			setI_Brand_Upload_ID (0);
        } */
    }

    /** Load Constructor */
    public X_I_Brand_Upload (Properties ctx, ResultSet rs, String trxName)
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
      StringBuffer sb = new StringBuffer ("X_I_Brand_Upload[")
        .append(get_ID()).append("]");
      return sb.toString();
    }

	/** Set File_Name.
		@param File_Name File_Name	  */
	public void setFile_Name (String File_Name)
	{
		set_Value (COLUMNNAME_File_Name, File_Name);
	}

	/** Get File_Name.
		@return File_Name	  */
	public String getFile_Name () 
	{
		return (String)get_Value(COLUMNNAME_File_Name);
	}

	/** Set Upload Brand.
		@param I_Brand_Upload_ID Upload Brand	  */
	public void setI_Brand_Upload_ID (int I_Brand_Upload_ID)
	{
		if (I_Brand_Upload_ID < 1) 
			set_ValueNoCheck (COLUMNNAME_I_Brand_Upload_ID, null);
		else 
			set_ValueNoCheck (COLUMNNAME_I_Brand_Upload_ID, Integer.valueOf(I_Brand_Upload_ID));
	}

	/** Get Upload Brand.
		@return Upload Brand	  */
	public int getI_Brand_Upload_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_I_Brand_Upload_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	/** Set I_Brand_Upload_UU.
		@param I_Brand_Upload_UU I_Brand_Upload_UU	  */
	public void setI_Brand_Upload_UU (String I_Brand_Upload_UU)
	{
		set_ValueNoCheck (COLUMNNAME_I_Brand_Upload_UU, I_Brand_Upload_UU);
	}

	/** Get I_Brand_Upload_UU.
		@return I_Brand_Upload_UU	  */
	public String getI_Brand_Upload_UU () 
	{
		return (String)get_Value(COLUMNNAME_I_Brand_Upload_UU);
	}

	/** Set isupload.
		@param isupload isupload	  */
	public void setisupload (boolean isupload)
	{
		set_Value (COLUMNNAME_isupload, Boolean.valueOf(isupload));
	}

	/** Get isupload.
		@return isupload	  */
	public boolean isupload () 
	{
		Object oo = get_Value(COLUMNNAME_isupload);
		if (oo != null) 
		{
			 if (oo instanceof Boolean) 
				 return ((Boolean)oo).booleanValue(); 
			return "Y".equals(oo);
		}
		return false;
	}

	/** Set process_id.
		@param process_id process_id	  */
	public void setprocess_id (int process_id)
	{
		set_ValueNoCheck (COLUMNNAME_process_id, Integer.valueOf(process_id));
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
}