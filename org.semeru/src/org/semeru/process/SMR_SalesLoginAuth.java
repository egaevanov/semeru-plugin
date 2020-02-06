package org.semeru.process;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;

import org.compiere.model.MBPartner;
import org.compiere.model.MUser;
import org.compiere.process.ProcessInfoParameter;
import org.compiere.process.SvrProcess;
import org.compiere.util.CLogger;
import org.compiere.util.DB;
import org.compiere.util.Secure;
import org.compiere.util.SecureEngine;

public class SMR_SalesLoginAuth extends SvrProcess{

	private static CLogger	log	= CLogger.getCLogger (Secure.class.getName());
	private String p_PasswordHashed = "";
	private String p_Salt = "";
//	private int p_user_org_id = 0;
	private String p_Password = "";
	private String p_EMail = "";
	private int p_ad_user_id = 0;



	
	@Override
	protected void prepare() {


		ProcessInfoParameter[] para = getParameter();
		for (int i = 0; i < para.length; i++)
		{
			String name = para[i].getParameterName();
			if (para[i].getParameter() == null);
			
			else if(name.equals("EMail"))
				p_EMail = (String)para[i].getParameterAsString();
			else if(name.equals("Password"))
				p_Password = (String)para[i].getParameterAsString();
	
			else
				log.log(Level.SEVERE, "Unknown Parameter: " + name);
		}
		
		
	}

	@Override
	protected String doIt() throws Exception {

		String as = "";

		
		StringBuilder SQLGetUserData = new StringBuilder();
		SQLGetUserData.append("SELECT password,salt,ad_org_id,ad_user_id ");
		SQLGetUserData.append(" FROM AD_User ");
		SQLGetUserData.append(" WHERE email = '"+p_EMail+"'");
	
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			pstmt = DB.prepareStatement(SQLGetUserData.toString(), get_TrxName());
			
			rs = pstmt.executeQuery();
			while (rs.next()) {
				
				p_PasswordHashed = rs.getString(1);
				p_Salt = rs.getString(2);
//				p_user_org_id = rs.getInt(3);
				p_ad_user_id = rs.getInt(4);
				
				
			}

		} catch (SQLException e) {
			log.log(Level.SEVERE, SQLGetUserData.toString(), e);
			as = "ERROR";
		} finally {
			DB.close(rs, pstmt);
			rs = null;
			pstmt = null;
		}

		if(p_ad_user_id <= 0) {
			as = "EMail Tidak Terdaftar";
		}
		
		boolean OK = false;
		if(p_EMail != null && !p_EMail.isEmpty() && p_EMail != "" && p_Salt != null && p_Salt != "" && !p_Salt.isEmpty()) {
			
			OK = authenticateHash(p_Password,p_PasswordHashed,p_Salt);
			
		}else {
			as = "ERROR";
			return as;
		}
		
		
		if(OK) {
			MUser user = new MUser(getCtx(), p_ad_user_id, get_TrxName());
			MBPartner bp = null;
			String type = "";
			if(user.getC_BPartner_ID() > 0) {
				bp  = new MBPartner(getCtx(), user.getC_BPartner_ID(), get_TrxName());
				if(bp.isSalesRep()&&bp.isEmployee()) {
					type = "SA";
				}else if(bp.isEmployee()&&!bp.isSalesRep()&&!bp.isCustomer()) {
					type = "SU";
				}
			}
				
			as = user.getName()+","+user.getEMail()+","+user.getAD_User_ID()+","+type;	
		}else {
			as = "ERROR";
		}
		
		return as;
	}
	
	
	public boolean authenticateHash (String passwordInput,String PassHashed, String salt)  {
		return SecureEngine.isMatchHash (PassHashed, salt, passwordInput);
	}	
	
	
	public static byte[] convertHexString (String hexString)
	{
		if (hexString == null || hexString.length() == 0)
			return null;
		int size = hexString.length()/2;
		byte[] retValue = new byte[size];
		String inString = hexString.toLowerCase();

		try
		{
			for (int i = 0; i < size; i++)
			{
				int index = i*2;
				int ii = Integer.parseInt(inString.substring(index, index+2), 16);
				retValue[i] = (byte)ii;
			}
			return retValue;
		}
		catch (Exception e)
		{
			if (log.isLoggable(Level.FINEST)) log.finest(hexString + " - " + e.getLocalizedMessage());
		}
		return null;
	}   //  convertToHexString

	
	public String getSHA512Hash (int iterations, String value, byte[] salt) throws NoSuchAlgorithmException, UnsupportedEncodingException
	{
		MessageDigest digest = MessageDigest.getInstance("SHA-512");
		digest.reset();
		digest.update(salt);
		byte[] input = digest.digest(value.getBytes("UTF-8"));
		for (int i = 0; i < iterations; i++) {
			digest.reset();
			input = digest.digest(input);
		}
		digest.reset();
		//
		return convertToHexString(input);
	}	//	getSHA512Hash
	

	
	public static String convertToHexString (byte[] bytes)
	{
		//	see also Util.toHex
		int size = bytes.length;
		StringBuilder buffer = new StringBuilder(size*2);
		for(int i=0; i<size; i++)
		{
			// convert byte to an int
			int x = bytes[i];
			// account for int being a signed type and byte being unsigned
			if (x < 0)
				x += 256;
			String tmp = Integer.toHexString(x);
			// pad out "1" to "01" etc.
			if (tmp.length() == 1)
				buffer.append("0");
			buffer.append(tmp);
		}
		return buffer.toString();
	}   //  convertToHexString
}
