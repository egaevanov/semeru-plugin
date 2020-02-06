package org.semeru.process;

import java.util.logging.Level;

import org.compiere.model.MClient;
import org.compiere.model.MMailText;
import org.compiere.model.MUser;
import org.compiere.model.MUserMail;
import org.compiere.process.SvrProcess;
import org.compiere.util.EMail;

public class SMR_ProcessChangePass extends SvrProcess {

	private int p_AD_User_ID = 0;
	private String p_newPass = "";
	
	
	private MMailText		m_MailText = null;
	private MClient			m_client = null;
	private MUser			m_from = null;
	//private MInterestArea 	m_Interest = null;
	@Override
	protected void prepare() {		
		
	}

	@Override
	protected String doIt() throws Exception {

		
		MUser user = new MUser(getCtx(), p_AD_User_ID, get_TrxName());
		user.setPassword(p_newPass);
		user.saveEx();
			
		if(user.getPassword().equals(p_newPass)){
			
			//TODO 
			sendIndividualMail (user.getName(),user.getAD_User_ID());			
		}
		
		return "";
	}
	
	
	/**
	 * 	Send Individual Mail
	 *	@param Name user name
	 *	@param AD_User_ID user
	 *	@param unsubscribe unsubscribe message
	 *	@return true if mail has been sent
	 */
	private Boolean sendIndividualMail (String Name, int AD_User_ID)
	{

		MUser to = new MUser (getCtx(), AD_User_ID, null);
		m_MailText.setUser(AD_User_ID);		//	parse context
		StringBuilder message = new StringBuilder(m_MailText.getMailText(true));
		
		message.append("Selamat.."+to.getName());
		message.append("\n");
		message.append("Perubahan Password Anda Berhasil Diupdate");
		
		message.append("Password Baru Anda :"+ p_newPass);		
		message.append("\n");
		message.append("\n");
		message.append("\n");
		message.append("\n");
		message.append("Salam Hangat");
		message.append("\n");
		message.append("\n");
		message.append("Team Decoris Ganteng");
		
	
		
		EMail email = m_client.createEMail(m_from, to, m_MailText.getMailHeader(), message.toString());
		
		if (m_MailText.isHtml())
			email.setMessageHTML(m_MailText.getMailHeader(), message.toString());
		else
		{
			email.setSubject (m_MailText.getMailHeader());
			email.setMessageText (message.toString());
		}
		if (!email.isValid() && !email.isValid(true))
		{
			log.warning("NOT VALID - " + email);
			to.setIsActive(false);
			to.addDescription("Invalid EMail");
			to.saveEx();
			return Boolean.FALSE;
		}
		boolean OK = EMail.SENT_OK.equals(email.send());
		new MUserMail(m_MailText, AD_User_ID, email).saveEx();
		//
		if (OK) {
			if (log.isLoggable(Level.FINE)) log.fine(to.getEMail());
		} else {
			log.warning("FAILURE - " + to.getEMail());
		}
		StringBuilder msglog = new StringBuilder((OK ? "@OK@" : "@ERROR@")).append(" - ").append(to.getEMail());
		addLog(0, null, null, msglog.toString());
		return new Boolean(OK);
	}	//	sendIndividualMail

}
