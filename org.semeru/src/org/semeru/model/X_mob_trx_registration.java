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
import java.sql.Timestamp;
import java.util.Properties;
import org.compiere.model.*;
import org.compiere.util.Env;

/** Generated Model for mob_trx_registration
 *  @author iDempiere (generated) 
 *  @version Release 6.2 - $Id$ */
public class X_mob_trx_registration extends PO implements I_mob_trx_registration, I_Persistent 
{

	/**
	 *
	 */
	private static final long serialVersionUID = 20191011L;

    /** Standard Constructor */
    public X_mob_trx_registration (Properties ctx, int mob_trx_registration_ID, String trxName)
    {
      super (ctx, mob_trx_registration_ID, trxName);
      /** if (mob_trx_registration_ID == 0)
        {
			setmob_trx_registration_no (null);
			setorg_name (null);
			setorg_value (null);
			setPackage_ID (0);
			setuser_email (null);
			setuser_name (null);
			setuser_pass (null);
        } */
    }

    /** Load Constructor */
    public X_mob_trx_registration (Properties ctx, ResultSet rs, String trxName)
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
      StringBuffer sb = new StringBuffer ("X_mob_trx_registration[")
        .append(get_ID()).append("]");
      return sb.toString();
    }

	public org.compiere.model.I_C_DocType getC_DocType() throws RuntimeException
    {
		return (org.compiere.model.I_C_DocType)MTable.get(getCtx(), org.compiere.model.I_C_DocType.Table_Name)
			.getPO(getC_DocType_ID(), get_TrxName());	}

	/** Set Document Type.
		@param C_DocType_ID 
		Document type or rules
	  */
	public void setC_DocType_ID (int C_DocType_ID)
	{
		if (C_DocType_ID < 0) 
			set_ValueNoCheck (COLUMNNAME_C_DocType_ID, null);
		else 
			set_ValueNoCheck (COLUMNNAME_C_DocType_ID, Integer.valueOf(C_DocType_ID));
	}

	/** Get Document Type.
		@return Document type or rules
	  */
	public int getC_DocType_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_C_DocType_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	/** Set contract_no.
		@param contract_no contract_no	  */
	public void setcontract_no (int contract_no)
	{
		set_Value (COLUMNNAME_contract_no, Integer.valueOf(contract_no));
	}

	/** Get contract_no.
		@return contract_no	  */
	public int getcontract_no () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_contract_no);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	/** Set is_extend.
		@param is_extend is_extend	  */
	public void setis_extend (boolean is_extend)
	{
		set_Value (COLUMNNAME_is_extend, Boolean.valueOf(is_extend));
	}

	/** Get is_extend.
		@return is_extend	  */
	public boolean is_extend () 
	{
		Object oo = get_Value(COLUMNNAME_is_extend);
		if (oo != null) 
		{
			 if (oo instanceof Boolean) 
				 return ((Boolean)oo).booleanValue(); 
			return "Y".equals(oo);
		}
		return false;
	}

	/** Set is_group.
		@param is_group is_group	  */
	public void setis_group (boolean is_group)
	{
		set_Value (COLUMNNAME_is_group, Boolean.valueOf(is_group));
	}

	/** Get is_group.
		@return is_group	  */
	public boolean is_group () 
	{
		Object oo = get_Value(COLUMNNAME_is_group);
		if (oo != null) 
		{
			 if (oo instanceof Boolean) 
				 return ((Boolean)oo).booleanValue(); 
			return "Y".equals(oo);
		}
		return false;
	}

	/** Set is_mutation.
		@param is_mutation is_mutation	  */
	public void setis_mutation (boolean is_mutation)
	{
		set_Value (COLUMNNAME_is_mutation, Boolean.valueOf(is_mutation));
	}

	/** Get is_mutation.
		@return is_mutation	  */
	public boolean is_mutation () 
	{
		Object oo = get_Value(COLUMNNAME_is_mutation);
		if (oo != null) 
		{
			 if (oo instanceof Boolean) 
				 return ((Boolean)oo).booleanValue(); 
			return "Y".equals(oo);
		}
		return false;
	}

	/** IsCreated AD_Reference_ID=319 */
	public static final int ISCREATED_AD_Reference_ID=319;
	/** Yes = Y */
	public static final String ISCREATED_Yes = "Y";
	/** No = N */
	public static final String ISCREATED_No = "N";
	/** Set Records created.
		@param IsCreated Records created	  */
	public void setIsCreated (String IsCreated)
	{

		set_Value (COLUMNNAME_IsCreated, IsCreated);
	}

	/** Get Records created.
		@return Records created	  */
	public String getIsCreated () 
	{
		return (String)get_Value(COLUMNNAME_IsCreated);
	}

	/** Set IsEmailed.
		@param IsEmailed IsEmailed	  */
	public void setIsEmailed (boolean IsEmailed)
	{
		set_Value (COLUMNNAME_IsEmailed, Boolean.valueOf(IsEmailed));
	}

	/** Get IsEmailed.
		@return IsEmailed	  */
	public boolean isEmailed () 
	{
		Object oo = get_Value(COLUMNNAME_IsEmailed);
		if (oo != null) 
		{
			 if (oo instanceof Boolean) 
				 return ((Boolean)oo).booleanValue(); 
			return "Y".equals(oo);
		}
		return false;
	}

	/** Set Mobile Registration.
		@param mob_trx_registration_ID Mobile Registration	  */
	public void setmob_trx_registration_ID (int mob_trx_registration_ID)
	{
		if (mob_trx_registration_ID < 1) 
			set_ValueNoCheck (COLUMNNAME_mob_trx_registration_ID, null);
		else 
			set_ValueNoCheck (COLUMNNAME_mob_trx_registration_ID, Integer.valueOf(mob_trx_registration_ID));
	}

	/** Get Mobile Registration.
		@return Mobile Registration	  */
	public int getmob_trx_registration_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_mob_trx_registration_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	/** Set mob_trx_registration_no.
		@param mob_trx_registration_no mob_trx_registration_no	  */
	public void setmob_trx_registration_no (String mob_trx_registration_no)
	{
		set_Value (COLUMNNAME_mob_trx_registration_no, mob_trx_registration_no);
	}

	/** Get mob_trx_registration_no.
		@return mob_trx_registration_no	  */
	public String getmob_trx_registration_no () 
	{
		return (String)get_Value(COLUMNNAME_mob_trx_registration_no);
	}

	/** Set mob_trx_registration_UU.
		@param mob_trx_registration_UU mob_trx_registration_UU	  */
	public void setmob_trx_registration_UU (String mob_trx_registration_UU)
	{
		set_ValueNoCheck (COLUMNNAME_mob_trx_registration_UU, mob_trx_registration_UU);
	}

	/** Get mob_trx_registration_UU.
		@return mob_trx_registration_UU	  */
	public String getmob_trx_registration_UU () 
	{
		return (String)get_Value(COLUMNNAME_mob_trx_registration_UU);
	}

	/** Set org_name.
		@param org_name org_name	  */
	public void setorg_name (String org_name)
	{
		set_Value (COLUMNNAME_org_name, org_name);
	}

	/** Get org_name.
		@return org_name	  */
	public String getorg_name () 
	{
		return (String)get_Value(COLUMNNAME_org_name);
	}

	/** Set org_value.
		@param org_value org_value	  */
	public void setorg_value (String org_value)
	{
		set_Value (COLUMNNAME_org_value, org_value);
	}

	/** Get org_value.
		@return org_value	  */
	public String getorg_value () 
	{
		return (String)get_Value(COLUMNNAME_org_value);
	}

	/** Set Package_ID.
		@param Package_ID Package_ID	  */
	public void setPackage_ID (int Package_ID)
	{
		if (Package_ID < 1) 
			set_Value (COLUMNNAME_Package_ID, null);
		else 
			set_Value (COLUMNNAME_Package_ID, Integer.valueOf(Package_ID));
	}

	/** Get Package_ID.
		@return Package_ID	  */
	public int getPackage_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_Package_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	/** Set reg_amt_paid.
		@param reg_amt_paid reg_amt_paid	  */
	public void setreg_amt_paid (BigDecimal reg_amt_paid)
	{
		set_Value (COLUMNNAME_reg_amt_paid, reg_amt_paid);
	}

	/** Get reg_amt_paid.
		@return reg_amt_paid	  */
	public BigDecimal getreg_amt_paid () 
	{
		BigDecimal bd = (BigDecimal)get_Value(COLUMNNAME_reg_amt_paid);
		if (bd == null)
			 return Env.ZERO;
		return bd;
	}

	/** Set reg_date.
		@param reg_date reg_date	  */
	public void setreg_date (Timestamp reg_date)
	{
		set_Value (COLUMNNAME_reg_date, reg_date);
	}

	/** Get reg_date.
		@return reg_date	  */
	public Timestamp getreg_date () 
	{
		return (Timestamp)get_Value(COLUMNNAME_reg_date);
	}

	/** Set reg_date_process.
		@param reg_date_process reg_date_process	  */
	public void setreg_date_process (Timestamp reg_date_process)
	{
		set_Value (COLUMNNAME_reg_date_process, reg_date_process);
	}

	/** Get reg_date_process.
		@return reg_date_process	  */
	public Timestamp getreg_date_process () 
	{
		return (Timestamp)get_Value(COLUMNNAME_reg_date_process);
	}

	/** Set reg_mth.
		@param reg_mth reg_mth	  */
	public void setreg_mth (int reg_mth)
	{
		set_Value (COLUMNNAME_reg_mth, Integer.valueOf(reg_mth));
	}

	/** Get reg_mth.
		@return reg_mth	  */
	public int getreg_mth () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_reg_mth);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	/** Set reg_paid_date.
		@param reg_paid_date reg_paid_date	  */
	public void setreg_paid_date (Timestamp reg_paid_date)
	{
		set_Value (COLUMNNAME_reg_paid_date, reg_paid_date);
	}

	/** Get reg_paid_date.
		@return reg_paid_date	  */
	public Timestamp getreg_paid_date () 
	{
		return (Timestamp)get_Value(COLUMNNAME_reg_paid_date);
	}

	/** Set user_address.
		@param user_address user_address	  */
	public void setuser_address (String user_address)
	{
		set_Value (COLUMNNAME_user_address, user_address);
	}

	/** Get user_address.
		@return user_address	  */
	public String getuser_address () 
	{
		return (String)get_Value(COLUMNNAME_user_address);
	}

	/** Set user_birthday.
		@param user_birthday user_birthday	  */
	public void setuser_birthday (Timestamp user_birthday)
	{
		set_Value (COLUMNNAME_user_birthday, user_birthday);
	}

	/** Get user_birthday.
		@return user_birthday	  */
	public Timestamp getuser_birthday () 
	{
		return (Timestamp)get_Value(COLUMNNAME_user_birthday);
	}

	/** Set User_City_ID.
		@param User_City_ID User_City_ID	  */
	public void setUser_City_ID (int User_City_ID)
	{
		if (User_City_ID < 1) 
			set_Value (COLUMNNAME_User_City_ID, null);
		else 
			set_Value (COLUMNNAME_User_City_ID, Integer.valueOf(User_City_ID));
	}

	/** Get User_City_ID.
		@return User_City_ID	  */
	public int getUser_City_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_User_City_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	/** Set User_Country_ID.
		@param User_Country_ID User_Country_ID	  */
	public void setUser_Country_ID (int User_Country_ID)
	{
		if (User_Country_ID < 1) 
			set_Value (COLUMNNAME_User_Country_ID, null);
		else 
			set_Value (COLUMNNAME_User_Country_ID, Integer.valueOf(User_Country_ID));
	}

	/** Get User_Country_ID.
		@return User_Country_ID	  */
	public int getUser_Country_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_User_Country_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	/** Set user_email.
		@param user_email user_email	  */
	public void setuser_email (String user_email)
	{
		set_Value (COLUMNNAME_user_email, user_email);
	}

	/** Get user_email.
		@return user_email	  */
	public String getuser_email () 
	{
		return (String)get_Value(COLUMNNAME_user_email);
	}

	/** Set user_name.
		@param user_name user_name	  */
	public void setuser_name (String user_name)
	{
		set_Value (COLUMNNAME_user_name, user_name);
	}

	/** Get user_name.
		@return user_name	  */
	public String getuser_name () 
	{
		return (String)get_Value(COLUMNNAME_user_name);
	}

	/** Set user_pass.
		@param user_pass user_pass	  */
	public void setuser_pass (String user_pass)
	{
		set_Value (COLUMNNAME_user_pass, user_pass);
	}

	/** Get user_pass.
		@return user_pass	  */
	public String getuser_pass () 
	{
		return (String)get_Value(COLUMNNAME_user_pass);
	}

	/** Set user_phone.
		@param user_phone user_phone	  */
	public void setuser_phone (String user_phone)
	{
		set_Value (COLUMNNAME_user_phone, user_phone);
	}

	/** Get user_phone.
		@return user_phone	  */
	public String getuser_phone () 
	{
		return (String)get_Value(COLUMNNAME_user_phone);
	}

	/** Set user_phone2.
		@param user_phone2 user_phone2	  */
	public void setuser_phone2 (String user_phone2)
	{
		set_Value (COLUMNNAME_user_phone2, user_phone2);
	}

	/** Get user_phone2.
		@return user_phone2	  */
	public String getuser_phone2 () 
	{
		return (String)get_Value(COLUMNNAME_user_phone2);
	}
}