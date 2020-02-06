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
import java.sql.Timestamp;
import java.util.Properties;
import org.compiere.model.*;
import org.compiere.util.Env;

/** Generated Model for SM_BOMRelease
 *  @author iDempiere (generated) 
 *  @version Release 6.2 - $Id$ */
public class X_SM_BOMRelease extends PO implements I_SM_BOMRelease, I_Persistent 
{

	/**
	 *
	 */
	private static final long serialVersionUID = 20190816L;

    /** Standard Constructor */
    public X_SM_BOMRelease (Properties ctx, int SM_BOMRelease_ID, String trxName)
    {
      super (ctx, SM_BOMRelease_ID, trxName);
      /** if (SM_BOMRelease_ID == 0)
        {
        } */
    }

    /** Load Constructor */
    public X_SM_BOMRelease (Properties ctx, ResultSet rs, String trxName)
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
      StringBuffer sb = new StringBuffer ("X_SM_BOMRelease[")
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

	public org.compiere.model.I_M_Production getM_Production() throws RuntimeException
    {
		return (org.compiere.model.I_M_Production)MTable.get(getCtx(), org.compiere.model.I_M_Production.Table_Name)
			.getPO(getM_Production_ID(), get_TrxName());	}

	/** Set Production.
		@param M_Production_ID 
		Plan for producing a product
	  */
	public void setM_Production_ID (int M_Production_ID)
	{
		if (M_Production_ID < 1) 
			set_ValueNoCheck (COLUMNNAME_M_Production_ID, null);
		else 
			set_ValueNoCheck (COLUMNNAME_M_Production_ID, Integer.valueOf(M_Production_ID));
	}

	/** Get Production.
		@return Plan for producing a product
	  */
	public int getM_Production_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_M_Production_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
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

	/** Set Required Date.
		@param RequiredDate Required Date	  */
	public void setRequiredDate (Timestamp RequiredDate)
	{
		set_Value (COLUMNNAME_RequiredDate, RequiredDate);
	}

	/** Get Required Date.
		@return Required Date	  */
	public Timestamp getRequiredDate () 
	{
		return (Timestamp)get_Value(COLUMNNAME_RequiredDate);
	}

	/** Set BOM Release.
		@param SM_BOMRelease_ID BOM Release	  */
	public void setSM_BOMRelease_ID (int SM_BOMRelease_ID)
	{
		if (SM_BOMRelease_ID < 1) 
			set_ValueNoCheck (COLUMNNAME_SM_BOMRelease_ID, null);
		else 
			set_ValueNoCheck (COLUMNNAME_SM_BOMRelease_ID, Integer.valueOf(SM_BOMRelease_ID));
	}

	/** Get BOM Release.
		@return BOM Release	  */
	public int getSM_BOMRelease_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_SM_BOMRelease_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	/** Set SM_BOMRelease_UU.
		@param SM_BOMRelease_UU SM_BOMRelease_UU	  */
	public void setSM_BOMRelease_UU (String SM_BOMRelease_UU)
	{
		set_ValueNoCheck (COLUMNNAME_SM_BOMRelease_UU, SM_BOMRelease_UU);
	}

	/** Get SM_BOMRelease_UU.
		@return SM_BOMRelease_UU	  */
	public String getSM_BOMRelease_UU () 
	{
		return (String)get_Value(COLUMNNAME_SM_BOMRelease_UU);
	}

	public I_SM_MPSLine getSM_MPSLine() throws RuntimeException
    {
		return (I_SM_MPSLine)MTable.get(getCtx(), I_SM_MPSLine.Table_Name)
			.getPO(getSM_MPSLine_ID(), get_TrxName());	}

	/** Set SM_MPSLine_ID.
		@param SM_MPSLine_ID SM_MPSLine_ID	  */
	public void setSM_MPSLine_ID (int SM_MPSLine_ID)
	{
		if (SM_MPSLine_ID < 1) 
			set_ValueNoCheck (COLUMNNAME_SM_MPSLine_ID, null);
		else 
			set_ValueNoCheck (COLUMNNAME_SM_MPSLine_ID, Integer.valueOf(SM_MPSLine_ID));
	}

	/** Get SM_MPSLine_ID.
		@return SM_MPSLine_ID	  */
	public int getSM_MPSLine_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_SM_MPSLine_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}
}