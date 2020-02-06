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

/** Generated Model for SM_Production_Qty
 *  @author iDempiere (generated) 
 *  @version Release 6.2 - $Id$ */
public class X_SM_Production_Qty extends PO implements I_SM_Production_Qty, I_Persistent 
{

	/**
	 *
	 */
	private static final long serialVersionUID = 20190824L;

    /** Standard Constructor */
    public X_SM_Production_Qty (Properties ctx, int SM_Production_Qty_ID, String trxName)
    {
      super (ctx, SM_Production_Qty_ID, trxName);
      /** if (SM_Production_Qty_ID == 0)
        {
			setC_Period_ID (0);
			setcustorderqty (Env.ZERO);
			setinventoryqty (Env.ZERO);
			setM_Product_ID (0);
			setProductionQty (Env.ZERO);
// 0
			setSM_Product_PlanLine_ID (0);
			setSM_Production_Qty_ID (0);
        } */
    }

    /** Load Constructor */
    public X_SM_Production_Qty (Properties ctx, ResultSet rs, String trxName)
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
      StringBuffer sb = new StringBuffer ("X_SM_Production_Qty[")
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

	/** Set custorderqty.
		@param custorderqty custorderqty	  */
	public void setcustorderqty (BigDecimal custorderqty)
	{
		set_Value (COLUMNNAME_custorderqty, custorderqty);
	}

	/** Get custorderqty.
		@return custorderqty	  */
	public BigDecimal getcustorderqty () 
	{
		BigDecimal bd = (BigDecimal)get_Value(COLUMNNAME_custorderqty);
		if (bd == null)
			 return Env.ZERO;
		return bd;
	}

	/** Set inventoryqty.
		@param inventoryqty inventoryqty	  */
	public void setinventoryqty (BigDecimal inventoryqty)
	{
		set_Value (COLUMNNAME_inventoryqty, inventoryqty);
	}

	/** Get inventoryqty.
		@return inventoryqty	  */
	public BigDecimal getinventoryqty () 
	{
		BigDecimal bd = (BigDecimal)get_Value(COLUMNNAME_inventoryqty);
		if (bd == null)
			 return Env.ZERO;
		return bd;
	}

	/** Set isdtf.
		@param isdtf isdtf	  */
	public void setisdtf (boolean isdtf)
	{
		set_Value (COLUMNNAME_isdtf, Boolean.valueOf(isdtf));
	}

	/** Get isdtf.
		@return isdtf	  */
	public boolean isdtf () 
	{
		Object oo = get_Value(COLUMNNAME_isdtf);
		if (oo != null) 
		{
			 if (oo instanceof Boolean) 
				 return ((Boolean)oo).booleanValue(); 
			return "Y".equals(oo);
		}
		return false;
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

	/** Set Production Quantity.
		@param ProductionQty 
		Quantity of products to produce
	  */
	public void setProductionQty (BigDecimal ProductionQty)
	{
		set_Value (COLUMNNAME_ProductionQty, ProductionQty);
	}

	/** Get Production Quantity.
		@return Quantity of products to produce
	  */
	public BigDecimal getProductionQty () 
	{
		BigDecimal bd = (BigDecimal)get_Value(COLUMNNAME_ProductionQty);
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

	/** Set SM_Production_Qty.
		@param SM_Production_Qty_ID SM_Production_Qty	  */
	public void setSM_Production_Qty_ID (int SM_Production_Qty_ID)
	{
		if (SM_Production_Qty_ID < 1) 
			set_ValueNoCheck (COLUMNNAME_SM_Production_Qty_ID, null);
		else 
			set_ValueNoCheck (COLUMNNAME_SM_Production_Qty_ID, Integer.valueOf(SM_Production_Qty_ID));
	}

	/** Get SM_Production_Qty.
		@return SM_Production_Qty	  */
	public int getSM_Production_Qty_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_SM_Production_Qty_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	/** Set SM_Production_Qty_UU.
		@param SM_Production_Qty_UU SM_Production_Qty_UU	  */
	public void setSM_Production_Qty_UU (String SM_Production_Qty_UU)
	{
		set_ValueNoCheck (COLUMNNAME_SM_Production_Qty_UU, SM_Production_Qty_UU);
	}

	/** Get SM_Production_Qty_UU.
		@return SM_Production_Qty_UU	  */
	public String getSM_Production_Qty_UU () 
	{
		return (String)get_Value(COLUMNNAME_SM_Production_Qty_UU);
	}
}