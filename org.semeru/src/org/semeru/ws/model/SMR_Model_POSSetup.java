package org.semeru.ws.model;


/**
 * 
 * @author Tegar N
 *
 */
public class SMR_Model_POSSetup{

	public String IsActive;
	public String Address;
	public String store_name;
	public String Name;
	public String StoreName;
	public String Footer;
	public Integer M_PriceList_ID;
	public Integer M_Sales_Locator_ID;
	public Integer M_Locator_ID;
	public Integer C_BankAccount_ID;
	public String Phone;
	public Integer C_Pos_Setup_ID;
	public Integer M_Warehouse_ID;
	public Integer C_Pos_ID;
	public String StorePhone;
	public String StoreAddress;
	public String StoreInvNote;

	public SMR_Model_POSSetup(String IsActive, String Name, String Address, String store_name, String Footer, Integer M_PriceList_ID,
			Integer M_Sales_Locator_ID, Integer M_Locator_ID, Integer C_BankAccount_ID,String Phone,Integer C_Pos_Setup_ID, Integer M_Warehouse_ID,
			Integer C_Pos_ID,String StorePhone, String StoreAddress, String StoreInvNote, String StoreName){
		
		this.IsActive = IsActive;
		this.Name = Name;
		this.StoreName = StoreName;
		this.StorePhone = StorePhone;
		this.StoreAddress = StoreAddress;
		this.StoreInvNote = StoreInvNote;
		this.M_PriceList_ID = M_PriceList_ID;
		this.M_Sales_Locator_ID = M_Sales_Locator_ID;
		this.M_Locator_ID = M_Locator_ID;
		this.C_BankAccount_ID = C_BankAccount_ID;
		this.Footer = Footer;
		this.Phone = Phone;
		this.C_Pos_Setup_ID = C_Pos_Setup_ID;
		this.M_Warehouse_ID = M_Warehouse_ID;
		this.C_Pos_ID = C_Pos_ID;
		this.store_name = store_name;
		this.Address = Address;
		
	}
}