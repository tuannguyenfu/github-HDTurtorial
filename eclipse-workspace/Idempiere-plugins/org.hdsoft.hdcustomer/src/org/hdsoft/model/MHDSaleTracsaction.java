package org.hdsoft.model;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.Date;
import java.util.Properties;

import org.compiere.util.DB;

public class MHDSaleTracsaction extends X_HD_Sale_Transaction {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5098508036000740936L;

	public MHDSaleTracsaction(Properties ctx, ResultSet rs, String trxName) {
		super(ctx, rs, trxName);
		// TODO Auto-generated constructor stub
	}

	public MHDSaleTracsaction(Properties ctx, int HD_Sale_Transaction_ID, String trxName) {
		super(ctx, HD_Sale_Transaction_ID, trxName);
		// TODO Auto-generated constructor stub
	}

	@Override
	protected boolean afterSave(boolean newRecord, boolean success) {
		// TODO Auto-generated method stub
		// update datetrx Customer
		MHDProduct rcMHDP = new MHDProduct(getCtx(), getHD_Product_ID(), get_TrxName());
		MHDCustomer rcMHDC = new MHDCustomer(getCtx(), getHD_Customer_ID(), get_TrxName());
		if (rcMHDC.getlastdatetrx().compareTo(getDateTrx()) <= 0) {
			rcMHDC.setlastdatetrx(getDateTrx());
			rcMHDC.saveEx();
		}
		// update productstock
		if (bfsaveExSaleQty == null) {
			rcMHDP.setstockcount(rcMHDP.getstockcount().subtract(getsaleqty()));
			rcMHDP.saveEx();
		} else {
			if (bfsaveExSaleQty.compareTo(getsaleqty()) != 0) {
				rcMHDP.setstockcount(rcMHDP.getstockcount().add(bfsaveExSaleQty).subtract(getsaleqty()));
				rcMHDP.saveEx();
			}
		}
		// setisStockProduct
		if (rcMHDP.getstockcount().compareTo(getBig(0.0)) <= 0) {
			rcMHDP.set_ValueOfColumn(rcMHDP.COLUMNNAME_isstock, false);
			rcMHDP.setIsActive(false);
			rcMHDP.saveEx();
		}
		BigDecimal unit = new BigDecimal(1);
		// update producttotalamt, update customertotalamt
		if (bfsaveExTTA == null) {
			rcMHDP.settotalrevenueamt(rcMHDP.gettotalrevenueamt().add(getTotalAmt()));
			rcMHDP.saveEx();
			rcMHDC.setTotalAmt(rcMHDC.getTotalAmt().add(getTotalAmt()));
			rcMHDC.saveEx();
		} else {
			if (bfsaveExTTA.compareTo(getTotalAmt()) != 0) {
				rcMHDP.settotalrevenueamt(rcMHDP.gettotalrevenueamt().subtract(bfsaveExTTA).add(getTotalAmt()));
				rcMHDP.saveEx();
				rcMHDC.setTotalAmt(rcMHDC.getTotalAmt().subtract(bfsaveExTTA).add(getTotalAmt()));
				rcMHDC.saveEx();
			}
		}
		// update CustomerOverLimitAmt
		if (bfsaveExTTA == null && getTotalAmt().compareTo(rcMHDC.getlimitamt()) >= 0) {
			rcMHDC.setoverlimitcount(rcMHDC.getoverlimitcount().add(unit));
			rcMHDC.saveEx();
		} else if (bfsaveExTTA != null && bfsaveExTTA.compareTo(rcMHDC.getlimitamt()) > 0) {
			if (getTotalAmt().compareTo(rcMHDC.getoverlimitcount()) < 0) {
				rcMHDC.setoverlimitcount(rcMHDC.getoverlimitcount().subtract(unit));
				rcMHDC.saveEx();
			}
		} else if (bfsaveExTTA != null && bfsaveExTTA.compareTo(rcMHDC.getlimitamt()) < 0) {
			if (getTotalAmt().compareTo(rcMHDC.getoverlimitcount()) >= 0) {
				rcMHDC.setoverlimitcount(rcMHDC.getoverlimitcount().add(unit));
				rcMHDC.saveEx();
			}
		}
		//Update islock
		if (rcMHDC.getoverlimitcount() != null && rcMHDC.getoverlimitcount().compareTo(rcMHDC.getmaxlimitcount()) >= 0) {
			rcMHDC.set_ValueOfColumn(rcMHDC.COLUMNNAME_IsLocked, true);
			rcMHDC.set_ValueOfColumn(rcMHDC.COLUMNNAME_IsActive, false);
			rcMHDC.saveEx();
			}

		return super.afterSave(newRecord, success);
	}

