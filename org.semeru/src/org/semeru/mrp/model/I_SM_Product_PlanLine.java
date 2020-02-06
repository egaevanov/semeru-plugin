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

/** Generated Interface for SM_Product_PlanLine
 *  @author iDempiere (generated) 
 *  @version Release 6.2
 */
@SuppressWarnings("all")
public interface I_SM_Product_PlanLine 
{

    /** TableName=SM_Product_PlanLine */
    public static final String Table_Name = "SM_Product_PlanLine";

    /** AD_Table_ID=1000083 */
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

    /** Column name calculate */
    public static final String COLUMNNAME_calculate = "calculate";

	/** Set calculate	  */
	public void setcalculate (String calculate);

	/** Get calculate	  */
	public String getcalculate();

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

    /** Column name generatemps */
    public static final String COLUMNNAME_generatemps = "generatemps";

	/** Set generatemps	  */
	public void setgeneratemps (boolean generatemps);

	/** Get generatemps	  */
	public boolean isgeneratemps();

    /** Column name getdataforecast */
    public static final String COLUMNNAME_getdataforecast = "getdataforecast";

	/** Set getdataforecast	  */
	public void setgetdataforecast (boolean getdataforecast);

	/** Get getdataforecast	  */
	public boolean isgetdataforecast();

    /** Column name getdataorder */
    public static final String COLUMNNAME_getdataorder = "getdataorder";

	/** Set Get Data Order	  */
	public void setgetdataorder (String getdataorder);

	/** Get Get Data Order	  */
	public String getgetdataorder();

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

    /** Column name M_Product_Line_ID */
    public static final String COLUMNNAME_M_Product_Line_ID = "M_Product_Line_ID";

	/** Set M_Product_Line_ID	  */
	public void setM_Product_Line_ID (int M_Product_Line_ID);

	/** Get M_Product_Line_ID	  */
	public int getM_Product_Line_ID();

	//public I_M_Product_Line getM_Product_Line() throws RuntimeException;

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

    /** Column name Qty */
    public static final String COLUMNNAME_Qty = "Qty";

	/** Set Quantity.
	  * Quantity
	  */
	public void setQty (BigDecimal Qty);

	/** Get Quantity.
	  * Quantity
	  */
	public BigDecimal getQty();

    /** Column name SM_MPS_ID */
    public static final String COLUMNNAME_SM_MPS_ID = "SM_MPS_ID";

	/** Set MPS	  */
	public void setSM_MPS_ID (int SM_MPS_ID);

	/** Get MPS	  */
	public int getSM_MPS_ID();

	public I_SM_MPS getSM_MPS() throws RuntimeException;

    /** Column name SM_Product_Plan_ID */
    public static final String COLUMNNAME_SM_Product_Plan_ID = "SM_Product_Plan_ID";

	/** Set SM_Product_Plan_ID	  */
	public void setSM_Product_Plan_ID (int SM_Product_Plan_ID);

	/** Get SM_Product_Plan_ID	  */
	public int getSM_Product_Plan_ID();

	public I_SM_Product_Plan getSM_Product_Plan() throws RuntimeException;

    /** Column name SM_Product_PlanLine_ID */
    public static final String COLUMNNAME_SM_Product_PlanLine_ID = "SM_Product_PlanLine_ID";

	/** Set Product Plan Line	  */
	public void setSM_Product_PlanLine_ID (int SM_Product_PlanLine_ID);

	/** Get Product Plan Line	  */
	public int getSM_Product_PlanLine_ID();

    /** Column name SM_Product_PlanLine_UU */
    public static final String COLUMNNAME_SM_Product_PlanLine_UU = "SM_Product_PlanLine_UU";

	/** Set SM_Product_PlanLine_UU	  */
	public void setSM_Product_PlanLine_UU (String SM_Product_PlanLine_UU);

	/** Get SM_Product_PlanLine_UU	  */
	public String getSM_Product_PlanLine_UU();

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

    /** Column name updatedtf */
    public static final String COLUMNNAME_updatedtf = "updatedtf";

	/** Set updatedtf	  */
	public void setupdatedtf (boolean updatedtf);

	/** Get updatedtf	  */
	public boolean isupdatedtf();
}
