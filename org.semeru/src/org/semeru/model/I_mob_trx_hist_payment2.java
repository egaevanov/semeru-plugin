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

/** Generated Interface for mob_trx_hist_payment2
 *  @author iDempiere (generated) 
 *  @version Release 3.1
 */
@SuppressWarnings("all")
public interface I_mob_trx_hist_payment2 
{

    /** TableName=mob_trx_hist_payment2 */
    public static final String Table_Name = "mob_trx_hist_payment2";

    /** AD_Table_ID=1000264 */
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

    /** Column name AD_TrxOrg_ID */
    public static final String COLUMNNAME_AD_TrxOrg_ID = "AD_TrxOrg_ID";

	/** Set AD_TrxOrg_ID	  */
	public void setAD_TrxOrg_ID (int AD_TrxOrg_ID);

	/** Get AD_TrxOrg_ID	  */
	public int getAD_TrxOrg_ID();

    /** Column name amt_mutation_after */
    public static final String COLUMNNAME_amt_mutation_after = "amt_mutation_after";

	/** Set amt_mutation_after	  */
	public void setamt_mutation_after (BigDecimal amt_mutation_after);

	/** Get amt_mutation_after	  */
	public BigDecimal getamt_mutation_after();

    /** Column name amt_mutation_before */
    public static final String COLUMNNAME_amt_mutation_before = "amt_mutation_before";

	/** Set amt_mutation_before	  */
	public void setamt_mutation_before (BigDecimal amt_mutation_before);

	/** Get amt_mutation_before	  */
	public BigDecimal getamt_mutation_before();

    /** Column name amt_paid */
    public static final String COLUMNNAME_amt_paid = "amt_paid";

	/** Set amt_paid	  */
	public void setamt_paid (BigDecimal amt_paid);

	/** Get amt_paid	  */
	public BigDecimal getamt_paid();

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

    /** Column name mob_trx_hist_payment2_ID */
    public static final String COLUMNNAME_mob_trx_hist_payment2_ID = "mob_trx_hist_payment2_ID";

	/** Set mob_trx_hist_payment2	  */
	public void setmob_trx_hist_payment2_ID (int mob_trx_hist_payment2_ID);

	/** Get mob_trx_hist_payment2	  */
	public int getmob_trx_hist_payment2_ID();

    /** Column name mob_trx_hist_payment2_UU */
    public static final String COLUMNNAME_mob_trx_hist_payment2_UU = "mob_trx_hist_payment2_UU";

	/** Set mob_trx_hist_payment2_UU	  */
	public void setmob_trx_hist_payment2_UU (String mob_trx_hist_payment2_UU);

	/** Get mob_trx_hist_payment2_UU	  */
	public String getmob_trx_hist_payment2_UU();

    /** Column name paid_date */
    public static final String COLUMNNAME_paid_date = "paid_date";

	/** Set paid_date	  */
	public void setpaid_date (Timestamp paid_date);

	/** Get paid_date	  */
	public Timestamp getpaid_date();

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
}
