package org.hdsoft.model;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.Date;
import java.util.Properties;

import com.sun.net.httpserver.Authenticator.Result;
import com.sun.source.tree.WhileLoopTree;


import org.compiere.db.*;
import org.compiere.model.POResultSet;
import org.compiere.util.DB;

public class MHDProductStock extends X_HD_Product_Stock {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2953753783546059937L;

	public MHDProductStock(Properties ctx, ResultSet rs, String trxName) {
		super(ctx, rs, trxName);
		// TODO Auto-generated constructor stub
	}

	public MHDProductStock(Properties ctx, int HD_Product_Stock_ID, String trxName) {
		super(ctx, HD_Product_Stock_ID, trxName);
		// TODO Auto-generated constructor stub
	}

	
//	//update current price, stock
	@Override
	protected boolean afterSave(boolean newRecord, boolean success) {
		// TODO Auto-generated method stub
		MHDProduct rMHD = new MHDProduct(getCtx(), getHD_Product_ID(), get_TrxName());
		String sql1 = "select Max (datetrx) from adempiere.HD_Product_Stock WHERE HD_Product_ID = "
				+ getHD_Product_ID();
		Date[] lastDateTemp = null;
		Date lastDate = DB.getSQLValueTS(get_TrxName(), sql1, lastDateTemp);
		if (lastDate.compareTo(getDateTrx()) <= 0) {
			rMHD.setcurrentprice(gettargetsaleprice());
			rMHD.save();
		}
		if (bfSaveStock == null) {
			rMHD.set_ValueOfColumn(rMHD.COLUMNNAME_stockcount, rMHD.getstockcount().add(getstockcount()));
			rMHD.save();
		} else {
			if (getstockcount().compareTo(bfSaveStock) != 0) {
				rMHD.setstockcount(rMHD.getstockcount().subtract(bfSaveStock).add(getstockcount()));
				rMHD.save();
			}
		}

		if (rMHD.getstockcount().compareTo(big(0.0)) > 0) {
			rMHD.set_ValueOfColumn(rMHD.COLUMNNAME_isstock, true);
			rMHD.setIsActive(true);
			rMHD.save();
		}
		
		return super.afterSave(newRecord, success);
	}

	static BigDecimal bfSaveStock = null;

	@Override
	protected boolean beforeSave(boolean newRecord) {
		// TODO Auto-generated method stub
		String sql2 = "select stockcount from adempiere.HD_Product_Stock\n" + "where HD_Product_Stock_ID = "
				+ getHD_Product_Stock_ID();
		BigDecimal[] bfSaveStockTemp = null;
		bfSaveStock = DB.getSQLValueBDEx(get_TrxName(), sql2, bfSaveStockTemp);
		return super.beforeSave(newRecord);
	}
	
	@Override
	protected boolean afterDelete(boolean success) {
		// TODO Auto-generated method stub
		//Remove Currentprice
		MHDProduct rMHD = new MHDProduct(getCtx(), getHD_Product_ID(), get_TrxName());
		BigDecimal[] priceTemp = null;
		BigDecimal currentPrice;
		String sql2 = "select targetsaleprice from adempiere.HD_Product_Stock\n" + 
				"Where datetrx = (select max(datetrx) "
				+"from adempiere.HD_Product_Stock where HD_Product_ID = " + getHD_Product_ID() + ")";
		currentPrice = DB.getSQLValueBDEx(get_TrxName(), sql2, priceTemp);
		rMHD.setcurrentprice(currentPrice);
		rMHD.save();
		return super.afterDelete(success);
	}
	
	@Override
	protected boolean beforeDelete() {
		// TODO Auto-generated method stub
		//Remove stockcount

		MHDProduct rMHD = new MHDProduct(getCtx(), getHD_Product_ID(), get_TrxName());
		rMHD.setstockcount(rMHD.getstockcount().subtract(getstockcount()));
		rMHD.save();
		return super.beforeDelete();
	}
//	

	public static BigDecimal big(double d) {
		BigDecimal big = new BigDecimal(d);
		return big;
	}

}
