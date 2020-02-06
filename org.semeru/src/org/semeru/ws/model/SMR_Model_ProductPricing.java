package org.semeru.ws.model;

import java.math.BigDecimal;

/**
 * 
 * @author Tegar N
 *
 */
public class SMR_Model_ProductPricing {

	public String IsActive;
	public String Name;
	public Integer M_Product_ID;
	public Integer C_UOM_ID;
	public Integer M_Product_Category_ID;
	public Integer C_TaxCategory_ID;
	public String ProductType;
	public String ImageUrl;
	public Integer M_AttributeSet_ID;
	public Integer M_Brand_ID;
	public Integer M_PriceList_Buy_ID;
	public Integer M_PriceList_Version_Buy_ID;
	public BigDecimal PriceList_Buy;
	public Integer M_PriceList_Rtl_ID;
	public Integer M_PriceList_Version_Rtl_ID;
	public BigDecimal PriceList_Rtl;
	public BigDecimal PriceLimit_Rtl;
	public Integer M_PriceList_Rgs_ID;
	public Integer M_PriceList_Version_Rgs_ID;
	public BigDecimal PriceList_Rgs;
	public BigDecimal PriceLimit_Rgs;
	public String IsAttributeSet;
	public String Value;


	public SMR_Model_ProductPricing(String IsActive, String Name,
			Integer M_Product_ID, Integer C_UOM_ID,
			Integer M_Product_Category_ID, Integer C_TaxCategory_ID,
			String ProductType, String ImageUrl, Integer M_AttributeSet_ID,
			Integer M_Brand_ID, Integer M_PriceList_Buy_ID,
			Integer M_PriceList_Version_Buy_ID, BigDecimal PriceList_Buy,
			Integer M_PriceList_Rtl_ID, Integer M_PriceList_Version_Rtl_ID,
			BigDecimal PriceList_Rtl, BigDecimal PriceLimit_Rtl,
			Integer M_PriceList_Rgs_ID, Integer M_PriceList_Version_Rgs_ID,
			BigDecimal PriceList_Rgs, BigDecimal PriceLimit_Rgs,String IsAttributeSet,String Value) {

		
		
		this.IsActive = IsActive;
		this.Name = Name;
		this.M_Product_ID = M_Product_ID;
		this.C_UOM_ID = C_UOM_ID;
		this.M_Product_Category_ID = M_Product_Category_ID;
		this.C_TaxCategory_ID = C_TaxCategory_ID;
		this.ProductType = ProductType;
		this.ImageUrl = ImageUrl;
		this.M_AttributeSet_ID = M_AttributeSet_ID;
		this.M_Brand_ID = M_Brand_ID;
		this.M_PriceList_Buy_ID = M_PriceList_Buy_ID;
		this.M_PriceList_Version_Buy_ID = M_PriceList_Version_Buy_ID;
		this.PriceList_Buy = PriceList_Buy;
		this.M_PriceList_Rtl_ID = M_PriceList_Rtl_ID;
		this.M_PriceList_Version_Rtl_ID = M_PriceList_Version_Rtl_ID;
		this.PriceList_Rtl = PriceList_Rtl;
		this.PriceLimit_Rtl = PriceLimit_Rtl;
		this.M_PriceList_Rgs_ID = M_PriceList_Rgs_ID;
		this.M_PriceList_Version_Rgs_ID = M_PriceList_Version_Rgs_ID;
		this.PriceList_Rgs = PriceList_Rgs;
		this.PriceLimit_Rgs = PriceLimit_Rgs;
		this.IsAttributeSet = IsAttributeSet;
		this.Value = Value;

		
		
	}
}
