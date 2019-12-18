package org.hdsoft.model;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.util.Properties;

import org.compiere.util.DB;

public class MHDProduct extends X_HD_Product{

	/**
	 * 
	 */
	private static final long sWerialVersionUID = 6322034510044862982L;

	public MHDProduct(Properties ctx, ResultSet rs, String trxName) {
		super(ctx, rs, trxName);
		// TODO Auto-generated constructor stub
	}

	public MHDProduct(Properties ctx, int HD_Product_ID, String trxName) {
		super(ctx, HD_Product_ID, trxName);
		// TODO Auto-generated constructor stub
	}
	


}
