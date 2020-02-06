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
package org.semeru.mrp.model;

import java.math.BigDecimal;
import java.sql.Timestamp;
import org.compiere.model.*;
import org.compiere.util.KeyNamePair;

/** Generated Interface for SM_MPSLine
 *  @author iDempiere (generated) 
 *  @version Release 6.2
 */
@SuppressWarnings("all")
public interface I_SM_MPSLine 
{

    /** TableName=SM_MPSLine */
    public static final String Table_Name = "SM_MPSLine";

    /** AD_Table_ID=1000053 */
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

    /** Column name atp */
    public static final String COLUMNNAME_atp = "atp";

	/** Set atp	  */
	public void setatp (BigDecimal atp);

	/** Get atp	  */
	public BigDecimal getatp();

    /** Column name C_Period_ID */
    public static final String COLUMNNAME_C_Period_ID = "C_Period_ID";

	/** Set Period.
	  * Period of the Calendar
	  */
	public void setC_Period_ID (int C_Period_ID);

	/** Get Period.
	  * Period of the Calendar
	  */
	public int getC_Period_ID();

	public org.compiere.model.I_C_Period getC_Period() throws RuntimeException;

    /** Column name cor */
    public static final String COLUMNNAME_cor = "cor";

	/** Set cor	  */
	public void setcor (BigDecimal cor);

	/** Get cor	  */
	public BigDecimal getcor();

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

    /** Column name Description */
    public static final String COLUMNNAME_Description = "Description";

	/** Set Description.
	  * Optional short description of the record
	  */
	public void setDescription (String Description);

	/** Get Description.
	  * Optional short description of the record
	  */
	public String getDescription();

    /** Column name frs */
    public static final String COLUMNNAME_frs = "frs";

	/** Set frs	  */
	public void setfrs (BigDecimal frs);

	/** Get frs	  */
	public BigDecimal getfrs();

    /** Column name isdtf */
    public static final String COLUMNNAME_isdtf = "isdtf";

	/** Set isdtf	  */
	public void setisdtf (boolean isdtf);

	/** Get isdtf	  */
	public boolean isdtf();

    /** Column name mps */
    public static final String COLUMNNAME_mps = "mps";

	/** Set mps	  */
	public void setmps (BigDecimal mps);

	/** Get mps	  */
	public BigDecimal getmps();

    /** Column name mpsdate */
    public static final String COLUMNNAME_mpsdate = "mpsdate";

	/** Set mpsdate	  */
	public void setmpsdate (Timestamp mpsdate);

	/** Get mpsdate	  */
	public Timestamp getmpsdate();

    /** Column name pab */
    public static final String COLUMNNAME_pab = "pab";

	/** Set pab	  */
	public void setpab (BigDecimal pab);

	/** Get pab	  */
	public BigDecimal getpab();

    /** Column name Processed */
    public static final String COLUMNNAME_Processed = "Processed";

	/** Set Processed.
	  * The document has been processed
	  */
	public void setProcessed (boolean Processed);

	/** Get Processed.
	  * The document has been processed
	  */
	public boolean isProcessed();

    /** Column name seq */
    public static final String COLUMNNAME_seq = "seq";

	/** Set seq	  */
	public void setseq (int seq);

	/** Get seq	  */
	public int getseq();

    /** Column name SM_MPS_ID */
    public static final String COLUMNNAME_SM_MPS_ID = "SM_MPS_ID";

	/** Set SM_MPS	  */
	public void setSM_MPS_ID (int SM_MPS_ID);

	/** Get SM_MPS	  */
	public int getSM_MPS_ID();

	public I_SM_MPS getSM_MPS() throws RuntimeException;

    /** Column name SM_MPSLine_ID */
    public static final String COLUMNNAME_SM_MPSLine_ID = "SM_MPSLine_ID";

	/** Set SM_MPSLine	  */
	public void setSM_MPSLine_ID (int SM_MPSLine_ID);

	/** Get SM_MPSLine	  */
	public int getSM_MPSLine_ID();

    /** Column name SM_MPSLine_UU */
    public static final String COLUMNNAME_SM_MPSLine_UU = "SM_MPSLine_UU";

	/** Set SM_MPSLine_UU	  */
	public void setSM_MPSLine_UU (String SM_MPSLine_UU);

	/** Get SM_MPSLine_UU	  */
	public String getSM_MPSLine_UU();

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

    /** Column name week */
    public static final String COLUMNNAME_week = "week";

	/** Set week	  */
	public void setweek (int week);

	/** Get week	  */
	public int getweek();
}
