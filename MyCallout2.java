package org.hdsoft.callouts;

import java.util.Properties;

import org.adempiere.base.IColumnCallout;
import org.compiere.model.GridField;
import org.compiere.model.GridTab;
import org.hdsoft.model.MHDProduct;
import org.hdsoft.model.MHDSaleTracsaction;

public class MyCallout2 implements IColumnCallout{

	@Override
	public String start(Properties ctx, int WindowNo, GridTab mTab, GridField mField, Object value, Object oldValue) {
		// TODO Auto-generated method stub
		MHDProduct m = new MHDProduct(ctx, Integer.parseInt(mTab.getValue(MHDSaleTracsaction.COLUMNNAME_HD_Product_ID).toString()), mTab.getLinkColumnName());
		mTab.setValue(MHDSaleTracsaction.COLUMNNAME_Price, m.getcurrentprice());
		return null;
	}

}
