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

/** Generated Model for C_Pos_Setup
 *  @author iDempiere (generated) 
 *  @version Release 6.2 - $Id$ */
public class X_C_Pos_Setup extends PO implements I_C_Pos_Setup, I_Persistent 
{

	/**
	 *
	 */
	private static final long serialVersionUID = 20191011L;

    /** Standard Constructor */
    public X_C_Pos_Setup (Properties ctx, int C_Pos_Setup_ID, String trxName)
    {
      super (ctx, C_Pos_Setup_ID, trxName);
      /** if (C_Pos_Setup_ID == 0)
        {
			setC_BankAccount_ID (0);
			setC_Pos_Setup_ID (0);
			setM_Locator_ID (0);
			setM_PriceList_ID (0);
			setM_Sales_Locator_ID (0);
        } */
    }

    /** Load Constructor */
    public X_C_Pos_Setup (Properties ctx, ResultSet rs, String trxName)
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
      StringBuffer sb = new StringBuffer ("X_C_Pos_Setup[")
        .append(get_ID()).append("]");
      return sb.toString();
    }

	/** Set Address.
		@param Address Address	  */
	public void setAddress (String Address)
	{
		set_ValueNoCheck (COLUMNNAME_Address, Address);
	}

	/** Get Address.
		@return Address	  */
	public String getAddress () 
	{
		return (String)get_Value(COLUMNNAME_Address);
	}

	public org.compiere.model.I_C_BankAccount getC_BankAccount() throws RuntimeException
    {
		return (org.compiere.model.I_C_BankAccount)MTable.get(getCtx(), org.compiere.model.I_C_BankAccount.Table_Name)
			.getPO(getC_BankAccount_ID(), get_TrxName());	}

	/** Set Bank Account.
		@param C_BankAccount_ID 
		Account at the Bank
	  */
	public void setC_BankAccount_ID (int C_BankAccount_ID)
	{
		if (C_BankAccount_ID < 1) 
			set_Value (COLUMNNAME_C_BankAccount_ID, null);
		else 
			set_Value (COLUMNNAME_C_BankAccount_ID, Integer.valueOf(C_BankAccount_ID));
	}

	/** Get Bank Account.
		@return Account at the Bank
	  */
	public int getC_BankAccount_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_C_BankAccount_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	/** Set C_Pos_Setup.
		@param C_Pos_Setup_ID C_Pos_Setup	  */
	public void setC_Pos_Setup_ID (int C_Pos_Setup_ID)
	{
		if (C_Pos_Setup_ID < 1) 
			set_ValueNoCheck (COLUMNNAME_C_Pos_Setup_ID, null);
		else 
			set_ValueNoCheck (COLUMNNAME_C_Pos_Setup_ID, Integer.valueOf(C_Pos_Setup_ID));
	}

	/** Get C_Pos_Setup.
		@return C_Pos_Setup	  */
	public int getC_Pos_Setup_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_C_Pos_Setup_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	/** Set C_Pos_Setup_UU.
		@param C_Pos_Setup_UU C_Pos_Setup_UU	  */
	public void setC_Pos_Setup_UU (String C_Pos_Setup_UU)
	{
		set_ValueNoCheck (COLUMNNAME_C_Pos_Setup_UU, C_Pos_Setup_UU);
	}

	/** Get C_Pos_Setup_UU.
		@return C_Pos_Setup_UU	  */
	public String getC_Pos_Setup_UU () 
	{
		return (String)get_Value(COLUMNNAME_C_Pos_Setup_UU);
	}

	/** Set footer.
		@param footer footer	  */
	public void setfooter (String footer)
	{
		set_Value (COLUMNNAME_footer, footer);
	}

	/** Get footer.
		@return footer	  */
	public String getfooter () 
	{
		return (String)get_Value(COLUMNNAME_footer);
	}

	public I_M_Locator getM_Locator() throws RuntimeException
    {
		return (I_M_Locator)MTable.get(getCtx(), I_M_Locator.Table_Name)
			.getPO(getM_Locator_ID(), get_TrxName());	}

	/** Set Locator.
		@param M_Locator_ID 
		Warehouse Locator
	  */
	public void setM_Locator_ID (int M_Locator_ID)
	{
		if (M_Locator_ID < 1) 
			set_ValueNoCheck (COLUMNNAME_M_Locator_ID, null);
		else 
			set_ValueNoCheck (COLUMNNAME_M_Locator_ID, Integer.valueOf(M_Locator_ID));
	}

	/** Get Locator.
		@return Warehouse Locator
	  */
	public int getM_Locator_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_M_Locator_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	public org.compiere.model.I_M_PriceList getM_PriceList() throws RuntimeException
    {
		return (org.compiere.model.I_M_PriceList)MTable.get(getCtx(), org.compiere.model.I_M_PriceList.Table_Name)
			.getPO(getM_PriceList_ID(), get_TrxName());	}

	/** Set Price List.
		@param M_PriceList_ID 
		Unique identifier of a Price List
	  */
	public void setM_PriceList_ID (int M_PriceList_ID)
	{
		if (M_PriceList_ID < 1) 
			set_Value (COLUMNNAME_M_PriceList_ID, null);
		else 
			set_Value (COLUMNNAME_M_PriceList_ID, Integer.valueOf(M_PriceList_ID));
	}

	/** Get Price List.
		@return Unique identifier of a Price List
	  */
	public int getM_PriceList_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_M_PriceList_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	public I_M_Sales_Locator getM_Sales_Locator() throws RuntimeException
    {
		return (I_M_Sales_Locator)MTable.get(getCtx(), I_M_Sales_Locator.Table_Name)
			.getPO(getM_Sales_Locator_ID(), get_TrxName());	}

	/** Set M_Sales_Locator.
		@param M_Sales_Locator_ID M_Sales_Locator	  */
	public void setM_Sales_Locator_ID (int M_Sales_Locator_ID)
	{
		if (M_Sales_Locator_ID < 1) 
			set_ValueNoCheck (COLUMNNAME_M_Sales_Locator_ID, null);
		else 
			set_ValueNoCheck (COLUMNNAME_M_Sales_Locator_ID, Integer.valueOf(M_Sales_Locator_ID));
	}

	/** Get M_Sales_Locator.
		@return M_Sales_Locator	  */
	public int getM_Sales_Locator_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_M_Sales_Locator_ID);
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

	/** Set store_name.
		@param store_name store_name	  */
	public void setstore_name (String store_name)
	{
		set_Value (COLUMNNAME_store_name, store_name);
	}

	/** Get store_name.
		@return store_name	  */
	public String getstore_name () 
	{
		return (String)get_Value(COLUMNNAME_store_name);
	}
}