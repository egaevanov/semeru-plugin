package org.semeru.form;

/******************************************************************************
 * Product: Adempiere ERP & CRM Smart Business Solution                       *
 * Copyright (C) 1999-2006 ComPiere, Inc. All Rights Reserved.                *
 * This program is free software; you can redistribute it and/or modify it    *
 * under the terms version 2 of the GNU General Public License as published   *
 * by the Free Software Foundation. This program is distributed in the hope   *
 * that it will be useful, but WITHOUT ANY WARRANTY; without even the implied *
 * warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.           *
 * See the GNU General Public License for more details.                       *
 * You should have received a copy of the GNU General Public License along    *
 * with this program; if not, write to the Free Software Foundation, Inc.,    *
 * 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA.                     *
 * For the text or an alternative of this public license, you may reach us    *
 * ComPiere, Inc., 2620 Augustine Dr. #245, Santa Clara, CA 95054, USA        *
 * or via info@compiere.org or http://www.compiere.org/license.html           *
 *****************************************************************************/
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.logging.Level;

import org.adempiere.exceptions.AdempiereException;
import org.adempiere.webui.AdempiereWebUI;
import org.adempiere.webui.apps.AEnv;
import org.adempiere.webui.component.Borderlayout;
import org.adempiere.webui.component.Button;
import org.adempiere.webui.component.ConfirmPanel;
import org.adempiere.webui.component.Grid;
import org.adempiere.webui.component.GridFactory;
import org.adempiere.webui.component.Panel;
import org.adempiere.webui.component.Row;
import org.adempiere.webui.component.Rows;
import org.adempiere.webui.component.Window;
import org.adempiere.webui.event.DialogEvents;
import org.adempiere.webui.window.FDialog;
import org.compiere.model.GridTab;
import org.compiere.model.MClient;
import org.compiere.util.Env;
import org.semeru.model.X_SMR_CashMethod;
import org.zkoss.image.AImage;
import org.zkoss.util.media.Media;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.event.UploadEvent;
import org.zkoss.zul.Center;
import org.zkoss.zul.Hbox;
import org.zkoss.zul.Image;
import org.zkoss.zul.North;
import org.zkoss.zul.Separator;
import org.zkoss.zul.South;

/**
 *  Base on the original Swing Image Dialog.
 *  @author   Jorg Janke
 *  
 *  Zk Port
 *  @author Low Heng Sin 
 *  
 */
public class WImageUpload extends ImageUpload implements EventListener<Event>,DialogEvents
{

	private Window window;
	private Integer AD_Client_ID = Env.getAD_Client_ID(Env.getCtx());
	
	private Button bntImg1 = new Button();
	private Button bntImg2 = new Button();
	private Button bntImg3 = new Button();

	private Image image1 = new Image();
	private Image image2 = new Image();
	private Image image3 = new Image();
	private Grid imageGrid = GridFactory.newGridLayout();


	//private static final String SAVE_PATH = "D:\\image\\";
	//private static final String SAVE_PATH = "/home/idempiere/idempiere.gtk.linux.x86_64/idempiere-server/image/";
	private String SAVE_PATH = "";
	private String AKSES_PATH = "";

	private ConfirmPanel confirmPanel = new ConfirmPanel(true,false,true,false,false,false);
	private int WindowNo;

	
	public static final String SELECT_DESELECT_ALL = "SelectAll";
	
	
	public WImageUpload(GridTab tab) 
	{
		super(tab);
		AD_Client_ID = (Integer)tab.getValue("AD_Client_ID");
		log.info(getGridTab().toString());
		
		window = new Window();
		
		WindowNo = getGridTab().getWindowNo();

		try
		{
			if (!dynInit())
				return;
			zkInit();
			//setInitOK(true);
		}
		catch(Exception e)
		{
			log.log(Level.SEVERE, "", e);
			setInitOK(false);
			throw new AdempiereException(e.getMessage());
		}
		AEnv.showWindow(window);
	}

	protected void zkInit() throws Exception{
		
		Borderlayout contentPane = new Borderlayout();
		window.appendChild(contentPane);
		window.setAttribute(Window.MODE_KEY, Window.MODE_HIGHLIGHTED);
		window.setSizable(true);
		
		window.setWidth("475px");
		window.setHeight("300px");
		window.setSizable(true);
		window.setBorder("normal");
		
		North north = new North();
		contentPane.appendChild(north);
		
		Center center = new Center();
        contentPane.appendChild(center);
        
       
        center.appendChild(imageGrid);
        imageGrid.setWidth("100%");
        imageGrid.setStyle("Height:100%;");

		Rows rows = null;
		Row row = null;

		rows = imageGrid.newRows();
        
        Hbox hboxImage1 = new Hbox();
        hboxImage1.setAlign("center");
        hboxImage1.setOrient("vertical");
        hboxImage1.setStyle("border: 1px solid #C0C0C0; border-radius:5px;margin:2px");
        bntImg1.setLabel("Upload");
        hboxImage1.setHflex("true");
        hboxImage1.setVflex("true");
        		
		row = rows.newRow();
		row.appendCellChild(hboxImage1,1);	
		hboxImage1.appendChild(image1);
        hboxImage1.appendChild(bntImg1);
        
		South south = new South();
		contentPane.appendChild(south);
		Panel southPanel = new Panel();
		south.appendChild(southPanel);
		
        Separator separator = new Separator();
		separator.setOrient("horizontal");
		separator.setBar(true);
		southPanel.appendChild(separator);
		southPanel.appendChild(confirmPanel);		
	
		contentPane.setWidth("100%");
		contentPane.setHeight("100%");
		confirmPanel.addActionListener(this);
				
		bntImg1.setUpload(AdempiereWebUI.getUploadSetting());
		bntImg1.addEventListener(Events.ON_UPLOAD, this);
		bntImg2.setUpload(AdempiereWebUI.getUploadSetting());
		bntImg2.addEventListener(Events.ON_UPLOAD, this);
		bntImg3.setUpload(AdempiereWebUI.getUploadSetting());
		bntImg3.addEventListener(Events.ON_UPLOAD, this);
		
		confirmPanel.addActionListener(Events.ON_CLICK, this);
		window.addEventListener(Events.ON_UPLOAD, this);
	    	
	}
	
