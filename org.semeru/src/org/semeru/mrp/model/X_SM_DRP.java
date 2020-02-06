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
/** Generated Model - DO NOT CHANGE */
package org.semeru.mrp.model;

import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.Properties;
import org.compiere.model.*;

/** Generated Model for SM_DRP
 *  @author iDempiere (generated) 
 *  @version Release 6.2 - $Id$ */
public class X_SM_DRP extends PO implements I_SM_DRP, I_Persistent 
{

	/**
	 *
	 */
	private static final long serialVersionUID = 20190820L;

    /** Standard Constructor */
    public X_SM_DRP (Properties ctx, int SM_DRP_ID, String trxName)
    {
      super (ctx, SM_DRP_ID, trxName);
      /** if (SM_DRP_ID == 0)
        {
			setC_DocTypeTarget_ID (0);
// 1000047
			setC_Period_ID (0);
			setDocStatus (null);
// DR
			setDocumentNo (null);
			setMovementDate (new Timestamp( System.currentTimeMillis() ));
			setProcessed (false);
			setSM_DRP_ID (0);
        } */
    }

    /** Load Constructor */
    public X_SM_DRP (Properties ctx, ResultSet rs, String trxName)
    {
      super (ctx, rs, trxName);
    }

    /** AccessLevel
      * @return 3 - Client - Org 
      */
    protected int get_AccessLevel()
    {
      return accessLevel.intValue();
    }

    /** Load Meta Data */
    protected POInfo initPO (Properties ctx)
    {
      POInfo poi = POInfo.getPOInfo (ctx, Table_ID, get_TrxName());
      return poi;
    }

    public String toString()
    {
      StringBuffer sb = new StringBuffer ("X_SM_DRP[")
        .append(get_ID()).append("]");
      return sb.toString();
    }

	public org.compiere.model.I_C_DocType getC_DocTypeTarget() throws RuntimeException
    {
		return (org.compiere.model.I_C_DocType)MTable.get(getCtx(), org.compiere.model.I_C_DocType.Table_Name)
			.getPO(getC_DocTypeTarget_ID(), get_TrxName());	}

	/** Set Target Document Type.
		@param C_DocTypeTarget_ID 
		Target document type for conversing documents
	  */
	public void setC_DocTypeTarget_ID (int C_DocTypeTarget_ID)
	{
		if (C_DocTypeTarget_ID < 1) 
			set_ValueNoCheck (COLUMNNAME_C_DocTypeTarget_ID, null);
		else 
			set_ValueNoCheck (COLUMNNAME_C_DocTypeTarget_ID, Integer.valueOf(C_DocTypeTarget_ID));
	}

	/** Get Target Document Type.
		@return Target document type for conversing documents
	  */
	public int getC_DocTypeTarget_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_C_DocTypeTarget_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	public org.compiere.model.I_C_Period getC_Period() throws RuntimeException
    {
		return (org.compiere.model.I_C_Period)MTable.get(getCtx(), org.compiere.model.I_C_Period.Table_Name)
			.getPO(getC_Period_ID(), get_TrxName());	}

	/** Set Period.
		@param C_Period_ID 
		Period of the Calendar
	  */
	public void setC_Period_ID (int C_Period_ID)
	{
		if (C_Period_ID < 1) 
			set_ValueNoCheck (COLUMNNAME_C_Period_ID, null);
		else 
			set_ValueNoCheck (COLUMNNAME_C_Period_ID, Integer.valueOf(C_Period_ID));
	}

	/** Get Period.
		@return Period of the Calendar
	  */
	public int getC_Period_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_C_Period_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	/** Set CalculateDRP.
		@param CalculateDRP CalculateDRP	  */
	public void setCalculateDRP (String CalculateDRP)
	{
		set_Value (COLUMNNAME_CalculateDRP, CalculateDRP);
	}

	/** Get CalculateDRP.
		@return CalculateDRP	  */
	public String getCalculateDRP () 
	{
		return (String)get_Value(COLUMNNAME_CalculateDRP);
	}

	/** Set Description.
		@param Description 
		Optional short description of the record
	  */
	public void setDescription (String Description)
	{
		set_Value (COLUMNNAME_Description, Description);
	}

	/** Get Description.
		@return Optional short description of the record
	  */
	public String getDescription () 
	{
		return (String)get_Value(COLUMNNAME_Description);
	}

	/** DocStatus AD_Reference_ID=131 */
	public static final int DOCSTATUS_AD_Reference_ID=131;
	/** Drafted = DR */
	public static final String DOCSTATUS_Drafted = "DR";
	/** Completed = CO */
	public static final String DOCSTATUS_Completed = "CO";
	/** Approved = AP */
	public static final String DOCSTATUS_Approved = "AP";
	/** Not Approved = NA */
	public static final String DOCSTATUS_NotApproved = "NA";
	/** Voided = VO */
	public static final String DOCSTATUS_Voided = "VO";
	/** Invalid = IN */
	public static final String DOCSTATUS_Invalid = "IN";
	/** Reversed = RE */
	public static final String DOCSTATUS_Reversed = "RE";
	/** Closed = CL */
	public static final String DOCSTATUS_Closed = "CL";
	/** Unknown = ?? */
	public static final String DOCSTATUS_Unknown = "??";
	/** In Progress = IP */
	public static final String DOCSTATUS_InProgress = "IP";
	/** Waiting Payment = WP */
	public static final String DOCSTATUS_WaitingPayment = "WP";
	/** Waiting Confirmation = WC */
	public static final String DOCSTATUS_WaitingConfirmation = "WC";
	/** Set Document Status.
		@param DocStatus 
		The current status of the document
	  */
	public void setDocStatus (String DocStatus)
	{

		set_Value (COLUMNNAME_DocStatus, DocStatus);
	}

