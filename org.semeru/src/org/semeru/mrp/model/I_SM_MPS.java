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

/** Generated Interface for SM_MPS
 *  @author iDempiere (generated) 
 *  @version Release 6.2
 */
@SuppressWarnings("all")
public interface I_SM_MPS 
{

    /** TableName=SM_MPS */
    public static final String Table_Name = "SM_MPS";

    /** AD_Table_ID=1000052 */
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

    /** Column name C_DocTypeTarget_ID */
    public static final String COLUMNNAME_C_DocTypeTarget_ID = "C_DocTypeTarget_ID";

	/** Set Target Document Type.
	  * Target document type for conversing documents
	  */
	public void setC_DocTypeTarget_ID (int C_DocTypeTarget_ID);

	/** Get Target Document Type.
	  * Target document type for conversing documents
	  */
	public int getC_DocTypeTarget_ID();

	public org.compiere.model.I_C_DocType getC_DocTypeTarget() throws RuntimeException;

    /** Column name calculatemps */
    public static final String COLUMNNAME_calculatemps = "calculatemps";

	/** Set calculatemps	  */
	public void setcalculatemps (String calculatemps);

	/** Get calculatemps	  */
	public String getcalculatemps();

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

    /** Column name DocStatus */
    public static final String COLUMNNAME_DocStatus = "DocStatus";

	/** Set Document Status.
	  * The current status of the document
	  */
	public void setDocStatus (String DocStatus);

	/** Get Document Status.
	  * The current status of the document
	  */
	public String getDocStatus();

    /** Column name DocumentNo */
    public static final String COLUMNNAME_DocumentNo = "DocumentNo";

	/** Set Document No.
	  * Document sequence number of the document
	  */
	public void setDocumentNo (String DocumentNo);

	/** Get Document No.
	  * Document sequence number of the document
	  */
	public String getDocumentNo();

    /** Column name dtf */
    public static final String COLUMNNAME_dtf = "dtf";

	/** Set dtf	  */
	public void setdtf (int dtf);

	/** Get dtf	  */
	public int getdtf();

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

    /** Column name leadtime */
    public static final String COLUMNNAME_leadtime = "leadtime";

	/** Set leadtime	  */
	public void setleadtime (int leadtime);

	/** Get leadtime	  */
	public int getleadtime();

    /** Column name linecreated */
    public static final String COLUMNNAME_linecreated = "linecreated";

	/** Set linecreated	  */
	public void setlinecreated (boolean linecreated);

	/** Get linecreated	  */
	public boolean islinecreated();

    /** Column name lotsizemethod */
    public static final String COLUMNNAME_lotsizemethod = "lotsizemethod";

	/** Set lotsizemethod	  */
	public void setlotsizemethod (String lotsizemethod);

	/** Get lotsizemethod	  */
	public String getlotsizemethod();

    /** Column name M_Product_ID */
    public static final String COLUMNNAME_M_Product_ID = "M_Product_ID";

	/** Set Product.
	  * Product, Service, Item
	  */
	public void setM_Product_ID (int M_Product_ID);

	/** Get Product.
	  * Product, Service, Item
	  */
	public int getM_Product_ID();

	public org.compiere.model.I_M_Product getM_Product() throws RuntimeException;

    /** Column name Name */
    public static final String COLUMNNAME_Name = "Name";

	/** Set Name.
	  * Alphanumeric identifier of the entity
	  */
	public void setName (String Name);

	/** Get Name.
	  * Alphanumeric identifier of the entity
	  */
	public String getName();

    /** Column name ohnqty */
    public static final String COLUMNNAME_ohnqty = "ohnqty";

	/** Set ohnqty	  */
	public void setohnqty (BigDecimal ohnqty);

	/** Get ohnqty	  */
	public BigDecimal getohnqty();

    /** Column name printmps */
    public static final String COLUMNNAME_printmps = "printmps";

	/** Set printmps	  */
	public void setprintmps (String printmps);

	/** Get printmps	  */
	public String getprintmps();

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

    /** Column name Processing */
    public static final String COLUMNNAME_Processing = "Processing";

	/** Set Process Now	  */
	public void setProcessing (boolean Processing);

	/** Get Process Now	  */
	public boolean isProcessing();

    /** Column name ptf */
    public static final String COLUMNNAME_ptf = "ptf";

	/** Set ptf	  */
	public void setptf (int ptf);

	/** Get ptf	  */
	public int getptf();

    /** Column name SafetyStock */
    public static final String COLUMNNAME_SafetyStock = "SafetyStock";

	/** Set Safety Stock Qty.
	  * Safety stock is a term used to describe a level of stock that is maintained below the cycle stock to buffer against stock-outs
	  */
	public void setSafetyStock (BigDecimal SafetyStock);

	/** Get Safety Stock Qty.
	  * Safety stock is a term used to describe a level of stock that is maintained below the cycle stock to buffer against stock-outs
	  */
	public BigDecimal getSafetyStock();

    /** Column name SM_MPS_ID */
    public static final String COLUMNNAME_SM_MPS_ID = "SM_MPS_ID";

	/** Set SM_MPS	  */
	public void setSM_MPS_ID (int SM_MPS_ID);

	/** Get SM_MPS	  */
	public int getSM_MPS_ID();

    /** Column name SM_MPS_UU */
    public static final String COLUMNNAME_SM_MPS_UU = "SM_MPS_UU";

	/** Set SM_MPS_UU	  */
	public void setSM_MPS_UU (String SM_MPS_UU);

	/** Get SM_MPS_UU	  */
	public String getSM_MPS_UU();

    /** Column name SM_Product_PlanLine_ID */
    public static final String COLUMNNAME_SM_Product_PlanLine_ID = "SM_Product_PlanLine_ID";

	/** Set SM_Product_PlanLine	  */
	public void setSM_Product_PlanLine_ID (int SM_Product_PlanLine_ID);

	/** Get SM_Product_PlanLine	  */
	public int getSM_Product_PlanLine_ID();

	public I_SM_Product_PlanLine getSM_Product_PlanLine() throws RuntimeException;

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

    /** Column name updatemps */
    public static final String COLUMNNAME_updatemps = "updatemps";

	/** Set updatemps	  */
	public void setupdatemps (String updatemps);

	/** Get updatemps	  */
	public String getupdatemps();
}
