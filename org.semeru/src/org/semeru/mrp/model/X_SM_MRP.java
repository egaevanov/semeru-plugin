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

/** Generated Model for SM_MRP
 *  @author iDempiere (generated) 
 *  @version Release 6.2 - $Id$ */
public class X_SM_MRP extends PO implements I_SM_MRP, I_Persistent 
{

	/**
	 *
	 */
	private static final long serialVersionUID = 20190823L;

    /** Standard Constructor */
    public X_SM_MRP (Properties ctx, int SM_MRP_ID, String trxName)
    {
      super (ctx, SM_MRP_ID, trxName);
      /** if (SM_MRP_ID == 0)
        {
			setbomgrouplevel (Env.ZERO);
			setleadtime (0);
			setlotsizemethod (null);
// 'LFL'
			setM_Product_ID (0);
			setmps (Env.ZERO);
			setProcessed (false);
			setSafetyStock (Env.ZERO);
			setSM_MPS_ID (0);
			setSM_MPSLine_ID (0);
			setSM_MRP_ID (0);
        } */
    }

    /** Load Constructor */
    public X_SM_MRP (Properties ctx, ResultSet rs, String trxName)
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
      StringBuffer sb = new StringBuffer ("X_SM_MRP[")
        .append(get_ID()).append("]");
      return sb.toString();
    }

	/** Set bomgrouplevel.
		@param bomgrouplevel bomgrouplevel	  */
	public void setbomgrouplevel (BigDecimal bomgrouplevel)
	{
		set_Value (COLUMNNAME_bomgrouplevel, bomgrouplevel);
	}

	/** Get bomgrouplevel.
		@return bomgrouplevel	  */
	public BigDecimal getbomgrouplevel () 
	{
		BigDecimal bd = (BigDecimal)get_Value(COLUMNNAME_bomgrouplevel);
		if (bd == null)
			 return Env.ZERO;
		return bd;
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

	/** Set leadtime.
		@param leadtime leadtime	  */
	public void setleadtime (int leadtime)
	{
		set_Value (COLUMNNAME_leadtime, Integer.valueOf(leadtime));
	}

	/** Get leadtime.
		@return leadtime	  */
	public int getleadtime () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_leadtime);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	/** Lot For Lot = LFL */
	public static final String LOTSIZEMETHOD_LotForLot = "LFL";
	/** Set lotsizemethod.
		@param lotsizemethod lotsizemethod	  */
	public void setlotsizemethod (String lotsizemethod)
	{

		set_Value (COLUMNNAME_lotsizemethod, lotsizemethod);
	}

	/** Get lotsizemethod.
		@return lotsizemethod	  */
	public String getlotsizemethod () 
	{
		return (String)get_Value(COLUMNNAME_lotsizemethod);
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

	/** Set mps.
		@param mps mps	  */
	public void setmps (BigDecimal mps)
	{
		set_Value (COLUMNNAME_mps, mps);
	}

	/** Get mps.
		@return mps	  */
	public BigDecimal getmps () 
	{
		BigDecimal bd = (BigDecimal)get_Value(COLUMNNAME_mps);
		if (bd == null)
			 return Env.ZERO;
		return bd;
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

	/** Set Safety Stock Qty.
		@param SafetyStock 
		Safety stock is a term used to describe a level of stock that is maintained below the cycle stock to buffer against stock-outs
	  */
	public void setSafetyStock (BigDecimal SafetyStock)
	{
		set_Value (COLUMNNAME_SafetyStock, SafetyStock);
	}

	/** Get Safety Stock Qty.
		@return Safety stock is a term used to describe a level of stock that is maintained below the cycle stock to buffer against stock-outs
	  */
	public BigDecimal getSafetyStock () 
	{
		BigDecimal bd = (BigDecimal)get_Value(COLUMNNAME_SafetyStock);
		if (bd == null)
			 return Env.ZERO;
		return bd;
	}

	public I_SM_MPS getSM_MPS() throws RuntimeException
    {
		return (I_SM_MPS)MTable.get(getCtx(), I_SM_MPS.Table_Name)
			.getPO(getSM_MPS_ID(), get_TrxName());	}

	/** Set SM_MPS.
		@param SM_MPS_ID SM_MPS	  */
	public void setSM_MPS_ID (int SM_MPS_ID)
	{
		if (SM_MPS_ID < 1) 
			set_ValueNoCheck (COLUMNNAME_SM_MPS_ID, null);
		else 
			set_ValueNoCheck (COLUMNNAME_SM_MPS_ID, Integer.valueOf(SM_MPS_ID));
	}

	/** Get SM_MPS.
		@return SM_MPS	  */
	public int getSM_MPS_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_SM_MPS_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	public I_SM_MPSLine getSM_MPSLine() throws RuntimeException
    {
		return (I_SM_MPSLine)MTable.get(getCtx(), I_SM_MPSLine.Table_Name)
			.getPO(getSM_MPSLine_ID(), get_TrxName());	}

	/** Set SM_MPSLine.
		@param SM_MPSLine_ID SM_MPSLine	  */
	public void setSM_MPSLine_ID (int SM_MPSLine_ID)
	{
		if (SM_MPSLine_ID < 1) 
			set_ValueNoCheck (COLUMNNAME_SM_MPSLine_ID, null);
		else 
			set_ValueNoCheck (COLUMNNAME_SM_MPSLine_ID, Integer.valueOf(SM_MPSLine_ID));
	}

	/** Get SM_MPSLine.
		@return SM_MPSLine	  */
	public int getSM_MPSLine_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_SM_MPSLine_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	/** Set SM_MRP.
		@param SM_MRP_ID SM_MRP	  */
	public void setSM_MRP_ID (int SM_MRP_ID)
	{
		if (SM_MRP_ID < 1) 
			set_ValueNoCheck (COLUMNNAME_SM_MRP_ID, null);
		else 
			set_ValueNoCheck (COLUMNNAME_SM_MRP_ID, Integer.valueOf(SM_MRP_ID));
	}

	/** Get SM_MRP.
		@return SM_MRP	  */
	public int getSM_MRP_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_SM_MRP_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	/** Set SM_MRP_UU.
		@param SM_MRP_UU SM_MRP_UU	  */
	public void setSM_MRP_UU (String SM_MRP_UU)
	{
		set_ValueNoCheck (COLUMNNAME_SM_MRP_UU, SM_MRP_UU);
	}

	/** Get SM_MRP_UU.
		@return SM_MRP_UU	  */
	public String getSM_MRP_UU () 
	{
		return (String)get_Value(COLUMNNAME_SM_MRP_UU);
	}
}