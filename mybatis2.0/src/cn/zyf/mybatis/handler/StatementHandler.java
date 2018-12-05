package cn.zyf.mybatis.handler;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import cn.zyf.mybatis.registory.MapperRegistory;
import cn.zyf.mybatis.registory.MapperRegistory.MapperData;

public class StatementHandler {

//	@SuppressWarnings("unused")
	private ResultSetHandler resultSetHandler;
	private ResultSet resultSet;
	
	public StatementHandler() {
		resultSetHandler = new ResultSetHandler();
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public <T> T query(MapperRegistory.MapperData mapperData, String parameter) {
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		try {
			connection = getConnection();
			String sql = String.format(mapperData.getSql(), Integer.parseInt(parameter));
			preparedStatement = connection.prepareStatement(sql);
			preparedStatement.execute();
			resultSet = preparedStatement.getResultSet();
			return (T)resultSetHandler.handle(resultSet,mapperData.getType());
		} catch (Exception e) {
			e.printStackTrace();
		}finally {
			closeStatement(preparedStatement);
		}
		return null;
	}
	
	public Connection getConnection() throws SQLException {
        String driver = "com.mysql.jdbc.Driver";
        String url = "jdbc:mysql:///test?useUnicode=true&characterEncoding=UTF-8&useSSL=false";
        String username = "root";
        String password = "666666";
        Connection connection = null;
        try {
            Class.forName(driver); //classLoader,加载对应驱动
            connection = DriverManager.getConnection(url, username, password);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return connection;
    }

	private void closeStatement(PreparedStatement preparedStatement) {
		try {
			if(preparedStatement!=null) {
				preparedStatement.close();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}
