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

/** Generated Interface for I_Setup_Temp
 *  @author iDempiere (generated) 
 *  @version Release 6.2
 */
@SuppressWarnings("all")
public interface I_I_Setup_Temp 
{

    /** TableName=I_Setup_Temp */
    public static final String Table_Name = "I_Setup_Temp";

    /** AD_Table_ID=1000172 */
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

    /** Column name count_setup */
    public static final String COLUMNNAME_count_setup = "count_setup";

	/** Set count_setup	  */
	public void setcount_setup (int count_setup);

	/** Get count_setup	  */
	public int getcount_setup();

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

    /** Column name insert_setup */
    public static final String COLUMNNAME_insert_setup = "insert_setup";

	/** Set insert_setup	  */
	public void setinsert_setup (boolean insert_setup);

	/** Get insert_setup	  */
	public boolean isinsert_setup();

    /** Column name I_Setup_Temp_ID */
    public static final String COLUMNNAME_I_Setup_Temp_ID = "I_Setup_Temp_ID";

	/** Set I_Setup_Temp	  */
	public void setI_Setup_Temp_ID (int I_Setup_Temp_ID);

	/** Get I_Setup_Temp	  */
	public int getI_Setup_Temp_ID();

    /** Column name possetup */
    public static final String COLUMNNAME_possetup = "possetup";

	/** Set possetup	  */
	public void setpossetup (String possetup);

	/** Get possetup	  */
	public String getpossetup();

    /** Column name process_id */
    public static final String COLUMNNAME_process_id = "process_id";

	/** Set process_id	  */
	public void setprocess_id (int process_id);

	/** Get process_id	  */
	public int getprocess_id();

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

    /** Column name setuptype */
    public static final String COLUMNNAME_setuptype = "setuptype";

	/** Set setuptype	  */
	public void setsetuptype (String setuptype);

	/** Get setuptype	  */
	public String getsetuptype();

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
