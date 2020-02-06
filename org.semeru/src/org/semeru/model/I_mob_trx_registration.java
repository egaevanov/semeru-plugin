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
package org.semeru.model;

import java.math.BigDecimal;
import java.sql.Timestamp;
import org.compiere.model.*;
import org.compiere.util.KeyNamePair;

/** Generated Interface for mob_trx_registration
 *  @author iDempiere (generated) 
 *  @version Release 6.2
 */
@SuppressWarnings("all")
public interface I_mob_trx_registration 
{

    /** TableName=mob_trx_registration */
    public static final String Table_Name = "mob_trx_registration";

    /** AD_Table_ID=1000041 */
    public static final int Table_ID = MTable.getTable_ID(Table_Name);

    KeyNamePair Model = new KeyNamePair(Table_ID, Table_Name);

    /** AccessLevel = 3 - Client - Org 
     */
    BigDecimal accessLevel = BigDecimal.valueOf(3);

    /** Load Meta Data */

    /** Column name AD_Client_ID */
    public static final String COLUMNNAME_AD_Client_ID = "AD_Client_ID";

	/** Get Client.
	  * Client/Tenant for this installation.
	  */
	public int getAD_Client_ID();

    /** Column name AD_Org_ID */
    public static final String COLUMNNAME_AD_Org_ID = "AD_Org_ID";

	/** Set Organization.
	  * Organizational entity within client
	  */
	public void setAD_Org_ID (int AD_Org_ID);

	/** Get Organization.
	  * Organizational entity within client
	  */
	public int getAD_Org_ID();

    /** Column name C_DocType_ID */
    public static final String COLUMNNAME_C_DocType_ID = "C_DocType_ID";

	/** Set Document Type.
	  * Document type or rules
	  */
	public void setC_DocType_ID (int C_DocType_ID);

	/** Get Document Type.
	  * Document type or rules
	  */
	public int getC_DocType_ID();

	public org.compiere.model.I_C_DocType getC_DocType() throws RuntimeException;

    /** Column name contract_no */
    public static final String COLUMNNAME_contract_no = "contract_no";

	/** Set contract_no	  */
	public void setcontract_no (int contract_no);

	/** Get contract_no	  */
	public int getcontract_no();

    /** Column name Created */
    public static final String COLUMNNAME_Created = "Created";

	/** Get Created.
	  * Date this record was created
	  */
	public Timestamp getCreated();

    /** Column name CreatedBy */
    public static final String COLUMNNAME_CreatedBy = "CreatedBy";

	/** Get Created By.
	  * User who created this records
	  */
	public int getCreatedBy();

    /** Column name is_extend */
    public static final String COLUMNNAME_is_extend = "is_extend";

	/** Set is_extend	  */
	public void setis_extend (boolean is_extend);

	/** Get is_extend	  */
	public boolean is_extend();

    /** Column name is_group */
    public static final String COLUMNNAME_is_group = "is_group";

	/** Set is_group	  */
	public void setis_group (boolean is_group);

	/** Get is_group	  */
	public boolean is_group();

    /** Column name is_mutation */
    public static final String COLUMNNAME_is_mutation = "is_mutation";

	/** Set is_mutation	  */
	public void setis_mutation (boolean is_mutation);

	/** Get is_mutation	  */
	public boolean is_mutation();

    /** Column name IsActive */
    public static final String COLUMNNAME_IsActive = "IsActive";

	/** Set Active.
	  * The record is active in the system
	  */
	public void setIsActive (boolean IsActive);

	/** Get Active.
	  * The record is active in the system
	  */
	public boolean isActive();

    /** Column name IsCreated */
    public static final String COLUMNNAME_IsCreated = "IsCreated";

	/** Set Records created	  */
	public void setIsCreated (String IsCreated);

	/** Get Records created	  */
	public String getIsCreated();

    /** Column name IsEmailed */
    public static final String COLUMNNAME_IsEmailed = "IsEmailed";

	/** Set IsEmailed	  */
	public void setIsEmailed (boolean IsEmailed);

	/** Get IsEmailed	  */
	public boolean isEmailed();

    /** Column name mob_trx_registration_ID */
    public static final String COLUMNNAME_mob_trx_registration_ID = "mob_trx_registration_ID";

	/** Set Mobile Registration	  */
	public void setmob_trx_registration_ID (int mob_trx_registration_ID);

	/** Get Mobile Registration	  */
	public int getmob_trx_registration_ID();

    /** Column name mob_trx_registration_no */
    public static final String COLUMNNAME_mob_trx_registration_no = "mob_trx_registration_no";

	/** Set mob_trx_registration_no	  */
	public void setmob_trx_registration_no (String mob_trx_registration_no);

	/** Get mob_trx_registration_no	  */
	public String getmob_trx_registration_no();

    /** Column name mob_trx_registration_UU */
    public static final String COLUMNNAME_mob_trx_registration_UU = "mob_trx_registration_UU";

