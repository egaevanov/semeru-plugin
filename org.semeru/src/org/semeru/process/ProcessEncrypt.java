package org.semeru.process;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.logging.Level;

import org.compiere.process.ProcessInfoParameter;
import org.compiere.process.SvrProcess;
import org.compiere.util.CLogger;
import org.compiere.util.Secure;
import org.compiere.util.SecureEngine;

public class ProcessEncrypt extends SvrProcess{

	private static CLogger	log	= CLogger.getCLogger (Secure.class.getName());
	private String p_PasswordHashed = "f049a2ee5d8ed333672a167c5ace71333dff8e53d434c3f43f79be1e74ff9480818a7660630932d0374255b984eeaa644a1a916ee928a096a6bd161af79115c7";
	private String p_Salt = "43dc6e5594740399";
	private String p_Password = "";


	
	@Override
	protected void prepare() {


		ProcessInfoParameter[] para = getParameter();
		for (int i = 0; i < para.length; i++)
		{
			String name = para[i].getParameterName();
			if (para[i].getParameter() == null);
			
			else if(name.equals("Password"))
				p_Password = (String)para[i].getParameterAsString();
//			
//			else if(name.equals("Salt"))
//				p_Salt = (String)para[i].getParameterAsString();
//			
//			else if(name.equals("Password"))
//				p_Password = (String)para[i].getParameterAsString();
//			
			else
				log.log(Level.SEVERE, "Unknown Parameter: " + name);
		}
		
		
	}

	@Override
	protected String doIt() throws Exception {

		String as = "";
		byte[] ByteSalt = convertHexString(p_Salt);
		String rsPassword = getSHA512Hash(1000, p_Password,ByteSalt);
		System.out.println("PASSWORD HASHED :"+p_PasswordHashed);
		System.out.println("RESULT PASSWORD :"+rsPassword);

		
		return as;
	}
	
	
	public boolean authenticateHash (String password)  {
		return SecureEngine.isMatchHash (p_PasswordHashed, p_Salt, password);
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
