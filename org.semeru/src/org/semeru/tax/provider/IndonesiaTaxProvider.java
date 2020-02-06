/**
 * 
 */
package org.semeru.tax.provider;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;

import org.adempiere.model.ITaxProvider;
import org.compiere.model.I_C_AllocationLine;
import org.compiere.model.I_C_InvoiceLine;
import org.compiere.model.I_C_OrderLine;
import org.compiere.model.I_M_RMALine;
import org.compiere.model.MAllocationHdr;
import org.compiere.model.MAllocationLine;
import org.compiere.model.MBPartner;
import org.compiere.model.MInvoice;
import org.compiere.model.MInvoiceLine;
import org.compiere.model.MInvoiceTax;
import org.compiere.model.MOrder;
import org.compiere.model.MOrderLine;
import org.compiere.model.MOrderTax;
import org.compiere.model.MRMA;
import org.compiere.model.MRMALine;
import org.compiere.model.MRMATax;
import org.compiere.model.MTax;
import org.compiere.model.MTaxProvider;
import org.compiere.model.Query;
import org.compiere.process.ProcessInfo;
import org.compiere.util.CLogger;
import org.compiere.util.DB;
import org.compiere.util.Env;
import org.compiere.util.Msg;

/**
 * @author tegar
 *
 */
public class IndonesiaTaxProvider implements ITaxProvider{

	private static CLogger log = CLogger.getCLogger(IndonesiaTaxProvider.class);
//	private int ROUNDING_MODE = BigDecimal.ROUND_DOWN ;
	
	@Override
	public boolean calculateOrderTaxTotal(MTaxProvider provider, MOrder order) {
		//		Lines
		BigDecimal totalLines = Env.ZERO;
		ArrayList<Integer> taxList = new ArrayList<Integer>();
		MOrderLine[] lines = order.getLines();
		for (int i = 0; i < lines.length; i++)
		{
			MOrderLine line = lines[i];
			totalLines = totalLines.add(line.getLineNetAmt());
			Integer taxID = new Integer(line.getC_Tax_ID());
			if (!taxList.contains(taxID))
			{
//				MTax tax = new MTax(order.getCtx(), taxID, order.get_TrxName());
//				if (tax.getC_TaxProvider_ID() != 0)
//					continue;
				MOrderTax oTax = MOrderTax.get (line, order.getPrecision(), false, order.get_TrxName());	//	current Tax
				oTax.setIsTaxIncluded(order.isTaxIncluded());
				if (!oTax.calculateTaxFromLines())
					return false;
				if (!oTax.save(order.get_TrxName()))
					return false;
				taxList.add(taxID);
			}
		}
			
		//	Taxes
		BigDecimal grandTotal = totalLines;
		MOrderTax[] taxes = order.getTaxes(true);
		for (int i = 0; i < taxes.length; i++)
		{
			MOrderTax oTax = taxes[i];
//			if (oTax.getC_TaxProvider_ID() != 0) {
//			if (!order.isTaxIncluded())
//				grandTotal = grandTotal.add(oTax.getTaxAmt());
//				continue;
//			}
			MTax tax = oTax.getTax();
			if (tax.isSummary())
			{
				MTax[] cTaxes = tax.getChildTaxes(false);
				for (int j = 0; j < cTaxes.length; j++)
				{
					MTax cTax = cTaxes[j];
					BigDecimal taxAmt = cTax.calculateTax(oTax.getTaxBaseAmt(), order.isTaxIncluded(), order.getPrecision());
					//
					MOrderTax newOTax = new MOrderTax(order.getCtx(), 0, order.get_TrxName());
					newOTax.setAD_Org_ID(order.getAD_Org_ID());
					newOTax.setC_Order_ID(order.getC_Order_ID());
					newOTax.setC_Tax_ID(cTax.getC_Tax_ID());
					newOTax.setPrecision(order.getPrecision());
					newOTax.setIsTaxIncluded(order.isTaxIncluded());
					newOTax.setTaxBaseAmt(oTax.getTaxBaseAmt());
					newOTax.setTaxAmt(taxAmt);
					if (!newOTax.save(order.get_TrxName()))
						return false;
					//
					if (!order.isTaxIncluded())
						grandTotal = grandTotal.add(taxAmt);
				}
				if (!oTax.delete(true, order.get_TrxName()))
					return false;
				if (!oTax.save(order.get_TrxName()))
					return false;
			}
			else
			{
				if (!order.isTaxIncluded())
					grandTotal = grandTotal.add(oTax.getTaxAmt());
			}
		}		
		//
		order.setTotalLines(totalLines);
		order.setGrandTotal(grandTotal);
		return true;
	}

