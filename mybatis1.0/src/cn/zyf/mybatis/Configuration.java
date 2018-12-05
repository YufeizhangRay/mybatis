package cn.zyf.mybatis;

import java.lang.reflect.Proxy;
import java.util.HashMap;
import java.util.Map;

public class Configuration {

	@SuppressWarnings("unchecked")
	public <T> T getMapper(Class<T> clazz,SqlSession sqlSession) {
		return (T) Proxy.newProxyInstance(this.getClass().getClassLoader(), 
				new Class[] {clazz}, 
				new MapperProxy(sqlSession));
	}
	
	
	/**
	 * 模拟xml解析好了
	 */
	static class TestMapperXml{
		public static final String nameSpace = "cn.zyf.mybatis.EntityMapper";
		
		public static final Map<String, String> methodSqlMapping = new HashMap<>();
		
		static {
			methodSqlMapping.put("selectByPrimaryKey", "select * from mybatis_test where id = %d");
		}
	}

}
