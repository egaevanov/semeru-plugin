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

/** Generated Model for M_Sales_Locator
 *  @author iDempiere (generated) 
 *  @version Release 3.1 - $Id$ */
public class X_M_Sales_Locator extends PO implements I_M_Sales_Locator, I_Persistent 
{

	/**
	 *
	 */
	private static final long serialVersionUID = 20190514L;

    /** Standard Constructor */
    public X_M_Sales_Locator (Properties ctx, int M_Sales_Locator_ID, String trxName)
    {
      super (ctx, M_Sales_Locator_ID, trxName);
      /** if (M_Sales_Locator_ID == 0)
        {
			setm_sales_locator_ID (0);
        } */
    }

    /** Load Constructor */
    public X_M_Sales_Locator (Properties ctx, ResultSet rs, String trxName)
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
      StringBuffer sb = new StringBuffer ("X_M_Sales_Locator[")
        .append(get_ID()).append("]");
      return sb.toString();
    }

	/** Set Address 1.
		@param Address1 
		Address line 1 for this location
	  */
	public void setAddress1 (String Address1)
	{
		set_ValueNoCheck (COLUMNNAME_Address1, Address1);
	}

	/** Get Address 1.
		@return Address line 1 for this location
	  */
	public String getAddress1 () 
	{
		return (String)get_Value(COLUMNNAME_Address1);
	}

	/** Set Address 2.
		@param Address2 
		Address line 2 for this location
	  */
	public void setAddress2 (String Address2)
	{
		set_ValueNoCheck (COLUMNNAME_Address2, Address2);
	}

	/** Get Address 2.
		@return Address line 2 for this location
	  */
	public String getAddress2 () 
	{
		return (String)get_Value(COLUMNNAME_Address2);
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

	/** Set m_sales_locator.
		@param m_sales_locator_ID m_sales_locator	  */
	public void setm_sales_locator_ID (int m_sales_locator_ID)
	{
		if (m_sales_locator_ID < 1) 
			set_ValueNoCheck (COLUMNNAME_m_sales_locator_ID, null);
		else 
			set_ValueNoCheck (COLUMNNAME_m_sales_locator_ID, Integer.valueOf(m_sales_locator_ID));
	}

	/** Get m_sales_locator.
		@return m_sales_locator	  */
	public int getm_sales_locator_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_m_sales_locator_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	/** Set m_sales_locator_UU.
		@param m_sales_locator_UU m_sales_locator_UU	  */
	public void setm_sales_locator_UU (String m_sales_locator_UU)
	{
		set_ValueNoCheck (COLUMNNAME_m_sales_locator_UU, m_sales_locator_UU);
	}

	/** Get m_sales_locator_UU.
		@return m_sales_locator_UU	  */
	public String getm_sales_locator_UU () 
	{
		return (String)get_Value(COLUMNNAME_m_sales_locator_UU);
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