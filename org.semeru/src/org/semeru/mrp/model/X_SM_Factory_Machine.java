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
package org.semeru.mrp.model;

import java.sql.ResultSet;
import java.util.Properties;
import org.compiere.model.*;

/** Generated Model for SM_Factory_Machine
 *  @author iDempiere (generated) 
 *  @version Release 6.2 - $Id$ */
public class X_SM_Factory_Machine extends PO implements I_SM_Factory_Machine, I_Persistent 
{

	/**
	 *
	 */
	private static final long serialVersionUID = 20190820L;

    /** Standard Constructor */
    public X_SM_Factory_Machine (Properties ctx, int SM_Factory_Machine_ID, String trxName)
    {
      super (ctx, SM_Factory_Machine_ID, trxName);
      /** if (SM_Factory_Machine_ID == 0)
        {
			setSM_Factory_Machine_ID (0);
			setSM_Factory_Plan_ID (0);
        } */
    }

    /** Load Constructor */
    public X_SM_Factory_Machine (Properties ctx, ResultSet rs, String trxName)
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
      StringBuffer sb = new StringBuffer ("X_SM_Factory_Machine[")
        .append(get_ID()).append("]");
      return sb.toString();
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

	/** Set SM_Factory_Machine.
		@param SM_Factory_Machine_ID SM_Factory_Machine	  */
	public void setSM_Factory_Machine_ID (int SM_Factory_Machine_ID)
	{
		if (SM_Factory_Machine_ID < 1) 
			set_ValueNoCheck (COLUMNNAME_SM_Factory_Machine_ID, null);
		else 
			set_ValueNoCheck (COLUMNNAME_SM_Factory_Machine_ID, Integer.valueOf(SM_Factory_Machine_ID));
	}

	/** Get SM_Factory_Machine.
		@return SM_Factory_Machine	  */
	public int getSM_Factory_Machine_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_SM_Factory_Machine_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	/** Set SM_Factory_Machine_UU.
		@param SM_Factory_Machine_UU SM_Factory_Machine_UU	  */
	public void setSM_Factory_Machine_UU (String SM_Factory_Machine_UU)
	{
		set_ValueNoCheck (COLUMNNAME_SM_Factory_Machine_UU, SM_Factory_Machine_UU);
	}

	/** Get SM_Factory_Machine_UU.
		@return SM_Factory_Machine_UU	  */
	public String getSM_Factory_Machine_UU () 
	{
		return (String)get_Value(COLUMNNAME_SM_Factory_Machine_UU);
	}

	public I_SM_Factory_Plan getSM_Factory_Plan() throws RuntimeException
    {
		return (I_SM_Factory_Plan)MTable.get(getCtx(), I_SM_Factory_Plan.Table_Name)
			.getPO(getSM_Factory_Plan_ID(), get_TrxName());	}

	/** Set SM_Factory_Plan.
		@param SM_Factory_Plan_ID SM_Factory_Plan	  */
	public void setSM_Factory_Plan_ID (int SM_Factory_Plan_ID)
	{
		if (SM_Factory_Plan_ID < 1) 
			set_ValueNoCheck (COLUMNNAME_SM_Factory_Plan_ID, null);
		else 
			set_ValueNoCheck (COLUMNNAME_SM_Factory_Plan_ID, Integer.valueOf(SM_Factory_Plan_ID));
	}

	/** Get SM_Factory_Plan.
		@return SM_Factory_Plan	  */
	public int getSM_Factory_Plan_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_SM_Factory_Plan_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}
}