	@Override
	public boolean updateOrderTax(MTaxProvider provider, MOrderLine line) {
//		MTax mtax = new MTax(line.getCtx(), line.getC_Tax_ID(), line.get_TrxName());
//    	if (mtax.getC_TaxProvider_ID() == 0)
    		return line.updateOrderTax(false);
//    	return true;
	}

	@Override
	public boolean calculateInvoiceTaxTotal(MTaxProvider provider,MInvoice invoice) {
		
		//	Lines
		BigDecimal totalLines = Env.ZERO;
		ArrayList<Integer> taxList = new ArrayList<Integer>();
		MInvoiceLine[] lines = invoice.getLines(false);
		for (int i = 0; i < lines.length; i++)
		{
			MInvoiceLine line = lines[i];
			totalLines = totalLines.add(line.getLineNetAmt());
			if (!taxList.contains(line.getC_Tax_ID()))
			{
//				MTax tax = new MTax(invoice.getCtx(), line.getC_Tax_ID(), invoice.get_TrxName());
//				if (tax.getC_TaxProvider_ID() != 0)
//					continue;
				MInvoiceTax iTax = MInvoiceTax.get (line, invoice.getPrecision(), false, invoice.get_TrxName()); //	current Tax
				if (iTax != null)
				{
					iTax.setIsTaxIncluded(invoice.isTaxIncluded());
					if (!iTax.calculateTaxFromLines())
						return false;
					iTax.saveEx();
					taxList.add(line.getC_Tax_ID());
				}
			}
		}

		//	Taxes
		BigDecimal grandTotal = totalLines;
		MInvoiceTax[] taxes = invoice.getTaxes(true);
		for (int i = 0; i < taxes.length; i++)
		{
			MInvoiceTax iTax = taxes[i];
//			if (iTax.getC_TaxProvider_ID() != 0) {
//				if (!invoice.isTaxIncluded())
//				    grandTotal = grandTotal.add(iTax.getTaxAmt());
//		    	continue;
//		    }
			MTax tax = iTax.getTax();
			if (tax.isSummary())
			{
				MTax[] cTaxes = tax.getChildTaxes(false);	//	Multiple taxes
				for (int j = 0; j < cTaxes.length; j++)
				{
					MTax cTax = cTaxes[j];
					BigDecimal taxAmt = cTax.calculateTax(iTax.getTaxBaseAmt(), invoice.isTaxIncluded(), invoice.getPrecision());
					//
					MInvoiceTax newITax = new MInvoiceTax(invoice.getCtx(), 0, invoice.get_TrxName());
					newITax.setAD_Org_ID(invoice.getAD_Org_ID());
					newITax.setAD_Org_ID(invoice.getAD_Org_ID());
					newITax.setC_Invoice_ID(invoice.getC_Invoice_ID());
					newITax.setC_Tax_ID(cTax.getC_Tax_ID());
					newITax.setPrecision(invoice.getPrecision());
					newITax.setIsTaxIncluded(invoice.isTaxIncluded());
					newITax.setTaxBaseAmt(iTax.getTaxBaseAmt());
					newITax.setTaxAmt(taxAmt);
					newITax.saveEx(invoice.get_TrxName());
					//
					if (!invoice.isTaxIncluded())
						grandTotal = grandTotal.add(taxAmt);
				}
				iTax.deleteEx(true, invoice.get_TrxName());
			}
			else
			{
				if (!invoice.isTaxIncluded())
					grandTotal = grandTotal.add(iTax.getTaxAmt());
			}
		}
			//
		invoice.setTotalLines(totalLines);
		invoice.setGrandTotal(grandTotal);
		
		//added by SS, Surya Sentosa, Kosta-Consulting
		int C_BPartner_Location_ID = 0;
		MBPartner bp = new MBPartner(invoice.getCtx(), invoice.getC_BPartner_ID(), invoice.get_TrxName());
//		MBPartnerLocation[] locs = bp.getLocations(true);
//		for (MBPartnerLocation loc : locs){
//			if (loc.get_ValueAsBoolean("IsTax")){
//				C_BPartner_Location_ID = loc.get_ID();
//				break;
//			}
//		}
		
		Map<MTax, BigDecimal> mapAdvanceAmt = new HashMap<MTax, BigDecimal>();
		Map<MTax, BigDecimal> mapBaseAdvanceAmt = new HashMap<MTax, BigDecimal>();
		
		//calculate invoice tax prepayment
		if (!invoice.get_ValueAsBoolean("IsPrepayment")){
			//get allocation 
			List<MAllocationLine> allocLines = new Query(invoice.getCtx(), I_C_AllocationLine.Table_Name,
											 I_C_AllocationLine.COLUMNNAME_C_Invoice_ID + "=? ", invoice.get_TrxName())
											.setParameters(new Object[]{invoice.get_ID()})
											.list();
				
			for (MAllocationLine allocLine:allocLines){
				MAllocationHdr allocHdr = new MAllocationHdr(allocLine.getCtx(), allocLine.getC_AllocationHdr_ID(), allocLine.get_TrxName());
				MAllocationLine[] loopLines = allocHdr.getLines(true);
				for (MAllocationLine loopLine:loopLines){
					if (loopLine.getC_Invoice_ID() != 0){
						if (loopLine.getC_Invoice_ID() == invoice.get_ID())
							continue;
						MInvoice loopInv = new MInvoice(allocLine.getCtx(), loopLine.getC_Invoice_ID(), allocLine.get_TrxName());
						if (loopInv.get_ValueAsBoolean("IsPrepayment")){
							MInvoiceTax[] invTaxes = loopInv.getTaxes(true);
							for (MInvoiceTax invTax:invTaxes){
								BigDecimal taxAdvanceAmt = Env.ZERO;
								BigDecimal taxBaseAdvanceAmt = Env.ZERO;
								MTax tx = new MTax(invTax.getCtx(), invTax.getC_Tax_ID(), invTax.get_TrxName());
								
								if (mapAdvanceAmt.get(tx)!=null){
									taxAdvanceAmt = mapAdvanceAmt.get(tx).add(invTax.getTaxAmt());
									mapAdvanceAmt.put(tx, taxAdvanceAmt);
								}else{
									taxAdvanceAmt = invTax.getTaxAmt();
									mapAdvanceAmt.put(tx, taxAdvanceAmt);
								}
								
								if (mapBaseAdvanceAmt.get(tx)!=null){
									taxBaseAdvanceAmt = mapBaseAdvanceAmt.get(tx).add(invTax.getTaxBaseAmt());
									mapBaseAdvanceAmt.put(tx, taxBaseAdvanceAmt);
								}else{
									taxBaseAdvanceAmt = invTax.getTaxBaseAmt();
									mapBaseAdvanceAmt.put(tx, taxBaseAdvanceAmt);
								}
							}
						}
					}
				}
			}
		}
		
		MInvoiceTax[] taxess = invoice.getTaxes(true);
		for (MInvoiceTax tax : taxess){
			MTax taxRate = new MTax(tax.getCtx(), tax.getC_Tax_ID(), tax.get_TrxName());
			if (C_BPartner_Location_ID !=0)
				tax.set_ValueOfColumn("C_BPartner_Location_ID", C_BPartner_Location_ID);
			tax.set_ValueOfColumn("TaxID", bp.getTaxID());	
			if (mapAdvanceAmt.get(taxRate)!= null){
				tax.set_ValueOfColumn("TaxAdvanceAmt", mapAdvanceAmt.get(taxRate));
			}
			if (mapBaseAdvanceAmt.get(taxRate)!= null){
				tax.set_ValueOfColumn("TaxBaseAdvanceAmt", mapBaseAdvanceAmt.get(taxRate));
			}
			tax.setC_TaxProvider_ID(taxRate.getC_TaxProvider_ID());
			tax.saveEx();
		}
		
		log.log(Level.FINE, "Indonesia Tax Provider !!");
		//end added by SS, Surya Sentosa, Kosta Consulting
		return true;
	}

