package cn.zyf.mybatis.handler;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.ResultSet;
import java.sql.SQLException;
import org.apache.ibatis.reflection.factory.DefaultObjectFactory;

public class ResultSetHandler {

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public <T> T handle(ResultSet rs, Class type)
			throws SQLException, NoSuchMethodException, InvocationTargetException, IllegalAccessException {
		Object resultObj = new DefaultObjectFactory().create(type);
		if (rs.next()) {
			for (Field field : resultObj.getClass().getDeclaredFields()) {
				setValue(resultObj, field, rs);
			}
		}
		closeResultSet(rs);
		return (T) resultObj;
	}

	private void setValue(Object resultObj, Field field, ResultSet rs)
			throws NoSuchMethodException, SQLException, InvocationTargetException, IllegalAccessException {
		Method setMethod = resultObj.getClass().getMethod("set" + upperCapital(field.getName()), field.getType());
		setMethod.invoke(resultObj, getResult(field, rs));
	}

	private Object getResult(Field field, ResultSet rs) throws SQLException {
		Class<?> type = field.getType();
		if (Integer.class == type) {
			return rs.getInt(field.getName());
		}
		if (String.class == type) {
			return rs.getString(field.getName());
		}
		return rs.getString(field.getName());
	}

	private String upperCapital(String name) {
		String first = name.substring(0, 1);
		String tail = name.substring(1);
		return first.toUpperCase() + tail;
	}

	private void closeResultSet(ResultSet rs) {

		try {
			if(rs!=null) {
				rs.close();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

	}

}