	@Override
	public void onEvent(Event e) throws Exception {
		
		if (e instanceof UploadEvent) 
		{
			UploadEvent ue = (UploadEvent) e;
			
			System.out.println(e.getTarget().toString());
			if(e.getTarget().equals(bntImg1)){
				processUploadMedia((Media) ue.getMedia(),bntImg1);
			}
			
			

		}
		
		else if (e.getTarget().getId().equals(ConfirmPanel.A_OK))
		{
			try{	
				//InputStream imageStream =image.getContent().getStreamData();
				ArrayList<Image> listImg = new ArrayList<Image>();

				if(image1.getContent() != null){
					listImg.add(image1);
				}
			

				for (int i = 0 ; i < listImg.size() ; i++){
					saveFile(listImg.get(i));
				}
				
				
				window.dispose();
				
			}
			catch (Exception ex){
				FDialog.error(WindowNo, null, "Error", "Process Upload Gagal"+ex);
			}
			
			FDialog.info(WindowNo, null, "","Gambar Baru Berhasil Ditambahkan","Info");

			
		}
		//  Cancel
		else if (e.getTarget().getId().equals(ConfirmPanel.A_CANCEL))
		{
			window.onClose();
		}
		else if (e.getTarget().getId().equals(ConfirmPanel.A_RESET))
		{
			AImage img = null;
			image1.setContent(img);
			image2.setContent(img);
			image3.setContent(img);
		}
		
		window.addEventListener(DialogEvents.ON_WINDOW_CLOSE, new EventListener<Event>() {

			@Override
			public void onEvent(Event event) throws Exception {
				// TODO 
			}
		});
		
		
	}
	
	
	private void processUploadMedia(Media imageFile, Button btn) {
		if (imageFile == null)
			return;
	
		String fileName = imageFile.getName();
		
		//  See if we can load & display it
		try
		{
			InputStream is =  imageFile.getStreamData();
			AImage aImage = new AImage(fileName, is);
			
			if(btn.equals(bntImg1)){
				image1.setContent(aImage);
			}

			is.close();
		}
		catch (Exception e)
		{
			log.log(Level.WARNING, "load image", e);
			return;
		}

		window.invalidate();
		AEnv.showCenterScreen(window);

	}
	
	
	private void saveFile(Image imgSource) {
		BufferedInputStream in = null;
		BufferedOutputStream out = null;
		MClient client = new MClient(null, AD_Client_ID, null);
	
		SAVE_PATH = "/root/idempiere.server.product.linux.gtkx86_64/images/";
		AKSES_PATH ="http://192.168.88.18/";
		
//		SAVE_PATH = "D:\\imagetest\\";
//		AKSES_PATH ="D:\\imagetest";
		
		try {
			
			InputStream fin =imgSource.getContent().getStreamData();
			in = new BufferedInputStream(fin);
			
			File baseDir = new File(SAVE_PATH);
			
			if (!baseDir.exists()) {
				baseDir.mkdirs();
			}
	
			File file = new File(SAVE_PATH +client.getValue()+imgSource.getContent().getName());

			
			OutputStream fout = new FileOutputStream(file);
			out = new BufferedOutputStream(fout);
			byte buffer[] = new byte[1024];
			int ch = in.read(buffer);
			while (ch != -1) {
				out.write(buffer, 0, ch);
				ch = in.read(buffer);
			}		
			
			if(imgSource.equals(image1)){
				X_SMR_CashMethod cash = new X_SMR_CashMethod(null,getGridTab().getRecord_ID(), null);
				
				cash.setImageURL(AKSES_PATH +client.getValue()+imgSource.getContent().getName());
				cash.saveEx();
			
			}
			
		} catch (IOException e) {
			throw new RuntimeException(e);
		} catch (Exception e) {
			throw new RuntimeException(e);
		} finally {
			try {
				if (out != null) 
					out.close();	
				
				if (in != null)
					in.close();
				
			} catch (IOException e) {
				throw new RuntimeException(e);
			}
		}

	}
	
}  
