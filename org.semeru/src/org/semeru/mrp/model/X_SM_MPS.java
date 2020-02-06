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

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.util.Properties;
import org.compiere.model.*;
import org.compiere.util.Env;

/** Generated Model for SM_MPS
 *  @author iDempiere (generated) 
 *  @version Release 6.2 - $Id$ */
public class X_SM_MPS extends PO implements I_SM_MPS, I_Persistent 
{

	/**
	 *
	 */
	private static final long serialVersionUID = 20190824L;

    /** Standard Constructor */
    public X_SM_MPS (Properties ctx, int SM_MPS_ID, String trxName)
    {
      super (ctx, SM_MPS_ID, trxName);
      /** if (SM_MPS_ID == 0)
        {
			setC_DocTypeTarget_ID (0);
// 1000050
			setDocStatus (null);
// DR
			setDocumentNo (null);
			setdtf (0);
			setleadtime (0);
			setlotsizemethod (null);
// 'LFL'
			setM_Product_ID (0);
			setohnqty (Env.ZERO);
			setProcessed (false);
			setptf (0);
			setSafetyStock (Env.ZERO);
			setSM_MPS_ID (0);
			setSM_Product_PlanLine_ID (0);
        } */
    }

    /** Load Constructor */
    public X_SM_MPS (Properties ctx, ResultSet rs, String trxName)
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
      StringBuffer sb = new StringBuffer ("X_SM_MPS[")
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

	/** Set calculatemps.
		@param calculatemps calculatemps	  */
	public void setcalculatemps (String calculatemps)
	{
		set_Value (COLUMNNAME_calculatemps, calculatemps);
	}

	/** Get calculatemps.
		@return calculatemps	  */
	public String getcalculatemps () 
	{
		return (String)get_Value(COLUMNNAME_calculatemps);
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

	/** Set dtf.
		@param dtf dtf	  */
	public void setdtf (int dtf)
	{
		set_Value (COLUMNNAME_dtf, Integer.valueOf(dtf));
	}

	/** Get dtf.
		@return dtf	  */
	public int getdtf () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_dtf);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	/** Set leadtime.
		@param leadtime leadtime	  */
	public void setleadtime (int leadtime)
	{
		set_Value (COLUMNNAME_leadtime, Integer.valueOf(leadtime));
	}

