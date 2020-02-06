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

/** Generated Interface for i_ordertrx_temp
 *  @author iDempiere (generated) 
 *  @version Release 3.1
 */
@SuppressWarnings("all")
public interface I_i_ordertrx_temp 
{

    /** TableName=i_ordertrx_temp */
    public static final String Table_Name = "i_ordertrx_temp";

    /** AD_Table_ID=1000126 */
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

    /** Column name count_order */
    public static final String COLUMNNAME_count_order = "count_order";

	/** Set count_order	  */
	public void setcount_order (int count_order);

	/** Get count_order	  */
	public int getcount_order();

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

    /** Column name i_ordertrx_temp_ID */
    public static final String COLUMNNAME_i_ordertrx_temp_ID = "i_ordertrx_temp_ID";

	/** Set Semeru Order Temporary	  */
	public void seti_ordertrx_temp_ID (int i_ordertrx_temp_ID);

	/** Get Semeru Order Temporary	  */
	public int geti_ordertrx_temp_ID();

    /** Column name insert_order */
    public static final String COLUMNNAME_insert_order = "insert_order";

	/** Set insert_order	  */
	public void setinsert_order (boolean insert_order);

	/** Get insert_order	  */
	public boolean isinsert_order();

    /** Column name order_detail */
    public static final String COLUMNNAME_order_detail = "order_detail";

	/** Set order_detail	  */
	public void setorder_detail (String order_detail);

	/** Get order_detail	  */
	public String getorder_detail();

    /** Column name orders */
    public static final String COLUMNNAME_orders = "orders";

	/** Set orders	  */
	public void setorders (String orders);

	/** Get orders	  */
	public String getorders();

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

    /** Column name transaction_id */
    public static final String COLUMNNAME_transaction_id = "transaction_id";

	/** Set transaction_id	  */
	public void settransaction_id (int transaction_id);

	/** Get transaction_id	  */
	public int gettransaction_id();

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
