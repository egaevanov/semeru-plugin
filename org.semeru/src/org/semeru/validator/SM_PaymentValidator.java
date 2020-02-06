package org.semeru.validator;

import org.adempiere.base.event.IEventTopics;
import org.compiere.model.MInvoice;
import org.compiere.model.MOrder;
import org.compiere.model.MPayment;
import org.compiere.model.PO;
import org.compiere.util.CLogger;
import org.compiere.util.Env;
import org.osgi.service.event.Event;



/**
 * 
 * @author Tegar N
 *
 */

public class SM_PaymentValidator {
	
public static CLogger log = CLogger.getCLogger(SM_PaymentValidator.class);
	
	public static String executePaymentValidatorEvent(Event event, PO po) {
		
		String msgPayment = "";
		MPayment Pay = (MPayment) po;
		if (event.getTopic().equals(IEventTopics.DOC_BEFORE_COMPLETE)) {
			
			msgPayment = PayBeforeComplete(Pay);
		}
		
	return msgPayment;

	}
	
	private static String PayBeforeComplete(MPayment payment) {
		String rs = "";
		int C_Order_ID = payment.getC_Order_ID();
		MOrder Ord = null;
		
		System.out.println("DocAction : "+payment.getDocAction());
		System.out.println("DocStatus : "+payment.getDocStatus());
		System.out.println(payment.getReversal_ID());
		
		if(payment.getReversal_ID() > 0) {
			return rs;
		}
		
		if(C_Order_ID <= 0 ) {
			
			int C_Invoice_ID = payment.getC_Invoice_ID();
			
			System.out.println(C_Invoice_ID);

			if(C_Invoice_ID >0) {
				MInvoice inv = new MInvoice(payment.getCtx(), C_Invoice_ID, payment.get_TrxName());
				
				Ord = inv.getOriginalOrder();
			}
				
		}else {
			Ord = new MOrder(payment.getCtx(), C_Order_ID, payment.get_TrxName());
		}
		
		
		if(Ord == null)
			return rs;
		
		System.out.println(Ord);
		System.out.println("BAnkAcct : "+Ord.get_ValueAsInt("C_BankAccount_ID"));

		if(payment.getPayAmt().compareTo(Env.ZERO)>0) {
		
			payment.setC_BankAccount_ID(Ord.get_ValueAsInt("C_BankAccount_ID"));
			payment.saveEx();
			
		}
	
		return rs;
	}

}