	/** Set mob_trx_registration_UU	  */
	public void setmob_trx_registration_UU (String mob_trx_registration_UU);

	/** Get mob_trx_registration_UU	  */
	public String getmob_trx_registration_UU();

    /** Column name org_name */
    public static final String COLUMNNAME_org_name = "org_name";

	/** Set org_name	  */
	public void setorg_name (String org_name);

	/** Get org_name	  */
	public String getorg_name();

    /** Column name org_value */
    public static final String COLUMNNAME_org_value = "org_value";

	/** Set org_value	  */
	public void setorg_value (String org_value);

	/** Get org_value	  */
	public String getorg_value();

    /** Column name Package_ID */
    public static final String COLUMNNAME_Package_ID = "Package_ID";

	/** Set Package_ID	  */
	public void setPackage_ID (int Package_ID);

	/** Get Package_ID	  */
	public int getPackage_ID();

    /** Column name reg_amt_paid */
    public static final String COLUMNNAME_reg_amt_paid = "reg_amt_paid";

	/** Set reg_amt_paid	  */
	public void setreg_amt_paid (BigDecimal reg_amt_paid);

	/** Get reg_amt_paid	  */
	public BigDecimal getreg_amt_paid();

    /** Column name reg_date */
    public static final String COLUMNNAME_reg_date = "reg_date";

	/** Set reg_date	  */
	public void setreg_date (Timestamp reg_date);

	/** Get reg_date	  */
	public Timestamp getreg_date();

    /** Column name reg_date_process */
    public static final String COLUMNNAME_reg_date_process = "reg_date_process";

	/** Set reg_date_process	  */
	public void setreg_date_process (Timestamp reg_date_process);

	/** Get reg_date_process	  */
	public Timestamp getreg_date_process();

    /** Column name reg_mth */
    public static final String COLUMNNAME_reg_mth = "reg_mth";

	/** Set reg_mth	  */
	public void setreg_mth (int reg_mth);

	/** Get reg_mth	  */
	public int getreg_mth();

    /** Column name reg_paid_date */
    public static final String COLUMNNAME_reg_paid_date = "reg_paid_date";

	/** Set reg_paid_date	  */
	public void setreg_paid_date (Timestamp reg_paid_date);

	/** Get reg_paid_date	  */
	public Timestamp getreg_paid_date();

    /** Column name Updated */
    public static final String COLUMNNAME_Updated = "Updated";

	/** Get Updated.
	  * Date this record was updated
	  */
	public Timestamp getUpdated();

    /** Column name UpdatedBy */
    public static final String COLUMNNAME_UpdatedBy = "UpdatedBy";

	/** Get Updated By.
	  * User who updated this records
	  */
	public int getUpdatedBy();

    /** Column name user_address */
    public static final String COLUMNNAME_user_address = "user_address";

	/** Set user_address	  */
	public void setuser_address (String user_address);

	/** Get user_address	  */
	public String getuser_address();

    /** Column name user_birthday */
    public static final String COLUMNNAME_user_birthday = "user_birthday";

	/** Set user_birthday	  */
	public void setuser_birthday (Timestamp user_birthday);

	/** Get user_birthday	  */
	public Timestamp getuser_birthday();

    /** Column name User_City_ID */
    public static final String COLUMNNAME_User_City_ID = "User_City_ID";

	/** Set User_City_ID	  */
	public void setUser_City_ID (int User_City_ID);

	/** Get User_City_ID	  */
	public int getUser_City_ID();

    /** Column name User_Country_ID */
    public static final String COLUMNNAME_User_Country_ID = "User_Country_ID";

	/** Set User_Country_ID	  */
	public void setUser_Country_ID (int User_Country_ID);

	/** Get User_Country_ID	  */
	public int getUser_Country_ID();

    /** Column name user_email */
    public static final String COLUMNNAME_user_email = "user_email";

	/** Set user_email	  */
	public void setuser_email (String user_email);

	/** Get user_email	  */
	public String getuser_email();

    /** Column name user_name */
    public static final String COLUMNNAME_user_name = "user_name";

	/** Set user_name	  */
	public void setuser_name (String user_name);

	/** Get user_name	  */
	public String getuser_name();

    /** Column name user_pass */
    public static final String COLUMNNAME_user_pass = "user_pass";

	/** Set user_pass	  */
	public void setuser_pass (String user_pass);

	/** Get user_pass	  */
	public String getuser_pass();

    /** Column name user_phone */
    public static final String COLUMNNAME_user_phone = "user_phone";

	/** Set user_phone	  */
	public void setuser_phone (String user_phone);

	/** Get user_phone	  */
	public String getuser_phone();

    /** Column name user_phone2 */
    public static final String COLUMNNAME_user_phone2 = "user_phone2";

	/** Set user_phone2	  */
	public void setuser_phone2 (String user_phone2);

	/** Get user_phone2	  */
	public String getuser_phone2();
}
