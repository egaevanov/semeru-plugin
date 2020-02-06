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

/** Generated Interface for mob_trx_contract
 *  @author iDempiere (generated) 
 *  @version Release 3.1
 */
@SuppressWarnings("all")
public interface I_mob_trx_contract 
{

    /** TableName=mob_trx_contract */
    public static final String Table_Name = "mob_trx_contract";

    /** AD_Table_ID=1000261 */
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

    /** Column name amt_last_paid */
    public static final String COLUMNNAME_amt_last_paid = "amt_last_paid";

	/** Set amt_last_paid	  */
	public void setamt_last_paid (BigDecimal amt_last_paid);

	/** Get amt_last_paid	  */
	public BigDecimal getamt_last_paid();

    /** Column name amt_ots */
    public static final String COLUMNNAME_amt_ots = "amt_ots";

	/** Set amt_ots	  */
	public void setamt_ots (BigDecimal amt_ots);

	/** Get amt_ots	  */
	public BigDecimal getamt_ots();

    /** Column name contract_no */
    public static final String COLUMNNAME_contract_no = "contract_no";

	/** Set contract_no	  */
	public void setcontract_no (int contract_no);

	/** Get contract_no	  */
	public int getcontract_no();

    /** Column name contract_status */
    public static final String COLUMNNAME_contract_status = "contract_status";

	/** Set contract_status	  */
	public void setcontract_status (String contract_status);

	/** Get contract_status	  */
	public String getcontract_status();

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

    /** Column name is_group */
    public static final String COLUMNNAME_is_group = "is_group";

	/** Set is_group	  */
	public void setis_group (boolean is_group);

	/** Get is_group	  */
	public boolean is_group();

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

    /** Column name last_mutation_date */
    public static final String COLUMNNAME_last_mutation_date = "last_mutation_date";

	/** Set last_mutation_date	  */
	public void setlast_mutation_date (Timestamp last_mutation_date);

	/** Get last_mutation_date	  */
	public Timestamp getlast_mutation_date();

    /** Column name last_package_id */
    public static final String COLUMNNAME_last_package_id = "last_package_id";

	/** Set last_package_id	  */
	public void setlast_package_id (int last_package_id);

	/** Get last_package_id	  */
	public int getlast_package_id();

    /** Column name last_paid_date */
    public static final String COLUMNNAME_last_paid_date = "last_paid_date";

	/** Set last_paid_date	  */
	public void setlast_paid_date (Timestamp last_paid_date);

	/** Get last_paid_date	  */
	public Timestamp getlast_paid_date();

    /** Column name mob_trx_contract_ID */
    public static final String COLUMNNAME_mob_trx_contract_ID = "mob_trx_contract_ID";

	/** Set mob_trx_contract	  */
	public void setmob_trx_contract_ID (int mob_trx_contract_ID);

	/** Get mob_trx_contract	  */
	public int getmob_trx_contract_ID();

    /** Column name mob_trx_contract_UU */
    public static final String COLUMNNAME_mob_trx_contract_UU = "mob_trx_contract_UU";

	/** Set mob_trx_contract_UU	  */
	public void setmob_trx_contract_UU (String mob_trx_contract_UU);

	/** Get mob_trx_contract_UU	  */
	public String getmob_trx_contract_UU();

    /** Column name next_due_date */
    public static final String COLUMNNAME_next_due_date = "next_due_date";

	/** Set next_due_date	  */
	public void setnext_due_date (Timestamp next_due_date);

	/** Get next_due_date	  */
	public Timestamp getnext_due_date();

    /** Column name package_id */
    public static final String COLUMNNAME_package_id = "package_id";

	/** Set package_id	  */
	public void setpackage_id (int package_id);

	/** Get package_id	  */
	public int getpackage_id();

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

    /** Column name user_id */
    public static final String COLUMNNAME_user_id = "user_id";

	/** Set user_id	  */
	public void setuser_id (int user_id);

	/** Get user_id	  */
	public int getuser_id();
}
