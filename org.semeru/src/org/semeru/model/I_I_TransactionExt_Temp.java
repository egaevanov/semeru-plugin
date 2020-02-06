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

/** Generated Interface for I_TransactionExt_Temp
 *  @author iDempiere (generated) 
 *  @version Release 3.1
 */
@SuppressWarnings("all")
public interface I_I_TransactionExt_Temp 
{

    /** TableName=I_TransactionExt_Temp */
    public static final String Table_Name = "I_TransactionExt_Temp";

    /** AD_Table_ID=1000142 */
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

    /** Column name count_trx */
    public static final String COLUMNNAME_count_trx = "count_trx";

	/** Set count_trx	  */
	public void setcount_trx (int count_trx);

	/** Get count_trx	  */
	public int getcount_trx();

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

    /** Column name I_TransactionExt_Temp_ID */
    public static final String COLUMNNAME_I_TransactionExt_Temp_ID = "I_TransactionExt_Temp_ID";

	/** Set I_TransactionExt_Temp	  */
	public void setI_TransactionExt_Temp_ID (int I_TransactionExt_Temp_ID);

	/** Get I_TransactionExt_Temp	  */
	public int getI_TransactionExt_Temp_ID();

    /** Column name insert_transaction */
    public static final String COLUMNNAME_insert_transaction = "insert_transaction";

	/** Set insert_transaction	  */
	public void setinsert_transaction (boolean insert_transaction);

	/** Get insert_transaction	  */
	public boolean isinsert_transaction();

    /** Column name pos */
    public static final String COLUMNNAME_pos = "pos";

	/** Set pos	  */
	public void setpos (String pos);

	/** Get pos	  */
	public String getpos();

    /** Column name Result */
    public static final String COLUMNNAME_Result = "Result";

	/** Set Result.
	  * Result of the action taken
	  */
	public void setResult (String Result);

	/** Get Result.
	  * Result of the action taken
	  */
	public String getResult();

    /** Column name transaction_detail */
    public static final String COLUMNNAME_transaction_detail = "transaction_detail";

	/** Set transaction_detail	  */
	public void settransaction_detail (String transaction_detail);

	/** Get transaction_detail	  */
	public String gettransaction_detail();

    /** Column name transaction_id */
    public static final String COLUMNNAME_transaction_id = "transaction_id";

	/** Set transaction_id	  */
	public void settransaction_id (int transaction_id);

	/** Get transaction_id	  */
	public int gettransaction_id();

    /** Column name transactions */
    public static final String COLUMNNAME_transactions = "transactions";

	/** Set transactions	  */
	public void settransactions (String transactions);

	/** Get transactions	  */
	public String gettransactions();

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
