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

/** Generated Model for SM_Machine_Capacity
 *  @author iDempiere (generated) 
 *  @version Release 6.2 - $Id$ */
public class X_SM_Machine_Capacity extends PO implements I_SM_Machine_Capacity, I_Persistent 
{

	/**
	 *
	 */
	private static final long serialVersionUID = 20190820L;

    /** Standard Constructor */
    public X_SM_Machine_Capacity (Properties ctx, int SM_Machine_Capacity_ID, String trxName)
    {
      super (ctx, SM_Machine_Capacity_ID, trxName);
      /** if (SM_Machine_Capacity_ID == 0)
        {
			setM_Product_ID (0);
			setmaxcapacity (0);
			setmaxdailyoperation (0);
			setmaxoperator (0);
			setSM_Factory_Machine_ID (0);
			setSM_Machine_Capacity_ID (0);
        } */
    }

    /** Load Constructor */
    public X_SM_Machine_Capacity (Properties ctx, ResultSet rs, String trxName)
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
      StringBuffer sb = new StringBuffer ("X_SM_Machine_Capacity[")
        .append(get_ID()).append("]");
      return sb.toString();
    }

	public org.compiere.model.I_M_Product getM_Product() throws RuntimeException
    {
		return (org.compiere.model.I_M_Product)MTable.get(getCtx(), org.compiere.model.I_M_Product.Table_Name)
			.getPO(getM_Product_ID(), get_TrxName());	}

	/** Set Product.
		@param M_Product_ID 
		Product, Service, Item
	  */
	public void setM_Product_ID (int M_Product_ID)
	{
		if (M_Product_ID < 1) 
			set_Value (COLUMNNAME_M_Product_ID, null);
		else 
			set_Value (COLUMNNAME_M_Product_ID, Integer.valueOf(M_Product_ID));
	}

	/** Get Product.
		@return Product, Service, Item
	  */
	public int getM_Product_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_M_Product_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	/** Set maxcapacity.
		@param maxcapacity maxcapacity	  */
	public void setmaxcapacity (int maxcapacity)
	{
		set_Value (COLUMNNAME_maxcapacity, Integer.valueOf(maxcapacity));
	}

	/** Get maxcapacity.
		@return maxcapacity	  */
	public int getmaxcapacity () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_maxcapacity);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	/** Set maxdailyoperation.
		@param maxdailyoperation maxdailyoperation	  */
	public void setmaxdailyoperation (int maxdailyoperation)
	{
		set_Value (COLUMNNAME_maxdailyoperation, Integer.valueOf(maxdailyoperation));
	}

	/** Get maxdailyoperation.
		@return maxdailyoperation	  */
	public int getmaxdailyoperation () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_maxdailyoperation);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	/** Set maxoperator.
		@param maxoperator maxoperator	  */
	public void setmaxoperator (int maxoperator)
	{
		set_Value (COLUMNNAME_maxoperator, Integer.valueOf(maxoperator));
	}

	/** Get maxoperator.
		@return maxoperator	  */
	public int getmaxoperator () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_maxoperator);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	public I_SM_Factory_Machine getSM_Factory_Machine() throws RuntimeException
    {
		return (I_SM_Factory_Machine)MTable.get(getCtx(), I_SM_Factory_Machine.Table_Name)
			.getPO(getSM_Factory_Machine_ID(), get_TrxName());	}

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

	/** Set SM_Machine_Capacity.
		@param SM_Machine_Capacity_ID SM_Machine_Capacity	  */
	public void setSM_Machine_Capacity_ID (int SM_Machine_Capacity_ID)
	{
		if (SM_Machine_Capacity_ID < 1) 
			set_ValueNoCheck (COLUMNNAME_SM_Machine_Capacity_ID, null);
		else 
			set_ValueNoCheck (COLUMNNAME_SM_Machine_Capacity_ID, Integer.valueOf(SM_Machine_Capacity_ID));
	}

	/** Get SM_Machine_Capacity.
		@return SM_Machine_Capacity	  */
	public int getSM_Machine_Capacity_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_SM_Machine_Capacity_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	/** Set SM_Machine_Capacity_UU.
		@param SM_Machine_Capacity_UU SM_Machine_Capacity_UU	  */
	public void setSM_Machine_Capacity_UU (String SM_Machine_Capacity_UU)
	{
		set_ValueNoCheck (COLUMNNAME_SM_Machine_Capacity_UU, SM_Machine_Capacity_UU);
	}

	/** Get SM_Machine_Capacity_UU.
		@return SM_Machine_Capacity_UU	  */
	public String getSM_Machine_Capacity_UU () 
	{
		return (String)get_Value(COLUMNNAME_SM_Machine_Capacity_UU);
	}
}