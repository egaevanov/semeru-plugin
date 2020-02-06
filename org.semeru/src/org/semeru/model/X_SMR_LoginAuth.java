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

/** Generated Model for SMR_LoginAuth
 *  @author iDempiere (generated) 
 *  @version Release 6.2 - $Id$ */
public class X_SMR_LoginAuth extends PO implements I_SMR_LoginAuth, I_Persistent 
{

	/**
	 *
	 */
	private static final long serialVersionUID = 20191203L;

    /** Standard Constructor */
    public X_SMR_LoginAuth (Properties ctx, int SMR_LoginAuth_ID, String trxName)
    {
      super (ctx, SMR_LoginAuth_ID, trxName);
      /** if (SMR_LoginAuth_ID == 0)
        {
        } */
    }

    /** Load Constructor */
    public X_SMR_LoginAuth (Properties ctx, ResultSet rs, String trxName)
    {
      super (ctx, rs, trxName);
    }

    /** AccessLevel
      * @return 4 - System 
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
      StringBuffer sb = new StringBuffer ("X_SMR_LoginAuth[")
        .append(get_ID()).append("]");
      return sb.toString();
    }

	/** Set EMail Address.
		@param EMail 
		Electronic Mail Address
	  */
	public void setEMail (String EMail)
	{
		set_Value (COLUMNNAME_EMail, EMail);
	}

	/** Get EMail Address.
		@return Electronic Mail Address
	  */
	public String getEMail () 
	{
		return (String)get_Value(COLUMNNAME_EMail);
	}

	/** Set ResultCheck.
		@param ResultCheck ResultCheck	  */
	public void setResultCheck (boolean ResultCheck)
	{
		set_Value (COLUMNNAME_ResultCheck, Boolean.valueOf(ResultCheck));
	}

	/** Get ResultCheck.
		@return ResultCheck	  */
	public boolean isResultCheck () 
	{
		Object oo = get_Value(COLUMNNAME_ResultCheck);
		if (oo != null) 
		{
			 if (oo instanceof Boolean) 
				 return ((Boolean)oo).booleanValue(); 
			return "Y".equals(oo);
		}
		return false;
	}

	/** Set SMR_LoginAuth.
		@param SMR_LoginAuth_ID SMR_LoginAuth	  */
	public void setSMR_LoginAuth_ID (int SMR_LoginAuth_ID)
	{
		if (SMR_LoginAuth_ID < 1) 
			set_ValueNoCheck (COLUMNNAME_SMR_LoginAuth_ID, null);
		else 
			set_ValueNoCheck (COLUMNNAME_SMR_LoginAuth_ID, Integer.valueOf(SMR_LoginAuth_ID));
	}

	/** Get SMR_LoginAuth.
		@return SMR_LoginAuth	  */
	public int getSMR_LoginAuth_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_SMR_LoginAuth_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	/** Set SMR_LoginAuth_UU.
		@param SMR_LoginAuth_UU SMR_LoginAuth_UU	  */
	public void setSMR_LoginAuth_UU (String SMR_LoginAuth_UU)
	{
		set_ValueNoCheck (COLUMNNAME_SMR_LoginAuth_UU, SMR_LoginAuth_UU);
	}

	/** Get SMR_LoginAuth_UU.
		@return SMR_LoginAuth_UU	  */
	public String getSMR_LoginAuth_UU () 
	{
		return (String)get_Value(COLUMNNAME_SMR_LoginAuth_UU);
	}
}