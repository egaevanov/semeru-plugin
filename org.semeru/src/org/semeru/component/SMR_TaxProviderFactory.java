/**
 * 
 */
package org.semeru.component;

import org.adempiere.base.ITaxProviderFactory;
import org.adempiere.model.ITaxProvider;
import org.semeru.tax.provider.IndonesiaTaxProvider;

/**
 * @author tegar
 *
 */
public class SMR_TaxProviderFactory implements ITaxProviderFactory{

	private static final String INDONESIA_TAX_PROVIDER = "org.semeru.tax.provider.IndonesiaTaxProvider";
	
	@Override
	public ITaxProvider newTaxProviderInstance(String className) {
		if (className.equalsIgnoreCase(INDONESIA_TAX_PROVIDER)){
			return new IndonesiaTaxProvider();
		}
		return null;
	}

}
