package org.hdsoft.process;

import java.lang.reflect.Constructor;
import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.List;

import org.adempiere.util.Callback;
import org.compiere.model.MProduct;
import org.compiere.process.ProcessInfoParameter;
import org.compiere.process.SvrProcess;
import org.compiere.util.DB;
import org.hdsoft.model.MHDCustomer;

public class MyProcess extends SvrProcess {
	private String button;
	@Override
	protected void prepare() {
		// TODO Auto-generated method stub
		ProcessInfoParameter[] para = getParameter();
		for (int i = 0; i < para.length; i++) {
			String name = para[i].getParameterName();
			if (para[i].getParameter() == null)
				;
			else if (name.equalsIgnoreCase("process")) {
				button = para[i].getParameterAsString();
			} else
				log.severe("Unknown Parameter: " + name);
		}
		log.info("Process prepared with " + button);
	}

	@Override
	protected String doIt() throws Exception {
		// TODO Auto-generated method stub
		// get record from table Customer where HD_Custoer_id = getRecord_ID and set overlimit = 0
		MHDCustomer rcCustomer = new MHDCustomer(getCtx(), getRecord_ID(), get_TrxName());
		rcCustomer.setoverlimitcount(new BigDecimal(0));
		// check Overlimit
		String sql = "select totalamt from adempiere.HD_Sale_transaction\n" + 
				"where HD_Customer_ID = " + rcCustomer.getHD_Customer_ID();
		ResultSet rs = null;
		Statement st = null;
		st = DB.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,
				ResultSet.CONCUR_READ_ONLY, get_TrxName());
		rs = st.executeQuery(sql);
		while(rs.next()) {
			if (rs.getBigDecimal("totalamt").compareTo(rcCustomer.getlimitamt()) >= 0) {
				rcCustomer.setoverlimitcount(rcCustomer.getoverlimitcount().add(new BigDecimal(1)));
			}
		}
		rcCustomer.saveEx();
		return null;
	}

}
