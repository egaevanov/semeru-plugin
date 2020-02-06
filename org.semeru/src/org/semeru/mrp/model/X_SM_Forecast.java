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

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.util.Properties;
import org.compiere.model.*;
import org.compiere.util.Env;

/** Generated Model for SM_Forecast
 *  @author iDempiere (generated) 
 *  @version Release 6.2 - $Id$ */
public class X_SM_Forecast extends PO implements I_SM_Forecast, I_Persistent 
{

	/**
	 *
	 */
	private static final long serialVersionUID = 20190820L;

    /** Standard Constructor */
    public X_SM_Forecast (Properties ctx, int SM_Forecast_ID, String trxName)
    {
      super (ctx, SM_Forecast_ID, trxName);
      /** if (SM_Forecast_ID == 0)
        {
			setC_Period_ID (0);
			setM_Product_ID (0);
			setQty (Env.ZERO);
			setSM_Product_PlanLine_ID (0);
        } */
    }

    /** Load Constructor */
    public X_SM_Forecast (Properties ctx, ResultSet rs, String trxName)
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
      StringBuffer sb = new StringBuffer ("X_SM_Forecast[")
        .append(get_ID()).append("]");
      return sb.toString();
    }

	public org.compiere.model.I_C_Period getC_Period() throws RuntimeException
    {
		return (org.compiere.model.I_C_Period)MTable.get(getCtx(), org.compiere.model.I_C_Period.Table_Name)
			.getPO(getC_Period_ID(), get_TrxName());	}

	/** Set Period.
		@param C_Period_ID 
		Period of the Calendar
	  */
	public void setC_Period_ID (int C_Period_ID)
	{
		if (C_Period_ID < 1) 
			set_ValueNoCheck (COLUMNNAME_C_Period_ID, null);
		else 
			set_ValueNoCheck (COLUMNNAME_C_Period_ID, Integer.valueOf(C_Period_ID));
	}

	/** Get Period.
		@return Period of the Calendar
	  */
	public int getC_Period_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_C_Period_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
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

	/** Set Quantity.
		@param Qty 
		Quantity
	  */
	public void setQty (BigDecimal Qty)
	{
		set_Value (COLUMNNAME_Qty, Qty);
	}

	/** Get Quantity.
		@return Quantity
	  */
	public BigDecimal getQty () 
	{
		BigDecimal bd = (BigDecimal)get_Value(COLUMNNAME_Qty);
		if (bd == null)
			 return Env.ZERO;
		return bd;
	}

	public I_SM_Product_PlanLine getSM_Product_PlanLine() throws RuntimeException
    {
		return (I_SM_Product_PlanLine)MTable.get(getCtx(), I_SM_Product_PlanLine.Table_Name)
			.getPO(getSM_Product_PlanLine_ID(), get_TrxName());	}

	/** Set SM_Product_PlanLine.
		@param SM_Product_PlanLine_ID SM_Product_PlanLine	  */
	public void setSM_Product_PlanLine_ID (int SM_Product_PlanLine_ID)
	{
		if (SM_Product_PlanLine_ID < 1) 
			set_ValueNoCheck (COLUMNNAME_SM_Product_PlanLine_ID, null);
		else 
			set_ValueNoCheck (COLUMNNAME_SM_Product_PlanLine_ID, Integer.valueOf(SM_Product_PlanLine_ID));
	}

	/** Get SM_Product_PlanLine.
		@return SM_Product_PlanLine	  */
	public int getSM_Product_PlanLine_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_SM_Product_PlanLine_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}
}