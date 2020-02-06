package org.semeru.component;

import java.util.ArrayList;
import java.util.List;

import org.adempiere.base.IColumnCallout;
import org.adempiere.base.IColumnCalloutFactory;
import org.semeru.mrp.callout.SM_CallOutMPS;
import org.semeru.mrp.callout.SM_CalloutProdPlan;
import org.semeru.mrp.model.I_SM_MPS;
import org.semeru.mrp.model.I_SM_Product_Plan;
/**
 * 
 * @author Tegar N
 *
 */

public class SMR_CallOutFactory implements IColumnCalloutFactory {

	@Override
	public IColumnCallout[] getColumnCallouts(String tableName,
			String columnName) {

		List<IColumnCallout> list = new ArrayList<IColumnCallout>();

		
		if (tableName.equals(I_SM_Product_Plan.Table_Name)){
			list.add (new SM_CalloutProdPlan());
		}else if (tableName.equals(I_SM_MPS.Table_Name)){
			list.add (new SM_CallOutMPS());
		}
		return list != null ? list.toArray(new IColumnCallout[0])
				: new IColumnCallout[0];
	}

}
