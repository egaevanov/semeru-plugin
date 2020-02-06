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

/** Generated Model for SMR_CashMethod
 *  @author iDempiere (generated) 
 *  @version Release 6.2 - $Id$ */
public class X_SMR_CashMethod extends PO implements I_SMR_CashMethod, I_Persistent 
{

	/**
	 *
	 */
	private static final long serialVersionUID = 20191202L;

    /** Standard Constructor */
    public X_SMR_CashMethod (Properties ctx, int SMR_CashMethod_ID, String trxName)
    {
      super (ctx, SMR_CashMethod_ID, trxName);
      /** if (SMR_CashMethod_ID == 0)
        {
        } */
    }

    /** Load Constructor */
    public X_SMR_CashMethod (Properties ctx, ResultSet rs, String trxName)
    {
      super (ctx, rs, trxName);
    }

    /** AccessLevel
      * @return 4 - System 
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
      StringBuffer sb = new StringBuffer ("X_SMR_CashMethod[")
        .append(get_ID()).append("]");
      return sb.toString();
    }

	/** Set Create lines from.
		@param CreateFrom 
		Process which will generate a new document lines based on an existing document
	  */
	public void setCreateFrom (String CreateFrom)
	{
		set_Value (COLUMNNAME_CreateFrom, CreateFrom);
	}

	/** Get Create lines from.
		@return Process which will generate a new document lines based on an existing document
	  */
	public String getCreateFrom () 
	{
		return (String)get_Value(COLUMNNAME_CreateFrom);
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

	/** Set Image URL.
		@param ImageURL 
		URL of  image
	  */
	public void setImageURL (String ImageURL)
	{
		set_Value (COLUMNNAME_ImageURL, ImageURL);
	}

	/** Get Image URL.
		@return URL of  image
	  */
	public String getImageURL () 
	{
		return (String)get_Value(COLUMNNAME_ImageURL);
	}

	/** Set Name.
		@param Name 
		Alphanumeric identifier of the entity
	  */
	public void setName (String Name)
	{
		set_Value (COLUMNNAME_Name, Name);
	}

	/** Get Name.
		@return Alphanumeric identifier of the entity
	  */
	public String getName () 
	{
		return (String)get_Value(COLUMNNAME_Name);
	}

	/** Set Rate.
		@param Rate 
		Rate or Tax or Exchange
	  */
	public void setRate (int Rate)
	{
		set_ValueNoCheck (COLUMNNAME_Rate, Integer.valueOf(Rate));
	}

	/** Get Rate.
		@return Rate or Tax or Exchange
	  */
	public int getRate () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_Rate);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	/** Set Cash Method.
		@param SMR_CashMethod_ID Cash Method	  */
	public void setSMR_CashMethod_ID (int SMR_CashMethod_ID)
	{
		if (SMR_CashMethod_ID < 1) 
			set_ValueNoCheck (COLUMNNAME_SMR_CashMethod_ID, null);
		else 
			set_ValueNoCheck (COLUMNNAME_SMR_CashMethod_ID, Integer.valueOf(SMR_CashMethod_ID));
	}

	/** Get Cash Method.
		@return Cash Method	  */
	public int getSMR_CashMethod_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_SMR_CashMethod_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	/** Set SMR_CashMethod_UU.
		@param SMR_CashMethod_UU SMR_CashMethod_UU	  */
	public void setSMR_CashMethod_UU (String SMR_CashMethod_UU)
	{
		set_ValueNoCheck (COLUMNNAME_SMR_CashMethod_UU, SMR_CashMethod_UU);
	}

	/** Get SMR_CashMethod_UU.
		@return SMR_CashMethod_UU	  */
	public String getSMR_CashMethod_UU () 
	{
		return (String)get_Value(COLUMNNAME_SMR_CashMethod_UU);
	}

	/** Set Search Key.
		@param Value 
		Search key for the record in the format required - must be unique
	  */
	public void setValue (String Value)
	{
		set_Value (COLUMNNAME_Value, Value);
	}

	/** Get Search Key.
		@return Search key for the record in the format required - must be unique
	  */
	public String getValue () 
	{
		return (String)get_Value(COLUMNNAME_Value);
	}
}