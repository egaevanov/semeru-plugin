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

/** Generated Model for SM_MRPLine
 *  @author iDempiere (generated) 
 *  @version Release 6.2 - $Id$ */
public class X_SM_MRPLine extends PO implements I_SM_MRPLine, I_Persistent 
{

	/**
	 *
	 */
	private static final long serialVersionUID = 20190823L;

    /** Standard Constructor */
    public X_SM_MRPLine (Properties ctx, int SM_MRPLine_ID, String trxName)
    {
      super (ctx, SM_MRPLine_ID, trxName);
      /** if (SM_MRPLine_ID == 0)
        {
			setgre (Env.ZERO);
			setmrpdate (new Timestamp( System.currentTimeMillis() ));
			setnet (Env.ZERO);
			setpab (Env.ZERO);
			setprc (Env.ZERO);
			setprl (Env.ZERO);
			setProcessed (false);
			setseq (0);
			setSM_MRP_ID (0);
			setSM_MRPLine_ID (0);
			setsrc (Env.ZERO);
			setweek (0);
        } */
    }

    /** Load Constructor */
    public X_SM_MRPLine (Properties ctx, ResultSet rs, String trxName)
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
      StringBuffer sb = new StringBuffer ("X_SM_MRPLine[")
        .append(get_ID()).append("]");
      return sb.toString();
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

	/** Set gre.
		@param gre gre	  */
	public void setgre (BigDecimal gre)
	{
		set_Value (COLUMNNAME_gre, gre);
	}

	/** Get gre.
		@return gre	  */
	public BigDecimal getgre () 
	{
		BigDecimal bd = (BigDecimal)get_Value(COLUMNNAME_gre);
		if (bd == null)
			 return Env.ZERO;
		return bd;
	}

	/** Set mrpdate.
		@param mrpdate mrpdate	  */
	public void setmrpdate (Timestamp mrpdate)
	{
		set_Value (COLUMNNAME_mrpdate, mrpdate);
	}

	/** Get mrpdate.
		@return mrpdate	  */
	public Timestamp getmrpdate () 
	{
		return (Timestamp)get_Value(COLUMNNAME_mrpdate);
	}

	/** Set net.
		@param net net	  */
	public void setnet (BigDecimal net)
	{
		set_Value (COLUMNNAME_net, net);
	}

	/** Get net.
		@return net	  */
	public BigDecimal getnet () 
	{
		BigDecimal bd = (BigDecimal)get_Value(COLUMNNAME_net);
		if (bd == null)
			 return Env.ZERO;
		return bd;
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

	/** Set prc.
		@param prc prc	  */
	public void setprc (BigDecimal prc)
	{
		set_Value (COLUMNNAME_prc, prc);
	}

	/** Get prc.
		@return prc	  */
	public BigDecimal getprc () 
	{
		BigDecimal bd = (BigDecimal)get_Value(COLUMNNAME_prc);
		if (bd == null)
			 return Env.ZERO;
		return bd;
	}

	/** Set prl.
		@param prl prl	  */
	public void setprl (BigDecimal prl)
	{
		set_Value (COLUMNNAME_prl, prl);
	}

	/** Get prl.
		@return prl	  */
	public BigDecimal getprl () 
	{
		BigDecimal bd = (BigDecimal)get_Value(COLUMNNAME_prl);
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

	public I_SM_MRP getSM_MRP() throws RuntimeException
    {
		return (I_SM_MRP)MTable.get(getCtx(), I_SM_MRP.Table_Name)
			.getPO(getSM_MRP_ID(), get_TrxName());	}

	/** Set SM_MRP.
		@param SM_MRP_ID SM_MRP	  */
	public void setSM_MRP_ID (int SM_MRP_ID)
	{
		if (SM_MRP_ID < 1) 
			set_ValueNoCheck (COLUMNNAME_SM_MRP_ID, null);
		else 
			set_ValueNoCheck (COLUMNNAME_SM_MRP_ID, Integer.valueOf(SM_MRP_ID));
	}

	/** Get SM_MRP.
		@return SM_MRP	  */
	public int getSM_MRP_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_SM_MRP_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	/** Set Semeru Material Requirement Planning Line.
		@param SM_MRPLine_ID Semeru Material Requirement Planning Line	  */
	public void setSM_MRPLine_ID (int SM_MRPLine_ID)
	{
		if (SM_MRPLine_ID < 1) 
			set_ValueNoCheck (COLUMNNAME_SM_MRPLine_ID, null);
		else 
			set_ValueNoCheck (COLUMNNAME_SM_MRPLine_ID, Integer.valueOf(SM_MRPLine_ID));
	}

	/** Get Semeru Material Requirement Planning Line.
		@return Semeru Material Requirement Planning Line	  */
	public int getSM_MRPLine_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_SM_MRPLine_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	/** Set SM_MRPLine_UU.
		@param SM_MRPLine_UU SM_MRPLine_UU	  */
	public void setSM_MRPLine_UU (String SM_MRPLine_UU)
	{
		set_ValueNoCheck (COLUMNNAME_SM_MRPLine_UU, SM_MRPLine_UU);
	}

	/** Get SM_MRPLine_UU.
		@return SM_MRPLine_UU	  */
	public String getSM_MRPLine_UU () 
	{
		return (String)get_Value(COLUMNNAME_SM_MRPLine_UU);
	}

	/** Set src.
		@param src src	  */
	public void setsrc (BigDecimal src)
	{
		set_Value (COLUMNNAME_src, src);
	}

	/** Get src.
		@return src	  */
	public BigDecimal getsrc () 
	{
		BigDecimal bd = (BigDecimal)get_Value(COLUMNNAME_src);
		if (bd == null)
			 return Env.ZERO;
		return bd;
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