package org.semeru.mrp.validator;

import org.adempiere.base.event.IEventTopics;
import org.compiere.model.PO;
import org.compiere.util.CLogger;
import org.osgi.service.event.Event;
import org.semeru.mrp.model.X_SM_DRP;

public class SM_DRPValidator {
	
	public static CLogger log = CLogger.getCLogger(SM_DRPValidator.class);
	
	public static String executeDRPEvent(Event event, PO po) {
		
		String msgDRP = "";
		X_SM_DRP DRP = (X_SM_DRP) po;
		if (event.getTopic().equals(IEventTopics.DOC_BEFORE_COMPLETE)) {
		
			DRPBeforeComplete(DRP);
				
		}
		
	return msgDRP;

	}
	
	
	private static String DRPBeforeComplete(X_SM_DRP DRP) {
		String rs = "";
		
		
//		MOrder Ord = new MOrder(Env.getCtx(), 0, null);
//		MDocType dt = MDocType.get(Env.getCtx(), 100000);
//		MInOut shipment = createShipment (Ord,dt, DRP.getMovementDate());

		
		return rs;
	}
	
//	private static MInOut createShipment(MOrder Ord,MDocType dt, Timestamp movementDate)
//	{
//		
//		String m_processMsg = "";
//		if (log.isLoggable(Level.INFO)) log.info("For " + dt);
//		MInOut shipment = new MInOut (Ord, dt.getC_DocTypeShipment_ID(), movementDate);
	//	shipment.setDateAcct(getDateAcct());
//		if (!shipment.save())
//		{
//			m_processMsg = "Could not create Shipment";
//			return null;
//		}
		//
//		MOrderLine[] oLines = Ord.getLines(true, null);
//		for (int i = 0; i < oLines.length; i++)
//		{
//			MOrderLine oLine = oLines[i];
//			//
//			MInOutLine ioLine = new MInOutLine(shipment);
//			//	Qty = Ordered - Delivered
//			BigDecimal MovementQty = oLine.getQtyOrdered().subtract(oLine.getQtyDelivered()); 
//			//	Location
//			int M_Locator_ID = MStorageOnHand.getM_Locator_ID (oLine.getM_Warehouse_ID(), 
//					oLine.getM_Product_ID(), oLine.getM_AttributeSetInstance_ID(), 
//					MovementQty, null);
//			if (M_Locator_ID == 0)		//	Get default Location
//			{
//				MWarehouse wh = MWarehouse.get(Env.getCtx(), oLine.getM_Warehouse_ID());
//				M_Locator_ID = wh.getDefaultLocator().getM_Locator_ID();
//			}
//			//
//			ioLine.setOrderLine(oLine, M_Locator_ID, MovementQty);
//			ioLine.setQty(MovementQty);
//			if (oLine.getQtyEntered().compareTo(oLine.getQtyOrdered()) != 0)
//				ioLine.setQtyEntered(MovementQty
//					.multiply(oLine.getQtyEntered())
//					.divide(oLine.getQtyOrdered(), 6, RoundingMode.HALF_UP));
//			if (!ioLine.save())
//			{
//				m_processMsg = "Could not create Shipment Line";
//				return null;
//			}
//		}
//
//		if (!shipment.processIt(DocAction.ACTION_Complete))
//			throw new AdempiereException("Failed when processing document - " + shipment.getProcessMsg());
//		// end added
//		shipment.saveEx();
//		if (!MOrder.DOCSTATUS_Completed.equals(shipment.getDocStatus()))
//		{
//			m_processMsg = "@M_InOut_ID@: " + shipment.getProcessMsg();
//			return null;
//		}
//		return shipment;
//	}	//	createShipment
	
	

}
