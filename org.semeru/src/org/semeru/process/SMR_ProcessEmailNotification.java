package org.semeru.process;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.logging.Level;

import org.compiere.model.MClient;
import org.compiere.model.MMailText;
import org.compiere.model.MUser;
import org.compiere.model.MUserMail;
import org.compiere.process.SvrProcess;
import org.compiere.util.DB;
import org.compiere.util.EMail;
import org.semeru.model.X_mob_mst_package;

public class SMR_ProcessEmailNotification extends SvrProcess{
	
	
	private int p_AD_Client_ID = 0;
	
	
	private int				m_R_MailText_ID = 1000000;
	private MMailText		m_MailText = null;
	private MClient			m_client = null;

	@Override
	protected void prepare() {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected String doIt() throws Exception {
		
		String rslt = "";
		final String Error = "ERROR";
		
		m_MailText = new MMailText (getCtx(), m_R_MailText_ID, get_TrxName());
		if (m_MailText.getR_MailText_ID() == 0)
			throw new Exception ("Not found @R_MailText_ID@=" + m_R_MailText_ID);
		
		//	Client Info
		m_client = MClient.get (getCtx());
		if (m_client.getAD_Client_ID() == 0)
			throw new Exception ("Not found @AD_Client_ID@");
		
		// Check SMTP Host
		if (m_client.getSMTPHost() == null || m_client.getSMTPHost().length() == 0)
			throw new Exception ("No SMTP Host found");
			
		StringBuilder SQLGetDueDate = new StringBuilder();
		SQLGetDueDate.append("SELECT user_id,mob_trx_contract_id,now(),package_id ");
		SQLGetDueDate.append(" FROM mob_trx_contract ");
		SQLGetDueDate.append(" WHERE AD_Client_ID = "+ p_AD_Client_ID);
		SQLGetDueDate.append(" AND next_due_date < now()");

		
		PreparedStatement pstmt = null;
     	ResultSet rs = null;
			try {
				pstmt = DB.prepareStatement(SQLGetDueDate.toString(), null);
				rs = pstmt.executeQuery();
				while (rs.next()) {
					int AD_User_ID = rs.getInt(1); 
					Timestamp p_Next_DueDate = rs.getTimestamp(2);
					Timestamp p_Now = rs.getTimestamp(3);
					int package_id = rs.getInt(4);
					MUser user = new MUser(getCtx(), AD_User_ID, get_TrxName());
					X_mob_mst_package pkg = new X_mob_mst_package(getCtx(), package_id, get_TrxName());
					
					
					if(p_Next_DueDate.compareTo(p_Now) < 3){
						
						sendIndividualMail(user.getName(), AD_User_ID, rs.getTimestamp(2),pkg,false);
						
					}else if(p_Next_DueDate.compareTo(p_Now) > 0) {
						
						sendIndividualMail(user.getName(), AD_User_ID, rs.getTimestamp(2),pkg,true);

					}

					
				}
				
				

			} catch (SQLException err) {
				
				log.log(Level.SEVERE, SQLGetDueDate.toString(), err);
				rslt = Error;
				
			} finally {
				DB.close(rs, pstmt);
				rs = null;
				pstmt = null;
			}
			
		return rslt;
	}

	/**
	 * 	Send Individual Mail
	 *	@param Name user name
	 *	@param AD_User_ID user
	 *	@param unsubscribe unsubscribe message
	 *	@return true if mail has been sent
	 */
	private Boolean sendIndividualMail (String Name, int AD_User_ID, Timestamp dueDate,X_mob_mst_package Package,boolean isEnd)
	{

		
	 	MUser m_from = null;
		
		MUser to = new MUser (getCtx(), AD_User_ID, null);
		m_MailText.setUser(AD_User_ID);		//	parse context
		StringBuilder message = new StringBuilder(m_MailText.getMailText(true));
		
		message.append(" "+to.getName());
		message.append("\n");	
		
		if(isEnd) {
			message.append("Lisensi "+ Package.getname_package()+" SemeruLite Anda Telah Habis ");			
		}else {
			message.append("Lisensi "+ Package.getname_package()+" SemeruLite Anda Akan Segera Habis ");

		}
		
		if(Package.getname_package().toUpperCase().equals("TRIAL")) {
			if(isEnd) {
				message.append("Silahkan Upgrade Lisensi Akun Anda Untuk Kembali Menggunakan Aplikasi Semerulite");
			}else {
				message.append("Silahkan Upgrade Lisensi Akun Anda Untuk Tetap Bisa Menggunakan Aplikasi Semerulite Sebelum Tanggal: "+dueDate);
			}
		}else {
			if(isEnd) {
				message.append("Mohon Selesaikan Pembayaran");
			}else {
				message.append("Mohon Selesaikan Pembayaran Sebelum Tanggal Jatuh Tempo :"+dueDate);

			}
		}
		message.append("\n");
		message.append("\n");
		message.append("\n");
		message.append("Terima Kasih.");
		message.append("\n");
		message.append("\n");
		message.append("\n");
		message.append("\n");
		message.append("Salam Hangat");
		message.append("\n");
		message.append("\n");
		message.append("Team Semeru");
		
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
		MUserMail uMail = new MUserMail(m_MailText, AD_User_ID, email);
		uMail.saveEx();
		
		//
		if (OK) {
			//if (log.isLoggable(Level.FINE)) log.fine(to.getEMail());
			uMail.setIsDelivered("Y");
			uMail.saveEx();

		} else {
			log.warning("FAILURE - " + to.getEMail());
		}
		StringBuilder msglog = new StringBuilder((OK ? "@OK@" : "@ERROR@")).append(" - ").append(to.getEMail());
		addLog(0, null, null, msglog.toString());
		return new Boolean(OK);
	}	//	sendIndividualMail
	
	
}
