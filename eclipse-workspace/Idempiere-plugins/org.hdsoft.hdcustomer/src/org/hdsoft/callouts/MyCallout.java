package org.hdsoft.callouts;

import java.math.BigDecimal;
import java.util.Properties;

import org.adempiere.base.IColumnCallout;
import org.compiere.model.GridField;
import org.compiere.model.GridTab;
import org.compiere.model.MTab;
import org.compiere.util.DB;
import org.hdsoft.model.MHDProduct;
import org.hdsoft.model.MHDSaleTracsaction;

public class MyCallout implements IColumnCallout{

	@Override
	public String start(Properties ctx, int WindowNo, GridTab mTab, GridField mField, Object value, Object oldValue) {
		// TODO Auto-generated method stub
		double price = Double.parseDouble(mTab.getValue(MHDSaleTracsaction.COLUMNNAME_Price).toString());
		double saleQty = Double.parseDouble(mTab.getValue(MHDSaleTracsaction.COLUMNNAME_saleqty).toString());
		double totalAmt = price * saleQty;
		BigDecimal ttAMT = new BigDecimal(totalAmt);
		mTab.setValue(MHDSaleTracsaction.COLUMNNAME_TotalAmt, ttAMT);
		return null;
	}

}
