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

/** Generated Model for SM_Product_PlanLine
 *  @author iDempiere (generated) 
 *  @version Release 6.2 - $Id$ */
public class X_SM_Product_PlanLine extends PO implements I_SM_Product_PlanLine, I_Persistent 
{

	/**
	 *
	 */
	private static final long serialVersionUID = 20190906L;

    /** Standard Constructor */
    public X_SM_Product_PlanLine (Properties ctx, int SM_Product_PlanLine_ID, String trxName)
    {
      super (ctx, SM_Product_PlanLine_ID, trxName);
      /** if (SM_Product_PlanLine_ID == 0)
        {
			setM_Product_ID (0);
			setProcessed (false);
			setQty (Env.ZERO);
			setSM_Product_Plan_ID (0);
			setSM_Product_PlanLine_ID (0);
        } */
    }

    /** Load Constructor */
    public X_SM_Product_PlanLine (Properties ctx, ResultSet rs, String trxName)
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
      StringBuffer sb = new StringBuffer ("X_SM_Product_PlanLine[")
        .append(get_ID()).append("]");
      return sb.toString();
    }

	/** Set calculate.
		@param calculate calculate	  */
	public void setcalculate (String calculate)
	{
		set_Value (COLUMNNAME_calculate, calculate);
	}

	/** Get calculate.
		@return calculate	  */
	public String getcalculate () 
	{
		return (String)get_Value(COLUMNNAME_calculate);
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

	/** Set generatemps.
		@param generatemps generatemps	  */
	public void setgeneratemps (boolean generatemps)
	{
		set_Value (COLUMNNAME_generatemps, Boolean.valueOf(generatemps));
	}

	/** Get generatemps.
		@return generatemps	  */
	public boolean isgeneratemps () 
	{
		Object oo = get_Value(COLUMNNAME_generatemps);
		if (oo != null) 
		{
			 if (oo instanceof Boolean) 
				 return ((Boolean)oo).booleanValue(); 
			return "Y".equals(oo);
		}
		return false;
	}

	/** Set getdataforecast.
		@param getdataforecast getdataforecast	  */
	public void setgetdataforecast (boolean getdataforecast)
	{
		set_Value (COLUMNNAME_getdataforecast, Boolean.valueOf(getdataforecast));
	}

	/** Get getdataforecast.
		@return getdataforecast	  */
	public boolean isgetdataforecast () 
	{
		Object oo = get_Value(COLUMNNAME_getdataforecast);
		if (oo != null) 
		{
			 if (oo instanceof Boolean) 
				 return ((Boolean)oo).booleanValue(); 
			return "Y".equals(oo);
		}
		return false;
	}

	/** Set Get Data Order.
		@param getdataorder Get Data Order	  */
	public void setgetdataorder (String getdataorder)
	{
		set_Value (COLUMNNAME_getdataorder, getdataorder);
	}

	/** Get Get Data Order.
		@return Get Data Order	  */
	public String getgetdataorder () 
	{
		return (String)get_Value(COLUMNNAME_getdataorder);
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

//	public I_M_Product_Line getM_Product_Line() throws RuntimeException
//    {
//		return (I_M_Product_Line)MTable.get(getCtx(), I_M_Product_Line.Table_Name)
//			.getPO(getM_Product_Line_ID(), get_TrxName());	}

	/** Set M_Product_Line_ID.
		@param M_Product_Line_ID M_Product_Line_ID	  */
	public void setM_Product_Line_ID (int M_Product_Line_ID)
	{
		if (M_Product_Line_ID < 1) 
			set_Value (COLUMNNAME_M_Product_Line_ID, null);
		else 
			set_Value (COLUMNNAME_M_Product_Line_ID, Integer.valueOf(M_Product_Line_ID));
	}

	/** Get M_Product_Line_ID.
		@return M_Product_Line_ID	  */
	public int getM_Product_Line_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_M_Product_Line_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
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

	public I_SM_MPS getSM_MPS() throws RuntimeException
    {
		return (I_SM_MPS)MTable.get(getCtx(), I_SM_MPS.Table_Name)
			.getPO(getSM_MPS_ID(), get_TrxName());	}

	/** Set MPS.
		@param SM_MPS_ID MPS	  */
	public void setSM_MPS_ID (int SM_MPS_ID)
	{
		if (SM_MPS_ID < 1) 
			set_ValueNoCheck (COLUMNNAME_SM_MPS_ID, null);
		else 
			set_ValueNoCheck (COLUMNNAME_SM_MPS_ID, Integer.valueOf(SM_MPS_ID));
	}

	/** Get MPS.
		@return MPS	  */
	public int getSM_MPS_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_SM_MPS_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	public I_SM_Product_Plan getSM_Product_Plan() throws RuntimeException
    {
		return (I_SM_Product_Plan)MTable.get(getCtx(), I_SM_Product_Plan.Table_Name)
			.getPO(getSM_Product_Plan_ID(), get_TrxName());	}

	/** Set SM_Product_Plan_ID.
		@param SM_Product_Plan_ID SM_Product_Plan_ID	  */
	public void setSM_Product_Plan_ID (int SM_Product_Plan_ID)
	{
		if (SM_Product_Plan_ID < 1) 
			set_Value (COLUMNNAME_SM_Product_Plan_ID, null);
		else 
			set_Value (COLUMNNAME_SM_Product_Plan_ID, Integer.valueOf(SM_Product_Plan_ID));
	}

	/** Get SM_Product_Plan_ID.
		@return SM_Product_Plan_ID	  */
	public int getSM_Product_Plan_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_SM_Product_Plan_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	/** Set Product Plan Line.
		@param SM_Product_PlanLine_ID Product Plan Line	  */
	public void setSM_Product_PlanLine_ID (int SM_Product_PlanLine_ID)
	{
		if (SM_Product_PlanLine_ID < 1) 
			set_ValueNoCheck (COLUMNNAME_SM_Product_PlanLine_ID, null);
		else 
			set_ValueNoCheck (COLUMNNAME_SM_Product_PlanLine_ID, Integer.valueOf(SM_Product_PlanLine_ID));
	}

	/** Get Product Plan Line.
		@return Product Plan Line	  */
	public int getSM_Product_PlanLine_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_SM_Product_PlanLine_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	/** Set SM_Product_PlanLine_UU.
		@param SM_Product_PlanLine_UU SM_Product_PlanLine_UU	  */
	public void setSM_Product_PlanLine_UU (String SM_Product_PlanLine_UU)
	{
		set_ValueNoCheck (COLUMNNAME_SM_Product_PlanLine_UU, SM_Product_PlanLine_UU);
	}

	/** Get SM_Product_PlanLine_UU.
		@return SM_Product_PlanLine_UU	  */
	public String getSM_Product_PlanLine_UU () 
	{
		return (String)get_Value(COLUMNNAME_SM_Product_PlanLine_UU);
	}

	/** Set updatedtf.
		@param updatedtf updatedtf	  */
	public void setupdatedtf (boolean updatedtf)
	{
		set_Value (COLUMNNAME_updatedtf, Boolean.valueOf(updatedtf));
	}

	/** Get updatedtf.
		@return updatedtf	  */
	public boolean isupdatedtf () 
	{
		Object oo = get_Value(COLUMNNAME_updatedtf);
		if (oo != null) 
		{
			 if (oo instanceof Boolean) 
				 return ((Boolean)oo).booleanValue(); 
			return "Y".equals(oo);
		}
		return false;
	}
}