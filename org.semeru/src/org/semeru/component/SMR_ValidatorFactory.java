package org.semeru.component;

import org.adempiere.base.event.AbstractEventHandler;
import org.adempiere.base.event.IEventTopics;
import org.adempiere.exceptions.AdempiereException;
import org.compiere.model.I_C_Order;
import org.compiere.model.MAllocationLine;
import org.compiere.model.MInvoice;
import org.compiere.model.MPayment;
import org.compiere.model.PO;
import org.compiere.util.CLogger;
import org.osgi.service.event.Event;
import org.semeru.mrp.model.X_SM_CustOrd_Upload;
import org.semeru.mrp.model.X_SM_DRP;
import org.semeru.mrp.model.X_SM_MRP;
import org.semeru.mrp.validator.SM_CustOrdValidator;
import org.semeru.mrp.validator.SM_DRPValidator;
import org.semeru.validator.SM_AllocationLineValidator;
import org.semeru.validator.SM_PaymentValidator;


/**
 * 
 * @author Tegar N
 *
 */

public class SMR_ValidatorFactory extends AbstractEventHandler{

	private CLogger log = CLogger.getCLogger(SMR_ValidatorFactory.class);

	
	@Override
	protected void doHandleEvent(Event event) {
		
		log.info("SEMERU EVENT MANAGER // INITIALIZED");
		String msg = "";
		
		if (event.getTopic().equals(IEventTopics.AFTER_LOGIN)) {
			/*
			LoginEventData eventData = getEventData(event);
			log.info(" topic="+event.getTopic()+" AD_Client_ID="+eventData.getAD_Client_ID()
					+" AD_Org_ID="+eventData.getAD_Org_ID()+" AD_Role_ID="+eventData.getAD_Role_ID()
					+" AD_User_ID="0+eventData.getAD_User_ID());
			 */
		} 

		else  {
			
			if (getPO(event).get_TableName().equals(I_C_Order.Table_Name)) {
				msg = SM_DRPValidator.executeDRPEvent(event, getPO(event));
			}else if (getPO(event).get_TableName().equals(X_SM_CustOrd_Upload.Table_Name)) {
				msg = SM_CustOrdValidator.executeCustOrdEvent(event, getPO(event));
			}else if (getPO(event).get_TableName().equals(MAllocationLine.Table_Name)) {
				msg = SM_AllocationLineValidator.executeMAllocationLineEvent(event, getPO(event));
			}else if (getPO(event).get_TableName().equals(MPayment.Table_Name)) {
				msg = SM_PaymentValidator.executePaymentValidatorEvent(event, getPO(event));
			}


			logEvent(event, getPO(event), msg);

		}

	}
	@Override
	protected void initialize() {
		
		registerEvent(IEventTopics.AFTER_LOGIN);
		
		//MRP - DRP
		registerTableEvent(IEventTopics.DOC_BEFORE_COMPLETE, X_SM_DRP.Table_Name);
		registerTableEvent(IEventTopics.DOC_BEFORE_VOID, X_SM_DRP.Table_Name);
		registerTableEvent(IEventTopics.DOC_AFTER_COMPLETE, X_SM_DRP.Table_Name);
		
		//MRP - MRP
		registerTableEvent(IEventTopics.DOC_BEFORE_COMPLETE, X_SM_MRP.Table_Name);
		registerTableEvent(IEventTopics.DOC_BEFORE_VOID, X_SM_MRP.Table_Name);
		registerTableEvent(IEventTopics.DOC_AFTER_COMPLETE, X_SM_MRP.Table_Name);
		
		//MRP - CustOrder
		registerTableEvent(IEventTopics.PO_AFTER_NEW, X_SM_CustOrd_Upload.Table_Name);
		registerTableEvent(IEventTopics.PO_AFTER_CHANGE, X_SM_CustOrd_Upload.Table_Name);

		//SEMERULite - AllocationLine
		registerTableEvent(IEventTopics.PO_AFTER_NEW, MAllocationLine.Table_Name);
		registerTableEvent(IEventTopics.PO_AFTER_CHANGE, MAllocationLine.Table_Name);
		
		//SEMERULite - Invoice
		registerTableEvent(IEventTopics.DOC_BEFORE_COMPLETE, MInvoice.Table_Name);
		//SEMERULite - Payment
		registerTableEvent(IEventTopics.DOC_BEFORE_COMPLETE, MPayment.Table_Name);

		
	}
	
	private void logEvent (Event event, PO po, String msg) {
		log.fine("EVENT MANAGER // "+event.getTopic()+" po="+po+" MESSAGE ="+msg);
		if (msg.length()  > 0) 
			throw new AdempiereException(msg);	
	}
	

}
