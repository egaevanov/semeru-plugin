package org.semeru.form;

import java.util.logging.Level;

import org.compiere.apps.IStatusBar;
import org.compiere.grid.CreateFrom;
import org.compiere.minigrid.IMiniTable;
import org.compiere.model.GridTab;
import org.compiere.util.CLogger;

public class ImageUpload extends CreateFrom {

	public ImageUpload(GridTab gridTab) {
		super(gridTab);
		if (log.isLoggable(Level.INFO)) log.info(gridTab.toString());
	}

	public CLogger log = CLogger.getCLogger(ImageUpload.class);

	@Override
	public Object getWindow() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean dynInit() throws Exception {
		log.config("");
		setTitle("Upload Image");
		return true;
	}

	@Override
	public void info(IMiniTable miniTable, IStatusBar statusBar) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean save(IMiniTable miniTable, String trxName) {
		// TODO Auto-generated method stub
		return true;
	}

	
}
