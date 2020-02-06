//package org.semeru.model;
//
//import java.math.BigDecimal;
//import java.sql.ResultSet;
//import java.sql.Timestamp;
//import java.util.ArrayList;
//import java.util.Properties;
//import java.util.logging.Level;
//
//import org.adempiere.exceptions.AdempiereException;
//import org.compiere.model.MBPartner;
//import org.compiere.model.MBankAccount;
//import org.compiere.model.MClient;
//import org.compiere.model.MConversionRate;
//import org.compiere.model.MConversionRateUtil;
//import org.compiere.model.MDocType;
//import org.compiere.model.MDocTypeCounter;
//import org.compiere.model.MInOutLine;
//import org.compiere.model.MInvoice;
//import org.compiere.model.MInvoiceLine;
//import org.compiere.model.MMatchInv;
//import org.compiere.model.MMatchPO;
//import org.compiere.model.MOrder;
//import org.compiere.model.MOrderLine;
//import org.compiere.model.MOrg;
//import org.compiere.model.MPayment;
//import org.compiere.model.MPaymentProcessor;
//import org.compiere.model.MPaymentTransaction;
//import org.compiere.model.MPeriod;
//import org.compiere.model.MProject;
//import org.compiere.model.MRMALine;
//import org.compiere.model.MSysConfig;
//import org.compiere.model.MUser;
//import org.compiere.model.ModelValidationEngine;
//import org.compiere.model.ModelValidator;
//import org.compiere.model.Query;
//import org.compiere.process.DocAction;
//import org.compiere.util.CLogger;
//import org.compiere.util.DB;
//import org.compiere.util.Env;
//import org.compiere.util.Msg;
//import org.compiere.util.TimeUtil;
//
//public class MInvoiceSemeru extends MInvoice{
//	
//	
//	/**
//	 * 
//	 */
//	private static final long serialVersionUID = 1L;
//
//	public MInvoiceSemeru(MOrder order, int C_DocTypeTarget_ID, Timestamp invoiceDate) {
//		super(order, C_DocTypeTarget_ID, invoiceDate);
//	}
//	public MInvoiceSemeru(Properties ctx, ResultSet rs, String trxName) {
//		super(ctx, rs, trxName);
//	}
//	
//	public MInvoiceSemeru(Properties ctx, int C_Invoice_ID, String trxName) {
//		super(ctx, C_Invoice_ID, trxName);
//	}
//
//
//	
//	
//
//	
//	
//	/**
//	 * 	Complete Document
//	 * 	@return new status (Complete, In Progress, Invalid, Waiting ..)
//	 */
//	@Override
//	public String completeIt(){
//		
//		/**	Process Message 			*/
//		String		m_processMsg = null;
//		/**	Just Prepared Flag			*/
//		boolean		m_justPrepared = false;
//
//		
//		//	Re-Check
//		if (!m_justPrepared)
//		{
//			String status = prepareIt();
//			m_justPrepared = false;
//			if (!DocAction.STATUS_InProgress.equals(status))
//				return status;
//		}
//
//		
//		MBankAccount ba = null;
//		// Set the definite document number after completed (if needed)
//		setDefiniteDocumentNo();
//
//		m_processMsg = ModelValidationEngine.get().fireDocValidate(this, ModelValidator.TIMING_BEFORE_COMPLETE);
//		if (m_processMsg != null)
//			return DocAction.STATUS_Invalid;
//
//		//	Implicit Approval
//		if (!isApproved())
//			approveIt();
//		if (log.isLoggable(Level.INFO)) log.info(toString());
//		StringBuilder info = new StringBuilder();
//		
//		// POS supports multiple payments
//		boolean fromPOS = false;
//		if ( getC_Order_ID() > 0 )
//		{
//			fromPOS = getC_Order().getC_POS_ID() > 0;
//		}
//
//  		//	Create Cash Payment
//		if (PAYMENTRULE_Cash.equals(getPaymentRule()) && !fromPOS )
//		{
//			
//			StringBuilder whereClause = new StringBuilder();
//			whereClause.append(" AD_Org_ID=? AND C_Currency_ID=? ");
//			
//			
//			MOrder ord = getOriginalOrder();
//			if(ord.get_ValueAsInt("C_BankAccount_ID") > 0) {
//				ba = new MBankAccount(getCtx(), ord.get_ValueOldAsInt("C_BankAccount_ID"), get_TrxName());
//			}else {			
//				ba = new Query(getCtx(),MBankAccount.Table_Name,whereClause.toString(),get_TrxName())
//					.setParameters(getAD_Org_ID(), getC_Currency_ID())
//					.setOnlyActiveRecords(true)
//					.setOrderBy("IsDefault DESC")
//					.first();	
//			}
//			
//			if (ba == null) {
//				m_processMsg = "@NoAccountOrgCurrency@";
//				return DocAction.STATUS_Invalid;
//			}
//			
//			String docBaseType = "";
//			if (isSOTrx())
//				docBaseType=MDocType.DOCBASETYPE_ARReceipt;
//			else
//				docBaseType=MDocType.DOCBASETYPE_APPayment;
//			
//			MDocType[] doctypes = MDocType.getOfDocBaseType(getCtx(), docBaseType);
//			if (doctypes == null || doctypes.length == 0) {
//				m_processMsg = "No document type ";
//				return DocAction.STATUS_Invalid;
//			}
//			MDocType doctype = null;
//			for (MDocType doc : doctypes) {
//				if (doc.getAD_Org_ID() == this.getAD_Org_ID()) {
//					doctype = doc;
//					break;
//				}
//			}
//			if (doctype == null)
//				doctype = doctypes[0];
//
//			MPayment payment = new MPayment(getCtx(), 0, get_TrxName());
//			payment.setAD_Org_ID(getAD_Org_ID());
//			payment.setTenderType(MPayment.TENDERTYPE_Cash);
//			payment.setC_BankAccount_ID(ba.getC_BankAccount_ID());
//			payment.setC_BPartner_ID(getC_BPartner_ID());
//			payment.setC_Invoice_ID(getC_Invoice_ID());
//			payment.setC_Currency_ID(getC_Currency_ID());			
//			payment.setC_DocType_ID(doctype.getC_DocType_ID());
//			if (isCreditMemo())
//				payment.setPayAmt(getGrandTotal().negate());
//			else
//				payment.setPayAmt(getGrandTotal());
//			payment.setIsPrepayment(false);					
//			payment.setDateAcct(getDateAcct());
//			payment.setDateTrx(getDateInvoiced());
//
//			//	Save payment
//			payment.saveEx();
//
//			payment.setDocAction(MPayment.DOCACTION_Complete);
//			if (!payment.processIt(MPayment.DOCACTION_Complete)) {
//				m_processMsg = "Cannot Complete the Payment : [" + payment.getProcessMsg() + "] " + payment;
//				return DocAction.STATUS_Invalid;
//			}
//
//			payment.saveEx();
//			info.append("@C_Payment_ID@: " + payment.getDocumentInfo());
//
//			// IDEMPIERE-2588 - add the allocation generation with the payment
//			if (payment.getJustCreatedAllocInv() != null)
//				addDocsPostProcess(payment.getJustCreatedAllocInv());
//		}	//	Payment
//
//		//	Update Order & Match
//		int matchInv = 0;
//		int matchPO = 0;
//		MInvoiceLine[] lines = getLines(false);
//		for (int i = 0; i < lines.length; i++)
//		{
//			MInvoiceLine line = lines[i];
//
//			//	Matching - Inv-Shipment
//			if (!isSOTrx()
//				&& line.getM_InOutLine_ID() != 0
//				&& line.getM_Product_ID() != 0
//				&& !isReversal())
//			{
//				MInOutLine receiptLine = new MInOutLine (getCtx(),line.getM_InOutLine_ID(), get_TrxName());
//				BigDecimal matchQty = line.getQtyInvoiced();
//
//				if (receiptLine.getMovementQty().compareTo(matchQty) < 0)
//					matchQty = receiptLine.getMovementQty();
//
//				MMatchInv inv = new MMatchInv(line, getDateInvoiced(), matchQty);
//				if (!inv.save(get_TrxName()))
//				{
//					m_processMsg = CLogger.retrieveErrorString("Could not create Invoice Matching");
//					return DocAction.STATUS_Invalid;
//				}
//				matchInv++;
//				addDocsPostProcess(inv);
//			}
//					
//			//	Update Order Line
//			MOrderLine ol = null;
//			if (line.getC_OrderLine_ID() != 0)
//			{
//				if (isSOTrx()
//					|| line.getM_Product_ID() == 0)
//				{
//					ol = new MOrderLine (getCtx(), line.getC_OrderLine_ID(), get_TrxName());
//					if (line.getQtyInvoiced() != null)
//						ol.setQtyInvoiced(ol.getQtyInvoiced().add(line.getQtyInvoiced()));
//					if (!ol.save(get_TrxName()))
//					{
//						m_processMsg = "Could not update Order Line";
//						return DocAction.STATUS_Invalid;
//					}
//				}
//				//	Order Invoiced Qty updated via Matching Inv-PO
//				else if (!isSOTrx()
//					&& line.getM_Product_ID() != 0
//					&& !isReversal())
//				{
//					//	MatchPO is created also from MInOut when Invoice exists before Shipment
//					BigDecimal matchQty = line.getQtyInvoiced();
//					MMatchPO po = MMatchPO.create (line, null,
//						getDateInvoiced(), matchQty);
//					if (po != null) 
//					{
//						if (!po.save(get_TrxName()))
//						{
//							m_processMsg = "Could not create PO Matching";
//							return DocAction.STATUS_Invalid;
//						}
//						matchPO++;
//						if (!po.isPosted())
//							addDocsPostProcess(po);
//						
//						MMatchInv[] matchInvoices = MMatchInv.getInvoiceLine(getCtx(), line.getC_InvoiceLine_ID(), get_TrxName());
//						if (matchInvoices != null && matchInvoices.length > 0) 
//						{
//							for(MMatchInv matchInvoice : matchInvoices)
//							{
//								if (!matchInvoice.isPosted())
//								{
//									addDocsPostProcess(matchInvoice);
//								}
//							}
//						}
//					}
//				}
//			}
//
//			//Update QtyInvoiced RMA Line
//			if (line.getM_RMALine_ID() != 0)
//			{
//				MRMALine rmaLine = new MRMALine (getCtx(),line.getM_RMALine_ID(), get_TrxName());
//				if (rmaLine.getQtyInvoiced() != null)
//					rmaLine.setQtyInvoiced(rmaLine.getQtyInvoiced().add(line.getQtyInvoiced()));
//				else
//					rmaLine.setQtyInvoiced(line.getQtyInvoiced());
//				if (!rmaLine.save(get_TrxName()))
//				{
//					m_processMsg = "Could not update RMA Line";
//					return DocAction.STATUS_Invalid;
//				}
//			}
//			//			
//		}	//	for all lines
//		if (matchInv > 0)
//			info.append(" @M_MatchInv_ID@#").append(matchInv).append(" ");
//		if (matchPO > 0)
//			info.append(" @M_MatchPO_ID@#").append(matchPO).append(" ");
//
//
//
//		//	Update BP Statistics
//		MBPartner bp = new MBPartner (getCtx(), getC_BPartner_ID(), get_TrxName());
//		DB.getDatabase().forUpdate(bp, 0);
//		//	Update total revenue and balance / credit limit (reversed on AllocationLine.processIt)
//		BigDecimal invAmt = MConversionRate.convertBase(getCtx(), getGrandTotal(true),	//	CM adjusted
//			getC_Currency_ID(), getDateAcct(), getC_ConversionType_ID(), getAD_Client_ID(), getAD_Org_ID());
//		if (invAmt == null)
//		{
//			m_processMsg = MConversionRateUtil.getErrorMessage(getCtx(), "ErrorConvertingCurrencyToBaseCurrency",
//					getC_Currency_ID(), MClient.get(getCtx()).getC_Currency_ID(), getC_ConversionType_ID(), getDateAcct(), get_TrxName());
//			return DocAction.STATUS_Invalid;
//		}
//		//	Total Balance
//		BigDecimal newBalance = bp.getTotalOpenBalance();
//		if (newBalance == null)
//			newBalance = Env.ZERO;
//		if (isSOTrx())
//		{
//			newBalance = newBalance.add(invAmt);
//			//
//			if (bp.getFirstSale() == null)
//				bp.setFirstSale(getDateInvoiced());
//			BigDecimal newLifeAmt = bp.getActualLifeTimeValue();
//			if (newLifeAmt == null)
//				newLifeAmt = invAmt;
//			else
//				newLifeAmt = newLifeAmt.add(invAmt);
//			BigDecimal newCreditAmt = bp.getSO_CreditUsed();
//			if (newCreditAmt == null)
//				newCreditAmt = invAmt;
//			else
//				newCreditAmt = newCreditAmt.add(invAmt);
//			//
//			if (log.isLoggable(Level.FINE)) log.fine("GrandTotal=" + getGrandTotal(true) + "(" + invAmt
//				+ ") BP Life=" + bp.getActualLifeTimeValue() + "->" + newLifeAmt
//				+ ", Credit=" + bp.getSO_CreditUsed() + "->" + newCreditAmt
//				+ ", Balance=" + bp.getTotalOpenBalance() + " -> " + newBalance);
//			bp.setActualLifeTimeValue(newLifeAmt);
//			bp.setSO_CreditUsed(newCreditAmt);
//		}	//	SO
//		else
//		{
//			newBalance = newBalance.subtract(invAmt);
//			if (log.isLoggable(Level.FINE)) log.fine("GrandTotal=" + getGrandTotal(true) + "(" + invAmt
//				+ ") Balance=" + bp.getTotalOpenBalance() + " -> " + newBalance);
//		}
//		bp.setTotalOpenBalance(newBalance);
//		bp.setSOCreditStatus();
//		if (!bp.save(get_TrxName()))
//		{
//			m_processMsg = "Could not update Business Partner";
//			return DocAction.STATUS_Invalid;
//		}
//
//		//	User - Last Result/Contact
//		if (getAD_User_ID() != 0)
//		{
//			MUser user = new MUser (getCtx(), getAD_User_ID(), get_TrxName());
//			user.setLastContact(new Timestamp(System.currentTimeMillis()));
//			StringBuilder msgset = new StringBuilder().append(Msg.translate(getCtx(), "C_Invoice_ID")).append(": ").append(getDocumentNo());
//			user.setLastResult(msgset.toString());
//			if (!user.save(get_TrxName()))
//			{
//				m_processMsg = "Could not update Business Partner User";
//				return DocAction.STATUS_Invalid;
//			}
//		}	//	user
//
//		//	Update Project
//		if (isSOTrx() && getC_Project_ID() != 0)
//		{
//			MProject project = new MProject (getCtx(), getC_Project_ID(), get_TrxName());
//			BigDecimal amt = getGrandTotal(true);
//			int C_CurrencyTo_ID = project.getC_Currency_ID();
//			if (C_CurrencyTo_ID != getC_Currency_ID())
//				amt = MConversionRate.convert(getCtx(), amt, getC_Currency_ID(), C_CurrencyTo_ID,
//					getDateAcct(), 0, getAD_Client_ID(), getAD_Org_ID());
//			if (amt == null)
//			{
//				m_processMsg = MConversionRateUtil.getErrorMessage(getCtx(), "ErrorConvertingCurrencyToProjectCurrency",
//						getC_Currency_ID(), C_CurrencyTo_ID, 0, getDateAcct(), get_TrxName());
//				return DocAction.STATUS_Invalid;
//			}
//			BigDecimal newAmt = project.getInvoicedAmt();
//			if (newAmt == null)
//				newAmt = amt;
//			else
//				newAmt = newAmt.add(amt);
//			if (log.isLoggable(Level.FINE)) log.fine("GrandTotal=" + getGrandTotal(true) + "(" + amt
//				+ ") Project " + project.getName()
//				+ " - Invoiced=" + project.getInvoicedAmt() + "->" + newAmt);
//			project.setInvoicedAmt(newAmt);
//			if (!project.save(get_TrxName()))
//			{
//				m_processMsg = "Could not update Project";
//				return DocAction.STATUS_Invalid;
//			}
//		}	//	project
//		
//		// auto delay capture authorization payment
//		if (isSOTrx() && !isReversal())
//		{
//			StringBuilder whereClause = new StringBuilder();
//			whereClause.append("C_Order_ID IN (");
//			whereClause.append("SELECT C_Order_ID ");
//			whereClause.append("FROM C_OrderLine ");
//			whereClause.append("WHERE C_OrderLine_ID IN (");
//			whereClause.append("SELECT C_OrderLine_ID ");
//			whereClause.append("FROM C_InvoiceLine ");
//			whereClause.append("WHERE C_Invoice_ID = ");
//			whereClause.append(getC_Invoice_ID()).append("))");
//			int[] orderIDList = MOrder.getAllIDs(MOrder.Table_Name, whereClause.toString(), get_TrxName());
//			
//			int[] ids = MPaymentTransaction.getAuthorizationPaymentTransactionIDs(orderIDList, getC_Invoice_ID(), get_TrxName());			
//			if (ids.length > 0)
//			{
//				boolean pureCIM = true;
//				ArrayList<MPaymentTransaction> ptList = new ArrayList<MPaymentTransaction>();
//				BigDecimal totalPayAmt = BigDecimal.ZERO;
//				for (int id : ids)
//				{
//					MPaymentTransaction pt = new MPaymentTransaction(getCtx(), id, get_TrxName());
//					
//					if (!pt.setPaymentProcessor())
//					{
//						if (pt.getC_PaymentProcessor_ID() > 0)
//						{
//							MPaymentProcessor pp = new MPaymentProcessor(getCtx(), pt.getC_PaymentProcessor_ID(), get_TrxName());
//							m_processMsg = Msg.getMsg(getCtx(), "PaymentNoProcessorModel") + ": " + pp.toString();
//						}
//						else
//							m_processMsg = Msg.getMsg(getCtx(), "PaymentNoProcessorModel");
//						return DocAction.STATUS_Invalid;
//					}
//					
//					boolean isCIM = pt.getC_PaymentProcessor_ID() > 0 && pt.getCustomerPaymentProfileID() != null && pt.getCustomerPaymentProfileID().length() > 0;
//					if (pureCIM && !isCIM)
//						pureCIM = false;
//					
//					totalPayAmt = totalPayAmt.add(pt.getPayAmt());
//					ptList.add(pt);
//				}
//				
//				// automatically void authorization payment and create a new sales payment when invoiced amount is NOT equals to the authorized amount (applied to CIM payment processor)
//				if (getGrandTotal().compareTo(totalPayAmt) != 0 && ptList.size() > 0 && pureCIM)
//				{
//					// create a new sales payment
//					MPaymentTransaction newSalesPT = MPaymentTransaction.copyFrom(ptList.get(0), new Timestamp(System.currentTimeMillis()), MPayment.TRXTYPE_Sales, "", get_TrxName());
//					newSalesPT.setIsApproved(false);
//					newSalesPT.setIsVoided(false);
//					newSalesPT.setIsDelayedCapture(false);
//					newSalesPT.setDescription("InvoicedAmt: " + getGrandTotal() + " <> TotalAuthorizedAmt: " + totalPayAmt);
//					newSalesPT.setC_Invoice_ID(getC_Invoice_ID());
//					newSalesPT.setPayAmt(getGrandTotal());
//					
//					// void authorization payment
//					for (MPaymentTransaction pt : ptList)
//					{
//						pt.setDescription("InvoicedAmt: " + getGrandTotal() + " <> AuthorizedAmt: " + pt.getPayAmt());
//						boolean ok = pt.voidOnlineAuthorizationPaymentTransaction();
//						pt.saveEx();
//						if (!ok)
//						{
//							m_processMsg = Msg.getMsg(getCtx(), "VoidAuthorizationPaymentFailed") + ": " + pt.getErrorMessage();
//							return DocAction.STATUS_Invalid;
//						}					
//					}
//					
//					// process the new sales payment
//					boolean ok = newSalesPT.processOnline();
//					newSalesPT.saveEx();
//					if (!ok)
//					{
//						m_processMsg = Msg.getMsg(getCtx(), "CreateNewSalesPaymentFailed") + ": " + newSalesPT.getErrorMessage();
//						return DocAction.STATUS_Invalid;
//					}
//				}
//				else if (getGrandTotal().compareTo(totalPayAmt) != 0 && ptList.size() > 0)
//				{
//					m_processMsg = "InvoicedAmt: " + getGrandTotal() + " <> AuthorizedAmt: " + totalPayAmt;
//					return DocAction.STATUS_Invalid;
//				}
//				else
//				{
//					// delay capture authorization payment
//					for (MPaymentTransaction pt : ptList)
//					{
//						boolean ok = pt.delayCaptureOnlineAuthorizationPaymentTransaction(getC_Invoice_ID());
//						pt.saveEx();
//						if (!ok)
//						{
//							m_processMsg = Msg.getMsg(getCtx(), "DelayCaptureAuthFailed") + ": " + pt.getErrorMessage();
//							return DocAction.STATUS_Invalid;
//						}					
//					}
//				}
//				if (testAllocation(true)) {
//					saveEx();
//				}
//			}
//		}
//
//		if (PAYMENTRULE_Cash.equals(getPaymentRule())) {
//			if (testAllocation(true)) {
//				saveEx();
//			}
//		}
//		//	User Validation
//		String valid = ModelValidationEngine.get().fireDocValidate(this, ModelValidator.TIMING_AFTER_COMPLETE);
//		if (valid != null)
//		{
//			m_processMsg = valid;
//			return DocAction.STATUS_Invalid;
//		}
//
//		//	Counter Documents
//		MInvoice counter = createCounterDoc();
//		if (counter != null)
//			info.append(" - @CounterDoc@: @C_Invoice_ID@=").append(counter.getDocumentNo());
//
//		m_processMsg = info.toString().trim();
//		setProcessed(true);
//		setDocAction(DOCACTION_Close);
//		return DocAction.STATUS_Completed;
//	}	//	completeIt
//
//	
//	/**
//	 * 	Set the definite document number after completed
//	 */
//	private void setDefiniteDocumentNo() {
//		if (isReversal() && ! MSysConfig.getBooleanValue(MSysConfig.Invoice_ReverseUseNewNumber, true, getAD_Client_ID())) // IDEMPIERE-1771
//			return;
//		MDocType dt = MDocType.get(getCtx(), getC_DocType_ID());
//		if (dt.isOverwriteDateOnComplete()) {
//			setDateInvoiced(TimeUtil.getDay(0));
//			if (getDateAcct().before(getDateInvoiced())) {
//				setDateAcct(getDateInvoiced());
//				MPeriod.testPeriodOpen(getCtx(), getDateAcct(), getC_DocType_ID(), getAD_Org_ID());
//			}
//		}
//		if (dt.isOverwriteSeqOnComplete()) {
//			String value = DB.getDocumentNo(getC_DocType_ID(), get_TrxName(), true, this);
//			if (value != null)
//				setDocumentNo(value);
//		}
//	}
//	
//	/**
//	 * 	Create Counter Document
//	 * 	@return counter invoice
//	 */
//	private MInvoice createCounterDoc()
//	{
//		//	Is this a counter doc ?
//		if (getRef_Invoice_ID() != 0)
//			return null;
//
//		//	Org Must be linked to BPartner
//		MOrg org = MOrg.get(getCtx(), getAD_Org_ID());
//		int counterC_BPartner_ID = org.getLinkedC_BPartner_ID(get_TrxName());
//		if (counterC_BPartner_ID == 0)
//			return null;
//		//	Business Partner needs to be linked to Org
//		MBPartner bp = new MBPartner (getCtx(), getC_BPartner_ID(), null);
//		int counterAD_Org_ID = bp.getAD_OrgBP_ID_Int();
//		if (counterAD_Org_ID == 0)
//			return null;
//
//		MBPartner counterBP = new MBPartner (getCtx(), counterC_BPartner_ID, null);
////		MOrgInfo counterOrgInfo = MOrgInfo.get(getCtx(), counterAD_Org_ID);
//		if (log.isLoggable(Level.INFO)) log.info("Counter BP=" + counterBP.getName());
//
//		//	Document Type
//		int C_DocTypeTarget_ID = 0;
//		MDocTypeCounter counterDT = MDocTypeCounter.getCounterDocType(getCtx(), getC_DocType_ID());
//		if (counterDT != null)
//		{
//			if (log.isLoggable(Level.FINE)) log.fine(counterDT.toString());
//			if (!counterDT.isCreateCounter() || !counterDT.isValid())
//				return null;
//			C_DocTypeTarget_ID = counterDT.getCounter_C_DocType_ID();
//		}
//		else	//	indirect
//		{
//			C_DocTypeTarget_ID = MDocTypeCounter.getCounterDocType_ID(getCtx(), getC_DocType_ID());
//			if (log.isLoggable(Level.FINE)) log.fine("Indirect C_DocTypeTarget_ID=" + C_DocTypeTarget_ID);
//			if (C_DocTypeTarget_ID <= 0)
//				return null;
//		}
//
//		//	Deep Copy
//		MInvoice counter = copyFrom(this, getDateInvoiced(), getDateAcct(),
//			C_DocTypeTarget_ID, !isSOTrx(), true, get_TrxName(), true);
//		//
//		counter.setAD_Org_ID(counterAD_Org_ID);
//	//	counter.setM_Warehouse_ID(counterOrgInfo.getM_Warehouse_ID());
//		//
////		counter.setBPartner(counterBP);// was set on copyFrom
//		//	References (Should not be required)
//		counter.setSalesRep_ID(getSalesRep_ID());
//		counter.saveEx(get_TrxName());
//
//		//	Update copied lines
//		MInvoiceLine[] counterLines = counter.getLines(true);
//		for (int i = 0; i < counterLines.length; i++)
//		{
//			MInvoiceLine counterLine = counterLines[i];
//			counterLine.setAD_Org_ID(counter.getAD_Org_ID());
//			counterLine.setInvoice(counter);	//	copies header values (BP, etc.)
//			counterLine.setPrice();
//			counterLine.setTax();
//			//
//			counterLine.saveEx(get_TrxName());
//		}
//
//		if (log.isLoggable(Level.FINE)) log.fine(counter.toString());
//
//		//	Document Action
//		if (counterDT != null)
//		{
//			if (counterDT.getDocAction() != null)
//			{
//				counter.setDocAction(counterDT.getDocAction());
//				// added AdempiereException by zuhri
//				if (!counter.processIt(counterDT.getDocAction()))
//					throw new AdempiereException("Failed when processing document - " + counter.getProcessMsg());
//				// end added
//				counter.saveEx(get_TrxName());
//			}
//		}
//		return counter;
//	}	//	createCounterDoc
//}