	/** Get Document Status.
		@return The current status of the document
	  */
	public String getDocStatus () 
	{
		return (String)get_Value(COLUMNNAME_DocStatus);
	}

	/** Set Document No.
		@param DocumentNo 
		Document sequence number of the document
	  */
	public void setDocumentNo (String DocumentNo)
	{
		set_ValueNoCheck (COLUMNNAME_DocumentNo, DocumentNo);
	}

	/** Get Document No.
		@return Document sequence number of the document
	  */
	public String getDocumentNo () 
	{
		return (String)get_Value(COLUMNNAME_DocumentNo);
	}

	/** Set Movement Date.
		@param MovementDate 
		Date a product was moved in or out of inventory
	  */
	public void setMovementDate (Timestamp MovementDate)
	{
		set_ValueNoCheck (COLUMNNAME_MovementDate, MovementDate);
	}

	/** Get Movement Date.
		@return Date a product was moved in or out of inventory
	  */
	public Timestamp getMovementDate () 
	{
		return (Timestamp)get_Value(COLUMNNAME_MovementDate);
	}

	/** Set Name.
		@param Name 
		Alphanumeric identifier of the entity
	  */
	public void setName (String Name)
	{
		set_Value (COLUMNNAME_Name, Name);
	}

	/** Get Name.
		@return Alphanumeric identifier of the entity
	  */
	public String getName () 
	{
		return (String)get_Value(COLUMNNAME_Name);
	}

	/** Set PrintDRP.
		@param PrintDRP PrintDRP	  */
	public void setPrintDRP (String PrintDRP)
	{
		set_Value (COLUMNNAME_PrintDRP, PrintDRP);
	}

	/** Get PrintDRP.
		@return PrintDRP	  */
	public String getPrintDRP () 
	{
		return (String)get_Value(COLUMNNAME_PrintDRP);
	}

	/** Set ProcessDRP.
		@param ProcessDRP ProcessDRP	  */
	public void setProcessDRP (String ProcessDRP)
	{
		set_Value (COLUMNNAME_ProcessDRP, ProcessDRP);
	}

	/** Get ProcessDRP.
		@return ProcessDRP	  */
	public String getProcessDRP () 
	{
		return (String)get_Value(COLUMNNAME_ProcessDRP);
	}

	/** Set Processed.
		@param Processed 
		The document has been processed
	  */
	public void setProcessed (boolean Processed)
	{
		set_Value (COLUMNNAME_Processed, Boolean.valueOf(Processed));
	}

	/** Get Processed.
		@return The document has been processed
	  */
	public boolean isProcessed () 
	{
		Object oo = get_Value(COLUMNNAME_Processed);
		if (oo != null) 
		{
			 if (oo instanceof Boolean) 
				 return ((Boolean)oo).booleanValue(); 
			return "Y".equals(oo);
		}
		return false;
	}

	/** Set Process Now.
		@param Processing Process Now	  */
	public void setProcessing (boolean Processing)
	{
		set_Value (COLUMNNAME_Processing, Boolean.valueOf(Processing));
	}

	/** Get Process Now.
		@return Process Now	  */
	public boolean isProcessing () 
	{
		Object oo = get_Value(COLUMNNAME_Processing);
		if (oo != null) 
		{
			 if (oo instanceof Boolean) 
				 return ((Boolean)oo).booleanValue(); 
			return "Y".equals(oo);
		}
		return false;
	}

	/** Set SM_DRP.
		@param SM_DRP_ID SM_DRP	  */
	public void setSM_DRP_ID (int SM_DRP_ID)
	{
		if (SM_DRP_ID < 1) 
			set_ValueNoCheck (COLUMNNAME_SM_DRP_ID, null);
		else 
			set_ValueNoCheck (COLUMNNAME_SM_DRP_ID, Integer.valueOf(SM_DRP_ID));
	}

	/** Get SM_DRP.
		@return SM_DRP	  */
	public int getSM_DRP_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_SM_DRP_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	/** Set SM_DRP_UU.
		@param SM_DRP_UU SM_DRP_UU	  */
	public void setSM_DRP_UU (String SM_DRP_UU)
	{
		set_ValueNoCheck (COLUMNNAME_SM_DRP_UU, SM_DRP_UU);
	}

	/** Get SM_DRP_UU.
		@return SM_DRP_UU	  */
	public String getSM_DRP_UU () 
	{
		return (String)get_Value(COLUMNNAME_SM_DRP_UU);
	}
}