	@Override
	public boolean updateInvoiceTax(MTaxProvider provider, MInvoiceLine line) {
//		MTax mtax = new MTax(line.getCtx(), line.getC_Tax_ID(), line.get_TrxName());
//    	if (mtax.getC_TaxProvider_ID() == 0)
    		return line.updateInvoiceTax(false);
//    	return true;
	}

	@Override
	public boolean recalculateTax(MTaxProvider provider, MInvoiceLine line,
			boolean newRecord) {
		if (!newRecord && line.is_ValueChanged(I_C_InvoiceLine.COLUMNNAME_C_Tax_ID))
		{
//			MTax mtax = new MTax(line.getCtx(), line.getC_Tax_ID(), line.get_TrxName());
//	    	if (mtax.getC_TaxProvider_ID() == 0)
//	    	{
				//	Recalculate Tax for old Tax
				if (!line.updateInvoiceTax(true))
					return false;
//	    	}
		}
		return line.updateHeaderTax();
	}

	@Override
	public boolean calculateRMATaxTotal(MTaxProvider provider, MRMA rma) {
		//	Lines
		BigDecimal totalLines = Env.ZERO;
		ArrayList<Integer> taxList = new ArrayList<Integer>();
		MRMALine[] lines = rma.getLines(false);
		for (int i = 0; i < lines.length; i++)
		{
			MRMALine line = lines[i];
			totalLines = totalLines.add(line.getLineNetAmt());
			Integer taxID = new Integer(line.getC_Tax_ID());
			if (!taxList.contains(taxID))
			{
//				MTax tax = new MTax(rma.getCtx(), taxID, rma.get_TrxName());
//				if (tax.getC_TaxProvider_ID() != 0)
//					continue;
				MRMATax oTax = MRMATax.get (line, rma.getPrecision(), false, rma.get_TrxName());	//	current Tax
				oTax.setIsTaxIncluded(rma.isTaxIncluded());
				if (!oTax.calculateTaxFromLines())
					return false;
				if (!oTax.save(rma.get_TrxName()))
					return false;
				taxList.add(taxID);
			}
		}
			
		//	Taxes
		BigDecimal grandTotal = totalLines;
		MRMATax[] taxes = rma.getTaxes(true);
		for (int i = 0; i < taxes.length; i++)
		{
			MRMATax oTax = taxes[i];
//			if (oTax.getC_TaxProvider_ID() != 0) {
//				if (!rma.isTaxIncluded())
//					grandTotal = grandTotal.add(oTax.getTaxAmt());
//				continue;
//			}
			MTax tax = oTax.getTax();
			if (tax.isSummary())
			{
				MTax[] cTaxes = tax.getChildTaxes(false);
				for (int j = 0; j < cTaxes.length; j++)
				{
					MTax cTax = cTaxes[j];
					BigDecimal taxAmt = cTax.calculateTax(oTax.getTaxBaseAmt(), rma.isTaxIncluded(), rma.getPrecision());
					//
					MRMATax newOTax = new MRMATax(rma.getCtx(), 0, rma.get_TrxName());
					newOTax.setAD_Org_ID(rma.getAD_Org_ID());
					newOTax.setM_RMA_ID(rma.getM_RMA_ID());
					newOTax.setC_Tax_ID(cTax.getC_Tax_ID());
					newOTax.setPrecision(rma.getPrecision());
					newOTax.setIsTaxIncluded(rma.isTaxIncluded());
					newOTax.setTaxBaseAmt(oTax.getTaxBaseAmt());
					newOTax.setTaxAmt(taxAmt);
					if (!newOTax.save(rma.get_TrxName()))
						return false;
					//
					if (!rma.isTaxIncluded())
						grandTotal = grandTotal.add(taxAmt);
				}
				if (!oTax.delete(true, rma.get_TrxName()))
					return false;
				if (!oTax.save(rma.get_TrxName()))
					return false;
			}
			else
			{
				if (!rma.isTaxIncluded())
					grandTotal = grandTotal.add(oTax.getTaxAmt());
			}
		}		
		//
		rma.setAmt(grandTotal);
		return true;
	}

