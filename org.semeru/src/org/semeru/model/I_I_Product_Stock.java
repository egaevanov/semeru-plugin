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

/** Generated Interface for I_Product_Stock
 *  @author iDempiere (generated) 
 *  @version Release 6.2
 */
@SuppressWarnings("all")
public interface I_I_Product_Stock 
{

    /** TableName=I_Product_Stock */
    public static final String Table_Name = "I_Product_Stock";

    /** AD_Table_ID=1000110 */
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

    /** Column name I_Product_Stock_ID */
    public static final String COLUMNNAME_I_Product_Stock_ID = "I_Product_Stock_ID";

	/** Set Import Product and Stock	  */
	public void setI_Product_Stock_ID (int I_Product_Stock_ID);

	/** Get Import Product and Stock	  */
	public int getI_Product_Stock_ID();

    /** Column name I_Product_Stock_UU */
    public static final String COLUMNNAME_I_Product_Stock_UU = "I_Product_Stock_UU";

	/** Set I_Product_Stock_UU	  */
	public void setI_Product_Stock_UU (String I_Product_Stock_UU);

	/** Get I_Product_Stock_UU	  */
	public String getI_Product_Stock_UU();

    /** Column name IsImportProduct */
    public static final String COLUMNNAME_IsImportProduct = "IsImportProduct";

	/** Set IsImportProduct	  */
	public void setIsImportProduct (boolean IsImportProduct);

	/** Get IsImportProduct	  */
	public boolean isImportProduct();

    /** Column name IsImportStock */
    public static final String COLUMNNAME_IsImportStock = "IsImportStock";

	/** Set IsImportStock	  */
	public void setIsImportStock (boolean IsImportStock);

	/** Get IsImportStock	  */
	public boolean isImportStock();

    /** Column name Locator_Value */
    public static final String COLUMNNAME_Locator_Value = "Locator_Value";

	/** Set Locator_Value	  */
	public void setLocator_Value (String Locator_Value);

	/** Get Locator_Value	  */
	public String getLocator_Value();

    /** Column name Product_Name */
    public static final String COLUMNNAME_Product_Name = "Product_Name";

	/** Set Product_Name	  */
	public void setProduct_Name (String Product_Name);

	/** Get Product_Name	  */
	public String getProduct_Name();

    /** Column name Product_Value */
    public static final String COLUMNNAME_Product_Value = "Product_Value";

	/** Set Product_Value	  */
	public void setProduct_Value (String Product_Value);

	/** Get Product_Value	  */
	public String getProduct_Value();

    /** Column name ProductCategory_Value */
    public static final String COLUMNNAME_ProductCategory_Value = "ProductCategory_Value";

	/** Set Product Category Key	  */
	public void setProductCategory_Value (String ProductCategory_Value);

	/** Get Product Category Key	  */
	public String getProductCategory_Value();

    /** Column name ProductType */
    public static final String COLUMNNAME_ProductType = "ProductType";

	/** Set Product Type.
	  * Type of product
	  */
	public void setProductType (String ProductType);

	/** Get Product Type.
	  * Type of product
	  */
	public String getProductType();

    /** Column name PurchasePrice */
    public static final String COLUMNNAME_PurchasePrice = "PurchasePrice";

	/** Set PurchasePrice	  */
	public void setPurchasePrice (BigDecimal PurchasePrice);

	/** Get PurchasePrice	  */
	public BigDecimal getPurchasePrice();

    /** Column name QtyCount */
    public static final String COLUMNNAME_QtyCount = "QtyCount";

	/** Set Quantity count.
	  * Counted Quantity
	  */
	public void setQtyCount (BigDecimal QtyCount);

	/** Get Quantity count.
	  * Counted Quantity
	  */
	public BigDecimal getQtyCount();

    /** Column name SalesPrice */
    public static final String COLUMNNAME_SalesPrice = "SalesPrice";

	/** Set SalesPrice	  */
	public void setSalesPrice (BigDecimal SalesPrice);

	/** Get SalesPrice	  */
	public BigDecimal getSalesPrice();

    /** Column name TaxType */
    public static final String COLUMNNAME_TaxType = "TaxType";

	/** Set TaxType	  */
	public void setTaxType (String TaxType);

	/** Get TaxType	  */
	public String getTaxType();

    /** Column name UOM_Value */
    public static final String COLUMNNAME_UOM_Value = "UOM_Value";

	/** Set UOM_Value	  */
	public void setUOM_Value (String UOM_Value);

	/** Get UOM_Value	  */
	public String getUOM_Value();

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

    /** Column name WholesalerPrice */
    public static final String COLUMNNAME_WholesalerPrice = "WholesalerPrice";

	/** Set WholesalerPrice	  */
	public void setWholesalerPrice (BigDecimal WholesalerPrice);

	/** Get WholesalerPrice	  */
	public BigDecimal getWholesalerPrice();
}