	static BigDecimal bfsaveExSaleQty = null;
	static BigDecimal bfsaveExTTA = null;

	@Override
	protected boolean beforeSave(boolean newRecord) {
		// TODO Auto-generated method stub
		// get bfsaveExSaleQty
		String sql = "select saleqty from adempiere.HD_Sale_Transaction\n" + "where hd_sale_transaction_id = "
				+ getHD_Sale_Transaction_ID();
		BigDecimal[] bfsaveExSaleQtyTemp = null;
		bfsaveExSaleQty = DB.getSQLValueBDEx(get_TrxName(), sql, bfsaveExSaleQtyTemp);
		// get bfsaveExTTA
		String sql2 = "select totalamt from adempiere.HD_Sale_Transaction\n" + "where hd_sale_transaction_id = "
				+ getHD_Sale_Transaction_ID();
		BigDecimal[] bfsaveExTTATemp = null;
		bfsaveExTTA = DB.getSQLValueBDEx(get_TrxName(), sql2, bfsaveExTTATemp);

		return super.beforeSave(newRecord);
	}

	public static BigDecimal getBig(double d) {
		BigDecimal big = new BigDecimal(d);
		return big;
	}

	@Override
	protected boolean afterDelete(boolean success) {
		// TODO Auto-generated method stub
		// removelastdatetrx
		MHDProduct rcMHDP = new MHDProduct(getCtx(), getHD_Product_ID(), get_TrxName());
		MHDCustomer rcMHDC = new MHDCustomer(getCtx(), getHD_Customer_ID(), get_TrxName());
		Timestamp[] lastDateTemp = null;
		Timestamp lastDate;
		String sql = "select max(datetrx) from adempiere.HD_Sale_Transaction\n" + "Where HD_Customer_ID = "
				+ getHD_Customer_ID();
		lastDate = DB.getSQLValueTSEx(get_TrxName(), sql, lastDateTemp);
		rcMHDC.setlastdatetrx(lastDate);
		rcMHDC.saveEx();

		return super.afterDelete(success);
	}

	@Override
	protected boolean beforeDelete() {
		// TODO Auto-generated method stub
		// remove total amount in RCMHDCustomer
		MHDProduct rcMHDP = new MHDProduct(getCtx(), getHD_Product_ID(), get_TrxName());
		MHDCustomer rcMHDC = new MHDCustomer(getCtx(), getHD_Customer_ID(), get_TrxName());
		rcMHDC.setTotalAmt(rcMHDC.getTotalAmt().subtract(getTotalAmt()));
		rcMHDC.saveEx();
		// remove Overcount
		if (getTotalAmt().compareTo(rcMHDC.getlimitamt()) >= 0) {
			rcMHDC.setoverlimitcount(rcMHDC.getoverlimitcount().subtract(new BigDecimal(1)));
			rcMHDC.saveEx();
		}

		// remove TotalRevenueAmt in Product
		rcMHDP.settotalrevenueamt(rcMHDP.gettotalrevenueamt().subtract(getTotalAmt()));
		rcMHDP.saveEx();
		return super.beforeDelete();
	}

}