	@Override
	public boolean updateRMATax(MTaxProvider provider, MRMALine line) {
//		MTax mtax = new MTax(line.getCtx(), line.getC_Tax_ID(), line.get_TrxName());
//    	if (mtax.getC_TaxProvider_ID() == 0)
    		return line.updateOrderTax(false);
//    	return true;
	}

	@Override
	public String validateConnection(MTaxProvider provider, ProcessInfo pi)
			throws Exception {
		throw new IllegalStateException(Msg.getMsg(provider.getCtx(), "ActionNotSupported"));
	}

	@Override
	public boolean recalculateTax(MTaxProvider provider, MOrderLine line,
			boolean newRecord) {
		if (!newRecord && line.is_ValueChanged(I_C_OrderLine.COLUMNNAME_C_Tax_ID) && !line.getParent().isProcessed())
		{
			MTax mtax = new MTax(line.getCtx(), line.getC_Tax_ID(), line.get_TrxName());
	    	if (mtax.getC_TaxProvider_ID() == 0)
	    	{
				//	Recalculate Tax for old Tax
				if (!line.updateOrderTax(true))
					return false;
	    	}
		}
		return line.updateHeaderTax();
	}

	@Override
	public boolean updateHeaderTax(MTaxProvider provider, MOrderLine line) {
//		Update Order Header
		String sql = "UPDATE C_Order i"
			+ " SET TotalLines="
				+ "(SELECT COALESCE(SUM(LineNetAmt),0) FROM C_OrderLine il WHERE i.C_Order_ID=il.C_Order_ID) "
			+ "WHERE C_Order_ID=" + line.getC_Order_ID();
		int no = DB.executeUpdate(sql, line.get_TrxName());
		if (no != 1)
			log.warning("(1) #" + no);

		if (line.isTaxIncluded())
			sql = "UPDATE C_Order i "
				+ " SET GrandTotal=TotalLines "
				+ "WHERE C_Order_ID=" + line.getC_Order_ID();
		else
			sql = "UPDATE C_Order i "
				+ " SET GrandTotal=TotalLines+"
					+ "(SELECT COALESCE(SUM(TaxAmt),0) FROM C_OrderTax it WHERE i.C_Order_ID=it.C_Order_ID) "
					+ "WHERE C_Order_ID=" + line.getC_Order_ID();
		no = DB.executeUpdate(sql, line.get_TrxName());
		if (no != 1)
			log.warning("(2) #" + no);

		line.clearParent();
		return no == 1;
	}

