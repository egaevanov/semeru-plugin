package org.semeru.component;

import org.compiere.grid.ICreateFrom;
import org.compiere.grid.ICreateFromFactory;
import org.compiere.model.GridTab;
import org.semeru.form.WImageUpload;
import org.semeru.model.I_SMR_CashMethod;


public class SMR_CreateFromFactory implements ICreateFromFactory {

	@Override
	public ICreateFrom create(GridTab mTab) {

		String tableName = mTab.getTableName();

		System.out.println(tableName);
		
		if (tableName.equals(I_SMR_CashMethod.Table_Name))
			return new WImageUpload(mTab);
		
		return null;
		
		
	}

}