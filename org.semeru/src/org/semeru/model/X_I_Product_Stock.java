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

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.util.Properties;
import org.compiere.model.*;
import org.compiere.util.Env;

/** Generated Model for I_Product_Stock
 *  @author iDempiere (generated) 
 *  @version Release 6.2 - $Id$ */
public class X_I_Product_Stock extends PO implements I_I_Product_Stock, I_Persistent 
{

	/**
	 *
	 */
	private static final long serialVersionUID = 20191217L;

    /** Standard Constructor */
    public X_I_Product_Stock (Properties ctx, int I_Product_Stock_ID, String trxName)
    {
      super (ctx, I_Product_Stock_ID, trxName);
      /** if (I_Product_Stock_ID == 0)
        {
			setI_Product_Stock_ID (0);
			setIsImportProduct (false);
			setIsImportStock (false);
			setLocator_Value (null);
			setProduct_Name (null);
			setProduct_Value (null);
			setProductCategory_Value (null);
			setProductType (null);
			setPurchasePrice (Env.ZERO);
			setQtyCount (Env.ZERO);
			setSalesPrice (Env.ZERO);
			setTaxType (null);
			setUOM_Value (null);
			setWholesalerPrice (Env.ZERO);
        } */
    }

    /** Load Constructor */
    public X_I_Product_Stock (Properties ctx, ResultSet rs, String trxName)
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
      StringBuffer sb = new StringBuffer ("X_I_Product_Stock[")
        .append(get_ID()).append("]");
      return sb.toString();
    }

	/** Set Import Product and Stock.
		@param I_Product_Stock_ID Import Product and Stock	  */
	public void setI_Product_Stock_ID (int I_Product_Stock_ID)
	{
		if (I_Product_Stock_ID < 1) 
			set_ValueNoCheck (COLUMNNAME_I_Product_Stock_ID, null);
		else 
			set_ValueNoCheck (COLUMNNAME_I_Product_Stock_ID, Integer.valueOf(I_Product_Stock_ID));
	}

	/** Get Import Product and Stock.
		@return Import Product and Stock	  */
	public int getI_Product_Stock_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_I_Product_Stock_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	/** Set I_Product_Stock_UU.
		@param I_Product_Stock_UU I_Product_Stock_UU	  */
	public void setI_Product_Stock_UU (String I_Product_Stock_UU)
	{
		set_ValueNoCheck (COLUMNNAME_I_Product_Stock_UU, I_Product_Stock_UU);
	}

	/** Get I_Product_Stock_UU.
		@return I_Product_Stock_UU	  */
	public String getI_Product_Stock_UU () 
	{
		return (String)get_Value(COLUMNNAME_I_Product_Stock_UU);
	}

	/** Set IsImportProduct.
		@param IsImportProduct IsImportProduct	  */
	public void setIsImportProduct (boolean IsImportProduct)
	{
		set_Value (COLUMNNAME_IsImportProduct, Boolean.valueOf(IsImportProduct));
	}

	/** Get IsImportProduct.
		@return IsImportProduct	  */
	public boolean isImportProduct () 
	{
		Object oo = get_Value(COLUMNNAME_IsImportProduct);
		if (oo != null) 
		{
			 if (oo instanceof Boolean) 
				 return ((Boolean)oo).booleanValue(); 
			return "Y".equals(oo);
		}
		return false;
	}

	/** Set IsImportStock.
		@param IsImportStock IsImportStock	  */
	public void setIsImportStock (boolean IsImportStock)
	{
		set_Value (COLUMNNAME_IsImportStock, Boolean.valueOf(IsImportStock));
	}

	/** Get IsImportStock.
		@return IsImportStock	  */
	public boolean isImportStock () 
	{
		Object oo = get_Value(COLUMNNAME_IsImportStock);
		if (oo != null) 
		{
			 if (oo instanceof Boolean) 
				 return ((Boolean)oo).booleanValue(); 
			return "Y".equals(oo);
		}
		return false;
	}

	/** Set Locator_Value.
		@param Locator_Value Locator_Value	  */
	public void setLocator_Value (String Locator_Value)
	{
		set_Value (COLUMNNAME_Locator_Value, Locator_Value);
	}

	/** Get Locator_Value.
		@return Locator_Value	  */
	public String getLocator_Value () 
	{
		return (String)get_Value(COLUMNNAME_Locator_Value);
	}

	/** Set Product_Name.
		@param Product_Name Product_Name	  */
	public void setProduct_Name (String Product_Name)
	{
		set_ValueNoCheck (COLUMNNAME_Product_Name, Product_Name);
	}

	/** Get Product_Name.
		@return Product_Name	  */
	public String getProduct_Name () 
	{
		return (String)get_Value(COLUMNNAME_Product_Name);
	}

	/** Set Product_Value.
		@param Product_Value Product_Value	  */
	public void setProduct_Value (String Product_Value)
	{
		set_ValueNoCheck (COLUMNNAME_Product_Value, Product_Value);
	}

	/** Get Product_Value.
		@return Product_Value	  */
	public String getProduct_Value () 
	{
		return (String)get_Value(COLUMNNAME_Product_Value);
	}

	/** Set Product Category Key.
		@param ProductCategory_Value Product Category Key	  */
	public void setProductCategory_Value (String ProductCategory_Value)
	{
		set_Value (COLUMNNAME_ProductCategory_Value, ProductCategory_Value);
	}

	/** Get Product Category Key.
		@return Product Category Key	  */
	public String getProductCategory_Value () 
	{
		return (String)get_Value(COLUMNNAME_ProductCategory_Value);
	}

	/** Set Product Type.
		@param ProductType 
		Type of product
	  */
	public void setProductType (String ProductType)
	{
		set_Value (COLUMNNAME_ProductType, ProductType);
	}
	
	@Override
	public String getProductType() {
		return (String)get_Value(COLUMNNAME_ProductType);

	}

	/** Set PurchasePrice.
		@param PurchasePrice PurchasePrice	  */
	public void setPurchasePrice (BigDecimal PurchasePrice)
	{
		set_Value (COLUMNNAME_PurchasePrice, PurchasePrice);
	}

	/** Get PurchasePrice.
		@return PurchasePrice	  */
	public BigDecimal getPurchasePrice () 
	{
		BigDecimal bd = (BigDecimal)get_Value(COLUMNNAME_PurchasePrice);
		if (bd == null)
			 return Env.ZERO;
		return bd;
	}

	/** Set Quantity count.
		@param QtyCount 
		Counted Quantity
	  */
	public void setQtyCount (BigDecimal QtyCount)
	{
		set_Value (COLUMNNAME_QtyCount, QtyCount);
	}

	/** Get Quantity count.
		@return Counted Quantity
	  */
	public BigDecimal getQtyCount () 
	{
		BigDecimal bd = (BigDecimal)get_Value(COLUMNNAME_QtyCount);
		if (bd == null)
			 return Env.ZERO;
		return bd;
	}

	/** Set SalesPrice.
		@param SalesPrice SalesPrice	  */
	public void setSalesPrice (BigDecimal SalesPrice)
	{
		set_Value (COLUMNNAME_SalesPrice, SalesPrice);
	}

	/** Get SalesPrice.
		@return SalesPrice	  */
	public BigDecimal getSalesPrice () 
	{
		BigDecimal bd = (BigDecimal)get_Value(COLUMNNAME_SalesPrice);
		if (bd == null)
			 return Env.ZERO;
		return bd;
	}

	/** Set TaxType.
		@param TaxType TaxType	  */
	public void setTaxType (String TaxType)
	{
		set_Value (COLUMNNAME_TaxType, TaxType);
	}

	/** Get TaxType.
		@return TaxType	  */
	public String getTaxType () 
	{
		return (String)get_Value(COLUMNNAME_TaxType);
	}

	/** Set UOM_Value.
		@param UOM_Value UOM_Value	  */
	public void setUOM_Value (String UOM_Value)
	{
		set_Value (COLUMNNAME_UOM_Value, UOM_Value);
	}

	/** Get UOM_Value.
		@return UOM_Value	  */
	public String getUOM_Value () 
	{
		return (String)get_Value(COLUMNNAME_UOM_Value);
	}

	/** Set WholesalerPrice.
		@param WholesalerPrice WholesalerPrice	  */
	public void setWholesalerPrice (BigDecimal WholesalerPrice)
	{
		set_Value (COLUMNNAME_WholesalerPrice, WholesalerPrice);
	}

	/** Get WholesalerPrice.
		@return WholesalerPrice	  */
	public BigDecimal getWholesalerPrice () 
	{
		BigDecimal bd = (BigDecimal)get_Value(COLUMNNAME_WholesalerPrice);
		if (bd == null)
			 return Env.ZERO;
		return bd;
	}
}