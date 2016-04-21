package org.bankalert;

import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.io.Serializable;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.kie.api.runtime.rule.AccumulateFunction;

public class UseCase2DbFunction implements AccumulateFunction {

	@Override
	public Class<?> getResultType() {
		return Transaction.class;
	}

	public static class ContextData implements Serializable {
		private static final long serialVersionUID = 1L;
		private Long transactionsCount = 0L;
		Connection conn = null;
		Statement stmt = null;
		ResultSet rs = null;
	}

	public void writeExternal(ObjectOutput out) throws IOException {
	}

	public void readExternal(ObjectInput in) throws IOException,
			ClassNotFoundException {
	}

	public Serializable createContext() {
		return new ContextData();
	}

	public void init(Serializable context) throws Exception {
		try {
			String DB_URL = App.properties.get("DB_URL");
			String DB_USERNAME = App.properties.get("DB_USERNAME");
			String DB_PASSWORD = App.properties.get("DB_PASSWORD");

//			Class.forName("com.mysql.jdbc.Driver");
//      ((ContextData) context).conn = DriverManager.getConnection(DB_URL,
//          DB_USERNAME, DB_PASSWORD);

      Class.forName("org.apache.phoenix.jdbc.PhoenixDriver");
      ((ContextData) context).conn = DriverManager.getConnection("jdbc:phoenix:sandbox.hortonworks.com:2181",
					"admin", "admin");

		} catch (SQLException se) {
			se.printStackTrace();
		}

		((ContextData) context).transactionsCount = 0L;
	}

	public void accumulate(Serializable context, Object value) {
		try {
			String USE_CASE_2_INTERVAL = App.properties
					.get("USE_CASE_2_INTERVAL");

			((ContextData) context).stmt = ((ContextData) context).conn
					.createStatement();
			Transaction transaction = (Transaction) value;

			java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat(
					"yyyy-MM-dd HH:mm:ss");

			String transactionDateTimeStr = sdf.format(transaction
					.getTxnDateTime());

			String sql;
			sql = "SELECT count(*) as rowCount from transaction where customer_id='"
					+ transaction.getCustomerID() + "' and t_date > DATE_SUB('"
					+ transactionDateTimeStr + "', INTERVAL "
					+ USE_CASE_2_INTERVAL + " )";
			((ContextData) context).rs = ((ContextData) context).stmt
					.executeQuery(sql);
		
			while (((ContextData) context).rs.next()) {
				// Retrieve by column name
				Long transactionsCount1 = ((ContextData) context).rs
						.getLong("rowCount");
				((ContextData) context).transactionsCount = transactionsCount1;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void reverse(Serializable context, Object value) throws Exception {
	}

	public Object getResult(Serializable context) throws Exception {
		return ((ContextData) context).transactionsCount;
	}

	public boolean supportsReverse() {
		return true;
	}
}
