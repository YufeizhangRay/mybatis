package cn.zyf.mybatis;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class SimpleExecutor implements Executor{

	@Override
	public <T> T query(String statement, String parameter) {
		Connection connection =null;
		PreparedStatement preparedStatement = null;
		Entity entity = null;
		try {
			connection = getConnection();
			String sql = String.format(statement, Integer.parseInt(parameter));
			preparedStatement = connection.prepareStatement(sql);
			ResultSet rs = preparedStatement.executeQuery();
			while (rs.next()) {
				entity = new Entity();
				entity.setId(rs.getInt(1));
				entity.setNums(rs.getInt(2));
				entity.setName(rs.getString(3));
            }
		} catch (SQLException e) {
			e.printStackTrace();
		}finally {
			try {
				if(connection!=null) {
					connection.close();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return (T) entity;	
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

}
