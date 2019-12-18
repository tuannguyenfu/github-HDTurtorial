package org.hdsoft.model;

import java.sql.ResultSet;
import java.util.Properties;

import org.adempiere.base.IProcessFactory;
import org.compiere.process.ProcessCall;

public class MHDCustomer extends X_HD_Customer {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4358117204558125186L;

	public MHDCustomer(Properties ctx, ResultSet rs, String trxName) {
		super(ctx, rs, trxName);
		// TODO Auto-generated constructor stub
	}

	public MHDCustomer(Properties ctx, int HD_Customer_ID, String trxName) {
		super(ctx, HD_Customer_ID, trxName);
		// TODO Auto-generated constructor stub
	}
	

	@Override
	protected boolean beforeSave(boolean newRecord) {
		// TODO Auto-generated method stub

		return super.beforeSave(newRecord);
	}
	
}