	/** Get leadtime.
		@return leadtime	  */
	public int getleadtime () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_leadtime);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	/** Set linecreated.
		@param linecreated linecreated	  */
	public void setlinecreated (boolean linecreated)
	{
		set_Value (COLUMNNAME_linecreated, Boolean.valueOf(linecreated));
	}

	/** Get linecreated.
		@return linecreated	  */
	public boolean islinecreated () 
	{
		Object oo = get_Value(COLUMNNAME_linecreated);
		if (oo != null) 
		{
			 if (oo instanceof Boolean) 
				 return ((Boolean)oo).booleanValue(); 
			return "Y".equals(oo);
		}
		return false;
	}

	/** Lot For Lot = LFL */
	public static final String LOTSIZEMETHOD_LotForLot = "LFL";
	/** Set lotsizemethod.
		@param lotsizemethod lotsizemethod	  */
	public void setlotsizemethod (String lotsizemethod)
	{

		set_Value (COLUMNNAME_lotsizemethod, lotsizemethod);
	}

	/** Get lotsizemethod.
		@return lotsizemethod	  */
	public String getlotsizemethod () 
	{
		return (String)get_Value(COLUMNNAME_lotsizemethod);
	}

	public org.compiere.model.I_M_Product getM_Product() throws RuntimeException
    {
		return (org.compiere.model.I_M_Product)MTable.get(getCtx(), org.compiere.model.I_M_Product.Table_Name)
			.getPO(getM_Product_ID(), get_TrxName());	}

	/** Set Product.
		@param M_Product_ID 
		Product, Service, Item
	  */
	public void setM_Product_ID (int M_Product_ID)
	{
		if (M_Product_ID < 1) 
			set_Value (COLUMNNAME_M_Product_ID, null);
		else 
			set_Value (COLUMNNAME_M_Product_ID, Integer.valueOf(M_Product_ID));
	}

	/** Get Product.
		@return Product, Service, Item
	  */
	public int getM_Product_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_M_Product_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
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

	/** Set ohnqty.
		@param ohnqty ohnqty	  */
	public void setohnqty (BigDecimal ohnqty)
	{
		set_Value (COLUMNNAME_ohnqty, ohnqty);
	}

	/** Get ohnqty.
		@return ohnqty	  */
	public BigDecimal getohnqty () 
	{
		BigDecimal bd = (BigDecimal)get_Value(COLUMNNAME_ohnqty);
		if (bd == null)
			 return Env.ZERO;
		return bd;
	}

	/** Set printmps.
		@param printmps printmps	  */
	public void setprintmps (String printmps)
	{
		set_Value (COLUMNNAME_printmps, printmps);
	}

	/** Get printmps.
		@return printmps	  */
	public String getprintmps () 
	{
		return (String)get_Value(COLUMNNAME_printmps);
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

	/** Set ptf.
		@param ptf ptf	  */
	public void setptf (int ptf)
	{
		set_Value (COLUMNNAME_ptf, Integer.valueOf(ptf));
	}

	/** Get ptf.
		@return ptf	  */
	public int getptf () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_ptf);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	/** Set Safety Stock Qty.
		@param SafetyStock 
		Safety stock is a term used to describe a level of stock that is maintained below the cycle stock to buffer against stock-outs
	  */
	public void setSafetyStock (BigDecimal SafetyStock)
	{
		set_Value (COLUMNNAME_SafetyStock, SafetyStock);
	}

	/** Get Safety Stock Qty.
		@return Safety stock is a term used to describe a level of stock that is maintained below the cycle stock to buffer against stock-outs
	  */
	public BigDecimal getSafetyStock () 
	{
		BigDecimal bd = (BigDecimal)get_Value(COLUMNNAME_SafetyStock);
		if (bd == null)
			 return Env.ZERO;
		return bd;
	}

	/** Set SM_MPS.
		@param SM_MPS_ID SM_MPS	  */
	public void setSM_MPS_ID (int SM_MPS_ID)
	{
		if (SM_MPS_ID < 1) 
			set_ValueNoCheck (COLUMNNAME_SM_MPS_ID, null);
		else 
			set_ValueNoCheck (COLUMNNAME_SM_MPS_ID, Integer.valueOf(SM_MPS_ID));
	}

	/** Get SM_MPS.
		@return SM_MPS	  */
	public int getSM_MPS_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_SM_MPS_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	/** Set SM_MPS_UU.
		@param SM_MPS_UU SM_MPS_UU	  */
	public void setSM_MPS_UU (String SM_MPS_UU)
	{
		set_ValueNoCheck (COLUMNNAME_SM_MPS_UU, SM_MPS_UU);
	}

	/** Get SM_MPS_UU.
		@return SM_MPS_UU	  */
	public String getSM_MPS_UU () 
	{
		return (String)get_Value(COLUMNNAME_SM_MPS_UU);
	}

	public I_SM_Product_PlanLine getSM_Product_PlanLine() throws RuntimeException
    {
		return (I_SM_Product_PlanLine)MTable.get(getCtx(), I_SM_Product_PlanLine.Table_Name)
			.getPO(getSM_Product_PlanLine_ID(), get_TrxName());	}

	/** Set SM_Product_PlanLine.
		@param SM_Product_PlanLine_ID SM_Product_PlanLine	  */
	public void setSM_Product_PlanLine_ID (int SM_Product_PlanLine_ID)
	{
		if (SM_Product_PlanLine_ID < 1) 
			set_ValueNoCheck (COLUMNNAME_SM_Product_PlanLine_ID, null);
		else 
			set_ValueNoCheck (COLUMNNAME_SM_Product_PlanLine_ID, Integer.valueOf(SM_Product_PlanLine_ID));
	}

	/** Get SM_Product_PlanLine.
		@return SM_Product_PlanLine	  */
	public int getSM_Product_PlanLine_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_SM_Product_PlanLine_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	/** Set updatemps.
		@param updatemps updatemps	  */
	public void setupdatemps (String updatemps)
	{
		set_Value (COLUMNNAME_updatemps, updatemps);
	}

	/** Get updatemps.
		@return updatemps	  */
	public String getupdatemps () 
	{
		return (String)get_Value(COLUMNNAME_updatemps);
	}
}