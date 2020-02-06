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
import java.sql.Timestamp;
import java.util.Properties;
import org.compiere.model.*;
import org.compiere.util.Env;

/** Generated Model for SM_MPSLine
 *  @author iDempiere (generated) 
 *  @version Release 6.2 - $Id$ */
public class X_SM_MPSLine extends PO implements I_SM_MPSLine, I_Persistent 
{

	/**
	 *
	 */
	private static final long serialVersionUID = 20190824L;

    /** Standard Constructor */
    public X_SM_MPSLine (Properties ctx, int SM_MPSLine_ID, String trxName)
    {
      super (ctx, SM_MPSLine_ID, trxName);
      /** if (SM_MPSLine_ID == 0)
        {
			setatp (Env.ZERO);
			setC_Period_ID (0);
			setcor (Env.ZERO);
			setfrs (Env.ZERO);
			setisdtf (false);
			setmps (Env.ZERO);
			setmpsdate (new Timestamp( System.currentTimeMillis() ));
			setpab (Env.ZERO);
			setseq (0);
			setSM_MPS_ID (0);
			setSM_MPSLine_ID (0);
			setweek (0);
        } */
    }

    /** Load Constructor */
    public X_SM_MPSLine (Properties ctx, ResultSet rs, String trxName)
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
      StringBuffer sb = new StringBuffer ("X_SM_MPSLine[")
        .append(get_ID()).append("]");
      return sb.toString();
    }

	/** Set atp.
		@param atp atp	  */
	public void setatp (BigDecimal atp)
	{
		set_Value (COLUMNNAME_atp, atp);
	}

	/** Get atp.
		@return atp	  */
	public BigDecimal getatp () 
	{
		BigDecimal bd = (BigDecimal)get_Value(COLUMNNAME_atp);
		if (bd == null)
			 return Env.ZERO;
		return bd;
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
			set_Value (COLUMNNAME_C_Period_ID, null);
		else 
			set_Value (COLUMNNAME_C_Period_ID, Integer.valueOf(C_Period_ID));
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

	/** Set cor.
		@param cor cor	  */
	public void setcor (BigDecimal cor)
	{
		set_Value (COLUMNNAME_cor, cor);
	}

	/** Get cor.
		@return cor	  */
	public BigDecimal getcor () 
	{
		BigDecimal bd = (BigDecimal)get_Value(COLUMNNAME_cor);
		if (bd == null)
			 return Env.ZERO;
		return bd;
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

	/** Set frs.
		@param frs frs	  */
	public void setfrs (BigDecimal frs)
	{
		set_Value (COLUMNNAME_frs, frs);
	}

	/** Get frs.
		@return frs	  */
	public BigDecimal getfrs () 
	{
		BigDecimal bd = (BigDecimal)get_Value(COLUMNNAME_frs);
		if (bd == null)
			 return Env.ZERO;
		return bd;
	}

	/** Set isdtf.
		@param isdtf isdtf	  */
	public void setisdtf (boolean isdtf)
	{
		set_Value (COLUMNNAME_isdtf, Boolean.valueOf(isdtf));
	}

	/** Get isdtf.
		@return isdtf	  */
	public boolean isdtf () 
	{
		Object oo = get_Value(COLUMNNAME_isdtf);
		if (oo != null) 
		{
			 if (oo instanceof Boolean) 
				 return ((Boolean)oo).booleanValue(); 
			return "Y".equals(oo);
		}
		return false;
	}

	/** Set mps.
		@param mps mps	  */
	public void setmps (BigDecimal mps)
	{
		set_Value (COLUMNNAME_mps, mps);
	}

	/** Get mps.
		@return mps	  */
	public BigDecimal getmps () 
	{
		BigDecimal bd = (BigDecimal)get_Value(COLUMNNAME_mps);
		if (bd == null)
			 return Env.ZERO;
		return bd;
	}

	/** Set mpsdate.
		@param mpsdate mpsdate	  */
	public void setmpsdate (Timestamp mpsdate)
	{
		set_Value (COLUMNNAME_mpsdate, mpsdate);
	}

	/** Get mpsdate.
		@return mpsdate	  */
	public Timestamp getmpsdate () 
	{
		return (Timestamp)get_Value(COLUMNNAME_mpsdate);
	}

	/** Set pab.
		@param pab pab	  */
	public void setpab (BigDecimal pab)
	{
		set_Value (COLUMNNAME_pab, pab);
	}

	/** Get pab.
		@return pab	  */
	public BigDecimal getpab () 
	{
		BigDecimal bd = (BigDecimal)get_Value(COLUMNNAME_pab);
		if (bd == null)
			 return Env.ZERO;
		return bd;
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

	/** Set seq.
		@param seq seq	  */
	public void setseq (int seq)
	{
		set_Value (COLUMNNAME_seq, Integer.valueOf(seq));
	}

	/** Get seq.
		@return seq	  */
	public int getseq () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_seq);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	public I_SM_MPS getSM_MPS() throws RuntimeException
    {
		return (I_SM_MPS)MTable.get(getCtx(), I_SM_MPS.Table_Name)
			.getPO(getSM_MPS_ID(), get_TrxName());	}

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

	/** Set SM_MPSLine.
		@param SM_MPSLine_ID SM_MPSLine	  */
	public void setSM_MPSLine_ID (int SM_MPSLine_ID)
	{
		if (SM_MPSLine_ID < 1) 
			set_ValueNoCheck (COLUMNNAME_SM_MPSLine_ID, null);
		else 
			set_ValueNoCheck (COLUMNNAME_SM_MPSLine_ID, Integer.valueOf(SM_MPSLine_ID));
	}

	/** Get SM_MPSLine.
		@return SM_MPSLine	  */
	public int getSM_MPSLine_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_SM_MPSLine_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	/** Set SM_MPSLine_UU.
		@param SM_MPSLine_UU SM_MPSLine_UU	  */
	public void setSM_MPSLine_UU (String SM_MPSLine_UU)
	{
		set_ValueNoCheck (COLUMNNAME_SM_MPSLine_UU, SM_MPSLine_UU);
	}

	/** Get SM_MPSLine_UU.
		@return SM_MPSLine_UU	  */
	public String getSM_MPSLine_UU () 
	{
		return (String)get_Value(COLUMNNAME_SM_MPSLine_UU);
	}

	/** Set week.
		@param week week	  */
	public void setweek (int week)
	{
		set_Value (COLUMNNAME_week, Integer.valueOf(week));
	}

	/** Get week.
		@return week	  */
	public int getweek () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_week);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}
}