	@Override
	public boolean updateHeaderTax(MTaxProvider provider, MInvoiceLine line) {
//		Update Invoice Header
		String sql = "UPDATE C_Invoice i"
			+ " SET TotalLines="
				+ "(SELECT COALESCE(SUM(LineNetAmt),0) FROM C_InvoiceLine il WHERE i.C_Invoice_ID=il.C_Invoice_ID) "
			+ "WHERE C_Invoice_ID=?";
		int no = DB.executeUpdateEx(sql, new Object[]{line.getC_Invoice_ID()}, line.get_TrxName());
		if (no != 1)
			log.warning("(1) #" + no);

		if (line.isTaxIncluded())
			sql = "UPDATE C_Invoice i "
				+ " SET GrandTotal=TotalLines "
				+ "WHERE C_Invoice_ID=?";
		else
			sql = "UPDATE C_Invoice i "
				+ " SET GrandTotal=TotalLines+"
					+ "(SELECT COALESCE(SUM(TaxAmt),0) FROM C_InvoiceTax it WHERE i.C_Invoice_ID=it.C_Invoice_ID) "
					+ "WHERE C_Invoice_ID=?";
		no = DB.executeUpdateEx(sql, new Object[]{line.getC_Invoice_ID()}, line.get_TrxName());
		if (no != 1)
			log.warning("(2) #" + no);
		line.clearParent();

		return no == 1;
	}

	@Override
	public boolean recalculateTax(MTaxProvider provider, MRMALine line,
			boolean newRecord) {
		if (!newRecord && line.is_ValueChanged(I_M_RMALine.COLUMNNAME_C_Tax_ID) && !line.getParent().isProcessed())
		{
			MTax mtax = new MTax(line.getCtx(), line.getC_Tax_ID(), line.get_TrxName());
	    	if (mtax.getC_TaxProvider_ID() == 0)
	    	{
				//	Recalculate Tax for old Tax
				if (!line.updateOrderTax(true))
					return false;
	    	}
		}

        return line.updateHeaderAmt();
	}

	@Override
	public boolean updateHeaderTax(MTaxProvider provider, MRMALine line) {
		//		Update RMA Header
		String sql = "UPDATE M_RMA "
			+ " SET Amt="
			+ "(SELECT COALESCE(SUM(LineNetAmt),0) FROM M_RMALine WHERE M_RMA.M_RMA_ID=M_RMALine.M_RMA_ID) "
			+ "WHERE M_RMA_ID=?";
		int no = DB.executeUpdateEx(sql, new Object[]{line.getM_RMA_ID()}, line.get_TrxName());
		if (no != 1)
			log.warning("(1) #" + no);

		line.clearParent();

		return no == 1;
	}
	
//	@Override
//	public BigDecimal calculateTax(MTax tax, BigDecimal amount,boolean taxIncluded, int scale) {
//		return tax.calculateTax(amount, taxIncluded, scale);
//	}

}
