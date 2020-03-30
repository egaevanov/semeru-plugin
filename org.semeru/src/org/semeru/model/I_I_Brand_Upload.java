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

/** Generated Interface for I_Brand_Upload
 *  @author iDempiere (generated) 
 *  @version Release 6.2
 */
@SuppressWarnings("all")
public interface I_I_Brand_Upload 
{

    /** TableName=I_Brand_Upload */
    public static final String Table_Name = "I_Brand_Upload";

    /** AD_Table_ID=1000185 */
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

    /** Column name File_Name */
    public static final String COLUMNNAME_File_Name = "File_Name";

	/** Set File_Name	  */
	public void setFile_Name (String File_Name);

	/** Get File_Name	  */
	public String getFile_Name();

    /** Column name I_Brand_Upload_ID */
    public static final String COLUMNNAME_I_Brand_Upload_ID = "I_Brand_Upload_ID";

	/** Set Upload Brand	  */
	public void setI_Brand_Upload_ID (int I_Brand_Upload_ID);

	/** Get Upload Brand	  */
	public int getI_Brand_Upload_ID();

    /** Column name I_Brand_Upload_UU */
    public static final String COLUMNNAME_I_Brand_Upload_UU = "I_Brand_Upload_UU";

	/** Set I_Brand_Upload_UU	  */
	public void setI_Brand_Upload_UU (String I_Brand_Upload_UU);

	/** Get I_Brand_Upload_UU	  */
	public String getI_Brand_Upload_UU();

    /** Column name isupload */
    public static final String COLUMNNAME_isupload = "isupload";

	/** Set isupload	  */
	public void setisupload (boolean isupload);

	/** Get isupload	  */
	public boolean isupload();

    /** Column name process_id */
    public static final String COLUMNNAME_process_id = "process_id";

	/** Set process_id	  */
	public void setprocess_id (int process_id);

	/** Get process_id	  */
	public int getprocess_id();

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
