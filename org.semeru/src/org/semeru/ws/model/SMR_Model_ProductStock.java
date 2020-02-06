package org.semeru.ws.model;

public class SMR_Model_ProductStock {
	
	public String Client;
	public String Organization;
	public String Product_Value;
	public String Product_Name;
	public String UOM_Value;
	public String ProductCategoryKey;
	public String ProductType;
	public String SalesPrice;
	public String WholeSalerPrice;
	public String PurchasePrice;
	public String LocatorValue;
	public String QuantityCount;
	public String TaxType;
	public String Brand_Value;

	public SMR_Model_ProductStock(String Client, String Organization,String Product_Value,String Product_Name,String UOM_Value,
			String ProductCategoryKey,String ProductType,String SalesPrice,String WholeSalerPrice,String PurchasePrice,
			String LocatorValue,String QuantityCount,String TaxType,String Brand_Value) {
		
		this.Client = Client;
		this.Organization = Organization;
		this.Product_Value = Product_Value;
		this.Product_Name = Product_Name;
		this.UOM_Value = UOM_Value;
		this.ProductCategoryKey = ProductCategoryKey;
		this.ProductType = ProductType;
		this.SalesPrice = SalesPrice;
		this.WholeSalerPrice = WholeSalerPrice;
		this.PurchasePrice = PurchasePrice;
		this.LocatorValue = LocatorValue;
		this.QuantityCount = QuantityCount;
		this.TaxType = TaxType;
		this.Brand_Value = Brand_Value;
		
		
	}	

}


