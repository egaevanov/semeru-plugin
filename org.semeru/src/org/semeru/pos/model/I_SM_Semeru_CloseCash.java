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
package org.semeru.pos.model;

import java.math.BigDecimal;
import java.sql.Timestamp;
import org.compiere.model.*;
import org.compiere.util.KeyNamePair;

/** Generated Interface for SM_Semeru_CloseCash
 *  @author iDempiere (generated) 
 *  @version Release 6.2
 */
@SuppressWarnings("all")
public interface I_SM_Semeru_CloseCash 
{

    /** TableName=SM_Semeru_CloseCash */
    public static final String Table_Name = "SM_Semeru_CloseCash";

    /** AD_Table_ID=1000104 */
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

    /** Column name BankPayment */
    public static final String COLUMNNAME_BankPayment = "BankPayment";

	/** Set Bank Payment	  */
	public void setBankPayment (BigDecimal BankPayment);

	/** Get Bank Payment	  */
	public BigDecimal getBankPayment();

    /** Column name Cash */
    public static final String COLUMNNAME_Cash = "Cash";

	/** Set Cash	  */
	public void setCash (BigDecimal Cash);

	/** Get Cash	  */
	public BigDecimal getCash();

    /** Column name CashIn */
    public static final String COLUMNNAME_CashIn = "CashIn";

	/** Set CashIn	  */
	public void setCashIn (BigDecimal CashIn);

	/** Get CashIn	  */
	public BigDecimal getCashIn();

    /** Column name CashOut */
    public static final String COLUMNNAME_CashOut = "CashOut";

	/** Set CashOut	  */
	public void setCashOut (BigDecimal CashOut);

	/** Get CashOut	  */
	public BigDecimal getCashOut();

    /** Column name CloseCashDate */
    public static final String COLUMNNAME_CloseCashDate = "CloseCashDate";

	/** Set CloseCashDate	  */
	public void setCloseCashDate (Timestamp CloseCashDate);

	/** Get CloseCashDate	  */
	public Timestamp getCloseCashDate();

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

    /** Column name CreatedByPos_ID */
    public static final String COLUMNNAME_CreatedByPos_ID = "CreatedByPos_ID";

	/** Set CreatedByPos_ID	  */
	public void setCreatedByPos_ID (int CreatedByPos_ID);

	/** Get CreatedByPos_ID	  */
	public int getCreatedByPos_ID();

    /** Column name CurrentBalance */
    public static final String COLUMNNAME_CurrentBalance = "CurrentBalance";

	/** Set Current balance.
	  * Current Balance
	  */
	public void setCurrentBalance (BigDecimal CurrentBalance);

	/** Get Current balance.
	  * Current Balance
	  */
	public BigDecimal getCurrentBalance();

    /** Column name DateTrx */
    public static final String COLUMNNAME_DateTrx = "DateTrx";

	/** Set Transaction Date.
	  * Transaction Date
	  */
	public void setDateTrx (Timestamp DateTrx);

	/** Get Transaction Date.
	  * Transaction Date
	  */
	public Timestamp getDateTrx();

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

    /** Column name LainLain */
    public static final String COLUMNNAME_LainLain = "LainLain";

	/** Set Lain Lain	  */
	public void setLainLain (BigDecimal LainLain);

	/** Get Lain Lain	  */
	public BigDecimal getLainLain();

    /** Column name LeasingPayment */
    public static final String COLUMNNAME_LeasingPayment = "LeasingPayment";

	/** Set Leasing Payment	  */
	public void setLeasingPayment (BigDecimal LeasingPayment);

	/** Get Leasing Payment	  */
	public BigDecimal getLeasingPayment();

    /** Column name OtherLeasingPayment */
    public static final String COLUMNNAME_OtherLeasingPayment = "OtherLeasingPayment";

	/** Set Other Leasing Payment	  */
	public void setOtherLeasingPayment (BigDecimal OtherLeasingPayment);

	/** Get Other Leasing Payment	  */
	public BigDecimal getOtherLeasingPayment();

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

    /** Column name SeqTutupKas */
    public static final String COLUMNNAME_SeqTutupKas = "SeqTutupKas";

	/** Set SeqTutupKas	  */
	public void setSeqTutupKas (int SeqTutupKas);

	/** Get SeqTutupKas	  */
	public int getSeqTutupKas();

    /** Column name SM_Semeru_CloseCash_ID */
    public static final String COLUMNNAME_SM_Semeru_CloseCash_ID = "SM_Semeru_CloseCash_ID";

	/** Set SM_Semeru_CloseCash	  */
	public void setSM_Semeru_CloseCash_ID (int SM_Semeru_CloseCash_ID);

	/** Get SM_Semeru_CloseCash	  */
	public int getSM_Semeru_CloseCash_ID();

    /** Column name SM_Semeru_CloseCash_UU */
    public static final String COLUMNNAME_SM_Semeru_CloseCash_UU = "SM_Semeru_CloseCash_UU";

	/** Set SM_Semeru_CloseCash_UU	  */
	public void setSM_Semeru_CloseCash_UU (String SM_Semeru_CloseCash_UU);

	/** Get SM_Semeru_CloseCash_UU	  */
	public String getSM_Semeru_CloseCash_UU();

    /** Column name TaxAmt */
    public static final String COLUMNNAME_TaxAmt = "TaxAmt";

	/** Set Tax Amount.
	  * Tax Amount for a document
	  */
	public void setTaxAmt (BigDecimal TaxAmt);

	/** Get Tax Amount.
	  * Tax Amount for a document
	  */
	public BigDecimal getTaxAmt();

    /** Column name TotalOmset */
    public static final String COLUMNNAME_TotalOmset = "TotalOmset";

	/** Set Total Omset	  */
	public void setTotalOmset (BigDecimal TotalOmset);

	/** Get Total Omset	  */
	public BigDecimal getTotalOmset();

    /** Column name TrxCount */
    public static final String COLUMNNAME_TrxCount = "TrxCount";

	/** Set Trx Count	  */
	public void setTrxCount (int TrxCount);

	/** Get Trx Count	  */
	public int getTrxCount();

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
